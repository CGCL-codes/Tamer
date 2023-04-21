package radeox.filter.regex;

import radeox.filter.context.FilterContext;
import radeox.regex.MatchResult;
import radeox.regex.Matcher;
import radeox.regex.Pattern;
import radeox.regex.Substitution;

/**
 * Filter that calls a special handler method handleMatch() for every occurance
 * of a regular expression.
 * 
 * @author stephan
 * @team sonicteam
 * @version $Id: RegexTokenFilter.java,v 1.11 2004/04/16 07:47:41 stephan Exp $
 */
public abstract class RegexTokenFilter extends RegexFilter {

    public RegexTokenFilter() {
        super();
    }

    /**
	 * create a new regular expression and set
	 */
    public RegexTokenFilter(String regex, boolean multiline) {
        super(regex, "", multiline);
    }

    /**
	 * create a new regular expression and set
	 */
    public RegexTokenFilter(String regex) {
        super(regex, "");
    }

    protected void setUp(FilterContext context) {
    }

    /**
	 * Method is called for every occurance of a regular expression. Subclasses
	 * have to implement this mehtod.
	 * 
	 * @param buffer
	 *            Buffer to write replacement string to
	 * @param result
	 *            Hit with the found regualr expression
	 * @param context
	 *            FilterContext for filters
	 */
    public abstract void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context);

    @Override
    public String filter(String input, final FilterContext context) {
        setUp(context);
        String result = null;
        int size = pattern.size();
        for (int i = 0; i < size; i++) {
            Pattern p = pattern.get(i);
            Matcher m = Matcher.create(input, p);
            result = m.substitute(new Substitution() {

                public void handleMatch(StringBuffer buffer, MatchResult result) {
                    RegexTokenFilter.this.handleMatch(buffer, result, context);
                }
            });
            input = result;
        }
        return input;
    }
}
