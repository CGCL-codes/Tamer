package net.naijatek.myalumni.modules.common.service;

import java.util.List;
import net.naijatek.myalumni.framework.exceptions.MyAlumniException;
import net.naijatek.myalumni.modules.common.domain.XlatGroupVO;

public interface IXlatGroupService extends BaseCrudService<XlatGroupVO, String> {

    public void updateGroupDetails(String userId, String groupId, List items) throws MyAlumniException;

    public List<XlatGroupVO> getAllGroups();
}
