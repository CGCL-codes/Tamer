package controller.compare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import dbsync4j.core.behavior.Column;
import dbsync4j.mapper.behavior.compare.DifferenceCompare;
import dbsync4j.mapper.concrete.compare.ConcreteTableDifferenceCompare;

public class CompareDatabaseDetailGraphic extends TagSupport {

    private static final long serialVersionUID = 1L;

    private Collection<DifferenceCompare> comparadores;

    /**
	 * @return the comparadores
	 */
    public Collection<DifferenceCompare> getComparadores() {
        return comparadores;
    }

    /**
	 * @param comparadores the comparadores to set
	 */
    public void setComparadores(Collection<DifferenceCompare> comparadores) {
        this.comparadores = comparadores;
    }

    public int doStartTag() throws JspException {
        try {
            JspWriter out = pageContext.getOut();
            int contDiff = 1;
            StringBuffer buffer = new StringBuffer("");
            buffer.append("<center>");
            buffer.append("<table width=\"90%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\">");
            buffer.append("<tr>");
            buffer.append("<td>");
            buffer.append("<table width=\"100%\" border=\"0\" cellpadding=\"5\" cellspacing=\"0\">");
            buffer.append("<tr bgcolor=\"white\">");
            buffer.append("<td width=\"3%\" class=\"topoNumeracaoDiff\">" + contDiff + "</td>");
            buffer.append("<td width=\"17%\">&nbsp;</td>");
            buffer.append("<td width=\"30%\">&nbsp;</td>");
            buffer.append("<td width=\"20%\">&nbsp;</td>");
            buffer.append("<td width=\"30%\">&nbsp;</td>");
            buffer.append("</tr>");
            for (DifferenceCompare dc : comparadores) {
                contDiff++;
                ConcreteTableDifferenceCompare tbDifferenceCompare = (ConcreteTableDifferenceCompare) dc;
                if (tbDifferenceCompare.diff().size() > 0) {
                    Collection<DifferenceCompare> columnDifferenceCompare = tbDifferenceCompare.getColumnDifferenceCompare();
                    Collection<DifferenceCompare> pkColumnDifferenceCompare = new ArrayList<DifferenceCompare>();
                    Collection<DifferenceCompare> otherColumnDifferenceCompare = new ArrayList<DifferenceCompare>();
                    for (DifferenceCompare cDifferenceCompare : columnDifferenceCompare) {
                        if (cDifferenceCompare.getSource() == null || cDifferenceCompare.getDestiny() == null) {
                            if (cDifferenceCompare.getSource() != null) {
                                Column c = (Column) cDifferenceCompare.getSource();
                                if (c.isPartOfPrimaryKey()) {
                                    pkColumnDifferenceCompare.add(cDifferenceCompare);
                                } else {
                                    otherColumnDifferenceCompare.add(cDifferenceCompare);
                                }
                            } else {
                                Column c = (Column) cDifferenceCompare.getDestiny();
                                if (c.isPartOfPrimaryKey()) {
                                    pkColumnDifferenceCompare.add(cDifferenceCompare);
                                } else {
                                    otherColumnDifferenceCompare.add(cDifferenceCompare);
                                }
                            }
                        } else {
                            Column c = (Column) cDifferenceCompare.getSource();
                            if (c.isPartOfPrimaryKey()) {
                                pkColumnDifferenceCompare.add(cDifferenceCompare);
                            } else {
                                otherColumnDifferenceCompare.add(cDifferenceCompare);
                            }
                        }
                    }
                    boolean tbOrigemNull = tbDifferenceCompare.getSource() == null;
                    boolean tbDestinoNull = tbDifferenceCompare.getDestiny() == null;
                    if (!(tbOrigemNull || tbDestinoNull)) {
                        String corLinha = "bgcolor=\"white\"";
                        String classDiff = "\"corpoNumeracaoDiff\"";
                        String classOrigem = "\"topoTabela\"";
                        String classDestino = "\"topoTabela\"";
                        String origemNome = tbDifferenceCompare.getSource().getName();
                        String destinoNome = tbDifferenceCompare.getDestiny().getName();
                        System.out.println("----------------------");
                        System.out.println(origemNome);
                        System.out.println(destinoNome);
                        System.out.println("---------------------");
                        buffer.append("<tr " + corLinha + ">");
                        buffer.append("<td width=\"3%\" class=" + classDiff + ">" + contDiff + "</td>");
                        buffer.append("<td width=\"17%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classOrigem + "><center><b>" + origemNome + "</b></center></td>");
                        buffer.append("<td width=\"20%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classDestino + "><center><b>" + destinoNome + "</b></center></td>");
                        buffer.append("</tr>");
                        contDiff++;
                        int pkcont = pkColumnDifferenceCompare.size();
                        int auxpk = 1;
                        corLinha = "";
                        for (DifferenceCompare pkdc : pkColumnDifferenceCompare) {
                            if (pkcont == auxpk) {
                                classDiff = "\"corpoNumeracaoDiff\"";
                                classOrigem = "\"finalPk\"";
                                classDestino = "\"finalPk\"";
                                origemNome = "<b>PK<b> ".concat(pkdc.getSource() == null ? " - " : pkdc.getSource().getName());
                                destinoNome = "<b>PK<b> ".concat(pkdc.getDestiny() == null ? " - " : pkdc.getDestiny().getName());
                            } else {
                                classDiff = "\"corpoNumeracaoDiff\"";
                                classOrigem = "\"corpoTabela\"";
                                classDestino = "\"corpoTabela\"";
                                origemNome = "<font><b>PK<b></font> ".concat(pkdc.getSource() == null ? " - " : pkdc.getSource().getName());
                                destinoNome = "<font><b>PK<b></font> ".concat(pkdc.getSource() == null ? " - " : pkdc.getSource().getName());
                            }
                            buffer.append("<tr " + corLinha + ">");
                            buffer.append("<td width=\"3%\" class=" + classDiff + ">" + contDiff + "</td>");
                            buffer.append("<td width=\"17%\">&nbsp;</td>");
                            buffer.append("<td width=\"30%\" class=" + classOrigem + ">" + origemNome + "</td>");
                            buffer.append("<td width=\"20%\">&nbsp;</td>");
                            buffer.append("<td width=\"30%\" class=" + classDestino + ">" + destinoNome + "</td>");
                            buffer.append("</tr>");
                            auxpk++;
                            contDiff++;
                        }
                        int ccont = otherColumnDifferenceCompare.size();
                        int auxc = 1;
                        for (DifferenceCompare cdc : otherColumnDifferenceCompare) {
                            if (ccont == auxc) {
                                classOrigem = "\"baseTabela\"";
                                classDestino = "\"baseTabela\"";
                            } else {
                                classOrigem = "\"corpoTabela\"";
                                classDestino = "\"corpoTabela\"";
                            }
                            if (cdc.getSource() == null || cdc.getDestiny() == null) {
                                if (cdc.getSource() == null) {
                                    corLinha = "bgcolor=\"#FF9999\"";
                                    classDiff = "\"corpoNumeracaoDiff2\"";
                                    origemNome = "-";
                                    destinoNome = cdc.getDestiny().getName();
                                } else {
                                    corLinha = "bgcolor=\"#FF9999\"";
                                    classDiff = "\"corpoNumeracaoDiff2\"";
                                    origemNome = cdc.getSource().getName();
                                    destinoNome = "-";
                                }
                            } else if (cdc.diff().size() > 0) {
                                corLinha = "bgcolor=\"#FF9999\"";
                                classDiff = "\"corpoNumeracaoDiff2\"";
                                origemNome = cdc.getSource().getName();
                                destinoNome = cdc.getDestiny().getName();
                            } else {
                                corLinha = "";
                                classDiff = "\"corpoNumeracaoDiff\"";
                                origemNome = cdc.getSource().getName();
                                destinoNome = cdc.getDestiny().getName();
                            }
                            buffer.append("<tr " + corLinha + ">");
                            buffer.append("<td width=\"3%\" class=" + classDiff + ">" + contDiff + "</td>");
                            buffer.append("<td width=\"17%\">&nbsp;</td>");
                            buffer.append("<td width=\"30%\" class=" + classOrigem + ">" + origemNome + "</td>");
                            buffer.append("<td width=\"20%\">&nbsp;</td>");
                            buffer.append("<td width=\"30%\" class=" + classDestino + ">" + destinoNome + "</td>");
                            buffer.append("</tr>");
                            System.out.println(origemNome + "/" + destinoNome);
                            auxc++;
                            contDiff++;
                        }
                        contDiff++;
                        classDiff = "corpoNumeracaoDiff";
                        classOrigem = "";
                        classDestino = "";
                        origemNome = "";
                        destinoNome = "";
                        corLinha = "";
                        buffer.append("<tr " + corLinha + ">");
                        buffer.append("<td width=\"3%\" class=" + classDiff + ">" + contDiff + "</td>");
                        buffer.append("<td width=\"17%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classOrigem + ">" + origemNome + "</td>");
                        buffer.append("<td width=\"20%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classDestino + ">" + destinoNome + "</td>");
                        buffer.append("</tr>");
                        contDiff++;
                        buffer.append("<tr " + corLinha + ">");
                        buffer.append("<td width=\"3%\" class=" + classDiff + ">" + contDiff + "</td>");
                        buffer.append("<td width=\"17%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classOrigem + ">" + origemNome + "</td>");
                        buffer.append("<td width=\"20%\">&nbsp;</td>");
                        buffer.append("<td width=\"30%\" class=" + classDestino + ">" + destinoNome + "</td>");
                        buffer.append("</tr>");
                    }
                }
            }
            buffer.append("<tr bgcolor=\"white\">");
            buffer.append("<td width=\"3%\" class=\"baseNumeracaoDiff\">" + (++contDiff) + "</td>");
            buffer.append("<td width=\"17%\">&nbsp;</td>");
            buffer.append("<td width=\"30%\">&nbsp;</td>");
            buffer.append("<td width=\"20%\">&nbsp;</td>");
            buffer.append("<td width=\"30%\">&nbsp;</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</td>");
            buffer.append("</tr>");
            buffer.append("</table>");
            buffer.append("</center>");
            out.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }
}
