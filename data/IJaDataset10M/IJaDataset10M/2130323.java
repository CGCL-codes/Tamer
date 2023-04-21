package com.inetmon.jn.statistic.general.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.Timer;
import jpcap.packet.Packet;
import com.inetmon.jn.core.engine.IRawPacketListener;
import com.inetmon.jn.core.internal.PacketContainer;
import com.inetmon.jn.decoder.DecodedPacket;
import com.inetmon.jn.statistic.PacketStat;
import com.inetmon.jn.statistic.PerSecondStat;
import com.inetmon.jn.statistic.general.views.GeneralView;

/** Listeners of the general statistics provider */
class GeneralStatListener {

    /** a view */
    IGeneralStatListener listener;

    /** a recorder */
    PacketStat recorder;
}

public class GeneralStatProvider extends IRawPacketListener {

    private static final long serialVersionUID = 3258410638299838514L;

    /**
	 * list of listener
	 */
    private Vector<GeneralStatListener> generalStatModelListeners;

    /** date of the last update of the values and the graph */
    private long lastUpdateTime = 0;

    /** date of the last packet captured */
    private long lastPacketTime = 0;

    private Timer timer;

    private static final int REFRESHDELAY = 1000;

    private boolean marker;

    private long markerBegin;

    /**
	 * Constructor
	 */
    public GeneralStatProvider() {
        generalStatModelListeners = new Vector<GeneralStatListener>();
        ActionListener taskPerformer = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                updateView();
            }
        };
        timer = new Timer(REFRESHDELAY, taskPerformer);
        timer.start();
    }

    /**
	 * Add a view to the model
	 * 
	 * @param view
	 *            a view to add
	 * @param statRecorder
	 *            the recorder corresponding to the view
	 */
    public synchronized void addView(GeneralView view, PacketStat statRecorder) {
        GeneralStatListener listener = new GeneralStatListener();
        listener.listener = view;
        listener.recorder = (PacketStat) statRecorder;
        generalStatModelListeners.add(listener);
    }

    /**
	 * Remove a view from the model
	 * 
	 * @param view
	 *            view to remove
	 */
    public synchronized void removeView(GeneralView view) {
        Iterator<GeneralStatListener> it = generalStatModelListeners.iterator();
        while (it.hasNext()) {
            GeneralStatListener listener = it.next();
            if (listener.listener.equals(view)) generalStatModelListeners.remove(listener);
            return;
        }
    }

    /**
	 * Update the view of the model
	 */
    private synchronized void updateView() {
        ListIterator<GeneralStatListener> it = generalStatModelListeners.listIterator();
        while (it.hasNext()) {
            it.next().listener.asyncUpdateView();
        }
    }

    public void newPacket2(DecodedPacket packet) {
        long packetTime = packet.getSecond();
        Iterator<GeneralStatListener> it = generalStatModelListeners.iterator();
        while (it.hasNext()) {
            GeneralStatListener listener = it.next();
            listener.recorder.addPacket(packet);
        }
        lastPacketTime = packetTime;
    }

    public void newPacket(PacketContainer packet) {
    }
}
