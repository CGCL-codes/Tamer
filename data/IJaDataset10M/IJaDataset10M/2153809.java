package org.goniolab.unitcircle;

import org.goniolab.unitcircle.events.PhiUpdater;
import java.awt.Point;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import javax.swing.SwingUtilities;
import org.goniolab.lib.Northing;
import org.goniolab.lib.graphics.GPoint2D;
import org.goniolab.lib.math.GAngle;
import org.goniolab.lib.math.GAngle.Interval;
import org.goniolab.lib.math.GAngle.Unit;
import org.goniolab.lib.math.GMath;
import org.goniolab.unitcircle.events.DeltaUpdater;
import org.openide.util.Lookup;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
public class Calculator implements PreferenceChangeListener {

    public static final String DECIMAL_FORMAT_FUNCTIONS = "0.00000000";

    public static final String DECIMAL_FORMAT_PHI = "0.000000000";

    public static final int LENGTH_OF_FUNCTIONS = 20;

    private static Calculator instance;

    private GAngle phi = new GAngle();

    private Delta delta = new Delta();

    private GAngle.Unit unit = Unit.RAD;

    private Calculator() {
        init();
    }

    public static Calculator getInstance() {
        if (instance == null) {
            instance = new Calculator();
        }
        return instance;
    }

    public GAngle getPhi() {
        return phi;
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        String key = evt.getKey();
        if (key.equalsIgnoreCase(Options.IntegerPref.INTERVAL.getNameKey())) {
            preferenceChangeInterval();
            updatePhi();
        } else if (key.equalsIgnoreCase(Options.IntegerPref.NORTHING.getNameKey())) {
            preferenceChangeNorthing();
            updatePhi();
        } else if (key.equalsIgnoreCase(Options.IntegerPref.UNIT.getNameKey())) {
            preferenceChangeUnit();
            updatePhi();
            updateDelta();
        } else if (key.equalsIgnoreCase(Options.BooleanPref.DELTA_X.getNameKey())) {
            preferenceChangeDelta();
        }
    }

    public void setDeltaValue(double aValue) {
        double inverse = GMath.getBoundedMinMax(-1.0, 1.0, aValue);
        double inverseCos = inverse;
        double inverseSin = inverse;
        if (Options.getInstance().getBoolean(Options.BooleanPref.DELTA_X)) {
            inverseSin = Math.sin(Math.acos(inverseCos));
            delta.getPhi1().setRad(Math.acos(inverseCos));
            if (delta.getPhi1().getInterval() == GAngle.Interval.NEGPOS) {
                delta.getPhi2().setRad(-delta.getPhi1().getRad());
            } else {
                delta.getPhi2().setRad(2 * Math.PI - delta.getPhi1().getRad());
            }
        } else {
            inverseCos = Math.cos(Math.asin(inverseSin));
            delta.getPhi1().setRad(Math.asin(inverseSin));
            delta.getPhi2().setRad(Math.PI - delta.getPhi1().getRad());
            if (delta.getPhi1().getInterval() == GAngle.Interval.POS && delta.getPhi1().getRad() < 0) {
                delta.getPhi1().setRad(2 * Math.PI + delta.getPhi1().getRad());
            }
            if (delta.getPhi1().getInterval() == GAngle.Interval.NEGPOS && delta.getPhi1().getRad() < 0) {
                delta.getPhi2().setRad(-(2 * Math.PI - delta.getPhi2().getRad()));
            }
        }
        delta.setDeltaX(inverseCos);
        delta.setDeltaY(inverseSin);
        updateDelta();
    }

    public void setDeltaValue(Point coordinate, int radius) {
        double deltaValue = 0F;
        double dX = 0F;
        double dY = 0F;
        switch(phi.getNorthing()) {
            case X:
                dX = coordinate.y;
                dY = coordinate.x;
                break;
            case Y:
                dX = coordinate.x;
                dY = coordinate.y;
                break;
        }
        if (Options.getInstance().getBoolean(Options.BooleanPref.DELTA_X)) {
            deltaValue = GMath.getBoundedMinMax(-1.0, 1.0, dX / radius);
        } else {
            deltaValue = GMath.getBoundedMinMax(-1.0, 1.0, dY / radius);
        }
        setDeltaValue(deltaValue);
    }

