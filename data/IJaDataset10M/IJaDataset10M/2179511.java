package com.wwwc.index.web.servlet;

import java.io.*;
import java.util.*;
import com.wwwc.util.web.*;

public class GenerateStockChart {

    public StringBuffer getChart(String stockTick, String tickSid, String sector, String volume_link) {
        StringBuffer sbf = new StringBuffer();
        long ctime = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(ctime);
        int h = c.get(Calendar.HOUR);
        int m = c.get(Calendar.MINUTE);
        int d = c.get(Calendar.DAY_OF_WEEK);
        if (d > 1 && d < 7) {
            if (c.get(Calendar.AM_PM) == 1) {
                if (h < 4) {
                    sbf.append("\n\n<meta http-equiv=\"Refresh\" Content=\"" + 300 + "\">\n\n");
                }
            } else {
                if (h == 9 && m >= 30 || h > 9) {
                    sbf.append("\n\n<meta http-equiv=\"Refresh\" Content=\"" + 300 + "\">\n\n");
                }
            }
        }
        String yahoo = "http://biz.yahoo.com/p/" + (stockTick.substring(0, 1)).toLowerCase() + "/" + stockTick.toLowerCase() + ".html";
        sbf.append("<TABLE cellSpacing='2' cellPadding='2' border='0' style='font-size:8pt'>");
        sbf.append("<TR>");
        sbf.append(MyHtml.makeLinkCell("Volume", volume_link));
        sbf.append(MyHtml.makeLinkCell("Back", "javaScript:history.go(-1);"));
        sbf.append("<TD style='BORDER: 1px solid'>");
        sbf.append("<A href='" + yahoo + "' target='_blank' style='text-decoration: none;'>Profile</A>");
        sbf.append("</TD>");
        sbf.append("<TD style='BORDER: 1px solid'>");
        sbf.append("<A href='http://stockcharts.com/def/servlet/SC.web?c=" + stockTick);
        sbf.append(",uu[m,a]daclyiay[pb50!b200][vc60][iUb5!La12,26,9]&pref=G' target='_blank' style='text-decoration: none;'>");
        sbf.append("StockChart.com(1)</A>");
        sbf.append("</TD>");
        sbf.append("<TD style='BORDER: 1px solid'>");
        sbf.append("<A href='http://stockcharts.com/def/servlet/SC.web?c=" + stockTick);
        sbf.append(",uu[m,a]daclyiay[pb/50!b200][vc60][iUk10!Ld20]&pref=G' target='_blank' style='text-decoration: none;'>");
        sbf.append("StockChart.com(2)</A>");
        sbf.append("</TD>");
        sbf.append("<TD style='BORDER: 1px solid'>");
        sbf.append("<A href='http://iw.thomsonfn.com/iwatch/cgi-bin/iw_ticker?t=" + stockTick);
        sbf.append("&range=30&mgp=0&i=3&hdate=&x=16&y=13=' target='_blank' style='text-decoration: none;'>Thomsonfn.com</A>");
        sbf.append("</TD>");
        sbf.append("</TR>");
        sbf.append("</TABLE>");
        sbf.append("<TABLE>");
        sbf.append("<TR><TD>");
        sbf.append("<INPUT type=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=nasdaq&time=1dy&freq=1mi&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&lf=1&");
        sbf.append("lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=3291&country=US&intMode=3&");
        sbf.append("pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&partner=0&");
        sbf.append("mocktick=1&rand=5497\" BORDER=\"0\">");
        sbf.append("</TD>");
        sbf.append("<TD><INPUT type=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=" + stockTick + "&time=1dy&freq=1mi&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&lf=1&");
        sbf.append("lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=" + tickSid + "&country=US&");
        sbf.append("intMode=3&pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&");
        sbf.append("partner=0&mocktick=1&rand=5497\" BORDER=\"0\">");
        sbf.append("</TD></TR>");
        sbf.append("<TR><TD><INPUT type=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=" + stockTick + "&time=10dy&freq=1mi&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&");
        sbf.append("lf=1&lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=" + tickSid + "&country=US&");
        sbf.append("intMode=3&pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&");
        sbf.append("partner=0&mocktick=1&rand=5497\" BORDER=\"0\"></TD>");
        sbf.append("<TD><INPUT type=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=" + stockTick + "&time=6mo&freq=1dy&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&");
        sbf.append("lf=1&lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=" + tickSid + "&country=US&");
        sbf.append("intMode=3&pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&");
        sbf.append("partner=0&mocktick=1&rand=5497\" BORDER=\"0\">");
        sbf.append("</TD></TR>");
        sbf.append("<TR><TD><INPUT TYPE=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=nasdaq&time=10dy&freq=1mi&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&lf=1&");
        sbf.append("lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=3291&country=US&intMode=3&");
        sbf.append("pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&partner=0&");
        sbf.append("mocktick=1&rand=5497\" BORDER=\"0\"></TD>");
        sbf.append("<TD><INPUT TYPE=IMAGE SRC=\"https://realtime.bigcharts.com/custom/datek-com/chart.asp?");
        sbf.append("symb=nasdaq&time=6mo&freq=1dy&compidx=aaaaa%7E0&ma=0&maval=9&uf=0&");
        sbf.append("lf=1&lf2=0&lf3=0&type=2&style=962&size=1&state=0&sid=3291&country=US&");
        sbf.append("intMode=3&pageTitle=Chart+for+Nasdaq+Composite+Index&doChart=True&");
        sbf.append("partner=0&mocktick=1&rand=5497\" BORDER=\"0\">");
        sbf.append("</TD>");
        sbf.append("</TR></TABLE>");
        return sbf;
    }
}
