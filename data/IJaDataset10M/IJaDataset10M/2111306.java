package net.sf.saxon.functions;

import net.sf.saxon.Controller;
import net.sf.saxon.AugmentedSource;
import net.sf.saxon.event.Builder;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.event.Sender;
import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.ExpressionVisitor;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.AtomicValue;
import net.sf.saxon.value.Whitespace;
import org.xml.sax.InputSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.Source;
import java.io.StringReader;

/**
* This class implements the saxon:parse() extension function,
* which is specially-recognized by the system because it needs access
* to parts of the static context
*/
public class Parse extends SystemFunction {

    String baseURI;

    /**
    * Method supplied by each class of function to check arguments during parsing, when all
    * the argument expressions have been read
    */
    public void checkArguments(ExpressionVisitor visitor) throws XPathException {
        if (baseURI == null) {
            super.checkArguments(visitor);
            baseURI = visitor.getStaticContext().getBaseURI();
        }
    }

    /**
     * Pre-evaluate a function at compile time. Static evaluation is suppressed for
     * saxon:parse because no controller is available.
     * @param visitor an expression visitor
     */
    public Expression preEvaluate(ExpressionVisitor visitor) throws XPathException {
        return this;
    }

    /**
    * Evaluate in a general context
    */
    public Item evaluateItem(XPathContext c) throws XPathException {
        Controller controller = c.getController();
        AtomicValue content = (AtomicValue) argument[0].evaluateItem(c);
        StringReader sr = new StringReader(content.getStringValue());
        InputSource is = new InputSource(sr);
        is.setSystemId(baseURI);
        Source source = new SAXSource(is);
        source.setSystemId(baseURI);
        Builder b = controller.makeBuilder();
        Receiver s = b;
        source = AugmentedSource.makeAugmentedSource(source);
        ((AugmentedSource) source).setStripSpace(Whitespace.XSLT);
        if (controller.getExecutable().stripsInputTypeAnnotations()) {
            s = controller.getConfiguration().getAnnotationStripper(s);
        }
        try {
            new Sender(controller.makePipelineConfiguration()).send(source, s);
            NodeInfo node = b.getCurrentRoot();
            b.reset();
            return node;
        } catch (XPathException err) {
            throw new XPathException(err);
        }
    }
}
