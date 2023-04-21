package br.com.fatecpg.zanotti.acessobanco;

import br.com.fatecpg.zanotti.modelo.Funcionario;
import java.sql.*;

/**
 * Classe responsável por realizar a consulta de todos dados pertinentes do usuário logado.
 * @author F.L. Zanotti
 */
public class conFunc {

    /**
 * Realiza uma consulta para Popular um objeto Funcionario que for compatível com o código de funcionario recebido.
 * @param cdfunc
 * @return
 */
    public static Funcionario consultar(String cdfunc) {
        String CDCARGO = "", CDFUNCIONARIO = "", NMFUNCIONARIO = "", NMCARGO = "", CDLOJA = "";
        try {
            Connection con = AbreConexao.getConexao();
            PreparedStatement pst = con.prepareStatement("SELECT CDCARGO, CDFUNCIONARIO, NMFUNCIONARIO, NMCARGO, CDLOJA from FUNCIONARIO join CARGO USING(cdcargo) where CDFUNCIONARIO = ?");
            pst.setString(1, cdfunc);
            System.out.println("Não Executou o Query...");
            ResultSet rs = pst.executeQuery();
            System.out.println("Executou o Query...");
            while (rs.next()) {
                CDCARGO = rs.getString("CDCARGO");
                CDFUNCIONARIO = rs.getString("CDFUNCIONARIO");
                NMFUNCIONARIO = rs.getString("NMFUNCIONARIO");
                NMCARGO = rs.getString("NMCARGO");
                CDLOJA = rs.getString("CDLOJA");
            }
            Funcionario Func = new Funcionario(CDCARGO, CDFUNCIONARIO, NMFUNCIONARIO, NMCARGO, CDLOJA);
            con.close();
            pst.close();
            return Func;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
    }
}
