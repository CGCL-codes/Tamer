package net.sourceforge.circuitsmith.netlist;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import net.sourceforge.circuitsmith.objects.EdaComponentBox;
import net.sourceforge.circuitsmith.objects.EdaDrawing;
import net.sourceforge.circuitsmith.objects.EdaNode;
import net.sourceforge.circuitsmith.objects.EdaObject;
import net.sourceforge.circuitsmith.objects.EdaPin;
import net.sourceforge.circuitsmith.objects.EdaSymbol;
import net.sourceforge.circuitsmith.panes.EdaSchematicPane;
import net.sourceforge.circuitsmith.parts.EdaAttribute;
import net.sourceforge.circuitsmith.parts.EdaPart;
import net.sourceforge.circuitsmith.parts.EdaPartList;
import net.sourceforge.circuitsmith.projects.EdaSchematic;
import net.sourceforge.circuitsmith.projects.EdaTreeNode;

/**
 * This class contains a netlist for a circuit. Roughly it is just a container for Nets.
 * @see Net
 * @see NetElement
 */
public class NetList {

    private final List<Net> list = new ArrayList<Net>();

    public final Net nullNet = new Net("No net", this);

    private final Map<EdaPart, List<EdaPin>> partPin = new HashMap<EdaPart, List<EdaPin>>();

    private final EdaPartList partList;

    private EdaDrawing drawing;

    public NetList(final EdaPartList aPartList) {
        partList = aPartList;
    }

    public void add(Net n) {
        list.add(n);
        n.setNetList(this);
    }

    public void add(NetList nl) {
        for (int i = 0; i < nl.size(); i++) {
            add(nl.get(i));
        }
    }

    protected List<Net> getArrayList() {
        return list;
    }

    public Net get(String name) {
        Net result = null;
        for (int i = 0; i < size(); i++) {
            if (get(i).getName() != null) {
                if (get(i).getName().equals(name)) {
                    result = get(i);
                    break;
                }
            }
        }
        return result;
    }

