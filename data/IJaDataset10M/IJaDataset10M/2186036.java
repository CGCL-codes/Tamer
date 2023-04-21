package org.waveprotocol.wave.media.model;

/**
 * This class is used to generate Attachment ids.
 *
 */
public interface AttachmentIdGenerator {

    /**
   * Creates an id for an uploaded attachment.
   */
    AttachmentId newAttachmentId();

    /**
   * Creates an id for an uploaded attachment. Allows a domain to be supplied
   * to override the default domain component of the id.
   */
    AttachmentId newAttachmentId(String overrideDomain);
}
