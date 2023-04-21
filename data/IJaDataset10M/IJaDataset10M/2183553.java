package classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import objetos.ClienteObj;
import interfaces.Cliente;

public class ClienteImpl implements Cliente {

    @Override
    public boolean atualizarDadosCliente(ClienteObj cliente) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            stm.execute("UPDATE clientes SET " + "nome = " + QueryAssist.stringValue(cliente.getNome()) + QueryAssist.comma + "sobrenome = " + QueryAssist.stringValue(cliente.getSobrenome()) + QueryAssist.comma + "sexo = " + QueryAssist.charValue(cliente.getSexo()) + QueryAssist.comma + "telefone_residencial = " + QueryAssist.stringValue(cliente.getTelefone_residencial()) + QueryAssist.comma + "telefone_comercial = " + QueryAssist.stringValue(cliente.getTelefone_comercial()) + QueryAssist.comma + "telefone_celular = " + QueryAssist.stringValue(cliente.getTelefone_celular()) + QueryAssist.comma + "cep = " + QueryAssist.stringValue(cliente.getCep()) + QueryAssist.comma + "endereco = " + QueryAssist.stringValue(cliente.getEndereco()) + QueryAssist.comma + "bairro = " + QueryAssist.stringValue(cliente.getBairro()) + QueryAssist.comma + "numero = " + cliente.getNumero() + QueryAssist.comma + "complemento = " + QueryAssist.stringValue(cliente.getComplemento()) + QueryAssist.comma + "cidade = " + QueryAssist.stringValue(cliente.getCidade()) + QueryAssist.comma + "estado = " + QueryAssist.stringValue(cliente.getEstado()) + QueryAssist.comma + "rg = " + QueryAssist.stringValue(cliente.getRg()) + QueryAssist.comma + "cpf = " + QueryAssist.stringValue(cliente.getCpf()) + QueryAssist.comma + "email = " + QueryAssist.stringValue(cliente.getEmail()) + QueryAssist.comma + "senha_acesso = " + QueryAssist.stringValue(cliente.getSenha()) + "WHERE id = " + cliente.getId());
            return true;
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean efetuarCadastro(ClienteObj cliente) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            stm.execute("INSERT INTO clientes VALUES (default," + QueryAssist.stringValue(cliente.getNome()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getSobrenome()) + QueryAssist.comma + QueryAssist.charValue(cliente.getSexo()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getTelefone_residencial()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getTelefone_comercial()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getTelefone_celular()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getCep()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getEndereco()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getBairro()) + QueryAssist.comma + cliente.getNumero() + QueryAssist.comma + QueryAssist.stringValue(cliente.getComplemento()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getCidade()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getEstado()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getRg()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getCpf()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getEmail()) + QueryAssist.comma + QueryAssist.stringValue(cliente.getSenha()) + ")");
            return true;
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean efetuarLogin(String login, String senha) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            if (stm.execute("SELECT email, senha_acesso FROM clientes WHERE email = " + QueryAssist.stringValue(login))) {
                ResultSet res = stm.getResultSet();
                if (res.next()) {
                    if (login.compareTo(res.getString(1)) == 0 && senha.compareTo(res.getString(2)) == 0) return true; else return false;
                } else return false;
            } else return false;
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public void efetuarLogout() {
    }
}