    public Net get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size(); i++) {
            Net n = get(i);
            s += "Net " + n.toString();
            s += "\n";
        }
        return s;
    }

    /**
     * @return a new netlist from a drawing
     * @param drawing the drawing which the new netlist is to be made from
     */
    public static NetList createNetList(final EdaDrawing drawing) {
        final EdaPartList partList = drawing.getDrawingPane().getDocument().getProject().getPartList();
        final NetList result = new NetList(partList);
        result.drawing = drawing;
        result.update(drawing);
        return result;
    }

    /**
     * @return the index of the specified net
     */
    public int getNetIndex(Net net) {
        return list.indexOf(net);
    }

    /**
     * @return a string of the spice netlist of this netlist
     */
    public String getSpiceNetList() {
        String result = "";
        List<String> modelList = new ArrayList<String>();
        List<String> modelNameList = new ArrayList<String>();
        for (int i = 0; i < partList.size(); i++) {
            String partLine = getSpicePartLine(partList.get(i));
            if (partLine != "") {
                result += partLine + "\n";
            }
            EdaAttribute at = partList.get(i).getAttribute("spice_model");
            if (at != null) {
                String value[] = at.getValue().split(" ", 2);
                boolean exists = false;
                for (int j = 0; j < modelNameList.size(); j++) {
                    if (modelNameList.get(j).toString().equals(value[0])) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    modelNameList.add(value[0]);
                    modelList.add(value[1]);
                }
            }
        }
        for (int i = 0; i < modelNameList.size(); i++) {
            result += ".MODEL " + modelNameList.get(i) + " " + modelList.get(i) + "\n";
        }
        return result;
    }

    /**
     * creates a line of a spice net list for the specified part
     */
    private String getSpicePartLine(EdaPart part) {
        String result = "";
        EdaAttribute at = part.getAttribute("spice_element");
        if (at != null) {
            result = at.getValue();
            result = result.replaceAll("\\Q%ref\\E", part.getReference());
            at = part.getAttribute("value");
            if (at != null) {
                result = result.replaceAll("\\Q%value\\E", at.getValue());
            } else {
                if (Pattern.compile("\\Q%value\\E").matcher(result).find()) {
                    System.out.println("NetList.getSpicePartLine value attribute does not exist in part " + part.getReference());
                }
            }
            for (final EdaPin pin : partPin.get(part)) {
                String pattern = "\\Q%pin\"" + pin.getNumber() + "\"\\E";
                String netName = pin.getNet().getName();
                if (netName == null) {
                    netName = "" + getNetIndex(pin.getNet());
                }
                result = result.replaceAll(pattern, netName);
            }
        }
        return result;
    }

    public Iterable<Net> getNets() {
        return list;
    }

    /**
     * checks that this netlist is correct. Anyting which does not belong is removed
     *
     */
    public void update(EdaDrawing drawing) {
        ArrayList<NetPoint> nps = new ArrayList<NetPoint>();
        List<EdaComponentBox> boxes = drawing.getComponentBoxes();
        for (NetObject no : drawing.getNetObjects()) {
            boolean notInBox = true;
            for (EdaComponentBox b : boxes) {
                Rectangle2D.Float rb = new Rectangle2D.Float();
                Rectangle2D.Float ro = new Rectangle2D.Float();
                b.getBounds(rb, 1);
                ((EdaObject) no).getBounds(ro, 1);
                if (rb.contains(ro)) {
                    notInBox = false;
                }
            }
            if (notInBox) {
                for (NetPoint np : no.getNetPoints()) {
                    nps.add(np);
                }
            }
        }
        for (Net n : list) {
            n.clean(nps);
        }
        collectAndAddPoints(nps);
        collectAndAddNets();
        addNodes(drawing);
        collectNets(drawing);
        System.out.println("NetList.update size of GND: " + get("GND").getNetPoints().size());
        calcSubnets();
        drawing.getDrawingPane().repaint();
    }

    public EdaPartList getPartList() {
        return partList;
    }

    protected void addNodes(EdaDrawing drawing) {
        ArrayList<EdaObject> nodes = new ArrayList<EdaObject>();
        for (EdaObject o : drawing.getAllEdaObjects()) {
            if (o instanceof EdaNode) {
                nodes.add(o);
            }
        }
        for (EdaObject o : nodes) {
            drawing.remove(o);
        }
        for (Net n : list) {
            n.calculateNodes(drawing);
        }
    }

    protected void collectAndAddPoints(ArrayList<NetPoint> nps) {
        while (nps.size() > 0) {
            Net net = new Net();
            NetPoint np1 = nps.get(0);
            nps.remove(np1);
            net.add(np1);
            for (int i = 1; i < nps.size(); i++) {
                NetPoint np2 = nps.get(i);
                if (np1.contacts(np2)) {
                    net.add(np2);
                    nps.remove(i);
                    i--;
                }
            }
            add(net);
        }
    }

    protected void collectAndAddNets() {
        for (int i = 0; i < list.size() - 1; i++) {
            Net n1 = list.get(i);
            if (n1.getNetPoints().size() > 0) {
                NetPoint np1 = n1.getNetPoints().get(0);
                for (int j = i + 1; j < list.size(); j++) {
                    Net n2 = list.get(j);
                    if (n2.getNetPoints().size() > 0) {
                        NetPoint np2 = n2.getNetPoints().get(0);
                        if (np1.contacts(np2)) {
                            n1.add(n2);
                            list.remove(j);
                            j--;
                            i--;
                            break;
                        }
                    }
                }
            }
        }
    }

    protected void collectNets(EdaDrawing drawing) {
        for (int i = 0; i < list.size() - 1; i++) {
            Net n = list.get(i);
            boolean changed = false;
            for (int j = i + 1; i < list.size(); j++) {
                if (j >= list.size()) {
                    break;
                }
                Net n2 = list.get(j);
                String name1 = n.getName();
                String name2 = n2.getName();
                if (name1 != null && name2 != null) {
                    if (name1.equals(name2)) {
                        combine(n, n2);
                        list.remove(n2);
                        changed = true;
                        break;
                    }
                }
            }
            if (n.isEmpty()) {
                list.remove(n);
                i--;
            } else {
                n.consolidate(drawing);
            }
            if (changed) {
                i--;
            }
        }
    }

    public void combine(Net n1, Net n2) {
        if (n1 == null || n2 == null) {
            return;
        }
        if (n1 != n2) {
            if (n2.getId() == 27) {
                System.out.println("NetList.combine adding " + n2.getName() + " " + n1.getNetPoints().size() + " " + n2.getNetPoints().size());
            }
            n1.add(n2);
        }
    }

    private void calcSubnets() {
        ArrayList<EdaDrawing> dList = new ArrayList<EdaDrawing>();
        ArrayList<EdaSymbol> sList = new ArrayList<EdaSymbol>();
        for (Net n : getNets()) {
            for (NetPoint np : n.getNetPoints()) {
                if (np instanceof PinNetPoint) {
                    PinNetPoint pnp = (PinNetPoint) np;
                    EdaSymbol s = pnp.getPin().getSymbol();
                    EdaAttribute subsheet = s.getAttributeList().get("subsheet");
                    if (subsheet != null) {
                        EdaTreeNode node = getPartList().getProject().getNode(s.getPart().getSourceDocumentPath());
                        if (node instanceof EdaSchematic) {
                            EdaSchematic schem = (EdaSchematic) node;
                            EdaSchematicPane sp = (EdaSchematicPane) schem.getDocumentPane();
                            EdaDrawing drawing = sp.getDrawing();
                            if (!dList.contains(drawing)) {
                                dList.add(drawing);
                                sList.add(s);
                            }
                        }
                    }
                }
            }
        }
        for (EdaDrawing d : dList) {
            System.out.println("Netlist.calcSubnets calculating netlist for " + d.getDrawingPane().getName());
            NetList nl = createNetList(d);
            System.out.println("Netlist.calcSubnets linking subnets");
            linkSubNetList(sList.get(dList.indexOf(d)), nl);
        }
    }

    public void linkSubNetList(EdaSymbol s, NetList snl) {
        ArrayList<Net> subNets = new ArrayList<Net>();
        ArrayList<Net> nets = new ArrayList<Net>();
        for (EdaPin p : s.getPins()) {
            Net n = p.getNet();
            EdaAttribute a = p.getAttributeList().get("localnet");
            String localNetName = p.getLocalNetName();
            if (localNetName != null) {
                Net sn = snl.get(localNetName);
                if (sn != null) {
                    subNets.add(sn);
                    nets.add(n);
                } else {
                    throw new RuntimeException("localnet \"" + localNetName + "\" does not exist in schematic \"" + snl.drawing.getDrawingPane().getDocument().getName() + "\"");
                }
            }
        }
        for (Net n : snl.getNets()) {
            n.setName(s.getRef() + " " + n.getName());
        }
        for (int i = 0; i < nets.size(); i++) {
            Net n1 = nets.get(i);
            Net n2 = subNets.get(i);
            System.out.println("NetList.linkSubNetList \"" + n1 + "\" \"" + n2 + "\"");
            combine(n1, n2);
        }
    }
}
