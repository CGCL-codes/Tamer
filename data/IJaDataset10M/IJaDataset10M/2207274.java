package sifter.ui;

import sifter.*;
import sifter.rcfile.*;
import sifter.translation.*;
import sifter.ui.*;
import sifter.messages.*;
import net.sourceforge.jmisc.Debug;
import net.sourceforge.jmisc.ProgrammerError;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.Component;

/** 
    A list of choices, used by the GUI work thread.

    @author Fred Gylys-Colwell
    @version $Name:  $, $Revision: 1.18 $
*/
public class CList {

    Node next = null;

    Node last = null;

    ParentNode lastDir = null;

    /** Add the Parent Node at the end of the work list. */
    Node appendParent(Parent c) {
        Debug.println(Bug.BUFFER, "CLIST.appendParent parent = " + c);
        Debug.println(Bug.BUFFER, "lastDir = " + lastDir);
        ParentNode node = new ParentNode(c, lastDir);
        add(node, last);
        lastDir = node;
        return node;
    }

    /** Add the EndDir Node at the end of the work list. */
    Node appendUp() {
        Debug.println(Bug.BUFFER, "CLIST.appendUP lastDir = " + lastDir);
        if (lastDir == null) {
            return append(new ChoiceSet("CODE ERROR: there is no last dir.", Verify.RECKLESS));
        }
        EndDirNode node = new EndDirNode(lastDir);
        add(node, last);
        lastDir.end = node;
        lastDir = lastDir.parent;
        return node;
    }

    /** Add the Node at the end of the work list. */
    Node append(ChoiceSet c) {
        Node node = new Node(c, lastDir);
        add(node, last);
        return node;
    }

    /** Insert the node after the one we're currently working on (next). */
    Node add(ChoiceSet c) {
        ParentNode parent;
        if (next instanceof ParentNode) {
            parent = (ParentNode) next;
        } else {
            parent = next.parent;
        }
        Node node = new Node(c, parent);
        add(node, next);
        node.prevVis = node.prev;
        return node;
    }

    /** Insert node into a doubly linked list after prev. */
    private void add(Node node, Node prev) {
        if (last == null) {
            if (prev != null) throw new ProgrammerError("CList has mangled pointers.");
            last = node;
            next = node;
        } else {
            node.prev = prev;
            node.next = prev.next;
            if (prev.next != null) {
                prev.next.prev = node;
            }
            prev.next = node;
        }
        if (last == prev) last = node;
    }

    ChoiceSet nextWork() {
        while (next instanceof EndDirNode) incrementWork(null);
        if (next == null) return null;
        return next.choice;
    }

    void incrementWork(MyPanel panel) {
        if (next == null) {
            last = null;
            return;
        }
        Node t = next;
        next = t.next;
        t.next = null;
        t.prev = null;
        if (next == null) {
            last = null;
        }
        if (panel != null) panel.noticeChange(t.gui);
    }

    void disableWork() throws IOException {
        if (next == null) return;
        if (next.gui == null) return;
        next.gui.disable();
    }

    void markAll(int level) {
        for (Node n = next; n != null; n = n.next) {
            if (n.choice.level <= level) n.choice.setOK(true);
        }
    }

    public static class Node {

        Node nextVis, prevVis;

        ParentNode parent;

        Node next, prev;

        ChoiceGUI gui;

        ChoiceSet choice;

        Node(ChoiceSet choice, ParentNode parent) {
            this.choice = choice;
            this.parent = parent;
            if (parent != null) choice.setIndent((Parent) parent.choice); else choice.setIndent(null);
        }

        void makeGUI(GUI top) {
            if (gui == null) gui = new ChoiceGUI(choice, top);
        }

        /** Return the next non-hidden node after this one. */
        Node nextVis() {
            return nextVis;
        }

        /** Return the previous non-hidden node before this one. */
        Node prevVis() {
            if (prevVis == null) return null;
            return prevVis.checkVis();
        }

        /** Return this node if it's visible, or the parent if not. */
        Node checkVis() {
            return this;
        }

