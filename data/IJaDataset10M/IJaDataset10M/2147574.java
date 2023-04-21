package org.tigr.seq.tdb.display;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import org.tigr.seq.display.*;
import org.tigr.seq.display.prefs.*;
import org.tigr.seq.log.*;
import org.tigr.seq.seqdata.*;
import org.tigr.seq.seqdata.display.*;

/**
 *
 * Describe class <code>TDBAssemblyPositionsSegmentPanel</code> here. 
 *
 * <p>
 * Copyright &copy; 2002 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: TDBAssemblyPositionsSegmentPanel.java,v $
 * $Revision: 1.38 $
 * $Date: 2005/12/29 14:56:08 $
 * $Author: dkatzel $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
public class TDBAssemblyPositionsSegmentPanel extends JPanel implements AdjustmentListener, IAssemblyComplementListener, IAssemblyCoordinatorListener, IAssemblySelectionListener, IAssemblySelectionMotionListener, IAutoscrollableComponent, IBasecallDisplayerPreferencesChangeListener, IChildGeometryChangeSource, KeyListener, MouseListener, IWindowListener, MouseMotionListener {

    /**
     * 
     */
    private static final long serialVersionUID = -540880280168319309L;

    /**
     * Describe variable <code>displayerPrefs</code> here.
     *
     *
     */
    private IBasecallDisplayerPreferences displayerPrefs;

    /**
     * Describe variable <code>colorPrefs</code> here.
     *
     *
     */
    private IBasecallColorPreferences colorPrefs;

    /**
     * Describe variable <code>assemblyCoordinator</code> here.
     *
     *
     */
    private IAssemblyCoordinator assemblyCoordinator;

    /**
     * Describe variable <code>paintIndices</code> here.
     *
     *
     */
    private int[] paintIndices = new int[2];

    /**
     * Describe variable <code>geometryChangeListeners</code> here.
     *
     *
     */
    private List geometryChangeListeners = new ArrayList();

    /**
     * Describe variable <code>measured</code> here.
     *
     *
     */
    private boolean measured;

    /**
     * Describe variable <code>defaultFont</code> here.
     *
     *
     */
    private Font defaultFont;

    /**
     * Describe variable <code>defaultCharacterMetrics</code> here.
     *
     *
     */
    private CharacterMetrics defaultCharacterMetrics;

    /**
     * Describe constant <code>EMPTY_DIMENSION</code> here.
     *
     *
     */
    private static final Dimension EMPTY_DIMENSION = new Dimension(0, 0);

    /**
     * Describe constant <code>NOT_SET</code> here.
     *
     *
     */
    private static final int NOT_SET = -1000000;

    /**
     * Describe variable <code>horizontalOffset</code> here.
     *
     *
     */
    private int horizontalOffset = TDBAssemblyPositionsSegmentPanel.NOT_SET;

    /**
     * Describe variable <code>lastSupportingRectangle</code> here.
     *
     *
     */
    private Rectangle lastSupportingRectangle;

    /**
     * Describe variable <code>selectionManager</code> here.
     *
     *
     */
    private IAssemblySelectionManager selectionManager;

    /**
     * Describe variable <code>selectionMotionManager</code> here.
     *
     *
     */
    private IAssemblySelectionMotionManager selectionMotionManager;

    /**
     * Describe constant <code>SELECTION_STROKE</code> here.
     *
     *
     */
    private static final BasicStroke SELECTION_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0.0f, new float[] { 1.0f }, 0);

    /** 
     * The index of the point before the start base in the selection,
     * used to fire selection events to selection listeners.  */
    private int startSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;

    /** 
     * The index of the point before the current base in the selection,
     * used to fire selection events to selection listeners.  */
    private int currentSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;

    /**
     * Describe variable <code>currentSelectionType</code> here.
     *
     *
     */
    private int currentSelectionType = TDBAssemblyPositionsSegmentPanel.NOT_SET;

    /**
     * The stroke to use for sequences
     *
     */
    private static final BasicStroke SEQUENCE_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL, 0.0f, new float[] { 1.0f }, 0);

    /**
     * Internal use only selection type constant.
     *
     *
     */
    private static final int SELECTION_START = 1;

    /**
     * Internal use only selection type constant.
     *
     *
     */
    private static final int SELECTION_END = 2;

    private IOnscreenLocationManager onscreenLocationManager;

    private IAssemblyDisplayPreferences assemblyDisplayPrefs;

    private IBaseAssemblySequence currentSelectingSequence;

    private IZoomActuationListener zoomActuationListener;

    private double[] positionPeriodThresholds;

    private int[] positionPeriods;

    private IWindow parentWindow;

    /**
     * Creates a new <code>TDBAssemblyPositionsSegmentPanel</code> instance.
     *
     *
     * @param pAssemblyCoordinator an <code>IAssemblyCoordinator</code> value
     * 
     * @param pDisplayerPrefs an <code>IBasecallDisplayerPreferences</code> value
     * 
     * @param pColorPrefs an <code>IBasecallColorPreferences</code> value
     * 
     */
    public TDBAssemblyPositionsSegmentPanel(IAssemblyCoordinator pAssemblyCoordinator, IAssemblyDisplayPreferences pAssemblyDisplayPrefs, IBasecallDisplayerPreferences pDisplayerPrefs, IBasecallColorPreferences pColorPrefs, IAssemblySelectionManager pSelectionManager, IAssemblySelectionMotionManager pSelectionMotionManager, IOnscreenLocationManager pOnscreenLocationManager, IZoomActuationListener pZoomActuationListener, IWindow parentWindow) {
        this.parentWindow = parentWindow;
        this.assemblyCoordinator = pAssemblyCoordinator;
        this.assemblyCoordinator.addAssemblyCoordinatorListener(this);
        this.zoomActuationListener = pZoomActuationListener;
        this.displayerPrefs = pDisplayerPrefs;
        this.displayerPrefs.addBasecallDisplayerPreferencesChangeListener(this);
        this.assemblyDisplayPrefs = pAssemblyDisplayPrefs;
        this.colorPrefs = pColorPrefs;
        this.selectionManager = pSelectionManager;
        this.selectionManager.addAssemblySelectionListener(this);
        this.selectionMotionManager = pSelectionMotionManager;
        this.selectionMotionManager.addAssemblySelectionMotionListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.onscreenLocationManager = pOnscreenLocationManager;
        AppUtil.addWindowListener(this);
    }

    /**
     * Describe <code>paint</code> method here.
     *
     *
     * @param g a <code>Graphics</code> value
     * 
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (this.lastSupportingRectangle != null) {
            IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
            if (assembly != null) {
                try {
                    Color originalColor = g.getColor();
                    if (this.positionPeriods == null || this.positionPeriodThresholds == null) {
                        this.calculatePositionPeriodThresholds();
                    }
                    int size;
                    if (this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences()) {
                        size = assembly.getFullRangeLength();
                    } else {
                        size = assembly.getGappedSize();
                    }
                    double mag = this.displayerPrefs.getOverviewHorizontalMagnification();
                    int width = this.defaultCharacterMetrics.width;
                    BasecallDisplayerUtils.findMonospacedBasecallIndices(width * mag, size, this.lastSupportingRectangle, this.paintIndices);
                    int start = this.paintIndices[0];
                    int stop = this.paintIndices[1];
                    AppUtil.setAntiAliasing(g, true);
                    int heightSoFar;
                    heightSoFar = this.defaultCharacterMetrics.height;
                    g.setColor(originalColor);
                    this.setFont(this.defaultFont);
                    start++;
                    stop++;
                    boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
                    boolean globalNumbering = this.displayerPrefs.isGlobalNumbering();
                    int horizontalFudge = 0;
                    if (fullRangeSequences) {
                        int frOffset = assembly.getFullRangeStartOffset();
                        start += frOffset;
                        stop += frOffset;
                        horizontalFudge -= frOffset;
                    }
                    if (globalNumbering) {
                        int aoffset = this.assemblyCoordinator.getAssembly().getStartOffset();
                        start += aoffset;
                        stop += aoffset;
                        horizontalFudge -= aoffset;
                    }
                    horizontalFudge *= (this.defaultCharacterMetrics.width * this.displayerPrefs.getOverviewHorizontalMagnification());
                    int period = this.displayerPrefs.getOverviewPositionsPeriod();
                    if (!fullRangeSequences || assembly.getFullRangeStartOffset() == 0) {
                        start = period;
                    } else {
                        start = (start / period) * period;
                    }
                    for (int i = start; i <= stop; i += period) {
                        BasecallDisplayerUtils.drawCenteredText(g, this.indexToPoint(i) + horizontalFudge, heightSoFar, Integer.toString(i));
                    }
                    Graphics2D g2 = (Graphics2D) g;
                    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);
                    Composite oc = g2.getComposite();
                    Rectangle rect = g.getClipBounds();
                    Color oldColor = g.getColor();
                    AppUtil.setAntiAliasing(g, false);
                    if (this.startSelectionIndex != TDBAssemblyPositionsSegmentPanel.NOT_SET) {
                        int min = Math.min(this.startSelectionIndex, this.currentSelectionIndex);
                        int max = Math.max(this.startSelectionIndex, this.currentSelectionIndex);
                        min = this.indexToPoint(min);
                        max = this.indexToPoint(max);
                        min += horizontalFudge;
                        max += horizontalFudge;
                        g.setColor(TDBAssemblyPanel.SELECTION_COLOR);
                        if ((min <= (rect.x + rect.width)) && (max >= rect.x)) {
                            g2.setComposite(ac);
                            int ch = this.defaultCharacterMetrics.height;
                            final int vFudge = 3;
                            if (this.currentSelectionType == AssemblySelectionEvent.DATA_TYPE_CONSENSUS) {
                                int startpoint = Math.max(min, rect.x);
                                int redge = rect.x + rect.width - 1;
                                int endpoint = Math.min(max, redge);
                                g2.fillRect(startpoint, vFudge, endpoint - startpoint + 1, ch);
                            } else {
                                int startpoint = Math.max(min, rect.x);
                                int endpoint = Math.min(max, rect.x + rect.width - 1);
                                g.drawLine(startpoint, vFudge, endpoint, vFudge);
                                g.drawLine(startpoint, vFudge + ch - 1, endpoint, vFudge + ch - 1);
                                if (min >= rect.x && min < rect.x + rect.width) {
                                    g.drawLine(min, vFudge + 1, min, vFudge + ch - 2);
                                }
                                if (max >= rect.x && max < rect.x + rect.width) {
                                    g.drawLine(max, vFudge + 1, max, vFudge + ch - 2);
                                }
                            }
                        }
                    }
                    List nuuFeatures = assembly.getAssemblyFeatureMap().getNonUniqueUnitigFeatures();
                    if (nuuFeatures.size() > 0) {
                        Iterator nuuIter = nuuFeatures.iterator();
                        int y, height;
                        y = 2;
                        height = this.getSize().height - 1 - y;
                        int minStartOffset = assembly.getFullRangeStartOffset();
                        while (nuuIter.hasNext()) {
                            INonUniqueUnitigFeature nuu = (INonUniqueUnitigFeature) nuuIter.next();
                            if (nuu.isClosed()) continue;
                            int x, wid;
                            int min = Math.min(nuu.getStartCoordinate(), nuu.getEndCoordinate());
                            min = Math.max(0, min);
                            int max = Math.max(nuu.getStartCoordinate(), nuu.getEndCoordinate());
                            max = Math.min(max, assembly.getEndOffset() - assembly.getStartOffset());
                            max++;
                            if (fullRangeSequences) {
                                min -= minStartOffset;
                                max -= minStartOffset;
                            }
                            min = this.indexToPoint(min);
                            max = this.indexToPoint(max);
                            max--;
                            x = min;
                            wid = max - min + 1;
                            Rectangle nuuRect = new Rectangle(x, y, wid, height);
                            Rectangle iRect = rect.intersection(nuuRect);
                            ac = TDBAssemblyPanel.NUU_COMPOSITE;
                            if (iRect.height > 0 && iRect.width > 0) {
                                g2.setComposite(ac);
                                g2.setColor(TDBAssemblyPanel.NUU_COLOR);
                                g2.fillRect(iRect.x, iRect.y, iRect.width, iRect.height);
                            }
                        }
                    }
                    g2.setComposite(oc);
                    g.setColor(oldColor);
                } catch (SeqdataException sx) {
                    Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
                }
            }
        }
    }

    /**
     * Measure ourselves, learning all the key things about character
     * metrics we'll need to know.
     *
     * */
    private void measure() {
        this.defaultFont = this.getFont();
        this.defaultCharacterMetrics = BasecallDisplayerUtils.measure(this.getFont(), this);
        this.measured = true;
    }

    /**
     * Describe <code>getPreferredSize</code> method here.
     *
     *
     * @return a <code>Dimension</code> value
     *
     */
    public Dimension getPreferredSize() {
        Dimension ret = TDBAssemblyPositionsSegmentPanel.EMPTY_DIMENSION;
        IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
        if (assembly != null) {
            if (!this.measured) {
                this.measure();
            }
            ret = new Dimension(0, this.defaultCharacterMetrics.heightWithDescent);
        }
        return ret;
    }

    /**
     * Describe <code>horizontalMagnificationChange</code> method here.
     *
     *
     * @param pNewMag a <code>double</code> value
     * 
     * @param pOldMag a <code>double</code> value
     * 
     */
    public void horizontalMagnificationChange(double pNewMag, double pOldMag) {
    }

    /**
     * Describe <code>positionsPeriodChange</code> method here.
     *
     *
     * @param pNewPeriod an <code>int</code> value
     * 
     * @param pOldPeriod an <code>int</code> value
     * 
     */
    public void positionsPeriodChange(int pNewPeriod, int pOldPeriod) {
    }

    /**
     * Fire a geometry change event to any registered
     * <code>IChildGeometryChangeListener</code>s.
     *
     * */
    private void fireGeometryChange() {
        IChildGeometryChangeListener listener;
        Iterator iter = this.geometryChangeListeners.iterator();
        while (iter.hasNext()) {
            listener = (IChildGeometryChangeListener) iter.next();
            listener.childGeometryChange(this);
        }
    }

    /**
     * Add a <code>IChildGeometryChangeListener</code> to our list of
     * listeners.
     *
     *
     * @param pListener an <code>IChildGeometryChangeListener</code> value
     * 
     */
    public void addChildGeometryChangeListener(IChildGeometryChangeListener pListener) {
        this.geometryChangeListeners.add(pListener);
    }

    /**
     * Remove a <code>IChildGeometryChangeListener</code> from our
     * list of listeners.
     *
     *
     * @param pListener an <code>IChildGeometryChangeListener</code> value
     * 
     */
    public void removeChildGeometryChangeListener(IChildGeometryChangeListener pListener) {
        this.geometryChangeListeners.remove(pListener);
    }

    /**
     * Describe <code>adjustmentValueChanged</code> method here.
     *
     *
     * @param ae an <code>AdjustmentEvent</code> value
     * 
     */
    public void adjustmentValueChanged(AdjustmentEvent ae) {
        Component comp = (Component) ae.getSource();
        JScrollPane sp = (JScrollPane) comp.getParent();
        JViewport vp = sp.getViewport();
        if (this.horizontalOffset == TDBAssemblyPositionsSegmentPanel.NOT_SET) {
            this.horizontalOffset = sp.getInsets().left;
        }
        Rectangle rect = vp.getViewRect();
        if (this.lastSupportingRectangle == null || !this.lastSupportingRectangle.equals(rect)) {
            this.lastSupportingRectangle = vp.getViewRect();
            this.repaint();
        }
    }

    /**
     * <code>IBasecallDisplayerPreferencesChangeListener</code>
     * method, nooped.
     *
     *
     * @param pNewMag a <code>double</code> value
     * 
     * @param pOldMag a <code>double</code> value
     * 
     */
    public void overviewHorizontalMagnificationChange(double pNewMag, double pOldMag) {
        try {
            int lastPeriod = this.displayerPrefs.getOverviewPositionsPeriod();
            int newPeriod = this.lookupPeriod();
            if (lastPeriod != newPeriod) {
                this.displayerPrefs.setOverviewPositionsPeriod(newPeriod);
            }
            this.fireGeometryChange();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
            String message = ResourceUtil.getResource(TDBAssemblyPositionsSegmentPanel.class, "message.caught_seqdata_exception");
            UserMessage.message(sx, message);
        }
    }

    /**
     * <code>IBasecallDisplayerPreferencesChangeListener</code>
     * method, nooped.
     *
     *
     * @param pNewPeriod an <code>int</code> value
     * 
     * @param pOldPeriod an <code>int</code> value
     * 
     */
    public void overviewPositionsPeriodChange(int pNewPeriod, int pOldPeriod) {
        this.repaint();
    }

    /**
     * Describe <code>mouseDragged</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseDragged(MouseEvent pEvent) {
        int x = pEvent.getX();
        if (x < 0) {
            this.onscreenLocationManager.autoscrollLeft(this);
        } else if (x >= this.getSize().width) {
            this.onscreenLocationManager.autoscrollRight(this);
        } else {
            try {
                this.onscreenLocationManager.breakAutoscroll(this);
                boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
                int index = this.pointToIndex(pEvent.getX());
                if (fullRangeSequences) {
                    IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
                    index += assembly.getFullRangeStartOffset();
                }
                if (index != this.currentSelectionIndex) {
                    this.currentSelectionIndex = index;
                    this.fireAssemblySelectionMotionEvent(pEvent, false);
                }
            } catch (SeqdataException sx) {
                Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
            }
        }
    }

    /**
     * Describe <code>mouseMoved</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseMoved(MouseEvent pEvent) {
    }

    /**
     * Describe <code>mouseClicked</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseClicked(MouseEvent pEvent) {
    }

    /**
     * Describe <code>mousePressed</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mousePressed(MouseEvent pEvent) {
        this.requestFocus();
        try {
            this.startSelectionIndex = this.pointToIndex(pEvent.getX());
            boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
            if (fullRangeSequences) {
                IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
                this.startSelectionIndex += assembly.getFullRangeStartOffset();
            }
            this.currentSelectionIndex = this.startSelectionIndex;
            this.fireAssemblySelectionMotionEvent(pEvent, true);
            this.evaluateSelectionEvent(pEvent, TDBAssemblyPositionsSegmentPanel.SELECTION_START);
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    /**
     * Describe <code>mouseReleased</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseReleased(MouseEvent pEvent) {
        try {
            this.onscreenLocationManager.breakAutoscroll(this);
            this.evaluateSelectionEvent(pEvent, TDBAssemblyPositionsSegmentPanel.SELECTION_END);
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    /**
     * Describe <code>mouseEntered</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseEntered(MouseEvent pEvent) {
    }

    /**
     * Describe <code>mouseExited</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     */
    public void mouseExited(MouseEvent pEvent) {
    }

    /**
     * Describe <code>evaluateSelectionEvent</code> method here.
     *
     *
     * @param pEvent a <code>MouseEvent</code> value
     * 
     * @param type an <code>int</code> value
     * 
     */
    private void evaluateSelectionEvent(MouseEvent pEvent, int type) throws SeqdataException {
        boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
        int i = this.pointToIndex(pEvent.getX());
        IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
        if (fullRangeSequences) {
            i += assembly.getFullRangeStartOffset();
        }
        i = BasecallDisplayerUtils.rangeCheckConsensusSelectionIndex(assembly, this.assemblyDisplayPrefs, i);
        if (type == TDBAssemblyPositionsSegmentPanel.SELECTION_START) {
            this.startSelectionIndex = i;
        } else {
            int endIndex = i;
            AssemblyConsensusSelectionEvent event = new AssemblyConsensusSelectionEvent(this, this.startSelectionIndex, endIndex);
            this.selectionManager.fireAssemblySelectionEvent(event);
        }
    }

    /**
     * Describe <code>assemblyConsensusSelectionMotionChange</code> method here.
     *
     *
     * @param pEvent an <code>AssemblyConsensusSelectionMotionEvent</code> value
     * 
     */
    public void assemblyConsensusSelectionMotionChange(AssemblyConsensusSelectionMotionEvent pEvent) {
        try {
            int index;
            IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
            if (pEvent.isStartEvent()) {
                index = pEvent.getIndex();
                index = BasecallDisplayerUtils.rangeCheckConsensusSelectionIndex(assembly, this.assemblyDisplayPrefs, index);
                this.startSelectionIndex = index;
            }
            index = pEvent.getIndex();
            index = BasecallDisplayerUtils.rangeCheckConsensusSelectionIndex(assembly, this.assemblyDisplayPrefs, index);
            this.currentSelectionIndex = index;
            this.currentSelectionType = pEvent.getDataType();
            this.repaint();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    /**
     * Describe <code>assemblySequenceSelectionMotionChange</code> method here.
     *
     *
     * @param pEvent an <code>AssemblySequenceSelectionMotionEvent</code> value
     * 
     */
    public void assemblySequenceSelectionMotionChange(AssemblySequenceSelectionMotionEvent pEvent) {
        try {
            int index;
            IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
            if (pEvent.isStartEvent()) {
                index = pEvent.getIndex();
                String seq_name = pEvent.getSequenceName();
                this.currentSelectingSequence = assembly.getSequenceByName(seq_name);
                index = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, index);
                this.startSelectionIndex = index;
            }
            index = pEvent.getIndex();
            index = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, index);
            this.currentSelectionIndex = index;
            this.currentSelectionType = pEvent.getDataType();
            this.repaint();
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    /**
     * Describe <code>fireAssemblySelectionMotionEvent</code> method here.
     *
     *
     * @param pEvent an <code>MouseEvent</code> value
     * 
     */
    public void fireAssemblySelectionMotionEvent(MouseEvent pEvent, boolean pStart) {
        AssemblyConsensusSelectionMotionEvent motionEvent = new AssemblyConsensusSelectionMotionEvent(this, this.currentSelectionIndex, pStart);
        this.selectionMotionManager.fireAssemblySelectionMotionEvent(motionEvent);
    }

    /**
     * <code>IAssemblySelectionListener</code> method.
     *
     *
     * @param pEvent an <code>AssemblySelectionEvent</code> value
     * 
     */
    public void assemblySequenceSelectionChange(AssemblySequenceSelectionEvent pEvent) {
        this.currentSelectionType = AssemblySelectionEvent.DATA_TYPE_SEQUENCE;
        if (pEvent.isDeselectionEvent()) {
            this.startSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;
            this.currentSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;
        } else {
            try {
                String seq_name = pEvent.getSequenceName();
                IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
                this.currentSelectingSequence = assembly.getSequenceByName(seq_name);
                boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
                if (pEvent.isWhollySelected()) {
                    IBaseAssemblySequence seq = this.currentSelectingSequence;
                    int start, end;
                    if (fullRangeSequences) {
                        start = seq.getFullRangeStartOffset();
                        end = seq.getFullRangeEndOffset() + 1;
                    } else {
                        start = seq.getStartOffset();
                        end = seq.getEndOffset() + 1;
                    }
                    start = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, start);
                    end = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, end);
                    this.startSelectionIndex = start;
                    this.currentSelectionIndex = end;
                } else {
                    int index;
                    index = pEvent.getStartIndex();
                    index = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, index);
                    this.startSelectionIndex = index;
                    index = pEvent.getEndIndex();
                    index = BasecallDisplayerUtils.rangeCheckCurrentSelectingSequenceIndex(assembly, this.currentSelectingSequence, this.assemblyDisplayPrefs, index);
                    this.currentSelectionIndex = index;
                }
            } catch (SeqdataException sx) {
                Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
            }
        }
        this.repaint();
    }

    /**
     * <code>IAssemblySelectionListener</code> method.
     *
     *
     * @param pEvent an <code>AssemblySelectionEvent</code> value
     * 
     */
    public void assemblyConsensusSelectionChange(AssemblyConsensusSelectionEvent pEvent) {
        this.currentSelectionType = AssemblySelectionEvent.DATA_TYPE_CONSENSUS;
        if (pEvent.isDeselectionEvent()) {
            this.startSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;
            this.currentSelectionIndex = TDBAssemblyPositionsSegmentPanel.NOT_SET;
        } else {
            this.startSelectionIndex = pEvent.getStartIndex();
            this.currentSelectionIndex = pEvent.getEndIndex();
        }
        this.repaint();
    }

    /**
     * <code>IAssemblyCoordinatorListener</code> method, the assembly
     * coordinated by our coordinator has changed.
     *
     *
     * @param pNewAssembly an <code>IBaseAssembly</code> value
     * 
     * @param pOldAssembly an <code>IBaseAssembly</code> value
     * 
     */
    public void assemblyCoordinatorChange(IBaseAssembly pNewAssembly, IBaseAssembly pOldAssembly) {
        if (pNewAssembly != null) {
            pNewAssembly.addAssemblyComplementListener(this);
        }
        if (pOldAssembly != null) {
            pOldAssembly.removeAssemblyComplementListener(this);
        }
        this.positionPeriods = null;
        this.positionPeriodThresholds = null;
        this.fireGeometryChange();
    }

    public int indexToPoint(int pIndex) {
        int ret;
        ret = (int) (pIndex * this.defaultCharacterMetrics.width * this.displayerPrefs.getOverviewHorizontalMagnification());
        ret += this.horizontalOffset;
        ret -= this.lastSupportingRectangle.x;
        return ret;
    }

    public int pointToIndex(int pPoint) {
        int ret = pPoint;
        ret = ret + this.lastSupportingRectangle.x - this.horizontalOffset;
        ret = (int) (ret / (this.defaultCharacterMetrics.width * this.displayerPrefs.getOverviewHorizontalMagnification()));
        return ret;
    }

    public void scrolledLeft(Point pPoint) {
        try {
            int tmp = this.pointToIndex(pPoint.x - this.lastSupportingRectangle.x - this.horizontalOffset);
            boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
            IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
            int minOffset = 0;
            if (fullRangeSequences) {
                minOffset = assembly.getFullRangeStartOffset();
                tmp += minOffset;
            }
            if (tmp != this.currentSelectionIndex) {
                AssemblyConsensusSelectionMotionEvent event = new AssemblyConsensusSelectionMotionEvent(this, tmp);
                this.selectionMotionManager.fireAssemblySelectionMotionEvent(event);
            } else if (tmp == minOffset) {
                this.onscreenLocationManager.breakAutoscroll(this);
            }
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    public void scrolledRight(Point pPoint) {
        try {
            int tmp = this.pointToIndex(pPoint.x - this.lastSupportingRectangle.x - this.horizontalOffset + this.getSize().width - 1);
            boolean fullRangeSequences = this.assemblyDisplayPrefs.getShowFullRangeAssemblySequences();
            IBaseAssembly assembly = this.assemblyCoordinator.getAssembly();
            int maxOffset = assembly.getGappedSize() - 1;
            if (fullRangeSequences) {
                tmp += assembly.getFullRangeStartOffset();
                maxOffset = assembly.getFullRangeEndOffset();
            }
            if (tmp != this.currentSelectionIndex) {
                AssemblyConsensusSelectionMotionEvent event = new AssemblyConsensusSelectionMotionEvent(this, tmp);
                this.selectionMotionManager.fireAssemblySelectionMotionEvent(event);
            } else if (tmp == maxOffset + 1) {
                this.onscreenLocationManager.breakAutoscroll(this);
            }
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    /**
     * Describe <code>assemblyDisplayPreferencesReverseComplementChange</code> method here.
     *
     *
     * @param pPrefs an <code>IAssemblyDisplayPreferences</code> value
     * 
     * @param pCurrentState a <code>boolean</code> value
     * 
     */
    public void assemblyComplemented(IBaseAssembly pAssembly, boolean pCurrentState) {
        try {
            if (this.currentSelectionIndex != TDBAssemblyPositionsSegmentPanel.NOT_SET) {
                this.currentSelectionIndex = pAssembly.complementOffset(this.currentSelectionIndex);
                this.startSelectionIndex = pAssembly.complementOffset(this.startSelectionIndex);
                this.currentSelectionIndex++;
                this.startSelectionIndex++;
            }
        } catch (SeqdataException sx) {
            Log.log(Log.ERROR, new Throwable(), sx, ResourceUtil.getMessage(TDBAssemblyPositionsSegmentPanel.class, "caught_seqdata_exception"));
        }
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.zoomActuationListener.zoomInitiated();
        }
    }

    public void keyReleased(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_CONTROL) {
            this.zoomActuationListener.zoomTerminated();
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    private void calculatePositionPeriodThresholds() throws SeqdataException {
        final int MAX_EXPONENT = 5;
        final int MIN_EXPONENT = 1;
        final int[] vals_within_exponent = new int[] { 1, 2, 5 };
        int size = this.assemblyCoordinator.getAssembly().getGappedSize();
        if (this.displayerPrefs.isGlobalNumbering()) {
            size += this.assemblyCoordinator.getAssembly().getStartOffset();
        }
        FontMetrics fm = this.getFontMetrics(this.defaultFont);
        int sw = fm.stringWidth(new Integer(size).toString());
        final int INTER_MARKER_SPACING = 100;
        sw = (int) Math.ceil((sw + INTER_MARKER_SPACING) / 10.0) * 10;
        this.positionPeriodThresholds = new double[vals_within_exponent.length * (MAX_EXPONENT - MIN_EXPONENT + 1)];
        this.positionPeriods = new int[positionPeriodThresholds.length];
        int i = 0;
        for (int exp = MIN_EXPONENT; exp <= MAX_EXPONENT; exp++) {
            for (int vali = 0; vali < vals_within_exponent.length; vali++) {
                this.positionPeriods[i] = (int) (vals_within_exponent[vali] * Math.pow(10, exp));
                this.positionPeriodThresholds[i] = sw / (double) (this.positionPeriods[i] * this.defaultCharacterMetrics.width);
                i++;
            }
        }
    }

    private int lookupPeriod() throws SeqdataException {
        double currentMag = this.displayerPrefs.getOverviewHorizontalMagnification();
        int i;
        if (this.positionPeriodThresholds == null || this.positionPeriods == null) {
            this.calculatePositionPeriodThresholds();
        }
        for (i = 0; i < this.positionPeriodThresholds.length; i++) {
            if (currentMag > this.positionPeriodThresholds[i]) {
                i--;
                break;
            }
        }
        i = Math.max(0, i);
        i = Math.min(this.positionPeriodThresholds.length - 1, i);
        return this.positionPeriods[i];
    }

    public void globalNumberingChange(boolean pIsGlobal, boolean pWasGlobal) {
        this.repaint();
    }

    public void windowCreated(IWindow ui) {
    }

    public void windowDestroyed(IWindow ui) {
        if (ui == parentWindow) {
            this.assemblyCoordinator.removeAssemblyCoordinatorListener(this);
            this.assemblyCoordinator = null;
            this.zoomActuationListener = null;
            this.displayerPrefs.removeBasecallDisplayerPreferencesChangeListener(this);
            this.displayerPrefs = null;
            this.selectionManager.removeAssemblySelectionListener(this);
            this.selectionManager = null;
            this.selectionMotionManager.removeAssemblySelectionMotionListener(this);
            this.selectionMotionManager = null;
            parentWindow = null;
            AppUtil.removeWindowListener(this);
        }
    }
}
