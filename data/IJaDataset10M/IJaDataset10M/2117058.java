package aamfetch.filter;

public class ConstantRule implements FilterRule {

    private boolean value;

    public ConstantRule(boolean value) {
        this.value = value;
    }

    public boolean matches(FilterItem fi) {
        return value;
    }
}
