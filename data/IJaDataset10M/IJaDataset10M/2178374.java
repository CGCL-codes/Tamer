package org.butu.ktable.models.mosaic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.butu.core.lazy.Lazy;
import org.butu.ktable.listeners.MosaicBlockCellSelectionListener;
import org.butu.ktable.listeners.MosaicBlockMouseListener;
import org.butu.ktable.listeners.MosaicRowCellSelectionListener;
import org.butu.ktable.listeners.MosaicRowMouseListener;
import org.eclipse.swt.graphics.Point;
import de.kupzog.ktable.KTableCellEditor;
import de.kupzog.ktable.KTableCellRenderer;

/**
 * ����� ������������ ������������ ������ ������� Mosaic ��� ������ ������� ����������� �������.
 * @author kbakaras
 *
 */
public class MosaicHeaderRow extends AMosaicHeaderRow {

    public static class BlockCoordinates {

        public BlockCoordinates(AMosaicHeaderRowBlock block, int index, int begining) {
            this.block = block;
            this.index = index;
            this.begining = begining;
        }

        private AMosaicHeaderRowBlock block;

        private int index;

        private int begining;

        public AMosaicHeaderRowBlock getBlock() {
            return block;
        }

        public int getIndex() {
            return index;
        }

        public int getBegining() {
            return begining;
        }

        public int getLocal(int col) {
            return col - begining;
        }
    }

    private KTableMosaicModel parent;

    private AMosaicCornerBlock headerBlock;

    private List<AMosaicHeaderRowBlock> blocks = new ArrayList<AMosaicHeaderRowBlock>();

    private int[] beginings;

    private BlockCoordinates[] coordinates;

    private boolean valid;

    private LastCacheMono<BlockCoordinates> lastCache = new LastCacheMono<BlockCoordinates>() {

        private Lazy<BlockCoordinates> lHeader = new Lazy<BlockCoordinates>() {

            protected BlockCoordinates doGet() {
                return new BlockCoordinates(headerBlock, 0, 0);
            }
        };

        public BlockCoordinates get(int i) {
            validate();
            return super.get(i);
        }

        protected BlockCoordinates doGet(int col) {
            int count = Math.abs(Arrays.binarySearch(beginings, col) + 1) - 1;
            if (count == -1) {
                return lHeader.get();
            } else {
                BlockCoordinates bc = coordinates[count];
                if (count == beginings.length - 1 && bc.getBegining() + bc.getBlock().getColumnCount(bc.getIndex()) <= col) {
                    throw new MosaicException();
                }
                return bc;
            }
        }
    };

    private int columnCount;

    public MosaicHeaderRow(KTableMosaicModel parent) {
        this.parent = parent;
    }

    public AMosaicCornerBlock getHeaderBlock() {
        return headerBlock;
    }

    public void setHeaderBlock(AMosaicCornerBlock block) {
        headerBlock = block;
    }

    public void addBlock(AMosaicHeaderRowBlock block) {
        blocks.add(block);
    }

    public List<AMosaicHeaderRowBlock> getBlocks() {
        return blocks;
    }

    public int getFixedHeaderColumnCount() {
        return getHeaderBlock().getColumnCount() - getHeaderBlock().getFixedColumnCount();
    }

    public int getFixedSelectableColumnCount() {
        return getHeaderBlock().getFixedColumnCount();
    }

    public int getInitialColumnWidth(int col) {
        BlockCoordinates bc = lastCache.get(col);
        return bc.getBlock().getInitialColumnWidth(bc.getIndex(), bc.getLocal(col));
    }

    public int getInitialRowHeight(int row) {
        return headerBlock.getInitialRowHeight(row);
    }

    public boolean isColumnResizable(int col) {
        BlockCoordinates bc = lastCache.get(col);
        return bc.getBlock().isColumnResizable(bc.getIndex(), bc.getLocal(col));
    }

    public boolean isRowResizable(int row) {
        return headerBlock.isRowResizable(row);
    }

    public int getColumnCount() {
        validate();
        return columnCount;
    }

    public int getRowCount() {
        return headerBlock.getRowCount();
    }

    public KTableCellEditor getCellEditor(int col, int row) {
        return null;
    }

    public KTableCellRenderer getCellRenderer(int col, int row) {
        BlockCoordinates bc = lastCache.get(col);
        return bc.getBlock().getCellRenderer(bc.getIndex(), bc.getLocal(col), row);
    }

    public Point belongsToCell(int col, int row) {
        BlockCoordinates bc = lastCache.get(col);
        Point point = bc.getBlock().belongsToCell(bc.getIndex(), bc.getLocal(col), row);
        if (point != null && bc.getBegining() != 0) {
            point.x = point.x + bc.getBegining();
        }
        return point;
    }

    public Object getContentAt(int col, int row) {
        BlockCoordinates bc = lastCache.get(col);
        return bc.getBlock().getContentAt(bc.getIndex(), bc.getLocal(col), row);
    }

    public void invalidate() {
        valid = false;
        lastCache.clear();
    }

    protected void validate() {
        if (!valid) {
            columnCount = getHeaderBlock().getColumnCount();
            for (AMosaicHeaderRowBlock block : getBlocks()) {
                for (int i = 0; i < block.size(); i++) {
                    columnCount += block.getColumnCount(i);
                }
            }
            doCacheBlocks();
            valid = true;
        }
    }

