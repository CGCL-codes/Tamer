package org.jude.client.graph;

import java.awt.*;
import java.applet.*;
import java.util.*;
import java.lang.*;
import java.io.StreamTokenizer;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;

/**
 * This class will load data (as a seperate thread) into a DataSet
 * given a URL.
 *
 * @version  $Revision: 1.2 $, $Date: 2001/02/12 00:34:16 $
 * @author   Leigh Brookshaw
 */
public class LoadData extends Thread {

    /**
     * Flag used to specify the type of data to be loaded. That is purely
     * numerical.
     */
    public final int NUMERIC = 1;

    /**
     * Flag used to specify the type of data to be loaded. That is 
     * can contain non-numerical data. Not Implemented
     */
    public final int OBJECT = 2;

    private Graph2D graph = null;

    private Applet applet = null;

    private URL file;

    private int Max = 250;

    private DataSet ds = null;

    private double array[] = null;

    private boolean finished = false;

    private boolean started = false;

    private int count = 0;

    private int dataType = NUMERIC;

    /**
     * Instantiate the class
     */
    public LoadData() {
        finished = false;
        started = false;
    }

    /**
     * Instantiate the class
     *
     * @param d DataSet to load the data into.
     */
    public LoadData(DataSet d) {
        ds = d;
        finished = false;
        started = false;
    }

    /**
     * Start loading the data into a/the DataSet.
     *
     * @param file URL of data file
     * @return The DataSet that the data will be loaded into
     */
    public DataSet loadDataSet(URL file) {
        if (file == null) return null;
        if (ds == null) ds = new DataSet();
        this.file = file;
        this.start();
        return ds;
    }

    /**
     * Start loading the data into an Array.
     *
     * @param file URL of data file
     */
    public void loadArray(URL file) {
        if (file == null) return;
        this.file = file;
        this.start();
    }

    /**
     * Start loading the data into a/the DataSet.
     *
     * @param file URL of data file
     * @param drawable An object that can be drawn to that will indicate
     * that data is loading. eg. An applet or the Graph2D canvas.
     */
    public DataSet loadDataSet(URL file, Object drawable) {
        if (file == null) return null;
        if (ds == null) ds = new DataSet();
        this.file = file;
        if (drawable != null) {
            if (drawable instanceof Applet) {
                applet = (Applet) drawable;
            } else if (drawable instanceof Graph2D) {
                graph = (Graph2D) drawable;
                graph.attachDataSet(ds);
            }
        }
        this.start();
        return ds;
    }

    /**
     * Start loading the data into an array.
     *
     * @param file URL of data file
     * @param drawable An object that can be drawn to that will indicate
     * that data is loading. eg. An applet or the Graph2D canvas.
     */
    public void loadArray(URL file, Object drawable) {
        if (file == null) return;
        this.file = file;
        if (drawable != null) {
            if (drawable instanceof Applet) {
                applet = (Applet) drawable;
            } else if (drawable instanceof Graph2D) {
                graph = (Graph2D) drawable;
                graph.attachDataSet(ds);
            }
        }
        this.start();
        return;
    }

    /**
     * The method to be run as a seperate thread. It does all the work
     */
    public void run() {
        int datamax = 2 * Max;
        byte b[] = new byte[50];
        int nbytes = 0;
        double data[] = new double[datamax];
        int size = 0;
        InputStream is = null;
        boolean comment = false;
        int c;
        setPriority(Thread.MIN_PRIORITY);
        try {
            is = file.openStream();
            started = true;
            count = 0;
            while ((c = is.read()) > -1) {
                switch(c) {
                    case '#':
                        comment = true;
                        break;
                    case '\r':
                    case '\n':
                        comment = false;
                    case ' ':
                    case '\t':
                        if (nbytes > 0) {
                            String s = new String(b, 0, 0, nbytes);
                            data[size] = Double.valueOf(s).doubleValue();
                            size++;
                            if (size >= datamax) {
                                append(data, size);
                                size = 0;
                            }
                            nbytes = 0;
                        }
                        break;
                    default:
                        if (!comment) {
                            b[nbytes] = (byte) c;
                            nbytes++;
                        }
                        break;
                }
            }
            if (size > 1) append(data, size);
            if (is != null) is.close();
        } catch (Exception e) {
            printmessage("Error loading data!");
            return;
        }
        started = false;
        finished = true;
        if (count == 0) {
            printmessage("Zero data loaded!");
            return;
        }
    }

    /**
     * @return The DataSet that is being filled.
     */
    public DataSet getDataSet() {
        return ds;
    }

    /**
     * @return The loaded data array.
     */
    public synchronized double[] getArray() {
        if (array == null || array.length == 0) return null;
        if (finished) return array;
        if (started) {
            double tmp[] = new double[array.length];
            System.arraycopy(array, 0, tmp, 0, array.length);
            return tmp;
        }
        return null;
    }

    /**
     * @return true if the data has started loading.
     */
    public boolean started() {
        return started;
    }

    /**
     * @return true if the data has finished loading.
     */
    public boolean finished() {
        return finished;
    }

    /**
     * @return The current size of the data array.
     */
    public int length() {
        return count;
    }

    private synchronized void append(double data[], int size) {
        if (size == 0) return;
        count += size;
        if (ds != null) {
            try {
                ds.append(data, size / 2);
            } catch (Exception e) {
                printmessage("Failed to append data to DataSet!");
            }
        } else if (array != null) {
            double tmp[] = new double[array.length + size];
            System.arraycopy(array, 0, tmp, 0, size);
            System.arraycopy(data, 0, tmp, array.length, size);
            array = tmp;
        } else {
            array = new double[size];
            System.arraycopy(data, 0, array, 0, size);
        }
        return;
    }

    private void printmessage(String s) {
        if (s == null) return;
        if (applet != null) applet.showStatus(s);
        System.out.println(s);
    }
}
