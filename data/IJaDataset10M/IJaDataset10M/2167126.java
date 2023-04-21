package ar.edu.uade.android.servicios;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import ar.edu.uade.android.controladores.ControladorGPS;
import ar.edu.uade.android.utils.GpsPosition;

public class RobotService extends Service {

    public static final String NEW_GPS_POSITION = "New_Gps_Position";

    private ControladorGPS servicioGPS;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int arg1) {
        super.onStart(intent, arg1);
        if (NEW_GPS_POSITION.equals(intent.getAction())) {
            new GPSAsyncTask().execute();
            stopSelf();
            return;
        }
    }

    private void sendNewGpsPositionUpdate(GpsPosition position) {
        Intent intent = new Intent(NEW_GPS_POSITION);
        intent.putExtra("newGpsPosition", position);
        sendBroadcast(intent);
    }

    private class GPSAsyncTask extends AsyncTask<Void, Void, GpsPosition> {

        @Override
        protected GpsPosition doInBackground(Void... params) {
            servicioGPS = new ControladorGPS();
            GpsPosition pos = new GpsPosition();
            pos.setLatitud(servicioGPS.getLatitude());
            pos.setLongitud(servicioGPS.getLongitude());
            return pos;
        }

        @Override
        protected void onPostExecute(GpsPosition position) {
            super.onPostExecute(position);
            sendNewGpsPositionUpdate(position);
        }
    }
}
