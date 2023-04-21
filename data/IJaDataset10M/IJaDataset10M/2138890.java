package org.toobsframework.transformpipeline.domain;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.toobsframework.transformpipeline.transformer.IXMLTransformerHelper;
import org.toobsframework.transformpipeline.transformer.ToobsTransformerException;
import org.xml.sax.SAXException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;
import java.util.Map;

/**
 */
public class StaticXSLTransformer extends BaseXMLTransformer {

    /**
   * Implementation of the transform() method. This method first checks some
   * input parameters. Then it creates a Source object and invoces the
   * {@link #makeTransformation makeTransformation()}method.
   *
   */
    public List<String> transform(List<String> inputXSLs, List<String> inputXMLs, Map<String, Object> inputParams, IXMLTransformerHelper transformerHelper) throws ToobsTransformerException {
        debugParams(inputParams);
        Iterator<String> xmlIterator = inputXMLs.iterator();
        List<String> resultingXMLs = new ArrayList<String>();
        ByteArrayOutputStream xmlOutputStream = null;
        while (xmlIterator.hasNext()) {
            try {
                Object xmlObject = xmlIterator.next();
                xmlOutputStream = new ByteArrayOutputStream();
                Result result = new StreamResult(xmlOutputStream);
                this.transform(result, inputXSLs, xmlObject, inputParams, transformerHelper);
                resultingXMLs.add(xmlOutputStream.toString("UTF-8"));
            } catch (UnsupportedEncodingException uee) {
                log.error("Error creating output string", uee);
                throw new ToobsTransformerException(uee);
            } finally {
                try {
                    if (xmlOutputStream != null) {
                        xmlOutputStream.close();
                        xmlOutputStream = null;
                    }
                } catch (IOException ex) {
                }
            }
        }
        return resultingXMLs;
    }

    /**
   * Implementation of the transform() method. This method first checks some
   * input parameters. Then it creates a Source object and invoces the
   * {@link #makeTransformation makeTransformation()}method.
   *
   */
    public void transform(Result xmlResult, List<String> inputXSLs, Object xmlObject, Map<String, Object> inputParams, IXMLTransformerHelper transformerHelper) throws ToobsTransformerException {
        debugParams(inputParams);
        Iterator<String> xslIterator = inputXSLs.iterator();
        ByteArrayInputStream xmlInputStream = null;
        Result wrappedResult = null;
        boolean useTempStream = false;
        if (xmlResult instanceof StreamResult) {
            useTempStream = inputXSLs.size() > 1 && !(((StreamResult) xmlResult).getOutputStream() instanceof ByteArrayOutputStream);
            if (useTempStream) {
                wrappedResult = new StreamResult(new ByteArrayOutputStream());
            } else {
                wrappedResult = xmlResult;
            }
        } else {
            wrappedResult = xmlResult;
        }
        try {
            while (xslIterator.hasNext()) {
                String xslFile = (String) xslIterator.next();
                if (xmlInputStream == null) {
                    if (xmlObject instanceof org.w3c.dom.Node) {
                        TransformerFactory tf = TransformerFactory.newInstance();
                        Transformer t = tf.newTransformer();
                        t.setOutputProperty(OutputKeys.INDENT, "yes");
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        t.transform(new DOMSource((org.w3c.dom.Node) xmlObject), new StreamResult(os));
                        xmlInputStream = new ByteArrayInputStream(os.toByteArray());
                        if (log.isTraceEnabled()) {
                            log.trace("Input XML for " + xslFile + " : " + os.toString("UTF-8"));
                        }
                    } else {
                        xmlInputStream = new ByteArrayInputStream(((String) xmlObject).getBytes("UTF-8"));
                        if (log.isTraceEnabled()) {
                            log.trace("Input XML for " + xslFile + " : " + xmlObject);
                        }
                    }
                }
                StreamSource xmlSource = new StreamSource(xmlInputStream);
                this.doTransform(xslFile, xmlSource, inputParams, transformerHelper, wrappedResult, xslFile);
                if (useTempStream) {
                    xmlInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) ((StreamResult) wrappedResult).getOutputStream()).toByteArray());
                    log.debug("First Pass: \n" + new String(((ByteArrayOutputStream) ((StreamResult) wrappedResult).getOutputStream()).toByteArray()));
                }
            }
            if (useTempStream) {
                ((ByteArrayOutputStream) ((StreamResult) wrappedResult).getOutputStream()).writeTo((((StreamResult) xmlResult).getOutputStream()));
            }
        } catch (UnsupportedEncodingException uee) {
            log.error("Error creating output string", uee);
            throw new ToobsTransformerException(uee);
        } catch (TransformerException te) {
            log.error("Error creating input xml: " + te.getMessage(), te);
            throw new ToobsTransformerException(te);
        } catch (IOException ioe) {
            log.error("Error creating output stream", ioe);
            throw new ToobsTransformerException(ioe);
        } finally {
            try {
                if (xmlInputStream != null) {
                    xmlInputStream.close();
                    xmlInputStream = null;
                }
            } catch (IOException ex) {
            }
        }
    }

    /**
   * This method actually does all the XML Document transformation.
   * <p>
   * @param xslSource
   *          holds the xslFile
   * @param xmlSource
   *          holds the xmlFile
   * @param params
   *          holds the params needed to do this transform
   * @param xmlResult
   *          holds the streamResult of the transform.
   */
    @SuppressWarnings("unchecked")
    protected void doTransform(String xslSource, Source xmlSource, Map params, IXMLTransformerHelper transformerHelper, Result xmlResult, String xslFile) throws ToobsTransformerException {
        try {
            Transformer transformer = getTemplates(xslFile, null).newTransformer();
            transformer.setErrorListener(saxTFactory.getErrorListener());
            transformer.setOutputProperty("encoding", "UTF-8");
            if (params != null) {
                Iterator paramIt = params.entrySet().iterator();
                while (paramIt.hasNext()) {
                    Map.Entry thisParam = (Map.Entry) paramIt.next();
                    transformer.setParameter((String) thisParam.getKey(), thisParam.getValue());
                }
            }
            if (transformerHelper != null) {
                transformer.setParameter(TRANSFORMER_HELPER, transformerHelper);
            }
            Date timer = new Date();
            transformer.transform(xmlSource, xmlResult);
            Date timer2 = new Date();
            if (log.isDebugEnabled()) {
                long diff = timer2.getTime() - timer.getTime();
                log.debug("Time to transform: " + diff + " mS XSL: " + xslFile);
            }
        } catch (TransformerConfigurationException tce) {
            throw new ToobsTransformerException(tce);
        } catch (TransformerException te) {
            throw new ToobsTransformerException(te);
        } catch (IOException ioe) {
            throw new ToobsTransformerException(ioe);
        } catch (SAXException se) {
            throw new ToobsTransformerException(se);
        }
    }

    public void setOutputProperties(Properties outputProperties) {
    }
}
