package epub.content.citations.trackback;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RdfReader {

    protected Model stringToModel(String xml) {
        if (xml == null || xml.equals("")) {
            return null;
        }
        InputStream in = new ByteArrayInputStream(xml.getBytes());
        Model model = ModelFactory.createDefaultModel();
        model.read(in, "");
        return model;
    }
}
