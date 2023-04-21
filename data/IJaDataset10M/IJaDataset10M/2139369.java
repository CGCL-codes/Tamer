package netgest.bo.xwc.components.connectors;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import netgest.bo.xwc.components.connectors.SortTerms.SortTerm;

public class OrderByTerms {

    public enum OrderByDir {

        SORT_ASC, SORT_DESC, SORT_DEFAULT
    }

    LinkedHashMap<String, OrderByTerm> orderByTerms = new LinkedHashMap<String, OrderByTerm>(1);

    private static final String patterns = "(order|group)[\\s+]by|\\/\\*\\+|\\*\\/|\\/\\*|\\{\\{|\\}\\}|[\\.\\,\\(\\)\\[\\]=<>!\\+\\-\\*/'\\n\\t\\?]|[a-zA-Z_$0-9]++|\\w++|\\W";

    private static final Pattern tokenizerRegExp = Pattern.compile(patterns, Pattern.CASE_INSENSITIVE);

    public OrderByTerms(String orderByPart) {
        parseCommaSeparated(new StringBuffer(orderByPart));
    }

    public OrderByTerm getSortTerm(String name) {
        return this.orderByTerms.get(name);
    }

    public Collection<OrderByTerm> sortTerms() {
        return this.orderByTerms.values();
    }

    private void parseCommaSeparated(StringBuffer part) {
        String g;
        StringBuffer fieldExpression = new StringBuffer();
        OrderByDir fieldOrder = null;
        Matcher m = tokenizerRegExp.matcher(part);
        while (m.find()) {
            g = skipQuotedOrComment(part, m, getGroupFromOriginal(part, m)).toString();
            if ("(".equals(g)) {
                fieldExpression.append("(");
                fieldExpression.append(skipPair(part, m, "(", ")"));
                fieldExpression.append(")");
            } else if ("[".equals(g)) {
                fieldExpression.append("[");
                fieldExpression.append(skipPair(part, m, "[", "]"));
                fieldExpression.append("]");
            } else if ("DESC".equals(g)) {
                fieldOrder = OrderByDir.SORT_DESC;
            } else if ("ASC".equals(g)) {
                fieldOrder = OrderByDir.SORT_DESC;
            } else if (",".equals(g)) {
                String finalExpr = fieldExpression.toString().trim();
                if (finalExpr.length() > 0) {
                    OrderByTerm x = new OrderByTerm(finalExpr, fieldOrder != null ? fieldOrder : OrderByDir.SORT_DEFAULT);
                    orderByTerms.put(x.getName(), x);
                    fieldExpression = new StringBuffer();
                    fieldOrder = null;
                }
            } else {
                fieldExpression.append(g);
            }
        }
        String finalExpr = fieldExpression.toString().trim();
        if (finalExpr.trim().length() > 0) {
            OrderByTerm x = new OrderByTerm(finalExpr, fieldOrder != null ? fieldOrder : OrderByDir.SORT_DEFAULT);
            orderByTerms.put(x.getName(), x);
            fieldOrder = null;
        }
    }

    private String getGroupFromOriginal(StringBuffer original, Matcher m) {
        return original.substring(m.start(), m.end());
    }

    private StringBuffer skipPair(StringBuffer original, Matcher m, String beginSeq, String endSeq) {
        String g;
        int deep = 1;
        StringBuffer ret = new StringBuffer();
        while (m.find()) {
            g = skipQuotedOrComment(original, m, getGroupFromOriginal(original, m)).toString();
            if ("?".equals(g)) {
                throw new RuntimeException("OrderByTerms parser doesn't support Query parameters [?].");
            } else if (g.equals(endSeq)) {
                deep--;
                if (deep == 0) {
                    break;
                }
            } else if (beginSeq != null && beginSeq.equals(g)) {
                deep++;
            }
            ret.append(skipQuotedOrComment(original, m, g));
        }
        return ret;
    }

    private StringBuffer skipQuotedOrComment(StringBuffer original, Matcher m, String current) {
        String g;
        String endChar;
        StringBuffer ret = new StringBuffer(current);
        if ("'".equals(current) || "\"".equals(current) || "/*".equals(current)) {
            if ("/*".equals(current)) endChar = "*/"; else endChar = current;
            while (m.find()) {
                g = getGroupFromOriginal(original, m);
                ret.append(g);
                if (endChar.equals(g)) {
                    break;
                }
            }
        }
        return ret;
    }

    public static class OrderByTerm {

        private OrderByDir dir;

        private String expression;

        private String name;

        private OrderByTerm(String expression, OrderByDir dir) {
            this.expression = expression;
            this.dir = dir;
            if (expression.startsWith("\"") && expression.endsWith("\"")) {
                this.name = expression.substring(1, expression.length() - 1);
            } else {
                this.name = expression;
            }
        }

        public String getExpression() {
            return this.expression;
        }

        public OrderByDir getOrderByDir() {
            return this.dir;
        }

        public String getName() {
            return this.name;
        }
    }
}
