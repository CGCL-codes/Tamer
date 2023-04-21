package org.axsl.fontR.output;

import org.axsl.fontR.FontException;
import org.axsl.fontR.FontServer;
import org.axsl.fontR.FontUse;

/**
 * Implementations of this interface create {@link FontOutput} implementations.
 * aXSL does not expose any methods to obtain implementations of this class.
 * They are used exclusively by the implementation to create {@link FontOutput}
 * implementations.
 * However client applications that wish to provide implementations of
 * FontOutputFactory for custom mime-types may create such implementations and
 * register them using {@link
 * FontServer#registerFontOutputFactory(FontOutputFactory)}.
 */
public interface FontOutputFactory {

    /**
     * Create a FontOutput implementation.
     * This method is not intended for use by client applications.
     * It is to be used only by FontUse implementations if they do not already
     * have a FontOutput for the desired mime type.
     * Client applications that need a FontOutput instance should instead use
     * {@link FontUse#getFontOutput(String)}.
     * @param fontUse The FontUse implementation for whom the FontOutput should
     * be created.
     * @return A FontOutput implementation suitable for this factory's
     * mime type.
     * @throws FontException For errors during creation of the FontOutput
     * instance.
     * @see FontUse#getFontOutput(String)
     */
    FontOutput createFontOutput(FontUse fontUse) throws FontException;

    /**
     * Returns the mime-type for this factory.
     * @return The mime type for which this factory generates FontOutput
     * instances.
     * For example, a FontOutputFactory returning FontOutput instances for use
     * with PDF (Portable Document Format) should return "application/pdf".
     * See the <a href="http://www.iana.org/assignments/media-types/"
     * target="new">Internet Assigned Numbers Authority MIME Media Types</a>
     * directory for a complete list of mime-types.
     */
    String getMimeType();
}
