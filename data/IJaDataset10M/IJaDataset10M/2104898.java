package org.tolven.deploy.rxnorm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.log4j.Logger;
import org.tolven.restful.client.LoadRESTfulClient;
import org.tolven.voc.umls.rrf.columns.IMrConso;
import org.tolven.voc.umls.rrf.columns.IMrRel;
import org.tolven.voc.umls.rrf.columns.IMrSat;

/**
 * Read RXNorm RRF files and create Trim documents
 * @author John Churin
 *
 */
public class LoadRxNorm extends LoadRESTfulClient {

    String dirRXNorm;

    String dirTrim;

    String trimFileName;

    Map<String, List<String>> mapNDC;

    public static final int BATCH_SIZE = 100;

    private Logger logger = Logger.getLogger(this.getClass());

    public static class ConsoMaps extends HashMap<String, Map<String, String>> {

        public void addTTY(String tty) {
            put(tty, new HashMap<String, String>(16000));
        }
    }

    public static class RelMaps extends HashMap<String, Map<String, List<String>>> {

        public void addRELA(String rela) {
            put(rela, new HashMap<String, List<String>>(16000));
        }
    }

    ConsoMaps consoMaps = new ConsoMaps();

    RelMaps relMaps = new RelMaps();

    /**
     * Initialize the loader.
     * @param configDir
     * @param directory
     * @throws NamingException
     */
    public LoadRxNorm(String userId, char[] password, String appRestfulURL, String authRestfulURL, String dirRXNorm) {
        init(userId, password, appRestfulURL, authRestfulURL);
        this.dirRXNorm = dirRXNorm;
    }

    public String encode(String string) {
        return string;
    }

