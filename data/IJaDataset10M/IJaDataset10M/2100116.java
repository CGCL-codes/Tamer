package remote.agent;

import hu.usz.inf.netspotter.agent.Action;
import hu.usz.inf.netspotter.agent.RunResults;
import hu.usz.inf.netspotter.agent.Schedule;
import hu.usz.inf.netspotter.agent.ScheduleType;
import hu.usz.inf.netspotter.agentlogic.FeedBack;
import hu.usz.inf.netspotter.agentlogic.Message;
import hu.usz.inf.netspotter.agentlogic.NTLV;
import java.net.InetAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import monitor.utils.AbstractIpAddress;
import net.AddressException;
import net.MACaddress;
import remote.CalendarConversions;
import remote.proxies.AgentFeedBack;
import remote.proxies.AgentMessage;
import remote.proxies.AgentNTLV;
import remote.proxies.AgentRunResults;
import remote.proxies.AgentSchedule;
import remote.proxies.IpAddressEntity;
import remote.proxies.MacAddressEntity;
import remote.proxies.StringEntity;

/**
 * @author Kasza Miklós
 */
public final class NodeConversions {

    public static remote.proxies.Action actionFromEnum(Action act) {
        switch(act) {
            case Remove:
                return remote.proxies.Action.REMOVE;
            default:
                return remote.proxies.Action.ADD;
        }
    }

    public static Action actionToEnum(remote.proxies.Action act) {
        switch(act) {
            case REMOVE:
                return Action.Remove;
            default:
                return Action.Add;
        }
    }

    public static final FeedBack agentFeedBackToFeedBack(final AgentFeedBack afb) {
        final FeedBack feedBack = new FeedBack();
        feedBack.setMessageId(afb.getMessageId());
        feedBack.setErrorCode(afb.getErrorCode());
        feedBack.setSuccess(afb.isSuccess());
        feedBack.setTestcaseid(afb.getTestCaseId());
        feedBack.setTime(afb.getTime().toGregorianCalendar().getTime());
        feedBack.setType(afb.getType());
        if (null != afb.getMessage()) {
            feedBack.m = agentMessageToMessage(afb.getMessage());
        }
        final Vector<NTLV> diffs = new Vector<NTLV>();
        if (null != afb.getDifferences()) {
            for (final AgentNTLV aNtlv : afb.getDifferences()) {
                final NTLV ntlv = new NTLV();
                ntlv.id = aNtlv.getId();
                ntlv.length = aNtlv.getLength();
                ntlv.name = aNtlv.getName();
                ntlv.response = aNtlv.getResponse();
                ntlv.type = aNtlv.getType();
                ntlv.value = aNtlv.getValue();
                diffs.add(ntlv);
            }
        }
        feedBack.differences = diffs;
        feedBack.response = new Vector<String>(stringEntityCollectionToStringList(afb.getResponse()));
        return feedBack;
    }

    public static final Message agentMessageToMessage(final AgentMessage am) {
        final Message message = new Message();
        message.data = am.getData();
        message.destAddr = am.getDestAddr();
        message.id = am.getId();
        message.ids = new Vector<String>(stringEntityCollectionToStringList(am.getIds()));
        message.length = new Vector<String>(stringEntityCollectionToStringList(am.getLength()));
        message.mask = am.getMask();
        message.messageLength = am.getMessageLength();
        message.srcAddr = am.getSrcAddr();
        message.success = am.isSuccess();
        message.type = new Vector<String>(stringEntityCollectionToStringList(am.getType()));
        message.value = new Vector<String>(stringEntityCollectionToStringList(am.getValue()));
        return message;
    }

    public static final RunResults agentRunResultsToRunResults(final AgentRunResults res) {
        final RunResults results = new RunResults();
        results.agent = res.getAgentName();
        results.testcaseid = res.getTestCaseId();
        results.time = res.getTime().toGregorianCalendar().getTime();
        final Vector<FeedBack> feedBacks = new Vector<FeedBack>();
        for (final AgentFeedBack feedBack : res.getFeedBacks()) {
            feedBacks.add(agentFeedBackToFeedBack(feedBack));
        }
        results.feedbacks = feedBacks;
        return results;
    }

    /**
	 * @param addr
	 */
    public static final IpAddressEntity createIPAddress(AbstractIpAddress addr) {
        if (null == addr) {
            return null;
        }
        final IpAddressEntity addrEntity = new IpAddressEntity();
        addrEntity.setAddressString(addr.toString());
        return addrEntity;
    }

    public static final IpAddressEntity createIPAddress(InetAddress iAddr) {
        if (null == iAddr) {
            return null;
        }
        final IpAddressEntity addrEntity = new IpAddressEntity();
        try {
            final AbstractIpAddress addr = AbstractIpAddress.createAddress(iAddr.getAddress());
            addrEntity.setAddressString(addr.toString());
            return addrEntity;
        } catch (AddressException x) {
            x.printStackTrace();
            return null;
        }
    }

    public static final AbstractIpAddress createIPAddress(IpAddressEntity addrEntity) {
        try {
            return AbstractIpAddress.createAddress(addrEntity.getAddressString());
        } catch (AddressException x) {
            x.printStackTrace();
            return null;
        }
    }

    public static final MacAddressEntity createMACAddress(MACaddress macAddr) {
        if (null == macAddr) {
            return null;
        }
        final MacAddressEntity macEntity = new MacAddressEntity();
        macEntity.setAddressString(macAddr.toString());
        return macEntity;
    }

    public static final AgentSchedule scheduleToAgentSchedule(final Schedule schedule) {
        final AgentSchedule aSchedule = new AgentSchedule();
        aSchedule.setAction(actionFromEnum(schedule.action));
        aSchedule.setSchedule(schedule.schedudle);
        aSchedule.setTask(schedule.task);
        aSchedule.setType(scheduleTypeFromEnum(schedule.type));
        aSchedule.setDate(CalendarConversions.createXMLGregorianCalendar(schedule.date));
        return aSchedule;
    }

    public static remote.proxies.ScheduleType scheduleTypeFromEnum(ScheduleType type) {
        switch(type) {
            case Cron:
                return remote.proxies.ScheduleType.CRON;
            default:
                return remote.proxies.ScheduleType.SIMPLE;
        }
    }

    public static ScheduleType scheduleTypeToEnum(remote.proxies.ScheduleType type) {
        switch(type) {
            case CRON:
                return ScheduleType.Cron;
            default:
                return ScheduleType.Simple;
        }
    }

    public static final List<String> stringEntityCollectionToStringList(Collection<StringEntity> strColl) {
        final List<String> _resultList = new LinkedList<String>();
        if (null != strColl && strColl.size() > 0) {
            for (final StringEntity strEnt : strColl) {
                _resultList.add(strEnt.getValue());
            }
        }
        return _resultList;
    }

    private NodeConversions() {
    }
}
