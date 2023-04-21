package recursos;

import java.sql.*;

public class Faturamento {

    private Connection con = null;

    private Statement stmt = null;

    public Faturamento() {
        con = Conecta.getInstance();
    }

    public String getNomeConvenio(String codconvenio) {
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String resp = "";
            ResultSet rs = null;
            String sql = "SELECT descr_convenio FROM convenio ";
            sql += "WHERE cod_convenio=" + codconvenio;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                resp = rs.getString("descr_convenio");
            }
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            return e.toString();
        }
    }

    public String getConvenios(String codconv, String cod_empresa) {
        String resp = "", sql = "";
        ResultSet rs = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "SELECT cod_convenio, descr_convenio FROM convenio ";
            sql += "WHERE cod_empresa=" + cod_empresa + " ORDER BY descr_convenio";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if (rs.getString("cod_convenio").equals(codconv)) {
                    resp += "<option value='" + rs.getString("cod_convenio") + "' selected>" + rs.getString("descr_convenio") + "</option>\n";
                } else {
                    resp += "<option value='" + rs.getString("cod_convenio") + "'>" + rs.getString("descr_convenio") + "</option>\n";
                }
            }
            rs.close();
            stmt.close();
            return resp + sql;
        } catch (SQLException e) {
            return "ERRO:" + e.toString() + " SQL=" + sql;
        }
    }

    public String getEspecialidades() {
        String sql = "SELECT codesp, descri FROM especialidade ";
        sql += "WHERE descri <> '' ORDER BY descri";
        String resp = "";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resp += "<option value='" + rs.getString("codesp") + "'>" + rs.getString("descri") + "</option>\n";
            }
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            return e.toString();
        }
    }

    public String[] getProcedimentos(String cod_plano) {
        String resp[] = { "' ','", "' ','", "' ','" };
        try {
            if (Util.isNull(cod_plano)) {
                resp[0] += "'";
                resp[1] += "'";
                resp[2] += "'";
                return resp;
            }
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql;
            ResultSet rs = null;
            sql = "SELECT procedimentos.COD_PROCED, procedimentos.Procedimento, ";
            sql += "valorprocedimentos.valor ";
            sql += "FROM valorprocedimentos INNER JOIN procedimentos ";
            sql += "ON valorprocedimentos.cod_proced = procedimentos.COD_PROCED ";
            sql += "WHERE valorprocedimentos.cod_plano=" + cod_plano;
            sql += " AND procedimentos.flag=1 ";
            sql += "ORDER BY Procedimento";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resp[0] += rs.getString("COD_PROCED") + "','";
                resp[1] += rs.getString("Procedimento") + "','";
                resp[2] += rs.getString("valor") + "','";
            }
            if (!resp[0].equals("' ','")) {
                resp[0] = resp[0].substring(0, resp[0].length() - 2);
                resp[1] = resp[1].substring(0, resp[1].length() - 2);
                resp[2] = resp[2].substring(0, resp[2].length() - 2);
            } else {
                resp[0] += "'";
                resp[1] += "'";
                resp[2] += "'";
            }
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            resp[0] = e.toString();
            return resp;
        }
    }

    public String getProfissionais(String cod_empresa) {
        String resp = "";
        String sql = "";
        ResultSet rs = null;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql += "SELECT nome, cod, prof_reg FROM profissional ";
            sql += "WHERE cod_empresa=" + cod_empresa;
            sql += " AND locacao='interno' and ativo='S' ORDER BY nome";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                resp += "<option value='" + rs.getString("prof_reg") + "'>" + rs.getString("nome") + "</option>\n";
            }
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            return "Erro:" + e.toString();
        }
    }

    public ResultSet getCheques(String codsubitem) {
        String sql = "";
        ResultSet rs = null;
        sql += "SELECT * FROM faturas_chq WHERE cod_subitem=" + codsubitem;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (SQLException e) {
            return null;
        }
    }

    public String procedimentoDuplicado(String codcli, String cod_proced, String prof_reg, String data) {
        String sql = "";
        ResultSet rs = null;
        sql += "SELECT procedimentos.tipoGuia, faturas.prof_reg ";
        sql += "FROM (procedimentos INNER JOIN faturas_itens ";
        sql += "ON procedimentos.COD_PROCED = faturas_itens.Cod_Proced) ";
        sql += "INNER JOIN faturas ON faturas_itens.Numero = faturas.Numero ";
        sql += "WHERE faturas_itens.Cod_Proced=" + cod_proced + " ";
        sql += "AND faturas.Data_Lanca='" + data + "' ";
        sql += "AND faturas.codcli=" + codcli;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                if (rs.getString("prof_reg").equals(prof_reg)) {
                    return "Mesmo%20M�dico";
                } else {
                    if (rs.getInt("tipoGuia") != 1) {
                        return "N�o%20�%20Consulta";
                    }
                }
            }
            rs.close();
            stmt.close();
            return "OK";
        } catch (SQLException e) {
            return "ERRO:" + e.toString();
        }
    }

    private String getImagem(String pagto, String convenio, String cod_subitem) {
        String img = "";
        String label = "";
        String resp = "";
        if (pagto.equals("1")) {
            img = "money.gif";
            label = "Pagamento em Dinheiro";
        } else if (pagto.equals("2")) {
            img = "cheque.gif";
            label = "Pagamento em Cheque";
        } else if (pagto.equals("3")) {
            img = "cartoes.gif";
            label = "Pagamento com Cart�o de Cr�dito";
        } else {
            resp = getNomeConvenio(convenio);
            return resp;
        }
        resp = "<a title='" + label + "' href=Javascript:detalhesPagamento(";
        resp += pagto + "," + cod_subitem + ")>";
        resp += "<img src='images/" + img + "' border=0></a>";
        return resp;
    }

    public String getItensFatura(String cod_fatura) {
        String resp = "";
        String sql = "";
        ResultSet rs = null;
        String sqtde, svalor;
        float soma = 0, subtotal = 0;
        int cont = 0;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "SELECT faturas_itens.*, procedimentos.Procedimento, ";
            sql += "paciente.codcli, paciente.nome, faturas.Data_Lanca, ";
            sql += "paciente.data_nascimento ";
            sql += "FROM ((faturas_itens INNER JOIN procedimentos ON ";
            sql += "faturas_itens.Cod_Proced = procedimentos.COD_PROCED) ";
            sql += "INNER JOIN faturas ON faturas_itens.Numero = ";
            sql += "faturas.Numero) INNER JOIN paciente ON faturas.codcli = ";
            sql += "paciente.codcli ";
            sql += "WHERE faturas_itens.Numero=" + cod_fatura;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sqtde = rs.getString("qtde");
                svalor = rs.getString("valor");
                subtotal = Float.parseFloat(sqtde) * Float.parseFloat(svalor);
                soma += subtotal;
                resp += "<tr>\n";
                resp += "	<td class='tdLight'>" + rs.getString("Procedimento") + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + Util.trataNulo(rs.getString("viaAcesso"), "") + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + sqtde + "&nbsp;</td>\n";
                resp += "	<td class='tdLight'>" + Util.formatCurrency(svalor) + "&nbsp;</td>\n";
                resp += "	<td class='tdLight'>" + Util.formatCurrency("" + subtotal) + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + getImagem(rs.getString("tipo_pagto"), rs.getString("cod_convenio"), rs.getString("cod_subitem")) + "</td>\n";
                resp += "	<td class='TdLight' align='center'><a title='Excluir Registro' href='Javascript:excluirProcedimento(" + rs.getString("cod_subitem") + ");'><img src='images/delete.gif' border='0'></a></td>\n";
                resp += "</tr>\n";
                cont++;
            }
            resp += "<tr><td><input type='hidden' name='subtotal' id='subtotal' value='" + soma + "'><input type='hidden' name='qtdeitens' id='qtdeitens' value='" + cont + "'></td></tr>";
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            return "Erro:" + e.toString();
        }
    }

    public String getItensFatura1(String cod_fatura) {
        String resp = "";
        String sql = "";
        ResultSet rs = null;
        String sqtde, svalor;
        float soma = 0, subtotal = 0;
        String imprime_faturamento = "";
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "SELECT faturas_itens.*, procedimentos.Procedimento, ";
            sql += "paciente.codcli, paciente.nome, faturas.Data_Lanca, ";
            sql += "paciente.data_nascimento ";
            sql += "FROM ((faturas_itens INNER JOIN procedimentos ON ";
            sql += "faturas_itens.Cod_Proced = procedimentos.COD_PROCED) ";
            sql += "INNER JOIN faturas ON faturas_itens.Numero = ";
            sql += "faturas.Numero) INNER JOIN paciente ON faturas.codcli = ";
            sql += "paciente.codcli ";
            sql += "WHERE faturas_itens.Numero=" + cod_fatura;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                sqtde = rs.getString("qtde");
                svalor = rs.getString("valor");
                subtotal = Float.parseFloat(sqtde) * Float.parseFloat(svalor);
                imprime_faturamento = "'" + rs.getString("paciente.codcli") + "','" + rs.getString("paciente.nome") + "','" + Util.formataData(rs.getString("faturas.Data_Lanca")) + "','" + Util.formataData(rs.getString("paciente.data_nascimento")) + "','" + rs.getString("cod_subitem");
                soma += subtotal;
                resp += "<tr>\n";
                resp += "	<td class='tdLight'>" + rs.getString("Procedimento") + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + Util.trataNulo(rs.getString("viaAcesso"), "") + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + sqtde + "&nbsp;</td>\n";
                resp += "	<td class='tdLight'>" + Util.formatCurrency(svalor) + "&nbsp;</td>\n";
                resp += "	<td class='tdLight'>" + Util.formatCurrency("" + subtotal) + "&nbsp;</td>\n";
                resp += "	<td class='tdLight' align='center'>" + getImagem(rs.getString("tipo_pagto"), rs.getString("cod_convenio"), rs.getString("cod_subitem")) + "</td>\n";
                resp += "	<td class='TdLight' align='center'><a title='Excluir Registro' href='Javascript:excluirProcedimento(" + rs.getString("cod_subitem") + ");'><img src='images/delete.gif' border='0'></a></td>\n";
                resp += "	<td class='TdLight' align='center'><a title='Imprimir Ficha de Atendimento' href=\"Javascript:fichaatendimento(" + imprime_faturamento + "');\"><img src='images/print.gif' border='0'></a></td>\n";
                resp += "</tr>\n";
            }
            resp += "<tr><td><input type='hidden' name='subtotal' id='subtotal' value='" + soma + "'></td></tr>";
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            return "Erro:" + e.toString();
        }
    }

    public String[] getFaturamento(String pesquisa, String campo, String ordem, int numPag, int qtdeporpagina, int tipo, String cod_empresa, String pagina) {
        String resp[] = { "", "" };
        String sql = "";
        String codigo = "";
        String solicitante = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        Statement stmt2 = null;
        float subtotal;
        pagina = new Banco().getPagina(pagina);
        pesquisa = pesquisa.trim();
        if (!Util.isNull(pesquisa)) {
            sql += "SELECT paciente.nome, Executante.nome, Solicitante.nome, ";
            sql += "faturas.Numero, faturas.Data_Lanca ";
            sql += "FROM ((paciente INNER JOIN faturas ON paciente.codcli = ";
            sql += "faturas.codcli) LEFT JOIN profissional AS Solicitante ON ";
            sql += "faturas.Cod_Solicitante = Solicitante.prof_reg) INNER JOIN ";
            sql += "profissional AS Executante ON faturas.prof_reg = ";
            sql += "Executante.prof_reg ";
            try {
                stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                if (tipo == 1) {
                    sql += "WHERE " + campo + "='" + pesquisa + "'";
                } else if (tipo == 2) {
                    sql += "WHERE " + campo + " LIKE '" + pesquisa + "%'";
                } else if (tipo == 3) {
                    sql += "WHERE " + campo + " LIKE '%" + pesquisa + "%'";
                }
                sql += " AND paciente.cod_empresa=" + cod_empresa;
                sql += " ORDER BY " + ordem;
                rs = stmt.executeQuery(sql);
                rs.last();
                int numRows = rs.getRow();
                rs.close();
                resp[1] = Util.criaPaginacao(pagina, numPag, qtdeporpagina, numRows);
                sql += " LIMIT " + ((numPag - 1) * qtdeporpagina) + "," + qtdeporpagina;
                rs = stmt.executeQuery(sql);
                stmt2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                while (rs.next()) {
                    codigo = rs.getString("faturas.Numero");
                    rs2 = stmt2.executeQuery("SELECT SUM(valor*qtde) as Total FROM faturas_itens WHERE Numero = " + codigo);
                    rs2.next();
                    subtotal = rs2.getFloat("Total");
                    rs2.close();
                    solicitante = rs.getString("Solicitante.nome") != null ? rs.getString("Solicitante.nome") : "N/C";
                    resp[0] += "<tr onClick=go('" + pagina + "?cod=" + codigo + "') onMouseOver='trocaCor(this,1);' onMouseOut='trocaCor(this,2);'>";
                    resp[0] += "<td width='110' class='tdLight'>" + rs.getString("paciente.nome") + "</td>\n";
                    resp[0] += "<td width='110' class='tdLight'>" + rs.getString("Executante.nome") + "&nbsp;</td>\n";
                    resp[0] += "<td width='110' class='tdLight'>" + solicitante + "</td>\n";
                    resp[0] += "<td width='60' class='tdLight'>" + Util.formataData(rs.getString("data_Lanca")) + "</td>\n";
                    resp[0] += "<td width='60' class='tdLight'>" + Util.formatCurrency(subtotal + "") + "</td>\n";
                    resp[0] += "</tr>";
                }
                if (resp[0].equals("")) {
                    resp[0] += "<tr>";
                    resp[0] += "<td colspan='6' width='600' class='tdLight'>";
                    resp[0] += "Nenhum registro";
                    resp[0] += "</td>";
                    resp[0] += "</tr>";
                }
                rs.close();
                stmt.close();
                return resp;
            } catch (SQLException e) {
                resp[0] = "Erro:" + e.toString();
                return resp;
            }
        } else {
            resp[1] = Util.criaPaginacao(pagina, numPag, qtdeporpagina, 0);
            resp[0] += "<tr>";
            resp[0] += "<td colspan='6' width='600' class='tdLight'>";
            resp[0] += "Nenhum registro encontrado";
            resp[0] += "</td>";
            resp[0] += "</tr>";
            return resp;
        }
    }

    public String[] getDados(String cod_faturamento) {
        String resp[] = new String[4];
        ResultSet rs = null;
        String sql;
        try {
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            sql = "SELECT paciente.nome, paciente.data_nascimento, paciente.foto, ";
            sql += "Solicitante.nome FROM (paciente INNER ";
            sql += "JOIN faturas ON paciente.codcli = faturas.codcli) ";
            sql += "LEFT JOIN profissional AS Solicitante ON ";
            sql += "faturas.Cod_Solicitante = Solicitante.prof_reg ";
            sql += "WHERE faturas.Numero=" + cod_faturamento;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                resp[0] = rs.getString("paciente.nome");
                resp[1] = Util.formataData(rs.getString("data_nascimento"));
                resp[2] = rs.getString("Solicitante.nome") != null ? rs.getString("Solicitante.nome") : "";
                resp[3] = rs.getString("paciente.foto");
            }
            rs.close();
            stmt.close();
            return resp;
        } catch (SQLException e) {
            resp[0] = "Erro" + e.toString();
            return resp;
        }
    }

    public String removeFatura(String numero, String usuario) {
        ResultSet rs = null;
        String sql, resp = "";
        try {
            Banco bc = new Banco();
            stmt = con.createStatement();
            sql = "SELECT cod_subitem FROM faturas_itens WHERE Numero=" + numero;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                bc.executaSQL("DELETE FROM faturas_chq WHERE cod_subitem=" + rs.getString("cod_subitem"));
                bc.executaSQL("DELETE FROM faturas_cartoes WHERE cod_subitem=" + rs.getString("cod_subitem"));
            }
            bc.executaSQL("DELETE FROM faturas_itens WHERE Numero=" + numero);
            bc.executaSQL("DELETE FROM faturas WHERE Numero=" + numero);
            rs.close();
            stmt.close();
            resp = "OK";
            return resp;
        } catch (SQLException e) {
            resp = e.toString();
            return resp;
        }
    }

    public String removeFaturaItem(String cod) {
        if (Util.isNull(cod)) {
            return "";
        }
        Banco bc = new Banco();
        bc.executaSQL("DELETE FROM faturas_itens WHERE cod_subitem=" + cod);
        bc.executaSQL("DELETE FROM procedimentossadt WHERE codsubitem=" + cod);
        bc.executaSQL("DELETE FROM faturas_chq WHERE cod_subitem=" + cod);
        bc.executaSQL("DELETE FROM faturas_cartoes WHERE cod_subitem=" + cod);
        return "OK";
    }

    public static void main(String args[]) {
        Faturamento fat = new Faturamento();
        String teste = fat.procedimentoDuplicado("31683", "41", "02042449", "2008-07-31");
    }
}
