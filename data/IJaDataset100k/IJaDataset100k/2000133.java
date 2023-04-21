package asd;

import java.io.*;

/**
   Instances are nodes in an ASD phrase structure,
   which results from a partial or complete parse of a given phrase.
   @author James A. Mason
   @version 1.03 2000 May 2; Jun 1, 8; 2001 Feb 5; Nov 24
 */
public class ASDPhraseNode implements Cloneable {

    /**
      Initializes an ASDPhraseNode with empty word and null fields.
    */
    public ASDPhraseNode() {
        nodeWord = "";
        nodeInstance = null;
        nodeNext = null;
        nodeSubphrase = null;
        nodeValue = null;
    }

    /**
      Returns the ASDGrammarNode that this ASDPhraseNode
      matched in an ASDGrammar; null if there is no match yet.
    */
    public ASDGrammarNode instance() {
        return nodeInstance;
    }

    /**
      Returns the next ASDPhraseNode (if any) in the subphrase
      to which this node belongs; null if there is no next node.
    */
    public ASDPhraseNode nextNode() {
        return nodeNext;
    }

    /**
      Returns the first ASDPhraseNode in the subphrase (if any)
      that this ASDPhraseNode represents; null if there is no
      subphrase.
    */
    public ASDPhraseNode subphrase() {
        return nodeSubphrase;
    }

    /**
      Returns the semantic value for this node; null if none.
    */
    public Object value() {
        return nodeValue;
    }

    /**
      Returns the vocabulary element in this node.
    */
    public String word() {
        return nodeWord;
    }

    /**
      Links the ASDPhraseNode to an ASDGrammarNode.
      @param node a node in an ASDGrammar that matches the current
      ASDPhraseNode.
    */
    void setInstance(ASDGrammarNode node) {
        nodeInstance = node;
    }

    /**
      Links the ASDPhraseNode to the next one in the subphrase it is in.
      @param next the next node in the subphrase.
    */
    void setNextNode(ASDPhraseNode next) {
        nodeNext = next;
    }

    /**
      Links the ASDPhraseNode to the first node
      in the subphrase that it represents as a whole.
      @param sub the first node in the subphrase below the given node
    */
    void setSubphrase(ASDPhraseNode sub) {
        nodeSubphrase = sub;
    }

    /**
      Sets the semantic value of the ASDPhraseNode.
      @param newValue the new semantic value
    */
    void setValue(Object newValue) {
        nodeValue = newValue;
    }

    /**
      Sets the vocabulary element in the ASDPhraseNode.
      @param newWord the new vocabulary element
    */
    void setWord(String newWord) {
        nodeWord = newWord;
    }

    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    /**
      Displays to System.out the tree rooted at the receiver,
      with node currentNode indicated by an asterisk and an arrow.
      @param currentNode the current node at the top level
      in the phrase structure
    */
    public void showTree(ASDPhraseNode currentNode) {
        showTreeMark("", currentNode);
        System.out.println();
    }

    /**
      Displays the portion of the tree starting at the
      receiver and indented with the given indentString as
      prefix for each line that does not represent a top-
      level node.  Top-level nodes are prefixed with three
      blanks or, in the case of the given aNode, an asterisk
      and an arrow whose purpose is to indicate the node
      which is the current node during a parse.
      @param indentString prefix for indenting of the
      current subtree
      @param aNode the node to be marked with an arrow
    */
    private void showTreeMark(String indentString, ASDPhraseNode aNode) {
        System.out.println();
        if (this == aNode) System.out.print("*->"); else System.out.print("   ");
        System.out.print(indentString + nodeWord + " ");
        if (nodeInstance != null) System.out.print(nodeInstance.instance()); else System.out.print("nil");
        if (nodeSubphrase != null) nodeSubphrase.showTreeMark(indentString + "   ", aNode);
        if (nodeNext != null) nodeNext.showTreeMark(indentString, aNode);
    }

    private String nodeWord;

    private ASDGrammarNode nodeInstance;

    private ASDPhraseNode nodeNext;

    private ASDPhraseNode nodeSubphrase;

    private Object nodeValue;
}
