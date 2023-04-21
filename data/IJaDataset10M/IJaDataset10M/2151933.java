package br.com.mcampos.ejb.cloudsystem.resale.dealer;

import br.com.mcampos.dto.resale.DealerDTO;
import br.com.mcampos.ejb.cloudsystem.resale.ResaleUtil;
import br.com.mcampos.ejb.cloudsystem.resale.dealer.entity.Dealer;
import br.com.mcampos.ejb.cloudsystem.resale.dealer.type.DealerTypeUtil;
import br.com.mcampos.ejb.cloudsystem.resale.entity.Resale;
import br.com.mcampos.ejb.cloudsystem.user.UserUtil;
import br.com.mcampos.ejb.cloudsystem.user.person.entity.Person;
import br.com.mcampos.sysutils.SysUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class DealerUtil {

    public static Dealer createEntity(Resale resale, Person person, DealerDTO dto) {
        if (dto == null) return null;
        Dealer entity = new Dealer(resale);
        entity.setDealer(person);
        return update(entity, dto);
    }

    public static Dealer update(Dealer entity, DealerDTO dto) {
        if (dto == null) return null;
        entity.setDealerType(DealerTypeUtil.createEntity(dto.getType()));
        return entity;
    }

    public static DealerDTO copy(Dealer entity) {
        if (entity == null) return null;
        DealerDTO dto = new DealerDTO();
        dto.setSequence(entity.getSequence());
        dto.setPerson(UserUtil.copy(entity.getDealer()));
        dto.setResale(ResaleUtil.copy(entity.getResale()));
        dto.setType(DealerTypeUtil.copy(entity.getDealerType()));
        return dto;
    }

    public static List<DealerDTO> toDTOList(List<Dealer> list) {
        if (SysUtils.isEmpty(list)) return Collections.emptyList();
        ArrayList<DealerDTO> listDTO = new ArrayList<DealerDTO>(list.size());
        for (Dealer m : list) {
            listDTO.add(copy(m));
        }
        return listDTO;
    }

    public static List<DealerDTO> toDTOList(List<Dealer> list, Integer dealerType) {
        if (SysUtils.isEmpty(list)) return Collections.emptyList();
        ArrayList<DealerDTO> listDTO = new ArrayList<DealerDTO>(list.size());
        for (Dealer m : list) {
            if (m.getDealerType().getId().equals(dealerType)) listDTO.add(copy(m));
        }
        return listDTO;
    }
}
