package com.impact.xbm.data.xml.batch.request;

import java.util.ArrayList;
import org.w3c.dom.Node;
import com.impact.xbm.client.data.xml.XmlNodeDataPanel;
import com.impact.xbm.utils.Xml;

public class BatchJob extends XmlNodeDataPanel {

    public ArrayList<BatchRequest> requests;

    public BatchJob(Node node) {
        super(node);
    }

    protected String getDefaultDescriptor() {
        return "Batch Job";
    }

    public void populateFromNode(Node node) {
        this.requests = new ArrayList<BatchRequest>();
        ArrayList<Node> requestNodes = Xml.getChildNodes(node, "Request");
        int numRequests = (requestNodes != null ? requestNodes.size() : 0);
        for (int i = 0; i < numRequests; i++) {
            this.requests.add(new BatchRequest(requestNodes.get(i)));
        }
        this.addDataListeners();
    }

    private void addDataListeners() {
        for (BatchRequest request : this.requests) {
            request.addDataModifiedListener(this);
        }
    }

    public String toXmlString(int indentLevel) {
        String indent = getIndentString(indentLevel);
        String xml = indent + "<XbmBatchJob>" + LINE_SEPARATOR;
        if ((this.requests != null) && (this.requests.size() > 0)) {
            for (BatchRequest request : this.requests) {
                xml += request.toXmlString(indentLevel + 1);
            }
        }
        xml += indent + "</XbmBatchJob>" + LINE_SEPARATOR;
        return xml;
    }

    protected void setupPanelContents() {
        addToPanel(this.requests);
    }
}
