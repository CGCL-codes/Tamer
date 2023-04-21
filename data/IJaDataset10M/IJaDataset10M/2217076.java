package com.maziade.qml.contentnode;

import java.io.IOException;
import org.w3c.dom.Element;
import com.maziade.qml.renderer.RendererIF;
import com.maziade.qml.tools.XMLUtils;

/**
 * 
 * @author Eric Maziade
 * 
 * Implamentation of the 'image' tag
 *
 */
public class QMLImage extends QMLContentNode {

    /**
	 * Constructor
	 * @param root
	 * @throws QMLParseException
	 */
    public QMLImage(Element root) throws QMLParseException {
        super(root);
        m_src = root.getAttribute("source");
        m_alt = root.getAttribute("text");
        m_default = XMLUtils.areStringsEqual(root.getAttribute("default"), "true");
    }

    /** 
	 * @return image source URL
	 */
    public String getSource() {
        return m_src;
    }

    /** 
	 * @return alternate text (if image is unavailable)
	 */
    public String getAltText() {
        return m_alt;
    }

    /** 
	 * @return true if this image should be the default image for stations that have no
	 * image information
	 */
    public boolean isDefault() {
        return m_default;
    }

    @Override
    public void render(RendererIF renderer) throws IOException {
        renderer.renderQMLImage(this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    private String m_src;

    private String m_alt;

    private boolean m_default;
}
