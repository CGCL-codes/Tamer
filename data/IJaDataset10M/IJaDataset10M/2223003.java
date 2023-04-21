package fitlibrary.parser.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Provides support for a Tree value, in a similar way that TypeAdapters
 * support String values.
 * Not thread safe.
 */
public class ListTree implements Tree, TreeInterface {

    private String title;

    private String text;

    private List<Tree> children;

    public ListTree(String title, List<Tree> children) {
        this.children = children;
        setTitle(title);
    }

    public ListTree(List<Tree> children) {
        this("", children);
    }

    public ListTree(String title, Tree[] children) {
        this(title, new ArrayList<Tree>(Arrays.asList(children)));
    }

    public ListTree(Tree[] children) {
        this("", Arrays.asList(children));
    }

    public ListTree(String title) {
        this(title, new ArrayList<Tree>());
    }

    public ListTree(String name, ListTree tree) {
        this(name, new Tree[] { tree });
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
        setText(this.title);
    }

    @Override
    public String getText() {
        return text;
    }

    public void setText(String title) {
        text = removeTags(title).trim();
    }

    @Override
    public List<Tree> getChildren() {
        return children;
    }

    public void clearChildren() {
        children = new ArrayList<Tree>();
    }

    public void addChild(ListTree tree) {
        children.add(tree);
    }

    @Override
    public String toString() {
        if (children.isEmpty()) return title;
        String result = title + "<ul>";
        for (Iterator<Tree> it = children.iterator(); it.hasNext(); ) result += "<li>" + ((ListTree) it.next()).toString() + "</li>";
        return result + "</ul>";
    }

    @Override
    public String text() {
        if (children.isEmpty()) return text;
        String result = text + "<ul>";
        for (Iterator<Tree> it = children.iterator(); it.hasNext(); ) result += "<li>" + ((ListTree) it.next()).text() + "</li>";
        return result + "</ul>";
    }

    @Override
    public boolean equals(Object object) {
        return equals(this, object);
    }

    public static boolean equals(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (!(o1 instanceof Tree) || !(o2 instanceof Tree)) return false;
        Tree t1 = (Tree) o1;
        Tree t2 = (Tree) o2;
        if (!t1.getText().equals(t2.getText())) return false;
        if (t1.getChildren().size() != t2.getChildren().size()) return false;
        Iterator<Tree> otherIt = t2.getChildren().iterator();
        for (Iterator<Tree> it = t1.getChildren().iterator(); it.hasNext(); ) {
            Tree tree1 = it.next();
            Tree tree2 = otherIt.next();
            if (!equals(tree1, tree2)) return false;
        }
        return true;
    }

    public static Tree parseTree(Tree tree) {
        return tree;
    }

    public static ListTree parse(String s) {
        int index = s.indexOf("<ul>");
        if (index < 0) return new ListTree(s);
        StringTokenizer tok = new StringTokenizer(s, "<");
        if (index == 0) return parse("", tok);
        return parse(tok.nextToken(), tok);
    }

    private static ListTree parse(String initialTitle, StringTokenizer tok) {
        String title = initialTitle;
        List<Tree> children = new ArrayList<Tree>();
        String nextToken = tok.nextToken();
        while (tok.hasMoreTokens() && !nextToken.startsWith("ul>") && !nextToken.startsWith("/li>")) {
            title += "<" + nextToken;
            nextToken = tok.nextToken();
        }
        if (nextToken.startsWith("ul>")) {
            while (tok.hasMoreTokens()) {
                String t = tok.nextToken();
                if (t.startsWith("/ul>")) break;
                if (t.startsWith("li>")) {
                    String subTitle = t.substring("li>".length());
                    Tree subTree = parse(subTitle, tok);
                    children.add(subTree);
                }
            }
        } else if (!nextToken.startsWith("/li>")) throw new RuntimeException("Bad list starting from " + nextToken);
        return new ListTree(title, children);
    }

    private static String removeTags(String s) {
        String text = "";
        StringTokenizer tok = new StringTokenizer(s, "<");
        while (tok.hasMoreTokens()) {
            String nextToken = tok.nextToken();
            int index = nextToken.indexOf(">");
            text += nextToken.substring(index + 1);
        }
        return text;
    }

    @Override
    public Tree toTree() {
        return this;
    }

    public String toString(int depth) {
        if (depth <= 1 || children.isEmpty()) return title;
        String result = title + "<ul>";
        for (Iterator<Tree> it = children.iterator(); it.hasNext(); ) result += "<li>" + ((ListTree) it.next()).toString(depth - 1) + "</li>";
        return result + "</ul>";
    }

    public String prune(int max) {
        int count = nodeCount(1);
        for (int depth = 2; ; depth++) {
            int nextCount = nodeCount(depth);
            if (nextCount > max || nextCount == count) return toString(depth - 1);
            count = nextCount;
        }
    }

    private int nodeCount(int depth) {
        if (depth <= 1 || children.isEmpty()) return 1;
        int count = 1;
        for (Iterator<Tree> it = children.iterator(); it.hasNext(); ) count += ((ListTree) it.next()).nodeCount(depth - 1);
        return count;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
