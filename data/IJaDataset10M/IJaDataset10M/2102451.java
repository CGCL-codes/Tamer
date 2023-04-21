package br.com.promove.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import br.com.promove.application.PromoveApplication;
import br.com.promove.entity.Ctrc;
import br.com.promove.exception.PromoveException;
import br.com.promove.service.CtrcService;
import br.com.promove.service.ExportacaoService;
import br.com.promove.service.ServiceFactory;
import br.com.promove.view.form.BaseForm;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

public class AverbacaoView extends BaseForm {

    private VerticalLayout layout = new VerticalLayout();

    private Button gerar;

    private PopupDateField txtDe;

    private PopupDateField txtAte;

    private PromoveApplication app;

    private ExportacaoService exportacaoService;

    public AverbacaoView(PromoveApplication app) {
        this.app = app;
        exportacaoService = ServiceFactory.getService(ExportacaoService.class);
        buildView();
    }

    private void buildView() {
        setWriteThrough(false);
        setImmediate(true);
        setSizeFull();
        txtDe = new PopupDateField("De");
        txtDe.setLocale(new Locale("pt", "BR"));
        txtDe.setResolution(DateField.RESOLUTION_DAY);
        txtAte = new PopupDateField("Até");
        txtAte.setLocale(new Locale("pt", "BR"));
        txtAte.setResolution(DateField.RESOLUTION_DAY);
        gerar = new Button("Gerar", new AverbacaoListener(this));
        Label label = new Label("<h3>Gerar Averbação</h3>");
        label.setContentMode(Label.CONTENT_XHTML);
        layout.addComponent(label);
        layout.addComponent(this);
        addField("txtDe", txtDe);
        addField("txtAte", txtAte);
        layout.addComponent(createFooter());
        layout.addComponent(gerar);
        layout.setSpacing(true);
        layout.setMargin(false, true, false, true);
    }

    private Component createFooter() {
        HorizontalLayout footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(gerar);
        footer.setVisible(true);
        return footer;
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public void setLayout(VerticalLayout layout) {
        this.layout = layout;
    }

    class AverbacaoListener implements ClickListener {

        private AverbacaoView view;

        public AverbacaoListener(AverbacaoView view) {
            this.view = view;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            try {
                Date de = txtDe.getValue() != null ? (Date) txtDe.getValue() : null;
                Date ate = txtAte.getValue() != null ? (Date) txtAte.getValue() : null;
                String fileName = "averbacao_" + new SimpleDateFormat("ddMMyyyy").format(de) + "_" + new SimpleDateFormat("ddMMyyyy").format(ate) + ".xls";
                if (de == null || ate == null) throw new IllegalArgumentException("Informe um período para busca");
                String file = "";
                try {
                    file = exportacaoService.exportarXLSAverbacao(fileName, de, ate, true);
                } catch (PromoveException e) {
                    showErrorMessage(view, e.getMessage());
                    e.printStackTrace();
                }
                WebApplicationContext ctx = (WebApplicationContext) app.getContext();
                String path = ctx.getHttpSession().getServletContext().getContextPath();
                event.getButton().getWindow().open(new ExternalResource(path + "/export?action=export_excel&fileName=" + fileName + "&file=" + file));
            } catch (IllegalArgumentException ie) {
                showErrorMessage(view, ie.getMessage());
            }
        }
    }
}
