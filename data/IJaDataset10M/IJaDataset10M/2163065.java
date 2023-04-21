package org.vikamine.gui.databaseIO;

import java.util.ArrayList;
import java.util.Iterator;
import org.vikamine.app.DMManager;
import org.vikamine.gui.subgroup.AllSubgroupPluginController;
import org.vikamine.kernel.data.DataRecord;
import org.vikamine.kernel.data.EvaluatableAttribute;
import org.vikamine.kernel.data.Population;
import org.vikamine.kernel.data.creators.DBConnector;
import org.vikamine.kernel.subgroup.SGDescription;

public class ExportToDatabase {

    public static void saveToDatabase(DBConnector db, String str, ArrayList list) {
        Population exportPOP = DMManager.getInstance().getOntology().getPopulation();
        Iterator<DataRecord> it = exportPOP.iterator();
        Iterator<EvaluatableAttribute> itatt = exportPOP.dataset().attributeIterator();
        ArrayList<EvaluatableAttribute> listattribute = new ArrayList<EvaluatableAttribute>();
        ArrayList<String> spaltename = new ArrayList<String>();
        ArrayList<String> spaltetype = new ArrayList<String>();
        while (itatt.hasNext()) {
            EvaluatableAttribute tatt = itatt.next();
            if (list.contains(tatt.getDescription())) {
                listattribute.add(tatt);
                spaltename.add((tatt.getDescription()));
                if (tatt.isNumeric()) {
                    spaltetype.add("float (20)");
                } else spaltetype.add("varchar(50)");
            }
        }
        db.createTables(str, spaltename, spaltetype);
        SGDescription sgDescr = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup().getSGDescription();
        while (it.hasNext()) {
            int count = 0;
            int countAttribute = 0;
            DataRecord temp = it.next();
            if (ExportAttributeDialog.subgroup) {
                if (!sgDescr.isMatching(temp)) {
                    continue;
                }
            }
            String query = "";
            while (count < temp.getNumAttributes() - 1) {
                if (!list.get(count).equals("")) {
                    EvaluatableAttribute tempatt = listattribute.get(countAttribute++);
                    if (!temp.isMissing(tempatt)) {
                        if (tempatt.isNumeric()) {
                            query = query + temp.getValue(tempatt) + ",";
                        } else {
                            try {
                                String tempString = temp.getStringValue(tempatt);
                                if (tempString.contains("'")) {
                                    int index = tempString.indexOf("'");
                                    tempString = tempString.substring(0, index) + "'" + tempString.substring(index);
                                }
                                query = query + "'" + tempString + "',";
                            } catch (Exception e) {
                                System.out.println(tempatt.getDescription());
                            }
                        }
                    } else {
                        query = query + null + ",";
                    }
                }
                count++;
            }
            query = query.substring(0, query.length() - 1);
            db.insertValue(str, query);
        }
    }

    public static void saveToDatabase(DBConnector db, String str, ArrayList list, ArrayList<ArrayList<String>> userList) {
        Population exportPOP = DMManager.getInstance().getOntology().getPopulation();
        Iterator<DataRecord> it = exportPOP.iterator();
        Iterator<EvaluatableAttribute> itatt = exportPOP.dataset().attributeIterator();
        ArrayList<EvaluatableAttribute> listattribute = new ArrayList<EvaluatableAttribute>();
        ArrayList<String> spaltename = new ArrayList<String>();
        ArrayList<String> spaltetype = new ArrayList<String>();
        while (itatt.hasNext()) {
            EvaluatableAttribute tatt = itatt.next();
            if (list.contains(tatt.getDescription())) {
                listattribute.add(tatt);
                spaltename.add((tatt.getDescription()));
                if (tatt.isNumeric()) {
                    spaltetype.add("float (20)");
                } else spaltetype.add("varchar(50)");
            }
        }
        for (int i = 0; i < userList.size(); i++) {
            spaltename.add(userList.get(i).get(0));
            spaltetype.add("varchar(50)");
        }
        String s = spaltetype.get(0);
        s = s + " not null";
        db.createTables(str, spaltename, spaltetype);
        int indexUser = 1;
        SGDescription sgDescr = AllSubgroupPluginController.getInstance().getSubgroupTreeController().getModel().getSubgroup().getSGDescription();
        while (it.hasNext()) {
            int count = 0;
            int countAttribute = 0;
            DataRecord temp = it.next();
            if (ExportAttributeDialog.subgroup) {
                if (!sgDescr.isMatching(temp)) {
                    indexUser++;
                    continue;
                }
            }
            String query = "";
            while (count < temp.getNumAttributes() - 1) {
                if (!list.get(count).equals("")) {
                    EvaluatableAttribute tempatt = listattribute.get(countAttribute++);
                    if (!temp.isMissing(tempatt)) {
                        if (tempatt.isNumeric()) {
                            query = query + temp.getValue(tempatt) + ",";
                        } else {
                            try {
                                String tempString = temp.getStringValue(tempatt);
                                if (tempString.contains("'")) {
                                    int index = tempString.indexOf("'");
                                    tempString = tempString.substring(0, index) + "'" + tempString.substring(index);
                                }
                                query = query + "'" + tempString + "',";
                            } catch (Exception e) {
                                System.out.println(tempatt.getDescription());
                            }
                        }
                    } else {
                        query = query + null + ",";
                    }
                }
                count++;
            }
            for (int i = 0; i < userList.size(); i++) {
                query = query + "'" + userList.get(i).get(indexUser) + "'" + ",";
            }
            indexUser++;
            query = query.substring(0, query.length() - 1);
            db.insertValue(str, query);
        }
    }
}
