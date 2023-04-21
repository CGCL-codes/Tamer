package alt.djudge.frontend.client.ui.grids;

interface Filter {

    boolean matches(String value);

    String getText();
}

class PrefixFilter implements Filter {

    final String prefix;

    public PrefixFilter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean matches(String value) {
        return prefix == null || value.startsWith(prefix);
    }

    @Override
    public String getText() {
        return prefix + "*";
    }
}

class SuffixFilter implements Filter {

    final String suffix;

    public SuffixFilter(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean matches(String value) {
        return suffix == null || value.endsWith(suffix);
    }

    @Override
    public String getText() {
        return "*" + suffix;
    }
}

class JavaRegexpFilter implements Filter {

    final String regexp;

    public JavaRegexpFilter(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean matches(String value) {
        return value.matches(regexp);
    }

    @Override
    public String getText() {
        return regexp;
    }
}

class JavascriptRegexpFilter implements Filter {

    final String regexp;

    private static native boolean matches(String str, String regexp);

    public JavascriptRegexpFilter(String regexp) {
        this.regexp = regexp;
    }

    @Override
    public boolean matches(String value) {
        return matches(value, regexp);
    }

    @Override
    public String getText() {
        return regexp;
    }
}

class IntegerFilterCompare implements Filter {

    private enum OP {

        EQ, NE, LT, GT, LTE, GTE
    }

    ;

    final Long valueToCompare;

    final OP operation;

    final String operationString;

    public IntegerFilterCompare(String valueString, String operationString) {
        this.operationString = operationString;
        valueToCompare = Long.parseLong(valueString);
        if ("<".equals(operationString)) operation = OP.LT; else if (">".equals(operationString)) operation = OP.GT; else if ("=".equals(operationString)) operation = OP.EQ; else if ("!=".equals(operationString)) operation = OP.NE; else if ("<=".equals(operationString)) operation = OP.LTE; else if (">=".equals(operationString)) operation = OP.GTE; else operation = OP.NE;
    }

    @Override
    public boolean matches(String valueString) {
        Long value = Long.parseLong(valueString);
        switch(operation) {
            case GT:
                return value > valueToCompare;
            case LT:
                return value < valueToCompare;
            case NE:
                return value != valueToCompare;
            case GTE:
                return value >= valueToCompare;
            case LTE:
                return value <= valueToCompare;
            default:
                return value == valueToCompare;
        }
    }

    @Override
    public String getText() {
        return operationString + " " + valueToCompare;
    }
}

class IntegerFilterRange implements Filter {

    final Long leftBound;

    final Long rightBound;

    public IntegerFilterRange(String leftBoundStr, String rightBoundStr) {
        leftBound = Long.parseLong(leftBoundStr);
        rightBound = Long.parseLong(rightBoundStr);
    }

    @Override
    public boolean matches(String valueString) {
        Long value = Long.parseLong(valueString);
        return value.compareTo(leftBound) >= 0 && value.compareTo(rightBound) <= 0;
    }

    @Override
    public String getText() {
        return "[" + leftBound + ", " + rightBound + "]";
    }
}
