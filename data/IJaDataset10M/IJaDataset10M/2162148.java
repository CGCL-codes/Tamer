package pl.wcislo.sbql4j.tools.javac.jvm;

import java.util.HashMap;
import java.util.Map;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCArrayAccess;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCArrayTypeTree;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCAssert;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCAssign;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCAssignOp;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCBinary;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCBlock;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCBreak;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCCase;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCCatch;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCConditional;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCContinue;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCDoWhileLoop;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCEnhancedForLoop;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCErroneous;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCExpressionStatement;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCFieldAccess;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCForLoop;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCIdent;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCIf;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCInstanceOf;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCLabeledStatement;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCLiteral;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCMethodInvocation;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCNewArray;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCNewClass;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCParens;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCPrimitiveTypeTree;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCReturn;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSkip;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSwitch;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCSynchronized;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCThrow;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCTry;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCTypeApply;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCTypeCast;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCTypeParameter;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCUnary;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCVariableDecl;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCWhileLoop;
import pl.wcislo.sbql4j.tools.javac.tree.JCTree.JCWildcard;
import pl.wcislo.sbql4j.tools.javac.util.ByteBuffer;
import pl.wcislo.sbql4j.tools.javac.util.List;
import pl.wcislo.sbql4j.tools.javac.util.ListBuffer;
import pl.wcislo.sbql4j.tools.javac.util.Log;
import pl.wcislo.sbql4j.tools.javac.util.Position;

