package org.apache.jetspeed.modules.actions.portlets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.jetspeed.modules.actions.portlets.security.SecurityConstants;
import org.apache.jetspeed.om.BaseSecurityReference;
import org.apache.jetspeed.om.SecurityReference;
import org.apache.jetspeed.om.registry.CapabilityMap;
import org.apache.jetspeed.om.registry.ClientEntry;
import org.apache.jetspeed.om.registry.MediaTypeEntry;
import org.apache.jetspeed.om.registry.Parameter;
import org.apache.jetspeed.om.registry.PortletEntry;
import org.apache.jetspeed.om.registry.PortletInfoEntry;
import org.apache.jetspeed.om.registry.RegistryEntry;
import org.apache.jetspeed.om.registry.base.BaseCachedParameter;
import org.apache.jetspeed.om.registry.base.BaseParameter;
import org.apache.jetspeed.om.registry.base.BaseSecurity;
import org.apache.jetspeed.portal.portlets.VelocityPortlet;
import org.apache.jetspeed.services.Registry;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.util.template.JetspeedLink;
import org.apache.jetspeed.util.template.JetspeedLinkFactory;
import org.apache.turbine.util.DynamicURI;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.TurbineException;
import org.apache.turbine.util.security.EntityExistsException;
import org.apache.velocity.context.Context;

/**
 * An abstract base class with default actions for many of the common 
 * fields and parameters shared by the registry entries.  To add a new registry
 * update action, simply derive from this class and override the resetForm,
 * clearUserData, and updateRegistry functions.  If you need to provide more
 * actions that those that are provided, simply create them in your derived class.
 *
 * @author <a href="mailto:caius1440@hotmail.com">Jeremy Ford</a>
 * @version $Id: RegistryUpdateAction.java,v 1.10 2004/03/31 04:49:10 morciuch Exp $
 */
public abstract class RegistryUpdateAction extends SecureVelocityPortletAction {

    protected String registryEntryName = "";

    protected String registry = "";

    protected String pane = "";

    private static final String REASON = "reason";

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(RegistryUpdateAction.class.getName());

    protected void buildNormalContext(VelocityPortlet portlet, Context context, RunData rundata) throws Exception {
        String msgid = rundata.getParameters().getString(SecurityConstants.PARAM_MSGID);
        if (msgid != null) {
            int id = Integer.parseInt(msgid);
            if (id < SecurityConstants.MESSAGES.length) context.put(SecurityConstants.PARAM_MSG, SecurityConstants.MESSAGES[id]);
        }
        String mode = rundata.getParameters().getString(SecurityConstants.PARAM_MODE);
        context.put(SecurityConstants.PARAM_MODE, mode);
        String reason = rundata.getParameters().getString(REASON);
        context.put(REASON, reason);
    }

    /**
     * Insert a registry entry into the registry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doInsert(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            if (entryName == null || entryName.length() == 0) {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_INSERT, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
            } else {
                RegistryEntry existingEntry = Registry.getEntry(registry, entryName);
                if (existingEntry != null) {
                    throw new EntityExistsException("RegistryEntry: " + entryName + " Already Exists!");
                }
                RegistryEntry registryEntry = Registry.createEntry(registry);
                registryEntry.setName(entryName);
                updateRegistryEntry(rundata, registryEntry);
                Registry.addEntry(registry, registryEntry);
                clearUserData(rundata);
                rundata.getUser().setTemp(RegistryBrowseAction.PREFIX + registry + ":" + RegistryBrowseAction.REFRESH, Boolean.TRUE);
            }
        } catch (EntityExistsException e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_INSERT, SecurityConstants.MID_ENTITY_ALREADY_EXISTS);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error(this.getClass().getName() + ": Trying to create duplicate entry");
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_INSERT, SecurityConstants.MID_UPDATE_FAILED);
            duri = duri.addQueryData(REASON, e.getMessage());
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Update a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doUpdate(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            RegistryEntry registryEntry = Registry.getEntry(registry, entryName);
            if (registryEntry != null) {
                updateRegistryEntry(rundata, registryEntry);
                Registry.addEntry(registry, registryEntry);
                clearUserData(rundata);
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry for updating");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            duri = duri.addQueryData(REASON, e.getMessage());
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Delete a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doDelete(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            if (entryName == null || entryName.length() == 0) {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_DELETE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry for deleting");
            } else {
                Registry.removeEntry(registry, entryName);
                clearUserData(rundata);
                rundata.getUser().setTemp(RegistryBrowseAction.PREFIX + registry + ":" + RegistryBrowseAction.REFRESH, Boolean.TRUE);
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_DELETE, SecurityConstants.MID_DELETE_FAILED);
            duri = duri.addQueryData(REASON, e.getMessage());
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Cleanup method
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doCancel(RunData rundata, Context context) throws Exception {
        clearUserData(rundata);
    }

    /**
     * Basic implementation of a method to update a registry entry.  The fields that
     * are common to all registry entries can simply be added below.
     * @param rundata The turbine rundata context for this request.
     * @param registryEntry The registry entry to update
     */
    protected void updateRegistryEntry(RunData rundata, RegistryEntry registryEntry) throws Exception {
        String description = rundata.getParameters().getString("description");
        String title = rundata.getParameters().getString("title");
        Boolean hidden = rundata.getParameters().getBool("hidden");
        String securityRef = rundata.getParameters().getString("security_ref");
        SecurityReference security = registryEntry.getSecurityRef();
        String securityParent = null;
        if (security != null) {
            securityParent = security.getParent();
        }
        if (hasChanged(securityParent, securityRef)) {
            if (security == null) {
                security = new BaseSecurityReference();
            }
            security.setParent(securityRef);
            registryEntry.setSecurityRef(security);
        }
        if (hasChanged(registryEntry.getDescription(), description)) {
            registryEntry.setDescription(description);
        }
        if (hasChanged(String.valueOf(registryEntry.isHidden()), String.valueOf(hidden))) {
            registryEntry.setHidden(hidden.booleanValue());
        }
        if (hasChanged(registryEntry.getTitle(), title)) {
            registryEntry.setTitle(title);
        }
    }

