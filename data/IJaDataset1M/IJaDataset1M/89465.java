package org.jforensics.ie;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

class IEIndexHashtable {

    List<ActivityLocator> activityPointers = new ArrayList<ActivityLocator>();

    private String id = null;

    private long size = 0;

    private long offset = 0;

    private int nextOffset = 0;

    public IEIndexHashtable(ByteBuffer buffer) {
        try {
            buffer.order(ByteOrder.nativeOrder());
            buffer.position(0);
            byte[] idArray = new byte[4];
            buffer.get(idArray);
            id = new String(idArray);
            int blocks = buffer.getInt(0x04);
            size = blocks * IEIndexParser.BLOCK_SIZE;
            nextOffset = buffer.getInt(0x08);
            buffer.position(0);
            for (int x = 0; (20 + (8 * x)) < size; x++) {
                int actFlag = buffer.getInt(16 + (8 * x));
                int actPointer = buffer.getInt(20 + (8 * x));
                ActivityLocator activityLoc = new ActivityLocator(actFlag, actPointer);
                if (activityLoc.isValid()) {
                    activityPointers.add(activityLoc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getNextOffset() {
        return nextOffset;
    }

    public List<ActivityLocator> getActivityPointerList() {
        return activityPointers;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("HashID: " + id + "\n");
        buffer.append("Size:   " + size + "\n");
        buffer.append("Offset: " + offset + "\n\n");
        return buffer.toString();
    }
}
