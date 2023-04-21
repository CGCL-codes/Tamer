package com.francetelecom.rd.maps.mep.mse.carteblanche.supervision.event;

import java.util.Date;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.enumeration.CarteBlanche_JOnASState;
import com.francetelecom.rd.maps.semeuse.t31d2_slachecking.serializable_objects.CarteBlanche_ServiceLevelAgreement_DTO;

public class CarteBlanche_ServiceLevelAgreement_CheckingEvent {

    private Date occuredAt;

    private String rawReport_CarteBlanche_Id;

    private Long allThreadsCount;

    private CarteBlanche_JOnASState state;

    private Long deploymentPlansCount;

    private Long sampleCluster2_Service_sessionServlet_maxTime;

    private CarteBlanche_ServiceLevelAgreement_DTO carteBlanche_ServiceLevelAgreement_DTO;

    public CarteBlanche_ServiceLevelAgreement_CheckingEvent(final Date occuredAt_, final String rawReport_CarteBlanche_Id_, final Long allThreadsCount_, final CarteBlanche_JOnASState state_, final Long deploymentPlansCount_, final Long sampleCluster2_Service_sessionServlet_maxTime_, final CarteBlanche_ServiceLevelAgreement_DTO carteBlanche_ServiceLevelAgreement_DTO_) {
        occuredAt = occuredAt_;
        rawReport_CarteBlanche_Id = rawReport_CarteBlanche_Id_;
        allThreadsCount = allThreadsCount_;
        state = state_;
        deploymentPlansCount = deploymentPlansCount_;
        sampleCluster2_Service_sessionServlet_maxTime = sampleCluster2_Service_sessionServlet_maxTime_;
        carteBlanche_ServiceLevelAgreement_DTO = carteBlanche_ServiceLevelAgreement_DTO_;
    }

    public Date getOccuredAt() {
        return occuredAt;
    }

    public String getRawReport_CarteBlanche_Id() {
        return rawReport_CarteBlanche_Id;
    }

    public Long getAllThreadsCount() {
        return allThreadsCount;
    }

    public CarteBlanche_JOnASState getState() {
        return state;
    }

    public Long getDeploymentPlansCount() {
        return deploymentPlansCount;
    }

    public Long getSampleCluster2_Service_sessionServlet_maxTime() {
        return sampleCluster2_Service_sessionServlet_maxTime;
    }

    public final CarteBlanche_ServiceLevelAgreement_DTO getCarteBlanche_ServiceLevelAgreement_DTO() {
        return carteBlanche_ServiceLevelAgreement_DTO;
    }

    @Override
    public String toString() {
        String result = this.getClass().getName() + ": [\n";
        result = result + "    occuredAt: " + getOccuredAt() + ",\n";
        result = result + "    rawReport_CarteBlanche_Id_: " + getRawReport_CarteBlanche_Id() + ",\n";
        result = result + "    allThreadsCount: " + getAllThreadsCount() + ",\n";
        result = result + "    state: " + getState() + ",\n";
        result = result + "    deploymentPlansCount: " + getDeploymentPlansCount() + ",\n";
        result = result + "    sampleCluster2_Service_sessionServlet_maxTime: " + getSampleCluster2_Service_sessionServlet_maxTime() + ",\n";
        result = result + "    serviceLevelAgreement: " + getCarteBlanche_ServiceLevelAgreement_DTO() + "]\n";
        return result;
    }
}
