package net.krecan.mapky;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Mapky extends Activity {

    private static final String QUERY_URI_TEMPLATE = "http://www.mapy.cz/search?query=%f+%f&centerX=135549861&centerY=134989697&portWidth=1310&portHeight=737&zoom=5&forceMapParams=1&mapType=base-n,firmpoi,basepoi&js=1";

    private static final String MAP_URI_TEMPLATE = "http://www.mapy.cz/#x=%d@y=%d@z=13@mm=TTtP@st=s@ssq=%f+%f";

    private static final int BUFFER_SIZE = 512;

    private OnClickListener okClickedHandler = new OnClickListener() {

        public void onClick(View v) {
            LocationManager locationManager = getLocationManager();
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                showMessage(R.string.no_gps);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location == null) {
                    showMessage(R.string.can_not_find_location);
                }
            }
            openLocation(location.getLongitude(), location.getLatitude());
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button okButton = (Button) findViewById(R.id.locate);
        okButton.setOnClickListener(okClickedHandler);
    }

    private void openLocation(double longitude, double latitude) {
        try {
            URL url = new URL(String.format(QUERY_URI_TEMPLATE, latitude, longitude));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copy(url.openStream(), baos);
            JSONObject object = new JSONObject(new String(baos.toByteArray()));
            JSONObject center = object.getJSONObject("searchSetup").getJSONObject("center");
            long x = center.getLong("x");
            long y = center.getLong("y");
            String targetUri = String.format(MAP_URI_TEMPLATE, x, y, latitude, longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetUri));
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, "net.krecan.mapky");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("mapky", e.getMessage());
        }
    }

    private LocationManager getLocationManager() {
        return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void showMessage(int messageId) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, getText(messageId), duration);
        toast.show();
    }

    /**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Closes both streams when done.
	 * @param in the stream to copy from
	 * @param out the stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }
}
