package edu.unibi.agbi.webservice.client.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import edu.unibi.agbi.webservice.client.generated.HibernateWebServiceStub.HQuery;
import edu.unibi.agbi.webservice.client.generated.HibernateWebServiceStub.QueryParameter;

/**
 * @author Benjamin Kormeier
 * @version 1.0 20.10.2011
 */
public class QueryReader {

    private InputStream input_stream = null;

    private XMLStreamReader reader = null;

    private Hashtable<String, HQuery> queries = new Hashtable<String, HQuery>();

    private static final String ENTRY = new String("entry");

    private static final String QUERY = new String("query");

    private static final String PROPERTY = new String("property");

    public QueryReader(String file) {
        this.input_stream = this.getClass().getResourceAsStream(file);
        try {
            loadSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSettings() throws XMLStreamException, IOException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        reader = factory.createXMLStreamReader(input_stream);
        String tag = null;
        HQuery hquery = new HQuery();
        String key = new String();
        ArrayList<QueryParameter> param_list = new ArrayList<QueryParameter>();
        while (reader.hasNext()) {
            switch(reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    tag = reader.getLocalName();
                    if (tag.equals(ENTRY)) {
                        hquery = new HQuery();
                        param_list = new ArrayList<QueryParameter>();
                        key = reader.getAttributeValue(0);
                    } else if (tag.equals(QUERY)) {
                        hquery.setHqlQuery(reader.getElementText());
                    } else if (tag.equals(PROPERTY)) {
                        QueryParameter qp = new QueryParameter();
                        qp.setParameter(reader.getAttributeValue(0));
                        qp.setValue(reader.getElementText());
                        param_list.add(qp);
                    }
                case XMLStreamConstants.END_ELEMENT:
                    tag = reader.getLocalName();
                    if (tag.equals(ENTRY)) {
                        hquery.setParameter(param_list.toArray(new QueryParameter[param_list.size()]));
                        queries.put(key, hquery);
                    }
            }
        }
    }

    public Hashtable<String, HQuery> getQueries() {
        return queries;
    }
}
