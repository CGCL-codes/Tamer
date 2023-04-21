package com.wrupple.muba.catalogs.client.module.services;

import java.util.List;
import com.wrupple.vegetate.client.module.services.SerializationService;
import com.wrupple.vegetate.domain.CatalogResponseContract;

public interface CatalogDeletingSerializer extends SerializationService<List<String>, CatalogResponseContract> {
}
