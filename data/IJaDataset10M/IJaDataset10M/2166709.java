package br.cefetrn.datinf.estoque.persistencia.sgbd;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import br.cefetrn.datinf.estoque.dominio.ItemVenda;
import br.cefetrn.datinf.estoque.dominio.Produto;
import br.cefetrn.datinf.estoque.negocio.CadastrosAdministrativos;

public class Main {

    public static void main(String[] args) {
        try {
            try {
                new CadastrosAdministrativos().inserirProduto(new Produto());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
