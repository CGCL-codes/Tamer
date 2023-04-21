package se.kth.cid.conzilla.filter;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import se.kth.cid.concept.Concept;
import se.kth.cid.conzilla.app.ConzillaKit;
import se.kth.cid.conzilla.app.Extra;
import se.kth.cid.conzilla.browse.ViewTool;
import se.kth.cid.conzilla.controller.MapController;
import se.kth.cid.conzilla.properties.ConzillaResourceManager;
import se.kth.cid.conzilla.tool.ToolsMenu;

/**
 * @author Matthias Palmer.
 */
public class FilterExtra implements Extra {

    FilterFactory filterFactory;

    public FilterExtra() {
        filterFactory = new SimpleFilterFactory();
    }

    public boolean initExtra(ConzillaKit kit) {
        return true;
    }

    public String getName() {
        return "filter";
    }

    public void refreshExtra() {
        filterFactory.refresh();
    }

    public boolean saveExtra() {
        return true;
    }

    public void exitExtra() {
    }

    public void extendMenu(ToolsMenu menu, MapController c) {
    }

    public void addExtraFeatures(MapController c) {
    }

    protected void recursivelyBuildMenu(final ViewTool va, JMenu menu, FilterNode node, Concept concept, MapController controller) {
        List contents = node.getContent(concept);
        if (contents.isEmpty()) return;
        FilterNode subnode;
        for (int i = 0; i < node.numOfRefines(); i++) {
            subnode = node.getRefine(i);
            if (subnode.numOfRefines() == 0) {
                FilterAction filteraction = new FilterAction(subnode) {

                    public void actionPerformed(ActionEvent ae) {
                    }
                };
                filteraction.setComponent(concept);
                JMenuItem menuItem = menu.add(filteraction);
                String toolTipText = subnode.getToolTipText();
                if (toolTipText != null) menuItem.setToolTipText(toolTipText);
            } else {
                JMenu submenu = new JMenu(subnode.getFilterTag());
                recursivelyBuildMenu(va, submenu, subnode, concept, controller);
                JMenuItem menuItem = menu.add(submenu);
                String toolTipText = subnode.getToolTipText();
                if (toolTipText != null) menuItem.setToolTipText(toolTipText);
            }
        }
        FilterAction menuAny = new FilterAction(node, "Any") {

            public boolean isEnabled() {
                Set set = this.node.getContentPassedRefines(component);
                return set.size() > 0;
            }

            public void actionPerformed(ActionEvent ae) {
            }
        };
        menuAny.setComponent(concept);
        FilterAction menuOther = new FilterAction(node, "Other") {

            public boolean isEnabled() {
                Set set = this.node.getContentPassedRefines(component);
                List list = this.node.getContent(component);
                List other = new Vector(list);
                other.removeAll(set);
                return other.size() > 0;
            }

            public void actionPerformed(ActionEvent ae) {
                Set set = this.node.getContentPassedRefines(component);
                List list = this.node.getContent(component);
                List other = new Vector(list);
                other.removeAll(set);
            }
        };
        menuOther.setComponent(concept);
        menu.addSeparator();
        ConzillaResourceManager.getDefaultManager().customizeButton(menu.add(menuAny), FilterExtra.class.getName(), "VIEW_ANY");
        ConzillaResourceManager.getDefaultManager().customizeButton(menu.add(menuOther), FilterExtra.class.getName(), "VIEW_OTHER");
        menu.addSeparator();
    }
}
