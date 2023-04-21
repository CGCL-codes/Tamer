package net.sf.osadm.linedata.table.template.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import net.sf.osadm.linedata.table.template.TableConfiguration;
import net.sf.osadm.linedata.table.template.TableConfigurationManager;

public class TableConfigurationManagerImpl implements TableConfigurationManager {

    /** Administration of TableConfiguration by the identification.  */
    private Map<String, TableConfiguration> tableConfigMap = new HashMap<String, TableConfiguration>();

    public void add(TableConfiguration tableConfig) {
        if (tableConfigMap.containsKey(tableConfig.getIdentification())) {
            throw new IllegalArgumentException("Argument 'tableConfig' with identification '" + tableConfig.getIdentification() + "' can not be added, as their is already a " + TableConfiguration.class.getSimpleName() + " instance registered with that identifier.");
        }
        tableConfigMap.put(tableConfig.getIdentification(), tableConfig);
    }

    public TableConfiguration get(String identification) {
        return tableConfigMap.get(identification);
    }

    public TableConfiguration find(List<String> headerList) {
        for (Entry<String, TableConfiguration> entry : tableConfigMap.entrySet()) {
            List<String> fieldNames = entry.getValue().getFieldNames();
            if (headerList.size() == fieldNames.size()) {
                boolean isValid = true;
                for (int index = 0; isValid && index < headerList.size(); index++) {
                    isValid = headerList.get(index).equalsIgnoreCase(fieldNames.get(index));
                }
                if (isValid) {
                    return entry.getValue();
                }
            }
        }
        for (Entry<String, TableConfiguration> entry : tableConfigMap.entrySet()) {
            List<String> fieldNameList = entry.getValue().getFieldNames();
            if (headerList.size() == fieldNameList.size()) {
                Set<String> fieldNames = new TreeSet<String>();
                add(fieldNames, fieldNameList);
                Set<String> headerNames = new TreeSet<String>();
                add(headerNames, headerList);
                String[] fieldNameArray = fieldNames.toArray(new String[fieldNames.size()]);
                String[] headerNameArray = headerNames.toArray(new String[headerNames.size()]);
                boolean isValid = true;
                for (int index = 0; isValid && index < headerNameArray.length; index++) {
                    isValid = headerNameArray[index].equalsIgnoreCase(fieldNameArray[index]);
                }
                if (isValid) {
                    System.out.println("Found table configuration '" + entry.getKey() + "' through alphabetic ordered fields.");
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    private void add(Set<String> fieldSet, List<String> fieldList) {
        for (String field : fieldList) {
            fieldSet.add(field.toLowerCase());
        }
    }

    public String[] getSourceFiles() {
        String[] sourceFiles = new String[] { "**/*.tsv", "**/*.csv" };
        return sourceFiles;
    }
}
