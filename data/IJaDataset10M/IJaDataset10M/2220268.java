package com.qasystems.qstudio.configuration;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.text.ParseException;

/**
 * This class represents the QCF notion Literal.
 *
 */
public class Literal extends BasicExpression {

    /**
   * Default constructor.
   */
    public Literal() {
        super();
    }

    /**
   * Parse a literal.
   *
   * @param tokenizer the supplier of tokens
   * @return this literal.
   * @throws IOException when an IO problem occured
   * @throws ParseException when parsing failed
   */
    public AttributeValue parse(StreamTokenizer tokenizer) throws IOException, ParseException {
        Literal result = null;
        if (tokenizer.nextToken() == StreamTokenizer.TT_NUMBER) {
            result = new NumericLiteral();
        } else if (tokenizer.ttype == '"') {
            result = new StringLiteral();
        } else {
            result = new BooleanLiteral();
        }
        tokenizer.pushBack();
        return (result.parse(tokenizer));
    }
}
