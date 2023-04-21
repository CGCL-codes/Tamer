package com.paulo.cursofacens.controller.cliente;

import com.paulo.cursofacens.bean.Cliente;
import com.paulo.cursofacens.bean.ClienteFisico;
import com.paulo.cursofacens.bean.ClienteJuridico;
import com.paulo.cursofacens.busines.ClienteBusiness;
import com.paulo.cursofacens.view.ClienteView;

/**
 *
 * @author visitante
 */
public class VisualizarClienteController extends ClienteView {

    protected Cliente clienteVisualizado;

    public VisualizarClienteController() {
    }

    public VisualizarClienteController(Integer codigo) {
        this.consultarCliente(codigo);
    }

    @Override
    protected void salvarCliente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void bloquearCliente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void cancelarCliente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void reativarCliente() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getTipoIdentificador() {
        return "CPF / CNPJ:";
    }

    @Override
    protected String getTituloJanela() {
        return "Visualizar Cliente";
    }

    @Override
    protected void consultarCliente() {
        Integer codigo = Integer.parseInt(txtCodigo.getText());
        this.consultarCliente(codigo);
    }

    protected void consultarCliente(Integer codigo) {
        clienteVisualizado = ClienteBusiness.getInstance().consultarClientePorCodigo(codigo);
        this.mostrarBeanCliente(clienteVisualizado);
    }

    @Override
    protected void mostrarBeanCliente(Cliente c) {
        if (c instanceof ClienteFisico) {
            ClienteFisico cf = (ClienteFisico) c;
            this.lblIdentificador.setText("CPF:");
            this.txtCpfCnpj.setText(cf.getCPF());
        } else if (c instanceof ClienteJuridico) {
            ClienteJuridico cj = (ClienteJuridico) c;
            this.lblIdentificador.setText("CNPJ:");
            this.txtCpfCnpj.setText(cj.getCnpj());
        } else {
            return;
        }
        super.mostrarBeanCliente(c);
    }
}
