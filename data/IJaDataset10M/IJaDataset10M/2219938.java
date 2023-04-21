package org.kalypso.nofdpidss.evaluation.ranking.navigation;

import javax.xml.namespace.QName;

public class PestCheckboxData {

    public final QName m_qName;

    public final String m_sLabel;

    public PestCheckboxData(final QName name, final String label) {
        m_qName = name;
        m_sLabel = label;
    }
}
