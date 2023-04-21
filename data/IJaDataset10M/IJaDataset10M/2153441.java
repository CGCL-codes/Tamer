package au.edu.diasb.annotation.danno.validation;

import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.ANNOTATES_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.ANNOTATION_CLASS;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.HTTP_BODY_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.HTTP_CONTENT_LENGTH_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.HTTP_CONTENT_TYPE_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.HTTP_MESSAGE_CLASS;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.IN_REPLY_TO_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.REPLY_CLASS;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.ROOT_PROPERTY;
import java.util.Collection;
import au.edu.diasb.annotation.danno.model.AnnoteaTypeException;
import au.edu.diasb.annotation.danno.model.ObjectType;
import au.edu.diasb.annotation.danno.model.RDFLiteral;
import au.edu.diasb.annotation.danno.model.RDFObject;
import au.edu.diasb.annotation.danno.model.RDFValue;

/**
 * This validator will validate an RDF object against the implied Annotea schema
 * 
 * @author scrawley
 */
public class BasicAnnoteaValidator implements RDFValidator {

    @Override
    public void validate(RDFObject object) throws AnnoteaTypeException {
        switch(determineObjectType(object)) {
            case ANNOTATION:
                validateAnnotation(object);
                break;
            case REPLY:
                validateReply(object);
                break;
            case HTTP_MESSAGE:
                validateHTTPMessage(object);
                break;
        }
    }

    private void validateHTTPMessage(RDFObject object) throws AnnoteaTypeException {
        RDFValue body = object.getProperty(HTTP_BODY_PROPERTY);
        if (body == null) {
            throw new AnnoteaTypeException("'Body' property is missing");
        }
        if (!body.isLiteral()) {
            throw new AnnoteaTypeException("'Body' property is not an RDF literal");
        }
        RDFValue contentType = object.getProperty(HTTP_CONTENT_TYPE_PROPERTY);
        if (contentType != null && !contentType.isLiteral()) {
            throw new AnnoteaTypeException("'ContentType' property is not an RDF literal");
        }
        RDFValue contentLength = object.getProperty(HTTP_CONTENT_LENGTH_PROPERTY);
        if (contentLength != null) {
            if (!contentLength.isLiteral()) {
                throw new AnnoteaTypeException("'ContentLength' property is not an RDF literal");
            }
            try {
                if (Integer.parseInt(((RDFLiteral) contentLength).asString()) < 0) {
                    throw new AnnoteaTypeException("'ContentLength' property is negative");
                }
            } catch (NumberFormatException ex) {
                throw new AnnoteaTypeException("'ContentLength' property is not an integer");
            }
        }
    }

    private void validateReply(RDFObject object) throws AnnoteaTypeException {
        RDFValue root = object.getProperty(ROOT_PROPERTY);
        if (root == null) {
            throw new AnnoteaTypeException("'root' property is missing");
        }
        if (!root.isURI()) {
            throw new AnnoteaTypeException("'root' property is not a URI");
        }
        RDFValue inReplyTo = object.getProperty(IN_REPLY_TO_PROPERTY);
        if (inReplyTo == null) {
            throw new AnnoteaTypeException("'inReplyTo' property is missing");
        }
        if (!inReplyTo.isURI()) {
            throw new AnnoteaTypeException("'inReplyTo' property is not a URI");
        }
    }

    private void validateAnnotation(RDFObject object) throws AnnoteaTypeException {
        Collection<RDFValue> annotates = object.getProperties(ANNOTATES_PROPERTY);
        if (annotates.isEmpty()) {
            throw new AnnoteaTypeException("'annotates' property is missing");
        }
        for (RDFValue value : annotates) {
            if (!value.isURI()) {
                throw new AnnoteaTypeException("'annotates' property is not a URI");
            }
        }
    }

    public static ObjectType determineObjectType(RDFObject object) throws AnnoteaTypeException {
        ObjectType putativeType = ObjectType.OTHER;
        Collection<String> types = object.getTypes();
        for (String type : types) {
            ObjectType seenType = ObjectType.OTHER;
            if (type.equals(ANNOTATION_CLASS)) {
                seenType = ObjectType.ANNOTATION;
            } else if (type.equals(REPLY_CLASS)) {
                seenType = ObjectType.REPLY;
            } else if (type.equals(HTTP_MESSAGE_CLASS)) {
                seenType = ObjectType.HTTP_MESSAGE;
            }
            if (seenType != ObjectType.OTHER && seenType != putativeType) {
                if (putativeType == ObjectType.OTHER) {
                    putativeType = seenType;
                } else {
                    throw new AnnoteaTypeException("Annotea object cannot be " + "both a " + putativeType + ", and " + seenType);
                }
            }
        }
        return putativeType;
    }
}
