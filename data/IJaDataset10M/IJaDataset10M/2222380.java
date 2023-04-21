package org.adapit.wctoolkit.events.actions.mof;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import org.adapit.wctoolkit.infrastructure.events.actions.AbstractAction;
import org.adapit.wctoolkit.infrastructure.treecontrollers.DefaultElementTreeController;
import org.adapit.wctoolkit.uml.ext.core.IElement;
import org.adapit.wctoolkit.uml.ext.core.Stereotype;

public class MarkAsPLDTAction extends AbstractAction {

    public MarkAsPLDTAction() {
        super();
    }

    public MarkAsPLDTAction(IElement element, DefaultElementTreeController controller) {
        super(element, controller);
    }

    @SuppressWarnings("unchecked")
    protected IElement[] doAction(ActionEvent e) {
        try {
            initElement();
            if (element instanceof org.adapit.wctoolkit.uml.classes.kernel.Class) {
                org.adapit.wctoolkit.uml.classes.kernel.Class c = (org.adapit.wctoolkit.uml.classes.kernel.Class) element;
                Stereotype st = new Stereotype(c);
                st.setName("ProgrammingLanguageDataType");
                c.assignStereotype(st);
                return new IElement[] { st };
            } else if (element instanceof org.adapit.wctoolkit.uml.ext.core.Package) {
                {
                    ArrayList elements = element.getElements(org.adapit.wctoolkit.uml.classes.kernel.Class.class);
                    Iterator<org.adapit.wctoolkit.uml.classes.kernel.Class> it = elements.iterator();
                    while (it.hasNext()) {
                        org.adapit.wctoolkit.uml.classes.kernel.Class c = (it.next());
                        Stereotype st = new Stereotype(c);
                        st.setName("ProgrammingLanguageDataType");
                        c.assignStereotype(st);
                    }
                }
                {
                    ArrayList elements = element.getElements(org.adapit.wctoolkit.uml.classes.kernel.Enumeration.class);
                    Iterator<org.adapit.wctoolkit.uml.classes.kernel.Enumeration> it = elements.iterator();
                    while (it.hasNext()) {
                        org.adapit.wctoolkit.uml.classes.kernel.Enumeration c = (it.next());
                        Stereotype st = new Stereotype(c);
                        st.setName("ProgrammingLanguageDataType");
                        c.assignStereotype(st);
                    }
                }
                element.getNode().updateNode();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }
}
