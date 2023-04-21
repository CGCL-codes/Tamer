package org.jxul.swing.attr;

import java.util.logging.Logger;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.xml.transform.stream.StreamSource;
import org.apache.batik.dom.AbstractDocument;
import org.jxul.swing.VisualElement;
import org.jxul.swing.XulDocumentImpl;
import org.jxul.util.AttrFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

/**
 * @author <a href="wetson@comcast.net">Will Etson</a>
 * 
 */
public class XulImageAttr extends XulSwingAttr {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(XulImageAttr.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public static class Factory implements AttrFactory {

        /**
		 * @see org.jxul.util.AttrFactory#create(java.lang.String,
		 *      org.apache.batik.dom.AbstractDocument)
		 */
        public Attr create(String name, AbstractDocument ownerDocument) {
            return new XulImageAttr(name, ownerDocument);
        }
    }

    /**
	 * The Attribute name
	 */
    public static final String ATTR_NAME = "image";

    private ImageIcon image;

    /**
	 * 
	 */
    public XulImageAttr() {
        super();
    }

    /**
	 * @param name
	 * @param owner
	 * @throws DOMException
	 */
    public XulImageAttr(String name, AbstractDocument owner) throws DOMException {
        super(name, owner);
    }

    /**
	 * @see org.jxul.swing.attr.XulSwingAttr#update()
	 */
    @Override
    protected void update() {
        if (this.ownerElement instanceof VisualElement) {
            Component component = ((VisualElement) this.ownerElement).getComponent();
            if (component instanceof AbstractButton) {
                ((AbstractButton) component).setMnemonic(this.getValue().codePointAt(0));
            }
        }
    }

    public ImageIcon getImage() {
        if (this.image == null) {
            XulDocumentImpl doc = (XulDocumentImpl) this.ownerDocument;
            StreamSource source = null;
            try {
                source = (StreamSource) doc.resolve(this.getValue(), this.ownerDocument.getDocumentURI());
                BufferedImage image = ImageIO.read(source.getInputStream());
                this.image = new ImageIcon(image);
            } catch (Exception e) {
                logger.log(Level.WARNING, "Unable to load image" + this.getValue(), e);
            }
        }
        return this.image;
    }
}
