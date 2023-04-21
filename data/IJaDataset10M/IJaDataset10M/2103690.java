package neoAtlantis.utilidades.entity.utils.converters;

import neoAtlantis.utilidades.entity.utils.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import neoAtlantis.utilidades.entity.ContainerEntities;
import neoAtlantis.utilidades.entity.SimpleEntity;

/**
 *
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class Container2Html {

    public static String parse(ContainerEntities cto) {
        return parse(cto, null, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres) {
        return parse(cto, nombres, null);
    }

    public static String parse(ContainerEntities cto, Properties nombres, Properties visibles) {
        StringBuffer sb = new StringBuffer("");
        if (cto == null || cto.size() == 0) {
            return null;
        }
        sb.append("<table border='0' class='NA_Tabla_Contenedor'>").append(System.getProperty("line.separator"));
        sb.append("  <tr>").append(System.getProperty("line.separator"));
        for (List alTmp : cto.get(0).getCache()) {
            if ((!Modifier.isPrivate(((Field) alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field) alTmp.get(0)).getModifiers())) || !UtilsEntities.esVisible((Field) alTmp.get(0), visibles)) {
                continue;
            }
            sb.append("    <td class='titulo'>").append(UtilsEntities.obtieneNombre((Field) alTmp.get(0), nombres)).append("</td>").append(System.getProperty("line.separator"));
        }
        sb.append("  </tr>").append(System.getProperty("line.separator"));
        for (int j = 0; j < cto.size(); j++) {
            sb.append("  <tr class='renglon").append(j % 2 == 0 ? "Par" : "Non").append("'>>").append(System.getProperty("line.separator"));
            for (List alTmp : cto.get(0).getCache()) {
                if ((!Modifier.isPrivate(((Field) alTmp.get(0)).getModifiers()) && !Modifier.isPublic(((Field) alTmp.get(0)).getModifiers())) || !UtilsEntities.esVisible((Field) alTmp.get(0), visibles)) {
                    continue;
                }
                if (Modifier.isPublic(((Field) alTmp.get(0)).getModifiers())) {
                    try {
                        sb.append("    <td>").append(((Field) alTmp.get(0)).get(cto.get(j))).append("</td>").append(System.getProperty("line.separator"));
                    } catch (Exception ex) {
                        throw new RuntimeException("No se logro recuperar el valor de '" + ((Field) alTmp.get(0)).getName() + "': " + ex);
                    }
                } else if (SimpleEntity.isEntity(((Field) alTmp.get(0)).getType())) {
                    try {
                        sb.append("    <td>").append("[Entity]").append("</td>").append(System.getProperty("line.separator"));
                    } catch (Exception ex) {
                        throw new RuntimeException("No se logro recuperar el valor de la entidad '" + ((Method) alTmp.get(1)).getName() + "': " + ex);
                    }
                } else {
                    try {
                        sb.append("    <td>").append(((Method) alTmp.get(1)).invoke(cto.get(j))).append("</td>").append(System.getProperty("line.separator"));
                    } catch (Exception ex) {
                        throw new RuntimeException("No se logro recuperar el valor de '" + ((Method) alTmp.get(1)).getName() + "': " + ex);
                    }
                }
            }
            sb.append("  </tr>").append(System.getProperty("line.separator"));
        }
        sb.append("</table>").append(System.getProperty("line.separator"));
        return sb.toString();
    }
}
