package net.sf.beezle.sushi.metadata.xml;

import net.sf.beezle.sushi.metadata.Variable;
import org.xml.sax.Locator;

public class SAXVariableException extends SAXLoaderException {

    public final Variable<?> variable;

    public SAXVariableException(Variable<?> variable, Locator locator, Throwable e) {
        super(variable.item.getName() + ": " + e.getMessage(), locator);
        this.variable = variable;
    }
}
