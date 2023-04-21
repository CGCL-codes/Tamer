package proguard.classfile.util;

import proguard.classfile.*;

/**
 * An <code>ExternalTypeEnumeration</code> provides an enumeration of all
 * types listed in a given external descriptor string. The method name can
 * be retrieved separately.
 * <p>
 * A <code>ExternalTypeEnumeration</code> object can be reused for processing
 * different subsequent descriptors, by means of the <code>setDescriptor</code>
 * method.
 *
 * @author Eric Lafortune
 */
public class ExternalTypeEnumeration {

    private String descriptor;

    private int index;

    public ExternalTypeEnumeration(String descriptor) {
        setDescriptor(descriptor);
    }

    ExternalTypeEnumeration() {
    }

    void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        reset();
    }

    public void reset() {
        index = descriptor.indexOf(ClassConstants.EXTERNAL_METHOD_ARGUMENTS_OPEN) + 1;
        if (index < 1) {
            throw new IllegalArgumentException("Missing opening parenthesis in descriptor [" + descriptor + "]");
        }
    }

    public boolean hasMoreTypes() {
        return index < descriptor.length() - 1;
    }

    public String nextType() {
        int startIndex = index;
        index = descriptor.indexOf(ClassConstants.EXTERNAL_METHOD_ARGUMENTS_SEPARATOR, startIndex);
        if (index < 0) {
            index = descriptor.indexOf(ClassConstants.EXTERNAL_METHOD_ARGUMENTS_CLOSE, startIndex);
            if (index < 0) {
                throw new IllegalArgumentException("Missing closing parenthesis in descriptor [" + descriptor + "]");
            }
        }
        return descriptor.substring(startIndex, index++).trim();
    }

    public String methodName() {
        return descriptor.substring(0, descriptor.indexOf(ClassConstants.EXTERNAL_METHOD_ARGUMENTS_OPEN)).trim();
    }
}
