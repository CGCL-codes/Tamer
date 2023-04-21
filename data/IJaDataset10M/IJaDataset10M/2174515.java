package net.sf.dz.view.tcp.server;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import net.sf.dz.device.actuator.AC;
import net.sf.dz.device.actuator.Damper;
import net.sf.dz.device.model.DamperController;
import net.sf.dz.device.model.Unit;
import net.sf.dz.device.model.Thermostat;
import net.sf.dz.event.ACListener;
import net.sf.dz.event.DamperListener;
import net.sf.dz.scheduler.Schedule;

/**
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2002
 * @version $Id: UnitServerProxy.java,v 1.17 2007-03-01 21:34:28 vtt Exp $
 */
public class UnitServerProxy extends AbstractServerProxy implements Unit, ACListener, DamperListener {

    private Unit unit;

    /**
     * Map of thermostats that are connected to this unit.
     *
     * The key is the thermostat proxy, the value is the thermostat.
     */
    private Map<ThermostatServerProxy, Thermostat> tsMap = new TreeMap<ThermostatServerProxy, Thermostat>();

    public UnitServerProxy(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit can't be null");
        }
        this.unit = unit;
        for (Iterator<? extends Thermostat> i = unit.iterator(); i.hasNext(); ) {
            Thermostat ts = i.next();
            ThermostatServerProxy tsProxy = new ThermostatServerProxy(this, ts);
            tsMap.put(tsProxy, ts);
            ts.addListener(this);
            Damper d = unit.getDamperController().getDamper(ts);
            d.addListener(this);
            Schedule s = unit.getSchedule(ts);
            if (s == null) {
                logger.warn("Unable to get the schedule for " + ts.getName());
            } else {
                s.addListener(tsProxy);
            }
        }
        unit.getAC().addListener(this);
    }

    public void controlSignalChanged(Thermostat source, Object signal) {
        if (signal instanceof Double) {
            send("status:unit:" + unit.getName() + ":ac:signal:" + source.getName() + ":" + signal);
        } else {
            send("status:unit:" + unit.getName() + ":ac:signal:" + source.getName() + ":E " + signal);
        }
    }

    public void enabledStateChanged(Thermostat source, boolean enabled) {
        send("status:unit:" + unit.getName() + ":zone:" + source.getName() + ":on:" + enabled);
    }

    public void votingStateChanged(Thermostat source, boolean voting) {
        send("status:unit:" + unit.getName() + ":zone:" + source.getName() + ":voting:" + voting);
    }

    public void holdStateChanged(Thermostat source, boolean hold) {
        send("status:unit:" + unit.getName() + ":zone:" + source.getName() + ":hold:" + hold);
    }

    public void throttleChanged(Damper d, double throttle) {
        send("status:unit:" + unit.getName() + ":zone:" + unit.getDamperController().getThermostat(d).getName() + ":damper:status:" + throttle + "/" + throttle);
    }

    public AC getAC() {
        throw new Error("Not Implemented");
    }

    public DamperController getDamperController() {
        throw new Error("Not Implemented");
    }

    public Iterator<ThermostatServerProxy> iterator() {
        throw new Error("Not Implemented");
    }

    public int getZoneCount() {
        return unit.getZoneCount();
    }

    public String getName() {
        return unit.getName();
    }

    public void renderReflection(PrintWriter pw) {
        pw.println("    <unit name=\"" + getName() + "\">");
        for (Iterator<ThermostatServerProxy> i = tsMap.keySet().iterator(); i.hasNext(); ) {
            i.next().renderReflection(pw);
        }
        pw.println("    </unit>");
    }

    public void dumpState(PrintWriter pw) {
        pw.println("status:unit:" + unit.getName() + ":ac:mode:" + unit.getAC().getMode());
        for (Iterator<ThermostatServerProxy> i = tsMap.keySet().iterator(); i.hasNext(); ) {
            i.next().dumpState(pw);
        }
    }

    public void listen(PrintWriter pw) {
        super.listen(pw);
        for (Iterator<ThermostatServerProxy> i = tsMap.keySet().iterator(); i.hasNext(); ) {
            i.next().listen(pw);
        }
    }

    public void parse(StringTokenizer st) throws Throwable {
        String target = st.nextToken();
        if (target.equals("zone")) {
            String zoneName = st.nextToken();
            for (Iterator<ThermostatServerProxy> i = tsMap.keySet().iterator(); i.hasNext(); ) {
                ThermostatServerProxy proxy = i.next();
                if (proxy.getName().equals(zoneName)) {
                    proxy.parse(st);
                    return;
                }
            }
            throw new IllegalArgumentException("Non-existent zone '" + zoneName + "'");
        } else if (target.equals("ac")) {
            String op = st.nextToken();
            if (op.equals("mode")) {
                int mode = Integer.parseInt(st.nextToken());
                unit.getAC().setMode(mode);
            } else {
                throw new IllegalArgumentException("Unknown AC operation: '" + op + "'");
            }
        } else {
            throw new IllegalArgumentException("Unknown target '" + target + "'");
        }
    }

    public void modeChanged(AC source, int mode) {
        send("status:unit:" + unit.getName() + ":ac:mode:" + mode);
    }

    public void stageChanged(AC source, int stage) {
        send("status:unit:" + unit.getName() + ":ac:stage:" + stage);
    }

    public void demandChanged(AC source, double demand) {
        send("status:unit:" + unit.getName() + ":ac:demand:" + demand);
    }

    public void fanSpeedChanged(AC source, double speed) {
        send("status:unit:" + unit.getName() + ":ac:fan:" + speed);
    }

    public Schedule getSchedule(Thermostat ts) {
        return unit.getSchedule(ts);
    }

    public int compareTo(Object other) {
        if (!(other instanceof Unit)) {
            if (other == null) {
                throw new ClassCastException("null argument");
            } else {
                throw new ClassCastException("Expected " + getClass().getName() + ", got " + other.getClass().getName());
            }
        }
        return getName().compareTo(((Unit) other).getName());
    }

    public int hashCode() {
        return getName().hashCode();
    }
}
