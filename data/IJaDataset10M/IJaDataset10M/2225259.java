package org.regola.webapp.action;

import org.regola.model.Item;
import org.regola.model.ItemId;
import org.regola.model.pattern.ItemPattern;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.annotation.ScopeEnd;
import org.apache.commons.lang.StringUtils;

public class ItemForm {

    @SuppressWarnings("unchecked")
    public void init() {
        formPage.setPlug(new FormPagePlugProxy(this));
        formPage.init();
        formPage.setValidationContext("ItemFormAmendments.xml");
        if (StringUtils.isNotEmpty(formPage.getEncodedId())) {
            ItemId id = ItemId.valueOf(formPage.getEncodedId());
            formPage.initUpdate(id);
        } else {
            formPage.initInsert(new Item());
        }
    }

    @ScopeEnd
    public String save() {
        String navigation = formPage.save();
        formPage.getEventBroker().publish("item.persistence.changes", null);
        return navigation;
    }

    @ScopeEnd
    public String cancel() {
        return formPage.cancel();
    }

    FormPage<Item, ItemId, ItemPattern> formPage;

    public void setFormPage(FormPage<Item, ItemId, ItemPattern> formPage) {
        this.formPage = formPage;
    }

    public FormPage<Item, ItemId, ItemPattern> getFormPage() {
        return formPage;
    }
}
