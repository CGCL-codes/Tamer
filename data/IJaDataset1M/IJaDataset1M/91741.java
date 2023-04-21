package nu.xom.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>
 * Demonstrates building a structured XML document,
 * from flat, tabular data. A different version of this 
 * example was originally developed for Chapter 4 of 
 * <cite><a 
 * href="http://www.cafeconleche.org/books/xmljava/">Processing 
 * XML with Java</a></cite>.
 * </p>
 * 
 * @author Elliotte Rusty Harold
 * @version 1.0
 *
 */
public class BudgetData {

    public static List parse(InputStream src) throws IOException {
        InputStreamReader isr = new InputStreamReader(src, "8859_1");
        BufferedReader in = new BufferedReader(isr);
        List records = new ArrayList();
        String lineItem;
        while ((lineItem = in.readLine()) != null) {
            records.add(splitLine(lineItem));
        }
        return records;
    }

    static final String[] keys = { "AgencyCode", "AgencyName", "BureauCode", "BureauName", "AccountCode", "AccountName", "TreasuryAgencyCode", "SubfunctionCode", "SubfunctionTitle", "BEACategory", "On-Off-BudgetIndicator", "FY1976", "TransitionQuarter", "FY1977", "FY1978", "FY1979", "FY1980", "FY1981", "FY1982", "FY1983", "FY1984", "FY1985", "FY1986", "FY1987", "FY1988", "FY1989", "FY1990", "FY1991", "FY1992", "FY1993", "FY1994", "FY1995", "FY1996", "FY1997", "FY1998", "FY1999", "FY2000", "FY2001", "FY2002", "FY2003", "FY2004", "FY2005", "FY2006" };

    private static Map splitLine(String record) {
        record = record.trim();
        int index = 0;
        Map result = new HashMap();
        for (int i = 0; i < keys.length; i++) {
            StringBuffer sb = new StringBuffer();
            char c;
            boolean inString = false;
            while (true) {
                c = record.charAt(index);
                if (!inString && c == '"') inString = true; else if (inString && c == '"') inString = false; else if (!inString && c == ',') break; else sb.append(c);
                index++;
                if (index == record.length()) break;
            }
            String s = sb.toString().trim();
            result.put(keys[i], s);
            index++;
        }
        return result;
    }
}
