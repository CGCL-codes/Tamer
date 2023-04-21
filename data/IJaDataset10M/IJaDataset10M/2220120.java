package netgest.bo.xwc.components.classic.renderers;

import java.io.IOException;
import java.util.Date;
import netgest.bo.runtime.boObject;
import netgest.bo.runtime.boRuntimeException;
import netgest.bo.xwc.framework.XUIResponseWriter;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import netgest.bo.xwc.framework.localization.XUILocalizationUtils;
import netgest.bo.xwc.xeo.components.FormEdit;
import netgest.bo.xwc.xeo.localization.XEOViewersMessages;

/**
 * 
 * Renders the form edit component as XML
 * 
 * @author PedroRio
 *
 */
public class XMLFormEditRenderer extends XMLBasicRenderer {

    private boObject currentObject = null;

    public boObject getXEOObject() {
        return currentObject;
    }

    @Override
    public void encodeBegin(XUIComponentBase component) throws IOException {
        super.encodeBegin(component);
        XUIResponseWriter rw = getResponseWriter();
        FormEdit formEditCmp = (FormEdit) component;
        currentObject = formEditCmp.getTargetObject();
        rw.startElement("formEditObject", component);
        rw.writeAttribute("creationDate", getCreationDate(), "creationDate");
        rw.writeAttribute("creationDateLbl", XEOViewersMessages.SHW_PROPS_CREATION_DATE.toString(), "creationDateLbl");
        rw.writeAttribute("lastUpdateDate", getLastModificationDate(), "lastUpdateDate");
        rw.writeAttribute("lastUpdateDateLbl", XEOViewersMessages.SHW_PROPS_LAST_CHANGE_DATE.toString(), "lastUpdateDateLbl");
        rw.writeAttribute("version", getVersion(), "version");
        rw.writeAttribute("versionLbl", XEOViewersMessages.SHW_PROPS_VERSION.toString(), "versionLbl");
        rw.writeAttribute("boui", getBoui(), "boui");
        rw.writeAttribute("bouiLbl", XEOViewersMessages.SHW_PROPS_BOUI.toString(), "bouiLbl");
        rw.startElement("createUser", component);
        rw.writeAttribute("creatorLbl", XEOViewersMessages.SHW_PROPS_CREATED_BY.toString(), "creatorLbl");
        rw.write(getCreatedBy());
        rw.endElement("createUser");
        rw.startElement("lastUpdateUser", component);
        rw.writeAttribute("lastUpdateUserLbl", XEOViewersMessages.SHW_PROPS_LAST_CHANGE_BY.toString(), "lastUpdateUserLbl");
        rw.write(getLastModificationBy());
        rw.endElement("lastUpdateUser");
        rw.endElement("formEditObject");
    }

    /**
	 * Retrieves the creation date associated with the object binded to the form
	 * 
	 * @return A string with the creation date of object
	 */
    private String getCreationDate() {
        try {
            Date date = getXEOObject().getAttribute("SYS_DTCREATE").getValueDate();
            if (date != null) {
                return XUILocalizationUtils.dateTimeToString(date);
            }
        } catch (boRuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
	 * Retrieves the last modification date of the object
	 * 
	 * @return A string with the last modified date
	 */
    private String getLastModificationDate() {
        try {
            Date date = getXEOObject().getAttribute("SYS_DTSAVE").getValueDate();
            if (date != null) {
                return XUILocalizationUtils.dateTimeToString(date);
            }
        } catch (boRuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
	 * 
	 * Retrieve the user that made the last modification to the object
	 * 
	 * @return A string with the CardID of the user that modified the object
	 */
    private String getLastModificationBy() {
        try {
            if (getXEOObject().getDataSet().findColumn("SYS_USER") > 0) {
                String user = getXEOObject().getDataRow().getString("SYS_USER");
                if (user != null && !"0".equals(user)) {
                    return boObject.getBoManager().loadObject(getXEOObject().getEboContext(), Long.parseLong(user)).getCARDID().toString();
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (boRuntimeException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
	 * Retrieves the user that created this object
	 * 
	 * @return A string with the CARDID of the user
	 */
    private String getCreatedBy() {
        try {
            boObject creator = getXEOObject().getAttribute("CREATOR").getObject();
            if (creator != null) {
                return creator.getCARDID().toString();
            }
        } catch (boRuntimeException e) {
            e.printStackTrace();
        }
        return "n/a";
    }

    /**
	 * 
	 * Retrieves the version of the object
	 * 
	 * @return A string with the version of the object
	 */
    private String getVersion() {
        if (getXEOObject().getDataSet().findColumn("SYS_ICN") > 0) {
            long sys_icn = getXEOObject().getDataRow().getLong("SYS_ICN");
            return Long.toString(sys_icn);
        }
        return "0";
    }

    /**
	 * 
	 * Retrieves the BOUI of the Object
	 * 
	 * @return A string with the BOUI of the Object
	 */
    private String getBoui() {
        return Long.toString(getXEOObject().getBoui());
    }

    public void encodeEnd(XUIComponentBase component) throws IOException {
        super.encodeEnd(component);
    }
}
