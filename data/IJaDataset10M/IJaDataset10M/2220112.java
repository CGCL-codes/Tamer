package br.com.promove.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import br.com.promove.application.PromoveApplication;
import br.com.promove.service.ImportacaoService;
import br.com.promove.service.ServiceFactory;
import br.com.promove.view.form.BaseForm;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;

public class ImportCtrcView extends BaseForm implements Serializable {

    private VerticalLayout layout = new VerticalLayout();

    private Button import_from_server;

    private ImportacaoService importService;

    private PopupDateField txtDe;

    private PopupDateField txtAte;

    private PromoveApplication app;

    public ImportCtrcView(PromoveApplication app) {
        this.app = app;
        buildLayout();
        importService = ServiceFactory.getService(ImportacaoService.class);
    }

    private void buildLayout() {
        txtDe = new PopupDateField("De");
        txtDe.setLocale(new Locale("pt", "BR"));
        txtDe.setResolution(DateField.RESOLUTION_DAY);
        txtAte = new PopupDateField("Até");
        txtAte.setLocale(new Locale("pt", "BR"));
        txtAte.setResolution(DateField.RESOLUTION_DAY);
        layout.setSpacing(true);
        layout.setMargin(false, true, false, true);
        Label label = new Label("<h3>Importar do WebService</h3>");
        label.setContentMode(Label.CONTENT_XHTML);
        addField("txtDe", txtDe);
        addField("txtAte", txtAte);
        layout.addComponent(label);
        layout.addComponent(this);
        setImmediate(true);
        import_from_server = new Button("Importar", new ImportFromServerListener(this));
        layout.addComponent(import_from_server);
    }

    public VerticalLayout getLayout() {
        return layout;
    }

    public void setLayout(VerticalLayout layout) {
        this.layout = layout;
    }

    class ImportFromServerListener implements ClickListener {

        private ImportCtrcView view;

        public ImportFromServerListener(ImportCtrcView view) {
            this.view = view;
        }

        @Override
        public void buttonClick(ClickEvent event) {
            try {
                Date de = txtDe.getValue() != null ? (Date) txtDe.getValue() : null;
                Date ate = txtAte.getValue() != null ? (Date) txtAte.getValue() : null;
                if (de == null || ate == null) throw new IllegalArgumentException("Informe um período para busca");
                WebApplicationContext ctx = (WebApplicationContext) app.getContext();
                String url = ctx.getHttpSession().getServletContext().getInitParameter("ctrc_ws_url");
                importService.importarGabardo(url + "?dataIni=" + new SimpleDateFormat("yyyy-MM-dd").format(de) + "&dataFim=" + new SimpleDateFormat("yyyy-MM-dd").format(ate));
            } catch (IllegalArgumentException ie) {
                showErrorMessage(view, ie.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
