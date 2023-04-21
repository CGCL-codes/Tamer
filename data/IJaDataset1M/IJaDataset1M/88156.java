package com.vmware.spring.workshop.services.facade;

import java.util.List;
import com.vmware.spring.workshop.dto.IdentifiedDTO;

/**
 * @author lgoldstein
 */
public interface CommonFacadeActions<DTO extends IdentifiedDTO> {

    Class<DTO> getDTOClass();

    List<DTO> findAll();

    DTO findById(Long id);

    void create(DTO dto);

    void update(DTO dto);

    void delete(DTO dto);

    DTO deleteById(Long id);
}
