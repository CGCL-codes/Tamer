package org.okkam.core.match.test;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.okkam.core.api.EntitySearchNG;
import org.okkam.core.match.query.annotated.AnnotatedQuery;
import org.okkam.core.match.query.annotated.Annotation;
import org.okkam.core.match.query.annotated.QueryToken;

public class SearchXmlTest {

    private static final Log log = LogFactory.getLog(SearchXmlTest.class);

    public static void main(String[] args) {
        SearchXmlTest test = new SearchXmlTest();
        test.doMain(args);
    }

    public void doMain(String[] args) {
        EntitySearchNG search = new EntitySearchNG();
        AnnotatedQuery query = new AnnotatedQuery();
        Annotation annotation = new Annotation();
        QueryToken token1 = new QueryToken();
        token1.setLabel("");
        token1.setValue("rossi");
        query.setQueryString("rossi");
        annotation.getToken().add(token1);
        query.setQueryAnnotation(annotation);
        StringWriter resultXml = new StringWriter();
        try {
            JAXBContext jc = JAXBContext.newInstance("org.okkam.core.match.query.annotated", getClass().getClassLoader());
            Marshaller m = jc.createMarshaller();
            m.marshal(query, resultXml);
        } catch (JAXBException e) {
            log.error("marshalling failed: " + e.getStackTrace().toString());
        }
        log.debug("delegation finished. returning.");
        String queryString = resultXml.toString();
        String res = search.searchAndGetXML(queryString);
        System.out.println(res);
    }
}
