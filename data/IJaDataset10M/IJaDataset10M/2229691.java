package br.com.mcampos.ejb.cloudsystem.user.attribute.companyposition;

import br.com.mcampos.dto.user.attributes.CompanyPositionDTO;
import br.com.mcampos.ejb.cloudsystem.user.attribute.companyposition.entity.CompanyPosition;
import br.com.mcampos.sysutils.SysUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompanyPositionUtil {

    public CompanyPositionUtil() {
        super();
    }

    public static CompanyPosition createEntity(CompanyPositionDTO dto) {
        if (dto == null) return null;
        CompanyPosition entity = new CompanyPosition(dto.getId());
        return update(entity, dto);
    }

    public static CompanyPosition update(CompanyPosition entity, CompanyPositionDTO dto) {
        if (dto == null) return null;
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static CompanyPositionDTO copy(CompanyPosition entity) {
        if (entity != null) return new CompanyPositionDTO(entity.getId(), entity.getDescription()); else return null;
    }

    public static List<CompanyPositionDTO> toDTOList(List<CompanyPosition> list) {
        if (SysUtils.isEmpty(list)) return Collections.emptyList();
        ArrayList<CompanyPositionDTO> listDTO = new ArrayList<CompanyPositionDTO>(list.size());
        for (CompanyPosition m : list) {
            listDTO.add(copy(m));
        }
        return listDTO;
    }
}
