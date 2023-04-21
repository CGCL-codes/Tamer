package gov.nasa.gsfc.visbard.vis;

import gov.nasa.gsfc.visbard.gui.MhdPanel;
import gov.nasa.gsfc.visbard.model.Dataset;
import gov.nasa.gsfc.visbard.model.MhdCutplane;
import gov.nasa.gsfc.visbard.model.MhdDataAccessor;
import gov.nasa.gsfc.visbard.model.VisbardMain;
import gov.nasa.gsfc.visbard.repository.resource.Resource;
import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VisbardException;
import gov.nasa.gsfc.visbard.vis.coord.CoordTransforms;
import gsfc.nssdc.cdf.CDFException;
import gsfc.nssdc.cdf.util.Epoch;
import java.awt.Dimension;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.vecmath.Point3f;
import visad.ConstantMap;
import visad.ContourControl;
import visad.DataReferenceImpl;
import visad.Display;
import visad.DisplayImpl;
import visad.FieldImpl;
import visad.FlatField;
import visad.FlowControl;
import visad.FunctionType;
import visad.GraphicsModeControl;
import visad.Integer1DSet;
import visad.Linear2DSet;
import visad.RealTupleType;
import visad.RealType;
import visad.SI;
import visad.ScalarMap;
import visad.Set;
import visad.SetType;
import visad.Unit;
import visad.VisADException;
import visad.util.LabeledColorWidget;

public class MhdCutplane3D extends MhdDataAccessor implements MhdCutplane {

    public int fAxis;

    public float fColorMin;

    public float fColorMax;

    public boolean fIsAutoColorScaling;

    public boolean fIsLogScalingColor;

    public boolean fIsContourLabelsEnabled;

    public double fRefTime;

    public float[][] fCutplaneColorValsGse;

    public float[][][] fCutplaneVectorValsGse;

    public float fCutplanePosition;

    public float[] fCutplaneCornersAndRes;

    public String fSelectedColorVar, fSelectedVectorVar;

    public int fNumContours;

    public float fVectorScale;

    public boolean fIsStreamlinePlottingEnabled;

    public boolean fIsVectorNormalizationEnabled;

    public float fStreamlineDensity;

    public float fStreamlineStepFactor;

    private DisplayImpl fDisplay = null;

    private int fCurrentTaskId = -1;

    private double fBestMhdTime = Double.MAX_VALUE;

    private DataReferenceImpl fDataRefColor = null;

    private DataReferenceImpl fDataRefIso = null;

    private DataReferenceImpl fDataRefVector = null;

    private static FieldImpl fTimeFieldScalar[] = { null, null, null };

    private static FieldImpl fTimeFieldVector[] = { null, null, null };

    private static ArrayList<File> fMhdFileCache = new ArrayList<File>();

    public MhdCutplane3D() {
        super("Performing MHD data operation", false);
    }

    public void execute() throws Exception {
        if (fCurrentTaskId == MhdCutplane.TASK_RETRIEVE_DATA) this.retrieveData(); else if (fCurrentTaskId == MhdCutplane.TASK_RENDER_DATA) this.renderData();
        return;
    }

    /**
	 * Checks if the currently-rendered cutplane is still valid for the 
	 * currently-selected time range
	 */
    public boolean isCurrentRenderingValid() throws VisbardException {
        double newClosestTime = super.getClosestMhdTimeToVisbardRange();
        return (newClosestTime == fBestMhdTime);
    }

