package org.xmlcml.cml.legacy2cml.molecule.msds;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Nodes;
import org.xmlcml.cml.base.CMLRuntimeException;
import org.xmlcml.cml.element.CMLFormula;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLParameter;
import org.xmlcml.cml.element.CMLProperty;
import org.xmlcml.cml.element.CMLScalar;
import org.xmlcml.cml.element.CMLFormula.Type;
import org.xmlcml.cml.legacy2cml.AbstractHTMLConverter;
import org.xmlcml.cml.legacy2cml.AbstractLegacyConverter;
import org.xmlcml.cml.legacy2cml.RDFConverter;

/**
 * This class handles conversion of MSDS documents as supplied by
 * Oxford University
 * @author Peter Murray-Rust
 * 
 */
public class ICSCConverter extends AbstractHTMLConverter {

    static final Logger logger = Logger.getLogger(ICSCConverter.class.getName());

    static final String PREFIX = "ICSC";

    /** gets prefix for RDF triple abbreviation
     * @return prefix
     */
    protected String getPrefix() {
        return PREFIX;
    }

    static {
        logger.setLevel(Level.ALL);
    }

    protected AbstractLegacyConverter getNewLegacyConverter(AbstractLegacyConverter abstractLegacyConverter) {
        if (!(abstractLegacyConverter instanceof ICSCConverter)) {
            throw new CMLRuntimeException("can only copy ICSCConverter");
        }
        return new ICSCConverter((AbstractLegacyConverter) abstractLegacyConverter);
    }

    private static Map<String, String> propertyMap;

    static {
        propertyMap = new HashMap<String, String>();
        propertyMap.put("Auto-ignition temperature", "cml:autoIgnition");
        propertyMap.put("Boiling point", "cml:bpt");
        propertyMap.put("Boiling point (decomposes)", "cml:bptDecomp");
        propertyMap.put("Density", "cml:density");
        propertyMap.put("Flash point", "cml:flashPoint");
        propertyMap.put("Melting point", "cml:mpt");
        propertyMap.put("Melting point (decomposes)", "cml:mptDecomp");
        propertyMap.put("Octanol/water partition coefficient as log Pow", "cml:logPow");
        propertyMap.put("Relative density (water = 1)", "cml:relativeDensity");
        propertyMap.put("Relative vapour density (air = 1)", "cml:relativeVapourDensity");
        propertyMap.put("Solubility in water", "cml:waterSolubility");
        propertyMap.put("Sublimation point", "cml:sublimationPoint");
        propertyMap.put("Relative density of the vapour/air-mixture at 20�C (air = 1)", S_EMPTY);
        propertyMap.put("Explosive limits, vol% in air", S_EMPTY);
    }

    /** constructor.
	 * 
	 * @param converter
	 */
    public ICSCConverter(AbstractLegacyConverter converter) {
        super(converter);
    }

    /** constructor.
	 */
    public ICSCConverter() {
        super();
    }

    /**
     * @param inputStream
     * @param fileId
     * @throws Exception
     * @return doc (null if parsing fails)
     */
    public Document parseLegacy(InputStream inputStream, String fileId) throws Exception {
        Document doc = createDocFromHTML(inputStream, fileId);
        parsedDocumentAsCML = null;
        if (doc != null) {
            CMLMolecule molecule = createMolecule(doc);
            molecule.setId(fileId);
            parsedDocumentAsCML = new Document(molecule);
            molecule.setConvention(this.getPrefix());
        }
        return parsedDocumentAsCML;
    }

    /**
	 * @param document
	 * @return mol
	 */
    protected CMLMolecule createMolecule(Document document) {
        molecule = new CMLMolecule();
        createAndAddMetadata(molecule, RDFConverter.DC_SOURCE, this.getSource());
        Element body = (Element) document.query("//body").get(0);
        Nodes nodes = body.query("table[1]");
        if (nodes.size() > 0) {
            Element table = (Element) nodes.get(0);
            names(molecule, table);
            nodes = table.query("tr[3]");
            if (nodes.size() > 0) {
                Element tr3 = (Element) nodes.get(0);
                Element table3 = (Element) tr3.query("td[1]/table[1]").get(0);
                identifiers(molecule, table3);
                formula(molecule, table3);
                molecularMass(molecule, table3);
                physicalProperties(molecule, body);
            }
        }
        return molecule;
    }