    /**
     * Determines whether a field has changed value.  Used in update methods.
     * 
     * @param oldValue The original value
     * @param newValue The new value
     */
    protected boolean hasChanged(String oldValue, String newValue) {
        boolean result = false;
        if (oldValue == null && newValue == null) {
            result = false;
        } else if (oldValue == null && (newValue != null && newValue.length() == 0)) {
            result = false;
        } else if (oldValue == null && (newValue != null)) {
            result = true;
        } else if (oldValue != null && newValue == null) {
            result = true;
        } else if (!oldValue.equals(newValue)) {
            result = true;
        }
        return result;
    }

    /**
     * Add a parameter to a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doAddparameter(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry regEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (regEntry != null) {
                String parameterName = rundata.getParameters().getString("parameter_name");
                if (parameterName != null && parameterName.length() > 0) {
                    Parameter parameter = null;
                    if (regEntry instanceof PortletEntry) {
                        parameter = new BaseCachedParameter();
                        boolean isCachedOnName = rundata.getParameters().getBoolean("cached_on_name", false);
                        boolean isCachedOnValue = rundata.getParameters().getBoolean("cached_on_value", false);
                        ((BaseCachedParameter) parameter).setCachedOnName(isCachedOnName);
                        ((BaseCachedParameter) parameter).setCachedOnValue(isCachedOnValue);
                    } else {
                        parameter = new BaseParameter();
                    }
                    String parameterValue = rundata.getParameters().getString("parameter_value");
                    boolean isHidden = rundata.getParameters().getBoolean("is_hidden", false);
                    String description = rundata.getParameters().getString("description");
                    String title = rundata.getParameters().getString("title");
                    String type = rundata.getParameters().getString("type");
                    parameter.setName(parameterName);
                    parameter.setValue(parameterValue);
                    parameter.setHidden(isHidden);
                    parameter.setDescription(description);
                    parameter.setTitle(title);
                    parameter.setType(type);
                    String securityRole = rundata.getParameters().getString("security_role");
                    String securityRef = rundata.getParameters().getString("security_ref");
                    if (securityRole != null && securityRole.length() > 0) {
                        BaseSecurity paramSecurity = new BaseSecurity();
                        paramSecurity.setRole(securityRole);
                        parameter.setSecurity(paramSecurity);
                    }
                    if (securityRef != null && securityRef.length() > 0) {
                        BaseSecurityReference paramSecurityRef = new BaseSecurityReference();
                        paramSecurityRef.setParent(securityRef);
                        parameter.setSecurityRef(paramSecurityRef);
                    }
                    regEntry.addParameter(parameter);
                    Registry.addEntry(registry, regEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to add a parameter");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Update parameters of a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doUpdateparameters(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry regEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (regEntry != null) {
                String[] parameters = rundata.getParameters().getStrings("update_parameter_name");
                if (parameters != null && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        String parameterName = parameters[i];
                        Parameter parameter = regEntry.getParameter(parameterName);
                        if (regEntry instanceof PortletEntry) {
                            if (parameter == null) {
                                parameter = new BaseCachedParameter();
                                regEntry.addParameter(parameter);
                            }
                            boolean isCachedOnName = rundata.getParameters().getBoolean(parameterName + ".cached_on_name", false);
                            boolean isCachedOnValue = rundata.getParameters().getBoolean(parameterName + ".cached_on_value", false);
                            ((BaseCachedParameter) parameter).setCachedOnName(isCachedOnName);
                            ((BaseCachedParameter) parameter).setCachedOnValue(isCachedOnValue);
                        } else if (parameter == null) {
                            parameter = new BaseParameter();
                            regEntry.addParameter(parameter);
                        }
                        String parameterValue = rundata.getParameters().getString(parameterName + ".parameter_value");
                        String description = rundata.getParameters().getString(parameterName + ".description");
                        String title = rundata.getParameters().getString(parameterName + ".title");
                        String securityRole = rundata.getParameters().getString(parameterName + ".security_role");
                        String securityRef = rundata.getParameters().getString(parameterName + ".security_ref");
                        String type = rundata.getParameters().getString(parameterName + ".type");
                        boolean isHidden = rundata.getParameters().getBoolean(parameterName + ".is_hidden", false);
                        parameter.setName(parameterName);
                        parameter.setValue(parameterValue);
                        parameter.setHidden(isHidden);
                        parameter.setDescription(description);
                        parameter.setTitle(title);
                        parameter.setType(type);
                        if (securityRef != null && securityRef.length() > 0) {
                            BaseSecurityReference paramSecurityRef = new BaseSecurityReference();
                            paramSecurityRef.setParent(securityRef);
                            parameter.setSecurityRef(paramSecurityRef);
                        }
                    }
                    Registry.addEntry(registry, regEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to update parameters");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Update parameter's values of a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doUpdateparametervalues(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry regEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (regEntry != null) {
                String[] parameters = rundata.getParameters().getStrings("update_parameter_name");
                if (parameters != null && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        String parameterName = parameters[i];
                        String parameterValue = rundata.getParameters().getString(parameterName + ".parameter_value");
                        Parameter parameter = regEntry.getParameter(parameterName);
                        if (parameter == null) {
                            if (regEntry instanceof PortletEntry) {
                                parameter = new BaseCachedParameter();
                            } else {
                                parameter = new BaseParameter();
                            }
                            parameter.setName(parameterName);
                            regEntry.addParameter(parameter);
                        }
                        if (parameter != null) {
                            parameter.setValue(parameterValue);
                        }
                    }
                    Registry.addEntry(registry, regEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to update parameters");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Remove parameters from a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doRemoveparameters(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry portletEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (portletEntry != null) {
                String[] parameters = rundata.getParameters().getStrings("parameter_name");
                if (parameters != null && parameters.length > 0) {
                    for (int i = 0; i < parameters.length; i++) {
                        String parameterName = parameters[i];
                        portletEntry.removeParameter(parameterName);
                    }
                    Registry.addEntry(registry, portletEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to remove parameters");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Add a media type to a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doAddmediatype(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry portletEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (portletEntry != null) {
                String mediaType = rundata.getParameters().getString("media_type");
                if (mediaType != null && mediaType.length() > 0) {
                    portletEntry.addMediaType(mediaType);
                    Registry.addEntry(registry, portletEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to add media type");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Remove media types from a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doRemovemediatypes(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            PortletInfoEntry portletEntry = (PortletInfoEntry) Registry.getEntry(registry, entryName);
            if (portletEntry != null) {
                String[] mediaTypes = rundata.getParameters().getStrings("media_type");
                if (mediaTypes != null && mediaTypes.length > 0) {
                    for (int i = 0; i < mediaTypes.length; i++) {
                        String mediaType = mediaTypes[i];
                        portletEntry.removeMediaType(mediaType);
                    }
                    Registry.addEntry(registry, portletEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_MISSING_PARAMETER);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to remove media types");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Add capabilities to a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doAddcapability(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            RegistryEntry regEntry = Registry.getEntry(registry, entryName);
            if (regEntry != null) {
                CapabilityMap cm = null;
                if (regEntry instanceof MediaTypeEntry) {
                    MediaTypeEntry mediaTypeEntry = (MediaTypeEntry) regEntry;
                    cm = mediaTypeEntry.getCapabilityMap();
                } else if (regEntry instanceof ClientEntry) {
                    ClientEntry clientEntry = (ClientEntry) regEntry;
                    cm = clientEntry.getCapabilityMap();
                } else {
                }
                if (cm != null) {
                    String[] capabilities = rundata.getParameters().getStrings("capability");
                    if (capabilities != null && capabilities.length > 0) {
                        for (int i = 0; i < capabilities.length; i++) {
                            String capability = capabilities[i];
                            if (cm.contains(capability)) {
                            } else {
                                if (capability != null && capability.length() > 0) {
                                    cm.addCapability(capability);
                                }
                            }
                        }
                    }
                    Registry.addEntry(registry, regEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to add capabilities");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Remove capabilites from a registry entry
     * @param rundata The turbine rundata context for this request.
     * @param context The velocity context for this request.
     * @throws Exception
     */
    public void doRemovecapability(RunData rundata, Context context) throws Exception {
        try {
            String entryName = rundata.getParameters().getString(registryEntryName);
            RegistryEntry regEntry = Registry.getEntry(registry, entryName);
            if (regEntry != null) {
                CapabilityMap cm = null;
                if (regEntry instanceof MediaTypeEntry) {
                    MediaTypeEntry mediaTypeEntry = (MediaTypeEntry) regEntry;
                    cm = mediaTypeEntry.getCapabilityMap();
                } else if (regEntry instanceof ClientEntry) {
                    ClientEntry clientEntry = (ClientEntry) regEntry;
                    cm = clientEntry.getCapabilityMap();
                } else {
                }
                if (cm != null) {
                    String[] capabilities = rundata.getParameters().getStrings("capability");
                    if (capabilities != null && capabilities.length > 0) {
                        for (int i = 0; i < capabilities.length; i++) {
                            String capability = capabilities[i];
                            cm.removeCapability(capability);
                        }
                        Registry.addEntry(registry, regEntry);
                        clearUserData(rundata);
                    }
                    Registry.addEntry(registry, regEntry);
                    clearUserData(rundata);
                } else {
                    DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
                    rundata.setRedirectURI(duri.toString());
                    resetForm(rundata);
                }
            } else {
                DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_INVALID_ENTITY_NAME);
                rundata.setRedirectURI(duri.toString());
                resetForm(rundata);
                logger.error(this.getClass().getName() + ": Failed to find registry entry while trying to remove capabilities");
            }
        } catch (Exception e) {
            DynamicURI duri = redirect(rundata, SecurityConstants.PARAM_MODE_UPDATE, SecurityConstants.MID_UPDATE_FAILED);
            rundata.setRedirectURI(duri.toString());
            resetForm(rundata);
            logger.error("Exception", e);
        }
    }

    /**
     * Method that sets up a redirect link given the rundata, the mode, and a reason
     * @param rundata
     * @param mode
     * @param reason
     * @return
     * @throws TurbineException
     */
    protected DynamicURI redirect(RunData rundata, String mode, int reason) throws TurbineException {
        JetspeedLink link = JetspeedLinkFactory.getInstance(rundata);
        DynamicURI duri = link.getPaneByName(pane).addPathInfo(SecurityConstants.PARAM_MODE, mode).addPathInfo(SecurityConstants.PARAM_MSGID, reason);
        String entryName = rundata.getParameters().getString(registryEntryName);
        if (entryName != null && entryName.length() > 0) {
            duri.addQueryData(registryEntryName, entryName);
        }
        JetspeedLinkFactory.putInstance(link);
        return duri;
    }

    /**
     * Remove any data that was added to the user's temporary storage
     * @param rundata
     */
    protected void clearUserData(RunData rundata) {
        rundata.getUser().removeTemp(registryEntryName);
        rundata.getUser().removeTemp("title");
        rundata.getUser().removeTemp("description");
    }

    /**
     * Method to reset data entered into the forms
     * @param rundata
     */
    protected void resetForm(RunData rundata) {
        String entryName = rundata.getParameters().getString(registryEntryName);
        String title = rundata.getParameters().getString("title");
        String description = rundata.getParameters().getString("description");
        rundata.getUser().setTemp(registryEntryName, entryName);
        rundata.getUser().setTemp("title", title);
        rundata.getUser().setTemp("description", description);
    }

    /**
     * Turns an iterator into a collection
     * 
     * @param iter An iterator
     * @return the collection
     */
    protected Collection iteratorToCollection(Iterator iter) {
        Collection collection = new ArrayList();
        while (iter.hasNext()) {
            collection.add(iter.next());
        }
        return collection;
    }
}
