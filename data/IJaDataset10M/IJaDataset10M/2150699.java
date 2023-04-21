package com.infocetera.chart;

import com.infocetera.util.*;
import java.applet.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Class ChartApplet _more_
 *
 *
 * @author IDV Development Team
 * @version $Revision: 1.3 $
 */
public class ChartApplet extends Applet {

    /** _more_          */
    public static final GuiUtils GU = null;

    /** _more_          */
    public static ChartApplet theApplet = null;

    /** _more_          */
    public ChartCanvas chartCanvas;

    /**
     * _more_
     */
    public ChartApplet() {
        theApplet = this;
        setBackground(Color.white);
    }

    /** _more_          */
    Choice editList;

    /** _more_          */
    Checkbox colOrRowRelativeCbx = null;

    /** _more_          */
    Checkbox flatCbx = null;

    /** _more_          */
    Button floatBtn;

    /** _more_          */
    Label label;

    /** _more_          */
    Button saveImageBtn;

    /** _more_          */
    Button scrLtBtn;

    /** _more_          */
    Button scrRtBtn;

    /** _more_          */
    Button scrEqBtn;

    /** _more_          */
    boolean flatDisplay = false;

    /** _more_          */
    boolean floating = false;

    /** _more_          */
    Frame floatFrame;

    /** _more_          */
    Container oldParent;

    /**
     * _more_
     *
     * @return _more_
     */
    public boolean getColOrRowRelative() {
        if (colOrRowRelativeCbx == null) {
            return true;
        }
        return colOrRowRelativeCbx.getState();
    }

    /**
     * _more_
     */
    public void saveImage() {
    }

    /**
     * _more_
     *
     * @param event _more_
     * @param obj _more_
     *
     * @return _more_
     */
    public boolean action(Event event, Object obj) {
        if (event.target == scrRtBtn) {
            chartCanvas.hTrans += 10;
            chartCanvas.repaint();
            return true;
        }
        if (event.target == saveImageBtn) {
            saveImage();
            return true;
        }
        if (event.target == scrLtBtn) {
            chartCanvas.hTrans -= 10;
            chartCanvas.repaint();
            return true;
        }
        if (event.target == scrEqBtn) {
            chartCanvas.hTrans = 0;
            chartCanvas.repaint();
            return true;
        }
        if (event.target == flatCbx) {
            flatDisplay = flatCbx.getState();
            checkFlat();
            chartCanvas.repaint();
            return true;
        }
        if (event.target == floatBtn) {
            if (floating) {
                Container contents = this;
                floatBtn.setLabel("Float");
                oldParent.add("Center", contents);
                floatFrame.dispose();
                invalidate();
                oldParent.validate();
                floating = false;
            } else {
                try {
                    Container contents = this;
                    oldParent = contents.getParent();
                    floatBtn.setLabel("Embed");
                    floatFrame = new Frame();
                    floatFrame.setLayout(new BorderLayout());
                    floatFrame.add("Center", contents);
                    floatFrame.pack();
                    floatFrame.show();
                    floating = true;
                } catch (Exception exc) {
                    System.err.println("err:" + exc);
                }
            }
            return true;
        }
        if (event.target == editList) {
            String value = editList.getSelectedItem();
            for (int idx = 0; idx < ChartCanvas.chartNames.length; idx++) {
                if (value.equals(ChartCanvas.chartNames[idx])) {
                    chartCanvas.setChartType(ChartCanvas.chartTypes[idx]);
                    break;
                }
            }
        } else {
            return super.action(event, obj);
        }
        return true;
    }

    /**
     * _more_
     */
    private void checkFlat() {
        if (flatDisplay) {
            chartCanvas.threeD1 = 0;
            chartCanvas.threeD2 = 0;
        } else {
            chartCanvas.threeD1 = ChartCanvas.BASE3D_DIMENSION;
            chartCanvas.threeD2 = ChartCanvas.BASE3D_DIMENSION;
        }
    }

    /** _more_          */
    int myPort;

    /** _more_          */
    String myHost;

    /** _more_          */
    String clickUrl;

    /**
     * _more_
     */
    public void init() {
        URL myUrl = getCodeBase();
        myHost = myUrl.getHost();
        myPort = myUrl.getPort();
        clickUrl = getParameter("clickurl");
        String param3D = getParameter("3d");
        if (param3D != null) {
            flatDisplay = !(new Boolean(param3D).booleanValue());
        }
        setLayout(new BorderLayout());
        chartCanvas = new ChartCanvas(this);
        chartCanvas.readParams();
        checkFlat();
        Component canvasPanel = chartCanvas.doMakeContents();
        add("Center", canvasPanel);
        Container top = null;
        label = new Label("                                          ");
        if (!chartCanvas.forPrint) {
            floatBtn = new Button("Float");
            floatBtn.setActionCommand("FLOAT");
            saveImageBtn = new Button("Save");
            scrLtBtn = new Button("<");
            scrRtBtn = new Button(">");
            scrEqBtn = new Button("=");
            Container scrPanel = GU.doLayout(new Component[] { scrLtBtn, scrEqBtn, scrRtBtn }, 4, GU.DS_N, GU.DS_N);
            editList = new Choice();
            for (int i = 0; i < ChartCanvas.chartNames.length; i++) {
                editList.addItem(ChartCanvas.chartNames[i]);
                if (chartCanvas.chartType == chartCanvas.chartTypes[i]) {
                    editList.select(i);
                }
            }
            GU.tmpInsets = new Insets(0, 2, 0, 2);
            top = GU.doLayout(new Component[] { floatBtn, scrPanel, label }, 3, GU.DS_NNY, GU.DS_N);
            add("North", top);
        } else {
            add("North", label);
        }
        Component parent = getParent();
        setBackground(Color.white);
        parent.setBackground(Color.white);
    }

    /**
     * _more_
     *
     * @param l1 _more_
     */
    public static void errorMsg(String l1) {
        System.err.println(l1);
    }

    /**
     * _more_
     *
     * @return _more_
     */
    public String getUrlPrefix() {
        if (myPort < 0) {
            return "http://" + myHost;
        }
        return "http://" + myHost + ":" + myPort;
    }

    /**
     * _more_
     *
     * @param url _more_
     *
     * @return _more_
     */
    public String getFullUrl(String url) {
        return getUrlPrefix() + url;
    }

    /**
     * _more_
     *
     * @param urlString _more_
     * @param which _more_
     */
    void showUrl(String urlString, String which) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (Throwable exc1) {
            try {
                url = new URL(getFullUrl(urlString));
            } catch (Throwable exc2) {
            }
        }
        if (url != null) {
            if ((which != null) && !which.equals("")) {
                getAppletContext().showDocument(url, which);
            } else {
                getAppletContext().showDocument(url);
            }
        }
    }
}
