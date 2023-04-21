package org.fao.geonet.services.reusable;

import jeeves.resources.dbms.Dbms;
import org.fao.geonet.kernel.setting.SettingManager;
import org.fao.geonet.kernel.thesaurus.ThesaurusManager;
import org.fao.geonet.services.extent.ExtentManager;
import org.fao.geonet.services.reusable.log.ReusableObjectLogger;
import org.jdom.Element;

public class ProcessParams {

    public final Dbms dbms;

    public final ReusableObjectLogger logger;

    public final Element elementToProcess;

    public final Element metadata;

    public final ThesaurusManager thesaurusManager;

    public final ExtentManager extentManager;

    public final String baseURL;

    public final String metadataId;

    public final boolean addOnly;

    public ProcessParams(Dbms dbms, ReusableObjectLogger logger, String metadataId, Element elementToProcess, Element metadata, ThesaurusManager thesaurusManager, ExtentManager extentManager, String baseURL, SettingManager settingMan, boolean addOnly) {
        this.dbms = dbms;
        this.logger = logger;
        this.elementToProcess = elementToProcess;
        this.metadata = metadata;
        this.thesaurusManager = thesaurusManager;
        this.extentManager = extentManager;
        this.baseURL = Utils.mkBaseURL(baseURL, settingMan);
        this.addOnly = addOnly;
        this.metadataId = metadataId;
    }

    public ProcessParams(Dbms dbms, String metadataId, Element elementToProcess, Element metadata, ThesaurusManager thesaurusManager, ExtentManager extentManager, SettingManager settingMan, String baseURL, boolean addOnly) {
        this(dbms, ReusableObjectLogger.THREAD_SAFE_LOGGER, metadataId, elementToProcess, metadata, thesaurusManager, extentManager, baseURL, settingMan, addOnly);
    }

    public ProcessParams(Dbms dbms, String metadataId, Element elementToProcess, Element metadata, ThesaurusManager thesaurusManager, ExtentManager extentManager, SettingManager settingMan, String baseURL) {
        this(dbms, ReusableObjectLogger.THREAD_SAFE_LOGGER, metadataId, elementToProcess, metadata, thesaurusManager, extentManager, baseURL, settingMan, false);
    }
}
