package br.com.netxcall.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import br.com.netxcall.conexao.Conexao;
import br.com.netxcall.dominio.Recado;

public class RecadoDAO {

    private static final String INSERIR = "insert into recado (id, mensagem,idFuncionario, lido, contato, telefone) VALUES (?,?,?,false,?,?)";

    private static final String LISTAR_DESTINATARIO = "SELECT count(idFuncionario) as qtd,idFuncionario,lido, fun.nome  from recado rec " + " inner join registro.funcionario fun on (rec.idFuncionario = fun.id) " + " where lido = false group by idFuncionario having (lido = false)";

    private static final String LISTAR_RECADO_POR_NOME = "select mensagem, idFuncionario,contato,telefone,recado.id, func.nome from registro.recado recado" + " inner join registro.funcionario func on (recado.idFuncionario = func.id) " + " where recado.idFuncionario = ? and recado.lido = false";

    private static final String ALTERAR_STATUS_RECADO = "update recado SET lido = true WHERE id = ?";

    Conexao conexao = new Conexao();

    Connection conn = null;

    public RecadoDAO() {
    }

    public int cadastrarRecado(Recado recado) {
        try {
            conn = conexao.conectar();
            PreparedStatement pstmt = conn.prepareStatement(INSERIR, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, recado.getIdChamadaRecebida());
            pstmt.setString(2, recado.getMensagem());
            pstmt.setInt(3, recado.getFuncionario().getId());
            pstmt.setString(4, recado.getContato());
            pstmt.setString(5, recado.getTelefone());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            int retorno = -1;
            while (rs.next()) {
                retorno = rs.getInt("GENERATED_KEY");
            }
            return retorno;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Recado> getListarDestinatario() throws SQLException {
        List<Recado> lista = new ArrayList<Recado>();
        try {
            conn = conexao.conectar();
            PreparedStatement pstmt = conn.prepareStatement(LISTAR_DESTINATARIO);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Recado recado = new Recado();
                recado.getFuncionario().setId(rs.getInt("idFuncionario"));
                recado.getFuncionario().setNome(rs.getString("nome"));
                int qtd = rs.getInt("qtd");
                recado.setQtd(qtd);
                lista.add(recado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        conn.close();
        return lista;
    }

    public List<Recado> getListarRecadoPorNome(Recado recado) {
        List<Recado> listaDeRecado = new ArrayList<Recado>();
        try {
            conn = conexao.conectar();
            PreparedStatement pstmt = conn.prepareStatement(LISTAR_RECADO_POR_NOME);
            pstmt.setInt(1, recado.getFuncionario().getId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recado = new Recado();
                recado.setMensagem(rs.getString("mensagem"));
                recado.getFuncionario().setNome(rs.getString("nome"));
                recado.setContato(rs.getString("contato"));
                recado.setTelefone(rs.getString("telefone"));
                int id = rs.getInt("recado.id");
                recado.setId(id);
                listaDeRecado.add(recado);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return listaDeRecado;
    }

    public boolean alterarStatusRecado(int id) {
        try {
            conn = conexao.conectar();
            PreparedStatement pstmt = conn.prepareStatement(ALTERAR_STATUS_RECADO);
            pstmt.setInt(1, id);
            pstmt.execute();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
