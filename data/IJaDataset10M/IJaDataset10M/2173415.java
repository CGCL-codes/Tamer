package tm.displayEngine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;
import tm.displayEngine.generators.AbstractGenerator;
import tm.displayEngine.generators.RegionGenerator;
import tm.displayEngine.generators.SelectionGenerator;
import tm.displayEngine.generators.StoreGenerator;
import tm.interfaces.CommandInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.ImageSourceInterface;
import tm.subWindowPkg.SmallToggleButton;
import tm.subWindowPkg.ToolBar;
import tm.subWindowPkg.WorkArea;
import tm.utilities.Assert;

/**
 * New class to display linked Datums ("box and arrow view")
 * 
 * This class is built on top of the notion of a LinkedDatumDisplay, a lightweight
 * class of self-drawing objects which represent the display of Datums
 * on the screen. In the linked view of datums a potentially arbitrary set
 * of datums act as a generator for a tree. The generator datums form the root
 * nodes of the tree and are displayed in the leftmost column. Branches occur
 * from every node which has pointers from it.
 * 
 * So far as the linked display is concerned, a node is a "top-level" datum,
 * that is a datum with no parent. A "root node" is any top level node in the
 * generator and "the root node" is the first root node in the generator.
 * 
 * Since Datums are hierarchial, DatumDisplay objects may
 * be expanded or contracted. An unexpanded compound D3 object acts as if it
 * were a scalar node, that is a leaf node.
 * 
 * @author Michael Bruce-Lockhart
 * @since April 12, 2001
 * @see LinkedDatumDisplay
 * @see LinkedD3Iterator
 * @see LinkedLayoutManager
 * @see RegionGenerator
 */
public class LinkedDisplay extends DataVisualizerAdapter implements DataDisplayView {

    private static final int LAST_STEP = 20;

    private static final int STATIC_BUTTON = 0;

    private static final int STACK_BUTTON = 1;

    private static final int SELECTION_BUTTON = 2;

    private SmallToggleButton[] buttons = new SmallToggleButton[3];

    private ToolBar toolBar;

    private Boolean stackDown;

    private Boolean staticDown;

    private boolean animate = false;

    private SelectionGenerator selectionGen;

    private LinkedLayoutManager layoutManager;

    public LinkedDisplay(DisplayManager dm, String configId) {
        super(dm, configId);
        CommandInterface cp = context.getCommandProcessor();
        localGenerator = new StoreGenerator(new RegionGenerator(cp.getStaticRegion()), new RegionGenerator(cp.getStackRegion()));
        selectionGen = new SelectionGenerator(dm, this);
        myGenerator = localGenerator;
        layoutManager = new LinkedLayoutManager(this);
        setScale(1, LinkedDatumDisplay.BASE_HEIGHT);
        ImageSourceInterface imageSource = context.getImageSource();
        buttons[STATIC_BUTTON] = new SmallToggleButton("Static", imageSource);
        buttons[STATIC_BUTTON].setToolTipText("View static datums");
        buttons[STACK_BUTTON] = new SmallToggleButton("Stack", imageSource);
        buttons[STACK_BUTTON].setToolTipText("View stack datums");
        buttons[SELECTION_BUTTON] = new SmallToggleButton("heapStore", imageSource);
        buttons[SELECTION_BUTTON].setToolTipText("View selected datums");
        buttons[STACK_BUTTON].setButton(true);
        toolBar = new ToolBar(buttons);
        mySubWindow.addToolBar(toolBar);
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public int getDatumView() {
        return 0;
    }

    public int getNameW() {
        return 0;
    }

    public int getValueW() {
        return 0;
    }

    public int getAddressW() {
        return 0;
    }

    public void refresh() {
        for (int i = 0; i < myGenerator.getNumChildren(); i++) {
            Datum kid = myGenerator.getChildAt(i);
            LinkedDatumDisplay ldd = LinkedDatumDisplay.getLDD(kid, this);
            if (ldd == null) {
                ldd = new LinkedDatumDisplay(kid, this, 0);
            }
        }
        myGenerator.refresh();
        LinkedD3Iterator iterator = myGenerator.getIterator(this);
        iterator.refresh();
        layoutManager.layoutDisplay(myGenerator);
        super.refresh();
    }

    public void buttonPushed(int i) {
        if (generatorMode != LOCAL) return;
        Assert.check(i >= 0 && i < 3);
        if (i == SELECTION_BUTTON) {
            if (buttons[SELECTION_BUTTON].isSelected()) {
                stackDown = buttons[STACK_BUTTON].isDown();
                buttons[STACK_BUTTON].setButton(false);
                staticDown = buttons[STATIC_BUTTON].isDown();
                buttons[STATIC_BUTTON].setButton(false);
                myGenerator = selectionGen;
            } else {
                buttons[STACK_BUTTON].setButton(stackDown);
                buttons[STATIC_BUTTON].setButton(staticDown);
                myGenerator = localGenerator;
            }
        } else {
            buttons[SELECTION_BUTTON].setButton(false);
            myGenerator = localGenerator;
            ((StoreGenerator) localGenerator).buttonPushed(i, buttons[i].isSelected());
        }
        refresh();
    }

    protected void mouseJustClicked(MouseEvent evt) {
        super.mouseJustClicked(evt);
        if (evt == null) return;
        Point location = evt.getPoint();
        LinkedD3Iterator iterator = myGenerator.getIterator(this);
        while (!iterator.atEnd()) {
            if (iterator.getNode().contains(location)) {
                iterator.getNode().mouseClicked(location);
                break;
            }
            iterator.increment();
        }
        evt.consume();
    }

    public void drawArea(Graphics2D screen) {
        if (screen == null || myGenerator == null) return;
        int step;
        if (animate) step = LinkedDatumDisplay.LAST_STEP; else step = LinkedDatumDisplay.LAST_STEP;
        LinkedD3Iterator iterator = myGenerator.getIterator(this);
        iterator.reset();
        while (!iterator.atEnd()) {
            iterator.getNode().drawLinks(screen);
            iterator.increment();
        }
        iterator.reset();
        while (!iterator.atEnd()) {
            LinkedDatumDisplay dd = iterator.getNode();
            int h = dd.getSize().height;
            dd.setStep(step);
            dd.draw(screen);
            iterator.increment();
        }
        animate = false;
    }

    public String toString() {
        return "LinkedDisplay for " + (myGenerator == null ? "null" : myGenerator.toString());
    }

    public String getDisplayString() {
        return "linkedDisplayDatum for " + myGenerator.toString();
    }

    public Vector<Datum> getSelected() {
        return null;
    }
}
