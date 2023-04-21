package gov.sns.apps.orbitdisplay;

import gov.sns.ca.ChannelRecord;
import gov.sns.ca.ConnectionChecker;
import gov.sns.ca.correlator.ChannelCorrelator;
import gov.sns.ca.Channel;
import gov.sns.tools.correlator.Correlation;
import gov.sns.tools.correlator.CorrelationFilter;
import gov.sns.tools.correlator.CorrelationNotice;
import gov.sns.xal.smf.impl.BPM;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Helper class which provides the cooreletion between BPMs from the selected sequence.
 *
 * @author <a href="mailto:anze.zupanc@cosylab.com">Anze Zupanc</a> 
 */
public class OrbitDisplayChannelCorrelator extends ChannelCorrelator {

    private BeamAnalyzerBean beamAnalyzerBean = null;

    private CorrelationNoticeListner listner = null;

    private ArrayList goodBPMs;

    private ArrayList badBPMs;

    private boolean noBadBPMs = true;

    private class CorrelationNoticeListner implements CorrelationNotice {

        HashMap xValues = new HashMap();

        HashMap yValues = new HashMap();

        /**
		 * DOCUMENT ME!
		 *
		 * @param sender DOCUMENT ME!
		 * @param correlation DOCUMENT ME!
		 */
        public void newCorrelation(Object sender, Correlation correlation) {
            java.util.Iterator itNames = correlation.names().iterator();
            while (itNames.hasNext()) {
                String name = (String) itNames.next();
                ChannelRecord result = (ChannelRecord) correlation.getRecord(name);
                if (name.endsWith("xAvg")) {
                    name = name.replaceAll(":xAvg", "");
                    xValues.put(name, new Double(result.doubleValue()));
                } else if (name.endsWith("yAvg")) {
                    name = name.replaceAll(":yAvg", "");
                    yValues.put(name, new Double(result.doubleValue()));
                }
            }
            getBeamAnalyzerBean().updateBeam(xValues, yValues);
        }

        /**
		 * DOCUMENT ME!
		 *
		 * @param sender DOCUMENT ME!
		 */
        public void noCorrelationCaught(Object sender) {
        }

        public void clear() {
            if (!xValues.isEmpty()) xValues.clear();
            if (!yValues.isEmpty()) yValues.clear();
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    public CorrelationNotice getCorrelationListner() {
        if (listner == null) {
            listner = new CorrelationNoticeListner();
        }
        listner.clear();
        return listner;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param aBinTimespan
	 */
    public OrbitDisplayChannelCorrelator(double aBinTimespan) {
        super(aBinTimespan);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param aBinTimespan
	 * @param aFilter
	 */
    public OrbitDisplayChannelCorrelator(double aBinTimespan, CorrelationFilter aFilter) {
        super(aBinTimespan, aFilter);
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param bpmList DOCUMENT ME!
	 */
    public synchronized void addBPMs(java.util.List bpmList) {
        if (goodBPMs != null) goodBPMs.clear(); else goodBPMs = new ArrayList();
        if (badBPMs != null) badBPMs.clear(); else badBPMs = new ArrayList();
        ArrayList pvs = new ArrayList(2 * bpmList.size());
        Iterator it = bpmList.iterator();
        while (it.hasNext()) {
            BPM bpm = (BPM) it.next();
            String xAvg = bpm.getId() + ":" + BPM.X_AVG_HANDLE;
            String yAvg = bpm.getId() + ":" + BPM.Y_AVG_HANDLE;
            pvs.add(xAvg);
            pvs.add(yAvg);
            bpm.getChannel(BPM.X_AVG_HANDLE).connect_async();
            bpm.getChannel(BPM.Y_AVG_HANDLE).connect_async();
        }
        ConnectionChecker cc = new ConnectionChecker(pvs);
        cc.checkThem();
        if (!cc.getGoodPVs().isEmpty()) goodBPMs.addAll(cc.getGoodPVs());
        if (!cc.getBadPVs().isEmpty()) {
            badBPMs.addAll(cc.getBadPVs());
            noBadBPMs = false;
        } else noBadBPMs = true;
        Iterator itGood = goodBPMs.iterator();
        while (itGood.hasNext()) {
            String pv = (String) itGood.next();
            addChannel(pv);
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @return
	 */
    public BeamAnalyzerBean getBeamAnalyzerBean() {
        return beamAnalyzerBean;
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param bean
	 */
    public void setBeamAnalyzerBean(BeamAnalyzerBean bean) {
        beamAnalyzerBean = bean;
    }

    /**
	 * DOCUMENT ME!
	 * @return
	 */
    public ArrayList getBadBPMs() {
        return badBPMs;
    }

    /**
	 * DOCUMENT ME!
	 * @return
	 */
    public boolean isNoBadBPMs() {
        return noBadBPMs;
    }

    /**
	 * DOCUMENT ME!
	 * @return
	 */
    public ArrayList getGoodBPMs() {
        return goodBPMs;
    }
}
