package org.translator.java.blocks;

import org.translator.java.code.CodeSegment;
import org.translator.java.code.Parameter;
import org.translator.java.code.Value;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author Joshua
 */
public class BlockConnector {

    private String connectorKind, connectorType, initType, label;

    private int conBlockId = -1;

    private Block connectedBlock = null;

    public BlockConnector(Node blockConnector) {
        load(blockConnector);
    }

    protected BlockConnector() {
    }

    public boolean hasConnectedBlock() {
        return connectedBlock != null;
    }

    public final String getLabel() {
        return new String(label);
    }

    public final Value getValue() {
        return null;
    }

    public String getDataType() {
        if (connectedBlock != null) return connectedBlock.getDataType(); else return null;
    }

    public String getConnectorKind() {
        return new String(connectorKind);
    }

    public Block getConnectedBlock() {
        return connectedBlock;
    }

    public Parameter getParameter(int iArg) {
        if (hasConnectedBlock()) {
            CodeSegment segment = connectedBlock.toCode();
            if (segment instanceof Parameter) {
                Parameter p = (Parameter) segment;
                p.setIndex(iArg);
                return p;
            }
        }
        return null;
    }

    public Collection<Value> getConstructorParameters() {
        if (connectedBlock != null) return connectedBlock.getConstructorParameters(); else return new ArrayList<Value>();
    }

    protected void load(Node blockConnector) {
        NamedNodeMap attributes = blockConnector.getAttributes();
        connectorKind = attributes.getNamedItem("connector-kind").getNodeValue();
        connectorType = attributes.getNamedItem("connector-type").getNodeValue();
        initType = attributes.getNamedItem("init-type").getNodeValue();
        label = attributes.getNamedItem("label").getNodeValue();
        Node blockId = attributes.getNamedItem("con-block-id");
        if (blockId != null) conBlockId = Integer.valueOf(blockId.getNodeValue());
    }

    protected void setReferences(HashMap<Integer, Block> blocksMap) {
        if (conBlockId != -1) connectedBlock = blocksMap.get(conBlockId);
    }
}
