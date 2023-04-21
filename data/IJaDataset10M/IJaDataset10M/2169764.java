package org.mobicents.tools.twiddle;

/**
 * Small utils class.
 * @author baranowb
 *
 */
public final class Utils {

    private Utils() {
    }

    public static final String SLEE_ALARM = javax.slee.management.AlarmMBean.OBJECT_NAME;

    public static final String SLEE_DEPLOYMENT = javax.slee.management.DeploymentMBean.OBJECT_NAME;

    public static final String SLEE_PROFILE_PROVISIONING = javax.slee.management.ProfileProvisioningMBean.OBJECT_NAME;

    public static final String SLEE_RESOURCE_MANAGEMENT = javax.slee.management.ResourceManagementMBean.OBJECT_NAME;

    public static final String SLEE_SERVICE_MANAGEMENT = javax.slee.management.ServiceManagementMBean.OBJECT_NAME;

    public static final String SLEE_MANAGEMENT = javax.slee.management.SleeManagementMBean.OBJECT_NAME;

    public static final String SLEE_TRACE = javax.slee.management.TraceMBean.OBJECT_NAME;

    public static final String MC_ACTIVITY_MANAGEMENT = "org.mobicents.slee:name=ActivityManagementMBean";

    public static final String MC_CONGESTION_CONTROL = "org.mobicents.slee:name=CongestionControlConfiguration";

    public static final String MC_DEPLOYER = "org.mobicents.slee:name=DeployerMBean";

    public static final String MC_EVENT_ROUTER = "org.mobicents.slee:name=EventRouterConfiguration";

    public static final String MC_EVENT_ROUTER_STATS = "org.mobicents.slee:name=EventRouterStatistics";

    public static final String MC_LOG_MANAGEMENT = "org.mobicents.slee:name=LogManagementMBean";

    public static final String MC_POLICY_MANAGEMENT = "org.mobicents.slee:name=PolicyManagementMBean";

    public static final String MC_SBB_ENTITIES = "org.mobicents.slee:name=SbbEntitiesMBean";

    public static final String MC_TIMER_FACILITY = "org.mobicents.slee:name=TimerFacilityConfiguration";

    public static final String MC_MANAGEMENT = "org.mobicents.slee:service=MobicentsManagement";

    public static final String MC_PROFILE_OBJECT_POOL = "org.mobicents.slee:service=ProfileObjectPoolManagement";

    public static final String MC_SBB_OBJECT_POOL = "org.mobicents.slee:service=SbbObjectPoolManagement";
}
