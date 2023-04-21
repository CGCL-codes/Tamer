package com.nzep.device;

import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.nzep.main.NetZeppelin;

/**
 * Gestionnaire de mouvements
 * 
 * @author Sebastien Villemain
 *
 */
public class SensorAccessManager implements SensorEventListener {

    /**
	 * Gestionnaire de mouvements
	 */
    private static SensorAccessManager sensorAccessManager = null;

    /**
	 * Orientation suivant les donn�es du compass
	 */
    private Compass compass = null;

    /**
	 * Gestionnaire de capteur
	 */
    private SensorManager sensorMgr = null;

    /**
	 * Etat du manager
	 */
    private boolean ready = false;

    /**
	 * Inversion des axes
	 */
    private boolean reversed = false;

    /**
	 * Nouveau gestionnaire de mouvements
	 */
    private SensorAccessManager() {
        NetZeppelin context = NetZeppelin.getContext();
        sensorMgr = (SensorManager) context.getSystemService(NetZeppelin.SENSOR_SERVICE);
        create();
        if (ready) {
            if (compass == null) compass = new Compass();
            ready = compass != null;
        } else {
            destroy();
        }
        sensorAccessManager = this;
    }

    /**
	 * Ajout de notre manager
	 */
    private void create() {
        reversed = (NetZeppelin.getOrientation() == Configuration.ORIENTATION_PORTRAIT);
        ready = sensorMgr.registerListener(this, sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_UI);
    }

    /**
	 * Retourne l'�tat des capteurs
	 * 
	 * @return
	 */
    public boolean isReady() {
        return ready;
    }

    /**
	 * Suppression de notre manager
	 */
    public static void destroy() {
        if (sensorAccessManager != null) {
            if (sensorAccessManager.sensorMgr != null) {
                sensorAccessManager.sensorMgr.unregisterListener(sensorAccessManager, sensorAccessManager.sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION));
            }
            sensorAccessManager.ready = false;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
	 * Ecoute des changements de valeurs des capteurs
	 */
    public void onSensorChanged(SensorEvent event) {
        if (ready) {
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                int azimuth = (int) event.values[SensorManager.DATA_X];
                int pitch = (int) event.values[SensorManager.DATA_Y];
                int roll = (int) event.values[SensorManager.DATA_Z];
                if (reversed) {
                    azimuth += 90;
                    if (azimuth > 359) {
                        azimuth = azimuth - 359;
                    }
                    int pitchSaved = pitch;
                    pitch = roll;
                    roll = pitchSaved;
                }
                if (azimuth != compass.getNorthOrientation()) {
                    compass.setNorthOrientation(azimuth);
                }
                if (pitch != compass.getVerticalOrientation()) {
                    compass.setVerticalOrientation(pitch);
                }
                if (roll != compass.getHorizontalOrientation()) {
                    compass.setHorizontalOrientation(roll);
                }
            }
        }
    }

    /**
	 * Retourne l'asservissement de mouvements
	 * 
	 * @return Compass ou null si aucun syst�me d'asservissement activ�
	 */
    public static Compass getCompass() {
        if (sensorAccessManager == null) {
            new SensorAccessManager();
        } else {
            sensorAccessManager.create();
        }
        return sensorAccessManager.compass;
    }
}
