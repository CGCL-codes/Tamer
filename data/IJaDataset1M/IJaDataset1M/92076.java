package org.eun.oai.server.crosswalk;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eun.oai.server.util.IloxCatalogUtil;
import ORG.oclc.oai.server.crosswalk.Crosswalk;
import ORG.oclc.oai.server.verb.CannotDisseminateFormatException;
import ORG.oclc.oai.server.verb.OAIInternalServerError;

public class Ilox2oai_ilox extends Crosswalk {

    public static String ILOX_LOCATION = "http://www.imsglobal.org/xsd/lodeilox_v1p0/digital_LO http://www.imsglobal.org/xsd/imsloilox_v1p0_ilox_work_v1p0.xsd";

    private IloxCatalogUtil crosswalkUtil;

    public Ilox2oai_ilox(Properties properties) throws OAIInternalServerError {
        super(ILOX_LOCATION);
        try {
            crosswalkUtil = IloxCatalogUtil.getInstance(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.getAnonymousLogger().log(Level.INFO, "An Ilox2oai_ilox object is created!");
    }

    @Override
    public String createMetadata(Object nativeItem) throws CannotDisseminateFormatException {
        return crosswalkUtil.createMetadata(nativeItem);
    }

    @Override
    public boolean isAvailableFor(Object nativeItem) {
        return true;
    }
}
