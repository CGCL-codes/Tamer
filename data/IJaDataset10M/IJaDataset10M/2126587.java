package core.reader;

/**
 *
 * @author francescoburato
 */
public class ReaderList {

    private class ReaderNode {

        public Reader key;

        public ReaderNode next;

        public ReaderNode(Reader k, ReaderNode n) {
            this.key = k;
            this.next = n;
        }
    }

    private ReaderNode root;

    public ReaderList() {
        this.root = null;
    }

    public void add(Reader r) {
        this.root = new ReaderNode(r, this.root);
    }

    private void addListener(InputListener l, ReaderNode r) {
        if (r != null) {
            r.key.addInputListener(l);
            this.addListener(l, r.next);
        }
    }

    public void addListener(InputListener l) {
        this.addListener(l, this.root);
    }

    public ReaderListIterator getIterator() {
        return new ReaderListIterator();
    }

    public class ReaderListIterator {

        private ReaderNode current;

        public ReaderListIterator() {
            this.current = root;
        }

        public void reset() {
            this.current = root;
        }

        public Reader getNext() {
            if (this.atEnd()) return null; else {
                Reader r = this.current.key;
                this.current = this.current.next;
                return r;
            }
        }

        public boolean atEnd() {
            return this.current == null;
        }
    }
}
