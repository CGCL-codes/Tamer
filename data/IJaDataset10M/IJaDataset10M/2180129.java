package jorgan.io.disposition;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class Convert {

    private Pattern pattern;

    private String xsl;

    public Convert(String pattern, String xsl) {
        this.pattern = Pattern.compile(pattern);
        this.xsl = xsl;
    }

    public String getPattern() {
        return this.pattern.toString();
    }

    public boolean isApplicable(String version) {
        return pattern.matcher(version).matches();
    }

    @Override
    public String toString() {
        return xsl;
    }

    public void exists() {
        if (getSource() == null) {
            throw new IllegalStateException(xsl);
        }
    }

    public InputStream convert(InputStream in) throws IOException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute("indent-number", new Integer(4));
        Transformer transform;
        try {
            InputStream stream = getSource();
            transform = factory.newTransformer(new StreamSource(stream));
            transform.setOutputProperty(OutputKeys.INDENT, "yes");
            File temp = File.createTempFile(xsl + ".", ".xml");
            transform.transform(new StreamSource(in), new StreamResult(temp));
            in.close();
            return new FileInputStream(temp);
        } catch (TransformerException e) {
            IOException ex = new IOException();
            ex.initCause(e);
            throw ex;
        }
    }

    private InputStream getSource() {
        return Convert.class.getResourceAsStream("conversion/" + xsl);
    }
}
