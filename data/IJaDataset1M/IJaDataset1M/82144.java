package pt.utl.ist.lucene.treceval.geoclef.parser;

import org.apache.log4j.Logger;
import org.dom4j.*;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import java.util.HashMap;
import pt.utl.ist.lucene.treceval.handlers.*;
import pt.utl.ist.lucene.treceval.GeoClefExample;
import pt.utl.ist.lucene.treceval.geoclef.parser.missingdocs.GeoClefMissingDocsGenerator;

/**
 * @author Jorge Machado
 * @date 5/Nov/2008
 * @see pt.utl.ist.lucene.treceval
 */
public class GeoClefGeoParserGeneratorFolha {

    private static final Logger logger = Logger.getLogger(GeoClefExample.class);

    /**
     * Number of Files to skip if already done
     */
    public static int skipFiles = 0;

    /**
     * Import every files from GeoParser
     * Generate a new Collection file just with missing files
     * Run Again and output to missing dir
     * Normalize Missing Dir
     *
     * Index will use GeoParserIterator that opens an internal iterator to missing files
     *
     */
    public static void main(String[] args) throws DocumentException, IOException {
        GeoClefMissingDocsGenerator.run("//DOC", "DOCNO", pt.utl.ist.lucene.treceval.geoclef.Globals.outputGeoParseDir + File.separator + "folha", "ISO-8859-1", pt.utl.ist.lucene.treceval.geoclef.Globals.collectionPathPt + "\\folha-pt");
    }

    /**
     * FOLHA EXAMPLE
            * <DOC>
            <DOCNO>FSP940101-131</DOCNO>
            <DOCID>FSP940101-131</DOCID>
            <DATE>940101</DATE>
            <TEXT>
            Livro retoma a conversa com Otto Lara
            ...
            </TEXT>
            </DOC>

     */
    static class FolhaFieldFilter implements FieldFilter {

        public FilteredFields filter(Node element, String fieldName) {
            if (GeoParserOutputFileMonitor.counter / GeoParserOutputFileMonitor.numberOfRecordsInFile < skipFiles) {
                GeoParserOutputFileMonitor.counter++;
            } else {
                Element docElem = (Element) element;
                Element docnoElem = (Element) docElem.selectSingleNode("DOCNO");
                if (docnoElem == null) {
                    logger.error("Record with no DOCNO");
                    logger.warn("trying docid");
                    docnoElem = (Element) docElem.selectSingleNode("DOCID");
                }
                if (docnoElem == null) logger.error("Record with no DOCID"); else {
                    String docno = docnoElem.getText();
                    Element textElem = (Element) docElem.selectSingleNode("TEXT");
                    String text = "";
                    if (textElem != null) text = textElem.getText(); else logger.warn("DOC " + docno + " with no text");
                    StringBuilder strBuilder = new StringBuilder();
                    strBuilder.append(text);
                    try {
                        Document dom = GeoParser.geoParse(strBuilder.toString(), "http://geoparser.digmap.eu/geoparser-dispatch");
                        GeoParserOutputFileMonitor.writeGeoParseElement(docno, dom.getRootElement());
                    } catch (IOException e) {
                        logger.error("DOC with DONO:" + docno + " - " + e.toString(), e);
                    } catch (DocumentException e) {
                        logger.error("DOC with DONO:" + docno + " - " + e.toString(), e);
                    }
                }
            }
            Map<String, String> fields = new HashMap<String, String>();
            return new FilteredFields(fields);
        }
    }
}
