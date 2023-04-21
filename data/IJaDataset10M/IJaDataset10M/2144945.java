package net.sf.astroobserver;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.InflateException;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.widget.ZoomButtonsController;
import android.widget.ZoomButtonsController.OnZoomListener;
import android.widget.TimePicker;

public class HandleSkyMap extends Activity {

    private static int year;

    private static int month, monthDay, hour, minute, second;

    public static ObjectSelected objectOrigineSelectionne;

    public static ObjectSelected objectDestinationSelectionne;

    private final int MENU_FOV = 1007;

    private final int MENU_DATE_ID = 1002;

    private final int MENU_PARAM_ID = 1003;

    private final int MENU_SEARCH_ID = 1004;

    private final int MENU_GPS_ID = 1005;

    private final int MENU_POLARIS_FINDER = 1006;

    private Boolean realTimeNow = true;

    public double degreePerPixelX = 0;

    public double degreePerPixelY = 0;

    public boolean dejaCalculer = false;

    long t1 = 0;

    float temp = 0;

    int cpt1 = 1;

    float[] aValues = new float[3];

    float[] mValues = new float[3];

    private int compteurBoucleOrientation = 0;

    double moyenneAzimuth = 0;

    double moyennePich = 0;

    double moyenneRoll = 0;

    static double moyenneRollFinal = 0;

    float[] values = new float[16];

    float[] RR = new float[16];

    float[] outR = new float[16];

    float[] I = new float[16];

    double azDestination = 180.0;

    double haDestination = 45.0;

    double azDepart = 80.0;

    DrawSkyMap drawSkyMap;

    Boolean orientationSupported;

    private SensorManager mSensorManager;

    GestureDetector gestureScanner;

    OnZoomListener zoomListener;

    ZoomButtonsController zbc;

    Button btTest;

    float oldSpacingForPinchToZoom = 1;

    int countForPinchToZoom = 0;

    Timer top;

    double deltaRaSaoEncour = -1;

    double deltaDecSaoEncour = -1;

    static ImageButton btPlusStar;

    static ImageButton btTextOnOff;

    static ImageButton btGrilleAltazEquato;

    static ImageButton btBlackAndWhite;

    static ImageButton btRing;

    static ImageButton btSensor;

    static int boolText = 0;

    static int grilleType = 0;

    static boolean boolFondBlanc = false;

    static boolean boolSensorActif = false;

    static boolean boolAideAuPointage = false;

    static double deltaAzimuthPushto = 0;

