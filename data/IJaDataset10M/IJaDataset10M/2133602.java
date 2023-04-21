package eu.irreality.age;

import java.util.ArrayList;
import java.util.List;
import eu.irreality.age.debug.Debug;

/**
* Camino de una habitaci�n a otra (salida de la habitaci�n)
*
*/
public class Path extends Entity implements Descriptible {

    public static final int NORTE = 0, SUR = 1, OESTE = 2, ESTE = 3, NOROESTE = 4, NORDESTE = 5, SUROESTE = 6, SUDESTE = 7, ARRIBA = 8, ABAJO = 9;

    public static final int NORTH = NORTE, SOUTH = SUR, WEST = OESTE, EAST = ESTE, NORTHWEST = NOROESTE, NORTHEAST = NORDESTE, SOUTHWEST = SUROESTE, SOUTHEAST = SUDESTE, UP = ARRIBA, DOWN = ABAJO;

    private World mundo;

    /**
	 * This method is only used to save Path objects to XML, and NOT to parse direction names or output them to
	 * the player. Therefore, it is not a multilanguage method and must not be used as such.
	 * @param dir
	 * @return
	 */
    public static final String directionName(int dir) {
        String s;
        switch(dir) {
            case NORTE:
                s = "norte";
                break;
            case SUR:
                s = "sur";
                break;
            case OESTE:
                s = "oeste";
                break;
            case ESTE:
                s = "este";
                break;
            case NOROESTE:
                s = "noroeste";
                break;
            case NORDESTE:
                s = "nordeste";
                break;
            case SUROESTE:
                s = "suroeste";
                break;
            case SUDESTE:
                s = "sudeste";
                break;
            case ARRIBA:
                s = "arriba";
                break;
            case ABAJO:
            default:
                s = "abajo";
                break;
        }
        return s;
    }

    /**
	 * This method is only used to build Path objects from XML, and NOT to parse direction names or output them to
	 * the player. Therefore, it is not a multilanguage method and must not be used as such.
	 * @param dir
	 * @return
	 */
    public static final int nameToDirection(String nombre) {
        String name = nombre.toLowerCase();
        if (name.equalsIgnoreCase("norte")) return NORTE;
        if (name.equalsIgnoreCase("sur")) return SUR;
        if (name.equalsIgnoreCase("este")) return ESTE;
        if (name.equalsIgnoreCase("oeste")) return OESTE;
        if (name.equalsIgnoreCase("noroeste")) return NOROESTE;
        if (name.equalsIgnoreCase("nordeste")) return NORDESTE;
        if (name.equalsIgnoreCase("suroeste")) return SUROESTE;
        if (name.equalsIgnoreCase("sudeste")) return SUDESTE;
        if (name.equalsIgnoreCase("arriba")) return ARRIBA;
        if (name.equalsIgnoreCase("abajo")) return ABAJO; else {
            Debug.println("Found strange std. path direction: " + name);
            (new Exception()).printStackTrace();
            return 10;
        }
    }

    protected byte exitTime;

    protected Description[] descriptionList;

    String[] exitCommand;

    int destination;

    protected boolean peerable;

    protected boolean isStandard;

    Inventory keys;

    protected Item associatedItem = null;

    public void setDestination(Room r) {
        destination = r.getID();
    }

    public Object clone() {
        Path p = new Path();
        p.mundo = this.mundo;
        p.exitTime = this.exitTime;
        p.descriptionList = new Description[this.descriptionList.length];
        p.isStandard = this.isStandard;
        p.destination = this.destination;
        p.associatedItem = this.associatedItem;
        p.peerable = peerable;
        if (this.keys != null) p.keys = (Inventory) this.keys.clone(); else p.keys = this.keys;
        for (int i = 0; i < this.descriptionList.length; i++) {
            p.descriptionList[i] = (Description) this.descriptionList[i].clone();
        }
        if (this.exitCommand != null) {
            p.exitCommand = new String[this.exitCommand.length];
            for (int i = 0; i < this.exitCommand.length; i++) {
                p.exitCommand[i] = this.exitCommand[i];
            }
        } else {
            p.exitCommand = null;
        }
        return p;
    }

    private Path() {
        ;
    }