        /** Delete this node from the visible list, and return the next
	    undeleted node in the list, or this if it wasn't deleted. */
        Node delete() {
            Debug.println(Bug.ORDER, "Delete called on  " + this);
            if (choice.status != ChoiceSet.DONE) return this;
            if (gui == null) return nextVis;
            if (prevVis != null) {
                Debug.println(Bug.ORDER, "prevVis =   " + prevVis);
                prevVis.nextVis = nextVis;
            }
            if (nextVis != null) {
                Debug.println(Bug.ORDER, "nextVis =   " + nextVis);
                nextVis.prevVis = prevVis;
            }
            gui = null;
            return nextVis;
        }

        public String toString() {
            return "&" + choice.toString();
        }
    }

    public static class ParentNode extends Node {

        EndDirNode end;

        ParentNode(ChoiceSet choice, ParentNode parent) {
            super(choice, parent);
        }

        ParentGUI getGUI() {
            return (ParentGUI) gui;
        }

        Node nextVis() {
            if (gui == null) return end.nextVis;
            if (getGUI().collapsed) {
                if (end == null) return null;
                if (end.gui == null) {
                    return null;
                }
                return end.nextVis;
            }
            return nextVis;
        }

        void makeGUI(GUI top) {
            if (gui == null) gui = new ParentGUI((Parent) choice, top);
        }

        /** Delete as much of the whole directory from the visible list, as
	    possible. */
        Node delete() {
            int dy = 0;
            Debug.println(Bug.ORDER, "End = " + end);
            Node next = nextVis;
            for (Node n = nextVis; (n != end) && (n != null); ) {
                next = n.delete();
                if (next == n) n = null; else n = next;
            }
            if (next == end) {
                Debug.println(Bug.ORDER, "deleting self ");
                nextVis = end.nextVis;
                end.gui = null;
                return super.delete();
            }
            return next;
        }

        /** Collapse the directory so that none of its files are shown.
	    @keytable -
	 */
        boolean collapse(GUIList list) {
            if (getGUI().collapsed) return false;
            getGUI().collapsed = true;
            list.current = this;
            list.shiftYValues(this);
            return true;
        }

        /** Expand the directory so that its files are shown again.
	    @keytable +
	    @keytable =
	    @name expand
	 */
        boolean uncollapse(GUIList list) {
            if (!getGUI().collapsed) return false;
            getGUI().collapsed = false;
            list.shiftYValues(this);
            return true;
        }

        /** When called on a directory, any key with the alt- held down will be
	    passed to all of the children.
	    @keytable M-ALL
	    @keytable A-ALL
	 */
        boolean passToChildren(KeyEvent key, KeyTable table) {
            boolean j = false;
            int mask = key.ALT_MASK | key.META_MASK;
            KeyEvent noAlt = new KeyEvent((Component) key.getSource(), 0, key.getWhen(), key.getModifiers() & (~mask), key.getKeyCode());
            for (Node n = this; (n != end) && (n != null) && (n.gui != null); ) {
                Node next = n.nextVis;
                j = table.handleKey(noAlt, n) | j;
                n = next;
            }
            return j;
        }
    }

    public static class EndDirNode extends Node {

        ParentNode startDir;

        EndDirNode(ParentNode parent) {
            super(new ChoiceSet(" ", parent.choice.level), parent);
            startDir = parent;
            parent.end = this;
        }

        ParentGUI getGUI() {
            return (ParentGUI) gui;
        }

        void makeGUI(GUI top) {
            if (gui == null) gui = new EndDir(choice, top, startDir.getGUI());
        }

        /** Delete this whole directory from the visible list. */
        Node delete() {
            Node n = startDir.delete();
            return n;
        }

        Node checkVis() {
            if (startDir.getGUI().collapsed) return startDir;
            return this;
        }

        /** At the end of a directory, any key with the alt- key modifier will
	    be passed to the parent, which passes it to all of its children.
	    @keytable M-ALL
 	    @keytable A-ALL
	    @keytable -
	    @keytable +
	    @keytable =
	 */
        boolean passToParent(KeyEvent key, KeyTable table) {
            return table.handleKey(key, startDir);
        }
    }
}
