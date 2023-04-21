package api.server.jUDDI;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import javax.xml.rpc.ServiceException;
import api.server.jUDDI.api_v2_uddi.BusinessInfo;
import api.server.jUDDI.api_v2_uddi.BusinessList;
import api.server.jUDDI.api_v2_uddi.CategoryBag;
import api.server.jUDDI.api_v2_uddi.ServiceDetail;
import api.server.jUDDI.api_v2_uddi.ServiceInfo;
import api.server.jUDDI.api_v2_uddi.ServiceList;
import api.server.service.Service;

public class UDDIVerwaltung {

    private static String uddiLogin = "mhof";

    private static String uddiPassword = "";

    static UDDIFassade fassade;

    static String businesskey;

    static String tagsServiceKey;

    public UDDIVerwaltung() {
        if (fassade == null) fassade = UDDIFassade.getInstance();
        loadBusinessKey();
    }

    public static void changePublishServiceLocator(String address) {
        fassade.psl.setPublishPort_address(address);
        try {
            fassade.pub = fassade.psl.getPublishPort();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static void changeInquireServiceLocator(String address) {
        fassade.isl.setInquirePort_address(address);
        try {
            fassade.inq = fassade.isl.getInquirePort();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    public static UDDIFassade getFassade() {
        if (fassade == null) new UDDIVerwaltung();
        return fassade;
    }

    /**
	 * Save default business for all services
	 */
    public static void saveNewBusiness() {
        try {
            businesskey = fassade.SaveBusiness("All Services1", "Business entity", uddiLogin, uddiPassword);
        } catch (Exception e) {
            System.out.println("Could not save new Business :(");
        }
    }

    /**
	 * set loadBusinessKey of "All Services" BusinessEntity if it exists return true
	 * otherwise return false
	 */
    public static void loadBusinessKey() {
        BusinessList defaultBusiness;
        BusinessInfo bInfo;
        try {
            defaultBusiness = fassade.FindBusinessbyName("All Services1");
            bInfo = defaultBusiness.getBusinessInfos().getBusinessInfo(0);
        } catch (Exception e) {
            saveNewBusiness();
            return;
        }
        businesskey = bInfo.getBusinessKey();
        return;
    }

    /**
	 * Save new Service for tags list
	 */
    public static void saveTagsService() throws Exception {
        tagsServiceKey = fassade.SaveService("Maximum Tags", "Tags of all services", businesskey, uddiLogin, uddiPassword);
        fassade.SetNewMetadatatoService(tagsServiceKey, "MaxTags", "0", uddiLogin, uddiPassword);
        System.out.println("new Tag List service created");
    }

    /**
	 * load tagsServiceKey of "Tags List" Service if it exists return true
	 * otherwise return false
	 */
    public static void loadTagServiceKey() throws Exception {
        ServiceList tagsService;
        ServiceInfo sInfo;
        try {
            tagsService = fassade.FindServicebyName("Maximum Tags");
            sInfo = tagsService.getServiceInfos().getServiceInfo(0);
        } catch (Exception e) {
            saveTagsService();
            return;
        }
        tagsServiceKey = sInfo.getServiceKey();
        return;
    }

    public static void saveServiceInfo(Service myService) throws Exception {
        String description = myService.getServiceDescription();
        if (description != null) {
            saveDescriptionUDDI(myService, description);
        }
        String author = myService.getAuthor();
        if (author != null) {
            saveAuthorUDDI(myService, author);
        }
        String WSDL = myService.getWsdlURL();
        if (WSDL != null) {
            saveWsdlUrlUDDI(myService, WSDL);
        }
        String version = myService.getVersion();
        if (version != null) {
            saveVersionUDDI(myService, version);
        }
        int ratingCount = myService.getRatingCount();
        if (myService.getRatingCount() > 0) {
            fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "RatingCount", Integer.toString(myService.getRatingCount()), uddiLogin, uddiPassword);
            fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Rating", Float.toString(myService.getRating()), uddiLogin, uddiPassword);
        }
        if (myService.getTags().size() > 0) {
            saveTagsUDDI(myService);
        }
        GregorianCalendar myDate = myService.getDate();
        if (myDate != null) {
            saveDateUDDI(myService, myDate);
        }
    }

    public static void saveNewServiceUDDI(Service myService) throws Exception {
        UDDIFassade f = fassade;
        f = null;
        String servicekey = fassade.SaveService(myService.getName(), "asjbdksdhk", businesskey, uddiLogin, uddiPassword);
        myService.setUDDIServiceKey(servicekey);
        saveServiceInfo(myService);
    }

    public static void saveDescriptionUDDI(Service myService, String description) throws Exception {
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Description", description, uddiLogin, uddiPassword);
    }

    public static void saveWsdlUrlUDDI(Service myService, String WsdlUrl) throws Exception {
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "WSDL", WsdlUrl, uddiLogin, uddiPassword);
    }

    public static void saveDateUDDI(Service myService, GregorianCalendar date) throws Exception {
        String dateString = Integer.toString(date.get(Calendar.DATE)) + "." + Integer.toString(date.get(Calendar.MONTH + 1)) + "." + Integer.toString(date.get(Calendar.YEAR));
        System.out.println(myService.getName() + " wurde registriert: " + dateString);
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Date", dateString, uddiLogin, uddiPassword);
    }

    public static void saveVersionUDDI(Service myService, String version) throws Exception {
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Version", version, uddiLogin, uddiPassword);
    }

    public static void saveAuthorUDDI(Service myService, String author) throws Exception {
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Author", author, uddiLogin, uddiPassword);
    }

    public static void saveRatingUDDI(Service myService, int bewertung) throws Exception {
    }

    public static void saveTagsUDDI2(Service myService) throws Exception {
        String myServiceKey = myService.getUDDIServiceKey();
        String oldTagsString = "";
        Vector tagsVector = myService.getTags();
        for (int i = 0; i < tagsVector.size(); i++) {
            oldTagsString += tagsVector.get(i) + ":";
            System.out.println("gesamt " + oldTagsString);
            System.out.println("  " + tagsVector.get(i));
        }
        fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), "Tags", oldTagsString, uddiLogin, uddiPassword);
        for (int j = 0; j < tagsVector.size(); j++) {
            String tag = (String) tagsVector.get(j);
            try {
                String oldServices = "";
                ServiceDetail sd = fassade.GetServiceDetailsByKey(tagsServiceKey);
                CategoryBag categoryBag = sd.getBusinessService(0).getCategoryBag();
                for (int i = 0; i < categoryBag.getKeyedReference().length; i++) {
                    if (tag.equals(categoryBag.getKeyedReference(i).getKeyName())) {
                        oldServices = categoryBag.getKeyedReference(i).getKeyValue() + ":";
                        break;
                    }
                }
                System.out.println(oldServices);
                fassade.SetNewMetadatatoService(tagsServiceKey, tag, oldServices + myServiceKey, uddiLogin, uddiPassword);
            } catch (Exception e) {
                fassade.SetNewMetadatatoService(tagsServiceKey, tag, myServiceKey, uddiLogin, uddiPassword);
                return;
            }
        }
    }

