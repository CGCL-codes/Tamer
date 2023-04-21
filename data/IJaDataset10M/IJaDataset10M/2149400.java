package com.jpeterson.x10.beans;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.jpeterson.x10.event.*;

public class X10DimSlider extends JPanel implements ChangeListener {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private JSlider slider;

    private Vector<X10TransmissionListener> x10TransmissionListeners = null;

    private char houseCode = 'A';

    private int deviceCode = 1;

    private int dimValue = 22;

    public X10DimSlider() {
        slider = new JSlider(SwingConstants.VERTICAL, 0, 22, 22);
        slider.addChangeListener(this);
        add(slider);
    }

    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        if (!source.getValueIsAdjusting()) {
            int newValue = (int) ((JSlider) e.getSource()).getValue();
            if (System.getProperty("DEBUG") != null) {
                System.out.println("Dim value: " + dimValue);
                System.out.println("new value: " + newValue);
            }
            if (newValue < dimValue) {
                sendDim(22 - newValue);
                dimValue = newValue;
            } else if (newValue > dimValue) {
                sendBright(newValue);
                dimValue = newValue;
            }
            if (System.getProperty("DEBUG") != null) {
                System.out.println("New dim value: " + dimValue);
            }
        }
    }

    public void sendDim(int dims) {
        fireX10Event(new AddressEvent(this, houseCode, deviceCode));
        fireX10Event(new DimEvent(this, houseCode, dims, 22));
    }

    public void sendBright(int dims) {
        fireX10Event(new AddressEvent(this, houseCode, deviceCode));
        fireX10Event(new BrightEvent(this, houseCode, dims, 22));
    }

    public void sendFull() {
        fireX10Event(new AddressEvent(this, houseCode, deviceCode));
        fireX10Event(new BrightEvent(this, houseCode, 22, 22));
    }

    /**
     * Fire an X10 Transmission.
     *
     * @param l X10 Transmission listener to add.
     *
     * @author Jesse Peterson <jpeterson@jpeterson.com>
     */
    @SuppressWarnings("unchecked")
    private void fireX10Event(X10Event event) {
        Vector<X10TransmissionListener> v = null;
        synchronized (this) {
            if (x10TransmissionListeners != null) {
                v = (Vector<X10TransmissionListener>) x10TransmissionListeners.clone();
            }
        }
        if (v != null) {
            int cnt = v.size();
            for (int i = 0; i < cnt; i++) {
                X10TransmissionListener client = (X10TransmissionListener) v.elementAt(i);
                client.x10Transmission(event);
            }
        }
    }

    /**
     * Add an X10 Transmission listener.
     *
     * @param l X10 Transmission listener to add.
     *
     * @author Jesse Peterson <jpeterson@jpeterson.com>
     */
    public synchronized void addX10TransmissionListener(X10TransmissionListener l) {
        if (x10TransmissionListeners == null) {
            x10TransmissionListeners = new Vector<X10TransmissionListener>();
        }
        if (!x10TransmissionListeners.contains(l)) {
            x10TransmissionListeners.addElement(l);
        }
    }

    /**
     * Remove an X10 Transmission listener.
     *
     * @param l X10 Transmission listener to remove.
     *
     * @author Jesse Peterson <jpeterson@jpeterson.com>
     */
    public synchronized void removeX10TransmissionListener(X10TransmissionListener l) {
        if (x10TransmissionListeners != null) {
            x10TransmissionListeners.removeElement(l);
        }
    }

    public void setHouseCode(char houseCode) {
        this.houseCode = houseCode;
    }

    public char getHouseCode() {
        return (houseCode);
    }

    public void setDeviceCode(int deviceCode) {
        if ((deviceCode > 16) || (deviceCode < 1)) {
            throw new IllegalArgumentException("Invalid device code");
        }
        this.deviceCode = deviceCode;
    }

    public int getDeviceCode() {
        return (deviceCode);
    }

    /**
     * Serialize the object.
     *
     * @author Jesse Peterson <jpeterson@jpeterson.com>
     */
    @SuppressWarnings("unchecked")
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        Vector<X10TransmissionListener> v = null;
        synchronized (this) {
            if (x10TransmissionListeners != null) {
                v = (Vector<X10TransmissionListener>) x10TransmissionListeners.clone();
            }
        }
        if (v != null) {
            int cnt = v.size();
            for (int i = 0; i < cnt; i++) {
                X10TransmissionListener l = (X10TransmissionListener) v.elementAt(i);
                if (l instanceof Serializable) {
                    stream.writeObject(l);
                }
            }
        }
        stream.writeObject(null);
    }

    /**
     * Deserialize the object.
     *
     * @author Jesse Peterson <jpeterson@jpeterson.com>
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        Object l;
        while (null != (l = stream.readObject())) {
            addX10TransmissionListener((X10TransmissionListener) l);
        }
    }
}
