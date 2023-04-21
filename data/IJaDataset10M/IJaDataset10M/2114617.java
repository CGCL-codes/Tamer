package com.bazaaroid.client.web.gwt.partnermodule.client.view.partnerinfo;

import com.bazaaroid.client.web.gwt.partnermodule.client.view.partnerinfo.impl.PartnerInfoTabViewImpl;
import com.google.gwt.user.client.ui.Composite;

public class PartnerGenericEntryView<DTO> extends Composite {

    private PartnerInfoTabViewImpl partnerInfoTabView;

    private DTO partnerGenericDTO;

    public PartnerGenericEntryView(PartnerInfoTabViewImpl partnerInfoTabView, DTO partnerGenericDTO) {
        this.partnerInfoTabView = partnerInfoTabView;
        this.partnerGenericDTO = partnerGenericDTO;
    }

    public DTO getPartnerGenericDTO() {
        return partnerGenericDTO;
    }

    public PartnerInfoTabViewImpl getPartnerInfoTabView() {
        return partnerInfoTabView;
    }
}
