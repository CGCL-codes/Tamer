package jp.seraph.jsade.sexpression;

import java.text.ParseException;
import java.util.List;

/**
 * String -> SExpression変換を行うパーザインターフェース
 *
 *
 */
public interface ExpressionParser {

    List<SExpression> parse(String aExpression) throws ParseException;
}
