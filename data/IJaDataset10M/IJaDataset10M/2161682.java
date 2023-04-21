package com.bluebrim.text.impl.shared;

import java.util.*;
import javax.swing.text.*;
import org.w3c.dom.*;
import com.bluebrim.base.shared.*;
import com.bluebrim.xml.shared.*;

/**
 * Abstract class for linebreakers.
 * 
 * @author: Dennis Malmstr�m
 */
public abstract class CoAbstractLineBreaker extends com.bluebrim.base.shared.CoObject implements CoLineBreakerIF {

    private CoMutableLineBreakerIF m_mutableProxy;

    protected class MutableProxy implements CoMutableLineBreakerIF {

        public void addPropertyChangeListener(CoPropertyChangeListener l) {
            CoAbstractLineBreaker.this.addPropertyChangeListener(l);
        }

        public void removePropertyChangeListener(CoPropertyChangeListener l) {
            CoAbstractLineBreaker.this.removePropertyChangeListener(l);
        }

        public final CoMutableLineBreakerIF getMutableProxy() {
            return CoAbstractLineBreaker.this.getMutableProxy();
        }

        public final String getFactoryKey() {
            return CoAbstractLineBreaker.this.getFactoryKey();
        }

        public CoLineBreakerIF.BreakPointIteratorIF getBreakPoints(Segment text) {
            return CoAbstractLineBreaker.this.getBreakPoints(text);
        }

        public String getUIname() {
            return CoAbstractLineBreaker.this.getUIname();
        }

        public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF v) {
            CoAbstractLineBreaker.this.xmlVisit(v);
        }

        public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
            CoAbstractLineBreaker.this.xmlImportFinished(node, context);
        }

        public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
            CoAbstractLineBreaker.this.xmlAddSubModel(parameter, subModel, context);
        }
    }

    ;

    protected CoMutableLineBreakerIF createMutableProxy() {
        return new MutableProxy();
    }

    public final CoMutableLineBreakerIF getMutableProxy() {
        if (m_mutableProxy == null) {
            m_mutableProxy = createMutableProxy();
        }
        return m_mutableProxy;
    }

    public String getUIname() {
        return "com.bluebrim.text.impl.client.CoAbstractLineBreakerUI";
    }

    /**
	 *	Used by a com.bluebrim.xml.shared.CoXmlVisitorIF instance when visiting an object.
	 *  The object then uses the com.bluebrim.xml.shared.CoXmlVisitorIF interface to feed the
	 *  visitor with state information
	 */
    public void xmlVisit(com.bluebrim.xml.shared.CoXmlVisitorIF visitor) {
    }

    /**
 * When the parser has created a sub model to this node, this method will
 * be called.  If the sub model has an identifier, it is passed to this
 * method.
 *
 * @param parameter A sub model identifier (can be <code>null</code>)
 *
 * @param subMmodel The sub model.  The <code>subModel</code> may be a
 * {@link java.io.InputStream InputStream}.  In that case, the actual model is a
 * binary chunk which can be read from that <code>InputStream</code>.
 * Another special case is when you have XML:ed a <code>Collection</code>.  In
 * that case the <code>subModel</code> will be an {@link java.util.Iterator Iterator}
 * from which the original <code>Collection</code> can be reconstructed.
 *
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(CoXmlWrapperFlavor, String, com.bluebrim.xml.shared.CoXmlExportEnabledIF)
 * @see com.bluebrim.xml.shared.CoXmlVisitorIF#export(String, Collection)
 * @see java.io.InputStream
 */
    public void xmlAddSubModel(String parameter, Object subModel, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
    }

    /**
 * This method is called after an object and all its sub-objects have
 * been read from an XML file.
 * <p>
 * Creation date: (2001-08-28 16:13:05)
 */
    public void xmlImportFinished(Node node, com.bluebrim.xml.shared.CoXmlContext context) throws com.bluebrim.xml.shared.CoXmlReadException {
    }
}
