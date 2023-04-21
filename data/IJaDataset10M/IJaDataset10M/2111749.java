package net.sourceforge.nattable.data.pricing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PricingDataFileLoader<T> {

    public List<T> loadDataFromFile() throws IOException {
        List<T> data = new ArrayList<T>();
        int i = 0;
        DelimitedFileReader reader = new DelimitedFileReader(new BufferedReader(new InputStreamReader(PricingDataFileLoader.class.getResourceAsStream("pricing_data.txt"))), '\t');
        if (reader.ready() && reader.markSupported()) {
            while (reader.read() > 0) {
                i++;
                parseTabDelimitedLine(reader.getTabbedLineRead(), data);
            }
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    public void parseTabDelimitedLine(StringTokenizer tabs, List<T> data) {
        while (tabs.hasMoreElements()) {
            PricingDataBean bean = new PricingDataBean();
            bean.setBatsTicker(extractStringFromToken(tabs.nextToken()));
            bean.setBid(extractDoubleFromToken(tabs.nextToken()));
            bean.setAsk(extractDoubleFromToken(tabs.nextToken()));
            bean.setBidYield(extractDoubleFromToken(tabs.nextToken()));
            bean.setAskYield(extractDoubleFromToken(tabs.nextToken()));
            bean.setBidSpread(extractDoubleFromToken(tabs.nextToken()));
            bean.setAskSpread(extractDoubleFromToken(tabs.nextToken()));
            bean.setBidOverAsk(extractDoubleFromToken(tabs.nextToken()));
            bean.setBidOverAskP(extractDoubleFromToken(tabs.nextToken()));
            bean.setBidAskType(extractStringFromToken(tabs.nextToken()));
            bean.setPricingModel(extractStringFromToken(tabs.nextToken()));
            bean.setBaseIssue(extractStringFromToken(tabs.nextToken()));
            bean.setAlias(extractStringFromToken(tabs.nextToken()));
            bean.setErrorMessage(extractStringFromToken(tabs.nextToken()));
            bean.setErrorSeverity(extractIntFromToken(tabs.nextToken()));
            bean.setPricingSource(extractStringFromToken(tabs.nextToken()));
            bean.setSecurityType(extractStringFromToken(tabs.nextToken()));
            bean.setIsin(extractStringFromToken(tabs.nextToken()));
            data.add((T) bean);
        }
    }

    private int extractIntFromToken(String token) {
        return token.trim().equals("") || token.trim().equals("\t") ? 0 : Integer.parseInt(token);
    }

    private double extractDoubleFromToken(String token) {
        return token.trim().equals("") || token.trim().equals("\t") ? 0 : Double.parseDouble(token);
    }

    private String extractStringFromToken(String token) {
        return token == null || (token.trim().equals("") || token.trim().equals("\t")) ? null : token;
    }
}
