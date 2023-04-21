package org.psepr.services.service;

import java.util.Iterator;
import org.psepr.services.ServiceStatus;

/**
 * <p>
 * Description of a service. From PsEPR's point of view, a service is something
 * that receives and send events. Thus a service description is the name of the service
 * (use something globally unique like 'org.example.services.myApp' rather than 'myApp',
 * some text describing the service and then the channels it sends and receives on and
 * the types of events that are sent and received.
 * </p>
 * <p>
 * An example from com.intel.ssm.toRepos:
 * <pre>
 *    	Global.ss = new ServiceStatus(Global.AppNameFull);
 *   	Global.ss.setSendService(serviceName);
 *   	Global.ss.setSendServicePassword(servicePassword);
 *   	Global.ss.setServiceDescription(new ServiceDescription(
 *   			Global.AppNameFull,							// qualified name of application
 *   			"Receive service events and store them into the repository",	// what the application does
 *   			new ChannelUseDescriptionCollection(		// which channels are used
 *   					new ChannelUseDescription(
 *	 							"/com/intel/ssm/service/parameters/",	// channel listened to
 *   							"listen for application parameters",	// what we're listening for
 *   							0,
 *   							0,
 *   							new EventDescriptionCollection(			// types of events received
 *   									new EventDescription( new DataSetAppParam().getNamespace(), null)
 *							        ),
 *								null		// no events sent
 *						),
 *						new ChannelUseDescription(
 *								"/com/intel/ssm/service/status/",
 *								"listen for parameters from the application instances",
 *								0,
 *								0,
 *								new EventDescriptionCollection(
 *										new EventDescription( new DataSetApp().getNamespace(), null )
 *									),
 *								null		// no events sent
 *					)
 *				)
 *			)
 *		);
 * </pre>
 * </p>
 */
public class ServiceDescription extends ServiceDescriptionObject {

    public static final String SERVICE_NAMESPACE = "http://psepr.org/schema/services/description-1.0";

    private String serviceName = null;

    private String serviceDesc = null;

    private ChannelUseDescriptionCollection channelUses = new ChannelUseDescriptionCollection();

    public ServiceDescription() {
        super();
    }

    public ServiceDescription(String name, String desc, ChannelUseDescriptionCollection cud) {
        super();
        this.setServiceName(name);
        this.setServiceDesc(desc);
        this.setChannelUses(cud);
    }

    public void addChannelUse(ChannelUseDescription cud) {
        channelUses.add(cud);
    }

    public ChannelUseDescriptionCollection getChannelUses() {
        return channelUses;
    }

    public void setChannelUses(ChannelUseDescriptionCollection channelUses) {
        this.channelUses = channelUses;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public void setServiceDesc(String serviceDesc) {
        this.serviceDesc = serviceDesc;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String serviceDescriptionXML() {
        StringBuffer buff = new StringBuffer();
        try {
            buff.append("<sd:serviceDescription xmlns:sd='" + SERVICE_NAMESPACE + "'>\n");
            outThing(buff, "sd:", "name", serviceName);
            outThing(buff, "sd:", "description", serviceDesc);
            buff.append("<sd:channelUseDescriptions>\n");
            for (Iterator<ChannelUseDescription> ii = channelUses.iterator(); ii.hasNext(); ) {
                try {
                    buff.append(ii.next().toXML("sd"));
                } catch (Exception e) {
                    buff.append("<EXCEPTION>");
                    buff.append("in ChannelUseDescription.toXML:");
                    buff.append(ServiceStatus.escapeXMLSpecial(e.toString()));
                    buff.append("</EXCEPTION>");
                }
            }
            buff.append("</sd:channelUseDescriptions>\n");
        } catch (Exception e) {
            try {
                buff.append("<EXCEPTION>");
                buff.append("in ChannelUseDescription.serviceDescriptionXML:");
                buff.append(ServiceStatus.escapeXMLSpecial(e.toString()));
                buff.append("</EXCEPTION>");
            } catch (Exception f) {
            }
        } finally {
            try {
                buff.append("</sd:serviceDescription>\n");
            } catch (Exception e) {
            }
        }
        return buff.toString();
    }

    private void outThing(StringBuffer buff, String pre, String element, String value) {
        if (value != null) {
            buff.append("<");
            buff.append(pre);
            buff.append(element);
            buff.append(">");
            buff.append(ServiceStatus.escapeXMLSpecial(value));
            buff.append("</");
            buff.append(pre);
            buff.append(element);
            buff.append(">\n");
        }
    }
}
