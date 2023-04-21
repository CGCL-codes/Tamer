package com.eryos.android.cigarettecounter.charts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import com.eryos.android.cigarettecounter.R;
import com.eryos.android.cigarettecounter.io.DataExporter;

public abstract class Chart {

    /** *************** Static Variables *************** **/
    private static final String LOG_TAG = "Chart";

    private static double CHART_RATIO = 0.625;

    private static boolean msgShown = false;

    private static boolean dataAvailable = true;

    protected static final String ROOT_URL = "http://chart.apis.google.com/chart?";

    public static final String BLANK_VALUE = "0";

    public static int DEFAULT_CHART_WIDTH = 320;

    public static int DEFAULT_CHART_HEIGHT = 200;

    public static int DEFAULT_THUMB_WIDTH = 320;

    public static int DEFAULT_THUMB_HEIGHT = 200;

    public static final String DATA_SEPARATOR = ",";

    public static final String LABEL_SEPARATOR = "|";

    protected String type = "lc";

    protected int width = -1;

    protected int height = -1;

    protected int thumbWidth = -1;

    protected int thumbHeight = -1;

    protected String bgColor = "000000";

    protected String title = null;

    protected String titleColor = "FFFFFF";

    protected double titleSize = 12.5;

    protected double textSize = 10.5;

    protected String axisColor = "FFFFFF";

    protected String data = "";

    protected int maxYValue = 22;

    protected int minYValue = 0;

    protected int maxXValue = 31;

    protected int minXValue = 0;

    protected String URL;

    protected String thumbURL;

    protected Bitmap chartBitmap;

    protected Bitmap thumbBitmap;

    public Chart() {
    }

    /**
     * Used to generate the Google Chart API based URL for main chart
     * @return : url based on chart size
     */
    public String getURL() {
        if (width < 0) URL = generateURL(DEFAULT_CHART_WIDTH, DEFAULT_CHART_HEIGHT); else URL = generateURL(width, height);
        return URL;
    }

    /**
     * Used to generate the Google Chart API based URL for thumb chart
     * @return : url depending on chart thumb size
     */
    public String getThumbURL() {
        if (thumbWidth < 0) thumbURL = generateURL(DEFAULT_THUMB_WIDTH, DEFAULT_THUMB_HEIGHT); else thumbURL = generateURL(thumbWidth, thumbHeight);
        return thumbURL;
    }

    /**
     * Used to generate the Google Chart API based url for specific chart size
     * @param _width : chart width
     * @param _height : chart height
     * @return : url for specific size
     */
    protected abstract String generateURL(int _width, int _height);

    /******************* Setters *******************/
    public void setWidth(int _width) {
        this.width = _width;
    }

