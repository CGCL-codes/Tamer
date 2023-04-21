package printer;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class FormLayout implements LayoutManager {

    private int m_hgap = 5;

    private int m_vgap = 5;

    private int m_externalHGap = 0;

    private int m_externalVGap = 0;

    private double m_pct = 0;

    private Vector m_Columns = new Vector();

    private ColumnLeader m_ColumnLeader = null;

    private RowLeader m_RowLeader = null;

    private Container m_container = null;

    /**
    *
    * Specifies default alignment for the associated component (used in the <code>FormLayout.add()</code> methods).
    *
    */
    public static final int DEFAULT = 0;

    /**
    *
    * Special alignment: the associated label will not align with other labels in this column (used in certain <code>FormLayout.add()</code> methods).
    *
    */
    public static final int FREE_LABEL = 1;

    /**
    *
    * Special alignment: the associated field will not align with other fields in this column (used in certain <code>FormLayout.add()</code> methods).
    *
    */
    public static final int FREE_FIELD = 2;

    /**
    *
    * Special alignment: the label and field will appear in subsequent columns, starting with the one specified (used in certain <code>FormLayout.add()</code> methods).
    *
    */
    public static final int LABEL_ON_TOP = 3;

    private static final int DEBUG_LEVEL = 0;

    public FormLayout() {
        debug("FormLayout.FormLayout()", 3);
        init();
    }

    /**
    * Most specific gap access available.
    */
    public FormLayout(int internalHGap, int internalVGap, int externalHGap, int externalVGap) {
        debug("FormLayout.FormLayout(int, int, int, int): internalHGap " + internalHGap + ", internalVGap " + internalVGap + ", externalHGap " + externalHGap + ", externalVGap " + externalVGap, 3);
        m_hgap = internalHGap;
        m_vgap = internalVGap;
        m_externalHGap = externalHGap;
        m_externalVGap = externalVGap;
        init();
    }

    /**
    *
    * externalHGap + left inset of the container.
    *
    */
    protected int getLeftInset() {
        return m_externalHGap + m_container.getInsets().left;
    }

    /**
    *
    * externalHGap + right inset of the container.
    *
    */
    protected int getRightInset() {
        return m_externalHGap + m_container.getInsets().right;
    }

    /**
    *
    * externalVGap + top inset of the container.
    *
    */
    protected int getTopInset() {
        return m_externalVGap + m_container.getInsets().top;
    }

    /**
    *
    * externalVGap + bottom inset of the container
    *
    */
    protected int getBottomInset() {
        return m_externalVGap + m_container.getInsets().bottom;
    }

    public int getInternalHGap() {
        return m_hgap;
    }

    public int getInternalVGap() {
        return m_vgap;
    }

    public int getExternalHGap() {
        return m_externalHGap;
    }

    public int getExternalVGap() {
        return m_externalVGap;
    }

    public void setInternalVGap(int gap) {
        m_vgap = gap;
    }

    public void setInternalHGap(int gap) {
        m_hgap = gap;
    }

    public void setExternalVGap(int gap) {
        m_externalVGap = gap;
    }

    public void setExternalHGap(int gap) {
        m_externalHGap = gap;
    }

    private void init() {
        debug("FormLayout.init()", 3);
        m_ColumnLeader = new ColumnLeader(this);
        m_Columns.add(m_ColumnLeader);
        m_RowLeader = new RowLeader(this);
    }

    public void layoutContainer(Container parent) {
        debug("FormLayout.layoutContainer(Container)", 8);
        m_container = parent;
        long currentTime = System.currentTimeMillis();
        m_RowLeader.layoutRows(parent.getSize().height);
        debugTimer("FormLayout.layoutContainter(Container) -- Rows", currentTime);
        currentTime = System.currentTimeMillis();
        m_ColumnLeader.layoutColumns(parent.getSize().width);
        debugTimer("FormLayout.layoutContainter(Container) -- Columns", currentTime);
    }

    public Dimension minimumLayoutSize(Container parent) {
        debug("FormLayout.minimumLayoutSize(Container)", 6);
        m_container = parent;
        Dimension minimumSize = new Dimension(m_ColumnLeader.getMinimumLocation(), m_RowLeader.getMinimumLocation());
        debug("FormLayout.minimumLayoutSize(Container): returning " + minimumSize, 6);
        return minimumSize;
    }

    public Dimension preferredLayoutSize(Container parent) {
        debug("FormLayout.preferredLayoutSize(Container)", 6);
        m_container = parent;
        Dimension preferredSize = new Dimension(m_ColumnLeader.getPreferredLocation(), m_RowLeader.getPreferredLocation());
        debug("FormLayout.preferredLayoutSize(Container): returning " + preferredSize, 6);
        return preferredSize;
    }

    public void removeLayoutComponent(Component component) {
        debug("FormLayout.removeLayoutComponent(Component)", 3);
        m_RowLeader.removeLayoutComponent(component);
        m_ColumnLeader.removeLayoutComponent(component);
    }

    private void debugLabel(Component label) {
        try {
            debug("FormLayout.add: Label is '" + ((JLabel) label).getText() + "'", 3);
        } catch (ClassCastException e) {
        }
    }

    /**
    * Add a <code>component</code> that will align with the labels in <code>column</code>.
    */
    public void add(Component component, int row, int column) {
        debug("FormLayout.add(Component, int, int): row " + row + ", column " + column, 3);
        m_RowLeader.add(component, row);
        m_ColumnLeader.add(component, row, column, m_pct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>.
    */
    public void add(Component label, Component field, int row, int column) {
        debug("FormLayout.add(Component, Component, int, int): row " + row + ", column " + column, 3);
        debugLabel(label);
        m_RowLeader.add(label, field, row);
        m_ColumnLeader.add(label, field, row, column, m_pct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>,
    * subject to the specified <code>mode</code>.
    */
    public void add(Component label, Component field, int row, int column, int mode) {
        debug("FormLayout.add(Component, Component, int, int, int): column " + column + ", row " + row + ", mode " + mode, 3);
        if ((mode < FormLayout.DEFAULT) || (mode > FormLayout.LABEL_ON_TOP)) {
            debug("FormLayout.add(Component, Component, int, int, int): Invalid mode: " + mode + "!  Adding components in default mode.", 0);
            add(label, field, row, column);
        }
        if ((column == 0) && (mode == FormLayout.FREE_LABEL)) {
            debug("FormLayout.add(Component, Component, int, int, int): Invalid mode: FREE_LABEL cannot be used on column #0!  Adding components in default mode.", 0);
            add(label, field, row, column);
        }
        if (mode == FormLayout.LABEL_ON_TOP) {
            if (row >= (Integer.MAX_VALUE - 1)) {
                debug("FormLayout.add(Component, Component, int, int, int): Invalid row for mode LABEL_ON_TOP: must be less than (Integer.MAX_VALUE - 1): " + (Integer.MAX_VALUE - 1) + ".  Adding components in default mode.", 0);
                add(label, field, row, column);
            }
            try {
                ((JComponent) label).setAlignmentY(Component.BOTTOM_ALIGNMENT);
            } catch (ClassCastException e) {
            }
            m_RowLeader.add(label, row);
            m_ColumnLeader.add(label, row, column, m_pct);
            m_RowLeader.add(field, row + 1);
            m_ColumnLeader.add(field, row + 1, column, m_pct);
            return;
        }
        debugLabel(label);
        m_RowLeader.add(label, field, row);
        m_ColumnLeader.add(label, field, row, column, mode, m_pct);
    }

    /**
    * Add a <code>component</code> that will align with the labels in <code>column</code>,
    * and span from <code>startRow</code> to <code>endRow</code>.
    */
    public void addMultiRow(Component component, int startRow, int endRow, int column) {
        debug("FormLayout.addMultiRow(Component, int, int, int): startRow " + startRow + ", endRow " + endRow + ", column " + column, 3);
        m_RowLeader.addMultiRow(component, startRow, endRow);
        m_ColumnLeader.addMultiRow(component, startRow, endRow, column, m_pct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>.
    * and span from <code>startRow</code> to <code>endRow</code>.
    */
    public void addMultiRow(Component label, Component field, int startRow, int endRow, int column) {
        debug("FormLayout.addMultiRow(Component, Component, int, int, int): startRow " + startRow + ", endRow " + endRow + ", column " + column, 3);
        debugLabel(label);
        m_RowLeader.addMultiRow(label, field, startRow, endRow);
        m_ColumnLeader.addMultiRow(label, field, startRow, endRow, column, m_pct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>,
    * subject to the specified <code>mode</code>, and span from <code>startRow</code> to <code>endRow</code>.
    */
    public void addMultiRow(Component label, Component field, int startRow, int endRow, int column, int mode) {
        debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): column " + column + ", startRow " + startRow + ", endRow " + endRow + ", mode " + mode, 3);
        if (startRow > endRow) {
            endRow = startRow;
        }
        if ((mode < FormLayout.DEFAULT) || (mode > FormLayout.LABEL_ON_TOP)) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): Invalid mode: " + mode + "!  Adding components in default mode.", 0);
            addMultiRow(label, field, startRow, endRow, column);
        }
        if ((mode == FormLayout.FREE_LABEL) || (mode == FormLayout.FREE_FIELD)) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): mode " + mode + " not supported for multi-row components.  Layout may behave strangely", 0);
        }
        if (mode == FormLayout.LABEL_ON_TOP) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): adding in mode FormLayout.LABEL_ON_TOP", 3);
            if (endRow >= (Integer.MAX_VALUE - 1)) {
                debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): Invalid endRow for mode LABEL_ON_TOP: must be less than (Integer.MAX_VALUE - 1): " + (Integer.MAX_VALUE - 1) + ".  Adding components in default mode.", 0);
                add(label, field, startRow, endRow, column);
            }
            if (startRow >= endRow) {
                endRow = startRow + 1;
            }
            try {
                ((JComponent) label).setAlignmentY(Component.BOTTOM_ALIGNMENT);
            } catch (ClassCastException e) {
            }
            m_RowLeader.add(label, startRow);
            m_ColumnLeader.add(label, startRow, column, m_pct);
            m_RowLeader.addMultiRow(field, startRow + 1, endRow);
            m_ColumnLeader.addMultiRow(field, startRow + 1, endRow, column, m_pct);
            return;
        }
        debugLabel(label);
        m_RowLeader.addMultiRow(label, field, startRow, endRow);
        m_ColumnLeader.addMultiRow(label, field, startRow, endRow, column, mode, m_pct);
    }

    /**
    * Add a <code>component</code> that will align with the labels in <code>column</code>,
    * and will stretch as far as <code>preferredSize.width * fillRightPct</code> to right justify.
    */
    public void add(Component component, int row, int column, double fillRightPct) {
        debug("FormLayout.add(Component, int, int, double): row " + row + ", column " + column, 3);
        m_RowLeader.add(component, row);
        m_ColumnLeader.add(component, row, column, fillRightPct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>.
    * and will stretch as far as <code>preferredSize.width * fillRightPct</code> to right justify.
    */
    public void add(Component label, Component field, int row, int column, double fillRightPct) {
        debug("FormLayout.add(Component, Component, int, int, double): row " + row + ", column " + column, 3);
        debugLabel(label);
        m_RowLeader.add(label, field, row);
        m_ColumnLeader.add(label, field, row, column, fillRightPct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>,
    * subject to the specified <code>mode</code>, and will stretch as far as <code>preferredSize.width * fillRightPct</code> to right justify.
    */
    public void add(Component label, Component field, int row, int column, int mode, double fillRightPct) {
        debug("FormLayout.add(Component, Component, int, int, int, double): column " + column + ", row " + row + ", mode " + mode, 3);
        if ((mode < FormLayout.DEFAULT) || (mode > FormLayout.LABEL_ON_TOP)) {
            debug("FormLayout.add(Component, Component, int, int, int): Invalid mode: " + mode + "!  Adding components in default mode.", 0);
            add(label, field, row, column, fillRightPct);
        }
        if ((column == 0) && (mode == FormLayout.FREE_LABEL)) {
            debug("FormLayout.add(Component, Component, int, int, int): Invalid mode: FREE_LABEL cannot be used on column #0!  Adding components in default mode.", 0);
            add(label, field, row, column, fillRightPct);
        }
        if (mode == FormLayout.LABEL_ON_TOP) {
            if (row >= (Integer.MAX_VALUE - 1)) {
                debug("FormLayout.add(Component, Component, int, int, int): Invalid row for mode LABEL_ON_TOP: must be less than (Integer.MAX_VALUE - 1): " + (Integer.MAX_VALUE - 1) + ".  Adding components in default mode.", 0);
                add(label, field, row, column, fillRightPct);
            }
            try {
                ((JComponent) label).setAlignmentY(Component.BOTTOM_ALIGNMENT);
            } catch (ClassCastException e) {
            }
            m_RowLeader.add(label, row);
            m_ColumnLeader.add(label, row, column, fillRightPct);
            m_RowLeader.add(field, row + 1);
            m_ColumnLeader.add(field, row + 1, column, fillRightPct);
        }
        debugLabel(label);
        m_RowLeader.add(label, field, row);
        m_ColumnLeader.add(label, field, row, column, mode, fillRightPct);
    }

    /**
    * Add a <code>component</code> that will align with the labels in <code>column</code>,
    * will span from <code>startRow</code> to <code>endRow</code>, and will stretch as far
    * as <code>preferredSize.width * fillRightPct</code> to reach the right side.
    */
    public void addMultiRow(Component component, int startRow, int endRow, int column, double fillRightPct) {
        debug("FormLayout.addMultiRow(Component, int, int, int, double): startRow " + startRow + ", endRow " + endRow + ", column " + column, 3);
        m_RowLeader.addMultiRow(component, startRow, endRow);
        m_ColumnLeader.addMultiRow(component, startRow, endRow, column, fillRightPct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>.
    * will span from <code>startRow</code> to <code>endRow</code>, and will stretch as far
    * as <code>preferredSize.width * fillRightPct</code> to reach the right side.
    */
    public void addMultiRow(Component label, Component field, int startRow, int endRow, int column, double fillRightPct) {
        debug("FormLayout.addMultiRow(Component, Component, int, int, int, double): startRow " + startRow + ", endRow " + endRow + ", column " + column, 3);
        debugLabel(label);
        m_RowLeader.addMultiRow(label, field, startRow, endRow);
        m_ColumnLeader.addMultiRow(label, field, startRow, endRow, column, fillRightPct);
    }

    /**
    * Add <code>label</code> and <code>field</code> with alignment respective to the other labels and fields in <code>column</code>,
    * subject to the specified <code>mode</code>, will span from <code>startRow</code> to <code>endRow</code>, and will stretch as far
    * as <code>preferredSize.width * fillRightPct</code> to reach the right side.
    */
    public void addMultiRow(Component label, Component field, int startRow, int endRow, int column, int mode, double fillRightPct) {
        debug("FormLayout.addMultiRow(Component, Component, int, int, int, int, double): column " + column + ", startRow " + startRow + ", endRow " + endRow + ", mode " + mode, 3);
        if (startRow > endRow) {
            endRow = startRow;
        }
        if ((mode < FormLayout.DEFAULT) || (mode > FormLayout.LABEL_ON_TOP)) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): Invalid mode: " + mode + "!  Adding components in default mode.", 0);
            addMultiRow(label, field, startRow, endRow, column, fillRightPct);
        }
        if ((mode == FormLayout.FREE_LABEL) || (mode == FormLayout.FREE_FIELD)) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): mode " + mode + " not supported for multi-row components.  Layout may behave strangely", 0);
        }
        if (mode == FormLayout.LABEL_ON_TOP) {
            debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): adding in mode FormLayout.LABEL_ON_TOP", 3);
            if (endRow >= (Integer.MAX_VALUE - 1)) {
                debug("FormLayout.addMultiRow(Component, Component, int, int, int, int): Invalid endRow for mode LABEL_ON_TOP: must be less than (Integer.MAX_VALUE - 1): " + (Integer.MAX_VALUE - 1) + ".  Adding components in default mode.", 0);
                add(label, field, startRow, endRow, column, fillRightPct);
            }
            if (startRow >= endRow) {
                endRow = startRow + 1;
            }
            try {
                ((JComponent) label).setAlignmentY(Component.BOTTOM_ALIGNMENT);
            } catch (ClassCastException e) {
            }
            m_RowLeader.add(label, startRow);
            m_ColumnLeader.add(label, startRow, column, fillRightPct);
            m_RowLeader.addMultiRow(field, startRow + 1, endRow);
            m_ColumnLeader.addMultiRow(field, startRow + 1, endRow, column, fillRightPct);
            return;
        }
        debugLabel(label);
        m_RowLeader.addMultiRow(label, field, startRow, endRow);
        m_ColumnLeader.addMultiRow(label, field, startRow, endRow, column, mode, fillRightPct);
    }

    /**
    * All subsequent <code>add()</code> calls that do not specify <code>fillRightPct</code> will use this <code>fillRightPct</code>.
    */
    public void setDefaultFillRightPct(double fillRightPct) {
        debug("FormLayout.setDefaultFillRightPct(double): pct " + fillRightPct, 3);
        m_pct = fillRightPct;
    }

    public double getDefaultFillRightPct() {
        return m_pct;
    }

    ColumnLayout getColumn(int column) {
        debug("FormLayout.getColumn(int): column " + column, 3);
        Enumeration e = m_Columns.elements();
        ColumnLayout nextColumn = null;
        int index = 0;
        while (e.hasMoreElements()) {
            nextColumn = (ColumnLayout) e.nextElement();
            if (column == nextColumn.getIndex()) {
                return nextColumn;
            }
            if (column < nextColumn.getIndex()) {
                ColumnLayout newColumn = new ColumnLayout(column, this);
                m_Columns.insertElementAt(newColumn, index);
                return newColumn;
            }
            index++;
        }
        debug("FormLayout.getColumn(int): Error!  Attempt to add column with index > Integer.MAX_VALUE!!", 0);
        return null;
    }

    /**
    * Terminated ancestor method.
    */
    public void addLayoutComponent(String name, Component comp) {
        debug("FormLayout.addLayoutComponent(String, Component): Warning!  Use of unsupported method!", 0);
    }

    class RowLeader extends RowLayout {

        public RowLeader(FormLayout containingLayout) {
            super(Integer.MAX_VALUE, containingLayout);
            this.debug("RowLeader.RowLeader(FormLayout)", 3);
        }

        public void add(Component label, Component field, int row) {
            this.debug("RowLeader.add(Component, Component, int): row " + row, 3);
            RowLayout rowLayout = m_RowLeader.getRow(row);
            rowLayout.add(label);
            rowLayout.add(field);
        }

        public void add(Component component, int row) {
            this.debug("RowLeader.add(Component, int): row " + row, 3);
            RowLayout rowLayout = m_RowLeader.getRow(row);
            rowLayout.add(component);
        }

        public void addMultiRow(Component component, int startRow, int endRow) {
            this.debug("RowLeader.addMultiRow(Component, int, int): startRow " + startRow + ", endRow " + endRow, 3);
            getRow(endRow).addFloater(component, startRow);
        }

        public void addMultiRow(Component label, Component field, int startRow, int endRow) {
            this.debug("RowLeader.addMultiRow(Component, Component, int, int): startRow " + startRow + ", endRow " + endRow, 3);
            getRow(startRow).add(label);
            getRow(endRow).addFloater(field, startRow);
        }

        protected void removeIfEmpty() {
        }

        public void layoutRows(int room) {
            this.debug("RowLeader.layoutRows(int): room " + room, 8);
            if (room < m_minLocation) {
                room = m_minLocation;
            }
            findPreferredLocation();
            findMinimumLocation();
            double pct = 1;
            if (m_prefLocation > m_minLocation) {
                pct = (double) (((double) (room - m_minLocation)) / ((double) (m_prefLocation - m_minLocation)));
            }
            if (pct > 1) {
                pct = 1;
            }
            setLocation(pct);
            doLayout();
        }

        public int getPreferredLocation() {
            this.debug("RowLeader.getPreferredLocation()", 6);
            findPreferredLocation();
            return m_prefLocation;
        }

        public int getMinimumLocation() {
            this.debug("RowLeader.getMinimumLocation()", 6);
            findMinimumLocation();
            return m_minLocation;
        }

        protected void setMinimumLocation(int minLoc) {
            this.debug("RowLeader.setMinimumLocation(int)", 6);
            m_minLocation = minLoc + getRightInset();
        }

        protected void setPreferredLocation(int prefLoc) {
            this.debug("RowLeader.setPreferredLocation(int)", 6);
            m_prefLocation = prefLoc + m_externalVGap;
        }

        protected int getUpperBoundary() {
            this.debug("RowLeader.getUpperBoundary(): returning " + m_currLocation, 8);
            return m_currLocation;
        }

        protected int getGap() {
            return m_externalVGap;
        }
    }

    class ColumnLeader extends ColumnLayout {

        private Vector m_invisibleGhosts = new Vector();

        public ColumnLeader(FormLayout containingLayout) {
            super(Integer.MAX_VALUE, containingLayout);
            debug("ColumnLeader.ColumnLeader(FormLayout)", 3);
        }

        public void add(Component component, int row, int column, double pct) {
            debug("ColumnLeader.add(Component, int, int, double)", 3);
            SegmentLayout segmentLayout = getSegment(getColumn(column), row);
            segmentLayout.add(component, FormLayout.FREE_FIELD, pct);
        }

        public void add(Component label, Component field, int row, int column, double pct) {
            debug("ColumnLeader.add(Component, Component, int, int, double)", 3);
            SegmentLayout segmentLayout = getSegment(getColumn(column), row);
            segmentLayout.add(label, pct);
            segmentLayout.add(field, pct);
        }

        public void add(Component label, Component field, int row, int column, int mode, double pct) {
            debug("ColumnLeader.add(Component, Component, int, int, int, double)", 3);
            SegmentLayout segmentLayout = getSegment(getColumn(column), row);
            segmentLayout.add(label, mode, pct);
            segmentLayout.add(field, pct);
        }

        public void addMultiRow(Component component, int startRow, int endRow, int column, double pct) {
            debug("ColumnLeader.addMultiRow(Component, int, int, int, double)", 3);
            add(component, startRow, column, pct);
            addMultiRows(component, startRow, endRow, column, FormLayout.FREE_FIELD);
        }

        public void addMultiRow(Component label, Component field, int startRow, int endRow, int column, double pct) {
            debug("ColumnLeader.addMultiRow(Component, Component, int, int, int, double)", 3);
            add(label, field, startRow, column, pct);
            addMultiRows(field, startRow, endRow, column, FormLayout.DEFAULT);
        }

        public void addMultiRow(Component label, Component field, int startRow, int endRow, int column, int mode, double pct) {
            debug("ColumnLeader.addMultiRow(Component, Component, int, int, int, int, double)", 3);
            add(label, field, startRow, column, mode, pct);
            addMultiRows(field, startRow, endRow, column, FormLayout.DEFAULT);
        }

        private void addMultiRows(Component field, int startRow, int endRow, int column, int mode) {
            debug("ColumnLeader.addMultiRows(Component, int, int, int, int) startRow " + startRow + ", endRow " + endRow + ", column " + column, 3);
            ColumnLayout columnLayout = getColumn(column);
            getSegment(columnLayout, startRow).setLastGhost(endRow);
            Enumeration e = m_segments.elements();
            SegmentLayout nextLeaderSegment = null;
            SegmentLayout nextSegment = null;
            while (e.hasMoreElements()) {
                nextLeaderSegment = (SegmentLayout) e.nextElement();
                debug("ColumnLeader.addMultiRows(Component, int, int, int, int): nextLeaderSegment " + nextLeaderSegment.getIndex(), 3);
                if ((nextLeaderSegment.getIndex() > startRow) && (nextLeaderSegment.getIndex() <= endRow)) {
                    nextSegment = nextLeaderSegment.getSegment(columnLayout);
                    nextSegment.addGhost(new JLabel(""));
                    nextSegment.add(field, mode, 0);
                }
            }
            m_invisibleGhosts.add(new InvisibleGhost(startRow, endRow, mode, field, columnLayout));
        }

        public void removeLayoutComponent(Component component) {
            debug("ColumnLeader.removeLayoutComponent(Component)", 3);
            Enumeration e = m_segments.elements();
            while (e.hasMoreElements()) {
                ((SegmentLayout) e.nextElement()).removeLayoutComponent(component);
            }
        }

        public SegmentLayout getSegment(ColumnLayout columnLayout, int row) {
            debug("ColumnLeader.getSegment(ColumnLayout, int): column of columnLayout " + columnLayout.getIndex() + ", row " + row, 3);
            SegmentLayout segmentLayout = findSegment(row);
            if (segmentLayout == null) {
                segmentLayout = new SegmentLayout(row);
                m_segments.add(segmentLayout);
                Enumeration e = m_invisibleGhosts.elements();
                while (e.hasMoreElements()) {
                    ((InvisibleGhost) e.nextElement()).makeVisible(segmentLayout);
                }
            }
            return segmentLayout.getSegment(columnLayout);
        }

        private SegmentLayout findSegment(int row) {
            debug("ColumnLeader.findSegment(int): row " + row + ", index " + this.getIndex(), 3);
            Enumeration e = m_segments.elements();
            SegmentLayout nextSegment = null;
            while (e.hasMoreElements()) {
                nextSegment = (SegmentLayout) e.nextElement();
                if (nextSegment.getIndex() == row) {
                    return nextSegment;
                }
            }
            return null;
        }

        public void layoutColumns(int room) {
            debug("ColumnLeader.layoutColumns(int): room " + room, 8);
            if (room < m_minLocation) {
                room = m_minLocation;
            } else if (room > m_prefLocation) {
                room = m_prefLocation;
            }
            room -= m_externalHGap;
            recalcMinimumLocations();
            recalcPreferredLocations();
            double pct = 1;
            if ((m_prefLocation - m_minLocation) > 0) {
                pct = (double) ((double) (room - (m_minLocation - m_externalHGap)) / (double) ((m_prefLocation - m_externalHGap) - (m_minLocation - m_externalHGap)));
            }
            debug("ColumnLeader.layoutColumns(int): ******* laying out Columns with min " + m_minLocation + ", pref " + m_prefLocation + ", room " + room + ", pct " + pct, 10);
            Enumeration e = m_Columns.elements();
            while (e.hasMoreElements()) {
                ColumnLayout nextColumn = (ColumnLayout) e.nextElement();
                nextColumn.findIntermediateLocation(pct);
            }
            m_currLocation = room;
            e = m_Columns.elements();
            while (e.hasMoreElements()) {
                ColumnLayout nextColumn = (ColumnLayout) e.nextElement();
                nextColumn.doLayout();
            }
        }

        public int getPreferredLocation() {
            debug("ColumnLeader.getPreferredLocation()", 6);
            recalcPreferredLocations();
            return m_prefLocation;
        }

        public int getMinimumLocation() {
            debug("ColumnLeader.getMinimumLocation()", 6);
            recalcMinimumLocations();
            return m_minLocation;
        }

        protected void setMinimumLocation(int minLoc) {
            debug("ColumnLeader.setMinimumLocation(int): " + minLoc, 6);
            m_minLocation = minLoc + m_externalHGap;
        }

        protected void setPreferredLocation(int prefLoc) {
            debug("ColumnLeader.setPreferredLocation(int): " + (prefLoc + m_externalHGap), 6);
            m_prefLocation = prefLoc + m_externalHGap;
        }

        void recalcMinimumLocations() {
            debug("ColumnLeader.recalcMinimumLocations()", 6);
            Enumeration e = m_Columns.elements();
            ColumnLayout nextColumn = null;
            while (e.hasMoreElements()) {
                nextColumn = (ColumnLayout) e.nextElement();
                nextColumn.findMinimumLocation();
            }
        }

        void recalcPreferredLocations() {
            debug("ColumnLeader.recalcPreferredLocations()", 6);
            Enumeration e = m_Columns.elements();
            ColumnLayout nextColumn = null;
            while (e.hasMoreElements()) {
                nextColumn = (ColumnLayout) e.nextElement();
                nextColumn.findPreferredLocation();
            }
        }

        class InvisibleGhost {

            private int m_startRow;

            private int m_endRow;

            private int m_mode;

            private Component m_field;

            private ColumnLayout m_columnLayout;

            public InvisibleGhost(int startRow, int endRow, int mode, Component field, ColumnLayout columnLayout) {
                debug("InvisibleGhost.InvisibleGhost(int, int, int, Component, ColumnLayout): columnLayout.getIndex() " + columnLayout.getIndex() + ", startRow " + startRow + ", endRow " + endRow, 3);
                m_startRow = startRow;
                m_endRow = endRow;
                m_mode = mode;
                m_field = field;
                m_columnLayout = columnLayout;
            }

            public void makeVisible(SegmentLayout segmentLayout) {
                debug("InvisibleGhost.makeVisible(SegmentLayout): segmentLayout index " + segmentLayout.getIndex() + ", m_columnLayout.getIndex() " + m_columnLayout.getIndex() + ", startRow " + m_startRow + ", endRow " + m_endRow, 3);
                if ((segmentLayout.getIndex() >= m_startRow) && (segmentLayout.getIndex() <= m_endRow)) {
                    debug("InvisibleGhost.makeVisible(SegmentLayout): Ja!", 3);
                    SegmentLayout ghostSegment = segmentLayout.getSegment(m_columnLayout);
                    ghostSegment.addGhost(new JLabel(""));
                    ghostSegment.add(m_field, m_mode, 0);
                }
            }
        }

        private void debug(String message, int level) {
            if (level <= FormLayout.DEBUG_LEVEL) {
                for (int index = 0; index < level; index++) {
                    message = "-" + message;
                }
                System.out.println(message);
            }
        }
    }

    private void debug(String message, int level) {
        if (level <= DEBUG_LEVEL) {
            for (int index = 0; index < level; index++) {
                message = "-" + message;
            }
            System.out.println(message);
        }
    }

    private void debugTimer(String method, long startTime) {
        debug(method + " total time: " + (System.currentTimeMillis() - startTime), 1);
    }
}
