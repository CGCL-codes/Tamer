package org.compiere.acct;

import java.math.*;
import java.sql.*;
import java.util.*;
import org.compiere.model.*;
import java.util.logging.*;
import org.compiere.util.*;

/**
 *  Post Invoice Documents.
 *  <pre>
 *  Table:              GL_Journal (224)
 *  Document Types:     GLJ
 *  </pre>
 *  @author Jorg Janke
 *  @version  $Id: Doc_GLJournal.java,v 1.3 2006/07/30 00:53:33 jjanke Exp $
 */
public class Doc_GLJournal extends Doc {

    /**
	 *  Constructor
	 * 	@param ass accounting schemata
	 * 	@param rs record
	 * 	@param trxName trx
	 */
    protected Doc_GLJournal(MAcctSchema[] ass, ResultSet rs, String trxName) {
        super(ass, MJournal.class, rs, null, trxName);
    }

    /** Posting Type				*/
    private String m_PostingType = null;

    private int m_C_AcctSchema_ID = 0;

    /**
	 *  Load Specific Document Details
	 *  @return error message or null
	 */
    protected String loadDocumentDetails() {
        MJournal journal = (MJournal) getPO();
        m_PostingType = journal.getPostingType();
        m_C_AcctSchema_ID = journal.getC_AcctSchema_ID();
        p_lines = loadLines(journal);
        log.fine("Lines=" + p_lines.length);
        return null;
    }

    /**
	 *	Load Invoice Line
	 *	@param journal journal
	 *  @return DocLine Array
	 */
    private DocLine[] loadLines(MJournal journal) {
        ArrayList<DocLine> list = new ArrayList<DocLine>();
        MJournalLine[] lines = journal.getLines(false);
        for (int i = 0; i < lines.length; i++) {
            MJournalLine line = lines[i];
            DocLine docLine = new DocLine(line, this);
            docLine.setAmount(line.getAmtSourceDr(), line.getAmtSourceCr());
            docLine.setConvertedAmt(m_C_AcctSchema_ID, line.getAmtAcctDr(), line.getAmtAcctCr());
            MAccount account = line.getAccount();
            docLine.setAccount(account);
            list.add(docLine);
        }
        int size = list.size();
        DocLine[] dls = new DocLine[size];
        list.toArray(dls);
        return dls;
    }

    /**************************************************************************
	 *  Get Source Currency Balance - subtracts line and tax amounts from total - no rounding
	 *  @return positive amount, if total invoice is bigger than lines
	 */
    public BigDecimal getBalance() {
        BigDecimal retValue = Env.ZERO;
        StringBuffer sb = new StringBuffer(" [");
        for (int i = 0; i < p_lines.length; i++) {
            retValue = retValue.add(p_lines[i].getAmtSource());
            sb.append("+").append(p_lines[i].getAmtSource());
        }
        sb.append("]");
        log.fine(toString() + " Balance=" + retValue + sb.toString());
        return retValue;
    }

    /**
	 *  Create Facts (the accounting logic) for
	 *  GLJ.
	 *  (only for the accounting scheme, it was created)
	 *  <pre>
	 *      account     DR          CR
	 *  </pre>
	 *  @param as acct schema
	 *  @return Fact
	 */
    public ArrayList<Fact> createFacts(MAcctSchema as) {
        ArrayList<Fact> facts = new ArrayList<Fact>();
        if (as.getC_AcctSchema_ID() != m_C_AcctSchema_ID) return facts;
        Fact fact = new Fact(this, as, m_PostingType);
        if (getDocumentType().equals(DOCTYPE_GLJournal)) {
            for (int i = 0; i < p_lines.length; i++) {
                if (p_lines[i].getC_AcctSchema_ID() == as.getC_AcctSchema_ID()) {
                    FactLine line = fact.createLine(p_lines[i], p_lines[i].getAccount(), getC_Currency_ID(), p_lines[i].getAmtSourceDr(), p_lines[i].getAmtSourceCr());
                }
            }
        } else {
            p_Error = "DocumentType unknown: " + getDocumentType();
            log.log(Level.SEVERE, p_Error);
            fact = null;
        }
        facts.add(fact);
        return facts;
    }
}