    public void setHeight(int _height) {
        this.height = _height;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public void setMaxYValue(int maxYValue) {
        this.maxYValue = maxYValue;
    }

    public void setMinYValue(int minYValue) {
        this.minYValue = minYValue;
    }

    public void setMaxXValue(int maxXValue) {
        this.maxXValue = maxXValue;
    }

    public void setMinXValue(int minXValue) {
        this.minXValue = minXValue;
    }

    public void setChartBitmap(Bitmap chartBitmap) {
        this.chartBitmap = chartBitmap;
    }

    public void setThumbBitmap(Bitmap thumbBitmap) {
        this.thumbBitmap = thumbBitmap;
    }

    /******************* Getters *******************/
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getBgColor() {
        return bgColor;
    }

    public String getTitle() {
        return title;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public String getData() {
        return data;
    }

    public int getMaxYValue() {
        return maxYValue;
    }

    public int getMinYValue() {
        return minYValue;
    }

    public int getMaxXValue() {
        return maxXValue;
    }

    public int getMinXValue() {
        return minXValue;
    }

    public double getTitleSize() {
        return titleSize;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public Bitmap getChartBitmap() {
        return chartBitmap;
    }

    public Bitmap getThumbBitmap() {
        return thumbBitmap;
    }

    public void addDataValue(String value) {
        if (data == null || data.length() == 0) data = value; else data = data + Chart.DATA_SEPARATOR + value;
    }

    public void addDataValue(int _value) {
        addDataValue(String.valueOf(_value));
    }

    public void addDataValue(double _value) {
        addDataValue(String.valueOf(_value));
    }

    /**
     * Define chart data, values must be separated with {@DATA_SEPARATOR}
     * @param _data
     */
    public void setData(String _data) {
        if (_data.length() > DATA_SEPARATOR.length() && _data.endsWith(DATA_SEPARATOR)) this.data = _data.substring(0, _data.length() - Chart.DATA_SEPARATOR.length()); else this.data = _data;
    }

    public void setTitleSize(double titleSize) {
        textSize = (titleSize * 0.8);
        this.titleSize = titleSize;
    }

    private static void setDefualtChartWidth(int dEFAULT_CHART_WIDTH) {
        DEFAULT_CHART_WIDTH = dEFAULT_CHART_WIDTH;
    }

    private static void setDefaultChartHeight(int dEFAULT_CHART_HEIGHT) {
        DEFAULT_CHART_HEIGHT = dEFAULT_CHART_HEIGHT;
    }

    private static void setDEFAULT_THUMB_WIDTH(int dEFAULT_THUMB_WIDTH) {
        DEFAULT_THUMB_WIDTH = dEFAULT_THUMB_WIDTH;
    }

    public static void setDEFAULT_THUMB_HEIGHT(int dEFAULT_THUMB_HEIGHT) {
        DEFAULT_THUMB_HEIGHT = dEFAULT_THUMB_HEIGHT;
    }

    /**
     * Define the size of the chart (overview and fullscreen) based on screen size
     * @param SCREEN_WIDTH Device Screen Width
     * @param SCREEN_HEIGHT Device Screen Heigh
     */
    public static void defineChartDefaultSize(int SCREEN_WIDTH, int SCREEN_HEIGHT) {
        Log.d(LOG_TAG, "defineChartDefaultSize");
        Log.d(LOG_TAG, "SCREEN_WIDTH : " + SCREEN_WIDTH);
        Log.d(LOG_TAG, "SCREEN_HEIGHT : " + SCREEN_HEIGHT);
        int OVERVIEW_WIDTH = 320;
        int OVERVIEW_HEIGHT = 200;
        int CHART_WIDTH = 320;
        int CHART_HEIGHT = 200;
        if (SCREEN_WIDTH > SCREEN_HEIGHT) {
            if (SCREEN_HEIGHT < 400) {
                OVERVIEW_WIDTH = (int) (0.67 * SCREEN_WIDTH);
                OVERVIEW_HEIGHT = (int) (CHART_RATIO * OVERVIEW_WIDTH);
            }
        } else {
            if (SCREEN_WIDTH < 400) {
                OVERVIEW_WIDTH = (int) (0.67 * SCREEN_WIDTH);
                OVERVIEW_HEIGHT = (int) (CHART_RATIO * OVERVIEW_WIDTH);
            }
        }
        CHART_WIDTH = (int) (0.95 * SCREEN_WIDTH);
        CHART_HEIGHT = (int) (CHART_RATIO * CHART_WIDTH);
        while ((CHART_WIDTH * CHART_HEIGHT) >= 300000) {
            CHART_WIDTH = (int) (CHART_WIDTH * 0.95);
            CHART_HEIGHT = (int) (CHART_HEIGHT * 0.95);
        }
        CHART_WIDTH = (int) (0.95 * CHART_WIDTH);
        CHART_HEIGHT = (int) (0.95 * CHART_HEIGHT);
        Chart.setDefualtChartWidth(CHART_WIDTH);
        Chart.setDefaultChartHeight(CHART_HEIGHT);
        Chart.setDEFAULT_THUMB_WIDTH(OVERVIEW_WIDTH);
        Chart.setDEFAULT_THUMB_HEIGHT(OVERVIEW_HEIGHT);
        Log.d(LOG_TAG, "FINAL CHART_WIDTH : " + DEFAULT_CHART_WIDTH);
        Log.d(LOG_TAG, "FINAL CHART_HEIGHT : " + DEFAULT_CHART_HEIGHT);
        Log.d(LOG_TAG, "OVERVIEW_WIDTH : " + DEFAULT_THUMB_WIDTH);
        Log.d(LOG_TAG, "OVERVIEW_HEIGHT : " + DEFAULT_THUMB_HEIGHT);
    }

    /**
     * Download a chart from a URL an return a bitmap
     * @param ctx : Application context
     * @param url : the url used to generate the chart
     * @return Bitmap : Bitmap of the chart
     */
    public static Bitmap downloadChartAsBitmap(Context ctx, String url) throws UnknownHostException, IOException {
        Log.i("CigaretteCounter", "downloadBitmap :" + url);
        if (dataAvailable) {
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
                Log.d("CigaretteCounter", "downloadBitmap : OK");
                return bm;
            } catch (UnknownHostException e) {
                dataAvailable = false;
                Log.w("CigaretteCounter", "downloadBitmap No available connection");
                if (!msgShown) {
                    Toast.makeText(ctx, "No available connection, please retry later ...", Toast.LENGTH_SHORT).show();
                    msgShown = true;
                }
                throw e;
            } catch (IOException e) {
                dataAvailable = false;
                Log.e("CigaretteCounter", "downloadBitmap Erreur IO", e);
                throw e;
            }
        }
        return getDefaultChartPicture(ctx);
    }

    /**
     * Return the default chart (used while loading)
     * @param ctx : Context of the application 
     * @return : Bitmap : default chart
     */
    public static Bitmap getDefaultChartPicture(Context ctx) {
        Drawable errorDrawable = ctx.getResources().getDrawable(R.drawable.eryos);
        return ((BitmapDrawable) errorDrawable).getBitmap();
    }

    /**
     * Write a Bitmap to a specific file on SD Cart
     * @param bmp : Bitmap to write
     * @param filename : destination file
     * @return : PathName of the file
     */
    public static String writeBitmapToSD(Bitmap bmp, String filename) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        try {
            DataExporter export = new DataExporter(filename + ".png");
            if (export.openStream()) export.writeDataToExternalFile(bytes.toByteArray());
            export.closeStream();
            return export.getFullPathName();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
