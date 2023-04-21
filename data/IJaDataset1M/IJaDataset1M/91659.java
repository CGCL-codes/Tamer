package org.infoeng.icws.documents;

import java.io.InputStream;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.xpath.XPathAPI;

public class SeriesListing extends ICWSDocument {

    public SeriesListing() throws Exception {
        super.init("SeriesListing");
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
    }

    public SeriesListing(String srsListing) throws Exception {
        super.load(new java.io.ByteArrayInputStream(srsListing.getBytes()));
    }

    public SeriesListing(InputStream is) throws Exception {
        super.load(is);
    }

    public String getSearchUnderlierLocator() {
        String tmpStr = super.getElementString("/SeriesListing/underlierLocator");
        if (tmpStr == null || "".equals(tmpStr.trim())) return null;
        return tmpStr;
    }

    public void setSearchUnderlierLocator(String srchTxt) throws Exception {
        super.simpleSetElementString("/SeriesListing/underlierLocator", srchTxt);
    }

    public String getSearchDigestValue() {
        String tmpStr = super.getElementString("/SeriesListing/digestValue");
        if (tmpStr == null || "".equals(tmpStr.trim())) return null;
        return tmpStr;
    }

    public void setSearchDigestValue(String dv) throws Exception {
        super.simpleSetElementString("/SeriesListing/digestValue", dv);
    }

    public void addSeriesListingResult(String seriesIdStr) throws Exception {
        Element sidElem = doc.createElement("seriesID");
        sidElem.setTextContent(seriesIdStr);
        doc.getDocumentElement().appendChild(sidElem);
        doc.getDocumentElement().appendChild(doc.createTextNode("\n"));
    }

    public String[] getSeriesListingResult() throws Exception {
        ArrayList arrayList = new ArrayList();
        NodeList sidNL = XPathAPI.selectNodeList(doc, "/SeriesListing/seriesID", nsCtxt);
        if (sidNL == null || sidNL.getLength() == 0) {
            return null;
        }
        int length = sidNL.getLength();
        for (int x = 0; x < length; x++) {
            Element sidElem = (Element) sidNL.item(x);
            arrayList.add(sidElem.getTextContent());
        }
        String[] strList = new String[arrayList.size()];
        arrayList.toArray(strList);
        return strList;
    }
}
