package com.trapezium.vrml.visitor;

import com.trapezium.pattern.Visitor;
import com.trapezium.vrml.node.PROTObase;
import com.trapezium.vrml.node.PROTOInstance;
import com.trapezium.vrml.Scene;
import com.trapezium.vrml.ROUTE;
import com.trapezium.vrml.VrmlElement;
import com.trapezium.vrml.node.Node;
import com.trapezium.vrml.node.DEFUSENode;
import com.trapezium.vrml.grammar.DEFNameFactory;
import com.trapezium.parse.TokenEnumerator;
import java.util.Vector;
import java.util.BitSet;
import java.util.Hashtable;

/** The PROTOcollector resolves conflicts introduced when one scene graph 
 *  is merged into another.  It collects infomation about all conflicts,
 *  then provides information about those conflicts.
 *
 *  The method "resolveConflicts" resolves those which can be handled in-place.
 *  Otherwise, it has methods for extracting the information token by token.
 *  In-place conflict resolution handles DEF/USE/PROTO naming conflicts, but
 *  does not handle moving PROTOs to new locations in the file.
 *
 *  Conflict resolution is one of two ways:  the DEFNameFactory in the destination
 *  scene is used if it exists, otherwise an algorithm of appending "_1", "_2", etc.
 *  is used until a name without a conflict is found.
 */
public class PROTOcollector extends Visitor {

    Vector protoList;

    Scene visitedScene;

    Scene destinationScene;

    BitSet protoInstanceLocations;

    BitSet protoLocations;

    BitSet useLocations;

    BitSet routeLocations;

    int numberTokens;

    Hashtable protoMapTable;

    Hashtable defMapTable;

    Hashtable protoTable;

    Hashtable routeTable;

    Vector routes;

    /** class constructor
     *
     *  @param s the Scene to merge into the destination Scene
     *  @param destinationScene the Scene that is growing
     */
    public PROTOcollector(Scene s, Scene destinationScene) {
        super(s.getTokenEnumerator());
        this.visitedScene = s;
        protoList = null;
        this.destinationScene = destinationScene;
        numberTokens = dataSource.getNumberTokens();
        protoInstanceLocations = new BitSet(numberTokens);
        protoLocations = new BitSet(numberTokens);
        useLocations = new BitSet(numberTokens);
        routeLocations = new BitSet(numberTokens);
        protoMapTable = new Hashtable();
        defMapTable = new Hashtable();
        protoTable = new Hashtable();
        routeTable = new Hashtable();
    }

    /** Get the scene that is going to be modified as a result of merging into
     *  the destinationScene.
     */
    public Scene getScene() {
        return (visitedScene);
    }

    /** were any PROTOs found? */
    public boolean hasPROTOs() {
        return (protoList != null);
    }

    /** How many PROTOs were found */
    public int getNumberPROTOs() {
        if (protoList != null) {
            return (protoList.size());
        } else {
            return (0);
        }
    }

    /** Get a particular PROTO */
    public PROTObase getPROTO(int offset) {
        return ((PROTObase) protoList.elementAt(offset));
    }

    /** Visit an object in the Scene, save PROTO, DEF/USE, and ROUTE info
     *  for possible conflict resolution.
     */
    public boolean visitObject(Object a) {
        if (a instanceof PROTObase) {
            savePROTO((PROTObase) a);
        } else if (a instanceof PROTOInstance) {
            savePROTOinstance((PROTOInstance) a);
        } else if (a instanceof DEFUSENode) {
            saveDEFUSENode((DEFUSENode) a);
        } else if (a instanceof ROUTE) {
            saveROUTE((ROUTE) a);
        }
        return (true);
    }

    /** Save a ROUTE, if either its source or dest object name is remapped
     *  according to the defMapTable, redo the token.
     */
    void saveROUTE(ROUTE route) {
        int offset = route.getFirstTokenOffset();
        routeLocations.set(offset);
        routeTable.put(new Integer(offset), route);
        String sourceObject = route.getSourceDEFname();
        String destObject = route.getDestDEFname();
        String remapSource = (String) defMapTable.get(sourceObject);
        String remapDest = (String) defMapTable.get(destObject);
        if (remapSource != null) {
            VrmlElement root = route.getRoot();
            if (root instanceof Scene) {
                Scene sroot = (Scene) root;
                TokenEnumerator te = sroot.getTokenEnumerator();
                VrmlElement v = route.getChildAt(0);
                te.replace(v.getFirstTokenOffset(), remapSource + "." + route.getSourceFieldName());
            }
        }
        if (remapDest != null) {
            VrmlElement root = route.getRoot();
            if (root instanceof Scene) {
                Scene sroot = (Scene) root;
                TokenEnumerator te = sroot.getTokenEnumerator();
                VrmlElement v = route.getChildAt(2);
                te.replace(v.getFirstTokenOffset(), remapDest + "." + route.getDestFieldName());
            }
        }
        if (routes == null) {
            routes = new Vector();
        }
        routes.addElement(route);
    }

    /** Check if any ROUTEs were saved */
    public boolean hasROUTEs() {
        return (routes != null);
    }

    /** Get the number of ROUTEs saved */
    public int getNumberROUTEs() {
        return (routes.size());
    }

    /** Get a specific ROUTE that was saved */
    public ROUTE getROUTE(int offset) {
        return ((ROUTE) routes.elementAt(offset));
    }

