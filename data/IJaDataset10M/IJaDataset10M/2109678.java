package org.progeeks.textile.ast;

import java.io.*;
import java.util.*;

/**
 *  A node in the textile abstract syntax tree.  For simplicity,
 *  these nodes are also responsible for processing the document
 *  tokens.
 *
 *  @version   $Revision: 1.2 $
 *  @author    Paul Speed
 */
public interface Node {

    public static final int NODE_ROOT = 0;

    public static final int NODE_PARAGRAPH = 1;

    public static final int NODE_LINE = 2;

    public static final int NODE_HEADER = 3;

    public static final int NODE_ORDERED_LIST = 4;

    public static final int NODE_UNORDERED_LIST = 5;

    public static final int NODE_LIST_ITEM = 6;

    public static final int NODE_TABLE = 7;

    public static final int NODE_TABLE_ROW = 8;

    public static final int NODE_TABLE_HEADING = 9;

    public static final int NODE_TABLE_DATA = 10;

    public static final String[] NODE_NAMES = { "Root", "Paragraph", "Line", "Header", "OrderedList", "UnorderedList", "ListItem", "Table", "TableRow", "TableHeading", "TableData" };

    /**
     *  Returns the type of this node from the list of textile
     *  node constants.
     */
    public int getType();

    /**
     *  Returns the parent of this node.
     */
    public Node getParent();

    /**
     *  Adds a new word to this node and returns whatever should
     *  be the new current parsing node.
     */
    public Node addWord(String word);

    /**
     *  Adds a new character token to this node and returns whatever
     *  should be the new current parsing node.
     */
    public Node addChar(char c);

    /**
     *  Returns the list of children associated with this node.
     */
    public List getChildren();

    /**
     *  Prints a text representation of this node at the specified
     *  indent... this is for traversal-independent debugging.
     */
    public void printNode(String indent);
}
