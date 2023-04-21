package com.trapezium.vrml;

import com.trapezium.parse.TokenEnumerator;
import com.trapezium.parse.TokenFactory;
import com.trapezium.vrml.node.PROTO;
import com.trapezium.vrml.node.PROTObase;
import com.trapezium.vrml.node.PROTOInstance;
import com.trapezium.vrml.node.DEFUSENode;
import com.trapezium.vrml.node.Node;
import com.trapezium.vrml.node.NodeType;
import com.trapezium.vrml.node.TokenData;
import com.trapezium.util.ReturnInteger;
import com.trapezium.vrml.grammar.VRML97;
import com.trapezium.vrml.grammar.DEFNameFactory;
import com.trapezium.vrml.visitor.DEFVisitor;
import com.trapezium.vrml.visitor.NodeSelectionVisitor;
import com.trapezium.vrml.grammar.VRML97parser;
import com.trapezium.vorlon.ErrorSummary;
import com.trapezium.parse.InputStreamFactory;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.io.InputStream;

/**
 *  Scene graph component representing an entire VRML file, or the
 *  body of a PROTO.
 *
 *  The only difference between the PROTO body and a Scene is that
 *  the PROTO body is required to have at least one node.  This
 *  restriction is not part of the Scene, and is a check performed
 *  by the grammar package during parsing.
 *
 *  The Scene has a single Declaration for each child element.
 *
 *  The Scene gives the name scope for PROTO and DEF nodes.  As a file is processed, any
 *  PROTO and DEF nodes are registered with the Scene.  Name uniqueness is forced during
 *  this registration by appending "_1", "_2", etc.
 *
 *  @author          Johannes N. Johannsen
 *  @version         1.1, 14 Jan 1998
 *
 *  @since           1.0
 */
public class Scene extends MultipleTokenElement implements SelectNode {

    /** The Scene tokens exist only in relation to a specific TokenEnumerator kept by all Scenes */
    TokenEnumerator dataSource = null;

    /** Set the data source for this Scene's text */
    public void setTokenEnumerator(TokenEnumerator t) {
        dataSource = t;
    }

    /** Get the data source for this Scene's text */
    public TokenEnumerator getTokenEnumerator() {
        return (dataSource);
    }

    /** Can get count from ComplexityVisitor and set here */
    int vrmlElementCount = 0;

    public void setVrmlElementCount(int n) {
        vrmlElementCount = n;
    }

    public int getVrmlElementCount() {
        return (vrmlElementCount);
    }

    /** ErrorSummary info, possibly null */
    ErrorSummary errorSummary;

    public ErrorSummary getErrorSummary() {
        return (errorSummary);
    }

    public void setErrorSummary(ErrorSummary errorSummary) {
        this.errorSummary = errorSummary;
    }

    /** List of nodes verified, created if necessary */
    Hashtable verifyList;

    public Hashtable getVerifyList() {
        if (verifyList == null) {
            verifyList = new Hashtable();
        }
        return (verifyList);
    }

    /** PROTO nodes by type */
    public Hashtable protoTable = null;

    /** PROTO node declarations available to Scene, identified by PROTO name */
    public Hashtable PROTONodes = null;

    /** DEF/USE nodes scope is limited to file */
    public Hashtable DEFNodes = null;

    /** list of all ROUTEs, used to check for duplicates */
    public Hashtable routeTable = null;

    /** add a ROUTE to the list kept by this Scene */
    public void addRoute(String sourceDEF, String sourceField, String destDEF, String destField) {
        if ((sourceDEF != null) && (sourceField != null) && (destDEF != null) && (destField != null)) {
            if (routeTable == null) {
                routeTable = new Hashtable();
            }
            String routeText = sourceDEF + '.' + sourceField + '_' + destDEF + '.' + destField;
            routeTable.put(routeText, routeText);
        }
    }

