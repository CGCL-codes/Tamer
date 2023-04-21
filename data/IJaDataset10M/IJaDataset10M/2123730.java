package uk.ac.lkl.migen.system.expresser;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import uk.ac.lkl.common.util.config.MiGenConfiguration;
import uk.ac.lkl.common.util.event.UpdateEvent;
import uk.ac.lkl.common.util.event.UpdateListener;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.ExpresserLauncher;
import uk.ac.lkl.migen.system.expresser.model.AttributeHandle;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.expresser.model.ModelCopier;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.tiednumber.TiedNumberExpression;
import uk.ac.lkl.migen.system.expresser.ui.AttributeManifest;
import uk.ac.lkl.migen.system.expresser.ui.AttributeManifestRow;
import uk.ac.lkl.migen.system.expresser.ui.BlockShapeCanvasPanel;
import uk.ac.lkl.migen.system.expresser.ui.DocumentCanvas;
import uk.ac.lkl.migen.system.expresser.ui.ExpresserModelPanel;
import uk.ac.lkl.migen.system.expresser.ui.ExpressionPanel;
import uk.ac.lkl.migen.system.expresser.ui.GlobalColorAllocationPanel;
import uk.ac.lkl.migen.system.expresser.ui.MasterSlaveUniverseMicroworldPanel;
import uk.ac.lkl.migen.system.expresser.ui.MultiUniverseTabbedPanel;
import uk.ac.lkl.migen.system.expresser.ui.ObjectCanvasFrame;
import uk.ac.lkl.migen.system.expresser.ui.ObjectCanvasPanel;
import uk.ac.lkl.migen.system.expresser.ui.ObjectSetCanvas;
import uk.ac.lkl.migen.system.expresser.ui.PlayAnimationButton;
import uk.ac.lkl.migen.system.expresser.ui.TiedNumberPanel;
import uk.ac.lkl.migen.system.expresser.ui.TileButton;
import uk.ac.lkl.migen.system.expresser.ui.UntiedNumberPanel;
import uk.ac.lkl.migen.system.expresser.ui.behaviour.DragSelectBehaviour;
import uk.ac.lkl.migen.system.expresser.ui.behaviour.PressDragBehaviour;
import uk.ac.lkl.migen.system.expresser.ui.behaviour.ShapePopupMenuBehaviour;
import uk.ac.lkl.migen.system.expresser.ui.ecollaborator.ActivityDocument;
import uk.ac.lkl.migen.system.expresser.ui.ecollaborator.DocumentCanvasContainer;
import uk.ac.lkl.migen.system.expresser.ui.menu.ActivityDocumentSaveAsTabMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.CloseTabMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.LoadActivityDocumentTabMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.LoadModelTabMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.ModelSaveAsTabMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.RedoMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.SelectAllMenuItem;
import uk.ac.lkl.migen.system.expresser.ui.menu.UndoMenuItem;
import uk.ac.lkl.migen.system.util.MiGenUtilities;

/**
 * Provides an "API" for external clients to access and alter the model,
 * property lists of shapes, and interface components
 * 
 * @author Ken Kahn
 * 
 */
public class ExternalInterface {

    private static UntiedNumberPanel<IntegerValue> numberSource;

    private static HashMap<ModelColor, TileButton> colorToTileButtons = new HashMap<ModelColor, TileButton>();

    private static JButton zoomInButton;

    private static JButton zoomOutButton;

    private static PlayAnimationButton playButton;

    private static JButton helpButton;

    private static LoadModelTabMenuItem loadTabMenuItem;

    private static ModelSaveAsTabMenuItem saveTabMenuItem;

    private static ActivityDocumentSaveAsTabMenuItem activityDocumentSaveTabMenuItem;

    private static LoadActivityDocumentTabMenuItem loadActivityDocumentTabMenuItem;

    private static CloseTabMenuItem closeTabMenuItem;

    private static JMenuItem exitMenuItem;

    private static SelectAllMenuItem selectAllMenuItem;

    private static UndoMenuItem undoDeleteMenuItem;

    private static RedoMenuItem redoDeleteMenuItem;

    private static JMenu fileMenu;

    private static JMenu tasksMenu;

    private static JMenu editMenu;

