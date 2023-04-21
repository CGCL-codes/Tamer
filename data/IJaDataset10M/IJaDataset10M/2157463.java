package edu.chop.bic.cnv.database;

import java.util.List;

/**
 * Generate a string of comma-separated question marks equal in number to a specified list.
 * User: murphy
 * Date: Apr 1, 2010
 * Time: 4:24:40 PM
 */
public class QuestionMarks {

    static String toCsvString(List list) {
        StringBuilder result = new StringBuilder(list.size() * 2 + 1);
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                result.append("?,");
            } else {
                result.append("?");
            }
        }
        return result.toString();
    }
}
