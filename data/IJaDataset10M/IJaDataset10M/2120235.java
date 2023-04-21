package verinec.gui.configurator.nodeeditor;

import luxor.swing.widget.CollapsiblePanel;
import org.jdom.Element;
import verinec.util.VerinecNamespaces;

/** Panel to edit an ethernet interface.
 * 
 * fixme: there seems to be a bug with handling multiple nw. Only the first is shown. GUI does not allow to add new ones.
 * 
 * @author david.buchmann at unifr.ch
 *
 */
public class EthernetPanel extends EditFieldsPanel {

    /** Create a panel to edit ethernet properties. 
	 * 
	 * @param xml The XML defining this panel.
	 * @param nodeEditor Root editor class for reference.
	 */
    public EthernetPanel(Element xml, NodeEditor nodeEditor) {
        super(xml, nodeEditor);
        EditFieldsPanel.displayPartName(xml, this, nodeEditor);
        EditFieldsPanel.displayPartHWAddress(xml, this, nodeEditor);
        EditFieldsPanel binding = new EditFieldsPanel(xml.getChild(ElementType.EthernetBinding, VerinecNamespaces.NS_NODE), nodeEditor);
        EditFieldsPanel.displayPartNw(binding.getXml(), binding, nodeEditor);
        this.add(new CollapsiblePanel(binding, binding.getNameForPanel()));
    }
}
