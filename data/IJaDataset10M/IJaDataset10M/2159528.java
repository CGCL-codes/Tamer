package unbbayes.io.mebn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StreamTokenizer;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import unbbayes.io.mebn.exceptions.IOMebnException;
import unbbayes.prs.Node;
import unbbayes.prs.mebn.ContextNode;
import unbbayes.prs.mebn.DomainMFrag;
import unbbayes.prs.mebn.InputNode;
import unbbayes.prs.mebn.MFrag;
import unbbayes.prs.mebn.MultiEntityBayesianNetwork;
import unbbayes.prs.mebn.OrdinaryVariable;
import unbbayes.prs.mebn.ResidentNode;
import unbbayes.prs.mebn.entity.ObjectEntity;
import unbbayes.prs.mebn.entity.ObjectEntityInstance;
import unbbayes.prs.mebn.entity.ObjectEntityInstanceOrdereable;
import unbbayes.prs.mebn.entity.exception.ObjectEntityHasInstancesException;
import unbbayes.prs.mebn.entity.exception.TypeException;
import unbbayes.util.Debug;

/**
 * <p>Title: UnBBayes</p>
 * <p>Description: Ubf file format manipulator. </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: UnB</p>
 * @author Shou Matsumoto (cardialfly@[yahoo|gmail].com)
 * @version 0.1
 * @since 01/05/2007
 * @see unbbayes.io.mebn.resources.IoUbfResources
 */
public class UbfIO implements MebnIO {

    private static final String prowlExtension = "owl";

    private ResourceBundle resource = ResourceBundle.getBundle("unbbayes.io.mebn.resources.IoUbfResources");

    private PrOwlIO prowlIO = null;

    private String[][] tokens = { { "CommentInitiator", "%" }, { "ArgumentSeparator", "," }, { "AttributionSeparator", "=" }, { "Quote", "\"" }, { "VersionDeclarator", "Version" }, { "PrOwlFileDeclarator", "PrOwl" }, { "MTheoryDeclarator", "MTheory" }, { "NextMFragDeclarator", "NextMFrag" }, { "NextResidentDeclarator", "NextResident" }, { "NextInputDeclarator", "NextInput" }, { "NextContextDeclarator", "NextContext" }, { "NextEntityDeclarator", "NextEntity" }, { "MFragDeclarator", "MFrag" }, { "NextOrdinalVarDeclarator", "NextOrdinaryVar" }, { "NodeDeclarator", "Node" }, { "TypeDeclarator", "Type" }, { "PositionDeclarator", "Position" }, { "SizeDeclarator", "Size" }, { "NextArgumentDeclarator", "NextArgument" }, { "DomainResidentType", "DomainResidentNode" }, { "GenerativeInputType", "GenerativeInputNode" }, { "ContextType", "ContextNode" }, { "OrdinalVarType", "OrdinalVar" }, { "ObjectEntityDeclarator", "ObjEntity" }, { "EntityInstancesDeclarator", "Instances" } };

    public static final double ubfVersion = 0.03;

    public static final String fileExtension = "ubf";

    private UbfIO() {
        super();
        this.prowlIO = new PrOwlIO();
        Debug.setDebug(true);
    }

    /**
	 * Construction method for UbfIO
	 * @return UbfIO instance
	 */
    public static UbfIO getInstance() {
        return new UbfIO();
    }

    private MFrag searchMFrag(String name, MultiEntityBayesianNetwork mebn) {
        for (Iterator iter = mebn.getDomainMFragList().iterator(); iter.hasNext(); ) {
            MFrag element = (MFrag) iter.next();
            if (name.compareTo(element.getName()) == 0) {
                return element;
            }
        }
        return null;
    }

    private Node searchNode(String name, DomainMFrag mfrag) {
        for (Iterator iter = mfrag.getResidentNodeList().iterator(); iter.hasNext(); ) {
            Node element = (Node) iter.next();
            if (name.compareTo(element.getName()) == 0) {
                return element;
            }
        }
        for (Iterator iter = mfrag.getInputNodeList().iterator(); iter.hasNext(); ) {
            Node element = (Node) iter.next();
            if (name.compareTo(element.getName()) == 0) {
                return element;
            }
        }
        for (Iterator iter = mfrag.getContextNodeList().iterator(); iter.hasNext(); ) {
            Node element = (Node) iter.next();
            if (name.compareTo(element.getName()) == 0) {
                return element;
            }
        }
        for (Iterator iter = mfrag.getOrdinaryVariableList().iterator(); iter.hasNext(); ) {
            Node element = (Node) iter.next();
            if (name.compareTo(element.getName()) == 0) {
                return element;
            }
        }
        return null;
    }

