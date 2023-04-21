package org.sbfc.converter.sbml2dot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.sbfc.converter.sbml2dot.ConstraintFileReader;
import org.sbml.jsbml.Annotation;
import org.sbml.jsbml.CVTerm;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.SBase;

/**
 * Enable to get a shape in a given format of a {@link SBase} SBML element. The constructor has to be defined with the name of 
 * the converter and then the function getShape can be called.
 * To add others converters/formats, add the option in FORMAT and a mapping function.
 * It is also possible to add new classes in the constraint file.
 * 
 * This class :
 * - gets a SBGN class through MIRIAM annotations or SBOterm and the sbo2sbgn.properties file.
 * - maps the SBGN-ML classes into another language or format using a property file (there is one per language or format). 
 * 
 * Available formats : dot, sbgnml
 * 
 * @author jalowcki
 *
 */
public class SBGNUtils {

    private static Logger logger = Logger.getLogger(SBGNUtils.class);

    private String[] FORMATS = new String[2];

    private String conversionFormatOutput;

    private static Properties properties = readProperties();

    private static ArrayList<String> tree = buildTree();

    public SBGNUtils(String conversionFormatOutput) {
        FORMATS[0] = "dot";
        FORMATS[1] = "sbgnml";
        this.conversionFormatOutput = conversionFormatOutput;
    }

    public static Hashtable<String, String> speciesAnnotationMap = new Hashtable<String, String>();

    /**
	 * Get the SBGN class of a glyph by reading the annotations. Return null if no SBGN class has been found 
	 * or if the given {@link Annotation} was null.
	 * @param {@link Annotation}
	 * @return sbgnClass
	 */
    public static String getSBGNClassThroughAnnotation(Annotation annotation) {
        String sbgnClass = null;
        if (annotation == null) {
            logger.warn("ShapeUtils : getSBGNClassThroughAnnotation : annotation string is null !");
            return sbgnClass;
        }
        if (annotation.getNumCVTerms() > 0) {
            for (CVTerm cvTerm : annotation.getListOfCVTerms()) {
                if (cvTerm.isBiologicalQualifier() && cvTerm.getBiologicalQualifierType().equals(CVTerm.Qualifier.BQB_HAS_PART)) {
                    return "complex";
                }
                Integer i = 0;
                String biopaxPhysicalEntityType = null;
                String previousSBGNClass = null;
                boolean canCompare = false;
                for (String uri : cvTerm.getResources()) {
                    i += 1;
                    logger.debug("Number of uri : " + i.toString() + " " + uri);
                    if (uri.matches("^#.*")) {
                        continue;
                    }
                    if (speciesAnnotationMap.containsKey(uri)) {
                        logger.debug("entire uri recognized");
                        biopaxPhysicalEntityType = speciesAnnotationMap.get(uri);
                    } else {
                        int lastIndex = uri.lastIndexOf(":");
                        String firstPartUri = uri.substring(0, lastIndex);
                        logger.debug("firstPartUri : " + firstPartUri);
                        if (speciesAnnotationMap.containsKey(firstPartUri)) {
                            biopaxPhysicalEntityType = speciesAnnotationMap.get(firstPartUri);
                            logger.debug("parsed annotation recognized is " + biopaxPhysicalEntityType);
                        }
                    }
                    if (biopaxPhysicalEntityType == null || biopaxPhysicalEntityType.trim().length() == 0) {
                        logger.warn("Warning : cannot find a valid class for this uri");
                        continue;
                    }
                    sbgnClass = biopaxTosbgnClass(biopaxPhysicalEntityType);
                    if (canCompare && (sbgnClass != previousSBGNClass)) {
                        logger.warn("Warning: annotations give different SBGN-ML class");
                    }
                    previousSBGNClass = sbgnClass;
                    canCompare = true;
                }
                sbgnClass = biopaxTosbgnClass(biopaxPhysicalEntityType);
            }
        }
        return sbgnClass;
    }