    /** Check if a ROUTE is already known to this Scene */
    public boolean hasRoute(String sourceDEF, String sourceField, String destDEF, String destField) {
        if (routeTable == null) {
            return (false);
        }
        if ((sourceDEF != null) && (sourceField != null) && (destDEF != null) && (destField != null)) {
            String routeText = sourceDEF + '.' + sourceField + '_' + destDEF + '.' + destField;
            return (routeTable.get(routeText) != null);
        }
        return (false);
    }

    Hashtable usageTable = null;

    public Hashtable getUsageTable() {
        if (usageTable == null) {
            usageTable = new Hashtable();
        }
        return (usageTable);
    }

    Hashtable coordTable = null;

    public Hashtable getCoordTable() {
        if (coordTable == null) {
            coordTable = new Hashtable();
        }
        return (coordTable);
    }

    Hashtable texCoordTable = null;

    public Hashtable getTexCoordTable() {
        if (texCoordTable == null) {
            texCoordTable = new Hashtable();
        }
        return (texCoordTable);
    }

    Hashtable colorTable = null;

    public Hashtable getColorTable() {
        if (colorTable == null) {
            colorTable = new Hashtable();
        }
        return (colorTable);
    }

    Hashtable normalTable = null;

    public Hashtable getNormalTable() {
        if (normalTable == null) {
            normalTable = new Hashtable();
        }
        return (normalTable);
    }

    /** A Scene has a PROTO parent if it is embedded within a PROTO node */
    PROTO protoParent = null;

    public PROTO getPROTOparent() {
        return (protoParent);
    }

    public void setPROTOparent(PROTO rent) {
        protoParent = rent;
    }

    /** for the autoDEF feature */
    DEFNameFactory defNameFactory;

    /** main url */
    String url;

    /** Construct a scene entirely from a url.
	 *
	 *  @param urlName url to use in constructing scene
	 */
    public Scene(String urlName) {
        super(-1);
        try {
            InputStream is = InputStreamFactory.getInputStream(urlName);
            if (is != null) {
                TokenEnumerator vrmlTokens = new TokenEnumerator(is, urlName);
                init(urlName, vrmlTokens, null);
                VRML97parser parser = new VRML97parser();
                parser.Build(vrmlTokens, this);
            }
        } catch (Exception e) {
        }
    }

    public Scene() {
        this(null, null, null);
    }

    public Scene(String urlName, TokenEnumerator te) {
        this(urlName, te, null);
    }

    /** Main constructor, all other constructors go through this one
	 *
	 *  @param urlName url used to identify the scene
	 *  @param te TokenEnumerator containing text used to create the Scene
	 *  @param defNameFactory optional DEFNameFactory for autoDEF feature
	 */
    static int staticSceneId = 1;

    int sceneId;

    public Scene(String urlName, TokenEnumerator te, DEFNameFactory defNameFactory) {
        super(-1);
        init(urlName, te, defNameFactory);
    }

    void init(String urlName, TokenEnumerator te, DEFNameFactory defNameFactory) {
        sceneId = staticSceneId++;
        this.url = urlName;
        this.dataSource = te;
        this.defNameFactory = defNameFactory;
    }

    /** Each scene assigned an id number as it is created */
    public int getSceneId() {
        return (sceneId);
    }

    /** Get the url used to identify the scene */
    public String getUrl() {
        return (url);
    }

    /** Get the DEFNameFactory associated with the Scene */
    public DEFNameFactory getDEFNameFactory() {
        return (defNameFactory);
    }

    /** Set the DEFNameFactory for the Scene */
    public void setDEFNameFactory(DEFNameFactory defNameFactory) {
        this.defNameFactory = defNameFactory;
    }

    /** untitled scenes */
    public static int untitledCount = 0;

    public static String getUntitle() {
        untitledCount++;
        return ("Untitled" + untitledCount + ".wrl");
    }

    /** optional text header used by display */
    String text;

    public void setText(String t) {
        text = t;
    }

    public String getText() {
        return (text);
    }

