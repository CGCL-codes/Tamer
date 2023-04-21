package net.walkingtools.android.view;

import java.util.Hashtable;
import java.util.Vector;
import com.google.android.maps.GeoPoint;
import net.walkingtools.android.Constants;
import net.walkingtools.android.Navigator;
import net.walkingtools.android.NavigatorListener;
import net.walkingtools.android.Utilities;
import net.walkingtools.android.activity.R;
import net.walkingtools.gpsTypes.hiperGps.HiperWaypoint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.hardware.*;
import android.location.*;
import android.os.Bundle;
import android.view.View;

/**
 * If implementing this class in your own Activity (instead of using the ready made 
 * CompassActivity: It is very important to call onResume() and setEnabled(true) when 
 * starting this widget, as that is where many good things like sensors and threads are 
 * wired up. The status  message will state "onResume() not called" as hopefully 
 * helpful reminder to anyone trying to implement this class in their own Activity. 
 * Also, call onPause() from the Activity onPause(), and setEnabled(false).
 * @see android.app.Activity
 * @author Brett Stalbaum
 * @since 0.0.9
 * @version 0.1.1
 * 
 */
public class CompassView extends View implements Navigator, LocationListener, GpsStatus.Listener, SensorEventListener {

    protected Location currentLocation = null;

    /**
	 * Coordinates describing target location
	 */
    protected Location target = null;

    /**
	 * current compass heading
	 */
    protected float gpsCompassHeading = 0;

    /**
	 * the altitude
	 */
    protected float altitude = 0;

    /**
	 * The speed in meters per second
	 */
    protected float speed = 0;

    /**
	 * true if in navigation mode
	 */
    protected boolean navigating = false;

    /**
	 * This is the argument to setTarget() that is mirrored in protected Location target
	 * it is kept so that it can be returned, but the Location target is used for 
	 * active location
	 * @see #setTarget(HiperWaypoint)
	 */
    protected HiperWaypoint targetWpt = null;

    private Vector<HiperWaypoint> auxPoints = null;

    private Location firstFix = null;

    private LocationManager lm = null;

    private Criteria criteria = null;

    private Paint paintFillSmall = null;

    private Paint paintMedium = null;

    private Paint paintMediumBold = null;

    private Paint paintBlack = null;

    private Paint[] compassRoseFont = { null, null, null };

    private float directionPointerAzimuth = 180;

    private boolean isMoving = false;

    private int viewState = -1;

    private Resources res = null;

    private int w = 160;

    private int h = 160;

    private float centerX = 80;

    private float centerY = 80;

    private boolean onTarget = false;

    private boolean showBlink = true;

    private BlinkTimer blinker = null;

    private boolean trailDirectionOn = false;

    private boolean trailDirectionTerminus = true;

    private String message = "onResume() not called";

    private SensorManager sensorManager = null;

    private Sensor sensorCompass = null;

    private boolean directionSensorAvailable = false;

    private boolean useDirectionSensor = false;

    private float sensorCompassHeading = 0;

    private float declination = 0;

    private Hashtable<Object, NavigatorListener> navigatorListeners = null;

    private long lastFixTime = 0;

    boolean showSpeedNotAzimuth = true;

    boolean isEnabled = false;

    private float[] compassSmoother = { 0, 0, 0, 0 };

    Context context;

