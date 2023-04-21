package verinec.gui.core;

import java.awt.Point;
import java.util.Iterator;
import java.util.Vector;
import org.jdom.Element;
import verinec.gui.*;
import verinec.util.VerinecNamespaces;

/** This class offers standard implementations of methods
 * that every interface must contain. 
 * An interface must know its node and its bindings.
 * It also computes positions for new bindings for that interface. 
 * 
 * @author Renato Loeffel
 *
 */
public abstract class NwInterface extends NwComponent {

    /** The node this interface belongs to. */
    protected PCNode owner;

    /** The side of the node this interface is on. */
    protected int side;

    /** Initializes the interface.
	 * 
	 * @param config The config element of the component.
     * @param pos The position of the interface.
	 * @param owner The node which this interface is part of.
	 * @param gui The main application.
	 */
    protected NwInterface(Element config, Point pos, PCNode owner, VerinecStudio gui) {
        super(config, pos, 0.25f, NetworkTypes.getInterfaceIconPath(config.getName()), gui, NetworkTypes.getInterfaceType(config.getName()));
        this.owner = owner;
        side = Integer.parseInt(config.getChild("interface", VerinecNamespaces.NS_GUI).getAttributeValue("side"));
    }

    /** Delete the interface: Remove it from owner and let super class handle the rest.
      * 
      */
    public void delete() {
        Iterator i = getBindings().iterator();
        while (i.hasNext()) ((NwBinding) i.next()).delete();
        owner.removeInterface(this);
        super.delete();
    }

    /** Set the side of the node this interface is on.
     * If you change this, you should set the new interface position 
     * afterwards to have the bindings positions updated too.
     *  
     * @param side The new side of the interface, one of PCNode.IF_RIGHT/LEFT/ABOVE/BELOW. 
     */
    public void setSide(int side) {
        this.side = side;
        Element nodeInfo = config.getChild("interface", VerinecNamespaces.NS_GUI);
        if (nodeInfo == null) {
            nodeInfo = new Element("interface", VerinecNamespaces.NS_GUI);
            config.addContent(0, nodeInfo);
        }
        nodeInfo.setAttribute("side", String.valueOf(side));
    }

    /** Find out on which side of the node this interface is situated.
     * 
     * @return Side indicator, one of PCNode.IF_RIGHT/LEFT/ABOVE/BELOW.
     */
    public int getSide() {
        return side;
    }

    /** Get the owner of this interface.
     * 
     * @return The owning PCNode instance.
     */
    public PCNode getOwner() {
        return owner;
    }

    /** Get all bindings owned by this interface.
 	 * 
	 * @return All bindings in a Vector of NwBindings.
	 */
    public abstract Vector getBindings();

    /** Removes a binding from the interface.
	 * 
	 * @param nwBinding The binding to remove.
	 */
    public abstract void removeBinding(NwBinding nwBinding);

    /**Adds a binding to the interface. If this is a SingleInterface, the old 
	 * binding is deleted first.
     * Of course, the type of the binding must match type of interface.
	 * 
	 * @param nwBinding The new binding.
	 */
    public abstract void addBinding(NwBinding nwBinding);

    /** Extract the node gui information child from the configuration, add a default if necessary. 
     * 
     * @param config The network xml object.
     * @return A gui element from configuration namespace.
     */
    protected Element getLayoutChild(Element config) {
        Element nodeInfo = config.getChild("interface", VerinecNamespaces.NS_GUI);
        if (nodeInfo == null) {
            nodeInfo = createInterfaceInfo(config, side);
        } else {
            side = Integer.parseInt(nodeInfo.getAttributeValue("side"));
        }
        return nodeInfo;
    }

    /** Create the interface layout information and add it to the config.
     * @param config The interface configuration the info will be attached to.
     * @param side Side of the node.
     * @return The generated interface information element.
     */
    protected static Element createInterfaceInfo(Element config, int side) {
        Element nodeInfo = new Element("interface", VerinecNamespaces.NS_GUI);
        nodeInfo.setAttribute("side", Integer.toString(side));
        config.addContent(0, nodeInfo);
        return nodeInfo;
    }

    /**Get a minimal configuration of an interface, including one binding.
	 * 
	 * @param type Network type of the element.
     * @param side The side of the node this interface is on.
	 * @return return the element of the interface.
	 */
    protected static Element createElement(int type, int side) {
        String name = NetworkTypes.getXmlName(type);
        Element create = new Element(name, VerinecNamespaces.NS_NODE);
        createInterfaceInfo(create, side);
        Element binding = NwBinding.createElement(name);
        create.addContent(binding);
        return create;
    }
}
