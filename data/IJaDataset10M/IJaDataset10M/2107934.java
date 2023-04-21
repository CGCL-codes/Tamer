package org.blueoxygen.obat.service.actions;

import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.persistence.PersistenceManager;
import org.blueoxygen.obat.BusinessPartner;
import org.blueoxygen.obat.Service;
import org.blueoxygen.obat.ServiceResponse;
import org.blueoxygen.obat.topic.Topic;
import com.opensymphony.xwork.ActionSupport;

/**
 * @author alex
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServiceForm extends ActionSupport implements PersistenceAware {

    protected PersistenceManager pm;

    protected Service service = new Service();

    protected ServiceResponse response = new ServiceResponse();

    protected Topic topic = new Topic();

    protected List topics = new ArrayList();

    protected List contact = new ArrayList();

    protected LogInformation logInfo;

    private List services;

    private String name = "";

    private String brand = "";

    private String phone = "";

    private String product = "";

    private String model = "";

    private String serialNumber = "";

    private String guaranted = "";

    private String dateOfPurchase = "";

    private String technision = "";

    private String dateStart = "";

    private String dateFinish = "";

    private String status = "";

    private String address = "";

    private String guarantedCard = "0";

    private String ram = "0";

    private String hdd = "0";

    private String monitor = "0";

    private String cablePower = "0";

    private String adaptor = "0";

    private String cableAdaptor = "0";

    private String mouse = "0";

    private String cdrom = "0";

    private String cableCdrom = "0";

    private String dvdrom = "0";

    private String cableDvdrom = "0";

    private String diskette = "0";

    private String battery = "0";

    private String modem = "0";

    private String cableModem = "0";

    private String pcmcia = "0";

    private String cablePcmcia = "0";

    private String recoveryCd = "0";

    private String otherCd = "0";

    private String floppyDiskDrive = "0";

    private String bag = "0";

    private String mousePad = "0";

    private String videoCamera = "0";

    private String scanner = "0";

    private String adaptorScanner = "0";

    private String serviceStatus = "0";

    private String id = "";

    private String description = "";

    private String topicId = "";

    private String contactId = "";

    /**
	 * @return Returns the contact.
	 */
    public List getContact() {
        return contact;
    }

    /**
	 * @param contact The contact to set.
	 */
    public void setContact(List contact) {
        this.contact = contact;
    }

    /**
	 * @return Returns the contactId.
	 */
    public String getContactId() {
        return contactId;
    }

    /**
	 * @param contactId The contactId to set.
	 */
    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    /**
	 * @return Returns the serviceStatus.
	 */
    public String getServiceStatus() {
        return serviceStatus;
    }

    /**
	 * @param serviceStatus The serviceStatus to set.
	 */
    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String execute() {
        topics = pm.findAllSorted(Topic.class, "name");
        return SUCCESS;
    }

    /**
	 * @return Returns the topicId.
	 */
    public String getTopicId() {
        return topicId;
    }

    /**
	 * @param topicId The topicId to set.
	 */
    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.pm = persistenceManager;
    }

    /**
	 * @return Returns the description.
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description The description to set.
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return Returns the adaptor.
	 */
    public String getAdaptor() {
        return adaptor;
    }

    /**
	 * @param adaptor The adaptor to set.
	 */
    public void setAdaptor(String adaptor) {
        this.adaptor = adaptor;
    }

    /**
	 * @return Returns the adaptorScanner.
	 */
    public String getAdaptorScanner() {
        return adaptorScanner;
    }

    /**
	 * @param adaptorScanner The adaptorScanner to set.
	 */
    public void setAdaptorScanner(String adaptorScanner) {
        this.adaptorScanner = adaptorScanner;
    }

    /**
	 * @return Returns the bag.
	 */
    public String getBag() {
        return bag;
    }

    /**
	 * @param bag The bag to set.
	 */
    public void setBag(String bag) {
        this.bag = bag;
    }

    /**
	 * @return Returns the battery.
	 */
    public String getBattery() {
        return battery;
    }

    /**
	 * @param battery The battery to set.
	 */
    public void setBattery(String battery) {
        this.battery = battery;
    }

    /**
	 * @return Returns the cableAdaptor.
	 */
    public String getCableAdaptor() {
        return cableAdaptor;
    }

    /**
	 * @param cableAdaptor The cableAdaptor to set.
	 */
    public void setCableAdaptor(String cableAdaptor) {
        this.cableAdaptor = cableAdaptor;
    }

    /**
	 * @return Returns the cableCdrom.
	 */
    public String getCableCdrom() {
        return cableCdrom;
    }

    /**
	 * @param cableCdrom The cableCdrom to set.
	 */
    public void setCableCdrom(String cableCdrom) {
        this.cableCdrom = cableCdrom;
    }

    /**
	 * @return Returns the cableDvdrom.
	 */
    public String getCableDvdrom() {
        return cableDvdrom;
    }

    /**
	 * @param cableDvdrom The cableDvdrom to set.
	 */
    public void setCableDvdrom(String cableDvdrom) {
        this.cableDvdrom = cableDvdrom;
    }

    /**
	 * @return Returns the cableModem.
	 */
    public String getCableModem() {
        return cableModem;
    }

    /**
	 * @param cableModem The cableModem to set.
	 */
    public void setCableModem(String cableModem) {
        this.cableModem = cableModem;
    }

    /**
	 * @return Returns the cablePcmcia.
	 */
    public String getCablePcmcia() {
        return cablePcmcia;
    }

    /**
	 * @param cablePcmcia The cablePcmcia to set.
	 */
    public void setCablePcmcia(String cablePcmcia) {
        this.cablePcmcia = cablePcmcia;
    }

    /**
	 * @return Returns the cablePower.
	 */
    public String getCablePower() {
        return cablePower;
    }

    /**
	 * @param cablePower The cablePower to set.
	 */
    public void setCablePower(String cablePower) {
        this.cablePower = cablePower;
    }

    /**
	 * @return Returns the cdrom.
	 */
    public String getCdrom() {
        return cdrom;
    }

    /**
	 * @param cdrom The cdrom to set.
	 */
    public void setCdrom(String cdrom) {
        this.cdrom = cdrom;
    }

    /**
	 * @return Returns the diskette.
	 */
    public String getDiskette() {
        return diskette;
    }

    /**
	 * @param diskette The diskette to set.
	 */
    public void setDiskette(String diskette) {
        this.diskette = diskette;
    }

    /**
	 * @return Returns the dvdrom.
	 */
    public String getDvdrom() {
        return dvdrom;
    }

    /**
	 * @param dvdrom The dvdrom to set.
	 */
    public void setDvdrom(String dvdrom) {
        this.dvdrom = dvdrom;
    }

    /**
	 * @return Returns the floppyDiskDrive.
	 */
    public String getFloppyDiskDrive() {
        return floppyDiskDrive;
    }

    /**
	 * @param floppyDiskDrive The floopyDiskDrive to set.
	 */
    public void setFloppyDiskDrive(String floppyDiskDrive) {
        this.floppyDiskDrive = floppyDiskDrive;
    }

    /**
	 * @return Returns the guarantedCard.
	 */
    public String getGuarantedCard() {
        return guarantedCard;
    }

    /**
	 * @param guarantedCard The guarantedCard to set.
	 */
    public void setGuarantedCard(String guarantedCard) {
        this.guarantedCard = guarantedCard;
    }

    /**
	 * @return Returns the hdd.
	 */
    public String getHdd() {
        return hdd;
    }

    /**
	 * @param hdd The hdd to set.
	 */
    public void setHdd(String hdd) {
        this.hdd = hdd;
    }

    /**
	 * @return Returns the modem.
	 */
    public String getModem() {
        return modem;
    }

    /**
	 * @param modem The modem to set.
	 */
    public void setModem(String modem) {
        this.modem = modem;
    }

    /**
	 * @return Returns the monitor.
	 */
    public String getMonitor() {
        return monitor;
    }

    /**
	 * @param monitor The monitor to set.
	 */
    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    /**
	 * @return Returns the mouse.
	 */
    public String getMouse() {
        return mouse;
    }

    /**
	 * @param mouse The mouse to set.
	 */
    public void setMouse(String mouse) {
        this.mouse = mouse;
    }

    /**
	 * @return Returns the mousePad.
	 */
    public String getMousePad() {
        return mousePad;
    }

    /**
	 * @param mousePad The mousePad to set.
	 */
    public void setMousePad(String mousePad) {
        this.mousePad = mousePad;
    }

    /**
	 * @return Returns the otherCd.
	 */
    public String getOtherCd() {
        return otherCd;
    }

    /**
	 * @param otherCd The otherCd to set.
	 */
    public void setOtherCd(String otherCd) {
        this.otherCd = otherCd;
    }

    /**
	 * @return Returns the pcmcia.
	 */
    public String getPcmcia() {
        return pcmcia;
    }

    /**
	 * @param pcmcia The pcmcia to set.
	 */
    public void setPcmcia(String pcmcia) {
        this.pcmcia = pcmcia;
    }

    /**
	 * @return Returns the ram.
	 */
    public String getRam() {
        return ram;
    }

    /**
	 * @param ram The ram to set.
	 */
    public void setRam(String ram) {
        this.ram = ram;
    }

    /**
	 * @return Returns the recoveryCd.
	 */
    public String getRecoveryCd() {
        return recoveryCd;
    }

    /**
	 * @param recoveryCd The recoveryCd to set.
	 */
    public void setRecoveryCd(String recoveryCd) {
        this.recoveryCd = recoveryCd;
    }

    /**
	 * @return Returns the scanner.
	 */
    public String getScanner() {
        return scanner;
    }

    /**
	 * @param scanner The scanner to set.
	 */
    public void setScanner(String scanner) {
        this.scanner = scanner;
    }

    /**
	 * @return Returns the videoCamera.
	 */
    public String getVideoCamera() {
        return videoCamera;
    }

    /**
	 * @param videoCamera The videoCamera to set.
	 */
    public void setVideoCamera(String videoCamera) {
        this.videoCamera = videoCamera;
    }

    /**
	 * @return Returns the address.
	 */
    public String getAddress() {
        return address;
    }

    /**
	 * @param address The address to set.
	 */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
	 * @return Returns the brand.
	 */
    public String getBrand() {
        return brand;
    }

    /**
	 * @param brand The brand to set.
	 */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
	 * @return Returns the dateFinish.
	 */
    public String getDateFinish() {
        return dateFinish;
    }

    /**
	 * @param dateFinish The dateFinish to set.
	 */
    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    /**
	 * @return Returns the dateOfPurchase.
	 */
    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    /**
	 * @param dateOfPurchase The dateOfPurchase to set.
	 */
    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    /**
	 * @return Returns the dateStart.
	 */
    public String getDateStart() {
        return dateStart;
    }

    /**
	 * @param dateStart The dateStart to set.
	 */
    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    /**
	 * @return Returns the guaranted.
	 */
    public String getGuaranted() {
        return guaranted;
    }

    /**
	 * @param guaranted The guaranted to set.
	 */
    public void setGuaranted(String guaranted) {
        this.guaranted = guaranted;
    }

    /**
	 * @return Returns the logInfo.
	 */
    public LogInformation getLogInfo() {
        return logInfo;
    }

    /**
	 * @param logInfo The logInfo to set.
	 */
    public void setLogInfo(LogInformation logInfo) {
        this.logInfo = logInfo;
    }

    /**
	 * @return Returns the model.
	 */
    public String getModel() {
        return model;
    }

    /**
	 * @param model The model to set.
	 */
    public void setModel(String model) {
        this.model = model;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the phone.
	 */
    public String getPhone() {
        return phone;
    }

    /**
	 * @param phone The phone to set.
	 */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
	 * @return Returns the pm.
	 */
    public PersistenceManager getPm() {
        return pm;
    }

    /**
	 * @param pm The pm to set.
	 */
    public void setPm(PersistenceManager pm) {
        this.pm = pm;
    }

    /**
	 * @return Returns the product.
	 */
    public String getProduct() {
        return product;
    }

    /**
	 * @param product The product to set.
	 */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
	 * @return Returns the serialNumber.
	 */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
	 * @param serialNumber The serialNumber to set.
	 */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
	 * @return Returns the service.
	 */
    public Service getService() {
        return service;
    }

    /**
	 * @param service The service to set.
	 */
    public void setService(Service service) {
        this.service = service;
    }

    /**
	 * @return Returns the status.
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @param status The status to set.
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * @return Returns the technision.
	 */
    public String getTechnision() {
        return technision;
    }

    /**
	 * @param technision The technision to set.
	 */
    public void setTechnision(String technision) {
        this.technision = technision;
    }

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return Returns the response.
	 */
    public ServiceResponse getResponse() {
        return response;
    }

    /**
	 * @param response The response to set.
	 */
    public void setResponse(ServiceResponse response) {
        this.response = response;
    }

    /**
	 * @return Returns the services.
	 */
    public List getServices() {
        return services;
    }

    /**
	 * @param services The services to set.
	 */
    public void setServices(List services) {
        this.services = services;
    }

    /**
	 * @return Returns the topic.
	 */
    public Topic getTopic() {
        return topic;
    }

    /**
	 * @param topic The topic to set.
	 */
    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    /**
	 * @return Returns the topics.
	 */
    public List getTopics() {
        return topics;
    }

    /**
	 * @param topics The topics to set.
	 */
    public void setTopics(List topics) {
        this.topics = topics;
    }
}
