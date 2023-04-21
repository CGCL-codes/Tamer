package com.jawise.serviceadapter.convert.xml;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.jawise.serviceadapter.convert.AbstractXmlConverter;
import com.jawise.serviceadapter.convert.MessageContext;
import com.jawise.serviceadapter.convert.TargetValue;
import com.jawise.serviceadapter.convert.XmlTargetValueAccepter;
import com.jawise.serviceadapter.core.MessageException;

public class XmlHttpToXmlHttpConverter extends AbstractXmlConverter {

    private static Logger logger = Logger.getLogger(XmlHttpToXmlHttpConverter.class);

    @SuppressWarnings("unchecked")
    private Map recursiveNodesMap;

    private XmlPath lastAddedTargetPath;

    public XmlHttpToXmlHttpConverter(MessageContext ctx) throws MessageException {
        super(ctx);
        try {
            createSourceDoc();
            recursiveNodesMap = new HashMap<XmlPath, String>();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MessageException("1013", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object doConvertion() throws MessageException {
        try {
            logger.debug("started conversion");
            populateSourceMap();
            convertParts();
            XmlComposer composer = new XmlComposer();
            String compose = composer.compose((HashMap) targetMap, (HashMap) recursiveNodesMap);
            logger.debug("finished conversion");
            return compose;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new MessageException("1008");
        } catch (MessageException e) {
            logger.error(e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new MessageException("1013", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map getSourceReferences() {
        return sourceReferences;
    }

    @Override
    protected String getSourceValue(String sourceName) {
        String sourceValue = (String) sourceMap.get(sourceName);
        return sourceValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Map getTargetReferances() {
        return targetReferences;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void setTargetValue(String targetName, Object targetValue, boolean recursive) throws MessageException {
        XmlTargetValueAccepter accepter = new XmlTargetValueAccepter(this, targetReferences, targetMap, null, recursiveNodesMap, lastAddedTargetPath);
        accepter.acceptTargetValue(new TargetValue(targetName, targetValue, recursive));
        lastAddedTargetPath = accepter.getLastAddedTargetPath();
    }

    @Override
    protected String getScriptTargetName(String targetName) {
        return new XmlPath(targetName).getName();
    }
}
