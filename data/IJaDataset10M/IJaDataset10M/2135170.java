package org.antdepo.types;

import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.antdepo.Constants;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tokenizer: An abstract class that implements {@link FunctionMapperInputGenerator}
 * oriented towards tokenizing some input based on the supplied regular expression.
 * Each matching value is split into individual tokens named via their Token expressed
 * within the Regex's Token element.
 * <p/>
 *
 * @since 1.2.9
 */
public abstract class Tokenizer extends ProjectComponent implements FunctionMapperInputGenerator {

    /**
     * Execute and generate a {@link FunctionMapperInput} object
     *
     * @return A FunctionMapperInput containing the set of tokens for each matched file.
     */
    public abstract FunctionMapperInput exec();

    /**
     * Validate the input parameters. Regex element is required.
     */
    protected void validate() {
        if (null == getRegex()) {
            throw new BuildException("regex element not defined");
        } else {
            getRegex().validate();
        }
        if (null == getProject()) {
            throw new BuildException("Ant project object null or was not set");
        }
    }

    /**
     * reference to order property
     */
    private String order = Select.ASCENDING;

    /**
     * Setter to order.  How to order the results
     *
     * @param ord can be ascending or descending
     */
    public void setOrder(final SortOrder ord) {
        order = ord.getValue();
    }

    protected String getOrder() {
        return order;
    }

    private Regex regex;

    protected Regex getRegex() {
        return regex;
    }

    public void addRegex(final Regex regex) {
        this.regex = regex;
    }

    public Regex createRegex() {
        final Regex regex = new Regex();
        addRegex(regex);
        return regex;
    }

    /**
     * Class for the regex tag elment
     */
    public class Regex {

        private String pattern;

        public void setPattern(final String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }

        public List tokens = new ArrayList();

        protected List getTokens() {
            return tokens;
        }

        public void addToken(final Token token) {
            tokens.add(token);
        }

        void validate() {
            if (null == pattern) {
                throw new BuildException("pattern attribute not set");
            }
            if (tokens.size() == 0) {
                throw new BuildException("no token elements defined");
            }
            for (Iterator iter = tokens.listIterator(); iter.hasNext(); ) {
                final Token token = (Token) iter.next();
                token.validate();
            }
        }

        public Token createToken() {
            final Token token = new Token();
            tokens.add(token);
            return token;
        }
    }

    /**
     * Class for the token tag element used by the Regex class
     */
    public class Token {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        private int matchgroup;

        protected int getMatchgroup() {
            return matchgroup;
        }

        public void setMatchgroup(final int matchgroup) {
            this.matchgroup = matchgroup;
        }

        void validate() {
            if (null == name || "".equals(name)) {
                throw new BuildException("name attribute not set in token");
            }
        }
    }

    public static class SortOrder extends EnumeratedAttribute {

        public String[] getValues() {
            return new String[] { Constants.PROPERTYSORT_ASCENDING, Constants.PROPERTYSORT_DESCENDING };
        }
    }
}
