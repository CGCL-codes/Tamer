package py.progweb.sgc.gui.client.dialog;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public abstract class FormularioBase extends VentanaBase {

    VLayout layout;

    DynamicForm form;

    Layout btnLayout;

    VLayout btnBar;

    AsyncCallback<Void> callback;

    VentanaBase padre;

    public FormularioBase(String titulo, VentanaBase padre) {
        super(titulo);
        this.padre = padre;
        setIsModal(true);
        layout = new VLayout();
        form = new DynamicForm();
        btnLayout = new Layout();
        btnBar = new VLayout();
        layout.setWidth100();
        layout.setAlign(Alignment.CENTER);
        btnBar.setWidth("100%");
        btnBar.setBackgroundColor("#cccccc");
        btnBar.setAlign(VerticalAlignment.CENTER);
        btnBar.setHeight(30);
        btnLayout.setVertical(false);
        btnLayout.setBackgroundColor("#cccccc");
        btnLayout.setWidth100();
        btnLayout.setHeight(20);
        btnLayout.setAlign(Alignment.CENTER);
        btnLayout.setMembersMargin(10);
        form.setPadding(20);
        form.setValidateOnChange(true);
        IButton btnGuardar = new IButton("Guardar");
        btnGuardar.setIcon("/sgc-web/images/save.gif");
        btnGuardar.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (form.validate()) FormularioBase.this.guardar(); else Window.alert("Por favor, corrija los errores de validación");
            }
        });
        btnLayout.addMember(btnGuardar);
        IButton btnCancelar = new IButton("Salir");
        btnCancelar.setIcon("/sgc-web/images/cancel.gif");
        btnCancelar.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                FormularioBase.this.hide();
            }
        });
        btnLayout.addMember(btnCancelar);
        btnBar.addMember(btnLayout);
        layout.addMember(form);
        layout.addMember(btnBar);
        this.addItem(layout);
        callback = new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                manejarError(caught);
            }

            @Override
            public void onSuccess(Void result) {
                Window.alert("Los datos fueron guardados");
                FormularioBase.this.hide();
                FormularioBase.this.padre.reset();
            }
        };
    }

    protected void setItems(FormItem... items) {
        form.setItems(items);
    }

    protected abstract void guardar();
}
