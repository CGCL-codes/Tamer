package org.slasoi.swslam.poc.impl;

import org.apache.log4j.Logger;
import org.slasoi.gslam.core.context.SLAMContextAware;
import org.slasoi.gslam.core.context.SLAManagerContext;
import org.slasoi.gslam.core.context.SLAManagerContext.SLAManagerContextException;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment.Plan;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment.PlanFormatException;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment.PlanFoundException;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment.Status;
import org.slasoi.gslam.core.poc.PlanningOptimization;
import org.slasoi.slamodel.sla.SLA;
import org.slasoi.slamodel.vocab.sla;
import org.slasoi.softwareservicemanager.IProvisioning;
import org.slasoi.softwareservicemanager.impl.BookingManager;
import org.slasoi.softwareservicemanager.impl.SoftwareServiceManagerFacade;
import org.slasoi.softwareservicemanager.provisioning.ProvisionServiceStub;
import org.slasoi.swslam.poc.planhandler.PlanHandlerImpl;

public class PlanningOptimizationImpl implements PlanningOptimization, SLAMContextAware {

    protected SLAManagerContext context;

    protected POCIAssessmentAndCustomize iAssessmentAndCustomizeImpl;

    protected POCINotification pocPOCINotification;

    protected POCIPlanStatus pocIPlanStatus;

    protected IReplan iReplan;

    private String landscapePath = System.getenv("SLASOI_ORC_HOME") + System.getProperty("file.separator") + "SW-landscape" + System.getProperty("file.separator") + "ORCLandscape.scm";

    private IProvisioning provisioningManager = new ProvisionServiceStub();

    private BookingManager bookingManager = new BookingManager();

    private SoftwareServiceManagerFacade softwareServiceManagerFacade;

    private static final Logger LOGGER = Logger.getLogger(PlanningOptimizationImpl.class);

    public PlanningOptimizationImpl() {
        iAssessmentAndCustomizeImpl = new POCIAssessmentAndCustomize();
        pocPOCINotification = new POCINotification();
        setSoftwareServiceManager();
        iAssessmentAndCustomizeImpl.setSoftwareServiceManagerFacade(getSoftwareServiceManager());
        pocPOCINotification.setSoftwareServiceManagerFacade(getSoftwareServiceManager());
    }

    public IAssessmentAndCustomize getIAssessmentAndCustomize() {
        return iAssessmentAndCustomizeImpl;
    }

    public INotification getINotification() {
        return pocPOCINotification;
    }

    public IPlanStatus getIPlanStatus() {
        return pocIPlanStatus;
    }

    public IReplan getIReplan() {
        return iReplan;
    }

    public void setSLAManagerContext(SLAManagerContext context) {
        this.context = context;
        iAssessmentAndCustomizeImpl.setContext(context);
    }

    private void setSoftwareServiceManager() {
        LOGGER.info("*** setSoftwareServiceManager() ");
        if (this.softwareServiceManagerFacade == null) {
            this.softwareServiceManagerFacade = SoftwareServiceManagerFacade.createSoftwareServiceManager(landscapePath, provisioningManager);
            if (this.softwareServiceManagerFacade.getBookingManager() == null) {
                this.softwareServiceManagerFacade.setBookingManager(this.bookingManager);
            }
        }
        assert (this.softwareServiceManagerFacade != null) : "SoftwareServiceManager should not be null";
    }

    protected SoftwareServiceManagerFacade getSoftwareServiceManager() {
        return this.softwareServiceManagerFacade;
    }

    class POCINotification implements INotification {

        private SoftwareServiceManagerFacade softwareServiceManagerFacade;

        public void activate(SLA newSLA) {
            if (newSLA.getPropertyValue(sla.service_id) != null) {
                try {
                    PlanHandlerImpl planHandler = new PlanHandlerImpl(newSLA);
                    planHandler.setSoftwareServiceManagerFacade(this.softwareServiceManagerFacade);
                    Plan plan = planHandler.planMaker();
                    if (plan != null) {
                        ProvisioningAdjustment pac = context.getProvisioningAdjustment();
                        pac.executePlan(plan);
                    } else {
                        LOGGER.error("Plan can not be executed due to the shortage of resources.");
                    }
                } catch (SLAManagerContextException e) {
                    e.printStackTrace();
                } catch (PlanFoundException e) {
                    e.printStackTrace();
                } catch (PlanFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        protected void setSoftwareServiceManagerFacade(SoftwareServiceManagerFacade softwareServiceManagerFacade) {
            this.softwareServiceManagerFacade = softwareServiceManagerFacade;
        }
    }

    /**
     * The <code>POCIPlanStatus</code> class represents the operations related to plan status during SLA provisioning
     * phase.
     * 
     * @author Kuan Lu
     * @version 1.0-SNAPSHOT
     */
    class POCIPlanStatus implements IPlanStatus {

        /**
         * Accepts the current status of a plan given its ID
         * 
         * @param planId
         *            the ID of a specific plan.
         * @param status
         *            the status of a specific plan.
         */
        public void planStatus(String planId, Status status) {
            assert (planId != null && !planId.equals("") && status != null) : "it requires a planId != null or not empty and plan status not null.";
        }
    }

    /**
     * The <code>POCIReplan</code> class represents the re-plan operations during SLA provisioning phase.
     * 
     * @author Kuan Lu
     * @version 1.0-SNAPSHOT
     */
    class POCIReplan implements IReplan {

        /**
         * Accepts the analysis of a specific plan
         * 
         * @param planId
         *            the ID of a specific plan.
         * @param analysis
         *            the reason for re-plan.
         */
        public void rePlan(String planId, String analysis) {
            assert (planId != null && !planId.equals("") && analysis != null && !analysis.equals("")) : "it requires a planId != null or not empty and analysis not null or not empty.";
        }
    }
}
