package tei.cr.filters;

import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import tei.cr.pipeline.AbstractBase;
import java.io.IOException;

/**
 * Do nothing but report the column and line numbers of an error if
 * a SAXException (most of the time a well-formedness error) occurs.
 *
 * @author Sylvain Loiseau
 * &lt;sylvain.loiseau@u-paris10.fr&lt;
 * @version 0.1
 */
public final class CheckWellFormedness extends AbstractBase implements ErrorHandler {

    private InputSource input = null;

    private String getLocation() {
        String location = "";
        if (locator != null) {
            location = "Line: " + locator.getLineNumber() + "; " + "column: " + locator.getColumnNumber();
        }
        return location;
    }

    public void parse(InputSource input) throws SAXException, IOException {
        this.input = input;
        try {
            super.parse(input);
        } catch (SAXException sE) {
            throw new FilterException(sE.getMessage() + " " + getLocation(), sE);
        } catch (IOException IOE) {
            throw new FilterException(IOE.getMessage() + " " + getLocation(), IOE);
        } catch (NullPointerException nPE) {
            throw new FilterException(nPE.getMessage() + " " + getLocation(), nPE);
        }
    }

    public void parse(String uri) throws SAXException, IOException {
        parse(new InputSource(uri));
    }

    public void error(SAXParseException exception) throws SAXException {
        throw new FilterException(getLocation());
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw new FilterException(getLocation());
    }

    public void warning(SAXParseException exception) throws SAXException {
        throw new FilterException(getLocation());
    }
}
