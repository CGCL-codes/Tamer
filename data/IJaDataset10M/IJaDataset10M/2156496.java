package at.fhjoanneum.cgvis.plots.mosaic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.NoSuchElementException;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import at.fhj.utils.misc.ProgressTracker;
import at.fhj.utils.swing.JMsgPane;
import at.fhj.utils.swing.ProgressDialog;
import at.fhj.utils.swing.ProgressWorker;
import at.fhjoanneum.cgvis.ViewPreferences;
import at.fhjoanneum.cgvis.cluster.PointSetHierarchicalClusterer;
import at.fhjoanneum.cgvis.data.AttrSelection;
import at.fhjoanneum.cgvis.data.CompoundAttrsPointSet;
import at.fhjoanneum.cgvis.data.DataUID;
import at.fhjoanneum.cgvis.data.ElementSelection;
import at.fhjoanneum.cgvis.data.IPointSet;
import at.fhjoanneum.cgvis.data.PointSet;
import at.fhjoanneum.cgvis.data.PointSetPerm;
import at.fhjoanneum.cgvis.data.PointSetTransp;
import at.fhjoanneum.cgvis.data.SparseElemPointSet;
import at.fhjoanneum.cgvis.plots.AbstractFloatingLabelsNode.LabelIterator;
import at.fhjoanneum.cgvis.plots.AbstractFloatingPanelNode;
import at.fhjoanneum.cgvis.plots.ColorScale;
import at.fhjoanneum.cgvis.plots.PaintedFloatingLabelsNode;
import at.fhjoanneum.cgvis.plots.PanHandler;
import at.fhjoanneum.cgvis.plots.ZoomHandler;
import at.fhjoanneum.cgvis.util.PiccoloUtils;
import ch.unifr.dmlib.cluster.ClusterNode;
import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PBounds;
import edu.umd.cs.piccolo.util.PPaintContext;

/**
 * @author Ilya Boyandin
 */
public class MosaicCanvas extends PCanvas {

    private static final long serialVersionUID = 2369594648894387993L;

    private static Logger logger = Logger.getLogger(MosaicCanvas.class.getName());

    private static final Insets NULL_INSETS = new Insets(0, 0, 0, 0);

    private CGVisMosaicPlotNode[] mosaicNodes;

    private DendrogramNode attrsDendrogramNode;

    private DendrogramNode elemsDendrogramNode;

    private boolean wasDataClustered;

    private IPointSet[] srcPointSets;

    private final MosaicCrosshairNode crosshair;

    private final ViewPreferences preferences;

    private PaintedFloatingLabelsNode attrLabelsNode;

    private double mosaicNodesGapX;

    private PaintedFloatingLabelsNode elemLabelsNode;

    private boolean showAttributeLabels = true;

    private boolean showElementLabels = true;

    private boolean showAttrsDendrogram = true;

    private boolean showElemsDendrogram = true;

    private final ZoomHandler zoomHandler;

    private boolean isSelectingAttrRange;

    private boolean isSelectingElemRange;

    private int attrRangeSelectionStart;

    private int elemRangeSelectionStart;

    private CGVisMosaicPlotNode attrRangeSelectionStartNode;

    private ElementSelection elemSelection;

    private final MosaicView view;

    private IPointSet compoundPointSet;

    private final PanHandler panHandler;

    private ColorScale colorScale;

