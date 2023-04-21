package edu.mit.lcs.haystack.adenine.constructs;

import edu.mit.lcs.haystack.adenine.parser2.ICodeBlockVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IConstructVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IExpressionVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.GenericToken;

/**
 * @author David Huynh
 */
public interface IForVisitor extends IConstructVisitor {

    public IExpressionVisitor onForIn(GenericToken forKeyword, GenericToken iterator, GenericToken inKeyword);

    public ICodeBlockVisitor onBody();
}
