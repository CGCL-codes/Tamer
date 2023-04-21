package org.argouml.BOTL;

import java.beans.PropertyVetoException;
import org.argouml.kernel.Project;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.botl_rule.ui.FigBOTLArrow;
import org.argouml.uml.diagram.botl_rule.ui.FigComment;
import org.argouml.uml.diagram.botl_rule.ui.UMLBOTLRuleDiagram;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MPackage;

/**
 * @author Frank
 */
public class Argo4BotlUtils {

    /**
   *  
   */
    public static UMLBOTLRuleDiagram createRuleDiagram() {
        Project p = ProjectBrowser.TheInstance.getProject();
        Object target = ProjectBrowser.TheInstance.getDetailsTarget();
        MNamespace ns = p.getCurrentNamespace();
        if (ns == null) {
            return null;
        }
        if (target instanceof MPackage) ns = (MNamespace) target;
        try {
            UMLBOTLRuleDiagram d = new UMLBOTLRuleDiagram(ns);
            p.addMember(d);
            ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
            ProjectBrowser.TheInstance.setTarget(d);
            FigBOTLArrow fba = new FigBOTLArrow();
            MComment arrow = UmlFactory.getFactory().getCore().buildComment(d.getOwner());
            arrow.setName("Object for BOTL Arrow");
            fba.setOwner(arrow);
            d.add(fba);
            MComment mc = UmlFactory.getFactory().getCore().buildComment(((UMLBOTLRuleDiagram) d).getOwner());
            FigComment fc = new FigComment(d.getGraphModel(), mc);
            fc.setLocation(-100, -100);
            d.add(fc);
            return d;
        } catch (PropertyVetoException pve) {
            return null;
        }
    }
}