    protected void doCacheBlocks() {
        int count = 0;
        for (AMosaicHeaderRowBlock block : blocks) {
            count += block.size();
        }
        beginings = new int[count];
        coordinates = new BlockCoordinates[count];
        count = 0;
        int begining = headerBlock != null ? headerBlock.getColumnCount() : 0;
        for (AMosaicHeaderRowBlock block : blocks) {
            for (int index = 0; index < block.size(); index++) {
                beginings[count] = begining;
                coordinates[count] = new BlockCoordinates(block, index, begining);
                begining += block.getColumnCount(index);
                count++;
            }
        }
    }

    public Point getAbsolutePoint(AMosaicHeaderRowBlock block, int blockIndex, int col, int row) {
        if (headerBlock != null && headerBlock.equals(block)) {
            return new Point(col, row);
        } else {
            int begining = headerBlock != null ? headerBlock.getColumnCount() : 0;
            for (IMosaicBlock mBlock : blocks) {
                if (mBlock.equals(block)) {
                    for (int i = 0; i < blockIndex; i++) {
                        begining += mBlock.getColumnCount(i);
                    }
                    return new Point(begining + col, row);
                } else {
                    for (int i = 0; i < mBlock.size(); i++) {
                        begining += mBlock.getColumnCount(blockIndex);
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param absoluteCol ���������� ����� �������
     * @return ���������� ���������� �����, � ������� ��������� �������
     * @see BlockCoordinates
     */
    public BlockCoordinates getBlock(int absoluteCol) {
        return lastCache.get(absoluteCol);
    }

    private static class LazyListeners<L> extends Lazy<Map<IMosaicBlock, L>> {

        protected Map<IMosaicBlock, L> doGet() {
            return new HashMap<IMosaicBlock, L>();
        }
    }

    private LazyListeners<MosaicBlockMouseListener> lMouseListeners = new LazyListeners<MosaicBlockMouseListener>();

    private Lazy<MosaicRowMouseListener> lMouseListener = new Lazy<MosaicRowMouseListener>() {

        protected MosaicRowMouseListener doGet() {
            return new MosaicRowMouseListener() {

                private MosaicBlockMouseListener listener;

                private int blockIndex;

                private int localCol;

                private boolean locateListener(int col) {
                    BlockCoordinates bc = lastCache.get(col);
                    blockIndex = bc.getIndex();
                    localCol = bc.getLocal(col);
                    listener = lMouseListeners.get().get(bc.getBlock());
                    return listener != null;
                }

                public void mouseDoubleClick(int index, int col, int row) {
                    if (locateListener(col)) listener.mouseDoubleClick(blockIndex, index, localCol, row);
                }

                public void mouseDown(int index, int col, int row) {
                    if (locateListener(col)) listener.mouseDown(blockIndex, index, localCol, row);
                }

                public void mouseUp(int index, int col, int row) {
                    if (locateListener(col)) listener.mouseUp(blockIndex, index, localCol, row);
                }
            };
        }
    };

    public void addMouseListener(IMosaicBlock block, MosaicBlockMouseListener listener) {
        if (lMouseListeners.isVirgin()) parent.addMouseListener(this, lMouseListener.get());
        lMouseListeners.get().put(block, listener);
    }

    public void removeMouseListener(IMosaicRow block, MosaicBlockMouseListener listener) {
        if (!lMouseListeners.isVirgin()) {
            lMouseListeners.get().remove(block);
            if (lMouseListeners.get().isEmpty()) {
                lMouseListeners.clear();
                parent.removeMouseListener(this, lMouseListener.get());
            }
        }
    }

    private LazyListeners<MosaicBlockCellSelectionListener> lCellSelectionListeners = new LazyListeners<MosaicBlockCellSelectionListener>();

    private Lazy<MosaicRowCellSelectionListener> lCellSelectionListener = new Lazy<MosaicRowCellSelectionListener>() {

        protected MosaicRowCellSelectionListener doGet() {
            return new MosaicRowCellSelectionListener() {

                private MosaicBlockCellSelectionListener listener;

                private int blockIndex;

                private int localCol;

                private boolean locateListener(int col) {
                    BlockCoordinates bc = lastCache.get(col);
                    blockIndex = bc.getIndex();
                    localCol = bc.getLocal(col);
                    listener = lCellSelectionListeners.get().get(bc.getBlock());
                    return listener != null;
                }

                public void fixedCellSelected(int index, int col, int row, int statemask) {
                    if (locateListener(col)) listener.fixedCellSelected(blockIndex, index, localCol, row, statemask);
                }

                public void cellSelected(int index, int col, int row, int statemask) {
                    if (locateListener(col)) listener.cellSelected(blockIndex, index, localCol, row, statemask);
                }
            };
        }
    };

    public void addCellSelectionListener(IMosaicBlock block, MosaicBlockCellSelectionListener listener) {
        if (lCellSelectionListeners.isVirgin()) parent.addCellSelectionListener(this, lCellSelectionListener.get());
        lCellSelectionListeners.get().put(block, listener);
    }

    public void removeCellSelectionListener(IMosaicRow block, MosaicBlockCellSelectionListener listener) {
        if (!lCellSelectionListeners.isVirgin()) {
            lCellSelectionListeners.get().remove(block);
            if (lCellSelectionListeners.get().isEmpty()) {
                lCellSelectionListeners.clear();
                parent.removeCellSelectionListener(this, lCellSelectionListener.get());
            }
        }
    }
}
