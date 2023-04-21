package view.simView;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import GenCol.*;
import model.modeling.*;
import model.simulation.*;
import model.simulation.realTime.*;
import controller.ControllerInterface;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import facade.modeling.*;
import facade.simulation.*;
import facade.simulation.hooks.*;
import controller.simulation.SimViewCoordinator;
import util.*;
import view.*;
import view.modeling.AtomicView;
import view.modeling.ComponentView;
import view.modeling.ContentView;
import view.modeling.DigraphView;
import view.modeling.DragViewListener;
import view.modeling.ViewableAtomic;
import view.modeling.ViewableComponent;
import view.modeling.ViewableDigraph;
import view.modeling.DynamicStructureViewer;

/**
 * An application that displays the execution of a simulation employing
 * a coupled devs model.
 * 
 * This Class is integrated into the Tracking Environment
 * so, several methods or functionalities from the origin removed
 *
 * @author      Jeff Mather
 * @modified Sungung Kim
 */
public class SimView {

    /**
     * Whether to always show couplings between components, or just when
     * the mouse is over one of them.
     */
    public static boolean alwaysShowCouplings = false;

    /**
     * The panel which displays the components of the current model, along
     * with the scrollpane containing that panel.
     */
    public static ModelView modelView;

    public static double speed = 5;

    /**
     * The width of the border (in pixels) between the top-level model being
     * viewed and the model-view.
     */
    protected final int modelBorderWidth = 5;

    /**
     * The name given to the default digraph that this sim-view uses to
     * wrap single atomic models that are to be viewed.
     */
    protected String wrapperDigraphName = "wrapper digraph";

    /**
     * The model on which the simulation is being executed.
     */
    protected ViewableDigraph model;

    private String sourcePath;

    private JScrollPane modelViewScrollPane;

    private Dimension wrapperSize;

    /**
     * These string store the model name and package name
     */
    private JPanel main;

    private JScrollPane scrollPane;

    private Dimension dim;

    /**
     * Constructs a SimView
     */
    public SimView() {
        constructUI();
    }

    /**
     * Constructs the UI of the main window of this application.
     */
    protected void constructUI() {
        main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        modelView = new ModelView();
        scrollPane = modelViewScrollPane = new JScrollPane(modelView);
        modelViewScrollPane.setAutoscrolls(true);
        main.add(scrollPane);
        ToolTipManager manager = ToolTipManager.sharedInstance();
        manager.setInitialDelay(0);
        manager.setReshowDelay(0);
    }

    public JPanel getSimView() {
        return main;
    }

    /**
     * The panel which displays the components of the current model.
     */
    public class ModelView extends JLayeredPane implements SimulatorHookListener, FCoupledSimulator.FRTCentralCoordX.Listener, FAtomicSimulator.FAtomicSimulatorX.Listener, FCoupledCoordinator.FCoupledCoordX.Listener, DynamicStructureViewer {

        /**
         * The thread responsible for moving the currently displayed
         * content views around this model-view.
         */
        protected MoveContentViewThread moveContentViewThread;

        /**
         * A map of the movement objects of the content views currently
         * being moved around this model-view.
         */
        protected Map contentViewMovementMap = new HashMap();

        /**
         * Whether this view should display the couplings between the
         * components in the model.
         */
        protected boolean showCouplings = false;

        /**
         * The list of couplings between all the components (and their
         * subcomponents) in the model.
         */
        protected List couplings = new ArrayList();

        /**
         * The panel, which should cover this entire view, on which are drawn
         * the lines meant to represent the couplings between the components
         * in the model.
         */
        protected CouplingsPanel couplingsPanel;

        /**
         * The views on the components of the model this view
         * is displaying.
         */
        protected List componentViews = new ArrayList();

        /**
         * The content views currently displayed in this view.
         */
        protected List contentViews = new ArrayList();

        /**
         * A map of the paths contents are taking as they travel from
         * their source to their destination.  The key used is based
         * of the content's latest step in its path, meaning it is
         * composed of the content as well as the step's component name.
         */
        protected Map contentPathMap = new HashMap();

