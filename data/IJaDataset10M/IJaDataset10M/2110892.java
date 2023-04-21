package com.jawise.serviceadapter.convert.nvp;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.jawise.serviceadapter.convert.MessageContext;
import com.jawise.serviceadapter.convert.MessageConverter;
import com.jawise.serviceadapter.convert.NVPHelper;
import com.jawise.serviceadapter.convert.NvpTargetValueAccepter;
import com.jawise.serviceadapter.convert.TargetValue;
import com.jawise.serviceadapter.core.MessageException;

/**
 * 
 * @author sathyan
 * 
 */
public class NvpToNvpConverter extends MessageConverter {

    private static Logger logger = Logger.getLogger(NvpToNvpConverter.class);

    @SuppressWarnings("unchecked")
    private Map sourceMap;

    @SuppressWarnings("unchecked")
    private Map targetMap;

    @SuppressWarnings("unchecked")
    public NvpToNvpConverter(MessageContext ctx) throws MessageException {
        super(ctx);
        sourceMap = (Map) ctx.get("messagse");
        targetMap = new HashMap<String, String>();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object doConvertion() throws MessageException {
        try {
            logger.debug("started conversion");
            convertParts();
            String targgetMsg = buildNvpMessage(getTargetReferances());
            logger.debug("finished conversion");
            return targgetMsg;
        } catch (MessageException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new MessageException("1008");
        }
    }

    @SuppressWarnings("unchecked")
    protected Map getTargetReferances() {
        return targetMap;
    }

    @SuppressWarnings("unchecked")
    protected Map getSourceReferences() {
        return sourceMap;
    }

    @SuppressWarnings("unchecked")
    protected void setTargetValue(String targetName, Object targetValue, boolean recursive) throws MessageException {
        NvpTargetValueAccepter accepter = new NvpTargetValueAccepter(this, null, targetMap, null);
        accepter.acceptTargetValue(new TargetValue(targetName, targetValue, recursive));
    }

    protected String getSourceValue(String sourceName) throws MessageException {
        try {
            String sourceValue = "";
            String[] sourceValues = (String[]) getSourceReferences().get(sourceName);
            if (sourceValues != null) {
                sourceValue = sourceValues[0];
            }
            return applyEncoding(sourceValue);
        } catch (UnsupportedEncodingException e) {
            throw new MessageException("1008");
        }
    }
}
