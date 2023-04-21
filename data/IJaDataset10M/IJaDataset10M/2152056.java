package com.wizzer.m3g.viewer.ui.property;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import com.wizzer.m3g.HeaderObject;
import com.wizzer.m3g.toolkit.util.Unsigned;

/**
 * This class is a Property Source for a M3G HeaderObject.
 * 
 * @author Mark Millard
 */
public class HeaderPropertySource implements IPropertySource {

    private static final String PROPERTY_VERSION_MAJOR = "com.wizzer.m3g.viewer.ui.header.versionmajor";

    private static final String PROPERTY_VERSION_MINOR = "com.wizzer.m3g.viewer.ui.header.versionminor";

    private static final String PROPERTY_HASEXTERNALREFERENCES = "com.wizzer.m3g.viewer.ui.header.hasexternalreferences";

    private static final String PROPERTY_TOTALFILESIZE = "com.wizzer.m3g.viewer.ui.header.totalfilesize";

    private static final String PROPERTY_APPROXIMATECONTENTSIZE = "com.wizzer.m3g.viewer.ui.header.approximatecontentsize";

    private static final String PROPERTY_AUTHORINGFIELD = "com.wizzer.m3g.viewer.ui.header.authoringfield";

    private HeaderObject m_header;

    private IPropertyDescriptor[] m_descriptors;

    private HeaderPropertySource() {
    }

    /**
	 * Create a new property source.
	 * 
	 * @param header The associated M3G HeaderObject for this property.
	 */
    public HeaderPropertySource(HeaderObject header) {
        m_header = header;
    }

    /**
	 * Get the associated Header data.
	 * 
	 * @return A <code>HeaderObject</code> is returned.
	 */
    public HeaderObject getHeader() {
        return m_header;
    }

    public Object getEditableValue() {
        return this;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (m_descriptors == null) {
            PropertyDescriptor versionMajorDescr = new PropertyDescriptor(PROPERTY_VERSION_MAJOR, "Version Major Number");
            PropertyDescriptor versionMinorDescr = new PropertyDescriptor(PROPERTY_VERSION_MINOR, "Version Minor Number");
            PropertyDescriptor hasExternalReferencesDescr = new PropertyDescriptor(PROPERTY_HASEXTERNALREFERENCES, "Has External References");
            PropertyDescriptor totalFileSizeDescr = new PropertyDescriptor(PROPERTY_TOTALFILESIZE, "Total File Size");
            PropertyDescriptor approximateContentSizeDescr = new PropertyDescriptor(PROPERTY_APPROXIMATECONTENTSIZE, "Approximate Content Size");
            PropertyDescriptor auhtoringFieldDescr = new PropertyDescriptor(PROPERTY_AUTHORINGFIELD, "Authoring Field");
            m_descriptors = new IPropertyDescriptor[] { versionMajorDescr, versionMinorDescr, hasExternalReferencesDescr, totalFileSizeDescr, approximateContentSizeDescr, auhtoringFieldDescr };
        }
        return m_descriptors;
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(PROPERTY_VERSION_MAJOR)) {
            return m_header.getVersionMajor();
        } else if (id.equals(PROPERTY_VERSION_MINOR)) {
            return m_header.getVersionMinor();
        } else if (id.equals(PROPERTY_HASEXTERNALREFERENCES)) {
            return m_header.isHasExternalReferences();
        } else if (id.equals(PROPERTY_TOTALFILESIZE)) {
            byte[] data = intToByteArray((int) m_header.getTotalFileSize());
            long value = Unsigned.readDWORD(data, 0);
            return value;
        } else if (id.equals(PROPERTY_APPROXIMATECONTENTSIZE)) {
            byte[] data = intToByteArray((int) m_header.getApproximateContentSize());
            long value = Unsigned.readDWORD(data, 0);
            return value;
        } else if (id.equals(PROPERTY_AUTHORINGFIELD)) {
            return m_header.getAuthoringField();
        } else return null;
    }

    public boolean isPropertySet(Object id) {
        if (PROPERTY_VERSION_MAJOR.equals(id) || PROPERTY_VERSION_MINOR.equals(id) || PROPERTY_HASEXTERNALREFERENCES.equals(id) || PROPERTY_TOTALFILESIZE.equals(id) || PROPERTY_APPROXIMATECONTENTSIZE.equals(id) || PROPERTY_AUTHORINGFIELD.equals(id)) return true;
        return false;
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
    }

    private byte[] intToByteArray(final int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];
        for (int n = 0; n < byteNum; n++) byteArray[3 - n] = (byte) (integer >>> (n * 8));
        return (byteArray);
    }
}
