package com.volantis.mcs.unit.layouts;

import com.volantis.mcs.layouts.AbstractGrid;
import com.volantis.mcs.layouts.AbstractGridMock;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Column;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.ColumnIteratorPaneMock;
import com.volantis.mcs.layouts.ColumnMock;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.DissectingPaneMock;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.FormFragmentMock;
import com.volantis.mcs.layouts.FormMock;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.FormatMock;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.FormatTypeMock;
import com.volantis.mcs.layouts.FormatVisitor;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.FragmentMock;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.GridMock;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.MontageLayout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.PaneMock;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.RegionMock;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.ReplicaMock;
import com.volantis.mcs.layouts.Row;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.layouts.RowIteratorPaneMock;
import com.volantis.mcs.layouts.RowMock;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.layouts.SegmentGridMock;
import com.volantis.mcs.layouts.SegmentMock;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.SpatialFormatIteratorMock;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIteratorMock;
import com.volantis.mcs.unit.layouts.visitor.VisitFormatMethodAction;
import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.MockFactory;

/**
 * Provides static methods to help construct mock layouts and formats.
 */
public class LayoutTestHelper {

    /**
     * The {@link MockFactory}.
     */
    private static final MockFactory mockFactory = MockFactory.getDefaultInstance();

    /**
     * An empty array of attributes, used to simplify initialisation of mock
     * objects.
     */
    private static final String[] EMPTY_ATTRIBUTES = new String[0];

    /**
     * Create a mock {@link Format}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param layout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link FormatMock}.
     */
    public static FormatMock createFormatMock(String identifier, ExpectationBuilder expectations, Layout layout) {
        String FORMAT_TYPE_NAME = "MOCK FORMAT";
        final FormatTypeMock mockFormatType = new FormatTypeMock("formatTypeMock", expectations, FORMAT_TYPE_NAME, FormatNamespace.SEGMENT, FormatTypeMock.class, "DUMMY", FormatType.Structure.SIMPLE_CONTAINER);
        mockFormatType.expects.getTypeName().returns(FORMAT_TYPE_NAME).any();
        FormatMock.MockProxy proxy = FormatMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(layout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final FormatMock formatMock = new FormatMock(identifier, expectations, 0, layout);
        expectsGetTypeInfo(formatMock.expects, mockFormatType);
        return formatMock;
    }

    /**
     * Create a mock {@link Pane}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link PaneMock}.
     */
    public static PaneMock createPaneMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        PaneMock.MockProxy proxy = PaneMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final PaneMock formatMock = new PaneMock(identifier, expectations, deviceLayout);
        PaneMock.Expects formatExpects = formatMock.expects;
        expectsGetTypeInfo(formatExpects, FormatType.PANE);
        allowVisiting(formatMock.fuzzy, Pane.class);
        formatMock.expects.getName().returns(identifier).any();
        return formatMock;
    }

    private static void expectsGetTypeInfo(FormatMock.Expects formatExpects, final FormatType formatType) {
        formatExpects.getFormatType().returns(formatType).any();
        formatExpects.getTypeName().returns(formatType.getTypeName()).any();
    }

