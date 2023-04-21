package com.jduchek.gfinance;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import android.util.Xml;

public class AllPortfoliosData extends FinanceData {

    int mNumPortfolios;

    PortfolioData[] mPortfolios;

    @Override
    public void storeData(gFinance app) {
        app.setPortfolioData(this);
    }

    public PortfolioData[] getPortfolios() {
        return mPortfolios;
    }

    protected class PDHandler extends DefaultHandler {

        boolean totalResults;

        int currentPortfolio = 0;

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.contentEquals("gd:feedLink")) {
                mPortfolios[currentPortfolio] = new PortfolioData(attributes.getValue("href"));
                currentPortfolio++;
            } else if (qName.contentEquals("openSearch:totalResults")) {
                totalResults = true;
            }
        }

        public void endElement(String uri, String localName, String qName) {
            if (qName.contentEquals("openSearch:totalResults")) {
                totalResults = false;
            }
        }

        public void characters(char[] ch, int start, int length) {
            String str = new String(ch, start, length);
            if (totalResults) {
                mNumPortfolios = Integer.parseInt(str);
                mPortfolios = new PortfolioData[mNumPortfolios];
            }
        }
    }

    @Override
    public void getData(HttpTransport transport) {
        HttpRequest request = transport.buildGetRequest();
        request.url = FinanceUrl.forAllPortfoliosFeed();
        String str;
        try {
            str = RedirectHandler.execute(request).parseAsString();
            Xml.parse(str, new PDHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < mNumPortfolios; i++) {
            mPortfolios[i].getData(transport);
        }
    }
}