    private void retrieveData() throws Exception {
        this.setProgress(0.0f);
        File mhdFiles[] = super.getMhdFiles();
        if (fMhdFileCache.size() == 0) {
            for (int i = 0; i < mhdFiles.length; i++) fMhdFileCache.add(mhdFiles[i]);
        } else if (!isMhdCacheValid(mhdFiles)) {
            for (int i = 0; i < 3; i++) {
                fTimeFieldScalar[i] = null;
                fTimeFieldVector[i] = null;
            }
            fMhdFileCache.clear();
            for (int i = 0; i < mhdFiles.length; i++) fMhdFileCache.add(mhdFiles[i]);
        }
        fBestMhdTime = super.getClosestMhdTimeToVisbardRange();
        int bestMhdIndex = super.getClosestMhdIndexToVisbardRange();
        String mhdCoordSys = super.getMhdCoordSys();
        float xMin = fCutplaneCornersAndRes[0];
        float xMax = fCutplaneCornersAndRes[1];
        float xRes = fCutplaneCornersAndRes[2];
        float yMin = fCutplaneCornersAndRes[3];
        float yMax = fCutplaneCornersAndRes[4];
        float yRes = fCutplaneCornersAndRes[5];
        float zMin = fCutplaneCornersAndRes[6];
        float zMax = fCutplaneCornersAndRes[7];
        float zRes = fCutplaneCornersAndRes[8];
        int xNum = (int) ((xMax - xMin) / xRes) + 1;
        int yNum = (int) ((yMax - yMin) / yRes) + 1;
        int zNum = (int) ((zMax - zMin) / zRes) + 1;
        boolean isCachedColorDataAvail[] = new boolean[] { false, false, false };
        boolean isCachedVectorDataAvail[] = new boolean[] { false, false, false };
        int iIndex1 = -1;
        int iIndex2 = -1;
        int jIndex1 = -1;
        int jIndex2 = -1;
        int NCOLS = -1;
        int NROWS = -1;
        int NTIMESTEPS = mhdFiles.length;
        int vecComponentIdxI = -1;
        int vecComponentIdxJ = -1;
        if (fAxis == MhdPanel.CUTPLANE_X) {
            this.setTaskTitle("Interpolating MHD Data for Y-Z Cut Plane");
            iIndex1 = 3;
            iIndex2 = 4;
            jIndex1 = 6;
            jIndex2 = 7;
            NCOLS = yNum;
            NROWS = zNum;
            vecComponentIdxI = 1;
            vecComponentIdxJ = 2;
        } else if (fAxis == MhdPanel.CUTPLANE_Y) {
            this.setTaskTitle("Interpolating MHD Data for X-Z Cut Plane");
            iIndex1 = 0;
            iIndex2 = 1;
            jIndex1 = 6;
            jIndex2 = 7;
            NCOLS = xNum;
            NROWS = zNum;
            vecComponentIdxI = 0;
            vecComponentIdxJ = 2;
        } else if (fAxis == MhdPanel.CUTPLANE_Z) {
            this.setTaskTitle("Interpolating MHD Data for X-Y Cut Plane");
            iIndex1 = 0;
            iIndex2 = 1;
            jIndex1 = 3;
            jIndex2 = 4;
            NCOLS = xNum;
            NROWS = yNum;
            vecComponentIdxI = 0;
            vecComponentIdxJ = 1;
        }
        fCutplaneColorValsGse = new float[NCOLS][NROWS];
        fCutplaneVectorValsGse = new float[NCOLS][NROWS][2];
        if (isCacheHit(fTimeFieldScalar, bestMhdIndex, fSelectedColorVar)) isCachedColorDataAvail[fAxis] = true;
        if (isCacheHit(fTimeFieldVector, bestMhdIndex, fSelectedVectorVar + "_x")) isCachedVectorDataAvail[fAxis] = true;
        if (!isCachedColorDataAvail[fAxis] || ((fSelectedVectorVar != null) && !isCachedVectorDataAvail[fAxis])) {
            super.open(mhdFiles[bestMhdIndex].getAbsolutePath());
            super.preloadScalarData(fSelectedColorVar);
            if ((fSelectedVectorVar != null)) {
                super.preloadVectorData(fSelectedVectorVar);
            }
            for (int i = 0; i < NCOLS; i++) {
                this.setProgress(i / (float) NCOLS);
                for (int j = 0; j < NROWS; j++) {
                    double curMhdCoord[] = null;
                    Point3f curMhdCoordPoint = new Point3f();
                    if (fAxis == MhdPanel.CUTPLANE_X) curMhdCoord = new double[] { fCutplanePosition, yMin + i * yRes, zMin + j * zRes }; else if (fAxis == MhdPanel.CUTPLANE_Y) curMhdCoord = new double[] { xMin + i * xRes, fCutplanePosition, zMin + j * zRes }; else if (fAxis == MhdPanel.CUTPLANE_Z) curMhdCoord = new double[] { xMin + i * xRes, yMin + j * yRes, fCutplanePosition };
                    curMhdCoordPoint.set((float) curMhdCoord[0], (float) curMhdCoord[1], (float) curMhdCoord[2]);
                    if (isBlockoutDistanceFromEarth(curMhdCoord[0], curMhdCoord[1], curMhdCoord[2])) {
                        fCutplaneColorValsGse[i][j] = Float.NaN;
                        fCutplaneVectorValsGse[i][j][0] = Float.NaN;
                        fCutplaneVectorValsGse[i][j][1] = Float.NaN;
                    } else {
                        fCutplaneColorValsGse[i][j] = super.getMhdScalarAtPoint(curMhdCoordPoint, fSelectedColorVar, fBestMhdTime);
                        if (fSelectedVectorVar != null) {
                            float vectorValsGse3D[] = super.getMhdVectorAtPoint(curMhdCoordPoint, fSelectedVectorVar, fBestMhdTime);
                            fCutplaneVectorValsGse[i][j][0] = vectorValsGse3D[vecComponentIdxI];
                            fCutplaneVectorValsGse[i][j][1] = vectorValsGse3D[vecComponentIdxJ];
                        }
                    }
                }
            }
            super.close();
        }
        RealType selectedVarColor, selectedVarIso, selectedVarVectorX, selectedVarVectorY;
        RealTupleType domainTupleCutplane;
        FunctionType funcColorMapping, funcMapperIso, funcVectorMapping;
        FunctionType funcTimestepToColorData, funcTimestepToVectorData;
        Set domainSetTime;
        Set domainSetCutplane;
        FlatField flatFieldCutplaneColor, flatFieldCutplaneIso, flatFieldCutplaneVector = null;
        this.setTaskTitle("Rendering Interpolated Data");
        selectedVarColor = RealType.getRealType(fSelectedColorVar);
        selectedVarIso = RealType.getRealType("isoVar");
        selectedVarVectorX = RealType.getRealType(fSelectedVectorVar + "_x");
        selectedVarVectorY = RealType.getRealType(fSelectedVectorVar + "_y");
        domainTupleCutplane = new RealTupleType(RealType.XAxis, RealType.YAxis);
        funcColorMapping = new FunctionType(domainTupleCutplane, selectedVarColor);
        domainSetCutplane = new Linear2DSet(domainTupleCutplane, fCutplaneCornersAndRes[iIndex1], fCutplaneCornersAndRes[iIndex2], NCOLS, fCutplaneCornersAndRes[jIndex1], fCutplaneCornersAndRes[jIndex2], NROWS);
        Unit msUnit = SI.second.scale(0.001);
        RealType epochIndex = RealType.getRealType("epochIndex", msUnit, null);
        funcTimestepToColorData = new FunctionType(epochIndex, funcColorMapping);
        domainSetTime = new Integer1DSet(epochIndex, NTIMESTEPS);
        if ((fTimeFieldScalar[fAxis] == null) || (!getCurrentlyCachedVariable(fTimeFieldScalar).equals(fSelectedColorVar))) fTimeFieldScalar[fAxis] = new FieldImpl(funcTimestepToColorData, domainSetTime);
        RealTupleType vectorRangeTuple = new RealTupleType(selectedVarVectorX, selectedVarVectorY);
        funcVectorMapping = new FunctionType(domainTupleCutplane, vectorRangeTuple);
        funcTimestepToVectorData = new FunctionType(epochIndex, funcVectorMapping);
        if ((fTimeFieldVector[fAxis] == null) || ((fSelectedVectorVar != null) && (!getCurrentlyCachedVariable(fTimeFieldVector).equals(fSelectedVectorVar + "_x")))) fTimeFieldVector[fAxis] = new FieldImpl(funcTimestepToVectorData, domainSetTime);
        float[][] flatSamplesColor = new float[1][NCOLS * NROWS];
        float[][] flatSamplesVector = new float[2][NCOLS * NROWS];
        if (isCachedColorDataAvail[fAxis]) {
            flatFieldCutplaneColor = (FlatField) ((FlatField) fTimeFieldScalar[fAxis].getSample(bestMhdIndex)).clone();
            flatSamplesColor = flatFieldCutplaneColor.getFloats();
        } else {
            for (int r = 0; r < NROWS; r++) for (int c = 0; c < NCOLS; c++) flatSamplesColor[0][r * NCOLS + c] = fCutplaneColorValsGse[c][r];
            flatFieldCutplaneColor = new FlatField(funcColorMapping, domainSetCutplane);
            flatFieldCutplaneColor.setSamples(flatSamplesColor);
            fTimeFieldScalar[fAxis].setSample(bestMhdIndex, (FlatField) flatFieldCutplaneColor.clone());
        }
        if (fIsLogScalingColor) {
            for (int r = 0; r < NROWS; r++) {
                for (int c = 0; c < NCOLS; c++) {
                    if (flatSamplesColor[0][r * NCOLS + c] < 0.1) flatSamplesColor[0][r * NCOLS + c] = -1f; else flatSamplesColor[0][r * NCOLS + c] = new Float(Math.log10(flatSamplesColor[0][r * NCOLS + c]));
                }
            }
            flatFieldCutplaneColor.setSamples(flatSamplesColor);
        }
        if (fIsAutoColorScaling) {
            for (int r = 0; r < NROWS; r++) {
                for (int c = 0; c < NCOLS; c++) {
                    float curVal = flatSamplesColor[0][r * NCOLS + c];
                    if (!Float.isNaN(curVal)) {
                        fColorMin = Math.min(curVal, fColorMin);
                        fColorMax = Math.max(curVal, fColorMax);
                    }
                }
            }
        }
        fDataRefColor = new DataReferenceImpl("color_data_ref");
        fDataRefColor.setData(flatFieldCutplaneColor);
        if (fSelectedVectorVar != null) {
            if (isCachedVectorDataAvail[fAxis]) {
                flatFieldCutplaneVector = (FlatField) ((FlatField) fTimeFieldVector[fAxis].getSample(bestMhdIndex)).clone();
                flatSamplesVector = flatFieldCutplaneVector.getFloats();
            } else {
                for (int r = 0; r < NROWS; r++) for (int c = 0; c < NCOLS; c++) {
                    flatSamplesVector[0][r * NCOLS + c] = fCutplaneVectorValsGse[c][r][0];
                    flatSamplesVector[1][r * NCOLS + c] = fCutplaneVectorValsGse[c][r][1];
                }
                flatFieldCutplaneVector = new FlatField(funcVectorMapping, domainSetCutplane);
                flatFieldCutplaneVector.setSamples(flatSamplesVector);
                fTimeFieldVector[fAxis].setSample(bestMhdIndex, (FlatField) flatFieldCutplaneVector.clone());
            }
            if (fIsVectorNormalizationEnabled && !fIsStreamlinePlottingEnabled) {
                for (int r = 0; r < NROWS; r++) {
                    for (int c = 0; c < NCOLS; c++) {
                        double vecMagnitude = Math.sqrt(Math.pow(flatSamplesVector[0][r * NCOLS + c], 2d) + Math.pow(flatSamplesVector[1][r * NCOLS + c], 2d));
                        flatSamplesVector[0][r * NCOLS + c] /= vecMagnitude;
                        flatSamplesVector[1][r * NCOLS + c] /= vecMagnitude;
                    }
                }
                flatFieldCutplaneVector.setSamples(flatSamplesVector);
            }
            fDataRefVector = new DataReferenceImpl("vector_data_ref");
            fDataRefVector.setData(flatFieldCutplaneVector);
        }
        if (fNumContours != -1) {
            funcMapperIso = new FunctionType(domainTupleCutplane, selectedVarIso);
            flatFieldCutplaneIso = new FlatField(funcMapperIso, domainSetCutplane);
            float flatIsoVals[][] = flatFieldCutplaneColor.getFloats(false);
            flatFieldCutplaneIso.setSamples(flatIsoVals, false);
            fDataRefIso = new DataReferenceImpl("iso_data_ref");
            fDataRefIso.setData(flatFieldCutplaneIso);
        }
        return;
    }