    /**
     * Create a mock {@link DissectingPane}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link DissectingPaneMock}.
     */
    public static DissectingPaneMock createDissectingPaneMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        DissectingPaneMock.MockProxy proxy = DissectingPaneMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final DissectingPaneMock formatMock = new DissectingPaneMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.DISSECTING_PANE).any();
        allowVisiting(formatMock.fuzzy, DissectingPane.class);
        return formatMock;
    }

    /**
     * Create a mock {@link Region}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link RegionMock}.
     */
    public static RegionMock createRegionMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        RegionMock.MockProxy proxy = RegionMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final RegionMock formatMock = new RegionMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.REGION);
        allowVisiting(formatMock.fuzzy, Region.class);
        return formatMock;
    }

    /**
     * Create a mock {@link ColumnIteratorPane}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link ColumnIteratorPaneMock}.
     */
    public static ColumnIteratorPaneMock createColumnIteratorPaneMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        ColumnIteratorPaneMock.MockProxy proxy = ColumnIteratorPaneMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final ColumnIteratorPaneMock formatMock = new ColumnIteratorPaneMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.COLUMN_ITERATOR_PANE).any();
        allowVisiting(formatMock.fuzzy, ColumnIteratorPane.class);
        return formatMock;
    }

    /**
     * Create a mock {@link RowIteratorPane}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link RowIteratorPaneMock}.
     */
    public static RowIteratorPaneMock createRowIteratorPaneMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        RowIteratorPaneMock.MockProxy proxy = RowIteratorPaneMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final RowIteratorPaneMock formatMock = new RowIteratorPaneMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.ROW_ITERATOR_PANE).any();
        allowVisiting(formatMock.fuzzy, RowIteratorPane.class);
        return formatMock;
    }

    /**
     * Create a mock {@link Grid}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @param gridStyleClass
     * @return A newly instantiated {@link GridMock}.
     */
    public static GridMock createGridMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout, String gridStyleClass) {
        GridMock.MockProxy proxy = GridMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        proxy.expects.getStyleClass().returns(gridStyleClass).any();
        final GridMock formatMock = new GridMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.GRID);
        allowVisiting(formatMock.fuzzy, Grid.class);
        return formatMock;
    }

    /**
     * Create a mock {@link Grid}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     * @param rows The number of rows in the grid.
     * @param columns The number of columns in the grid.
     *
     * @param gridStyleClass
     * @return A newly instantiated {@link GridMock}.
     */
    public static GridMock createGridMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout, int rows, int columns, String gridStyleClass) {
        GridMock gridMock = createGridMock(identifier, expectations, deviceLayout, gridStyleClass);
        final GridMock.Expects gridMockExpects = gridMock.expects;
        addGridChildren(gridMockExpects, expectations, deviceLayout, rows, columns);
        return gridMock;
    }

    private static void addGridChildren(final AbstractGridMock.Expects gridMockExpects, ExpectationBuilder expectations, Layout layout, int rows, int columns) {
        final FormatMock[][] children = new FormatMock[rows][columns];
        final FormatMock[] childList = new FormatMock[rows * columns];
        final FormatMock.Expects[] childExpectsList = new FormatMock.Expects[rows * columns];
        for (int r = 0, i = 0; r < rows; r += 1) {
            for (int c = 0; c < columns; c += 1, i += 1) {
                final FormatMock formatMock = createFormatMock("[row " + r + ", column " + c + "]", expectations, layout);
                children[r][c] = formatMock;
                childList[i] = formatMock;
                childExpectsList[i] = formatMock.expects;
                gridMockExpects.getChildAt(r, c).returns(formatMock).any();
            }
        }
        LayoutTestHelper.addChildren(gridMockExpects, childExpectsList);
        gridMockExpects.getRows().returns(rows).any();
        gridMockExpects.getColumns().returns(columns).any();
        final RowMock[] rowMocks = new RowMock[rows];
        for (int i = 0; i < rowMocks.length; i++) {
            RowMock mock = LayoutTestHelper.createRowMock("rowMock " + i, expectations, (AbstractGrid) gridMockExpects._getMock());
            rowMocks[i] = mock;
            gridMockExpects.getRow(i).returns(mock).any();
        }
        final ColumnMock[] columnMocks = new ColumnMock[columns];
        for (int i = 0; i < columnMocks.length; i++) {
            ColumnMock mock = LayoutTestHelper.createColumnMock("columnMock " + i, expectations, (AbstractGrid) gridMockExpects._getMock());
            columnMocks[i] = mock;
            gridMockExpects.getColumn(i).returns(mock).any();
        }
    }

    /**
     * Create a mock {@link Fragment}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link FragmentMock}.
     */
    public static FragmentMock createFragmentMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        FragmentMock.MockProxy proxy = FragmentMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(1);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final FragmentMock formatMock = new FragmentMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.FRAGMENT);
        formatMock.expects.getName().returns(identifier).any();
        allowVisiting(formatMock.fuzzy, Fragment.class);
        return formatMock;
    }

    /**
     * Add the array of formats as children of the parentExpects format.
     *
     * @param parentExpects   The parentExpects format.
     * @param children The array of children.
     */
    public static void addChildren(FormatMock.Expects parentExpects, FormatMock.Expects[] children) {
        parentExpects.getNumChildren().returns(children.length).any();
        for (int i = 0; i < children.length; i++) {
            FormatMock.Expects child = children[i];
            Format parent = (Format) parentExpects._getMock();
            if (parent instanceof Fragment) {
                Fragment enclosingFragment = (Fragment) parent;
                child.getEnclosingFragment().returns(enclosingFragment).any();
            }
            child.getParent().returns(parent).any();
            parentExpects.getChildAt(i).returns((Format) child._getMock()).any();
        }
    }

    /**
     * Create a mock {@link Row}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param grid The grid with which the row is associated.
     *
     * @return A newly instantiated {@link RowMock}.
     */
    public static RowMock createRowMock(String identifier, ExpectationBuilder expectations, AbstractGrid grid) {
        RowMock.MockProxy proxy = RowMock.MockProxy.get(identifier);
        proxy.expects.setAttribute(FormatConstants.HEIGHT_UNITS_ATTRIBUTE, FormatConstants.HEIGHT_UNITS_VALUE_PIXELS);
        return new RowMock(identifier, expectations, grid);
    }

    /**
     * Create a mock {@link Column}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param grid The grid with which the column is associated.
     *
     * @return A newly instantiated {@link ColumnMock}.
     */
    public static ColumnMock createColumnMock(String identifier, ExpectationBuilder expectations, AbstractGrid grid) {
        ColumnMock.MockProxy proxy = ColumnMock.MockProxy.get(identifier);
        proxy.expects.setAttribute(FormatConstants.WIDTH_UNITS_ATTRIBUTE, FormatConstants.WIDTH_UNITS_VALUE_PERCENT);
        return new ColumnMock(identifier, expectations, grid);
    }

    /**
     * Create a mock {@link Replica}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link ReplicaMock}.
     */
    public static ReplicaMock createReplicaMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        ReplicaMock.MockProxy proxy = ReplicaMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final ReplicaMock formatMock = new ReplicaMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.REPLICA);
        return formatMock;
    }

    /**
     * Create a mock {@link Segment}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link SegmentMock}.
     */
    public static SegmentMock createSegmentMock(String identifier, ExpectationBuilder expectations, MontageLayout deviceLayout) {
        SegmentMock.MockProxy proxy = SegmentMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final SegmentMock formatMock = new SegmentMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.SEGMENT);
        allowVisiting(formatMock.fuzzy, Segment.class);
        return formatMock;
    }

    /**
     * Create a mock {@link SegmentGrid}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link SegmentGridMock}.
     */
    public static SegmentGridMock createSegmentGridMock(String identifier, ExpectationBuilder expectations, MontageLayout deviceLayout) {
        SegmentGridMock.MockProxy proxy = SegmentGridMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(0);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final SegmentGridMock formatMock = new SegmentGridMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.SEGMENT_GRID).any();
        allowVisiting(formatMock.fuzzy, SegmentGrid.class);
        return formatMock;
    }

    /**
     * Create a mock {@link SegmentGrid}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     * @param rows The number of rows in the grid.
     * @param columns The number of columns in the grid.
     *
     * @return A newly instantiated {@link SegmentGridMock}.
     */
    public static SegmentGridMock createSegmentGridMock(String identifier, ExpectationBuilder expectations, MontageLayout deviceLayout, int rows, int columns) {
        SegmentGridMock gridMock = createSegmentGridMock(identifier, expectations, deviceLayout);
        final AbstractGridMock.Expects gridMockExpects = gridMock.expects;
        addGridChildren(gridMockExpects, expectations, deviceLayout, rows, columns);
        return gridMock;
    }

    /**
     * Initialise the columns associated with the grid.
     *
     * @param grid The grid with which the columns are associated.
     * @param columnStyleClass The style class for the columns.
     */
    public static void initialiseColumns(final AbstractGrid grid, final String columnStyleClass) {
        for (int c = 0; c < grid.getColumns(); c++) {
            ColumnMock columnMock = (ColumnMock) grid.getColumn(c);
            columnMock.expects.getWidth().returns(String.valueOf((c + 1) * 100)).any();
            columnMock.expects.getWidthUnits().returns(FormatConstants.WIDTH_UNITS_VALUE_PERCENT).any();
            columnMock.expects.getStyleClass().returns(columnStyleClass).any();
        }
    }

    /**
     * Initialise the columns associated with the grid.
     *
     * @param grid The grid with which the columns are associated.
     * @param rowStyleClass The style class for the rows.
     */
    public static void initialiseRows(final AbstractGrid grid, final String rowStyleClass) {
        for (int r = 0; r < grid.getRows(); r++) {
            RowMock rowMock = (RowMock) grid.getRow(r);
            rowMock.expects.getHeight().returns(String.valueOf((r + 1) * 100)).any();
            rowMock.expects.getHeightUnits().returns(FormatConstants.HEIGHT_UNITS_VALUE_PIXELS).any();
            rowMock.expects.getStyleClass().returns(rowStyleClass).any();
        }
    }

    /**
     * Create a mock {@link Form}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link FormMock}.
     */
    public static FormMock createFormMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        FormMock.MockProxy proxy = FormMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(1);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final FormMock formatMock = new FormMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.FORM);
        allowVisiting(formatMock.fuzzy, Form.class);
        return formatMock;
    }

    /**
     * Create a mock {@link FormFragment}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link FormFragmentMock}.
     */
    public static FormFragmentMock createFormFragmentMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        FormFragmentMock.MockProxy proxy = FormFragmentMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(1);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final FormFragmentMock formatMock = new FormFragmentMock(identifier, expectations, deviceLayout);
        expectsGetTypeInfo(formatMock.expects, FormatType.FORM_FRAGMENT);
        allowVisiting(formatMock.fuzzy, FormFragment.class);
        return formatMock;
    }

    /**
     * Create a mock {@link TemporalFormatIterator}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link TemporalFormatIteratorMock}.
     */
    public static TemporalFormatIteratorMock createTemporalFormatIteratorMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        TemporalFormatIteratorMock.MockProxy proxy = TemporalFormatIteratorMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(1);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final TemporalFormatIteratorMock formatMock = new TemporalFormatIteratorMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.TEMPORAL_FORMAT_ITERATOR).any();
        allowVisiting(formatMock.fuzzy, TemporalFormatIterator.class);
        return formatMock;
    }

    /**
     * Create a mock {@link SpatialFormatIterator}.
     *
     * @param identifier The identifier for the mock object.
     * @param expectations The expectation container to use.
     * @param deviceLayout The device layout with which the format is
     * associated.
     *
     * @return A newly instantiated {@link SpatialFormatIteratorMock}.
     */
    public static SpatialFormatIteratorMock createSpatialFormatIteratorMock(String identifier, ExpectationBuilder expectations, CanvasLayout deviceLayout) {
        SpatialFormatIteratorMock.MockProxy proxy = SpatialFormatIteratorMock.MockProxy.get(identifier);
        proxy.expects.setNumChildren(1);
        proxy.expects.setDeviceLayout(deviceLayout);
        proxy.expects.getDefaultAttributes().returns(EMPTY_ATTRIBUTES);
        final SpatialFormatIteratorMock formatMock = new SpatialFormatIteratorMock(identifier, expectations, deviceLayout);
        formatMock.expects.getFormatType().returns(FormatType.SPATIAL_FORMAT_ITERATOR).any();
        allowVisiting(formatMock.fuzzy, SpatialFormatIterator.class);
        return formatMock;
    }

    /**
     * Allow the mock to be visited.
     *
     * @param fuzzy The mock format's fuzzy instance.
     * @param formatClass The class of thr format.
     */
    private static void allowVisiting(FormatMock.Fuzzy fuzzy, final Class formatClass) {
        fuzzy.visit(mockFactory.expectsInstanceOf(FormatVisitor.class), mockFactory.expectsAny()).does(new VisitFormatMethodAction(formatClass)).any();
    }
}
