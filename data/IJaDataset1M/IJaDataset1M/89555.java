package org.xmlcml.cml.legacy.react;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import nu.xom.Document;
import org.xmlcml.cml.base.CMLException;
import org.xmlcml.cml.base.CMLSerializer;
import org.xmlcml.cml.element.CMLAtom;
import org.xmlcml.cml.element.CMLLink;
import org.xmlcml.cml.element.CMLList;
import org.xmlcml.cml.element.CMLMap;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLProduct;
import org.xmlcml.cml.element.CMLProductList;
import org.xmlcml.cml.element.CMLReactant;
import org.xmlcml.cml.element.CMLReactantList;
import org.xmlcml.cml.element.CMLReaction;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.tools.ReactionTool;

/**
 * converts RXN format to CMLReact
 * 
 * @author Administrator
 */
public class RXNConverter {

    static final Logger logger = Logger.getLogger(RXNConverter.class.getName());

    /**
     */
    public static final String RXN = "$RXN";

    /** */
    public static final String FROM_MDL_MAPPING = "from MDL-RXN mapping";

    String id;

    int nProducts;

    int nReactants;

    CMLReaction reaction;

    CMLReactantList reactantList;

    CMLProductList productList;

    String line = null;

    String inDir = "";

    String outDir = "";

    String inSuffix = ".rxn";

    String outSuffix = ".cml";

    Document doc;

    /**
     * Constructor for the RXNConverter object
     */
    public RXNConverter() {
        this("");
    }

    /**
     * Constructor for the RXNConverter object
     * 
     * @param reactionId
     */
    public RXNConverter(String reactionId) {
        id = reactionId;
    }

    /**
     * Current version
     * 
     * @return version
     */
    public String getVersion() {
        return "V2000";
    }

    /**
     * Sets the inDir attribute of the RXNConverter object
     * 
     * @param inDir
     *            The new inDir value
     */
    public void setInDir(String inDir) {
        this.inDir = inDir;
    }

    /**
     * Gets the inDir attribute of the RXNConverter object
     * 
     * @return The inDir value
     */
    public String getInDir() {
        return this.inDir;
    }

    /**
     * Sets the outDir attribute of the RXNConverter object
     * 
     * @param outDir
     *            The new outDir value
     */
    public void setOutDir(String outDir) {
        this.outDir = outDir;
    }

    /**
     * Gets the outDir attribute of the RXNConverter object
     * 
     * @return The outDir value
     */
    public String getOutDir() {
        return this.outDir;
    }

    /**
     * does the conversion
     * 
     * @exception IOException
     * @exception CMLException
     */
    public void process() throws IOException, CMLException {
        if (!inDir.equals("") && !outDir.equals("")) {
            String userDir = System.getProperties().getProperty("user.dir");
            File inDirF = new File(inDir);
            logger.info("userDir" + userDir);
            logger.info("inDirF" + inDirF);
            File[] files = inDirF.listFiles();
            for (int i = 0; i < files.length; i++) {
                String fName = files[i].getPath();
                if (fName.endsWith(inSuffix)) {
                    logger.info("Reading" + fName);
                    int idx = fName.lastIndexOf(".");
                    String fileroot = fName.substring(0, idx);
                    idx = fileroot.lastIndexOf(File.separator);
                    String id = fileroot.substring(idx + 1);
                    String outfile = fileroot + outSuffix;
                    LineNumberReader lnr = new LineNumberReader(new FileReader(files[i]));
                    RXNConverter rxn = new RXNConverter(id);
                    doc = (Document) rxn.read(lnr);
                    logger.info("Writing To" + outfile);
                    FileOutputStream fos = new FileOutputStream(outfile);
                    CMLSerializer serializer = new CMLSerializer(fos);
                    serializer.write(doc);
                    fos.close();
                }
            }
        }
    }

    /**
     * read into document
     * 
     * @param br
     * @return document
     * @exception CMLException
     * @exception IOException
     */
    public Document read(LineNumberReader br) throws CMLException, IOException {
        return read(doc, br);
    }

