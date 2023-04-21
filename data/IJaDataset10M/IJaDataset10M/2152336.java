package org.jcyclone.ext.adisk;

/**
 * A completion event indicating that EOF was reached on the given
 * file during an attempted I/O operation.
 *
 * @author Matt Welsh
 */
public class AFileEOFReached extends AFileCompletion {

    AFileEOFReached(AFileRequest req) {
        super(req);
    }
}
