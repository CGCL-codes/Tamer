package org.adapit.wctoolkit.fomda.diagram.transformers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.adapit.wctoolkit.fomda.diagram.transformers.action.AddToTransformationDiagramAction;
import org.adapit.wctoolkit.fomda.diagram.transformers.toolbar.TransformerDiagramToolBarController;
import org.adapit.wctoolkit.fomda.features.view.FeatureRelationViewComponent;
import org.adapit.wctoolkit.fomda.features.view.FeatureViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.ExpressionBlockViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.SharedVarViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.SimplePropertyViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.SpecializationPointViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.TransformationDependenceViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.TransformationDescriptorComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.TransformationParameterViewComponent;
import org.adapit.wctoolkit.fomda.features.view.transformation.TransformerViewComponent;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.diagram.AbstractDiagramInternalFrame;
import org.adapit.wctoolkit.infrastructure.treecontrollers.DefaultElementTreeController;
import org.adapit.wctoolkit.models.config.InnerXMLExporter;
import org.adapit.wctoolkit.models.diagram.AbstractGraphicComponent;
import org.adapit.wctoolkit.models.util.AllElements;
import org.adapit.wctoolkit.models.util.ObserverMutableTreeNode;
import org.adapit.wctoolkit.models.util.ITreeDisplayable;
import org.adapit.wctoolkit.uml.ext.core.ElementImpl;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.FomdaModel;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.FeatureElement;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.features.ParameterDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.ExpressionBlock;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.SharedVariable;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.SimpleProperty;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.SpecializationPoint;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationDescriptor;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationParameter;
import org.adapit.wctoolkit.uml.ext.fomda.metamodel.transformation.TransformationParameter.TransformationParameterKind;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@SuppressWarnings({ "unchecked" })
public class TransformerDiagramPainterInternalFrame extends AbstractDiagramInternalFrame implements ITreeDisplayable, InnerXMLExporter, java.lang.Comparable {

    private static final long serialVersionUID = 2346889347486L;

    public static org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile messages = org.adapit.wctoolkit.fomda.features.i18n.I18N_FomdaProfile.getInstance();

    protected String operation = "";

    protected int x, y;

    static int diagramNumber = 1;

    protected HashSet<TransformerViewComponent> transformerViewComponents = new TransformerViewComponentHashSet(this);

    protected HashSet<SharedVarViewComponent> sharedVarViewComponents = new HashSet<SharedVarViewComponent>();

    protected HashSet<FeatureRelationViewComponent> featureRelationViewComponents = new HashSet<FeatureRelationViewComponent>();

    protected HashSet<FeatureViewComponent> featureViewComponents = new HashSet<FeatureViewComponent>();

    protected Vector<TransformerViewComponent> startPoints;

    public TransformerViewComponent getStartPoint() {
        if (startPoints == null || startPoints.size() == 0) return null;
        return startPoints.get(0);
    }

    public void setStartPoint(TransformerViewComponent tvc) {
        if (startPoints == null) startPoints = new Vector<TransformerViewComponent>();
        if (startPoints.contains(tvc)) startPoints.remove(tvc);
        startPoints.add(0, tvc);
    }

    public void setSequenceOrder(TransformerViewComponent tvc) {
        if (startPoints == null) startPoints = new Vector<TransformerViewComponent>();
        if (startPoints.size() == 0) startPoints.add(0, tvc); else {
            int size = startPoints.size();
            int position = 0;
            position = getSequenceOrder(tvc);
            if (startPoints.contains(tvc)) startPoints.remove(tvc); else size++;
            String values[] = new String[size];
            for (int i = 0; i < values.length; i++) values[i] = (i + 1) + "";
            String order = (String) JOptionPane.showInputDialog(DefaultApplicationFrame.getInstance(), messages.getMessage("In_which_position_you_want_to_allocate_the_selected_item"), messages.getMessage("Position_the_component"), JOptionPane.QUESTION_MESSAGE, null, values, values[position]);
            if (order != null && !order.equals("")) {
                int newPos = (Integer.parseInt(order) - 1);
                startPoints.add(newPos, tvc);
            }
        }
    }

    public int getSequenceOrder(TransformerViewComponent tvc) {
        int position = 0;
        if (startPoints.contains(tvc)) {
            for (TransformerViewComponent t : startPoints) {
                if (t == tvc) break;
                position++;
            }
        }
        return position;
    }

    public void removeFromSequence(TransformerViewComponent tvc) {
        if (startPoints == null || !startPoints.contains(tvc)) return;
        startPoints.remove(tvc);
    }

    public enum EditorMode {

        READ_ONLY, FULL
    }

    ;

    private EditorMode mode = EditorMode.FULL;

