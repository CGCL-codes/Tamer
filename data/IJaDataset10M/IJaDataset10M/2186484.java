package de.uniwue.tm.textmarker.kernel.extensions;

import java.util.List;
import antlr.ANTLRException;
import de.uniwue.tm.textmarker.kernel.expression.TextMarkerExpression;
import de.uniwue.tm.textmarker.kernel.expression.string.StringFunctionExpression;

public interface ITextMarkerStringFunctionExtension extends ITextMarkerExtension {

    StringFunctionExpression createStringFunction(String name, List<TextMarkerExpression> args) throws ANTLRException;
}
