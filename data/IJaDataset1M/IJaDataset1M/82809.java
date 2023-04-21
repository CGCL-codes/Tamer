package com.windsor.node.data.dao;

import java.util.List;
import com.windsor.node.common.domain.NotificationType;

public interface NotificationDao extends ListableDao {

    void save(String accountId, List notifications);

    List getByAccountId(String id);

    List getByFlowId(String id);

    List getByFlowIdAndType(String id, NotificationType type);

    void deleteByUserId(String id);

    void deleteByFlowId(String id);
}
