package org.jacp.javafx2.rcp.workbench;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.api.component.IRootComponent;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.coordinator.IComponentDelegator;
import org.jacp.api.coordinator.IMessageDelegator;
import org.jacp.api.coordinator.IPerspectiveCoordinator;
import org.jacp.api.handler.IComponentHandler;
import org.jacp.api.launcher.Launcher;
import org.jacp.api.perspective.IPerspective;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.api.workbench.IWorkbench;
import org.jacp.javafx2.rcp.action.FX2Action;
import org.jacp.javafx2.rcp.action.FX2ActionListener;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.componentLayout.FX2WorkbenchLayout;
import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx2.rcp.coordinator.FX2ComponentDelegator;
import org.jacp.javafx2.rcp.coordinator.FX2MessageDelegator;
import org.jacp.javafx2.rcp.coordinator.FX2PerspectiveCoordinator;
import org.jacp.javafx2.rcp.handler.FX2WorkbenchHandler;

/**
 * represents the basic JavaFX2 workbench instance; handles perspectives and
 * components;
 * 
 * @author Andy Moncsek
 */
public abstract class AFX2Workbench implements IWorkbench<Node, EventHandler<Event>, Event, Object>, IRootComponent<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> {

    private List<IPerspective<EventHandler<Event>, Event, Object>> perspectives;

    private IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> componentHandler;

    private final IPerspectiveCoordinator<EventHandler<Event>, Event, Object> perspectiveCoordinator = new FX2PerspectiveCoordinator();

    private final IComponentDelegator<EventHandler<Event>, Event, Object> componentDelegator = new FX2ComponentDelegator();

    private final IMessageDelegator<EventHandler<Event>, Event, Object> messageDelegator = new FX2MessageDelegator();

    private final IWorkbenchLayout<Node> workbenchLayout = new FX2WorkbenchLayout();

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private Launcher<?> launcher;

    private Stage stage;

    private GridPane root;

    private Pane glassPane;

    private JACPModalDialog dimmer;

