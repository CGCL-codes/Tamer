package jsattrak.objects;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.Vector;
import javax.swing.ImageIcon;
import jsattrak.customsat.InitialConditionsNode;
import jsattrak.customsat.PropogatorNode;
import jsattrak.customsat.StopNode;
import jsattrak.utilities.StateVector;
import jsattrak.utilities.TLE;
import name.gano.astro.AstroConst;
import name.gano.astro.GeoFunctions;
import name.gano.astro.Kepler;
import name.gano.astro.time.Time;
import name.gano.math.interpolation.LagrangeInterp;
import name.gano.swingx.treetable.CustomTreeTableNode;
import name.gano.astro.coordinates.J2kCoordinateConversion;
import name.gano.worldwind.modelloader.WWModel3D_new;
import net.java.joglutils.model.ModelFactory;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

/**
 * NOTE !!!!!!!!  -- internal time for epehemeris is TT time all input times UTC
 * @author sgano
 */
public class CustomSatellite extends AbstractSatellite {

    private int ephemerisIncrement = 30;

    private Vector<StateVector> ephemeris = new Vector<StateVector>(ephemerisIncrement, ephemerisIncrement);

    private DefaultTreeTableModel missionTableModel = new DefaultTreeTableModel();

    String name = "Custom Sat";

    double currentJulianDate = -1;

    double tleEpochJD = -1;

    private double[] j2kPos;

    private double[] j2kVel;

    private double[] posTEME;

    private double[] velTEME = new double[3];

    private double[] lla;

    private boolean plot2d = true;

    private Color satColor = Color.RED;

    private boolean plot2DFootPrint = true;

    private boolean fillFootPrint = true;

    private int numPtsFootPrint = 101;

    boolean showGroundTrack = true;

    private int grnTrkPointsPerPeriod = 121;

    private double groundTrackLeadPeriodMultiplier = 2.0;

    private double groundTrackLagPeriodMultiplier = 1.0;

    double[][] latLongLead;

    double[][] latLongLag;

    private double[][] temePosLead;

    private double[][] temePosLag;

    private double[] timeLead;

    private double[] timeLag;

    boolean groundTrackIni = false;

    private boolean showName2D = true;

    private boolean show3DOrbitTrace = true;

    private boolean show3DFootprint = true;

    private boolean show3DName = true;

    private boolean show3D = true;

    private boolean showGroundTrack3d = false;

    private boolean show3DOrbitTraceECI = true;

    private boolean showConsoleOnPropogate = true;

    private boolean use3dModel = false;

    private String threeDModelPath = "globalstar/Globalstar.3ds";

    private transient WWModel3D_new threeDModel;

    private double threeDModelSizeFactor = 300000;

    public CustomSatellite(String name, Time scenarioEpochDate) {
        this.name = name;
        iniMissionTableModel(scenarioEpochDate);
    }

    private void iniMissionTableModel(Time scenarioEpochDate) {
        Vector<String> tableHeaders = new Vector<String>();
        tableHeaders.add("Mission Objects");
        missionTableModel.setColumnIdentifiers(tableHeaders);
        String[] str = new String[3];
        str[0] = name;
        CustomTreeTableNode rootNode = new CustomTreeTableNode(str);
        rootNode.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icons/custom/sat_icon.png"))));
        missionTableModel.setRoot(rootNode);
        new InitialConditionsNode(rootNode, scenarioEpochDate);
        new PropogatorNode(rootNode);
        new StopNode(rootNode);
    }

