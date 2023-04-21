package org.osmius.webapp.action;

import org.osmius.model.OsmDDiscovery;
import org.osmius.model.OsmDDiscoveryTypinstances;
import org.osmius.model.OsmGendata;
import org.osmius.model.OsmTypinstance;
import org.osmius.service.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class OsmDiscoveryController implements Controller {

    private OsmDDiscoveryManager osmDDiscoveryManager;

    private OsmDDiscoveryTypinstancesManager osmDDiscoveryTypinstancesManager;

    private OsmDTypinstanceScriptManager osmDTypinstanceScriptManager;

    private OsmTypinstanceManager osmTypinstanceManager;

    private OsmGendataManager osmGendataManager;

    public void setOsmGendataManager(OsmGendataManager osmGendataManager) {
        this.osmGendataManager = osmGendataManager;
    }

    public void setOsmTypinstanceManager(OsmTypinstanceManager osmTypinstanceManager) {
        this.osmTypinstanceManager = osmTypinstanceManager;
    }

    public void setOsmDDiscoveryManager(OsmDDiscoveryManager osmDDiscoveryManager) {
        this.osmDDiscoveryManager = osmDDiscoveryManager;
    }

    public void setOsmDDiscoveryTypinstancesManager(OsmDDiscoveryTypinstancesManager osmDDiscoveryTypinstancesManager) {
        this.osmDDiscoveryTypinstancesManager = osmDDiscoveryTypinstancesManager;
    }

    public void setOsmDTypinstanceScriptManager(OsmDTypinstanceScriptManager osmDTypinstanceScriptManager) {
        this.osmDTypinstanceScriptManager = osmDTypinstanceScriptManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView("discovery/discovery");
        OsmDDiscovery discovery = osmDDiscoveryManager.getOsmDDiscovery();
        List typInstances = osmDDiscoveryTypinstancesManager.getOsmDDiscoveryTypinstances();
        List typinstances = osmTypinstanceManager.getOsmTypinstances(null);
        List osmGendatas = osmGendataManager.getOsmGendatas(null);
        String typPlatform = "";
        if (osmGendatas != null) {
            OsmGendata osmGendata = (OsmGendata) osmGendatas.get(0);
            typPlatform = osmGendata.getTypPlatform();
        }
        model.addObject("typPlatform", typPlatform);
        String discover = request.getParameter("discover");
        if (discover == null || "".equals(discover)) {
            OsmTypinstance typinstance;
            HashMap hashTypinstances = new HashMap();
            for (int i = 0; i < typinstances.size(); i++) {
                typinstance = (OsmTypinstance) typinstances.get(i);
                hashTypinstances.put(typinstance.getTypInstance(), typinstance);
            }
            model.addObject("discovery", discovery);
            model.addObject("typInstances", typInstances);
            model.addObject("hashTypInstances", hashTypinstances);
        } else if ("1".equals(discover)) {
            model = new ModelAndView("genericView");
            String ipRange = request.getParameter("ipRange");
            String ports = request.getParameter("ports");
            model.addObject("mdl", "61");
            try {
                osmDDiscoveryManager.startDiscovery(ipRange, ports);
                model.addObject("ok", true);
                model.addObject("error", null);
            } catch (Exception e) {
                model.addObject("ok", false);
                model.addObject("error", e.getMessage());
            }
        } else if ("0".equals(discover)) {
            model = new ModelAndView("genericView");
            model.addObject("mdl", "61");
            try {
                if (osmDDiscoveryManager.stopDiscovery()) {
                    model.addObject("error", null);
                } else {
                    model.addObject("error", "discoverstop");
                }
                model.addObject("ok", true);
            } catch (Exception e) {
                model.addObject("ok", false);
                model.addObject("error", e.getMessage());
            }
        } else if ("2".equals(discover)) {
            model = new ModelAndView("discovery/discoveryAJAX");
            OsmTypinstance typinstance;
            HashMap hashTypinstances = new HashMap();
            for (int i = 0; i < typinstances.size(); i++) {
                typinstance = (OsmTypinstance) typinstances.get(i);
                hashTypinstances.put(typinstance.getTypInstance(), typinstance);
            }
            model.addObject("discovery", discovery);
            model.addObject("typInstances", typInstances);
            model.addObject("hashTypInstances", hashTypinstances);
        } else if ("3".equals(discover)) {
            String typInstance = request.getParameter("typInstance");
            OsmDDiscoveryTypinstances osmDDiscoveryTypinstances = osmDDiscoveryTypinstancesManager.getOsmDDiscoveryTypinstance(typInstance);
            if (osmDDiscoveryTypinstances != null) {
                byte[] csv = osmDDiscoveryTypinstances.getBinSpreadsheet();
                model = new ModelAndView("discovery/viewCSVAJAX");
                model.addObject("csv", csv);
                model.addObject("typInstance", typInstance);
            }
        }
        return model;
    }
}
