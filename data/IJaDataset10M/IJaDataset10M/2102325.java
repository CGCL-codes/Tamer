package jmax.editors.patcher;

import java.awt.*;
import java.awt.event.*;
import java.awt.AWTEvent.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import jmax.commons.*;
import jmax.fts.*;
import jmax.ui.*;
import jmax.ui.fonts.*;
import jmax.toolkit.*;
import jmax.editors.patcher.objects.*;
import jmax.editors.patcher.menus.*;
import jmax.editors.patcher.interactions.*;

/** The graphic workbench for the patcher editor.
 * It handles the interaction of the user with the objects,
 * propagates the mouse events to the objects themselves,
 * allow the selection, moving, erasing, resizing of objects.
 */
public class ErmesSketchPad extends JComponent implements FtsUpdateGroupListener {

    private TransferHandler itsTransferHandler;

    ErmesSketchWindow itsSketchWindow;

    private ContextualMenu itsContextualMenu;

    private Fts fts;

    public Settings getSettings() {
        return itsSketchWindow.getSettings();
    }

    Rectangle invalid = new Rectangle();

    boolean somethingToDraw = false;

    public void updateGroupStart() {
        somethingToDraw = false;
    }

    public void updateGroupEnd() {
        if (somethingToDraw) ;
        repaint(invalid);
    }

    public void paintAtUpdateEnd(int x, int y, int w, int h) {
        if (somethingToDraw) SwingUtilities.computeUnion(x, y, w, h, invalid); else {
            somethingToDraw = true;
            invalid.setBounds(x, y, w, h);
        }
    }

    private DisplayList displayList;

    public DisplayList getDisplayList() {
        return displayList;
    }

    private KeyMap keyMap;

    KeyMap getKeyMap() {
        return keyMap;
    }

    public Fts getFts() {
        return fts;
    }

    FtsObject itsPatcher;

    public FtsObject getFtsPatcher() {
        return itsPatcher;
    }

    FtsPatcherDataObject itsPatcherData;

    private Font currentFont;

    public Font getCurrentFont() {
        return currentFont;
    }

    public final void setCurrentFont(Font font, boolean dirty) {
        currentFont = font;
        itsPatcher.setStringPropertyValue(FtsProperty.OBJECT_WINDOW_FONT, font.getName(), dirty);
        itsPatcher.setIntPropertyValue(FtsProperty.OBJECT_WINDOW_FONT_SIZE, font.getSize(), dirty);
        if (font.getStyle() != Font.PLAIN) itsPatcher.setIntPropertyValue(FtsProperty.OBJECT_WINDOW_FONT_STYLE, font.getStyle(), dirty);
    }

    public final ErmesSketchWindow getSketchWindow() {
        return itsSketchWindow;
    }

    private int incrementalPasteOffsetX;

    private int incrementalPasteOffsetY;

    int numberOfPaste = 0;

    private FtsObject anOldPastedObject = null;

    void resetPaste(int n) {
        numberOfPaste = n;
    }

