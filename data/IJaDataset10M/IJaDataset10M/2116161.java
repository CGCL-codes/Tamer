package org.eaasyst.eaa.apps.oe;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.apps.EditApplicationBase;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.OptionListSource;
import org.eaasyst.eaa.data.OptionListSourceFactory;
import org.eaasyst.eaa.data.RecordSet;
import org.eaasyst.eaa.data.impl.SecurityConfigurationDabFactory;
import org.eaasyst.eaa.data.impl.SecurityConfigurationFactory;
import org.eaasyst.eaa.forms.SecConfigEditForm;
import org.eaasyst.eaa.security.UserProfileManager;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.SecurityConfigurationData;
import org.eaasyst.eaa.syst.data.transients.Event;
import org.eaasyst.eaa.utils.StringUtils;
import org.eaasyst.people.data.impl.PersonConfigurationFactory;
import org.eaasyst.tables.data.impl.TableOptionListSourceFactory;

/**
 * <p>This application updates the security configuration information.</p>
 *
 * @version 2.9
 * @author Jeff Chilton
 */
public class SecConfigEdit extends EditApplicationBase {

    /**
	 * <p>Constructs a new "SecConfigEdit" object.</p>
	 *
	 * @since Eaasy Street 2.0
	 */
    public SecConfigEdit() {
        super();
        className = StringUtils.computeClassName(getClass());
        setFormName("secConfigEditForm");
        setFormType("org.eaasyst.eaa.forms.SecConfigEditForm");
        setApplTitleKey("title.security.configuration.edit");
        setViewComponent("secedit.jsp");
        setDefaultRecordKey(new Integer(0));
        setKeyFieldName("id");
        setOnSubmit("return validateSecConfigEditForm(this)");
        setAccessBeanFactory(new SecurityConfigurationDabFactory(EaasyStreet.getProperty(Constants.CFG_SEC_CONFIG_STRING)));
    }

    /**
	 * Called by the <code>Controller</code> whenever the application is
	 * invoked through either navigation or an application request.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.0
	 */
    public void initialize(HttpServletRequest req) {
        EaasyStreet.logTrace("[In] SecConfigEdit.initialize()");
        if ("database".equalsIgnoreCase(EaasyStreet.getProperty(Constants.CFG_SEC_CONFIG_SOURCE))) {
            super.initialize(req);
        } else {
            req.setAttribute(Constants.RAK_SYSTEM_ACTION, Constants.SYSTEM_ACTION_BACK);
            EaasyStreet.handleSafeEvent(req, new Event(Constants.EAA0034I));
        }
        EaasyStreet.logTrace("[Out] SecConfigEdit.initialize()");
    }

    /**
	 * Called by the <code>Controller</code> to obtain unformatted
	 * application results.
	 *
	 * @param	req	the <code>HttpServletRequest</code> object
	 * @since Eaasy Street 2.0.4
	 */
    public void prepareOutput(HttpServletRequest req) {
        EaasyStreet.logTrace("[In] SecConfigEdit.prepareOutput()");
        super.prepareOutput(req);
        OptionListSourceFactory factory = new TableOptionListSourceFactory();
        OptionListSource ols = factory.getOptionListSource("Boolean");
        if (ols != null) {
            List booleanOptions = ols.getOptions();
            req.setAttribute("booleanOptions", booleanOptions);
        }
        req.setAttribute("config", SecurityConfigurationFactory.getConfiguration());
        req.setAttribute("persConfig", PersonConfigurationFactory.getConfiguration());
        EaasyStreet.logTrace("[Out] SecConfigEdit.prepareOutput()");
    }

