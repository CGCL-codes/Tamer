package org.progeeks.audio;

import java.util.*;
import javax.sound.sampled.*;

/**
 *  Provides a listening point for sound input devices.  Callers
 *  can register listeners to be notified about new sound data events.
 *
 *  @version   $Revision: 1.5 $
 *  @author    Paul Speed
 */
public class SoundInput {

    private String name;

    private List listenerList = new ArrayList();

    private DataListener[] listeners;

    public SoundInput(String name) {
        this.name = name;
    }

    public String getName() {
        return (name);
    }

    public void addDataListener(DataListener listener) {
        listenerList.add(listener);
        DataListener[] newListeners = new DataListener[listenerList.size()];
        listeners = (DataListener[]) listenerList.toArray(newListeners);
    }

    public void removeDataListener(DataListener listener) {
        listenerList.remove(listener);
        DataListener[] newListeners = new DataListener[listenerList.size()];
        listeners = (DataListener[]) listenerList.toArray(newListeners);
    }

    public boolean hasDataListener() {
        return (listenerList.size() > 0);
    }

    /**
     *  Called by the local sound classes to create a data event.
     */
    protected void dispatchSoundData(byte[] buffer, int size, AudioFormat format, int synchId) {
        if (listeners == null) return;
        SoundEvent event = new SoundEvent(this, buffer, size, format, synchId);
        DataListener[] array = listeners;
        for (int i = 0; i < array.length; i++) {
            array[i].newSoundData(event);
        }
    }
}
