package freemarker.core;

import java.util.*;
import java.io.IOException;
import freemarker.template.TemplateException;

/**
 * Encapsulates an array of <tt>TemplateElement</tt> objects. 
 */
final class MixedContent extends TemplateElement {

    MixedContent() {
        nestedElements = new ArrayList();
    }

    void addElement(TemplateElement element) {
        nestedElements.add(element);
    }

    TemplateElement postParseCleanup(boolean stripWhitespace) throws ParseException {
        super.postParseCleanup(stripWhitespace);
        if (nestedElements.size() == 1) {
            return (TemplateElement) nestedElements.get(0);
        }
        return this;
    }

    /**
     * Processes the contents of the internal <tt>TemplateElement</tt> list,
     * and outputs the resulting text.
     */
    void accept(Environment env) throws TemplateException, IOException {
        for (int i = 0; i < nestedElements.size(); i++) {
            TemplateElement element = (TemplateElement) nestedElements.get(i);
            env.visit(element);
        }
    }

    public String getCanonicalForm() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < nestedElements.size(); i++) {
            TemplateElement element = (TemplateElement) nestedElements.get(i);
            buf.append(element.getCanonicalForm());
        }
        return buf.toString();
    }

    public String getDescription() {
        if (parent == null) {
            return "root element";
        }
        return "content";
    }

    boolean isIgnorable() {
        return nestedElements == null || nestedElements.size() == 0;
    }
}
