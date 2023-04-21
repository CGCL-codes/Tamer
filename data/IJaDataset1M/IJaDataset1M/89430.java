package skycastle.flexibleui.viewhost;

import skycastle.flexibleui.viewhost.layout.Layout;
import skycastle.flexibleui.viewhost.layout.LayoutListener;
import skycastle.flexibleui.viewhost.layout.operations.LayoutOperation;
import skycastle.flexibleui.viewhost.views.NodeView;
import skycastle.flexibleui.viewhost.views.RootView;
import skycastle.flexibleui.viewhost.views.View;
import skycastle.model.Model;
import skycastle.model.reference.ModelReferenceImpl;
import skycastle.util.ParameterChecker;
import skycastle.util.uiidentity.UiIdentity;
import skycastle.util.uiidentity.UiIdentityImpl;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Hans H�ggstr�m
 */
public final class ViewContainerImpl implements ViewContainer {

    private final LayoutListener myLayoutListener = createLayoutListener();

    private final Map<String, View> myViews = new HashMap<String, View>();

    private final Random myRandom = new Random();

    private final Model myModel;

    private JPanel myEditModePanel = null;

    private RootView myRootView;

    private JComponent myUiComponent = null;

    private JSplitPane myEditModeSplitPane = null;

    private JPanel myRootPanel = null;

    private boolean myEditModeOn = false;

    private Layout myLayout = null;

    private double myPreviousDividerLocation = EDIT_MODE_SPLIT_PANE_RESIZE_WEIGHT;

    private static final String ROOT_VIEW_IDENTIFIER = "Root";

    private static final String TOGGLE_EDIT_MODE_ACTION_KEY = "F12";

    private static final int EDIT_MODE_SPLIT_PANE_DIVIDER_SIZE = 8;

    private static final double EDIT_MODE_SPLIT_PANE_RESIZE_WEIGHT = 0.2;

    /**
     * Creates a ViewContainerImpl.
     *
     * @param model the data model backing up the view.  Views can display parts of this data model.
     */
    public ViewContainerImpl(final Model model) {
        ParameterChecker.checkNotNull(model, "model");
        myModel = model;
        buildRootView();
    }

    public JComponent getUiComponent() {
        if (myRootPanel == null) {
            myRootPanel = new JPanel(new BorderLayout());
            myEditModeSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            myEditModePanel = createEditModePanel();
            myUiComponent = myRootView.getUi();
            myEditModeSplitPane.setRightComponent(myUiComponent);
            myEditModeSplitPane.setLeftComponent(myEditModePanel);
            myEditModeSplitPane.setResizeWeight(EDIT_MODE_SPLIT_PANE_RESIZE_WEIGHT);
            myRootPanel.add(myEditModeSplitPane, BorderLayout.CENTER);
            myEditModeSplitPane.setDividerLocation(myPreviousDividerLocation);
            setupEditModeToggleAction();
            updateUiOnEditModeChange();
        }
        return myRootPanel;
    }

    public void setLayout(Layout layout) {
        ParameterChecker.checkNotNull(layout, "layout");
        if (myLayout != null) {
            myLayout.removeLayoutListener(myLayoutListener);
        }
        myLayout = layout;
        myLayout.applyLayout(this);
        myLayout.addLayoutListener(myLayoutListener);
    }

    public Layout getLayout() {
        return myLayout;
    }

    public void setEditMode(boolean editModeOn) {
        if (editModeOn != myEditModeOn) {
            myEditModeOn = editModeOn;
            updateUiOnEditModeChange();
        }
    }

    public void removeAllViews() {
        myViews.clear();
        myRootView.setContainedView(null);
        myRootView.setViewContainer(null);
        myRootView = null;
        buildRootView();
    }

    public View getView(final String viewIdentifier) {
        final View view = getExistingView(viewIdentifier);
        if (view == null) {
            logWarning("A view with identifier '" + viewIdentifier + "' was not found when creating the UI.");
            throw new IllegalArgumentException("Was not able to find view '" + view + "'.");
        }
        return view;
    }