    /** Save a DEFUSENode.  If it is a DEF, it is renamed if its name
     *  conflicts with one in the destination scene.  The DEF is also
     *  registered with the destination scene to allow detection of
     *  other conflicts.  If it is a USE, it is renamed if the corresponding
     *  DEF was renamed.
     */
    void saveDEFUSENode(DEFUSENode dun) {
        if (dun.isDEF()) {
            String defName = dun.getDEFName();
            if (destinationScene.getDEF(defName) == null) {
                destinationScene.registerDEF(dun);
            } else {
                DEFNameFactory dnf = destinationScene.getDEFNameFactory();
                if (dnf == null) {
                    for (int i = 1; i < 100; i++) {
                        String s = defName + "_" + i;
                        if (destinationScene.getDEF(s) == null) {
                            defMapTable.put(defName, s);
                            dun.setDEFName(s);
                            destinationScene.registerDEF(dun);
                            return;
                        }
                    }
                } else {
                    String nodeName = "unknown";
                    Node n = dun.getNode();
                    if (n != null) {
                        nodeName = n.getNodeName();
                    }
                    String s = dnf.createDEFName(nodeName);
                    defMapTable.put(defName, s);
                    dun.setDEFName(s);
                    destinationScene.registerDEF(dun);
                    return;
                }
            }
        } else {
            int offset = dun.getFirstTokenOffset();
            offset = dataSource.getNextToken(offset);
            if (offset != -1) {
                useLocations.set(offset);
            }
        }
    }

    /** Save a PROTO, renaming it if its name conflicts with one
     *  in the destination scene.  The PROTO is also registered in
     *  the destination scene, since this is its ultimate destination,
     *  so that conflicts between this and others merging into the
     *  same destination are detected.
     */
    void savePROTO(PROTObase proto) {
        if (protoList == null) {
            protoList = new Vector();
        }
        protoList.addElement(proto);
        int offset = proto.getFirstTokenOffset();
        protoTable.put(new Integer(offset), proto);
        if (offset != -1) {
            protoLocations.set(offset);
        }
        String protoId = proto.getId();
        if (destinationScene.getPROTO(protoId) == null) {
            destinationScene.registerPROTO(proto);
        } else {
            for (int i = 1; i < 100; i++) {
                String s = protoId + "_" + i;
                if (destinationScene.getPROTO(s) == null) {
                    protoMapTable.put(protoId, s);
                    proto.setId(s);
                    destinationScene.registerPROTO(proto);
                    return;
                }
            }
        }
    }

    /** Save a PROTOInstance, if the PROTO declaration has a naming conflict
     *  all PROTOInstances must be renamed.  This method saves the location of
     *  a PROTOInstance in case this is necessary.
     */
    void savePROTOinstance(PROTOInstance a) {
        int offset = a.getFirstTokenOffset();
        if (offset != -1) {
            protoInstanceLocations.set(offset);
        }
    }

    int currentToken = -1;

    /** Start scanning tokens in the scene to merge */
    public void scanTokens() {
        currentToken = 0;
    }

    /** Check if there are more tokens to access in the scene to merge */
    public boolean hasMoreTokens() {
        return (currentToken < numberTokens);
    }

    /** Is there a PROTO instance at a particular token offset that is renamed due
     *  to a PROTO declaration conflict.
     *
     *  @param tokenOffset the offset to check
     *
     *  @return true if the token offset is the first token in a PROTO instance,
     *    and that PROTO instance has a corresponding PROTO declaration which
     *    has been renamed due to a conflict with another PROTO declartion in the
     *    destination Scene.
     */
    public boolean protoIsRemapped(int tokenOffset) {
        return (protoInstanceLocations.get(tokenOffset));
    }

    /** Is there a USE node at a particular token offset that is renamed due to
    *   a DEF declaration conflict.
    *
    *  @param tokenOFfset the offset to check
    *
    *  @return true if the token offset is the first token in a USE node, and that
    *    USE node has a corresponding DEF that has been renamed due to a conflict
    *    with another DEF in the destination Scene.
    */
    public boolean useIsRemapped(int tokenOffset) {
        return (useLocations.get(tokenOffset));
    }

    /** Get the name for a remapped PROTO.
     *
     *  @param tokenOffset the offset of the PROTO
     *
     *  @return the new name for the PROTO, or original name if the tokenOffset parameter
     *     does not indicate a remapped PROTO (this latter case should not occur
     *     normally)
     */
    public String remapProto(int tokenOffset) {
        return (remap(tokenOffset, protoMapTable));
    }

    /** Get the name for a remapped USE node.
     *
     *  @param tokenOffset the offset for the USE node
     *
     *  @return the new name for the USE, or original name if the tokenOffset parameter
     *     does not indicate a remapped USE (this latter case should not occur
     *     normally)
     */
    public String remapUse(int tokenOffset) {
        return (remap(tokenOffset, defMapTable));
    }

    /** Generic remapping of Strings based on token offset.
     *
     *  @param tokenOffset the offset used as a key into the remapping table
     *  @param table the remapping table
     *
     *  @return the remapped name, based on the contents of the remapping table.
     */
    String remap(int tokenOffset, Hashtable table) {
        String original = dataSource.toString(tokenOffset);
        String remap = (String) table.get(original);
        if (remap == null) {
            return (original);
        } else {
            return (remap);
        }
    }

    /** Get the next token offset */
    public int getNextToken() {
        currentToken++;
        while (protoLocations.get(currentToken) || routeLocations.get(currentToken)) {
            if (protoLocations.get(currentToken)) {
                PROTObase p = (PROTObase) protoTable.get(new Integer(currentToken));
                if (p != null) {
                    currentToken = p.getLastTokenOffset();
                    currentToken++;
                    if (currentToken >= numberTokens) {
                        return (-1);
                    }
                }
            } else if (routeLocations.get(currentToken)) {
                ROUTE r = (ROUTE) routeTable.get(new Integer(currentToken));
                if (r != null) {
                    currentToken = r.getLastTokenOffset();
                    currentToken++;
                    if (currentToken >= numberTokens) {
                        return (-1);
                    }
                }
            }
        }
        return (currentToken);
    }
}
