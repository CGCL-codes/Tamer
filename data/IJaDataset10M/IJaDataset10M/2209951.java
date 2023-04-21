package gov.sns.xal.model.xml;

import java.io.File;
import java.io.IOException;
import org.w3c.dom.Document;
import gov.sns.tools.xml.XmlDataAdaptor;
import gov.sns.xal.model.probe.EnvelopeProbe;
import gov.sns.xal.model.probe.Probe;

/**
 * Saves probe instances to an XML file.
 * 
 * @author Craig McChesney
 * @version $id:
 * 
 */
public class ProbeXmlWriter {

    private static final String DOC_TYPE = Probe.PROBE_LABEL;

    /** Sako
	 * Writes supplied <code>Probe</code> to the specified XML file with Twiss.
	 * 
	 * @param aProbe <code>Probe</code> to output
	 * @param fileURI String URI of output file
	 * 
	 * @throws IOException error writing to fileURI
	 */
    public static void writeXmlAsTwiss(EnvelopeProbe aProbe, String fileURI) throws IOException {
        ProbeXmlWriter writer = new ProbeXmlWriter();
        XmlDataAdaptor doc = writer.writeProbeToDocAsTwiss(aProbe);
        doc.writeTo(new File(fileURI));
    }

    /**
	 * Writes supplied <code>Probe</code> to the specified XML file.
	 * 
	 * @param aProbe <code>Probe</code> to output
	 * @param fileURI String URI of output file
	 * 
	 * @throws IOException error writing to fileURI
	 */
    public static void writeXml(Probe aProbe, String fileURI) throws IOException {
        ProbeXmlWriter writer = new ProbeXmlWriter();
        XmlDataAdaptor doc = writer.writeProbeToDoc(aProbe);
        doc.writeTo(new File(fileURI));
    }

    /**
	 * Returns a DOM for the supplied lattice.
	 * 
	 * @param lattice the Lattice whose DOM to return
	 * @return a DOM for the supplied lattice
	 */
    public static Document documentForProbe(Probe aProbe) throws IOException {
        ProbeXmlWriter writer = new ProbeXmlWriter();
        XmlDataAdaptor doc = writer.writeProbeToDoc(aProbe);
        return doc.document();
    }

    /**
	 * Writes supplied <code>Probe</code> to the specified XML file.
	 * 
	 * @param aProbe <code>Probe</code> to write to XML file
	 * 
	 * @throws IOException error writing to fileURI
	 */
    public XmlDataAdaptor writeProbeToDoc(Probe aProbe) throws IOException {
        XmlDataAdaptor document = XmlDataAdaptor.newEmptyDocumentAdaptor(DOC_TYPE, null);
        aProbe.save(document);
        return document;
    }

    /**
	 * Writes supplied <code>Probe</code> to the specified XML file with Twiss.
	 * 
	 * @param aProbe <code>Probe</code> to write to XML file
	 * 
	 * @throws IOException error writing to fileURI
	 */
    public XmlDataAdaptor writeProbeToDocAsTwiss(EnvelopeProbe aProbe) throws IOException {
        XmlDataAdaptor document = XmlDataAdaptor.newEmptyDocumentAdaptor(DOC_TYPE, null);
        aProbe.saveAsTwiss(document);
        return document;
    }
}
