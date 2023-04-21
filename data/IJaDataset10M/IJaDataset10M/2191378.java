package de.knowwe.report.message;

import de.knowwe.core.kdom.objects.TermDefinition;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.report.KDOMError;

public class ObjectAlreadyDefinedError extends KDOMError {

    private final String text;

    private Section<? extends TermDefinition<?>> definition = null;

    public ObjectAlreadyDefinedError(String text) {
        this.text = text;
    }

    public ObjectAlreadyDefinedError(String text, Section<? extends TermDefinition<?>> s) {
        this(text);
        definition = s;
    }

    @Override
    public String getVerbalization() {
        String result = "Object already defined: " + text;
        if (definition != null) {
            result += " in: " + definition.getTitle();
        }
        return result;
    }
}