    void PasteObjects(java.util.List<FtsObject> objects, java.util.List<FtsConnection> connections) throws MaxError {
        numberOfPaste += 1;
        if (currentEditedObject != null) currentEditedObject.endEdit();
        ErmesSelection.patcherSelection.setOwner(this);
        if (!ErmesSelection.patcherSelection.isEmpty()) {
            ErmesSelection.patcherSelection.redraw();
            ErmesSelection.patcherSelection.deselectAll();
        }
        FtsObject firstObject = objects.get(0);
        if (numberOfPaste == 0) {
            incrementalPasteOffsetX = 0;
            incrementalPasteOffsetY = 0;
        } else if (numberOfPaste == 1) {
            anOldPastedObject = firstObject;
            incrementalPasteOffsetX = 20;
            incrementalPasteOffsetY = 20;
        } else if (numberOfPaste == 2) {
            incrementalPasteOffsetX = (anOldPastedObject.getIntPropertyValue(FtsProperty.OBJECT_X) - firstObject.getIntPropertyValue(FtsProperty.OBJECT_X));
            incrementalPasteOffsetY = (anOldPastedObject.getIntPropertyValue(FtsProperty.OBJECT_Y) - firstObject.getIntPropertyValue(FtsProperty.OBJECT_Y));
        }
        for (FtsObject fo : objects) {
            int newPosX = fo.getIntPropertyValue(FtsProperty.OBJECT_X) + numberOfPaste * incrementalPasteOffsetX;
            int newPosY = fo.getIntPropertyValue(FtsProperty.OBJECT_Y) + numberOfPaste * incrementalPasteOffsetY;
            fo.setIntPropertyValue(FtsProperty.OBJECT_X, newPosX, true);
            fo.setIntPropertyValue(FtsProperty.OBJECT_Y, newPosY, true);
            GraphicObject object = GraphicObject.makeGraphicObject(this, fo);
            displayList.add(object);
            ErmesSelection.patcherSelection.select(object);
            object.redraw();
        }
        for (FtsConnection fc : connections) {
            GraphicConnection connection;
            connection = new GraphicConnection(this, displayList.getGraphicObjectFor(fc.getFrom()), fc.getFromOutlet(), displayList.getGraphicObjectFor(fc.getTo()), fc.getToInlet(), fc);
            displayList.add(connection);
            ErmesSelection.patcherSelection.select(connection);
            connection.updateDimensions();
            connection.redraw();
        }
        displayList.reassignLayers();
        displayList.sortDisplayList();
    }

    void InitFromFtsContainer(FtsPatcherDataObject theContainerObject) throws MaxError {
        boolean doLayers = false;
        for (FtsObject ftsObject : theContainerObject.getObjects()) {
            GraphicObject object = GraphicObject.makeGraphicObject(this, ftsObject);
            displayList.add(object);
            if (object.getLayer() < 0) doLayers = true;
        }
        for (FtsConnection fc : theContainerObject.getConnections()) {
            GraphicConnection connection;
            connection = new GraphicConnection(this, displayList.getGraphicObjectFor(fc.getFrom()), fc.getFromOutlet(), displayList.getGraphicObjectFor(fc.getTo()), fc.getToInlet(), fc);
            displayList.add(connection);
            connection.updateDimensions();
        }
        if (doLayers) displayList.reassignLayers();
        displayList.sortDisplayList();
    }

    ErmesSketchPad(Fts fts, ErmesSketchWindow theSketchWindow, FtsPatcherDataObject thePatcherData) throws MaxError {
        super();
        String s;
        this.fts = fts;
        itsSketchWindow = theSketchWindow;
        itsPatcherData = thePatcherData;
        itsPatcher = thePatcherData.getContainerObject();
        itsTransferHandler = new PatcherTransferHandler(this);
        setTransferHandler(itsTransferHandler);
        fts.addUpdateGroupListener(this);
        itsContextualMenu = new ContextualMenu(itsSketchWindow);
        FtsProperty fontSizeProperty;
        FtsProperty fontNameProperty;
        FtsProperty fontStyleProperty;
        fontNameProperty = itsPatcher.getStringProperty(FtsProperty.OBJECT_WINDOW_FONT);
        fontSizeProperty = itsPatcher.getIntProperty(FtsProperty.OBJECT_WINDOW_FONT_SIZE);
        fontStyleProperty = itsPatcher.getIntProperty(FtsProperty.OBJECT_WINDOW_FONT_STYLE);
        if (fontNameProperty.isSet() && fontSizeProperty.isSet()) {
            String fontName;
            int fontSize;
            int fontStyle;
            fontName = fontNameProperty.getStringValue();
            fontSize = fontSizeProperty.getIntValue();
            if (fontStyleProperty.isSet()) fontStyle = fontStyleProperty.getIntValue(); else fontStyle = Font.PLAIN;
            setCurrentFont(FontCache.lookupFont(fontName, fontStyle, fontSize), false);
        } else setCurrentFont(FontManager.getFont("/jmax/editors/patcher/defaultFont/"), false);
        displayList = new DisplayList(this);
        engine = new InteractionEngine(this);
        keyMap = new KeyMap(this, this.getSketchWindow());
        setOpaque(true);
        setLayout(null);
        InitFromFtsContainer(itsPatcherData);
        fixSize();
        requestFocusInWindow();
    }

