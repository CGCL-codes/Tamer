package org.xngr.context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import junit.framework.TestCase;
import org.bounce.xml.NamespaceContextMap;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.xngr.Action;
import org.xngr.NodeMarker;
import org.xngr.NodeService;
import org.xngr.Service;

public class NodeContextTest extends TestCase {

    public void testDocumentContextMarkersUpdated() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
    }

    public void testAddSameNodeMarker() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAttributeMarker schemaAttributeMarker = new SchemaAttributeMarker();
        manager.add(schemaAttributeMarker);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 23, context.getNodeContexts().size());
        assertEquals("#Markers added to node-context", 1, context.getNodeContexts().get(4).getNodeMarkers().size());
        schemaAttributeMarker = new SchemaAttributeMarker();
        manager.add(schemaAttributeMarker);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 23, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(4).getNodeMarkers().size());
    }

    /**
	 * Multiple node markers added for the same node.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
    public void testAddMultipleNodeMarkersSameNodes() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        SchemaElementMarker2 schemaElementMarker2 = new SchemaElementMarker2();
        manager.add(schemaElementMarker);
        NodeContext nodeContext = context.getNodeContexts().get(0);
        assertEquals("Node-Context Name", "Element: name = service-description", nodeContext.getName());
        assertEquals("Node-Context Description", "Schema Element Viewer", nodeContext.getDescription());
        assertEquals("Node-Context Icon", null, nodeContext.getIcon());
        manager.add(schemaElementMarker2);
        assertEquals("#Markers added to manager", 2, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 2, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        nodeContext = context.getNodeContexts().get(0);
        assertEquals("#Node-Context Markers", 2, nodeContext.getNodeMarkers().size());
        assertEquals("#Node-Context Markers", 2, nodeContext.getMarkers().size());
        assertEquals("Node-Context Name", "Element: name = service-description", nodeContext.getName());
        assertEquals("Node-Context Description", "Schema Element Viewer", nodeContext.getDescription());
    }

    /**
	 * Multiple node markers added for different nodes.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
    public void testAddMultipleNodeMarkersDifferentNodes() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        SchemaAttributeMarker schemaAttributeMarker = new SchemaAttributeMarker();
        manager.add(schemaAttributeMarker);
        assertEquals("#Markers added to manager", 2, manager.getMarkers().size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 2, context.getNodeMarkers().size());
        assertEquals("#Node contexts", 56, context.getNodeContexts().size());
        assertEquals("#Markers added to node-context", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
    }

    public void testDocumentContextServicesUpdatedOneMarkerInstance() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        SchemaNodeServiceImpl schemaElementService = new SchemaNodeServiceImpl();
        schemaElementService.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 1, manager.getServices().size());
        assertEquals("#Markers added to manager", 1, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getServices().size());
        context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
    }

    public void testDocumentContextServicesUpdatedTwoMarkerInstances() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        schemaElementMarker = new SchemaElementMarker();
        SchemaNodeServiceImpl schemaElementService = new SchemaNodeServiceImpl();
        schemaElementService.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 1, manager.getServices().size());
        assertEquals("#Markers added to manager", 1, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
        context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
    }

    public void testAddSameNodeService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        SchemaNodeServiceImpl schemaElementService = new SchemaNodeServiceImpl();
        schemaElementService.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 1, manager.getServices().size());
        assertEquals("#Markers added to manager", 1, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
        schemaElementService.getMarkers().add(schemaElementMarker);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 1, manager.getServices().size());
        assertEquals("#Markers added to manager", 1, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
    }

    /**
	 * Multiple node services added for the same node.
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
    public void testAddMultipleNodeServicesSameNode() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        SchemaNodeServiceImpl schemaElementService = new SchemaNodeServiceImpl();
        schemaElementService.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 1, manager.getServices().size());
        assertEquals("#Markers added to manager", 1, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 1, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 1, context.getNodeContexts().get(0).getNodeServices().size());
        SchemaNodeServiceImpl2 schemaElementService2 = new SchemaNodeServiceImpl2();
        schemaElementService2.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService2);
        assertEquals("#Markers added to manager", 1, manager.getMarkers().size());
        assertEquals("#Markers added to manager", 2, manager.getServices().size());
        assertEquals("#Markers added to manager", 2, manager.getServices(schemaElementMarker).size());
        assertEquals("#Document Markers added to document-context", 0, context.getDocumentMarkers().size());
        assertEquals("#Node Markers added to document-context", 1, context.getNodeMarkers().size());
        assertEquals("#Node services added to document-context", 2, context.getNodeServices().size());
        assertEquals("#Node contexts", 33, context.getNodeContexts().size());
        assertEquals("#Node-Context Markers", 1, context.getNodeContexts().get(0).getNodeMarkers().size());
        assertEquals("#Node-Context Services", 2, context.getNodeContexts().get(0).getNodeServices().size());
    }

    public void testRunElementService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaElementMarker schemaElementMarker = new SchemaElementMarker();
        manager.add(schemaElementMarker);
        SchemaNodeServiceImpl schemaElementService = new SchemaNodeServiceImpl();
        schemaElementService.getMarkers().add(schemaElementMarker);
        manager.add(schemaElementService);
        assertEquals("Not executed yet", null, schemaElementService.getExecutedValue());
        context.getNodeContexts().get(0).open(schemaElementService);
        assertEquals("Executed", "Element: name = service-description", schemaElementService.getExecutedValue());
    }

    public void testRunAttributeService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAttributeMarker schemaAttributeMarker = new SchemaAttributeMarker();
        manager.add(schemaAttributeMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaAttributeMarker);
        manager.add(schemaNodeService);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        context.getNodeContexts().get(0).open(schemaNodeService);
        assertEquals("Executed", "Attribute: name = service-description", schemaNodeService.getExecutedValue());
    }

    public void testRunTextService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaTextMarker schemaTextMarker = new SchemaTextMarker();
        manager.add(schemaTextMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaTextMarker);
        manager.add(schemaNodeService);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        context.getNodeContexts().get(0).open(schemaNodeService);
        assertEquals("Executed", "Text: [Example Service] A short description of the service, displayed on the Desktop.", schemaNodeService.getExecutedValue());
    }

    public void testRunCommentService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaCommentMarker schemaCommentMarker = new SchemaCommentMarker();
        manager.add(schemaCommentMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaCommentMarker);
        manager.add(schemaNodeService);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        assertEquals("# Comment Nodes", 2, context.getNodeContexts().size());
        context.getNodeContexts().get(0).open(schemaNodeService);
        assertEquals("Executed", "Comment: Comment 1", schemaNodeService.getExecutedValue());
    }

    public void testRunCommentAllService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAllCommentMarker schemaCommentMarker = new SchemaAllCommentMarker();
        manager.add(schemaCommentMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaCommentMarker);
        manager.add(schemaNodeService);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        assertEquals("# Comment Nodes", 4, context.getNodeContexts().size());
        context.getNodeContexts().get(0).open(schemaNodeService);
        assertEquals("Executed", "Comment: eXchaNGeR Service v0.2", schemaNodeService.getExecutedValue());
    }

    public void testRunPIService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaPIMarker schemaPIMarker = new SchemaPIMarker();
        manager.add(schemaPIMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaPIMarker);
        manager.add(schemaNodeService);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        assertEquals("# PI Nodes", 2, context.getNodeContexts().size());
        context.getNodeContexts().get(0).open(schemaNodeService);
        assertEquals("Executed", "Processing Instruction: test=\"pi2\"", schemaNodeService.getExecutedValue());
    }

    public void testRunPIAllService() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAllPIMarker schemaPIMarker = new SchemaAllPIMarker();
        manager.add(schemaPIMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaPIMarker);
        manager.add(schemaNodeService);
        manager.add(new SchemaNodeServiceImpl2());
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        assertEquals("# PI Nodes", 3, context.getNodeContexts().size());
        assertEquals("# services", 2, context.getNodeContexts().get(0).getNodeServices().size());
        context.getNodeContexts().get(0).open(schemaNodeService, new HashMap<String, String>());
        context.getNodeContexts().get(0).getNodeServices().size();
        assertEquals("Executed", "Processing Instruction: test=\"pi1\"", schemaNodeService.getExecutedValue());
    }

    public void testServiceListener() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAllPIMarker schemaPIMarker = new SchemaAllPIMarker();
        manager.add(schemaPIMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaPIMarker);
        manager.add(schemaNodeService);
        DefaultServiceContextListener listener = new DefaultServiceContextListener();
        context.getNodeContexts().get(2).addServiceContextListener(listener);
        NodeMarker marker = new SchemaPIMarker();
        manager.add(marker);
        assertEquals("Marker Event Type", MarkersChangedEvent.Type.ADDED, listener.markerEvent.getType());
        assertEquals("Marker Event Context", context.getNodeContexts().get(2), listener.markerEvent.getContext());
        assertEquals("Marker Event Source", marker, listener.markerEvent.getSource());
        manager.remove(marker);
        assertEquals("Marker Event Type", MarkersChangedEvent.Type.REMOVED, listener.markerEvent.getType());
        assertEquals("Marker Event Context", context.getNodeContexts().get(2), listener.markerEvent.getContext());
        assertEquals("Marker Event Source", marker, listener.markerEvent.getSource());
        assertEquals("Marker Event Source", marker, listener.markerEvent.getMarker());
        listener.markerEvent = null;
        marker = new SchemaElementMarker();
        manager.add(marker);
        assertNull("Marker Event Type", listener.markerEvent);
        manager.remove(marker);
        assertNull("Marker Event Type", listener.markerEvent);
        SchemaNodeServiceImpl2 schemaNodeService2 = new SchemaNodeServiceImpl2();
        schemaNodeService2.getMarkers().add(schemaPIMarker);
        assertNull("Service Event", listener.serviceEvent);
        manager.add(schemaNodeService2);
        assertEquals("Service Event", ServiceContextEvent.Type.ADDED, listener.serviceEvent.getType());
        listener.serviceEvent = null;
        manager.remove(schemaNodeService2);
        assertEquals("Service Event", ServiceContextEvent.Type.REMOVED, listener.serviceEvent.getType());
        schemaNodeService2 = new SchemaNodeServiceImpl2();
        manager.add(schemaNodeService2);
        assertEquals("Service Event Type", ServiceContextEvent.Type.ADDED, listener.serviceEvent.getType());
        assertEquals("Service Event Context", context.getNodeContexts().get(2), listener.serviceEvent.getContext());
        assertEquals("Service Event Source", schemaNodeService2, listener.serviceEvent.getSource());
        assertEquals("Service Event Source", schemaNodeService2, listener.serviceEvent.getService());
        System.out.println(listener.serviceEvent.toString());
        context.getNodeContexts().get(2).removeServiceContextListener(listener);
        listener.serviceEvent = null;
        manager.add(schemaNodeService2);
        assertEquals("Service Event", null, listener.serviceEvent);
        assertEquals("Not executed yet", null, schemaNodeService.getExecutedValue());
        assertEquals("# PI Nodes", 3, context.getNodeContexts().size());
        assertEquals("# services", 2, context.getNodeContexts().get(0).getNodeServices().size());
        context.getNodeContexts().get(0).open(schemaNodeService, new HashMap<String, String>());
        assertEquals("Executed", "Processing Instruction: test=\"pi1\"", schemaNodeService.getExecutedValue());
    }

    public void testActions() throws IOException, URISyntaxException {
        ServiceContextManager manager = new ServiceContextManager();
        DocumentContext context = (DocumentContext) manager.getContext(getClass().getResource("/schema.xsd").toURI());
        SchemaAllPIMarker schemaPIMarker = new SchemaAllPIMarker();
        manager.add(schemaPIMarker);
        SchemaNodeServiceImpl schemaNodeService = new SchemaNodeServiceImpl();
        schemaNodeService.getMarkers().add(schemaPIMarker);
        manager.add(schemaNodeService);
        SchemaAction action = new SchemaAction(schemaNodeService);
        DefaultServiceContextListener listener = new DefaultServiceContextListener();
        context.getNodeContexts().get(2).addServiceContextListener(listener);
        manager.add(action);
        assertEquals("Action Event Type", ActionsChangedEvent.Type.ADDED, listener.actionEvent.getType());
        assertEquals("Action Event Context", context.getNodeContexts().get(2), listener.actionEvent.getContext());
        assertEquals("Action Event Source", action, listener.actionEvent.getSource());
        assertEquals("Actions", 1, manager.getActions(schemaNodeService).size());
        assertEquals("Actions", 1, context.getNodeContexts().get(2).getActions().size());
        context.getNodeContexts().get(2).open(action);
        assertEquals("Executed", "Processing Instruction: test=\"pi3\"", schemaNodeService.getExecutedValue());
        manager.remove(action);
        assertEquals("Action Event Type", ActionsChangedEvent.Type.REMOVED, listener.actionEvent.getType());
        assertEquals("Action Event Context", context.getNodeContexts().get(2), listener.actionEvent.getContext());
        assertEquals("Action Event Source", action, listener.actionEvent.getSource());
        assertEquals("Actions", 0, manager.getActions(schemaNodeService).size());
        assertEquals("Actions", 0, context.getNodeContexts().get(2).getActions().size());
        listener.actionEvent = null;
        assertNotNull("Manager", context.getNodeContexts().get(2).getServiceContextManager());
        context.getNodeContexts().get(2).dispose();
        assertNull("Manager", context.getNodeContexts().get(2).getServiceContextManager());
    }

    private static class SchemaNodeServiceImpl2 extends SchemaNodeServiceImpl {

        public String getIdentifier() {
            return "org.xngr.service.SchemaElementViewer2";
        }
    }

    private static class SchemaNodeServiceImpl implements NodeService {

        private String value = null;

        private List<NodeMarker> markers = null;

        public SchemaNodeServiceImpl() {
            markers = new ArrayList<NodeMarker>();
        }

        @Override
        public List<NodeMarker> getMarkers() {
            return markers;
        }

        public boolean close() {
            return false;
        }

        public String getIdentifier() {
            return "org.xngr.service.SchemaElementViewer";
        }

        public String getDescription() {
            return null;
        }

        public ImageIcon getIcon() {
            return null;
        }

        public String getExecutedValue() {
            return value;
        }

        public String getName() {
            return "Schema Element Viewer";
        }

        public void open(Node node, Map<String, String> arguments) {
            value = markers.get(0).getName(node);
        }

        @Override
        public Map<String, Object> getProperties() {
            return null;
        }
    }

    private static class SchemaElementMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaElementMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//xs:element");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Element Viewer";
        }

        public String getName(Node node) {
            Node attribute = node.getAttributes().getNamedItem("name");
            if (attribute != null) {
                return "Element: name = " + attribute.getTextContent();
            }
            attribute = node.getAttributes().getNamedItem("ref");
            return "Element: ref = " + attribute.getTextContent();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaElementMarker2 implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaElementMarker2";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//xs:element");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Element Viewer No 2";
        }

        public String getName(Node node) {
            Node attribute = node.getAttributes().getNamedItem("name");
            if (attribute != null) {
                return "Element 2: name = " + attribute.getTextContent();
            }
            attribute = node.getAttributes().getNamedItem("ref");
            return "Element 2: ref = " + attribute.getTextContent();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaAttributeMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaAttributeMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//@name");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Attribute Viewer";
        }

        public String getName(Node node) {
            return "Attribute: name = " + node.getTextContent();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaTextMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaTextMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//xs:documentation/text()");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Text Viewer";
        }

        public String getName(Node node) {
            return "Text: " + node.getTextContent().trim();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaCommentMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaCommentMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("/xs:schema//comment()");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Comment Viewer";
        }

        public String getName(Node node) {
            if (node instanceof CharacterData) {
                return "Comment: " + ((CharacterData) node).getData().trim();
            }
            return node.toString();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaPIMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaPIMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("/xs:schema//processing-instruction()");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema PI Viewer";
        }

        public String getName(Node node) {
            if (node instanceof ProcessingInstruction) {
                return "Processing Instruction: " + ((ProcessingInstruction) node).getData().trim();
            }
            return node.toString();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaAllCommentMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaAllCommentMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//comment()");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema Comment Viewer";
        }

        public String getName(Node node) {
            if (node instanceof CharacterData) {
                return "Comment: " + ((CharacterData) node).getData().trim();
            }
            return node.toString();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaAllPIMarker implements NodeMarker {

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaAllPIMarker";
        }

        public XPathExpression getXPathExpression() {
            XPathExpression expression = null;
            XPath path = XPathFactory.newInstance().newXPath();
            NamespaceContextMap context = new NamespaceContextMap();
            context.put("xs", "http://www.w3.org/2001/XMLSchema");
            path.setNamespaceContext(context);
            try {
                expression = path.compile("//processing-instruction()");
            } catch (XPathExpressionException e) {
                e.printStackTrace();
            }
            return expression;
        }

        public List<QName> getRootElementNames() {
            return Arrays.asList(new QName[] { new QName("http://www.w3.org/2001/XMLSchema", "schema") });
        }

        public String getDescription(Node node) {
            return "Schema PI Viewer";
        }

        public String getName(Node node) {
            if (node instanceof ProcessingInstruction) {
                return "Processing Instruction: " + ((ProcessingInstruction) node).getData().trim();
            }
            return node.toString();
        }

        public ImageIcon getIcon(Node node) {
            return null;
        }

        @Override
        public Map<String, Object> getProperties(Node node) {
            return null;
        }
    }

    private static class SchemaAction implements Action {

        Service service = null;

        public SchemaAction(Service service) {
            this.service = service;
        }

        @Override
        public String getIdentifier() {
            return "org.xngr.service.test.SchemaAction";
        }

        @Override
        public Map<String, String> getArguments() {
            return new HashMap<String, String>();
        }

        @Override
        public String getDescription() {
            return "Schema Action";
        }

        @Override
        public Icon getIcon() {
            return null;
        }

        @Override
        public String getName() {
            return "Schema Action";
        }

        @Override
        public Map<String, Object> getProperties() {
            return new HashMap<String, Object>();
        }

        @Override
        public Service getService() {
            return service;
        }
    }

    private static class DefaultServiceContextListener implements ServiceContextListener {

        public ServicesChangedEvent serviceEvent = null;

        public MarkersChangedEvent markerEvent = null;

        public ActionsChangedEvent actionEvent = null;

        @Override
        public void servicesChanged(ServicesChangedEvent event) {
            System.out.println("servicesChanged(" + event + ")");
            serviceEvent = event;
        }

        @Override
        public void markersChanged(MarkersChangedEvent event) {
            System.out.println("markersChanged(" + event + ")");
            markerEvent = event;
        }

        @Override
        public void actionsChanged(ActionsChangedEvent event) {
            System.out.println("actionsChanged(" + event + ")");
            actionEvent = event;
        }
    }
}
