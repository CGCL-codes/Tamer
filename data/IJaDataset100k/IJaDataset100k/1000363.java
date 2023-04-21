package com.wrupple.muba.catalogs.client.module.services.logic;

import java.util.Set;
import com.wrupple.muba.common.client.application.DataCallback;
import com.wrupple.muba.common.shared.StateTransition;
import com.wrupple.vegetate.domain.CatalogDescriptor;

public interface CatalogDescriptionService {

    String LIST_ACTION_TOKEN = "list";

    void loadCatalogDescriptor(final String catalogId, final StateTransition<CatalogDescriptor> onDone);

    CatalogDescriptor loadFromCache(String catalog);

    void loadCatalogNames(DataCallback<Set<String>> callback);
}
