package com.kitten.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.table.TableModel;
import com.kitten.dao.KittenDao;

public class KittenCountMatchesModel {

    private int occurances = 0;

    private Pattern pat;

    private Matcher mat;

    private KittenDao kittenDao;

    public KittenCountMatchesModel(KittenDao kittenDao) {
        this.kittenDao = kittenDao;
    }

    public int count(TableModel t, String valueField, boolean caseSensitive, boolean matchr) {
        for (int i = 0; i < t.getRowCount(); i++) {
            for (int j = 1; j < t.getColumnCount(); j++) {
                String compareText = null;
                if (t.getValueAt(i, j) == null) {
                    compareText = "";
                } else {
                    compareText = t.getValueAt(i, j).toString();
                }
                if (caseSensitive) {
                    if (matchr) {
                        String compare = compareText;
                        if (compare.equals(valueField)) {
                            occurances++;
                        }
                    } else {
                        pat = Pattern.compile(valueField);
                        mat = pat.matcher(compareText);
                        if (mat.find()) {
                            occurances++;
                        }
                    }
                } else {
                    if (matchr) {
                        String compare = compareText;
                        if (compare.equalsIgnoreCase(valueField)) {
                            occurances++;
                        }
                    } else {
                        pat = Pattern.compile(valueField, Pattern.CASE_INSENSITIVE);
                        mat = pat.matcher(compareText);
                        if (mat.find()) {
                            occurances++;
                        }
                    }
                }
            }
        }
        return occurances;
    }
}
