package net.sourceforge.ivi.waveview.ui;

import java.util.Hashtable;

public abstract class iviAbstractWaveTraceRenderer implements IWaveTraceRenderer {

    /****************************************************************
     * iviAbstractWaveTraceRenderer()
     ****************************************************************/
    public iviAbstractWaveTraceRenderer() {
        d_data = new Hashtable();
        d_configData = new Hashtable();
    }

    /****************************************************************
     * init()
     ****************************************************************/
    public void init(IWaveTraceRendererInitData init_data) {
        d_waveDisplay = init_data.getWaveDisplay();
    }

    /****************************************************************
     * getTraceLabel()
     ****************************************************************/
    public String getTraceLabel() {
        return d_fullName;
    }

    /****************************************************************
     * getNextEdge()
     ****************************************************************/
    public long getNextEdge(long currTime) {
        return currTime;
    }

    /****************************************************************
     * getPrevEdge()
     ****************************************************************/
    public long getPrevEdge(long currTime) {
        return currTime;
    }

    /****************************************************************
     * isExpandable()
     ****************************************************************/
    public boolean isExpandable() {
        return false;
    }

    /****************************************************************
     * isExpanded()
     ****************************************************************/
    public boolean isExpanded() {
        return d_isExpanded;
    }

    /****************************************************************
     * setIsExpanded()
     ****************************************************************/
    public void setIsExpanded(boolean expanded) {
        d_isExpanded = expanded;
    }

    /****************************************************************
     * getSubTraces()
     ****************************************************************/
    public IWaveTraceRenderer[] getSubTraces() {
        return new IWaveTraceRenderer[0];
    }

    /****************************************************************
     * getTraceValue()
     ****************************************************************/
    public String getTraceValue(long time) {
        return "";
    }

    /****************************************************************
     * RenderTrace()
     ****************************************************************/
    public abstract void RenderTrace(iviWaveTraceRenderData rdata);

    /****************************************************************
     * Protected Data
     ****************************************************************/
    protected IWaveDisplay d_waveDisplay;

    protected String d_fullName;

    protected boolean d_isExpanded;

    protected Hashtable d_data;

    protected Object d_renderCache;

    protected Hashtable d_configData;
}
