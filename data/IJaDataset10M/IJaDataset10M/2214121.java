package br.com.radaction.DAO;

import br.com.radaction.entidades.SW;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author robson
 */
public class PontuacaoSWDAO {

    Connection conexao;

    public PontuacaoSWDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void insertNota(SW sw) throws SQLException {
        String query = "INSERT INTO PONTUACAOSW(CODSW,CODFUNC,NOTA)" + "VALUES(?,?,?)";
        PreparedStatement pst = this.conexao.prepareStatement(query);
        pst.setInt(1, sw.getCod());
        pst.setInt(2, sw.getFuncionario().getCod());
        pst.setInt(3, sw.getNota());
        pst.executeUpdate();
        pst.close();
        query = "UPDATE FUNCIONARIO SET FLGVOTO = TRUE WHERE COD=?";
        pst = this.conexao.prepareStatement(query);
        pst.setInt(1, sw.getFuncionario().getCod());
        pst.executeUpdate();
        pst.close();
    }

    public void pontuaSW(ArrayList<SW> listaAfirmacoesPontuadas) throws SQLException {
        for (SW sw : listaAfirmacoesPontuadas) {
            this.insertNota(sw);
        }
    }

    public void zeraPontuacao() throws SQLException {
        String query = "DELETE FROM PONTUACAOSW WHERE CODSW > 0";
        PreparedStatement pst = this.conexao.prepareStatement(query);
        pst.execute();
        pst.close();
    }

    public int getTotal() throws SQLException {
        int totalRet = 0;
        String query = "SELECT CODSW, SUM(NOTA) FROM PONTUACAOSW GROUP BY CODSW ORDER BY CODSW";
        Statement st = this.conexao.createStatement();
        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            totalRet = rs.getInt("SUM");
        }
        st.close();
        rs.close();
        return totalRet;
    }
}
