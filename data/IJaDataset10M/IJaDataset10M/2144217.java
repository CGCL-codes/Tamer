package uk.azdev.openfire.net.messages;

import java.nio.ByteBuffer;
import uk.azdev.openfire.net.attrvalues.AttributeMap;

public abstract class MapBasedMessage<MapKeyType, MapType extends AttributeMap<MapKeyType>> implements IMessage {

    protected MapType attrMap;

    public int getMessageContentSize() {
        MapType populatedMap = getPopulatedAttributeMap();
        return populatedMap.getSize();
    }

    private MapType getPopulatedAttributeMap() {
        if (attrMap == null) {
            attrMap = createAttributeMap();
            populateAttributeMap(attrMap);
        }
        return attrMap;
    }

    public void readMessageContent(ByteBuffer buffer) {
        attrMap = createAttributeMap();
        attrMap.read(buffer);
        interpretAttributeMap(attrMap);
    }

    protected abstract void interpretAttributeMap(MapType map);

    public void writeMessageContent(ByteBuffer buffer) {
        MapType populatedMap = getPopulatedAttributeMap();
        populatedMap.write(buffer);
    }

    protected abstract MapType createAttributeMap();

    protected abstract void populateAttributeMap(MapType map);
}