        /**
         * Whether or not this model-view should execute just one
         * iteration of the simulation at a time, and wait until the user
         * tells it to execute the next iteration, or if it should just
         * keep on executing iterations without stopping each time.
         */
        protected boolean stepMode = true;

        /**
         * Constructs a model-view.
         */
        public ModelView() {
            setOpaque(true);
            setBackground(Color.white);
            setLayout(null);
            JPanel panel = couplingsPanel = new CouplingsPanel();
            add(panel, new Integer(2));
            addComponentListener(new ComponentAdapter() {

                public void componentResized(ComponentEvent e) {
                    couplingsPanel.setSize(getSize());
                }
            });
            Thread thread = moveContentViewThread = new MoveContentViewThread();
            thread.start();
        }

        /**
         * Adds the given devs-component view to this model view.
         *
         * @param   view        The view to add.
         */
        public void addView(ComponentView view, JComponent parent) {
            componentViews.add(view);
            JComponent comp = (JComponent) view;
            parent.add(comp, new Integer(0));
            comp.setLocation(view.getPreferredLocation());
            comp.setSize(view.getPreferredSize());
        }

        public void removeView(ComponentView view, JComponent parent) {
            componentViews.remove(view);
            JComponent comp = (JComponent) view;
            ((DigraphView) parent).remove(comp, new Integer(0));
        }

        /**
         * Informs this model view that the user has injected an input
         * into one of the components being viewed.
         */
        public void inputInjected() {
            removeContentViews();
        }

        /**
         * Removes all atomic views from this model-view.
         */
        public void removeAllViews() {
            componentViews.clear();
            modelView.removeAll();
            modelView.add(couplingsPanel);
        }

        /**
         * Informs this model-view that a single iteration of the simulation
         * is about to be executed.
         */
        public void stepToBeTaken() {
            removeContentViews();
            stepMode = true;
        }

        /**
         * Informs this model-view that a run of simulation iterations
         * (versus individual iteration steps) is about to occur.
         */
        public void runToOccur() {
            removeContentViews();
            stepMode = false;
        }

        public void clockChanged(double newTime) {
        }

        public void iterationsCompleted() {
        }

        public void postComputeInputOutputHook() {
        }

        public void simulatorStateChangeHook() {
        }

        /**
         * See ViewableAtomicSimulator.Listener interface method.
         */
        public void contentOutputted(content content, devs source, String sourcePortName) {
            if (!(source instanceof ViewableComponent)) return;
            if (!stepMode) return;
            ContentPathStep step = new ContentPathStep();
            step.view = ((ViewableComponent) source).getView();
            step.portName = sourcePortName;
            List path = new ArrayList();
            path.add(step);
            ContentPathKey key = new ContentPathKey(content, source.getName());
            contentPathMap.put(key, path);
        }

        public void savingModelViewCouplingsRemove(ViewableComponent iod, couprel savedCr) {
            couprel mc = savedCr;
            Iterator it = mc.iterator();
            ViewableDigraph parent = null;
            if (iod instanceof ViewableAtomic) {
                parent = ((ViewableAtomic) iod).getMyParent();
            } else if (iod instanceof ViewableDigraph) {
                parent = ((ViewableDigraph) iod).getMyParent();
            }
            while (it.hasNext()) {
                Pair pr = (Pair) it.next();
                Pair cs = (Pair) pr.getKey();
                Pair cd = (Pair) pr.getValue();
                String src = (String) cs.getKey();
                String dst = (String) cd.getKey();
                IODevs source, dest;
                if (src.equals(parent.getName())) {
                    source = parent;
                } else {
                    source = parent.withName(src);
                }
                if (dst.equals(parent.getName())) {
                    dest = parent;
                } else {
                    dest = parent.withName(dst);
                }
                this.couplingRemoved(source, (String) cs.getValue(), dest, (String) cd.getValue());
            }
            this.modelRemoved(iod, parent);
            DigraphView parentView = parent.getDigraphView();
            if (iod instanceof ViewableAtomic) {
                ViewableAtomic atomic = (ViewableAtomic) iod;
                atomic.createView(modelView);
                AtomicView view1 = atomic.getAtomicView();
                addView(view1, parentView);
            }
            if (iod instanceof ViewableDigraph) {
                ViewableDigraph digraph = (ViewableDigraph) iod;
                createViews(digraph, parentView);
            }
            repaint();
        }

