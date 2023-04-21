package br.com.goals.tableedit.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.com.goals.tableedit.TableEdit;
import br.com.goals.tableedit.model.FieldConfig;
import br.com.goals.tableedit.model.HtmlSelectOption;
import br.com.goals.tableedit.model.ReplaceString;
import br.com.goals.tableedit.model.TableToHtml;

/**
 * Lista as ocorrencias de uma tabela
 * 
 * @author Fabio Issamu Oshiro
 * 
 */
public class ListHtmlFactory {

    private Connection con;

    /**
	 * resultados por p�gina
	 */
    private int resultsPerPage = 10;

    private static final int MAXCHAR = 30;

    public ListHtmlFactory(Connection conn) throws SQLException {
        this.con = conn;
    }

    /**
	 * @param tabela
	 * @throws SQLException
	 */
    public String createList(TableToHtml tabela) throws SQLException {
        int n = 0;
        StringBuilder sb = new StringBuilder();
        String sql = "Select *";
        sql += " From " + tabela.nome + " " + tabela.where;
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        sb.append("<table>");
        sb.append("<tr><th>#</th>");
        for (FieldConfig fieldConfig : tabela.getListFieldConfig()) {
            if (fieldConfig.dontHtml) {
                continue;
            }
            sb.append("<th>");
            sb.append(fieldConfig.displayName);
            sb.append("</th>");
        }
        sb.append("<th>Apagar</th>");
        sb.append("</tr>");
        while (rs.next()) {
            if (n % 2 == 0) {
                sb.append("<tr class=\"" + TableEdit.CSS_PREFIX + "_row_alt\">");
            } else {
                sb.append("<tr>");
            }
            sb.append("<td>");
            Integer pk = rs.getInt(tabela.columnAutoIncrement.columnName);
            String strActionColumn = tabela.actionColumn.template.replace("{pk.htmlname}", tabela.columnAutoIncrement.htmlName);
            String strApagarColumn = tabela.actionColumn.apagar.replace("{pk.htmlname}", tabela.columnAutoIncrement.htmlName).replace("{pk.value}", pk.toString());
            Pattern pat = Pattern.compile("\\{rs\\.(.*?)\\}");
            Matcher mat = pat.matcher(strActionColumn);
            while (mat.find()) {
                String val = rs.getObject(mat.group(1)).toString();
                strActionColumn = mat.replaceFirst(val);
                mat = pat.matcher(strActionColumn);
            }
            mat = pat.matcher(strApagarColumn);
            while (mat.find()) {
                String val = rs.getObject(mat.group(1)).toString();
                strApagarColumn = mat.replaceFirst(val);
                mat = pat.matcher(strApagarColumn);
            }
            sb.append(strActionColumn.replace("{pk.value}", pk.toString()));
            sb.append("</td>");
            int i = 0;
            for (FieldConfig fieldConfig : tabela.getListFieldConfig()) {
                i++;
                if (fieldConfig.dontHtml) {
                    continue;
                }
                sb.append("<td>");
                boolean chk_len = true;
                String dado = "N/A";
                try {
                    dado = rs.getObject(i) == null ? "N/A" : String.valueOf(rs.getObject(fieldConfig.columnName));
                } catch (Exception er) {
                }
                if (fieldConfig.getCustomHtmlRender() != null) {
                    dado = fieldConfig.getCustomHtmlRender().getHtml(tabela, fieldConfig, rs);
                    chk_len = false;
                } else if (fieldConfig.htmlSelect != null) {
                    if (dado.equals("null")) {
                        dado = "N/A";
                    } else {
                        for (HtmlSelectOption opt : fieldConfig.htmlSelect.getOptions()) {
                            if (opt.value.equals(dado)) {
                                dado = opt.label;
                                break;
                            }
                        }
                    }
                } else if (fieldConfig.className.equals("java.lang.String")) {
                    for (ReplaceString rep : fieldConfig.getListReplaceDisplayString()) {
                        if (dado.equals(rep.getSearch())) {
                            dado = rep.getNewValue();
                        }
                    }
                } else if (fieldConfig.className.equals("java.lang.Boolean")) {
                    dado = dado.equals("true") ? "sim" : "n&atilde;o";
                } else if (fieldConfig.className.equals("java.sql.Date")) {
                    dado = Conversor.deYYYYMMDDparaDDMMYYYY(dado);
                }
                if (dado.length() > MAXCHAR && chk_len) {
                    int blank = dado.lastIndexOf(" ", MAXCHAR);
                    if (blank != -1) dado = dado.substring(0, blank) + "..."; else dado = dado.substring(0, MAXCHAR) + "...";
                }
                if (fieldConfig.getCustomHtmlRender() == null) {
                    sb.append(dado.replaceAll("<", "&lt;"));
                } else {
                    sb.append(dado);
                }
                sb.append("</td>");
            }
            sb.append("<td class=\"del\">" + strApagarColumn + "</td>");
            sb.append("</tr>");
            n++;
        }
        sb.append("</table>");
        st.close();
        rs.close();
        return sb.toString();
    }

    public int getResultsPerPage() {
        return resultsPerPage;
    }

    public void setResultsPerPage(int resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }
}
