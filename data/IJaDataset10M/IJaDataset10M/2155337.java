package com.uk.ui.kontrata;

import com.uk.ui.CVerticalSplitPanel;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

public class KontrataListView extends CVerticalSplitPanel {

    public KontrataListView(KontrataList kontrataList, KontrataForm kontrataForm) {
        addStyleName("view");
        setFirstComponent(kontrataList);
        setSecondComponent(kontrataForm);
        setSplitPosition(40);
    }

    public void setKontrataList(KontrataList kontrataList) {
        setFirstComponent(kontrataList);
    }
}
