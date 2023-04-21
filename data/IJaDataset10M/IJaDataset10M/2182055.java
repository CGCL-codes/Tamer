package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import util.DBUtil;
import business.Cliente;

public class ClienteDao {

    private static final Logger log = Logger.getLogger(ClienteDao.class);

    public void adicionarCliente(Cliente cliente) {
        String sql1 = "INSERT INTO pessoa(i_idPessoa,s_nome,c_tPessoa,i_tFixo,i_tCel,s_end) values";
        sql1 += "(";
        if (cliente.getIdPessoa() == 0) {
            sql1 += "null";
            log.debug(" meu idPessoa � nulo ");
        } else {
            sql1 += cliente.getIdPessoa();
            log.debug(" meu idPessoa �  " + cliente.getIdPessoa());
        }
        sql1 += ",";
        sql1 += "'" + cliente.getNome() + "'";
        sql1 += ",";
        sql1 += "'" + cliente.getTPessoa() + "'";
        sql1 += ",";
        sql1 += cliente.getTFixo();
        sql1 += ",";
        sql1 += cliente.getTCel();
        sql1 += ",";
        sql1 += "'" + cliente.getEndereco() + "'";
        sql1 += ");";
        String sql2 = "INSERT INTO fisica(s_cpf,Pessoa_i_idPessoa,dt_nasc,s_rg) values";
        sql2 += "(";
        sql2 += "'" + cliente.getCpf() + "'";
        sql2 += ",";
        if (cliente.getIdPessoa() == 0) {
            log.debug("id do cliente igual a zero");
            sql2 += "(SELECT MAX(i_idPessoa) FROM pessoa)";
        } else {
            sql2 += cliente.getIdPessoa();
        }
        sql2 += ",";
        if (cliente.getDt_Nasc().length() == 10) {
            log.debug("parametro correto" + cliente.getDt_Nasc());
            sql2 += "'" + cliente.getDt_Nasc() + "'";
        } else {
            sql2 += "'2008-01-01'";
            log.debug("parametro incorreto" + cliente.getDt_Nasc());
        }
        sql2 += ",";
        sql2 += "'" + cliente.getRg() + "'";
        sql2 += ");";
        try {
            Connection con = DBUtil.getConnection();
            Statement st = null;
            st = con.createStatement();
            log.debug("sql1:" + sql1);
            log.debug("sql2:" + sql2);
            st.executeUpdate(sql1);
            st.executeUpdate(sql2);
            st.executeUpdate("commit");
            st.close();
        } catch (SQLException e) {
            log.error("SQLException", e);
        }
    }

    public void removerCliente(String cpf) {
        String sql2 = "DELETE FROM pessoa WHERE i_idPessoa = " + "(select Pessoa_i_idPessoa from fisica where s_cpf";
        sql2 += "='";
        sql2 += cpf;
        sql2 += "');";
        log.debug(sql2);
        String sql1 = "DELETE FROM fisica WHERE s_cpf ='";
        sql1 += cpf;
        sql1 += "';";
        log.debug(sql1);
        try {
            Connection con = DBUtil.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql2);
            stmt.executeUpdate("commit");
            stmt.executeUpdate(sql1);
            stmt.executeUpdate("commit");
            stmt.close();
        } catch (SQLException e) {
            log.debug("SQLException", e);
        }
    }

    public int lastReqPessoa() {
        String sql = "SELECT MAX(i_idPessoa) FROM pessoa";
        Connection con = DBUtil.getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int a = rs.getInt(1);
            rs.close();
            stmt.close();
            return a;
        } catch (SQLException e) {
            log.error("SQLException", e);
            return 0;
        }
    }

    public ArrayList listarClientes(String cpf) {
        String sql = "select * FROM pessoa,fisica WHERE " + "fisica.s_cpf = '";
        sql += cpf;
        sql += "' and pessoa.i_idPessoa = fisica.Pessoa_i_idPessoa";
        try {
            Connection con = DBUtil.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ArrayList clientes = new ArrayList();
            log.debug("criei o arraylist " + clientes);
            while (rs.next()) {
                log.debug("to no while " + clientes);
                Cliente p = montaCliente(rs);
                clientes.add(p);
                log.debug("adicionei cliente " + clientes.size());
            }
            rs.close();
            stmt.close();
            log.debug("passei depois do while" + clientes);
            return clientes;
        } catch (SQLException e) {
            log.error("SQLException", e);
            return null;
        }
    }

    private Cliente montaCliente(ResultSet rs) throws SQLException {
        int id = rs.getInt("pessoa.i_idPessoa");
        log.debug("id_pessoa_will " + id);
        int tCel = rs.getInt("pessoa.i_tCel");
        int tFixo = rs.getInt("pessoa.i_tFixo");
        String cpf = rs.getString("fisica.s_cpf");
        log.debug("id_cpf_will " + cpf);
        String rg = rs.getString("fisica.s_rg");
        log.debug("id_rg_will " + rg);
        String dt_Nasc = rs.getString("fisica.dt_nasc");
        log.debug("id_nasc_will " + dt_Nasc);
        String tPessoa = rs.getString("pessoa.c_tPessoa");
        String nome = rs.getString("pessoa.s_nome");
        log.debug("pessoa_will ");
        String endereco = rs.getString("pessoa.s_end");
        Cliente c = new Cliente();
        c.setIdPessoa(id);
        c.setNome(nome);
        c.setTCel(tCel);
        c.setEndereco(endereco);
        c.setTFixo(tFixo);
        c.setTPessoa(tPessoa);
        c.setCpf(cpf);
        c.setDt_Nasc(dt_Nasc);
        c.setRg(rg);
        return c;
    }
}
