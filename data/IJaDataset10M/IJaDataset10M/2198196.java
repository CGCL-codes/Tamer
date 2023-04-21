package edu.unibi.agbi.biodwh.gui.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import edu.unibi.agbi.biodwh.project.ProjectManager;
import edu.unibi.agbi.biodwh.project.object.ParserMonitorObject;
import edu.unibi.agbi.biodwh.project.object.ParserObject;
import edu.unibi.agbi.biodwh.project.object.ProjectObject;

/**
 * @author Benjamin Kormeier
 * @version 1.0 16.10.2008
 */
public class ProjectWriter {

    private XMLStreamWriter writer = null;

    private File file = null;

    public ProjectWriter(String file) {
        this.file = new File(file);
    }

    public ProjectWriter(File file) {
        this.file = file;
    }

    public void saveProjects() throws XMLStreamException, IOException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        writer = output.createXMLStreamWriter(new FileWriter(file));
        writer.writeStartDocument();
        writer.writeStartElement(XMLTagLibrary.ROOT);
        for (String project_name : ProjectManager.getProjectNames()) {
            ProjectObject object = ProjectManager.getProject(project_name);
            writeProject(object);
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public void saveProjects(LinkedList<ProjectObject> projects) throws XMLStreamException, IOException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        writer = output.createXMLStreamWriter(new FileWriter(file));
        writer.writeStartDocument();
        writer.writeStartElement(XMLTagLibrary.ROOT);
        for (ProjectObject project : projects) {
            writeProject(project);
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public void saveProject(String projectName) throws XMLStreamException, IOException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        writer = output.createXMLStreamWriter(new FileWriter(file));
        writer.writeStartDocument("biowdh");
        ProjectObject object = ProjectManager.getProject(projectName);
        writeProject(object);
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    private void writeProject(ProjectObject object) throws XMLStreamException {
        writer.writeStartElement(XMLTagLibrary.PROJECT);
        writer.writeAttribute(XMLTagLibrary.PROJECT_ATTRIBUTE_NAME, object.getProjectName());
        writer.writeStartElement(XMLTagLibrary.PROJECT_DESCRIPTION);
        writer.writeCharacters(object.getProjectDescription());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.PROJECT_EMAIL);
        writer.writeCharacters(object.getProjectEmail());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE);
        writer.writeStartElement(XMLTagLibrary.DATABASE_MANUFACTURER);
        writer.writeCharacters(object.getManufacturer());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_HOSTNAME);
        writer.writeCharacters(object.getHostname());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_PORT);
        writer.writeCharacters(object.getPort());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_NAME);
        writer.writeCharacters(object.getDatabaseName());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_URL);
        writer.writeAttribute(XMLTagLibrary.DATABASE_URL_ATTRIBUTE_USE, String.valueOf(object.isUseURL()));
        if (object.getConnectionURL().length() > 0) writer.writeCharacters(object.getConnectionURL());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_USER);
        writer.writeCharacters(object.getUsername());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.DATABASE_PASSWORD);
        writer.writeCharacters(object.getPassword());
        writer.writeEndElement();
        writer.writeEndElement();
        writeParser(object.getParserList());
        writer.writeEndElement();
    }

    private void writeParser(Vector<ParserObject> parser) throws XMLStreamException {
        writer.writeStartElement(XMLTagLibrary.PARSER_LIST);
        for (ParserObject object : parser) {
            writer.writeStartElement(XMLTagLibrary.PARSER);
            writer.writeAttribute(XMLTagLibrary.PARSER_ATTRIBUTE_ID, object.getParserID());
            writer.writeAttribute(XMLTagLibrary.PARSER_ATTRIBUTE_DOWNLOAD, String.valueOf(object.isDownloadFiles()));
            writer.writeAttribute(XMLTagLibrary.PARSER_ATTRIBUTE_RENAME, String.valueOf(object.isRenameOldTables()));
            writer.writeAttribute(XMLTagLibrary.PARSER_ATTRIBUTE_MONITOR, String.valueOf(object.hasMonitorConfiguration()));
            writer.writeStartElement(XMLTagLibrary.PARSER_SOURCE);
            writer.writeCharacters(object.getSourceDirectory().getAbsolutePath());
            writer.writeEndElement();
            if (object.getMonitorObject() != null) writeMonitor(object.getMonitorObject());
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    private void writeMonitor(ParserMonitorObject object) throws XMLStreamException {
        writer.writeStartElement(XMLTagLibrary.MONITOR);
        writer.writeAttribute(XMLTagLibrary.MONITOR_ATTRIBUTE_UPDATE, String.valueOf(object.getUpdateInterval()));
        writer.writeAttribute(XMLTagLibrary.MONITOR_ATTRIBUTE_BACKUP, String.valueOf(object.isBackup()));
        writer.writeAttribute(XMLTagLibrary.MONITOR_ATTRIBUTE_UNZIP, String.valueOf(object.isUnzip()));
        writer.writeStartElement(XMLTagLibrary.MONITOR_SOURCE);
        writer.writeCharacters(object.getSourceURL().toString());
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.MONITOR_DATE);
        if (object.getDate() != null) writer.writeCharacters(String.valueOf(object.getDate().getTime()));
        writer.writeEndElement();
        writer.writeStartElement(XMLTagLibrary.MONITOR_DOWNLOAD_LIST);
        if (object.getDowloadFiles() != null) {
            for (String file : object.getDowloadFiles()) {
                writer.writeStartElement(XMLTagLibrary.MONITOR_DOWNLOAD);
                writer.writeCharacters(file);
                writer.writeEndElement();
            }
        }
        writer.writeEndElement();
        writer.writeEndElement();
    }
}
