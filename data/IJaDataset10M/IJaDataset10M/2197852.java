package br.com.fatecpg.zanotti.acessobanco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe responsável por realizar todas consultas relacionadas ao JComboBox Loja.
 * @author F.L. Zanotti
 */
public class conComboLoja {

    /**
 * Realiza uma consulta por Nome das Lojas
 * @return String[] Nome das lojas
 */
    public String[] consulta() {
        try {
            Connection con = AbreConexao.getConexao();
            String query = "SELECT NMLOJA from LOJA";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            String[] lista = new String[2];
            int cont = 0;
            while (rs.next()) {
                String NMLOJA = rs.getString("NMLOJA");
                NMLOJA = NMLOJA.trim();
                lista[cont] = NMLOJA;
                cont++;
            }
            con.close();
            rs.close();
            return lista;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Realiza uma consulta por Código das Lojas.
     * @param loja Nome da Lojas
     * @return String Código da Loja
     */
    public String consultaCDLOJA(String loja) {
        try {
            Connection con = AbreConexao.getConexao();
            String query = "SELECT CDLOJA from LOJA where NMLOJA like ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, loja);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String CDLOJA = rs.getString("CDLOJA");
                CDLOJA = CDLOJA.trim();
                loja = CDLOJA;
            }
            con.close();
            rs.close();
            return loja;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    /**
     * Realiza uma consulta por Nome da Loja
     * @param loja Código da Loja
     * @return String NOme da Loja
     */
    public String consultaNMLOJA(String loja) {
        try {
            Connection con = AbreConexao.getConexao();
            String query = "SELECT NMLOJA from LOJA where CDLOJA like ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, loja);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String CDLOJA = rs.getString("NMLOJA");
                CDLOJA = CDLOJA.trim();
                loja = CDLOJA;
            }
            con.close();
            rs.close();
            return loja;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}