    Path(World mundo, boolean isStandard, String curToken) {
        this.mundo = mundo;
        byte ntokens = (byte) StringMethods.numToks(curToken, '$');
        this.isStandard = isStandard;
        try {
            destination = Integer.valueOf(StringMethods.getTok(curToken, 1, '$')).intValue();
        } catch (NumberFormatException NumExc) {
            destination = 0;
        }
        descriptionList = Utility.loadDescriptionListFromString(StringMethods.getTok(curToken, 2, '$'));
        if (ntokens > 2) exitTime = Byte.valueOf(StringMethods.getTok(curToken, 3, '$')).byteValue();
        if (isStandard) {
            if (ntokens > 3) peerable = Boolean.valueOf(StringMethods.getTok(curToken, 4, '$')).booleanValue();
            if (ntokens > 4) {
                String thetoken = StringMethods.getTok(curToken, 5, '$');
                if (thetoken.length() > 4 && thetoken.substring(0, 4).equalsIgnoreCase("item")) {
                    associatedItem = mundo.getItem(thetoken.substring(4));
                } else {
                    setNewState(Integer.valueOf(StringMethods.getTok(curToken, 5, '$')).intValue());
                }
            }
            if (ntokens > 5) {
                String thetoken = StringMethods.getTok(curToken, 6, '$');
                int nKeys = StringMethods.numToks(thetoken, '&');
                keys = new Inventory(10000000, 10000000);
                for (int i = 0; i < nKeys; i++) {
                    try {
                        if (Integer.valueOf(StringMethods.getTok(thetoken, i + 1, '&')).intValue() > 0) keys.addItem(mundo.getItem(StringMethods.getTok(thetoken, i + 1, '&')));
                    } catch (Exception exc) {
                        mundo.write("Excepci�n absurda. �Habr� alguna llave muy pesada?");
                        Debug.println(exc);
                    }
                }
            } else {
                keys = new Inventory(10000000, 10000000);
            }
        } else {
            if (ntokens > 3) {
                String commandList = StringMethods.getTok(curToken, 4, '$');
                exitCommand = new String[StringMethods.numToks(commandList, '&')];
                for (int j = 0; j < exitCommand.length; j++) exitCommand[j] = StringMethods.getTok(commandList, j + 1, '&');
            }
            if (ntokens > 4) peerable = Boolean.valueOf(StringMethods.getTok(curToken, 5, '$')).booleanValue();
            if (ntokens > 5) {
                String thetoken = StringMethods.getTok(curToken, 6, '$');
                if (thetoken.length() > 4 && thetoken.substring(0, 4).equalsIgnoreCase("item")) {
                    associatedItem = mundo.getItem(thetoken.substring(4));
                } else {
                    setNewState(Integer.valueOf(StringMethods.getTok(curToken, 6, '$')).intValue());
                }
            }
            if (ntokens > 6) {
                String thetoken = StringMethods.getTok(curToken, 7, '$');
                int nKeys = StringMethods.numToks(thetoken, '&');
                keys = new Inventory(10000000, 10000000);
                for (int i = 0; i < nKeys; i++) {
                    try {
                        if (Integer.valueOf(StringMethods.getTok(thetoken, i + 1, '&')).intValue() > 0) keys.addItem(mundo.getItem(StringMethods.getTok(thetoken, i + 1, '&')));
                    } catch (Exception exc) {
                        mundo.write("Excepci�n absurda. �Habr� alguna llave muy pesada?");
                    }
                }
            } else {
                keys = new Inventory(10000000, 10000000);
            }
        }
    }

    public boolean isValid() {
        return (destination != 0);
    }

    public int getDestinationID() {
        return destination;
    }

    public String getDescription(long comparand) {
        String desString = "";
        for (int i = 0; i < descriptionList.length; i++) {
            if (descriptionList[i].matches(comparand)) desString += descriptionList[i].getText();
        }
        return desString;
    }

    public String getDescription(Entity viewer) {
        String desString = "";
        for (int i = 0; i < descriptionList.length; i++) {
            if (descriptionList[i].matchesConditions(this, viewer)) desString += descriptionList[i].getText();
        }
        return desString;
    }

    /**
	* Nos dice si la salida "se da por aludida" ante el contenido de un comando "ir" (por ejemplo, "Norte").
	*
	* return Si la salida se corresponde al comando.
	*/
    public boolean matchExitCommand(String toParse) {
        for (int i = 0; i < exitCommand.length; i++) if (toParse.toLowerCase().endsWith(exitCommand[i].toLowerCase())) return true;
        return false;
    }

    public String getNonStandardName() {
        Debug.println("Path: " + destination + " " + isStandard);
        return exitCommand[0];
    }

    public String[] getNonStandardNames() {
        return exitCommand;
    }

    void go(Mobile c) {
        c.setNewState(2, exitTime * 12 / c.getStat("velocidad"));
        c.setNewTarget(destination);
        Debug.println("Target is: " + c.getTarget());
        Debug.println("Entity is: " + c.getID());
        Debug.println("Exit time: " + exitTime);
    }

    public void changeState(World mundo) {
    }

    public int getState() {
        if (associatedItem == null) return super.getState(); else return associatedItem.getState();
    }

