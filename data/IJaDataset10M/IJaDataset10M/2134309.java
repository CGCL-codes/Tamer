package util;

import java.util.Collection;
import java.util.Iterator;
import modelo.repositorio.RepositorioMDR;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.StereotypeClass;

/**
 *
 * @author Juan Timoteo Ponce Ortiz
 */
public class EstereotipoUtil {

    public static String aCadena(Collection stereotypes) {
        Iterator i = stereotypes.iterator();
        String ret = "";
        while (i.hasNext()) {
            Stereotype s = (Stereotype) i.next();
            ret += s.getName();
            if (i.hasNext()) ret += ",";
        }
        return ret;
    }

    public static boolean tieneEstereotipo(ModelElement me, String stereo) {
        Iterator i = me.getStereotype().iterator();
        while (i.hasNext()) {
            Stereotype s = (Stereotype) i.next();
            if (s.getName().equals(stereo)) return true;
        }
        return false;
    }
}
