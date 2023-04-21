package edu.thu.keg.iw.services.engine.select.ruleparser;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import edu.thu.tsinghua.iw.app.model.Context;

public class Group {

    private Vector<Service> serviceCandidates;

    private Map<Context, Float> coefficient;

    private Map<Service, Float> utility;

    private Map<String, String> context;

    private String deviceID;

    private String groupName;

    public Group(String id, Map<Context, Float> coefficient) {
        serviceCandidates = new Vector<Service>();
        context = new HashMap<String, String>();
        deviceID = id;
        this.coefficient = coefficient;
    }

    public Group(Vector<String> serviceStringCandidates, String id, Map<Context, Float> coefficient) {
        this.coefficient = coefficient;
        deviceID = id;
        serviceCandidates = new Vector<Service>();
        context = new HashMap<String, String>();
        for (int i = 0; i < serviceStringCandidates.size(); i++) {
            Service s = new Service();
            s.setServiceID(serviceStringCandidates.get(i));
            serviceCandidates.add(s);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void addService(Service s) {
        serviceCandidates.add(s);
    }

    public Service getService(int i) {
        return serviceCandidates.get(i);
    }

    public Service removeService(int i) {
        return serviceCandidates.remove(i);
    }

    public Map<Context, Float> getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Map<Context, Float> coefficient) {
        this.coefficient = coefficient;
    }

    public void loadGroupInformation() {
        DBAdapter db = new DBAdapter();
        db.createServiceConnection();
        for (int i = 0; i < serviceCandidates.size(); i++) {
            serviceCandidates.get(i).setRules(db.getRules(serviceCandidates.get(i).getServiceID()));
            serviceCandidates.get(i).setQos(db.getQoS(serviceCandidates.get(i).getServiceID()));
        }
        db.closeServiceConnection();
        db.createContextConnection();
        context = db.getContext(deviceID);
        db.closeContextConnection();
    }

    private int getQoSIndex(Service s, int type) {
        int index = 0;
        for (int i = 0; i < serviceCandidates.size(); i++) {
            switch(type) {
                case 1:
                    if (s.getQos().getReliableRate() < serviceCandidates.get(i).getQos().getReliableRate()) {
                        index++;
                    }
                    break;
                case 2:
                    if (s.getQos().getResponseTime() < serviceCandidates.get(i).getQos().getResponseTime()) {
                        index++;
                    }
                    break;
                case 3:
                    if (s.getQos().getUseCount() < serviceCandidates.get(i).getQos().getUseCount()) {
                        index++;
                    }
                    break;
                default:
                    break;
            }
        }
        return index;
    }

    public void calc() {
        utility = new HashMap<Service, Float>();
        for (int i = 0; i < serviceCandidates.size(); i++) {
            Service service = serviceCandidates.get(i);
            float serviceUtility = 0f;
            for (Context c : coefficient.keySet()) {
                if (c.getRef().equals("QoS")) {
                    if (serviceCandidates.size() > 1) {
                        if (c.getName().equals("responseTime")) {
                            serviceUtility += coefficient.get(c) / (serviceCandidates.size() - 1) * (getQoSIndex(service, 2));
                        }
                        if (c.getName().equals("reliableRate")) {
                            serviceUtility += coefficient.get(c) / (serviceCandidates.size() - 1) * (serviceCandidates.size() - 1 - getQoSIndex(service, 1));
                        }
                        if (c.getName().equals("useCount")) {
                            serviceUtility += coefficient.get(c) / (serviceCandidates.size() - 1) * (serviceCandidates.size() - 1 - getQoSIndex(service, 3));
                        }
                    }
                } else {
                    if (!service.getRules().containsKey(c.getName())) {
                        serviceUtility += 0.5 * coefficient.get(c);
                    } else {
                        String rule = service.getRules().get(c.getName());
                        try {
                            RuleParser parser = new RuleParser(rule, context, c.getName());
                            serviceUtility += parser.getRuleScore() * coefficient.get(c);
                        } catch (IlligalRuleFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            utility.put(service, serviceUtility);
        }
    }

    public Map<Service, Float> getUtility() {
        return utility;
    }

    public void setUtility(Map<Service, Float> utility) {
        this.utility = utility;
    }

    public void printContext() {
        System.out.println("-----print out context----");
        for (String s : context.keySet()) {
            System.out.println(s + ":" + context.get(s));
        }
    }

    public void printQoS() {
        System.out.println("-----print out QoS----");
        for (int i = 0; i < serviceCandidates.size(); i++) {
            serviceCandidates.get(i).printQoS();
        }
    }

    public void printRules() {
        System.out.println("-----print out rules----");
        for (int i = 0; i < serviceCandidates.size(); i++) {
            serviceCandidates.get(i).print();
        }
    }

    public void printUtility() {
        System.out.println("-----print out utility----");
        for (Service s : utility.keySet()) {
            System.out.println(s.getServiceID() + ":" + utility.get(s));
        }
    }
}
