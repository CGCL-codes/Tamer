package net.sourceforge.plantuml.xmi;

import java.io.IOException;
import java.io.OutputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.Log;
import net.sourceforge.plantuml.classdiagram.ClassDiagram;
import net.sourceforge.plantuml.cucadiagram.CucaDiagram;

public final class CucaDiagramXmiMaker {

    private final CucaDiagram diagram;

    private final FileFormat fileFormat;

    public CucaDiagramXmiMaker(CucaDiagram diagram, FileFormat fileFormat) throws IOException {
        this.diagram = diagram;
        this.fileFormat = fileFormat;
    }

    public void createFiles(OutputStream fos) throws IOException {
        try {
            final IXmiClassDiagram xmi;
            if (fileFormat == FileFormat.XMI_STANDARD) {
                xmi = new XmiClassDiagramStandard((ClassDiagram) diagram);
            } else if (fileFormat == FileFormat.XMI_ARGO) {
                xmi = new XmiClassDiagramArgo((ClassDiagram) diagram);
            } else if (fileFormat == FileFormat.XMI_STAR) {
                xmi = new XmiClassDiagramStar((ClassDiagram) diagram);
            } else {
                throw new UnsupportedOperationException();
            }
            xmi.transformerXml(fos);
        } catch (ParserConfigurationException e) {
            Log.error(e.toString());
            e.printStackTrace();
            throw new IOException(e.toString());
        } catch (TransformerException e) {
            Log.error(e.toString());
            e.printStackTrace();
            throw new IOException(e.toString());
        }
    }
}