        public void savedModelViewCouplingsAdd(ViewableComponent iod, couprel savedCr) {
            couprel mc = savedCr;
            Iterator it = mc.iterator();
            ViewableDigraph parent = null;
            if (iod instanceof ViewableAtomic) {
                parent = ((ViewableAtomic) iod).getMyParent();
            } else if (iod instanceof ViewableDigraph) {
                parent = ((ViewableDigraph) iod).getMyParent();
            }
            while (it.hasNext()) {
                Pair pr = (Pair) it.next();
                Pair cs = (Pair) pr.getKey();
                Pair cd = (Pair) pr.getValue();
                String src = (String) cs.getKey();
                String dst = (String) cd.getKey();
                IODevs source, dest;
                if (src.equals(parent.getName())) {
                    source = parent;
                } else {
                    source = parent.withName(src);
                }
                if (dst.equals(parent.getName())) {
                    dest = parent;
                } else {
                    dest = parent.withName(dst);
                }
                this.couplingAdded(source, (String) cs.getValue(), dest, (String) cd.getValue());
            }
        }

        /**
         * See ViewableAtomicSimulator.Listener interface method.
         */
        public void couplingAddedToContentPath(content oldContent, devs destComponent, String destPortName, content newContent, String sourceComponentName) {
            if (!(destComponent instanceof ViewableComponent)) return;
            if (!stepMode) return;
            ContentPathStep step = new ContentPathStep();
            step.view = ((ViewableComponent) destComponent).getView();
            step.portName = destPortName;
            ContentPathKey key = new ContentPathKey(oldContent, sourceComponentName);
            List path = (List) contentPathMap.get(key);
            if (path == null) return;
            ContentViewMovement movement = (ContentViewMovement) contentViewMovementMap.remove(path);
            path = new ArrayList(path);
            path.add(step);
            key = new ContentPathKey(newContent, destComponent.getName());
            contentPathMap.put(key, path);
            if (movement != null) {
                movement.path = path;
                contentViewMovementMap.put(path, movement);
            } else {
                ContentView view = new ContentView(newContent);
                view.setVisible(false);
                add(view, new Integer(3));
                contentViews.add(view);
                view.setSize(view.getPreferredSize());
                DragViewListener listener = new DragViewListener(view, this);
                view.addMouseListener(listener);
                view.addMouseMotionListener(listener);
                movement = new ContentViewMovement();
                movement.view = view;
                movement.path = path;
                moveContentViewThread.addContentViewMovement(movement);
                contentViewMovementMap.put(path, movement);
            }
        }

        /**
         * Sizes this model-view to be just larger than the size of its
         * top-level model.
         */
        public Dimension getPreferredSize() {
            if (model == null || model.getView() == null) {
                return super.getPreferredSize();
            }
            Dimension size = ((JComponent) model.getView()).getSize();
            return new Dimension(size.width + modelBorderWidth * 10, size.height + modelBorderWidth * 10);
        }

        /**
         * Together, these force this model-view's size to be its
         * preferred size.
         */
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        /**
         * Removes from this model-view each of the content-views
         * it is displaying.
         */
        protected void removeContentViews() {
            for (int i = 0; i < contentViews.size(); i++) {
                ContentView view = (ContentView) contentViews.get(i);
                remove(view);
            }
            contentViews.clear();
            repaint();
        }

        /**
         * Informs this model-view that the mouse has entered a port-box
         * on one of the model's components.
         */
        public void mouseEnteredPort() {
            showCouplings = true;
            repaint();
        }

        /**
         * Informs this model-view that the mouse has exited a port-box
         * on one of the model's components.
         */
        public void mouseExitedPort() {
            showCouplings = false;
            repaint();
        }

