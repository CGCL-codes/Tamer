package net.sf.refactorit.transformations;

import net.sf.refactorit.classmodel.BinVariable;
import net.sf.refactorit.classmodel.CompilationUnit;
import net.sf.refactorit.loader.Comment;
import net.sf.refactorit.parser.ASTImpl;
import net.sf.refactorit.parser.ASTUtil;
import net.sf.refactorit.source.SourceCoordinate;

/**
 * Finds where array-declaration brackets end (after the variable name). I created
 * this class because there is no such info in ASTs, and could not figure out any other way to find it.
 *
 * @author  RISTO A
 */
public final class BracketFinder {

    public static SourceCoordinate findBracketsEndAfterName(BinVariable var) {
        if (!hasBracketsAfterName(var)) {
            return null;
        }
        SourceCoordinate c = SourceCoordinate.getForEnd(var.getNameAstOrNull());
        for (int i = 0; i < getBracketCountAfterName(var); i++) {
            c = findNextClosingBracket(var.getCompilationUnit(), c);
        }
        return c;
    }

    public static boolean hasBracketsAfterName(BinVariable var) {
        return getBracketCountAfterName(var) > 0;
    }

    public static int getBracketCountAfterName(BinVariable var) {
        ASTImpl typeNode = var.getTypeAst();
        int result = 0;
        while (!ASTUtil.isBefore(typeNode, var.getNameAstOrNull())) {
            result++;
            typeNode = (ASTImpl) typeNode.getFirstChild();
        }
        return result;
    }

    public static SourceCoordinate findNextClosingBracket(CompilationUnit compilationUnit, SourceCoordinate c) {
        int pos = compilationUnit.getLineIndexer().coordinateToPos(c);
        pos = findNextClosingBracketPosition(compilationUnit, pos);
        return compilationUnit.getLineIndexer().posToLineCol(pos);
    }

    private static int findNextClosingBracketPosition(CompilationUnit compilationUnit, int pos) {
        String content = compilationUnit.getContent();
        do {
            pos++;
        } while (content.charAt(pos) != ']' || isCommentAt(compilationUnit, pos));
        return pos;
    }

    private static boolean isCommentAt(final CompilationUnit compilationUnit, final int pos) {
        return Comment.findAt(compilationUnit, pos) != null;
    }
}
