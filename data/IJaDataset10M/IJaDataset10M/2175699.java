package org.grobid.core.engines.patent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.SAXParserFactory;
import org.chasen.crfpp.Tagger;
import org.grobid.core.GrobidModels;
import org.grobid.core.data.BibDataSet;
import org.grobid.core.data.BiblioItem;
import org.grobid.core.data.BiblioSet;
import org.grobid.core.data.PatentItem;
import org.grobid.core.document.OPSService;
import org.grobid.core.document.PatentDocument;
import org.grobid.core.engines.AbstractParser;
import org.grobid.core.engines.CitationParser;
import org.grobid.core.exceptions.GrobidException;
import org.grobid.core.exceptions.GrobidResourceException;
import org.grobid.core.features.FeaturesVectorReference;
import org.grobid.core.lexicon.Lexicon;
import org.grobid.core.sax.PatentAnnotationSaxParser;
import org.grobid.core.sax.TextSaxParser;
import org.grobid.core.utilities.Consolidation;
import org.grobid.core.utilities.GrobidProperties;
import org.grobid.core.utilities.OffsetPosition;
import org.grobid.core.utilities.TextUtilities;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Extraction of patent and NPL references from the content body of patent document with Conditional
 * Random Fields.
 *
 * @author Patrice Lopez
 */
public class ReferenceExtractor extends AbstractParser {

    private Tagger taggerPatent = null;

    private Tagger taggerNPL = null;

    private Tagger taggerAll = null;

    private CitationParser nplParser = null;

    private PatentRefParser patentParser = null;

    private Consolidation consolidator = null;

    private String tmpPath = null;

    private String pathXML = null;

    public boolean debug = true;

    public Lexicon lexicon = Lexicon.getInstance();

    public String currentPatentNumber = null;

    public OPSService ops = null;

    private String description = null;

    public ArrayList<org.grobid.core.data.BibDataSet> resBib = null;

    private String path = null;

    private static String delimiters = " \n\t" + TextUtilities.fullPunctuations;

    public void setDocumentPath(String dirName) {
        path = dirName;
    }

    public ReferenceExtractor() {
        taggerNPL = createTagger(GrobidModels.PATENT_NPL);
        taggerAll = createTagger(GrobidModels.PATENT_ALL);
        taggerPatent = createTagger(GrobidModels.PATENT_PATENT);
    }

    /**
     * Extract all reference from the full text retrieve via OPS.
     */
    public int extractAllReferencesOPS(boolean filterDuplicate, boolean consolidate, List<PatentItem> patents, List<BibDataSet> articles) {
        try {
            if (description != null) {
                return extractAllReferencesString(description, filterDuplicate, consolidate, patents, articles);
            }
        } catch (Exception e) {
            throw new GrobidException(e);
        }
        return 0;
    }

    /**
     * Extract all reference from a patent in XML ST.36 like.
     */
    public int extractPatentReferencesXMLFile(String pathXML, boolean filterDuplicate, boolean consolidate, List<PatentItem> patents) {
        return extractAllReferencesXMLFile(pathXML, filterDuplicate, consolidate, patents, null);
    }

