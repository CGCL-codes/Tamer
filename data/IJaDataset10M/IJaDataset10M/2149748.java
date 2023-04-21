package org.exist.http.sleepy.annotations;

import org.exist.xquery.Annotation;
import static org.exist.http.sleepy.annotations.RESTAnnotation.AnnotationName.formparam;
import org.exist.http.sleepy.impl.KeyVar;
import org.exist.xquery.LiteralValue;
import org.exist.xquery.XPathException;
import org.exist.xquery.value.AtomicValue;
import org.exist.xquery.value.Type;

/**
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class FormParamAnnotation extends AbstractParamAnnotation {

    protected FormParamAnnotation(Annotation annotation) throws RESTAnnotationException {
        super(annotation, formparam);
    }

    @Override
    protected KeyVar parseAnnotationValue() throws RESTAnnotationException {
        final LiteralValue[] annotationValue = getAnnotation().getValue();
        if (annotationValue.length != 2) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0014);
        } else {
            return parseKeyVar(annotationValue[0].getValue(), annotationValue[1].getValue());
        }
    }

    private KeyVar parseKeyVar(AtomicValue keyValue, AtomicValue varValue) throws RESTAnnotationException {
        if (keyValue.getType() != Type.STRING) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0015);
        }
        if (varValue.getType() != Type.STRING) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0016);
        }
        try {
            final String keyStr = keyValue.getStringValue();
            final String varStr = varValue.getStringValue();
            if (keyStr.isEmpty()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0015);
            }
            if (varStr.isEmpty()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0016);
            }
            mtcFnParameter.reset(varStr);
            if (!mtcFnParameter.matches()) {
                throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0017);
            }
            final String varName = mtcFnParameter.group(1);
            return new KeyVar(keyStr, varName);
        } catch (XPathException xpe) {
            throw new RESTAnnotationException(RESTAnnotationErrorCodes.RQST0014, xpe);
        }
    }
}
