package org.freedom.library.swing.frame;

import java.awt.Component;
import org.freedom.infra.model.jdbc.DbConnection;
import org.freedom.library.functions.Funcoes;
import org.freedom.library.swing.component.JLabelPad;
import org.freedom.library.swing.component.JPasswordFieldPad;
import org.freedom.library.swing.component.JTextFieldPad;
import org.freedom.library.swing.dialog.FFDialogo;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class FPassword extends FFDialogo {

    public static final long serialVersionUID = 1L;

    /**
	 * Permiss�o para vender o produto abaixo do custo.
	 */
    public static final int BAIXO_CUSTO = 0;

    /**
	 * Permiss�o para abrir gaveta do PDV.
	 */
    public static final int ABRE_GAVETA = 1;

    /**
	 * Permiss�o para alterar as parcelas no fechamento da venda.
	 */
    public static final int ALT_PARC_VENDA = 2;

    /**
	 * Permiss�o para venda de produto com receita.
	 */
    public static final int APROV_RECEITA_PROD = 3;

    /**
	 * Permiss�o para visualiza��o de tela de libera cr�dito.
	 */
    public static final int LIBERA_CRED = 4;

    /**
	 * Permiss�o para visualiza��o de tela de libera cr�dito.
	 */
    public static final int VENDA_IMOBLIZIADO = 5;

    /**
	 * Permiss�o para digitacao do peso nas telas de pesagem.
	 */
    public static final int LIBERA_CAMPO_PESAGEM = 6;

    private JTextFieldPad txtUsu = new JTextFieldPad(JTextFieldPad.TP_STRING, 8, 0);

    private JPasswordFieldPad txtPass = new JPasswordFieldPad(10);

    private String[] param = null;

    private int tipo = 0;

    private int[] log = null;

    /**
	 * 
	 * @param arg0
	 *            Component pai.
	 * @param arg1
	 *            Tipo da permiss�o.
	 * @param arg2
	 *            Parametros para o log(para o tipo BAIXO_CUSTO).
	 * @param arg3
	 *            Titulo.
	 * @param arg4
	 *            Conex�o.
	 */
    public FPassword(Component arg0, int arg1, String[] arg2, String arg3, DbConnection arg4) {
        super(arg0);
        tipo = arg1;
        setParam(arg2);
        setTitulo(arg3);
        setConexao(arg4);
        montaTela();
    }

    public FPassword(Component arg0, int arg1, String arg2, DbConnection arg3) {
        this(arg0, arg1, null, arg2, arg3);
    }

    private void montaTela() {
        setAtribos(300, 140);
        adic(new JLabelPad("Usu�rio: "), 7, 10, 100, 20);
        adic(new JLabelPad("Senha: "), 7, 30, 100, 20);
        adic(txtUsu, 110, 10, 150, 20);
        adic(txtPass, 110, 30, 150, 20);
        adic(new JLabelPad("Senha: "), 7, 30, 100, 20);
        eUltimo();
        txtUsu.setVlrString(Aplicativo.strUsuario);
        setPrimeiroFoco(txtPass);
    }

    public void execShow() {
        setVisible(true);
        firstFocus();
    }

    public void ok() {
        boolean ret = false;
        switch(tipo) {
            case BAIXO_CUSTO:
                ret = getBaixoCusto();
                break;
            case ABRE_GAVETA:
                ret = getAbreGaveta();
                break;
            case ALT_PARC_VENDA:
                ret = getAltParcVenda();
                break;
            case APROV_RECEITA_PROD:
                ret = getAprovReceitaProd();
                break;
            case LIBERA_CRED:
                ret = getLiberaCredito();
                break;
            case VENDA_IMOBLIZIADO:
                ret = getVendaImobilizado();
                break;
            case LIBERA_CAMPO_PESAGEM:
                ret = getLiberaCampoPesagem();
                break;
            default:
                break;
        }
        OK = ret;
        setVisible(false);
    }

    private boolean getBaixoCusto() {
        boolean ret = getPermissao(BAIXO_CUSTO);
        if (ret) {
            log = AplicativoPD.gravaLog(txtUsu.getVlrString().toLowerCase().trim(), "PR", "LIB", "Libera��o de " + param[0] + " abaixo do custo", param[0] + " [" + param[1] + "], " + "Item: [" + param[2] + "], " + "Produto: [" + param[3] + "], " + "Pre�o: [" + param[4] + "]", con);
        }
        return ret;
    }

    private boolean getAbreGaveta() {
        return getPermissao(ABRE_GAVETA);
    }

    private boolean getAltParcVenda() {
        return getPermissao(ALT_PARC_VENDA);
    }

    private boolean getAprovReceitaProd() {
        return getPermissao(APROV_RECEITA_PROD);
    }

    private boolean getLiberaCredito() {
        return getPermissao(LIBERA_CRED);
    }

    private boolean getLiberaCampoPesagem() {
        return getPermissao(LIBERA_CAMPO_PESAGEM);
    }

    private boolean getVendaImobilizado() {
        return getPermissao(VENDA_IMOBLIZIADO);
    }

    private boolean getPermissao(int tipo) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Properties props = null;
        String sIDUsu = null;
        StringBuffer sSQL = new StringBuffer();
        boolean[] permissoes = new boolean[7];
        try {
            props = new Properties();
            sIDUsu = txtUsu.getVlrString().toLowerCase().trim();
            props.put("user", sIDUsu);
            props.put("password", txtPass.getVlrString());
            if ("".equals(sIDUsu) || "".equals(txtPass.getVlrString().trim())) {
                Funcoes.mensagemErro(this, "Campo em branco!");
                return false;
            }
            DriverManager.getConnection(Aplicativo.strBanco, props).close();
            sSQL.append("SELECT BAIXOCUSTOUSU, ABREGAVETAUSU, ALTPARCVENDA, APROVRECEITA, LIBERACREDUSU, VENDAPATRIMUSU, LIBERACAMPOPESAGEM ");
            sSQL.append("FROM SGUSUARIO ");
            sSQL.append("WHERE IDUSU=? AND CODEMP=? AND CODFILIAL=?");
            ps = con.prepareStatement(sSQL.toString());
            ps.setString(1, sIDUsu);
            ps.setInt(2, Aplicativo.iCodEmp);
            ps.setInt(3, Aplicativo.iCodFilial);
            rs = ps.executeQuery();
            if (rs.next()) {
                permissoes[0] = "S".equals(rs.getString(1));
                permissoes[1] = "S".equals(rs.getString(2));
                permissoes[2] = "S".equals(rs.getString(3));
                permissoes[3] = "S".equals(rs.getString(4));
                permissoes[4] = "S".equals(rs.getString(5));
                permissoes[5] = "S".equals(rs.getString(6));
                permissoes[6] = "S".equals(rs.getString(7));
            }
            if (!permissoes[tipo]) {
                Funcoes.mensagemErro(this, "A��o n�o permitida para este usu�rio ! ! !");
            }
        } catch (SQLException sqle) {
            if (sqle.getErrorCode() == 335544472) {
                Funcoes.mensagemErro(this, "Nome do usu�rio ou senha inv�lidos ! ! !");
            } else {
                Funcoes.mensagemErro(this, "Erro ao verificar senha.", true, con, sqle);
            }
            sqle.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ps = null;
            rs = null;
            props = null;
            sIDUsu = null;
            sSQL = null;
        }
        return permissoes[tipo];
    }

    public String[] getLog() {
        return new String[] { String.valueOf(Aplicativo.iCodEmp), String.valueOf(log[0]), String.valueOf(log[1]) };
    }

    public void setParam(String[] arg) {
        param = arg;
    }

    public void setTitulo(String arg) {
        if (!(arg != null && arg.trim().length() > 0)) {
            arg = "Permiss�o";
        }
        super.setTitulo(arg);
    }
}
