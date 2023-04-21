package apollo.dataadapter.das.simple;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;
import org.xml.sax.helpers.DefaultHandler;
import apollo.dataadapter.NonFatalDataAdapterException;
import apollo.dataadapter.das.*;

/** 2/2006: I am a little confused about why this is called *server*, since it
 *  is actually client-side--it gets data *from* a DAS server.
 *
 *  Updating to handle DAS/2.
 * $PREFIX/sequence - a "sources" request  
 *   This is the top-level entry point to a DAS 2 server.  It returns a
 *   list of the available genomic sequence and their versions.
 *
 * $PREFIX/sequence/$SOURCE - a "source" request
 *   Returns the available versions of the given genomic sequence.

 * $PREFIX/sequence/$SOURCE/$VERSION - a "versioned source" request
 *   Returns information about a given version of a genomic sequence.
 *   Clients may assume that the sequence and assembly are constant for a
 *   given version of a source. Note that annotation data on a server
 *   with curational write-back support may change without changing the
 *   version.
 *
 * I think in DAS1, the "dsn" request was equivalent to 
 * $PREFIX/sequence/$SOURCE/$VERSION - it gave you a complete list of
 * versioned genome dbs, e.g., "ensembl_Anopheles_gambiae_core_28_2c",
 * ensembl_Anopheles_gambiae_core_29", etc.
 **/
public class SimpleDASServer implements DASServerI {

    private String url = "";

    private String server;

    private static final float version = 1.0f;

    /**
   * You <em>must</em> pass in the URL of a DAS-reference server, used
   * in all further requests.
  **/
    public SimpleDASServer(String url) {
        setURL(url);
    }

    public void setURL(String url) {
        if (url.indexOf("http://") != 0) {
            url = "http://" + url;
        }
        if (url.substring(url.length() - 1).equals("/") == false) {
            url = url + "/";
        }
        this.url = url;
    }

    public String getURL() {
        return url;
    }

