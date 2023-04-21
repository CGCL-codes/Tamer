package com.explosion.expfmodules.rdbmsconn.dbom.utils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.utilities.GeneralConstants;

/**
 * @author Steve.Cowx
 * This formatter has a LONG way to go.  this is not well implemented an should be done with a 
 * proper lexer.
 */
public class SQLFormatter {

    private static Logger log = LogManager.getLogger(SQLFormatter.class);

    public static String format(String SQLToFormat) throws Exception {
        String data = SQLToFormat;
        ArrayList matches = new ArrayList();
        ArrayList replacements = new ArrayList();
        matches.add("\t");
        replacements.add(GeneralConstants.LS + " ");
        matches.add("  ");
        replacements.add(GeneralConstants.LS + " ");
        matches.add(GeneralConstants.LS);
        replacements.add("");
        matches.add(",( {0,})");
        replacements.add("," + GeneralConstants.LS + "\t");
        matches.add("\\(");
        replacements.add(GeneralConstants.LS + "\\(" + GeneralConstants.LS);
        matches.add("\\)");
        replacements.add(GeneralConstants.LS + "\\)" + GeneralConstants.LS);
        matches.add("and");
        replacements.add(GeneralConstants.LS + "\tand");
        matches.add("with");
        replacements.add(GeneralConstants.LS + "with");
        matches.add("left");
        replacements.add(GeneralConstants.LS + "left");
        matches.add("right");
        replacements.add(GeneralConstants.LS + "right");
        matches.add("where");
        replacements.add(GeneralConstants.LS + "where");
        matches.add("from");
        replacements.add(GeneralConstants.LS + "from");
        matches.add(GeneralConstants.LS + "{2,}");
        replacements.add("");
        for (int i = 0; i < matches.size(); i++) {
            String toFind = (String) matches.get(i);
            String toInsert = (String) replacements.get(i);
            Pattern pattern = Pattern.compile(toFind, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(data);
            if (matcher != null) {
                data = matcher.replaceAll(toInsert);
            }
        }
        return data;
    }
}
