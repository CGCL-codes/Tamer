package com.be.http.view;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.be.vo.LedgerBookingVO;
import com.core.util.Resources;
import com.core.util.StringFormatter;

public class LedgerBookingView {

    public LedgerBookingView() {
    }

    public void write(HttpServletResponse res, HttpSession session, String tableName) {
        System.out.println(session + " " + tableName);
        LedgerBookingVO[] ledger = (LedgerBookingVO[]) session.getAttribute(tableName);
        if (ledger == null) {
            return;
        }
        try {
            PrintWriter out = res.getWriter();
            out.write("<div class='admin-content-head-title'>");
            if (ledger.length > 0) {
                Resources resources = new Resources(session);
                out.write(resources.getString("report.title.bookings") + ": ");
            }
            out.write("</div>");
            out.write("<table class='table-data3' cellpadding='0' cellspacing='0' border='0'>");
            out.write("<thead><tr>");
            out.write("<th>" + "Konto" + "</th><th>" + "Name" + "</th><th>" + "Typ" + "</th><th class='th-right'>" + "Start" + "</th><th class='th-right'>" + "Vorjahr" + "</th><th class='th-right'>" + "Soll" + "</th><th class='th-right'>" + "Haben" + "</th><th class='th-right'>" + "Saldo" + "</th><th class='th-right'>" + "W�hr." + "</th><th class='th-right'>" + "Kurs" + "</th><th class='th-right'>" + "Real" + "</th>");
            out.write("</tr></thead><tbody>");
            out.write("<tr>");
            out.write("<td><b>" + "OrderID" + "</b></td><td>" + "Text" + "</td><td>" + "Storno" + "</td><td class='td-right'>" + "Datum" + "</td><td class='td-right'>" + "Valuta" + "</td><td class='td-right'>" + "Soll" + "</td><td class='td-right'>" + "Haben" + "</td><td class='td-right'>" + "Saldo" + "</td><td class='td-right'>" + "W�hr." + "</td><td class='td-right'>" + "Kurs" + "</td><td class='td-right'>" + "Real" + "</td>");
            out.write("</tr>");
            for (int i = ledger.length - 1; i > 0; i--) {
                if (ledger[i].getOrderID() == 0) {
                    out.write("<tr class='tr-green'>");
                    out.write("<td>" + ledger[i].getAccountID() + "</td><td>" + ledger[i].getLedgerName().substring(0, Math.min(30, ledger[i].getLedgerName().length())) + "</td><td>" + ledger[i].getLedgerType() + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountStart()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountStartRef()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountDebit()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountCredit()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmount()) + "</td><td align='right'>" + ledger[i].getCurrencyID() + "</td><td align='right'>" + ledger[i].getExchangeRate() + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountRef()) + "</td>");
                    out.write("</tr>");
                } else {
                    out.write("<tr>");
                    out.write("<td><b>" + ledger[i].getOrderID() + "</b></td><td>" + ledger[i].getBookText().substring(0, Math.min(30, ledger[i].getBookText().length())) + "</td><td>" + StringFormatter.formatZero(ledger[i].getCancelID()) + "</td><td align='right'>" + ledger[i].getBookDate() + "</td><td align='right'>" + ledger[i].getValueDate() + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountDebit()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountCredit()) + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmount()) + "</td><td align='right'>" + ledger[i].getCurrencyID() + "</td><td align='right'>" + ledger[i].getExchangeRate() + "</td><td align='right'>" + StringFormatter.formatNull(ledger[i].getAmountRef()) + "</td>");
                    out.write("</tr>");
                }
            }
            out.write("</tbody></table>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
