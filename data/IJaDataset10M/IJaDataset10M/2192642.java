package org.vosao.service.back;

import java.util.List;
import java.util.Map;
import org.vosao.entity.FolderPermissionEntity;
import org.vosao.service.AbstractService;
import org.vosao.service.ServiceResponse;
import org.vosao.service.vo.FolderPermissionVO;

/**
 * @author Alexander Oleynik
 */
public interface FolderPermissionService extends AbstractService {

    ServiceResponse remove(final List<String> ids);

    FolderPermissionEntity getById(final Long id);

    ServiceResponse save(final Map<String, String> vo);

    List<FolderPermissionVO> selectByFolder(final Long folderId);

    FolderPermissionEntity getPermission(final Long folderId);
}
