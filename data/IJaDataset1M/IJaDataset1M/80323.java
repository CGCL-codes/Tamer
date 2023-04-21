package org.tru42.signal.model;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.tru42.signal.lang.ISignal;

public class SignalConnectionXMLWriter {

    private final OutputStream outStream;

    private XMLStreamWriter xmlStreamWriter;

    private int indentDepth = 0;

    public SignalConnectionXMLWriter(OutputStream outStream) {
        this.outStream = outStream;
    }

    public void writeEmptyElement(String localName) throws XMLStreamException {
        xmlStreamWriter.writeCharacters("\n");
        for (int i = 0; i < indentDepth; i++) xmlStreamWriter.writeCharacters("\t");
        xmlStreamWriter.writeEmptyElement(localName);
    }

    public void writeStartElement(String localName) throws XMLStreamException {
        xmlStreamWriter.writeCharacters("\n");
        for (int i = 0; i < indentDepth; i++) xmlStreamWriter.writeCharacters("\t");
        xmlStreamWriter.writeStartElement(localName);
        indentDepth++;
    }

    public void writeEndElement() throws XMLStreamException {
        indentDepth--;
        xmlStreamWriter.writeCharacters("\n");
        for (int i = 0; i < indentDepth; i++) xmlStreamWriter.writeCharacters("\t");
        xmlStreamWriter.writeEndElement();
    }

    public void writeModel(SignalDiagram model) throws IOException {
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        try {
            xmlStreamWriter = outputFactory.createXMLStreamWriter(outStream);
            xmlStreamWriter.writeStartDocument("UTF-8", "1.0");
            writeStartElement("SignalConnection");
            xmlStreamWriter.writeAttribute("version", "0.0.1");
            if (model != null) {
                if (model.getName() != null) xmlStreamWriter.writeAttribute("name", model.getName());
                writeStartElement("Processors");
                for (ISignalProcessor p : model.getSignalProcessors()) writeSignalProcessor(p);
                writeEndElement();
                writeStartElement("Connections");
                for (ISignal s : model.getSignals()) writeSignalConnection(s);
                writeEndElement();
            }
            writeEndElement();
            xmlStreamWriter.writeEndDocument();
            xmlStreamWriter.flush();
            xmlStreamWriter.close();
        } catch (XMLStreamException e) {
            throw new IOException("XML error");
        }
    }

    private void writeSignalConnection(ISignal s) throws XMLStreamException {
        if (s.getConnectedSinks().length == 0 || !(s.getOwner() instanceof ISignalProcessor)) return;
        writeStartElement("Source");
        xmlStreamWriter.writeAttribute("name", s.getName());
        xmlStreamWriter.writeAttribute("processor", ((ISignalProcessor) s.getOwner()).getName());
        for (Sink sink : s.getConnectedSinks()) if (sink.getOwner() instanceof ISignalProcessor) {
            ISignalProcessor proc = (ISignalProcessor) sink.getOwner();
            writeEmptyElement("Sink");
            xmlStreamWriter.writeAttribute("name", sink.getName());
            xmlStreamWriter.writeAttribute("processor", proc.getName());
        }
        writeEndElement();
    }

    private void writeSignalProcessor(ISignalProcessor p) throws XMLStreamException {
        if (p.getName() == null) return;
        writeEmptyElement("SignalProcessor");
        writeSignalProcessorAttributes(p);
    }

    protected void writeSignalProcessorAttributes(ISignalProcessor p) throws XMLStreamException {
        xmlStreamWriter.writeAttribute("name", p.getName());
        xmlStreamWriter.writeAttribute("class", p.getClass().getCanonicalName());
    }
}
