package org.monet.backmobile.producer;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import org.monet.backmobile.content.NodeRepository;
import org.monet.backmobile.exception.DeserializeException;
import org.monet.backmobile.exception.SerializeException;
import org.monet.backmobile.model.node.ContainerNode;
import org.monet.backmobile.model.node.LightNode;
import org.monet.backmobile.model.node.Node;
import org.monet.backmobile.util.Log;
import org.monet.kernel.model.definition.ContainerDefinition;
import org.monet.kernel.model.definition.ContainerDefinitionBase.Contain;
import org.monet.kernel.model.definition.Definition;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Xml;

public class NodeContainerProducer {

    private static final String XML_NS = "";

    private static final String XML_CODE = "code";

    private static final String XML_VALUE = "value";

    private static final String XML_ATTRIBUTE = "attribute";

    private static final String XML_INDICATOR = "indicator";

    public ContainerNode fromCursor(Cursor cursor, String portId, Definition definition, NodeRepository repository) throws DeserializeException {
        int totalNumberOfChilds = 0;
        ContainerDefinition containerDefinition = (ContainerDefinition) definition;
        ContainerNode container = new ContainerNode();
        container.setId(cursor.getString(0));
        container.setLabel(cursor.getString(1));
        container.setDescription(cursor.getString(2));
        container.setDefinitionCode(definition.getCode());
        String content = cursor.getString(4);
        HashMap<String, String> childsMap = deserializeContent(content);
        List<LightNode> childs = container.getChilds();
        String nodeId;
        for (Contain contain : containerDefinition.getContainList()) {
            nodeId = childsMap.get(contain.getNode());
            childs.add(repository.getLightNode(portId, nodeId));
        }
        totalNumberOfChilds = containerDefinition.getContainList().size();
        container.setTotalNumberOfChilds(totalNumberOfChilds);
        container.setPartial(false);
        return container;
    }

    public ContentValues toContentValue(ContainerNode node) {
        return null;
    }

    public String serializeContent(List<LightNode> childNodes) throws SerializeException {
        StringWriter writer = new StringWriter();
        XmlSerializer serializer = Xml.newSerializer();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "attributelist");
            for (Node node : childNodes) {
                serializer.startTag("", "attribute");
                serializer.attribute("", "code", node.getDefinitionCode());
                serializer.startTag("", "indicatorlist");
                serializer.startTag("", "indicator");
                serializer.attribute("", "code", "value");
                serializer.text(String.valueOf(node.getId()));
                serializer.endTag("", "indicator");
                serializer.endTag("", "indicatorlist");
                serializer.endTag("", "attribute");
            }
            serializer.endTag("", "attributelist");
            serializer.endDocument();
            return writer.toString();
        } catch (Exception e) {
            Log.error(e);
            throw new SerializeException("Can't serialize the content of the Node", e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
    }

    private HashMap<String, String> deserializeContent(String content) throws DeserializeException {
        HashMap<String, String> childsMap = new HashMap<String, String>();
        try {
            StringReader reader = new StringReader(content);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(reader);
            int currentToken = parser.getEventType();
            String code = null;
            do {
                switch(currentToken) {
                    case XmlPullParser.START_TAG:
                        String tag = parser.getName();
                        if (tag.equals(XML_INDICATOR)) {
                            if (parser.getAttributeValue(XML_NS, XML_CODE).equals(XML_VALUE)) {
                                parser.next();
                                childsMap.put(code, parser.getText());
                            }
                        } else if (tag.equals(XML_ATTRIBUTE)) {
                            code = parser.getAttributeValue(XML_NS, XML_CODE);
                        }
                        break;
                }
            } while ((currentToken = parser.next()) != XmlPullParser.END_DOCUMENT);
        } catch (Exception e) {
            throw new DeserializeException("Can't deserialize the content of the Node.", e);
        }
        return childsMap;
    }
}
