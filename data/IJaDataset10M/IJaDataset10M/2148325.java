package br.com.mcampos.ejb.cloudsystem.user.address.addresstype;

import br.com.mcampos.dto.address.AddressTypeDTO;
import br.com.mcampos.ejb.cloudsystem.user.address.addresstype.entity.AddressType;
import br.com.mcampos.sysutils.SysUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class AddressTypeUtil {

    public AddressTypeUtil() {
        super();
    }

    public static AddressTypeDTO copy(AddressType entity) {
        return new AddressTypeDTO(entity.getId(), entity.getDescription());
    }

    public static AddressType createEntity(AddressTypeDTO dto) {
        AddressType entity = new AddressType(dto.getId());
        return copy(entity, dto);
    }

    public static AddressType copy(AddressType entity, AddressTypeDTO dto) {
        entity.setDescription(dto.getDescription());
        return entity;
    }

    public static List<AddressTypeDTO> toDTOList(List<AddressType> list) {
        if (SysUtils.isEmpty(list)) return Collections.emptyList();
        ArrayList<AddressTypeDTO> listDTO = new ArrayList<AddressTypeDTO>(list.size());
        for (AddressType m : list) {
            listDTO.add(copy(m));
        }
        return listDTO;
    }
}
