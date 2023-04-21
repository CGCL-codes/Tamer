package net.sf.doolin.app.mt.gui.task;

import net.sf.doolin.app.mt.gui.bean.MTContext;
import net.sf.doolin.app.mt.gui.bean.MTTask;
import net.sf.doolin.app.mt.gui.facade.MTFacade;
import net.sf.doolin.gui.action.ActionContext;
import net.sf.doolin.gui.action.task.AbstractGUITask;
import org.springframework.beans.factory.annotation.Autowired;

public class SaveTaskTask extends AbstractGUITask<MTTask, Void> {

    @Autowired
    private MTFacade facade;

    @Override
    public Void doInBackground(ActionContext actionContext, MTTask bean) throws Exception {
        MTContext context = (MTContext) actionContext.getWindow().getWindowData();
        this.facade.saveTask(context, bean);
        return null;
    }

    @Override
    public void doOnResult(ActionContext actionContext, MTTask bean, Void result) {
    }
}