    public static void saveTagsUDDI3(Service myService) throws Exception {
        String myServiceKey = myService.getUDDIServiceKey();
        Vector tagsVector = myService.getTags();
        int tagsAmount = tagsVector.size();
        for (int i = 0; i < tagsVector.size(); i++) {
            int tagNum = i + 1;
            String keyName = "Tag" + tagNum;
            fassade.SetNewMetadatatoService(myService.getUDDIServiceKey(), keyName, (String) tagsVector.get(i), uddiLogin, uddiPassword);
        }
        ServiceDetail sd = fassade.GetServiceDetailsByKey(tagsServiceKey);
        CategoryBag categoryBag = sd.getBusinessService(0).getCategoryBag();
        for (int i = 0; i < categoryBag.getKeyedReference().length; i++) {
            if ("MaxTags".equals(categoryBag.getKeyedReference(i).getKeyName())) {
                String maxTags = categoryBag.getKeyedReference(i).getKeyValue();
                if (Integer.parseInt(maxTags) < tagsAmount) fassade.SetNewMetadatatoService(tagsServiceKey, "MaxTags", String.valueOf(tagsAmount), uddiLogin, uddiPassword);
                break;
            }
        }
    }

    public static void saveTagsUDDI(Service myService) throws Exception {
        String myServiceKey = myService.getUDDIServiceKey();
        Vector<String> tagsVector = myService.getTags();
        deleteTagsUDDI(myService);
        for (int i = 0; i < tagsVector.size(); i++) {
            String tag = (String) tagsVector.get(i);
            fassade.SetNewMetadatatoService(myServiceKey, "&" + tag, "tag", uddiLogin, uddiPassword);
        }
    }

