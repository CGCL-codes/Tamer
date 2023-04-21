package nuts.exts.oxm.adapter.filters;

import nuts.core.oxm.adapter.filters.IncludePropertyFilter;

/**
 */
public class PagerPropertyFilter extends IncludePropertyFilter {

    /**
	 * Constructor
	 */
    public PagerPropertyFilter() {
        super();
        includes.add("s");
        includes.add("l");
        includes.add("t");
    }
}
