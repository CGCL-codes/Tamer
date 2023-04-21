package lcm.providers.simple;

public class RangeChangeUndoManager {

    RangeChange listStart = new DummyNode();

    RangeChange head = listStart;

    private BufferChangedLines bcl;

    private Object lastUndoId = null;

    private RangeChange reverseNode = null;

    private boolean reverseMode = false;

    public RangeChangeUndoManager(BufferChangedLines bcl) {
        this.bcl = bcl;
    }

    public void add(RangeChange op) {
        Object undoId = bcl.getBuffer().getUndoId();
        if (undoId == lastUndoId) {
            if (op instanceof DummyNode) return;
            if (head instanceof DummyNode) {
                if (!(op instanceof DummyNode)) {
                    if (head != listStart) head = head.prev;
                    head.append(op);
                    head = op;
                }
                return;
            }
            CompoundChange cc;
            if (!(head instanceof CompoundChange)) {
                cc = new CompoundChange();
                head.prev.append(cc);
                cc.add(head);
                head = cc;
            } else cc = (CompoundChange) head;
            cc.add(op);
        } else {
            lastUndoId = undoId;
            head.append(op);
            head = op;
        }
    }

    public void undo() {
        if (head == listStart) return;
        if (head == reverseNode) reverseMode = true;
        if (!reverseMode) head.undo(); else head.redo();
        head = head.prev;
    }

    public void redo() {
        if (head.next == null) return;
        head = head.next;
        if (!reverseMode) head.redo(); else head.undo();
        if (head == reverseNode) reverseMode = false;
    }

    public void setCleanState() {
        reverseNode = head;
    }

    public void reverse() {
        RangeChange prev = null;
        RangeChange cur = listStart.next;
        RangeChange next;
        while (cur != null) {
            next = cur.next;
            cur.next = prev;
            cur.prev = next;
            cur.reverse();
            prev = cur;
            cur = next;
        }
        if (prev != null) {
            prev.prev = listStart;
            head = listStart.next;
            listStart.next = prev;
        }
    }

    public void rewind(int steps) {
        for (int i = 0; i < steps; i++) head = head.prev;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Undo: ");
        RangeChange rc = listStart.next;
        while (rc != null) {
            if (rc == head) sb.append("-->");
            sb.append(rc.toString());
            rc = rc.next;
            if (rc != null) sb.append(",");
        }
        return sb.toString();
    }

    public abstract class RangeChange {

        public RangeChange prev = null, next = null;

        abstract void undo();

        abstract void redo();

        public void reverse() {
        }

        public void append(RangeChange op) {
            op.prev = this;
            op.next = null;
            next = op;
        }
    }

    public class DummyNode extends RangeChange {

        @Override
        public void redo() {
        }

        @Override
        public void undo() {
        }

        public String toString() {
            return "Dummy";
        }
    }

    public class RangeAdd extends RangeChange {

        private Range r;

        public RangeAdd(Range r) {
            this.r = r;
        }

        @Override
        public void undo() {
            bcl.remove(r);
        }

        @Override
        public void redo() {
            bcl.add(r);
        }

        public String toString() {
            return "Add(" + r.first + "-" + r.last + ")";
        }
    }

    public class RangeRemove extends RangeChange {

        private Range r;

        public RangeRemove(Range r) {
            this.r = r;
        }

        @Override
        public void undo() {
            bcl.add(r);
        }

        @Override
        public void redo() {
            bcl.remove(r);
        }

        public String toString() {
            return "Remove(" + r.first + "-" + r.last + ")";
        }
    }

    public class RangeUpdate extends RangeChange {

        private int lineDiff;

        private Range precedingRange;

        public RangeUpdate(Range r, int diff) {
            precedingRange = new Range(r);
            lineDiff = diff;
        }

        @Override
        public void undo() {
            bcl.updateRanges(precedingRange, 0 - lineDiff);
        }

        @Override
        public void redo() {
            bcl.updateRanges(precedingRange, lineDiff);
        }

        public String toString() {
            return "Update(" + precedingRange.first + "-" + precedingRange.last + "," + lineDiff + ")";
        }
    }

    public class CompoundChange extends RangeChange {

        RangeChange first = null, last = null;

        public void add(RangeChange op) {
            if (first == null) first = op; else last.append(op);
            last = op;
        }

        @Override
        void redo() {
            for (RangeChange op = first; op != null; op = op.next) op.redo();
        }

        @Override
        void undo() {
            for (RangeChange op = last; op != null; op = op.prev) op.undo();
        }

        @Override
        public void reverse() {
            RangeChange next = null;
            for (RangeChange op = last; op != null; op = op.next) {
                op.next = op.prev;
                op.prev = next;
                next = op;
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("Compound:[");
            for (RangeChange op = first; op != null; op = op.next) {
                if (op != first) sb.append(",");
                sb.append(op.toString());
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
