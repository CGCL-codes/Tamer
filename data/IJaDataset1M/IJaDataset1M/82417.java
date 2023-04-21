package org.datanucleus.query;

import java.util.StringTokenizer;
import org.datanucleus.ObjectManagerFactoryImpl;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.ExecutionContext;
import org.datanucleus.store.query.Query;
import org.datanucleus.util.ClassUtils;
import org.datanucleus.util.Localiser;
import org.datanucleus.util.NucleusLogger;

/**
 * Parser for handling (simple) SQL string-based queries.
 * Takes a SQLQuery and the query string and parses it into its constituent parts, updating
 * the SQLQuery accordingly with the result that after calling the parse() method the SQLQuery
 * is populated.
 * <pre>
 * SELECT [ {result} ]
 *        [FROM {candidate-classes} ]
 *        [WHERE {filter}]
 *        [GROUP BY {grouping-clause} ]
 *        [HAVING {having-clause} ]
 *        [ORDER BY {ordering-clause}]
 * e.g SELECT t.COL1 FROM MYTABLE t WHERE t.COL2 = 1
 * </pre>
 * <p>
 * Note that {filter} and {having-clause} can contain subqueries, hence containing keywords
 * <pre>
 * SELECT c FROM Customer c WHERE NOT EXISTS (SELECT o1 FROM c.orders o1)
 * </pre>
 * So the "filter" for the outer query is "NOT EXISTS (SELECT o1 FROM c.orders o1)"
 */
public class SQLSingleStringParser {

    /** Localiser for messages. */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.Localisation", ObjectManagerFactoryImpl.class.getClassLoader());

    /** The SQL query to populate. */
    private Query query;

    /** The single-string query string. */
    private String queryString;

    /** Record of the keyword currently being processed, so we can check for out of order keywords. */
    int keywordPosition = -1;

    /**
     * Constructor for the Single-String parser.
     * @param query The query into which we populate the components of the query
     * @param queryString The Single-String query
     */
    public SQLSingleStringParser(Query query, String queryString) {
        if (NucleusLogger.QUERY.isDebugEnabled()) {
            NucleusLogger.QUERY.debug(LOCALISER.msg("043000", queryString));
        }
        this.query = query;
        this.queryString = queryString;
    }

    /**
     * Method to parse the Single-String query
     */
    public void parse() {
        new Compiler(new Parser(queryString)).compile();
    }

    /**
     * Compiler to process keywords contents. In the query the keywords often have
     * content values following them that represent the constituent parts of the query. 
     * This takes the keyword and sets the constituent part accordingly.
     */
    private class Compiler {

        Parser tokenizer;

        Compiler(Parser tokenizer) {
            this.tokenizer = tokenizer;
        }

        private void compile() {
            compileQuery();
            String keyword = tokenizer.parseKeyword();
            if (keyword != null) {
                if (JPQLQueryHelper.isKeyword(keyword)) {
                    throw new NucleusUserException(LOCALISER.msg("043001", keyword));
                } else {
                }
            }
        }

        private void compileQuery() {
            boolean update = false;
            boolean delete = false;
            if (tokenizer.parseKeywordIgnoreCase("SELECT")) {
            } else if (tokenizer.parseKeywordIgnoreCase("UPDATE")) {
                update = true;
                query.setType(Query.BULK_UPDATE);
            } else if (tokenizer.parseKeywordIgnoreCase("DELETE")) {
                delete = true;
                query.setType(Query.BULK_DELETE);
            } else {
                throw new NucleusUserException(LOCALISER.msg("043002"));
            }
            if (update) {
                compileUpdate();
            } else if (!delete) {
                compileResult();
            }
            if (tokenizer.parseKeywordIgnoreCase("FROM")) {
                compileFrom();
            }
            if (tokenizer.parseKeywordIgnoreCase("WHERE")) {
                compileWhere();
            }
            if (tokenizer.parseKeywordIgnoreCase("GROUP BY")) {
                if (update || delete) {
                    throw new NucleusUserException(LOCALISER.msg("043007"));
                }
                compileGroup();
            }
            if (tokenizer.parseKeywordIgnoreCase("HAVING")) {
                if (update || delete) {
                    throw new NucleusUserException(LOCALISER.msg("043008"));
                }
                compileHaving();
            }
            if (tokenizer.parseKeywordIgnoreCase("ORDER BY")) {
                if (update || delete) {
                    throw new NucleusUserException(LOCALISER.msg("043009"));
                }
                compileOrder();
            }
        }

