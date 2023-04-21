package jp.riken.omicspace.gps.das;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.ProteinTools;
import org.biojava.bio.seq.RNATools;
import org.biojava.bio.symbol.IllegalAlphabetException;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.utils.xml.XMLWriter;
import org.biojava.servlets.dazzle.DASStatus;
import org.biojava.servlets.dazzle.DazzleException;
import org.biojava.servlets.dazzle.DazzleResponse;
import org.biojava.servlets.dazzle.DazzleServlet;
import org.biojava.servlets.dazzle.Segment;
import org.biojava.servlets.dazzle.datasource.DataSourceException;
import org.biojava.servlets.dazzle.datasource.DazzleDataSource;

public class OmicBrowseSequenceHandler extends OmicBrowseHandler {

    protected Log log = LogFactory.getLog(this.getClass());

    protected int limitation = Integer.MIN_VALUE;

    protected String sequenceServerConfig;

    public OmicBrowseSequenceHandler() {
        super(new String[] { "sequence", "dna" }, new String[] { "sequence/1.0", "dna/1.0" });
    }

    @Override
    public void run(DazzleServlet dazzleServlet, DazzleDataSource dazzleDataSource, String command, HttpServletRequest request, DazzleResponse response) throws IOException, DataSourceException, ServletException, DazzleException {
        rootTagName = "DAS" + command.toUpperCase();
        super.run(dazzleServlet, dazzleDataSource, command, request, response);
        if (limitation == Integer.MIN_VALUE || sequenceServerConfig == null) {
            readLimitationAndSequenceServerConfig(dazzleServlet.getServletContext());
        }
        String[] segmentArgumentStrings = request.getParameterValues("segment");
        if (segmentArgumentStrings == null || segmentArgumentStrings.length == 0) {
            String errorMessage = "At least one segment argument is required.";
            log.error(errorMessage);
            throw new DazzleException(DASStatus.STATUS_BAD_COMMAND_ARGUMENTS, errorMessage);
        }
        List<Segment> segments = new ArrayList<Segment>();
        for (String segmentArgumentString : segmentArgumentStrings) {
            Segment segment = checkSegment(segmentArgumentString);
            segments.add(segment);
        }
        if (log.isDebugEnabled()) {
            log.debug(command + " command accepted.");
            log.debug("segments: " + segments);
        }
        try {
            opening(response);
            for (Segment segment : segments) {
                if ("sequence".equals(command)) {
                    printSequence(segment, false);
                } else if ("dna".equals(command)) {
                    printSequence(segment, true);
                }
            }
            closing();
        } catch (Exception e) {
            String errorMessage = e + ":" + e.getMessage() + ", " + Arrays.toString(e.getStackTrace());
            log.error(errorMessage);
            throw new DazzleException(e, errorMessage);
        }
    }

    private Segment checkSegment(String segmentArgumentString) throws DataSourceException, DazzleException {
        Segment segment = null;
        try {
            segment = createSegmentFromString(segmentArgumentString);
        } catch (OutOfRangeException e) {
            log.error(e.getMessage());
            throw new DazzleException(DASStatus.STATUS_BAD_COORDS, e, e.getMessage());
        } catch (UnknownSegmentException e) {
            log.error(e.getMessage());
            throw new DazzleException(DASStatus.STATUS_BAD_REFERENCE, e, e.getMessage());
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            throw new DazzleException(DASStatus.STATUS_BAD_COMMAND_ARGUMENTS, e, e.getMessage());
        }
        int length = segment.getMax() - segment.getMin() + 1;
        if (length > limitation) {
            String errorMessage = "Requested range(" + segment + ") is wider than the limitation(" + limitation + ").";
            log.error(errorMessage);
            throw new DazzleException(DASStatus.STATUS_BAD_COORDS, errorMessage);
        }
        return segment;
    }

    private void printSequence(Segment segment, boolean isDNA) throws IOException, DataSourceException {
        xmlWriter.openTag("SEQUENCE");
        xmlWriter.attribute("id", segment.getReference());
        xmlWriter.attribute("version", featureSource.getLandmarkVersion(segment.getReference()));
        xmlWriter.attribute("start", Integer.toString(segment.getStart()));
        xmlWriter.attribute("stop", Integer.toString(segment.getStop()));
        featureSource.setSequenceServerConfig(sequenceServerConfig);
        Sequence sequence = featureSource.getSequence(segment.getReference());
        String molType = sequence.getAlphabet().getName();
        if (sequence.getAlphabet() == DNATools.getDNA()) {
            molType = "DNA";
        } else if (sequence.getAlphabet() == RNATools.getRNA()) {
            molType = "ssRNA";
        } else if (sequence.getAlphabet() == ProteinTools.getAlphabet() || sequence.getAlphabet() == ProteinTools.getTAlphabet()) {
            molType = "Protein";
        }
        xmlWriter.attribute("moltype", molType);
        if (isDNA) {
            xmlWriter.openTag("DNA");
            int length = segment.getMax() - segment.getMin() + 1;
            xmlWriter.attribute("length", Integer.toString(length));
        }
        SymbolList symbolList = sequence.subList(segment.getMin(), segment.getMax());
        if (segment.isInverted()) {
            try {
                symbolList = DNATools.reverseComplement(symbolList);
            } catch (IllegalAlphabetException e) {
                String errorMessage = "IllegalAlphabetException shouldn't be " + "occured.: " + symbolList;
                log.error(errorMessage);
                throw new IllegalArgumentException(errorMessage, e);
            }
        }
        for (int position = 1; position <= symbolList.length(); position += 60) {
            int maxPosition = Math.min(symbolList.length(), position + 59);
            xmlWriter.println(symbolList.subStr(position, maxPosition));
        }
        if (isDNA) {
            xmlWriter.closeTag("DNA");
        }
        xmlWriter.closeTag("SEQUENCE");
    }

    private void readLimitationAndSequenceServerConfig(ServletContext context) {
        InputStream is = context.getResourceAsStream("/WEB-INF/web.xml");
        if (log.isDebugEnabled()) {
            log.debug("getResourceAsStream(): " + is);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String sequenceViewerMaxLength = null;
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                if (log.isDebugEnabled()) {
                    log.debug("line: " + line);
                }
                if (line.indexOf("SEQUENCE_VIEWER_MAX_LENGTH") > 0) {
                    sequenceViewerMaxLength = reader.readLine().replaceAll("[^0-9]+", "");
                    if (log.isDebugEnabled()) {
                        log.debug("sequenceViewerMaxLength: " + sequenceViewerMaxLength);
                    }
                } else if (line.indexOf("SEQUENCE_SERVER_CONFIG") > 0) {
                    sequenceServerConfig = reader.readLine().replaceAll("^[^/]+", "");
                    sequenceServerConfig = sequenceServerConfig.replaceAll("txt.*$", "txt");
                }
            }
        } catch (IOException e) {
            String errorMessage = "IOException occurred while reading web.xml.";
            log.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
        if (sequenceViewerMaxLength != null) {
            limitation = Integer.parseInt(sequenceViewerMaxLength);
            if (log.isDebugEnabled()) {
                log.debug("sequence view max limitation: " + limitation);
            }
        }
    }
}