    public void fixSize() {
        Rectangle totalBounds = new Rectangle();
        Dimension preferredSize = new Dimension();
        boolean redraw = false;
        totalBounds.x = 0;
        totalBounds.y = 0;
        totalBounds.width = 0;
        totalBounds.height = 0;
        displayList.getBounds(totalBounds);
        if ((totalBounds.x < 0) || (totalBounds.y < 0)) {
            int dx, dy;
            if (totalBounds.x < 0) dx = (-1) * totalBounds.x; else dx = 0;
            if (totalBounds.y < 0) dy = (-1) * totalBounds.y; else dy = 0;
            displayList.moveAllBy(dx, dy);
            redraw = true;
        }
        if (totalBounds.width == 0) if (itsPatcher.getIntPropertyValue(FtsProperty.WINDOW_WIDTH) == 0) totalBounds.width = 300; else totalBounds.width = itsPatcher.getIntPropertyValue(FtsProperty.WINDOW_WIDTH);
        if (totalBounds.height == 0) if (itsPatcher.getIntPropertyValue(FtsProperty.WINDOW_HEIGHT) == 0) totalBounds.height = 300; else totalBounds.height = itsPatcher.getIntPropertyValue(FtsProperty.WINDOW_HEIGHT);
        if ((totalBounds.width > getWidth()) || (totalBounds.height > getHeight())) {
            preferredSize.height = totalBounds.height + 5;
            preferredSize.width = totalBounds.width + 5;
            if (preferredSize.height < 20) preferredSize.height = 20;
            if (preferredSize.width < 20) preferredSize.width = 20;
            setPreferredSize(preferredSize);
        }
        if (redraw) redraw();
        revalidate();
    }

    public boolean pointIsVisible(Point point, int margin) {
        Rectangle r = itsSketchWindow.itsScrollerView.getViewport().getViewRect();
        return ((point.x > r.x + margin) && (point.x < r.x + r.width - margin) && (point.y > r.y + margin) && (point.y < r.y + r.height - margin));
    }

    public void scrollBy(int dx, int dy) {
        Rectangle r = itsSketchWindow.itsScrollerView.getViewport().getViewRect();
        r.x = r.x + dx;
        r.y = r.y + dy;
        scrollRectToVisible(r);
        revalidate();
        redraw();
    }

    public Point whereItIs(Point point, Point direction, int margin) {
        Rectangle r = itsSketchWindow.itsScrollerView.getViewport().getViewRect();
        direction.x = 0;
        direction.y = 0;
        if (point.x <= r.x + margin) direction.x = -1; else if (point.x >= r.x + r.width - margin) direction.x = 1;
        if (point.y <= r.y + margin) direction.y = -1; else if (point.y >= r.y + r.height - margin) direction.y = 1;
        return direction;
    }

    void showSelection() {
        Rectangle selectionBounds = ErmesSelection.patcherSelection.getBounds();
        Rectangle visibleRectangle = itsSketchWindow.itsScrollerView.getViewport().getViewRect();
        if (selectionBounds != null) {
            if (!SwingUtilities.isRectangleContainingRectangle(visibleRectangle, selectionBounds)) {
                scrollRectToVisible(selectionBounds);
                revalidate();
            }
        }
    }

    private static final Dimension minSize = new Dimension(30, 20);

    public Dimension getMinimumSize() {
        return minSize;
    }

    GraphicObject makeObject(ObjectCreator creator, int x, int y) throws MaxError {
        FtsObject fo;
        GraphicObject object = null;
        fo = creator.makeFtsObject(fts, itsPatcher);
        object = GraphicObject.makeGraphicObject(this, fo);
        object.setX(x);
        object.setY(y);
        displayList.add(object);
        displayList.reassignLayers();
        object.redraw();
        return object;
    }

