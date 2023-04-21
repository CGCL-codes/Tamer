package opencafe.models;

import opencafe.Main;
import opencafe.banco.Banco;
import opencafe.banco.listener.BancoDataChangedAdapter;
import opencafe.beans.Cliente;
import darkside.sombriks.engines.form.AbstractComboModel;
import darkside.sombriks.exception.ChainException;

public class ClienteComboModel extends AbstractComboModel<Cliente> implements BancoDataChangedAdapter {

    private String parteNome;

    private int totRegistros = 10;

    public ClienteComboModel() {
        Main.getInstancia().getBanco().addBancoDataChangedListener(this);
    }

    @Override
    public String extractLabel(Cliente thing) {
        return thing.getDescricao();
    }

    @Override
    public Cliente get(int idx) throws ChainException {
        if (getSize() > 0) return Main.getInstancia().getBanco().listClientes(parteNome, totRegistros).get(idx); else return null;
    }

    @Override
    public int getSize() {
        return Main.getInstancia().getBanco().listClientes(parteNome, totRegistros).size();
    }

    @Override
    public void dataChanged(Banco self) {
        try {
            fireUpdate();
        } catch (ChainException e) {
            e.printStackTrace();
        }
    }

    public void setParteNome(String parteNome) throws ChainException {
        this.parteNome = parteNome;
        fireUpdate();
    }
}
