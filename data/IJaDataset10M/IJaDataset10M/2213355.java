package vobs.webapp;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.http.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.apache.struts.action.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.xmldb.api.base.Collection;
import org.xmldb.api.modules.*;
import vobs.datamodel.*;
import vobs.dbaccess.*;
import wdc.settings.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import org.xml.sax.SAXException;
import org.xmldb.api.base.XMLDBException;
import org.apache.log4j.Logger;
import java.text.*;

public final class PlumeAction extends Action {

    private Logger log = Logger.getLogger(PlumeAction.class);

    private static String userDB = Settings.get("vo_meta.userProfilesResource");

    private static String httpURI = Settings.get("vo_meta.httpUri");

    private static String xmldbURI = Settings.get("vo_meta.uri");

    private static String logsDB = Settings.get("vo_meta.logsResource");

    private static String settingsDB = Settings.get("vo_meta.settingsResource");

    private static String forumDB = Settings.get("vo_meta.forumResource");

    private static String timeZone = Settings.get("vo_meta.timeZone");

    private static String rootDB = Settings.get("vo_meta.rootCollection");

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Locale locale = getLocale(request);
        ActionErrors errors = new ActionErrors();
        HttpSession session = request.getSession();
        User voUser = (User) session.getAttribute("voUser");
        session.removeAttribute("currentPage");
        VO virtObs = (VO) session.getAttribute("vobean");
        if (null == virtObs) {
            return (mapping.findForward("logon"));
        }
        if (request.getParameter("source") != null && virtObs != null) {
            virtObs.setAction(request.getParameter("source"));
            session.setAttribute("vobean", virtObs);
        }
        if (voUser == null) {
            log.error("Session is missing or has expired for client from " + request.getRemoteAddr());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.session.nouser"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
        if (!voUser.isAnonymous()) {
            Element etmp = null;
            String s = null;
            DecimalFormat myFormatter = null;
            try {
                Document doc = new Document();
                Element eRoot = new Element("InputFile");
                doc.setRootElement(eRoot);
                etmp = new Element("ForecastArea");
                etmp.addContent("0");
                eRoot.addContent(etmp);
                etmp = new Element("DateFormat");
                etmp.addContent("dd.mm.yyyy");
                eRoot.addContent(etmp);
                etmp = new Element("TimeFormat");
                etmp.addContent("hh:mm");
                eRoot.addContent(etmp);
                myFormatter = new DecimalFormat("00");
                s = myFormatter.format(Integer.parseInt(request.getParameter("hour")));
                s += ":" + myFormatter.format(Integer.parseInt(request.getParameter("minutes")));
                etmp = new Element("EmissionTime");
                etmp.addContent(s);
                eRoot.addContent(etmp);
                s = myFormatter.format(Integer.parseInt(request.getParameter("day")));
                s += "." + myFormatter.format(Integer.parseInt(request.getParameter("month")));
                s += "." + request.getParameter("year");
                etmp = new Element("EmissionDate");
                etmp.addContent(s);
                eRoot.addContent(etmp);
                Calendar cal = Calendar.getInstance();
                int year = Integer.parseInt(request.getParameter("year"));
                int month = Integer.parseInt(request.getParameter("month"));
                int day = Integer.parseInt(request.getParameter("day"));
                int hour = Integer.parseInt(request.getParameter("hour"));
                int minutes = Integer.parseInt(request.getParameter("minutes"));
                cal.set(year, month - 1, day, hour, minutes);
                cal.add(Calendar.MINUTE, Integer.parseInt(request.getParameter("msrtime")));
                SimpleDateFormat df1 = new SimpleDateFormat("dd.MM.yyyy");
                SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
                etmp = new Element("MeasuresTime");
                etmp.addContent(df2.format(cal.getTime()));
                eRoot.addContent(etmp);
                etmp = new Element("MeasuresDate");
                etmp.addContent(df1.format(cal.getTime()));
                eRoot.addContent(etmp);
                Element eGrid = new Element("Grid");
                eRoot.addContent(eGrid);
                etmp = new Element("DimensionX");
                etmp.addContent(request.getParameter("dimx"));
                eGrid.addContent(etmp);
                etmp = new Element("DimensionY");
                etmp.addContent(request.getParameter("dimy"));
                eGrid.addContent(etmp);
                Element eObjectLocation = new Element("ObjectLocation");
                eRoot.addContent(eObjectLocation);
                etmp = new Element("ObjLat");
                etmp.addContent(request.getParameter("objlat"));
                eObjectLocation.addContent(etmp);
                etmp = new Element("ObjLon");
                etmp.addContent(request.getParameter("objlon"));
                eObjectLocation.addContent(etmp);
                etmp = new Element("LatMin");
                etmp.addContent(request.getParameter("latmin"));
                eObjectLocation.addContent(etmp);
                etmp = new Element("LonMin");
                etmp.addContent(request.getParameter("lonmin"));
                eObjectLocation.addContent(etmp);
                etmp = new Element("LatMax");
                etmp.addContent(request.getParameter("latmax"));
                eObjectLocation.addContent(etmp);
                etmp = new Element("LonMax");
                etmp.addContent(request.getParameter("lonmax"));
                eObjectLocation.addContent(etmp);
                Element eSourceScenario = new Element("SourceScenario");
                eRoot.addContent(eSourceScenario);
                etmp = new Element("TotalMass");
                etmp.addContent(request.getParameter("mass"));
                eSourceScenario.addContent(etmp);
                etmp = new Element("ChimneyHeight");
                etmp.addContent(request.getParameter("chheight"));
                eSourceScenario.addContent(etmp);
                etmp = new Element("CloudAltitude");
                etmp.addContent(request.getParameter("clalt"));
                eSourceScenario.addContent(etmp);
                etmp = new Element("CloudRadius");
                etmp.addContent(request.getParameter("clrad"));
                eSourceScenario.addContent(etmp);
                Element eSubstance = new Element("Substance");
                eSourceScenario.addContent(eSubstance);
                etmp = new Element("Name");
                etmp.addContent("unknown");
                eSubstance.addContent(etmp);
                etmp = new Element("LongName");
                etmp.addContent(request.getParameter("substance"));
                eSubstance.addContent(etmp);
                etmp = new Element("Mass");
                etmp.addContent(request.getParameter("mass"));
                eSubstance.addContent(etmp);
                Element eTimeScenario = new Element("TimeScenario");
                eSourceScenario.addContent(eTimeScenario);
                Element eTimeInterval = new Element("TimeInterval");
                eTimeScenario.addContent(eTimeInterval);
                etmp = new Element("StartTime");
                etmp.addContent("0.0");
                eTimeInterval.addContent(etmp);
                etmp = new Element("SpanTime");
                etmp.addContent("1.00e-01");
                eTimeInterval.addContent(etmp);
                etmp = new Element("Altitude");
                etmp.addContent("0.00e+00");
                eTimeInterval.addContent(etmp);
                Element eGroup = new Element("Group");
                eTimeInterval.addContent(eGroup);
                etmp = new Element("GravitySpeed");
                etmp.addContent("0.00e+00");
                eGroup.addContent(etmp);
                etmp = new Element("DryDeposition");
                etmp.addContent("0.00e+00");
                eGroup.addContent(etmp);
                etmp = new Element("Scavenging");
                etmp.addContent("0.00e+00");
                eGroup.addContent(etmp);
                Element eSubsGroup = new Element("SubsGroup");
                eGroup.addContent(eSubsGroup);
                etmp = new Element("SubsName");
                etmp.addContent("unknown");
                eSubsGroup.addContent(etmp);
                myFormatter = new DecimalFormat("0.00E00", new DecimalFormatSymbols(new Locale("en")));
                s = myFormatter.format(Integer.parseInt(request.getParameter("mass")));
                if (s.length() == 7) {
                    s = s.substring(0, 5).toLowerCase() + '+' + s.substring(5, 7);
                }
                etmp = new Element("SubsMass");
                etmp.addContent(s);
                eSubsGroup.addContent(etmp);
                Element eMeteoScenario = new Element("MeteoScenario");
                eRoot.addContent(eMeteoScenario);
                Element eMeteoTimeInterval = new Element("MeteoTimeInterval");
                eMeteoScenario.addContent(eMeteoTimeInterval);
                etmp = new Element("MeteoStartTime");
                etmp.addContent("0");
                eMeteoTimeInterval.addContent(etmp);
                String dt = request.getParameter("datetime");
                float wWindU = Float.parseFloat(request.getParameter(dt + "***U_GRD.HTGL"));
                float wWindV = Float.parseFloat(request.getParameter(dt + "***V_GRD.HTGL"));
                float windSpeed = (float) Math.pow(Math.pow(wWindV, 2) + Math.pow(wWindU, 2), 0.5);
                float windDir = (float) (Math.atan2(-wWindU, -wWindV) * 180 / Math.PI);
                etmp = new Element("WindDirection");
                etmp.addContent(Float.toString(Math.round(windDir)));
                eMeteoTimeInterval.addContent(etmp);
                etmp = new Element("WindSpeed");
                etmp.addContent(Float.toString(Math.round(windSpeed)));
                eMeteoTimeInterval.addContent(etmp);
                etmp = new Element("Precipitation");
                etmp.addContent(request.getParameter(dt + "***A_PCP.SFC"));
                eMeteoTimeInterval.addContent(etmp);
                etmp = new Element("StabilityClass");
                etmp.addContent("E");
                eMeteoTimeInterval.addContent(etmp);
                etmp = new Element("SurfaceType");
                etmp.addContent("1");
                eMeteoTimeInterval.addContent(etmp);
                Element eCalculationParameters = new Element("CalculationParameters");
                eRoot.addContent(eCalculationParameters);
                etmp = new Element("TimeStep");
                etmp.addContent(request.getParameter("timestep"));
                eCalculationParameters.addContent(etmp);
                etmp = new Element("ModelType");
                etmp.addContent(request.getParameter("modeltype"));
                eCalculationParameters.addContent(etmp);
                etmp = new Element("ParticlesNumber");
                etmp.addContent(request.getParameter("particles"));
                eCalculationParameters.addContent(etmp);
                etmp = new Element("OutputTimeStep");
                etmp.addContent(request.getParameter("outputts"));
                eCalculationParameters.addContent(etmp);
                XMLOutputter xmlout = new XMLOutputter();
                String fileName = "plume_" + System.currentTimeMillis() + ".xml";
                File outFile = new File(wdc.settings.Settings.get("locations.localExportDir") + "/" + fileName);
                FileWriter writ = new FileWriter(outFile);
                writ.write(xmlout.outputString(doc));
                writ.close();
                String linkToFile = wdc.settings.Settings.get("locations.httpExportDir") + "/" + fileName;
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                throw e;
            }
            return (mapping.findForward("success"));
        } else {
            log.error("User from " + request.getRemoteAddr() + " is not authorize to use this page.");
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.voUser.authorize"));
            saveErrors(request, errors);
            return (mapping.findForward("logon"));
        }
    }
}
