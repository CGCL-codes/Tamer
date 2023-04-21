package org.dhmp.io;

import org.dhmp.util.HierarchicalMap;

/**
 * This interface contains various helper classes needed during
 * stream operation. It defines the type used to represent
 * each element of HierarchicalMap during serialization.
 * Path to keep sequence of keys associated to a particular
 * element written to or read from stream.
 * @author hideyuki
 */
interface MapStream {

    /**
     * Mask to obtain the Object Type during serialization
     */
    public static final int OBJ_TYPE = 0x0F;

    /**
     * Object type is ignored
     */
    public static final int OBJ_TYPE_NOP = 0;

    /**
     * Object type for any Object
     */
    public static final int OBJ_TYPE_UNKNOWN = 1;

    /**
     * Object type for HierarchicalMap
     */
    public static final int OBJ_TYPE_MAP = 2;

    /**
     * Object type for reference. It is used during serialization of
     * recursive map.
     */
    public static final int OBJ_TYPE_REFERENCE = 3;

    /**
     * Flag used to indicate the last Object
     */
    public static final int OBJ_TYPE_LAST = 0x80;

    /**
     * This class representes a sequence of keys.
     * These keys are keeped in a linked list as instance of Positions.
     */
    public static class Path {

        private Position head;

        public Path() {
            head = new Position("root", 0);
        }

        /**
         * <p>This class is the internal representation of
         * each element in a path. <p>It is composed by
         * the key and index. As HierarchicalMap key can be repeated,
         * an index has been associated to uniquely identify the key.
         * Each mapping in HierarchicalMap is enumerated during
         * output stream operation and index 0 is associated to the first
         * mapping and so on.<p>
         */
        public class Position {

            private boolean valid = true;

            int index;

            String key;

            Position pos;

            Position before;

            Position after;

            Position(String key, int index) {
                this.index = index;
                this.key = key;
                after = before = this;
            }

            Position(String key, Position pos) {
                this.key = key;
                this.setPos(pos);
                after = before = this;
            }

            boolean hasNext() {
                return this.after != head;
            }

            Position next() {
                if (this.after == head) throw new java.util.NoSuchElementException();
                return this.after;
            }

            boolean hasPrevious() {
                return this != head;
            }

            Position previous() {
                if (this == head) throw new java.util.NoSuchElementException();
                return this.before;
            }

            boolean hasSameKey(String key) {
                return this.key.equals(key);
            }

            int getIndex() {
                return this.index;
            }

            String getKey() {
                return this.key;
            }

            boolean isValid() {
                return valid;
            }

            Position remove() {
                this.valid = false;
                this.after.before = this.before;
                this.before.after = this.after;
                return this;
            }

            void addBefore(Position newPosition) {
                newPosition.after = this;
                newPosition.before = this.before;
                this.before.after = newPosition;
                this.before = newPosition;
            }

            public String toString() {
                StringBuffer buff = new StringBuffer("[");
                buff.append(this.key);
                buff.append(":");
                buff.append(this.index);
                buff.append("]");
                return buff.toString();
            }

            public Position getPos() {
                return pos;
            }

            public void setPos(Position pos) {
                this.pos = pos;
            }
        }

        public Position add(String key) {
            return add(key, 0);
        }

        public Position add(String key, int index) {
            Position newPos = new Position(key, index);
            head.addBefore(newPos);
            return newPos;
        }

        public Position pop() {
            if (head.before == head) return null;
            Position pos = head.before.remove();
            return pos;
        }

        public Position getRoot() {
            return head;
        }

        public Position getLast() {
            return head.before;
        }

        public boolean isValid(Position element) {
            return (element == null) ? false : element.valid;
        }

        public int purge(Position element) {
            int ret = 0;
            while (head.before != head) {
                ret++;
                if (pop().equals(element)) break;
            }
            return ret;
        }

        public String toString() {
            StringBuffer buff = new StringBuffer("<");
            buff.append(getKey());
            buff.append(">");
            return buff.toString();
        }

        public StringBuffer getKey() {
            return getKey(null);
        }

        public StringBuffer getKey(Position current) {
            return getKey(null, current);
        }

        public StringBuffer getKey(Position start, Position end) {
            StringBuffer buff = new StringBuffer();
            start = (start == null) ? this.getRoot() : start;
            end = (end == null) ? this.getLast() : end;
            if (!end.isValid() || start == end) return buff;
            if (start.isValid()) {
                Position current = start.after;
                while (current != head) {
                    if (current.key != null) {
                        buff.append(current.key);
                        if (current.equals(end)) break;
                        if (current.hasNext()) buff.append('/');
                    }
                    current = current.next();
                }
            } else {
                for (Position current = start; current != end; current = current.after) {
                    buff.append("..");
                    if (current.after != end) buff.append('/');
                }
            }
            return buff;
        }

        public String getReference() {
            StringBuffer buff = getKey();
            buff.append(':');
            Position pos = this.head;
            while (pos.after != head) {
                pos = pos.after;
                if (pos.key != null) {
                    buff.append(pos.index);
                    if (pos.after != head) buff.append('.');
                }
            }
            return buff.toString();
        }
    }

    public class BookMark {

        MapStream.Path target = new MapStream.Path();

        MapStream.Path.Position startPos = null;

        MapStream.Path.Position curr = null;

        public BookMark(MapStream.Path.Position pos, Object path) {
            if (path != null) {
                String[] tok = path.toString().split(HierarchicalMap.DEFAULT_PATH_SEPARATOR);
                for (int i = 0; i < tok.length; i++) {
                    if ("".equals(tok[i])) continue;
                    target.add(tok[i]);
                }
                this.curr = target.getRoot();
                curr.pos = startPos = pos;
                if (!this.curr.hasNext()) {
                    throw new IllegalArgumentException("BookMark received empty path argument. It will never match anything!");
                }
            }
        }

        public boolean match() {
            return match(null);
        }

        public boolean match(String key) {
            if (startPos == null || !startPos.valid) {
                return false;
            }
            while (curr.pos != null && !curr.pos.valid && curr.hasPrevious()) {
                curr = curr.before;
            }
            MapStream.Path.Position prev = curr;
            while (curr.hasNext()) {
                prev = curr;
                curr = curr.after;
                if (curr.pos == null || !curr.pos.valid) break;
            }
            curr = prev;
            MapStream.Path.Position path = curr.pos;
            while (curr.hasNext() && path.hasNext()) {
                if (!curr.after.key.equals(path.after.key)) break;
                curr = curr.after;
                path = path.after;
                curr.pos = path;
            }
            if (path.hasNext()) {
                return false;
            }
            if (key != null) {
                if (curr.hasNext()) {
                    MapStream.Path.Position nextTarget = curr.after;
                    return (!nextTarget.hasNext() && nextTarget.hasSameKey(key));
                } else {
                    return "".equals(key);
                }
            }
            return !curr.hasNext();
        }

        public boolean isValid() {
            return (startPos == null) ? false : startPos.valid;
        }

        public String toString() {
            StringBuffer buff = new StringBuffer("[");
            MapStream.Path.Position itr = target.getRoot();
            while (itr.hasNext()) {
                buff.append(((MapStream.Path.Position) itr.next()).key);
                if (itr.hasNext()) buff.append("/");
            }
            buff.append("]");
            return buff.toString();
        }
    }
}
