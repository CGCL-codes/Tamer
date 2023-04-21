package com.be.http.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.be.vo.LedgerSaldoVO;
import com.core.util.Resources;

public class LedgerSaldoView {

    public LedgerSaldoView() {
    }

    public void write(HttpServletResponse res, HttpSession session, String tableName) {
        System.out.println(session + " " + tableName);
        LedgerSaldoVO[] ledger = (LedgerSaldoVO[]) session.getAttribute(tableName);
        if (ledger == null) {
            return;
        }
        try {
            PrintWriter out = res.getWriter();
            out.write("<div class='admin-content-head-title'>");
            if (ledger.length > 0) {
                Resources resources = new Resources(session);
                out.write(resources.getString("report.title.saldo.list") + ": " + ledger[0].getEvalStart() + " - " + ledger[0].getEvalDate());
            }
            out.write("</div>");
            out.write("<table class='table-data3' cellpadding='0' cellspacing='0' border='0'>");
            out.write("<thead><tr>");
            out.write("<th>" + "Konto" + "</th><th>" + "Name" + "</th><th>" + "Typ" + "</th><th class='th-right'>" + "Start" + "</th><th class='th-right'>" + "Vorjahr" + "</th><th class='th-right'>" + "Soll" + "</th><th class='th-right'>" + "Haben" + "</th><th class='th-right'>" + "Saldo" + "</th><th class='th-right'>" + "Whr." + "</th><th class='th-right'>" + "Kurs" + "</th><th class='th-right'>" + "Real" + "</th>");
            out.write("</tr></thead><tbody>");
            for (int i = 0; i < ledger.length; i++) {
                out.write("<tr>");
                out.write("<td>" + ledger[i].getAccountID() + "</td><td>" + ledger[i].getLedgerName().substring(0, Math.min(30, ledger[i].getLedgerName().length())) + "</td><td>" + ledger[i].getLedgerType() + "</td><td class='td-right'>" + ledger[i].getAmountStart() + "</td><td class='td-right'>" + ledger[i].getAmountPrevious() + "</td><td class='td-right'>" + ledger[i].getAmountDebit() + "</td><td class='td-right'>" + ledger[i].getAmountCredit() + "</td><td class='td-right'>" + ledger[i].getAmount() + "</td><td class='td-right'>" + ledger[i].getCurrencyID() + "</td><td class='td-right'>" + ledger[i].getExchangeRate() + "</td><td class='td-right'>" + ledger[i].getAmountRef() + "</td>");
                out.write("</tr>");
            }
            out.write("</tbody></table>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
