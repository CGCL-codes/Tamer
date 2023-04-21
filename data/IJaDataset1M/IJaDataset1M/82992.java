package de.mogwai.kias.example;

import de.mogwai.kias.CommandList;
import de.mogwai.kias.example.bo.PersistentObject;
import de.mogwai.kias.example.service.PersistenceService;
import de.mogwai.kias.example.service.RecordInfo;
import de.mogwai.kias.forms.FormContext;
import de.mogwai.kias.forms.FormController;
import de.mogwai.kias.forms.definition.Forward;
import de.mogwai.kias.forms.validation.KIASValidateBeforeExecution;

public abstract class NavigatableController<E extends PersistentObject, T extends NavigatableFormBean<E>> extends FormController<T> {

    protected PersistenceService persistenceService;

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void onFirstInit(FormContext aContext) {
        lastClicked(aContext);
    }

    protected abstract E createNew();

    protected abstract Class getObjectClass();

    protected void enrichData(FormContext aContext) {
        NavigatableFormBean<E> theBean = getBean(aContext);
        if (theBean.getData() != null) {
            RecordInfo theInfo = persistenceService.getRecordInfo(getObjectClass(), theBean.getData());
            if (theBean.getData().getId() != null) theBean.setRecordInfo(theInfo.getNumber() + " / " + theInfo.getCount()); else theBean.setRecordInfo("" + theInfo.getCount());
        } else theBean.setRecordInfo("");
    }

    public void onForward(FormContext aContext, String aCommand) {
        if ("yes".equals(aCommand)) {
            persistenceService.delete(getBean(aContext).getData());
            aCommand = "new";
        }
        if ("new".equals(aCommand)) {
            getBean(aContext).setData(createNew());
            enrichData(aContext);
        }
    }

    public void onForward(FormContext aContext) {
        super.onForward(aContext);
    }

    public void onForward(FormContext aContext, E aValue) {
        getBean(aContext).setData(aValue);
        enrichData(aContext);
    }

    public Forward firstClicked(FormContext aContext) {
        E theObject = (E) persistenceService.findFirst(getObjectClass());
        if (theObject == null) theObject = createNew();
        getBean(aContext).setData(theObject);
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward priorClicked(FormContext aContext) {
        E theObject = (E) persistenceService.findPrior(getObjectClass(), getBean(aContext).getData());
        if (theObject == null) theObject = createNew();
        getBean(aContext).setData(theObject);
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward nextClicked(FormContext aContext) {
        E theObject = (E) persistenceService.findNext(getObjectClass(), getBean(aContext).getData());
        if (theObject == null) theObject = createNew();
        getBean(aContext).setData(theObject);
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward lastClicked(FormContext aContext) {
        E theObject = (E) persistenceService.findLast(getObjectClass());
        if (theObject == null) theObject = createNew();
        getBean(aContext).setData(theObject);
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward newClicked(FormContext aContext) {
        getBean(aContext).setData(createNew());
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward deleteClicked(FormContext aContext) {
        CommandList aCommandList = new CommandList(aContext);
        aCommandList.addResource("YES", "deleteCurrentRecord");
        aCommandList.addResource("NO", null);
        return createMessageDialogForward(aContext, aContext.getResourceFromKey("DELETERECORD"), aContext.getResourceFromKey("AREYOUSURETODELETERECORD"), aCommandList);
    }

    public void deleteCurrentRecord(FormContext aContext) {
        persistenceService.delete(getBean(aContext).getData());
        getBean(aContext).setData(createNew());
        enrichData(aContext);
    }

    @KIASValidateBeforeExecution
    public Forward updateClicked(FormContext aContext) {
        persistenceService.saveOrUpdate(getBean(aContext).getData());
        enrichData(aContext);
        return aContext.getForm().getForward(SELF_FORWARD);
    }

    public Forward searchClicked(FormContext aContext) {
        return aContext.getForm().getForward("search");
    }
}
