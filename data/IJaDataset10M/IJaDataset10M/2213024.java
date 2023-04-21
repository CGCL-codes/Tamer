package net.asgarli.abalone.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * This class represents individual encoded Unit to be used by "client" side of
 * the application. Thus, this unit is supposed to store unencrypted(!)
 * information.
 * 
 * @author Elchin
 */
public class Unit implements Marshallable {

    private static final long serialVersionUID = 1L;

    /** ID of the Unit. */
    private Long id;

    /** Entries. */
    private List<UnitEntry<?>> unitEntries = Lists.newArrayList();

    /**
     * Map of local attachment IDs to global attachment IDs (The ones stored
     * globally in Vault.
     */
    private Map<Long, Long> attachments = Maps.newHashMap();

    /**
     * Inserts new global and local attachment ID mapping.
     * 
     * @param localId
     *            local ID
     * @param globalId
     *            global ID
     */
    public void putAttachmentMap(Long localId, Long globalId) {
        attachments.put(localId, globalId);
    }

    /**
     * Returns the global attachment ID that matches given local attachment ID.
     * 
     * @param localId
     *            The local attachment ID for which global attachment ID has to
     *            be returned.
     * @return the global attachment ID that matches given local attachment ID.
     */
    public Long getGlobalId(Long localId) {
        return attachments.get(localId);
    }

    @Override
    public byte[] writeToBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeLong(id);
        dos.writeInt(unitEntries.size());
        for (UnitEntry<?> ue : unitEntries) {
            byte[] upInBytes = ue.writeToBytes();
            dos.writeInt(upInBytes.length);
            dos.write(upInBytes);
        }
        dos.writeInt(attachments.size());
        for (Long localId : attachments.keySet()) {
            dos.writeLong(localId);
            dos.writeLong(attachments.get(localId));
        }
        return bos.toByteArray();
    }

    @Override
    public void restoreFromBytes(byte[] bytes) throws IOException {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
        id = in.readLong();
        int unitEntriesSize = in.readInt();
        unitEntries.clear();
        for (int i = 0; i < unitEntriesSize; i++) {
            int size = in.readInt();
            byte[] ueInByte = new byte[size];
            in.read(ueInByte);
            UnitEntry<Object> ue = new UnitEntry<Object>();
            ue.restoreFromBytes(ueInByte);
            unitEntries.add(ue);
        }
        int attachmentsSize = in.readInt();
        attachments.clear();
        for (int i = 0; i < attachmentsSize; i++) {
            Long localId = in.readLong();
            Long globalId = in.readLong();
            attachments.put(localId, globalId);
        }
    }

    public void addEntry(UnitEntry<?> unitEntry) {
        unitEntries.add(unitEntry);
    }

    public void addEntries(UnitEntry<?>... entry) {
        unitEntries.addAll(Arrays.asList(entry));
    }

    public void removeEntry(UnitEntry<?> unitEntry) {
        unitEntries.remove(unitEntry);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UnitEntry<?>> getEntries() {
        return unitEntries;
    }

    public void setEntries(List<UnitEntry<?>> unitEntries) {
        this.unitEntries = unitEntries;
    }

    public Map<Long, Long> getAttachmentIds() {
        return attachments;
    }

    public void setAttachmentIds(Map<Long, Long> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Unit [id=");
        builder.append(id);
        builder.append(", unitEntries=");
        builder.append(unitEntries);
        builder.append(", attachments=");
        builder.append(attachments);
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((attachments == null) ? 0 : attachments.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((unitEntries == null) ? 0 : unitEntries.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Unit)) return false;
        Unit other = (Unit) obj;
        if (attachments == null) {
            if (other.attachments != null) return false;
        } else if (!attachments.equals(other.attachments)) return false;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (unitEntries == null) {
            if (other.unitEntries != null) return false;
        } else if (!unitEntries.equals(other.unitEntries)) return false;
        return true;
    }
}
