package com.vladium.emma.data;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import com.vladium.util.IConstants;
import com.vladium.util.IntObjectMap;
import com.vladium.util.IntSet;
import com.vladium.util.asserts.$assert;

/**
 * @author Vlad Roubtsov, (C) 2003
 */
public final class MethodDescriptor implements IConstants, IMetadataConstants, Serializable {

    public MethodDescriptor(final String name, final String descriptor, final int status, final int[] blockSizes, final int[][] blockMap, final int firstLine) {
        if (name == null) throw new IllegalArgumentException("null input: name");
        if (descriptor == null) throw new IllegalArgumentException("null input: descriptor");
        if ((status & METHOD_NO_BLOCK_DATA) == 0) {
            final int blockCount = blockSizes.length;
            if ($assert.ENABLED) $assert.ASSERT(blockCount > 0, "blockCount must be positive: " + blockCount);
            m_blockSizes = blockSizes;
            if ((status & METHOD_NO_LINE_DATA) == 0) {
                if ($assert.ENABLED) $assert.ASSERT(firstLine > 0, "firstLine must be positive: " + firstLine);
                if ((blockMap == null) || (blockMap.length == 0)) throw new IllegalArgumentException("null or empty input: blockMap");
                if ($assert.ENABLED) {
                    $assert.ASSERT(blockCount == blockMap.length, "blockCount " + blockCount + " != blockMap.length " + blockMap.length);
                    for (int i = 0; i < blockMap.length; ++i) {
                        $assert.ASSERT(blockMap[i] != null, "blockMap[" + i + "] is null");
                    }
                }
                m_blockMap = blockMap;
                m_firstLine = firstLine;
            } else {
                m_blockMap = null;
                m_firstLine = 0;
            }
        } else {
            m_blockSizes = null;
            m_blockMap = null;
            m_firstLine = 0;
        }
        m_name = name;
        m_descriptor = descriptor;
        m_status = status;
    }

    public String getName() {
        return m_name;
    }

    public String getDescriptor() {
        return m_descriptor;
    }

    public int getStatus() {
        return m_status;
    }

    public int getBlockCount() {
        return m_blockSizes.length;
    }

    public int[] getBlockSizes() {
        return m_blockSizes;
    }

    public int[][] getBlockMap() {
        return m_blockMap;
    }

    public IntObjectMap getLineMap() {
        IntObjectMap lineMap = m_lineMap;
        if (lineMap != null) return lineMap; else if ((m_status & METHOD_NO_LINE_DATA) == 0) {
            lineMap = new IntObjectMap();
            final int[][] blockMap = m_blockMap;
            for (int bl = 0, blCount = blockMap.length; bl < blCount; ++bl) {
                final int[] lines = blockMap[bl];
                if (lines != null) {
                    final int lineCount = lines.length;
                    for (int l = 0; l < lineCount; ++l) {
                        final int line = lines[l];
                        IntSet blockIDs = (IntSet) lineMap.get(line);
                        if (blockIDs == null) {
                            blockIDs = new IntSet();
                            lineMap.put(line, blockIDs);
                        }
                        blockIDs.add(bl);
                    }
                }
            }
            final int[] lines = lineMap.keys();
            for (int l = 0, lineCount = lines.length; l < lineCount; ++l) {
                final int line = lines[l];
                final int[] blockIDs = ((IntSet) lineMap.get(line)).values();
                if ($assert.ENABLED) $assert.ASSERT(blockIDs != null && blockIDs.length > 0, "wrong line mapping for line #" + line);
                lineMap.put(line, blockIDs);
            }
            m_lineMap = lineMap;
            return lineMap;
        }
        return null;
    }

    public int getFirstLine() {
        return m_firstLine;
    }

    public boolean hasLineNumberInfo() {
        return (m_status & METHOD_NO_LINE_DATA) == 0;
    }

    public String toString() {
        return toString("");
    }

    public String toString(final String indent) {
        StringBuffer s = new StringBuffer(indent + "method [" + m_name + "] descriptor:");
        if ((m_status & METHOD_NO_LINE_DATA) == 0) {
            for (int bl = 0; bl < m_blockMap.length; ++bl) {
                s.append(EOL);
                s.append(indent + INDENT_INCREMENT + "block " + bl + " (" + m_blockSizes[bl] + " instrs) : ");
                final int[] lines = m_blockMap[bl];
                for (int l = 0; l < lines.length; ++l) {
                    if (l != 0) s.append(", ");
                    s.append(lines[l]);
                }
            }
            s.append(EOL);
            s.append(indent + INDENT_INCREMENT + "---");
            final int[] lines = m_lineMap.keys();
            for (int l = 0; l < lines.length; ++l) {
                s.append(EOL);
                s.append(indent + INDENT_INCREMENT + "line " + lines[l] + ": ");
                final int[] blocks = (int[]) m_lineMap.get(lines[l]);
                for (int bl = 0; bl < blocks.length; ++bl) {
                    if (bl != 0) s.append(", ");
                    s.append(blocks[bl]);
                }
            }
        } else {
            s.append(" <no line info>");
        }
        return s.toString();
    }

    static MethodDescriptor readExternal(final DataInput in) throws IOException {
        final String name = in.readUTF();
        final String descriptor = in.readUTF();
        final int status = in.readInt();
        int[] blockSizes = null;
        int[][] blockMap = null;
        int firstLine = 0;
        if ((status & METHOD_NO_BLOCK_DATA) == 0) {
            blockSizes = DataFactory.readIntArray(in);
            if ((status & METHOD_NO_LINE_DATA) == 0) {
                final int length = in.readInt();
                blockMap = new int[length][];
                for (int i = 0; i < length; ++i) {
                    blockMap[i] = DataFactory.readIntArray(in);
                }
                firstLine = in.readInt();
            }
        }
        return new MethodDescriptor(name, descriptor, status, blockSizes, blockMap, firstLine);
    }

    static void writeExternal(final MethodDescriptor method, final DataOutput out) throws IOException {
        out.writeUTF(method.m_name);
        out.writeUTF(method.m_descriptor);
        final int status = method.m_status;
        out.writeInt(status);
        if ((status & METHOD_NO_BLOCK_DATA) == 0) {
            DataFactory.writeIntArray(method.m_blockSizes, out);
            if ((status & METHOD_NO_LINE_DATA) == 0) {
                final int[][] blockMap = method.m_blockMap;
                final int length = blockMap.length;
                out.writeInt(length);
                for (int i = 0; i < length; ++i) {
                    DataFactory.writeIntArray(blockMap[i], out);
                }
                out.writeInt(method.m_firstLine);
            }
        }
    }

    private final String m_name;

    private final String m_descriptor;

    private final int m_status;

    private final int[] m_blockSizes;

    private final int[][] m_blockMap;

    private final int m_firstLine;

    private IntObjectMap m_lineMap;
}
