package jds.com.locations.sites;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import jds.com.locations.AbstractFeeder;
import jds.com.service.QuotesPersistanceService;
import org.lab.dataUniverse.Quote;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebTable;

public class BolsaMadrid extends AbstractFeeder {

    private DateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public BolsaMadrid(String name, String tickerName, QuotesPersistanceService persistance) {
        super(name, tickerName, persistance);
    }

    public int download(String url) throws Exception {
        int results = 0;
        DecimalFormat dfLocal = new DecimalFormat();
        DecimalFormatSymbols dfsLocal = new DecimalFormatSymbols();
        dfsLocal.setDecimalSeparator(',');
        dfsLocal.setGroupingSeparator('.');
        dfLocal.setDecimalFormatSymbols(dfsLocal);
        WebConversation webConversation = new WebConversation();
        this.configProxy(webConversation);
        WebRequest webRequest = new GetMethodWebRequest(url);
        WebResponse webResponse = webConversation.getResponse(webRequest);
        WebTable webTable = webResponse.getTableStartingWith("Fecha");
        if (webTable == null) return 0;
        int rows = webTable.getRowCount();
        for (int row = 1; row < rows; row++) {
            String date = webTable.getCellAsText(row, 0);
            String cierre = webTable.getCellAsText(row, 1);
            String anterior = webTable.getCellAsText(row, 2);
            String volumen = webTable.getCellAsText(row, 3);
            String max = webTable.getCellAsText(row, 6);
            String min = webTable.getCellAsText(row, 7);
            Quote quote = null;
            Date dateD = DATE_FORMATTER.parse(date);
            quote = quotes.getByTickerAndDate(tickerName, dateD);
            if (quote == null) {
                quote = new Quote();
                quote.setDate(dateD);
            }
            quote.setClose(new Double(dfLocal.parse(cierre).doubleValue()));
            quote.setVolume(new Long(dfLocal.parse(volumen).longValue()));
            quote.setMax(new Double(dfLocal.parse(max).doubleValue()));
            quote.setMin(new Double(dfLocal.parse(min).doubleValue()));
            quote.setTicker(tickerName);
            quote.setOpen(new Double(dfLocal.parse(anterior).doubleValue()));
            quotes.add(quote);
            results++;
        }
        return results;
    }

    public String getUrl(int dayMin, int dayMax, int month, int year) {
        return ("http://www.bolsamadrid.es/comun/empresas/infhist.asp?id=esp&isin=" + name + "&bolsa=0&scontrata=02&tipo=htm&dia1=" + df2.format(dayMin) + "&mes1=" + df2.format(month + 1) + "&anyo1=" + (year) + "&dia2=" + df2.format(dayMax) + "&mes2=" + df2.format(month + 1) + "&anyo2=" + year + "&Buscar=Aceptar");
    }
}
