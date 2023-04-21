package org.trackplan.app.serialization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import org.trackplan.app.gui.TrackPlanMain;
import org.trackplan.app.gui.control_table.ControlTable;
import org.trackplan.app.gui.layout_editor.LayoutComponent;
import org.trackplan.app.gui.layout_editor.TrackLayout;

/**
 * Saves project information from memory to a TrackPlan XML file.
 * 
 * @author James Mistry
 *
 */
public class ProjectSerializer {

    private static final String XML_NAMESPACE_NAME = "trpl";

    private static final String XML_NAMESPACE_PREFIX = XML_NAMESPACE_NAME + ":";

    public static final String NODE_NAME_COMPONENT = XML_NAMESPACE_PREFIX + "component";

    public static final String ATTRIBUTE_NAME_COMPONENT_TYPE = "type";

    public static final String ATTRIBUTE_NAME_COMPONENT_ORIENTATION = "orientation";

    public static final String NODE_NAME_COMPONENT_POSITIONING = XML_NAMESPACE_PREFIX + "positioning";

    public static final String ATTRIBUTE_NAME_COMPONENT_X = "x";

    public static final String ATTRIBUTE_NAME_COMPONENT_Y = "y";

    public static final String ATTRIBUTE_NAME_COMPONENT_WIDTH = "width";

    public static final String ATTRIBUTE_NAME_COMPONENT_HEIGHT = "height";

    public static final String ATTRIBUTE_NAME_COMPONENT_DEFAULT_WIDTH = "defaultWidth";

    public static final String ATTRIBUTE_NAME_COMPONENT_DEFAULT_HEIGHT = "defaultHeight";

    public static final String NODE_NAME_COMPONENT_NAME = XML_NAMESPACE_PREFIX + "name";

    public static final String NODE_NAME_COMPONENT_CONNECTIONS = XML_NAMESPACE_PREFIX + "connections";

    public static final String NODE_NAME_COMPONENT_INDIVIDUAL_CONNECTION = XML_NAMESPACE_PREFIX + "connection";

    public static final String ATTRIBUTE_NAME_COMPONENT_CONNECTION_TYPE = "type";

    public static final String ATTRIBUTE_NAME_COMPONENT_CONNECTION_FOREIGN_COMPONENT = "foreignComponent";

    public static final String ATTRIBUTE_NAME_COMPONENT_CONNECTION_FOREIGN_CONNECTION = "foreignConnection";

    public static final String UNEXPECTED_ERROR = "An unexpected error occurred while trying to save the project.";

    public static final String FILE_DOES_NOT_EXIST = "The file could not be written to because it did not exist when the save operation was attempted.";

    public static final String GENERAL_IO_ERROR = "An error occurred while trying to write to the file.";

    public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    public static final String NAMESPACE_DECLARATION = "xmlns:" + XML_NAMESPACE_NAME + "=\"http://www.trackplan.org/schema/v1\"";

    public static final String NODE_NAME_ROOT = XML_NAMESPACE_PREFIX + "trackPlanProject";

    public static final String NODE_NAME_FORMAT_VERSION = XML_NAMESPACE_PREFIX + "formatVersion";

    public static final String NODE_NAME_MAJOR_VERSION = XML_NAMESPACE_PREFIX + "majorVersion";

    public static final String NODE_NAME_MINOR_VERSION = XML_NAMESPACE_PREFIX + "minorVersion";

    public static final String NODE_NAME_META = XML_NAMESPACE_PREFIX + "meta";

    public static final String NODE_NAME_PROJECT_NAME = XML_NAMESPACE_PREFIX + "projectName";

    public static final String NODE_NAME_PROJECT_DESC = XML_NAMESPACE_PREFIX + "projectDescription";

    public static final String NODE_NAME_PROJECT_AUTHOR = XML_NAMESPACE_PREFIX + "projectAuthor";

    public static final String NODE_NAME_LAYOUT = XML_NAMESPACE_PREFIX + "layout";

    public static final String ATTRIBUTE_NAME_LAYOUT_DIMENSIONS = XML_NAMESPACE_PREFIX + "dimensions";

    public static final String ATTRIBUTE_NAME_LAYOUT_DIMENSION_WIDTH = "width";

    public static final String ATTRIBUTE_NAME_LAYOUT_DIMENSION_HEIGHT = "height";

    public static final String NODE_NAME_CONTROL_TABLE = XML_NAMESPACE_PREFIX + "controlTable";

    public static final String NODE_NAME_TABLE_ROUTE = XML_NAMESPACE_PREFIX + "route";

    public static final String NODE_NAME_TABLE_ENTRY_SIGNAL = XML_NAMESPACE_PREFIX + "entrySignal";

    public static final String NODE_NAME_TABLE_EXIT_SIGNAL = XML_NAMESPACE_PREFIX + "exitSignal";

    public static final String NODE_NAME_TABLE_SIGNAL_CONFIG = XML_NAMESPACE_PREFIX + "signalConfiguration";

    public static final String NODE_NAME_TABLE_SIGNAL = XML_NAMESPACE_PREFIX + "signal";

    public static final String ATTRIBUTE_NAME_TABLE_NAME = "name";

    public static final String ATTRIBUTE_NAME_TABLE_STOP = "stop";

    public static final String NODE_NAME_TABLE_POINT_CONFIG = XML_NAMESPACE_PREFIX + "pointConfiguration";

