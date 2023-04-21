package otservices.translator.language.sparql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class MapTable implements Serializable {

    private List<MapRow> rows = new ArrayList<MapRow>();

    /**
	 * 
	 */
    public void addRow(String sourceConcept, String sourceVar, String targetConcept, String targetVar) {
        MapRow row = new MapRow();
        row.setSourceConcept(sourceConcept);
        row.setSourceVar(sourceVar);
        row.setTargetConcept(targetConcept);
        row.setTargetVar(targetVar);
        this.rows.add(row);
    }

    /**
	 * 
	 */
    public Boolean contains(MapRow.FieldType searchField, String value) {
        Boolean result = new Boolean(false);
        MapRow mapRow;
        for (Iterator<MapRow> r = rows.iterator(); r.hasNext() && (!result.booleanValue()); ) {
            mapRow = r.next();
            if (searchField.equals(MapRow.FieldType.SOURCECONCEPT)) {
                if (value.equals(mapRow.getSourceConcept())) {
                    result = new Boolean(true);
                }
            } else if (searchField.equals(MapRow.FieldType.SOURCEVAR)) {
                if (value.equals(mapRow.getSourceVar())) {
                    result = new Boolean(true);
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETCONCEPT)) {
                if (value.equals(mapRow.getTargetConcept())) {
                    result = new Boolean(true);
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETVAR)) {
                if (value.equals(mapRow.getTargetVar())) {
                    result = new Boolean(true);
                }
            }
        }
        return result;
    }

    /**
	 * 
	 */
    public String[] get(MapRow.FieldType searchField, String value, MapRow.FieldType returnField) {
        String result[] = null;
        MapRow mapRow = null;
        List<String> listResult = new ArrayList<String>();
        for (Iterator<MapRow> r = rows.iterator(); r.hasNext(); ) {
            mapRow = r.next();
            if (searchField.equals(MapRow.FieldType.SOURCECONCEPT)) {
                if (value.equals(mapRow.getSourceConcept())) {
                    listResult.add(mapRow.get(returnField));
                }
            } else if (searchField.equals(MapRow.FieldType.SOURCEVAR)) {
                if (value.equals(mapRow.getSourceVar())) {
                    listResult.add(mapRow.get(returnField));
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETCONCEPT)) {
                if (value.equals(mapRow.getTargetConcept())) {
                    listResult.add(mapRow.get(returnField));
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETVAR)) {
                if (value.equals(mapRow.getTargetVar())) {
                    listResult.add(mapRow.get(returnField));
                }
            }
        }
        Object[] o = listResult.toArray();
        result = new String[o.length];
        for (int i = 0; i < o.length; i++) {
            result[i] = (String) o[i];
        }
        return result;
    }

    public Integer[] getIndex(MapRow.FieldType searchField, String value) {
        Integer[] result = null;
        MapRow mapRow = null;
        List<Integer> listResult = new ArrayList<Integer>();
        Integer index = new Integer(0);
        for (Iterator<MapRow> r = rows.iterator(); r.hasNext(); ) {
            mapRow = r.next();
            if (searchField.equals(MapRow.FieldType.SOURCECONCEPT)) {
                if (value.equals(mapRow.getSourceConcept())) {
                    listResult.add(index);
                }
            } else if (searchField.equals(MapRow.FieldType.SOURCEVAR)) {
                if (value.equals(mapRow.getSourceVar())) {
                    listResult.add(index);
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETCONCEPT)) {
                if (value.equals(mapRow.getTargetConcept())) {
                    listResult.add(index);
                }
            } else if (searchField.equals(MapRow.FieldType.TARGETVAR)) {
                if (value.equals(mapRow.getTargetVar())) {
                    listResult.add(index);
                }
            }
            index++;
        }
        Object[] o = listResult.toArray();
        result = new Integer[o.length];
        for (int i = 0; i < o.length; i++) {
            result[i] = (Integer) o[i];
        }
        return result;
    }

    /**
	 * 
	 */
    public Map<String, String[]> getSourceToTarget(MapRow.FieldType sourceField, MapRow.FieldType targetField) {
        Map<String, String[]> result = new Hashtable<String, String[]>();
        String source;
        List<String> target;
        MapRow row;
        MapRow r;
        for (Iterator<MapRow> i = this.rows.iterator(); i.hasNext(); ) {
            row = i.next();
            source = row.get(sourceField);
            target = new ArrayList<String>();
            for (Iterator<MapRow> j = this.rows.iterator(); j.hasNext(); ) {
                r = j.next();
                if (source.equals(r.get(sourceField))) {
                    target.add(r.get(targetField));
                }
            }
            Object[] o = target.toArray();
            String[] strTarget = new String[o.length];
            for (int x = 0; x < o.length; x++) {
                strTarget[x] = (String) o[x];
            }
            result.put(source, strTarget);
        }
        return result;
    }

    /**
	 * 
	 */
    public Iterator<String> iterator(MapRow.FieldType field) {
        List<String> list = new ArrayList<String>();
        MapRow row;
        for (Iterator<MapRow> l = this.rows.iterator(); l.hasNext(); ) {
            row = l.next();
            list.add(row.get(field));
        }
        return list.iterator();
    }

    /**
	 * 
	 */
    public void print() {
        MapRow row;
        for (Iterator<MapRow> l = this.rows.iterator(); l.hasNext(); ) {
            row = l.next();
            System.out.println(row.getSourceConcept() + " " + row.getSourceVar() + " " + row.getTargetConcept() + " " + row.getTargetVar());
        }
    }
}