    public boolean getPropertyValueAsBoolean(String propName) {
        if (propName.equals("closed") || propName.equals("locked")) {
            if (associatedItem == null) return super.getPropertyValueAsBoolean(propName); else return associatedItem.getPropertyValueAsBoolean(propName);
        } else return super.getPropertyValueAsBoolean(propName);
    }

    public boolean isOpen() {
        return ((256 & getState()) == 0 && !getPropertyValueAsBoolean("closed"));
    }

    public boolean isClosed() {
        return !isOpen();
    }

    public boolean isLocked() {
        return !isUnlocked();
    }

    public boolean isUnlocked() {
        return ((512 & getState()) == 0 && !getPropertyValueAsBoolean("locked"));
    }

    public boolean unlocksWithKey(Item key) {
        if (associatedItem != null) {
            return associatedItem.unlocksWithKey(key);
        } else {
            for (int i = 0; i < keys.size(); i++) {
                if (keys.elementAt(i).equals(key)) return true;
            }
            return false;
        }
    }

    public boolean isStandard() {
        return isStandard;
    }

    public boolean isExtended() {
        return (exitCommand != null && exitCommand.length > 0);
    }

    public static String invert(String s) {
        String temp = StringMethods.textualSubstitution(s, "norte", "$%%N%%$");
        temp = StringMethods.textualSubstitution(temp, "sur", "$%%S%%$");
        temp = StringMethods.textualSubstitution(temp, "oeste", "$%%W%%$");
        temp = StringMethods.textualSubstitution(temp, "este", "$%%E%%$");
        temp = StringMethods.textualSubstitution(temp, "arriba", "$%%U%%$");
        temp = StringMethods.textualSubstitution(temp, "abajo", "$%%D%%$");
        temp = StringMethods.textualSubstitution(temp, "dentro", "$%%I%%$");
        temp = StringMethods.textualSubstitution(temp, "fuera", "$%%O%%$");
        temp = StringMethods.textualSubstitution(temp, "$%%N%%$", "sur");
        temp = StringMethods.textualSubstitution(temp, "$%%S%%$", "norte");
        temp = StringMethods.textualSubstitution(temp, "$%%E%%$", "oeste");
        temp = StringMethods.textualSubstitution(temp, "$%%W%%$", "este");
        temp = StringMethods.textualSubstitution(temp, "$%%U%%$", "abajo");
        temp = StringMethods.textualSubstitution(temp, "$%%D%%$", "arriba");
        temp = StringMethods.textualSubstitution(temp, "$%%I%%$", "fuera");
        temp = StringMethods.textualSubstitution(temp, "$%%O%%$", "dentro");
        return temp;
    }

