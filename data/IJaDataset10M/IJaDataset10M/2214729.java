package com.maziade.qml.chunk;

import java.io.IOException;
import com.maziade.qml.renderer.RendererIF;

/**
 * @author Eric Maziade
 *
 * Holds only plain text
 */
public class QMLChunkPlainText implements QMLChunkIF {

    /**
	 * Constructor
	 * @param value
	 */
    public QMLChunkPlainText(String value) {
        m_value = value;
    }

    /** 
	 * @return numeric value
	 */
    public String getValue() {
        return m_value;
    }

    @Override
    public void renderChunk(RendererIF renderer) throws IOException {
        renderer.renderChunkPlainText(this);
    }

    @Override
    public String toString() {
        return m_value;
    }

    @Override
    public boolean isEmpty() {
        return m_value == null || m_value.isEmpty();
    }

    private String m_value;
}