    private void renderData() throws Exception {
        ScalarMap iMap, jMap, varMap, varIsoMap;
        ConstantMap altMap;
        RealType selectedVarColor = RealType.getRealType(fSelectedColorVar);
        RealType selectedVarIso = RealType.getRealType("isoVar");
        RealType selectedVarVectorX = RealType.getRealType(fSelectedVectorVar + "_x");
        RealType selectedVarVectorY = RealType.getRealType(fSelectedVectorVar + "_y");
        GraphicsModeControl dispGMC = (GraphicsModeControl) fDisplay.getGraphicsModeControl();
        dispGMC.setScaleEnable(false);
        iMap = new ScalarMap(RealType.XAxis, Display.XAxis);
        jMap = new ScalarMap(RealType.YAxis, Display.YAxis);
        altMap = new ConstantMap(0d, Display.ZAxis);
        varMap = new ScalarMap(selectedVarColor, Display.RGB);
        varMap.setRange(fColorMin, fColorMax);
        ScalarMap vectorMapX = new ScalarMap(selectedVarVectorX, Display.Flow1X);
        ScalarMap vectorMapY = new ScalarMap(selectedVarVectorY, Display.Flow1Y);
        fDisplay.addMap(iMap);
        fDisplay.addMap(jMap);
        fDisplay.addMap(altMap);
        fDisplay.addMap(varMap);
        fDisplay.getDisplayRenderer().setBoxOn(false);
        if (fSelectedVectorVar != null) {
            fDisplay.addMap(vectorMapX);
            fDisplay.addMap(vectorMapY);
            FlowControl flowControlX = (FlowControl) vectorMapX.getControl();
            FlowControl flowControlY = (FlowControl) vectorMapY.getControl();
            flowControlX.setFlowScale(fVectorScale);
            flowControlY.setFlowScale(fVectorScale);
            flowControlX.enableStreamlines(fIsStreamlinePlottingEnabled);
            flowControlY.enableStreamlines(fIsStreamlinePlottingEnabled);
            if (fIsStreamlinePlottingEnabled) {
                flowControlX.setStreamlineDensity(fStreamlineDensity);
                flowControlY.setStreamlineDensity(fStreamlineDensity);
                flowControlX.setStepFactor(fStreamlineStepFactor);
                flowControlY.setStepFactor(fStreamlineStepFactor);
            }
        }
        if (fNumContours != -1) {
            varIsoMap = new ScalarMap(selectedVarIso, Display.IsoContour);
            fDisplay.addMap(varIsoMap);
            ContourControl isoControl = (ContourControl) varIsoMap.getControl();
            float interval = Math.abs(fColorMax - fColorMin) / (float) fNumContours;
            float base = 0f;
            isoControl.setContourInterval(interval, fColorMin, fColorMax, base);
            isoControl.setAutoScaleLabels(true);
            isoControl.enableLabels(fIsContourLabelsEnabled);
            isoControl.setContourFill(false);
            fDisplay.addReference(fDataRefIso);
        }
        if (fSelectedVectorVar != null) fDisplay.addReference(fDataRefVector);
        fDisplay.addReference(fDataRefColor);
        LabeledColorWidget colorWidget = new LabeledColorWidget(varMap);
        colorWidget.setMinimumSize(new Dimension(350, 100));
        colorWidget.setMaximumSize(new Dimension(425, 200));
        colorWidget.setPreferredSize(new Dimension(350, 140));
        float existingColorTable[][] = VisbardMain.getVisWindow().getMhdColorBarTableCutplane();
        if (existingColorTable != null) colorWidget.setTable(existingColorTable);
        VisbardMain.getVisWindow().setMhdColorbarWidgetCutplane(colorWidget);
        VisbardMain.getVisWindow().setCurrentlyRenderedCutplaneTimeLabel(fBestMhdTime);
    }