    public static void deleteTagsUDDI(Service myService) throws Exception {
        String myServiceKey = myService.getUDDIServiceKey();
        CategoryBag categoryBag = fassade.GetServiceDetailsByKey(myServiceKey).getBusinessService(0).getCategoryBag();
        for (int i = 0; i < categoryBag.getKeyedReference().length; i++) {
            String key = categoryBag.getKeyedReference(i).getKeyName();
            String value = categoryBag.getKeyedReference(i).getKeyValue();
            if (key.charAt(0) == '&' && value.equals("tag")) if (!myService.getTags().contains(key.substring(1))) fassade.SetNewMetadatatoService(myServiceKey, key, "null", uddiLogin, uddiPassword);
        }
    }

    public static Service loadService(String serviceKey) {
        ServiceDetail sd;
        try {
            sd = fassade.GetServiceDetailsByKey(serviceKey);
        } catch (Exception e) {
            return null;
        }
        String service_name = "", serviceDescription = "", version = "", author = "", UDDIServiceKey = "", wsdlURL = "";
        CategoryBag categoryBag = sd.getBusinessService(0).getCategoryBag();
        String key = "", value = "";
        int count;
        float bewertung;
        Vector<String> tags;
        GregorianCalendar date = new GregorianCalendar();
        Vector<String> comments;
        Vector<String> references;
        for (int i = 0; i < categoryBag.getKeyedReference().length; i++) {
            key = categoryBag.getKeyedReference(i).getKeyName();
            value = categoryBag.getKeyedReference(i).getKeyValue();
            if (key.equalsIgnoreCase("name")) {
                service_name = value;
            } else if (key.equalsIgnoreCase("description")) {
                serviceDescription = value;
            } else if (key.equalsIgnoreCase("date")) {
                String dates[] = value.split("\\.");
                date = new GregorianCalendar(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
            } else if (key.equalsIgnoreCase("version")) {
                version = value;
            } else if (key.equalsIgnoreCase("wsdl")) {
                wsdlURL = value;
            } else if (key.equalsIgnoreCase("Author")) {
                author = value;
            } else if (key.equalsIgnoreCase("rating")) {
                bewertung = Float.parseFloat(value);
            } else if (key.equalsIgnoreCase("ratingcount")) {
                count = Integer.parseInt(value);
            } else if (key.equalsIgnoreCase("tags")) {
            }
        }
        Service foundService = new Service();
        foundService.setAuthor(author);
        foundService.setName(service_name);
        foundService.setDate(date);
        foundService.setServiceDescription(serviceDescription);
        foundService.setUDDIServiceKey(UDDIServiceKey);
        foundService.setVersion(version);
        foundService.setUDDIServiceKey(serviceKey);
        foundService.setWsdlURL(wsdlURL);
        return foundService;
    }

    public static boolean deleteService(String key) {
        try {
            fassade.DeleteService(key, uddiLogin, uddiPassword);
            return true;
        } catch (Exception e) {
            System.err.println(e.getStackTrace());
            return false;
        }
    }

    public static void main(String[] args) {
        UDDIVerwaltung uVerwaltung = new UDDIVerwaltung();
        try {
            UDDIFassade.getInstance().SaveService("TagsOfAllServices", "aaabb", businesskey, uddiLogin, uddiPassword);
            ServiceList findResults = UDDIVerwaltung.getFassade().FindServicebyName("TagsOfAllServices");
            ServiceInfo serviceInfo = findResults.getServiceInfos().getServiceInfo(0);
            String UDDIServiceKey = serviceInfo.getServiceKey();
            System.out.println(" ff" + UDDIServiceKey);
            {
            }
        } catch (Exception e) {
            System.out.print("Error: ");
            System.out.print(e.getClass());
            System.out.print(e.getMessage());
        }
    }
}