    /**
     * Extract all reference from an XML file in ST.36 or MAREC format.
     */
    public int extractAllReferencesXMLFile(String pathXML, boolean filterDuplicate, boolean consolidate, List<PatentItem> patents, List<BibDataSet> articles) {
        try {
            if (patents == null) {
                System.out.println("Warning patents List is null!");
            }
            TextSaxParser sax = new TextSaxParser();
            sax.setFilter("description");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setFeature("http://xml.org/sax/features/namespaces", false);
            spf.setFeature("http://xml.org/sax/features/validation", false);
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
                }
            });
            reader.setContentHandler(sax);
            InputSource input = new InputSource(pathXML);
            input.setEncoding("UTF-8");
            reader.parse(input);
            description = sax.getText();
            currentPatentNumber = sax.currentPatentNumber;
            consolidate = false;
            filterDuplicate = true;
            if (description != null) {
                return extractAllReferencesString(description, filterDuplicate, consolidate, patents, articles);
            } else return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Extract all reference from the PDF file of a patent publication.
     */
    public int extractAllReferencesPDFFile(String inputFile, boolean filterDuplicate, boolean consolidate, List<PatentItem> patents, List<BibDataSet> articles) {
        try {
            PatentDocument doc = new PatentDocument();
            int startPage = -1;
            int endPage = -1;
            pathXML = doc.pdf2xml(true, false, startPage, endPage, inputFile, tmpPath, false);
            if (pathXML == null) {
                throw new GrobidException("PDF parsing fails");
            }
            doc.setPathXML(pathXML);
            if (doc.getBlocks() == null) {
                throw new GrobidException("PDF parsing resulted in empty content");
            }
            description = doc.getAllBlocksClean(25, -1);
            if (description != null) {
                return extractAllReferencesString(description, filterDuplicate, consolidate, patents, articles);
            } else return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Extract all reference from a simple piece of text.
     */
    public int extractAllReferencesString(String text, boolean filterDuplicate, boolean consolidate, List<PatentItem> patents, List<BibDataSet> articles) {
        try {
            if ((patentParser == null) && (patents != null)) {
                patentParser = new PatentRefParser();
            }
            if ((nplParser == null) && (articles != null)) {
                nplParser = new CitationParser();
            }
            ArrayList<String> tokenizations = new ArrayList<String>();
            ArrayList<String> patentBlocks = new ArrayList<String>();
            text = text.replace("\n", " ");
            text = text.replace("\t", " ");
            text = text.replace("  ", " ");
            StringTokenizer st = new StringTokenizer(text, delimiters, true);
            int offset = 0;
            if (st.countTokens() == 0) {
                return 0;
            }
            while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                tokenizations.add(tok);
            }
            List<OffsetPosition> journalPositions = null;
            List<OffsetPosition> abbrevJournalPositions = null;
            List<OffsetPosition> conferencePositions = null;
            List<OffsetPosition> publisherPositions = null;
            {
                journalPositions = lexicon.inJournalNames(text);
                abbrevJournalPositions = lexicon.inAbbrevJournalNames(text);
                conferencePositions = lexicon.inConferenceNames(text);
                publisherPositions = lexicon.inPublisherNames(text);
            }
            boolean isJournalToken = false;
            boolean isAbbrevJournalToken = false;
            boolean isConferenceToken = false;
            boolean isPublisherToken = false;
            int currentJournalPositions = 0;
            int currentAbbrevJournalPositions = 0;
            int currentConferencePositions = 0;
            int currentPublisherPositions = 0;
            boolean skipTest = false;
            st = new StringTokenizer(text, delimiters, true);
            int posit = 0;
            while (st.hasMoreTokens()) {
                isJournalToken = false;
                isAbbrevJournalToken = false;
                isConferenceToken = false;
                isPublisherToken = false;
                skipTest = false;
                String tok = st.nextToken();
                if (tok.trim().length() == 0) {
                    continue;
                }
                if (journalPositions != null) {
                    if (currentJournalPositions == journalPositions.size() - 1) {
                        if (journalPositions.get(currentJournalPositions).end < posit) {
                            skipTest = true;
                        }
                    }
                    if (!skipTest) {
                        for (int i = currentJournalPositions; i < journalPositions.size(); i++) {
                            if ((journalPositions.get(i).start <= posit) && (journalPositions.get(i).end >= posit)) {
                                isJournalToken = true;
                                currentJournalPositions = i;
                                break;
                            } else if (journalPositions.get(i).start > posit) {
                                isJournalToken = false;
                                currentJournalPositions = i;
                                break;
                            }
                        }
                    }
                }
                skipTest = false;
                if (abbrevJournalPositions != null) {
                    if (currentAbbrevJournalPositions == abbrevJournalPositions.size() - 1) {
                        if (abbrevJournalPositions.get(currentAbbrevJournalPositions).end < posit) {
                            skipTest = true;
                        }
                    }
                    if (!skipTest) {
                        for (int i = currentAbbrevJournalPositions; i < abbrevJournalPositions.size(); i++) {
                            if ((abbrevJournalPositions.get(i).start <= posit) && (abbrevJournalPositions.get(i).end >= posit)) {
                                isAbbrevJournalToken = true;
                                currentAbbrevJournalPositions = i;
                                break;
                            } else if (abbrevJournalPositions.get(i).start > posit) {
                                isAbbrevJournalToken = false;
                                currentAbbrevJournalPositions = i;
                                break;
                            }
                        }
                    }
                }
                skipTest = false;
                if (conferencePositions != null) {
                    if (currentConferencePositions == conferencePositions.size() - 1) {
                        if (conferencePositions.get(currentConferencePositions).end < posit) {
                            skipTest = true;
                        }
                    }
                    if (!skipTest) {
                        for (int i = currentConferencePositions; i < conferencePositions.size(); i++) {
                            if ((conferencePositions.get(i).start <= posit) && (conferencePositions.get(i).end >= posit)) {
                                isConferenceToken = true;
                                currentConferencePositions = i;
                                break;
                            } else if (conferencePositions.get(i).start > posit) {
                                isConferenceToken = false;
                                currentConferencePositions = i;
                                break;
                            }
                        }
                    }
                }
                skipTest = false;
                if (publisherPositions != null) {
                    if (currentPublisherPositions == publisherPositions.size() - 1) {
                        if (publisherPositions.get(currentPublisherPositions).end < posit) {
                            skipTest = true;
                        }
                    }
                    if (!skipTest) {
                        for (int i = currentPublisherPositions; i < publisherPositions.size(); i++) {
                            if ((publisherPositions.get(i).start <= posit) && (publisherPositions.get(i).end >= posit)) {
                                isPublisherToken = true;
                                currentPublisherPositions = i;
                                break;
                            } else if (publisherPositions.get(i).start > posit) {
                                isPublisherToken = false;
                                currentPublisherPositions = i;
                                break;
                            }
                        }
                    }
                }
                FeaturesVectorReference featureVector = FeaturesVectorReference.addFeaturesPatentReferences(tok, tokenizations.size(), posit, isJournalToken, isAbbrevJournalToken, isConferenceToken, isPublisherToken);
                patentBlocks.add(featureVector.printVector());
                posit++;
            }
            patentBlocks.add("\n");
            String theResult = null;
            if (articles == null) {
                theResult = taggerRun(patentBlocks, taggerPatent);
            } else if (patents == null) {
                theResult = taggerRun(patentBlocks, taggerNPL);
            } else {
                theResult = taggerRun(patentBlocks, taggerAll);
            }
            StringTokenizer stt = new StringTokenizer(theResult, "\n");
            ArrayList<String> referencesPatent = new ArrayList<String>();
            ArrayList<String> referencesNPL = new ArrayList<String>();
            ArrayList<Integer> offsets_patent = new ArrayList<Integer>();
            ArrayList<Integer> offsets_NPL = new ArrayList<Integer>();
            boolean currentPatent = true;
            String reference = null;
            offset = 0;
            int currentOffset = 0;
            String label = null;
            String actual = null;
            int p = 0;
            while (stt.hasMoreTokens()) {
                String line = stt.nextToken();
                if (line.trim().length() == 0) {
                    continue;
                }
                StringTokenizer st2 = new StringTokenizer(line, "\t");
                boolean start = true;
                boolean addSpace = false;
                label = null;
                actual = null;
                while (st2.hasMoreTokens()) {
                    if (start) {
                        actual = st2.nextToken().trim();
                        start = false;
                        boolean strop = false;
                        while ((!strop) && (p < tokenizations.size())) {
                            String tokOriginal = tokenizations.get(p);
                            if (tokOriginal.equals(" ")) {
                                addSpace = true;
                            } else if (tokOriginal.equals(actual)) {
                                strop = true;
                            }
                            p++;
                        }
                    } else {
                        label = st2.nextToken().trim();
                    }
                }
                if (label == null) {
                    continue;
                }
                if (actual != null) {
                    if (label.endsWith("<refPatent>")) {
                        if (reference == null) {
                            reference = actual;
                            currentOffset = offset;
                            currentPatent = true;
                        } else {
                            if (currentPatent) {
                                if (label.equals("I-<refPatent>")) {
                                    referencesPatent.add(reference);
                                    offsets_patent.add(currentOffset);
                                    currentPatent = true;
                                    reference = actual;
                                    currentOffset = offset;
                                } else {
                                    if (addSpace) {
                                        reference += " " + actual;
                                    } else reference += actual;
                                }
                            } else {
                                referencesNPL.add(reference);
                                offsets_NPL.add(currentOffset);
                                currentPatent = true;
                                reference = actual;
                                currentOffset = offset;
                            }
                        }
                    } else if (label.endsWith("<refNPL>")) {
                        if (reference == null) {
                            reference = actual;
                            currentOffset = offset;
                            currentPatent = false;
                        } else {
                            if (currentPatent) {
                                referencesPatent.add(reference);
                                offsets_patent.add(currentOffset);
                                currentPatent = false;
                                reference = actual;
                                currentOffset = offset;
                            } else {
                                if (label.equals("I-<refNPL>")) {
                                    referencesNPL.add(reference);
                                    offsets_NPL.add(currentOffset);
                                    currentPatent = false;
                                    reference = actual;
                                    currentOffset = offset;
                                } else {
                                    if (addSpace) {
                                        reference += " " + actual;
                                    } else reference += actual;
                                }
                            }
                        }
                    } else if (label.equals("<other>")) {
                        if (reference != null) {
                            if (currentPatent) {
                                referencesPatent.add(reference);
                                offsets_patent.add(currentOffset);
                            } else {
                                referencesNPL.add(reference);
                                offsets_NPL.add(currentOffset);
                            }
                            currentPatent = false;
                        }
                        reference = null;
                    }
                }
                offset++;
            }
            int j = 0;
            for (String ref : referencesPatent) {
                patentParser.setRawRefText(ref);
                patentParser.setRawRefTextOffset(offsets_patent.get(j).intValue());
                ArrayList<PatentItem> patents0 = patentParser.processRawRefText();
                for (PatentItem pat : patents0) {
                    pat.setContext(ref);
                    patents.add(pat);
                    if (pat.getApplication()) {
                        if (pat.getProvisional()) {
                            if (debug) {
                                System.out.println(pat.getAuthority() + " " + pat.getNumber() + " P application " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                            }
                        } else {
                            if (debug) {
                                System.out.println(pat.getAuthority() + " " + pat.getNumber() + " application " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                            }
                        }
                    } else if (pat.getReissued()) {
                        if (pat.getAuthority().equals("US")) {
                            if (debug) {
                                System.out.println(pat.getAuthority() + "RE" + pat.getNumber() + " E " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                            }
                        }
                    } else if (pat.getPlant()) {
                        if (pat.getAuthority().equals("US")) {
                            if (debug) System.out.println(pat.getAuthority() + "PP" + pat.getNumber() + " " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                        }
                    } else {
                        if (debug) {
                            if (pat.getKindCode() != null) {
                                System.out.println(pat.getAuthority() + " " + pat.getNumber() + " " + pat.getKindCode() + " " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                            } else {
                                System.out.println(pat.getAuthority() + " " + pat.getNumber() + " " + pat.getOffsetBegin() + ":" + pat.getOffsetEnd() + "\n");
                            }
                            System.out.println(pat.getContext());
                        }
                    }
                }
                j++;
            }
            ArrayList<String> numberListe = new ArrayList<String>();
            if (filterDuplicate) {
                ArrayList<PatentItem> toRemove = new ArrayList<PatentItem>();
                for (PatentItem pat : patents) {
                    if (!numberListe.contains(pat.getNumber())) {
                        numberListe.add(pat.getNumber());
                    } else {
                        toRemove.add(pat);
                    }
                }
                for (PatentItem pat : toRemove) {
                    patents.remove(pat);
                }
            }
            if (articles != null) {
                int k = 0;
                for (String ref : referencesNPL) {
                    BiblioItem result = nplParser.processing(ref, consolidate);
                    BibDataSet bds = new BibDataSet();
                    bds.setResBib(result);
                    bds.setRawBib(ref);
                    bds.addOffset(offsets_NPL.get(k).intValue());
                    articles.add(bds);
                    k++;
                }
            }
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
        int nbs = 0;
        if (patents != null) {
            nbs = patents.size();
        }
        if (articles != null) nbs += articles.size();
        return nbs;
    }

    private String taggerRun(ArrayList<String> ress, Tagger tagger) throws Exception {
        tagger.clear();
        for (String piece : ress) {
            tagger.add(piece);
            tagger.add("\n");
        }
        if (!tagger.parse()) {
            throw new Exception("CRF++ parsing failed.");
        }
        StringBuffer res = new StringBuffer();
        for (int i = 0; i < tagger.size(); i++) {
            for (int j = 0; j < tagger.xsize(); j++) {
                res.append(tagger.x(i, j) + "\t");
            }
            res.append(tagger.y2(i));
            res.append("\n");
        }
        return res.toString();
    }

    /**
     * Get the TEI XML string corresponding to the recognized citation section
     */
    public String references2TEI2() {
        String result = "<tei>\n";
        BiblioSet bs = new BiblioSet();
        for (BibDataSet bib : resBib) {
            BiblioItem bit = bib.getResBib();
            if (path != null) {
                bit.buildBiblioSet(bs, path);
            }
        }
        result += bs.toTEI();
        result += "<listbibl>\n";
        for (BibDataSet bib : resBib) {
            BiblioItem bit = bib.getResBib();
            result += "\n" + bit.toTEI2(bs);
        }
        result += "\n</listbibl>\n</tei>\n";
        return result;
    }

    /**
     * Get the TEI XML string corresponding to the recognized citation section for
     * a particular citation
     */
    public String reference2TEI(int i) {
        String result = "";
        if (resBib != null) {
            if (i <= resBib.size()) {
                BibDataSet bib = resBib.get(i);
                BiblioItem bit = bib.getResBib();
                if (path != null) {
                    bit.setPath(path);
                }
                result += bit.toTEI(i);
            }
        }
        return result;
    }

    /**
     * Get the BibTeX string corresponding to the recognized citation section
     */
    public String references2BibTeX() {
        String result = "";
        for (BibDataSet bib : resBib) {
            BiblioItem bit = bib.getResBib();
            if (path != null) {
                bit.setPath(path);
            }
            result += "\n" + bit.toBibTeX();
        }
        return result;
    }

    /**
     * Get the TEI XML string corresponding to the recognized citation section,
     * with pointers and advanced structuring
     */
    public String references2TEI() {
        String result = "<listbibl>\n";
        int p = 0;
        for (BibDataSet bib : resBib) {
            BiblioItem bit = bib.getResBib();
            if (path == null) {
                bit.setPath(path);
            }
            result += "\n" + bit.toTEI(p);
            p++;
        }
        result += "\n</listbibl>\n";
        return result;
    }

    /**
     * Get the BibTeX string corresponding to the recognized citation section
     * for a given citation
     */
    public String reference2BibTeX(int i) {
        String result = "";
        if (resBib != null) {
            if (i <= resBib.size()) {
                BibDataSet bib = resBib.get(i);
                BiblioItem bit = bib.getResBib();
                if (path == null) {
                    bit.setPath(path);
                }
                result += bit.toBibTeX();
            }
        }
        return result;
    }

    /**
     * Annotate XML files with extracted reference results. Not used.
     */
    private void annotate(File file, ArrayList<PatentItem> patents, ArrayList<BibDataSet> articles) {
        try {
            ArrayList<String> sources = new ArrayList<String>();
            ArrayList<String> targets = new ArrayList<String>();
            for (PatentItem pi : patents) {
                String context = pi.getContext();
                String source = context;
                sources.add(source);
                String target = " <patcit>" + context + "</patcit> ";
                targets.add(target);
                System.out.println(source + " -> " + target);
            }
            for (BibDataSet bi : articles) {
                String context = bi.getRawBib();
                String source = context;
                sources.add(source);
                String target = " <nplcit>" + context + "</nplcit> ";
                targets.add(target);
                System.out.println(source + " -> " + target);
            }
            FileInputStream fileIn = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fileIn, "UTF-8");
            BufferedReader bufReader = new BufferedReader(reader);
            String line;
            StringBuffer content = new StringBuffer();
            content.append("");
            while ((line = bufReader.readLine()) != null) {
                content.append(line);
                content.append("\n");
            }
            bufReader.close();
            reader.close();
            int i = 0;
            String contentString = content.toString();
            for (String source : sources) {
                String target = targets.get(i);
                contentString = contentString.replace(source, target);
                i++;
            }
            System.out.println(contentString);
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }

    /**
     * Annotate a new XML patent document based on training data format with the current model.
     *
     * @param documentPath    is the path to the file to be processed
     * @param newTrainingPath new training path
     */
    public void generateTrainingData(String documentPath, String newTrainingPath) {
        if (documentPath == null) {
            throw new GrobidResourceException("Cannot process the patent file, because the document path is null.");
        }
        if (!documentPath.endsWith(".xml")) {
            throw new GrobidResourceException("Only patent XML files (ST.36 or Marec) can be processed to " + "generate traning data.");
        }
        File documentFile = new File(documentPath);
        if (!documentFile.exists()) {
            throw new GrobidResourceException("Cannot process the patent file, because path '" + documentFile.getAbsolutePath() + "' does not exists.");
        }
        if (newTrainingPath == null) {
            newTrainingPath = GrobidProperties.getInstance().getTempPath().getAbsolutePath();
        }
        File newTrainingFile = new File(newTrainingPath);
        if (!newTrainingFile.exists()) {
            throw new GrobidResourceException("Cannot process the patent file, because path '" + newTrainingFile.getAbsolutePath() + "' does not exists.");
        }
        try {
            TextSaxParser sax = new TextSaxParser();
            sax.setFilter("description");
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            spf.setFeature("http://xml.org/sax/features/namespaces", false);
            spf.setFeature("http://xml.org/sax/features/validation", false);
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId, String systemId) {
                    return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
                }
            });
            reader.setContentHandler(sax);
            InputSource input = new InputSource(documentPath);
            input.setEncoding("UTF-8");
            reader.parse(input);
            String description = sax.getText();
            String currentPatentNumber = sax.currentPatentNumber;
            ArrayList<PatentItem> patents = new ArrayList<PatentItem>();
            ArrayList<BibDataSet> articles = new ArrayList<BibDataSet>();
            if (description != null) {
                extractAllReferencesString(description, false, false, patents, articles);
                Writer writer = new OutputStreamWriter(new FileOutputStream(new File(newTrainingPath + "/" + currentPatentNumber + ".training.xml"), false), "UTF-8");
                PatentAnnotationSaxParser saxx = new PatentAnnotationSaxParser();
                saxx.setWriter(writer);
                saxx.setPatents(patents);
                saxx.setArticles(articles);
                spf = SAXParserFactory.newInstance();
                spf.setValidating(false);
                spf.setFeature("http://xml.org/sax/features/namespaces", false);
                spf.setFeature("http://xml.org/sax/features/validation", false);
                reader = XMLReaderFactory.createXMLReader();
                reader.setEntityResolver(new EntityResolver() {

                    public InputSource resolveEntity(String publicId, String systemId) {
                        return new InputSource(new ByteArrayInputStream("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes()));
                    }
                });
                reader.setContentHandler(saxx);
                input = new InputSource(documentPath);
                input.setEncoding("UTF-8");
                reader.parse(input);
                writer.close();
                StringBuffer allBufferReference = new StringBuffer();
                ArrayList<String> inputs = new ArrayList<String>();
                for (BibDataSet article : articles) {
                    String refString = article.getRawBib();
                    if (refString.trim().length() > 1) {
                        inputs.add(refString.trim());
                    }
                }
                if (inputs.size() > 0) {
                    if (nplParser == null) {
                        nplParser = new CitationParser();
                    }
                    for (String inpu : inputs) {
                        ArrayList<String> inpus = new ArrayList<String>();
                        inpus.add(inpu);
                        StringBuilder bufferReference = nplParser.trainingExtraction(inpus);
                        if (bufferReference != null) {
                            allBufferReference.append(bufferReference.toString() + "\n");
                        }
                    }
                }
                if (allBufferReference != null) {
                    if (allBufferReference.length() > 0) {
                        Writer writerReference = new OutputStreamWriter(new FileOutputStream(new File(newTrainingPath + "/" + currentPatentNumber + ".tranining.references.xml"), false), "UTF-8");
                        writerReference.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                        writerReference.write("<citations>\n");
                        writerReference.write(allBufferReference.toString());
                        writerReference.write("</citations>\n");
                        writerReference.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }

    /**
     * Get a patent description by its number and OPS
     */
    public boolean getDocOPS(String number) {
        try {
            if (ops == null) ops = new OPSService();
            description = ops.descriptionRetrieval(number);
            if (description == null) return false; else if (description.length() < 600) return false; else return true;
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }

    /**
     * Write the list of extracted references in an XML file
     */
    public void generateXMLReport(File file, ArrayList<PatentItem> patents, ArrayList<BibDataSet> articles) {
        try {
            OutputStream tos = new FileOutputStream(file, false);
            Writer writer = new OutputStreamWriter(tos, "UTF-8");
            StringBuffer content = new StringBuffer();
            content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            if ((patents.size() > 0) || (articles.size() > 0)) content.append("<citations>\n");
            if (patents.size() > 0) content.append("<patent-citations>\n");
            int i = 0;
            for (PatentItem pi : patents) {
                String dnum = pi.getAuthority() + pi.getNumber();
                if (pi.getKindCode() != null) dnum += pi.getKindCode();
                content.append("<patcit if=\"pcit" + i + " dnum=\"" + dnum + "\">" + "<text>" + pi.getContext() + "</text></patcit>");
                content.append("\n");
                i++;
            }
            if (patents.size() > 0) content.append("</patent-citations>\n");
            if (articles.size() > 0) content.append("<npl-citations>\n");
            i = 0;
            for (BibDataSet bds : articles) {
                content.append("<nplcit if=\"ncit" + i + "\">");
                content.append(bds.getResBib().toTEI(i));
                content.append("<text>" + bds.getRawBib() + "</text></nplcit>");
                content.append("\n");
                i++;
            }
            if (articles.size() > 0) content.append("</npl-citations>\n");
            if ((patents.size() > 0) || (articles.size() > 0)) content.append("</citations>\n");
            writer.write(content.toString());
            writer.close();
            tos.close();
        } catch (Exception e) {
            throw new GrobidException("An exception occured while running Grobid.", e);
        }
    }

    /**
     * not used...
     */
    private static boolean checkPositionRange(int currentPosition, int posit, List<OffsetPosition> positions) {
        boolean isInRange = false;
        boolean skipTest = false;
        if (currentPosition == positions.size() - 1) {
            if (positions.get(currentPosition).end < posit) {
                skipTest = true;
            }
        }
        if (!skipTest) {
            for (int i = currentPosition; i < positions.size(); i++) {
                if ((positions.get(i).start <= posit) && (positions.get(i).end >= posit)) {
                    isInRange = true;
                    currentPosition = i;
                    break;
                } else if (positions.get(i).start > posit) {
                    isInRange = false;
                    currentPosition = i;
                    break;
                }
            }
        }
        return isInRange;
    }
}
