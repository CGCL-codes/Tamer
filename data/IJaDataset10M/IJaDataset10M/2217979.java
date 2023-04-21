package at.rc.tacos.client.ui;

import java.util.Calendar;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.perspectives.ResourcePerspective;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.GetMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.util.MyUtils;

/**
 * This workbench advisor creates the window advisor, and specifies the
 * perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = ResourcePerspective.ID;

    /**
	 * Creates the application workbench advisor.
	 * 
	 * @param configurer
	 *            the configuring workbench information
	 * @return the configuration information for a workbench window
	 */
    @Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    /**
	 * Return the initial perspective used for new workbench windows.
	 * 
	 * @return the idenitfication of the perspective
	 */
    @Override
    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }

    /**
	 * Initializes the application and restores the previous settings
	 */
    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
        initData();
    }

    /**
	 * Helper method to request the initial data for the views
	 */
    private void initData() {
        MessageIoSession session = NetWrapper.getSession();
        String currentDate = MyUtils.timestampToString(Calendar.getInstance().getTimeInMillis(), MyUtils.timeAndDateFormat);
        GetMessage<Location> getLocation = new GetMessage<Location>(new Location());
        GetMessage<Job> getJob = new GetMessage<Job>(new Job());
        GetMessage<ServiceType> getService = new GetMessage<ServiceType>(new ServiceType());
        GetMessage<Competence> getCompetence = new GetMessage<Competence>(new Competence());
        GetMessage<MobilePhoneDetail> getPhones = new GetMessage<MobilePhoneDetail>(new MobilePhoneDetail());
        GetMessage<RosterEntry> getRoster = new GetMessage<RosterEntry>(new RosterEntry());
        GetMessage<DayInfoMessage> getInfo = new GetMessage<DayInfoMessage>(new DayInfoMessage());
        GetMessage<VehicleDetail> getVehicle = new GetMessage<VehicleDetail>(new VehicleDetail());
        GetMessage<Login> getLogin = new GetMessage<Login>(new Login());
        GetMessage<Disease> getDisease = new GetMessage<Disease>(new Disease());
        GetMessage<StaffMember> getStaff = new GetMessage<StaffMember>(new StaffMember());
        GetMessage<Transport> getTransport = new GetMessage<Transport>(new Transport());
        GetMessage<DialysisPatient> getDialysis = new GetMessage<DialysisPatient>(new DialysisPatient());
        getRoster.addParameter(IFilterTypes.DATE_FILTER, currentDate);
        getInfo.addParameter(IFilterTypes.DATE_FILTER, currentDate);
        getTransport.addParameter(IFilterTypes.DATE_FILTER, currentDate);
        getDialysis.addParameter(IFilterTypes.DATE_FILTER, currentDate);
        getLocation.asnchronRequest(session);
        getJob.asnchronRequest(session);
        getService.asnchronRequest(session);
        getCompetence.asnchronRequest(session);
        getPhones.asnchronRequest(session);
        getRoster.asnchronRequest(session);
        getInfo.asnchronRequest(session);
        getVehicle.asnchronRequest(session);
        getLogin.asnchronRequest(session);
        getDisease.asnchronRequest(session);
        getStaff.asnchronRequest(session);
        getTransport.asnchronRequest(session);
        getDialysis.asnchronRequest(session);
    }
}
