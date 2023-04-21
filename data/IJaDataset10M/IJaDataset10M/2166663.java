package com.codemonster.surinam.controller;

import com.codemonster.surinam.core.framework.ServiceToFieldInjectionPair;
import com.codemonster.surinam.core.model.ContractBlueprint;
import com.codemonster.surinam.export.framework.ServiceBlock;
import com.codemonster.surinam.export.framework.ServiceBlockAdmin;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is an Contract Action object which registers a Service Contract.
 */
public class RegisterContractAction implements Action {

    private static final String actionName = RegisterContractAction.class.getSimpleName();

    private List<ServiceToFieldInjectionPair> dependencyList = new ArrayList<ServiceToFieldInjectionPair>();

    /**
     * The blueprint object.
     */
    private ContractBlueprint contractBlueprint = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return actionName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<ServiceToFieldInjectionPair> getRequiredServices() {
        return dependencyList.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(ServiceBlock serviceBlock) throws MalformedURLException, ClassNotFoundException {
        ((ServiceBlockAdmin) serviceBlock).registerContract(contractBlueprint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void injectServices(ServiceBlock serviceBlock) {
    }

    public ContractBlueprint getContractBlueprint() {
        return contractBlueprint;
    }

    public void setContractBlueprint(ContractBlueprint contractBlueprint) {
        this.contractBlueprint = contractBlueprint;
    }
}
