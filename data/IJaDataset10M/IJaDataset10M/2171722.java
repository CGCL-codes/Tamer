package org.monet.kernel.extension;

import java.util.Iterator;
import org.monet.kernel.constants.Common;
import org.monet.kernel.constants.ErrorCode;
import org.monet.kernel.constants.LabelCode;
import org.monet.kernel.constants.MessageCode;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.constants.TaskType;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.model.Account;
import org.monet.kernel.model.Attribute;
import org.monet.kernel.model.BusinessUnit;
import org.monet.kernel.model.Context;
import org.monet.kernel.model.Dictionary;
import org.monet.kernel.model.HistoryStoreLink;
import org.monet.kernel.model.Language;
import org.monet.kernel.model.Node;
import org.monet.kernel.model.Session;
import org.monet.kernel.model.Task;
import org.monet.kernel.model.TaskList;
import org.monet.kernel.model.definition.FieldDeclaration;
import org.monet.kernel.model.definition.FormDefinition;
import org.monet.kernel.model.definition.LinkFieldDeclaration;
import org.monet.kernel.model.definition.SelectFieldDeclaration;
import org.monet.kernel.model.definition.TextFieldDeclaration;

public class Enricher implements IEnricher {

    private HistoryStoreLink historyStoreLink;

    private static Enricher oInstance;

    private Enricher() {
        this.historyStoreLink = null;
    }

    public static synchronized Enricher getInstance() {
        if (oInstance == null) oInstance = new Enricher();
        return oInstance;
    }

    public Boolean setHistoryStoreLink(HistoryStoreLink dataLink) {
        this.historyStoreLink = dataLink;
        return true;
    }

    public Boolean completeNodeAttributes(FormDefinition formDefinition, Attribute oAttribute, Integer iErrors) {
        FieldDeclaration fieldDeclaration = formDefinition.getFieldDeclaration(oAttribute.getCode());
        String code = oAttribute.getIndicatorValue(Common.DataStoreField.CODE);
        String sValue = oAttribute.getIndicatorValue(Common.DataStoreField.VALUE);
        if (fieldDeclaration == null) return false;
        if (!sValue.equals(Strings.EMPTY)) {
            String datastore = null;
            if (fieldDeclaration instanceof TextFieldDeclaration) {
                org.monet.kernel.model.definition.TextFieldDeclaration.AllowHistory allowHistory = ((TextFieldDeclaration) fieldDeclaration).getAllowHistory();
                if (allowHistory != null) datastore = allowHistory.getDatastore();
            } else if (fieldDeclaration instanceof SelectFieldDeclaration) {
                org.monet.kernel.model.definition.SelectFieldDeclaration.AllowHistory allowHistory = ((SelectFieldDeclaration) fieldDeclaration).getAllowHistory();
                if (allowHistory != null) datastore = allowHistory.getDatastore();
            } else if (fieldDeclaration instanceof LinkFieldDeclaration) {
                org.monet.kernel.model.definition.LinkFieldDeclaration.AllowHistory allowHistory = ((LinkFieldDeclaration) fieldDeclaration).getAllowHistory();
                datastore = allowHistory.getDatastore();
            }
            if (datastore != null) this.historyStoreLink.addTerm(datastore, code, sValue);
        }
        if ((fieldDeclaration.isRequired()) && (sValue.equals(Strings.EMPTY))) iErrors++;
        for (Attribute oChildAttribute : oAttribute.getAttributeList()) {
            this.completeNodeAttributes(formDefinition, oChildAttribute, iErrors);
        }
        return true;
    }

    public Boolean completeNode(Node oNode, TaskList taskList) {
        Session oSession = (Session) Context.getInstance().getCurrentSession();
        Dictionary oDictionary = BusinessUnit.getInstance().getBusinessModel().getDictionary();
        Iterator<String> oIter = oNode.getAttributeList().get().keySet().iterator();
        Account oAccount = oSession.getAccount();
        Task task = null;
        Integer iErrors = 0;
        FormDefinition formDefinition;
        if (oAccount == null) return true;
        if (!oDictionary.isFormDefinition(oNode.getDefinitionType().getCode())) return true;
        taskList.removeOfType(TaskType.REVISION);
        formDefinition = oDictionary.getFormDefinition(oNode.getDefinitionType().getCode());
        try {
            while (oIter.hasNext()) {
                String codeAttribute = oIter.next();
                Attribute oAttribute = (Attribute) oNode.getAttributeList().get(codeAttribute);
                this.completeNodeAttributes(formDefinition, oAttribute, iErrors);
            }
            if (iErrors > 0) {
                String sDescription = Language.getInstance().getMessage(MessageCode.REQUIRED_FIELDS, Language.getCurrent());
                sDescription += "<div class='messages'><div class='errors'>" + iErrors.toString() + Strings.SPACE + Language.getInstance().getLabel(LabelCode.ERROR_FIELDS, Language.getCurrent()) + "</div>";
                sDescription += "<div class='warnings'>0 " + Language.getInstance().getLabel(LabelCode.WARNING_FIELDS, Language.getCurrent()) + "</div></div>";
                task = new Task();
                task.setTarget(oNode);
                task.setType(TaskType.REVISION);
                task.setLabel(oNode.getLabel());
                task.setDescription(sDescription);
                task.addEnrolment(oAccount.getUser().getId());
            }
            if (task != null) taskList.add(task);
        } catch (NullPointerException oException) {
            throw new DataException(ErrorCode.COMPLETE_NODE, oNode.getId(), oException);
        }
        return true;
    }
}
