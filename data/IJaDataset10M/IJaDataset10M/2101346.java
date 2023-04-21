package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import org.argouml.kernel.Project;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MPackage;

public class ActionClassDiagram extends UMLChangeAction {

    public static ActionClassDiagram SINGLETON = new ActionClassDiagram();

    public ActionClassDiagram() {
        super("ClassDiagram");
    }

    public void actionPerformed(ActionEvent ae) {
        Project p = ProjectBrowser.TheInstance.getProject();
        Object target = ProjectBrowser.TheInstance.getDetailsTarget();
        MNamespace ns = p.getCurrentNamespace();
        if (target instanceof MPackage) ns = (MNamespace) target;
        try {
            ArgoDiagram d = new UMLClassDiagram(ns);
            p.addMember(d);
            ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
            ProjectBrowser.TheInstance.setTarget(d);
        } catch (PropertyVetoException pve) {
        }
        super.actionPerformed(ae);
    }
}
