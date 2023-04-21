package org.rubypeople.rdt.internal.corext.template.ruby;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.templates.GlobalTemplateVariables;
import org.rubypeople.rdt.core.IRubyScript;

public class RubyContextType extends RubyScriptContextType {

    public static final String NAME = "ruby";

    /**
     * Creates a ruby context type.
     */
    public RubyContextType() {
        super(NAME);
        addResolver(new GlobalTemplateVariables.Cursor());
        addResolver(new GlobalTemplateVariables.WordSelection());
        addResolver(new GlobalTemplateVariables.LineSelection());
        addResolver(new GlobalTemplateVariables.Dollar());
        addResolver(new GlobalTemplateVariables.Date());
        addResolver(new GlobalTemplateVariables.Year());
        addResolver(new GlobalTemplateVariables.Time());
        addResolver(new GlobalTemplateVariables.User());
    }

    public RubyScriptContext createContext(IDocument document, int offset, int length, IRubyScript script) {
        return new RubyContext(this, document, offset, length, script);
    }
}
