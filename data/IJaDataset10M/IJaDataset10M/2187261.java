package org.ziggurat.fenix.web.clientes;

import java.util.Arrays;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.ziggurat.fenix.common.database.hibernate.HibClienteRepository;
import org.ziggurat.fenix.common.modelo.clientes.Cliente;
import org.ziggurat.fenix.common.modelo.clientes.Responsabilidad;
import org.ziggurat.fenix.common.modelo.clientes.TiposDocumentos;
import org.ziggurat.fenix.web.FocusOnBehavior;

public class NuevoCliente extends AbstractClientesPage {

    private Cliente cliente;

    public NuevoCliente() {
        cliente = new Cliente();
        add(new NuevoClienteForm("nuevoClienteForm"));
    }

    private class NuevoClienteForm extends Form {

        public NuevoClienteForm(String id) {
            super(id);
            CompoundPropertyModel<Cliente> model = new CompoundPropertyModel<Cliente>(cliente);
            this.setModel(model);
            TextField<Cliente> nombre = new TextField<Cliente>("nombre");
            nombre.setRequired(true);
            nombre.add(new FocusOnBehavior());
            add(nombre);
            DropDownChoice<TiposDocumentos> tipo_doc = new DropDownChoice<TiposDocumentos>("tipo_doc", Arrays.asList(TiposDocumentos.values()));
            tipo_doc.setRequired(true);
            add(tipo_doc);
            TextField<Cliente> documento = new TextField<Cliente>("documento");
            documento.setRequired(true);
            add(documento);
            TextField<Cliente> domicilio = new TextField<Cliente>("domicilio");
            domicilio.setRequired(true);
            add(domicilio);
            TextField<Cliente> localidad = new TextField<Cliente>("localidad");
            add(localidad);
            TextField<Cliente> telefono = new TextField<Cliente>("telefono");
            add(telefono);
            DropDownChoice<Responsabilidad> responsabilidad = new DropDownChoice<Responsabilidad>("responsabilidad", Arrays.asList(Responsabilidad.values()));
            responsabilidad.setRequired(true);
            add(responsabilidad);
            TextField<Cliente> fax = new TextField<Cliente>("fax");
            add(fax);
            TextField<Cliente> celular = new TextField<Cliente>("celular");
            add(celular);
            TextField email = new TextField("email");
            add(email);
            add(new FeedbackPanel("feedback"));
        }

        @Override
        protected void onSubmit() {
            HibClienteRepository repo = new HibClienteRepository();
            repo.makePersistent(cliente);
            setResponsePage(ListadoDeClientes.class);
        }
    }
}
