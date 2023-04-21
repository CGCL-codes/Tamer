package org.kablink.teaming.extension;

import org.kablink.teaming.exception.UncheckedCodedException;

public class ExtensionExistsException extends UncheckedCodedException {

    private static final String ExtensionExistsException_ErrorCode = "errorcode.extension.exists";

    public ExtensionExistsException() {
        super(ExtensionExistsException_ErrorCode, new Object[] { "" });
    }

    public ExtensionExistsException(String extension) {
        super(ExtensionExistsException_ErrorCode, new Object[] { extension });
    }

    public ExtensionExistsException(String extension, Throwable cause) {
        super(ExtensionExistsException_ErrorCode, new Object[] { extension });
    }

    public ExtensionExistsException(Throwable cause) {
        super(ExtensionExistsException_ErrorCode, new Object[] { cause });
    }
}