    static double deltaHauteurPushto = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RelativeLayout relative = new RelativeLayout(this);
        RelativeLayout.LayoutParams paramsLayout6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsLayout5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsLayout4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsLayout2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsLayout3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams paramsLayout1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btPlusStar = new ImageButton(this);
        btTextOnOff = new ImageButton(this);
        btGrilleAltazEquato = new ImageButton(this);
        btBlackAndWhite = new ImageButton(this);
        btRing = new ImageButton(this);
        btSensor = new ImageButton(this);
        double coeff = Global.coefHeight;
        if (coeff > 1) {
            coeff = 1;
        }
        double ecartEntreBouton = 70 * coeff;
        int deltaXicon = 5;
        int top = (int) (17 * Global.coefWidth);
        btPlusStar.setBackgroundResource(R.drawable.btmorestar);
        paramsLayout1.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 1) - deltaXicon;
        paramsLayout1.topMargin = top;
        relative.addView(btPlusStar, paramsLayout1);
        btPlusStar.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                refreshSao250();
                drawSkyMap.invalidate();
            }
        });
        btTextOnOff.setBackgroundResource(R.drawable.bttextonoff);
        paramsLayout2.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 2) - deltaXicon;
        paramsLayout2.topMargin = top;
        relative.addView(btTextOnOff, paramsLayout2);
        btTextOnOff.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (boolText == 0) {
                    boolText = 1;
                } else {
                    boolText = 0;
                }
                drawSkyMap.invalidate();
            }
        });
        btGrilleAltazEquato.setBackgroundResource(R.drawable.btgrilleequatoaltaz);
        paramsLayout3.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 3) - deltaXicon;
        paramsLayout3.topMargin = top;
        relative.addView(btGrilleAltazEquato, paramsLayout3);
        btGrilleAltazEquato.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                grilleType++;
                if (grilleType == 3) grilleType = 0;
                drawSkyMap.invalidate();
            }
        });
        btBlackAndWhite.setBackgroundResource(R.drawable.btblackwihte);
        paramsLayout4.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 4) - deltaXicon;
        paramsLayout4.topMargin = top;
        relative.addView(btBlackAndWhite, paramsLayout4);
        btBlackAndWhite.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (boolFondBlanc) {
                    boolFondBlanc = false;
                } else {
                    boolFondBlanc = true;
                }
                drawSkyMap.invalidate();
            }
        });
        btSensor.setBackgroundResource(R.drawable.btsensor);
        paramsLayout5.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 6) - deltaXicon;
        paramsLayout5.topMargin = top;
        relative.addView(btSensor, paramsLayout5);
        btSensor.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                sensor();
            }
        });
        btRing.setBackgroundResource(R.drawable.btring);
        paramsLayout6.leftMargin = Global.screenHeight - (int) (ecartEntreBouton * 7) - deltaXicon;
        paramsLayout6.topMargin = top;
        relative.addView(btRing, paramsLayout6);
        btRing.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (boolAideAuPointage) {
                    boolAideAuPointage = false;
                } else {
                    boolAideAuPointage = true;
                }
                drawSkyMap.invalidate();
            }
        });
        drawSkyMap = new DrawSkyMap(this);
        relative.addView(drawSkyMap);
        setContentView(relative);
        btPlusStar.bringToFront();
        btTextOnOff.bringToFront();
        btGrilleAltazEquato.bringToFront();
        btBlackAndWhite.bringToFront();
        btRing.bringToFront();
        btSensor.bringToFront();
        gestureScanner = new GestureDetector(this, new MyGestureListener());
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Global.objectDestinationselected = false;
        Global.objectOrigineselected = false;
        zbc = new ZoomButtonsController(drawSkyMap);
        zoomListener = new OnZoomListener() {

            public void onVisibilityChanged(boolean visible) {
            }

            public void onZoom(boolean zoomIn) {
                double zoom = drawSkyMap.getZoom();
                if (zoomIn) {
                    zoom = zoom * 1.1;
                } else {
                    zoom = zoom * 0.9;
                }
                drawSkyMap.setZoom(zoom);
                drawSkyMap.invalidate();
            }
        };
        zbc.setOnZoomListener(zoomListener);
        objectOrigineSelectionne = new ObjectSelected();
        objectDestinationSelectionne = new ObjectSelected();
        setDateNow();
        drawSkyMap.setAzLookat(0);
        drawSkyMap.setZoom(0.6);
        drawSkyMap.setHauteurLookat(45);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void onStart() {
        top = new Timer();
        top.schedule(new refreshScreen(), 60000, 60000);
        super.onStart();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuFov = menu.add(0, MENU_FOV, 0, "");
        MenuItem menuDate = menu.add(0, MENU_DATE_ID, 0, "");
        MenuItem menuParam = menu.add(0, MENU_PARAM_ID, 0, getResources().getString(R.string.parametre));
        MenuItem menuSearch = menu.add(0, MENU_SEARCH_ID, 0, getResources().getString(R.string.search));
        MenuItem menuGps = menu.add(0, MENU_GPS_ID, 0, "");
        MenuItem menuPolaris = menu.add(0, MENU_POLARIS_FINDER, 0, "");
        menuFov.setIcon(R.drawable.fov);
        menuGps.setIcon(R.drawable.btgps);
        menuParam.setIcon(R.drawable.btsetupbis);
        menuDate.setIcon(R.drawable.btdate);
        menuPolaris.setIcon(R.drawable.btpolaris);
        menuSearch.setIcon(getResources().getDrawable(R.drawable.btsearch));
        setMenuBackground();
        return super.onCreateOptionsMenu(menu);
    }

    protected void setMenuBackground() {
        getLayoutInflater().setFactory(new Factory() {

            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
                    try {
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);
                        new Handler().post(new Runnable() {

                            public void run() {
                                view.setBackgroundColor(Color.BLACK);
                                view.setFadingEdgeLength(0);
                            }
                        });
                        return view;
                    } catch (InflateException e) {
                    } catch (ClassNotFoundException e) {
                    }
                }
                return null;
            }
        });
    }

    public void alertChangeDateHeure() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View alertDialogView = factory.inflate(R.layout.dateheure, null);
        final DatePicker datepicker = (DatePicker) alertDialogView.findViewById(R.id.datePicker1);
        final TimePicker timePicker = (TimePicker) alertDialogView.findViewById(R.id.timePicker1);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Date ");
        adb.setIcon(android.R.drawable.ic_menu_compass);
        timePicker.setIs24HourView(DateFormat.is24HourFormat(this));
        datepicker.updateDate(year, month - 1, monthDay);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                setDatePerso(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                realTimeNow = false;
                drawSkyMap.invalidate();
            }
        });
        adb.setNeutralButton("Now", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                realTimeNow = true;
                setDateNow();
                drawSkyMap.invalidate();
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
            }
        });
        adb.show();
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case MENU_FOV:
                Intent intent_fovActivity = new Intent(this, fovActivity.class);
                startActivityForResult(intent_fovActivity, 10001);
                break;
            case MENU_DATE_ID:
                alertChangeDateHeure();
                break;
            case MENU_PARAM_ID:
                Intent intent_paramActivity = new Intent(this, ParamActivity.class);
                startActivity(intent_paramActivity);
                break;
            case MENU_SEARCH_ID:
                Intent intent_searchActivity = new Intent(this, SearchDeepSkyActivity.class);
                startActivityForResult(intent_searchActivity, 10000);
                break;
            case MENU_GPS_ID:
                Intent intent_gpsActivity = new Intent(this, ParamGPS.class);
                startActivity(intent_gpsActivity);
                break;
            case MENU_POLARIS_FINDER:
                Intent intent_polaris = new Intent(this, Polaris2Activity.class);
                startActivity(intent_polaris);
                break;
            default:
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void sensor() {
        if (!boolSensorActif) {
            Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            Sensor magField = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
            mSensorManager.registerListener(sensorEventListener, magField, SensorManager.SENSOR_DELAY_FASTEST);
            boolSensorActif = true;
        } else {
            mSensorManager.unregisterListener(sensorEventListener);
            boolSensorActif = false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        Bundle extras = data.getExtras();
        switch(requestCode) {
            case 10000:
                searchRresult(extras.getString("TypeObjet"), extras.getString("numeroObjet"), extras.getInt("position"));
                break;
            case 10001:
                Toast.makeText(this, "Retour FOV", 1000).show();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void searchRresult(String type, String numeObjet, int position) {
        if (type.equals("star")) {
            initValeurObjetOrigine("star", position);
            Star ss = Global.listStar.get(position);
            drawSkyMap.setLookAt(ss.xyz);
            drawSkyMap.invalidate();
        }
        if (type.equals("ngc") || type.equals("ic")) {
            initValeurObjetOrigine(type, position);
            NgcIc nic = Global.listNGC.get(position);
            drawSkyMap.setLookAt(ss.xyz);
            drawSkyMap.invalidate();
        }
        if (type.equals("constellation")) {
            ConstellationName cn = Global.listConstellationName.get(position);
            drawSkyMap.setLookAt(cn.xyz);
            drawSkyMap.invalidate();
        }
        if (type.equals("planet")) {
            initValeurObjetOrigine(type, position);
            PlanetParameters pl = Global.listPlanet.get(position);
            drawSkyMap.setLookAt(pl.xyz);
            drawSkyMap.invalidate();
        }
        if (type.equals("messier")) {
            initValeurObjetOrigine(type, position);
            Messier mes = Global.listMessier.get(position);
            drawSkyMap.setLookAt(mes.xyz);
            drawSkyMap.invalidate();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            Intent intent_searchActivity = new Intent(this, SearchDeepSkyActivity.class);
            startActivityForResult(intent_searchActivity, 10000);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        zbc.setVisible(false);
        top.cancel();
        top.purge();
        super.onPause();
    }

    @Override
    protected void onResume() {
        miseAjourDesAzimuthHauteur();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mSensorManager.unregisterListener(sensorEventListener);
        boolSensorActif = false;
        dejaCalculer = false;
        super.onStop();
    }

    private void initValeurObjetOrigine(String type, int position) {
        objectOrigineSelectionne = new ObjectSelected();
        if (type == "star") {
            Star star = new Star();
            star = Global.listStar.get(position);
            objectOrigineSelectionne.setName(star.name);
            objectOrigineSelectionne.setInfoSupType(star.starName);
            objectOrigineSelectionne.setMagnitude(star.magnitude);
            objectOrigineSelectionne.setDecDecimal(star.dec);
            objectOrigineSelectionne.setRaDecimal(star.ra);
            objectOrigineSelectionne.setAzDecimal(star.azimuth);
            objectOrigineSelectionne.setHaDecimal(star.hauteur);
            objectOrigineSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(star.heureAuMeridien));
            Global.objectOrigineselected = true;
        }
        if (type.equals("ngc") || type.equals("ic")) {
            NgcIc nic = new NgcIc();
            nic = Global.listNGC.get(position);
            objectOrigineSelectionne.setInfoSupType(nic.type);
            objectOrigineSelectionne.setName(nic.name);
            objectOrigineSelectionne.setMagnitude(nic.mag);
            objectOrigineSelectionne.setSize("" + nic.size);
            objectOrigineSelectionne.setDecDecimal(nic.dec);
            objectOrigineSelectionne.setRaDecimal(nic.ra);
            objectOrigineSelectionne.setAzDecimal(nic.azimuth);
            objectOrigineSelectionne.setHaDecimal(nic.hauteur);
            objectOrigineSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(nic.heureAuMeridien));
            Global.objectOrigineselected = true;
        }
        if (type.equals("planet")) {
            PlanetParameters pn = Global.listPlanet.get(position);
            objectOrigineSelectionne.setName(pn.name);
            objectOrigineSelectionne.setInfoSupType("");
            objectOrigineSelectionne.setMagnitude("");
            objectOrigineSelectionne.setDecDecimal(pn.dec);
            objectOrigineSelectionne.setRaDecimal(pn.ra);
            objectOrigineSelectionne.setAzDecimal(pn.azimuth);
            objectOrigineSelectionne.setHaDecimal(pn.hauteur);
            objectOrigineSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(pn.heureAuMeridien));
            Global.objectOrigineselected = true;
            Global.objectOrigineselected = true;
        }
        if (type.equals("messier")) {
            Messier mes = Global.listMessier.get(position);
            objectOrigineSelectionne.setName(mes.longName);
            objectOrigineSelectionne.setInfoSupType(mes.type);
            objectOrigineSelectionne.setMagnitude(mes.magnitude);
            objectOrigineSelectionne.setSize("" + mes.size);
            objectOrigineSelectionne.setDecDecimal(mes.decDecimal);
            objectOrigineSelectionne.setRaDecimal(mes.raDecimal);
            objectOrigineSelectionne.setAzDecimal(mes.azimuth);
            objectOrigineSelectionne.setHaDecimal(mes.hauteur);
            objectOrigineSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(mes.heureAuMeridien));
            Global.objectOrigineselected = true;
            Global.objectOrigineselected = true;
        }
    }

    private void initValeurObjetDestination(String type, int position) {
        objectDestinationSelectionne = new ObjectSelected();
        if (type == "star") {
            Star star = new Star();
            star = Global.listStar.get(position);
            objectDestinationSelectionne.setName(star.name);
            objectDestinationSelectionne.setInfoSupType(star.starName);
            objectDestinationSelectionne.setMagnitude(star.mag);
            objectDestinationSelectionne.setDecDecimal(star.dec);
            objectDestinationSelectionne.setRaDecimal(star.ra);
            objectDestinationSelectionne.setAzDecimal(star.azimuth);
            objectDestinationSelectionne.setHaDecimal(star.hauteur);
            objectDestinationSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(star.heureAuMeridien));
            Global.objectDestinationselected = true;
        }
        if (type == "ngc") {
            NgcIc nic = new NgcIc();
            nic = Global.listNGC.get(position);
            objectDestinationSelectionne.setName(nic.name);
            objectDestinationSelectionne.setSize("" + nic.size);
            objectDestinationSelectionne.setInfoSupType(nic.type);
            objectDestinationSelectionne.setMagnitude(nic.mag);
            objectDestinationSelectionne.setDecDecimal(nic.dec);
            objectDestinationSelectionne.setRaDecimal(nic.ra);
            objectDestinationSelectionne.setAzDecimal(nic.azimuth);
            objectDestinationSelectionne.setHaDecimal(nic.hauteur);
            objectDestinationSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(nic.heureAuMeridien));
            Global.objectDestinationselected = true;
        }
        if (type.equals("planet")) {
            PlanetParameters pn = Global.listPlanet.get(position);
            objectDestinationSelectionne.setName(pn.name);
            objectDestinationSelectionne.setInfoSupType("");
            objectDestinationSelectionne.setMagnitude("");
            objectDestinationSelectionne.setDecDecimal(pn.dec);
            objectDestinationSelectionne.setRaDecimal(pn.ra);
            objectDestinationSelectionne.setAzDecimal(pn.azimuth);
            objectDestinationSelectionne.setHaDecimal(pn.hauteur);
            objectDestinationSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(pn.heureAuMeridien));
            Global.objectDestinationselected = true;
        }
        if (type.equals("messier")) {
            Messier mes = Global.listMessier.get(position);
            objectDestinationSelectionne.setName(mes.longName);
            objectDestinationSelectionne.setSize("" + mes.size);
            objectDestinationSelectionne.setInfoSupType(mes.type);
            objectDestinationSelectionne.setMagnitude(mes.magnitude);
            objectDestinationSelectionne.setDecDecimal(mes.decDecimal);
            objectDestinationSelectionne.setRaDecimal(mes.raDecimal);
            objectDestinationSelectionne.setAzDecimal(mes.azimuth);
            objectDestinationSelectionne.setHaDecimal(mes.hauteur);
            objectDestinationSelectionne.setHeureMeridian(AstroCompute.degreDecimalToHMS(mes.heureAuMeridien));
            Global.objectDestinationselected = true;
        }
    }

    public void setDateNow() {
        double azTemp = drawSkyMap.getAzLookat();
        Time t = new Time();
        t.setToNow();
        t.switchTimezone("GMT");
        setDatePerso(t.year, t.month, t.monthDay, t.hour, t.minute, t.second);
        int dt[] = { year, month, monthDay };
        int ti[] = { hour, minute, second };
        AstroCompute.setDate(dt, ti);
        miseAjourDesAzimuthHauteur();
        drawSkyMap.setAzLookat(azTemp);
    }

    public void setDatePerso(int _year, int _month, int _monthDay, int _hour, int _minute, int _second) {
        double azTemp = drawSkyMap.getAzLookat();
        Time t = new Time();
        t.year = _year;
        t.month = _month;
        t.monthDay = _monthDay;
        t.hour = _hour;
        t.minute = _minute;
        t.second = 0;
        year = _year;
        month = _month + 1;
        monthDay = _monthDay;
        hour = _hour;
        minute = _minute;
        second = _second;
        int dt[] = { year, month, monthDay };
        int ti[] = { hour, minute, second };
        AstroCompute.setDate(dt, ti);
        miseAjourDesAzimuthHauteur();
        drawSkyMap.setAzLookat(azTemp);
    }

    public void passageAuMeridien() {
        int cpt = 0;
        int[] dateTU = { year, month, monthDay };
        int[] heureTU = { hour, minute, second };
        cpt = 0;
        for (Star star : Global.listStar) {
            star.heureAuMeridien = AstroCompute.toHeureMeridianLocalTime(dateTU, heureTU, star.ra);
            Global.listStar.set(cpt, star);
            cpt++;
        }
        cpt = 0;
        for (Messier mes : Global.listMessier) {
            mes.heureAuMeridien = AstroCompute.toHeureMeridianLocalTime(dateTU, heureTU, mes.raDecimal);
            Global.listMessier.set(cpt, mes);
            cpt++;
        }
        cpt = 0;
        for (NgcIc nic : Global.listNGC) {
            nic.heureAuMeridien = AstroCompute.toHeureMeridianLocalTime(dateTU, heureTU, nic.ra);
            Global.listNGC.set(cpt, nic);
            cpt++;
        }
    }

    public void passageAuMeridienPlanete() {
        int cpt = 0;
        for (PlanetParameters pn : Global.listPlanet) {
            pn.heureAuMeridien = AstroCompute.toHeureMeridianLocalTime(pn.ra);
            Global.listPlanet.set(cpt, pn);
            cpt++;
        }
    }

    public void miseAjourDesAzimuthHauteur() {
        double _hauteur;
        double _Azimuth;
        double[] altaz;
        double _hauteur2;
        double _Azimuth2;
        double[] altaz2;
        long t1 = SystemClock.uptimeMillis();
        int cpt = 0;
        for (StarSao star : Global.listStarSao250) {
            altaz = AstroCompute.hauteurAzimutDegree(star.ra, star.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            star.hauteur = _hauteur;
            star.azimuth = _Azimuth;
            Global.listStarSao250.set(cpt, star);
            cpt++;
        }
        cpt = 0;
        for (Equato eq : Global.listEquato) {
            altaz = AstroCompute.hauteurAzimutDegree(eq.ra, eq.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            eq.ha = (float) _hauteur;
            eq.az = (float) _Azimuth;
            Global.listEquato.set(cpt, eq);
            cpt++;
        }
        cpt = 0;
        for (Star star : Global.listStar) {
            altaz = AstroCompute.hauteurAzimutDegree(star.ra, star.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            star.hauteur = _hauteur;
            star.azimuth = _Azimuth;
            Global.listStar.set(cpt, star);
            cpt++;
        }
        cpt = 0;
        for (NameNebulaAndGalaxy nb : Global.listNameNebulaGalaxy) {
            altaz = AstroCompute.hauteurAzimutDegree(nb.ra, nb.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            nb.hauteur = _hauteur;
            nb.azimuth = _Azimuth;
            Global.listNameNebulaGalaxy.set(cpt, nb);
            cpt++;
        }
        cpt = 0;
        for (Messier mes : Global.listMessier) {
            altaz = AstroCompute.hauteurAzimutDegree(mes.raDecimal, mes.decDecimal);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            mes.hauteur = _hauteur;
            mes.azimuth = _Azimuth;
            Global.listMessier.set(cpt, mes);
            cpt++;
        }
        Log.i("Timing", "stars, etc. " + (SystemClock.uptimeMillis() - t1));
        initListPlanet(dateTU, heureTU);
        cpt = 0;
        for (PlanetParameters pn : Global.listPlanet) {
            altaz = AstroCompute.hauteurAzimutDegree(pn.ra, pn.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            pn.hauteur = _hauteur;
            pn.azimuth = _Azimuth;
            Global.listPlanet.set(cpt, pn);
            cpt++;
        }
        passageAuMeridienPlanete();
        cpt = 0;
        for (NgcIc nic : Global.listNGC) {
            altaz = AstroCompute.hauteurAzimutDegree(nic.ra, nic.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            nic.hauteur = _hauteur;
            nic.azimuth = _Azimuth;
            Global.listNGC.set(cpt, nic);
            cpt++;
        }
        cpt = 0;
        for (ConstellationName cs : Global.listConstellationName) {
            altaz = AstroCompute.hauteurAzimutDegree(cs.ra, cs.dec);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            cs.hauteur = _hauteur;
            cs.azimuth = _Azimuth;
            Global.listConstellationName.set(cpt, cs);
            cpt++;
        }
        cpt = 0;
        for (ConstellationLine val : Global.listConstellationLines) {
            altaz = AstroCompute.hauteurAzimutDegree(dateTU, heureTU, val.ra1, val.dec1);
            _hauteur = altaz[0];
            _Azimuth = altaz[1];
            val.ha1 = _hauteur;
            val.az1 = _Azimuth;
            altaz2 = AstroCompute.hauteurAzimutDegree(val.ra2, val.dec2);
            _hauteur2 = altaz2[0];
            _Azimuth2 = altaz2[1];
            val.ha2 = _hauteur2;
            val.az2 = _Azimuth2;
            Global.listConstellationLines.set(cpt, val);
            cpt++;
        }
        altaz = AstroCompute.hauteurAzimutDegree(objectOrigineSelectionne.getRaDecimal(), objectOrigineSelectionne.getDecDecimal());
        _hauteur = altaz[0];
        _Azimuth = altaz[1];
        objectOrigineSelectionne.setAzDecimal(_Azimuth);
        objectOrigineSelectionne.setHaDecimal(_hauteur);
        altaz = AstroCompute.hauteurAzimutDegree(objectDestinationSelectionne.getRaDecimal(), objectDestinationSelectionne.getDecDecimal());
        _hauteur = altaz[0];
        _Azimuth = altaz[1];
        objectDestinationSelectionne.setAzDecimal(_Azimuth);
        objectDestinationSelectionne.setHaDecimal(_hauteur);
        if (!dejaCalculer) {
            passageAuMeridien();
            dejaCalculer = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (gestureScanner.onTouchEvent(ev)) return true; else return false;
    }

    private float[] calculateOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        float[] outR = new float[9];
        SensorManager.getRotationMatrix(R, null, aValues, mValues);
        SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X, SensorManager.AXIS_Z, outR);
        SensorManager.getOrientation(outR, values);
        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);
        return values;
    }

    double nbBoucle = 20;

    double deltaAz = 0;

    double deltaHA = 0;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent event) {
            double az, pitch, roll;
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) aValues = event.values.clone();
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) mValues = event.values.clone();
            float result[] = calculateOrientation();
            az = result[0];
            pitch = result[1];
            roll = result[2];
            compteurBoucleOrientation++;
            if (compteurBoucleOrientation <= nbBoucle) {
                moyenneAzimuth += az;
                moyennePich += pitch;
                moyenneRoll += roll;
            } else {
                compteurBoucleOrientation = 0;
                moyenneAzimuth = moyenneAzimuth / nbBoucle;
                moyennePich = moyennePich / nbBoucle;
                moyenneRoll = ((moyenneRoll / nbBoucle) + 90) * -1;
                if (Math.abs(moyenneRollFinal - moyenneRoll) > 2) {
                    moyenneRollFinal = moyenneRoll;
                }
                if (moyenneAzimuth < 0) {
                    moyenneAzimuth = moyenneAzimuth + 360.0;
                }
                ;
                moyennePich = moyennePich * -1;
                azDestination = moyenneAzimuth;
                haDestination = moyennePich;
                deltaAz = AstroCompute.calculateDifferenceBetweenAngles(azDestination, drawSkyMap.getAzLookat());
                deltaHA = AstroCompute.calculateDifferenceBetweenAngles(haDestination, drawSkyMap.getHauteurLookat());
            }
            if ((System.currentTimeMillis() - t1) > 50) {
                double div = 10.0;
                double truc = drawSkyMap.getAzLookat() - deltaAz / div;
                if (truc < 0) {
                    truc = truc + 360;
                }
                ;
                drawSkyMap.setAzLookat(truc);
                drawSkyMap.setHauteurLookat(drawSkyMap.getHauteurLookat() - deltaHA / div);
                t1 = System.currentTimeMillis();
                drawSkyMap.invalidate();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void refreshSao250() {
        String req = "";
        double[] radec = { 0, 0 };
        double ra = 0;
        double dec = 0;
        double raMin = 0;
        double raMax = 0;
        double deltaRa = 10;
        double deltaDec = 5;
        radec = AstroCompute.altazToRaDEc(drawSkyMap.getHauteurLookat(), drawSkyMap.getAzLookat());
        ra = radec[0];
        dec = radec[1];
        if (Math.abs(dec) >= 0 && Math.abs(dec) < 10) {
            deltaDec = 5;
            deltaRa = 6;
        }
        if (Math.abs(dec) >= 10 && Math.abs(dec) < 20) {
            deltaDec = 5;
            deltaRa = 6;
        }
        if (Math.abs(dec) >= 20 && Math.abs(dec) < 30) {
            deltaDec = 5;
            deltaRa = 6;
        }
        if (Math.abs(dec) >= 30 && Math.abs(dec) < 40) {
            deltaDec = 5;
            deltaRa = 6;
        }
        if (Math.abs(dec) >= 40 && Math.abs(dec) < 50) {
            deltaDec = 5;
            deltaRa = 6;
        }
        if (Math.abs(dec) >= 50 && Math.abs(dec) < 60) {
            deltaDec = 5;
            deltaRa = 10;
        }
        if (Math.abs(dec) >= 60 && Math.abs(dec) < 70) {
            deltaDec = 5;
            deltaRa = 12;
        }
        if (Math.abs(dec) >= 70 && Math.abs(dec) < 80) {
            deltaDec = 5;
            deltaRa = 15;
        }
        if (Math.abs(dec) >= 80 && Math.abs(dec) < 90) {
            deltaDec = 3;
            deltaRa = 30;
        }
        if (ra < 0) ra = 360 + ra;
        raMin = ra - deltaRa;
        if (raMin < 0) raMin = 360 + raMin;
        raMin = raMin % 360;
        raMax = ra + deltaRa;
        raMax = raMax % 360;
        if (raMin > raMax) {
            req = " ((ra<=360 and ra>" + raMin + ") or ( ra>=0 and ra<" + raMax + ")) and (dec<" + (dec + deltaDec) + " and dec>" + (dec - deltaDec) + ") ";
        } else {
            req = " ra>" + raMin + " and ra<" + raMax + " and dec<" + (dec + deltaDec) + " and dec>" + (dec - deltaDec) + " ";
        }
        DataBaseHelper2 dh = new DataBaseHelper2(this);
        dh.openDataBase();
        dh.selectWhereStarSao(req);
        dh.close();
        miseAjourDesAzimuthHauteur();
    }

    class refreshScreen extends TimerTask {

        public void run() {
            HandleSkyMap.this.runOnUiThread(new Runnable() {

                public void run() {
                    if (realTimeNow) {
                        setDateNow();
                        drawSkyMap.invalidate();
                    }
                }
            });
        }
    }

    public void log(String str) {
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    private void initListPlanet(int[] dateTU, int[] heureTU) {
        Global.listPlanet.clear();
        PlanetComputing mercury = new PlanetComputing("mercury", dateTU, heureTU);
        PlanetParameters pn3 = new PlanetParameters();
        pn3.ra = mercury.getRa();
        pn3.dec = mercury.getDec();
        pn3.name = "mercury";
        Global.listPlanet.add(pn3);
        PlanetComputing venus = new PlanetComputing("venus", dateTU, heureTU);
        PlanetParameters pn4 = new PlanetParameters();
        pn4.ra = venus.getRa();
        pn4.dec = venus.getDec();
        pn4.name = "venus";
        Global.listPlanet.add(pn4);
        PlanetComputing mars = new PlanetComputing("mars", dateTU, heureTU);
        PlanetParameters pn5 = new PlanetParameters();
        pn5.ra = mars.getRa();
        pn5.dec = mars.getDec();
        pn5.name = "mars";
        Global.listPlanet.add(pn5);
        PlanetComputing jupiter = new PlanetComputing("jupiter", dateTU, heureTU);
        PlanetParameters pn = new PlanetParameters();
        pn.ra = jupiter.getRa();
        pn.dec = jupiter.getDec();
        pn.name = "jupiter";
        Global.listPlanet.add(pn);
        PlanetComputing saturn = new PlanetComputing("saturn", dateTU, heureTU);
        PlanetParameters pn1 = new PlanetParameters();
        pn1.ra = saturn.getRa();
        pn1.dec = saturn.getDec();
        pn1.name = "saturn";
        Global.listPlanet.add(pn1);
        PlanetComputing uranus = new PlanetComputing("uranus", dateTU, heureTU);
        PlanetParameters pn6 = new PlanetParameters();
        pn6.ra = uranus.getRa();
        pn6.dec = uranus.getDec();
        pn6.name = "uranus";
        Global.listPlanet.add(pn6);
        PlanetComputing neptune = new PlanetComputing("neptune", dateTU, heureTU);
        PlanetParameters pn7 = new PlanetParameters();
        pn7.ra = neptune.getRa();
        pn7.dec = neptune.getDec();
        pn7.name = "neptune";
        Global.listPlanet.add(pn7);
        PlanetComputing moon = new PlanetComputing("moon", dateTU, heureTU);
        PlanetParameters pn8 = new PlanetParameters();
        pn8.ra = moon.getRa();
        pn8.dec = moon.getDec();
        pn8.name = "moon";
        Global.listPlanet.add(pn8);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        Timer timing = new Timer();
        timing.schedule(new TimerTask() {

            @Override
            public void run() {
                closeOptionsMenu();
            }
        }, 6000);
        return super.onPrepareOptionsMenu(menu);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        public boolean onSingleTapUp(MotionEvent ev) {
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent ev) {
            boolean trouver = false;
            double tx = ev.getX();
            double ty = ev.getY();
            double delta = 40;
            if (Global.objectOrigineselected && Global.objectDestinationselected) {
                Global.objectOrigineselected = false;
                Global.objectDestinationselected = false;
            }
            if (!Global.objectOrigineselected) {
                for (PlanetParameters pla : Global.listPlanet) {
                    if (pla.x > (tx - delta) && pla.x < (tx + delta) && pla.y < (ty + delta) && pla.y > (ty - delta)) {
                        trouver = true;
                        initValeurObjetOrigine("planet", Global.listPlanet.indexOf(pla));
                        break;
                    }
                }
                if (!trouver) {
                    for (Messier mes : Global.listMessier) {
                        if (mes.x > (tx - delta) && mes.x < (tx + delta) && mes.y < (ty + delta) && mes.y > (ty - delta)) {
                            trouver = true;
                            initValeurObjetOrigine("messier", Global.listMessier.indexOf(mes));
                            break;
                        }
                    }
                }
                if (!trouver) {
                    for (NgcIc nic : Global.listNGC) {
                        if (nic.x > (tx - delta) && nic.x < (tx + delta) && nic.y < (ty + delta) && nic.y > (ty - delta) && (nic.mag <= Global.magnitudeNgcIC || Global.showNebula)) {
                            trouver = true;
                            initValeurObjetOrigine("ngc", Global.listNGC.indexOf(nic));
                            break;
                        }
                    }
                }
                if (!trouver) {
                    for (Star star : Global.listStar) {
                        if (star.x > (tx - delta) && star.x < (tx + delta) && star.y < (ty + delta) && star.y > (ty - delta) && star.mag < 5.1) {
                            trouver = true;
                            initValeurObjetOrigine("star", Global.listStar.indexOf(star));
                            break;
                        }
                    }
                }
            } else {
                for (PlanetParameters pla : Global.listPlanet) {
                    if (pla.x > (tx - delta) && pla.x < (tx + delta) && pla.y < (ty + delta) && pla.y > (ty - delta)) {
                        trouver = true;
                        initValeurObjetDestination("planet", Global.listPlanet.indexOf(pla));
                        break;
                    }
                }
                if (!trouver) {
                    for (Messier mes : Global.listMessier) {
                        if (mes.x > (tx - delta) && mes.x < (tx + delta) && mes.y < (ty + delta) && mes.y > (ty - delta)) {
                            trouver = true;
                            initValeurObjetDestination("messier", Global.listMessier.indexOf(mes));
                            break;
                        }
                    }
                }
                if (!trouver) {
                    for (NgcIc nic : Global.listNGC) {
                        if (nic.x > (tx - delta) && nic.x < (tx + delta) && nic.y < (ty + delta) && nic.y > (ty - delta) && (nic.mag <= Global.magnitudeNgcIC || Global.showNebula)) {
                            trouver = true;
                            initValeurObjetDestination("ngc", Global.listNGC.indexOf(nic));
                            break;
                        }
                    }
                }
                if (!trouver) {
                    for (Star star : Global.listStar) {
                        if (star.x > (tx - delta) && star.x < (tx + delta) && star.y < (ty + delta) && star.y > (ty - delta) && star.mag < 5.1) {
                            trouver = true;
                            initValeurObjetDestination("star", Global.listStar.indexOf(star));
                            break;
                        }
                    }
                }
            }
            drawSkyMap.invalidate();
            return super.onSingleTapConfirmed(ev);
        }

        @Override
        public void onShowPress(MotionEvent ev) {
        }

        @Override
        public void onLongPress(MotionEvent ev) {
            openOptionsMenu();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            switch(e2.getPointerCount()) {
                case 1:
                    if (countForPinchToZoom == 2) {
                        drawSkyMap.setAzLookat(drawSkyMap.getAzLookat() + (distanceX * Global.scale) / (30.0 * drawSkyMap.getZoom()));
                        drawSkyMap.setHauteurLookat(drawSkyMap.getHauteurLookat() - ((distanceY * Global.scale) / (30f * drawSkyMap.getZoom())) / 1);
                        drawSkyMap.invalidate();
                    }
                    break;
                case 2:
                    countForPinchToZoom--;
                    if (countForPinchToZoom < 0) {
                        float scale = spacing(e2) / oldSpacingForPinchToZoom;
                        double zoom = drawSkyMap.getZoom() * scale;
                        drawSkyMap.setZoom(zoom);
                        drawSkyMap.invalidate();
                    }
                    oldSpacingForPinchToZoom = spacing(e2);
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent ev) {
            if (Global.showBtZoom) {
                zbc.setVisible(true);
            }
            countForPinchToZoom = 2;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (Global.objectOrigineselected && Global.objectDestinationselected) {
                Global.objectDestinationselected = false;
            } else {
                Global.objectOrigineselected = false;
            }
            drawSkyMap.invalidate();
            return super.onDoubleTap(e);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