    /**
	 * Constructor
	 * @param context
	 */
    public CompassView(Context context) {
        super(context);
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensorCompass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            directionSensorAvailable = true;
        }
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setBearingRequired(true);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setSpeedRequired(true);
        criteria.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setCostAllowed(false);
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        lastFixTime = System.currentTimeMillis();
        firstFix = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        paintFillSmall = new Paint();
        paintFillSmall.setTypeface(Typeface.SANS_SERIF);
        paintFillSmall.setStyle(Paint.Style.FILL);
        paintFillSmall.setColor(Color.BLACK);
        paintFillSmall.setStrokeWidth(4);
        paintMedium = new Paint();
        paintMedium.setTypeface(Typeface.SANS_SERIF);
        paintMedium.setTextAlign(Align.CENTER);
        paintMedium.setStyle(Paint.Style.STROKE);
        paintMediumBold = new Paint();
        paintMediumBold.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
        paintMediumBold.setTextAlign(Align.CENTER);
        paintBlack = new Paint(paintFillSmall);
        paintBlack.setColor(Color.BLACK);
        compassRoseFont[0] = paintFillSmall;
        compassRoseFont[1] = paintMedium;
        compassRoseFont[2] = paintMediumBold;
        res = getResources();
        isEnabled = true;
    }

    @Override
    public void setTarget(HiperWaypoint wpt) {
        targetWpt = wpt;
        Location loc = new Location((String) null);
        loc.setLatitude(wpt.getLatitude());
        loc.setLongitude(wpt.getLongitude());
        loc.setAltitude(wpt.getElevation());
        this.target = loc;
        navigating = true;
        updateState();
        if (blinker == null) {
            blinker = new BlinkTimer();
            blinker.start();
        }
        invalidate();
    }

    @Override
    public void addAuxiliaryTarget(HiperWaypoint wpt) {
        if (auxPoints == null) {
            auxPoints = new Vector<HiperWaypoint>(50);
        }
        auxPoints.add(wpt);
    }

    @Override
    public void removeAuxiliaryTarget(HiperWaypoint wpt) {
        if (auxPoints != null) {
            auxPoints.remove(wpt);
        }
    }

    @Override
    public void removeAllAuxiliaryTargets() {
        auxPoints = null;
    }

    /**
	 * stops navigation and turns off navigation arrow
	 */
    @Override
    public void stopNavigation() {
        navigating = false;
        lm.removeUpdates(this);
        lm.removeGpsStatusListener(this);
        target = null;
        auxPoints = null;
        updateState();
        invalidate();
    }

    /**
	 * A preference setting method, whether or not to use the compass sensor if available
	 * @param useDirectionSensor
	 */
    public void useDirectionSensor(boolean useDirectionSensor) {
        if (this.useDirectionSensor == true && useDirectionSensor == false) {
            if (directionSensorAvailable) {
                sensorManager.unregisterListener(this);
            }
        } else if (this.useDirectionSensor == false && useDirectionSensor == true) {
            sensorManager.registerListener(this, sensorCompass, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            return;
        }
        this.useDirectionSensor = useDirectionSensor;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            if (directionSensorAvailable && useDirectionSensor) {
                sensorManager.registerListener(this, sensorCompass, SensorManager.SENSOR_DELAY_NORMAL);
            }
            if (navigating) {
                if (blinker == null) {
                    blinker = new BlinkTimer();
                    blinker.start();
                }
            }
        } else {
            if (directionSensorAvailable && useDirectionSensor) {
                sensorManager.unregisterListener(this);
            }
            if (blinker != null) {
                blinker.kill();
                blinker = null;
            }
        }
        this.isEnabled = enabled;
    }

    /**
	 * Pauses the View, removing location updates. Should be called 
	 * by the Activity onPause() method.
	 * @see android.app.Activity#onPause()
	 */
    public void onPause() {
        lm.removeUpdates(this);
        lm.removeGpsStatusListener(this);
        viewState = -1;
    }

    /**
	 * Activates the view by requesting location updates
	 * by the Activity onResume() method.
	 * @see android.app.Activity#onResume()
	 */
    public void onResume() {
        if (message.equals("onResume() not called")) {
            message = "";
        }
        lm.requestLocationUpdates(3000, 1, criteria, this, null);
        lm.addGpsStatusListener(this);
        updateState();
    }

    /**
	 * By default the compass displays the current speed. Some applications may require azimuth
	 * (or alternation between the values.) This method toggles the displayed value. (Upper right 
	 * hand corner of the screen.)
	 */
    public void toggleSpeedAzimuthDisplay() {
        showSpeedNotAzimuth = !showSpeedNotAzimuth;
    }

    /** Returns the cardinal or intercardinal direction of a set of Coordinates
     * in relation to the user's current position.
     * @param mc the Coordinates
     * @return the direction String
     */
    public String directionTo(Location mc) {
        int azimuthTocurrentLocation = (int) currentLocation.bearingTo(mc);
        String direction = "";
        if (azimuthTocurrentLocation > 337 || azimuthTocurrentLocation <= 22) {
            direction = "North";
        } else if (azimuthTocurrentLocation > 22 && azimuthTocurrentLocation <= 67) {
            direction = "North East";
        } else if (azimuthTocurrentLocation > 67 && azimuthTocurrentLocation <= 112) {
            direction = "East";
        } else if (azimuthTocurrentLocation > 112 && azimuthTocurrentLocation <= 157) {
            direction = "South East";
        } else if (azimuthTocurrentLocation > 157 && azimuthTocurrentLocation <= 202) {
            direction = "South";
        } else if (azimuthTocurrentLocation > 202 && azimuthTocurrentLocation <= 247) {
            direction = "South West";
        } else if (azimuthTocurrentLocation > 247 && azimuthTocurrentLocation <= 292) {
            direction = "West";
        } else if (azimuthTocurrentLocation > 247 && azimuthTocurrentLocation <= 337) {
            direction = "North West";
        }
        return direction;
    }

    private String timeConverter(int tSeconds) {
        int minutes = tSeconds / 60;
        int seconds = tSeconds % 60;
        String time = minutes + ":";
        if (seconds < 10) {
            time += "0" + seconds;
        } else {
            time += seconds;
        }
        return time;
    }

    /** Returns the distance from users current
     * position to a set of Coordinates.
     * @param mc the Coordinates
     * @return the distance
     */
    public float distanceTo(Location mc) {
        return currentLocation.distanceTo(mc);
    }

    /** Returns the time (in milliseconds) it will take to reach a set of Coordinates
     * from the user's current position given current rate of speed.
     * (This is a very dynamic variable, suitable for realtime updates.)
     * returns -1 if the current location is not known.
     * @param mc the Coordinates
     * @return the time in milliseconds to the destination
     */
    public float timeTo(Location mc) {
        if (currentLocation == null) {
            return -1;
        } else {
            return (currentLocation.distanceTo(mc) / speed) * 1000;
        }
    }

    /** Returns an estimate (in milliseconds) of the time it would take 
     * an average human to walk to a location from the current position.
     * The estimate is very rough, based on the assumption that average 
     * human walking speed is 5K per hour. Suitable for on screen estimates. 
     * returns -1 if the current location is not known.
     * @param mc the Coordinates
     * @return the time estimate in milliseconds to the destination
     */
    public float estimateTimeTo(Location mc) {
        if (currentLocation == null) {
            return -1;
        } else {
            return (currentLocation.distanceTo(mc) / 1.388889f) * 1000;
        }
    }

    /** Method returns data from distanceTo, directionTo, and timeTo
     * as a single String object in the following format:
     * Direction: North East
     * Distance: 105 m
     * Time: 0:39
     * @param mc the Coordinates ob
     * @return String the data
     */
    public String getInfo(Location mc) {
        float distance = this.distanceTo(mc);
        String time = timeConverter((int) this.timeTo(mc));
        StringBuffer s = new StringBuffer();
        s.append("Direction: " + this.directionTo(mc) + "\n");
        if (distance >= 1000) {
            s.append("Distance: " + (int) ((distance / 1000) + .5) + " Kilometers \nTime:" + time);
        } else {
            s.append("Distance: " + distance + " Meters \nTime:" + time);
        }
        return s.toString();
    }

    /**
     * Sets an extra message to be displayed on the compass, for any purpose
     * @param message the message to display
     */
    public void setMessage(String message) {
        this.message = message;
        invalidate();
    }

    /**
     * GeoPoint representing the current location
     * @return a GeoPoint representing the current location
     */
    public GeoPoint getCurrentGeoPoint() {
        if (currentLocation != null) {
            return new GeoPoint((int) (currentLocation.getLatitude() * 1000000), (int) (currentLocation.getLongitude() * 1000000));
        } else {
            if (firstFix == null) {
                return new GeoPoint(0, 0);
            } else {
                return new GeoPoint((int) (firstFix.getLatitude() * 1000000), (int) (firstFix.getLongitude() * 1000000));
            }
        }
    }

    /**
     * Turns on the trail direction indicator toward the terminus (end) of trail.
     */
    public void setTowardTerminus() {
        trailDirectionTerminus = true;
        trailDirectionOn = true;
        invalidate();
    }

    /**
     * Turns on the trail direction indicator toward the trail head (begin) of trail.
     */
    public void setTowardTrailHead() {
        trailDirectionTerminus = false;
        trailDirectionOn = true;
        invalidate();
    }

    /**
     * Turns off the trail direction indicator
     */
    public void turnOffTrailDirection() {
        trailDirectionOn = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();
        centerX = w / 2;
        centerY = h / 2;
        if (!isEnabled) {
            paintFillSmall.setColor(res.getColor(R.color.LTGRAY));
            paintMediumBold.setColor(res.getColor(R.color.LTGRAY));
            canvas.drawCircle(centerX, centerY, w / 4, paintFillSmall);
            return;
        }
        float heading = (directionSensorAvailable && useDirectionSensor ? sensorCompassHeading : gpsCompassHeading);
        int textSize = 15;
        paintFillSmall.setTextSize(textSize);
        float pixelLenthChar = Utilities.calculateTextSize(paintFillSmall);
        while (pixelLenthChar * 30 < w) {
            textSize++;
            paintFillSmall.setTextSize(textSize);
            pixelLenthChar = Utilities.calculateTextSize(paintFillSmall);
        }
        paintMedium.setTextSize(textSize * 1.5f);
        paintMediumBold.setTextSize(textSize * 1.5f);
        canvas.drawColor(Color.BLACK);
        float space = (paintMedium.getFontSpacing() / 2) + 2;
        switch(viewState) {
            case STATE_IS_MOVING:
            case STATE_MOVING_NAVIGATING:
                if (onTarget) {
                    paintFillSmall.setColor(Color.GREEN);
                    paintMedium.setColor(Color.GREEN);
                    paintMediumBold.setColor(Color.GREEN);
                } else {
                    paintFillSmall.setColor(res.getColor(R.color.HIGHVISYELLOW));
                    paintMedium.setColor(res.getColor(R.color.HIGHVISYELLOW));
                    paintMediumBold.setColor(res.getColor(R.color.HIGHVISYELLOW));
                }
                canvas.drawCircle(centerX, centerY, w / 4, paintFillSmall);
                canvas.drawText(message, centerX, h - centerY / 3, paintMedium);
                break;
            case STATE_NOT_MOVING:
            case STATE_NOT_MOVING_NAVIGATING:
                paintFillSmall.setColor(res.getColor(R.color.ORANGE));
                paintMedium.setColor(res.getColor(R.color.ORANGE));
                paintMediumBold.setColor(res.getColor(R.color.ORANGE));
                canvas.drawText("MOVE for COMPASS", centerX, centerY / 2, paintMediumBold);
                canvas.drawCircle(centerX, centerY, w / 4, paintFillSmall);
                canvas.drawText(message, centerX, h - centerY / 3, paintMedium);
                break;
            case STATE_UN_LOCATED:
                paintFillSmall.setColor(Color.RED);
                paintMediumBold.setColor(Color.RED);
                paintMedium.setColor(Color.RED);
                canvas.drawText("SEEKING LOCATION", centerX, centerY / 2, paintMediumBold);
                canvas.drawCircle(centerX, centerY, w / 4, paintFillSmall);
                canvas.drawRect(centerX - space, centerY - space, centerX + space, centerY + space, paintBlack);
                canvas.drawText("?", centerX, centerY + paintFillSmall.getFontSpacing() / 2, paintMedium);
                canvas.drawText(message, centerX, h - centerY / 3, paintMedium);
                return;
            default:
        }
        float radius = w / 4;
        float offset = 0;
        float roseAngle = 270 - heading;
        float radians;
        float anchorPoint;
        float x;
        float y;
        float length;
        float vertRads;
        String data = null;
        switch(viewState) {
            case STATE_NOT_MOVING:
                return;
            case STATE_IS_MOVING:
            case STATE_MOVING_NAVIGATING:
                if (showSpeedNotAzimuth) {
                    data = "Speed:" + (int) ((speed / 1000) * 60 * 60) + "KpH";
                } else {
                    data = "Azimuth:" + (int) heading;
                }
                float len = getStringLengthPixels(data, paintFillSmall);
                canvas.drawText(data, w - len - 10, paintFillSmall.getFontSpacing(), paintFillSmall);
                data = "ALT:" + altitude;
                canvas.drawText(data, 10, paintFillSmall.getFontSpacing(), paintFillSmall);
                radius = w / 4;
                offset = 0;
                radians = (float) (-(heading + 270f) * (Math.PI / 180));
                anchorPoint = w / 8;
                x = centerX - anchorPoint;
                y = h / 12;
                length = centerX + anchorPoint;
                anchorPoint = w / 12;
                if (showBlink) {
                    float[] triangle = { centerX, 0, x, y, x, y, length, y, length, y, centerX, 0 };
                    canvas.drawLines(triangle, paintFillSmall);
                    canvas.drawLine(x + anchorPoint, y, x + anchorPoint, y + y, paintFillSmall);
                    canvas.drawLine(length - anchorPoint, y, length - anchorPoint, y + y, paintFillSmall);
                }
                PathEffect savedPathEffect = paintBlack.getPathEffect();
                PathEffect dotPathEffect = new DashPathEffect(new float[] { 5, 5 }, 0);
                paintBlack.setPathEffect(dotPathEffect);
                x = rotateX(radians, radius) + centerX;
                y = rotateY(radians, radius) + centerY;
                radians = -(heading + 90) * ((float) (Math.PI / 180));
                length = rotateX(radians, radius) + centerX;
                anchorPoint = rotateY(radians, radius) + centerY;
                canvas.drawLine(x, y, length, anchorPoint, paintBlack);
                vertRads = radius - (radius * .15f);
                x = -(heading + 80);
                y = -(heading + 100);
                paintBlack.setPathEffect(savedPathEffect);
                canvas.drawLine(length, anchorPoint, rotateX(x * (((float) Math.PI) / 180f), vertRads) + centerX, rotateY(x * (((float) Math.PI) / 180f), vertRads) + centerY, paintFillSmall);
                canvas.drawLine(length, anchorPoint, rotateX(y * (((float) Math.PI) / 180f), vertRads) + centerX, rotateY(y * (((float) Math.PI) / 180f), vertRads) + centerY, paintFillSmall);
                paintBlack.setPathEffect(dotPathEffect);
                radians = -(heading + 180) * (((float) Math.PI) / 180f);
                x = rotateX(radians, radius) + centerX;
                y = rotateY(radians, radius) + centerY;
                radians = -(heading) * (((float) Math.PI) / 180f);
                length = rotateX(radians, radius) + centerX;
                anchorPoint = rotateY(radians, radius) + centerY;
                canvas.drawLine(x, y, length, anchorPoint, paintBlack);
                paintBlack.setPathEffect(savedPathEffect);
                Paint temp = null;
                paintFillSmall.setTextAlign(Align.CENTER);
                for (int i = 0; i < Constants.compassRoseAzimuth.length; i++) {
                    length = (i % 2);
                    if (i == 0) {
                        temp = compassRoseFont[2];
                    } else {
                        temp = compassRoseFont[Math.abs((int) length - 1)];
                    }
                    length++;
                    roseAngle = Constants.compassRoseAzimuth[i] - heading;
                    if (roseAngle < 0) {
                        roseAngle = 360 + roseAngle;
                    }
                    float compassRoseRadius = radius + radius / 4f;
                    radians = roseAngle * (((float) Math.PI) / 180);
                    x = rotateX(radians, compassRoseRadius);
                    y = rotateY(radians, compassRoseRadius);
                    space = temp.getFontSpacing() / 3;
                    canvas.drawText(Constants.compassRoseLabels, (int) offset, (int) length, x + centerX, y + centerY + space, temp);
                    offset += length;
                }
                paintFillSmall.setTextAlign(Align.LEFT);
        }
        switch(viewState) {
            case STATE_MOVING_NAVIGATING:
                if (onTarget) {
                    canvas.drawText("FOLLOW ARROW", centerX, centerY / 2, paintMediumBold);
                } else {
                    if (showBlink) {
                        if (directionSensorAvailable && useDirectionSensor) {
                            canvas.drawText("TURN ARROW TO ARROW", centerX, centerY / 2, paintMediumBold);
                        } else {
                            canvas.drawText("WALK ARROW TO ARROW", centerX, centerY / 2, paintMediumBold);
                        }
                    }
                }
                if (showBlink) {
                    offset = (int) (radius * 0.95);
                    roseAngle = 90 + directionPointerAzimuth - heading;
                    radians = roseAngle * (((float) Math.PI) / 180f);
                    x = rotateX(radians, offset) + centerX;
                    y = rotateY(radians, offset) + centerY;
                    radians = (roseAngle + 180) * (((float) Math.PI) / 180f);
                    length = rotateX(radians, offset) + centerX;
                    anchorPoint = rotateY(radians, offset) + centerY;
                    canvas.drawLine(x, y, length, anchorPoint, paintBlack);
                    vertRads = radius - (int) (radius * .3);
                    x = (roseAngle + 170);
                    y = (roseAngle + 190);
                    canvas.drawLine(length, anchorPoint, rotateX(x * (((float) Math.PI) / 180f), vertRads) + centerX, rotateY(x * (((float) Math.PI) / 180f), vertRads) + centerY, paintBlack);
                    canvas.drawLine(length, anchorPoint, rotateX(y * (((float) Math.PI) / 180f), vertRads) + centerX, rotateY(y * (((float) Math.PI) / 180f), vertRads) + centerY, paintBlack);
                }
            case STATE_NOT_MOVING_NAVIGATING:
                length = currentLocation.distanceTo(target);
                String centDis = null;
                if (length >= 1000) {
                    length = ((int) (length / 1000) + 1);
                    data = "Dis:" + length + "K";
                    centDis = (int) length + "K";
                } else {
                    data = "Dis:" + ((int) length) + "M";
                    centDis = (int) length + "M";
                }
                x = getStringLengthPixels(centDis, paintFillSmall);
                y = paintFillSmall.getFontSpacing();
                canvas.drawRect(centerX - x / 2 - 3, centerY - y / 2 - 3, centerX + x / 2 + 3, centerY + y / 2 + 3, paintBlack);
                canvas.drawText(centDis, centerX - (x / 2), centerY + y / 3, paintFillSmall);
                canvas.drawText(data, 10, h - y, paintFillSmall);
                if (trailDirectionOn) {
                    String trailTerminusString = null;
                    y = paintFillSmall.getFontSpacing();
                    x = 0;
                    if (trailDirectionTerminus) {
                        trailTerminusString = "OUTBOUND (terminus)";
                    } else {
                        trailTerminusString = "INBOUND (trailhead)";
                    }
                    x = getStringLengthPixels(trailTerminusString, paintFillSmall);
                    canvas.drawText(trailTerminusString, w - x - 10, h - y, paintFillSmall);
                }
        }
    }

    private void updateState() {
        if (currentLocation == null) {
            viewState = STATE_UN_LOCATED;
        } else if (isMoving) {
            if (navigating) {
                viewState = STATE_MOVING_NAVIGATING;
            } else {
                viewState = STATE_IS_MOVING;
            }
        } else {
            if (navigating) {
                if (directionSensorAvailable && useDirectionSensor) {
                    viewState = STATE_MOVING_NAVIGATING;
                } else {
                    viewState = STATE_NOT_MOVING_NAVIGATING;
                }
            } else {
                if (directionSensorAvailable && useDirectionSensor) {
                    viewState = STATE_IS_MOVING;
                } else {
                    viewState = STATE_NOT_MOVING;
                }
            }
        }
    }

    private float rotateX(float radians, float radius) {
        float x = (float) (radius * Math.cos(radians));
        return x;
    }

    private float rotateY(float radians, float radius) {
        float y = (float) (radius * Math.sin(radians));
        return y;
    }

    private float getStringLengthPixels(String str, Paint paint) {
        float[] lens = new float[str.length()];
        paint.getTextWidths(str, 0, str.length(), lens);
        float accumulate = 0f;
        for (int i = 0; i < lens.length; i++) {
            accumulate += lens[i];
        }
        return accumulate;
    }

    @Override
    public void onGpsStatusChanged(int event) {
        if (event == GpsStatus.GPS_EVENT_FIRST_FIX) {
            firstFix = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            GeomagneticField field = new GeomagneticField((float) firstFix.getLatitude(), (float) firstFix.getLongitude(), (float) firstFix.getAltitude(), System.currentTimeMillis());
            declination = field.getDeclination();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        gpsCompassHeading = location.getBearing();
        speed = location.getSpeed();
        if (speed > 0) {
            isMoving = true;
        } else {
            isMoving = false;
        }
        altitude = (float) location.getAltitude();
        if (navigating) {
            directionPointerAzimuth = currentLocation.bearingTo(target);
            if (directionPointerAzimuth < 0) {
                directionPointerAzimuth = directionPointerAzimuth + 360;
            }
        }
        updateState();
        invalidate();
        long currentFixTime = location.getTime();
        if (navigatorListeners != null) {
            for (NavigatorListener listener : navigatorListeners.values()) {
                listener.motionStatusUpdate(isMoving);
                listener.navigationReady(!(currentLocation == null));
                listener.updateInterval(currentFixTime - lastFixTime);
                listener.locationUpdate(getCurrentGeoPoint());
                if (navigating) {
                    float distance = currentLocation.distanceTo(target);
                    if (distance <= targetWpt.getRadius()) {
                        listener.arrivedAtTarget(targetWpt, (int) distance);
                    }
                    if (auxPoints != null) {
                        for (int i = 0; i < auxPoints.size(); i++) {
                            Location tempLoc = new Location((String) null);
                            HiperWaypoint tempHwpt = auxPoints.get(i);
                            tempLoc.setLatitude(tempHwpt.getLatitude());
                            tempLoc.setLongitude(tempHwpt.getLongitude());
                            tempLoc.setAltitude(tempHwpt.getElevation());
                            distance = currentLocation.distanceTo(tempLoc);
                            if (distance <= tempHwpt.getRadius()) {
                                listener.arrivedAtAuxiliaryTarget(tempHwpt, (int) distance);
                            }
                        }
                    }
                }
            }
        }
        lastFixTime = currentFixTime;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch(status) {
            case LocationProvider.OUT_OF_SERVICE:
                currentLocation = null;
                updateState();
                break;
        }
    }

    @Override
    public void addNavigatorListener(NavigatorListener navigatorListener) {
        if (navigatorListeners == null) {
            navigatorListeners = new Hashtable<Object, NavigatorListener>(5);
        }
        navigatorListeners.put(navigatorListener, navigatorListener);
    }

    @Override
    public void removeNavigatorListener(NavigatorListener navigatorListener) {
        if (navigatorListeners == null) {
            return;
        }
        navigatorListeners.remove(navigatorListener);
        if (navigatorListeners.isEmpty()) {
            navigatorListeners = null;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
            switch(accuracy) {
                case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
                    if (!directionSensorAvailable) {
                        sensorManager.registerListener(this, sensorCompass, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    directionSensorAvailable = true;
                    break;
                case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
                case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
                case SensorManager.SENSOR_STATUS_UNRELIABLE:
                    sensorManager.unregisterListener(this);
                    directionSensorAvailable = false;
                    break;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            for (int i = 0; i < compassSmoother.length - 1; i++) {
                compassSmoother[i] = compassSmoother[i + 1];
            }
            compassSmoother[compassSmoother.length - 1] = event.values[0] + declination;
            float accumulate = 0;
            for (int i = 0; i < compassSmoother.length; i++) {
                accumulate += compassSmoother[i];
            }
            sensorCompassHeading = accumulate / compassSmoother.length;
            postInvalidate();
        }
    }

    private class BlinkTimer extends Thread {

        boolean alive = true;

        void kill() {
            alive = false;
        }

        @Override
        public void run() {
            while (navigating && alive) {
                float heading = (directionSensorAvailable ? sensorCompassHeading : gpsCompassHeading);
                float positiveHeading = heading + 360;
                float positiveDirectionPointer = directionPointerAzimuth + 360;
                if ((positiveHeading >= positiveDirectionPointer - 7 && positiveHeading <= positiveDirectionPointer + 7)) {
                    onTarget = true;
                    showBlink = true;
                } else {
                    onTarget = false;
                    showBlink = !showBlink;
                }
                if (System.currentTimeMillis() - lastFixTime > 20000) {
                    currentLocation = null;
                    updateState();
                }
                try {
                    postInvalidate();
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                }
            }
            showBlink = false;
            onTarget = false;
        }
    }
}