    /** How many node classes, including PROTOs, exist for a particular actual type */
    public int getClassCount(String typeString) {
        String[] typeList = (String[]) NodeType.typeTable.get(typeString);
        if (typeList != null) {
            System.out.println("Count for nodeType '" + typeString + "' is " + typeList.length);
            return (typeList.length);
        } else {
            System.out.println("Count for actualType '" + typeString + "' is 0");
            return (0);
        }
    }

    /** Get a string listing current classes, including protos */
    public String getClassList(String typeString) {
        StringBuffer result = new StringBuffer("NULL");
        String[] typeList = (String[]) NodeType.typeTable.get(typeString);
        if (typeList != null) {
            for (int i = 0; i < typeList.length; i++) {
                result.append(",");
                result.append(typeList[i]);
            }
        }
        return (new String(result));
    }

    /** create DEF node hashtable if it doesn't already exist */
    void createDEFtable() {
        if (DEFNodes == null) {
            DEFNodes = new Hashtable();
        }
    }

    /** register DEF node in Scene.  This is done as file is processed so at this
	 *  point names are changed to enforce uniqueness.
	 */
    public void registerDEF(DEFUSENode def) {
        deregisterDEF(def);
        DEFNodes.put(def.getId(), def);
    }

    /** deregister DEF node in Scene, used before a rename */
    public void deregisterDEF(DEFUSENode def) {
        createDEFtable();
        if (DEFNodes.get(def.getId()) != null) {
            DEFNodes.remove(def.getId());
        }
    }

    /** just reserve the String of a DEF */
    public void registerDEF(String defName) {
        createDEFtable();
        DEFNodes.put(defName, "");
    }

    /** get a def node given a "USE" */
    public DEFUSENode getDEF(DEFUSENode use) {
        createDEFtable();
        DEFUSENode d = (DEFUSENode) DEFNodes.get(use.getId());
        return (d);
    }

    /** lookup a DEF node given an id */
    public DEFUSENode getDEF(String id) {
        if (DEFNodes != null) {
            return ((DEFUSENode) DEFNodes.get(id));
        } else {
            return (null);
        }
    }

    /** is there a DEF node with a given name */
    public boolean DEFexists(String id) {
        if (DEFNodes != null) {
            return (DEFNodes.get(id) != null);
        } else {
            return (false);
        }
    }

    public Hashtable getDEFtable() {
        return (DEFNodes);
    }

    /**
	 *  Register PROTO node in Scene.
	 */
    public void registerPROTO(PROTObase proto) {
        if (proto.getBuiltInNodeType() == null) {
        } else if (NodeType.typeTable.get(proto.getBuiltInNodeType()) != null) {
            if (protoTable == null) {
                protoTable = new Hashtable();
            }
            Vector vec = (Vector) protoTable.get(proto.getBuiltInNodeType());
            if (vec == null) {
                vec = new Vector();
                protoTable.put(proto.getBuiltInNodeType(), vec);
            }
            vec.addElement(proto);
        }
        if (PROTONodes == null) {
            PROTONodes = new Hashtable();
        } else if (PROTONodes.get(proto.getId()) != null) {
            PROTONodes.remove(proto.getId());
        }
        PROTONodes.put(proto.getId(), proto);
    }

    /**
	 *  Get the built in node name.  First check for the name in the PROTO nodes, and if
	 *  found, use the proto to determine the built in node name.  Otherwise, assume it is
	 *  a built in node, and use that name.
	 */
    public String getBuiltInNodeName(String name) {
        if (PROTONodes != null) {
            PROTObase p = (PROTObase) PROTONodes.get(name);
            if (p != null) {
                return (p.getBuiltInNodeType());
            }
        }
        if (parent != null) {
            Scene s = (Scene) parent.getScene();
            if (s != null) {
                return (s.getBuiltInNodeName(name));
            }
        }
        return (name);
    }

