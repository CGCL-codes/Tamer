package com.technoetic.xplanner.actions;

import java.util.Iterator;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.xplanner.dao.AttributeDao;
import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.Attribute;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import com.technoetic.xplanner.DomainSpecificPropertiesFactory;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.domain.Nameable;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.ProjectEditorForm;
import com.technoetic.xplanner.wiki.WikiFormat;

/**
 *    XplannerPlus, agile planning software
 *    @author Maksym_Chyrkov. 
 *    Copyright (C) 2009  Maksym Chyrkov
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 	 
 */
public class EditProjectAction extends EditObjectAction<Project> {

    private AttributeDao attributeDao;

    DomainSpecificPropertiesFactory domainSpecificPropertiesFactory;

    public void setDomainSpecificPropertiesFactory(DomainSpecificPropertiesFactory domainSpecificPropertiesFactory) {
        this.domainSpecificPropertiesFactory = domainSpecificPropertiesFactory;
    }

    @Override
    protected ActionForward doExecute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse reply) throws Exception {
        ProjectEditorForm pef = (ProjectEditorForm) actionForm;
        if (pef.getAction() != null && (pef.getAction().equals(UpdateTimeNotificationReceivers.ADD) || pef.getAction().equals(UpdateTimeNotificationReceivers.DELETE))) {
            return new ActionForward("/do/edit/project/notification", false);
        }
        return super.doExecute(actionMapping, actionForm, request, reply);
    }

    @Override
    protected void saveForm(AbstractEditorForm form, ActionMapping actionMapping, HttpServletRequest request) throws Exception {
        String oid = form.getOid();
        Class objectClass = getObjectType(actionMapping, request);
        Project object;
        String action = form.getAction();
        if (action.equals(UPDATE_ACTION)) {
            object = getCommonDao().getById(Project.class, Integer.parseInt(oid));
            populateObject(request, object, form);
            getCommonDao().save(object);
        } else if (action.equals(CREATE_ACTION)) {
            object = createObject(objectClass, request, form);
        } else {
            throw new ServletException("Unknown editor action: " + action);
        }
        setTargetObject(request, object);
        form.setAction(null);
        saveOrUpdateAttribute(XPlannerProperties.WIKI_URL_KEY, object, ((ProjectEditorForm) form).getWikiUrl());
        saveOrUpdateAttribute(XPlannerProperties.SEND_NOTIFICATION_KEY, object, (new Boolean(((ProjectEditorForm) form).isSendingMissingTimeEntryReminderToAcceptor())).toString());
        saveOrUpdateAttribute(WikiFormat.ESCAPE_BRACKETS_KEY, object, (new Boolean(((ProjectEditorForm) form).isOptEscapeBrackets())).toString());
    }

    private void saveOrUpdateAttribute(String attributeName, Nameable object, String currentAttributeValue) throws Exception {
        String attr = object.getAttribute(attributeName);
        if (attr != null) {
            Attribute attribute = new Attribute(object.getId(), attributeName, currentAttributeValue);
            attributeDao.save(attribute);
        } else {
            String existingAttributeValue = new XPlannerProperties().getProperty(attributeName);
            if (existingAttributeValue != null && !existingAttributeValue.equals(currentAttributeValue) && !currentAttributeValue.equals("")) {
                Attribute attribute = new Attribute(object.getId(), attributeName, currentAttributeValue);
                attributeDao.save(attribute);
            }
            if (existingAttributeValue == null && !currentAttributeValue.equals("")) {
                Attribute attribute = new Attribute(object.getId(), attributeName, currentAttributeValue);
                attributeDao.save(attribute);
            }
        }
    }

    @Override
    protected void populateForm(AbstractEditorForm form, DomainObject object) throws Exception {
        super.populateForm(form, object);
        ProjectEditorForm pef = (ProjectEditorForm) form;
        Properties properties = domainSpecificPropertiesFactory.createPropertiesFor(object);
        pef.setWikiUrl(properties.getProperty(XPlannerProperties.WIKI_URL_KEY, "http://"));
        pef.setSendemail(Boolean.valueOf(properties.getProperty(XPlannerProperties.SEND_NOTIFICATION_KEY, "true")).booleanValue());
        pef.setOptEscapeBrackets(Boolean.valueOf(properties.getProperty(WikiFormat.ESCAPE_BRACKETS_KEY, "true")).booleanValue());
        Project project = (Project) object;
        ProjectEditorForm projectEditorForm = (ProjectEditorForm) form;
        Iterator itr = project.getNotificationReceivers().iterator();
        while (itr.hasNext()) {
            Person person = (Person) itr.next();
            projectEditorForm.addPersonInfo("" + person.getId(), person.getUserId(), person.getInitials(), person.getName());
        }
    }

    public void setAttributeDao(AttributeDao attributeDao) {
        this.attributeDao = attributeDao;
    }
}