    public void registerView(final View view) {
        ParameterChecker.checkNotNull(view, "view");
        ParameterChecker.checkNotAlreadyContained(view, myViews, "myViews");
        view.setViewContainer(this);
        myViews.put(view.getIdentifier(), view);
    }

    public UiIdentity createUniqueIdentity(final String baseName) {
        ParameterChecker.checkIsIdentifier(baseName, "baseName");
        return new UiIdentityImpl(baseName + "_" + Math.abs(myRandom.nextLong() / 2));
    }

    public Model getModel() {
        return myModel;
    }

    public boolean isEditModeOn() {
        return myEditModeOn;
    }

    /**
     * Create an action for toggling the editmode, and register it with the root panel.
     */
    private void setupEditModeToggleAction() {
        registerActionOnAllConditions(KeyStroke.getKeyStroke(TOGGLE_EDIT_MODE_ACTION_KEY), "skycastle_toggleEditMode");
        myRootPanel.getActionMap().put("skycastle_toggleEditMode", new AbstractAction("skycastle_toggleEditMode") {

            public void actionPerformed(ActionEvent evt) {
                setEditMode(!isEditModeOn());
            }
        });
    }

    private void registerActionOnAllConditions(final KeyStroke keyStroke, final String actionMapKey) {
        myRootPanel.getInputMap(JComponent.WHEN_FOCUSED).put(keyStroke, actionMapKey);
        myRootPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionMapKey);
        myRootPanel.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, actionMapKey);
    }

    private JPanel createEditModePanel() {
        final JPanel editPanel = new JPanel(new BorderLayout());
        editPanel.add(new JLabel("Edit Panel (Toggle with " + TOGGLE_EDIT_MODE_ACTION_KEY + ")"), BorderLayout.NORTH);
        final NodeView nodeView = new NodeView(new UiIdentityImpl("ViewContainer_NodeBrowser"));
        nodeView.setNodeReference(new ModelReferenceImpl("avatar"));
        nodeView.setViewContainer(this);
        editPanel.add(nodeView.getUi(), BorderLayout.CENTER);
        return editPanel;
    }

    private void updateUiOnEditModeChange() {
        if (isEditModeOn()) {
            myEditModeSplitPane.setDividerSize(EDIT_MODE_SPLIT_PANE_DIVIDER_SIZE);
            myEditModeSplitPane.setDividerLocation(myPreviousDividerLocation);
            myEditModePanel.setVisible(true);
        } else {
            if (myEditModeSplitPane.getDividerLocation() > 0 && myEditModeSplitPane.getWidth() > 0) {
                myPreviousDividerLocation = 1.0 * myEditModeSplitPane.getDividerLocation() / myEditModeSplitPane.getWidth();
            }
            myEditModeSplitPane.setDividerSize(0);
            myEditModeSplitPane.setDividerLocation(0);
            myEditModePanel.setVisible(false);
        }
        myRootView.setEditMode(isEditModeOn());
        myRootPanel.repaint();
    }

    /**
     * Creates and registers the root view.
     */
    private void buildRootView() {
        myRootView = new RootView(new UiIdentityImpl(ROOT_VIEW_IDENTIFIER));
        registerView(myRootView);
    }

    private LayoutListener createLayoutListener() {
        final ViewContainer thisContainer = this;
        return new LayoutListener() {

            public void onLayoutOperationApplied(Layout layout, LayoutOperation operation) {
                operation.applyOperation(thisContainer, myModel);
            }

            public void onLayoutChanged(Layout layout) {
                layout.applyLayout(thisContainer);
            }
        };
    }

    private View getExistingView(final String viewIdentifier) {
        return myViews.get(viewIdentifier);
    }

    private void logWarning(final String message) {
        Logger.getLogger(getClass().getName()).warning(message);
    }
}
