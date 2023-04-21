package org.hibernate.hql.classic;

import org.hibernate.QueryException;
import org.hibernate.util.StringHelper;

/**
 * Parses the ORDER BY clause of a query
 */
public class OrderByParser implements Parser {

    private final PathExpressionParser pathExpressionParser;

    {
        pathExpressionParser = new PathExpressionParser();
        pathExpressionParser.setUseThetaStyleJoin(true);
    }

    public void token(String token, QueryTranslatorImpl q) throws QueryException {
        if (q.isName(StringHelper.root(token))) {
            ParserHelper.parse(pathExpressionParser, q.unalias(token), ParserHelper.PATH_SEPARATORS, q);
            q.appendOrderByToken(pathExpressionParser.getWhereColumn());
            pathExpressionParser.addAssociation(q);
        } else if (token.startsWith(ParserHelper.HQL_VARIABLE_PREFIX)) {
            q.addNamedParameter(token.substring(1));
            q.appendOrderByToken("?");
        } else {
            q.appendOrderByToken(token);
        }
    }

    public void start(QueryTranslatorImpl q) throws QueryException {
    }

    public void end(QueryTranslatorImpl q) throws QueryException {
    }
}
