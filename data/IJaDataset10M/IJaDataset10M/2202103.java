package org.fao.geonet.kernel.harvest.harvester.oaipmh;

import java.io.File;
import java.sql.SQLException;
import java.util.UUID;
import jeeves.exceptions.BadInputEx;
import jeeves.interfaces.Logger;
import jeeves.resources.dbms.Dbms;
import jeeves.server.context.ServiceContext;
import jeeves.server.resources.ResourceManager;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.harvest.harvester.AbstractHarvester;
import org.fao.geonet.kernel.harvest.harvester.AbstractParams;
import org.fao.geonet.lib.Lib;
import org.jdom.Element;

public class OaiPmhHarvester extends AbstractHarvester {

    public static void init(ServiceContext context) throws Exception {
    }

    public String getType() {
        return "oaipmh";
    }

    protected void doInit(Element node) throws BadInputEx {
        params = new OaiPmhParams(dataMan);
        params.create(node);
    }

    protected void doDestroy(Dbms dbms) throws SQLException {
        File icon = new File(context.getAppPath() + "images/logos", params.uuid + ".gif");
        icon.delete();
        Lib.sources.delete(dbms, params.uuid);
    }

    protected String doAdd(Dbms dbms, Element node) throws BadInputEx, SQLException {
        params = new OaiPmhParams(dataMan);
        params.create(node);
        params.uuid = UUID.randomUUID().toString();
        String id = settingMan.add(dbms, "harvesting", "node", getType());
        storeNode(dbms, params, "id:" + id);
        Lib.sources.update(dbms, params.uuid, params.name, true);
        Lib.sources.copyLogo(context, "/images/harvesting/" + params.icon, params.uuid);
        return id;
    }

    protected void doUpdate(Dbms dbms, String id, Element node) throws BadInputEx, SQLException {
        OaiPmhParams copy = params.copy();
        copy.update(node);
        String path = "harvesting/id:" + id;
        settingMan.removeChildren(dbms, path);
        storeNode(dbms, copy, path);
        Lib.sources.update(dbms, copy.uuid, copy.name, true);
        Lib.sources.copyLogo(context, "/images/harvesting/" + copy.icon, copy.uuid);
        params = copy;
    }

    protected void storeNodeExtra(Dbms dbms, AbstractParams p, String path, String siteId, String optionsId) throws SQLException {
        OaiPmhParams params = (OaiPmhParams) p;
        settingMan.add(dbms, "id:" + siteId, "url", params.url);
        settingMan.add(dbms, "id:" + siteId, "icon", params.icon);
        settingMan.add(dbms, "id:" + optionsId, "validate", params.validate);
        for (Search s : params.getSearches()) {
            String searchID = settingMan.add(dbms, path, "search", "");
            settingMan.add(dbms, "id:" + searchID, "from", s.from);
            settingMan.add(dbms, "id:" + searchID, "until", s.until);
            settingMan.add(dbms, "id:" + searchID, "set", s.set);
            settingMan.add(dbms, "id:" + searchID, "prefix", s.prefix);
            settingMan.add(dbms, "id:" + searchID, "stylesheet", s.stylesheet);
        }
    }

    public AbstractParams getParams() {
        return params;
    }

    protected void doAddInfo(Element node) {
        if (result == null) return;
        Element info = node.getChild("info");
        Element res = new Element("result");
        add(res, "total", result.total);
        add(res, "added", result.added);
        add(res, "updated", result.updated);
        add(res, "unchanged", result.unchanged);
        add(res, "unknownSchema", result.unknownSchema);
        add(res, "removed", result.locallyRemoved);
        add(res, "unretrievable", result.unretrievable);
        add(res, "badFormat", result.badFormat);
        add(res, "doesNotValidate", result.doesNotValidate);
        info.addContent(res);
    }

    protected void doHarvest(Logger log, ResourceManager rm) throws Exception {
        Dbms dbms = (Dbms) rm.open(Geonet.Res.MAIN_DB);
        Harvester h = new Harvester(log, context, dbms, params);
        result = h.harvest();
    }

    private OaiPmhParams params;

    private OaiPmhResult result;
}

class OaiPmhResult {

    public int total;

    public int added;

    public int updated;

    public int unchanged;

    public int locallyRemoved;

    public int unknownSchema;

    public int unretrievable;

    public int badFormat;

    public int doesNotValidate;
}