    Editable currentEditedObject = null;

    public void setCurrentEditedObject(Editable obj) {
        currentEditedObject = obj;
    }

    public Editable getCurrentEditedObject() {
        return currentEditedObject;
    }

    public void showObject(Object obj) {
        if (obj instanceof FtsObject) {
            GraphicObject object = displayList.getGraphicObjectFor((FtsObject) obj);
            if (object != null) {
                ErmesSelection.patcherSelection.setOwner(this);
                if (ErmesSelection.patcherSelection.isEmpty()) {
                    ErmesSelection.patcherSelection.select(object);
                    object.redraw();
                } else {
                    ErmesSelection.patcherSelection.redraw();
                    ErmesSelection.patcherSelection.deselectAll();
                    ErmesSelection.patcherSelection.select(object);
                    object.redraw();
                }
                showSelection();
            }
        }
    }

    public void paintComponent(Graphics g) {
        displayList.paint(g);
    }

    public final void redraw() {
        repaint();
    }

    void cleanAll() {
        fts.removeUpdateGroupListener(this);
        engine.dispose();
        if (ErmesSelection.patcherSelection.ownedBy(this)) ErmesSelection.patcherSelection.deselectAll();
        displayList.disposeAllObjects();
        itsSketchWindow = null;
        itsPatcher = null;
        anOldPastedObject = null;
    }

    private int waiting = 0;

    public void waiting() {
        if (waiting >= 0) setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        waiting++;
    }

    public void stopWaiting() {
        waiting--;
        if (waiting <= 0) setCursor(Cursor.getDefaultCursor());
    }

    private boolean locked = false;

    void setLocked(boolean locked) {
        this.locked = locked;
        if (locked) setRunModeInteraction(); else setEditModeInteraction();
        if (locked) {
            if (currentEditedObject != null) currentEditedObject.endEdit();
            if (ErmesSelection.patcherSelection.ownedBy(this)) ErmesSelection.patcherSelection.deselectAll();
        }
        redraw();
    }

    public final boolean isLocked() {
        return locked;
    }

    KeyEventClient keyEventClient;

    public void setKeyEventClient(KeyEventClient kv) {
        if (kv == keyEventClient) return;
        if (keyEventClient != null) {
            removeKeyListener(keyEventClient);
            this.keyEventClient.keyInputLost();
        }
        keyEventClient = kv;
        if (keyEventClient != null) {
            addKeyListener(keyEventClient);
            keyEventClient.keyInputGained();
        }
    }

    public void showContextualMenu(Point p) {
        itsContextualMenu.popup(this, p.x, p.y);
    }

    private InteractionEngine engine;

    public void setRunModeInteraction() {
        if (currentEditedObject != null) currentEditedObject.endEdit();
        itsSketchWindow.resetToolBar();
        resetHighlightedInlet();
        resetHighlightedOutlet();
        engine.setTopInteraction(Interactions.runModeInteraction);
    }

    public void setEditModeInteraction() {
        itsSketchWindow.showToolBar();
        itsSketchWindow.resetToolBar();
        engine.setTopInteraction(Interactions.editModeInteraction);
    }

    ObjectCreator newObjectCreator = null;

