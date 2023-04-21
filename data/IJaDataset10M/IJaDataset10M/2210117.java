package test.sitemap;

import java.util.regex.Pattern;

/**
 * @author jli
 *
 */
public class RegExFilter implements PageFilter {

    private static final long serialVersionUID = 1L;

    private boolean include = false;

    private boolean expandable = false;

    private Pattern pattern = null;

    protected RegExFilter() {
    }

    ;

    public RegExFilter(String filter) {
        this(filter.charAt(0) != '!', filter.charAt(1) == 'E', filter.substring(2));
    }

    public RegExFilter(boolean include, boolean expandable, String regEx) {
        pattern = Pattern.compile(regEx);
        this.include = include;
        this.expandable = expandable;
    }

    @Override
    public boolean isExpandable(String url) {
        if (pattern != null && pattern.matcher(url).matches()) {
            return expandable;
        }
        return false;
    }

    @Override
    public boolean isPageIgnored(String url) {
        if (pattern != null && pattern.matcher(url).matches()) {
            return !include;
        }
        return false;
    }

    @Override
    public boolean isUnknown(String url) {
        return pattern == null || !pattern.matcher(url).matches();
    }
}