    /**
	 * For a given {@link SBase} element, return the SBGN-ML class of the element or of this nearest parent. The SBGN-ML class returned 
	 * will have a meaning in terms of SBGN graphical representation.
	 * SBGN-ML classes relevant for SBGN representation are stored in the properties file.
	 * @param sBase, properties
	 * @return sboClass
	 */
    public static String getSBGNClassThroughSboterm(SBase sBase) {
        if (sBase.isSetSBOTerm()) {
            int sboTermInt = sBase.getSBOTerm();
            String sboTerm = sBase.getSBOTermID();
            if (!SBO.checkTerm(sboTerm)) {
                logger.warn("Warning : Bad sboTerm : " + sboTerm);
            } else {
                String sboClass = properties.getProperty(sboTerm);
                if (sboClass == null) {
                    for (String parent : tree) {
                        int parentSboTerm = SBO.stringToInt(parent);
                        if (SBO.isChildOf(sboTermInt, parentSboTerm)) {
                            return properties.getProperty(parent);
                        }
                    }
                } else {
                    return sboClass;
                }
            }
        }
        return null;
    }

    /**
	 * Return the corresponding sbo {@link String} from a given SBO class. Checked classes are only relevant for SBGN-ML.
	 * If not found, null is returned.
	 * @return
	 */
    public static Integer getSBOTermThroughSBGNClass(String SBGNClass) {
        Iterator<Object> itKey = properties.values().iterator();
        while (itKey.hasNext()) {
            String key = (String) itKey.next();
            String value = properties.getProperty(key);
            if (value.equals(SBGNClass)) {
                return SBO.stringToInt(key);
            }
        }
        return null;
    }

