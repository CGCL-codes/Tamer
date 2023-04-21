package net.adrianromero.tpv.panels;

import net.adrianromero.data.loader.IKeyed;
import net.adrianromero.tpv.forms.AppLocal;

/**
 *
 * @author adrianromero
 * Created on February 12, 2007, 10:49 PM
 *
 */
public class ComboItemLocal implements IKeyed {

    protected Integer m_iKey;

    protected String m_sKeyValue;

    public ComboItemLocal(Integer iKey, String sKeyValue) {
        m_iKey = iKey;
        m_sKeyValue = sKeyValue;
    }

    public Object getKey() {
        return m_iKey;
    }

    public Object getValue() {
        return m_sKeyValue;
    }

    public String toString() {
        return AppLocal.getIntString(m_sKeyValue);
    }
}
