package net.sf.ninjakore.packetdata;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class PacketFactory {

    private final ClientVersion version;

    private final Map<Integer, PacketBuilder> idRegistry;

    private final Map<Class<? extends PacketData>, PacketBuilder> classRegistry;

    private final Map<String, PacketBuilder> nameRegistry;

    public PacketFactory(ClientVersion version) {
        this.version = version;
        idRegistry = new HashMap<Integer, PacketBuilder>();
        nameRegistry = new HashMap<String, PacketBuilder>();
        classRegistry = new HashMap<Class<? extends PacketData>, PacketBuilder>();
        populateRegistries();
    }

    /**
	 * Marshals a {@link PacketData} into a bytestream
	 * 
	 * @param data
	 * @return
	 */
    public byte[] marshal(PacketData data) {
        PacketBuilder builder = classRegistry.get(data.getClass());
        return builder.marshal(data);
    }

    /**
	 * Helper function to unmarshal a byte stream by inspecting the stream and
	 * getting the packet id
	 * 
	 * @param stream
	 * @return
	 */
    public <T extends PacketData> T unmarshal(byte[] stream) {
        Integer id = Integer.valueOf(ByteBuffer.wrap(stream).order(ByteOrder.LITTLE_ENDIAN).getShort());
        PacketBuilder builder = getBuilderById(id);
        return builder.unmarshal(stream);
    }

    /**
	 * Helper function to unmarshal a byte stream by the packet's name
	 * 
	 * @param name
	 * @param stream
	 * @return
	 */
    public <T extends PacketData> T unmarshal(String name, byte[] stream) {
        PacketBuilder builder = getBuilderByName(name);
        return builder.unmarshal(stream);
    }

    /**
	 * Helper function to unmarshal a byte stream by the packet's class
	 * 
	 * @param clazz
	 * @param stream
	 * @return
	 */
    public <T extends PacketData> T unmarshal(Class<T> clazz, byte[] stream) {
        PacketBuilder builder = getBuilderForClass(clazz);
        return builder.unmarshal(stream);
    }

    /**
	 * Retrieves the {@link PacketBuilder} by the name of the {@link PacketData}
	 * it builds
	 * 
	 * @param name
	 * @return
	 */
    public PacketBuilder getBuilderByName(String name) {
        PacketBuilder builder = nameRegistry.get(name);
        if (builder == null) throw new IllegalStateException("builder for " + name + " not found");
        return builder;
    }

    /**
	 * Retrieves the {@link PacketBuilder} by the class of the
	 * {@link PacketData} it builds
	 * 
	 * @param name
	 * @return
	 */
    public PacketBuilder getBuilderForClass(Class<? extends PacketData> clazz) {
        PacketBuilder builder = classRegistry.get(clazz);
        if (builder == null) throw new IllegalStateException("builder for " + clazz + " not found");
        return builder;
    }

    /**
	 * Retrieves the {@link PacketBuilder} by the id of the {@link PacketData}
	 * it builds
	 * 
	 * @param name
	 * @return
	 */
    public PacketBuilder getBuilderById(Integer id) {
        PacketBuilder builder = idRegistry.get(id);
        if (builder == null) throw new IllegalStateException("builder for " + id + " not found");
        return builder;
    }

    public String getBuilderNameById(Integer id) {
        String name = idRegistry.get(id).getClass().getSimpleName();
        if (name == null) throw new IllegalStateException(id + " not found");
        return name;
    }

    public Class<? extends PacketBuilder> getBuilderClassById(Integer id) {
        Class<? extends PacketBuilder> clazz = idRegistry.get(id).getClass();
        if (clazz == null) throw new IllegalStateException(id + " not found");
        return clazz;
    }

    private void populateRegistries() {
        registerBuilder(new SyncResponseBuilder(version, 0x7f));
        registerBuilder(new AccountServerLoginBuilder(version, 0x64));
        registerBuilder(new WorldListBuilder(version, 0x69));
        registerBuilder(new CharacterServerLoginBuilder(version, 0x65));
        registerBuilder(new CharacterListBuilder(version, 0x6B));
        registerBuilder(new CharacterChoiceBuilder(version, 0x66));
        registerBuilder(new MapServerInfoBuilder(version, 0x71));
    }

    private void registerBuilder(PacketBuilder builder) {
        idRegistry.put(builder.getId(), builder);
        classRegistry.put(builder.packetClass(), builder);
        nameRegistry.put(builder.packetClass().getSimpleName(), builder);
    }
}