    /**
     * read into document
     * 
     * @param doc
     * @param br
     * @return document
     * @exception CMLException
     * @exception IOException
     */
    public Document read(Document doc, LineNumberReader br) throws CMLException, IOException {
        this.doc = doc;
        reaction = new CMLReaction();
        doc.appendChild(reaction);
        reaction.setId(id);
        nProducts = 0;
        nReactants = 0;
        readFileHeader(br);
        if (nReactants == 0 && nProducts == 0) {
            throw new CMLException("RXN: no reactants and no products");
        }
        reactantList = new CMLReactantList();
        reaction.addReactantList(reactantList);
        productList = new CMLProductList();
        reaction.addProductList(productList);
        for (int i = 0; i < nReactants; i++) {
            CMLMolecule mol = readMolecule(br);
            String molId = (id.equals("")) ? "r" + (i + 1) : id + ".r" + (i + 1);
            mol.setId(molId);
            CMLReactant reactant = new CMLReactant();
            if (mol == null) {
                throw new CMLException("Null molecule");
            } else {
                reactant.addMolecule(mol);
            }
            reactantList.addReactant(reactant);
        }
        for (int i = 0; i < nProducts; i++) {
            CMLMolecule mol = readMolecule(br);
            String molId = (id.equals("")) ? "p" + (i + 1) : id + ".p" + (i + 1);
            mol.setId(molId);
            CMLProduct product = new CMLProduct();
            product.addMolecule(mol);
            productList.addProduct(product);
        }
        mapAtoms();
        return doc;
    }

    void mapAtoms() throws CMLException {
        List reactantAtoms = ReactionTool.getAtoms(reactantList);
        List productAtoms = ReactionTool.getAtoms(productList);
        Map<String, CMLAtom> productMap = new HashMap<String, CMLAtom>();
        for (int i = 0; i < productAtoms.size(); i++) {
            CMLScalar scalar = (CMLScalar) ((CMLAtom) productAtoms.get(i)).getFirstCMLChild("scalar");
            if (scalar != null) {
                String atomNumber = scalar.getXMLContent();
                if (productMap.containsKey(atomNumber)) {
                    throw new CMLException("Duplicate ISIS RXN atom map: " + atomNumber);
                }
                productMap.put(atomNumber, (CMLAtom) productAtoms.get(i));
            }
        }
        CMLMap cmlMap = null;
        if (productMap.size() > 0) {
            cmlMap = new CMLMap();
            reaction.appendChild(cmlMap);
            cmlMap.setTitle("from product to reactant");
        }
        if (cmlMap != null) {
            for (int i = 0; i < reactantAtoms.size(); i++) {
                CMLScalar scalar = (CMLScalar) ((CMLAtom) reactantAtoms.get(i)).getFirstCMLChild("scalar");
                if (scalar != null) {
                    String atomNumber = scalar.getXMLContent();
                    CMLAtom targetAtom = productMap.get(atomNumber);
                    if (targetAtom == null) {
                        throw new CMLException("No target for " + atomNumber + " in RXN atom map");
                    }
                    CMLLink link = new CMLLink();
                    link.setFrom(targetAtom.getId());
                    link.setTo(((CMLAtom) reactantAtoms.get(i)).getId());
                    cmlMap.addLink(link);
                    link.setTitle(FROM_MDL_MAPPING);
                    productMap.remove(atomNumber);
                }
            }
        }
    }

    /**
     * read the reaction
     * 
     * @param doc
     * @param br
     * @return reaction
     * @exception CMLException
     * @exception IOException
     */
    public CMLReaction readReaction(Document doc, LineNumberReader br) throws CMLException, IOException {
        doc = (Document) read(doc, br);
        return (CMLReaction) doc.getRootElement();
    }

    /**
     * read the reaction
     * 
     * @param br
     * @return reaction
     * @exception CMLException
     * @exception IOException
     */
    public CMLReaction readReaction(LineNumberReader br) throws CMLException, IOException {
        doc = (Document) read(br);
        return (CMLReaction) doc.getRootElement();
    }

    /**
     * read the molecule
     * 
     * @param br
     * @return molecule
     * @exception IOException
     * @exception CMLException
     */
    CMLMolecule readMolecule(LineNumberReader br) throws IOException, CMLException {
        String line = br.readLine();
        if (line == null || !line.startsWith("$MOL")) {
            throw new CMLException("RXN: expected $MOL record");
        }
        CMLMolecule mol = null;
        return mol;
    }