    /** get the PROTO declaration associated with a PROTO name.
     * If not found in this scene, and this scene is contained within another
     * scene (i.e. this scene itself is contained in a PROTO), check its parent
     * scene for the PROTO also.
     */
    public PROTObase getPROTO(String name) {
        PROTObase result = null;
        if (PROTONodes != null) {
            result = (PROTObase) PROTONodes.get(name);
        }
        if (result == null) {
            if (parent != null) {
                Scene s = (Scene) parent.getScene();
                if (s != null) {
                    return (s.getPROTO(name));
                }
            }
        }
        return (result);
    }

    /** Create an instance of a particular PROTO */
    public PROTOInstance PROTOFactory(String PROTOName) {
        PROTObase pb = getPROTO(PROTOName);
        if (pb == null) {
            return (null);
        } else {
            return (new PROTOInstance(pb));
        }
    }

    /** Is there a PROTO with the given name */
    public boolean isPROTO(String name) {
        return (getPROTO(name) != null);
    }

    /** template method, overrides VrmlElement.isScene(), used for finding
     *  Scene that contains a particular VrmlElement.
     */
    public boolean isScene() {
        return (true);
    }

    /**
	 *  Get the first node type in the Scene.  When Scene is contained in a PROTO,
	 *  this first type is the the actual type for PROTO nodes.
	 */
    public String getFirstNodeType() {
        if (children != null) {
            int nChildren = numberChildren();
            for (int i = 0; i < nChildren; i++) {
                VrmlElement e = getChildAt(i);
                if (e instanceof Node) {
                    Node n = (Node) e;
                    if (n instanceof DEFUSENode) {
                        DEFUSENode dun = (DEFUSENode) n;
                        n = n.getNode();
                        if (n == null) {
                            return (null);
                        }
                    }
                    return (n.getBaseName());
                }
            }
        }
        return (null);
    }

    /** Get the name of an existing PROTO that is the closest match with
	 *  the unknown PROTO name provided.
	 */
    public String getClosestMatch(String protoString, ReturnInteger result) {
        return (VRML97.getClosestMatch(protoString, PROTONodes, result));
    }

    /** Add a ROUTE to the scene.  If the ROUTE has DEFs that conflict with
     *  previously existing DEFs in this scene, then the Scene may have
     *  re DEFfed those Nodes to eliminate the conflict (if the Scene was
     *  constructed with a DEFNameFactory).  In this case, the ROUTE DEF 
     *  names are renamed in exactly the same way.  Otherwise, the ROUTE
     *  names are preserved, which may result in a parsing error if the
     *  DEF names they refer to do not exist.
     *
     *  @param route  ROUTE from another scene graph
     *  @return newly added ROUTE
     */
    public ROUTE addROUTE(ROUTE route) {
        StringBuffer routeString = new StringBuffer();
        Scene s = (Scene) route.getScene();
        TokenEnumerator dataSource = s.getTokenEnumerator();
        Hashtable mapper = getNameMapper(s);
        for (int i = route.getFirstTokenOffset(); i <= route.getLastTokenOffset(); i++) {
            String sourceString = dataSource.toString(i);
            if (mapper != null) {
                if (sourceString.indexOf(".") > 0) {
                    StringTokenizer st = new StringTokenizer(sourceString, ".");
                    String oldName = st.nextToken();
                    String newName = (String) mapper.get(oldName);
                    if (newName != null) {
                        sourceString = new String(newName + "." + st.nextToken());
                    }
                }
            }
            routeString.append(sourceString);
            if (i < route.getLastTokenOffset()) {
                routeString.append(" ");
            }
        }
        return (addROUTE(new String(routeString)));
    }

    /** Add a ROUTE from a String
     *
     *  @param route  String form of ROUTE
     *  @return newly added ROUTE
     */
    public ROUTE addROUTE(String routeString) {
        TokenEnumerator dataDestination = getTokenEnumerator();
        int newTokenOffset = dataDestination.getNumberTokens();
        dataDestination.addLine(routeString, new TokenFactory());
        dataDestination.setState(newTokenOffset);
        ROUTE newROUTE = new ROUTE(newTokenOffset, dataDestination);
        RouteSource rs = new RouteSource(dataDestination.getNextToken(), dataDestination, this);
        newROUTE.addChild(rs);
        newROUTE.addChild(new TO(dataDestination.getNextToken(), dataDestination));
        RouteDestination rd = new RouteDestination(dataDestination.getNextToken(), dataDestination, this);
        newROUTE.addChild(rd);
        newROUTE.setLastTokenOffset(rd.getLastTokenOffset());
        addChild(newROUTE);
        setLastTokenOffset(newROUTE.getLastTokenOffset());
        return (newROUTE);
    }

