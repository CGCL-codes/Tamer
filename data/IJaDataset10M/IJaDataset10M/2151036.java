package net.sf.laja.context;

import java.util.List;
import net.sf.laja.TemplateTextWriter;
import net.sf.laja.template.Macro;
import net.sf.laja.template.Namespaces;
import net.sf.laja.template.data.AttributeRef;
import net.sf.laja.template.data.AttributeRefs;

public abstract class Context {

    public abstract String getName();

    public abstract boolean contains(String attributeName);

    public abstract void set(String attributeName, Object value);

    public abstract boolean isLazy(AttributeRef attributeRef);

    public abstract Object evaluate(AttributeRef attributeRef);

    public abstract Object evaluateMacro(AttributeRef attributeRef);

    public abstract Macro getMacro(String macroName);

    public abstract void addMacro(Macro macro);

    private Namespaces namespaces;

    private TemplateTextWriter templateTextWriter;

    private ContextFactory contextFactory;

    public Context(Namespaces namespaces, TemplateTextWriter templateTextWriter) {
        this.namespaces = namespaces;
        this.templateTextWriter = templateTextWriter;
        contextFactory = new ContextFactory(namespaces, templateTextWriter);
    }

    public Namespaces getNamespaces() {
        return namespaces;
    }

    public Context createContext(Object attribute, AttributeRef attributeRef) {
        return contextFactory.createContext(attribute, attributeRef);
    }

    public TemplateTextWriter getTemplateTextWriter() {
        return templateTextWriter;
    }

    public Object get(String attributeName) {
        return null;
    }

    public boolean isList(Object object) {
        return object != null && (object instanceof List || object.getClass().isArray());
    }
}