    /**
     * @param pointSets
     *            Elements in the pointsets must be sorted
     * @param settings
     */
    public MosaicCanvas(MosaicView view, IPointSet[] pointSets, ViewPreferences preferences) {
        setBackground(new Color(47, 89, 134));
        setDefaultRenderQuality(PPaintContext.LOW_QUALITY_RENDERING);
        this.view = view;
        this.preferences = preferences;
        this.srcPointSets = pointSets;
        final PCamera camera = getCamera();
        camera.addInputEventListener(new CameraInputHandler());
        crosshair = new MosaicCrosshairNode(this);
        camera.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if (PCamera.PROPERTY_BOUNDS == evt.getPropertyName()) {
                    crosshair.setBounds(getCamera().getBoundsReference());
                }
            }
        });
        camera.addChild(0, crosshair);
        setZoomEventHandler(null);
        zoomHandler = new ZoomHandler(.5, 50);
        addInputEventListener(zoomHandler);
        panHandler = new PanHandler();
        setPanEventHandler(panHandler);
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                getCamera().setViewConstraint(getCamera().getViewConstraint());
                getCamera().repaint();
            }
        });
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        clearSelection();
                        break;
                    case 'a':
                    case 'A':
                        if (e.isControlDown()) {
                            selectAllAttrs();
                        }
                        break;
                    case 'e':
                    case 'E':
                        if (e.isControlDown()) {
                            selectAllElements();
                        }
                        break;
                }
            }
        });
    }

    private transient boolean fitInCameraViewOnce = false;

    private void fitInCameraViewOnce() {
        if (!fitInCameraViewOnce) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    fitInCameraView(false);
                }
            });
            fitInCameraViewOnce = true;
        }
    }

    public void init() {
        if (srcPointSets.length == 0) {
            return;
        } else if (srcPointSets.length == 1) {
            mosaicNodes = new CGVisMosaicPlotNode[1];
            final IPointSet pset = srcPointSets[0];
            mosaicNodes[0] = new CGVisMosaicPlotNode(pset, this);
            getLayer().addChild(mosaicNodes[0]);
            compoundPointSet = pset;
        } else {
            final IPointSet[] psets = SparseElemPointSet.joinElementsByLabel(srcPointSets);
            final IPointSet[] newPsets = new IPointSet[psets.length];
            mosaicNodes = new CGVisMosaicPlotNode[psets.length];
            for (int i = 0; i < psets.length; i++) {
                newPsets[i] = new PointSet(psets[i]);
                final CGVisMosaicPlotNode mosaic = new CGVisMosaicPlotNode(newPsets[i], this);
                mosaicNodes[i] = mosaic;
                getLayer().addChild(mosaic);
            }
            compoundPointSet = new CompoundAttrsPointSet(newPsets);
        }
        elemSelection = new ElementSelection(compoundPointSet);
        srcPointSets = null;
        double minV = Double.POSITIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (CGVisMosaicPlotNode mosaic : mosaicNodes) {
            final double mMinV = mosaic.getMinValue();
            if (!Double.isNaN(mMinV)) {
                if (mMinV < minV) minV = mMinV;
            }
            final double mMaxV = mosaic.getMaxValue();
            if (!Double.isNaN(mMaxV)) {
                if (mMaxV > maxV) maxV = mMaxV;
            }
        }
        double maxWidth = 0;
        for (CGVisMosaicPlotNode mosaic : mosaicNodes) {
            final PBounds b = mosaic.getBounds();
            if (b.getWidth() > maxWidth) {
                maxWidth = b.getWidth();
            }
        }
        mosaicNodesGapX = Math.floor(maxWidth * .01);
        double offsetX = 0;
        for (CGVisMosaicPlotNode mosaic : mosaicNodes) {
            final PBounds b = mosaic.getBounds();
            mosaic.setBounds(b.getX() + offsetX, b.getY(), b.getWidth(), b.getHeight());
            offsetX = offsetX + b.getWidth() + mosaicNodesGapX;
        }
        attrLabelsNode = new PaintedFloatingLabelsNode(true, createAttrsLabelIterator());
        attrLabelsNode.setVisible(showAttributeLabels);
        attrLabelsNode.setPickable(showAttributeLabels);
        elemLabelsNode = new PaintedFloatingLabelsNode(false, createElementsLabelIterator());
        elemLabelsNode.setVisible(showElementLabels);
        elemLabelsNode.setPickable(showElementLabels);
        attrLabelsNode.addDisjointNode(elemLabelsNode);
        getCamera().addChild(0, attrLabelsNode);
        getCamera().addChild(0, elemLabelsNode);
        final PropertyChangeListener fitOnceListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                fitInCameraViewOnce();
                final Object src = evt.getSource();
                if (src instanceof PaintedFloatingLabelsNode) {
                    ((PaintedFloatingLabelsNode) src).removePropertyChangeListener(this);
                }
            }
        };
        if (elemLabelsNode.getVisible()) {
            elemLabelsNode.addPropertyChangeListener(PaintedFloatingLabelsNode.PROPERTY_SIZE_ADJUSTED_TO_LABELS, fitOnceListener);
        } else if (attrLabelsNode.getVisible()) {
            attrLabelsNode.addPropertyChangeListener(PaintedFloatingLabelsNode.PROPERTY_SIZE_ADJUSTED_TO_LABELS, fitOnceListener);
        }
    }

    public boolean getShowAttributeLabels() {
        return showAttributeLabels;
    }

    public void setShowAttributeLabels(boolean value) {
        attrLabelsNode.setVisible(value);
        attrLabelsNode.setPickable(value);
        showAttributeLabels = value;
    }

    public boolean getShowElementLabels() {
        return showElementLabels;
    }

    public void setShowElementLabels(boolean value) {
        elemLabelsNode.setVisible(value);
        elemLabelsNode.setPickable(value);
        showElementLabels = value;
    }

    public boolean getShowAttrsDendrogram() {
        return showAttrsDendrogram;
    }

    public void setShowAttrsDendrogram(boolean value) {
        if (attrsDendrogramNode != null) {
            attrsDendrogramNode.setVisible(value);
            attrsDendrogramNode.setPickable(value);
        }
        showAttrsDendrogram = value;
    }

    public boolean getShowElemsDendrogram() {
        return showElemsDendrogram;
    }

    public void setShowElementsDendrogram(boolean value) {
        if (elemsDendrogramNode != null) {
            elemsDendrogramNode.setVisible(value);
            elemsDendrogramNode.setPickable(value);
        }
        showElemsDendrogram = value;
    }

    private LabelIterator<String> createAttrsLabelIterator() {
        return new LabelIterator<String>() {

            private int pointSetIndex = 0;

            private int attrIndex = 0;

            private double pos;

            public double getItemPosition() {
                return pos;
            }

            public double getItemSize() {
                return CGVisMosaicPlotNode.SQUARE_WIDTH;
            }

            public boolean hasNext() {
                if (mosaicNodes.length == 0) {
                    return false;
                }
                if (attrIndex < mosaicNodes[pointSetIndex].getPointSet().getDimension()) {
                    return true;
                } else if (pointSetIndex < mosaicNodes.length - 1) {
                    return true;
                }
                return false;
            }

            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                IPointSet pset = mosaicNodes[pointSetIndex].getPointSet();
                while (attrIndex > pset.getDimension() - 1) {
                    if (pointSetIndex >= mosaicNodes.length - 1) {
                        pos = Double.NaN;
                        return null;
                    } else {
                        pset = mosaicNodes[++pointSetIndex].getPointSet();
                        attrIndex = 0;
                    }
                }
                final String label = pset.getAttributeLabel(attrIndex);
                pos = mosaicNodes[pointSetIndex].getBoundsReference().getX() + attrIndex * (CGVisMosaicPlotNode.SQUARE_WIDTH + CGVisMosaicPlotNode.SPACING);
                attrIndex++;
                return label;
            }

            public void reset() {
                pointSetIndex = 0;
                attrIndex = 0;
                pos = Double.NaN;
            }
        };
    }

    private LabelIterator createElementsLabelIterator() {
        return new LabelIterator() {

            private int elemIndex = 0;

            private double pos;

            public double getItemPosition() {
                return pos;
            }

            public double getItemSize() {
                return CGVisMosaicPlotNode.SQUARE_HEIGHT;
            }

            public boolean hasNext() {
                if (mosaicNodes.length == 0) {
                    return false;
                }
                if (elemIndex < mosaicNodes[0].getPointSet().getSize()) {
                    return true;
                }
                return false;
            }

            public String next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                final CGVisMosaicPlotNode mosaic = mosaicNodes[0];
                final IPointSet pset = mosaic.getPointSet();
                final String label = pset.getElementLabel(elemIndex);
                pos = mosaic.getBoundsReference().getY() + elemIndex * (CGVisMosaicPlotNode.SQUARE_HEIGHT + CGVisMosaicPlotNode.SPACING);
                elemIndex++;
                return label;
            }

            public void reset() {
                elemIndex = 0;
                pos = Double.NaN;
            }
        };
    }

    private PBounds getMosaicBounds() {
        if (mosaicNodes.length == 0) {
            return null;
        }
        final PBounds b = mosaicNodes[0].getBounds();
        for (int i = 1; i < mosaicNodes.length; i++) {
            Rectangle2D.union(b, mosaicNodes[i].getBoundsReference(), b);
        }
        return b;
    }

    private interface FloatingPanelVisitor {

        void visit(AbstractFloatingPanelNode fp);
    }

    private void traverseFloatingPanels(FloatingPanelVisitor v) {
        final PCamera camera = getCamera();
        for (int i = 0, size = camera.getChildrenCount(); i < size; i++) {
            final PNode child = camera.getChild(i);
            if (child instanceof AbstractFloatingPanelNode) {
                v.visit((AbstractFloatingPanelNode) child);
            }
        }
    }

    private Insets getContentInsets() {
        final Insets insets = new Insets(0, 0, 0, 0);
        final Rectangle b = getBounds();
        final int maxHGap = (int) Math.floor(b.getWidth() / 25);
        final int maxVGap = (int) Math.floor(b.getHeight() / 25);
        traverseFloatingPanels(new FloatingPanelVisitor() {

            public void visit(AbstractFloatingPanelNode fp) {
                if (fp.getVisible()) {
                    final PBounds fpb = fp.getBoundsReference();
                    if (fp.isHorizontal()) {
                        if (fpb.y <= maxVGap) {
                            insets.top = Math.max(insets.top, (int) Math.ceil(fpb.y + fpb.height));
                        } else if (fpb.y + fpb.height + maxVGap >= b.height) {
                            insets.bottom = Math.max(insets.bottom, (int) Math.ceil(fpb.height - (fpb.y + fpb.height - b.height)));
                        }
                    } else {
                        if (fpb.x <= maxHGap) {
                            insets.left = Math.max(insets.left, (int) Math.ceil(fpb.x + fpb.width));
                        } else if (fpb.x + fpb.width + maxHGap >= b.width) {
                            insets.right = Math.max(insets.right, (int) Math.ceil(fpb.width - (fpb.x + fpb.width - b.width)));
                        }
                    }
                }
            }
        });
        return insets;
    }

    /**
     * Bounds of the area where mosaic should be placed (without the floating
     * panels) in the camera coordinate system.
     *
     * @return
     */
    private Rectangle2D getContentBounds() {
        final Insets insets = getContentInsets();
        final PBounds vb = getCamera().getBounds();
        vb.x += insets.left;
        vb.y += insets.top;
        vb.width -= insets.left + insets.right;
        vb.height -= insets.top + insets.bottom;
        return vb;
    }

    private Rectangle2D getViewContentBounds() {
        final Rectangle2D cb = getContentBounds();
        getCamera().localToView(cb);
        return cb;
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        final int oldWidth = getWidth();
        final int oldHeight = getHeight();
        final Rectangle2D oldVisibleBounds = getVisibleMosaicBounds();
        final Rectangle2D oldContentBounds = getViewContentBounds();
        super.setBounds(x, y, w, h);
        final int maxHGap = (int) Math.floor(oldWidth / 25);
        final int maxVGap = (int) Math.floor(oldHeight / 25);
        traverseFloatingPanels(new FloatingPanelVisitor() {

            public void visit(AbstractFloatingPanelNode fp) {
                if (fp.getVisible()) {
                    final PBounds fpb = fp.getBoundsReference();
                    if (fp.isHorizontal()) {
                        if (oldHeight > 0) {
                            final double d = oldHeight - (fpb.y + fpb.height);
                            if (d <= maxVGap) {
                                fp.setBounds(fpb.x, getHeight() - fpb.height, fpb.width, fpb.height);
                            }
                        }
                    } else {
                        if (oldWidth > 0) {
                            final double d = oldWidth - (fpb.x + fpb.width);
                            if (d <= maxHGap) {
                                fp.setBounds(getWidth() - fpb.width, fpb.y, fpb.width, fpb.height);
                            }
                        }
                    }
                }
            }
        });
        if (oldVisibleBounds != null) {
            fitVisibleInCameraView(oldVisibleBounds, oldContentBounds);
        }
    }

    /**
     * Bounds of the part of the mosaic (all of the MosaicNodes together) that
     * is currently visible in the view (in the view coordinate system).
     *
     * @return
     */
    public Rectangle2D getVisibleMosaicBounds() {
        final PBounds mb = getMosaicBounds();
        if (mb != null) {
            final PBounds vb = getCamera().getViewBounds();
            Rectangle2D.intersect(vb, mb, vb);
            return vb;
        }
        return null;
    }

    public void fitVisibleInCameraView(Rectangle2D visibleBounds, Rectangle2D contentBounds) {
        if (contentBounds.getWidth() <= 0 || contentBounds.getHeight() <= 0) {
            return;
        }
        final Insets insets;
        if (contentBounds.contains(visibleBounds)) {
            insets = getContentInsets();
            insets.left += 5;
            insets.top += 5;
            insets.bottom += 5;
            insets.right += 5;
        } else {
            insets = NULL_INSETS;
        }
        PiccoloUtils.setViewPaddedBounds(getCamera(), visibleBounds, insets);
    }

    public void fitInCameraView(boolean animate) {
        final PBounds mb = getMosaicBounds();
        if (mb != null) {
            final Insets insets = getContentInsets();
            insets.left += 5;
            insets.top += 5;
            insets.bottom += 5;
            insets.right += 5;
            if (animate && preferences.useAnimation()) {
                PiccoloUtils.animateViewToPaddedBounds(getCamera(), mb, insets, preferences.getShortAnimationsDuration());
            } else {
                PiccoloUtils.setViewPaddedBounds(getCamera(), mb, insets);
            }
        }
    }

    protected void startClustering() {
        if (wasDataClustered) {
            return;
        }
        final ProgressTracker progress = new ProgressTracker();
        final ClustererWorker worker = new ClustererWorker(progress);
        final ProgressDialog progressDlg = new ProgressDialog(getOwnerFrame(), "Clustering", worker, false);
        progress.addProgressListener(progressDlg);
        worker.start();
        progressDlg.setVisible(true);
    }

    private Frame getOwnerFrame() {
        final Window owner = SwingUtilities.getWindowAncestor(this);
        return (owner instanceof Frame ? (Frame) owner : null);
    }

    private class ClustererWorker extends ProgressWorker {

        public ClustererWorker(ProgressTracker progress) {
            super(progress);
        }

        @Override
        public Object construct() {
            try {
                performClustering(getProgressTracker());
            } catch (final Throwable th) {
                JMsgPane.showErrorDialog(getOwnerFrame(), th);
                logger.error("Clustering failed", th);
            }
            return null;
        }
    }

    private void performClustering(ProgressTracker progress) {
        logger.info("Starting clustering");
        final int pointSetNum = mosaicNodes.length;
        final PointSetHierarchicalClusterer hc = new PointSetHierarchicalClusterer();
        progress.startTask("Preparing pointset", .05);
        final PointSet pset = new PointSet(compoundPointSet);
        if (progress.isCancelled()) {
            return;
        }
        progress.taskCompleted();
        progress.startTask("Clustering elements", .45);
        hc.cluster(pset, progress);
        if (progress.isCancelled()) {
            return;
        }
        progress.taskCompleted();
        final int[] elementPerm = hc.getElementPermutation();
        final ClusterNode elemRC = hc.getRootCluster();
        if (progress.isCancelled()) {
            return;
        }
        final IPointSet[] psets = new IPointSet[pointSetNum];
        final ClusterNode[] elemRCs = new ClusterNode[pointSetNum];
        final ClusterNode[] attrRCs = new ClusterNode[pointSetNum];
        for (int i = 0; i < pointSetNum; i++) {
            final CGVisMosaicPlotNode mosaic = mosaicNodes[i];
            IPointSet pointSet = mosaic.getPointSet();
            progress.startTask("Preparing pointset", .05 / pointSetNum);
            final PointSet ps = new PointSet(new PointSetTransp(pointSet));
            if (progress.isCancelled()) {
                return;
            }
            progress.taskCompleted();
            progress.startTask("Clustering attributes", .45 / pointSetNum);
            hc.cluster(ps, progress);
            if (progress.isCancelled()) {
                return;
            }
            final ClusterNode attrRC = hc.getRootCluster();
            final int[] attrPerm = hc.getElementPermutation();
            progress.taskCompleted();
            pointSet = new PointSetPerm(pointSet, elementPerm, attrPerm);
            elemRCs[i] = elemRC;
            attrRCs[i] = attrRC;
            mosaic.setPointSet(pointSet);
            psets[i] = pointSet;
            mosaic.setDirty(true);
        }
        final Rectangle2D oldVisibleBounds = getVisibleMosaicBounds();
        final Rectangle2D oldContentBounds = getViewContentBounds();
        elemsDendrogramNode = new DendrogramNode(mosaicNodes, elemRCs, false);
        attrsDendrogramNode = new DendrogramNode(mosaicNodes, attrRCs, true);
        attrLabelsNode.addDisjointNode(elemLabelsNode);
        attrLabelsNode.addDisjointNode(elemsDendrogramNode);
        elemLabelsNode.addDisjointNode(attrsDendrogramNode);
        attrsDendrogramNode.addDisjointNode(elemsDendrogramNode);
        final PCamera camera = getCamera();
        camera.addChild(0, attrsDendrogramNode);
        view.attrsDendrogramAdded();
        camera.addChild(0, elemsDendrogramNode);
        view.elementsDendrogramAdded();
        if (getShowAttributeLabels() && attrLabelsNode.getY() == 0) {
            attrLabelsNode.setY(getBounds().height - attrLabelsNode.getHeight());
        }
        if (getShowElementLabels() && elemLabelsNode.getX() == 0) {
            elemLabelsNode.setX(getBounds().width - elemLabelsNode.getWidth());
        }
        compoundPointSet = new CompoundAttrsPointSet(psets);
        elemSelection.setPointSet(compoundPointSet);
        wasDataClustered = true;
        progress.processFinished();
        logger.info("Clustering finished");
        repaint();
        fitVisibleInCameraView(oldVisibleBounds, oldContentBounds);
    }

    class CameraInputHandler extends PBasicInputEventHandler {

        private PNode dragStartNode;

        @Override
        public void mouseEntered(PInputEvent event) {
            updateCrosshair(event);
        }

        @Override
        public void mouseMoved(PInputEvent event) {
            if (isSelectingAttrRange() && !event.isShiftDown()) {
                stopSelectingAttrRange();
            }
            if (isSelectingElemRange() && !event.isAltDown()) {
                stopSelectingElemRange();
            }
            updateCrosshair(event);
        }

        @Override
        public void mouseDragged(PInputEvent event) {
            dragStartNode = event.getPickedNode();
            if (isSelectingAttrRange() && !event.isShiftDown()) {
                stopSelectingAttrRange();
            }
            if (isSelectingElemRange() && !event.isAltDown()) {
                stopSelectingElemRange();
            }
            updateCrosshair(event);
        }

        @Override
        public void mouseReleased(PInputEvent event) {
            dragStartNode = null;
        }

        @Override
        public void mouseExited(PInputEvent event) {
            if (dragStartNode instanceof CGVisMosaicPlotNode) {
                panHandler.setAutopan(true);
            } else {
                panHandler.setAutopan(false);
            }
            stopSelectingAttrRange();
        }

        private Point getCell(CGVisMosaicPlotNode mosaic, PInputEvent event) {
            final Point2D pos = event.getCanvasPosition();
            getCamera().localToView(pos);
            return mosaic.pointToCell(pos);
        }

        private PNode getNode(PInputEvent event) {
            return event.getInputManager().getMouseOver().getPickedNode();
        }

        private void updateCrosshair(PInputEvent event) {
            final PNode node = getNode(event);
            if (node instanceof CGVisMosaicPlotNode) {
                final CGVisMosaicPlotNode mosaic = (CGVisMosaicPlotNode) node;
                final Point cell = getCell(mosaic, event);
                if (cell != null) {
                    showCrosshair(mosaic, cell);
                } else {
                    hideCrosshair();
                }
            } else {
                hideCrosshair();
            }
            crosshair.repaint();
        }

        private void showCrosshair(final CGVisMosaicPlotNode mosaic, final Point cell) {
            crosshair.showCrosshair(mosaic, cell);
        }

        private void hideCrosshair() {
            crosshair.hideCrosshair();
        }

        @Override
        public void mousePressed(PInputEvent event) {
            final PNode node = getNode(event);
            if (node instanceof CGVisMosaicPlotNode) {
                final CGVisMosaicPlotNode mosaic = (CGVisMosaicPlotNode) node;
                final Point cell = getCell(mosaic, event);
                if (cell != null) {
                    if (event.isShiftDown()) {
                        if (isSelectingAttrRange()) {
                            if (mosaic == getAttrRangeSelectionStartNode()) {
                                mosaic.selectAttrRange(Math.min(cell.x, attrRangeSelectionStart), Math.max(cell.x, attrRangeSelectionStart));
                            }
                            stopSelectingAttrRange();
                        } else {
                            if (mosaic.invertAttrSelection(cell.x)) {
                                startSelectingAttrRange(mosaic, cell.x);
                            }
                        }
                    } else if (event.isAltDown()) {
                        if (isSelectingElemRange()) {
                            elemSelection.selectRange(Math.min(cell.y, elemRangeSelectionStart), Math.max(cell.y, elemRangeSelectionStart));
                            stopSelectingElemRange();
                        } else {
                            if (elemSelection.invert(cell.y)) {
                                startSelectingElemRange(cell.y);
                            }
                        }
                        view.fireElementSelectionChanged(elemSelection.getSelectionUIDs());
                    }
                    repaint();
                }
            }
        }
    }

    public boolean isSelectingAttrRange() {
        return isSelectingAttrRange;
    }

    private void startSelectingAttrRange(CGVisMosaicPlotNode mosaic, int attrIndex) {
        if (!isSelectingAttrRange) {
            attrRangeSelectionStartNode = mosaic;
            attrRangeSelectionStart = attrIndex;
            isSelectingAttrRange = true;
        }
    }

    public CGVisMosaicPlotNode getAttrRangeSelectionStartNode() {
        return attrRangeSelectionStartNode;
    }

    private void stopSelectingAttrRange() {
        if (isSelectingAttrRange) {
            isSelectingAttrRange = false;
        }
    }

    public boolean isSelectingElemRange() {
        return isSelectingElemRange;
    }

    private void startSelectingElemRange(int elemIndex) {
        if (!isSelectingElemRange) {
            elemRangeSelectionStart = elemIndex;
            isSelectingElemRange = true;
        }
    }

    private void stopSelectingElemRange() {
        if (isSelectingElemRange) {
            isSelectingElemRange = false;
        }
    }

    public AttrSelection[] getAttrSelection() {
        int nzCS = 0;
        for (int i = 0; i < mosaicNodes.length; i++) {
            final AttrSelection cs = mosaicNodes[i].getAttrSelection();
            if (cs.getSize() > 0) {
                nzCS++;
            }
        }
        final AttrSelection[] sel = new AttrSelection[nzCS];
        for (int i = 0, cnt = 0; i < mosaicNodes.length; i++) {
            final AttrSelection cs = mosaicNodes[i].getAttrSelection();
            if (cs.getSize() > 0) {
                sel[cnt++] = cs;
            }
        }
        return sel;
    }

    public void clearSelection() {
        for (CGVisMosaicPlotNode mosaic : mosaicNodes) {
            mosaic.clearSelection();
        }
        if (elemSelection.getSize() > 0) {
            elemSelection.clearAll();
            view.fireElementSelectionChanged(elemSelection.getSelectionUIDs());
        }
    }

    private void selectAllElements() {
        elemSelection.selectAll();
        view.fireElementSelectionChanged(elemSelection.getSelectionUIDs());
        repaint();
    }

    private void selectAllAttrs() {
        for (CGVisMosaicPlotNode mosaic : mosaicNodes) {
            mosaic.getAttrSelection().selectAll();
        }
        repaint();
    }

    public ElementSelection getElemSelection() {
        return elemSelection;
    }

    public void setElementSelection(DataUID[] selection) {
        elemSelection.setSelectionUIDs(selection);
        repaint();
    }
}
