package com.acgvision.core.ws;

import com.acgvision.core.model.Host;
import com.acgvision.core.model.LocaleUtils;
import com.acgvision.core.model.Service;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 
 * @author Rémi Debay <remi.debay@acgcenter.com>
 * @created 2 juil. 2009
 */
public class Hostws implements Serializable {

    private Long id;

    private String hostname;

    private String ip;

    private String os;

    private String owneremail;

    private List<String> services = new ArrayList();

    public Hostws() {
    }

    public Hostws(Host h) {
        if (h != null) {
            this.id = h.getId();
            this.hostname = h.getHostName();
            this.ip = h.getIp();
            this.os = h.getOperatingSystem();
            if (h.getOwner() != null) {
                this.owneremail = h.getOwner().getEmail();
            }
            if (h.getServices() != null && !h.getServices().isEmpty()) {
                Iterator<Service> it = h.getServices().iterator();
                ResourceBundle msgBundle = LocaleUtils.getResourceBundle(Locale.getDefault());
                while (it.hasNext()) {
                    String service = it.next().getName();
                    if (msgBundle.containsKey(service)) {
                        this.services.add(msgBundle.getString(service));
                    } else {
                        this.services.add(service);
                    }
                }
            }
        }
    }

    public Hostws(Host h, Locale loc) {
        if (h != null) {
            this.id = h.getId();
            this.hostname = h.getHostName();
            this.ip = h.getIp();
            this.os = h.getOperatingSystem();
            if (h.getOwner() != null) {
                this.owneremail = h.getOwner().getEmail();
            }
            if (h.getServices() != null && !h.getServices().isEmpty()) {
                Iterator<Service> it = h.getServices().iterator();
                ResourceBundle msgBundle = LocaleUtils.getResourceBundle(loc);
                while (it.hasNext()) {
                    String service = it.next().getName();
                    if (msgBundle.containsKey(service)) {
                        this.services.add(msgBundle.getString(service));
                    } else {
                        this.services.add(service);
                    }
                }
            }
        }
    }

    public static List<Hostws> toHostws(List<Host> lHost, Locale locale) {
        if (lHost == null) return new ArrayList();
        List<Hostws> response = new ArrayList();
        Iterator<Host> it = lHost.iterator();
        while (it.hasNext()) {
            Host i = it.next();
            response.add(new Hostws(i, locale));
        }
        return response;
    }

    public static List<Hostws> toHostws(List<Host> lHost) {
        if (lHost == null) return new ArrayList();
        List<Hostws> response = new ArrayList();
        Iterator<Host> it = lHost.iterator();
        while (it.hasNext()) {
            Host i = it.next();
            response.add(new Hostws(i));
        }
        return response;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the os
     */
    public String getOs() {
        return os;
    }

    /**
     * @param os the os to set
     */
    public void setOs(String os) {
        this.os = os;
    }

    /**
     * @return the owneremail
     */
    public String getOwneremail() {
        return owneremail;
    }

    /**
     * @param owneremail the owneremail to set
     */
    public void setOwneremail(String owneremail) {
        this.owneremail = owneremail;
    }

    /**
     * @return the services
     */
    public List<String> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<String> services) {
        this.services = services;
    }
}