    /**
	 * Returns a HashMap object containing the required parameters for
	 * the data access bean update/insert command.
	 *
	 * @param req the <code>HttpServletRequest</code> object
	 * @return the data access bean update parameters
	 * @since Eaasy Street 2.0.1
	 */
    protected Map getUpdateParameters(HttpServletRequest req, ActionForm actionForm) {
        Map parameters = new HashMap();
        HttpSession ses = req.getSession();
        SecConfigEditForm form = (SecConfigEditForm) actionForm;
        SecurityConfigurationData configData = null;
        try {
            configData = (SecurityConfigurationData) ses.getAttribute("rawData");
            configData.setUserProfileDabFactoryName(form.getUserProfileDabFactoryName());
            configData.setPasswordEncrypterName(form.getPasswordEncrypterName());
            configData.setMaxLogonAttempts(StringUtils.intValue(form.getMaxLogonAttempts()));
            configData.setPasswordManageable(StringUtils.booleanValue(form.getPasswordManageable()));
            configData.setPasswordExpireDays(StringUtils.intValue(form.getPasswordExpireDays()));
            configData.setMinPasswordLength(StringUtils.intValue(form.getMinPasswordLength()));
            configData.setMaxPasswordLength(StringUtils.intValue(form.getMaxPasswordLength()));
            configData.setUpperCaseRequired(StringUtils.booleanValue(form.getUpperCaseRequired()));
            configData.setLowerCaseRequired(StringUtils.booleanValue(form.getLowerCaseRequired()));
            configData.setNumericRequired(StringUtils.booleanValue(form.getNumericRequired()));
            configData.setSpecialCharRequired(StringUtils.booleanValue(form.getSpecialCharRequired()));
            configData.setValidSpecialCharacters(form.getValidSpecialCharacters());
            configData.setMaxPreviousPasswords(StringUtils.intValue(form.getMaxPreviousPasswords()));
            configData.setPromptQuestionsUsed(StringUtils.intValue(form.getPromptQuestionsUsed()));
            configData.setAvailablePromptQuestions(form.getAvailablePromptQuestions());
            configData.setContactIds(StringUtils.join(form.getContactIdList(), Constants.LF));
            configData.setPersonAttributeNames(StringUtils.join(form.getPersonAttributeList(), Constants.LF));
            configData.setUserAttributeNames(form.getUserAttributeNames());
            configData.setListAttributeNames(form.getListAttributeNames());
            configData.setBrowseAttributeNames(form.getBrowseAttributeNames());
            configData.setEditAttributeNames(form.getEditAttributeNames());
            configData.setLastUpdate(new Date());
            configData.setLastUpdateBy(UserProfileManager.getUserId());
            parameters.put(DataConnector.RECORD_PARAMETER, configData);
            parameters.put(DataConnector.RECORD_KEY_PARAMETER, getDefaultRecordKey());
        } catch (ClassCastException e) {
            try {
                List fieldList = new ArrayList();
                fieldList.add(form.getUserProfileDabFactoryName());
                fieldList.add(form.getPasswordEncrypterName());
                fieldList.add("" + StringUtils.intValue(form.getMaxLogonAttempts()));
                fieldList.add("" + StringUtils.nuleanValue(form.getPasswordManageable()));
                fieldList.add("" + StringUtils.intValue(form.getPasswordExpireDays()));
                fieldList.add("" + StringUtils.intValue(form.getMinPasswordLength()));
                fieldList.add("" + StringUtils.intValue(form.getMaxPasswordLength()));
                fieldList.add("" + StringUtils.nuleanValue(form.getUpperCaseRequired()));
                fieldList.add("" + StringUtils.nuleanValue(form.getLowerCaseRequired()));
                fieldList.add("" + StringUtils.nuleanValue(form.getNumericRequired()));
                fieldList.add("" + StringUtils.nuleanValue(form.getSpecialCharRequired()));
                fieldList.add(form.getValidSpecialCharacters());
                fieldList.add("" + StringUtils.intValue(form.getMaxPreviousPasswords()));
                fieldList.add("" + StringUtils.intValue(form.getPromptQuestionsUsed()));
                fieldList.add(form.getAvailablePromptQuestions());
                fieldList.add(StringUtils.join(form.getContactIdList(), Constants.LF));
                fieldList.add(StringUtils.join(form.getPersonAttributeList(), Constants.LF));
                fieldList.add(form.getUserAttributeNames());
                fieldList.add(form.getListAttributeNames());
                fieldList.add(form.getBrowseAttributeNames());
                fieldList.add(form.getEditAttributeNames());
                fieldList.add(UserProfileManager.getUserId());
                parameters.put(DataConnector.RECORD_PARAMETER, fieldList);
                parameters.put(DataConnector.RECORD_KEY_PARAMETER, getDefaultRecordKey());
            } catch (ClassCastException e1) {
                EaasyStreet.logError("Error posting results", e1);
            }
        }
        return parameters;
    }

    /**
	 * <p>Returns a blank Struts <code>DynaActionForm</code> configured for
	 * this application.</p>
	 *
	 * @return a blank Struts <code>DynaActionForm</code> configured for
	 * this application
	 * @since Eaasy Street 2.0.4
	 */
    public ActionForm getBlankForm() {
        return new SecConfigEditForm();
    }

    /**
	 * Returns a populated Struts <code>ActionForm</code>.
	 * 
	 * @return a populated Struts <code>ActionForm</code>
	 * @since Eaasy Street 2.0.4
	 */
    protected ActionForm populateActionForm(Object rawData) {
        SecurityConfigurationData configData = null;
        try {
            configData = (SecurityConfigurationData) rawData;
        } catch (Throwable t) {
            try {
                configData = new SecurityConfigurationData();
                RecordSet rs = (RecordSet) rawData;
                configData = new SecurityConfigurationData(rs.getDynaActionForm(getFormName(), 1));
            } catch (Throwable t1) {
                EaasyStreet.logError("Error populating form", t1);
            }
        }
        EaasyStreet.getServletRequest().getSession().setAttribute("rawData", rawData);
        return new SecConfigEditForm(configData);
    }
}
