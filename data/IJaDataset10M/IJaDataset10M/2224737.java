package br.unb.cic.gerval.client.componentes;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class PopUpAvisoConfirmacao extends PopupAviso {

    private ClickListener clickListener;

    private Button bCancel = new Button("Cancelar");

    public PopUpAvisoConfirmacao() {
        super("Confirma opera��o ?");
        bCancel.addClickListener(this);
        VerticalPanel v = (VerticalPanel) this.getWidget();
        HorizontalPanel hp = (HorizontalPanel) v.getWidget(1);
        hp.add(bCancel);
        v.setWidth("150px");
        v.setHeight("20px");
    }

    public PopUpAvisoConfirmacao(ClickListener arg0) {
        this();
        this.clickListener = arg0;
    }

    public void onClick(Widget arg0) {
        if (arg0 instanceof HyperlinkConfirmacao || arg0 instanceof ButtonConfirmacao) {
            super.setPopupPosition(Window.getClientWidth() / 2, Window.getClientHeight() / 2);
            this.show();
        } else {
            Button botao = (Button) arg0;
            if (botao.equals(this.getBotaoOK())) {
                clickListener.onClick(arg0);
            }
            super.onClick(arg0);
        }
    }
}
