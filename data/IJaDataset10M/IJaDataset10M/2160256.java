package edu.mit.lcs.haystack.adenine.constructs;

import edu.mit.lcs.haystack.adenine.parser2.ICodeBlockVisitor;
import edu.mit.lcs.haystack.adenine.parser2.IConstructVisitor;
import edu.mit.lcs.haystack.adenine.tokenizer.GenericToken;

/**
 * @author David Huynh
 */
public interface IFunctionVisitor extends IConstructVisitor {

    public void onFunction(GenericToken functionKeyword, GenericToken name);

    public void onParameter(GenericToken parameter);

    public ICodeBlockVisitor onBody();
}
