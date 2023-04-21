package cz.cvut.phone.core.dto.adapter;

import cz.cvut.phone.core.dto.BankDTO;
import cz.cvut.phone.core.data.entity.BankEntity;

/**
 *
 * @author Frantisek Hradil
 */
public class BankDTOAdapter {

    public static BankEntity parseBankDTO(BankDTO bankDTO) {
        BankEntity bankEntity = new BankEntity();
        bankEntity.setBankID(new Integer(bankDTO.getBankID()));
        bankEntity.setName(bankDTO.getName());
        bankEntity.setVersion(bankDTO.getVersion());
        return bankEntity;
    }

    public static BankDTO parseBankEntity(BankEntity bankEntity) {
        BankDTO bankDTO = new BankDTO();
        String id = bankEntity.getBankID().toString();
        while (id.length() < 4) {
            id = "0" + id;
        }
        bankDTO.setBankID(id);
        bankDTO.setName(bankEntity.getName());
        bankDTO.setVersion(bankEntity.getVersion());
        return bankDTO;
    }
}