    public void setPhiValue(double phiValue) {
        phi.set(phiValue);
        updatePhi();
    }

    public void setPhiValue(Point aPoint) {
        double dX = 0F;
        double dY = 0F;
        switch(phi.getNorthing()) {
            case X:
                dX = aPoint.y;
                dY = aPoint.x;
                break;
            case Y:
                dX = aPoint.x;
                dY = aPoint.y;
                break;
        }
        if (dX == 0) {
            dX = 0.00000001;
        }
        double tempPhi = Math.atan(dY / dX);
        if (dY < 0 && dX > 0) {
            tempPhi = tempPhi + 2 * Math.PI;
        }
        if (dY < 0 && dX < 0) {
            tempPhi = tempPhi + Math.PI;
        }
        if (dY >= 0 && dX < 0) {
            tempPhi = tempPhi + Math.PI;
        }
        if (phi.getInterval() == GAngle.Interval.NEGPOS && tempPhi > Math.PI) {
            tempPhi = -(2 * Math.PI - tempPhi);
        }
        phi.setAngle(tempPhi);
        updatePhi();
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
        phi.setUnit(unit);
    }

    public void updateMe(PhiUpdater phiUpdater) {
        phiUpdater.phiUpdated(phi);
    }

    public void updatePhi() {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (PhiUpdater phiUpdater : Lookup.getDefault().lookupAll(PhiUpdater.class)) {
                    phiUpdater.phiUpdated(phi);
                }
            }
        });
    }

    private void init() {
        Options.getInstance().getPreferences().addPreferenceChangeListener(this);
        preferenceChangeInterval();
        preferenceChangeNorthing();
        preferenceChangeUnit();
        preferenceChangeDelta();
        updateDelta();
        updatePhi();
    }

    private void preferenceChangeInterval() {
        phi.setInterval(GAngle.Interval.values()[Options.getInstance().getInteger(Options.IntegerPref.INTERVAL)]);
        if (phi.getInterval() == Interval.NEGPOS && phi.getRad() > Unit.RAD.getHalfCycle()) {
            phi.setRad(phi.getRad() - Unit.RAD.getFullCycle());
        }
        if (phi.getInterval() == Interval.POS && phi.getRad() < 0) {
            phi.setRad(phi.getRad() + Unit.RAD.getFullCycle());
        }
    }

    private void preferenceChangeNorthing() {
        Northing n = Northing.values()[Options.getInstance().getInteger(Options.IntegerPref.NORTHING)];
        GPoint2D.setNorthing(n);
        phi.setNorthing(n);
    }

    private void preferenceChangeUnit() {
        setUnit(Unit.values()[Options.getInstance().getInteger(Options.IntegerPref.UNIT)]);
    }

    private void preferenceChangeDelta() {
        if (Options.getInstance().getBoolean(Options.BooleanPref.DELTA_X)) {
            delta.setMode(Delta.Mode.ARCCOS);
            setDeltaValue(delta.getDeltaX());
        } else {
            delta.setMode(Delta.Mode.ARCSIN);
            setDeltaValue(delta.getDeltaY());
        }
    }

    private void updateDelta() {
        delta.getPhi1().setUnit(phi.getUnit());
        delta.getPhi2().setUnit(phi.getUnit());
        delta.getPhi1().setNorthing(phi.getNorthing());
        delta.getPhi2().setNorthing(phi.getNorthing());
        delta.getPhi1().setInterval(phi.getInterval());
        delta.getPhi2().setInterval(phi.getInterval());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                for (DeltaUpdater deltaUpdater : Lookup.getDefault().lookupAll(DeltaUpdater.class)) {
                    deltaUpdater.deltaUpdated(delta);
                }
            }
        });
    }
}
