package manager.ui.app;

import manager.app.ManagerProperties;
import org.json.simple.JSONObject;
import prisms.arch.PrismsApplication;

/** Allows the user to edit properties of an application */
public class ApplicationEditor implements prisms.arch.AppPlugin {

    prisms.arch.PrismsSession theSession;

    private String theName;

    PrismsApplication theApp;

    public void initPlugin(prisms.arch.PrismsSession session, prisms.arch.PrismsConfig config) {
        theSession = session;
        theName = config.get("name");
        theApp = theSession.getProperty(ManagerProperties.selectedApp);
        session.addPropertyChangeListener(ManagerProperties.selectedApp, new prisms.arch.event.PrismsPCL<PrismsApplication>() {

            public void propertyChange(prisms.arch.event.PrismsPCE<PrismsApplication> evt) {
                setApp(evt.getNewValue());
            }

            @Override
            public String toString() {
                return "Manager App Editor Content Updater";
            }
        });
    }

    public void initClient() {
        boolean visible = theApp != null;
        JSONObject evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "setVisible");
        evt.put("visible", Boolean.valueOf(visible));
        theSession.postOutgoingEvent(evt);
        if (!visible) return;
        evt = new JSONObject();
        evt.put("plugin", theName);
        evt.put("method", "setValue");
        JSONObject val = new JSONObject();
        val.put("name", theApp.getName());
        val.put("descrip", theApp.getDescription());
        evt.put("value", val);
        theSession.postOutgoingEvent(evt);
    }

    public void processEvent(JSONObject evt) {
        throw new IllegalArgumentException("Unrecognized " + theName + " event " + evt);
    }

    void setApp(PrismsApplication app) {
        if (theApp == null && app == null) return;
        theApp = app;
        initClient();
    }
}