    /**
     * Sets the taskVariableIndex-th task variable of the model in the
     * tabIndex-th tab
     * 
     * @param taskVariableIndex
     * @param tabIndex
     * @param newValue
     * @return true if the variable and tab exist
     */
    @Deprecated
    public static boolean setVariable(int taskVariableIndex, int tabIndex, int newValue) {
        TiedNumberExpression<IntegerValue> tiedNumberExpression = getTaskVariable(taskVariableIndex, tabIndex);
        if (tiedNumberExpression == null) {
            return false;
        }
        tiedNumberExpression.setValue(new IntegerValue(newValue));
        return true;
    }

    @Deprecated
    private static TiedNumberExpression<IntegerValue> getTaskVariable(int taskVariableIndex, int tabIndex) {
        List<TiedNumberExpression<IntegerValue>> taskVariables = getTaskVariables(tabIndex);
        if (taskVariables == null) {
            return null;
        }
        if (taskVariableIndex < 0 || taskVariableIndex >= taskVariables.size()) {
            return null;
        }
        TiedNumberExpression<IntegerValue> tiedNumberExpression = (TiedNumberExpression<IntegerValue>) taskVariables.get(taskVariableIndex);
        return tiedNumberExpression;
    }

    private static List<TiedNumberExpression<IntegerValue>> getTaskVariables(int tabIndex) {
        ExpresserModel model = getModel(tabIndex);
        if (model == null) {
            return null;
        }
        return model.getUnlockedNumbers();
    }

    /**
     * 
     * @param taskVariableIndex
     * @param tabIndex
     * @return the value of the taskVariableIndex-th task variable of the model
     *         in the tabIndex-th tab
     *         or Integer.MIN_VALUE if there is no such variable
     */
    @Deprecated
    public static int getVariableValue(int taskVariableIndex, int tabIndex) {
        TiedNumberExpression<IntegerValue> tiedNumberExpression = getTaskVariable(taskVariableIndex, tabIndex);
        if (tiedNumberExpression == null) {
            return Integer.MIN_VALUE;
        } else {
            return tiedNumberExpression.getValue().getInt();
        }
    }

    /**
     * 
     * @param taskVariableIndex
     * @param tabIndex
     * @return the name of the taskVariableIndex-th task variable of the model
     *         in the tabIndex-th tab or null if there is no such variable
     */
    @Deprecated
    public static String getVariableName(int taskVariableIndex, int tabIndex) {
        TiedNumberExpression<IntegerValue> tiedNumberExpression = getTaskVariable(taskVariableIndex, tabIndex);
        if (tiedNumberExpression == null) {
            return null;
        } else {
            return tiedNumberExpression.getName();
        }
    }

    /**
     * @param tabIndex
     * @return the number of task variables or zero if there are none
     */
    @Deprecated
    public static int getTaskVariableCount(int tabIndex) {
        List<TiedNumberExpression<IntegerValue>> taskVariables = getTaskVariables(tabIndex);
        if (taskVariables == null) {
            return 0;
        } else {
            return taskVariables.size();
        }
    }

    public static ExpresserModel getModel(int tabIndex) {
        MultiUniverseTabbedPanel modelCopyTabbedPanel = ExpresserLauncher.getModelCopyTabbedPanel();
        if (modelCopyTabbedPanel == null) {
            return null;
        }
        return modelCopyTabbedPanel.getModelAt(tabIndex);
    }

    public static boolean enableColouring() {
        MiGenConfiguration.setHideColourAllocationAttributes(false);
        return true;
    }

    public static boolean disableUnlocking() {
        TiedNumberPanel.expresserFullyLockedDown = true;
        return true;
    }

    public static boolean enableUnlocking() {
        TiedNumberPanel.expresserFullyLockedDown = false;
        return true;
    }

    public static boolean hideNumberGenerator() {
        JPanel numberGenerator = ObjectCanvasPanel.getExpressionResourcePanel();
        numberGenerator.setVisible(false);
        return true;
    }

    public static boolean showNumberGenerator() {
        JPanel numberGenerator = ObjectCanvasPanel.getExpressionResourcePanel();
        numberGenerator.setVisible(true);
        return true;
    }

    public static List<BlockShape> getShapes(int tabIndex) {
        return getModel(tabIndex).getShapes();
    }

