package de.mindcrimeilab.xsanalyzer.xsext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.apache.xerces.xs.XSTypeDefinition;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class TypeDescriptionFactory {

    private final MessageDigest messageDigest;

    public TypeDescriptionFactory() throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance("MD5");
    }

    public TypeDescriptionFactory(String algorithm) throws NoSuchAlgorithmException {
        messageDigest = MessageDigest.getInstance(algorithm);
    }

    public String generateTypeDescriptionSignature(XSTypeDefinition typedef) {
        final String signature;
        switch(typedef.getTypeCategory()) {
            case XSTypeDefinition.SIMPLE_TYPE:
                signature = SimpleTypeDescription.getSignatureDigest((XSSimpleTypeDefinition) typedef, messageDigest);
                break;
            case XSTypeDefinition.COMPLEX_TYPE:
                signature = ComplexTypeDescription.getSignatureDigest((XSComplexTypeDefinition) typedef, messageDigest);
                break;
            default:
                throw new RuntimeException("Unexpected type category [" + typedef.getTypeCategory() + "]");
        }
        return signature;
    }
}
