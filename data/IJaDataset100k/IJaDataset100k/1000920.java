package org.exist.xquery.functions.response;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.exist.EXistException;
import org.exist.dom.QName;
import org.exist.http.servlets.ResponseWrapper;
import org.exist.storage.BrokerPool;
import org.exist.storage.DBBroker;
import org.exist.storage.serializers.Serializer;
import org.exist.util.serializer.SAXSerializer;
import org.exist.util.serializer.SerializerPool;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Option;
import org.exist.xquery.Variable;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.JavaObjectValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.xml.sax.SAXException;

public class Stream extends BasicFunction {

    private static final Logger logger = Logger.getLogger(Stream.class);

    public static final FunctionSignature signature = new FunctionSignature(new QName("stream", ResponseModule.NAMESPACE_URI, ResponseModule.PREFIX), "Stream can only be used within a servlet context. Itt directly streams its input to the servlet's output stream. " + "It should thus be the last statement in the XQuery.", new SequenceType[] { new FunctionParameterSequenceType("content", Type.ITEM, Cardinality.ZERO_OR_MORE, "The source sequence"), new FunctionParameterSequenceType("serialization-options", Type.STRING, Cardinality.EXACTLY_ONE, "The serialization options") }, new SequenceType(Type.ITEM, Cardinality.EMPTY));

    /**
	 * @param context
	 * @param signature
	 */
    public Stream(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (args[0].isEmpty()) {
            return Sequence.EMPTY_SEQUENCE;
        }
        Sequence inputNode = args[0];
        Properties serializeOptions = new Properties();
        String serOpts = args[1].getStringValue();
        String[] contents = Option.tokenize(serOpts);
        for (int i = 0; i < contents.length; i++) {
            String[] pair = Option.parseKeyValuePair(contents[i]);
            if (pair == null) throw new XPathException(this, "Found invalid serialization option: " + pair);
            logger.info("Setting serialization property: " + pair[0] + " = " + pair[1]);
            serializeOptions.setProperty(pair[0], pair[1]);
        }
        ResponseModule myModule = (ResponseModule) context.getModule(ResponseModule.NAMESPACE_URI);
        Variable respVar = myModule.resolveVariable(ResponseModule.RESPONSE_VAR);
        if (respVar == null) throw new XPathException(this, "No response object found in the current XQuery context.");
        if (respVar.getValue().getItemType() != Type.JAVA_OBJECT) throw new XPathException(this, "Variable $response is not bound to an Java object.");
        JavaObjectValue respValue = (JavaObjectValue) respVar.getValue().itemAt(0);
        if (!"org.exist.http.servlets.HttpResponseWrapper".equals(respValue.getObject().getClass().getName())) throw new XPathException(this, signature.toString() + " can only be used within the EXistServlet or XQueryServlet");
        ResponseWrapper response = (ResponseWrapper) respValue.getObject();
        String mediaType = serializeOptions.getProperty("media-type", "application/xml");
        String encoding = serializeOptions.getProperty("encoding", "UTF-8");
        if (mediaType != null) {
            response.setContentType(mediaType + "; charset=" + encoding);
        }
        Serializer serializer = null;
        BrokerPool db = null;
        DBBroker broker = null;
        try {
            db = BrokerPool.getInstance();
            broker = db.get(null);
            serializer = broker.getSerializer();
            serializer.reset();
            OutputStream sout = response.getOutputStream();
            PrintWriter output = new PrintWriter(new OutputStreamWriter(sout, encoding));
            SerializerPool serializerPool = SerializerPool.getInstance();
            SAXSerializer sax = (SAXSerializer) serializerPool.borrowObject(SAXSerializer.class);
            try {
                sax.setOutput(output, serializeOptions);
                serializer.setProperties(serializeOptions);
                serializer.setSAXHandlers(sax, sax);
                serializer.toSAX(inputNode, 1, inputNode.getItemCount(), false, false);
            } catch (SAXException e) {
                e.printStackTrace();
                throw new IOException(e);
            } finally {
                serializerPool.returnObject(sax);
            }
            output.flush();
            output.close();
            response.flushBuffer();
        } catch (IOException e) {
            throw new XPathException(this, "IO exception while streaming node: " + e.getMessage(), e);
        } catch (EXistException e) {
            throw new XPathException(this, "Exception while streaming node: " + e.getMessage(), e);
        } finally {
            if (db != null) db.release(broker);
        }
        return Sequence.EMPTY_SEQUENCE;
    }
}