    /** Add a Node to the scene.
     *
     *  @param node Node, possibly from another scene graph, to add to this scene
     *  @return newly added Node
     */
    public Node addNode(Node node) {
        DEFResolver defResolver = createDEFNames(node);
        TokenData sceneTokenData = new TokenData(this);
        TokenData nodeTokenData = new TokenData(node, TokenData.ReCreate);
        sceneTokenData.insert(nodeTokenData);
        Node newNode = nodeTokenData.getNode();
        addChild(newNode);
        setLastTokenOffset(newNode.getLastTokenOffset());
        if (defResolver != null) {
            defResolver.resolve(newNode);
        }
        return (newNode);
    }

    /** Add a Node to the Scene.
     *
     *  @param sourceNode node in String form
     *  @return newly added Node
     */
    public Node addNode(String sourceNode) {
        TokenData newTokenData = new TokenData(sourceNode, defNameFactory);
        return (addNode(newTokenData.getNode()));
    }

    /** table of source Scenes for node copies */
    Hashtable sourceScenes = null;

    /** Rename conflicting DEFs with new names using DEFNameFactory.
     *
     *  @param node  Node with possibly conflicting DEFs being added to Scene
     *
     *  @return DEFResolver object that has all info necessary to resolve
     *     conflicting DEFs after new node gets created
     */
    public DEFResolver createDEFNames(Node node) {
        if (defNameFactory != null) {
            DEFVisitor defVisitor = new DEFVisitor();
            node.traverse(defVisitor);
            int defCount = defVisitor.getNumberDEFs();
            Scene s = (Scene) node.getScene();
            if (s != null) {
                for (int i = 0; i < defCount; i++) {
                    if (DEFexists(defVisitor.getDEF(i))) {
                        DEFUSENode dun = getDEF(defVisitor.getDEF(i));
                        if ((dun != null) && (dun.getNode() != null)) {
                            resolveDEF(s, defVisitor.getDEF(i), defNameFactory.createDEFName(dun.getNode().getBaseName()));
                        }
                    }
                }
                if (defCount > 0) {
                    return (new DEFResolver(this, s));
                }
            }
        }
        return (null);
    }

    /** Resolve a DEF name conflict, generate a new name for the DEF.
     *
     *  @param scene source Scene for original Node
     *  @param originalName original DEF name
     *  @param newName new name as generated by DEFNameFactory
     */
    void resolveDEF(Scene scene, String originalName, String newName) {
        if (sourceScenes == null) {
            sourceScenes = new Hashtable();
        }
        Hashtable mapper = (Hashtable) sourceScenes.get(scene);
        if (mapper == null) {
            mapper = new Hashtable();
            sourceScenes.put(scene, mapper);
        }
        mapper.put(originalName, newName);
    }

    /** Get the name mapper for a particular source Scene. */
    public Hashtable getNameMapper(Scene scene) {
        if (sourceScenes == null) {
            return (null);
        } else {
            return ((Hashtable) sourceScenes.get(scene));
        }
    }

    /** Select a node, SelectNode interface */
    public boolean selectNode(NodeSelection nodeSelection) {
        if ((nodeSelection.startLine == -1) || (nodeSelection.endLine == -1)) {
            return (false);
        }
        if ((nodeSelection.startColumn == -1) || (nodeSelection.endColumn == -1)) {
            return (false);
        }
        NodeSelectionVisitor nsv = new NodeSelectionVisitor(dataSource, nodeSelection);
        traverse(nsv);
        return (nsv.updateNodeSelection());
    }
}
