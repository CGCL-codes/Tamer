package servletTools;

import html.basic.HTMLParameters;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import baseDeDatos.GenericDB.FieldsDB;

public class ServletUtil {

    static Vector i_eliminarRepetidos(String values[]) {
        Vector valuesNoRepetidos;
        valuesNoRepetidos = new Vector();
        for (int i = 0; i < values.length; i++) {
            if (valuesNoRepetidos.contains(values[i]) == false) valuesNoRepetidos.add(values[i]);
        }
        return valuesNoRepetidos;
    }

    static String i_concatenaPorComas(String values[]) {
        String valueConcatenado;
        Vector valuesNoRepetidos;
        int numValues;
        valueConcatenado = "";
        valuesNoRepetidos = i_eliminarRepetidos(values);
        numValues = valuesNoRepetidos.size();
        for (int i = 0; i < numValues; i++) {
            String valueI;
            valueI = (String) valuesNoRepetidos.get(i);
            if (valueConcatenado.length() > 0) valueConcatenado += "," + valueI; else valueConcatenado = valueI;
        }
        return valueConcatenado;
    }

    public static HTMLParameters getCampos(HttpServletRequest entrada) {
        Enumeration parametros;
        HTMLParameters parametrosHTML;
        parametros = entrada.getParameterNames();
        parametrosHTML = new HTMLParameters();
        while (parametros.hasMoreElements() == true) {
            String value, key;
            String values[];
            key = (String) parametros.nextElement();
            values = entrada.getParameterValues(key);
            value = i_concatenaPorComas(values);
            if (parametrosHTML.exist(key) == false) parametrosHTML.setParameter(key, value); else {
                String cadena;
                cadena = parametrosHTML.getParameter(key);
                parametrosHTML.setParameter(key, cadena);
            }
        }
        return parametrosHTML;
    }

    public static FieldsDB getCamposDB(HttpServletRequest entrada) {
        Enumeration parametros;
        FieldsDB campos;
        parametros = entrada.getParameterNames();
        campos = new FieldsDB();
        while (parametros.hasMoreElements() == true) {
            String value, key;
            String values[];
            key = (String) parametros.nextElement();
            values = entrada.getParameterValues(key);
            value = i_concatenaPorComas(values);
            if (campos.existe(key) == false) campos.setCadena(key, value); else {
                String cadena;
                cadena = campos.getCadena(key);
                campos.setCadena(key, cadena);
            }
        }
        return campos;
    }
}
