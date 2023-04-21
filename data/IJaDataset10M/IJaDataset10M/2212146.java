package classes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import objetos.FornecedorObj;
import interfaces.Fornecedor;

public class FornecedorImpl implements Fornecedor {

    @Override
    public boolean atualizarCadastro(FornecedorObj fornecedor) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            if (stm.execute("UPDATE fornecedores SET " + "nome = '" + fornecedor.getNome() + "'," + "endereco = '" + fornecedor.getEndereco() + "', " + "rg = '" + fornecedor.getRg() + "', " + "cpf = '" + fornecedor.getCpf() + "', " + "telefone_fixo = '" + fornecedor.getTelefoneFixo() + "', " + "telefone_cel = '" + fornecedor.getTelefoneCelular() + "', " + "skype = '" + fornecedor.getContatoSkype() + "', " + "email = '" + fornecedor.getEmail() + "'" + " WHERE id = " + fornecedor.getId())) return true; else return false;
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public FornecedorObj buscarFornecedorPorMarca(String nomeMarca) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            if (stm.execute("SELECT fornecedores.nome FROM marcas LEFT JOIN fornecedores " + "ON marcas.fornecedor_id = fornecedores.id WHERE marcas.nome = '" + nomeMarca + "'")) {
                ResultSet res = stm.getResultSet();
                res.next();
                String nome_for = res.getString(1);
                if (nome_for != null) {
                    FornecedorObj f = this.buscarFornecedor(nome_for);
                    return f;
                } else return null;
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public boolean efetuarCadastro(FornecedorObj fornecedor) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            if (stm.execute("INSERT INTO fornecedores VALUES (default, '" + fornecedor.getNome() + "', '" + fornecedor.getEndereco() + "', '" + fornecedor.getRg() + "', '" + fornecedor.getCpf() + "', '" + fornecedor.getTelefoneFixo() + "', '" + fornecedor.getTelefoneCelular() + "', '" + fornecedor.getContatoSkype() + "', '" + fornecedor.getEmail() + "')")) return true; else return false;
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public FornecedorObj buscarFornecedor(String nome) {
        try {
            DataSource.init();
            Connection con = DataSource.getConnection();
            Statement stm = con.createStatement();
            if (stm.execute("SELECT * FROM fornecedores WHERE nome = '" + nome + "'")) {
                ResultSet result = stm.getResultSet();
                if (result.next()) {
                    FornecedorObj rp = new FornecedorObj();
                    rp.setId(result.getInt(1));
                    rp.setNome(result.getString(2));
                    rp.setEndereco(result.getString(3));
                    rp.setRg(result.getString(4));
                    rp.setCpf(result.getString(5));
                    rp.setTelefoneFixo(result.getString(6));
                    rp.setTelefoneCelular(result.getString(7));
                    rp.setContatoSkype(result.getString(8));
                    rp.setEmail(result.getString(9));
                    return rp;
                } else return null;
            }
        } catch (IOException e) {
            System.out.println(e);
        } catch (SQLException e) {
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return null;
    }
}