    public static final String NODE_NAME_TABLE_POINT = XML_NAMESPACE_PREFIX + "point";

    public static final String ATTRIBUTE_NAME_TABLE_POSITION = "position";

    public static final String NODE_NAME_TABLE_TRACK_CONFIG = XML_NAMESPACE_PREFIX + "trackConfiguration";

    public static final String NODE_NAME_TABLE_TRACK = XML_NAMESPACE_PREFIX + "track";

    public static final String ATTRIBUTE_NAME_TABLE_CLEAR = "clear";

    public static final String NODE_NAME_TABLE_OVERLAP = XML_NAMESPACE_PREFIX + "overlap";

    public static final String NODE_NAME_TABLE_CONFLICTING = XML_NAMESPACE_PREFIX + "conflictingRoutes";

    public static final String NODE_NAME_TABLE_FLANK = XML_NAMESPACE_PREFIX + "flankProtection";

    private static String lastSerializeError = null;

    /**
	 * Saves the current project to a file in the TrackPlan XML format. If false is returned,
	 * lastSerializeError should be checked for a description of the reason why the serialization
	 * failed.
	 * 
	 * @param destination The file object to write to. The file must exist.
	 * @return True if the serialization was successful, false otherwise.
	 */
    public static boolean serializeToFile(File destination) {
        lastSerializeError = null;
        try {
            FileOutputStream streamOut = new FileOutputStream(destination, false);
            OutputStreamWriter streamWriter = new OutputStreamWriter(streamOut, Charset.forName("UTF8"));
            try {
                streamWriter.write(XML_DECLARATION);
                streamWriter.write("\n<" + NODE_NAME_ROOT + " " + NAMESPACE_DECLARATION + ">");
                streamWriter.write("\n<" + NODE_NAME_FORMAT_VERSION + ">");
                streamWriter.write("\n<" + NODE_NAME_MAJOR_VERSION + ">" + ProjectSerializer.escapeXML(TrackPlanMain.FILE_FORMAT_MAJOR_VERSION) + "</" + NODE_NAME_MAJOR_VERSION + ">");
                streamWriter.write("\n<" + NODE_NAME_MINOR_VERSION + ">" + ProjectSerializer.escapeXML(TrackPlanMain.FILE_FORMAT_MINOR_VERSION) + "</" + NODE_NAME_MINOR_VERSION + ">");
                streamWriter.write("\n</" + NODE_NAME_FORMAT_VERSION + ">");
                streamWriter.write("\n<" + NODE_NAME_META + ">");
                streamWriter.write("\n<" + NODE_NAME_PROJECT_NAME + ">" + ProjectSerializer.escapeXML(TrackPlanMain.getProjectName()) + "</" + NODE_NAME_PROJECT_NAME + ">");
                streamWriter.write("\n<" + NODE_NAME_PROJECT_DESC + ">" + ProjectSerializer.escapeXML(TrackPlanMain.getProjectDescription()) + "</" + NODE_NAME_PROJECT_DESC + ">");
                streamWriter.write("\n<" + NODE_NAME_PROJECT_AUTHOR + ">" + ProjectSerializer.escapeXML(TrackPlanMain.getProjectAuthor()) + "</" + NODE_NAME_PROJECT_AUTHOR + ">");
                streamWriter.write("\n</" + NODE_NAME_META + ">");
                streamWriter.write("\n<" + NODE_NAME_LAYOUT + ">");
                streamWriter.write("\n<" + ATTRIBUTE_NAME_LAYOUT_DIMENSIONS + " " + ATTRIBUTE_NAME_LAYOUT_DIMENSION_WIDTH + "=\"" + TrackLayout.getWidth() + "\" " + ATTRIBUTE_NAME_LAYOUT_DIMENSION_HEIGHT + "=\"" + TrackLayout.getHeight() + "\" " + "/>");
                for (int i = 0; i < TrackLayout.getComponents().size(); i++) {
                    LayoutComponent currentComponent = TrackLayout.getComponents().get(i);
                    currentComponent.toXML(streamWriter);
                }
                streamWriter.write("\n</" + NODE_NAME_LAYOUT + ">");
                streamWriter.write("\n<" + NODE_NAME_CONTROL_TABLE + ">");
                for (int i = 0; i < ControlTable.getRoutes().size(); i++) {
                    ControlTable.getRoutes().get(i).toXML(streamWriter);
                }
                streamWriter.write("</" + NODE_NAME_CONTROL_TABLE + ">");
                streamWriter.write("\n</" + NODE_NAME_ROOT + ">");
                streamWriter.close();
            } catch (IOException e) {
                lastSerializeError = GENERAL_IO_ERROR + " " + e.getMessage();
            }
        } catch (FileNotFoundException e) {
            lastSerializeError = FILE_DOES_NOT_EXIST;
        }
        if (lastSerializeError == null) return true; else return false;
    }

    public static String escapeXML(String toEscape) {
        String escapedText = toEscape.replaceAll("&", "&amp;");
        escapedText = escapedText.replaceAll("\"", "&quot;");
        escapedText = escapedText.replaceAll("<", "&lt;");
        escapedText = escapedText.replaceAll(">", "&gt;");
        return escapedText;
    }

    public static String getLastError() {
        if (lastSerializeError == null) return "The operation completed successfully."; else return lastSerializeError;
    }
}
