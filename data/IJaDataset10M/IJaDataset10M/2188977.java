package edu.mit.lcs.haystack.content;

import java.io.InputStream;

/**
 * Returns an InputStream providing access to the content of the associated resource
 * and a mime type, if known.
 */
public class ContentAndMimeType {

    public ContentAndMimeType() {
    }

    public ContentAndMimeType(InputStream is) {
        m_content = is;
    }

    public ContentAndMimeType(InputStream is, String mimeType) {
        m_content = is;
        m_mimeType = mimeType;
    }

    public InputStream m_content;

    public String m_mimeType;
}
