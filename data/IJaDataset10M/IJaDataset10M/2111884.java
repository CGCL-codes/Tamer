package org.ximtec.igesture.batch.core.jdom;

import org.jdom.Element;
import org.ximtec.igesture.batch.core.BatchForValue;

/**
 * Comment
 * @version 1.0 15.10.2008
 * @author Ueli Kurmann
 */
public class JdomBatchForValue {

    public static String ROOT_TAG = "for";

    public static String ATTRIBUTE_START = "start";

    public static String ATTRIBUTE_END = "end";

    public static String ATTRIBUTE_STEP = "step";

    public static BatchForValue unmarshal(Element parameter) {
        final BatchForValue value = new BatchForValue();
        value.setStart(Double.parseDouble(parameter.getAttributeValue(ATTRIBUTE_START)));
        value.setEnd(Double.parseDouble(parameter.getAttributeValue(ATTRIBUTE_END)));
        value.setStep(Double.parseDouble(parameter.getAttributeValue(ATTRIBUTE_STEP)));
        return value;
    }
}
