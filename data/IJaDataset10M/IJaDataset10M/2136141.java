package emprestimo.modelo.dao;

import emprestimo.modelo.dao.*;
import emprestimo.controle.JConexao;
import emprestimo.modelo.modelo.JBairro;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Carlos Alexandre
 */
public class JBairroDao {

    private String erro = "Erro ao ";

    private final String aviso = "Erro!";

    public JBairroDao() {
    }

    public void inserir(JBairro bairro) throws Exception {
        try {
            String sql = "INSERT INTO Bairro (bai_nome, cid_cod) VALUES (?, ?)";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ps.setString(1, bairro.getBai_nome());
            ps.setLong(2, bairro.getCidade().getCid_cod());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            String exception = "incluir Bairro! ";
            JOptionPane.showMessageDialog(null, erro + exception, aviso, JOptionPane.ERROR_MESSAGE);
            throw new Exception(erro + exception + e.getMessage());
        }
    }

    public void alterar(JBairro bairro) throws Exception {
        try {
            String sql = "UPDATE Bairro SET bai_nome = ?, cid_cod = ? WHERE bai_cod = ?";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ps.setLong(3, bairro.getBai_cod());
            ps.setString(1, bairro.getBai_nome());
            ps.setLong(2, bairro.getCidade().getCid_cod());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            String exception = "alterar Bairro! ";
            JOptionPane.showMessageDialog(null, erro + exception, aviso, JOptionPane.ERROR_MESSAGE);
            throw new Exception(erro + exception + e.getMessage());
        }
    }

    public void excluir(int bai_cod) throws Exception {
        try {
            String sql = "DELETE FROM Bairro WHERE bai_cod =  " + bai_cod;
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            String exception = "excluir Bairro! ";
            JOptionPane.showMessageDialog(null, erro + exception, aviso, JOptionPane.ERROR_MESSAGE);
            throw new Exception(erro + exception + e.getMessage());
        }
    }

    public List<JBairro> retornaListaBairros() throws Exception {
        List<JBairro> listaBairros = new ArrayList<JBairro>();
        try {
            String sql;
            sql = "SELECT * FROM Bairro";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JBairro bairro = new JBairro();
                JCidadeDao cidadeDao = new JCidadeDao();
                bairro.setBai_cod(rs.getInt("bai_cod"));
                bairro.setBai_nome(rs.getString("bai_nome"));
                bairro.setCidade(cidadeDao.retornaListaCidade(rs.getInt("cid_cod")));
                listaBairros.add(bairro);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                finalize();
            } catch (Throwable ex) {
                Logger.getLogger(JEstadoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return listaBairros;
    }

    public List<JBairro> retornaBairro(String nome) throws Exception {
        List<JBairro> listaBairro = new ArrayList<JBairro>();
        try {
            String sql = "SELECT * FROM Bairro WHERE bai_nome LIKE '" + nome + "%' ";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JBairro bairro = new JBairro();
                JCidadeDao cidadeDao = new JCidadeDao();
                bairro.setBai_nome(rs.getString("bai_nome"));
                bairro.setCidade(cidadeDao.retornaListaCidade(rs.getInt("bai_cod")));
                listaBairro.add(bairro);
            }
        } catch (Exception e) {
            e.printStackTrace();
            String exception = "buscar o Bairro! ";
            JOptionPane.showMessageDialog(null, erro + exception, aviso, JOptionPane.ERROR_MESSAGE);
            throw new Exception(erro + exception + e.getMessage());
        }
        return listaBairro;
    }

    public JBairro retornaListaBairro(int bai_cod) throws Exception {
        try {
            String sql;
            sql = "SELECT * FROM Bairro WHERE bai_cod = ?";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ps.setInt(1, bai_cod);
            ResultSet rs = ps.executeQuery();
            JBairro bairro = null;
            if (rs.next()) {
                bairro = new JBairro();
                JCidadeDao cidadeDao = new JCidadeDao();
                bairro.setBai_cod(rs.getInt("bai_cod"));
                bairro.setBai_nome(rs.getString("bai_nome"));
                bairro.setCidade(cidadeDao.retornaListaCidade(rs.getInt("cid_cod")));
            }
            return bairro;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public List<JBairro> retornaListaBairrosPorCidade(int cid_cod) throws Exception {
        List<JBairro> listaBairros = new ArrayList<JBairro>();
        try {
            String sql = "SELECT * FROM Bairro WHERE cid_cod = ?";
            PreparedStatement ps = JConexao.openConexao().prepareStatement(sql);
            ps.setInt(1, cid_cod);
            ResultSet rs = ps.executeQuery();
            JBairro bairro = null;
            JCidadeDao cidDao = new JCidadeDao();
            while (rs.next()) {
                bairro = new JBairro();
                bairro.setBai_cod(rs.getInt("bai_cod"));
                bairro.setBai_nome(rs.getString("bai_nome"));
                bairro.setCidade(cidDao.retornaListaCidade(rs.getInt("cid_cod")));
                listaBairros.add(bairro);
            }
            return listaBairros;
        } catch (Exception erro) {
            erro.printStackTrace();
            throw new Exception("Erro ao localizar Bairro por Cidade: " + erro.getMessage());
        }
    }
}