    /**
	 * Determines what part of the code will be run when go() is called
	 */
    public void setTask(int taskId) {
        fCurrentTaskId = taskId;
    }

    public boolean isSuccess() {
        return super.isSuccess();
    }

    /**
	 * @param selectedVar	user-selected variable to render
	 * @param axis 			axis as basis for cutplane (as specified in MhdPanel class)
     * @param colorMin		minimum data value to use at bottom of palette
     * @param colorMax		maximum data value to use at bottom of palette
     * @param isAutoColorScaling	boolean defining if color range should be auto-scaled to data range
     * @param isLogScaling	boolean defining whether to apply log10 scaling to data before coloring
     * @param cutplanePosition	position on axis to place cutplane
     * @param cutplaneCornersAndRes	[xMin, xMax, xRes, yMin, yMax, yRes zMin, zMax, zRes] locations/resolutions of cutplane
     * @param numContours	number of contour lines to plot;  if -1, contour plotting is disabled
	 */
    public void setParams(String selectedColorVar, int axis, float colorMin, float colorMax, boolean isAutoColorScaling, boolean isLogScaling, float cutplanePosition, float cutplaneCornersAndRes[], int numContours, boolean isContourLabelsEnabled, String selectedVectorVar, float vectorScale, boolean isVectorNormalizationEnabled, boolean isStreamlinePlottingEnabled, float streamlineDensity, float streamlineStepFactor) {
        fSelectedColorVar = selectedColorVar;
        fAxis = axis;
        fColorMin = colorMin;
        fColorMax = colorMax;
        fIsAutoColorScaling = isAutoColorScaling;
        fIsLogScalingColor = isLogScaling;
        fCutplanePosition = cutplanePosition;
        fCutplaneCornersAndRes = cutplaneCornersAndRes;
        fNumContours = numContours;
        fIsContourLabelsEnabled = isContourLabelsEnabled;
        fSelectedVectorVar = selectedVectorVar;
        fVectorScale = vectorScale;
        fIsVectorNormalizationEnabled = isVectorNormalizationEnabled;
        fIsStreamlinePlottingEnabled = isStreamlinePlottingEnabled;
        fStreamlineDensity = streamlineDensity;
        fStreamlineStepFactor = streamlineStepFactor;
    }

