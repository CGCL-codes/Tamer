package org.j2eebuilder.services.rundata;

import org.apache.jetspeed.services.rundata.DefaultJetspeedRunData;
import org.apache.turbine.util.Log;
import org.apache.jetspeed.om.security.JetspeedUser;
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.om.profile.Profile;
import org.apache.jetspeed.capability.CapabilityMap;
import org.apache.jetspeed.capability.CapabilityMapFactory;
import org.apache.jetspeed.services.statemanager.SessionState;
import org.apache.jetspeed.services.statemanager.StateManagerService;
import org.apache.turbine.services.rundata.DefaultTurbineRunData;
import org.apache.turbine.services.TurbineServices;
import org.apache.turbine.util.security.AccessControlList;
import java.util.Stack;

/**
 * This interface extends the RunData interface with methods
 * specific to the needs of a Jetspeed like portal implementation.
 *
 * <note>Several of these properties may be put in the base RunData
 * interface in future releases of Turbine</note>
 *
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:sgala@apache.org">Santiago Gala</a>
 * @author <a href="mailto:paulsp@apache.org">Paul Spencer</a>
 * @version $Id: DefaultJ2eebuilderRunData.java 86 2003-04-21 21:24:43Z ohioedge $
 */
public class DefaultJ2eebuilderRunData extends DefaultJetspeedRunData implements J2eebuilderRunData {
}