    /**
	 * @param molecule
	 * @param table
	 * @throws CMLRuntimeException
	 */
    private void names(CMLMolecule molecule, Element table) throws CMLRuntimeException {
        String title = table.query("tr[1]/td[1]/table[1]/tr[1]/td[1]/b").get(0).getValue();
        createAndAddName(molecule, title.toLowerCase());
        Nodes nodes = table.query("tr[2]/td[1]/table[1]/tr[2]/td[2]");
        if (nodes.size() > 0) {
            Element names = (Element) nodes.get(0);
            Nodes nameNodes = names.query("text()");
            for (int i = 0; i < nameNodes.size(); i++) {
                String nameS = nameNodes.get(i).getValue().trim();
                if (nameS.length() > 0) {
                    createAndAddSynonym(molecule, nameNodes.get(i).getValue().toLowerCase());
                }
            }
        }
    }

    /**
	 * @param molecule
	 * @param table3
	 */
    private void identifiers(CMLMolecule molecule, Element table3) {
        addIdentifier(molecule, "CAS", table3, "tr[1]/td[2]");
        addIdentifier(molecule, "RTECS", table3, "tr[2]/td[2]");
        addIdentifier(molecule, "UN", table3, "tr[3]/td[2]");
        addIdentifier(molecule, "EINECS", table3, "tr[4]/td[2]");
    }

    /**
	 * @param molecule
	 * @param table3
	 */
    private void formula(CMLMolecule molecule, Element table3) {
        Nodes nodes = table3.query("tr[1]/td[3]");
        if (nodes.size() > 0) {
            String formulaS = nodes.get(0).getValue();
            String[] formulas = formulaS.split("/");
            try {
                CMLFormula formula = CMLFormula.createFormula(formulas[0], Type.ANY);
                molecule.addFormula(formula);
            } catch (Exception e) {
                System.err.println("Cannot create formula for (" + fileId + ") for " + formulas[0]);
            }
        }
    }

    /**
	 * @param molecule
	 * @param table3
	 * @throws NumberFormatException
	 * @throws CMLRuntimeException
	 */
    private void molecularMass(CMLMolecule molecule, Element table3) throws NumberFormatException, CMLRuntimeException {
        Nodes nodes = table3.query("tr[2]/td[3]");
        if (nodes.size() > 0) {
            String mwS = nodes.get(0).getValue();
            String[] mws = mwS.split(":");
            try {
                double mwt = new Double(mws[1]).doubleValue();
                createAndAddProperty(molecule, "dict:molarMass", mwt, "cml:dalton");
            } catch (NumberFormatException e) {
                System.err.println("Bad molecular mass for (" + fileId + "): " + mwS);
            }
        }
    }

    /**
	 * @param molecule
	 * @param body
	 */
    private void physicalProperties(CMLMolecule molecule, Element body) {
        Nodes nodes = body.query("table[tr[1]/td/b[.='IMPORTANT DATA']]");
        if (nodes.size() > 0) {
            Element table2 = (Element) nodes.get(0);
            Nodes props = table2.query("tr[td[1]/b[.='PHYSICAL PROPERTIES']]");
            if (props.size() > 0) {
                Element physTitle = (Element) props.get(0);
                int idx = indexOfElementChild(table2, physTitle);
                Element physprop = table2.getChildElements().get(idx + 1);
                Nodes td = physprop.query(".//td/text()");
                for (int i = 0; i < td.size(); i++) {
                    try {
                        addProperty(molecule, td.get(i).getValue().trim());
                    } catch (CMLRuntimeException e) {
                        System.err.println("Bad property: " + e);
                    }
                }
            }
        }
    }

    private void addProperty(CMLMolecule molecule, String s) {
        if (s.indexOf(S_COLON) != -1 && !s.endsWith(S_COLON)) {
            String[] split = s.split(S_COLON);
            String title = split[0].trim();
            title = title.replace(S_NEWLINE, S_SPACE);
            String value = split[1].trim();
            String dictRef = propertyMap.get(title);
            if (dictRef == null) {
                complexProperty(molecule, title, value);
            } else if (dictRef.equals(S_EMPTY)) {
            } else {
                createAndAddGuessedPropertyTypeAndUnits(molecule, dictRef, value);
            }
        } else if (s.length() > 0) {
            System.err.println("Cannot interpret as property for (" + fileId + "): " + s);
        }
    }