    public void setDisplay(DisplayImpl display) {
        fDisplay = display;
    }

    public DisplayImpl getDisplay() {
        return fDisplay;
    }

    /**
	 * Checks if given, FieldImpl of FlatFields (ie cache) matches with the current parameters
	 * the user is trying to load.  Checks cutplane position, bounds, resolution,
	 * and whether passed FlatField is valid.
	 *   
	 * @param timeField  FieldImpl array (one element for each axis) that contains any 
	 * 					 cached data for each time step.   
	 * @param timeIdx	 Index of time step attempting to be loaded
	 * @param selectedVar GUI-selected variable (i.e. "u" for velocity or "b" for mag field)
	 * @return			 Returns true if cache hit, false otherwise
	 * @throws VisADException
	 * @throws RemoteException
	 */
    private boolean isCacheHit(FieldImpl timeField[], int timeIdx, String selectedVar) throws VisADException, RemoteException {
        if (timeField[fAxis] == null) return false;
        FlatField cachedFlatField = (FlatField) timeField[fAxis].getSample(timeIdx);
        if (cachedFlatField.isMissing()) return false;
        Set cachedRangeSet = cachedFlatField.getRangeSets()[0];
        if (!(((RealType) ((SetType) cachedRangeSet.getType()).getDomain().getComponent(0)).getName().equals(selectedVar))) return false;
        float iMin, iMax, iRes, jMin, jMax, jRes;
        int iNum, jNum;
        Linear2DSet cachedDomainSet = (Linear2DSet) cachedFlatField.getDomainSet();
        iMin = cachedDomainSet.getX().getLowX();
        iMax = cachedDomainSet.getX().getHiX();
        iNum = cachedDomainSet.getX().getLength();
        iRes = (iMax - iMin) / (float) iNum;
        jMin = cachedDomainSet.getY().getLowX();
        jMax = cachedDomainSet.getY().getHiX();
        jNum = cachedDomainSet.getY().getLength();
        jRes = (jMax - jMin) / (float) jNum;
        float resErrorMargin = 0.01f;
        if (fAxis == MhdPanel.CUTPLANE_X) {
            if ((Math.abs(iRes - fCutplaneCornersAndRes[5]) > resErrorMargin) || (Math.abs(jRes - fCutplaneCornersAndRes[8]) > resErrorMargin)) return false;
            if ((iMin != fCutplaneCornersAndRes[3]) || (iMax != fCutplaneCornersAndRes[4]) || (jMin != fCutplaneCornersAndRes[6]) || (jMax != fCutplaneCornersAndRes[7])) return false;
        } else if (fAxis == MhdPanel.CUTPLANE_Y) {
            if ((Math.abs(iRes - fCutplaneCornersAndRes[2]) > resErrorMargin) || (Math.abs(jRes - fCutplaneCornersAndRes[8]) > resErrorMargin)) return false;
            if ((iMin != fCutplaneCornersAndRes[0]) || (iMax != fCutplaneCornersAndRes[1]) || (jMin != fCutplaneCornersAndRes[6]) || (jMax != fCutplaneCornersAndRes[7])) return false;
        } else if (fAxis == MhdPanel.CUTPLANE_Z) {
            if ((Math.abs(iRes - fCutplaneCornersAndRes[2]) > resErrorMargin) || (Math.abs(jRes - fCutplaneCornersAndRes[5]) > resErrorMargin)) return false;
            if ((iMin != fCutplaneCornersAndRes[0]) || (iMax != fCutplaneCornersAndRes[1]) || (jMin != fCutplaneCornersAndRes[3]) || (jMax != fCutplaneCornersAndRes[4])) return false;
        }
        return true;
    }

