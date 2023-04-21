package org.tripcom.dam.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.faces.model.SelectItem;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.richfaces.model.ScrollableTableDataModel.SimpleRowKey;
import org.richfaces.model.selection.SimpleSelection;
import org.tripcom.dam.backend.serviceapi.ServiceAPIProxy;
import org.tripcom.dam.frontend.Asset;
import org.tripcom.dam.frontend.AssetCollaborator;
import org.tripcom.dam.frontend.Contract;
import org.tripcom.dam.frontend.Service;
import org.tripcom.dam.frontend.ServiceEvaluation;
import org.tripcom.dam.frontend.User;
import org.tripcom.dam.utils.ontology.PropertiesReader;

public class UserController {

    private User u = new User();

    private Asset asset = new Asset();

    private ArrayList services = new ArrayList();

    private Service service = new Service();

    private ArrayList contracts = new ArrayList();

    private Contract contract = new Contract();

    private SimpleSelection selection = new SimpleSelection();

    private ServiceEvaluation se = new ServiceEvaluation();

    private AssetCollaborator[] assetsCollaborator = new AssetCollaborator[10];

    private AssetCollaborator assetCollaborator = new AssetCollaborator();

    private String assetIni = "Music";

    private Service[] servicesArray;

    private SelectItem[] assetTypes = { new SelectItem("Music"), new SelectItem("Films"), new SelectItem("Books") };

    private Category log = Logger.getLogger(this.getClass());

    ServiceAPIProxy serviceAPIProxy = null;

    public UserController(User u, ArrayList services, Service service, ArrayList contracts, Contract contract, SimpleSelection selection, ServiceEvaluation se) {
        super();
        this.u = u;
        this.services = services;
        this.service = service;
        this.contracts = contracts;
        this.contract = contract;
        this.selection = selection;
        this.se = se;
    }

    public UserController() {
        PropertiesReader ga = new PropertiesReader();
        Properties propiedades = ga.obtainConfiguration("properties/log4j");
        PropertyConfigurator.configure(propiedades);
        log = Category.getInstance("tripcom");
        serviceAPIProxy = new ServiceAPIProxy();
    }

    public SimpleSelection getSelection() {
        return selection;
    }

    public void setSelection(SimpleSelection selection) {
        System.out.println("Setting Started");
        this.selection = selection;
        System.out.println("Setting Complete");
    }

    public void takeSelectionContract() {
        Iterator<SimpleRowKey> iterator = getSelection().getKeys();
        while (iterator.hasNext()) {
            SimpleRowKey key = iterator.next();
            contract = (Contract) getContracts().get(key.intValue());
        }
    }

    public void takeSelectionService() {
        Iterator<SimpleRowKey> iterator = getSelection().getKeys();
        while (iterator.hasNext()) {
            SimpleRowKey key = iterator.next();
            service = (Service) getServicesArray()[key.intValue()];
        }
    }

    public void saveEvaluation() {
        try {
            log.info(this.getClass() + ": " + "Saving evaluation service");
            se.setUser(u.getUserName());
            serviceAPIProxy.evaluateService(se, service.getServiceName());
        } catch (Exception e) {
            log.error(this.getClass() + ": " + "Save Evaluation failed. Error: " + e.toString());
        }
    }

    public String fillAssetContent() {
        if (assetIni.equals("Music")) {
            return ("assetMusic");
        } else if (assetIni.equals("Films")) {
            return ("assetFilms");
        } else {
            return ("assetBooks");
        }
    }

    public User getU() {
        return u;
    }

    public void setU(User u) {
        this.u = u;
    }

    public ArrayList getServices() {
        return services;
    }

    public void setServices(ArrayList services) {
        this.services = services;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public User getUser() {
        return u;
    }

    public void setUser(User u) {
        this.u = u;
    }

    public String toSearchServices() {
        return "searchServices";
    }

    public String toViewServices() {
        try {
            log.info(this.getClass() + ": " + "Listing Services");
            servicesArray = null;
            servicesArray = serviceAPIProxy.getServiceUser(u.getUserName());
        } catch (Exception e) {
            log.error(this.getClass() + ": " + "List services failed. Error:" + e.toString());
        }
        return "viewServices";
    }

    public String toEvaluate() {
        takeSelectionService();
        return "evaluate";
    }

    public ArrayList getContracts() {
        return contracts;
    }

    public void setContracts(ArrayList contracts) {
        this.contracts = contracts;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public ServiceEvaluation getSe() {
        return se;
    }

    public void setSe(ServiceEvaluation se) {
        this.se = se;
    }

    public String getAssetIni() {
        return assetIni;
    }

    public void setAssetIni(String assetIni) {
        this.assetIni = assetIni;
    }

    public SelectItem[] getAssetTypes() {
        return assetTypes;
    }

    public void setAssetTypes(SelectItem[] assetTypes) {
        this.assetTypes = assetTypes;
    }

    public AssetCollaborator[] getAssetsCollaborator() {
        return assetsCollaborator;
    }

    public void setAssetsCollaborator(AssetCollaborator[] assetsCollaborator) {
        this.assetsCollaborator = assetsCollaborator;
    }

    public AssetCollaborator getAssetCollaborator() {
        return assetCollaborator;
    }

    public void setAssetCollaborator(AssetCollaborator assetCollaborator) {
        this.assetCollaborator = assetCollaborator;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String toAddActors() {
        return "addActors";
    }

    public String toAddMusicians() {
        return "addMusicians";
    }

    public String toAssetFilms() {
        return "assetFilms";
    }

    public String toAssetMusic() {
        return "assetMusic";
    }

    public String searchServices() {
        try {
            log.info(this.getClass() + ": " + "Serching services");
            servicesArray = serviceAPIProxy.searchServices(asset);
        } catch (Exception e) {
            log.error(this.getClass() + ": " + "Search services failed. Error:" + e.toString());
        }
        return "toSearchServices";
    }

    public String toBuyService() {
        try {
            log.info(this.getClass() + ": " + "Buy service start");
            takeSelectionService();
            serviceAPIProxy.subscribeToService(service.getServiceName(), u);
            log.info(this.getClass() + ": " + "Service buy");
        } catch (Exception e) {
            log.info(this.getClass() + ": " + "Buy services failed. Error:" + e.toString());
        }
        return "toViewServices";
    }

    public void toUnsubscribe() {
        try {
            log.info(this.getClass() + ": " + "Unsubscribing service");
            serviceAPIProxy.unsubscribeService(u.getUserName(), service.getServiceName());
            log.info(this.getClass() + ": " + "Service unsubscribed");
        } catch (Exception e) {
            log.error(this.getClass() + ": " + "Unsubscribed service failed. Error: " + e.toString());
        }
    }

    public Service[] getServicesArray() {
        return servicesArray;
    }

    public void setServicesArray(Service[] servicesArray) {
        this.servicesArray = servicesArray;
    }
}