    public void setAddModeInteraction(ObjectCreator creator) {
        newObjectCreator = creator;
        showMessage(creator.getMessage());
        if (currentEditedObject != null) currentEditedObject.endEdit();
        Cursor customCursor = null;
        try {
            customCursor = Toolkit.getDefaultToolkit().createCustomCursor(Icons.getImage(creator.getIconURI()), new Point(0, 0), "object cursor");
        } catch (MaxError e) {
            e.logError();
        }
        if (customCursor == null) setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); else setCursor(customCursor);
        engine.setTopInteraction(Interactions.addModeInteraction);
    }

    public void makeAddModeObject(int x, int y, boolean edit) throws MaxError {
        GraphicObject object = makeObject(newObjectCreator, x, y);
        if (object instanceof Standard) ((Standard) object).setIgnoreError(true);
        if (edit && (object instanceof Editable)) {
            final Editable obj = (Editable) object;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    ((Editable) obj).edit();
                }
            });
        }
    }

    /** Add an object specified by a description at a given coordinates */
    public void addObject(ObjectCreator creator, int x, int y) throws MaxError {
        GraphicObject object = makeObject(creator, x, y);
        if (object instanceof Standard) ((Standard) object).setIgnoreError(true);
    }

    public void addObject(ObjectCreator creator, Point p) throws MaxError {
        addObject(creator, (int) p.getX(), (int) p.getY());
    }

    public void addObject(final String description, Point p) throws MaxError {
        ObjectCreator creator = new ObjectCreator() {

            public String getIconURI() {
                return null;
            }

            public String getMessage() {
                return "";
            }

            public String getDescription() {
                return description;
            }

            public FtsObject makeFtsObject(Fts fts, FtsObject patcher) throws MaxError {
                return fts.makeFtsObject(patcher, description);
            }
        };
        addObject(creator, (int) p.getX(), (int) p.getY());
    }

    public InteractionEngine getEngine() {
        return engine;
    }

    public void endInteraction() {
        engine.popInteraction();
    }

    private int highlightedInlet;

    private GraphicObject highlightedInletObject = null;

    public int getHighlightedInlet() {
        return highlightedInlet;
    }

    public GraphicObject getHighlightedInletObject() {
        return highlightedInletObject;
    }

    public boolean isHighlightedInlet(GraphicObject object, int inlet) {
        return ((highlightedInletObject == object) && (highlightedInlet == inlet));
    }

    public boolean hasHighlightedInlet(GraphicObject object) {
        return (highlightedInletObject == object);
    }

    public boolean hasHighlightedInlet() {
        return (highlightedInletObject != null);
    }

    public void setHighlightedInlet(GraphicObject object, int inlet) {
        highlightedInletObject = object;
        highlightedInlet = inlet;
        if (highlightedInletObject != null) {
            highlightedInletObject.redraw();
            highlightedInletObject.redrawConnections();
        }
    }

    public void resetHighlightedInlet() {
        if (highlightedInletObject != null) {
            highlightedInletObject.redraw();
            highlightedInletObject.redrawConnections();
        }
        highlightedInletObject = null;
    }

    int highlightedOutlet;

    GraphicObject highlightedOutletObject = null;

    public int getHighlightedOutlet() {
        return highlightedOutlet;
    }

    public GraphicObject getHighlightedOutletObject() {
        return highlightedOutletObject;
    }

    public boolean isHighlightedOutlet(GraphicObject object, int outlet) {
        return ((highlightedOutletObject == object) && (highlightedOutlet == outlet));
    }

    public boolean hasHighlightedOutlet(GraphicObject object) {
        return (highlightedOutletObject == object);
    }

    public boolean hasHighlightedOutlet() {
        return (highlightedOutletObject != null);
    }

    public void setHighlightedOutlet(GraphicObject object, int outlet) {
        highlightedOutletObject = object;
        highlightedOutlet = outlet;
        if (highlightedOutletObject != null) {
            highlightedOutletObject.redraw();
            highlightedOutletObject.redrawConnections();
        }
    }

    public void resetHighlightedOutlet() {
        if (highlightedOutletObject != null) {
            highlightedOutletObject.redraw();
            highlightedOutletObject.redrawConnections();
        }
        highlightedOutletObject = null;
    }

    public void resetHighlighted() {
        resetHighlightedOutlet();
        resetHighlightedInlet();
    }

    public void showMessage(String text) {
        itsSketchWindow.showMessage(text);
    }

    public void resetMessage() {
        itsSketchWindow.resetMessage();
    }

    public boolean isMessageReset() {
        return itsSketchWindow.isMessageReset();
    }

    public void queueMouseEvent(MouseEvent e) {
        processMouseEvent(e);
    }
}
