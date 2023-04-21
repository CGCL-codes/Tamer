package org.indi.nexstar.gt;

import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.util.Date;
import org.indi.clientmessages.GetProperties;
import org.indi.objects.Number;
import org.indi.objects.NumberVector;
import org.indi.objects.Permission;
import org.indi.objects.Standard;
import org.indi.objects.State;
import org.indi.objects.Switch;
import org.indi.objects.SwitchRule;
import org.indi.objects.SwitchVector;
import org.indi.reactor.TimerCallback;
import org.indi.serial.SerialDevice;
import org.indi.server.BasicDevice;
import org.indi.server.IndiServer;

/**
 * A simple telescope simulator. The telescope can be connected as well as
 * dsiconnected. It can be asked to move to a specified position and keeps
 * tracking when it reaches its target
 * 
 * @author Richard van Nieuwenhoven
 * 
 */
public class NexstarGTMount extends SerialDevice implements TimerCallback {

    /**
     * the Number vector to keep the coordinates of the telescope
     */
    private NumberVector coordinates = null;

    /**
     * the time between the calls of the onTimer method in milliseconds
     */
    private final long timeout = 250;

    /**
     * the siderial movement of the stars in degrees per second
     */
    private final double sidrate = 0.004178;

    /**
     * the slew rate a which the motor can move the telescope in degrees per
     * second
     */
    private final double slewrate = 1.0;

    /**
     * the last time when on timer was called in milliseconds since epoch
     */
    private Double lastTime = null;

    /**
     * the indiobjects.Number representing the altitude the telescope is poiting
     * to
     */
    private Number alt = null;

    /**
     * the indiobjects.Number representing the azimuth telescope is pointing to
     */
    private Number az = null;

    /**
     * the altitude to point to as requested by the user
     */
    private double targetAlt;

    /**
     * the azimuth to point to as requested by the user
     */
    private double targetAz;

    /**
     * class constructor
     * 
     * @param server
     *                the server to host this device
     */
    public NexstarGTMount() {
        super("Nexstar GT Mount");
        this.coordinates = new NumberVector(this.name, Standard.Vector.HORIZONTAL_COORD, getMainControlGroup(), State.Idle, Permission.ReadWrite, 0);
        this.alt = new Number(Standard.Property.ALT.id, "ALT  D:M:S", "%f", 0d, 90d, 0d, 0d);
        this.coordinates.add(this.alt);
        this.az = new Number(Standard.Property.AZ.id, "AZ D:M:S", "%f", -180d, 180d, 0d, 0d);
        this.coordinates.add(this.az);
    }

    @Override
    public void onNew(NumberVector vector) {
        if (!((Standard.Vector.HORIZONTAL_COORD.id.equals(vector.getName())) && (this.name.equals(vector.getDevice())))) {
            return;
        }
        if (this.connectswitch.getState() == Switch.State.Off) {
            msg("Position discaded because Telescope is disconnected");
            return;
        }
        if (this.powerswitch.getState() == State.Idle) {
            this.coordinates.setState(State.Idle);
            set(this.coordinates, "Telescope is offline");
        }
        double newAlt = 0, newAz = 0;
        int nset = 0;
        for (Number n : vector.getChlidren()) {
            String name = n.getName();
            if (Standard.Property.ALT.id.equals(name)) {
                newAlt = n.getDouble();
                nset += ((newAlt >= 0d) && (newAlt <= 90d)) ? 1 : 0;
            }
            if (Standard.Property.AZ.id.equals(name)) {
                newAz = n.getDouble();
                nset += ((newAz >= -180d) && (newAz <= 180d)) ? 1 : 0;
            }
            if (nset == 2) {
                this.targetAlt = newAlt;
                this.targetAz = newAz;
                this.coordinates.setState(State.Busy);
                set(this.coordinates, "Moving to Alt Az " + Double.toString(newAlt) + " " + Double.toString(newAz));
            } else {
                this.coordinates.setState(State.Idle);
                set(this.coordinates, "Alt or Az absent or bogus");
            }
        }
    }

    @Override
    public void onGetProperties(GetProperties o) {
        super.onGetProperties(o);
        def(this.coordinates);
        timer(this.timeout);
    }

    @Override
    public void onTimer() {
        double time = new Date().getTime();
        if (this.lastTime == null) {
            this.lastTime = time;
        }
        double dt = (time - this.lastTime) / 1000.0;
        this.lastTime = time;
        switch(this.coordinates.getState()) {
            case Idle:
                this.az.addDouble((this.sidrate * dt / 15.));
                set(this.coordinates);
                break;
            case Busy:
                double da = this.slewrate * dt;
                int nlocked = 0;
                double dx = this.targetAz - this.az.getDouble();
                if (Math.abs(dx) <= da / 15.) {
                    this.az.setDouble(this.targetAz);
                    nlocked++;
                } else if (dx > 0) {
                    this.az.addDouble(da / 15.);
                } else {
                    this.az.subtractDouble(da / 15.);
                }
                dx = this.targetAlt - this.alt.getDouble();
                if (Math.abs(dx) <= da) {
                    this.alt.setDouble(this.targetAlt);
                    nlocked++;
                } else if (dx > 0) {
                    this.alt.addDouble(da);
                } else {
                    this.alt.subtractDouble(da);
                }
                if (nlocked == 2) {
                    this.coordinates.setState(State.Ok);
                    msg("Now tracking");
                }
                set(this.coordinates);
                break;
            case Ok:
                set(this.coordinates);
                break;
            case Alert:
                set(this.coordinates);
                break;
        }
        timer(this.timeout);
    }

    protected void setSerialPortParameters(SerialPort serialPort) throws UnsupportedCommOperationException {
        serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }
}
