package org.argouml.uml.diagram.botl_rule.ui;

import java.awt.FlowLayout;
import java.beans.PropertyVetoException;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.Action;
import org.argouml.kernel.Project;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.botl_model.ui.UMLBOTLSourceDiagram;
import org.argouml.uml.diagram.botl_rule.BOTLRuleDiagramGraphModel;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.ui.ToolBar;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @author Janos Kovats
 *
 * This class creates a new BOTL Rule Diagram and initialize the toolbar
 */
public class UMLBOTLRuleDiagram extends UMLDiagram {

    protected static Action _actionObject = new CmdCreateNode(MInstance.class, "Instance");

    protected static Action _actionLink = new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", MLink.class, "Link");

    protected static int _BOTLRuleDiagramSerial = 1;

    /** Creates a new Diagram name **/
    protected static String getNewDiagramName() {
        String name = null;
        Object[] args = { name };
        do {
            name = "BOTL Rule Diagram " + _BOTLRuleDiagramSerial;
            _BOTLRuleDiagramSerial++;
            args[0] = name;
        } while (TheInstance.vetoCheck("name", args));
        return name;
    }

    /** Constructor */
    public UMLBOTLRuleDiagram() {
        super();
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    /** Constructor */
    public UMLBOTLRuleDiagram(MNamespace m) {
        super(getNewDiagramName(), m);
    }

    public void setNamespace(MNamespace m) {
        super.setNamespace(m);
        BOTLRuleDiagramGraphModel gm = new BOTLRuleDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        BOTLRuleDiagramRenderer rend = new BOTLRuleDiagramRenderer();
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /** initialize the toolbar for this diagram type */
    protected void initToolBar() {
        _toolBar = new ToolBar();
        _toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        _toolBar.add(_actionSelect);
        _toolBar.add(_actionBroom);
        _toolBar.addSeparator();
        _toolBar.add(_actionObject);
        _toolBar.add(_actionLink);
        _toolBar.addSeparator();
        _toolBar.add(ActionAddNote.SINGLETON);
        _toolBar.addSeparator();
        _toolBar.add(_diagramName);
    }

    /** To set the Source Diagram (Comment) for this Rule Diagram */
    public void setSource(UMLBOTLSourceDiagram s) {
        Enumeration enu = this.elements();
        boolean found = false;
        Object o = null;
        while (!found) {
            o = enu.nextElement();
            if (o instanceof FigBOTLArrow) found = true;
        }
        if (!found) return;
        ((MComment) ((FigBOTLArrow) o).getOwner()).setName(s.getName());
    }

    /** To get the Source Diagram (Comment) for this Rule Diagram */
    public UMLBOTLSourceDiagram getSource() {
        Enumeration enu = this.elements();
        boolean found = false;
        Object o = null;
        while (!found) {
            o = enu.nextElement();
            if (o instanceof FigBOTLArrow) {
                found = true;
            }
        }
        if (!found) {
            return null;
        }
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = pb.getProject();
        Vector diagrams = p.getDiagrams();
        int i = 0;
        while (i < diagrams.size()) {
            ArgoDiagram d = (ArgoDiagram) diagrams.elementAt(i);
            if (d.getName().equals(((MComment) ((FigBOTLArrow) o).getOwner()).getName())) {
                return (UMLBOTLSourceDiagram) d;
            }
            i++;
        }
        return null;
    }
}
