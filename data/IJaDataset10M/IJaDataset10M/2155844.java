package ch.iserver.ace.collaboration.jupiter.server.document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SimplePartitioner implements DocumentPartitioner {

    private List partitions = new ArrayList();

    public void documentUpdated(DocumentEvent e) {
        final int length = e.getLength();
        final int offset = e.getOffset();
        final String text = e.getText();
        if (text.length() == 0) {
            removeUpdate(offset, length);
        } else if (length == 0) {
            insertUpdate(offset, text, e.getAttributes());
        } else {
            replaceUpdate(offset, length, text, e.getAttributes());
        }
    }

    protected void removeUpdate(int offset, int length) {
        int[] range = findRange(offset, length, partitions);
        int p0 = range[0];
        int p1 = range[1];
        assert p0 <= p1 : "p0 > p1";
        if (p0 == p1) {
            AttributedRegionImpl region = (AttributedRegionImpl) partitions.get(p0);
            region.length -= length;
            updateOffsets(p0, -length);
        } else {
            AttributedRegionImpl first = (AttributedRegionImpl) partitions.get(p0);
            AttributedRegionImpl last = (AttributedRegionImpl) partitions.get(p1);
            for (int i = p0 + 1; i < p1; i++) {
                partitions.remove(p0 + 1);
            }
            if (first.getAttributes().equals(last.getAttributes())) {
                partitions.remove(p0 + 1);
                first.length -= first.getEnd() - offset;
                first.length -= (offset + length) - last.getEnd();
                updateOffsets(p0, -length);
            } else {
                first.length -= first.getEnd() - offset;
                last.length -= last.getEnd() - (offset + length);
                last.offset = first.getEnd();
                updateOffsets(p0 + 1, -length);
            }
        }
    }

    protected void insertUpdate(int offset, String text, Map attributes) {
        int[] range = findRange(offset, 0, partitions);
        int p0 = range[0];
        int p1 = range[1];
        assert (p0 == p1 || p0 + 1 == p1) : "insert can affect at most two positions";
        if (p0 == -1) {
            AttributedRegionImpl inserted = new AttributedRegionImpl(offset, text.length(), attributes);
            partitions.add(inserted);
        } else if (p0 == p1) {
            AttributedRegionImpl region = (AttributedRegionImpl) partitions.get(p0);
            if (region.attributes.equals(attributes)) {
                region.length += text.length();
                updateOffsets(p0, text.length());
            } else if (offset == region.offset) {
                AttributedRegionImpl inserted = new AttributedRegionImpl(offset, text.length(), attributes);
                partitions.add(p0, inserted);
                updateOffsets(p0, text.length());
            } else if (offset == region.getEnd()) {
                AttributedRegionImpl inserted = new AttributedRegionImpl(offset, text.length(), attributes);
                partitions.add(p0 + 1, inserted);
                updateOffsets(p0 + 1, text.length());
            } else {
                AttributedRegionImpl inserted = new AttributedRegionImpl(offset, text.length(), attributes);
                int oldLength = region.length;
                region.length = offset - region.offset;
                partitions.add(p0 + 1, inserted);
                AttributedRegionImpl fragment = new AttributedRegionImpl(offset + text.length(), oldLength - region.length, region.getAttributes());
                partitions.add(p0 + 2, fragment);
                updateOffsets(p0 + 2, text.length());
            }
        } else if (p0 < p1) {
            AttributedRegionImpl first = (AttributedRegionImpl) partitions.get(p0);
            AttributedRegionImpl second = (AttributedRegionImpl) partitions.get(p1);
            if (first.attributes.equals(attributes)) {
                first.length += text.length();
                updateOffsets(p0, text.length());
            } else if (second.attributes.equals(attributes)) {
                second.length += text.length();
                updateOffsets(p1, text.length());
            } else {
                AttributedRegionImpl inserted = new AttributedRegionImpl(offset, text.length(), attributes);
                partitions.add(p1, inserted);
                updateOffsets(p1, text.length());
            }
        } else {
            throw new RuntimeException("p0 > p1");
        }
    }

    protected void replaceUpdate(int offset, int length, String text, Map attributes) {
        removeUpdate(offset, length);
        insertUpdate(offset, text, attributes);
    }

    private void updateOffsets(int idx, int delta) {
        for (int i = idx + 1; i < partitions.size(); i++) {
            AttributedRegionImpl region = (AttributedRegionImpl) partitions.get(i);
            region.offset += delta;
        }
    }

    protected static int[] findRange(int offset, int length, List partitions) {
        int start = 0;
        int end = 0;
        int left = 0;
        int right = partitions.size();
        if (right == 0) {
            return new int[] { -1, -1 };
        } else if (offset == getLength(partitions)) {
            return new int[] { right - 1, right - 1 };
        }
        while (left <= right) {
            int mid = (right + left) / 2;
            Region region = (Region) partitions.get(mid);
            if (isAtStart(offset, region)) {
                start = mid == 0 ? 0 : mid - 1;
                break;
            } else if (isWithin(offset, region)) {
                start = mid;
                break;
            } else if (isAtEnd(offset, region)) {
                start = mid;
                break;
            } else if (isAfter(offset, region)) {
                left = mid + 1;
            } else if (isBefore(offset, region)) {
                right = mid - 1;
            }
        }
        left = start;
        right = partitions.size();
        while (left <= right) {
            int mid = (right + left) / 2;
            Region region = (Region) partitions.get(mid);
            if (isAtStart(offset + length, region)) {
                end = mid;
                break;
            } else if (isWithin(offset + length, region)) {
                end = mid;
                break;
            } else if (isAtEnd(offset + length, region)) {
                end = mid == partitions.size() - 1 ? mid : mid + 1;
                break;
            } else if (isAfter(offset + length, region)) {
                left = mid + 1;
            } else if (isBefore(offset, region)) {
                right = mid - 1;
            }
        }
        return new int[] { start, end };
    }

    private static int getLength(List regions) {
        int length = 0;
        Iterator it = regions.iterator();
        while (it.hasNext()) {
            Region region = (Region) it.next();
            length += region.getLength();
        }
        return length;
    }

    protected static boolean isWithin(int offset, Region region) {
        return offset > region.getStart() && offset < region.getEnd();
    }

    protected static boolean isAfter(int offset, Region region) {
        return offset > region.getEnd();
    }

    protected static boolean isBefore(int offset, Region region) {
        return offset < region.getStart();
    }

    protected static boolean isAtStart(int offset, Region region) {
        return offset == region.getStart();
    }

    protected static boolean isAtEnd(int offset, Region region) {
        return offset == region.getEnd();
    }

    public AttributedRegion[] getRegions() {
        return (AttributedRegion[]) partitions.toArray(new AttributedRegion[partitions.size()]);
    }

    private static class AttributedRegionImpl implements AttributedRegion, Comparable {

        private int offset;

        private int length;

        private Map attributes;

        private AttributedRegionImpl(int offset, int length, Map attributes) {
            this.offset = offset;
            this.length = length;
            this.attributes = new HashMap(attributes);
        }

        public int getStart() {
            return offset;
        }

        public int getLength() {
            return length;
        }

        public int getEnd() {
            return getStart() + getLength();
        }

        public Map getAttributes() {
            return new HashMap(attributes);
        }

        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        public Iterator getAttributeNames() {
            return attributes.keySet().iterator();
        }

        public int compareTo(Object o) {
            Region region = (Region) o;
            if (getStart() < region.getStart() || (getStart() == region.getStart() && getLength() < region.getLength())) {
                return -1;
            } else if (getStart() == region.getStart() && getLength() == region.getLength()) {
                return 0;
            } else {
                return 1;
            }
        }

        public String toString() {
            return "[start=" + offset + ",length=" + length + ",attributes=" + attributes + "]";
        }
    }
}