    private void complexProperty(CMLMolecule molecule, String title, String value) {
        String dictRef = null;
        double parameterValue = Double.NaN;
        String parameterUnits = null;
        String parameterDictRef = null;
        String units = null;
        title = title.trim();
        value = value.trim();
        if (title.startsWith("Boiling point at ")) {
            Pattern pattern = Pattern.compile("Boiling point at (\\d*\\.\\d*)\\s*(\\S+)");
            Matcher matcher = pattern.matcher(title);
            if (matcher.matches()) {
                parameterUnits = "units:" + matcher.group(2);
                parameterDictRef = "cml:pressure";
                try {
                    parameterValue = new Double(matcher.group(1)).doubleValue();
                    dictRef = "cml:bpt";
                } catch (NumberFormatException n) {
                }
            }
        } else if (title.startsWith("Solubility in water, ")) {
            Pattern pattern = Pattern.compile("Solubility in water, (.*) at (\\d+\\.?\\s*)\\s*�C");
            dictRef = "cml:waterSolubility";
            Matcher matcher = pattern.matcher(title);
            if (matcher.matches()) {
                units = matcher.group(1);
                try {
                    parameterValue = new Double(matcher.group(2)).doubleValue();
                    parameterUnits = "units:celsius";
                    parameterDictRef = "cml:temperature";
                } catch (NumberFormatException n) {
                }
            } else {
                units = title.substring("Solubility in water, ".length()).trim();
            }
            units = units.replace(S_SPACE, S_EMPTY);
            units = units.replace(S_SLASH, "_per_");
            units = "units:" + units;
        } else if (title.startsWith("Vapour pressure, ")) {
            Pattern pattern = Pattern.compile("Vapour pressure\\, (.*) at (\\d+\\.?\\d*)\\s*�C");
            dictRef = "cml:vaporPressure";
            Matcher matcher = pattern.matcher(title);
            if (matcher.matches()) {
                units = "units:" + matcher.group(1);
                try {
                    parameterValue = new Double(matcher.group(2)).doubleValue();
                    parameterUnits = "units:celsius";
                    parameterDictRef = "cml:temperature";
                } catch (NumberFormatException n) {
                }
            } else {
                throw new CMLRuntimeException("cannot parse: " + title);
            }
        } else {
            System.err.println("Unknown property :" + title + ":");
        }
        if (dictRef != null) {
            CMLProperty property = createAndAddProperty(molecule, dictRef, value);
            property.getScalarElements().get(0).setUnits(units);
            if (!Double.isNaN(parameterValue)) {
                CMLParameter parameter = new CMLParameter();
                CMLScalar scalar = new CMLScalar(parameterValue);
                scalar.setUnits(parameterUnits);
                parameter.addScalar(scalar);
                property.appendChild(parameter);
            }
        }
    }

    private void addIdentifier(CMLMolecule molecule, String convention, Element parent, String query) {
        Nodes nodes = parent.query(query);
        if (nodes.size() > 0) {
            String value = nodes.get(0).getValue();
            if (!value.trim().equals("")) {
                createAndAddIdentifier(convention, molecule, value);
            }
        }
    }

    /**
     * Provides a command line and graphical user interface to PTCLConverter.
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            System.exit(0);
        }
        ICSCConverter icsc = new ICSCConverter();
        icsc.processArgs1(args);
    }

    /**
	 * @param args
	 */
    private void processArgs1(String[] args) {
        int i = 0;
        while (i < args.length) {
            if (args[i].equalsIgnoreCase("-FOO")) {
                i++;
            } else {
                i = this.processArgs(args, i);
            }
        }
        try {
            this.runIterator();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("Exception: " + e);
        }
    }

    /**
	 * 
	 */
    protected static void usage() {
        System.out.println("Usage: org.xmlcml.legacy.molecule.msds.ICSCConverter [options]");
        System.out.println("       -FOO (does nothing)");
        AbstractHTMLConverter.usage();
    }
}

;
