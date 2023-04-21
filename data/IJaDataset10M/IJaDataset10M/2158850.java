package csjavacc.parser;

import csjavacc.struct.Choice;
import csjavacc.struct.Expansion;
import csjavacc.struct.Lookahead;
import csjavacc.struct.ROneOrMore;
import csjavacc.struct.RRepetitionRange;
import csjavacc.struct.RSequence;
import csjavacc.struct.RZeroOrMore;
import csjavacc.struct.RZeroOrOne;
import csjavacc.struct.TreeWalkerOp;
import csjavacc.struct.TryBlock;
import csjavacc.struct.ZeroOrMore;
import csjavacc.struct.ZeroOrOne;

/**
 * A set of routines that walk down the Expansion tree in
 * various ways.
 */
public class ExpansionTreeWalker {

    /**
   * Visits the nodes of the tree rooted at "node" in pre-order.
   * i.e., it executes opObj.action first and then visits the
   * children.
   */
    static void preOrderWalk(Expansion node, TreeWalkerOp opObj) {
        opObj.action(node);
        if (opObj.goDeeper(node)) {
            if (node instanceof Choice) {
                for (java.util.Enumeration anEnum = ((Choice) node).choices.elements(); anEnum.hasMoreElements(); ) {
                    preOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof Sequence) {
                for (java.util.Enumeration anEnum = ((Sequence) node).units.elements(); anEnum.hasMoreElements(); ) {
                    preOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof OneOrMore) {
                preOrderWalk(((OneOrMore) node).expansion, opObj);
            } else if (node instanceof ZeroOrMore) {
                preOrderWalk(((ZeroOrMore) node).expansion, opObj);
            } else if (node instanceof ZeroOrOne) {
                preOrderWalk(((ZeroOrOne) node).expansion, opObj);
            } else if (node instanceof Lookahead) {
                Expansion nested_e = ((Lookahead) node).la_expansion;
                if (!(nested_e instanceof Sequence && (Expansion) (((Sequence) nested_e).units.elementAt(0)) == node)) {
                    preOrderWalk(nested_e, opObj);
                }
            } else if (node instanceof TryBlock) {
                preOrderWalk(((TryBlock) node).exp, opObj);
            } else if (node instanceof RChoice) {
                for (java.util.Enumeration anEnum = ((RChoice) node).choices.elements(); anEnum.hasMoreElements(); ) {
                    preOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof RSequence) {
                for (java.util.Enumeration anEnum = ((RSequence) node).units.elements(); anEnum.hasMoreElements(); ) {
                    preOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof ROneOrMore) {
                preOrderWalk(((ROneOrMore) node).regexpr, opObj);
            } else if (node instanceof RZeroOrMore) {
                preOrderWalk(((RZeroOrMore) node).regexpr, opObj);
            } else if (node instanceof RZeroOrOne) {
                preOrderWalk(((RZeroOrOne) node).regexpr, opObj);
            } else if (node instanceof RRepetitionRange) {
                preOrderWalk(((RRepetitionRange) node).regexpr, opObj);
            }
        }
    }

    /**
   * Visits the nodes of the tree rooted at "node" in post-order.
   * i.e., it visits the children first and then executes
   * opObj.action.
   */
    static void postOrderWalk(Expansion node, TreeWalkerOp opObj) {
        if (opObj.goDeeper(node)) {
            if (node instanceof Choice) {
                for (java.util.Enumeration anEnum = ((Choice) node).choices.elements(); anEnum.hasMoreElements(); ) {
                    postOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof Sequence) {
                for (java.util.Enumeration anEnum = ((Sequence) node).units.elements(); anEnum.hasMoreElements(); ) {
                    postOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof OneOrMore) {
                postOrderWalk(((OneOrMore) node).expansion, opObj);
            } else if (node instanceof ZeroOrMore) {
                postOrderWalk(((ZeroOrMore) node).expansion, opObj);
            } else if (node instanceof ZeroOrOne) {
                postOrderWalk(((ZeroOrOne) node).expansion, opObj);
            } else if (node instanceof Lookahead) {
                Expansion nested_e = ((Lookahead) node).la_expansion;
                if (!(nested_e instanceof Sequence && (Expansion) (((Sequence) nested_e).units.elementAt(0)) == node)) {
                    postOrderWalk(nested_e, opObj);
                }
            } else if (node instanceof TryBlock) {
                postOrderWalk(((TryBlock) node).exp, opObj);
            } else if (node instanceof RChoice) {
                for (java.util.Enumeration anEnum = ((RChoice) node).choices.elements(); anEnum.hasMoreElements(); ) {
                    postOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof RSequence) {
                for (java.util.Enumeration anEnum = ((RSequence) node).units.elements(); anEnum.hasMoreElements(); ) {
                    postOrderWalk((Expansion) anEnum.nextElement(), opObj);
                }
            } else if (node instanceof ROneOrMore) {
                postOrderWalk(((ROneOrMore) node).regexpr, opObj);
            } else if (node instanceof RZeroOrMore) {
                postOrderWalk(((RZeroOrMore) node).regexpr, opObj);
            } else if (node instanceof RZeroOrOne) {
                postOrderWalk(((RZeroOrOne) node).regexpr, opObj);
            } else if (node instanceof RRepetitionRange) {
                postOrderWalk(((RRepetitionRange) node).regexpr, opObj);
            }
        }
        opObj.action(node);
    }
}
