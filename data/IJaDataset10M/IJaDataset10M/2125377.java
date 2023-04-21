package com.farata.java_test.service;

import java.util.List;
import com.farata.java_test.dto.AssociateDTO;
import clear.cdb.annotations.CX_FillMethod;
import clear.cdb.annotations.CX_GenerateDataCollection;
import clear.cdb.annotations.CX_Service;

@CX_Service
public interface IAssociateService {

    @CX_GenerateDataCollection(collectionType = "com.farata.java_test.collections.AssociateCollection")
    @CX_FillMethod(autoSyncEnabled = true)
    List<AssociateDTO> getAssociates(Long companyId);
}
