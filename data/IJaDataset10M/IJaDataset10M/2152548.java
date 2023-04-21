package hydrological;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utility.I18N;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class ExportPDF extends HttpServlet {

    private static final long serialVersionUID = -6033026500372479591L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultSet rs = null;
        ResultSet rs_anni_prec = null;
        Statement sm = null;
        Statement sm_anni_prec = null;
        Connection cn = null;
        double[] Qmax = new double[12];
        double[] Qmin = new double[12];
        double[] Qmedia = new double[12];
        double[] Qm = new double[12];
        double[] deflusso = new double[12];
        double[] afflusso = new double[12];
        double matrix[][] = new double[31][12];
        int gg_counter[] = new int[12];
        double max, min, Qmax_annuale, Qmedia_annuale, tot_annuale, Qmin_annuale, qm_annuale;
        double area, secs_annuali, deflusso_annuale, afflusso_annuale;
        int gg_annuali, gg_c;
        try {
            String schema = request.getParameter("schema");
            String schema_name = schema.toUpperCase().substring(0, 1) + schema.substring(1, schema.length());
            String idstazione = request.getParameter("idstazione");
            String nomestazione = request.getParameter("nomestazione");
            String anni = request.getParameter("anni");
            String lang = request.getParameter("lang");
            String stringaSQL;
            String driverName = "org.postgresql.Driver";
            String url = "jdbc:postgresql://localhost:5432/fisr?charSet=UNICODE";
            Class.forName(driverName);
            cn = DriverManager.getConnection(url, "admin", "2Tette");
            stringaSQL = "SELECT date, avg_val ";
            stringaSQL += "FROM " + schema + ".m_daily_flow ";
            stringaSQL += "WHERE station_id = '" + idstazione + "' AND year = '" + anni + "'";
            Document document = new Document(PageSize.A4.rotate());
            response.setContentType("application/pdf");
            PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            Font font = new Font(Font.TIMES_ROMAN, 10, Font.NORMAL);
            PdfPTable table;
            PdfPCell cell;
            Paragraph pr;
            document.add(new Paragraph(I18N.t("basin", lang) + " - " + schema_name, font));
            document.add(new Paragraph(I18N.t("station_of", lang) + " " + nomestazione + "\n\n", font));
            pr = new Paragraph(new Paragraph(I18N.t("portate_medie_misurate_pdf", lang) + "\n\n", font));
            pr.setAlignment(Element.ALIGN_CENTER);
            document.add(pr);
            float[] widthsMonthly = { 0.16f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f, 0.07f };
            table = new PdfPTable(widthsMonthly);
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(I18N.t("giorno", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("gen", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("feb", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("mar", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("apr", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("mag", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("giu", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("lug", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("ago", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("set", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("ott", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("nov", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("dic", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            Locale locale = Locale.US;
            NumberFormat nf = NumberFormat.getInstance(locale);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            for (int i = 0; i < 31; i++) {
                for (int j = 0; j < 12; j++) {
                    matrix[i][j] = -1;
                }
            }
            sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sm.executeQuery(stringaSQL);
            while (rs.next()) {
                int gg = Integer.parseInt(rs.getString(1).substring(rs.getString(1).lastIndexOf("-") + 1, rs.getString(1).length()));
                int mm = Integer.parseInt(rs.getString(1).substring(rs.getString(1).indexOf("-") + 1, rs.getString(1).lastIndexOf("-")));
                matrix[gg - 1][mm - 1] = rs.getDouble(2);
            }
            rs.close();
            sm.close();
            for (int i = 0; i < 31; i++) {
                cell = new PdfPCell(new Paragraph(Integer.toString(i + 1), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                for (int j = 0; j < 12; j++) {
                    if (matrix[i][j] != -1) {
                        cell = new PdfPCell(new Paragraph(nf.format(matrix[i][j]), font));
                    } else {
                        cell = new PdfPCell(new Paragraph("-", font));
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            }
            document.add(table);
            document.newPage();
            document.add(new Paragraph(I18N.t("basin", lang) + " - " + schema_name, font));
            document.add(new Paragraph(I18N.t("station_of", lang) + " " + nomestazione + "\n\n", font));
            pr = new Paragraph(new Paragraph(I18N.t("elementi_caratteristic_anno", lang) + " " + anni + "\n\n", font));
            pr.setAlignment(Element.ALIGN_CENTER);
            document.add(pr);
            float[] widths = { 0.22f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f, 0.06f };
            table = new PdfPTable(widths);
            table.setWidthPercentage(100);
            cell = new PdfPCell(new Paragraph(I18N.t("indici", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("year", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("gen", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("feb", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("mar", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("apr", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("mag", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("giu", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("lug", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("ago", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("set", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("ott", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("nov", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph(I18N.t("dic", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            gg_c = 0;
            tot_annuale = 0;
            for (int j = 0; j < 12; j++) {
                max = -5000000;
                min = 5000000;
                Qmedia[j] = 0;
                gg_c = 0;
                for (int i = 0; i < 31; i++) {
                    tot_annuale += matrix[i][j];
                    if (matrix[i][j] != -1) {
                        gg_counter[j] = ++gg_c;
                        Qmedia[j] += matrix[i][j];
                        if (matrix[i][j] > max) {
                            max = matrix[i][j];
                        }
                        if (matrix[i][j] < min) {
                            min = matrix[i][j];
                        }
                    }
                }
                Qmax[j] = max;
                Qmin[j] = min;
                Qmedia[j] /= gg_c;
            }
            cell = new PdfPCell(new Paragraph(I18N.t("qmax", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            Qmax_annuale = Qmax[0];
            for (int i = 0; i < 12; i++) {
                if (Qmax[i] > Qmax_annuale) {
                    Qmax_annuale = Qmax[i];
                }
            }
            cell = new PdfPCell(new Paragraph(nf.format(Qmax_annuale), font));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            for (int i = 0; i < 12; i++) {
                cell = new PdfPCell(new Paragraph(nf.format(Qmax[i]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Paragraph(I18N.t("qmedia", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            gg_annuali = 0;
            for (int i = 0; i < 12; i++) {
                gg_annuali += gg_counter[i];
            }
            Qmedia_annuale = tot_annuale / gg_annuali;
            cell = new PdfPCell(new Paragraph(nf.format(Qmedia_annuale), font));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            for (int i = 0; i < 12; i++) {
                cell = new PdfPCell(new Paragraph(nf.format(Qmedia[i]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            }
            cell = new PdfPCell(new Paragraph(I18N.t("qmin", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            Qmin_annuale = Qmin[0];
            for (int i = 0; i < 12; i++) {
                if (Qmin[i] < Qmin_annuale) {
                    Qmin_annuale = Qmin[i];
                }
            }
            cell = new PdfPCell(new Paragraph(nf.format(Qmin_annuale), font));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            for (int i = 0; i < 12; i++) {
                cell = new PdfPCell(new Paragraph(nf.format(Qmin[i]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            }
            area = -1;
            qm_annuale = 0;
            deflusso_annuale = 0;
            stringaSQL = "SELECT area from tevere.stations where station_id ='" + idstazione + "'";
            sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sm.executeQuery(stringaSQL);
            if (rs.next()) {
                area = rs.getInt(1);
            }
            rs.close();
            sm.close();
            if (area != -1) {
                cell = new PdfPCell(new Paragraph(I18N.t("qm", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                qm_annuale = (Qmedia_annuale * 1000) / area;
                cell = new PdfPCell(new Paragraph(nf.format(qm_annuale), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    Qm[i] = (Qmedia[i] * 1000) / area;
                    cell = new PdfPCell(new Paragraph(nf.format(Qm[i]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                secs_annuali = gg_annuali * 24 * 60 * 60;
                deflusso_annuale = (qm_annuale * secs_annuali) / Math.pow(10, 6);
                cell = new PdfPCell(new Paragraph(I18N.t("deflusso", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(deflusso_annuale), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    deflusso[i] = (Qm[i] * gg_counter[i] * 24 * 60 * 60) / Math.pow(10, 6);
                    cell = new PdfPCell(new Paragraph(nf.format(deflusso[i]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            } else {
                cell = new PdfPCell(new Paragraph(I18N.t("qm", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("-", font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph("-", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("deflusso", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph("-", font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph("-", font));
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);
                }
            }
            afflusso_annuale = 0;
            stringaSQL = "SELECT jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec ";
            stringaSQL += "FROM " + schema + ".influx ";
            stringaSQL += "WHERE year = '" + anni + "' AND station_id = '" + idstazione + "'";
            sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = sm.executeQuery(stringaSQL);
            cell = new PdfPCell(new Paragraph(I18N.t("afflussi", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            if (rs.next()) {
                for (int i = 0; i < 12; i++) {
                    afflusso[i] = rs.getDouble(i + 1);
                    afflusso_annuale += afflusso[i];
                }
                cell = new PdfPCell(new Paragraph(nf.format(afflusso_annuale), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(afflusso[i]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            } else {
                cell = new PdfPCell(new Paragraph("-", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph("-", font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            }
            rs.close();
            sm.close();
            cell = new PdfPCell(new Paragraph(I18N.t("coeff_deflusso", lang), font));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
            if ((deflusso_annuale != 0) && (afflusso_annuale != 0)) {
                cell = new PdfPCell(new Paragraph(nf.format(deflusso_annuale / afflusso_annuale), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(deflusso[i] / afflusso[i]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            } else {
                cell = new PdfPCell(new Paragraph("-", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph("-", font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
            }
            document.add(table);
            stringaSQL = "SELECT DISTINCT year FROM " + schema + ".m_daily_flow ";
            stringaSQL += "WHERE station_id = '" + idstazione + "' AND year < '" + anni + "' ";
            stringaSQL += "ORDER BY year DESC";
            sm_anni_prec = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs_anni_prec = sm_anni_prec.executeQuery(stringaSQL);
            rs_anni_prec.last();
            int tot_anni_prec = rs_anni_prec.getRow();
            rs_anni_prec.beforeFirst();
            if (tot_anni_prec > 0) {
                double media_anni_prec[][][] = new double[7][13][tot_anni_prec];
                int z = 0;
                while (rs_anni_prec.next()) {
                    String anno_prec = rs_anni_prec.getString(1);
                    stringaSQL = "SELECT date, avg_val ";
                    stringaSQL += "FROM " + schema + ".m_daily_flow ";
                    stringaSQL += "WHERE station_id = '" + idstazione + "' AND year = '" + anno_prec + "'";
                    for (int i = 0; i < 31; i++) {
                        for (int j = 0; j < 12; j++) {
                            matrix[i][j] = -1;
                        }
                    }
                    sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = sm.executeQuery(stringaSQL);
                    while (rs.next()) {
                        int gg = Integer.parseInt(rs.getString(1).substring(rs.getString(1).lastIndexOf("-") + 1, rs.getString(1).length()));
                        int mm = Integer.parseInt(rs.getString(1).substring(rs.getString(1).indexOf("-") + 1, rs.getString(1).lastIndexOf("-")));
                        matrix[gg - 1][mm - 1] = rs.getDouble(2);
                    }
                    rs.close();
                    sm.close();
                    gg_c = 0;
                    tot_annuale = 0;
                    for (int j = 0; j < 12; j++) {
                        max = -5000000;
                        min = 5000000;
                        Qmedia[j] = 0;
                        gg_c = 0;
                        for (int i = 0; i < 31; i++) {
                            tot_annuale += matrix[i][j];
                            if (matrix[i][j] != -1) {
                                gg_counter[j] = ++gg_c;
                                Qmedia[j] += matrix[i][j];
                                if (matrix[i][j] > max) {
                                    max = matrix[i][j];
                                }
                                if (matrix[i][j] < min) {
                                    min = matrix[i][j];
                                }
                            }
                        }
                        Qmax[j] = max;
                        Qmin[j] = min;
                        Qmedia[j] /= gg_c;
                    }
                    Qmax_annuale = Qmax[0];
                    for (int i = 0; i < 12; i++) {
                        if (Qmax[i] > Qmax_annuale) {
                            Qmax_annuale = Qmax[i];
                        }
                    }
                    media_anni_prec[0][0][z] = Qmax_annuale;
                    for (int i = 0; i < 12; i++) {
                        media_anni_prec[0][i + 1][z] = Qmax[i];
                    }
                    gg_annuali = 0;
                    for (int i = 0; i < 12; i++) {
                        gg_annuali += gg_counter[i];
                    }
                    Qmedia_annuale = tot_annuale / gg_annuali;
                    media_anni_prec[1][0][z] = Qmedia_annuale;
                    for (int i = 0; i < 12; i++) {
                        media_anni_prec[1][i + 1][z] = Qmedia[i];
                    }
                    Qmin_annuale = Qmin[0];
                    for (int i = 0; i < 12; i++) {
                        if (Qmin[i] < Qmin_annuale) {
                            Qmin_annuale = Qmin[i];
                        }
                    }
                    media_anni_prec[2][0][z] = Qmin_annuale;
                    for (int i = 0; i < 12; i++) {
                        media_anni_prec[2][i + 1][z] = Qmin[i];
                    }
                    area = -1;
                    qm_annuale = 0;
                    deflusso_annuale = 0;
                    stringaSQL = "SELECT area from tevere.stations where station_id ='" + idstazione + "'";
                    sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = sm.executeQuery(stringaSQL);
                    if (rs.next()) {
                        area = rs.getInt(1);
                    }
                    rs.close();
                    sm.close();
                    if (area != -1) {
                        qm_annuale = (Qmedia_annuale * 1000) / area;
                        media_anni_prec[3][0][z] = qm_annuale;
                        for (int i = 0; i < 12; i++) {
                            Qm[i] = (Qmedia[i] * 1000) / area;
                            media_anni_prec[3][i + 1][z] = Qm[i];
                        }
                        secs_annuali = gg_annuali * 24 * 60 * 60;
                        deflusso_annuale = (qm_annuale * secs_annuali) / Math.pow(10, 6);
                        media_anni_prec[4][0][z] = deflusso_annuale;
                        for (int i = 0; i < 12; i++) {
                            deflusso[i] = (Qm[i] * gg_counter[i] * 24 * 60 * 60) / Math.pow(10, 6);
                            media_anni_prec[4][i + 1][z] = deflusso[i];
                        }
                    } else {
                    }
                    afflusso_annuale = 0;
                    stringaSQL = "SELECT jan, feb, mar, apr, may, jun, jul, aug, sep, oct, nov, dec ";
                    stringaSQL += "FROM " + schema + ".influx ";
                    stringaSQL += "WHERE year = '" + anno_prec + "' AND station_id = '" + idstazione + "'";
                    sm = cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = sm.executeQuery(stringaSQL);
                    if (rs.next()) {
                        for (int i = 0; i < 12; i++) {
                            afflusso[i] = rs.getDouble(i + 1);
                            afflusso_annuale += afflusso[i];
                        }
                        media_anni_prec[5][0][z] = afflusso_annuale;
                        for (int i = 0; i < 12; i++) {
                            media_anni_prec[5][i + 1][z] = afflusso[i];
                        }
                    } else {
                    }
                    rs.close();
                    sm.close();
                    if ((deflusso_annuale != 0) && (afflusso_annuale != 0)) {
                        media_anni_prec[6][0][z] = deflusso_annuale / afflusso_annuale;
                        for (int i = 0; i < 12; i++) {
                            media_anni_prec[6][i + 1][z] = deflusso[i] / afflusso[i];
                        }
                    } else {
                    }
                    z++;
                }
                rs_anni_prec.first();
                String ultimo_anno = rs_anni_prec.getString(1);
                rs_anni_prec.last();
                String primo_anno = rs_anni_prec.getString(1);
                rs_anni_prec.close();
                sm_anni_prec.close();
                pr = new Paragraph(new Paragraph("\n\n" + I18N.t("elementi_caratteristic_periodo", lang) + " " + primo_anno + "/" + ultimo_anno + "\n\n", font));
                pr.setAlignment(Element.ALIGN_CENTER);
                document.add(pr);
                table = new PdfPTable(widths);
                table.setWidthPercentage(100);
                cell = new PdfPCell(new Paragraph(I18N.t("indici", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("year", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("gen", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("feb", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("mar", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("apr", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("mag", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("giu", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("lug", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("ago", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("set", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("ott", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("nov", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(I18N.t("dic", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
                double media_valori[][] = new double[7][13];
                double somma = 0;
                for (int r = 0; r < 7; r++) {
                    for (int c = 0; c < 13; c++) {
                        somma = 0;
                        for (int p = 0; p < tot_anni_prec; p++) {
                            somma = somma + media_anni_prec[r][c][p];
                        }
                        media_valori[r][c] = somma / tot_anni_prec;
                    }
                }
                cell = new PdfPCell(new Paragraph(I18N.t("qmax", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(media_valori[0][0]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[0][i + 1]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("qmedia", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(media_valori[1][0]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[1][i + 1]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("qmin", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(media_valori[2][0]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[2][i + 1]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("qm", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(media_valori[3][0]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[3][i + 1]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("deflusso", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                cell = new PdfPCell(new Paragraph(nf.format(media_valori[4][0]), font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[4][i + 1]), font));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("afflussi", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                if (media_valori[5][0] != 0) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[5][0]), font));
                } else {
                    cell = new PdfPCell(new Paragraph("-", font));
                }
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    if (media_valori[5][i + 1] != 0) {
                        cell = new PdfPCell(new Paragraph(nf.format(media_valori[5][i + 1]), font));
                    } else {
                        cell = new PdfPCell(new Paragraph("-", font));
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                cell = new PdfPCell(new Paragraph(I18N.t("coeff_deflusso", lang), font));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                if (media_valori[5][0] != 0) {
                    cell = new PdfPCell(new Paragraph(nf.format(media_valori[6][0]), font));
                } else {
                    cell = new PdfPCell(new Paragraph("-", font));
                }
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                for (int i = 0; i < 12; i++) {
                    if (media_valori[5][i + 1] != 0) {
                        cell = new PdfPCell(new Paragraph(nf.format(media_valori[6][i + 1]), font));
                    } else {
                        cell = new PdfPCell(new Paragraph("-", font));
                    }
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                }
                document.add(table);
            } else {
                pr = new Paragraph(new Paragraph(I18N.t("no_match_found_previous_years", lang), font));
                pr.setAlignment(Element.ALIGN_CENTER);
                document.add(pr);
            }
            document.close();
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            e.printStackTrace(response.getWriter());
        } finally {
            try {
                if (rs != null) rs.close();
                if (rs_anni_prec != null) rs_anni_prec.close();
                if (sm != null) sm.close();
                if (sm_anni_prec != null) sm_anni_prec.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                response.setContentType("text/html; charset=UTF-8");
                e.printStackTrace(response.getWriter());
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
}