    /**
   * I return all data-sources from the das server URL passed in when 
   * this instance was constructed. The list consists of objects implementing
   * the <code>apollo.dataadapter.das.DASDsn</code> interface. In this case,
   * I populate the returned list with instances of <code>SimpleDASDsn</code>.
  **/
    public List getDSNs() {
        List dsnList = new ArrayList();
        String queryString;
        DASDsnContentHandler theSAXHandler = new DASDsnContentHandler();
        queryString = getURL() + "dsn";
        System.out.println("SimpleDASServer.getDSNs: query string = " + queryString);
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(theSAXHandler);
            parser.parse(queryString);
        } catch (SAXException theException) {
            throw new NonFatalDataAdapterException(theException.getMessage());
        } catch (IOException theException) {
            throw new NonFatalDataAdapterException(theException.getMessage());
        }
        dsnList = theSAXHandler.getDsns();
        return dsnList;
    }

    /**
   * I return all entry-points from the input DASDsn instance passed in. 
   * The list consists of objects implementing
   * the <code>apollo.dataadapter.das.DASSegment</code> interface. In this case,
   * I populate the returned list with instances of <code>SimpleDASSegment</code>.
  **/
    public List getEntryPoints(DASDsn dsn) {
        List entryPoints = new ArrayList();
        String queryString;
        DASEntryPointContentHandler theSAXHandler = new DASEntryPointContentHandler();
        queryString = getURL() + dsn.getSourceId() + "/entry_points";
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(theSAXHandler);
            parser.parse(queryString);
        } catch (SAXException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("SAX Problem fetching Entry Points: " + theException.getMessage());
        } catch (IOException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("IO Problem fetching Entry Points: " + theException.getMessage());
        }
        entryPoints = theSAXHandler.getSegments();
        return entryPoints;
    }

    /**
   * <p> I am the call used by apollo to fetch annotations (features) from
   * the input dsn, for the entry-points and ranges given by the input segments</p>
   *
   * <p>The return list consists of objects implementing
   * the <code>apollo.dataadapter.das.DASFeature</code> interface. In this case,
   * I populate the returned list with instances of <code>SimpleDASFeature</code>.</p>
   *
  **/
    public List getFeatures(DASDsn dsn, DASSegment[] segmentSelection) {
        HashMap segmentMap = new HashMap();
        for (int i = 0; i < segmentSelection.length; i++) {
            segmentMap.put(segmentSelection[i].getId(), segmentSelection[i]);
        }
        return getFeatures(dsn, segmentMap, new HashMap(), Long.valueOf(segmentSelection[0].getStart()).longValue(), Long.valueOf(segmentSelection[0].getStop()).longValue());
    }

    /**
   * <p> I am the <code>getFeatures</code> signature called internally by the 
   * DASFeatureContentHandler. Since I exist for the purposes of a recursive-descent
   * (for assembly) in this implementation, I am <em>not</em> a part of a DASServerI 
   * interface.</p> 
   *
   * <p> I am invoked directly from DASFeatureContentHandler when the features 
   * returned have 'children' - that is, they contain further nested features. In that
   * case, the handler will collect all such features of interest, create a new 
   * DASSegment for each such feature, and re-invoke this method (recursion!) passing
   * in both a set of segments corresponding to the parent features, <em>and</em> 
   * the parent features themselves. </p>
   *
   * @param dsn               The dsn of the das data source (for example 'ensembl729')
   * @param segmentSelection  The DAS-segments of interest indexed by segment id
   * @param parentFeatures    If any particular segment has a parent feature (i.e. 
   * we are invoking the method because we want to find the children of features from
   * a prior invocation) then the parent features for each requested segment are
   * stored here, indexed by segment id (thus each feature corresponds 1-1 to the
   * segments input above). The most important thing contained in each parent is the 
   * global start and end of the requested range in the input segments.
   * @param globalStart       The global start position of the (very first) requested range
   * @param globalEnd         Global end position of the (very first) requested range.
  **/
    public List getFeatures(DASDsn dsn, HashMap segmentSelection, HashMap parentFeatures, long globalStart, long globalEnd) {
        List dasFeatures;
        String dsnId = dsn.getSourceId();
        Iterator segmentIterator = segmentSelection.values().iterator();
        DASSegment aSegment;
        String segmentId;
        String start;
        String stop;
        String queryString;
        DASFeatureContentHandler theSAXHandler = new DASFeatureContentHandler(this, dsn, segmentSelection, parentFeatures, globalStart, globalEnd);
        queryString = getURL() + dsnId + "/features?";
        while (segmentIterator.hasNext()) {
            aSegment = (DASSegment) segmentIterator.next();
            segmentId = aSegment.getId();
            start = aSegment.getStart();
            stop = aSegment.getStop();
            queryString += "segment=" + segmentId + ":" + start + "," + stop + ";";
        }
        DataInputStream xmlStream = null;
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(theSAXHandler);
            parser.parse(queryString);
        } catch (SAXException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("SAX Problem fetching features: " + theException.getMessage());
        } catch (IOException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("IO Problem fetching features: " + theException.getMessage());
        }
        return theSAXHandler.getFeatures();
    }

    /**
   * <p> I am the call used by apollo to fetch dna (sequence) from
   * the input dsn, for the entry-points and ranges given by the input segments</p>
   *
   * <p>The return list consists of objects implementing
   * the <code>apollo.dataadapter.das.DASSequence</code> interface. In this case,
   * I populate the returned list with instances of <code>SimpleDASSequence</code>.</p>
   * 
   * @param segmentSelection A list of segments containing entry points and ranges
   * of interest. NOTE: I only pay attention to the FIRST element of the list - 
   * all subsequence elements are IGNORED!!! Therefore, my return value is a list
   * of size 1 under all circumstances.
  **/
    public List getSequences(DASDsn dsn, DASSegment[] segmentSelection) {
        List sequences = null;
        String dsnId = dsn.getSourceId();
        String segmentId = segmentSelection[0].getId();
        String start = segmentSelection[0].getStart();
        String stop = segmentSelection[0].getStop();
        String queryString;
        DASSequenceContentHandler theSAXHandler = new DASSequenceContentHandler(this, dsn, segmentSelection);
        queryString = getURL() + dsnId + "/dna?segment=" + segmentId + ":" + start + "," + stop;
        DataInputStream xmlStream = null;
        URL sequenceURL;
        DataInputStream content;
        StringBuffer sequenceString = new StringBuffer();
        String dataLine;
        StringBufferInputStream stringReader;
        String lastCharacter;
        try {
            sequenceURL = new URL(queryString);
            content = new DataInputStream(sequenceURL.openStream());
            dataLine = content.readLine();
            lastCharacter = dataLine.substring(dataLine.length() - 1);
            if (!lastCharacter.equals(">")) {
                dataLine += ">";
            }
            sequenceString.append(dataLine);
            dataLine = content.readLine();
            while (dataLine != null) {
                sequenceString.append(dataLine);
                dataLine = content.readLine();
            }
            stringReader = new StringBufferInputStream(sequenceString.toString());
        } catch (IOException exception) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("attempt to read sequence failed:" + exception.getMessage());
        }
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(theSAXHandler);
            InputSource inputSource = new InputSource(stringReader);
            inputSource.setSystemId(getURL());
            parser.parse(inputSource);
        } catch (SAXException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("SAX Problem fetching sequence: " + theException.getMessage());
        } catch (IOException theException) {
            throw new apollo.dataadapter.NonFatalDataAdapterException("IO Problem fetching sequence: " + theException.getMessage());
        }
        return theSAXHandler.getSequences();
    }
}
