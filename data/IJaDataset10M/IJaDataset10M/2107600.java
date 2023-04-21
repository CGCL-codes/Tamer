package org.antban.antser.mapping.std;

import org.antban.antser.NodeBuilderException;
import org.antban.antser.node.ValueNode;

/**
 * @author Dmitry Sorokin
 */
public class BooleanNodeBuilder extends AbstractValueNodeBuilder<Boolean> {

    @Override
    protected byte[] pack(Boolean value) throws NodeBuilderException {
        return new byte[] { value ? (byte) 1 : (byte) 0 };
    }

    @Override
    protected Boolean unpack(ValueNode node) throws NodeBuilderException {
        byte[] data = node.getData();
        if (data.length != 1) {
            throw new NodeBuilderException("Boolean value must be 1byte long, but " + data.length + " bytes read.");
        }
        return data[0] != 0;
    }

    @Override
    public Class<Boolean> getClazz() {
        return Boolean.class;
    }
}
