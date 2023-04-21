package org.ros.rosjava.android.tf.android_tf_tools;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import org.ros.node.DefaultNodeFactory;
import org.ros.node.Node;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMain;
import org.ros.rosjava.tf.pubsub.TransformBroadcaster;

/**
 * @author nick@heuristiclabs.com (Nick Armstrong-Crews)
 */
public class GlobalPosePublisherTf implements NodeMain {

    protected final LocationManager locMan;

    private final SensorManager sensorManager;

    private Node node;

    private OrientationListener orientationListener;

    private static String[] providerPriorityList = { "gps", "network" };

    private final class OrientationListener implements SensorEventListener {

        protected String provider;

        protected org.ros.message.geometry_msgs.Point origin;

        protected OrientationListener(String provider) {
            this.provider = provider;
            origin = null;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                if (provider != null) {
                    Location loc = locMan.getLastKnownLocation(provider);
                    if (origin == null) origin = new org.ros.message.geometry_msgs.Point();
                    origin.x = loc.getLatitude();
                    origin.y = loc.getLongitude();
                    if (loc.hasAltitude()) origin.z = loc.getAltitude(); else origin.z = 0;
                }
                if (origin != null) {
                    float[] q = new float[4];
                    SensorManager.getQuaternionFromVector(q, event.values);
                    tfb.sendTransform("/earth", "/phone", ((long) System.currentTimeMillis()) * 1000000, origin.x, origin.y, origin.z, q[1], q[2], q[3], q[0]);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }

    protected TransformBroadcaster tfb;

    public GlobalPosePublisherTf(SensorManager sensorManager, LocationManager locMan) {
        this.sensorManager = sensorManager;
        this.locMan = locMan;
    }

    @Override
    public void main(NodeConfiguration configuration) throws Exception {
        try {
            node = new DefaultNodeFactory().newNode("android/global_pose_publisher", configuration);
            tfb = new TransformBroadcaster(node);
            String provider = null;
            for (String p : providerPriorityList) {
                if (locMan.isProviderEnabled(p)) {
                    Location loc = locMan.getLastKnownLocation(p);
                    if (loc != null) {
                        provider = p;
                        break;
                    }
                }
            }
            orientationListener = new OrientationListener(provider);
            Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            sensorManager.registerListener(orientationListener, sensor, 500000);
        } catch (Exception e) {
            if (node != null) {
                node.getLog().fatal(e);
            } else {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void shutdown() {
        sensorManager.unregisterListener(orientationListener);
        node.shutdown();
    }
}
