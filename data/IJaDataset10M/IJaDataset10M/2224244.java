package com.centropresse.struts.action;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.ajaxtags.xml.AjaxXmlBuilder;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;
import com.centropresse.dao.AnnoDAO;
import com.centropresse.dao.BaseDAO;
import com.centropresse.dao.CategoriaDAO;
import com.centropresse.dao.CorsaDAO;
import com.centropresse.dao.CostruttoreDAO;
import com.centropresse.dao.LuceDAO;
import com.centropresse.dao.MacchinaDAO;
import com.centropresse.dao.SpintaDAO;
import com.centropresse.dao.StatoCondizioniDAO;
import com.centropresse.dao.UbicazioneDAO;
import com.centropresse.dto.Anno;
import com.centropresse.dto.Categoria;
import com.centropresse.dto.Corsa;
import com.centropresse.dto.Costruttore;
import com.centropresse.dto.Luce;
import com.centropresse.dto.Macchina;
import com.centropresse.dto.Spinta;
import com.centropresse.dto.Stato;
import com.centropresse.dto.Tipo;
import com.centropresse.dto.Ubicazione;
import com.centropresse.struts.form.RicercaAsservimentiForm;
import com.centropresse.struts.form.RicercaPresseForm;
import com.centropresse.struts.form.RicercaMacchineForm;
import com.centropresse.util.*;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/** 
 * MyEclipse Struts
 * Creation date: 12-14-2007
 * 
 * XDoclet definition:
 * @struts.action path="/ricercaPresse" name="ricercaPresseForm" input="/jsp/form/ricercaPresse.jsp" scope="request" validate="true"
 * @struts.action-forward name="/risultatiRicercaPresse" path="/jsp/view/risultatiRicercaPresse"
 */
public class RicercaAsservimentiAction extends Action {

    private static final String nameClass = "RicercaAsservimentiAction";

    public static Logger logger = LogFactory.getWebLogger();

