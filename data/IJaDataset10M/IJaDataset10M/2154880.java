package gui.janelas;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author 0213101
 */
public class JanelaLogin extends LoginForm {

    public JanelaLogin() {
        this.setWidth(5, UNITS_CM);
        this.setHeight(5, UNITS_CM);
        this.setCaption("Entrar no SRS");
        Label lLogin = new Label("Login", Label.CONTENT_XHTML);
        Label lSenha = new Label("Senha", Label.CONTENT_XHTML);
        TextField txLogin = new TextField();
        TextField txSenha = new TextField();
        Button bEntrar = new Button("Entrar");
        bEntrar.addListener(new EventoBotaoLogar());
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(lLogin);
        layout.addComponent(txLogin);
        layout.addComponent(lSenha);
        layout.addComponent(txSenha);
        layout.addComponent(bEntrar);
    }

    private class EventoBotaoLogar implements Button.ClickListener {

        @Override
        public void buttonClick(ClickEvent event) {
        }
    }
}
