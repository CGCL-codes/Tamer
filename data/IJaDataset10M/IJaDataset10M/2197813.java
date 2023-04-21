package org.gjt.sp.jedit.textarea;

import java.util.Vector;
import org.gjt.sp.jedit.buffer.JEditBuffer;
import org.gjt.sp.jedit.textarea.Selection.Rect;

public class ColumnBlock extends Rect implements Node {

    private Node parent;

    private Vector<Node> children = new Vector<Node>();

    private Vector<ColumnBlockLine> lines = new Vector<ColumnBlockLine>();

    float columnBlockWidth;

    private boolean tabSizesDirty = true;

    private JEditBuffer buffer;

    private boolean isDirty;

    @Override
    public void addChild(Node node) {
        ColumnBlock block = (ColumnBlock) node;
        ColumnBlock blockBelow = searchChildren(block.startLine);
        if (blockBelow != null) {
            if (blockBelow.isLineWithinThisBlock(block.endLine) >= 0) {
                throw new IllegalArgumentException("Overlapping column blocks: " + block + " \n&\n" + blockBelow);
            }
            int index = children.indexOf(blockBelow);
            children.add(index, node);
        } else {
            children.add(node);
        }
    }

    @Override
    public Vector getChildren() {
        return children;
    }

    @Override
    public Node getParent() {
        return parent;
    }

    public void setWidth(int width) {
        columnBlockWidth = width;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setLines(Vector<ColumnBlockLine> lines) {
        this.lines = lines;
    }

    public Vector<ColumnBlockLine> getLines() {
        return lines;
    }

    public ColumnBlock() {
    }

    public ColumnBlock(JEditBuffer buffer, int startLine, int startColumn, int endLine, int endColumn) {
        super(buffer, startLine, startColumn, endLine, endColumn);
        this.buffer = buffer;
    }

    public ColumnBlock(JEditBuffer buffer, int startLine, int endLine) {
        this.startLine = startLine;
        this.endLine = endLine;
        this.buffer = buffer;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    @Override
    public int getEndLine() {
        return endLine;
    }

    public int getColumnWidth() {
        return (int) columnBlockWidth;
    }

    public int isLineWithinThisBlock(int line) {
        if (line < startLine) {
            return line - startLine;
        } else if (line > endLine) {
            return line - endLine;
        } else {
            return 0;
        }
    }

    public ColumnBlock getContainingBlock(int line, int offset) {
        ColumnBlock retBlock = null;
        if (line >= startLine && line <= endLine) {
            int relativeOffset = offset - buffer.getLineStartOffset(line);
            if (lines != null && !lines.isEmpty()) {
                ColumnBlockLine blockLine = lines.get(line - startLine);
                if (blockLine.getColumnEndIndex() >= relativeOffset && blockLine.getColumnStartIndex() <= relativeOffset) {
                    retBlock = this;
                }
            }
            if (retBlock == null && children != null && !children.isEmpty()) {
                ColumnBlock block = searchChildren(line);
                if (block != null && block.isLineWithinThisBlock(line) == 0) {
                    retBlock = block.getContainingBlock(line, offset);
                }
            }
        }
        return retBlock;
    }

    public ColumnBlock getColumnBlock(int line, int offset) {
        if (isDirty) {
            return null;
        }
        synchronized (buffer.columnBlockLock) {
            ColumnBlock colBlock = null;
            if (line >= startLine && line <= endLine) {
                if (lines != null && !lines.isEmpty()) {
                    ColumnBlockLine blockLine = lines.get(line - startLine);
                    if (blockLine.getColumnEndIndex() + buffer.getLineStartOffset(line) == offset) {
                        colBlock = this;
                    }
                }
                if (colBlock == null && children != null && !children.isEmpty()) {
                    ColumnBlock block = searchChildren(line, 0, children.size() - 1);
                    if (block == null || block.isLineWithinThisBlock(line) != 0) {
                        throwException(offset, line);
                    }
                    colBlock = block.getColumnBlock(line, offset);
                }
            }
            if (colBlock == null) throwException(offset, line);
            return colBlock;
        }
    }

    public ColumnBlock searchChildren(int line) {
        if (children != null && !children.isEmpty()) {
            return searchChildren(line, 0, children.size() - 1);
        } else {
            return null;
        }
    }

    private ColumnBlock searchChildren(int line, int startIndex, int stopIndex) {
        if (children != null) {
            if (startIndex > stopIndex) {
                return (ColumnBlock) children.get(startIndex);
            }
            int currentSearchIndex = (startIndex + stopIndex) / 2;
            int found = ((ColumnBlock) children.get(currentSearchIndex)).isLineWithinThisBlock(line);
            if (found == 0) {
                return (ColumnBlock) children.get(currentSearchIndex);
            } else if (found > 0) {
                if (children.size() - 1 > currentSearchIndex) {
                    return searchChildren(line, currentSearchIndex + 1, stopIndex);
                } else {
                    return null;
                }
            } else if (found < 0) {
                if (currentSearchIndex > 0) {
                    return searchChildren(line, startIndex, currentSearchIndex - 1);
                } else {
                    return (ColumnBlock) children.get(0);
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("ColumnBlock[startLine : ").append(startLine).append(" ,endLine : ").append(endLine).append(" ,columnBlockWidth : ").append(columnBlockWidth).append("] LINES:");
        for (int i = 0; i < lines.size(); i++) {
            buf.append('\n');
            buf.append("LINE ").append(i).append(':').append(lines.elementAt(i));
        }
        for (int i = 0; i < children.size(); i++) {
            buf.append('\n');
            buf.append("CHILD ").append(i).append(':').append(children.elementAt(i));
        }
        return buf.toString();
    }

    private void throwException(int offset, int line) {
        throw new IllegalArgumentException("{ELSTIC TABSTOP}CORRUPT DATA@{" + System.currentTimeMillis() + "} & Thread : " + Thread.currentThread().getName() + " :Cannot find the size for tab at offset " + (offset - buffer.getLineStartOffset(line)) + "in line " + line + "while searching in \n " + this);
    }

    public void setDirtyStatus(boolean status) {
        synchronized (buffer.columnBlockLock) {
            isDirty = status;
        }
    }

    public void updateLineNo(int line) {
        startLine += line;
        endLine += line;
        for (int i = 0; i < lines.size(); i++) {
            lines.elementAt(i).updateLineNo(line);
        }
        for (int i = 0; i < children.size(); i++) {
            ((ColumnBlock) children.elementAt(i)).updateLineNo(line);
        }
    }

    public void updateColumnBlockLineOffset(int line, int offsetAdd, boolean increaseStartOffset) {
        if (line >= startLine && line <= endLine) {
            if (lines != null && !lines.isEmpty()) {
                ColumnBlockLine blockLine = lines.get(line - startLine);
                if (increaseStartOffset) {
                    blockLine.colStartIndex += offsetAdd;
                }
                blockLine.colEndIndex += offsetAdd;
            }
            if (children != null && !children.isEmpty()) {
                ColumnBlock block = searchChildren(line);
                if (block != null && block.isLineWithinThisBlock(line) == 0) {
                    block.updateColumnBlockLineOffset(line, offsetAdd, true);
                }
            }
        }
    }

    public void setTabSizeDirtyStatus(boolean dirty, boolean recursive) {
        tabSizesDirty = dirty;
        if (recursive && children != null && !children.isEmpty()) {
            for (int i = 0; i < children.size(); i++) {
                ((ColumnBlock) children.elementAt(i)).setTabSizeDirtyStatus(true, true);
            }
        }
    }

    public boolean areTabSizesDirty() {
        return tabSizesDirty;
    }
}