    /**
     * @param tabIndex
     * @param shape
     * @param attributeHandle
     * @param on
     * @return whether the attribute exists
     * if on then creates it if necessary and 'highlights' it
     * if off removes any highlighting that may exist
     */
    public static boolean setAttributeHighlighted(int tabIndex, BlockShape shape, AttributeHandle<IntegerValue> attributeHandle, boolean on) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        AttributeManifestRow rowPanel = AttributeManifest.rowPanelForAttributeOfHandle(attributeHandle, shape, masterCanvas, false);
        boolean rowPanelExists = rowPanel != null;
        if (on) {
            if (!rowPanelExists) {
                rowPanel = AttributeManifest.rowPanelForAttributeOfHandle(attributeHandle, shape, masterCanvas, true);
            }
            rowPanel.setBackground(Color.RED);
        } else {
            if (rowPanelExists) {
                rowPanel.setBackground(null);
            }
        }
        return rowPanelExists;
    }

    /**
     * @param tabIndex
     * @param shape
     * @param attributeHandle
     * @return the AttributeManifestRow that is the row in an open
     * property list of the attribute with attributeHandle
     * of the shape in the tab with tabIndex.
     * AttributeManifestRow extends JPanel with getExpressionPanel()
     */
    public static AttributeManifestRow getRowPanelForAttributeOfHandle(int tabIndex, BlockShape shape, AttributeHandle<IntegerValue> attributeHandle) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        if (masterCanvas == null) {
            return null;
        }
        return AttributeManifest.rowPanelForAttributeOfHandle(attributeHandle, shape, masterCanvas, false);
    }

    /**
     * @param tabIndex
     * @param shape
     * @return the first part of the construction expression for a shape
     * or null if there is none open for the shape in the tab.
     */
    public Component getConstructionExpressionStart(int tabIndex, BlockShape shape) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        AttributeManifest manifest = AttributeManifest.manifestOpenForShape(masterCanvas, shape);
        if (manifest == null) {
            return null;
        } else {
            return manifest.getConstructionExpressionStart();
        }
    }

    /**
     * @param tabIndex
     * @param shape
     * @param on if on ensures that the property list is open, if off ensures there it isn't open 
     * @return whether there is shape in the tab with an open property list
     * 
     */
    public static boolean setPropertyListOpen(int tabIndex, BlockShape shape, boolean on) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        AttributeManifest manifest = AttributeManifest.manifestOpenForShape(masterCanvas, shape);
        if (on) {
            if (manifest == null) {
                AttributeManifest attributeManifest = AttributeManifest.attributeManifestForShape(shape, masterCanvas, true);
                masterCanvas.setManifestLocation(shape, attributeManifest);
                return false;
            } else {
                return true;
            }
        } else {
            if (manifest == null) {
                return false;
            } else {
                manifest.dispose();
                return true;
            }
        }
    }

    /**
     * @param tabIndex
     * @param shape
     * @return the property list of the shape
     * in the tab indexed with tabIndex.
     */
    public static AttributeManifest getManifestOpenForShape(int tabIndex, BlockShape shape) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        if (masterCanvas == null) {
            return null;
        }
        return AttributeManifest.manifestOpenForShape(masterCanvas, shape);
    }

    /**
     * @param tabIndex
     * @param shape
     * @param x
     * @param y
     * @return Creates and adds a popup menu at x and y
     * for the shape in the tabIndex-th master panel canvas.
     * Note it is the callers responsibility to eventually remove the popup.
     */
    public static JPopupMenu addPopupMenu(int tabIndex, BlockShape shape, int x, int y) {
        ObjectSetCanvas masterCanvas = getMasterCanvas(tabIndex);
        if (masterCanvas == null) {
            return null;
        }
        ShapePopupMenuBehaviour shapePopupMenuBehaviour = new ShapePopupMenuBehaviour(masterCanvas);
        return shapePopupMenuBehaviour.addPopupMenu(shape, x, y);
    }

    /**
     * @param expressionPanel
     * @return Adds and returns a popup menu for the expressionPanel.
     * Note that the caller should dismiss the menu when finished.
     */
    public static JPopupMenu addPopupMenu(ExpressionPanel<IntegerValue> expressionPanel) {
        return expressionPanel.addPopupMenu();
    }

    /**
     * @param tiedNumberPanel
     * @return Adds and returns a popup menu for the tied number.
     * Note that the caller should dismiss the menu when finished.
     * 
     */
    public static JPopupMenu addPopupMenu(TiedNumberPanel<IntegerValue> tiedNumberPanel) {
        return tiedNumberPanel.addPopupMenu();
    }

    /**
     * @param menu
     * @param labelName
     * @return The menu item whose label is named by labelName
     * or null if there is none.
     */
    public static JMenuItem getMenuItem(JPopupMenu menu, String labelName) {
        Component[] components = menu.getComponents();
        String label = MiGenUtilities.getLocalisedMessage(labelName);
        for (Component component : components) {
            if (component instanceof JMenuItem) {
                JMenuItem menuItem = (JMenuItem) component;
                if (menuItem.getText().equals(label)) {
                    return menuItem;
                }
            }
        }
        return null;
    }

    /**
     * @param tabIndex
     * @return the panel with the master and slave with that index
     * or null if there is none
     */
    public static MasterSlaveUniverseMicroworldPanel getMasterSlaveUniverseMicroworldPanel(int tabIndex) {
        MultiUniverseTabbedPanel modelCopyTabbedPanel = ExpresserLauncher.getModelCopyTabbedPanel();
        if (modelCopyTabbedPanel == null) {
            return null;
        }
        ExpresserModelPanel panel = modelCopyTabbedPanel.getInstanceAt(tabIndex);
        if (panel instanceof MasterSlaveUniverseMicroworldPanel) {
            return (MasterSlaveUniverseMicroworldPanel) panel;
        } else {
            return null;
        }
    }

    /**
     * @return index of the currently selected tab (aka page)
     */
    public static int getTabbedPanelSelectedIndex() {
        return ExpresserLauncher.getModelCopyTabbedPanel().getSelectedIndex();
    }

    /**
     * @return the selected panel with the master and slave panel
     */
    public static ExpresserModelPanel getSelectedExpresserModelPanel() {
        return ExpresserLauncher.getModelCopyTabbedPanel().getSelectedModelPanel();
    }

    /**
     * @return the panel to add support buttons 
     * (should the task be intelligently supported)
     */
    public static ExpresserModelPanel getPanelToBeSupported() {
        return getSelectedExpresserModelPanel();
    }

    /**
     * @param tabIndex
     * @return the master panel (My Model) with the index
     * or null if there is none
     */
    public static BlockShapeCanvasPanel getMasterPanel(int tabIndex) {
        MasterSlaveUniverseMicroworldPanel masterSlaveUniverseMicroworldPanel = getMasterSlaveUniverseMicroworldPanel(tabIndex);
        if (masterSlaveUniverseMicroworldPanel == null) {
            return null;
        } else {
            return masterSlaveUniverseMicroworldPanel.getMasterPanel();
        }
    }

    /**
     * @param tabIndex
     * @return the canvas in the master panel (My Model) with the index
     * otherwise null
     */
    public static ObjectSetCanvas getMasterCanvas(int tabIndex) {
        BlockShapeCanvasPanel masterPanel = getMasterPanel(tabIndex);
        if (masterPanel == null) {
            return null;
        } else {
            return masterPanel.getCanvas();
        }
    }

    public static UntiedNumberPanel<IntegerValue> getNumberSource() {
        return numberSource;
    }

    public static void setNumberSource(UntiedNumberPanel<IntegerValue> numberSource) {
        ExternalInterface.numberSource = numberSource;
    }

    public static void addTileButton(ModelColor color, TileButton tileButton) {
        colorToTileButtons.put(color, tileButton);
    }

    /**
     * @param color
     * @return The TileButton that creates tiles of color.
     */
    public static TileButton getTileButton(ModelColor color) {
        return colorToTileButtons.get(color);
    }

    /**
     * @param tabIndex
     * @param panelIndex
     * removed @param master if true use master panel otherwise slave panel
     * @return the panelIndex-th color allocation panel (aka rule panel)
     * of the tabIndex-th tab
     */
    public static GlobalColorAllocationPanel getGlobalAllocationPanel(int tabIndex, int panelIndex) {
        BlockShapeCanvasPanel canvasPanel = getMasterPanel(tabIndex);
        if (canvasPanel == null) {
            return null;
        }
        return canvasPanel.getGlobalAllocationPanel(panelIndex);
    }

    /**
     * @param tabIndex
     * @param panelIndex
     * @return the button that can be activated to add 
     * the popup menu for color choices.
     */
    public static AbstractButton addGlobalAllocationColorChooser(int tabIndex, int panelIndex) {
        GlobalColorAllocationPanel globalAllocationPanel = getGlobalAllocationPanel(tabIndex, panelIndex);
        if (globalAllocationPanel == null) {
            return null;
        }
        return globalAllocationPanel.getColorChooser();
    }

    public static JButton getZoomInButton() {
        return zoomInButton;
    }

    public static void setZoomInButton(JButton zoomInButton) {
        ExternalInterface.zoomInButton = zoomInButton;
    }

    public static JButton getZoomOutButton() {
        return zoomOutButton;
    }

    public static void setZoomOutButton(JButton zoomOutButton) {
        ExternalInterface.zoomOutButton = zoomOutButton;
    }

    public static PlayAnimationButton getPlayButton() {
        return playButton;
    }

    public static void setPlayButton(PlayAnimationButton playButton) {
        ExternalInterface.playButton = playButton;
    }

    public static JButton getHelpButton() {
        return helpButton;
    }

    public static void setHelpButton(JButton helpButton) {
        ExternalInterface.helpButton = helpButton;
    }

    public static HashMap<ModelColor, TileButton> getColorToTileButtons() {
        return colorToTileButtons;
    }

    public static void setColorToTileButtons(HashMap<ModelColor, TileButton> colorToTileButtons) {
        ExternalInterface.colorToTileButtons = colorToTileButtons;
    }

    public static LoadModelTabMenuItem getLoadTabMenuItem() {
        return loadTabMenuItem;
    }

    public static void setLoadTabMenuItem(LoadModelTabMenuItem loadTabMenuItem) {
        ExternalInterface.loadTabMenuItem = loadTabMenuItem;
    }

    public static ModelSaveAsTabMenuItem getSaveTabMenuItem() {
        return saveTabMenuItem;
    }

    public static void setSaveTabMenuItem(ModelSaveAsTabMenuItem saveTabMenuItem) {
        ExternalInterface.saveTabMenuItem = saveTabMenuItem;
    }

    public static CloseTabMenuItem getCloseTabMenuItem() {
        return closeTabMenuItem;
    }

    public static void setCloseTabMenuItem(CloseTabMenuItem closeTabMenuItem) {
        ExternalInterface.closeTabMenuItem = closeTabMenuItem;
    }

    public static JMenuItem getExitMenuItem() {
        return exitMenuItem;
    }

    public static void setExitMenuItem(JMenuItem exitMenuItem) {
        ExternalInterface.exitMenuItem = exitMenuItem;
    }

    public static JMenu getFileMenu() {
        return fileMenu;
    }

    public static void setFileMenu(JMenu fileMenu) {
        ExternalInterface.fileMenu = fileMenu;
    }

    public static JMenu getTasksMenu() {
        return tasksMenu;
    }

    public static void setTasksMenu(JMenu tasksMenu) {
        ExternalInterface.tasksMenu = tasksMenu;
    }

    public static JMenu getEditMenu() {
        return editMenu;
    }

    public static void setEditMenu(JMenu editMenu) {
        ExternalInterface.editMenu = editMenu;
    }

    public static SelectAllMenuItem getSelectAllMenuItem() {
        return selectAllMenuItem;
    }

    public static void setSelectAllMenuItem(SelectAllMenuItem selectAllMenuItem) {
        ExternalInterface.selectAllMenuItem = selectAllMenuItem;
    }

    public static UndoMenuItem getUndoDeleteMenuItem() {
        return undoDeleteMenuItem;
    }

    public static void setUndoDeleteMenuItem(UndoMenuItem undoDeleteMenuItem) {
        ExternalInterface.undoDeleteMenuItem = undoDeleteMenuItem;
    }

    public static RedoMenuItem getRedoDeleteMenuItem() {
        return redoDeleteMenuItem;
    }

    public static void setRedoDeleteMenuItem(RedoMenuItem redoDeleteMenuItem) {
        ExternalInterface.redoDeleteMenuItem = redoDeleteMenuItem;
    }

    public static ActivityDocumentSaveAsTabMenuItem getActivityDocumentSaveTabMenuItem() {
        return activityDocumentSaveTabMenuItem;
    }

    public static void setActivityDocumentSaveTabMenuItem(ActivityDocumentSaveAsTabMenuItem activityDocumentSaveTabMenuItem) {
        ExternalInterface.activityDocumentSaveTabMenuItem = activityDocumentSaveTabMenuItem;
    }

    public static LoadActivityDocumentTabMenuItem getLoadActivityDocumentTabMenuItem() {
        return loadActivityDocumentTabMenuItem;
    }

    public static void setLoadActivityDocumentTabMenuItem(LoadActivityDocumentTabMenuItem loadActivityDocumentTabMenuItem) {
        ExternalInterface.loadActivityDocumentTabMenuItem = loadActivityDocumentTabMenuItem;
    }

    /**
     * @param model -- model being viewed
     * @param gridSize -- initial grid size
     * @param addAnimationToolbar -- if true animation controls are added
     * @param showGrid -- if true the grid is displayed
     * @param addZoomButtons -- if true zoom buttons are added to the AnimationToolbar
     * @param shapesDraggable -- if true shapes can be dragged
     * @param popupShapeMenu -- if true when a shape is clicked a popup menu appears
     * @param enableKeyboardComands -- if true Delete, control-u (undo), control-r (redo) are enabled
     * @param enableReplay -- if true can type control-l to load a set of files and then navigate them 
     *                        with control-r, control-u, control-b (beginning).
     *                        Replay can be from the database (untested) or from a folder of autosaved models.
     * @return a JComponent that views the model
     */
    public static JComponent getModelViewer(ExpresserModel model, int gridSize, boolean addAnimationToolbar, boolean showGrid, boolean addZoomButtons, boolean shapesDraggable, boolean popupShapeMenu, boolean enableKeyboardComands, boolean enableReplay) {
        DocumentCanvasContainer documentCanvasContainer = ActivityDocument.createCanvas(null, model, addAnimationToolbar, "", null, showGrid, addZoomButtons, false, false, false, false, false, false);
        final DocumentCanvas documentCanvas = documentCanvasContainer.getDocumentCanvas();
        documentCanvas.addBehaviour(new DragSelectBehaviour(documentCanvas));
        if (shapesDraggable) {
            documentCanvas.addBehaviour(new PressDragBehaviour(documentCanvas, null));
        }
        if (popupShapeMenu) {
            documentCanvas.addBehaviour(new ShapePopupMenuBehaviour(documentCanvas));
        }
        List<BlockShape> shapes = new ArrayList<BlockShape>(model.getShapes());
        model.removeAllObjects();
        for (BlockShape shape : shapes) {
            model.addObject(shape);
        }
        documentCanvas.setGridSize(gridSize);
        if (addAnimationToolbar) {
            ModelCopier modelCopier = documentCanvas.getModelCopier();
            UpdateListener<ModelCopier> listener = new UpdateListener<ModelCopier>() {

                @Override
                public void objectUpdated(UpdateEvent<ModelCopier> e) {
                    documentCanvas.repaint();
                }
            };
            modelCopier.addUpdateListener(listener);
            modelCopier.copyModelProcess();
        }
        if (enableKeyboardComands) {
            documentCanvas.enableKeyboardCommands();
        }
        if (enableReplay) {
            documentCanvas.enableReplay();
        }
        return documentCanvasContainer.getContents();
    }

    public static ActivityDocument getActivityDocument() {
        ObjectCanvasFrame<IntegerValue> frame = ExpresserLauncher.getFrame();
        if (frame == null) {
            return null;
        } else {
            return frame.getActivityDocument();
        }
    }

    public static JComponent getGeneralModel() {
        return getActivityDocument().getGeneralModelCanvas();
    }

    public static void showGeneralModel() {
        getActivityDocument().setSelectedFrame((DocumentCanvasContainer) getGeneralModel());
    }
}
