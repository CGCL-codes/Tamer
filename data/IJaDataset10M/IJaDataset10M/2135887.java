package com.ibm.csdl.ecm.ta.critsitEX.retriever;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.ibm.csdl.ecm.ta.critsitEX.getter.PMRGetter;
import com.ibm.csdl.ecm.ta.critsitEX.getter.SituationGetter;
import com.ibm.csdl.ecm.ta.critsitEX.pojo.Situation;
import com.meterware.httpunit.WebConversation;

/**
 * @author Liven
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SaveOnEachCritsitStrategy extends CritsitRetrieveStrategy {

    public void retrieve(Date startDate, Date endDate, String username, String password) {
        WebConversation webConv = new WebConversation();
        webConv.setAuthentication("w3", username, password);
        retrieveOneSituationPage(SituationGetter.SIT_OWNED_BY_IM_CLOSED, startDate, endDate, webConv);
        retrieveOneSituationPage(SituationGetter.SIT_OWNED_BY_IM_BACKLOG, startDate, endDate, webConv);
        retrieveOneSituationPage(SituationGetter.SIT_NO_RTL_CLOSED, startDate, endDate, webConv);
        retrieveOneSituationPage(SituationGetter.SIT_NO_RTL_BACKLOG, startDate, endDate, webConv);
    }

    protected void retrieveOneSituationPage(String pageName, Date startDate, Date endDate, WebConversation webConv) {
        SituationGetter sg = new SituationGetter(pageName, startDate, endDate, webConv);
        List sitList = new ArrayList(), pmrList = null, list = null;
        log.info("Retrieving situation page: " + pageName);
        list = sg.retrieveComplaintTable();
        if (list != null) sitList.addAll(list);
        list = sg.retrieveProactiveTable();
        if (list != null) sitList.addAll(list);
        if (sitList == null) return;
        for (Iterator iter = sitList.iterator(); iter.hasNext(); ) {
            Situation sit = (Situation) iter.next();
            pmrList = new ArrayList();
            webConv.clearContents();
            PMRGetter pg = new PMRGetter(sit.getSit_name(), webConv);
            log.info("Retrieveing PMR for Sit #" + sit.getSit_name());
            sit.setAbs_desc(pg.retrieveAbstract());
            list = pg.retrieveOpenPMRs();
            if (list != null) pmrList.addAll(list);
            list = pg.retrieveClosedPMRs();
            if (list != null) pmrList.addAll(list);
            try {
                writer.writeOneSitWithPMRs(sit, pmrList);
            } catch (Exception e) {
                log.error(e);
            }
        }
        log.info("Situation page: " + pageName + " retrieving done.");
    }
}
