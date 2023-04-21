package org.progeeks.textile;

import java.io.*;
import java.util.*;

/**
 *
 *  @version   $Revision: 1.1 $
 *  @author    Paul Speed
 */
public class TextileParser {

    public static void parseFile(File f) throws Exception {
        ParseNode node = new ParseNode("root");
        FileReader in = new FileReader(f);
        try {
            StreamTokenizer st = new StreamTokenizer(in);
            st.eolIsSignificant(true);
            st.ordinaryChars(48, 57);
            st.ordinaryChars(45, 46);
            st.wordChars(48, 57);
            st.wordChars(45, 46);
            st.wordChars('*', '*');
            st.wordChars('#', '#');
            st.ordinaryChar(' ');
            st.ordinaryChar(9);
            st.ordinaryChar('/');
            boolean lineStart = true;
            int token;
            while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
                switch(token) {
                    case StreamTokenizer.TT_NUMBER:
                        System.out.println("Number:" + st.nval);
                        break;
                    case StreamTokenizer.TT_WORD:
                        System.out.println("Word:" + st.sval);
                        node = node.addWord(st.sval);
                        break;
                    default:
                        System.out.println("Char:" + token + "(" + (char) token + ")");
                        node = node.addChar((char) token);
                        break;
                }
            }
        } finally {
            in.close();
        }
        ParseNode n = node;
        while (n.getParent() != null) n = n.getParent();
        n.printNode("");
    }

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < args.length; i++) {
            parseFile(new File(args[i]));
        }
    }

    public static class BulletType {

        char type;

        int level;

        public BulletType(String s) {
            type = s.charAt(0);
            if (type != '#' && type != '*') {
                level = -1;
                return;
            }
            level = s.length();
            for (int i = 1; i < level; i++) {
                char c = s.charAt(i);
                if (c != type) {
                    level = -1;
                    return;
                }
            }
        }

        public static boolean isBullet(String s) {
            char type = s.charAt(0);
            if (type != '#' && type != '*') return (false);
            int length = s.length();
            for (int i = 1; i < length; i++) {
                char c = s.charAt(i);
                if (c != type) return (false);
            }
            return (true);
        }
    }

    public static class ParseNode {

        private String name;

        private ParseNode parent;

        private List children = new ArrayList();

        public ParseNode(String name) {
            this.name = name;
        }

        public void setParent(ParseNode parent) {
            this.parent = parent;
        }

        public ParseNode getParent() {
            return (parent);
        }

        public String getName() {
            return (name);
        }

        public ParseNode addWord(String word) {
            if ("h2.".equals(word)) {
                Header h = new Header();
                addChild(h);
                return (h);
            } else {
                BulletType bt = new BulletType(word);
                if (bt.level > 0) {
                    BulletList bullets = new BulletList(bt);
                    addChild(bullets);
                    return (bullets.addWord(word));
                }
            }
            Paragraph p = new Paragraph();
            addChild(p);
            return (p.addWord(word));
        }

        public ParseNode addChar(char c) {
            if (c == '|') {
                Table t = new Table();
                addChild(t);
                return (t.addChar(c));
            } else if (Character.isWhitespace(c)) return (this);
            Paragraph p = new Paragraph();
            addChild(p);
            return (p.addChar(c));
        }

        public void addChild(ParseNode node) {
            children.add(node);
            node.setParent(this);
        }

        public void replaceChild(ParseNode oldNode, ParseNode newNode) {
            int i = children.indexOf(oldNode);
            if (i < 0) throw new RuntimeException("Parse error, cannot substitued node:" + oldNode);
            children.set(i, newNode);
        }

        public String toString() {
            return (name);
        }

        public void printNode(String indent) {
            System.out.println(indent + toString());
            for (Iterator i = children.iterator(); i.hasNext(); ) {
                ParseNode node = (ParseNode) i.next();
                node.printNode(indent + "  ");
            }
        }
    }

    public static class Line extends ParseNode {

        protected StringBuffer text = new StringBuffer();

        public Line() {
            super("Line");
        }

        public Line(String name) {
            super(name);
        }

        public int size() {
            return (text.length());
        }

        public ParseNode addWord(String word) {
            text.append(word);
            return (this);
        }

        public ParseNode addChar(char c) {
            if (c == 10) return (getParent());
            text.append(c);
            return (this);
        }

        public String toString() {
            return (super.toString() + "[" + text + "]");
        }
    }

    public static class Paragraph extends ParseNode {

        public Paragraph(String name) {
            super(name);
        }

        public Paragraph() {
            super("Paragraph");
        }

        public ParseNode addWord(String word) {
            if (BulletType.isBullet(word)) return (getParent().addWord(word));
            Line l = new Line();
            addChild(l);
            ParseNode node = l.addWord(word);
            return (node);
        }

        public ParseNode addChar(char c) {
            if (c == ' ') return (this);
            Line l = new Line();
            addChild(l);
            ParseNode node = l.addChar(c);
            if (node == this && l.size() == 0) return (getParent());
            return (node);
        }
    }

    public static class Table extends Paragraph {

        public Table() {
            super("Table");
        }
    }

    public static class Header extends Line {

        private boolean firstLine = true;

        public Header() {
            super("Header");
        }

        public ParseNode addChar(char c) {
            if (c == 10) {
                if (!firstLine) return (getParent());
                firstLine = false;
                return (this);
            }
            text.append(c);
            return (this);
        }
    }

    public static class BulletList extends ParseNode {

        private char type;

        private int level;

        public BulletList(String name, int level) {
            super(name);
            this.level = level;
        }

        public BulletList(BulletType bt) {
            this(bt.type, bt.level);
        }

        public BulletList(char type, int level) {
            super("BulletList");
            this.type = type;
            this.level = level;
        }

        public String toString() {
            return (getName() + "(" + type + ", " + level + ")");
        }

        public ParseNode addWord(String word) {
            if (!BulletType.isBullet(word)) return (getParent().addWord(word));
            BulletType bt = new BulletType(word);
            if (bt.level > level) {
                BulletList list = new BulletList(bt);
                addChild(list);
                return (list.addWord(word));
            } else if (bt.level < level) {
                ParseNode parent = getParent();
                return (parent.addWord(word));
            }
            Paragraph p = new Paragraph("Bullet");
            addChild(p);
            return (p);
        }

        public ParseNode addChar(char c) {
            if (c == ' ') return (this);
            if (c == 10) return (getParent());
            Paragraph p = new Paragraph("Bullet");
            addChild(p);
            ParseNode node = p.addChar(c);
            return (node);
        }
    }
}
