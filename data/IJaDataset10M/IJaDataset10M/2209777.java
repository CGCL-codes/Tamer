package net.sf.osadm.docbook;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 * @author tverhagen
 */
public class WildCardFileFilter extends RegExpFileFilter {

    public WildCardFileFilter(String baseDirStr, String wildCardExp) {
        super(baseDirStr, Pattern.compile(convertWildCard2RegularExpression(wildCardExp)));
    }

    public WildCardFileFilter(String baseDirStr, List<String> wildCardExpList) {
        super(baseDirStr, Pattern.compile(convertWildCard2RegularExpression(wildCardExpList)));
    }

    /**
	 * Converts the input <code>List&lt;String&gt;</code> into a regular expression.
	 *  
	 * @param wildCardExpList  The paths including wild card characters.
	 * @return The input represented as regular expression.
	 */
    public static String convertWildCard2RegularExpression(List<String> wildCardExpList) {
        StringBuilder builder = new StringBuilder();
        for (String wildCardExpStr : wildCardExpList) {
            if (builder.length() > 0) {
                builder.append("|");
            }
            builder.append(WildCardFileFilter.convertWildCard2RegularExpression(wildCardExpStr));
        }
        return builder.toString();
    }

    /**
	 * Converts the input String into a regular expression.
	 *  
	 * @param wildCardExpStr  The path including wild card characters.
	 * @return The input represented as regular expression.
	 */
    public static String convertWildCard2RegularExpression(String wildCardExpStr) {
        String regExpFilter = wildCardExpStr.replace(".", "\\.");
        if (regExpFilter.contains("/")) {
            final String[] wildCharArray = regExpFilter.split("/");
            String[] regExpArray = new String[wildCharArray.length];
            int index = 0;
            for (; index < wildCharArray.length - 1; index++) {
                if (wildCharArray[index].equals("**")) {
                    regExpArray[index] = wildCharArray[index].replace("**", ".*");
                } else if (wildCharArray[index].indexOf("*") > -1) {
                    regExpArray[index] = wildCharArray[index].replace("*", "[^/]*");
                } else {
                    regExpArray[index] = wildCharArray[index];
                }
                System.out.println("  [" + index + "] was  " + wildCharArray[index] + "  " + regExpArray[index]);
            }
            if (wildCharArray[index].indexOf("*") > -1) {
                regExpArray[index] = wildCharArray[index].replace("*", "[^/]*");
            } else {
                regExpArray[index] = wildCharArray[index];
            }
            System.out.println("  [" + index + "] was  " + wildCharArray[index] + "  " + regExpArray[index]);
            StringBuilder builder = new StringBuilder();
            for (int index2 = 0; index2 < regExpArray.length; index2++) {
                if (builder.length() > 0) {
                    builder.append("/");
                }
                builder.append(regExpArray[index2]);
            }
            if (wildCharArray[wildCharArray.length - 2].equals("**")) {
                StringBuilder builder2 = new StringBuilder();
                for (int index2 = 0; index2 < regExpArray.length - 2; index2++) {
                    if (builder2.length() > 0) {
                        builder2.append("/");
                    }
                    builder2.append(regExpArray[index2]);
                }
                if (builder2.length() > 0) {
                    builder2.append("/");
                }
                builder2.append(regExpArray[regExpArray.length - 1]);
                builder.append("|").append(builder2);
            }
            regExpFilter = builder.toString();
        } else {
            regExpFilter = regExpFilter.replace("*", "[^/]*");
        }
        System.out.println("filter  [" + wildCardExpStr + "]  reg exp  [" + regExpFilter + "]");
        return regExpFilter;
    }
}