    /**
	 * JavaFX2 specific start sequence
	 * 
	 * @param stage
	 * @throws Exception
	 */
    public final void start(final Stage stage) throws Exception {
        this.stage = stage;
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent arg0) {
                System.exit(0);
            }
        });
        this.log("1: init workbench");
        this.handleInitialLayout(new FX2Action("TODO", "init"), this.getWorkbenchLayout());
        this.setBasicLayout(stage);
        postHandle(new FX2ComponentLayout(this.getWorkbenchLayout().getMenu(), this.getWorkbenchLayout().getRegisteredToolbars(), glassPane));
        this.log("3: handle initialisation sequence");
        componentHandler = new FX2WorkbenchHandler(this.launcher, this.workbenchLayout, this.root, this.perspectives);
        this.perspectiveCoordinator.setComponentHandler(componentHandler);
        this.componentDelegator.setComponentHandler(componentHandler);
        this.messageDelegator.setComponentHandler(componentHandler);
        this.handleInitialisationSequence();
    }

    @Override
    public void init(Launcher<?> launcher) {
        this.launcher = launcher;
    }

    @Override
    public final void initComponents(final IAction<Event, Object> action) {
        final List<IPerspective<EventHandler<Event>, Event, Object>> perspectivesTmp = this.getPerspectives();
        for (int i = 0; i < perspectivesTmp.size(); i++) {
            final IPerspective<EventHandler<Event>, Event, Object> perspective = perspectivesTmp.get(i);
            this.log("3.4.1: register component: " + perspective.getName());
            this.registerComponent(perspective);
            this.log("3.4.2: create perspective menu");
            if (perspective.isActive()) {
                Platform.runLater(new Runnable() {

                    @Override
                    public final void run() {
                        AFX2Workbench.this.componentHandler.initComponent(new FX2Action(perspective.getId(), perspective.getId(), "init"), perspective);
                    }
                });
            }
        }
    }

    /**
	 * handles sequence for workbench size, menu bar, tool bar and perspective
	 * initialisation
	 */
    private void handleInitialisationSequence() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                AFX2Workbench.this.stage.show();
                ((FX2PerspectiveCoordinator) AFX2Workbench.this.perspectiveCoordinator).start();
                ((FX2ComponentDelegator) AFX2Workbench.this.componentDelegator).start();
                ((FX2MessageDelegator) AFX2Workbench.this.messageDelegator).start();
                AFX2Workbench.this.log("3.2: workbench tool bars");
                AFX2Workbench.this.log("3.3: workbench init perspectives");
                AFX2Workbench.this.initComponents(null);
            }
        });
    }

    @Override
    public void handleInitialLayout(final IAction<Event, Object> action, final IWorkbenchLayout<Node> layout) {
        this.handleInitialLayout(action, layout, this.stage);
    }

    /**
	 * JavaFX2 specific initialization method to create a workbench instance
	 * 
	 * @param action
	 * @param layout
	 * @param stage
	 */
    public abstract void handleInitialLayout(final IAction<Event, Object> action, final IWorkbenchLayout<Node> layout, final Stage stage);

    /**
	 * Handle menu and bar entries created in @see
	 * {@link org.jacp.javafx2.rcp.workbench.AFX2Workbench#handleInitialLayout(IAction, IWorkbenchLayout, Stage)}
	 * 
	 * @param layout
	 */
    public abstract void postHandle(final FX2ComponentLayout layout);

    @Override
    public final void registerComponent(final IPerspective<EventHandler<Event>, Event, Object> perspective) {
        perspective.init(this.componentDelegator.getComponentDelegateQueue(), this.messageDelegator.getMessageDelegateQueue());
        this.perspectiveCoordinator.addPerspective(perspective);
        this.componentDelegator.addPerspective(perspective);
        this.messageDelegator.addPerspective(perspective);
    }

    @Override
    public final void unregisterComponent(final IPerspective<EventHandler<Event>, Event, Object> perspective) {
        this.perspectiveCoordinator.removePerspective(perspective);
        this.componentDelegator.removePerspective(perspective);
        this.messageDelegator.removePerspective(perspective);
    }

    @Override
    public final FX2WorkbenchLayout getWorkbenchLayout() {
        return (FX2WorkbenchLayout) this.workbenchLayout;
    }

    @Override
    public IComponentHandler<IPerspective<EventHandler<Event>, Event, Object>, IAction<Event, Object>> getComponentHandler() {
        return componentHandler;
    }

    @Override
    public final void setPerspectives(final List<IPerspective<EventHandler<Event>, Event, Object>> perspectives) {
        this.perspectives = perspectives;
    }

    @Override
    public final List<IPerspective<EventHandler<Event>, Event, Object>> getPerspectives() {
        return this.perspectives;
    }

    @Override
    public final IActionListener<EventHandler<Event>, Event, Object> getActionListener() {
        return new FX2ActionListener(new FX2Action("workbench"), perspectiveCoordinator.getMessageQueue());
    }

    /**
	 * set basic layout manager for workspace
	 * 
	 * @param stage
	 *            javafx.stage.Stage
	 */
    private void setBasicLayout(final Stage stage) {
        int x = this.getWorkbenchLayout().getWorkbenchSize().getX();
        int y = this.getWorkbenchLayout().getWorkbenchSize().getY();
        StackPane absoluteRoot = new StackPane();
        final BorderPane baseLayoutPane = new BorderPane();
        this.root = new GridPane();
        root.setId("root-pane");
        JACPModalDialog.initDialog(baseLayoutPane);
        dimmer = JACPModalDialog.getInstance();
        dimmer.setVisible(false);
        glassPane = this.getWorkbenchLayout().getGlassPane();
        glassPane.autosize();
        glassPane.setVisible(false);
        glassPane.setPrefSize(0, 0);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(0);
        baseLayoutPane.setEffect(blur);
        stage.initStyle((StageStyle) this.getWorkbenchLayout().getStyle());
        if (!this.getWorkbenchLayout().getRegisteredToolbars().isEmpty()) {
            if (this.getWorkbenchLayout().isMenuEnabled()) {
                baseLayoutPane.setTop(this.getWorkbenchLayout().getMenu());
            }
            final BorderPane toolbarPane = new BorderPane();
            baseLayoutPane.setCenter(toolbarPane);
            final Map<ToolbarPosition, ToolBar> registeredToolbars = this.getWorkbenchLayout().getRegisteredToolbars();
            final Iterator<Entry<ToolbarPosition, ToolBar>> it = registeredToolbars.entrySet().iterator();
            while (it.hasNext()) {
                final Entry<ToolbarPosition, ToolBar> entry = it.next();
                final ToolbarPosition position = entry.getKey();
                final ToolBar toolBar = entry.getValue();
                this.assignCorrectToolBarLayout(position, toolBar, toolbarPane);
            }
            toolbarPane.setCenter(root);
        }
        absoluteRoot.getChildren().add(baseLayoutPane);
        absoluteRoot.setId("root");
        stage.setScene(new Scene(absoluteRoot, x, y));
        initCSS(stage.getScene());
        absoluteRoot.getChildren().add(glassPane);
        absoluteRoot.getChildren().add(dimmer);
    }

    private void initCSS(Scene scene) {
        scene.getStylesheets().addAll(AFX2Workbench.class.getResource("/styles/jacp-styles.css").toExternalForm(), com.sun.javafx.scene.web.skin.HTMLEditorSkin.class.getResource("html-editor.css").toExternalForm());
    }

    /**
	 * set toolBars to correct position
	 * 
	 * @param layout
	 * @param bar
	 * @param pane
	 * @param x
	 * @param y
	 */
    private void assignCorrectToolBarLayout(ToolbarPosition position, ToolBar bar, BorderPane pane) {
        switch(position) {
            case NORTH:
                pane.setTop(bar);
                break;
            case SOUTH:
                pane.setBottom(bar);
                break;
            case EAST:
                bar.setOrientation(Orientation.VERTICAL);
                pane.setRight(bar);
                break;
            case WEST:
                bar.setOrientation(Orientation.VERTICAL);
                pane.setLeft(bar);
                break;
        }
    }

    private void log(final String message) {
        if (this.logger.isLoggable(Level.FINE)) {
            this.logger.fine(">> " + message);
        }
    }
}