        public void modelAdded(ViewableComponent iod, ViewableDigraph parent) {
            DigraphView parentView = parent.getDigraphView();
            if (parent.isBlackBox() || parent.isHidden()) {
                iod.setHidden(true);
            }
            if (iod instanceof ViewableAtomic) {
                ViewableAtomic atomic = (ViewableAtomic) iod;
                atomic.createView(modelView);
                AtomicView view1 = atomic.getAtomicView();
                addView(view1, parentView);
            }
            if (iod instanceof ViewableDigraph) {
                ViewableDigraph digraph = (ViewableDigraph) iod;
                createViews(digraph, parentView);
                detmCouplings((ViewableDigraph) iod);
            }
            modelView.repaint();
        }

        public void modelRemoved(ViewableComponent iod, ViewableDigraph parent) {
            DigraphView parentView = parent.getDigraphView();
            if (iod instanceof ViewableAtomic) {
                removeView(((ViewableAtomic) iod).getAtomicView(), parentView);
            } else if (iod instanceof ViewableDigraph) {
                destroyModelView((ViewableDigraph) iod, parentView);
            }
            modelView.repaint();
        }

        public void destroyModelView(ViewableDigraph model, ComponentView parentView) {
            String srcName, destName, compName = "";
            Iterator i = model.getComponents().iterator();
            while (i.hasNext()) {
                Object component = i.next();
                if (component instanceof ViewableAtomic) {
                    compName = ((ViewableAtomic) component).getName();
                    removeView(((ViewableAtomic) component).getView(), (JComponent) (model.getView()));
                }
                if (component instanceof ViewableDigraph) {
                    compName = ((ViewableDigraph) component).getName();
                    destroyModelView((ViewableDigraph) component, model.getView());
                }
                int csize = couplings.size();
                for (int j = csize - 1; j >= 0; j--) {
                    Coupling coupling = (Coupling) couplings.get(j);
                    srcName = (coupling.sourceView).getViewableComponent().getName();
                    destName = (coupling.destView).getViewableComponent().getName();
                    if (srcName.compareTo(compName) == 0 || destName.compareTo(compName) == 0) {
                        couplings.remove(j);
                    }
                }
            }
            removeView(model.getView(), (JComponent) parentView);
        }

        public void couplingAdded(IODevs src, String p1, IODevs dest, String p2) {
            if (!(src instanceof ViewableComponent && dest instanceof ViewableComponent)) {
                System.out.println("Coupling could not be displayed." + "\n\tFrom: " + src.getName() + ", port " + p1 + "\n\tTo: " + dest.getName() + ", port " + p2);
                return;
            }
            Coupling coupling = new Coupling();
            coupling.sourceView = ((ViewableComponent) src).getView();
            coupling.sourcePortName = p1;
            coupling.destView = ((ViewableComponent) dest).getView();
            coupling.destPortName = p2;
            addCoupling(coupling);
            repaint();
        }

        public void couplingRemoved(IODevs src, String p1, IODevs dest, String p2) {
            String srcName, destName;
            ViewableDigraph parent;
            for (int i = 0; i < couplings.size(); i++) {
                Coupling coupling = (Coupling) couplings.get(i);
                srcName = (coupling.sourceView).getViewableComponent().getName();
                ViewableComponent srcView = (coupling.sourceView).getViewableComponent();
                if (srcView instanceof ViewableDigraph) {
                    parent = ((ViewableDigraph) srcView).getMyParent();
                } else {
                    parent = ((ViewableAtomic) srcView).getMyParent();
                }
                destName = (coupling.destView).getViewableComponent().getName();
                ViewableComponent destView = (coupling.destView).getViewableComponent();
                if (srcName.compareTo(src.getName()) == 0 && coupling.sourcePortName.compareTo(p1) == 0 && destName.compareTo(dest.getName()) == 0 && coupling.destPortName.compareTo(p2) == 0) {
                    couplings.remove(i);
                    break;
                }
            }
        }

        /**
         * An object associating a content-view with various information
         * about its movement along a path within the model-view.
         */
        protected class ContentViewMovement {

