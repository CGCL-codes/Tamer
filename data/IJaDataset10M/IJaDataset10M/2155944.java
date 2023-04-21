package edu.ncsu.buddi.plugins;

import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.homeunix.thecave.buddi.i18n.keys.PluginReportDateRangeChoices;
import org.homeunix.thecave.buddi.plugin.api.BuddiReportPlugin;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccount;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableAccountType;
import org.homeunix.thecave.buddi.plugin.api.model.ImmutableDocument;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlHelper;
import org.homeunix.thecave.buddi.plugin.api.util.HtmlPage;
import ca.digitalcave.moss.common.Version;
import ca.digitalcave.moss.swing.MossDocumentFrame;

public class BalanceSheet extends BuddiReportPlugin {

    @Override
    public HtmlPage getReport(ImmutableDocument model, MossDocumentFrame frame, Date startDate, Date endDate) {
        startDate = getDateOneYearAgo(endDate);
        StringBuilder sb = HtmlHelper.getHtmlHeader("Balance Sheet", "", startDate, endDate);
        sb.append(generateBalanceSheet(model, startDate, endDate));
        sb.append(HtmlHelper.getHtmlFooter());
        Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();
        return new HtmlPage(sb.toString(), images);
    }

    private Date getDateOneYearAgo(Date date) {
        Calendar tempCal = new GregorianCalendar();
        tempCal.setTime(date);
        tempCal.set(Calendar.YEAR, tempCal.get(Calendar.YEAR) - 1);
        return tempCal.getTime();
    }

    private String generateBalanceSheet(ImmutableDocument model, Date startDate, Date endDate) {
        HashMap<String, Long> assetMap = new HashMap<String, Long>();
        HashMap<String, Long> liabilityMap = new HashMap<String, Long>();
        mapAssetsLiabilities(model, startDate, endDate, assetMap, liabilityMap);
        long retained = getRetained(model, startDate);
        long totalAssets = getTotal(assetMap);
        long totalLiabilities = getTotal(liabilityMap);
        long total = totalAssets - totalLiabilities + retained;
        String sheetTable = "<table class=\"main\" align=\"center\">\n";
        Iterator<String> it;
        String tempName, tempNumber;
        sheetTable += "<tr class=\"main\"><td class=\"main\" colspan=\"2\"><b>Assets:</b></td></tr>\n";
        for (it = assetMap.keySet().iterator(); it.hasNext(); ) {
            tempName = it.next();
            tempNumber = getCurrencyFormat(assetMap.get(tempName).longValue());
            sheetTable += "<tr class=\"main\"><td class=\"main\">" + tempName + "</td><td class=\"main\" align=\"right\">" + tempNumber + "</td></tr>\n";
        }
        if (retained >= 0) {
            sheetTable += "<tr class=\"main\"><td class=\"main\">Retained Earnings</td><td class=\"main\" align=\"right\">" + getCurrencyFormat(retained) + "</td></tr>\n";
        }
        sheetTable += "<tr class=\"main\"><td class=\"main\"><i>Total</i></td><td class=\"main\" align=\"right\">" + getCurrencyFormat(totalAssets) + "</td></tr>\n";
        sheetTable += "<tr class=\"main\"><td class=\"main\" colspan=\"2\">&nbsp;</td></tr>\n";
        sheetTable += "<tr class=\"main\"><td class=\"main\" colspan=\"2\"><b>Liabilities:</b></tr>\n";
        for (it = liabilityMap.keySet().iterator(); it.hasNext(); ) {
            tempName = it.next();
            tempNumber = getCurrencyFormat(liabilityMap.get(tempName).longValue());
            sheetTable += "<tr class=\"main\"><td class=\"main\">" + tempName + "</td><td class=\"main\" align=\"right\">" + tempNumber + "</td></tr>\n";
        }
        if (retained < 0) {
            sheetTable += "<tr class=\"main\"><td class=\"main\">Retained Losses</td><td class=\"main\" align=\"right\">" + getCurrencyFormat(retained * (-1)) + "</td></tr>\n";
        }
        sheetTable += "<tr class=\"main\"><td class=\"main\"><i>Total</i></td><td class=\"main\" align=\"right\">" + getCurrencyFormat(totalLiabilities) + "</td></tr>\n";
        sheetTable += "<tr class=\"main\"><td class=\"main\" colspan=\"2\">&nbsp;</td></tr>\n";
        sheetTable += "<tr class=\"main\"><td class=\"main\"><b><i>Total:</i></b></td><td class=\"main\" align=\"right\">" + getCurrencyFormat(total) + "</td></tr>\n";
        return (sheetTable + "</table>\n");
    }

    private String getCurrencyFormat(long number) {
        double num = ((double) number) / 100.00;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        return nf.format(num);
    }

    private void mapAssetsLiabilities(ImmutableDocument model, Date startDate, Date endDate, HashMap<String, Long> assetMap, HashMap<String, Long> liabilityMap) {
        long tempVal;
        List<ImmutableAccount> accounts = model.getImmutableAccounts();
        for (Iterator<ImmutableAccount> it = accounts.iterator(); it.hasNext(); ) {
            ImmutableAccount account = it.next();
            ImmutableAccountType accountType = account.getAccountType();
            tempVal = account.getBalance(endDate) - account.getBalance(startDate);
            if (!accountType.isCredit()) {
                addValueToMap(assetMap, accountType.getName(), tempVal);
            } else {
                addValueToMap(liabilityMap, accountType.getName(), tempVal);
            }
        }
        return;
    }

    private void addValueToMap(HashMap<String, Long> map, String key, long value) {
        Long val = (Long) map.get(key);
        if (val != null) {
            map.put(key, new Long(val.longValue() + value));
        } else {
            map.put(key, new Long(value));
        }
        return;
    }

    private long getTotal(HashMap<String, Long> map) {
        long total = 0;
        for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
            String key = it.next();
            total += (map.get(key)).longValue();
        }
        return total;
    }

    private long getRetained(ImmutableDocument model, Date startDate) {
        long sum = 0;
        List<ImmutableAccount> accounts = model.getImmutableAccounts();
        for (Iterator<ImmutableAccount> it = accounts.iterator(); it.hasNext(); ) {
            ImmutableAccount account = it.next();
            if (!account.getAccountType().isCredit()) {
                sum += account.getBalance(startDate);
            } else {
                sum -= account.getBalance(startDate);
            }
        }
        return sum;
    }

    @Override
    public PluginReportDateRangeChoices getDateRangeChoice() {
        return PluginReportDateRangeChoices.END_ONLY;
    }

    public String getDescription() {
        return "Show me my balance sheets for";
    }

    public String getName() {
        return "Balance Sheet";
    }

    public boolean isPluginActive() {
        return true;
    }

    public Version getMaximumVersion() {
        return null;
    }

    public Version getMinimumVersion() {
        return null;
    }
}