    private double readVersion(StreamTokenizer st) throws IOException {
        while (st.nextToken() != st.TT_EOF) {
            if ((st.ttype == st.TT_WORD) && (this.getToken("VersionDeclarator").compareTo(st.sval) == 0)) {
                if (st.nextToken() == st.TT_NUMBER) {
                    return st.nval;
                } else {
                    break;
                }
            }
        }
        throw new IOException(resource.getString("InvalidSyntax"));
    }

    private String readOwlFile(StreamTokenizer st) throws IOException {
        while (st.nextToken() != st.TT_EOF) {
            if ((st.ttype == st.TT_WORD) && (this.getToken("PrOwlFileDeclarator").compareTo(st.sval) == 0)) {
                if (st.nextToken() != st.TT_EOL) {
                    return st.sval;
                } else {
                    break;
                }
            }
        }
        throw new IOException(resource.getString("InvalidSyntax"));
    }

    private void updateMTheory(StreamTokenizer st, MultiEntityBayesianNetwork mebn) throws IOException {
        while (st.nextToken() != st.TT_EOF) {
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("MFragDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                break;
            }
            if (this.getToken("ObjectEntityDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                break;
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("MTheoryDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextMFragDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mebn.setDomainMFragNum((int) st.nval);
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextResidentDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mebn.setDomainResidentNodeNum((int) st.nval);
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextInputDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mebn.setGenerativeInputNodeNum((int) st.nval);
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextContextDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mebn.setContextNodeNum((int) st.nval);
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextEntityDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mebn.setEntityNum((int) st.nval);
                        break;
                    }
                }
            }
        }
    }

