package com.wrupple.muba.catalogs.client.activity.process.state;

import com.google.inject.Provider;
import com.wrupple.muba.catalogs.client.module.services.presentation.CatalogPlaceInterpret;
import com.wrupple.muba.catalogs.domain.CatalogProcessDescriptor;
import com.wrupple.muba.common.domain.DesktopPlace;
import com.wrupple.muba.common.shared.State;

public interface BrowsePlaceInterpret extends State.ContextAware<DesktopPlace, CatalogProcessDescriptor> {

    Provider<String> getCatalogIdProvider();

    Provider<String> getOriginalEntryIdProvider();

    void setPlaceInterpret(CatalogPlaceInterpret cpi);
}
