package filter;

import encoder.TermsEncoder;

public abstract class DataFilter {

    public DataFilter() {
    }

    public abstract boolean eval(String str);
}
