package com.impact.xbm.data.xml.batch.request;

import java.io.File;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import com.impact.qwicgui.event.QFileChangeEvent;
import com.impact.xbm.client.XbmClientUtils;
import com.impact.xbm.client.data.xml.XmlNodeDataPanel;
import com.impact.xbm.enums.XbmRequestType;
import com.impact.xbm.utils.Xml;

public class BatchRequest extends XmlNodeDataPanel {

    private static final String FILE_NAME_LABEL = "File";

    private static final String REQUEST_TYPE_LABEL = "Request Type";

    private static final File DATA_FOLDER = XbmClientUtils.getXbmDataDirectory();

    public String requestFileName;

    public XbmRequestType requestType;

    private JLabel typeLabel;

    public BatchRequest(Node node) {
        super(node);
    }

    protected String getDefaultDescriptor() {
        return "Batch Request";
    }

    public static Document getDocument(String fileName) {
        Document doc = null;
        try {
            doc = Xml.readXmlFromFile(DATA_FOLDER.getPath() + File.separator + fileName);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to read file " + fileName, "File Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(e);
        }
        return doc;
    }

    public Document getDocument() {
        return getDocument(this.requestFileName);
    }

    public void populateFromNode(Node node) {
        this.requestFileName = Xml.getAttributeValue(node, "filename");
        this.requestType = XbmRequestType.fromDisplayName(Xml.getAttributeValue(node, "type"));
        this.addDataListeners();
    }

    private void addDataListeners() {
    }

    public String toXmlString(int indentLevel) {
        String indent = getIndentString(indentLevel);
        String xml = indent + "<Request " + getAttributeXml("filename", this.requestFileName) + getAttributeXml("type", this.requestType.displayName) + " />" + LINE_SEPARATOR;
        return xml;
    }

    @Override
    public void fileChanged(String dataItemIdentifier, QFileChangeEvent event) {
        if (dataItemIdentifier.equals(FILE_NAME_LABEL)) {
            String fileName = modifiedFileSelection(event, true);
            if (fileName != null) {
                Document doc = getDocument(fileName);
                XbmRequestType reqType = null;
                if (doc != null) {
                    reqType = XbmClientUtils.getXbmRequestFileType(doc);
                    if (reqType == null) {
                        JOptionPane.showMessageDialog(null, "Selected file is not a valid XBM request file", "Invalid File Selected", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                this.requestType = reqType;
                this.requestFileName = fileName;
                this.typeLabel.setText(this.requestType.displayName);
            }
        }
    }

    protected void setupPanelContents() {
        addToPanel(this.getFileChooserJPanel(FILE_NAME_LABEL, DATA_FOLDER.getPath(), this.requestFileName, new FileSelectionListener(this, FILE_NAME_LABEL), "xml"));
        JPanel typePanel = this.getDisplayFieldJPanel(REQUEST_TYPE_LABEL, this.requestType.displayName);
        addToPanel(typePanel);
        this.typeLabel = getJPanelDisplayField(typePanel);
    }
}
