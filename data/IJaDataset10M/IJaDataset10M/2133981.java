package org.monet.backoffice.presentation.user.views;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import org.monet.backoffice.configuration.Configuration;
import org.monet.backoffice.control.constants.Actions;
import org.monet.backoffice.core.constants.Tags;
import org.monet.backoffice.core.model.Language;
import org.monet.backoffice.presentation.user.agents.AgentRender;
import org.monet.backoffice.presentation.user.agents.AgentTemplates;
import org.monet.backoffice.presentation.user.constants.ViewTag;
import org.monet.backoffice.presentation.user.constants.ViewType;
import org.monet.backoffice.presentation.user.util.Context;
import org.monet.kernel.agents.AgentLogger;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.library.LibraryDate;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.User;

public abstract class View {

    private String sTitle;

    protected Context context;

    protected User oUser;

    protected String mode;

    protected String sTemplateParameters;

    protected HashMap<String, String> hmTemplateParameters;

    protected HashMap<String, String> hmParameters;

    protected String type;

    protected Object target;

    protected AgentTemplates agentTemplates;

    protected AgentRender agentRender;

    protected AgentLogger agentLogger;

    protected String codeLanguage;

    protected ViewsFactory viewsFactory;

    public View(Context oContext, AgentRender oAgentRender, String codeLanguage) {
        this.sTitle = Strings.EMPTY;
        this.context = new Context();
        this.oUser = null;
        this.mode = null;
        this.sTemplateParameters = null;
        this.hmTemplateParameters = new HashMap<String, String>();
        this.hmParameters = new HashMap<String, String>();
        this.type = ViewType.BROWSE;
        this.target = null;
        this.agentTemplates = AgentTemplates.getInstance();
        this.agentRender = oAgentRender;
        this.agentLogger = AgentLogger.getInstance();
        this.codeLanguage = codeLanguage;
        this.viewsFactory = ViewsFactory.getInstance();
    }

    private Boolean putLanguageTags(Properties propTags) {
        Enumeration<?> ePropertyNames = propTags.propertyNames();
        String code;
        while (ePropertyNames.hasMoreElements()) {
            code = (String) ePropertyNames.nextElement();
            this.context.put(code, propTags.getProperty(code));
        }
        return true;
    }

    private Boolean createCommonTags() {
        Configuration oConfiguration = Configuration.getInstance();
        org.monet.kernel.configuration.Configuration monetConfiguration = org.monet.kernel.configuration.Configuration.getInstance();
        org.monet.backoffice.configuration.Configuration backofficeConfiguration = org.monet.backoffice.configuration.Configuration.getInstance();
        Language language = Language.getInstance();
        this.putLanguageTags(language.getLabels());
        this.putLanguageTags(language.getMessages());
        this.putLanguageTags(language.getErrorMessages());
        this.context.put(Tags.URL, oConfiguration.getUrl());
        this.context.put(Tags.PORT, String.valueOf(oConfiguration.getPort()));
        this.context.put(Tags.API_URL, oConfiguration.getApiUrl());
        this.context.put(Tags.PUSH_API_URL, oConfiguration.getPushApiUrl());
        this.context.put(Tags.PUSH_ENABLED, oConfiguration.isPushEnable());
        this.context.put(Tags.API_PORT, String.valueOf(oConfiguration.getApiPort()));
        this.context.put(Tags.IMAGES_PATH, oConfiguration.getImagesPath());
        this.context.put(Tags.APPLICATION_BACKOFFICE_RESOURCE_URL, backofficeConfiguration.getUrl());
        this.context.put(Tags.APPLICATION_FMS_URL, backofficeConfiguration.getFmsServletUrl());
        this.context.put(Tags.LANGUAGES_URL, oConfiguration.getLanguagesUrl());
        this.context.put(Tags.JAVASCRIPT_URL, oConfiguration.getJavascriptUrl());
        this.context.put(Tags.STYLES_URL, oConfiguration.getStylesUrl());
        this.context.put(Tags.ENTERPRISE_LOGIN_URL, String.valueOf(oConfiguration.getEnterpriseLoginUrl()));
        this.context.put(Tags.ACTION_DOLOGIN, Actions.LOGIN);
        this.context.put(Tags.ACTION_DOLOGOUT, Actions.LOGOUT);
        this.context.put(Tags.EXTENSION_THESAURUS_SEPARATOR, monetConfiguration.getValue(org.monet.kernel.configuration.Configuration.BUSINESS_MODEL_THESAURUS_SEPARATOR));
        this.context.put(Tags.ENCRIPT_DATA, monetConfiguration.encriptParameters());
        this.context.put(Tags.TEST_CASE, "false");
        return true;
    }