            /**
             * The content-view to be moved.
             */
            protected ContentView view;

            /**
             * The path of steps along which the content view is to move.
             */
            protected List path;

            /**
             * The index of the current step on the path along which
             * the content view is moving.
             */
            protected int currentStepIndex;

            /**
             * The x and y offsets to take with each step along the current line.
             */
            protected double dx, dy;

            /**
             * The current location of this view along the current line, with more
             * precision than just integer coordinates, so that this view
             * stays centered on the line as it moves.
             */
            protected Point2D.Double location;

            /**
             * How many moves it will take to get to the current destination.
             */
            protected int movesRequired;

            /**
             * How many moves have been made so far towards the current
             * destination.
             */
            protected int movesDone;
        }

        /**
         * Moves each content view in its list along a series of steps
         * (i.e. couplings) to its final destination port.
         */
        protected class MoveContentViewThread extends Thread {

            /**
             * The content view movements this thread is currently performing.
             */
            protected List movements = new ArrayList();

            /**
             * Tells this thread to execute the given content-view movement.
             *
             * @param   movement        The content-view movement to execute.
             */
            public void addContentViewMovement(ContentViewMovement movement) {
                movements.add(movement);
            }

            /**
             * See parent method.
             */
            public void run() {
                while (true) {
                    moveContentViews();
                    Util.sleep(20);
                }
            }

            /**
             * Performs the actual movements of the content views.
             */
            protected void moveContentViews() {
                for (int i = 0; i < movements.size(); i++) {
                    ContentViewMovement movement = (ContentViewMovement) movements.get(i);
                    if (movement.movesDone == 0) {
                        ContentPathStep step = (ContentPathStep) movement.path.get(movement.currentStepIndex);
                        if (step.view.getViewableComponent().isHidden()) {
                            movement.currentStepIndex++;
                            if (movement.currentStepIndex >= movement.path.size()) {
                                discardMovement(movement);
                            }
                            continue;
                        }
                        JComponent view = (JComponent) movement.view;
                        if (movement.currentStepIndex == 0 || movement.location == null) {
                            Point start = modelView.getLocation((JComponent) step.view);
                            PointUtil.translate(start, step.view.getPortLocation(step.portName));
                            int viewX = start.x - view.getWidth() / 2, viewY = start.y - view.getHeight() / 2;
                            view.setLocation(viewX, viewY);
                            view.setVisible(true);
                            movement.location = new Point2D.Double(viewX, viewY);
                            movement.currentStepIndex++;
                            if (movement.currentStepIndex >= movement.path.size()) {
                                discardMovement(movement);
                            }
                            continue;
                        }
                        Point start = new Point((int) movement.location.x + view.getWidth() / 2, (int) movement.location.y + view.getHeight() / 2);
                        Point finish = modelView.getLocation((JComponent) step.view);
                        PointUtil.translate(finish, step.view.getPortLocation(step.portName));
                        movement.location = new Point2D.Double(start.x, start.y);
                        double angle = Math.atan2(finish.y - start.y, finish.x - start.x);
                        movement.dx = speed * Math.cos(angle);
                        movement.dy = speed * Math.sin(angle);
                        Point2D.Double location = movement.location;
                        movement.movesRequired = (int) Math.rint(location.distance(finish) / speed);
                        movement.movesDone = 0;
                        location.x -= view.getWidth() / 2;
                        location.y -= view.getHeight() / 2;
                        view.setLocation((int) Math.rint(location.x), (int) Math.rint(location.y));
                    }
                    if (movement.movesDone < movement.movesRequired) {
                        Point2D.Double location = movement.location;
                        location.x += movement.dx;
                        location.y += movement.dy;
                        movement.view.setLocation((int) Math.rint(location.x), (int) Math.rint(location.y));
                        movement.movesDone++;
                    } else {
                        movement.movesDone = 0;
                        movement.currentStepIndex++;
                        if (movement.currentStepIndex >= movement.path.size()) {
                            discardMovement(movement);
                        }
                    }
                }
            }

