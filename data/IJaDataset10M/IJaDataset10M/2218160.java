package com.ravi.notify.mail;

/**
 * Resolver to determine Content-Type for file attachments.
 * <p/>
 * Strategy introduced to work around mail providers having problems with this such as geronimo mail jars.
 * <p/>
 * Note using SUN mail jar have no problem with resolving Content-Type based on file attachments. This resolver
 * is thus only needed to work around mail providers having bugs or when you a new mime type is unknown by the
 * mail provider allowing you to determine it.
 *
 * @version 
 */
public interface ContentTypeResolver {

    /**
     * Resolves the mime content-type based on the attachment file name.
     * <p/>
     * Return <tt>null</tt> if you cannot resolve a content type or want to rely on the mail provider
     * to resolve it for you.
     * <p/>
     * The returned value should only be the mime part of the ContentType header, for example:
     * <tt>image/jpeg</tt> should be returned. Camel will add the remaining <tt>; name=FILENAME</tt>.
     *
     * @param fileName  the attachment file name
     * @return the Content-Type or <tt>null</tt> to rely on the mail provider
     */
    String resolveContentType(String fileName);
}
