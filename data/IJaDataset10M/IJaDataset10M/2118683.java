package test.org.tolven.trim.scan;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import org.tolven.app.TrimLocal;
import org.tolven.app.el.TrimExpressionEvaluator;
import org.tolven.app.entity.TrimHeader;
import org.tolven.trim.Act;
import org.tolven.trim.ActParticipation;
import org.tolven.trim.ActRelationship;
import org.tolven.trim.Application;
import org.tolven.trim.BindTo;
import org.tolven.trim.CopyTo;
import org.tolven.trim.Field;
import org.tolven.trim.InfrastructureRoot;
import org.tolven.trim.Trim;
import org.tolven.trim.ValueSet;
import org.tolven.trim.ex.ActEx;
import org.tolven.trim.ex.TrimEx;
import org.tolven.trim.ex.TrimFactory;
import org.tolven.trim.scan.IncludeScanner;
import org.tolven.trim.scan.Scanner;

/**
 * Special include scanner subclass for testing
 * @author John Churin
 */
public class SpecialIncludeScanner extends IncludeScanner {

    JAXBContext jc;

    public Trim unmarshalStream(InputStream input) throws JAXBException, IOException {
        Unmarshaller u = jc.createUnmarshaller();
        u.setProperty("com.sun.xml.bind.ObjectFactory", trimFactory);
        Object o = u.unmarshal(input);
        return (Trim) o;
    }

    public Trim openTrim(String name) {
        Trim trim;
        try {
            InputStream extensionStream = getClass().getResourceAsStream(name);
            trim = unmarshalStream(extensionStream);
        } catch (Exception e) {
            throw new RuntimeException("Error opening include trim", e);
        }
        return trim;
    }

    public JAXBContext getJc() {
        return jc;
    }

    public void setJc(JAXBContext jc) {
        this.jc = jc;
    }
}
