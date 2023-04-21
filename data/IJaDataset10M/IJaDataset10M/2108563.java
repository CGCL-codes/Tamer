package cowsultants.itracker.ejb.client.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import cowsultants.itracker.ejb.client.models.ComponentModel;
import cowsultants.itracker.ejb.client.models.ConfigurationModel;
import cowsultants.itracker.ejb.client.models.CustomFieldModel;
import cowsultants.itracker.ejb.client.models.GenericModel;
import cowsultants.itracker.ejb.client.models.IssueAttachmentModel;
import cowsultants.itracker.ejb.client.models.IssueFieldModel;
import cowsultants.itracker.ejb.client.models.IssueHistoryModel;
import cowsultants.itracker.ejb.client.models.IssueModel;
import cowsultants.itracker.ejb.client.models.ProjectModel;
import cowsultants.itracker.ejb.client.models.SystemConfigurationModel;
import cowsultants.itracker.ejb.client.models.UserModel;
import cowsultants.itracker.ejb.client.models.VersionModel;
import cowsultants.itracker.ejb.client.resources.ITrackerResources;

/**
  * This class provides functionality needed to parse an XML document to be imported into
  * an ITracker instance, into a set of data models.
  */
public class ImportHandler extends DefaultHandler implements ImportExportTags {

    private Vector models;

    private StringBuffer tagBuffer;

    private SAXException endException;

    private GenericModel parentModel;

    private GenericModel childModel;

    private Vector itemList;

    private String tempStorage;

    public ImportHandler() {
        models = new Vector();
        endException = null;
    }

    public GenericModel[] getModels() {
        GenericModel[] modelsArray = new GenericModel[models.size()];
        models.copyInto(modelsArray);
        return modelsArray;
    }

    public void startDocument() {
        Logger.logDebug("Started import xml parsing.");
    }

    public void endDocument() {
        Logger.logDebug("Completed import xml parsing.");
    }

