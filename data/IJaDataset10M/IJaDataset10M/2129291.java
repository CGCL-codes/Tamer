package etp.client.gui;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import etp.client.Projeto;
import etp.client.rpc.ProjetoAdminService;
import etp.client.rpc.ProjetoAdminServiceAsync;

public class ProjetoWidget extends AbsolutePanel {

    private FlowPanel pnlOptions = new FlowPanel();

    private Button btnRemover = new Button();

    private Button btnEditar = new Button();

    private Image imgPrjImage = new Image("images/projeto.png");

    private Image imgResp = new Image();

    private Label lblNome = new Label();

    private Label lblCod = new Label();

    private Hyperlink hplResp = new Hyperlink();

    private Projeto model;

    private List<ButtonsActionHandler> buttonHandlers = new ArrayList<ButtonsActionHandler>();

    private ProjetoAdminServiceAsync projService = GWT.create(ProjetoAdminService.class);

    public ProjetoWidget(Projeto prj) {
        this.getElement().setClassName("ProjetoWidget");
        this.lblNome.getElement().setId("lblNome");
        this.lblCod.getElement().setId("lblCod");
        this.hplResp.getElement().setId("hplResp");
        this.btnEditar.getElement().setId("btnEditar");
        this.btnRemover.getElement().setId("btnRemover");
        this.pnlOptions.getElement().setId("pnlOptions");
        DockPanel pnlMain = new DockPanel();
        DockPanel pnlContent = new DockPanel();
        pnlContent.add(this.lblCod, DockPanel.NORTH);
        pnlContent.add(this.lblNome, DockPanel.NORTH);
        FlowPanel pnlPrjID = new FlowPanel();
        pnlPrjID.add(this.lblCod);
        pnlPrjID.add(this.lblNome);
        pnlContent.add(pnlPrjID, DockPanel.NORTH);
        pnlMain.add(this.imgPrjImage, DockPanel.WEST);
        pnlMain.add(pnlContent, DockPanel.CENTER);
        this.add(pnlMain);
        this.addDomHandler(new ActDoubleClick(), DoubleClickEvent.getType());
        this.addDomHandler(new ActMouseOver(), MouseOverEvent.getType());
        this.addDomHandler(new ActMouseOut(), MouseOutEvent.getType());
        this.setModel(prj);
    }

    public static native void teste(String teste);

    public void setNome(String nome) {
        this.lblNome.setText(nome);
        this.model.setNome(nome);
    }

    public void setResp(String resp, String token) {
        this.hplResp.setTitle(resp);
        this.hplResp.setTargetHistoryToken(token);
    }

    public String getNome() {
        return this.lblNome.getText();
    }

    public String getResp() {
        return this.hplResp.getText();
    }

    public String getRespTarget() {
        return this.hplResp.getTargetHistoryToken();
    }

    /**
	 * @return the lblCod
	 */
    public String getCod() {
        return lblCod.getText();
    }

    /**
	 * @param lblCod the lblCod to set
	 */
    public void setCod(String cod) {
        this.lblCod.setText(cod);
    }

    protected class ActRemover implements ClickHandler {

        @Override
        public void onClick(ClickEvent event) {
            ProjetoWidget.this.projService.remover(ProjetoWidget.this.getCod(), new AsyncCallback<String>() {

                @Override
                public void onSuccess(String result) {
                }

                @Override
                public void onFailure(Throwable caught) {
                }
            });
        }
    }

    public Projeto getModel() {
        return model;
    }

    public void setModel(Projeto model) {
        this.model = model;
    }

    public void updateFromModel() {
        this.lblCod.setText(model.getCod());
        this.lblNome.setText(model.getNome());
    }

    public void addButtonsHandler(ButtonsActionHandler handler) {
        this.buttonHandlers.add(handler);
    }

    public boolean removeButtonsHandler(ButtonsActionHandler handler) {
        return this.buttonHandlers.remove(handler);
    }

    protected class ActMouseOver implements MouseOverHandler {

        public void onMouseOver(MouseOverEvent event) {
            String classNames = ProjetoWidget.this.getStyleName();
            if (!classNames.contains(" ProjetoWidgetOver")) {
                classNames += " ProjetoWidgetOver";
                ProjetoWidget.this.setStyleName(classNames);
                ProjetoWidget.this.pnlOptions.setVisible(true);
            }
        }
    }

    protected class ActMouseOut implements MouseOutHandler {

        public void onMouseOut(MouseOutEvent event) {
            String classNames = ProjetoWidget.this.getStyleName();
            classNames = classNames.replace(" ProjetoWidgetOver", "");
            ProjetoWidget.this.setStyleName(classNames);
            ProjetoWidget.this.pnlOptions.setVisible(false);
        }
    }

    protected class ActButtons implements ClickHandler {

        public void onClick(ClickEvent event) {
            if (event.getSource() == ProjetoWidget.this.btnEditar) {
                for (ButtonsActionHandler bah : ProjetoWidget.this.buttonHandlers) {
                    bah.editClicked(ProjetoWidget.this);
                }
            } else if (event.getSource() == ProjetoWidget.this.btnRemover) {
                for (ButtonsActionHandler bah : ProjetoWidget.this.buttonHandlers) {
                    bah.removeClicked(ProjetoWidget.this);
                }
            }
        }
    }

    protected class ActDoubleClick implements DoubleClickHandler {

        @Override
        public void onDoubleClick(DoubleClickEvent event) {
            for (ButtonsActionHandler bah : ProjetoWidget.this.buttonHandlers) {
                bah.editClicked(ProjetoWidget.this);
            }
        }
    }

    public interface ButtonsActionHandler {

        public void editClicked(ProjetoWidget w);

        public void removeClicked(ProjetoWidget w);
    }
}