    private void updateMFrag(StreamTokenizer st, MultiEntityBayesianNetwork mebn) throws IOException, ClassCastException {
        MFrag mfrag = null;
        while (st.nextToken() != st.TT_EOF) {
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("ObjectEntityDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                break;
            }
            if (this.getToken("MFragDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_WORD) {
                        mfrag = this.searchMFrag(st.sval, mebn);
                        break;
                    }
                }
            }
            if (mfrag == null) {
                continue;
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NextOrdinalVarDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        mfrag.setOrdinaryVariableNum((int) st.nval);
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NodeDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                try {
                    this.updateNode(st, (DomainMFrag) mfrag);
                } catch (ClassCastException e) {
                    throw new ClassCastException(resource.getString("MFragTypeException"));
                }
            }
        }
    }

    private void updateObjectEntities(StreamTokenizer st, MultiEntityBayesianNetwork mebn) throws IOException, ClassCastException {
        ObjectEntity objectEntity = null;
        while (st.nextToken() != st.TT_EOF) {
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("ObjectEntityDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_WORD) {
                        objectEntity = mebn.getObjectEntityContainer().getObjectEntityByName(st.sval);
                        break;
                    }
                }
            }
            if (objectEntity == null) {
                continue;
            } else {
                try {
                    objectEntity.setOrdereable(true);
                } catch (ObjectEntityHasInstancesException e) {
                    e.printStackTrace();
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("EntityInstancesDeclarator").compareTo(st.sval) == 0) {
                ObjectEntityInstanceOrdereable prev = null;
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_WORD) {
                        String name = st.sval;
                        try {
                            ObjectEntityInstanceOrdereable oe = (ObjectEntityInstanceOrdereable) objectEntity.addInstance(name);
                            mebn.getObjectEntityContainer().addEntityInstance(oe);
                            oe.setPrev(prev);
                            if (prev != null) prev.setProc(oe);
                            prev = oe;
                        } catch (TypeException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void updateNode(StreamTokenizer st, DomainMFrag mfrag) throws IOException {
        Node node = null;
        while (st.nextToken() != st.TT_EOF) {
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("MFragDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                break;
            }
            if (this.getToken("ObjectEntityDeclarator").compareTo(st.sval) == 0) {
                st.pushBack();
                break;
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("NodeDeclarator").compareTo(st.sval) == 0) {
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_WORD) {
                        node = this.searchNode(st.sval, mfrag);
                        break;
                    }
                }
            }
            if (node == null) {
                continue;
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("PositionDeclarator").compareTo(st.sval) == 0) {
                int posx = 15;
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        posx = (int) st.nval;
                        while (st.nextToken() != st.TT_EOL) {
                            if (st.ttype == st.TT_NUMBER) {
                                node.setPosition(posx, (int) st.nval);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            if (st.ttype != st.TT_WORD) {
                continue;
            }
            if (this.getToken("SizeDeclarator").compareTo(st.sval) == 0) {
                double width = 100;
                while (st.nextToken() != st.TT_EOL) {
                    if (st.ttype == st.TT_NUMBER) {
                        width = st.nval;
                        while (st.nextToken() != st.TT_EOL) {
                            if (st.ttype == st.TT_NUMBER) {
                                node.setSize(width, st.nval);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
	 * Convert a token type to .ubf syntax. Search for UBF syntax.
	 * @param token identifier to be searched
	 * @return string representing that entity in .ubf syntax
	 * @see this.tokens
	 */
    protected String getToken(String s) throws IllegalArgumentException {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i][0].compareTo(s) == 0) {
                return tokens[i][1];
            }
        }
        throw new IllegalArgumentException(this.resource.getString("InvalidSyntax"));
    }

    /**
	 * Sets a token value. Use it only if you want to change UBF synax in runtime.
	 * @param s: token identifier to be searched
	 * @param newValue: new value for s
	 * @return string representing that entity in .ubf syntax
	 * @see this.tokens
	 */
    protected void setToken(String s, String newValue) throws IllegalArgumentException {
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i][0].compareTo(s) == 0) {
                tokens[i][1] = newValue;
            }
        }
        throw new IllegalArgumentException(this.resource.getString("InvalidSyntax"));
    }

    public MultiEntityBayesianNetwork loadMebn(File file) throws IOException, IOMebnException {
        MultiEntityBayesianNetwork mebn = null;
        String owlFilePath = file.getPath().substring(0, file.getPath().lastIndexOf(this.fileExtension)) + prowlExtension;
        File prowlFile = null;
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new FileReader(file)));
        st.wordChars('A', 'z');
        st.wordChars('.', '.');
        st.whitespaceChars(this.getToken("ArgumentSeparator").charAt(0), this.getToken("ArgumentSeparator").charAt(0));
        st.whitespaceChars(this.getToken("AttributionSeparator").charAt(0), this.getToken("AttributionSeparator").charAt(0));
        st.whitespaceChars(' ', ' ');
        st.whitespaceChars('\t', '\t');
        st.quoteChar('"');
        st.quoteChar('\'');
        st.eolIsSignificant(true);
        st.commentChar(this.getToken("CommentInitiator").charAt(0));
        double version = this.readVersion(st);
        if (this.ubfVersion < version) {
            throw new IOMebnException(resource.getString("IncompatibleVersion"));
        }
        try {
            owlFilePath = this.readOwlFile(st);
        } catch (IOException e) {
            throw e;
        }
        owlFilePath = file.getParentFile().getCanonicalPath().concat("/" + owlFilePath);
        Debug.println("Opening .owl file: " + owlFilePath);
        try {
            prowlFile = new File(owlFilePath);
            if (!prowlFile.exists()) {
                throw new IOException(this.resource.getString("NoProwlFound"));
            }
            mebn = this.prowlIO.loadMebn(prowlFile);
        } catch (Exception e) {
            throw new IOException(e.getLocalizedMessage() + " : " + this.resource.getString("InvalidProwlScheme"));
        }
        try {
            this.updateMTheory(st, mebn);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage() + " : " + resource.getString("MTheoryConfigError"));
        }
        try {
            this.updateMFrag(st, mebn);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage() + " : " + resource.getString("MFragConfigError"));
        }
        updateObjectEntities(st, mebn);
        return mebn;
    }

    public void saveMebn(File file, MultiEntityBayesianNetwork mebn) throws IOException, IOMebnException {
        String varType = null;
        String noExtensionFileName = file.getPath().substring(0, file.getPath().lastIndexOf(this.fileExtension));
        File prowlFile = new File(noExtensionFileName + prowlExtension);
        PrintStream out = new PrintStream(new FileOutputStream(file));
        try {
            this.prowlIO.saveMebn(prowlFile, mebn);
        } catch (Exception e) {
            throw new IOException(e.getLocalizedMessage() + " : " + this.resource.getString("UnknownPrOWLError"));
        }
        URI relativeURI = file.getCanonicalFile().getParentFile().toURI();
        relativeURI = relativeURI.relativize(prowlFile.toURI());
        out.println(this.getToken("CommentInitiator") + resource.getString("UBFFileHeader"));
        out.println(this.getToken("VersionDeclarator") + this.getToken("AttributionSeparator") + this.ubfVersion);
        out.println(this.getToken("PrOwlFileDeclarator") + this.getToken("AttributionSeparator") + this.getToken("Quote") + relativeURI.getPath() + this.getToken("Quote"));
        out.println();
        out.println(this.getToken("CommentInitiator") + resource.getString("UBFMTheory"));
        out.println(this.getToken("MTheoryDeclarator") + this.getToken("AttributionSeparator") + mebn.getName());
        out.println(this.getToken("NextMFragDeclarator") + this.getToken("AttributionSeparator") + mebn.getDomainMFragNum());
        out.println(this.getToken("NextResidentDeclarator") + this.getToken("AttributionSeparator") + mebn.getDomainResidentNodeNum());
        out.println(this.getToken("NextInputDeclarator") + this.getToken("AttributionSeparator") + mebn.getGenerativeInputNodeNum());
        out.println(this.getToken("NextContextDeclarator") + this.getToken("AttributionSeparator") + mebn.getContextNodeNum());
        out.println(this.getToken("NextEntityDeclarator") + this.getToken("AttributionSeparator") + mebn.getEntityNum());
        out.println();
        out.println(this.getToken("CommentInitiator") + resource.getString("UBFMFragsNodes"));
        for (Iterator iter = mebn.getDomainMFragList().iterator(); iter.hasNext(); ) {
            DomainMFrag mfrag = (DomainMFrag) iter.next();
            out.println();
            out.println(this.getToken("CommentInitiator") + resource.getString("UBFMFrags"));
            out.println(this.getToken("MFragDeclarator") + this.getToken("AttributionSeparator") + mfrag.getName());
            out.println(this.getToken("NextOrdinalVarDeclarator") + this.getToken("AttributionSeparator") + mfrag.getOrdinaryVariableNum());
            if (mfrag.getResidentNodeList() != null) {
                if (!mfrag.getResidentNodeList().isEmpty()) {
                    out.println();
                    out.println(this.getToken("CommentInitiator") + resource.getString("UBFResidentNodes"));
                    out.println();
                    for (Iterator iterator = mfrag.getResidentNodeList().iterator(); iterator.hasNext(); ) {
                        ResidentNode node = (ResidentNode) iterator.next();
                        out.println();
                        out.println(this.getToken("NodeDeclarator") + this.getToken("AttributionSeparator") + node.getName());
                        out.println(this.getToken("TypeDeclarator") + this.getToken("AttributionSeparator") + this.getToken("DomainResidentType"));
                        out.println(this.getToken("PositionDeclarator") + this.getToken("AttributionSeparator") + node.getPosition().getX() + this.getToken("ArgumentSeparator") + node.getPosition().getY());
                        out.println(this.getToken("SizeDeclarator") + this.getToken("AttributionSeparator") + node.getWidth() + this.getToken("ArgumentSeparator") + node.getHeight());
                    }
                }
            }
            if (mfrag.getInputNodeList() != null) {
                if (!mfrag.getInputNodeList().isEmpty()) {
                    out.println();
                    out.println(this.getToken("CommentInitiator") + resource.getString("UBFInputNodes"));
                    out.println();
                    for (Iterator iterator = mfrag.getInputNodeList().iterator(); iterator.hasNext(); ) {
                        InputNode node = (InputNode) iterator.next();
                        out.println();
                        out.println(this.getToken("NodeDeclarator") + this.getToken("AttributionSeparator") + node.getName());
                        out.println(this.getToken("TypeDeclarator") + this.getToken("AttributionSeparator") + this.getToken("GenerativeInputType"));
                        out.println(this.getToken("PositionDeclarator") + this.getToken("AttributionSeparator") + node.getPosition().getX() + this.getToken("ArgumentSeparator") + node.getPosition().getY());
                        out.println(this.getToken("SizeDeclarator") + this.getToken("AttributionSeparator") + node.getWidth() + this.getToken("ArgumentSeparator") + node.getHeight());
                    }
                }
            }
            if (mfrag.getContextNodeList() != null) {
                if (!mfrag.getContextNodeList().isEmpty()) {
                    out.println();
                    out.println(this.getToken("CommentInitiator") + resource.getString("UBFContextNodes"));
                    out.println();
                    for (Iterator iterator = mfrag.getContextNodeList().iterator(); iterator.hasNext(); ) {
                        ContextNode node = (ContextNode) iterator.next();
                        out.println();
                        out.println(this.getToken("NodeDeclarator") + this.getToken("AttributionSeparator") + node.getName());
                        out.println(this.getToken("TypeDeclarator") + this.getToken("AttributionSeparator") + this.getToken("ContextType"));
                        out.println(this.getToken("PositionDeclarator") + this.getToken("AttributionSeparator") + node.getPosition().getX() + this.getToken("ArgumentSeparator") + node.getPosition().getY());
                        out.println(this.getToken("SizeDeclarator") + this.getToken("AttributionSeparator") + node.getWidth() + this.getToken("ArgumentSeparator") + node.getHeight());
                    }
                }
            }
            if (mfrag.getOrdinaryVariableList() != null) {
                if (!mfrag.getOrdinaryVariableList().isEmpty()) {
                    out.println();
                    out.println(this.getToken("CommentInitiator") + resource.getString("UBFOrdinalVars"));
                    out.println();
                    for (Iterator iterator = mfrag.getOrdinaryVariableList().iterator(); iterator.hasNext(); ) {
                        OrdinaryVariable node = (OrdinaryVariable) iterator.next();
                        out.println();
                        out.println(this.getToken("NodeDeclarator") + this.getToken("AttributionSeparator") + node.getName());
                        out.println(this.getToken("TypeDeclarator") + this.getToken("AttributionSeparator") + this.getToken("OrdinalVarType"));
                        out.println(this.getToken("PositionDeclarator") + this.getToken("AttributionSeparator") + node.getPosition().getX() + this.getToken("ArgumentSeparator") + node.getPosition().getY());
                        out.println(this.getToken("SizeDeclarator") + this.getToken("AttributionSeparator") + node.getWidth() + this.getToken("ArgumentSeparator") + node.getHeight());
                    }
                }
            }
        }
        out.println();
        out.println(this.getToken("CommentInitiator") + resource.getString("UBFObjectEntityInstances"));
        for (ObjectEntity objectEntity : mebn.getObjectEntityContainer().getListEntity()) {
            if (objectEntity.isOrdereable()) {
                out.println();
                out.println(this.getToken("ObjectEntityDeclarator") + this.getToken("AttributionSeparator") + objectEntity.getName());
                out.print(this.getToken("EntityInstancesDeclarator") + this.getToken("AttributionSeparator"));
                List<ObjectEntityInstanceOrdereable> list = new ArrayList<ObjectEntityInstanceOrdereable>();
                for (ObjectEntityInstance instance : objectEntity.getInstanceList()) {
                    list.add((ObjectEntityInstanceOrdereable) instance);
                }
                String instances = "";
                for (ObjectEntityInstance instance : ObjectEntityInstanceOrdereable.ordererList(list)) {
                    instances += instance.getName() + this.getToken("ArgumentSeparator");
                }
                if (list.size() > 0) {
                    instances = instances.substring(0, instances.length() - this.getToken("ArgumentSeparator").length());
                }
                out.println(instances);
            }
        }
    }

    /**
	 * @return Returns the resource.
	 */
    public ResourceBundle getResource() {
        return resource;
    }

    /**
	 * @param resource The resource to set.
	 */
    public void setResource(ResourceBundle resource) {
        this.resource = resource;
    }
}