    /**
     * read start of file
     * 
     * @param br
     * @exception CMLException
     * @throws IOException
     */
    protected void readFileHeader(LineNumberReader br) throws IOException, CMLException {
        String line;
        line = br.readLine();
        if (line == null) {
            throw new CMLException("Empty RXN file");
        }
        if (!line.startsWith(RXN)) {
            throw new IOException("non-RXN: RXN file must start with '$RXN':" + line + ":");
        }
        line = br.readLine();
        if (line == null || !line.trim().equals("")) {
            throw new CMLException("Corrupt RXN file - expected blank line 2");
        }
        line = br.readLine();
        if (line == null) {
            throw new CMLException("Unexpected EOF RXN file 3");
        }
        line = br.readLine();
        if (line == null) {
            throw new CMLException("Unexpected EOF RXN file 4");
        }
        line = br.readLine();
        if (line == null) {
            throw new CMLException("Unexpected EOF RXN file 5");
        }
        if (line.length() < 6) {
            throw new CMLException("Corrupt RXN file 5");
        }
        try {
            nReactants = Integer.parseInt(line.substring(0, 3).trim());
            nProducts = Integer.parseInt(line.substring(3, 6).trim());
        } catch (NumberFormatException nfe) {
            throw new CMLException("Bad integers for counts in line: " + line);
        }
    }

    /**
     * outputs CML as an RXN if possible. NYI
     * 
     * @param writer
     *            to output it to
     * @return string
     * @exception CMLException
     * @exception IOException
     */
    public String output(Writer writer) throws CMLException, IOException {
        return null;
    }

    /**
     * write data NYI
     * 
     * @param writer
     * @return string
     * @throws CMLException
     * @throws IOException
     */
    public String writeData(Writer writer) throws CMLException, IOException {
        return null;
    }

    /**
     * NYI
     * 
     * @param writer
     * @param list
     * @exception CMLException
     * @exception IOException
     */
    void output(Writer writer, CMLList list) throws CMLException, IOException {
    }

    /**
     * parse into document
     * 
     * @param infile
     * @return document
     * @exception IOException
     * @exception CMLException
     */
    public static Document parse(File infile) throws IOException, CMLException {
        return (Document) new RXNConverter().read(new LineNumberReader(new FileReader(infile)));
    }

    /**
     * The main program for the RXNConverter class
     * 
     * @param args
     *            The command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.info("Usage: org.xmlcml.legacy.reaction.RXNConverter [options]");
            logger.info("        -IN inputFile (RXN assumed)");
            logger.info("        -INDIR inputFiles (RXN assumed)");
            logger.info("        -OUT outputFile (CML)");
            logger.info("        -OUTDIR inputFiles (CML assumed)");
            logger.info("        -ID reactionID");
            System.exit(0);
        }
        int i = 0;
        boolean jump = false;
        String infile = "";
        String indir = "";
        String outdir = "";
        String outfile = "";
        String reactionId = "";
        while (i < args.length) {
            if (1 == 2) {
                ;
            } else if (args[i].equalsIgnoreCase("-ID")) {
                reactionId = args[++i];
                i++;
            } else if (args[i].equalsIgnoreCase("-IN")) {
                infile = args[++i];
                i++;
            } else if (args[i].equalsIgnoreCase("-INDIR")) {
                indir = args[++i];
                i++;
            } else if (args[i].equalsIgnoreCase("-OUT")) {
                outfile = args[++i];
                i++;
            } else if (args[i].equalsIgnoreCase("-OUTDIR")) {
                outdir = args[++i];
                i++;
            } else {
                logger.severe("Unknown arg: " + args[i]);
                i++;
            }
        }
        Document doc = null;
        try {
            if (!indir.equals("") && !outdir.equals("")) {
                RXNConverter rxn = new RXNConverter();
                rxn.setInDir(indir);
                rxn.setOutDir(outdir);
                rxn.process();
                jump = true;
            } else if (!infile.equals("")) {
                LineNumberReader lnr = new LineNumberReader(new FileReader(infile));
                RXNConverter rxn = new RXNConverter(reactionId);
                doc = (Document) rxn.read(lnr);
            }
            if (!jump) {
                if (!reactionId.equals("")) {
                    CMLReaction reaction = (CMLReaction) doc.getRootElement();
                    reaction.setId(reactionId);
                }
                if (!outfile.equals("")) {
                    CMLSerializer serializer = new CMLSerializer(new FileOutputStream(outfile));
                    serializer.write(doc);
                } else {
                    ;
                }
            }
        } catch (IOException ioe) {
            logger.info("IOEXception: " + ioe);
        } catch (CMLException cmle) {
            logger.info("CMLEXception: " + cmle);
            cmle.printStackTrace();
        }
    }
}