    public void startElement(String uri, String name, String qName, Attributes atts) throws SAXException {
        Logger.logDebug("Parsing import tag " + qName);
        if (endException != null) {
            throw endException;
        }
        tempStorage = "";
        try {
            if (TAG_COMPONENT.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for component.");
                }
                childModel = new ComponentModel();
                childModel.setId(new Integer(id));
            } else if (TAG_COMPONENTS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_CONFIGURATION.equals(qName)) {
                parentModel = new SystemConfigurationModel();
            } else if (TAG_CUSTOM_FIELD.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for issue.");
                }
                childModel = new CustomFieldModel();
                childModel.setId(new Integer(id));
            } else if (TAG_CUSTOM_FIELD_OPTION.equals(qName)) {
                tempStorage = ITrackerResources.unescapeUnicodeString(atts.getValue(ATTR_VALUE));
            } else if (TAG_CUSTOM_FIELDS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_HISTORY_ENTRY.equals(qName)) {
                String creatorId = atts.getValue(ATTR_CREATOR_ID);
                String date = atts.getValue(ATTR_DATE);
                String status = atts.getValue(ATTR_STATUS);
                if (creatorId == null) {
                    throw new SAXException("Attribute creatorId was null for issue history.");
                } else if (date == null) {
                    throw new SAXException("Attribute date was null for issue history.");
                }
                childModel = new IssueHistoryModel();
                ((IssueHistoryModel) childModel).setUser((UserModel) findModel(creatorId));
                ((IssueHistoryModel) childModel).setStatus(status != null && !status.equals("") ? Integer.parseInt(status) : IssueUtilities.HISTORY_STATUS_AVAILABLE);
                ((IssueHistoryModel) childModel).setCreateDate(getDateValue(date, qName));
                tagBuffer = new StringBuffer();
            } else if (TAG_ISSUE.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for issue.");
                }
                parentModel = new IssueModel();
                parentModel.setId(new Integer(id));
            } else if (TAG_ISSUE_ATTACHMENT.equals(qName)) {
                childModel = new IssueAttachmentModel();
            } else if (TAG_ISSUE_ATTACHMENTS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_ISSUE_COMPONENTS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_ISSUE_FIELD.equals(qName)) {
                String id = atts.getValue(ATTR_ID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for issue field.");
                }
                childModel = new IssueFieldModel((CustomFieldModel) findModel(id));
            } else if (TAG_ISSUE_FIELDS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_ISSUE_HISTORY.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_ISSUE_VERSIONS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_PROJECT.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for project.");
                }
                parentModel = new ProjectModel();
                parentModel.setId(new Integer(id));
            } else if (TAG_PROJECT_FIELDS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_PROJECT_OWNERS.equals(qName)) {
                itemList = new Vector();
            } else if (TAG_RESOLUTION.equals(qName)) {
                String value = atts.getValue(ATTR_VALUE);
                String order = atts.getValue(ATTR_ORDER);
                childModel = new ConfigurationModel(SystemConfigurationUtilities.TYPE_RESOLUTION, value, Integer.parseInt(order));
                tagBuffer = new StringBuffer();
            } else if (TAG_SEVERITY.equals(qName)) {
                String value = atts.getValue(ATTR_VALUE);
                String order = atts.getValue(ATTR_ORDER);
                childModel = new ConfigurationModel(SystemConfigurationUtilities.TYPE_SEVERITY, value, Integer.parseInt(order));
                tagBuffer = new StringBuffer();
            } else if (TAG_STATUS.equals(qName)) {
                String value = atts.getValue(ATTR_VALUE);
                String order = atts.getValue(ATTR_ORDER);
                childModel = new ConfigurationModel(SystemConfigurationUtilities.TYPE_STATUS, value, Integer.parseInt(order));
                tagBuffer = new StringBuffer();
            } else if (TAG_USER.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for user.");
                }
                parentModel = new UserModel();
                parentModel.setId(new Integer(id));
            } else if (TAG_VERSION.equals(qName)) {
                String id = atts.getValue(ATTR_SYSTEMID);
                if (id == null) {
                    throw new SAXException("Attribute " + ATTR_SYSTEMID + " was null for version.");
                }
                childModel = new VersionModel();
                childModel.setId(new Integer(id));
            } else if (TAG_VERSIONS.equals(qName)) {
                itemList = new Vector();
            } else {
                tagBuffer = new StringBuffer();
            }
        } catch (NumberFormatException nfe) {
            throw new SAXException("Attribute in " + qName + " did not contain a numeric value.");
        }
    }

    public void endElement(String uri, String name, String qName) {
        Logger.logDebug("Completing import tag " + qName);
        try {
            if (TAG_ISSUE.equals(qName) || TAG_PROJECT.equals(qName) || TAG_USER.equals(qName) || TAG_CONFIGURATION.equals(qName)) {
                models.add(parentModel.clone());
                parentModel = null;
                childModel = null;
                itemList = null;
            } else if (TAG_RESOLUTION.equals(qName) || TAG_SEVERITY.equals(qName) || TAG_STATUS.equals(qName)) {
                ((ConfigurationModel) childModel).setName(getBuffer());
                models.add(childModel.clone());
                ((SystemConfigurationModel) parentModel).addConfiguration((ConfigurationModel) childModel);
                childModel = null;
            } else if (TAG_COMPONENT.equals(qName) || TAG_VERSION.equals(qName) || TAG_CUSTOM_FIELD.equals(qName)) {
                models.add(childModel.clone());
                itemList.add(childModel.clone());
                childModel = null;
            } else if (TAG_HISTORY_ENTRY.equals(qName)) {
                ((IssueHistoryModel) childModel).setDescription(getBuffer());
                itemList.add(childModel.clone());
                childModel = null;
            } else if (TAG_ISSUE_ATTACHMENT.equals(qName)) {
                itemList.add(childModel.clone());
                childModel = null;
            } else if (TAG_ISSUE_FIELD.equals(qName)) {
                itemList.add(childModel.clone());
                childModel = null;
            } else if (TAG_COMPONENTS.equals(qName)) {
                ComponentModel[] itemListArray = new ComponentModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (ComponentModel) itemList.elementAt(i);
                }
                ((ProjectModel) parentModel).setComponents(itemListArray);
            } else if (TAG_COMPONENT_DESCRIPTION.equals(qName)) {
                ((ComponentModel) childModel).setDescription(getBuffer());
            } else if (TAG_COMPONENT_ID.equals(qName)) {
                if (itemList == null) {
                    itemList = new Vector();
                }
                itemList.add((ComponentModel) findModel(getBuffer()));
            } else if (TAG_COMPONENT_NAME.equals(qName)) {
                ((ComponentModel) childModel).setName(getBuffer());
            } else if (TAG_CONFIGURATION_VERSION.equals(qName)) {
                ((SystemConfigurationModel) parentModel).setVersion(getBuffer());
            } else if (TAG_CREATE_DATE.equals(qName)) {
                ((IssueModel) parentModel).setCreateDate(getDateValue(getBuffer(), qName));
            } else if (TAG_CREATOR.equals(qName)) {
                ((IssueModel) parentModel).setCreator((UserModel) findModel(getBuffer()));
            } else if (TAG_CUSTOM_FIELDS.equals(qName)) {
                CustomFieldModel[] itemListArray = new CustomFieldModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (CustomFieldModel) itemList.elementAt(i);
                }
                ((SystemConfigurationModel) parentModel).setCustomFields(itemListArray);
            } else if (TAG_CUSTOM_FIELD_DATEFORMAT.equals(qName)) {
                ((CustomFieldModel) childModel).setDateFormat(getBuffer());
            } else if (TAG_CUSTOM_FIELD_LABEL.equals(qName)) {
                ((CustomFieldModel) childModel).setName(getBuffer());
            } else if (TAG_CUSTOM_FIELD_OPTION.equals(qName)) {
                ((CustomFieldModel) childModel).addOption(tempStorage, getBuffer());
            } else if (TAG_CUSTOM_FIELD_REQUIRED.equals(qName)) {
                ((CustomFieldModel) childModel).setRequired(("true".equalsIgnoreCase(getBuffer()) ? true : false));
            } else if (TAG_CUSTOM_FIELD_SORTOPTIONS.equals(qName)) {
                ((CustomFieldModel) childModel).setSortOptionsByName(("true".equalsIgnoreCase(getBuffer()) ? true : false));
            } else if (TAG_CUSTOM_FIELD_TYPE.equals(qName)) {
                ((CustomFieldModel) childModel).setFieldType(getBufferAsInt());
            } else if (TAG_EMAIL.equals(qName)) {
                ((UserModel) parentModel).setEmail(getBuffer());
            } else if (TAG_FIRST_NAME.equals(qName)) {
                ((UserModel) parentModel).setFirstName(getBuffer());
            } else if (TAG_ISSUE_ATTACHMENTS.equals(qName)) {
                IssueAttachmentModel[] itemListArray = new IssueAttachmentModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (IssueAttachmentModel) itemList.elementAt(i);
                }
                ((IssueModel) parentModel).setAttachments(itemListArray);
            } else if (TAG_ISSUE_ATTACHMENT_CREATOR.equals(qName)) {
                ((IssueAttachmentModel) childModel).setUser((UserModel) findModel(getBuffer()));
            } else if (TAG_ISSUE_ATTACHMENT_DESCRIPTION.equals(qName)) {
                ((IssueAttachmentModel) childModel).setDescription(getBuffer());
            } else if (TAG_ISSUE_ATTACHMENT_FILENAME.equals(qName)) {
                ((IssueAttachmentModel) childModel).setFileName(getBuffer());
            } else if (TAG_ISSUE_ATTACHMENT_ORIGFILE.equals(qName)) {
                ((IssueAttachmentModel) childModel).setOriginalFileName(getBuffer());
            } else if (TAG_ISSUE_ATTACHMENT_SIZE.equals(qName)) {
                ((IssueAttachmentModel) childModel).setSize(getBufferAsLong());
            } else if (TAG_ISSUE_ATTACHMENT_TYPE.equals(qName)) {
                ((IssueAttachmentModel) childModel).setType(getBuffer());
            } else if (TAG_ISSUE_COMPONENTS.equals(qName)) {
                ComponentModel[] itemListArray = new ComponentModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (ComponentModel) itemList.elementAt(i);
                }
                ((IssueModel) parentModel).setComponents(itemListArray);
            } else if (TAG_ISSUE_DESCRIPTION.equals(qName)) {
                ((IssueModel) parentModel).setDescription(getBuffer());
            } else if (TAG_ISSUE_FIELDS.equals(qName)) {
                IssueFieldModel[] itemListArray = new IssueFieldModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (IssueFieldModel) itemList.elementAt(i);
                }
                ((IssueModel) parentModel).setFields(itemListArray);
            } else if (TAG_ISSUE_HISTORY.equals(qName)) {
                IssueHistoryModel[] itemListArray = new IssueHistoryModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (IssueHistoryModel) itemList.elementAt(i);
                }
                ((IssueModel) parentModel).setHistory(itemListArray);
            } else if (TAG_ISSUE_PROJECT.equals(qName)) {
                ((IssueModel) parentModel).setProject((ProjectModel) findModel(getBuffer()));
            } else if (TAG_ISSUE_RESOLUTION.equals(qName)) {
                ((IssueModel) parentModel).setResolution(getBuffer());
            } else if (TAG_ISSUE_SEVERITY.equals(qName)) {
                ((IssueModel) parentModel).setSeverity(getBufferAsInt());
            } else if (TAG_ISSUE_STATUS.equals(qName)) {
                ((IssueModel) parentModel).setStatus(getBufferAsInt());
            } else if (TAG_ISSUE_VERSIONS.equals(qName)) {
                VersionModel[] itemListArray = new VersionModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (VersionModel) itemList.elementAt(i);
                }
                ((IssueModel) parentModel).setVersions(itemListArray);
            } else if (TAG_LAST_MODIFIED.equals(qName)) {
                ((IssueModel) parentModel).setLastModifiedDate(getDateValue(getBuffer(), qName));
            } else if (TAG_LAST_NAME.equals(qName)) {
                ((UserModel) parentModel).setLastName(getBuffer());
            } else if (TAG_LOGIN.equals(qName)) {
                ((UserModel) parentModel).setLogin(getBuffer());
            } else if (TAG_OWNER.equals(qName)) {
                ((IssueModel) parentModel).setOwner((UserModel) findModel(getBuffer()));
            } else if (TAG_PROJECT_NAME.equals(qName)) {
                ((ProjectModel) parentModel).setName(getBuffer());
            } else if (TAG_PROJECT_DESCRIPTION.equals(qName)) {
                ((ProjectModel) parentModel).setDescription(getBuffer());
            } else if (TAG_PROJECT_FIELDS.equals(qName)) {
                CustomFieldModel[] itemListArray = new CustomFieldModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (CustomFieldModel) itemList.elementAt(i);
                }
                ((ProjectModel) parentModel).setCustomFields(itemListArray);
            } else if (TAG_PROJECT_FIELD_ID.equals(qName)) {
                itemList.add((CustomFieldModel) findModel(getBuffer()));
            } else if (TAG_PROJECT_OPTIONS.equals(qName)) {
                ((ProjectModel) parentModel).setOptions(getBufferAsInt());
            } else if (TAG_PROJECT_OWNERS.equals(qName)) {
                UserModel[] itemListArray = new UserModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (UserModel) itemList.elementAt(i);
                }
                ((ProjectModel) parentModel).setOwners(itemListArray);
            } else if (TAG_PROJECT_OWNER_ID.equals(qName)) {
                itemList.add((UserModel) findModel(getBuffer()));
            } else if (TAG_PROJECT_STATUS.equals(qName)) {
                ((ProjectModel) parentModel).setStatus(ProjectUtilities.STATUS_LOCKED);
                String currBuffer = getBuffer();
                HashMap projectStatuses = ProjectUtilities.getStatusNames(EXPORT_LOCALE);
                for (Iterator iter = projectStatuses.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    String keyValue = (String) projectStatuses.get(key);
                    if (keyValue != null && keyValue.equalsIgnoreCase(currBuffer)) {
                        ((ProjectModel) parentModel).setStatus(Integer.parseInt(key));
                        break;
                    }
                }
            } else if (TAG_SUPER_USER.equals(qName)) {
                ((UserModel) parentModel).setSuperUser(("true".equalsIgnoreCase(getBuffer()) ? true : false));
            } else if (TAG_TARGET_VERSION_ID.equals(qName)) {
                ((IssueModel) parentModel).setTargetVersion((VersionModel) findModel(getBuffer()));
            } else if (TAG_USER_STATUS.equals(qName)) {
                ((UserModel) parentModel).setStatus(UserUtilities.STATUS_LOCKED);
                String currBuffer = getBuffer();
                HashMap userStatuses = UserUtilities.getStatusNames(EXPORT_LOCALE);
                for (Iterator iter = userStatuses.keySet().iterator(); iter.hasNext(); ) {
                    String key = (String) iter.next();
                    String keyValue = (String) userStatuses.get(key);
                    if (keyValue != null && keyValue.equalsIgnoreCase(currBuffer)) {
                        ((UserModel) parentModel).setStatus(Integer.parseInt(key));
                        break;
                    }
                }
            } else if (TAG_VERSIONS.equals(qName)) {
                VersionModel[] itemListArray = new VersionModel[itemList.size()];
                for (int i = 0; i < itemList.size(); i++) {
                    itemListArray[i] = (VersionModel) itemList.elementAt(i);
                }
                ((ProjectModel) parentModel).setVersions(itemListArray);
            } else if (TAG_VERSION_DESCRIPTION.equals(qName)) {
                ((VersionModel) childModel).setDescription(getBuffer());
            } else if (TAG_VERSION_ID.equals(qName)) {
                if (itemList == null) {
                    itemList = new Vector();
                }
                itemList.add((VersionModel) findModel(getBuffer()));
            } else if (TAG_VERSION_NUMBER.equals(qName)) {
                ((VersionModel) childModel).setVersionInfo(getBuffer());
            }
        } catch (Exception e) {
            Logger.logDebug("Error importing data.", e);
            endException = new SAXException("Error processing tag " + qName + ": " + e.getMessage());
        }
        tagBuffer = null;
    }

    public void characters(char[] ch, int start, int length) {
        Logger.logDebug("Read " + ch.length + " Start: " + start + " Length: " + length);
        Logger.logDebug("String: " + new String(ch, start, length));
        if (tagBuffer != null) {
            tagBuffer.append(ITrackerResources.unescapeUnicodeString(new String(ch, start, length)));
        }
    }

    private String getBuffer() {
        if (tagBuffer == null) {
            return "";
        } else {
            return tagBuffer.toString();
        }
    }

    private int getBufferAsInt() throws SAXException {
        if (tagBuffer == null) {
            return -1;
        } else {
            try {
                return Integer.parseInt(tagBuffer.toString());
            } catch (NumberFormatException nfe) {
                throw new SAXException("Could not convert string buffer to int value.");
            }
        }
    }

    private long getBufferAsLong() throws SAXException {
        if (tagBuffer == null) {
            return -1;
        } else {
            try {
                return Long.parseLong(tagBuffer.toString());
            } catch (NumberFormatException nfe) {
                throw new SAXException("Could not convert string buffer to long value.");
            }
        }
    }

    private GenericModel findModel(String modelTypeId) {
        if (modelTypeId != null && !modelTypeId.equals("")) {
            for (int i = 0; i < models.size(); i++) {
                GenericModel model = (GenericModel) models.elementAt(i);
                if (getModelTypeIdString(model).equalsIgnoreCase(modelTypeId)) {
                    return model;
                }
            }
        }
        Logger.logDebug("Unable to find model id " + modelTypeId + " during import.");
        return null;
    }

    private String getModelTypeIdString(GenericModel model) {
        String idString = "UNKNOWN";
        if (model != null && model.getId() != null) {
            String type = "";
            if (model instanceof ComponentModel) {
                type = TAG_COMPONENT;
            } else if (model instanceof CustomFieldModel) {
                type = TAG_CUSTOM_FIELD;
            } else if (model instanceof IssueModel) {
                type = TAG_ISSUE;
            } else if (model instanceof ProjectModel) {
                type = TAG_PROJECT;
            } else if (model instanceof UserModel) {
                type = TAG_USER;
            } else if (model instanceof VersionModel) {
                type = TAG_VERSION;
            }
            idString = type + model.getId();
        }
        return idString;
    }

    private Date getDateValue(String dateString, String qName) throws SAXException {
        if (dateString == null || "".equals(dateString)) {
            return new Date();
        }
        try {
            return DATE_FORMATTER.parse(dateString);
        } catch (Exception e) {
            throw new SAXException("Value in " + qName + " did not contain a valid date value.");
        }
    }
}