    public void loadSelectedConcepts() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dirRXNorm + "/" + "RXNCONSO.RRF")));
        String record;
        String[] fields;
        while (reader.ready()) {
            record = reader.readLine();
            fields = record.split("\\|", 19);
            Map<String, String> selection = consoMaps.get(fields[IMrConso.TTY]);
            if (selection != null) {
                selection.put(fields[IMrConso.CUI], encode(fields[IMrConso.STR]));
                logger.debug(fields[IMrConso.STR]);
            }
        }
        reader.close();
        for (String tty : consoMaps.keySet()) {
            logger.info(tty + " " + consoMaps.get(tty).size());
        }
    }

    public Map<String, List<String>> loadAttributesFor(Map<String, String> concepts, String atn) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dirRXNorm + "/" + "RXNSAT.RRF")));
        String record;
        String[] fields;
        Map<String, List<String>> attributes = new HashMap<String, List<String>>(16000);
        while (reader.ready()) {
            record = reader.readLine();
            fields = record.split("\\|", 13);
            if (atn.equals(fields[IMrSat.ATN]) && "RXNORM".equals(fields[IMrSat.SAB]) && "AUI".equals(fields[IMrSat.STYPE]) && concepts.containsKey(fields[IMrSat.CUI])) {
                if (attributes.containsKey(fields[IMrSat.CUI])) {
                    List<String> ndcs = attributes.get(fields[IMrSat.CUI]);
                    ndcs.add(fields[IMrSat.ATV]);
                } else {
                    List<String> ndcs = new ArrayList<String>();
                    ndcs.add(fields[IMrSat.ATV]);
                    attributes.put(fields[IMrSat.CUI], ndcs);
                }
                logger.debug(fields[IMrSat.CUI] + " NDC=" + fields[IMrSat.ATV]);
                logger.debug(record);
            }
        }
        reader.close();
        logger.info(atn + " Count: " + attributes.size());
        return attributes;
    }

    public static String extractBrand(String str) {
        int x = str.indexOf("[");
        return str.substring(x, str.length() - 1);
    }

    public void loadSelectedRelationships() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(dirRXNorm + "/" + "RXNREL.RRF")));
        String record;
        String[] fields;
        while (reader.ready()) {
            record = reader.readLine();
            fields = record.split("\\|", 16);
            Map<String, List<String>> selection = relMaps.get(fields[IMrRel.RELA]);
            if (selection != null) {
                if (selection.containsKey(fields[IMrRel.CUI1])) {
                    List<String> list = selection.get(fields[IMrRel.CUI1]);
                    list.add(fields[IMrRel.CUI2]);
                } else {
                    List<String> list = new ArrayList<String>();
                    list.add(fields[IMrRel.CUI2]);
                    selection.put(fields[IMrRel.CUI1], list);
                }
                logger.debug(fields[IMrConso.STR]);
            }
        }
        reader.close();
        for (String rela : relMaps.keySet()) {
            logger.info(rela + " " + relMaps.get(rela).size());
        }
    }

    /**
	 * Read RXNConso to just load up the Branded Drug Form
	 * @throws IOException 
	 */
    public void setup() throws IOException {
        consoMaps.addTTY("BN");
        consoMaps.addTTY("SCDF");
        consoMaps.addTTY("SBD");
        loadSelectedConcepts();
        relMaps.addRELA("ingredient_of");
        relMaps.addRELA("tradename_of");
        relMaps.addRELA("isa");
        loadSelectedRelationships();
    }

    /**
	 * Return a unique set of Brand names for a supplied scdf (cui)
	 * @param scdfcui
	 * @return
	 */
    public Set<String> getBrandsFor(String scdfcui) {
        Map<String, String> mapBN = consoMaps.get("BN");
        Map<String, List<String>> mapIngredient = relMaps.get("ingredient_of");
        Map<String, List<String>> mapHasTradename = relMaps.get("tradename_of");
        Map<String, List<String>> mapIsA = relMaps.get("isa");
        Set<String> brands = new HashSet<String>();
        List<String> tradenamerels = mapHasTradename.get(scdfcui);
        if (tradenamerels != null) {
            for (String tradenamerel : tradenamerels) {
                List<String> isarels = mapIsA.get(tradenamerel);
                if (isarels != null) {
                    for (String isarel : isarels) {
                        List<String> ingredientrels = mapIngredient.get(isarel);
                        if (ingredientrels != null) {
                            for (String ingredientrel : ingredientrels) {
                                String bn = mapBN.get(ingredientrel);
                                if (bn != null) {
                                    brands.add(bn);
                                }
                            }
                        }
                    }
                }
            }
        }
        return brands;
    }

    public static void writeCE(String code, String displayName, XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement("CE");
        writer.writeStartElement("displayName");
        writer.writeCharacters(displayName);
        writer.writeEndElement();
        writer.writeStartElement("code");
        writer.writeCharacters(code);
        writer.writeEndElement();
        writer.writeStartElement("codeSystem");
        writer.writeCharacters("2.16.840.1.113883.6.56");
        writer.writeEndElement();
        writer.writeStartElement("codeSystemVersion");
        writer.writeCharacters("2007AA");
        writer.writeEndElement();
        writer.writeEndElement();
    }

    public static String truncate(int maxLength, String in) {
        if (in == null || in.length() <= maxLength) return in;
        return in.substring(0, maxLength - 1);
    }

    /**
	 * Generate a single trim from the elements extracted from RxNorm 
	 * @param scdf
	 * @param trimWriter
	 * @throws XMLStreamException 
	 */
    public void generateTrim(Entry<String, String> scdf, XMLStreamWriter writer) throws XMLStreamException {
        Map<String, String> mapSBD = consoMaps.get("SBD");
        Map<String, List<String>> mapHasTradename = relMaps.get("tradename_of");
        Map<String, List<String>> mapIsA = relMaps.get("isa");
        String trimName = "medication/RXN-" + scdf.getKey();
        writer.writeStartElement("trim");
        writer.writeNamespace(null, "urn:tolven-org:trim:4.0");
        writer.writeStartElement("extends");
        writer.writeCharacters("sbadm/rqo/medication");
        writer.writeEndElement();
        writer.writeStartElement("name");
        writer.writeCharacters(trimName);
        writer.writeEndElement();
        writer.writeStartElement("description");
        writer.writeCharacters(truncate(255, scdf.getValue()));
        writer.writeEndElement();
        Set<String> bns = getBrandsFor(scdf.getKey());
        for (String brand : bns) {
            writer.writeStartElement("searchPhrase");
            writer.writeAttribute("type", "bn");
            writer.writeCharacters(brand);
            writer.writeEndElement();
        }
        List<String> tradenamerels = mapHasTradename.get(scdf.getKey());
        writer.writeStartElement("act");
        writer.writeAttribute("classCode", "OBS");
        writer.writeAttribute("modeCode", "EVN");
        writer.writeStartElement("observation");
        writer.writeStartElement("value");
        writeCE(scdf.getKey(), truncate(255, scdf.getValue()), writer);
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeStartElement("valueSet");
        writer.writeAttribute("name", "material");
        writeCE(scdf.getKey(), truncate(255, scdf.getValue()), writer);
        if (tradenamerels != null) {
            for (String tradenamerel : tradenamerels) {
                List<String> isarels = mapIsA.get(tradenamerel);
                if (isarels != null) {
                    for (String isarel : isarels) {
                        String sbd = mapSBD.get(isarel);
                        if (sbd != null) {
                            writeCE(isarel, sbd, writer);
                        }
                    }
                }
            }
        }
        writer.writeEndElement();
        writer.writeEndElement();
    }

    /**
	 * Read RxNorm and directly generate trim and upload to the server
	 * @param trimWriter
	 * @throws Exception
	 */
    public void uploadDirect() throws Exception {
        Map<String, String> mapSCDF = consoMaps.get("SCDF");
        for (Entry<String, String> scdf : mapSCDF.entrySet()) {
            StringWriter bos = new StringWriter();
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(bos);
            writer.writeStartDocument();
            generateTrim(scdf, writer);
            writer.writeEndDocument();
            writer.close();
            bos.close();
            createTrimHeader(bos.toString());
        }
        logger.info("Activating headers... ");
        activate();
    }

    /**
	 * Read intermediate file and upload to the server
	 * @param trimWriter
	 * @throws Exception
	 */
    public void uploadTrimsFile(String fileName) throws Exception {
        try {
            logger.info("Uploading RXNorm trims from " + fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            int c;
            int rowCount = 0;
            while (br.ready()) {
                rowCount++;
                if (rowCount >= getIterationLimit()) {
                    logger.info("Upload stopped early due to " + UPLOAD_LIMIT + " property being set");
                    break;
                }
                StringBuilder trimBuffer = new StringBuilder(10000);
                while (br.ready()) {
                    c = br.read();
                    trimBuffer.append(Character.toChars(c));
                    if ("<trim ".equals(trimBuffer.toString())) break;
                    if (!"<trim ".startsWith(trimBuffer.toString())) {
                        trimBuffer.delete(0, 1);
                    }
                }
                while (br.ready()) {
                    c = br.read();
                    trimBuffer.append(Character.toChars(c));
                    int len = trimBuffer.length();
                    if (len > 7 && "</trim>".equals(trimBuffer.substring(len - 7, len))) {
                        createTrimHeader(trimBuffer.toString());
                        logger.debug(trimBuffer.toString());
                        break;
                    }
                }
            }
            logger.info("Count of RxNorm Med trims uploaded: " + rowCount);
            logger.info("Now activating headers... (See server log)");
            activate();
        } finally {
            logout();
        }
    }

    /**
	 * Read RxNorm and create an intermediate file that can be used for to upload later
	 * @param trimWriter
	 * @throws Exception
	 */
    public void CreateTrimsFile(String fileName) throws Exception {
        logger.info("Create trims file: " + fileName);
        OutputStream os = new FileOutputStream(fileName);
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer = factory.createXMLStreamWriter(os);
        writer.writeStartDocument();
        writer.writeStartElement("trims");
        writer.writeAttribute("type", "rxnorm");
        Map<String, String> mapSCDF = consoMaps.get("SCDF");
        for (Entry<String, String> scdf : mapSCDF.entrySet()) {
            generateTrim(scdf, writer);
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.close();
        os.close();
    }

    /**
	 * Test the program
	 * @param args The directory containing RXNorm files and the directory containing the trim files to be
	 * created or updated.
	 * @throws Exception 
	 */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Arguments: configDirectory");
            return;
        }
    }

    public String getDirRXNorm() {
        return dirRXNorm;
    }

    public void setDirRXNorm(String dirRXNorm) {
        this.dirRXNorm = dirRXNorm;
    }

    public String getDirTrim() {
        return dirTrim;
    }

    public void setDirTrim(String dirTrim) {
        this.dirTrim = dirTrim;
    }

    public String getTrimFileName() {
        return trimFileName;
    }

    public void setTrimFileName(String trimFileName) {
        this.trimFileName = trimFileName;
    }
}
