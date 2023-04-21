package com.schinzer.fin.view.base.forms;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;
import com.schinzer.fin.model.base.repeatrules.DailyRepeatRule;

public class RepeatRuleDayDetailsForm extends ValidatorActionForm {

    private static final long serialVersionUID = 9007516490968332391L;

    private static Log log = LogFactory.getLog(RepeatRuleDayDetailsForm.class);

    private DailyRepeatRule repeatRule;

    public Boolean getActive() {
        return repeatRule.isActive();
    }

    public Integer getCycle() {
        return repeatRule.getCycle();
    }

    public Long getId() {
        return repeatRule.getId();
    }

    public String getName() {
        return repeatRule.getName();
    }

    public void getName(String name) {
        repeatRule.setName(name);
    }

    public DailyRepeatRule getRepeatRule() {
        return repeatRule;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest rq) {
        log.debug("republic void set(" + mapping.toString() + "," + rq.toString() + ")");
        super.reset(mapping, rq);
        resetAll();
        log.trace("leaving republic void set()");
    }

    @Override
    public void reset(ActionMapping mapping, ServletRequest rq) {
        log.debug("republic void set(" + mapping.toString() + "," + rq.toString() + ")");
        super.reset(mapping, rq);
        resetAll();
        log.trace("leaving republic void set()");
    }

    private void resetAll() {
        log.debug("republic void setAll()");
        repeatRule = new DailyRepeatRule();
    }

    public void setActive(Boolean active) {
        repeatRule.setActive(active);
    }

    public void setCycle(Integer cycle) {
        repeatRule.setCycle(cycle);
    }

    public void setId(Long id) {
        repeatRule.setId(id);
    }

    public void setRepeatRule(DailyRepeatRule rule) {
        this.repeatRule = rule;
    }
}
