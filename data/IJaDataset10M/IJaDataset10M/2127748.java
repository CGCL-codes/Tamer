package jade.imtp.leap;

import jade.core.BackEnd;
import jade.core.FrontEnd;
import jade.core.IMTPException;
import jade.core.MicroRuntime;
import jade.core.NotFoundException;
import jade.core.ServiceException;
import jade.core.Specifier;
import jade.imtp.leap.JICP.JICPProtocol;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.security.JADESecurityException;
import java.util.Vector;

/**
 * Class declaration
 * @author Giovanni Caire - TILAB
 */
public class BackEndStub extends MicroStub implements BackEnd {

    static final int BORN_AGENT = 20;

    static final int DEAD_AGENT = 21;

    static final int SUSPENDED_AGENT = 22;

    static final int RESUMED_AGENT = 23;

    static final int MESSAGE_OUT = 24;

    static final int SERVICE_INVOKATION = 25;

    public BackEndStub(Dispatcher d) {
        super(d);
    }

    /**
	 */
    public String bornAgent(String name) throws JADESecurityException, IMTPException {
        Command c = new Command(BORN_AGENT);
        c.addParam(name);
        Command r = executeRemotely(c, 0);
        if (r.getCode() == Command.ERROR) {
            throw new JADESecurityException((String) r.getParamAt(2));
        }
        if (r.getParamCnt() > 0) {
            return (String) r.getParamAt(0);
        } else {
            return null;
        }
    }

    /**
	 */
    public void deadAgent(String name) throws IMTPException {
        Command c = new Command(DEAD_AGENT);
        c.addParam(name);
        executeRemotely(c, -1);
    }

    /**
	 */
    public void suspendedAgent(String name) throws NotFoundException, IMTPException {
        Command c = new Command(SUSPENDED_AGENT);
        c.addParam(name);
        Command r = executeRemotely(c, -1);
        if (r != null && r.getCode() == Command.ERROR) {
            throw new NotFoundException((String) r.getParamAt(2));
        }
    }

    /**
	 */
    public void resumedAgent(String name) throws NotFoundException, IMTPException {
        Command c = new Command(RESUMED_AGENT);
        c.addParam(name);
        Command r = executeRemotely(c, -1);
        if (r != null && r.getCode() == Command.ERROR) {
            throw new NotFoundException((String) r.getParamAt(2));
        }
    }

    /**
	 */
    public void messageOut(ACLMessage msg, String sender) throws NotFoundException, IMTPException {
        Command c = new Command(MESSAGE_OUT);
        c.addParam(msg);
        c.addParam(sender);
        Command r = executeRemotely(c, -1);
        if (r != null && r.getCode() == Command.ERROR) {
            throw new NotFoundException((String) r.getParamAt(2));
        }
    }

    /**
	 */
    public Object serviceInvokation(String actor, String serviceName, String methodName, Object[] methodParams) throws NotFoundException, ServiceException, IMTPException {
        Command c = new Command(SERVICE_INVOKATION);
        c.addParam(actor);
        c.addParam(serviceName);
        c.addParam(methodName);
        if (methodParams != null) {
            for (int i = 0; i < methodParams.length; ++i) {
                c.addParam(methodParams[i]);
            }
        }
        Command r = executeRemotely(c, 0);
        if (r != null && r.getCode() == Command.ERROR) {
            if (((String) r.getParamAt(1)).equals("jade.core.NotFoundException")) {
                throw new NotFoundException((String) r.getParamAt(2));
            }
            if (((String) r.getParamAt(1)).equals("jade.core.ServiceException")) {
                throw new ServiceException((String) r.getParamAt(2));
            }
        }
        if (r.getParamCnt() > 0) {
            return r.getParamAt(0);
        } else {
            return null;
        }
    }

    public static final void parseCreateMediatorResponse(String responseMessage, Properties pp) {
        Vector v = Specifier.parseList(responseMessage, '#');
        for (int i = 0; i < v.size(); ++i) {
            String s = (String) v.elementAt(i);
            if (s.length() > 0) {
                try {
                    int index = s.indexOf('=');
                    String key = s.substring(0, index);
                    String value = s.substring(index + 1);
                    pp.setProperty(key, value);
                } catch (Exception e) {
                    Logger.println("Property format error: " + s);
                    e.printStackTrace();
                }
                String mediatorId = pp.getProperty(JICPProtocol.MEDIATOR_ID_KEY);
                if (mediatorId != null) {
                    pp.setProperty(MicroRuntime.CONTAINER_NAME_KEY, mediatorId);
                }
            }
        }
    }

    /**
	 * The method encodes the create mediator request, setting all the common properties 
	 * retrived in the passed property parameter.
	 * @param pp 
	 * @return a StringBuffer to allow the dispatcher to add dispatcher specific properties.
	 */
    public static final StringBuffer encodeCreateMediatorRequest(Properties pp) {
        StringBuffer sb = new StringBuffer();
        appendProp(sb, JICPProtocol.MEDIATOR_CLASS_KEY, pp.getProperty(JICPProtocol.MEDIATOR_CLASS_KEY));
        appendProp(sb, JICPProtocol.MAX_DISCONNECTION_TIME_KEY, pp.getProperty(JICPProtocol.MAX_DISCONNECTION_TIME_KEY));
        appendProp(sb, FrontEnd.REMOTE_BACK_END_ADDRESSES, pp.getProperty(FrontEnd.REMOTE_BACK_END_ADDRESSES));
        appendProp(sb, MicroRuntime.OWNER_KEY, pp.getProperty(JICPProtocol.OWNER_KEY));
        appendProp(sb, MicroRuntime.AGENTS_KEY, pp.getProperty(MicroRuntime.AGENTS_KEY));
        appendProp(sb, MicroRuntime.BE_REQUIRED_SERVICES_KEY, pp.getProperty(MicroRuntime.BE_REQUIRED_SERVICES_KEY));
        appendProp(sb, JICPProtocol.KEEP_ALIVE_TIME_KEY, pp.getProperty(JICPProtocol.KEEP_ALIVE_TIME_KEY));
        appendProp(sb, MicroRuntime.PLATFORM_KEY, pp.getProperty(MicroRuntime.PLATFORM_KEY));
        appendProp(sb, JICPProtocol.MSISDN_KEY, pp.getProperty(JICPProtocol.MSISDN_KEY));
        appendProp(sb, JICPProtocol.VERSION_KEY, pp.getProperty(JICPProtocol.VERSION_KEY));
        return sb;
    }

    public static void appendProp(StringBuffer sb, String key, String val) {
        if ((val != null) && (val.length() != 0)) {
            sb.append(key);
            sb.append('=');
            sb.append(val);
            sb.append('#');
        }
    }
}
