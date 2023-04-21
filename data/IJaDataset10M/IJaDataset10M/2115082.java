package br.com.klis.batendoumabola.client.widgets;

import br.com.klis.batendoumabola.client.resources.Resources;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FooterHorizontalPanel extends Composite {

    public FooterHorizontalPanel() {
        super();
        GWT.log("init footer panel...", null);
        VerticalPanel footer = new VerticalPanel();
        footer.setSize("100%", "100%");
        footer.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        Label texto = new Label("Rede social para integração de jogadores, organizadores, torcedores, familiares, patrocinadores, empresários, enfim, todos os envolvidos no apaixonante mundo dos bate-bolas.");
        Label rodape = new Label("@2010 - Desenvolvido por José Inacio da Silva Junior - inacio@klis.com.br");
        Image imagem = new Image(Resources.INSTANCE.appengine());
        footer.add(texto);
        footer.add(rodape);
        footer.add(imagem);
        initWidget(footer);
    }
}
