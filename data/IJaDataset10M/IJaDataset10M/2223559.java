package com.objectcode.time4u.server.services.handler;

import org.springframework.beans.factory.annotation.Required;
import com.objectcode.time4u.server.api.data.ChangeOperation;
import com.objectcode.time4u.server.api.data.ChangeType;
import com.objectcode.time4u.server.api.data.ClientChange;
import com.objectcode.time4u.server.api.data.Day;
import com.objectcode.time4u.server.api.data.ServerChange;
import com.objectcode.time4u.server.api.data.WorkItem;
import com.objectcode.time4u.server.auth.ITime4UUserDetails;
import com.objectcode.time4u.server.dao.ChangeData;
import com.objectcode.time4u.server.dao.IGetEntityOperation;
import com.objectcode.time4u.server.dao.IInsertEntityOperation;
import com.objectcode.time4u.server.dao.IWorkItemDao;
import com.objectcode.time4u.server.ejb.entities.IContext;
import com.objectcode.time4u.server.ejb.entities.WorkItemEntity;

public class WorkItemChangeHandler implements IChangeHandler {

    IWorkItemDao workItemDao;

    @Required
    public void setWorkItemDao(IWorkItemDao workItemDao) {
        this.workItemDao = workItemDao;
    }

    public ChangeType getChangeTpye() {
        return ChangeType.WORKITEM_OLDSTYLE;
    }

    public ServerChange perform(ITime4UUserDetails user, ClientChange clientChange, IGetEntityOperation<ServerChange, ChangeData> operation) {
        final Day day = (Day) clientChange.getChangeData();
        workItemDao.deleteForDay(user.getPersonId(), day.getDay(), day.getMonth(), day.getYear());
        for (final WorkItem workItem : day.getWorkItems()) {
            workItemDao.insert(user.getPersonId(), new IInsertEntityOperation<Long, WorkItemEntity>() {

                public void setId(Long id) {
                }

                public void performInsert(IContext context, WorkItemEntity entity) {
                    entity.fromAPI(context, workItem, day);
                }
            });
        }
        return new ServerChange(-1L, ChangeType.WORKITEM_OLDSTYLE, ChangeOperation.UPDATE, day);
    }
}
