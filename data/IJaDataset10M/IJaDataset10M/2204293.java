package verinec.gui.configurator.nodeeditor;

import java.util.Iterator;
import org.jdom.Element;
import verinec.util.VerinecNamespaces;

/** Panel to edit a node. 
 * 
 * @author david.buchmann at unifr.ch
 *
 */
public class NodePanel extends EditFieldsPanel {

    /** Create the panel to edit nodes. 
	 * 
	 * @param xml The xml the panel is based on.
	 * @param nodeEditor The node editor to use.
	 */
    public NodePanel(Element xml, NodeEditor nodeEditor) {
        super(xml, nodeEditor);
        EditorTextField tf = new EditorTextField(ElementType.Hostname, xml, nodeEditor, InputValidator.NodeName, length, true, "Hostname");
        tf.addVetoableChangeListener(new NameChangeListener());
        addField(tf);
        Iterator ethernetListIter = xml.getChild(ElementType.Hardware, VerinecNamespaces.NS_NODE).getChildren(ElementType.Ethernet, VerinecNamespaces.NS_NODE).iterator();
        while (ethernetListIter.hasNext()) {
            add(new EthernetPanel((Element) ethernetListIter.next(), nodeEditor));
        }
        Iterator wlanListIter = xml.getChild(ElementType.Hardware, VerinecNamespaces.NS_NODE).getChildren(ElementType.Wlan, VerinecNamespaces.NS_NODE).iterator();
        while (wlanListIter.hasNext()) {
            add(new WlanPanel((Element) wlanListIter.next(), nodeEditor));
        }
        Iterator serialListIter = xml.getChild(ElementType.Hardware, VerinecNamespaces.NS_NODE).getChildren(ElementType.Serial, VerinecNamespaces.NS_NODE).iterator();
        while (serialListIter.hasNext()) {
            add(new SerialPanel((Element) serialListIter.next(), nodeEditor));
        }
    }
}
