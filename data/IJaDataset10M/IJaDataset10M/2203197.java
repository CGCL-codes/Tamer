package jp.riken.omicspace.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;

class ServiceManager {

    private static Pattern periodPattern = Pattern.compile(".");

    public static ServiceManager getInstance() {
        return instance;
    }

    private ServiceManager() {
    }

    public void lookup(String s, ArrayList<SpeciesVersion> acoordinate) {
        arrayListViewOfServices = new ArrayList();
        hashtableViewOfServices = new Hashtable();
        for (SpeciesVersion coordinate : acoordinate) {
            addService(new Service(coordinate.getAlias(), coordinate.getAlias(), ""));
            addService(new Service(coordinate.getCheck(), coordinate.getAlias() + "." + coordinate.getCheck(), ""));
            Service aservice[] = MenuXmlParser.parse(s, coordinate.getAlias(), coordinate.getCheck());
            for (int j = 0; j < aservice.length; j++) addService(aservice[j]);
        }
    }

    private void addService(Service service) {
        if (!hashtableViewOfServices.containsKey(service.getPath())) {
            arrayListViewOfServices.add(service);
            hashtableViewOfServices.put(service.getPath(), service);
        }
    }

    public Service[] getServices() {
        Service aservice[] = new Service[arrayListViewOfServices.size()];
        return (Service[]) arrayListViewOfServices.toArray(aservice);
    }

    public Service getService(String s) {
        Service service = (Service) hashtableViewOfServices.get(s);
        if (service == null) {
            String as[] = periodPattern.split(s);
            ;
            if (as.length == 1) return null;
            String s1 = "";
            for (int i = 0; i < as.length - 1; i++) if (i == 0) s1 = s1 + as[i]; else s1 = s1 + "." + as[i];
            return getService(s1);
        } else {
            return service;
        }
    }

    private static ServiceManager instance = new ServiceManager();

    private ArrayList arrayListViewOfServices;

    private Hashtable hashtableViewOfServices;
}
