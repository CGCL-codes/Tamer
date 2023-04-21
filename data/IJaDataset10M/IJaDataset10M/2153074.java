package org.owasp.orizon.tools.osh.parser;

import java.util.*;
import java.lang.reflect.*;

/**
 * A set of static utility routines, mostly for working with Node objects.
 * These methods were not added to the Node interface in order to keep it 
 * fairly easy for someone to write their own Node implementation. 
 */
public abstract class Nodes {

    public static <T extends Node> T firstChildOfType(Node node, Class<T> clazz) {
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (clazz.isInstance(child)) {
                return clazz.cast(child);
            }
        }
        return null;
    }

    public static <T extends Node> List<T> childrenOfType(Node node, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (clazz.isInstance(child)) {
                result.add(clazz.cast(child));
            }
        }
        return result;
    }

    public static List<Token> getTokens(Node node) {
        List<Token> result = new ArrayList<Token>();
        for (int i = 0; i < node.getChildCount(); i++) {
            Node child = node.getChild(i);
            if (child instanceof Token) {
                result.add((Token) child);
            } else {
                result.addAll(getTokens(child));
            }
        }
        return result;
    }

    public static List<Token> getRealTokens(Node n) {
        List<Token> result = new ArrayList<Token>();
        for (Token token : getTokens(n)) {
            if (!token.isUnparsed()) {
                result.add(token);
            }
        }
        return result;
    }

    public static ListIterator<Node> iterator(final Node node) {
        return new ListIterator<Node>() {

            int current = -1;

            boolean justModified;

            public boolean hasNext() {
                return current + 1 < node.getChildCount();
            }

            public Node next() {
                justModified = false;
                return node.getChild(++current);
            }

            public Node previous() {
                justModified = false;
                return node.getChild(--current);
            }

            public void remove() {
                if (justModified) throw new IllegalStateException();
                node.removeChild(current);
                --current;
                justModified = true;
            }

            public void add(Node n) {
                if (justModified) throw new IllegalStateException();
                node.addChild(current + 1, n);
                justModified = true;
            }

            public boolean hasPrevious() {
                return current > 0;
            }

            public int nextIndex() {
                return current + 1;
            }

            public int previousIndex() {
                return current;
            }

            public void set(Node n) {
                node.setChild(current, n);
            }
        };
    }

    /**
     * Expands (in place) a Node's children to include any comment tokens hanging
     * off the regular tokens.
     * @param n the Node 
     * @param recursive whether to recurse into child nodes.
     */
    public static void expandSpecialTokens(Node n, boolean recursive) {
        List<Token> expandedList = getAllTokens(n, true, false);
        n.clearChildren();
        for (Node child : expandedList) {
            n.addChild(child);
            if (recursive && child.getChildCount() > 0) {
                expandSpecialTokens(child, true);
            }
        }
    }

    /**
     * @return a List containing all the tokens in a Node
     * @param n The Node 
     * @param includeCommentTokens Whether to include comment tokens
     * @param recursive Whether to recurse into child Nodes.
     */
    public static List<Token> getAllTokens(Node n, boolean includeCommentTokens, boolean recursive) {
        List<Token> result = new ArrayList<Token>();
        for (Iterator<Node> it = iterator(n); it.hasNext(); ) {
            Node child = it.next();
            if (child instanceof Token) {
                Token token = (Token) child;
                if (token.isUnparsed()) {
                    continue;
                }
                if (includeCommentTokens) {
                    Token specialToken = token;
                    while (specialToken.specialToken != null) {
                        specialToken = specialToken.specialToken;
                    }
                    while (specialToken != token && specialToken != null) {
                        result.add(specialToken);
                        specialToken = specialToken.next;
                    }
                }
                result.add(token);
            } else if (child.getChildCount() > 0) {
                result.addAll(getAllTokens(child, includeCommentTokens, recursive));
            }
        }
        return result;
    }

    public static void copyLocationInfo(Node from, Node to) {
        to.setInputSource(from.getInputSource());
        to.setBeginLine(from.getBeginLine());
        to.setBeginColumn(from.getBeginColumn());
        to.setEndLine(from.getEndLine());
        to.setEndColumn(from.getEndColumn());
    }

    private static String stringrep(Node n) {
        if (n instanceof Token) {
            return n.toString().trim();
        }
        return n.getClass().getSimpleName();
    }

    public static void dump(Node n, String prefix) {
        String output = stringrep(n);
        if (output.length() > 0) {
            System.out.println(prefix + output);
        }
        for (Iterator<Node> it = iterator(n); it.hasNext(); ) {
            Node child = it.next();
            dump(child, prefix + "  ");
        }
    }

    public static <T extends Node> T getFirstAncestorOfType(Node n, Class<T> clazz) {
        Node parent = n;
        while (parent != null) {
            parent = parent.getParent();
            if (clazz.isInstance(parent)) {
                return clazz.cast(parent);
            }
        }
        return null;
    }

    /**
     * An abstract base class for classes that
     * implement the visitor pattern.
     */
    public abstract static class Visitor {

        private Map<Class<? extends Node>, Method> handlers = new HashMap<Class<? extends Node>, Method>();

        public void visit(Node node) {
            try {
                Class<? extends Node> clazz = node.getClass();
                Method visitMethod = handlers.get(clazz);
                if (visitMethod == null) {
                    if (!handlers.containsKey(clazz)) {
                        visitMethod = this.getClass().getMethod("visit", clazz);
                        handlers.put(clazz, visitMethod);
                    } else {
                        fallback(node);
                    }
                }
                visitMethod.invoke(this, node);
            } catch (InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new RuntimeException(ite);
            } catch (NoSuchMethodException nsme) {
                handlers.put(node.getClass(), null);
                fallback(node);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        public void recurse(Node node) {
            for (int i = 0; i < node.getChildCount(); i++) {
                visit(node.getChild(i));
            }
        }

        /**
         * This method is invoked if a handler has no visit method
         * defined for a given Node type. In the base implementation
         * this simply recurses into the node's children.
         */
        protected void fallback(Node node) {
            recurse(node);
        }
    }
}
