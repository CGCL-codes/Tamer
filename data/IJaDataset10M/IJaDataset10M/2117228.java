package cz.cvut.phone.core.dto.adapter;

import cz.cvut.phone.core.dto.RoleDTO;
import cz.cvut.phone.core.data.entity.RoleEntity;

/**
 *
 * @author Frantisek Hradil
 */
public class RoleDTOAdapter {

    public static RoleEntity parseRoleDTO(RoleDTO dto) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleID(dto.getRoleID());
        roleEntity.setName(dto.getName());
        roleEntity.setVersion(dto.getVersion());
        return roleEntity;
    }

    public static RoleDTO parseRoleEntity(RoleEntity entity) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRoleID(entity.getRoleID());
        roleDTO.setName(entity.getName());
        roleDTO.setVersion(entity.getVersion());
        return roleDTO;
    }
}