            /**
             * Removes the given movement and its associated objects from
             * further consideration, so they may be gc'd.
             *
             * @param   movement        The movement object to discard.
             */
            protected void discardMovement(ContentViewMovement movement) {
                contentPathMap.remove(movement.view.getContent());
                contentViewMovementMap.remove(movement.path);
                movements.remove(movement);
            }
        }

        /**
         * A panel, which should cover the entire model-view, on which are drawn
         * the lines meant to represent the couplings between the components
         * in the model.
         */
        protected class CouplingsPanel extends JPanel {

            public CouplingsPanel() {
                setOpaque(false);
            }

            public void paint(Graphics g) {
                if (showCouplings || alwaysShowCouplings) {
                    g.setColor(Color.lightGray);
                    for (int i = 0; i < couplings.size(); i++) {
                        Coupling coupling = (Coupling) couplings.get(i);
                        if (coupling.sourceView.getViewableComponent().isHidden() || coupling.destView.getViewableComponent().isHidden()) {
                            continue;
                        }
                        Point source = ModelView.this.getLocation((JComponent) coupling.sourceView);
                        PointUtil.translate(source, coupling.sourceView.getPortLocation(coupling.sourcePortName));
                        Point dest = ModelView.this.getLocation((JComponent) coupling.destView);
                        PointUtil.translate(dest, coupling.destView.getPortLocation(coupling.destPortName));
                        g.drawLine(source.x, source.y, dest.x, dest.y);
                    }
                }
            }
        }

        /**
         * Informs this model-view that the simulation has been restarted.
         */
        public void simulationRestarted() {
            removeContentViews();
        }

        /**
         * Returns the location of the given descendant component within
         * this view.
         *
         * @param   component       The descendent component whose location is
         *                          desired.
         * @return                  The location of the component, relative to
         *                          the upper-left corner of this view.
         */
        public Point getLocation(JComponent component) {
            return ComponentUtil.getLocationRelativeToAncestor(component, this);
        }

        /**
         * Adds a coupling to this view's list of couplings.
         *
         * @param   coupling        The coupling to add.
         */
        public void addCoupling(Coupling coupling) {
            couplings.add(coupling);
        }

        /**
         * Clears this view's list of couplings.
         */
        public void clearCouplings() {
            couplings.clear();
        }
    }