    @Override
    public void propogate2JulDate(double julDate) {
        this.currentJulianDate = julDate;
        double tempTime, maxTime, minTime;
        double currentMJDtime = julDate - AstroConst.JDminusMJD;
        double deltaTT2UTC = Time.deltaT(currentMJDtime);
        if (ephemeris.size() > 0) {
            minTime = ephemeris.get(0).state[0] - deltaTT2UTC;
            maxTime = ephemeris.get(ephemeris.size() - 1).state[0] - deltaTT2UTC;
            if (julDate <= maxTime && julDate >= minTime) {
                StateVector tempState = ephemeris.elementAt(1);
                tempTime = ephemeris.get(1).state[0] - deltaTT2UTC;
                int i = 1;
                while (tempTime < julDate) {
                    i++;
                    tempState = ephemeris.get(i);
                    tempTime = tempState.state[0] - deltaTT2UTC;
                }
                int i1, i2, i3;
                if (i == 1) {
                    i1 = 0;
                    i2 = 1;
                    i3 = 2;
                } else if (i == ephemeris.size() - 1) {
                    i3 = ephemeris.size() - 1;
                    i2 = ephemeris.size() - 2;
                    i1 = ephemeris.size() - 3;
                } else {
                    i1 = i - 1;
                    i2 = i;
                    i3 = i + 1;
                }
                tempState = ephemeris.get(i1);
                double t1 = tempState.state[0];
                double x1 = tempState.state[1];
                double y1 = tempState.state[2];
                double z1 = tempState.state[3];
                tempState = ephemeris.get(i2);
                double t2 = tempState.state[0];
                double x2 = tempState.state[1];
                double y2 = tempState.state[2];
                double z2 = tempState.state[3];
                tempState = ephemeris.get(i3);
                double t3 = tempState.state[0];
                double x3 = tempState.state[1];
                double y3 = tempState.state[2];
                double z3 = tempState.state[3];
                double timeSecEpoch = julDate + deltaTT2UTC;
                if (j2kPos == null) {
                    j2kPos = new double[3];
                }
                j2kPos[0] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, x1, t2, x2, t3, x3);
                j2kPos[1] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, y1, t2, y2, t3, y3);
                j2kPos[2] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, z1, t2, z2, t3, z3);
                tempState = ephemeris.get(i1);
                t1 = tempState.state[0];
                x1 = tempState.state[4];
                y1 = tempState.state[5];
                z1 = tempState.state[6];
                tempState = ephemeris.get(i2);
                t2 = tempState.state[0];
                x2 = tempState.state[4];
                y2 = tempState.state[5];
                z2 = tempState.state[6];
                tempState = ephemeris.get(i3);
                t3 = tempState.state[0];
                x3 = tempState.state[4];
                y3 = tempState.state[5];
                z3 = tempState.state[6];
                if (j2kVel == null) {
                    j2kVel = new double[3];
                }
                j2kVel[0] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, x1, t2, x2, t3, x3);
                j2kVel[1] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, y1, t2, y2, t3, y3);
                j2kVel[2] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, z1, t2, z2, t3, z3);
                double mjd = julDate - AstroConst.JDminusMJD;
                double ttt = (mjd - AstroConst.MJD_J2000) / 36525.0;
                double[][] A = J2kCoordinateConversion.teme_j2k(J2kCoordinateConversion.Direction.from, ttt, 24, 2, 'a');
                posTEME = J2kCoordinateConversion.matvecmult(A, j2kPos);
                velTEME = J2kCoordinateConversion.matvecmult(A, j2kVel);
                double[] oldLLA = new double[3];
                if (lla != null) {
                    oldLLA = lla.clone();
                }
                lla = GeoFunctions.GeodeticLLA(posTEME, currentMJDtime);
                if (showGroundTrack == true) {
                    if (groundTrackIni == false || oldLLA == null) {
                        initializeGroundTrack();
                    } else if (oldLLA[0] < 0 && lla[0] >= 0) {
                        initializeGroundTrack();
                    } else if (timeLead[timeLead.length - 1] < julDate || timeLag[0] > julDate) {
                        initializeGroundTrack();
                    }
                }
            } else {
                if (j2kPos != null) {
                    j2kPos = null;
                    posTEME = null;
                    lla = null;
                    groundTrackIni = false;
                    latLongLead = null;
                    latLongLag = null;
                    temePosLag = null;
                    temePosLead = null;
                    timeLead = null;
                    timeLag = null;
                }
            }
        } else {
        }
    }

    public double getSatTleEpochJulDate() {
        if (ephemeris.size() > 0) {
            return ephemeris.firstElement().state[0];
        } else {
            return 0;
        }
    }

    /**
     * Calculate MOD position of this sat at a given JulDateTime (doesn't save the time) - can be useful for event searches or optimization
     * @param julDate - julian date
     * @return j2k position of satellite in meters
     */
    public double[] calculateTemePositionFromUT(double julDate) {
        double[] j2kPosTemp = calculateJ2KPositionFromUT(julDate);
        double[] ptPos = new double[3];
        if (j2kPosTemp != null) {
            double mjd = julDate - AstroConst.JDminusMJD;
            double ttt = (mjd - AstroConst.MJD_J2000) / 36525.0;
            double[][] A = J2kCoordinateConversion.teme_j2k(J2kCoordinateConversion.Direction.from, ttt, 24, 2, 'a');
            ptPos = J2kCoordinateConversion.matvecmult(A, j2kPosTemp);
        }
        return ptPos;
    }

    /**
     * Calculate J2K position of this sat at a given JulDateTime (doesn't save the time) - can be useful for event searches or optimization
     * @param julDate - julian date
     * @return j2k position of satellite in meters
     */
    @Override
    public double[] calculateJ2KPositionFromUT(double julDate) {
        double[] ptPos = new double[3];
        double tempTime, maxTime, minTime;
        double deltaTT2UTC = Time.deltaT(julDate - AstroConst.JDminusMJD);
        if (ephemeris.size() > 0) {
            minTime = ephemeris.get(0).state[0] - deltaTT2UTC;
            maxTime = ephemeris.get(ephemeris.size() - 1).state[0] - deltaTT2UTC;
            if (julDate <= maxTime && julDate >= minTime) {
                StateVector tempState = ephemeris.elementAt(1);
                tempTime = ephemeris.get(1).state[0] - deltaTT2UTC;
                int i = 1;
                while (tempTime < julDate) {
                    i++;
                    tempState = ephemeris.get(i);
                    tempTime = tempState.state[0] - deltaTT2UTC;
                }
                int i1, i2, i3;
                if (i == 1) {
                    i1 = 0;
                    i2 = 1;
                    i3 = 2;
                } else if (i == ephemeris.size() - 1) {
                    i3 = ephemeris.size() - 1;
                    i2 = ephemeris.size() - 2;
                    i1 = ephemeris.size() - 3;
                } else {
                    i1 = i - 1;
                    i2 = i;
                    i3 = i + 1;
                }
                tempState = ephemeris.get(i1);
                double t1 = tempState.state[0];
                double x1 = tempState.state[1];
                double y1 = tempState.state[2];
                double z1 = tempState.state[3];
                tempState = ephemeris.get(i2);
                double t2 = tempState.state[0];
                double x2 = tempState.state[1];
                double y2 = tempState.state[2];
                double z2 = tempState.state[3];
                tempState = ephemeris.get(i3);
                double t3 = tempState.state[0];
                double x3 = tempState.state[1];
                double y3 = tempState.state[2];
                double z3 = tempState.state[3];
                double timeSecEpoch = julDate + deltaTT2UTC;
                ptPos[0] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, x1, t2, x2, t3, x3);
                ptPos[1] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, y1, t2, y2, t3, y3);
                ptPos[2] = LagrangeInterp.Lagrange3pt(timeSecEpoch, t1, z1, t2, z2, t3, z3);
            } else {
            }
        }
        return ptPos;
    }

    private void initializeGroundTrack() {
        if (currentJulianDate == -1) {
            return;
        }
        double lastAscendingNodeTime = currentJulianDate;
        double periodMin = Kepler.CalculatePeriod(AstroConst.GM_Earth, j2kPos, j2kVel) / (60.0);
        lastAscendingNodeTime = currentJulianDate;
        double leadEndTime = lastAscendingNodeTime + groundTrackLeadPeriodMultiplier * periodMin / (60.0 * 24);
        double lagEndTime = lastAscendingNodeTime - groundTrackLagPeriodMultiplier * periodMin / (60.0 * 24);
        fillGroundTrack(lastAscendingNodeTime, leadEndTime, lagEndTime);
        groundTrackIni = true;
        return;
    }

    private void fillGroundTrack(double lastAscendingNodeTime, double leadEndTime, double lagEndTime) {
        int ptsLead = (int) Math.ceil(grnTrkPointsPerPeriod * groundTrackLeadPeriodMultiplier);
        latLongLead = new double[ptsLead][3];
        temePosLead = new double[ptsLead][3];
        timeLead = new double[ptsLead];
        for (int i = 0; i < ptsLead; i++) {
            double ptTime = lastAscendingNodeTime + i * (leadEndTime - lastAscendingNodeTime) / (ptsLead - 1);
            double deltaTT2UTC = Time.deltaT(ptTime - AstroConst.JDminusMJD);
            if (ptTime >= ephemeris.firstElement().state[0] - deltaTT2UTC && ptTime <= ephemeris.lastElement().state[0] - deltaTT2UTC) {
                double[] ptLlaXyz = calculateLatLongAltXyz(ptTime);
                latLongLead[i][0] = ptLlaXyz[0];
                latLongLead[i][1] = ptLlaXyz[1];
                latLongLead[i][2] = ptLlaXyz[2];
                temePosLead[i][0] = ptLlaXyz[3];
                temePosLead[i][1] = ptLlaXyz[4];
                temePosLead[i][2] = ptLlaXyz[5];
            } else {
                latLongLead[i][0] = Double.NaN;
                latLongLead[i][1] = Double.NaN;
                latLongLead[i][2] = Double.NaN;
                temePosLead[i][0] = Double.NaN;
                temePosLead[i][1] = Double.NaN;
                temePosLead[i][2] = Double.NaN;
            }
            timeLead[i] = ptTime;
        }
        int ptsLag = (int) Math.ceil(grnTrkPointsPerPeriod * groundTrackLagPeriodMultiplier);
        latLongLag = new double[ptsLag][3];
        temePosLag = new double[ptsLag][3];
        timeLag = new double[ptsLag];
        for (int i = 0; i < ptsLag; i++) {
            double ptTime = lastAscendingNodeTime + i * (lagEndTime - lastAscendingNodeTime) / (ptsLag - 1);
            double deltaTT2UTC = Time.deltaT(ptTime - AstroConst.JDminusMJD);
            if (ptTime >= ephemeris.firstElement().state[0] - deltaTT2UTC && ptTime <= ephemeris.lastElement().state[0] - deltaTT2UTC) {
                double[] ptLlaXyz = calculateLatLongAltXyz(ptTime);
                latLongLag[i][0] = ptLlaXyz[0];
                latLongLag[i][1] = ptLlaXyz[1];
                latLongLag[i][2] = ptLlaXyz[2];
                temePosLag[i][0] = ptLlaXyz[3];
                temePosLag[i][1] = ptLlaXyz[4];
                temePosLag[i][2] = ptLlaXyz[5];
            } else {
                latLongLag[i][0] = Double.NaN;
                latLongLag[i][1] = Double.NaN;
                latLongLag[i][2] = Double.NaN;
                temePosLag[i][0] = Double.NaN;
                temePosLag[i][1] = Double.NaN;
                temePosLag[i][2] = Double.NaN;
            }
            timeLag[i] = ptTime;
        }
    }

    private double[] calculateLatLongAltXyz(double julDate) {
        double[] ptPos = calculateTemePositionFromUT(julDate);
        double[] ptLla = GeoFunctions.GeodeticLLA(ptPos, julDate - AstroConst.JDminusMJD);
        double[] ptLlaXyz = new double[] { ptLla[0], ptLla[1], ptLla[2], ptPos[0], ptPos[1], ptPos[2] };
        return ptLlaXyz;
    }

    @Override
    public void updateTleData(TLE newTLE) {
    }

    public double getPeriod() {
        if (j2kPos != null) {
            return Kepler.CalculatePeriod(AstroConst.GM_Earth, j2kPos, j2kVel) / (60.0);
        } else {
            return 0;
        }
    }

    public double[] getKeplarianElements() {
        return Kepler.SingularOsculatingElements(AstroConst.GM_Earth, j2kPos, j2kVel);
    }

    public void setShowGroundTrack(boolean showGrndTrk) {
        showGroundTrack = showGrndTrk;
        if (showGrndTrk == false) {
            groundTrackIni = false;
            latLongLead = new double[][] { {} };
            latLongLag = new double[][] { {} };
            temePosLag = new double[][] { {} };
            temePosLead = new double[][] { {} };
            timeLead = new double[] {};
            timeLag = new double[] {};
        } else {
            initializeGroundTrack();
        }
    }

    public boolean getShowGroundTrack() {
        return showGroundTrack;
    }

    public double getLatitude() {
        if (lla != null) return lla[0]; else return 180;
    }

    public double getLongitude() {
        if (lla != null) return lla[1]; else return 270;
    }

    public double getAltitude() {
        if (lla != null) return lla[2]; else return 0;
    }

    public double[] getLLA() {
        if (lla == null) {
            return null;
        }
        return lla.clone();
    }

    public double getCurrentJulDate() {
        return currentJulianDate;
    }

    public double[] getJ2000Position() {
        if (j2kPos == null) {
            return null;
        }
        return j2kPos.clone();
    }

    public double[] getJ2000Velocity() {
        if (j2kVel == null) {
            return null;
        }
        return j2kVel.clone();
    }

    public boolean getPlot2D() {
        return plot2d;
    }

    public Color getSatColor() {
        return satColor;
    }

    public boolean getPlot2DFootPrint() {
        return plot2DFootPrint;
    }

    public boolean getGroundTrackIni() {
        return groundTrackIni;
    }

    public void setGroundTrackIni2False() {
        groundTrackIni = false;
    }

    public int getNumGroundTrackLeadPts() {
        if (latLongLead != null) return latLongLead.length; else return 0;
    }

    public int getNumGroundTrackLagPts() {
        if (latLongLag != null) return latLongLag.length; else return 0;
    }

    public double[] getGroundTrackLlaLeadPt(int index) {
        return new double[] { latLongLead[index][0], latLongLead[index][1], latLongLead[index][2] };
    }

    public double[] getGroundTrackLlaLagPt(int index) {
        return new double[] { latLongLag[index][0], latLongLag[index][1], latLongLag[index][2] };
    }

    public double[] getGroundTrackXyzLeadPt(int index) {
        return new double[] { getTemePosLead()[index][0], getTemePosLead()[index][1], getTemePosLead()[index][2] };
    }

    public double[] getGroundTrackXyzLagPt(int index) {
        return new double[] { getTemePosLag()[index][0], getTemePosLag()[index][1], getTemePosLag()[index][2] };
    }

    public String getName() {
        return name;
    }

    public double getTleEpochJD() {
        return tleEpochJD;
    }

    public double getTleAgeDays() {
        return 0;
    }

    public int getNumPtsFootPrint() {
        return numPtsFootPrint;
    }

    public void setNumPtsFootPrint(int numPtsFootPrint) {
        this.numPtsFootPrint = numPtsFootPrint;
    }

    public boolean isShowName2D() {
        return showName2D;
    }

    public void setShowName2D(boolean showName2D) {
        this.showName2D = showName2D;
    }

    public boolean isFillFootPrint() {
        return fillFootPrint;
    }

    public void setFillFootPrint(boolean fillFootPrint) {
        this.fillFootPrint = fillFootPrint;
    }

    public int getGrnTrkPointsPerPeriod() {
        return grnTrkPointsPerPeriod;
    }

    public void setGrnTrkPointsPerPeriod(int grnTrkPointsPerPeriod) {
        this.grnTrkPointsPerPeriod = grnTrkPointsPerPeriod;
    }

    public double getGroundTrackLeadPeriodMultiplier() {
        return groundTrackLeadPeriodMultiplier;
    }

    public void setGroundTrackLeadPeriodMultiplier(double groundTrackLeadPeriodMultiplier) {
        this.groundTrackLeadPeriodMultiplier = groundTrackLeadPeriodMultiplier;
    }

    public double getGroundTrackLagPeriodMultiplier() {
        return groundTrackLagPeriodMultiplier;
    }

    public void setGroundTrackLagPeriodMultiplier(double groundTrackLagPeriodMultiplier) {
        this.groundTrackLagPeriodMultiplier = groundTrackLagPeriodMultiplier;
    }

    public void setPlot2d(boolean plot2d) {
        this.plot2d = plot2d;
    }

    public void setSatColor(Color satColor) {
        this.satColor = satColor;
    }

    public void setPlot2DFootPrint(boolean plot2DFootPrint) {
        this.plot2DFootPrint = plot2DFootPrint;
    }

    public double[] getTEMEPos() {
        if (posTEME == null) {
            return null;
        }
        return posTEME.clone();
    }

    public boolean isShow3DOrbitTrace() {
        return show3DOrbitTrace;
    }

    public void setShow3DOrbitTrace(boolean show3DOrbitTrace) {
        this.show3DOrbitTrace = show3DOrbitTrace;
    }

    public boolean isShow3DFootprint() {
        return show3DFootprint;
    }

    public void setShow3DFootprint(boolean show3DFootprint) {
        this.show3DFootprint = show3DFootprint;
    }

    public boolean isShow3DName() {
        return show3DName;
    }

    public void setShow3DName(boolean show3DName) {
        this.show3DName = show3DName;
    }

    public boolean isShowGroundTrack3d() {
        return showGroundTrack3d;
    }

    public void setShowGroundTrack3d(boolean showGroundTrack3d) {
        this.showGroundTrack3d = showGroundTrack3d;
    }

    public boolean isShow3DOrbitTraceECI() {
        return show3DOrbitTraceECI;
    }

    public void setShow3DOrbitTraceECI(boolean show3DOrbitTraceECI) {
        this.show3DOrbitTraceECI = show3DOrbitTraceECI;
    }

    public boolean isShow3D() {
        return show3D;
    }

    public void setShow3D(boolean show3D) {
        this.show3D = show3D;
    }

    public double[][] getTemePosLead() {
        return temePosLead;
    }

    public double[][] getTemePosLag() {
        return temePosLag;
    }

    public double[] getTimeLead() {
        return timeLead;
    }

    public double[] getTimeLag() {
        return timeLag;
    }

    public DefaultTreeTableModel getMissionTableModel() {
        return missionTableModel;
    }

    public Vector<StateVector> getEphemeris() {
        return ephemeris;
    }

    public void setEphemeris(Vector<StateVector> e) {
        this.ephemeris = e;
    }

    public boolean isShowConsoleOnPropogate() {
        return showConsoleOnPropogate;
    }

    public void setShowConsoleOnPropogate(boolean showConsoleOnPropogate) {
        this.showConsoleOnPropogate = showConsoleOnPropogate;
    }

    private double secantMethod(double xn_1, double xn, double tol, int maxIter) {
        double d;
        double fn_1 = this.calculateLatLongAltXyz(xn_1)[0];
        double fn = this.calculateLatLongAltXyz(xn)[0];
        for (int n = 1; n <= maxIter; n++) {
            d = (xn - xn_1) / (fn - fn_1) * fn;
            if (Math.abs(d) < tol) {
                return xn;
            }
            xn_1 = xn;
            fn_1 = fn;
            xn = xn - d;
            fn = this.calculateLatLongAltXyz(xn)[0];
        }
        System.out.println("Warning: Secant Method - Max Iteration limit reached finding Asending Node.");
        return xn;
    }

    public boolean isUse3dModel() {
        return use3dModel;
    }

    public void setUse3dModel(boolean use3dModel) {
        this.use3dModel = use3dModel;
        if (use3dModel && threeDModelPath.length() > 0) {
            loadNewModel(threeDModelPath);
        }
    }

    public String getThreeDModelPath() {
        return threeDModelPath;
    }

    /**
     * Relative path to the model -- relative from "user.dir"/data/models/
     * @param path
     */
    public void setThreeDModelPath(String path) {
        if (use3dModel && !(path.equalsIgnoreCase(this.threeDModelPath))) {
            loadNewModel(path);
        }
        this.threeDModelPath = path;
    }

    private void loadNewModel(String path) {
        String localPath = "data/models/";
        try {
            net.java.joglutils.model.geometry.Model model3DS = ModelFactory.createModel(localPath + path);
            threeDModel = new WWModel3D_new(model3DS, new Position(Angle.fromRadians(this.getLatitude()), Angle.fromRadians(this.getLongitude()), this.getAltitude()));
            threeDModel.setMaitainConstantSize(true);
            threeDModel.setSize(threeDModelSizeFactor);
            threeDModel.updateAttitude(this);
        } catch (Exception e) {
            System.out.println("ERROR LOADING 3D MODEL");
        }
    }

    public WWModel3D_new getThreeDModel() {
        return threeDModel;
    }

    public double[] getTEMEVelocity() {
        if (velTEME == null) {
            return null;
        }
        return velTEME.clone();
    }

    public double getThreeDModelSizeFactor() {
        return threeDModelSizeFactor;
    }

    public void setThreeDModelSizeFactor(double modelSizeFactor) {
        if (modelSizeFactor != threeDModelSizeFactor && use3dModel && threeDModelPath.length() > 0) {
            if (threeDModel != null) {
                threeDModel.setSize(modelSizeFactor);
            }
        }
        this.threeDModelSizeFactor = modelSizeFactor;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