    public Path(World mundo, org.w3c.dom.Node n) throws XMLtoWorldException {
        this.mundo = mundo;
        if (!(n instanceof org.w3c.dom.Element)) {
            throw (new XMLtoWorldException("Path node not Element"));
        } else {
            peerable = false;
            isStandard = false;
            org.w3c.dom.Element e = (org.w3c.dom.Element) n;
            try {
                if (!e.hasAttribute("destination")) throw (new XMLtoWorldException("Destination attribute missing at path. Node is " + n));
                destination = Integer.valueOf(e.getAttribute("destination")).intValue();
            } catch (NumberFormatException nfe) {
                int dest = mundo.roomNameToID(e.getAttribute("destination"));
                if (dest >= 0) {
                    destination = dest;
                } else {
                    throw (new XMLtoWorldException("Destination attribute invalid at path. Value found is " + "\"" + e.getAttribute("destination") + "\""));
                }
            }
            try {
                if (!e.hasAttribute("exitTime")) exitTime = 30; else exitTime = Byte.valueOf(e.getAttribute("exitTime")).byteValue();
            } catch (NumberFormatException nfe) {
                throw (new XMLtoWorldException("exitTime attribute invalid at path"));
            }
            if (e.hasAttribute("peerable")) peerable = Boolean.valueOf(e.getAttribute("peerable")).booleanValue();
            if (e.hasAttribute("standard")) isStandard = Boolean.valueOf(e.getAttribute("standard")).booleanValue();
            readPropListFromXML(mundo, n);
            org.w3c.dom.NodeList descrListNodes = e.getElementsByTagName("DescriptionList");
            if (descrListNodes.getLength() > 0) {
                org.w3c.dom.Element descrListNode = (org.w3c.dom.Element) descrListNodes.item(0);
                org.w3c.dom.NodeList descrNodes = descrListNode.getElementsByTagName("Description");
                descriptionList = new Description[descrNodes.getLength()];
                for (int i = 0; i < descrNodes.getLength(); i++) {
                    org.w3c.dom.Element descrNode = (org.w3c.dom.Element) descrNodes.item(i);
                    try {
                        descriptionList[i] = new Description(mundo, descrNode);
                    } catch (XMLtoWorldException xe) {
                        throw (new XMLtoWorldException("Error at path description: " + xe.getMessage()));
                    }
                }
            }
            if (descriptionList == null) descriptionList = new Description[0];
            org.w3c.dom.NodeList cmdListNodes = e.getElementsByTagName("CommandList");
            if (cmdListNodes.getLength() > 0) {
                org.w3c.dom.Element cmdListNode = (org.w3c.dom.Element) cmdListNodes.item(0);
                org.w3c.dom.NodeList cmdNodes = cmdListNode.getElementsByTagName("Command");
                exitCommand = new String[cmdNodes.getLength()];
                for (int i = 0; i < cmdNodes.getLength(); i++) {
                    org.w3c.dom.Element cmdNode = (org.w3c.dom.Element) cmdNodes.item(i);
                    if (cmdNode.hasAttribute("name")) exitCommand[i] = cmdNode.getAttribute("name"); else throw (new XMLtoWorldException("Error at path: exit command without a name"));
                }
            }
            org.w3c.dom.NodeList keyListNodes = e.getElementsByTagName("KeyList");
            if (keyListNodes.getLength() > 0) {
                org.w3c.dom.Element keyListNode = (org.w3c.dom.Element) keyListNodes.item(0);
                org.w3c.dom.NodeList keyInvList = keyListNode.getElementsByTagName("Inventory");
                if (keyInvList.getLength() > 0) {
                    org.w3c.dom.Element keyInvElt = (org.w3c.dom.Element) keyInvList.item(0);
                    try {
                        keys = new Inventory(mundo, keyInvElt);
                    } catch (XMLtoWorldException xe) {
                        throw (new XMLtoWorldException("Error at path key ID's inventory: " + xe.getMessage()));
                    }
                } else keys = new Inventory(10000, 10000);
            }
            org.w3c.dom.NodeList assocItemNodes = e.getElementsByTagName("AssociatedItem");
            if (assocItemNodes.getLength() > 0) {
                org.w3c.dom.Element assocItemNode = (org.w3c.dom.Element) assocItemNodes.item(0);
                if (assocItemNode.hasAttribute("id")) {
                    associatedItem = mundo.getItem(assocItemNode.getAttribute("id"));
                } else {
                    throw (new XMLtoWorldException("Error at path, associated item has no attribute named id."));
                }
            }
        }
    }

    public org.w3c.dom.Node getXMLRepresentation(org.w3c.dom.Document doc, String standardExitNameAttr) {
        org.w3c.dom.Node n = getXMLRepresentation(doc);
        if (n instanceof org.w3c.dom.Element) ((org.w3c.dom.Element) n).setAttribute("direction", standardExitNameAttr);
        return n;
    }

    public org.w3c.dom.Node getXMLRepresentation(org.w3c.dom.Document doc) {
        org.w3c.dom.Element suElemento = doc.createElement("Path");
        suElemento.setAttribute("destination", String.valueOf(destination));
        suElemento.setAttribute("exitTime", String.valueOf(exitTime));
        suElemento.setAttribute("peerable", String.valueOf(peerable));
        suElemento.setAttribute("standard", String.valueOf(isStandard));
        suElemento.appendChild(getPropListXMLRepresentation(doc));
        org.w3c.dom.Element listaDesc = doc.createElement("DescriptionList");
        for (int i = 0; i < descriptionList.length; i++) {
            Description nuestraDescripcion = descriptionList[i];
            listaDesc.appendChild(nuestraDescripcion.getXMLRepresentation(doc));
        }
        suElemento.appendChild(listaDesc);
        if (exitCommand != null) {
            org.w3c.dom.Element listaCmd = doc.createElement("CommandList");
            for (int i = 0; i < exitCommand.length; i++) {
                String comandoSalida = exitCommand[i];
                org.w3c.dom.Element elementoComando = doc.createElement("Command");
                elementoComando.setAttribute("name", exitCommand[i]);
                listaCmd.appendChild(elementoComando);
            }
            suElemento.appendChild(listaCmd);
        }
        if (keys != null) {
            org.w3c.dom.Element eltLlaves = doc.createElement("KeyList");
            org.w3c.dom.Node listaLlaves = keys.getXMLRepresentation(doc);
            eltLlaves.appendChild(listaLlaves);
            suElemento.appendChild(eltLlaves);
        }
        if (associatedItem != null) {
            org.w3c.dom.Element item = doc.createElement("AssociatedItem");
            item.setAttribute("id", String.valueOf(associatedItem.getID()));
            suElemento.appendChild(item);
        }
        return suElemento;
    }

    public int getID() {
        return -1;
    }
}
