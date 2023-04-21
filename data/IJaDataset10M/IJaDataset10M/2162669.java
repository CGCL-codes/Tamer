package Persistencia.Portal;

import Persistencia.Portal.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Entidade.Portal.Notificacao;

public class NotificacaoDAO {

    private static NotificacaoDAO instance = new NotificacaoDAO();

    public static NotificacaoDAO getInstance() {
        return instance;
    }

    private NotificacaoDAO() {
    }

    public List<Notificacao> carregaMsg(int codigoColaborador) {
        Statement stmt = null;
        List<Notificacao> lstMsg = new ArrayList<Notificacao>();
        Connection conn = Conexao.getInstance().criaConexao();
        if (conn != null) {
            try {
                stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                ResultSet rs = stmt.executeQuery("SELECT * FROM notificacao " + "WHERE Colaborador_codigo =" + codigoColaborador + " ");
                while (rs.next()) {
                    Notificacao msg = new Notificacao();
                    msg.setCodigo(rs.getInt("codigo"));
                    msg.setCodigoColaborador(rs.getInt("Colaborador_codigo"));
                    msg.setAssunto(rs.getString("descricao"));
                    msg.setRemetenteNotificacao(rs.getString("remetentenotificacao"));
                    msg.setMensagem(rs.getString("mensagem"));
                    msg.setDataCadastro(rs.getDate("datacadstro"));
                    lstMsg.add(msg);
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return lstMsg;
    }

    public Notificacao leMsg(int codigo) {
        Statement stmt = null;
        Notificacao msg = null;
        Connection conn = Conexao.getInstance().criaConexao();
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(" SELECT * FROM notificacao WHERE codigo =" + codigo + "");
                if (rs.next()) {
                    msg = carregaDadosNoObjeto(rs);
                } else {
                    msg = null;
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return msg;
    }

    private Notificacao carregaDadosNoObjeto(ResultSet rs) throws SQLException {
        Notificacao msg = new Notificacao();
        msg.setCodigo(rs.getInt("codigo"));
        msg.setCodigoColaborador(rs.getInt("Colaborador_codigo"));
        msg.setAssunto(rs.getString("descricao"));
        msg.setRemetenteNotificacao(rs.getString("remetentenotificacao"));
        msg.setMensagem(rs.getString("mensagem"));
        msg.setDataCadastro(rs.getDate("datacadstro"));
        return msg;
    }

    public int apaga(int codigoMsg) {
        int n = 0;
        Statement stmt = null;
        Connection conn = Conexao.getInstance().criaConexao();
        if (conn != null) {
            try {
                stmt = conn.createStatement();
                n = stmt.executeUpdate("DELETE FROM notificacao WHERE codigo = " + codigoMsg);
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return n;
    }

    public int gravaMsg(Notificacao notificacao) {
        int n = 0;
        Connection conn = Conexao.getInstance().criaConexao();
        if (conn != null) {
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement("insert into notificacao " + "(colaborador_codigo,descricao,datacadstro,remetentenotificacao,mensagem)" + " VALUES (?, ?, ?, ?, ?)");
                pstmt.setInt(1, notificacao.getCodigoColaborador());
                pstmt.setString(2, notificacao.getAssunto());
                pstmt.setDate(3, new java.sql.Date(notificacao.getDataCadastro().getTime()));
                pstmt.setString(4, notificacao.getRemetenteNotificacao());
                pstmt.setString(5, notificacao.getMensagem());
                n = pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Inclusao Falhou!!!\n" + e.getMessage());
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return n;
    }
}