    private static String prefixLogClass = Constants.APPLICATION_CODE_WEB + "." + nameClass + ".class";

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String prefixLogMethod = prefixLogClass + "::execute - ";
        logger.info(prefixLogMethod + "BEGIN");
        Connection conn = null;
        String methodName = "execute";
        String go = "";
        Macchina[] elencoAsservimenti = null;
        try {
            RicercaAsservimentiForm ricercaAsservimentiForm = (RicercaAsservimentiForm) form;
            logger.info(prefixLogMethod + " RICERCA ASSERVIMENTI FORM:\n" + ricercaAsservimentiForm.toString());
            HttpSession session = request.getSession();
            ServletContext application = session.getServletContext();
            Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
            logger.info(prefixLogMethod + " LOCALE country :" + locale.getCountry());
            logger.info(prefixLogMethod + " LOCALE language:" + locale.getLanguage());
            String country = locale.getLanguage();
            if (ricercaAsservimentiForm.getActionDettaglio() != null && !ricercaAsservimentiForm.getActionDettaglio().equals("")) {
                logger.info(prefixLogMethod + " *** DETTAGLIO ***");
                try {
                    elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                    for (int i = 0; i < elencoAsservimenti.length; i++) {
                        Macchina asservimento = elencoAsservimenti[i];
                        if (asservimento.getId_macchina().equals(ricercaAsservimentiForm.getId_macchina())) {
                            ricercaAsservimentiForm.setAsservimento(asservimento);
                            logger.info(prefixLogMethod + " TROVATA!:\n" + asservimento.toString());
                            break;
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                go = "dettaglio";
            } else if (ricercaAsservimentiForm.getActionDettaglioPdf() != null && !ricercaAsservimentiForm.getActionDettaglioPdf().equals("")) {
                try {
                    MessageResources messages = getResources(request);
                    String label_dettaglio = messages.getMessage(locale, Constants.LABEL_DETTAGLIO);
                    String nomeFilePdf = label_dettaglio;
                    response.setContentType("application/download;");
                    response.setHeader("Content-Disposition", "attachment; filename=" + nomeFilePdf + ".pdf;");
                    try {
                        elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                        for (int i = 0; i < elencoAsservimenti.length; i++) {
                            Macchina asservimento = elencoAsservimenti[i];
                            if (asservimento.getId_macchina().equals(ricercaAsservimentiForm.getId_macchina())) {
                                ricercaAsservimentiForm.setAsservimento(asservimento);
                                logger.info(prefixLogMethod + " TROVATA!:\n" + asservimento.toString());
                                OutputStream stream = response.getOutputStream();
                                new com.centropresse.util.pdf.PdfDettaglioMacchina(stream, asservimento, messages, locale).creaPdfDettaglio();
                                break;
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else if (ricercaAsservimentiForm.getActionFirst() != null && !ricercaAsservimentiForm.getActionFirst().equals("")) {
                logger.info(prefixLogMethod + " *** NAVIGAZIONE FIRST ***");
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_FIRST, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                RicercaAsservimentiForm formSession = (RicercaAsservimentiForm) request.getSession().getAttribute("ricercaAsservimentiForm");
                ricercaAsservimentiForm = invertiOrdinamento(ricercaAsservimentiForm, formSession, null);
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionLast() != null && !ricercaAsservimentiForm.getActionLast().equals("")) {
                logger.info(prefixLogMethod + " *** NAVIGAZIONE LAST ***");
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_LAST, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                RicercaAsservimentiForm formSession = (RicercaAsservimentiForm) request.getSession().getAttribute("ricercaAsservimentiForm");
                ricercaAsservimentiForm = invertiOrdinamento(ricercaAsservimentiForm, formSession, null);
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionPrev() != null && !ricercaAsservimentiForm.getActionPrev().equals("")) {
                logger.info(prefixLogMethod + " *** NAVIGAZIONE PREV ***");
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_PREV, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                RicercaAsservimentiForm formSession = (RicercaAsservimentiForm) request.getSession().getAttribute("ricercaAsservimentiForm");
                ricercaAsservimentiForm = invertiOrdinamento(ricercaAsservimentiForm, formSession, null);
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionNext() != null && !ricercaAsservimentiForm.getActionNext().equals("")) {
                logger.info(prefixLogMethod + " *** NAVIGAZIONE NEXT ***");
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_NEXT, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                RicercaAsservimentiForm formSession = (RicercaAsservimentiForm) request.getSession().getAttribute("ricercaAsservimentiForm");
                ricercaAsservimentiForm = invertiOrdinamento(ricercaAsservimentiForm, formSession, null);
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionVisualizzaRisultati() != null && ricercaAsservimentiForm.getActionVisualizzaRisultati().equals("visualizza")) {
                int paginaCorrente = ricercaAsservimentiForm.getPaginaCorrente();
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                if (paginaCorrente == -1) {
                    aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_INIT, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                } else {
                    aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_TO, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                }
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionRicercaAsservimenti() != null && !ricercaAsservimentiForm.getActionRicercaAsservimenti().equals("")) {
                logger.info(prefixLogMethod + " *** RICERCA ***");
                conn = BaseDAO.getconn();
                MacchinaDAO macchinaDAO = new MacchinaDAO(conn);
                Macchina macchina = new Macchina();
                Spinta[] elencoSpinte = (Spinta[]) application.getAttribute(Constants.ELENCO_SPINTE);
                Luce[] elencoLuci = (Luce[]) application.getAttribute(Constants.ELENCO_LUCI);
                Corsa[] elencoCorse = (Corsa[]) application.getAttribute(Constants.ELENCO_CORSE);
                macchina.setCategoria(new Categoria(Constants.ID_ASSERVIMENTO, null));
                if (ricercaAsservimentiForm.getId_costruttore() != null && !ricercaAsservimentiForm.getId_costruttore().equals("")) {
                    macchina.setCostruttore(new Costruttore(ricercaAsservimentiForm.getId_costruttore(), ""));
                }
                if (ricercaAsservimentiForm.getId_anno() != null && !ricercaAsservimentiForm.getId_anno().equals("")) {
                    macchina.setId_anno(ricercaAsservimentiForm.getId_anno());
                }
                if (ricercaAsservimentiForm.getId_ubicazione() != null && !ricercaAsservimentiForm.getId_ubicazione().equals("")) {
                    macchina.setUbicazione(new Ubicazione(ricercaAsservimentiForm.getId_ubicazione(), ""));
                }
                if (ricercaAsservimentiForm.getId_tipo() != null && !ricercaAsservimentiForm.getId_tipo().equals("")) {
                    macchina.setTipo(new Tipo(ricercaAsservimentiForm.getId_tipo(), null, ""));
                }
                if (ricercaAsservimentiForm.getId_stato() != null && !ricercaAsservimentiForm.getId_stato().equals("")) {
                    macchina.setStato_condizioni(new Stato(ricercaAsservimentiForm.getId_stato(), ""));
                }
                if (ricercaAsservimentiForm.getId_spinta() != null && !ricercaAsservimentiForm.getId_spinta().equals("")) {
                    for (int i = 0; i < elencoSpinte.length; i++) {
                        Spinta spinta = elencoSpinte[i];
                        if (ricercaAsservimentiForm.getId_spinta().equals(spinta.getId_spinta())) {
                            macchina.getElencoCaratteristiche().add(spinta);
                            break;
                        }
                    }
                }
                if (ricercaAsservimentiForm.getId_luce() != null && !ricercaAsservimentiForm.getId_luce().equals("")) {
                    for (int i = 0; i < elencoLuci.length; i++) {
                        Luce luce = elencoLuci[i];
                        if (ricercaAsservimentiForm.getId_luce().equals(luce.getId_luce())) {
                            macchina.getElencoCaratteristiche().add(luce);
                            break;
                        }
                    }
                }
                if (ricercaAsservimentiForm.getId_corsa() != null && !ricercaAsservimentiForm.getId_corsa().equals("")) {
                    for (int i = 0; i < elencoCorse.length; i++) {
                        Corsa corsa = elencoCorse[i];
                        if (ricercaAsservimentiForm.getId_corsa().equals(corsa.getId_corsa())) {
                            macchina.getElencoCaratteristiche().add(corsa);
                            break;
                        }
                    }
                }
                elencoAsservimenti = macchinaDAO.select(macchina, country, null, null);
                logger.info(prefixLogMethod + " elencoAsservimenti:" + elencoAsservimenti.length);
                ricercaAsservimentiForm.setId_costruttore(null);
                if (elencoAsservimenti != null && elencoAsservimenti.length > 0) {
                    ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                    ricercaAsservimentiForm.setVersoAnno(Constants.ORDINAMENTO_ASC);
                    ricercaAsservimentiForm.setVersoCostruttore(Constants.ORDINAMENTO_ASC);
                    ricercaAsservimentiForm.setVersoStato(Constants.ORDINAMENTO_ASC);
                    ricercaAsservimentiForm.setVersoTipo(Constants.ORDINAMENTO_ASC);
                    ricercaAsservimentiForm.setVersoUbicazione(Constants.ORDINAMENTO_ASC);
                    request.getSession().setAttribute("ricercaAsservimentiForm", ricercaAsservimentiForm);
                    request.getSession().setAttribute(Constants.ELENCO_ASSERVIMENTI, elencoAsservimenti);
                    aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_INIT, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                    go = "risultati";
                } else {
                    ActionMessages messages = new ActionMessages();
                    messages.add("ricercaAsservimenti", new ActionMessage(Constants.MSG_NESSUN_RISULTATO));
                    saveMessages(request, messages);
                    go = "refine";
                }
            } else if (ricercaAsservimentiForm.getActionRicercaAsservimentiTipo() != null && !ricercaAsservimentiForm.getActionRicercaAsservimentiTipo().equalsIgnoreCase("")) {
                logger.info(prefixLogMethod + " *** RICERCA X TIPO ***");
                try {
                    conn = BaseDAO.getconn();
                    MacchinaDAO macchinaDAO = new MacchinaDAO(conn);
                    Macchina macchina = new Macchina();
                    macchina.setTipo(new Tipo(ricercaAsservimentiForm.getId_tipo(), Constants.ID_MACCHINA_UTENSILE, null));
                    elencoAsservimenti = macchinaDAO.select(macchina, country, null, null);
                    if (elencoAsservimenti != null && elencoAsservimenti.length > 0) {
                        System.out.println(Utility.intestazioneLog(nameClass, methodName) + " elencoAsservimenti:" + elencoAsservimenti.length);
                        ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                        ricercaAsservimentiForm.setVersoAnno(Constants.ORDINAMENTO_ASC);
                        ricercaAsservimentiForm.setVersoCostruttore(Constants.ORDINAMENTO_ASC);
                        ricercaAsservimentiForm.setVersoStato(Constants.ORDINAMENTO_ASC);
                        ricercaAsservimentiForm.setVersoTipo(Constants.ORDINAMENTO_ASC);
                        ricercaAsservimentiForm.setVersoUbicazione(Constants.ORDINAMENTO_ASC);
                        request.getSession().setAttribute("ricercaAsservimentiForm", ricercaAsservimentiForm);
                        request.getSession().setAttribute(Constants.ELENCO_ASSERVIMENTI, elencoAsservimenti);
                        aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_INIT, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                        go = "risultati";
                    } else {
                        ActionMessages messages = new ActionMessages();
                        messages.add("ricercaAsservimenti", new ActionMessage(Constants.MSG_NESSUN_RISULTATO));
                        saveMessages(request, messages);
                        go = "refine";
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else if (ricercaAsservimentiForm.getDesc_costruttore() != null && !ricercaAsservimentiForm.getDesc_costruttore().equals("")) {
                logger.info(prefixLogMethod + " RICERCA X INIZIALE DEL COSTRUTTORE");
                try {
                    String xml = null;
                    conn = BaseDAO.getconn();
                    CostruttoreDAO costruttoreDAO = new CostruttoreDAO(conn);
                    Costruttore costruttore = new Costruttore();
                    costruttore.setStartsWith(ricercaAsservimentiForm.getDesc_costruttore());
                    List list = Arrays.asList(costruttoreDAO.select(null, null, costruttore));
                    xml = new AjaxXmlBuilder().addItems(list, "desc_costruttore", "id_costruttore", true).toString();
                    response.setContentType("text/xml");
                    response.setHeader("Cache-Control", "no-cache");
                    PrintWriter pw = response.getWriter();
                    pw.write(xml);
                    pw.close();
                    return null;
                } catch (Throwable t) {
                    t.printStackTrace();
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not create response");
                    return null;
                }
            } else if (ricercaAsservimentiForm.getActionNuovaRicerca() != null && !ricercaAsservimentiForm.getActionNuovaRicerca().equals("")) {
                go = "refine";
            } else if (ricercaAsservimentiForm.getActionDownload() != null && ricercaAsservimentiForm.getActionDownload().equalsIgnoreCase("pdf")) {
                try {
                    Font[] fonts = new Font[14];
                    fonts[0] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.NORMAL);
                    fonts[1] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.ITALIC);
                    fonts[2] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD);
                    fonts[3] = new Font(Font.COURIER, Font.DEFAULTSIZE, Font.BOLD | Font.ITALIC);
                    fonts[4] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.NORMAL);
                    fonts[5] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.ITALIC);
                    fonts[6] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLD);
                    fonts[7] = new Font(Font.HELVETICA, Font.DEFAULTSIZE, Font.BOLDITALIC);
                    fonts[8] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.NORMAL);
                    fonts[9] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.ITALIC);
                    fonts[10] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLD);
                    fonts[11] = new Font(Font.TIMES_ROMAN, Font.DEFAULTSIZE, Font.BOLDITALIC);
                    fonts[12] = new Font(Font.SYMBOL);
                    fonts[13] = new Font(Font.ZAPFDINGBATS);
                    MessageResources messages = getResources(request);
                    String label_costruttore = messages.getMessage(locale, Constants.LABEL_COSTRUTTORE);
                    String label_tipo = messages.getMessage(locale, Constants.LABEL_TIPO);
                    String label_anno = messages.getMessage(locale, Constants.LABEL_ANNO);
                    String label_stato = messages.getMessage(locale, Constants.LABEL_STATO);
                    String label_ubicazione = messages.getMessage(locale, Constants.LABEL_UBICAZIONE);
                    String label_dettaglio = messages.getMessage(locale, Constants.LABEL_DETTAGLIO);
                    String label_foto = messages.getMessage(locale, Constants.LABEL_FOTO);
                    String label_video = messages.getMessage(locale, Constants.LABEL_VIDEO);
                    String nomeFileXls = "Elenco_Asservimenti";
                    response.setContentType("application/download;");
                    response.setHeader("Content-Disposition", "attachment; filename=" + nomeFileXls + ".pdf;");
                    OutputStream stream = response.getOutputStream();
                    Document document = new Document();
                    PdfWriter.getInstance(document, stream);
                    document.open();
                    float[] widths = { 0.1f, 0.2f, 0.2f, 0.1f, 0.20f, 0.20f };
                    PdfPTable table = new PdfPTable(widths);
                    PdfPCell costruttore = new PdfPCell(new Paragraph(label_costruttore, fonts[6]));
                    PdfPCell tipo = new PdfPCell(new Paragraph(label_tipo, fonts[6]));
                    PdfPCell anno = new PdfPCell(new Paragraph(label_anno, fonts[6]));
                    PdfPCell stato = new PdfPCell(new Paragraph(label_stato, fonts[6]));
                    PdfPCell ubicazione = new PdfPCell(new Paragraph(label_ubicazione, fonts[6]));
                    table.addCell("N�");
                    table.addCell(costruttore);
                    table.addCell(tipo);
                    table.addCell(anno);
                    table.addCell(stato);
                    table.addCell(ubicazione);
                    elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                    for (int i = 0; i < elencoAsservimenti.length; i++) {
                        Macchina macchina = elencoAsservimenti[i];
                        int id = i + 1;
                        table.addCell(id + "");
                        table.addCell(macchina.getCostruttore().getDesc_costruttore());
                        table.addCell(macchina.getTipo().getDesc_tipo());
                        table.addCell(macchina.getId_anno());
                        table.addCell(macchina.getStato_condizioni().getDesc_stato());
                        table.addCell(macchina.getUbicazione().getDesc_ubicazione());
                    }
                    document.add(table);
                    document.close();
                    stream.close();
                    return null;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else if (ricercaAsservimentiForm.getActionDownload() != null && ricercaAsservimentiForm.getActionDownload().equalsIgnoreCase("xls")) {
                try {
                    elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                    String nomeFileXls = "Elenco_Asservimenti";
                    response.setContentType("application/download;");
                    response.setHeader("Content-Disposition", "attachment; filename=" + nomeFileXls + ".xls;");
                    OutputStream stream = response.getOutputStream();
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    HSSFSheet sheet = null;
                    sheet = workbook.createSheet("Elenco Asservimenti");
                    HSSFRow row = sheet.createRow((short) 0);
                    HSSFCellStyle styleBold = workbook.createCellStyle();
                    HSSFFont font = workbook.createFont();
                    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    styleBold.setFont(font);
                    MessageResources messages = getResources(request);
                    String label_costruttore = messages.getMessage(locale, Constants.LABEL_COSTRUTTORE);
                    String label_tipo = messages.getMessage(locale, Constants.LABEL_TIPO);
                    String label_anno = messages.getMessage(locale, Constants.LABEL_ANNO);
                    String label_stato = messages.getMessage(locale, Constants.LABEL_STATO);
                    String label_ubicazione = messages.getMessage(locale, Constants.LABEL_UBICAZIONE);
                    String label_dettaglio = messages.getMessage(locale, Constants.LABEL_DETTAGLIO);
                    String label_foto = messages.getMessage(locale, Constants.LABEL_FOTO);
                    String label_video = messages.getMessage(locale, Constants.LABEL_VIDEO);
                    HSSFCell cell0 = row.createCell((short) 0);
                    HSSFCell cell1 = row.createCell((short) 1);
                    HSSFCell cell2 = row.createCell((short) 2);
                    HSSFCell cell3 = row.createCell((short) 3);
                    HSSFCell cell4 = row.createCell((short) 4);
                    cell0.setCellValue(label_costruttore);
                    cell1.setCellValue(label_tipo);
                    cell2.setCellValue(label_anno);
                    cell3.setCellValue(label_stato);
                    cell4.setCellValue(label_ubicazione);
                    cell0.setCellStyle(styleBold);
                    cell1.setCellStyle(styleBold);
                    cell2.setCellStyle(styleBold);
                    cell3.setCellStyle(styleBold);
                    cell4.setCellStyle(styleBold);
                    for (int i = 0; i < elencoAsservimenti.length; i++) {
                        row = sheet.createRow((short) i + 1);
                        Macchina macchina = elencoAsservimenti[i];
                        row.createCell((short) 0).setCellValue(macchina.getCostruttore().getDesc_costruttore());
                        row.createCell((short) 1).setCellValue(macchina.getTipo().getDesc_tipo());
                        row.createCell((short) 2).setCellValue(macchina.getId_anno());
                        row.createCell((short) 3).setCellValue(macchina.getStato_condizioni().getDesc_stato());
                        row.createCell((short) 4).setCellValue(macchina.getUbicazione().getDesc_ubicazione());
                    }
                    workbook.write(stream);
                    stream.close();
                    return null;
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            } else if (ricercaAsservimentiForm.getActionOrdina() != null && !ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase("")) {
                logger.info(prefixLogMethod + " *** ORDINAMENTO ***");
                RicercaAsservimentiForm ricercaAsservimentiFormSession = (RicercaAsservimentiForm) request.getSession().getAttribute("ricercaAsservimentiForm");
                ricercaAsservimentiForm = invertiOrdinamento(ricercaAsservimentiForm, ricercaAsservimentiFormSession, ricercaAsservimentiForm.getActionOrdina());
                elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                MacchinaSort sort = new MacchinaSort();
                if (ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase(Constants.ORDINAMENTO_X_COSTRUTTORE)) {
                    Comparator orderByCostruttore = sort.getOrderByCostruttoreComparator(ricercaAsservimentiForm.getVersoCostruttore());
                    Arrays.sort(elencoAsservimenti, orderByCostruttore);
                }
                if (ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase(Constants.ORDINAMENTO_X_TIPO)) {
                    Comparator orderByTipo = sort.getOrderByTipoComparator(ricercaAsservimentiForm.getVersoTipo());
                    Arrays.sort(elencoAsservimenti, orderByTipo);
                }
                if (ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase(Constants.ORDINAMENTO_X_ANNO)) {
                    Comparator orderByAnno = sort.getOrderByAnnoComparator(ricercaAsservimentiForm.getVersoAnno());
                    Arrays.sort(elencoAsservimenti, orderByAnno);
                }
                if (ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase(Constants.ORDINAMENTO_X_STATO)) {
                    Comparator orderByStato = sort.getOrderByStatoComparator(ricercaAsservimentiForm.getVersoStato());
                    Arrays.sort(elencoAsservimenti, orderByStato);
                }
                if (ricercaAsservimentiForm.getActionOrdina().equalsIgnoreCase(Constants.ORDINAMENTO_X_UBICAZIONE)) {
                    Comparator orderByUbicazione = sort.getOrderByUbicazioneComparator(ricercaAsservimentiForm.getVersoUbicazione());
                    Arrays.sort(elencoAsservimenti, orderByUbicazione);
                }
                ricercaAsservimentiForm.setElencoAsservimenti(elencoAsservimenti);
                aggiornaNavigazione(ricercaAsservimentiForm, Constants.NAV_TO, Constants.NAV_RISULTATI_PER_PAGINA, -1);
                request.getSession().setAttribute("ricercaAsservimentiForm", ricercaAsservimentiForm);
                go = "risultati";
            } else if (ricercaAsservimentiForm.getActionFoto() != null && ricercaAsservimentiForm.getActionFoto().equalsIgnoreCase("visualizza")) {
                logger.info(prefixLogMethod + " *** FOTO ***");
                try {
                    elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                    for (int i = 0; i < elencoAsservimenti.length; i++) {
                        Macchina asservimento = elencoAsservimenti[i];
                        if (asservimento.getId_macchina().equals(ricercaAsservimentiForm.getId_macchina())) {
                            ricercaAsservimentiForm.setAsservimento(asservimento);
                            System.out.println(Utility.intestazioneLog(nameClass, methodName) + " TROVATA!:\n" + asservimento.toString());
                            break;
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                go = "foto";
            } else if (ricercaAsservimentiForm.getActionVideo() != null && ricercaAsservimentiForm.getActionVideo().equalsIgnoreCase("visualizza")) {
                logger.info(prefixLogMethod + " *** VIDEO ***");
                try {
                    elencoAsservimenti = (Macchina[]) request.getSession().getAttribute(Constants.ELENCO_ASSERVIMENTI);
                    for (int i = 0; i < elencoAsservimenti.length; i++) {
                        Macchina asservimento = elencoAsservimenti[i];
                        if (asservimento.getId_macchina().equals(ricercaAsservimentiForm.getId_macchina())) {
                            ricercaAsservimentiForm.setAsservimento(asservimento);
                            logger.info(prefixLogMethod + " TROVATA!:\n" + asservimento.toString());
                            break;
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                go = "video";
            } else {
                logger.info(prefixLogMethod + " *** ELSE ***");
                Categoria[] elencoCategorie = (Categoria[]) application.getAttribute(Constants.ELENCO_CATEGORIE);
                Anno[] elencoAnni = (Anno[]) application.getAttribute(Constants.ELENCO_ANNI);
                Ubicazione[] elencoUbicazioni = (Ubicazione[]) application.getAttribute(Constants.ELENCO_UBICAZIONI);
                Stato[] elencoStati = (Stato[]) application.getAttribute(Constants.ELENCO_STATI);
                Spinta[] elencoSpinte = (Spinta[]) application.getAttribute(Constants.ELENCO_SPINTE);
                Luce[] elencoLuci = (Luce[]) application.getAttribute(Constants.ELENCO_LUCI);
                Corsa[] elencoCorse = (Corsa[]) application.getAttribute(Constants.ELENCO_CORSE);
                Costruttore[] elencoCostruttori = (Costruttore[]) application.getAttribute(Constants.ELENCO_COSTRUTTORI);
                Tipo[] elencoTipiAsservimento = (Tipo[]) application.getAttribute(Constants.ELENCO_TIPI_ASSERVIMENTO);
                ricercaAsservimentiForm.setElencoCategorie(elencoCategorie);
                ricercaAsservimentiForm.setElencoCostruttori(elencoCostruttori);
                ricercaAsservimentiForm.setElencoAnni(elencoAnni);
                ricercaAsservimentiForm.setElencoCorse(elencoCorse);
                ricercaAsservimentiForm.setElencoLuci(elencoLuci);
                ricercaAsservimentiForm.setElencoSpinte(elencoSpinte);
                ricercaAsservimentiForm.setElencoStati(elencoStati);
                ricercaAsservimentiForm.setElencoUbicazioni(elencoUbicazioni);
                ricercaAsservimentiForm.setElencoTipiAsservimento(elencoTipiAsservimento);
                request.getSession().setAttribute("ricercaAsservimentiForm", ricercaAsservimentiForm);
                go = "refine";
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            logger.info(prefixLogMethod + " DOVE STO ANDANDO?!?! " + go);
            logger.debug(prefixLogMethod + "END.");
        }
        return mapping.findForward(go);
    }

    public void aggiornaNavigazione(RicercaAsservimentiForm ricercaAsservimentiForm, int operazione, int risultatiPerPagina, int pageTo) throws Throwable {
        if (ricercaAsservimentiForm.getElencoAsservimenti() != null) {
            int numeroRisultati = ricercaAsservimentiForm.getElencoAsservimenti().length;
            int modulo = numeroRisultati % risultatiPerPagina;
            int numeroPagine = (int) Math.ceil(numeroRisultati / risultatiPerPagina) + (modulo == 0 ? 0 : 1);
            int paginaCorrente = ricercaAsservimentiForm.getPaginaCorrente();
            if (numeroRisultati == 0) {
                ricercaAsservimentiForm.setNumeroRisultati(0);
                ricercaAsservimentiForm.setNumeroPagine(0);
                ricercaAsservimentiForm.setPaginaCorrente(0);
                ricercaAsservimentiForm.setMostraPrecedente(false);
                ricercaAsservimentiForm.setMostraSuccessiva(false);
                ricercaAsservimentiForm.setMostraPrima(false);
                ricercaAsservimentiForm.setMostraUltima(false);
                ricercaAsservimentiForm.setMacchinaDa("" + 0);
                ricercaAsservimentiForm.setMacchinaA("" + 0);
                ricercaAsservimentiForm.setMacchinaLength("" + 0);
            } else {
                if ((operazione == Constants.NAV_INIT) || (operazione == Constants.NAV_FIRST)) {
                    paginaCorrente = 1;
                } else if (operazione == Constants.NAV_NEXT) {
                    paginaCorrente = Math.min(paginaCorrente + 1, numeroPagine);
                } else if (operazione == Constants.NAV_PREV) {
                    paginaCorrente = Math.max(1, paginaCorrente - 1);
                } else if (operazione == Constants.NAV_LAST) {
                    paginaCorrente = numeroPagine;
                } else if ((operazione == Constants.NAV_TO) && (pageTo >= 1) && (pageTo <= numeroPagine)) {
                    paginaCorrente = pageTo;
                } else if (operazione == Constants.NAV_REFRESH) {
                }
                ricercaAsservimentiForm.setNumeroRisultati(numeroRisultati);
                ricercaAsservimentiForm.setNumeroPagine(numeroPagine);
                ricercaAsservimentiForm.setPaginaCorrente(paginaCorrente);
                ricercaAsservimentiForm.setMostraPrecedente(paginaCorrente > 1);
                ricercaAsservimentiForm.setMostraSuccessiva(paginaCorrente + 1 <= numeroPagine);
                ricercaAsservimentiForm.setMostraPrima(paginaCorrente > 1);
                ricercaAsservimentiForm.setMostraUltima(paginaCorrente + 1 <= numeroPagine);
                int macchinaDa = (paginaCorrente - 1) * risultatiPerPagina;
                int macchinaA = Math.min(paginaCorrente * risultatiPerPagina - 1, numeroRisultati - 1);
                ricercaAsservimentiForm.setMacchinaDa("" + macchinaDa);
                ricercaAsservimentiForm.setMacchinaA("" + macchinaA);
                ricercaAsservimentiForm.setMacchinaLength("" + Math.min(macchinaA - macchinaDa + 1, risultatiPerPagina));
            }
        }
    }

    private RicercaAsservimentiForm invertiOrdinamento(RicercaAsservimentiForm formRequest, RicercaAsservimentiForm formSession, String campo) {
        try {
            if (campo != null && campo.equalsIgnoreCase(Constants.ORDINAMENTO_X_ANNO)) {
                if (formRequest.getVersoAnno() == null || formRequest.getVersoAnno().equals("") || formRequest.getVersoAnno().equalsIgnoreCase(Constants.ORDINAMENTO_DESC)) {
                    formRequest.setVersoAnno(Constants.ORDINAMENTO_ASC);
                } else {
                    formRequest.setVersoAnno(Constants.ORDINAMENTO_DESC);
                }
                formRequest.setVersoCostruttore(formSession.getVersoCostruttore());
                formRequest.setVersoStato(formSession.getVersoStato());
                formRequest.setVersoTipo(formSession.getVersoTipo());
                formRequest.setVersoUbicazione(formSession.getVersoUbicazione());
            } else if (campo != null && campo.equalsIgnoreCase(Constants.ORDINAMENTO_X_COSTRUTTORE)) {
                if (formRequest.getVersoCostruttore() == null || formRequest.getVersoCostruttore().equals("") || formRequest.getVersoCostruttore().equalsIgnoreCase(Constants.ORDINAMENTO_DESC)) {
                    formRequest.setVersoCostruttore(Constants.ORDINAMENTO_ASC);
                } else {
                    formRequest.setVersoCostruttore(Constants.ORDINAMENTO_DESC);
                }
                formRequest.setVersoAnno(formSession.getVersoAnno());
                formRequest.setVersoStato(formSession.getVersoStato());
                formRequest.setVersoTipo(formSession.getVersoTipo());
                formRequest.setVersoUbicazione(formSession.getVersoUbicazione());
            } else if (campo != null && campo.equalsIgnoreCase(Constants.ORDINAMENTO_X_STATO)) {
                if (formRequest.getVersoStato() == null || formRequest.getVersoStato().equals("") || formRequest.getVersoStato().equalsIgnoreCase(Constants.ORDINAMENTO_DESC)) {
                    formRequest.setVersoStato(Constants.ORDINAMENTO_ASC);
                } else {
                    formRequest.setVersoStato(Constants.ORDINAMENTO_DESC);
                }
                formRequest.setVersoAnno(formSession.getVersoAnno());
                formRequest.setVersoCostruttore(formSession.getVersoCostruttore());
                formRequest.setVersoTipo(formSession.getVersoTipo());
                formRequest.setVersoUbicazione(formSession.getVersoUbicazione());
            } else if (campo != null && campo.equalsIgnoreCase(Constants.ORDINAMENTO_X_TIPO)) {
                if (formRequest.getVersoTipo() == null || formRequest.getVersoTipo().equals("") || formRequest.getVersoTipo().equalsIgnoreCase(Constants.ORDINAMENTO_DESC)) {
                    formRequest.setVersoTipo(Constants.ORDINAMENTO_ASC);
                } else {
                    formRequest.setVersoTipo(Constants.ORDINAMENTO_DESC);
                }
                formRequest.setVersoAnno(formSession.getVersoAnno());
                formRequest.setVersoCostruttore(formSession.getVersoCostruttore());
                formRequest.setVersoStato(formSession.getVersoStato());
                formRequest.setVersoUbicazione(formSession.getVersoUbicazione());
            } else if (campo != null && campo.equalsIgnoreCase(Constants.ORDINAMENTO_X_UBICAZIONE)) {
                if (formRequest.getVersoUbicazione() == null || formRequest.getVersoUbicazione().equals("") || formRequest.getVersoUbicazione().equalsIgnoreCase(Constants.ORDINAMENTO_DESC)) {
                    formRequest.setVersoUbicazione(Constants.ORDINAMENTO_ASC);
                } else {
                    formRequest.setVersoUbicazione(Constants.ORDINAMENTO_DESC);
                }
                formRequest.setVersoAnno(formSession.getVersoAnno());
                formRequest.setVersoCostruttore(formSession.getVersoCostruttore());
                formRequest.setVersoStato(formSession.getVersoStato());
                formRequest.setVersoTipo(formSession.getVersoTipo());
            } else {
                formRequest.setVersoAnno(formSession.getVersoAnno());
                formRequest.setVersoCostruttore(formSession.getVersoCostruttore());
                formRequest.setVersoStato(formSession.getVersoStato());
                formRequest.setVersoTipo(formSession.getVersoTipo());
                formRequest.setVersoUbicazione(formSession.getVersoUbicazione());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return formRequest;
    }
}
