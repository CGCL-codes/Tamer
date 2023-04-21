package net.sf.refactorit.classmodel;

import net.sf.refactorit.commonIDE.ItemByCoordinateFinder;
import net.sf.refactorit.query.LineIndexer;
import net.sf.refactorit.source.SourceCoordinate;

/**
 * Keeps selection range.
 *
 * @author Vladislav Vislogubov
 */
public final class BinSelection extends AbstractLocationAware {

    protected CompilationUnit compilationUnit;

    protected SourceCoordinate start;

    protected SourceCoordinate end;

    /**
   * Constructor for BinSelection. Used only in Netbeans and JBuilder
   *
   * If you are using this constructor please do not forget to call
   * setCompilationUnit() method later !!!
   */
    public BinSelection(String text, int startLine, int startColumn, int endLine, int endColumn) {
        start = new SourceCoordinate(startLine, startColumn);
        end = new SourceCoordinate(endLine, endColumn);
    }

    /**
   * Constructor for BinSelection.
   */
    public BinSelection(CompilationUnit compilationUnit, String text, int startPos, int endPos) {
        this.compilationUnit = compilationUnit;
        final LineIndexer indexer = this.compilationUnit.getLineIndexer();
        start = indexer.posToLineCol(startPos);
        end = indexer.posToLineCol(endPos);
        trim();
    }

    public SourceCoordinate getStartSourceCoordinate() {
        return start;
    }

    public SourceCoordinate getEndSourceCoordinate() {
        return end;
    }

    public int getStartLine() {
        return start.getLine();
    }

    public int getStartColumn() {
        return start.getColumn();
    }

    public int getEndLine() {
        return end.getLine();
    }

    public int getEndColumn() {
        return end.getColumn();
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        CompilationUnit oldCompilationUnit = this.compilationUnit;
        this.compilationUnit = compilationUnit;
        if (this.compilationUnit != oldCompilationUnit) {
            trim();
        }
    }

    public String getText() {
        try {
            return super.getText();
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public String toString() {
        return "BinSelection: " + compilationUnit + ", " + start.getLine() + ":" + start.getColumn() + " - " + end.getLine() + ":" + end.getColumn() + " (" + getStartPosition() + ":" + getEndPosition() + ")";
    }

    private void trim() {
        final String content = getCompilationUnit().getContent();
        int startPos = getStartPosition();
        int endPos = getEndPosition();
        while (startPos < content.length() && Character.isWhitespace(content.charAt(startPos))) {
            ++startPos;
        }
        while (endPos - 1 >= 0 && Character.isWhitespace(content.charAt(endPos - 1))) {
            --endPos;
        }
        final LineIndexer indexer = getCompilationUnit().getLineIndexer();
        this.start = indexer.posToLineCol(startPos);
        this.end = indexer.posToLineCol(endPos);
    }

    public BinItem startsWith() {
        int pos = getStartPosition();
        CompilationUnit compilationUnit = getCompilationUnit();
        String content = compilationUnit.getContent();
        while (pos < content.length() && !Character.isJavaIdentifierStart(content.charAt(pos))) {
            ++pos;
        }
        if (pos >= content.length()) {
            return null;
        }
        return new ItemByCoordinateFinder(compilationUnit).findItemAt(compilationUnit.getLineIndexer().posToLineCol(pos));
    }

    public final BinCIType getParentType() {
        ItemByCoordinateFinder finder = new ItemByCoordinateFinder(getCompilationUnit());
        BinItem item = finder.findItemAt(getStartSourceCoordinate());
        if (item != null && !(item instanceof BinCIType)) {
            item = item.getParentType();
        }
        if (item != null) {
            return (BinCIType) item;
        } else {
            return super.getParentType();
        }
    }
}