    /**
	 * This function reads "sbo2sbgn.properties" file and return the associated {@link Properties} object.
	 * The file has to be located near the SBGNUtils file class.Return {@link IOException} otherwise
	 * @return {@link Properties}
	 */
    public static Properties readProperties() {
        Properties properties = new Properties();
        try {
            properties.load(SBGNUtils.class.getResourceAsStream("sbo2sbgn.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
	 * Return the nearest SBGN class for the given {@link SBase} element. Check the sboTerm first to get this class, annotations otherwise.
	 * Return null if no class available.
	 * @param {@link SBase}
	 * @return SBGNclass or null
	 */
    public static String getSBGNClass(SBase sBase) {
        String sbgnClassBySboTerm = getSBGNClassThroughSboterm(sBase);
        logger.debug("for the reaction " + sBase.getSBOTermID() + " sbgnClassBySboTerm is = " + sbgnClassBySboTerm);
        String sbgnClassByAnnotation = getSBGNClassThroughAnnotation(sBase.getAnnotation());
        logger.debug("SBGN class by the annotation is = " + sbgnClassByAnnotation);
        if (sbgnClassBySboTerm != null) {
            if (!sbgnClassBySboTerm.equals(sbgnClassByAnnotation)) {
                logger.warn("Warning: for MetaID: " + sBase.getMetaId() + " shape for sboTerm is " + sbgnClassBySboTerm + " instead of " + sbgnClassByAnnotation + " for annotations");
            }
            return sbgnClassBySboTerm;
        }
        if (sbgnClassByAnnotation != null) {
            return sbgnClassByAnnotation;
        } else {
            return null;
        }
    }

    /**
	 * Return for a bioPax type the associated SBGN class.
	 * @param biopaxType
	 * @return sbgnClass
	 */
    public static String biopaxTosbgnClass(String biopaxType) {
        HashMap<String, String> link = new HashMap<String, String>(5);
        link.put("complex", "complex");
        link.put("protein", "macromolecule");
        link.put("rna", "macromolecule");
        link.put("dna", "macromolecule");
        link.put("smallMolecule", "simple chemical");
        String sbgnClass = link.get(biopaxType);
        return sbgnClass;
    }

    /**
	 * Read the .properties file and build a {@link ArrayList} where all the SBO ontology is sorted as a tree.
	 * The {@link ArrayList} returned is an easy way to browse all SBOterms by keeping their order as in the properties file.
	 * @return tree or null if the file does not exist.
	 */
    private static ArrayList<String> buildTree() {
        InputStream is = SBGNUtils.class.getResourceAsStream("sbo2sbgn.properties");
        if (is == null) {
            System.out.println("The file 'sbo2sbgn.properties' was not found !!!");
            return new ArrayList<String>();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader in = new BufferedReader(isr);
        String line = "";
        ArrayList<String> tree = new ArrayList<String>(0);
        try {
            while ((line = in.readLine()) != null) {
                String[] sboTab = line.split("=", 1);
                if (!line.startsWith("#") && !line.equals("") && sboTab.length == 2) {
                    String correctSboTerm = sboTab[0].replace("\\:", ":");
                    if (SBO.checkTerm(correctSboTerm)) {
                        tree.add(correctSboTerm);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tree;
    }

    /**
	 * This method returns a shape regarding the selected format. This result is based on a SBGN shape retrieval.
	 * However null will be returned if the converter is wrongly define.
	 * Null can be put as defaultSBGNClass but therefore null will be returned if the process fails, defaultShape is a security.
	 * @param {@link SBase}
	 * @param defaultShape
	 * @return shape
	 */
    public String getOutputFromClass(SBase sBase, String defaultSBGNClass) {
        String sbgnClass = getSBGNClass(sBase);
        if (sbgnClass == null) {
            sbgnClass = defaultSBGNClass;
            if (defaultSBGNClass == null) {
                logger.info("Shape for metaId: " + sBase.getMetaId() + " cannot be given.");
                return null;
            }
        }
        if (sBase.getClass().toString().equals("class org.sbml.jsbml.Reaction")) {
            String regex = "^consumption$|^production$";
            if (defaultSBGNClass.matches(regex)) {
                if (!sbgnClass.matches(regex)) {
                    sbgnClass = defaultSBGNClass;
                }
            }
            regex = "^modulation$|(^necessary |^)stimulation$|^catalysis$|^inhibition$";
            if (defaultSBGNClass.matches(regex)) {
                if (!sbgnClass.matches(regex)) {
                    sbgnClass = defaultSBGNClass;
                }
            }
            regex = "(^uncertain |^omitted |^)process$|^association$|^dissociation$|^phenotype$";
            if (defaultSBGNClass.matches(regex)) {
                if (!sbgnClass.matches(regex)) {
                    sbgnClass = defaultSBGNClass;
                }
            }
        }
        if (conversionFormatOutput.equalsIgnoreCase("dot")) {
            HashMap<String, String> map = sbgn2DotMap();
            return map.get(sbgnClass);
        } else if (conversionFormatOutput.equalsIgnoreCase("sbgnml")) {
            return sbgnClass;
        }
        System.out.println("Warning : format " + conversionFormatOutput + " does not exist.");
        return null;
    }

    /**
	 * Return a {@link HashMap} which is mapping sbgn-ml classes and dot shapes.
	 * @param hm
	 */
    private static HashMap<String, String> sbgn2DotMap() {
        String imagePath = "src/org/sbfc/converter/sbml2dot/dotImages/";
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("macromolecule multimer", "\"rectangle\", style=rounded");
        hm.put("complex multimer", "\"octagon\"");
        hm.put("nucleic acid feature multimer", "\"ellipse\"");
        hm.put("simple chemical multimer", "\"circle\"");
        hm.put("complex", "\"octagon\"");
        hm.put("nucleic acid feature", "\"ellipse\"");
        hm.put("empty set", "plaintext, label=\"Ø\", fontsize=15,fontcolor=blue];");
        hm.put("simple chemical", "\"circle\"");
        hm.put("compartment", "\"rectangle\", style=rounded");
        hm.put("unspecified entity", "\"ellipse\"");
        hm.put("macromolecule", "\"rectangle\", style=rounded");
        hm.put("perturbation", "\"ellipse\"");
        hm.put("association", "\"circle\", style=filled, fillcolor=black");
        hm.put("dissociation", "\"doublecircle\", fixedsize=true, width=0.001, height=0.001]//");
        hm.put("submap", "\"rectangle\"");
        hm.put("omitted process", "\"square\", label=\"\\\\\\\\\" ");
        hm.put("uncertain process", "\"square\", label=\"?\"");
        hm.put("phenotype", "\"hexagon\"");
        hm.put("process", "\"square\"");
        hm.put("and", "\"circle\", label=\"AND\"");
        hm.put("or", "\"circle\", label=\"OR\"");
        hm.put("not", "\"circle\", label=\"NOT\"");
        hm.put("consumption", "\"none\"");
        hm.put("production", "\"normal\"");
        hm.put("necessary stimulation", "\"teeonormal\"");
        hm.put("catalysis", "\"odot\"");
        hm.put("stimulation", "\"onormal\"");
        hm.put("inhibition", "\"tee\"");
        hm.put("modulation", "\"odiamond\"");
        hm.put("logic arc", "\"none\"");
        return hm;
    }
}
