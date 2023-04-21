package org.matsim.contrib.freight.carrier;

import java.util.ArrayList;
import java.util.Collection;
import org.matsim.api.core.v01.Id;

public class CarrierImpl implements Carrier {

    private Id id;

    private CarrierPlan selectedPlan;

    private Collection<CarrierPlan> plans = new ArrayList<CarrierPlan>();

    private Id depotLinkId;

    private Collection<CarrierContract> contracts = new ArrayList<CarrierContract>();

    private CarrierCapabilities carrierCapabilities;

    private Collection<CarrierContract> expiredContracts = new ArrayList<CarrierContract>();

    private Collection<CarrierContract> newContracts = new ArrayList<CarrierContract>();

    public CarrierImpl(Id id, Id depotLinkId) {
        super();
        this.id = id;
        this.depotLinkId = depotLinkId;
    }

    @Override
    public Id getId() {
        return id;
    }

    @Override
    public Id getDepotLinkId() {
        return depotLinkId;
    }

    @Override
    public Collection<CarrierPlan> getPlans() {
        return plans;
    }

    @Override
    public Collection<CarrierContract> getContracts() {
        return contracts;
    }

    @Override
    public CarrierPlan getSelectedPlan() {
        return selectedPlan;
    }

    @Override
    public void setSelectedPlan(CarrierPlan selectedPlan) {
        this.selectedPlan = selectedPlan;
    }

    @Override
    public void setCarrierCapabilities(CarrierCapabilities carrierCapabilities) {
        this.carrierCapabilities = carrierCapabilities;
    }

    @Override
    public CarrierCapabilities getCarrierCapabilities() {
        return carrierCapabilities;
    }

    @Override
    public Collection<CarrierContract> getNewContracts() {
        return newContracts;
    }

    @Override
    public Collection<CarrierContract> getExpiredContracts() {
        return expiredContracts;
    }
}
