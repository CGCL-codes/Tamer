package remote.clientadapters;

import hu.usz.inf.netspotter.agentlogic.FeedBack;
import hu.usz.inf.netspotter.agentlogic.NTLV;
import java.util.LinkedList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import remote.CalendarConversions;
import remote.proxies.AgentFeedBack;
import remote.proxies.AgentNTLV;
import remote.proxies.StringEntity;

/**
 * @author Kasza Miklós
 */
public class AgentFeedBackAdapter extends AgentFeedBack {

    /**
	 * @param feedback
	 */
    public AgentFeedBackAdapter(FeedBack feedback) {
        setErrorCode(feedback.getErrorCode());
        setMessageId(feedback.getMessageId());
        setSuccess(feedback.isSuccess());
        setTestCaseId(feedback.getTestcaseid());
        setType(feedback.getType());
        XMLGregorianCalendar ts = CalendarConversions.createXMLGregorianCalendar(feedback.getTime());
        setTimestamp(ts);
        List<StringEntity> response = new LinkedList<StringEntity>();
        for (String s : feedback.response) {
            StringEntity sEnt = new StringEntity();
            sEnt.setValue(s);
            response.add(sEnt);
        }
        getResponse().addAll(response);
        List<AgentNTLV> ntlvs = new LinkedList<AgentNTLV>();
        for (NTLV ntlv : feedback.differences) {
            ntlvs.add(new AgentNTLVAdapter(ntlv));
        }
        getDifferences().addAll(ntlvs);
        setMessage(new AgentMessageAdapter(feedback.m));
    }
}
