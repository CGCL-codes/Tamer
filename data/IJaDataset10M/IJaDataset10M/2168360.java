package com.ecomponentes.util.mail;

/**
 *
 * @author Dj
 */
public interface Renderable {

    Attachment getAttachment(int i);

    int getAttachmentCount();

    String getBodytext();

    String getSubject();
}