        private void compileResult() {
            String content = tokenizer.parseContent(null, false);
            if (content.length() > 0) {
                query.setResult(content);
            }
        }

        private void compileUpdate() {
            String content = tokenizer.parseContent(null, false);
            if (content.length() == 0) {
                throw new NucleusUserException(LOCALISER.msg("043010"));
            }
            String contentUpper = content.toUpperCase();
            int setIndex = contentUpper.indexOf("SET");
            if (setIndex < 0) {
                throw new NucleusUserException(LOCALISER.msg("043011"));
            }
            query.setFrom(content.substring(0, setIndex).trim());
            query.setUpdate(content.substring(setIndex + 3).trim());
        }

        private void compileFrom() {
            String content = tokenizer.parseContent(null, false);
            if (content.length() > 0) {
                query.setFrom(content);
            }
        }

        private void compileWhere() {
            String content = tokenizer.parseContent("FROM", true);
            if (content.length() == 0) {
                throw new NucleusUserException(LOCALISER.msg("043004", "WHERE", "<filter>"));
            }
            String contentUpper = content.toUpperCase();
            if (contentUpper.indexOf("SELECT") > 0) {
                StringBuffer contentStr = new StringBuffer(content);
                boolean parsed = false;
                int currentPosition = 0;
                int subqueryNum = 1;
                while (!parsed) {
                    contentUpper = contentStr.toString().toUpperCase();
                    int selectPos = contentUpper.indexOf("SELECT", currentPosition);
                    if (selectPos < 0) {
                        parsed = true;
                        break;
                    } else {
                        currentPosition = selectPos;
                        int startPosition = currentPosition;
                        for (int i = currentPosition - 1; i >= 0; i--) {
                            if (contentStr.charAt(i) == '(') {
                                startPosition = i;
                                break;
                            }
                        }
                        int level = 0;
                        for (int i = currentPosition; i < content.length(); i++) {
                            if (contentStr.charAt(i) == '(') {
                                level++;
                            } else if (contentStr.charAt(i) == ')') {
                                level--;
                            }
                            if (level == -1) {
                                String subqueryString = contentStr.substring(currentPosition, i);
                                String subqueryVarName = "DATANUCLEUS_SUBQUERY_" + subqueryNum;
                                Query subquery = (Query) ClassUtils.newInstance(query.getClass(), new Class[] { ExecutionContext.class, String.class }, new Object[] { query.getObjectManager(), subqueryString });
                                query.addSubquery(subquery, "double " + subqueryVarName, null, null);
                                contentStr.replace(startPosition, i + 1, " " + subqueryVarName + " ");
                                subqueryNum++;
                                break;
                            }
                        }
                    }
                }
                query.setFilter(contentStr.toString());
            } else {
                query.setFilter(content);
            }
        }

        private void compileGroup() {
            String content = tokenizer.parseContent(null, false);
            if (content.length() == 0) {
                throw new NucleusUserException(LOCALISER.msg("043004", "GROUP BY", "<grouping>"));
            }
            query.setGrouping(content);
        }

        private void compileHaving() {
            String content = tokenizer.parseContent("FROM", true);
            if (content.length() == 0) {
                throw new NucleusUserException(LOCALISER.msg("043004", "HAVING", "<having>"));
            }
            query.setHaving(content);
        }

        private void compileOrder() {
            String content = tokenizer.parseContent(null, false);
            if (content.length() == 0) {
                throw new NucleusUserException(LOCALISER.msg("043004", "ORDER BY", "<ordering>"));
            }
            query.setOrdering(content);
        }
    }

    /**
     * Tokenizer that provides access to current token.
     */
    private class Parser {

        final String queryString;

        int queryStringPos = 0;

        /** tokens */
        final String[] tokens;