    private Boolean generateTemplateParameters() {
        String[] aParameters;
        Integer iPos = 0;
        if (this.sTemplateParameters == null) return true;
        aParameters = this.sTemplateParameters.split(Strings.AMPERSAND);
        this.hmTemplateParameters.clear();
        for (iPos = 0; iPos < aParameters.length; iPos++) {
            String[] aParameter = aParameters[iPos].split(Strings.EQUAL);
            if (aParameter.length < 2) continue;
            if ((aParameter[0] != null) && (aParameter[1] != null)) {
                this.hmTemplateParameters.put(aParameter[0], aParameter[1]);
            }
        }
        return true;
    }

    private Boolean printParameter(String sName, String sValue) {
        Boolean bIsInteger;
        Integer iValue = 0;
        if (sValue == null) return true;
        bIsInteger = false;
        if (Character.isDigit(sValue.charAt(0))) {
            try {
                iValue = Integer.valueOf(sValue);
                bIsInteger = true;
            } catch (NumberFormatException ex) {
            }
        }
        if (bIsInteger) this.context.put(sName, iValue); else this.context.put(sName, sValue);
        return true;
    }

    private Boolean printParameters() {
        Iterator<String> iter;
        iter = this.hmTemplateParameters.keySet().iterator();
        while (iter.hasNext()) {
            String sName = iter.next();
            this.printParameter(sName, this.hmTemplateParameters.get(sName));
        }
        iter = this.hmParameters.keySet().iterator();
        while (iter.hasNext()) {
            String sName = iter.next();
            this.printParameter(sName, this.hmParameters.get(sName));
        }
        return true;
    }

    protected String getTemplateFilename(String code) {
        BusinessUnit oBusinessUnit = BusinessUnit.getInstance();
        String sTemplateFilename = oBusinessUnit.getTemplateFilename(code, this.type, this.mode);
        return sTemplateFilename;
    }

    public String getTitle() {
        return this.sTitle;
    }

    public Boolean setTitle(String sTitle) {
        this.sTitle = sTitle;
        return true;
    }

    public Boolean setUser(User oUser) {
        this.oUser = oUser;
        return true;
    }

    public Boolean setMode(String sMode) {
        Integer iPos = sMode.lastIndexOf(Strings.QUESTION);
        if (iPos == -1) iPos = sMode.length();
        this.mode = sMode.substring(0, iPos);
        if (iPos < sMode.length()) this.sTemplateParameters = sMode.substring(iPos + 1);
        return true;
    }

    public Boolean setType(String sType) {
        this.type = sType;
        return true;
    }

    public Object getTarget() {
        return target;
    }

    public Boolean setTarget(Object oTarget) {
        this.target = oTarget;
        return true;
    }

    public Boolean addParameter(String sName, String sValue) {
        this.hmParameters.put(sName, sValue);
        return true;
    }

    public String execute() {
        StringWriter writer = new StringWriter();
        this.execute(writer);
        return writer.toString();
    }

    public void execute(Writer writer) {
        BusinessUnit oBusinessUnit = BusinessUnit.getInstance();
        if (this.oUser != null) this.context.put(ViewTag.USER, this.oUser);
        this.context.put(ViewTag.LANGUAGE, this.codeLanguage);
        this.context.put(ViewTag.DATETIME_FORMAT_TEXT, LibraryDate.Format.TEXT);
        this.context.put(ViewTag.DATETIME_FORMAT_NUMERIC, LibraryDate.Format.NUMERIC);
        this.context.put(ViewTag.RENDER, this.agentRender);
        if (oBusinessUnit != null) this.context.put(Tags.BUSINESSUNIT, oBusinessUnit);
        this.createCommonTags();
        this.generateTemplateParameters();
        this.printParameters();
    }
}