    /**
	 * Examines given data for its currently-rendered variable (i.e., its range variable); uses 
	 * first valid time step found.  If range has several components, returns its 0th component 
	 * 
	 * @param timeField	 	data cache to search
	 * @return				returns a String of the variable name, "NotAvailable" otherwise.
	 * @throws VisADException
	 * @throws RemoteException
	 */
    private String getCurrentlyCachedVariable(FieldImpl timeField[]) throws VisADException, RemoteException {
        String notAvail = "NotAvailable";
        if (timeField[fAxis] == null) return notAvail;
        FlatField cachedFlatField = null;
        for (int i = 0; i < timeField.length; i++) {
            cachedFlatField = (FlatField) timeField[fAxis].getSample(i);
            if (!cachedFlatField.isMissing()) {
                Set cachedRangeSet = cachedFlatField.getRangeSets()[0];
                String cachedRangeVar = ((RealType) ((SetType) cachedRangeSet.getType()).getDomain().getComponent(0)).getName();
                return cachedRangeVar;
            }
        }
        return notAvail;
    }

    private boolean isMhdCacheValid(File curMhdFiles[]) {
        if (fMhdFileCache.size() == 0) {
            for (int i = 0; i < curMhdFiles.length; i++) fMhdFileCache.add(curMhdFiles[i]);
        } else {
            if (fMhdFileCache.size() == curMhdFiles.length) {
                for (int i = 0; i < fMhdFileCache.size(); i++) {
                    if (!fMhdFileCache.get(i).getAbsolutePath().equals(curMhdFiles[i].getAbsolutePath())) return false;
                }
                return true;
            }
        }
        return false;
    }
}
