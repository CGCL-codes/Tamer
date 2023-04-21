package com.uk.ui.fatura;

import com.uk.data.entities.Kontrata;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class KontrataInfo extends VerticalLayout {

    Label label = new Label();

    public KontrataInfo() {
    }

    public void clearKontrata() {
        this.removeComponent(label);
    }

    public void loadKontrata(Kontrata kontrata) {
        this.clearKontrata();
        if (kontrata == null) return;
        String result = "ID:<strong>" + kontrata.getId() + "</strong>" + "<br/>Emri:<strong>" + kontrata.getEmri() + "</strong>" + "<br/>Mbiemri:<strong>" + kontrata.getMbiemri() + "</strong>" + "<br/>Adresa:<strong>" + kontrata.getAdresa() + "</strong>" + "<br/>Perdorimi:<strong>" + kontrata.getPerdorimi().getTag() + "</strong>";
        label = new Label(result);
        label.setContentMode(Label.CONTENT_XHTML);
        this.addComponent(label);
    }
}
