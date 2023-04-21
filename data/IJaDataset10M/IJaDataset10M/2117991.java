package se.liu.ida.JessTab;

import edu.stanford.smi.protege.widget.DocumentationWidget;
import edu.stanford.smi.protege.model.*;

/**
 * This class implements a Protege widget for displaying the documentation for a
 * Jess definition in JessTab.
 *
 * @author Henrik Eriksson
 */
@SuppressWarnings("serial")
public class JessDocumentationWidget extends DocumentationWidget {

    public void setEditable(boolean b) {
        super.setEditable(false);
    }

    public static boolean isSuitable(Cls cls, Slot slot, Facet facet) {
        return slot.getName().equals(Model.Slot.DOCUMENTATION) && cls.hasSuperclass(JessTab.getCls(cls.getKnowledgeBase(), JessTabSystemClasses.JESS_DEFINITION));
    }
}
