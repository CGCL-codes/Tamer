package com.cyberkinetx.ecr.country;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import com.cyberkinetx.ecr.CurrencyUnit;
import com.cyberkinetx.ecr.util.DataUrlResolver;
import com.cyberkinetx.ecr.util.DomainName;

/**
 * The country object for exchange currency data for Europe/Slovakia
 * @author Andrei Erimicioi <erani.mail@gmail.com>
 */
public class Slovakia extends AbstractCountry {

    @SuppressWarnings("unchecked")
    @Override
    public void parse() throws IOException {
        org.dom4j.io.SAXReader reader = new org.dom4j.io.SAXReader();
        org.dom4j.Document doc = null;
        try {
            doc = reader.read(new URL((new DataUrlResolver()).getDataUrl(DomainName.SLOVAKIA)));
        } catch (DocumentException e1) {
            throw new IOException(e1);
        }
        org.dom4j.Element root = doc.getRootElement().element("Cube").element("Cube");
        for (Iterator<Element> i = root.elementIterator(); i.hasNext(); ) {
            org.dom4j.Element e = i.next();
            CurrencyUnit unit = new CurrencyUnit(e.attributeValue("currency"), Float.valueOf(e.attributeValue("rate")), DEFAULT_MULTIPLIER);
            this.set.add(unit);
        }
    }
}
