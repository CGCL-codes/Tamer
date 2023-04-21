package org.criticalfailure.anp.core.persistence.vo.translator.impl;

import org.criticalfailure.anp.core.application.entity.Alert;
import org.criticalfailure.anp.core.application.services.AlertService;
import org.criticalfailure.anp.core.domain.entity.Contact;
import org.criticalfailure.anp.core.domain.entity.Task;
import org.criticalfailure.anp.core.domain.factory.ITaskFactory;
import org.criticalfailure.anp.core.persistence.Messages;
import org.criticalfailure.anp.core.persistence.storage.IDomainObjectPersister;
import org.criticalfailure.anp.core.persistence.vo.ContactVO;
import org.criticalfailure.anp.core.persistence.vo.TaskVO;
import org.criticalfailure.anp.core.persistence.vo.translator.IContactValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ITaskValueObjectTranslator;
import org.criticalfailure.anp.core.persistence.vo.translator.ValueObjectTranslationException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pauly
 * 
 */
@Component("taskValueObjectTranslator")
public class TaskValueObjectTranslatorImpl implements ITaskValueObjectTranslator {

    private Logger logger;

    @Autowired
    private IDomainObjectPersister domainObjectPersister;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ITaskFactory taskFactory;

    @Autowired
    private IContactValueObjectTranslator contactValueObjectTranslator;

    public TaskValueObjectTranslatorImpl() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public Task translateFrom(TaskVO vo) throws ValueObjectTranslationException {
        Task task = (Task) domainObjectPersister.getCachedObject(vo.getId());
        if (task == null) {
            task = taskFactory.createTask();
            task.setId(vo.getId());
            domainObjectPersister.cacheObject(task);
            translateFromWithMerge(vo, task);
        }
        return task;
    }

    public void translateFromWithMerge(TaskVO vo, Task obj) throws ValueObjectTranslationException {
        obj.setSummary(vo.getSummary());
        obj.setDetails(vo.getDetails());
        obj.setDueDate(new DateTime(vo.getDueDate()));
        try {
            Contact contact = (Contact) domainObjectPersister.getCachedObject(vo.getAssigneeId());
            if (contact == null) {
                ContactVO contactVO = domainObjectPersister.loadContact(vo.getAssigneeId());
                contact = contactValueObjectTranslator.translateFrom(contactVO);
            }
            obj.setAssignee(contact);
        } catch (Exception e) {
            logger.error("Exception caught while trying to load assignee for task: " + e.getLocalizedMessage(), e);
            alertService.addAlert(new Alert(Alert.Type.ERROR, this.getClass().getSimpleName(), Messages.getString("school.management.translator.task.error.assignee.text") + ": " + e.getLocalizedMessage()));
        }
    }

    public TaskVO translateTo(Task obj) {
        TaskVO vo = new TaskVO();
        vo.setId(obj.getId());
        vo.setSummary(obj.getSummary());
        vo.setDetails(obj.getDetails());
        vo.setDueDate(obj.getDueDate().getMillis());
        if (obj.getAssignee() != null) {
            vo.setAssigneeId(obj.getAssignee().getId());
        }
        return vo;
    }

    /**
     * @return the domainObjectPersister
     */
    public IDomainObjectPersister getDomainObjectPersister() {
        return domainObjectPersister;
    }

    /**
     * @param domainObjectPersister
     *            the domainObjectPersister to set
     */
    public void setDomainObjectPersister(IDomainObjectPersister domainObjectPersister) {
        this.domainObjectPersister = domainObjectPersister;
    }

    /**
     * @return the alertService
     */
    public AlertService getAlertService() {
        return alertService;
    }

    /**
     * @param alertService
     *            the alertService to set
     */
    public void setAlertService(AlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * @return the taskFactory
     */
    public ITaskFactory getTaskFactory() {
        return taskFactory;
    }

    /**
     * @param taskFactory
     *            the taskFactory to set
     */
    public void setTaskFactory(ITaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    /**
     * @return the contactValueObjectTranslator
     */
    public IContactValueObjectTranslator getContactValueObjectTranslator() {
        return contactValueObjectTranslator;
    }

    /**
     * @param contactValueObjectTranslator
     *            the contactValueObjectTranslator to set
     */
    public void setContactValueObjectTranslator(IContactValueObjectTranslator contactValueObjectTranslator) {
        this.contactValueObjectTranslator = contactValueObjectTranslator;
    }
}
