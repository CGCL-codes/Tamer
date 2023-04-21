package org.escapek.client.repository.creationJobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.escapek.client.ClientPlugin;
import org.escapek.client.tools.StringTools;
import org.escapek.core.dto.cmdb.definitions.CIClassDTO;
import org.escapek.core.dto.cmdb.definitions.CIPropertyDefDTO;
import org.escapek.core.dto.cmdb.definitions.PropertyTypeDTO;
import org.escapek.core.dto.cmdb.definitions.RelationClassDTO;
import org.escapek.core.dto.cmdb.definitions.RelationPropertyDefDTO;
import org.escapek.core.dto.cmdb.definitions.RelationTypeDTO;
import org.escapek.core.dto.security.TicketDTO;
import org.escapek.core.serviceManager.ServiceWrapper;
import org.escapek.core.serviceManager.Exceptions.ConnectionException;
import org.escapek.core.services.interfaces.IRemoteCmdbService;
import org.escapek.i18n.LocaleService;
import org.escapek.logger.LoggerPlugin;
import org.escapek.logger.LoggingConstants;

/**
 * This job is responsible of miscellanous creation in the repository. It is called once core initialization
 * has been successfully done.
 * @author nicolasjouanin
 *
 */
public class CMDBJob extends EscapeKJob {

    private IExtensionRegistry registry;

    private static final String CMDB_CONTRIB_EXT_POINT = "org.escapek.client.cmdb.cmdbContributions";

    private static final String ATT_NAME = "name";

    private static final String ATT_DISPLAY_NAME = "displayName";

    private static final String ATT_DESCRIPTION = "description";

    private static final String ATT_PARENT_CLASS = "parentClass";

    private static final String ATT_IS_ABSTRACT = "isAbstract";

    private static final String ATT_TYPE_NAME = "typeName";

    private static final String ATT_JAVA_TYPE = "javaType";

    private static final String ATT_DTO_TYPE = "dtoType";

    private static final String ATT_PROP_DISPLAY_NAME = "displayName";

    private static final String ATT_PROP_DESCRIPTION = "description";

    private static final String ATT_PROPERTY_DEF_NAME = "name";

    private static final String ATT_PROPERTY_DEF_DISPLAYNAME = "displayName";

    private static final String ATT_PROPERTY_DEF_DESCRIPTION = "description";

    private static final String ATT_PROPERTY_DEF_TYPE = "type";

    private static final String ATT_PROPERTY_DEF_IS_MANDATORY = "isMandatory";

    private static final String ATT_PROPERTY_DEF_DEFAULTVALUE = "defaultValue";

    private static final String ATT_RELATION_TYPE = "relationType";

    private static final String ATT_RELATION_SOURCE_CLASS = "sourceClass";

    private static final String ATT_RELATION_TARGET_CLASS = "targetClass";

    private static final String ATT_RELATION_CLASS_NAME = "className";

    private static final String ATT_RELATION_CLASS_ROLE_DISPLAYNAME = "roleDisplayName";

    private static final String ATT_RELATION_CLASS_ROLE_DESCRIPTION = "roleDescription";

    private static final String ATT_RELATION_CLASS_MIN_CARDINALITY = "minCardinality";

    private static final String ATT_RELATION_CLASS_MAX_CARDINALITY = "maxCardinality";

    private ArrayList<IConfigurationElement> propertyTypes;

    private ArrayList<IConfigurationElement> ciClasses;

    private ArrayList<IConfigurationElement> relationClasses;

    private ArrayList<IConfigurationElement> relationTypes;

