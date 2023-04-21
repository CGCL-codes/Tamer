package GShape.Core.Expression;

public class GExpressionItem extends GExpressionItemPart {

    private String cSeperatorParts = ":";

    private String cDistanceUnits[] = { "km", "dm", "cm", "mm", "m", "km2", "dm2", "cm2", "mm2", "m2", "km3", "dm3", "cm3", "mm3", "m3" };

    private int cDistanceDimensions[] = { 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3 };

    private float cDistanceConversion[] = { 1000, (float) 0.1, (float) 0.01, (float) 0.001, 1, 1000000, (float) 0.01, (float) 0.0001, (float) 0.000001, 1, 1000000000, (float) 0.001, (float) 0.000001, (float) 0.000000001, 1 };

    public java.util.Vector<GExpressionItemPart> Parts;

    private String expression;

    private GExpressionItem previousItem;

    private GExpression parent;

    GExpressionItem(String expression, GExpressionItem previousItem, GExpression parent) {
        super(parent);
        this.expression = expression.trim();
        this.previousItem = previousItem;
        this.parent = parent;
        Parts = new java.util.Vector<GExpressionItemPart>();
        Compile();
        for (GExpressionItemPart part : Parts) {
            if (part.isNumber && super.isNumber == false && super.isAttribute == false) super.CopyNumber(part);
            if (part.isAttribute && super.isNumber == false && super.isAttribute == false) super.CopyNumber(part);
            if (part.isString && super.isString == false) super.CopyString(part);
        }
    }

    private void Compile() {
        String tmpParts[] = expression.trim().split(cSeperatorParts);
        for (int i = 0; i < tmpParts.length; i++) {
            GExpressionItemPart newPart = new GExpressionItemPart(parent);
            Parts.add(newPart);
            if (isNumber(tmpParts[i], newPart)) {
            } else if (isAttribute(tmpParts[i], newPart)) {
            } else {
                newPart.isString = true;
                newPart.String = tmpParts[i].trim();
            }
        }
    }

    public float getOffset(float length, GExpression parent) throws Exception {
        if (previousItem != null) return previousItem.getSum(length, parent); else return 0;
    }

    public float getSum(float length, GExpression parent) throws Exception {
        if (previousItem != null) {
            return Parts.get(0).getNumber(length, 0) * Parts.get(0).getCount(length) + previousItem.getSum(length, parent);
        } else {
            return Parts.get(0).getNumber(length, 0) * Parts.get(0).getCount(length);
        }
    }

    private boolean isAttribute(String Name, GExpressionItemPart returnval) {
        if (parent.core.getAttributes().get(Name) != 0) {
            returnval.isAttribute = true;
            returnval.AttributeName = Name;
            return true;
        }
        return false;
    }

    ;

    private boolean isNumber(String val, GExpressionItemPart returnval) {
        int iUnit = -1;
        for (int i = 0; i < cDistanceUnits.length; i++) {
            if (endsWith(val, cDistanceUnits[i])) {
                iUnit = i;
                break;
            }
        }
        String strNumber = val;
        if (iUnit != -1) strNumber = strNumber.substring(0, val.length() - cDistanceUnits[iUnit].length()).trim();
        String Prefix[] = { "<=", ">=", "=", "<", ">", "" };
        for (String prefix : Prefix) {
            if (startsWith(strNumber, prefix)) {
                if (prefix.equals("<=")) returnval.isPreEqualSmaller = true;
                if (prefix.equals(">=")) returnval.isPreEqualBigger = true;
                if (prefix.equals("=")) returnval.isPreEqual = true;
                if (prefix.equals("<")) returnval.isPreSmaller = true;
                if (prefix.equals(">")) returnval.isPreBigger = true;
                if (prefix.equals("")) returnval.isPreEqual = true;
                strNumber = strNumber.trim().substring(prefix.length()).trim();
                break;
            }
        }
        java.util.regex.Pattern p1 = java.util.regex.Pattern.compile("(-?[0-9]*)");
        java.util.regex.Pattern p2 = java.util.regex.Pattern.compile("(-?[0-9]*)\\.([0-9]*)");
        java.util.regex.Pattern p10 = java.util.regex.Pattern.compile("X");
        java.util.regex.Pattern p11 = java.util.regex.Pattern.compile("~(-?[0-9]*)");
        java.util.regex.Pattern p12 = java.util.regex.Pattern.compile("~(-?[0-9]*)\\.([0-9]*)");
        java.util.regex.Pattern p21 = java.util.regex.Pattern.compile("(-?[0-9]*)%");
        java.util.regex.Pattern p22 = java.util.regex.Pattern.compile("(-?[0-9]*)\\.([0-9]*)%");
        if (p1.matcher(strNumber).matches() || p2.matcher(strNumber).matches()) returnval.isAbsolut = true;
        if (p10.matcher(strNumber).matches()) returnval.isX = true;
        if (p11.matcher(strNumber).matches() || p12.matcher(strNumber).matches()) returnval.isTilde = true;
        if (p21.matcher(strNumber).matches() || p22.matcher(strNumber).matches()) returnval.isPercentage = true;
        if (p1.matcher(strNumber).matches() || p2.matcher(strNumber).matches() || p10.matcher(strNumber).matches() || p11.matcher(strNumber).matches() || p12.matcher(strNumber).matches() || p21.matcher(strNumber).matches() || p22.matcher(strNumber).matches()) {
            returnval.isNumber = true;
            float factor = 1;
            if (iUnit != -1) factor = cDistanceConversion[iUnit];
            returnval.Number = new Float(strNumber.replace('~', ' ').replace('X', '0').replace('%', ' ').trim()).floatValue() * factor;
            if (iUnit != -1 && cDistanceDimensions[iUnit] == 2) returnval.isSquare = true;
            if (iUnit != -1 && cDistanceDimensions[iUnit] == 3) returnval.isCube = true;
            return true;
        }
        return false;
    }

    private boolean endsWith(String val, String end) {
        int i = val.length() - 1;
        for (int iEnd = end.length() - 1; iEnd >= 0; i--, iEnd--) {
            if (val.charAt(i) != end.charAt(iEnd)) return false;
        }
        return true;
    }

    private boolean startsWith(String val, String start) {
        if (val.length() > start.length()) {
            if (val.substring(0, start.length()).equals(start)) return true;
        }
        return false;
    }

    public String getString(int i) {
        if (Parts.size() > i) return Parts.get(i).String;
        return "";
    }
}
