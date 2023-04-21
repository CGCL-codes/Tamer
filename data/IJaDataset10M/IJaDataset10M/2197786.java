package com.inetmon.jn.mplsgeneralstatistic.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;
import javax.swing.Timer;
import com.inetmon.jn.decoder.DecodedPacket;
import com.inetmon.jn.decoder.Decoder;
import com.inetmon.jn.core.engine.IRawPacketListener;
import com.inetmon.jn.core.internal.PacketContainer;
import com.inetmon.jn.statistic.MPLSPacketStat;
import com.inetmon.jn.statistic.PacketStat;
import com.inetmon.jn.statistic.VLANPacketStat;

/** Listeners of the general statistics provider */
class MPLSGeneralStatListener {

    /** a view */
    GeneralMPLSView listener;

    /** a recorder */
    MPLSPacketStat recorder;
}

public class MPLSStatProvider extends IRawPacketListener {

    private static final long serialVersionUID = 3258410638299838514L;

    /**
	 * list of listener
	 */
    private Vector<MPLSGeneralStatListener> generalStatModelListeners;

    private Timer timer;

    Decoder decoder2 = new Decoder();

    GeneralMPLSView listener;

    private static final int REFRESHDELAY = 1000;

    /**
	 * Constructor
	 */
    public MPLSStatProvider() {
        generalStatModelListeners = new Vector<MPLSGeneralStatListener>();
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
    public void addView(GeneralMPLSView view, MPLSPacketStat statRecorder) {
        MPLSGeneralStatListener listener = new MPLSGeneralStatListener();
        listener.listener = view;
        listener.recorder = (MPLSPacketStat) statRecorder;
        generalStatModelListeners.add(listener);
    }

    /**
	 * Remove a view from the model
	 * 
	 * @param view
	 *            view to remove
	 */
    public void removeView(GeneralMPLSView view) {
        Iterator<MPLSGeneralStatListener> it = generalStatModelListeners.iterator();
        while (it.hasNext()) {
            MPLSGeneralStatListener listener = it.next();
            if (listener.listener.equals(view)) generalStatModelListeners.remove(listener);
            return;
        }
    }

    /**
	 * Update the view of the model
	 */
    private void updateView() {
        ListIterator<MPLSGeneralStatListener> it = generalStatModelListeners.listIterator();
        while (it.hasNext()) {
            it.next().listener.asyncUpdateView();
        }
    }

    public void newPacket(PacketContainer packet) {
    }

    public void newPacket2(DecodedPacket packet) {
        Iterator<MPLSGeneralStatListener> it = generalStatModelListeners.iterator();
        while (it.hasNext()) {
            MPLSGeneralStatListener listener = it.next();
            listener.recorder.addPacket2(packet);
        }
    }

    public void addPacket(DecodedPacket packet) {
    }
}
