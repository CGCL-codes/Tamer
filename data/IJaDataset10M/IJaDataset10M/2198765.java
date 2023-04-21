package de.cologneintelligence.fitgoodies.file;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import fit.Parse;
import fit.TypeAdapter;

/**
 * This Fixture can be used to test the content of a XML file using XPath-Expressions.
 * <br /><br />
 *
 * Example:<br />
 * <table>
 * 		<tr><td>fitgoodies.file.XMLFileFixture</td><td>file=/myfile.xml</td></tr>
 * 		<tr><td>/books/book[0]/author</td><td>Terry Pratchett</td></tr>
 * 		<tr><td>/books/book[1]/id</td><td>326172</td></tr>
 * </table>
 *
 * @author jwierum
 * @version $Id: XMLFileFixture.java 46 2011-09-04 14:59:16Z jochen_wierum $
 */
public class XMLFileFixture extends AbstractFileReaderFixture {

    /** for internal use only - used to solve cross references. */
    public String selectedValue;

    private Document doc;

    private XPathFactory xPathFactory;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.parse(getFile().openInputStream());
        xPathFactory = XPathFactory.newInstance();
    }

    @Override
    public void doRow(final Parse row) {
        if (row.parts.more != null) {
            String xpath = row.parts.text();
            XPath path = xPathFactory.newXPath();
            try {
                selectedValue = path.evaluate(xpath, doc);
                check(row.parts.more, TypeAdapter.on(this, this.getClass().getField("selectedValue")));
            } catch (XPathExpressionException e) {
                exception(row.parts, e);
            } catch (SecurityException e) {
                exception(row.parts, e);
            } catch (NoSuchFieldException e) {
                exception(row.parts, e);
            }
        }
    }
}
