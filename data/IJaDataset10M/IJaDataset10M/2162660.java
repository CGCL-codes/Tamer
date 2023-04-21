package org.monet.docservice.docprocessor.templates.opendocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.monet.docservice.docprocessor.templates.common.Attributes;
import org.monet.docservice.docprocessor.templates.common.BaseXmlProcessor;
import org.monet.docservice.docprocessor.templates.common.Model;

public class TableProcessor extends BaseXmlProcessor {

    Collection<Model> currentCollection;

    int depth = 0;

    ByteArrayOutputStream onMemoryStream;

    XMLStreamWriter onMemoryWriter;

    XMLStreamWriter fileWriter;

    OutputStream fileUnderlayingStream;

    boolean firstRow = true;

    boolean rowIsATemplate = false;

    boolean ignoreRestOfTemplateRows = false;

    public TableProcessor(XMLStreamReader reader, XMLStreamWriter writer, OutputStream underlayingOutputStream) throws XMLStreamException, FactoryConfigurationError {
        super(reader, writer, underlayingOutputStream);
        fileWriter = writer;
        fileUnderlayingStream = underlayingOutputStream;
        onMemoryStream = new ByteArrayOutputStream();
        onMemoryWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(onMemoryStream, "UTF-8");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        onMemoryWriter.close();
        onMemoryStream.close();
    }

    @Override
    protected boolean handleStartElement(String localName, Attributes attributes) throws IOException, XMLStreamException {
        if (localName.equals("table")) {
            if (depth == 0) {
                depth++;
            } else {
                TableProcessor tableProc = new TableProcessor(reader, writer, underlayingOutputStream);
                tableProc.setCollectionModel(currentCollection);
                tableProc.Start();
            }
        } else if (localName.equals("table-row")) {
            onMemoryWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(onMemoryStream, "UTF-8");
            this.writer = onMemoryWriter;
            this.underlayingOutputStream = onMemoryStream;
            this.writer.setNamespaceContext(reader.getNamespaceContext());
        } else {
            rowIsATemplate = localName.equals("user-field-get");
        }
        return false;
    }

    @Override
    protected boolean handleEndElement(String localName) throws XMLStreamException, FactoryConfigurationError, IOException {
        if (localName.equals("table") && depth > 0) {
            depth--;
            Stop();
        } else if (localName.equals("table-row")) {
            writer = fileWriter;
            fileWriter.flush();
            if (rowIsATemplate && !ignoreRestOfTemplateRows) {
                ignoreRestOfTemplateRows = true;
                onMemoryWriter.writeEndElement();
                onMemoryWriter.flush();
                ByteArrayOutputStream nsSupportStream = new ByteArrayOutputStream();
                XMLStreamWriter nsSupportWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(nsSupportStream);
                nsSupportWriter.writeStartElement("_8E03AB25A2E342ea84854A32DEA84BBC");
                for (Namespace n : this.getNamespaceContext().values()) nsSupportWriter.writeNamespace(n.Prefix, n.URI);
                nsSupportWriter.writeCharacters("");
                nsSupportWriter.flush();
                nsSupportStream.write(onMemoryStream.toByteArray());
                nsSupportWriter.writeEndDocument();
                nsSupportWriter.flush();
                this.underlayingOutputStream = fileUnderlayingStream;
                ByteArrayInputStream tempInputStream = new ByteArrayInputStream(nsSupportStream.toByteArray());
                int rowIndex = 0;
                for (Model model : currentCollection) {
                    tempInputStream.reset();
                    XMLStreamReader onMemoryReader = XMLInputFactory.newInstance().createXMLStreamReader(tempInputStream);
                    RootProcessor proc = new RootProcessor(onMemoryReader, fileWriter, underlayingOutputStream);
                    proc.setModel(model);
                    proc.setPartial(true);
                    proc.setNamespceContext(this.getNamespaceContext());
                    proc.setRowIndex(rowIndex);
                    proc.Start();
                    onMemoryReader.close();
                    rowIndex++;
                }
            } else if (!rowIsATemplate) {
                onMemoryWriter.writeEndElement();
                onMemoryWriter.flush();
                this.underlayingOutputStream = fileUnderlayingStream;
                this.underlayingOutputStream.write(onMemoryStream.toByteArray());
            }
            onMemoryWriter.close();
            onMemoryStream.reset();
            return true;
        }
        return false;
    }

    @Override
    protected boolean handleContent(String content) {
        return false;
    }

    @SuppressWarnings("unchecked")
    public void setCollectionModel(Collection<?> currentCollection) {
        this.currentCollection = (Collection<Model>) currentCollection;
    }
}
