package util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author pab
 * Esta clase modela la asignacion de nombres de campos a paths de arquetipos.
 * Esos nombres de campos seran usados en la generacion de GUI.
 * Esta clase mantiene el mapeo entre los nombres de campos y cada path.
 */
public class FieldNames {

    private int counter;

    private Map<String, String> map;

    private Map<String, String> inverso;

    private static FieldNames instance = null;

    private FieldNames() {
        this.map = new LinkedHashMap<String, String>();
        this.inverso = new LinkedHashMap<String, String>();
        this.counter = 1;
    }

    public static FieldNames getInstance() {
        if (instance == null) instance = new FieldNames();
        return instance;
    }

    /**
    * Obtiene el nombre de campo para esa path, si no tiene nombre asignado, genera uno nuevo.
    * @param path
    * @return
    */
    public String getField(String path) {
        if (!map.containsKey(path)) {
            String newField = "field_" + counter;
            map.put(path, newField);
            inverso.put(newField, path);
            counter++;
            return newField;
        }
        return map.get(path);
    }

    /**
    * Obtiene la path buscando por el nombre del campo.
    * @param field
    * @return
    */
    public String getPath(String field) {
        return inverso.get(field);
    }

    public Map getMapping() {
        return map;
    }

    public Map getInverseMapping() {
        return inverso;
    }
}