    /**
     * Creates an atomic-view for each viewable atomic in the given
     * digraph model, as well as for those recursively found contained
     * within in the given model's children models.  Each new
     * atomic-view is added to the model-view.
     *
     * @param   model       The model for whose atomic components views are
     *                      to be created.
     * @param   parent      The parent component to which the given model's
     *                      views are to be added.
     */
    protected void createViews(ViewableDigraph model, JComponent parent) {
        model.createView(modelView);
        if (!model.isBlackBox()) {
            try {
                if (!model.layoutForSimViewOverride()) {
                    model.layoutForSimView();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DigraphView view = model.getDigraphView();
        modelView.addView(view, parent);
        if (parent == modelView) {
            view.setLocation(new Point(20, 20));
        }
        Iterator i = model.getComponents().iterator();
        while (i.hasNext()) {
            Object component = i.next();
            if (model.isBlackBox() || model.isHidden()) {
                if (component instanceof ViewableComponent) {
                    ViewableComponent comp = (ViewableComponent) component;
                    comp.setHidden(true);
                }
            }
            if (component instanceof ViewableAtomic) {
                ViewableAtomic atomic = (ViewableAtomic) component;
                atomic.createView(modelView);
                AtomicView view1 = atomic.getAtomicView();
                modelView.addView(view1, view);
                if (model.getName().equals(wrapperDigraphName)) {
                    view1.setLocation(modelViewScrollPane.getWidth() / 2 - view.getWidth() / 2, modelViewScrollPane.getHeight() / 2 - view.getHeight() / 2);
                }
            }
            if (component instanceof ViewableDigraph) {
                ViewableDigraph digraph = (ViewableDigraph) component;
                createViews(digraph, view);
            }
        }
    }

    /**
     * Creates an instance of the model class of the given name (including
     * the package name).  Also, creates the views and coordinator for the
     * model instance.
     *
     * @param   name        The name of the model class to instantiate.
     */
    public void useModelClass(FModel rootModel, String sourcePath) {
        model = ((FCoupledModel) rootModel).getModel();
        this.sourcePath = sourcePath;
        modelView.removeAllViews();
        createViews(model, modelView);
        modelView.clearCouplings();
        detmCouplings(model);
        ((JComponent) modelViewScrollPane.getParent()).revalidate();
    }

    /**
     * A coupling between a source port on one devs component view, and
     * a destination port on another.
     */
    protected class Coupling {

        /**
         * The source and destination component views.
         */
        public ComponentView sourceView, destView;

        /**
         * The source and destination port names.
         */
        public String sourcePortName, destPortName;
    }

    /**
     * Determines all the couplings present within the given model, and adds
     * them to the model-view for display.
     *
     * @param   model       The model for which to find all the couplings.
     */
    protected void detmCouplings(ViewableDigraph model) {
        detmCouplings((ViewableComponent) model);
        Iterator i = model.getComponents().iterator();
        while (i.hasNext()) {
            Object component = i.next();
            if (component instanceof ViewableDigraph) {
                ViewableDigraph digraph = (ViewableDigraph) component;
                detmCouplings(digraph);
            } else if (component instanceof ViewableComponent) {
                detmCouplings((ViewableComponent) component);
            }
        }
    }

    /**
     * Determines all the couplings for which a port on the given component
     * is the source, and adds those couplings to the model-view for display.
     *
     * @param   comp       The devs component whose couplings are to be found.
     */
    protected void detmCouplings(ViewableComponent comp) {
        detmCouplings(comp, comp.getOutportNames());
        if (comp instanceof ViewableDigraph) {
            detmCouplings(comp, comp.getInportNames());
        }
    }

    /**
     * Determines all the couplings for which a port from the given list
     * (on the given component) is the source, and adds those couplings
     * to the model-view for display.
     *
     * @param   comp        The devs component to which the ports belong.
     * @param   portNames   The list of port names on the above component
     *                      to look for couplings.
     */
    protected void detmCouplings(ViewableComponent comp, List portNames) {
        for (int i = 0; i < portNames.size(); i++) {
            String portName = (String) portNames.get(i);
            List couplings = null;
            if (comp instanceof ViewableAtomic) {
                couplings = ((coupledSimulator) ((atomic) comp).getSim()).getCouplingsToSourcePort(portName);
            } else if (comp instanceof ViewableDigraph) {
                couplings = ((digraph) comp).getCoordinator().getCouplingsToSourcePort(portName);
            }
            for (int j = 0; j < couplings.size(); j++) {
                Pair pair = (Pair) couplings.get(j);
                Coupling coupling = new Coupling();
                coupling.sourceView = comp.getView();
                coupling.sourcePortName = portName;
                entity destEntity = (entity) pair.getKey();
                String destPortName = (String) pair.getValue();
                if (!(destEntity instanceof ViewableComponent)) {
                    System.out.println("Coupling could not be displayed." + "\n\tFrom: " + comp.getName() + ", port " + portName + "\n\tTo: " + destEntity.getName() + ", port " + destPortName);
                    continue;
                }
                coupling.destView = ((ViewableComponent) destEntity).getView();
                coupling.destPortName = destPortName;
                modelView.addCoupling(coupling);
            }
        }
    }

    /**
     * A step in a content-path list of such steps.
     */
    protected class ContentPathStep {

        /**
         * At which view the content should be displayed for this step.
         */
        public ComponentView view;

        /**
         * At which port on the above view the content should be
         * displayed for this step.
         */
        public String portName;
    }

    /**
     * Returns this sim-view's model-view.
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * A key used to find a particular content-path in the content-path-map.
     */
    protected class ContentPathKey {

        /**
         * The latest incarnation of the content traversing the path.
         */
        ContentInterface content;

        /**
         * The current component the above content has reached in its path.
         */
        String componentName;

        /**
         * Constructs a key.
         */
        public ContentPathKey(ContentInterface content_, String componentName_) {
            content = content_;
            componentName = componentName_;
        }

        /**
         * Equates the given object to this key if it is of the same class,
         * if its content port and values match this key's,
         * and if its component name matches this key's.
         */
        public boolean equals(Object o) {
            if (o instanceof ContentPathKey) {
                ContentPathKey pair = (ContentPathKey) o;
                return content.equals(pair.content) && componentName.equals(pair.componentName);
            }
            return false;
        }

        /**
         * Returns a hash-code for this key that is based on a combination
         * of its component-name and its content's port-name and value.
         */
        public int hashCode() {
            return (componentName + content.getPort() + content.getValue()).hashCode();
        }
    }

    /**
     * Saves the current top-level model's layout (within this sim-view)
     * to its source code file.
     */
    public void saveModelLayout() {
        saveModelLayout(model);
    }

    protected void saveModelLayout(ViewableDigraph model) {
        System.out.print("DSOAD Simple version. Please do not save layout.\\n");
        return;
    }

    /**
     * Saves the given model's layout (within this sim-view)
     * to its source code file.
     */
    protected void saveModelLayout_org(ViewableDigraph model) {
        if (model == null || model.isBlackBox() || model.isHidden()) return;
        if (!model.getLayoutChanged()) {
            saveLayoutsOfChildren(model);
            return;
        }
        String className = model.getClass().getName().replace('.', '/');
        File file = new File(sourcePath + "/" + className + ".java");
        String code = FileUtil.getContentsAsString(file);
        int index = code.indexOf("void layoutForSimView()");
        if (index != -1) {
            int startIndex = code.lastIndexOf("/**", index);
            startIndex = code.lastIndexOf("\n", startIndex);
            int endIndex = code.indexOf("}", index);
            endIndex = code.indexOf("\n", endIndex);
            code = code.substring(0, startIndex) + code.substring(endIndex, code.length());
        }
        index = code.lastIndexOf("}");
        index = code.lastIndexOf("\n", index);
        StringBuffer method = new StringBuffer("\n");
        method.append("    /**\n");
        method.append("     * Automatically generated by the SimView program.\n");
        method.append("     * Do not edit this manually, as such changes will get overwritten.\n");
        method.append("     */\n");
        method.append("    public void layoutForSimView()\n");
        method.append("    {\n");
        if (!model.isBlackBox()) {
            method.append("        preferredSize = new Dimension(");
            Dimension size = ((JComponent) model.getView()).getSize();
            method.append(size.width);
            method.append(", ");
            method.append(size.height);
            method.append(");\n");
        }
        Iterator i = model.getComponents().iterator();
        while (i.hasNext()) {
            Object next = i.next();
            if (!(next instanceof ViewableComponent)) continue;
            ViewableComponent component = (ViewableComponent) next;
            if (component.isHidden()) continue;
            method.append("        ");
            method.append("((ViewableComponent)withName(" + component.getLayoutName());
            method.append(")).setPreferredLocation(new Point(");
            Point location = component.getPreferredLocation();
            method.append(location.x);
            method.append(", ");
            method.append(location.y);
            method.append("));\n");
        }
        method.append("    }\n");
        code = code.substring(0, index) + method + code.substring(index + 1, code.length());
        file = new File(file.getPath());
        if (model.getComponents().size() != 1) {
            try {
                FileWriter fw = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(fw);
                out.write(code, 0, code.length());
                out.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        model.setLayoutChanged(false);
        saveLayoutsOfChildren(model);
    }

    /**
     * Saves the layouts (within this sim-view) of the children components
     * of the given model to their associated source code files.
     */
    protected void saveLayoutsOfChildren(ViewableDigraph model) {
        Iterator i = model.getComponents().iterator();
        while (i.hasNext()) {
            Object next = i.next();
            if (!(next instanceof ViewableComponent)) continue;
            ViewableComponent component = (ViewableComponent) next;
            if (component instanceof ViewableDigraph) {
                ViewableDigraph digraph = (ViewableDigraph) component;
                saveModelLayout(digraph);
            }
        }
    }
}
