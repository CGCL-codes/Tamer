package com.retroago.bodymonitor.gui.handlers;

import java.util.List;
import org.springframework.util.Assert;
import com.retroago.bodymonitor.dao.BodyFatEntityDao;
import com.retroago.bodymonitor.gui.control.BodyMonitorController;
import com.retroago.bodymonitor.gui.events.BodyFatEntityDeleteEvent;
import com.retroago.bodymonitor.gui.events.BodyFatEntityPublishEvent;
import com.retroago.bodymonitor.gui.events.StartPanelUpdateEvent;
import com.retroago.bodymonitor.model.BodyFatEntity;
import com.retroago.bodymonitor.model.User;

/**
 * @author am0kk
 * 
 */
public class BodyFatEntityDeleteHandler extends BodyMonitorEventHandler<BodyFatEntityDeleteEvent> {

    private BodyFatEntityDao bodyFatEntityDao;

    public BodyFatEntityDeleteHandler() {
        super();
    }

    @Override
    protected void handleEvent(BodyFatEntityDeleteEvent ev, BodyMonitorController controller, User user) {
        Assert.notNull(bodyFatEntityDao, "bodyFatEntityDao is null");
        bodyFatEntityDao.deleteEntity(user, ev.getEntity());
        List<BodyFatEntity> list = bodyFatEntityDao.fetchEntities(user);
        BodyFatEntityPublishEvent epe = new BodyFatEntityPublishEvent(list);
        controller.incomingEvent(epe);
        controller.incomingEvent(new StartPanelUpdateEvent());
    }

    @Override
    protected Class<BodyFatEntityDeleteEvent> getEventClassHandled() {
        return BodyFatEntityDeleteEvent.class;
    }

    public BodyFatEntityDao getBodyFatEntityDao() {
        return bodyFatEntityDao;
    }

    public void setBodyFatEntityDao(BodyFatEntityDao bodyFatEntityDao) {
        this.bodyFatEntityDao = bodyFatEntityDao;
    }
}
