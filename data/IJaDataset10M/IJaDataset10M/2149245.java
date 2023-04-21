package com.beardediris.ajaqs.util;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.beardediris.ajaqs.db.Answer;
import com.beardediris.ajaqs.db.Faq;
import com.beardediris.ajaqs.db.FaqUser;
import com.beardediris.ajaqs.db.Project;
import com.beardediris.ajaqs.db.Question;
import com.beardediris.ajaqs.ex.DatabaseNotFoundException;
import com.beardediris.ajaqs.ex.FaqNotFoundException;
import com.beardediris.ajaqs.ex.ProjectNotFoundException;
import com.beardediris.ajaqs.ex.UserNotFoundException;
import com.beardediris.ajaqs.oql.QueryDB;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;
import org.exolab.castor.jdo.PersistenceException;
import org.exolab.castor.jdo.TransactionNotInProgressException;

/**
 * <p>This class implements a servlet used to download an FAQ
 * as a PDF document.  See <tt>web.xml</tt> and
 * <tt>projectpage.jsp</tt> for how this servlet is used.</p>
 */
public final class GetFaqPdf extends Submitter {

    private static final Logger logger = Logger.getLogger(GetFaqPdf.class.getName());

    /**
     * Servlet parameters.
     */
    private static final String PROJECT = "project";

    private static final String FAQ = "faq";

    /**
     * Bogus faq ID used to denote absence of FAQ parameter.
     * See notes below.
     */
    private static final int NEED_FULL_PROJ = -1000;

    private void insertFaq(Faq faq, Document doc, Font titFont) throws DocumentException {
        Font qfont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, new Color(0, 0, 255));
        doc.add(new Paragraph(faq.getName(), titFont));
        Iterator questIt = faq.getQuestions().iterator();
        while (questIt.hasNext()) {
            Question q = (Question) questIt.next();
            logger.info("Adding question");
            Paragraph pq = new Paragraph(q.getQuestion(), qfont);
            doc.add(pq);
            List ansList = new List(true, 20);
            Iterator ansIt = q.getAnswers().iterator();
            while (ansIt.hasNext()) {
                logger.info("Adding answer to list");
                Answer a = (Answer) ansIt.next();
                ListItem lit = new ListItem(a.getAnswer());
                ansList.add(lit);
            }
            logger.info("Adding list");
            doc.add(ansList);
            doc.add(Chunk.NEWLINE);
        }
    }

    private void servePdf(QueryDB query, String logon, int projId, int faqId, HttpServletResponse resp) throws ServletException, IOException {
        FaqUser fuser = null;
        try {
            fuser = query.getFaqUser(logon, false);
        } catch (UserNotFoundException unfe) {
            throw (ServletException) new ServletException("FaqUser with logon \"" + logon + "\" not found").initCause(unfe);
        }
        Project proj = null;
        try {
            proj = fuser.getProject(projId);
        } catch (ProjectNotFoundException pnfe) {
            throw (ServletException) new ServletException("Could not edit FAQ").initCause(pnfe);
        }
        ArrayList faqList = new ArrayList();
        if (faqId >= 0) {
            try {
                faqList.add(proj.getFaq(faqId));
            } catch (FaqNotFoundException fnfe) {
                throw (ServletException) new ServletException("Could not edit FAQ").initCause(fnfe);
            }
        } else {
            faqList.addAll(proj.getFaqs());
        }
        resp.setContentType("application/pdf");
        resp.setHeader("Expires", "0");
        resp.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        resp.setHeader("Pragma", "public");
        StringBuffer title = new StringBuffer();
        title.append(proj.getName());
        String ts = (new Date()).toString();
        OutputStream ostream = resp.getOutputStream();
        try {
            Document doc = new Document(PageSize.LETTER);
            PdfWriter.getInstance(doc, ostream);
            logger.info("Opening Document");
            doc.open();
            Font titFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
            Font subtitFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
            logger.info("Adding title ... ");
            doc.add(new Paragraph(title.toString(), titFont));
            doc.add(new Paragraph(ts.toString(), subtitFont));
            doc.add(Chunk.NEWLINE);
            Iterator faqIt = faqList.iterator();
            while (faqIt.hasNext()) {
                Faq faq = (Faq) faqIt.next();
                insertFaq(faq, doc, titFont);
            }
            logger.info("closing Document");
            doc.close();
        } catch (DocumentException dex) {
            throw (ServletException) new ServletException("Could not dynamically generate PDF output for FAQ").initCause(dex);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QueryDB query = null;
        try {
            query = new QueryDB(getServletContext());
        } catch (DatabaseNotFoundException dnfe) {
            serveError(req, resp, dnfe);
            return;
        }
        String logon = req.getRemoteUser();
        if (null == logon || logon.length() <= 0) {
            ServletException ex = new ServletException("Remote user not authenticated");
            serveError(req, resp, ex);
            return;
        }
        int projId;
        try {
            projId = parseInteger(req, PROJECT);
        } catch (Exception ex) {
            serveError(req, resp, ex);
            return;
        }
        int faqId;
        try {
            faqId = parseInteger(req, FAQ);
        } catch (Exception ex) {
            faqId = NEED_FULL_PROJ;
        }
        Exception pdfEx = null;
        try {
            query.getDb().begin();
            servePdf(query, logon, projId, faqId, resp);
            query.getDb().commit();
        } catch (Exception ex) {
            logger.info("could not end transaction: " + ex);
            pdfEx = ex;
            try {
                query.getDb().rollback();
            } catch (TransactionNotInProgressException tnpe) {
                logger.info("could not rollback transaction: " + tnpe);
            }
        } finally {
            try {
                query.getDb().close();
            } catch (PersistenceException pe) {
            }
        }
        if (null != pdfEx) {
            serveError(req, resp, pdfEx);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
