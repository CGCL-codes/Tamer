package org.regola.webapp.action.icefaces;

import java.io.Serializable;
import java.util.HashMap;
import org.hibernate.validator.InvalidValue;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.webapp.action.AutoCompleteBean;
import org.regola.webapp.action.BasePage;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.component.FormPageComponent;
import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

public class FormPageIceFaces<T, ID extends Serializable, F extends ModelPattern> extends BasePageIceFaces implements FormPageComponent<T, ID, F> {

    HashMap<String, Effect> effects = new HashMap<String, Effect>();

    FormPage<T, ID, F> formPage;

    public FormPage<T, ID, F> getFormPage() {
        return formPage;
    }

    public void setFormPage(FormPage<T, ID, F> formPage) {
        this.formPage = formPage;
    }

    public <MODEL, MODELID extends Serializable, FILTER extends ModelPattern> void addAutoCompleteLookUp(String property, MODEL model, FILTER filter, GenericManager<MODEL, MODELID> manager) {
        AutoCompleteBean<MODEL, MODELID, FILTER> ac = new AutoCompleteBeanIceFaces<MODEL, MODELID, FILTER>();
        ac.init(model, filter, manager);
        formPage.getLookups().put(property, ac);
    }

    public void init() {
        state = PersistentFacesState.getInstance();
    }

    @SuppressWarnings("unchecked")
    public <T> InvalidValue[] validate(InvalidValue[] msgs) {
        setEffectPanel(null);
        getEffects().clear();
        for (InvalidValue msg : msgs) {
            effects.put(msg.getPropertyName(), new Highlight("#FFFF00"));
        }
        return msgs;
    }

    public HashMap<String, Effect> getEffects() {
        return effects;
    }

    public void setPage(FormPage<T, ID, F> page) {
        formPage = page;
    }
}