    public TransformerDiagramPainterInternalFrame(String name) throws Exception {
        super(name);
        try {
            this.setName(name);
            if (DefaultApplicationFrame.getInstance().getDefaultContentPane() != null && DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController() != null) {
                defaultElementTreeControllerObservers.add(DefaultApplicationFrame.getInstance().getDefaultContentPane().getDefaultElementTreeController());
            }
            add(getPaintScrolling(), BorderLayout.CENTER);
            add(getTransformerDiagramToolBar().getToolBar(), BorderLayout.NORTH);
            DefaultApplicationFrame.getInstance().addKeyListener((KeyListener) getPaintPanel());
            initialize();
            elementImpl.setIcon("/img/cog.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TransformerDiagramPainterInternalFrame() throws Exception {
        super(messages.getMessage("Composed_Transformer") + " " + diagramNumber++);
        try {
            add(getPaintScrolling(), BorderLayout.CENTER);
            add(getTransformerDiagramToolBar().getToolBar(), BorderLayout.NORTH);
            try {
                if (getPaintPanel() instanceof KeyListener) DefaultApplicationFrame.getInstance().addKeyListener((KeyListener) getPaintPanel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            initialize();
            elementImpl.setIcon("/img/cog.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TransformerDiagramToolBarController transformerDiagramToolBar;

    public TransformerDiagramToolBarController getTransformerDiagramToolBar() {
        if (transformerDiagramToolBar == null) {
            transformerDiagramToolBar = new TransformerDiagramToolBarController(this);
        }
        return transformerDiagramToolBar;
    }

    public TransformerDiagramToolBarController resetToolBar() {
        remove(getTransformerDiagramToolBar().getToolBar());
        transformerDiagramToolBar = null;
        add(getTransformerDiagramToolBar().getToolBar(), BorderLayout.NORTH);
        updateUI();
        return transformerDiagramToolBar;
    }

    protected DefaultElementTreeController treeCtrl;

    protected TransformerMouseMotionListener treeListener;

    private void registerTreeListener() {
        try {
            try {
                if (treeCtrl == null) treeCtrl = DefaultApplicationFrame.getSelectedElementTreeController();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (treeListener == null) treeListener = new TransformerMouseMotionListener();
            if (treeCtrl != null) {
                treeCtrl.getTree().addMouseMotionListener(treeListener);
                treeCtrl.getTree().addMouseListener(treeListener);
                addInternalFrameListener(new InternalFrameAdapter() {

                    @Override
                    public void internalFrameClosed(InternalFrameEvent arg0) {
                        try {
                            treeCtrl.getTree().removeMouseMotionListener(treeListener);
                            treeCtrl.getTree().removeMouseListener(treeListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        registerTreeListener();
    }

    private AddToTransformationDiagramAction addTransf = new AddToTransformationDiagramAction(this);

    public class TransformerMouseMotionListener extends MouseAdapter implements MouseMotionListener {

        private boolean dragged = false;

        private Cursor oldCursor = DefaultApplicationFrame.getInstance().getCursor();

        @SuppressWarnings("deprecation")
        @Override
        public void mouseDragged(MouseEvent evt) {
            try {
                dragged = true;
                Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
                d.setSize(d.getWidth(), d.getHeight() - 30);
                setMx((int) (evt.getX() - (d.getWidth() - getWidth())));
                setMy(evt.getY() - 100);
                DefaultApplicationFrame.getInstance().setCursor(Cursor.HAND_CURSOR);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            } catch (HeadlessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            try {
                if (dragged) addTransf.actionPerformed(null);
                dragged = false;
                toFront();
                updateUI();
                DefaultApplicationFrame.getInstance().setCursor(oldCursor);
                setCursor(oldCursor);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    protected JScrollPane paintScrolling;

    public JScrollPane getPaintScrolling() {
        if (paintScrolling == null) {
            paintScrolling = new JScrollPane();
            paintScrolling.add(getPaintPanel());
            getPaintPanel().setPreferredSize(new Dimension(2000, 2000));
            paintScrolling.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            paintScrolling.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            paintScrolling.setViewportView(getPaintPanel());
        }
        return paintScrolling;
    }

    protected TransformerDiagramPaintPanel paintPanel;

    protected JPopupMenu popup;

    private int mx = 0;

    private int my = 0;

    private JPopupMenu getPopup() {
        if (popup == null) {
            popup = new TransformerDiagramPopupMenu(this);
        }
        return popup;
    }

    protected TransformerViewComponent addCascadingParameter(int mx, int my, ParameterDescriptor element) {
        TransformationDescriptor td = (TransformationDescriptor) element.getParentElement();
        TransformerViewComponent tvc = null;
        TransformerViewComponent elvc = null;
        int cont = 100;
        try {
            tvc = new TransformerViewComponent(td);
            tvc.setLocation(mx, my);
            tvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
            tvc.setDrawingPanel(getPaintPanel());
            addGraphicComponent(tvc);
            if (td.getSharedVariables() != null && td.getSharedVariables().size() > 0) {
                Iterator<SharedVariable> it = td.getSharedVariables().values().iterator();
                while (it.hasNext()) {
                    cont += 10;
                    SharedVariable sv = it.next();
                    try {
                        SharedVarViewComponent svc = new SharedVarViewComponent(td, sv);
                        if (sv == element) elvc = svc;
                        svc.setTransformerViewComponent(tvc);
                        tvc.setNextLocation(svc);
                        svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                        svc.setDrawingPanel(getPaintPanel());
                        addGraphicComponent(svc);
                        tvc.getChildrenViewComponents().add(svc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (td.getParameters() != null && td.getParameters().size() > 0) {
                Iterator<TransformationParameter> it = td.getParameters().values().iterator();
                while (it.hasNext()) {
                    cont += 10;
                    TransformationParameter sv = it.next();
                    try {
                        TransformationParameterViewComponent svc = new TransformationParameterViewComponent(td, sv);
                        if (sv == element) elvc = svc;
                        svc.setTransformerViewComponent(tvc);
                        tvc.setNextLocation(svc);
                        svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                        svc.setDrawingPanel(getPaintPanel());
                        addGraphicComponent(svc);
                        tvc.getChildrenViewComponents().add(svc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (td.getSpecializationPoints() != null && td.getSpecializationPoints().size() > 0) {
                Iterator<SpecializationPoint> it = td.getSpecializationPoints().values().iterator();
                int i = 1;
                cont = -80;
                while (it.hasNext()) {
                    cont += 30;
                    SpecializationPoint sp = it.next();
                    try {
                        SpecializationPointViewComponent svc = new SpecializationPointViewComponent(sp, tvc, i);
                        if (sp == element) elvc = svc;
                        svc.setTransformerViewComponent(tvc);
                        tvc.setNextLocation(svc);
                        svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                        svc.setDrawingPanel(getPaintPanel());
                        addGraphicComponent(svc);
                        tvc.getChildrenViewComponents().add(svc);
                        addExpressionBlockToDiagram(sp, svc, tvc);
                        addSimplePropertyToDiagram(sp, svc, tvc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elvc;
    }

    protected TransformerViewComponent setIntoTransformerViewComponent(TransformerViewComponent tvc, ParameterDescriptor element) throws Exception {
        TransformationDescriptor td = (TransformationDescriptor) element.getParentElement();
        TransformerViewComponent elvc = null;
        int cont = 100;
        if (element instanceof SharedVariable && td.getSharedVariables() != null && td.getSharedVariables().size() > 0) {
            Iterator<SharedVariable> it = td.getSharedVariables().values().iterator();
            while (it.hasNext()) {
                cont += 10;
                SharedVariable sv = it.next();
                try {
                    SharedVarViewComponent svc = new SharedVarViewComponent(td, sv);
                    if (sv == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (element instanceof TransformationParameter && td.getParameters() != null && td.getParameters().size() > 0) {
            Iterator<TransformationParameter> it = td.getParameters().values().iterator();
            while (it.hasNext()) {
                cont += 10;
                TransformationParameter sv = it.next();
                try {
                    TransformationParameterViewComponent svc = new TransformationParameterViewComponent(td, sv);
                    if (sv == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (element instanceof SpecializationPoint && td.getSpecializationPoints() != null && td.getSpecializationPoints().size() > 0) {
            Iterator<SpecializationPoint> it = td.getSpecializationPoints().values().iterator();
            int i = 1;
            cont = -80;
            while (it.hasNext()) {
                cont += 30;
                SpecializationPoint sp = it.next();
                try {
                    SpecializationPointViewComponent svc = new SpecializationPointViewComponent(sp, tvc, i);
                    if (sp == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                    addExpressionBlockToDiagram(sp, svc, tvc);
                    addSimplePropertyToDiagram(sp, svc, tvc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        return elvc;
    }

    public TransformerViewComponent addTransformerAndAssociations(int mx, int my, TransformationDescriptor td) {
        TransformerViewComponent tvc = null;
        try {
            tvc = new TransformerViewComponent(td);
            tvc.setLocation(mx, my);
            tvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
            tvc.setDrawingPanel(getPaintPanel());
            addGraphicComponent(tvc);
            if (td.getParameters() != null && td.getParameters().size() > 0) {
                int cont = -100;
                for (TransformationParameter tp : td.getParameters().values()) {
                    TransformationParameterViewComponent tpvc = new TransformationParameterViewComponent(td, tp);
                    tpvc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(tpvc);
                    cont += 100;
                    addGraphicComponent(tpvc);
                }
            }
            if (td.getOutputParameters() != null && td.getOutputParameters().size() > 0) {
                int cont = -100;
                for (TransformationParameter tp : td.getOutputParameters().values()) {
                    TransformationParameterViewComponent tpvc = new TransformationParameterViewComponent(td, tp);
                    tpvc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(tpvc);
                    cont += 100;
                    addGraphicComponent(tpvc);
                }
            }
            if (td.getRequiredFeatures() != null && td.getRequiredFeatures().size() > 0) {
                int cont = -100;
                for (FeatureElement fe : td.getRequiredFeatures()) {
                    TransformationDependenceViewComponent tdvc = new TransformationDependenceViewComponent(td, fe, false);
                    tdvc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(tdvc);
                    cont += 100;
                    addGraphicComponent(tdvc);
                }
            }
            if (td.getExclusionFeatures() != null && td.getExclusionFeatures().size() > 0) {
                int cont = -100;
                for (FeatureElement fe : td.getExclusionFeatures()) {
                    TransformationDependenceViewComponent tdvc = new TransformationDependenceViewComponent(td, fe, true);
                    tdvc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(tdvc);
                    cont += 100;
                    addGraphicComponent(tdvc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tvc;
    }

    public TransformerViewComponent addTransformerAndParameter(int mx, int my, ParameterDescriptor element) {
        TransformationDescriptor td = (TransformationDescriptor) element.getParentElement();
        TransformerViewComponent tvc = null;
        TransformerViewComponent elvc = null;
        try {
            tvc = new TransformerViewComponent(td);
            tvc.setLocation(mx, my);
            tvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
            tvc.setDrawingPanel(getPaintPanel());
            addGraphicComponent(tvc);
            elvc = setIntoTransformerViewComponent(tvc, element);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return elvc;
    }

    public TransformerViewComponent addTransformer(int mx, int my, TransformationDescriptor td) {
        TransformerViewComponent tvc = null;
        try {
            tvc = new TransformerViewComponent(td);
            tvc.setLocation(mx, my);
            tvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
            tvc.setDrawingPanel(getPaintPanel());
            addGraphicComponent(tvc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tvc;
    }

    public TransformerDiagramPaintPanel getPaintPanel() {
        if (paintPanel == null) {
            paintPanel = new TransformerDiagramPaintPanel(this);
            paintPanel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseReleased(MouseEvent evt) {
                    try {
                        if (evt.getButton() == MouseEvent.BUTTON3) {
                            setMx(evt.getX());
                            setMy(evt.getY());
                            Iterator<TransformerViewComponent> it = transformerViewComponents.iterator();
                            while (it.hasNext()) {
                                TransformerViewComponent fvc = it.next();
                                if (fvc.isInArea(getMx(), getMy())) return;
                            }
                            getPopup().show(TransformerDiagramPainterInternalFrame.this, getMx() + 8, getMy() + 40);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return paintPanel;
    }

    protected class PopupListener extends MouseAdapter {

        JPopupMenu popup;

        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }

        public void mousePressed(MouseEvent e) {
            try {
                x = e.getX();
                y = e.getY();
                showPopup(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        public void mouseReleased(MouseEvent e) {
            try {
                x = e.getX();
                y = e.getY();
                showPopup(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        private void showPopup(MouseEvent e) {
            try {
                if (e.isPopupTrigger()) {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ObserverMutableTreeNode getNode() {
        try {
            elementImpl.setNode(new ObserverMutableTreeNode(this));
            return elementImpl.getNode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void notifyElementChanged() {
        checkDependecies();
    }

    public AbstractGraphicComponent addElement(AbstractGraphicComponent el) {
        try {
            if (el instanceof TransformerViewComponent) {
                if (transformerViewComponents == null) transformerViewComponents = new HashSet<TransformerViewComponent>();
                transformerViewComponents.add((TransformerViewComponent) el);
                ((TransformerViewComponent) el).setDiagram(this);
                return el;
            } else if (el instanceof FeatureRelationViewComponent) {
                if (featureRelationViewComponents == null) featureRelationViewComponents = new HashSet<FeatureRelationViewComponent>();
                featureRelationViewComponents.add((FeatureRelationViewComponent) el);
                return el;
            } else if (el instanceof SharedVarViewComponent) {
                if (sharedVarViewComponents == null) sharedVarViewComponents = new HashSet<SharedVarViewComponent>();
                sharedVarViewComponents.add((SharedVarViewComponent) el);
                ((SharedVarViewComponent) el).setDiagram(this);
                return el;
            }
            try {
                return addGraphicComponent(el);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return el;
    }

    public void reset(DefaultElementTreeController treeObserver, String title) {
        try {
            super.reset(title);
            defaultElementTreeControllerObservers.add(treeObserver);
            setBackground(Color.WHITE);
            this.setLayout(null);
            setIcon("/img/FeaturesDiagram.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ObserverMutableTreeNode createNodes() {
        try {
            ObserverMutableTreeNode node = super.createNodes();
            node.setUserObject(this);
            node.setElement(this);
            return node;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public HashSet<FeatureRelationViewComponent> getFeatureRelationViewComponents() {
        return featureRelationViewComponents;
    }

    public HashSet<TransformerViewComponent> getFeatureViewComponents() {
        return transformerViewComponents;
    }

    @Override
    public AbstractGraphicComponent addGraphicComponent(AbstractGraphicComponent component) throws Exception {
        try {
            if (component instanceof TransformerViewComponent) {
                transformerViewComponents.add((TransformerViewComponent) component);
                ((TransformerViewComponent) component).setDiagram(this);
                ((TransformerViewComponent) component).setDrawingPanel(getPaintPanel());
                return component;
            } else if (component instanceof FeatureViewComponent) {
                featureViewComponents.add((FeatureViewComponent) component);
                ((FeatureViewComponent) component).setDrawingPanel(getPaintPanel());
                return component;
            } else return super.addGraphicComponent(component);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public HashSet<SharedVarViewComponent> getSharedVarViewComponents() {
        return sharedVarViewComponents;
    }

    public void setSharedVarViewComponents(HashSet<SharedVarViewComponent> sharedVarViewComponents) {
        this.sharedVarViewComponents = sharedVarViewComponents;
    }

    public HashSet<TransformerViewComponent> getTransformerViewComponents() {
        return transformerViewComponents;
    }

    public void setTransformerViewComponents(HashSet<TransformerViewComponent> transformerViewComponents) {
        this.transformerViewComponents = transformerViewComponents;
    }

    protected AbstractGraphicComponent selectedGraphic;

    public AbstractGraphicComponent getSelectedGraphic() {
        return selectedGraphic;
    }

    public void setSelectedGraphic(AbstractGraphicComponent fvc) {
        selectedGraphic = fvc;
    }

    public EditorMode getMode() {
        return mode;
    }

    public void setMode(EditorMode mode) {
        this.mode = mode;
    }

    public String exportXMLForm(int tab) {
        String str = "";
        try {
            str += '\n';
            for (int i = 0; i < tab; i++) {
                str += '\t';
            }
            str += "<Diagram " + getExportAttributes() + " type=\"" + getClass().getName() + "\" >";
            try {
                str += super.exportXMI1_2AsString(tab + 1);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                if (graphicComponents != null && graphicComponents.size() > 0) {
                    Iterator it = graphicComponents.iterator();
                    while (it.hasNext()) {
                        AbstractGraphicComponent i = (AbstractGraphicComponent) it.next();
                        str += i.exportXMLForm(tab + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (innerExportations != null && innerExportations.size() > 0) {
                    Iterator it = innerExportations.iterator();
                    while (it.hasNext()) {
                        InnerXMLExporter i = (InnerXMLExporter) it.next();
                        str += i.getInnerXml(tab + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
                    Iterator it = transformerViewComponents.iterator();
                    while (it.hasNext()) {
                        TransformerViewComponent i = (TransformerViewComponent) it.next();
                        str += i.exportXMLForm(tab + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (sharedVarViewComponents != null && sharedVarViewComponents.size() > 0) {
                    Iterator it = sharedVarViewComponents.iterator();
                    while (it.hasNext()) {
                        SharedVarViewComponent i = (SharedVarViewComponent) it.next();
                        str += i.exportXMLForm(tab + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (featureRelationViewComponents != null && featureRelationViewComponents.size() > 0) {
                    Iterator it = featureRelationViewComponents.iterator();
                    while (it.hasNext()) {
                        FeatureRelationViewComponent i = (FeatureRelationViewComponent) it.next();
                        str += i.exportXMLForm(tab + 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (startPoints != null) {
                for (TransformerViewComponent startPoint : startPoints) try {
                    str += '\n';
                    for (int i = 0; i < tab + 1; i++) {
                        str += '\t';
                    }
                    str += "<StartPoint idref=\"" + startPoint.getId() + "\"/>";
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (diagramInputParameterList != null) {
                try {
                    for (TransformerViewComponent tvc : diagramInputParameterList) {
                        str += '\n';
                        for (int i = 0; i < tab + 1; i++) {
                            str += '\t';
                        }
                        str += "<InputParameter idref=\"" + tvc.getId() + "\"/>";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (diagramOutputParameters != null) {
                try {
                    for (TransformerViewComponent tvc : diagramOutputParameters) {
                        str += '\n';
                        for (int i = 0; i < tab + 1; i++) {
                            str += '\t';
                        }
                        str += "<OutputParameter idref=\"" + tvc.getId() + "\"/>";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (innerTransformer != null) {
                innerTransformer.setExportTagName("InnerTransformer");
                str += innerTransformer.exportXMLForm(tab + 1);
            }
            str += '\n';
            for (int i = 0; i < tab; i++) {
                str += '\t';
            }
            str += "</Diagram>";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public void importXMLForm(Node node) {
        try {
            try {
                super.importXMI1_2(node);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (node.getNodeName().equals("Diagram")) {
                elementImpl.getImportAttributes(node);
                AllElements.getInstance().getElements().put(getId(), this);
                logger.debug("...Importing Diagram " + elementImpl.getName());
                NodeList nl = node.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node n = nl.item(i);
                    if (n.getNodeName().equals("GraphicComponent")) {
                        String type = n.getAttributes().getNamedItem("type").getNodeValue();
                        if (graphicComponents == null) graphicComponents = new HashSet<AbstractGraphicComponent>();
                        try {
                            logger.debug("...Importing Graphic Model");
                            AbstractGraphicComponent agc = null;
                            Object el = AllElements.getInstance().getElements().get(n.getAttributes().getNamedItem("elementId").getNodeValue());
                            String tdid = null;
                            if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                            TransformationDescriptor td = null;
                            if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                            if (type.equalsIgnoreCase(TransformationParameterViewComponent.class.getSimpleName())) {
                                agc = new TransformationParameterViewComponent(td, (TransformationParameter) el);
                            } else if (type.equalsIgnoreCase(TransformationDescriptorComponent.class.getSimpleName())) {
                                agc = new TransformationDescriptorComponent(td, 0, 0, this);
                            }
                            @SuppressWarnings("unused") String gcid = n.getAttributes().getNamedItem("xmi.id").getNodeValue();
                            if (agc != null) try {
                                agc.importXMLForm(n);
                                addGraphicComponent(agc);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (n.getNodeName().equals("TransformerViewComponent") || n.getNodeName().equals("TransformationParameterViewComponent")) {
                        String tdid = null;
                        if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                        TransformationDescriptor td = null;
                        if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                        try {
                            logger.debug("...Importing TransformationViewComponent");
                            TransformerViewComponent tvc = null;
                            if (n.getNodeName().equals("TransformationParameterViewComponent")) {
                                String elId = n.getAttributes().getNamedItem("elementId").getNodeValue();
                                Object el = AllElements.getInstance().getElements().get(elId);
                                tvc = new TransformationParameterViewComponent(td, (TransformationParameter) el);
                            } else tvc = new TransformerViewComponent(td);
                            if (graphicComponents == null) graphicComponents = new HashSet<AbstractGraphicComponent>();
                            tvc.importXMLForm(n);
                            addElement(tvc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (n.getNodeName().equals("InnerTransformer")) {
                        TransformationDescriptor td = new TransformationDescriptor(this);
                        td.importXMLForm(n);
                        innerTransformer = td;
                    } else if (n.getNodeName().equals("StartPoint")) {
                        String idref = n.getAttributes().getNamedItem("idref").getNodeValue();
                        startPointIds.add(idref);
                    } else if (n.getNodeName().equals("InputParameter")) {
                        String idref = n.getAttributes().getNamedItem("idref").getNodeValue();
                        if (diagramInputParameterIds == null) diagramInputParameterIds = new Vector<String>();
                        diagramInputParameterIds.add(idref);
                    } else if (n.getNodeName().equals("OutputParameter")) {
                        String idref = n.getAttributes().getNamedItem("idref").getNodeValue();
                        if (diagramOutputParameterIds == null) diagramOutputParameterIds = new Vector<String>();
                        diagramOutputParameterIds.add(idref);
                    } else if (n.getNodeName().equals("FeatureRelationViewComponent")) {
                    } else if (n.getNodeName().equals("SharedVarViewComponent")) {
                        String tdid = null;
                        if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                        TransformationDescriptor td = null;
                        SharedVariable sv = null;
                        if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                        if (n.getAttributes().getNamedItem("sharedVar.id") != null) {
                            tdid = n.getAttributes().getNamedItem("sharedVar.id").getNodeValue();
                            if (tdid != null) sv = (SharedVariable) AllElements.getElementById(tdid);
                        }
                        try {
                            logger.debug("...Importing SharedVarViewComponent");
                            SharedVarViewComponent tvc = new SharedVarViewComponent(td, sv);
                            tvc.importXMLForm(n);
                            addElement(tvc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (n.getNodeName().equals("ExpressionBlockViewComponent")) {
                        String tdid = null;
                        if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                        TransformationDescriptor td = null;
                        if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                        try {
                            logger.debug("...Importing ExpressionBlockViewComponent");
                            ExpressionBlockViewComponent tvc = new ExpressionBlockViewComponent();
                            tvc.importXMLForm(n);
                            addElement(tvc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (n.getNodeName().equals("SimplePropertyViewComponent")) {
                        String tdid = null;
                        if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                        TransformationDescriptor td = null;
                        if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                        try {
                            logger.debug("...Importing SimplePropertyViewComponent");
                            SimplePropertyViewComponent tvc = new SimplePropertyViewComponent();
                            tvc.importXMLForm(n);
                            addElement(tvc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (n.getNodeName().equals("SpecializationPointViewComponent")) {
                        String tdid = null;
                        if (n.getAttributes().getNamedItem("transfDesc.id") != null) tdid = n.getAttributes().getNamedItem("transfDesc.id").getNodeValue();
                        TransformationDescriptor td = null;
                        if (tdid != null) td = (TransformationDescriptor) AllElements.getElementById(tdid);
                        try {
                            logger.debug("...Importing SpecializationPointViewComponent");
                            SpecializationPointViewComponent tvc = new SpecializationPointViewComponent(td);
                            tvc.importXMLForm(n);
                            addElement(tvc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (DOMException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterImport() throws Exception {
        try {
            super.afterImport();
            {
                Iterator it = transformerViewComponents.iterator();
                while (it.hasNext()) {
                    AbstractGraphicComponent abs = (AbstractGraphicComponent) it.next();
                    abs.afterImport();
                }
                try {
                    if (startPointIds != null && startPointIds.size() > 0) {
                        for (String startPointId : startPointIds) {
                            TransformerViewComponent startPoint = (TransformerViewComponent) AllElements.getElementById(startPointId);
                            if (startPoints == null) startPoints = new Vector<TransformerViewComponent>();
                            startPoints.add(startPoint);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (diagramInputParameterIds != null) {
                        for (String idref : diagramInputParameterIds) {
                            TransformerViewComponent tvc = (TransformerViewComponent) AllElements.getElementById(idref);
                            addDiagramInputParameter(tvc);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (diagramOutputParameterIds != null) {
                        for (String idref : diagramOutputParameterIds) {
                            TransformerViewComponent tvc = (TransformerViewComponent) AllElements.getElementById(idref);
                            addDiagramOutputParameter(tvc);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (innerTransformer != null) {
                        innerTransformer.afterImport();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Vector<String> startPointIds = new Vector<String>();

    @Override
    public String getInnerXml(int tab) {
        return "";
    }

    public boolean isStartPoint(TransformerViewComponent tvc) {
        if (startPoints == null || !startPoints.contains(tvc)) return false;
        return startPoints.get(0) == tvc;
    }

    public boolean isSequenceOrdered(TransformerViewComponent tvc) {
        if (startPoints == null || !startPoints.contains(tvc)) return false;
        return true;
    }

    private Vector<String> diagramInputParameterIds;

    private Vector<TransformerViewComponent> diagramInputParameterList;

    public void addDiagramInputParameter(TransformerViewComponent tvc) {
        try {
            if (diagramInputParameterList == null) diagramInputParameterList = new Vector<TransformerViewComponent>();
            diagramInputParameterList.add(tvc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDiagramInputParameter(TransformerViewComponent tvc) {
        if (diagramInputParameterList != null) try {
            diagramInputParameterList.remove(tvc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isDiagramInputParameter(TransformerViewComponent tvc) {
        try {
            if (diagramInputParameterList != null && diagramInputParameterList.size() > 0) {
                for (TransformerViewComponent c : diagramInputParameterList) {
                    if (c == tvc) return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Vector<String> diagramOutputParameterIds;

    private Vector<TransformerViewComponent> diagramOutputParameters;

    public void addDiagramOutputParameter(TransformerViewComponent tvc) {
        try {
            if (diagramOutputParameters == null) diagramOutputParameters = new Vector<TransformerViewComponent>();
            diagramOutputParameters.add(tvc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDiagramOutputParameter(TransformerViewComponent tvc) {
        if (diagramOutputParameters != null) try {
            diagramOutputParameters.remove(tvc);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isDiagramOutputParameter(TransformerViewComponent tvc) {
        try {
            if (diagramOutputParameters != null && diagramOutputParameters.size() > 0) {
                for (TransformerViewComponent c : diagramOutputParameters) {
                    if (c == tvc) return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private TransformationDescriptor innerTransformer;

    public TransformationDescriptor getInnerTransformer() {
        return innerTransformer;
    }

    public void setInnerTransformer(TransformationDescriptor innerTransformer) {
        this.innerTransformer = innerTransformer;
    }

    public TransformationDescriptor createInnerTransformer() throws Exception {
        if (innerTransformer == null && startPoints != null && startPoints.size() > 0 && diagramInputParameterList != null && diagramInputParameterList.size() > 0) {
            try {
                innerTransformer = new TransformationDescriptor(this);
                innerTransformer.setName(getName());
                innerTransformer.setReturnType("java.lang.Object");
                innerTransformer.setConcreteTransformationDescriptor((TransformationDescriptor) startPoints.get(0).getElement());
                innerTransformer.clearDefaultParameter();
                if (diagramInputParameterList != null && diagramInputParameterList.size() > 0) {
                    for (TransformerViewComponent tvc : diagramInputParameterList) {
                        TransformationParameter tp = (TransformationParameter) tvc.getElement();
                        innerTransformer.addLikedParameter(tp.getName(), tp);
                    }
                }
                if (diagramOutputParameters != null && diagramOutputParameters.size() > 0) {
                    for (TransformerViewComponent tvc : diagramOutputParameters) {
                        TransformationParameter tp = (TransformationParameter) tvc.getElement();
                        innerTransformer.addLikedParameter(tp.getName(), tp);
                    }
                }
                return innerTransformer;
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        } else {
            throw new Exception("Necessary requeriments: startPoint != null && diagramInputParameterList != null && diagramInputParameterList.size()>0");
        }
    }

    @Override
    public void openDiagram() {
        mode = EditorMode.READ_ONLY;
        try {
            this.setTitle(getName());
            DefaultApplicationFrame.getSelectedElementTreeController().getDiagramDesktopObserver().addInternalFrame(this);
            this.updateUI();
            this.notifyElementChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void checkDependecies() {
        Vector<TransformationParameter> list = new Vector<TransformationParameter>();
        Hashtable<TransformationParameter, TransformationParameterViewComponent> ht = new Hashtable<TransformationParameter, TransformationParameterViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                if (tvc instanceof TransformationParameterViewComponent) {
                    TransformationParameter tp = (TransformationParameter) tvc.getElement();
                    TransformationParameterViewComponent tpvc = ((TransformationParameterViewComponent) tvc);
                    if (tp.getDependencies() != null && tp.getDependencies().size() > 0 && !tpvc.containsDependencies()) {
                        ht.put(tp, tpvc);
                        list.add(tp);
                    }
                }
            }
        }
        if (list.size() > 0) {
            for (TransformationParameter tp : list) {
                ElementImpl el = (ElementImpl) tp.getDependencies().get(0);
                for (TransformerViewComponent tvc : transformerViewComponents) {
                    if (tvc.getElement() == el) {
                        ht.get(tp).addParameterDependency(tvc);
                    }
                }
            }
        }
    }

    public void remove(TransformerViewComponent tvc) {
        try {
            getTransformerViewComponents().remove(tvc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int cont = 100;

    public int getCont() {
        return cont;
    }

    public void setCont(int cont) {
        this.cont = cont;
    }

    public void add(ParameterDescriptor element, TransformerViewComponent tvc) {
        try {
            TransformationDescriptor td = (TransformationDescriptor) element.getParentElement();
            TransformerViewComponent elvc = null;
            if (element instanceof SharedVariable) {
                SharedVariable sv = (SharedVariable) element;
                try {
                    SharedVarViewComponent svc = new SharedVarViewComponent(td, sv);
                    if (sv == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (element instanceof TransformationParameter) {
                TransformationParameter sv = (TransformationParameter) element;
                try {
                    TransformationParameterViewComponent svc = new TransformationParameterViewComponent(td, sv);
                    if (sv == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (element instanceof SpecializationPoint && td.getSpecializationPoints() != null && td.getSpecializationPoints().size() > 0) {
                SpecializationPoint sp = (SpecializationPoint) element;
                try {
                    SpecializationPointViewComponent svc = new SpecializationPointViewComponent(sp, tvc, td.getSpecializationPointIndex(sp));
                    if (sp == element) elvc = svc;
                    svc.setTransformerViewComponent(tvc);
                    tvc.setNextLocation(svc);
                    svc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    svc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(svc);
                    tvc.getChildrenViewComponents().add(svc);
                    addExpressionBlockToDiagram(sp, svc, tvc);
                    addSimplePropertyToDiagram(sp, svc, tvc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cont += 50;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addExpressionBlockToDiagram(SpecializationPoint sp, SpecializationPointViewComponent svc, TransformerViewComponent tvc) throws Exception {
        if (sp.getExpressionBlocks() != null && sp.getExpressionBlocks().size() > 0) {
            Iterator<ExpressionBlock> it2 = sp.getExpressionBlocks().iterator();
            while (it2.hasNext()) {
                cont += 10;
                ExpressionBlock eb = it2.next();
                try {
                    ExpressionBlockViewComponent ebvc = new ExpressionBlockViewComponent(sp, eb);
                    ebvc.setTransformerViewComponent(svc);
                    svc.setNextLocation(ebvc);
                    ebvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    ebvc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(ebvc);
                    tvc.getChildrenViewComponents().add(ebvc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addSimplePropertyToDiagram(SpecializationPoint sp, SpecializationPointViewComponent svc, TransformerViewComponent tvc) throws Exception {
        if (sp.getOverridableProperties() != null && sp.getOverridableProperties().size() > 0) {
            Iterator<SimpleProperty> it2 = sp.getOverridableProperties().iterator();
            while (it2.hasNext()) {
                cont += 10;
                SimpleProperty eb = it2.next();
                try {
                    SimplePropertyViewComponent ebvc = new SimplePropertyViewComponent(sp, eb);
                    ebvc.setTransformerViewComponent(svc);
                    svc.setNextLocation(ebvc);
                    ebvc.setDiagram(TransformerDiagramPainterInternalFrame.this);
                    ebvc.setDrawingPanel(getPaintPanel());
                    addGraphicComponent(ebvc);
                    tvc.getChildrenViewComponents().add(ebvc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<TransformerViewComponent> getTransformationDescriptorViews() {
        List<TransformerViewComponent> arr = new ArrayList<TransformerViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                if (tvc.getElement() instanceof TransformationDescriptor) {
                    arr.add(tvc);
                } else if (tvc.getLinkedDiagram() != null && tvc.getLinkedDiagram() instanceof TransformerDiagramPainterInternalFrame) {
                    arr.add(tvc);
                }
            }
        }
        return arr;
    }

    public List<TransformerViewComponent> getOutputParameterViews(TransformationParameter tp) {
        List<TransformerViewComponent> arr = new ArrayList<TransformerViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                TransformationParameter output = null;
                if (tvc.getElement() instanceof TransformationParameter) {
                    output = (TransformationParameter) tvc.getElement();
                    if (output.getTransformationParameterKind() != TransformationParameterKind.OUTPUT) continue;
                }
                if (output != null) try {
                    if (output == tp || output.getParentElement() == tp.getParentElement()) continue;
                    if (Class.forName(tp.getType()).isAssignableFrom(Class.forName(output.getType()))) {
                        arr.add(tvc);
                    }
                } catch (ClassNotFoundException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    public List<TransformerViewComponent> getTransformationDescriptorViews(TransformationParameter tp) {
        List<TransformerViewComponent> arr = new ArrayList<TransformerViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                TransformationDescriptor td = null;
                if (tvc.getElement() instanceof TransformationDescriptor) {
                    td = (TransformationDescriptor) tvc.getElement();
                } else if (tvc.getLinkedDiagram() != null && startPoints != null && startPoints.size() > 0) {
                    td = (TransformationDescriptor) startPoints.get(0).getElement();
                    if (td == null) try {
                        td = tvc.getLinkedDiagram().createInnerTransformer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (td != null && tp.getType() != null && td.getReturnType() != null) try {
                    if (Class.forName(tp.getType()).isAssignableFrom(Class.forName(td.getReturnType()))) {
                        arr.add(tvc);
                    }
                } catch (ClassNotFoundException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    public List<TransformerViewComponent> getTransformationDescriptorViews(SpecializationPoint sp) {
        List<TransformerViewComponent> arr = new ArrayList<TransformerViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                TransformationDescriptor td = null;
                if (tvc.getElement() instanceof TransformationDescriptor) {
                    td = (TransformationDescriptor) tvc.getElement();
                } else if (tvc.getLinkedDiagram() != null && startPoints != null && startPoints.size() > 0) {
                    td = (TransformationDescriptor) startPoints.get(0).getElement();
                    if (td == null) try {
                        td = tvc.getLinkedDiagram().createInnerTransformer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (td != null && td.getReturnType() != null) try {
                    arr.add(tvc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    public List<TransformerViewComponent> getSharedVariablesViews(TransformationParameter tp) {
        List<TransformerViewComponent> arr = new ArrayList<TransformerViewComponent>();
        if (transformerViewComponents != null && transformerViewComponents.size() > 0) {
            for (TransformerViewComponent tvc : transformerViewComponents) {
                SharedVariable td = null;
                if (tvc.getElement() instanceof SharedVariable) {
                    td = (SharedVariable) tvc.getElement();
                }
                if (td != null) try {
                    if (Class.forName(tp.getType()).isAssignableFrom(Class.forName(td.getType()))) {
                        arr.add(tvc);
                    }
                } catch (ClassNotFoundException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }

    public void setMy(int my) {
        this.my = my;
    }

    public int getMy() {
        return my;
    }

    public void setMx(int mx) {
        this.mx = mx;
    }

    public int getMx() {
        return mx;
    }

    static int transfromercount = 1;

    public void processClick(MouseEvent evt) throws Exception {
        if (getTransformerDiagramToolBar().getAddTdButton().isSelected()) {
            FomdaModel model = (FomdaModel) DefaultApplicationFrame.getInstance().getSelectedModel();
            TransformationDescriptor td = new TransformationDescriptor(model);
            td.setName(getName() + transfromercount++);
            model.addElement(td);
            resetToolBar();
            addTransformer(evt.getX() + 8, evt.getY() + 40, td);
        } else if (getTransformerDiagramToolBar().getTdUtility() != null) {
            addTransformer(evt.getX() + 8, evt.getY() + 40, getTransformerDiagramToolBar().getTdUtility());
            resetToolBar();
        }
    }
}
