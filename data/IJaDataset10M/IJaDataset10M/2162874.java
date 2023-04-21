package pms.common.dao;

import java.util.List;
import pms.common.value.TargetValue;

public interface TargetDao {

    public List<TargetValue> getTargetList(int relId);

    public void updateTarget(TargetValue value);

    public void insertTarget(TargetValue value);

    public void deleteTarget(int targetId, int userId);

    public void updateTargetStatus(int targetId, int status);
}
