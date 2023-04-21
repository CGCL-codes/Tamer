package co.edu.unal.ungrid.image.processing.filter;

import org.jdom.Element;
import co.edu.unal.ungrid.parameters.AbstractParameterSet;
import co.edu.unal.ungrid.xml.XmlUtil;

public abstract class FilterParameters extends AbstractParameterSet {

    public void config(Element root) {
        setInImageProcessing(XmlUtil.getBoolean(root, "in-image-processing"));
    }

    public boolean isInImageProcessing() {
        return m_bInImageProcessing;
    }

    public void setInImageProcessing(boolean inImageProcessing) {
        m_bInImageProcessing = inImageProcessing;
    }

    private boolean m_bInImageProcessing;
}
