package de.uniwue.dltk.textmarker.core.extensions;

import java.util.List;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.eclipse.dltk.ast.expressions.Expression;
import de.uniwue.dltk.textmarker.parser.ast.TextMarkerExpression;

public abstract class AbstractIDENumberFunctionExtension implements IIDENumberFunctionExtension {

    public TextMarkerExpression createNumberFunction(Token type, List<Expression> args) throws RecognitionException {
        return new TextMarkerExpression(0, 0, args.get(0), 0);
    }
}
