package org.iptc.ines.component.persistence.model;

import org.iptc.ines.component.persistence.util.LabelProperty;
import org.iptc.nar.core.datatype.QCodeType;

/**
 * Persistence class to map LabelType element associated with AnyItem directly
 * 
 * @author Bertrand GOUPIL
 *
 */
public class ItemLabel extends LabelType {

    private LabelProperty m_labelProperty;

    /**
	 * URI form of role value
	 */
    private QCodeType m_role;

    public LabelProperty getLabelProperty() {
        return m_labelProperty;
    }

    public void setLabelProperty(LabelProperty labelName) {
        m_labelProperty = labelName;
    }

    public QCodeType getRole() {
        return m_role;
    }

    public void setRole(QCodeType role) {
        m_role = role;
    }
}
