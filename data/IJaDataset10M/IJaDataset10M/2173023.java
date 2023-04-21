package org.restlet.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;
import org.restlet.data.CharacterSet;
import org.restlet.data.Language;
import org.restlet.data.MediaType;

/**
 * Represents an Unicode string that can be converted to any character set
 * supported by Java.
 *
 * @author Jerome Louvel (contact@noelios.com)
 */
public class StringRepresentation extends StreamRepresentation {

    private CharSequence text;

    /**
     * Constructor. The following metadata are used by default: "text/plain"
     * media type, no language and the ISO-8859-1 character set.
     *
     * @param text
     *            The string value.
     */
    public StringRepresentation(CharSequence text) {
        this(text, MediaType.TEXT_PLAIN);
    }

    /**
     * Constructor. The following metadata are used by default: "text/plain"
     * media type, no language and the ISO-8859-1 character set.
     *
     * @param text
     *            The string value.
     * @param language
     *            The language.
     */
    public StringRepresentation(CharSequence text, Language language) {
        this(text, MediaType.TEXT_PLAIN, language);
    }

    /**
     * Constructor. The following metadata are used by default: no language and
     * the ISO-8859-1 character set.
     *
     * @param text
     *            The string value.
     * @param mediaType
     *            The media type.
     */
    public StringRepresentation(CharSequence text, MediaType mediaType) {
        this(text, mediaType, null);
    }

    /**
     * Constructor. The following metadata are used by default: ISO-8859-1
     * character set.
     *
     * @param text
     *            The string value.
     * @param mediaType
     *            The media type.
     * @param language
     *            The language.
     */
    public StringRepresentation(CharSequence text, MediaType mediaType, Language language) {
        this(text, mediaType, language, CharacterSet.ISO_8859_1);
    }

    /**
     * Constructor.
     *
     * @param text
     *            The string value.
     * @param mediaType
     *            The media type.
     * @param language
     *            The language.
     * @param characterSet
     *            The character set.
     */
    public StringRepresentation(CharSequence text, MediaType mediaType, Language language, CharacterSet characterSet) {
        super(mediaType);
        this.text = text;
        setMediaType(mediaType);
        if (language != null) {
            getLanguages().add(language);
        }
        setCharacterSet(characterSet);
        updateSize();
    }

    /**
     * Returns a stream with the representation's content. This method is
     * ensured to return a fresh stream for each invocation unless it is a
     * transient representation, in which case null is returned.
     *
     * @return A stream with the representation's content.
     * @throws IOException
     */
    @Override
    public InputStream getStream() throws IOException {
        if (getText() != null) {
            if (getCharacterSet() != null) {
                return new ByteArrayInputStream(getText().getBytes(getCharacterSet().getName()));
            } else {
                return new ByteArrayInputStream(getText().getBytes());
            }
        } else {
            return null;
        }
    }

    /**
     * Converts the representation to a string value. Be careful when using this
     * method as the conversion of large content to a string fully stored in
     * memory can result in OutOfMemoryErrors being thrown.
     *
     * @return The representation as a string value.
     */
    @Override
    public String getText() {
        return (this.text == null) ? null : this.text.toString();
    }

    /**
     * Sets the string value.
     *
     * @param text
     *            The string value.
     */
    public void setText(String text) {
        this.text = text;
        updateSize();
    }

    /**
     * Sets the character set or null if not applicable.
     *
     * @param characterSet
     *            The character set or null if not applicable.
     */
    @Override
    public void setCharacterSet(CharacterSet characterSet) {
        super.setCharacterSet(characterSet);
        updateSize();
    }

    /**
     * Updates the expected size according to the current string value.
     */
    protected void updateSize() {
        if (getText() != null) {
            try {
                if (getCharacterSet() != null) {
                    setSize(getText().getBytes(getCharacterSet().getName()).length);
                } else {
                    setSize(getText().getBytes().length);
                }
            } catch (UnsupportedEncodingException e) {
                Logger.getLogger(StringRepresentation.class.getCanonicalName());
                setSize(UNKNOWN_SIZE);
            }
        } else {
            setSize(UNKNOWN_SIZE);
        }
    }

    /**
     * Writes the representation to a byte stream. This method is ensured to
     * write the full content for each invocation unless it is a transient
     * representation, in which case an exception is thrown.
     *
     * @param outputStream
     *            The output stream.
     * @throws IOException
     */
    @Override
    public void write(OutputStream outputStream) throws IOException {
        if (getText() != null) {
            OutputStreamWriter osw = null;
            if (getCharacterSet() != null) {
                osw = new OutputStreamWriter(outputStream, getCharacterSet().getName());
            } else {
                osw = new OutputStreamWriter(outputStream);
            }
            osw.write(getText());
            osw.flush();
        }
    }
}