    public CMDBJob(String name) {
        super(name);
        messages = LocaleService.getMessageService(LocaleService.repositoryCreationMessages);
        registry = Platform.getExtensionRegistry();
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        try {
            HashMap<String, PropertyTypeDTO> propertyTypesCache = new HashMap<String, PropertyTypeDTO>();
            HashMap<String, RelationTypeDTO> relationTypesCache = new HashMap<String, RelationTypeDTO>();
            propertyTypes = new ArrayList<IConfigurationElement>();
            relationTypes = new ArrayList<IConfigurationElement>();
            ciClasses = new ArrayList<IConfigurationElement>();
            relationClasses = new ArrayList<IConfigurationElement>();
            IRemoteCmdbService cmdbService = ServiceWrapper.getInstance().getCmdbService();
            TicketDTO ticket = ServiceWrapper.getInstance().getTicket();
            readContributions();
            monitor.beginTask(messages.getString("cmdbJob.propertyTypeCreation.taskName"), propertyTypes.size());
            output.append(messages.getString("cmdbJob.propertyTypeCreation.taskName") + ": ");
            for (IConfigurationElement configElem : propertyTypes) {
                PropertyTypeDTO dto = new PropertyTypeDTO();
                String typeName = configElem.getAttribute(ATT_TYPE_NAME);
                dto.setTypeName(typeName);
                dto.setDescription(configElem.getAttribute(ATT_PROP_DESCRIPTION));
                dto.setDisplayName(configElem.getAttribute(ATT_PROP_DISPLAY_NAME));
                dto.setDtoType(configElem.getAttribute(ATT_DTO_TYPE));
                dto.setJavaType(configElem.getAttribute(ATT_JAVA_TYPE));
                dto = cmdbService.createPropertyType(ticket.getId(), dto);
                propertyTypesCache.put(typeName, dto);
                monitor.worked(1);
                output.append("#");
            }
            monitor.done();
            output.append("#\n");
            monitor.beginTask(messages.getString("cmdbJob.relationTypeCreation.taskName"), relationTypes.size());
            output.append(messages.getString("cmdbJob.relationTypeCreation.taskName") + ": ");
            for (IConfigurationElement configElem : relationTypes) {
                RelationTypeDTO relation = new RelationTypeDTO();
                String typeName = configElem.getAttribute(ATT_TYPE_NAME);
                relation.setRelationTypeName(typeName);
                relation.setDescription(configElem.getAttribute(ATT_DESCRIPTION));
                relation.setDisplayName(configElem.getAttribute(ATT_DISPLAY_NAME));
                relation = cmdbService.createRelationType(ticket.getId(), relation);
                relationTypesCache.put(typeName, relation);
                monitor.worked(1);
                output.append("#");
            }
            monitor.done();
            output.append("#\n");
            monitor.beginTask(messages.getString("cmdbJob.classCreation.taskName"), ciClasses.size());
            output.append(messages.getString("cmdbJob.classCreation.taskName") + ": ");
            for (IConfigurationElement configElem : ciClasses) {
                String className = configElem.getAttribute(ATT_NAME);
                String parentClassName = configElem.getAttribute(ATT_PARENT_CLASS);
                String displayName = configElem.getAttribute(ATT_DISPLAY_NAME);
                String description = configElem.getAttribute(ATT_DESCRIPTION);
                boolean isAbstract = false;
                if (configElem.getAttribute(ATT_IS_ABSTRACT) != null && configElem.getAttribute(ATT_IS_ABSTRACT).equals("true")) isAbstract = true;
                CIClassDTO newClass = new CIClassDTO(className);
                if (parentClassName != null) {
                    CIClassDTO parentClass = cmdbService.getCIClass(ticket.getId(), parentClassName);
                    if (parentClass != null) newClass.setParentClassId(parentClass.getId()); else {
                        LoggerPlugin.LogWarning(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Contributed class '" + className + "': Couldn't find parent class '" + parentClassName + "'");
                    }
                }
                newClass.setAbstractClass(isAbstract);
                newClass.setDisplayName(displayName);
                newClass.setDescription(description);
                newClass = cmdbService.createCIClass(ticket.getId(), newClass);
                ArrayList<IConfigurationElement> propertyDefTab = getPropertyDefinitions(configElem);
                for (IConfigurationElement propertyDef : propertyDefTab) {
                    CIPropertyDefDTO propDef = new CIPropertyDefDTO();
                    String propertyName = propertyDef.getAttribute(ATT_PROPERTY_DEF_NAME);
                    propDef.setPropertyName(propertyName);
                    propDef.setDisplayName(propertyDef.getAttribute(ATT_PROPERTY_DEF_DISPLAYNAME));
                    propDef.setDescription(propertyDef.getAttribute(ATT_PROPERTY_DEF_DESCRIPTION));
                    propDef.setDefaultValue(propertyDef.getAttribute(ATT_PROPERTY_DEF_DEFAULTVALUE));
                    propDef.setUIInfo(getUIInfo(propertyDef));
                    if (propertyDef.getAttribute(ATT_PROPERTY_DEF_IS_MANDATORY) != null && propertyDef.getAttribute(ATT_PROPERTY_DEF_IS_MANDATORY).equals("true")) propDef.setMandatory(true); else propDef.setMandatory(false);
                    String propertyType = propertyDef.getAttribute(ATT_PROPERTY_DEF_TYPE);
                    PropertyTypeDTO type = propertyTypesCache.get(propertyType);
                    if (type == null) {
                        type = cmdbService.getPropertyType(ticket.getId(), propertyType);
                        if (type == null) {
                            LoggerPlugin.LogWarning(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Couldn't create property '" + propertyName + "' for class '" + className + "': Couldn't find property type '" + propertyType + "'");
                        }
                        propertyTypesCache.put(propertyName, type);
                    }
                    propDef.setPropertyTypeId(type.getId());
                    cmdbService.createCIPropertyDef(ticket.getId(), newClass.getId(), propDef);
                }
                monitor.worked(1);
                output.append("#");
            }
            monitor.done();
            output.append("#\n");
            monitor.beginTask(messages.getString("cmdbJob.relationClassCreation.taskName"), ciClasses.size());
            output.append(messages.getString("cmdbJob.relationClassCreation.taskName") + ": ");
            for (IConfigurationElement configElem : relationClasses) {
                String relationName = configElem.getAttribute(ATT_NAME);
                String relationTypeName = configElem.getAttribute(ATT_RELATION_TYPE);
                String displayName = configElem.getAttribute(ATT_DISPLAY_NAME);
                String description = configElem.getAttribute(ATT_DESCRIPTION);
                RelationClassDTO newClass = new RelationClassDTO();
                if (relationTypeName != null) {
                    RelationTypeDTO type = relationTypesCache.get(relationTypeName);
                    if (type == null) {
                        type = cmdbService.getRelationType(ticket.getId(), relationTypeName);
                        if (type == null) {
                            LoggerPlugin.LogError(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Cannot create relation class '" + relationName + "': Couldn't find relation type '" + relationTypeName + "'");
                            continue;
                        }
                        relationTypesCache.put(relationTypeName, type);
                    }
                    newClass.setRelationTypeId(type.getId());
                }
                newClass.setRelationName(relationName);
                newClass.setDisplayName(displayName);
                newClass.setDescription(description);
                String card = null;
                IConfigurationElement[] children = configElem.getChildren(ATT_RELATION_SOURCE_CLASS);
                for (IConfigurationElement elem : children) {
                    String className = elem.getAttribute(ATT_RELATION_CLASS_NAME);
                    CIClassDTO sourceClass = cmdbService.getCIClass(ticket.getId(), className);
                    if (sourceClass == null) {
                        LoggerPlugin.LogError(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Cannot create relation class '" + relationName + "': Couldn't find source class '" + className + "'");
                        continue;
                    }
                    newClass.setSourceClassId(sourceClass.getId());
                    newClass.setSourceRoleDescription(elem.getAttribute(ATT_RELATION_CLASS_ROLE_DESCRIPTION));
                    newClass.setSourceRoleDisplayName(elem.getAttribute(ATT_RELATION_CLASS_ROLE_DISPLAYNAME));
                    card = elem.getAttribute(ATT_RELATION_CLASS_MIN_CARDINALITY);
                    if (card != null) newClass.setSourceMinCardinality(Integer.parseInt(card));
                    card = elem.getAttribute(ATT_RELATION_CLASS_MAX_CARDINALITY);
                    if (card != null) newClass.setSourceMaxCardinality(Integer.parseInt(card));
                }
                card = null;
                children = configElem.getChildren(ATT_RELATION_TARGET_CLASS);
                for (IConfigurationElement elem : children) {
                    String className = elem.getAttribute(ATT_RELATION_CLASS_NAME);
                    CIClassDTO targetClass = cmdbService.getCIClass(ticket.getId(), className);
                    if (targetClass == null) {
                        LoggerPlugin.LogError(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Cannot create relation class '" + relationName + "': Couldn't find target class '" + className + "'");
                        continue;
                    }
                    newClass.setTargetClassId(targetClass.getId());
                    newClass.setTargetRoleDescription(elem.getAttribute(ATT_RELATION_CLASS_ROLE_DESCRIPTION));
                    newClass.setTargetRoleDisplayName(elem.getAttribute(ATT_RELATION_CLASS_ROLE_DISPLAYNAME));
                    card = elem.getAttribute(ATT_RELATION_CLASS_MIN_CARDINALITY);
                    if (card != null) newClass.setTargetMinCardinality(Integer.parseInt(card));
                    card = elem.getAttribute(ATT_RELATION_CLASS_MAX_CARDINALITY);
                    if (card != null) newClass.setTargetMaxCardinality(Integer.parseInt(card));
                }
                newClass = cmdbService.createRelationClass(ticket.getId(), newClass);
                ArrayList<IConfigurationElement> propertyDefTab = getPropertyDefinitions(configElem);
                for (IConfigurationElement propertyDef : propertyDefTab) {
                    RelationPropertyDefDTO propDef = new RelationPropertyDefDTO();
                    String propertyName = propertyDef.getAttribute(ATT_PROPERTY_DEF_NAME);
                    propDef.setPropertyName(propertyName);
                    propDef.setDisplayName(propertyDef.getAttribute(ATT_PROPERTY_DEF_DISPLAYNAME));
                    propDef.setDescription(propertyDef.getAttribute(ATT_PROPERTY_DEF_DESCRIPTION));
                    propDef.setDefaultValue(propertyDef.getAttribute(ATT_PROPERTY_DEF_DEFAULTVALUE));
                    propDef.setUIInfo(getUIInfo(propertyDef));
                    if (propertyDef.getAttribute(ATT_PROPERTY_DEF_IS_MANDATORY) != null && propertyDef.getAttribute(ATT_PROPERTY_DEF_IS_MANDATORY).equals("true")) propDef.setMandatory(true); else propDef.setMandatory(false);
                    String propertyType = propertyDef.getAttribute(ATT_PROPERTY_DEF_TYPE);
                    PropertyTypeDTO type = propertyTypesCache.get(propertyType);
                    if (type == null) {
                        type = cmdbService.getPropertyType(ticket.getId(), propertyType);
                        if (type == null) {
                            LoggerPlugin.LogWarning(ClientPlugin.PLUGIN_ID, LoggingConstants.NULL_OBJECT, "Couldn't create property '" + propertyName + "' for relation '" + relationName + "': Couldn't find property type '" + propertyType + "'");
                        }
                        propertyTypesCache.put(propertyName, type);
                    }
                    propDef.setPropertyTypeId(type.getId());
                    cmdbService.createRelationPropertyDef(ticket.getId(), newClass.getId(), propDef);
                }
                monitor.worked(1);
                output.append("#");
            }
            monitor.done();
            output.append("#\n");
        } catch (ConnectionException e) {
            LoggerPlugin.LogWarning(ClientPlugin.PLUGIN_ID, LoggingConstants.SERVER_CONNECTION, "couldn't check service availability", e);
            output.append("\n " + messages.getString("connectionException.message") + " :\n");
            output.append(StringTools.getThrowableStack(e));
            return failure(messages.getString("connectionException.message"), e);
        } catch (Exception e) {
            LoggerPlugin.LogError(ClientPlugin.PLUGIN_ID, LoggingConstants.UNEXPECTED_EXCEPTION, "Unexpected Exception", e);
            output.append("\n " + messages.getString("unexpectedException.message") + " :\n");
            output.append(StringTools.getThrowableStack(e));
            return failure(messages.getString("unexpectedException.message"), e);
        } finally {
            monitor.done();
        }
        output.append(" " + messages.getString("DONE") + "\n");
        return new Status(IStatus.OK, ClientPlugin.PLUGIN_ID, IStatus.OK, messages.getString("miscJob.status.title"), null);
    }

    @Override
    public Action getCompletedAction() {
        return new Action(messages.getString("miscJob.status")) {

            public void run() {
                MessageDialog.openInformation(PlatformUI.getWorkbench().getDisplay().getActiveShell(), messages.getString("miscJob.status.title"), messages.getString("miscJob.status.message"));
            }
        };
    }

    @Override
    public boolean belongsTo(Object family) {
        return EscapeKJob.JOB_FAMILY_REPOSITORY_CREATION.equals(family);
    }

    public void readContributions() {
        String ELEM_PROPERTY_TYPE = "PropertyType";
        String ELEM_CICLASS = "CIClass";
        String ELEM_RELATION_TYPE = "RelationType";
        String ELEM_RELATION_CLASS = "RelationClass";
        IExtensionPoint extPoint = registry.getExtensionPoint(CMDB_CONTRIB_EXT_POINT);
        for (IExtension extension : extPoint.getExtensions()) {
            for (IConfigurationElement element : extension.getConfigurationElements()) {
                if (element.getName().equals(ELEM_PROPERTY_TYPE)) {
                    propertyTypes.add(element);
                    continue;
                }
                if (element.getName().equals(ELEM_RELATION_TYPE)) {
                    relationTypes.add(element);
                    continue;
                }
                if (element.getName().equals(ELEM_CICLASS)) {
                    ciClasses.add(element);
                    continue;
                }
                if (element.getName().equals(ELEM_RELATION_CLASS)) {
                    relationClasses.add(element);
                    continue;
                }
            }
        }
    }

    public ArrayList<IConfigurationElement> getPropertyDefinitions(IConfigurationElement ciClassElement) {
        String ELEM_PROPERTIES = "properties";
        ArrayList<IConfigurationElement> propertyDefTab = new ArrayList<IConfigurationElement>();
        IConfigurationElement[] propertiesTab = ciClassElement.getChildren(ELEM_PROPERTIES);
        for (IConfigurationElement properties : propertiesTab) {
            IConfigurationElement[] propertyTab = properties.getChildren();
            for (IConfigurationElement property : propertyTab) propertyDefTab.add(property);
        }
        return propertyDefTab;
    }

    public ArrayList<IConfigurationElement> getLinksDefinitions(IConfigurationElement ciClassElement) {
        String ELEM_PROPERTIES = "links";
        ArrayList<IConfigurationElement> linkDefTab = new ArrayList<IConfigurationElement>();
        IConfigurationElement[] linksTab = ciClassElement.getChildren(ELEM_PROPERTIES);
        for (IConfigurationElement links : linksTab) {
            IConfigurationElement[] linkTab = links.getChildren();
            for (IConfigurationElement link : linkTab) linkDefTab.add(link);
        }
        return linkDefTab;
    }

    public Properties getUIInfo(IConfigurationElement ciClassElement) {
        String ELEM_UIINFO = "UIInfo";
        String ATT_ID = "id";
        String ATT_VALUE = "value";
        Properties uiInfos = new Properties();
        IConfigurationElement[] UIInfoTab = ciClassElement.getChildren(ELEM_UIINFO);
        for (IConfigurationElement uiInfo : UIInfoTab) {
            IConfigurationElement[] infoTab = uiInfo.getChildren();
            for (IConfigurationElement info : infoTab) {
                String id = info.getAttribute(ATT_ID);
                String value = info.getAttribute(ATT_VALUE);
                uiInfos.put(id, value);
            }
        }
        return uiInfos;
    }
}