        /** keywords */
        final String[] keywords;

        /** current token cursor position */
        int tokenIndex = -1;

        /**
         * Constructor
         * @param str Query string
         */
        public Parser(String str) {
            queryString = str;
            StringTokenizer tokenizer = new StringTokenizer(str);
            tokens = new String[tokenizer.countTokens()];
            keywords = new String[tokenizer.countTokens()];
            int i = 0;
            while (tokenizer.hasMoreTokens()) {
                tokens[i++] = tokenizer.nextToken();
            }
            for (i = 0; i < tokens.length; i++) {
                if (JPQLQueryHelper.isKeyword(tokens[i])) {
                    keywords[i] = tokens[i];
                } else if (i < tokens.length - 1 && JPQLQueryHelper.isKeyword(tokens[i] + ' ' + tokens[i + 1])) {
                    keywords[i] = tokens[i];
                    i++;
                    keywords[i] = tokens[i];
                }
            }
        }

        /**
         * Parse the content until a keyword is found.
         * @param keywordToIgnore Ignore this keyword if found first
         * @param allowSubentries Whether to permit subentries (in parentheses) in this next block
         * @return the content
         */
        public String parseContent(String keywordToIgnore, boolean allowSubentries) {
            String content = "";
            int level = 0;
            while (tokenIndex < tokens.length - 1) {
                tokenIndex++;
                if (allowSubentries) {
                    for (int i = 0; i < tokens[tokenIndex].length(); i++) {
                        char c = tokens[tokenIndex].charAt(i);
                        if (c == '(') {
                            level++;
                        } else if (c == ')') {
                            level--;
                        }
                    }
                }
                if (level == 0 && JPQLQueryHelper.isKeyword(tokens[tokenIndex]) && !tokens[tokenIndex].equals(keywordToIgnore)) {
                    tokenIndex--;
                    break;
                } else if (level == 0 && tokenIndex < tokens.length - 1 && JPQLQueryHelper.isKeyword(tokens[tokenIndex] + ' ' + tokens[tokenIndex + 1])) {
                    tokenIndex--;
                    break;
                } else {
                    int endPos = queryString.indexOf(tokens[tokenIndex], queryStringPos) + tokens[tokenIndex].length();
                    String contentValue = queryString.substring(queryStringPos, endPos);
                    queryStringPos = endPos;
                    if (content.length() == 0) {
                        content = contentValue;
                    } else {
                        content += contentValue;
                    }
                }
            }
            return content;
        }

        /**
         * Parse the next token looking for a keyword. 
         * The cursor position is skipped in one tick if a keyword is found
         * @param keyword the searched keyword
         * @return true if the keyword
         */
        public boolean parseKeywordIgnoreCase(String keyword) {
            if (tokenIndex < tokens.length - 1) {
                tokenIndex++;
                if (keywords[tokenIndex] != null) {
                    if (keywords[tokenIndex].equalsIgnoreCase(keyword)) {
                        queryStringPos = queryString.indexOf(keywords[tokenIndex], queryStringPos) + keywords[tokenIndex].length() + 1;
                        return true;
                    }
                    if (keyword.indexOf(' ') > -1) {
                        if ((keywords[tokenIndex] + ' ' + keywords[tokenIndex + 1]).equalsIgnoreCase(keyword)) {
                            queryStringPos = queryString.indexOf(keywords[tokenIndex], queryStringPos) + keywords[tokenIndex].length() + 1;
                            queryStringPos = queryString.indexOf(keywords[tokenIndex + 1], queryStringPos) + keywords[tokenIndex + 1].length() + 1;
                            tokenIndex++;
                            return true;
                        }
                    }
                }
                tokenIndex--;
            }
            return false;
        }

        /**
         * Parse the next token looking for a keyword. The cursor position is
         * skipped in one tick if a keyword is found
         * @return the parsed keyword or null
         */
        public String parseKeyword() {
            if (tokenIndex < tokens.length - 1) {
                tokenIndex++;
                if (keywords[tokenIndex] != null) {
                    return keywords[tokenIndex];
                }
                tokenIndex--;
            }
            return null;
        }
    }
}