/** This class contains the CharacterRangeTable for some method
 *  and the hashtable for mapping trees or lists of trees to their
 *  ending positions.
 *
 *  <p><b>This is NOT part of any API supported by Sun Microsystems.  If
 *  you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class CRTable implements CRTFlags {

    private final boolean crtDebug = false;

    /** The list of CRTable entries.
     */
    private ListBuffer<CRTEntry> entries = new ListBuffer<CRTEntry>();

    /** The hashtable for source positions.
     */
    private Map<Object, SourceRange> positions = new HashMap<Object, SourceRange>();

    /** The hashtable for ending positions stored in the parser.
     */
    private Map<JCTree, Integer> endPositions;

    /** The tree of the method this table is intended for.
     *  We should traverse this tree to get source ranges.
     */
    JCTree.JCMethodDecl methodTree;

    /** Constructor
     */
    public CRTable(JCTree.JCMethodDecl tree, Map<JCTree, Integer> endPositions) {
        this.methodTree = tree;
        this.endPositions = endPositions;
    }

    /** Create a new CRTEntry and add it to the entries.
     *  @param tree     The tree or the list of trees for which
     *                  we are storing the code pointers.
     *  @param flags    The set of flags designating type of the entry.
     *  @param startPc  The starting code position.
     *  @param endPc    The ending code position.
     */
    public void put(Object tree, int flags, int startPc, int endPc) {
        entries.append(new CRTEntry(tree, flags, startPc, endPc));
    }

    /** Compute source positions and write CRT to the databuf.
     *  @param databuf  The buffer to write bytecodes to.
     */
    public int writeCRT(ByteBuffer databuf, Position.LineMap lineMap, Log log) {
        int crtEntries = 0;
        new SourceComputer().csp(methodTree);
        for (List<CRTEntry> l = entries.toList(); l.nonEmpty(); l = l.tail) {
            CRTEntry entry = l.head;
            if (entry.startPc == entry.endPc) continue;
            SourceRange pos = positions.get(entry.tree);
            assert pos != null : "CRT: tree source positions are undefined";
            if ((pos.startPos == Position.NOPOS) || (pos.endPos == Position.NOPOS)) continue;
            if (crtDebug) {
                System.out.println("Tree: " + entry.tree + ", type:" + getTypes(entry.flags));
                System.out.print("Start: pos = " + pos.startPos + ", pc = " + entry.startPc);
            }
            int startPos = encodePosition(pos.startPos, lineMap, log);
            if (startPos == Position.NOPOS) continue;
            if (crtDebug) {
                System.out.print("End:   pos = " + pos.endPos + ", pc = " + (entry.endPc - 1));
            }
            int endPos = encodePosition(pos.endPos, lineMap, log);
            if (endPos == Position.NOPOS) continue;
            databuf.appendChar(entry.startPc);
            databuf.appendChar(entry.endPc - 1);
            databuf.appendInt(startPos);
            databuf.appendInt(endPos);
            databuf.appendChar(entry.flags);
            crtEntries++;
        }
        return crtEntries;
    }

    /** Return the number of the entries.
     */
    public int length() {
        return entries.length();
    }

    /** Return string describing flags enabled.
     */
    private String getTypes(int flags) {
        String types = "";
        if ((flags & CRT_STATEMENT) != 0) types += " CRT_STATEMENT";
        if ((flags & CRT_BLOCK) != 0) types += " CRT_BLOCK";
        if ((flags & CRT_ASSIGNMENT) != 0) types += " CRT_ASSIGNMENT";
        if ((flags & CRT_FLOW_CONTROLLER) != 0) types += " CRT_FLOW_CONTROLLER";
        if ((flags & CRT_FLOW_TARGET) != 0) types += " CRT_FLOW_TARGET";
        if ((flags & CRT_INVOKE) != 0) types += " CRT_INVOKE";
        if ((flags & CRT_CREATE) != 0) types += " CRT_CREATE";
        if ((flags & CRT_BRANCH_TRUE) != 0) types += " CRT_BRANCH_TRUE";
        if ((flags & CRT_BRANCH_FALSE) != 0) types += " CRT_BRANCH_FALSE";
        return types;
    }

    /** Source file positions in CRT are integers in the format:
     *  line-number << LINESHIFT + column-number
     */
    private int encodePosition(int pos, Position.LineMap lineMap, Log log) {
        int line = lineMap.getLineNumber(pos);
        int col = lineMap.getColumnNumber(pos);
        int new_pos = Position.encodePosition(line, col);
        if (crtDebug) {
            System.out.println(", line = " + line + ", column = " + col + ", new_pos = " + new_pos);
        }
        if (new_pos == Position.NOPOS) log.warning(pos, "position.overflow", line);
        return new_pos;
    }

    /**
     *  This class contains methods to compute source positions for trees.
     *  Extends Tree.Visitor to traverse the abstract syntax tree.
     */
    class SourceComputer extends JCTree.Visitor {

        /** The result of the tree traversal methods.
         */
        SourceRange result;

        /** Visitor method: compute source positions for a single node.
         */
        public SourceRange csp(JCTree tree) {
            if (tree == null) return null;
            tree.accept(this);
            if (result != null) {
                positions.put(tree, result);
            }
            return result;
        }

        /** Visitor method: compute source positions for a list of nodes.
         */
        public SourceRange csp(List<? extends JCTree> trees) {
            if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<? extends JCTree> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        /**  Visitor method: compute source positions for
         *    a list of case blocks of switch statements.
         */
        public SourceRange cspCases(List<JCCase> trees) {
            if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<JCCase> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        /**  Visitor method: compute source positions for
         *   a list of catch clauses in try statements.
         */
        public SourceRange cspCatchers(List<JCCatch> trees) {
            if ((trees == null) || !(trees.nonEmpty())) return null;
            SourceRange list_sr = new SourceRange();
            for (List<JCCatch> l = trees; l.nonEmpty(); l = l.tail) {
                list_sr.mergeWith(csp(l.head));
            }
            positions.put(trees, list_sr);
            return list_sr;
        }

        public void visitMethodDef(JCMethodDecl tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitVarDef(JCVariableDecl tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            csp(tree.vartype);
            sr.mergeWith(csp(tree.init));
            result = sr;
        }

        public void visitSkip(JCSkip tree) {
            SourceRange sr = new SourceRange(startPos(tree), startPos(tree));
            result = sr;
        }

        public void visitBlock(JCBlock tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            csp(tree.stats);
            result = sr;
        }

        public void visitDoLoop(JCDoWhileLoop tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            sr.mergeWith(csp(tree.cond));
            result = sr;
        }

        public void visitWhileLoop(JCWhileLoop tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitForLoop(JCForLoop tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.init));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.step));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitForeachLoop(JCEnhancedForLoop tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.var));
            sr.mergeWith(csp(tree.expr));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitLabelled(JCLabeledStatement tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitSwitch(JCSwitch tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.selector));
            sr.mergeWith(cspCases(tree.cases));
            result = sr;
        }

        public void visitCase(JCCase tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.pat));
            sr.mergeWith(csp(tree.stats));
            result = sr;
        }

        public void visitSynchronized(JCSynchronized tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lock));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitTry(JCTry tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.body));
            sr.mergeWith(cspCatchers(tree.catchers));
            sr.mergeWith(csp(tree.finalizer));
            result = sr;
        }

        public void visitCatch(JCCatch tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.param));
            sr.mergeWith(csp(tree.body));
            result = sr;
        }

        public void visitConditional(JCConditional tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.truepart));
            sr.mergeWith(csp(tree.falsepart));
            result = sr;
        }

        public void visitIf(JCIf tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.thenpart));
            sr.mergeWith(csp(tree.elsepart));
            result = sr;
        }

        public void visitExec(JCExpressionStatement tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitBreak(JCBreak tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitContinue(JCContinue tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitReturn(JCReturn tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitThrow(JCThrow tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitAssert(JCAssert tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.cond));
            sr.mergeWith(csp(tree.detail));
            result = sr;
        }

        public void visitApply(JCMethodInvocation tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.meth));
            sr.mergeWith(csp(tree.args));
            result = sr;
        }

        public void visitNewClass(JCNewClass tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.encl));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.args));
            sr.mergeWith(csp(tree.def));
            result = sr;
        }

        public void visitNewArray(JCNewArray tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.elemtype));
            sr.mergeWith(csp(tree.dims));
            sr.mergeWith(csp(tree.elems));
            result = sr;
        }

        public void visitParens(JCParens tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitAssign(JCAssign tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitAssignop(JCAssignOp tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitUnary(JCUnary tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.arg));
            result = sr;
        }

        public void visitBinary(JCBinary tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.lhs));
            sr.mergeWith(csp(tree.rhs));
            result = sr;
        }

        public void visitTypeCast(JCTypeCast tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.expr));
            result = sr;
        }

        public void visitTypeTest(JCInstanceOf tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.expr));
            sr.mergeWith(csp(tree.clazz));
            result = sr;
        }

        public void visitIndexed(JCArrayAccess tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.indexed));
            sr.mergeWith(csp(tree.index));
            result = sr;
        }

        public void visitSelect(JCFieldAccess tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.selected));
            result = sr;
        }

        public void visitIdent(JCIdent tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitLiteral(JCLiteral tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitTypeIdent(JCPrimitiveTypeTree tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            result = sr;
        }

        public void visitTypeArray(JCArrayTypeTree tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.elemtype));
            result = sr;
        }

        public void visitTypeApply(JCTypeApply tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.clazz));
            sr.mergeWith(csp(tree.arguments));
            result = sr;
        }

        public void visitTypeParameter(JCTypeParameter tree) {
            SourceRange sr = new SourceRange(startPos(tree), endPos(tree));
            sr.mergeWith(csp(tree.bounds));
            result = sr;
        }

        public void visitWildcard(JCWildcard tree) {
            result = null;
        }

        public void visitErroneous(JCErroneous tree) {
            result = null;
        }

        public void visitTree(JCTree tree) {
            assert false;
        }

        /** The start position of given tree.
         */
        public int startPos(JCTree tree) {
            if (tree == null) return Position.NOPOS;
            return tree.pos;
        }

        /** The end position of given tree, if it has
         *  defined endpos, NOPOS otherwise.
         */
        public int endPos(JCTree tree) {
            if (tree == null) return Position.NOPOS;
            if (tree.getTag() == JCTree.BLOCK) return ((JCBlock) tree).endpos;
            Integer endpos = endPositions.get(tree);
            if (endpos != null) return endpos.intValue();
            return Position.NOPOS;
        }
    }

    /** This class contains a CharacterRangeTableEntry.
     */
    static class CRTEntry {

        /** A tree or a list of trees to obtain source positions.
         */
        Object tree;

        /** The flags described in the CharacterRangeTable spec.
         */
        int flags;

        /** The starting code position of this entry.
         */
        int startPc;

        /** The ending code position of this entry.
         */
        int endPc;

        /** Constructor */
        CRTEntry(Object tree, int flags, int startPc, int endPc) {
            this.tree = tree;
            this.flags = flags;
            this.startPc = startPc;
            this.endPc = endPc;
        }
    }

    /** This class contains source positions
     *  for some tree or list of trees.
     */
    static class SourceRange {

        /** The starting source position.
         */
        int startPos;

        /** The ending source position.
         */
        int endPos;

        /** Constructor */
        SourceRange() {
            startPos = Position.NOPOS;
            endPos = Position.NOPOS;
        }

        /** Constructor */
        SourceRange(int startPos, int endPos) {
            this.startPos = startPos;
            this.endPos = endPos;
        }

        /** Compare the starting and the ending positions
         *  of the source range and combines them assigning
         *  the widest range to this.
         */
        SourceRange mergeWith(SourceRange sr) {
            if (sr == null) return this;
            if (startPos == Position.NOPOS) startPos = sr.startPos; else if (sr.startPos != Position.NOPOS) startPos = (startPos < sr.startPos ? startPos : sr.startPos);
            if (endPos == Position.NOPOS) endPos = sr.endPos; else if (sr.endPos != Position.NOPOS) endPos = (endPos > sr.endPos ? endPos : sr.endPos);
            return this;
        }
    }
}
