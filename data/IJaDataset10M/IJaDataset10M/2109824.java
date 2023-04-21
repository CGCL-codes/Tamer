package net.sourceforge.pmd.ui.views;

import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * A SWT-Composite for showing a DataflowGraph
 * as well as other Information in Form of a Table
 * 
 * @author SebastianRaffel  ( 31.05.2005 )
 */
public class DataflowGraphTable extends Composite implements PaintListener {

    private ArrayList tableData;

    private int numRows;

    private int numCols;

    private Integer[] colWidths;

    private Integer rowHeight;

    private Color bgColor;

    private Color fgColor;

    private Color lineColor;

    protected Composite header;

    protected Composite bodyFrame;

    protected Composite bodyArea;

    protected Composite graphArea;

    protected Point tablePosition;

    protected Point tableSize;

    protected int graphColumn;

    protected static final Color DEFAULT_BG_COLOR = new Color(null, 255, 255, 255);

    protected static final Color DEFAULT_FG_COLOR = new Color(null, 0, 0, 0);

    protected static final Color DEFAULT_LINE_COLOR = new Color(null, 192, 192, 192);

    protected static final int DEFAULT_ROW_HEIGHT = 20;

    protected static final int DEFAULT_COL_WIDTH = 100;

    /**
	 * Constructor
	 * 
	 * @param parent, the parent Composite
	 * @param style, the SWT-Style
	 */
    public DataflowGraphTable(Composite parent, int style) {
        super(parent, style);
        numCols = 0;
        numRows = 0;
        graphColumn = 1;
        setLayoutData(new GridData(GridData.FILL_BOTH));
        header = buildTableHeader(this);
        Composite[] tableBody = buildTableBody(this);
        bodyFrame = tableBody[0];
        bodyArea = tableBody[1];
        bodyFrame.addPaintListener(this);
        bodyArea.addPaintListener(this);
        bodyFrame.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent event) {
                redraw();
            }
        });
        initScrollBars(bodyFrame);
        GridLayout mainLayout = new GridLayout(1, false);
        mainLayout.horizontalSpacing = mainLayout.verticalSpacing = mainLayout.marginHeight = mainLayout.marginWidth = 0;
        setLayout(mainLayout);
    }

    /**
	 * Builds the TableHeader (made of Buttons)
	 * 
	 * @param parent
	 * @return the Composite representing the Table's Header
	 */
    private Composite buildTableHeader(Composite parent) {
        Composite headerCanvas = new Composite(parent, SWT.NONE);
        headerCanvas.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        GridLayout layout = new GridLayout(1, false);
        layout.horizontalSpacing = layout.verticalSpacing = layout.marginHeight = layout.marginWidth = 0;
        headerCanvas.setLayout(layout);
        return headerCanvas;
    }

    /**
	 * Builds the Table's Body,
	 * creates two Areas, one that carries the real Table,
	 * another one that is the Table's "Background" or "Frame"
	 * and fills the empty Space when the Viewing Area is 
	 * larger than the Table
	 * 
	 * @param parent
	 * @return a Composite-Array with the 
	 * Table's Frame [0] and the Table's Body [1]
	 */
    private Composite[] buildTableBody(Composite parent) {
        Composite frameCanvas = new Composite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
        frameCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite bodyCanvas = new Composite(frameCanvas, SWT.NONE);
        GridData data = new GridData(GridData.FILL_BOTH);
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        bodyCanvas.setLayoutData(data);
        bodyCanvas.setSize(numCols * DEFAULT_COL_WIDTH, numRows * DEFAULT_ROW_HEIGHT);
        int spacing = 10;
        GridLayout bodyLayout = new GridLayout(numCols, false);
        bodyLayout.marginHeight = bodyLayout.marginWidth = spacing / 2;
        bodyLayout.horizontalSpacing = bodyLayout.verticalSpacing = spacing;
        bodyCanvas.setLayout(bodyLayout);
        GridLayout frameLayout = new GridLayout(1, false);
        frameLayout.horizontalSpacing = frameLayout.verticalSpacing = frameLayout.marginWidth = frameLayout.marginHeight = 0;
        frameCanvas.setLayout(frameLayout);
        tablePosition = new Point(bodyCanvas.getLocation().x, bodyCanvas.getLocation().y);
        tableSize = new Point(bodyCanvas.getSize().x, bodyCanvas.getSize().y);
        return new Composite[] { frameCanvas, bodyCanvas };
    }

    /**
	 * Inits the Columns, thereby gives the Header somthing to show
	 * (the Widths- and Titles-Field should be the same size, so that
	 * every Column has a real Width and Title)
	 * 
	 * @param widths, int-Field with the Widths of Columns
	 * @param titles, String-Field with the Titles
	 * @param graphPos, Number of Column, where to show the Graph
	 */
    public void setColumns(int[] widths, String[] titles, int graphPos) {
        numCols = widths.length;
        colWidths = new Integer[numCols];
        for (int i = 0; i < widths.length; i++) {
            colWidths[i] = new Integer(widths[i]);
        }
        if ((graphPos >= 0) && (graphPos <= numCols)) graphColumn = graphPos;
        String[] headerTitles = getHeaderTitles(titles);
        GridLayout headerLayout = (GridLayout) header.getLayout();
        headerLayout.numColumns = numCols + 1;
        GridData data;
        for (int i = 0; i <= numCols; i++) {
            Button button = new Button(header, SWT.NONE);
            data = new GridData();
            if (i < numCols) {
                button.setText(headerTitles[i]);
                data.widthHint = widths[i];
            } else if (i == numCols) {
                data.grabExcessHorizontalSpace = true;
                data.horizontalAlignment = GridData.FILL;
            }
            button.setLayoutData(data);
        }
        int newWidth = 0;
        for (int k = 0; k < widths.length; k++) {
            newWidth += colWidths[k].intValue();
        }
        ((GridLayout) bodyArea.getLayout()).numColumns = numCols;
        tableSize.x = newWidth;
        redraw();
    }

    /**
	 * Checks, if the Header's Titles are correct,
	 * if there are fewer Titles than Columns, it adds empty Strings
	 * 
	 * @param givenTitles
	 * @return the corrected String-Array of Titles
	 */
    private String[] getHeaderTitles(String[] givenTitles) {
        String[] headerTitles = new String[numCols];
        if (givenTitles == null) {
            for (int i = 0; i < numCols; i++) {
                headerTitles[i] = "";
            }
        } else if (givenTitles.length < numCols) {
            int remain = numCols - givenTitles.length;
            for (int j = 0; j < givenTitles.length; j++) {
                headerTitles[j] = givenTitles[j];
            }
            for (int k = 0; k < remain; k++) {
                headerTitles[givenTitles.length + k] = "";
            }
        } else {
            headerTitles = givenTitles;
        }
        return headerTitles;
    }

    /**
	 * Sets the Number of Rows and the Height of each Row of this Table
	 * 
	 * @param count
	 * @param height
	 */
    public void setRows(int count, int height) {
        numRows = count;
        rowHeight = new Integer(height);
        tableSize.y = numRows * height;
        redraw();
    }

    /**
	 * Set the Table's Foreground-, Background- and Line-Color
	 * 
	 * @param foreGround
	 * @param backGround
	 * @param line
	 */
    public void setColors(Color foreGround, Color backGround, Color line) {
        this.fgColor = foreGround;
        this.bgColor = backGround;
        this.lineColor = line;
        bodyArea.setBackground(bgColor);
        bodyArea.setForeground(fgColor);
        redraw();
    }

    /**
	 * Gives the Table real Data to show
	 * 
	 * @param data
	 */
    public void setTableData(ArrayList data) {
        tableData = data;
        buildTableData(bodyArea, data);
        redraw();
    }

    /**
	 * Returns the Composite-Area, the Graph should be built in
	 * 
	 * @return the DataflowGraph-Composite
	 */
    public Composite getGraphArea() {
        return graphArea;
    }

    /**
	 * Redraws this Table
	 */
    public void redraw() {
        Point bodySize = bodyFrame.getSize();
        Point parentSize = getParent().getSize();
        Point empty = new Point(0, 0);
        if (bodySize.equals(empty) && !parentSize.equals(empty)) {
            bodyFrame.setSize(parentSize);
        }
        syncScrollBars(bodyFrame);
        syncViewPosition(bodyFrame);
        syncHeader();
        super.redraw();
    }

    /**
	 * Inits the ScrollBars for the Table
	 * 
	 * @param parent
	 */
    private void initScrollBars(Composite parent) {
        ScrollBar horizontal = parent.getHorizontalBar();
        horizontal.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                scrollHorizontally((ScrollBar) event.widget);
            }
        });
        ScrollBar vertical = parent.getVerticalBar();
        vertical.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                scrollVertically((ScrollBar) event.widget);
            }
        });
    }

    /**
	 * Syncronizes the ScrollBars when the Source-View is resized
	 * 
	 * @param source
	 */
    private void syncScrollBars(Composite source) {
        int sourceWidth = source.getSize().x - source.getVerticalBar().getSize().x;
        int sourceHeight = source.getSize().y - source.getHorizontalBar().getSize().y;
        ScrollBar horizontalBar = bodyFrame.getHorizontalBar();
        ScrollBar verticalBar = bodyFrame.getVerticalBar();
        horizontalBar.setEnabled(true);
        verticalBar.setEnabled(true);
        if (sourceWidth >= tableSize.x) {
            horizontalBar.setEnabled(false);
        } else {
            horizontalBar.setMaximum(tableSize.x);
            horizontalBar.setIncrement(tableSize.x / 100);
            horizontalBar.setPageIncrement(tableSize.x);
            horizontalBar.setThumb(sourceWidth);
        }
        if (sourceHeight >= tableSize.y) {
            verticalBar.setEnabled(false);
        } else {
            verticalBar.setMaximum(tableSize.y);
            verticalBar.setIncrement(tableSize.y / 100);
            verticalBar.setPageIncrement(tableSize.y);
            verticalBar.setThumb(sourceHeight);
        }
    }

    /**
	 * Syncronizes the Frame's and Source's (the TableBody's) 
	 * Position when scrolled
	 * 
	 * @param source
	 */
    private void syncViewPosition(Composite source) {
        tablePosition.x = -bodyFrame.getHorizontalBar().getSelection();
        tablePosition.y = -bodyFrame.getVerticalBar().getSelection();
        int viewWidth = source.getSize().x - source.getVerticalBar().getSize().x;
        int viewHeight = source.getSize().y - source.getHorizontalBar().getSize().y;
        if (viewWidth > tableSize.x) tablePosition.x = 0;
        if (viewHeight > tableSize.y) tablePosition.y = 0;
    }

    /**
	 * Syncronizes the Header with the TableBody when scrolled
	 * so the Header won't stay on its Position while the Body 
	 * is scrolled  
	 */
    protected void syncHeader() {
        Control[] buttons = header.getChildren();
        int width = 0;
        for (int k = 0; k < buttons.length; k++) {
            Button button = (Button) buttons[k];
            button.setLocation(tablePosition.x + width, button.getLocation().y);
            width += button.getSize().x;
            if (k == buttons.length - 1) {
                button.setSize(button.getSize().x - tablePosition.x, button.getSize().y);
            }
        }
    }

    /**
	 * Scrolls the horizontal Bar horizontally
	 * 
	 * @param bar
	 */
    protected void scrollHorizontally(ScrollBar bar) {
        int x = bar.getSelection();
        int y = bodyArea.getLocation().y;
        bodyArea.setLocation(-x, y);
        tablePosition.x = -x;
        syncHeader();
    }

    /**
	 * Scrolls the vertical Bar vertically
	 * 
	 * @param bar
	 */
    protected void scrollVertically(ScrollBar bar) {
        int x = bodyArea.getLocation().x;
        int y = bar.getSelection();
        bodyArea.setLocation(x, -y);
        tablePosition.y = -y;
    }

    public void paintControl(PaintEvent e) {
        Composite source = (Composite) e.getSource();
        bodyArea.getClientArea().width = tableSize.x;
        bodyArea.getClientArea().height = tableSize.y;
        bodyArea.setSize(tableSize);
        bodyArea.setLocation(tablePosition);
        if (source.equals(bodyFrame)) {
            buildFrameLines(e.gc);
        } else if (source.equals(bodyArea)) {
            buildBodyLines(e.gc);
            clearGraphArea(e.gc);
            bodyFrame.redraw();
        }
    }

    /**
	 * Clears the Area of the DataflowGraph
	 * 
	 * @param g
	 */
    protected void clearGraphArea(GC g) {
        if ((colWidths == null) || (rowHeight == null)) return;
        int xPos = 0;
        if (graphColumn > 0) {
            for (int i = 0; i < graphColumn; i++) {
                xPos += colWidths[i].intValue();
            }
        }
        int width = colWidths[graphColumn].intValue();
        int height = numRows * rowHeight.intValue();
        Color formerColor = g.getBackground();
        if (bgColor == null) bgColor = DEFAULT_BG_COLOR;
        g.setBackground(bgColor);
        g.fillRectangle(xPos, 1, width - 1, height);
        if (formerColor == null) formerColor = bgColor;
        g.setBackground(bgColor);
    }

    /**
	 * Builds Lines for the TableBody
	 * 
	 * @param g
	 */
    protected void buildBodyLines(GC g) {
        if (lineColor == null) lineColor = DEFAULT_LINE_COLOR;
        g.setForeground(lineColor);
        if (rowHeight == null) rowHeight = new Integer(DEFAULT_ROW_HEIGHT);
        int rowWidth = tableSize.x;
        for (int i = 0; i <= numRows; i++) {
            g.drawLine(0, i * rowHeight.intValue(), rowWidth, i * rowHeight.intValue());
        }
        int colHeight = tableSize.y;
        int width = 0;
        for (int j = 0; j < numCols; j++) {
            if (colWidths == null) width += DEFAULT_COL_WIDTH; else width += colWidths[j].intValue();
            g.drawLine(width - 1, 0, width - 1, colHeight);
        }
    }

    /**
	 * Build Lines, that are visible, when the Table's Size is 
	 * smaller than the Viewers Size; then, - like other Eclipse Views - 
	 * the Rest is filled with Lines till the End of the View 
	 * 
	 * @param g
	 */
    protected void buildFrameLines(GC g) {
        if (bgColor == null) bgColor = DEFAULT_BG_COLOR;
        bodyFrame.setBackground(bgColor);
        if (lineColor == null) lineColor = DEFAULT_LINE_COLOR;
        g.setForeground(lineColor);
        int hSpace = ((GridLayout) bodyArea.getLayout()).horizontalSpacing;
        int tableX = tablePosition.x;
        int tableY = tablePosition.y;
        if (rowHeight == null) rowHeight = new Integer(DEFAULT_ROW_HEIGHT);
        int viewWidth = bodyFrame.getSize().x;
        if (viewWidth > tableSize.x) {
            for (int i = 0; i < numRows; i++) {
                g.drawLine(tableSize.x, tableY + i * rowHeight.intValue(), viewWidth, tableY + i * rowHeight.intValue());
            }
        }
        int viewHeight = bodyFrame.getSize().y;
        if (viewHeight > tableSize.y) {
            int yPos = tableY + tableSize.y;
            while (yPos < viewHeight) {
                g.drawLine(0, yPos, viewWidth, yPos);
                yPos += rowHeight.intValue();
            }
            yPos = tableY + tableSize.y;
            int width = 0;
            for (int j = 0; j < numCols; j++) {
                if (colWidths == null) width += DEFAULT_COL_WIDTH; else width += colWidths[j].intValue();
                g.drawLine(tableX + width - 1, yPos, tableX + width - 1, viewHeight);
            }
        }
    }

    /**
	 * Build Labels for showing the TableData
	 * 
	 * @param table
	 * @param tableData
	 */
    private void buildTableData(Composite table, ArrayList tableData) {
        if (bgColor == null) bgColor = DEFAULT_BG_COLOR;
        if (fgColor == null) fgColor = DEFAULT_FG_COLOR;
        int hSpace = ((GridLayout) table.getLayout()).horizontalSpacing;
        int vSpace = ((GridLayout) table.getLayout()).verticalSpacing;
        for (int i = 0; i < tableData.size(); i++) {
            ArrayList rowData = (ArrayList) tableData.get(i);
            int xPos = 0;
            int width = 0;
            int height = 0;
            for (int j = 0; j < numCols; j++) {
                DataflowGraphTableData data = (DataflowGraphTableData) rowData.get(j);
                String text = "";
                int style = SWT.NONE;
                if (data != null) {
                    text = data.getData().toString();
                    style = data.getStyle();
                }
                width = colWidths[j].intValue() - hSpace;
                height = rowHeight.intValue() - vSpace;
                if (j == graphColumn) {
                    if (i == 0) {
                        graphArea = createGraphArea(table, bgColor, fgColor, new Point(xPos, i * rowHeight.intValue()), new Point(width, numRows * height));
                    } else {
                        xPos += colWidths[j].intValue();
                    }
                    continue;
                }
                createLabel(table, style, text, bgColor, fgColor, new Point(xPos, i * rowHeight.intValue()), new Point(width, height));
                xPos += colWidths[j].intValue();
            }
        }
    }

    /**
	 * Creates a Label that shows the given Text
	 * 
	 * @param parent
	 * @param style
	 * @param text, the Text to show
	 * @param bgColor
	 * @param fgColor
	 * @param coord, where to put the Label 
	 * @param size, how big the Label should be
	 */
    protected void createLabel(Composite parent, int style, String text, Color bgColor, Color fgColor, Point coord, Point size) {
        Label label = new Label(parent, style);
        GridData data = new GridData();
        data.widthHint = size.x;
        data.heightHint = size.y;
        label.setLayoutData(data);
        label.setLocation(coord.x, coord.y);
        label.setBackground(bgColor);
        if (text == null) text = "";
        label.setText(text);
    }

    /**
	 * Creates a Composite, where the graph should be shown
	 * it spans over all Rows of the Table
	 * 
	 * @param parent
	 * @param bgColor
	 * @param fgColor
	 * @param coord
	 * @param size
	 * @return the Graph's Area
	 */
    private Composite createGraphArea(Composite parent, Color bgColor, Color fgColor, Point coord, Point size) {
        Composite graphCanvas = new Composite(parent, SWT.NONE);
        GridData data = new GridData(GridData.FILL_VERTICAL);
        data.widthHint = size.x;
        data.heightHint = size.y;
        data.verticalSpan = numRows;
        graphCanvas.setLayoutData(data);
        graphCanvas.setSize(size);
        graphCanvas.setLayout(new FillLayout());
        return graphCanvas;
    }
}
