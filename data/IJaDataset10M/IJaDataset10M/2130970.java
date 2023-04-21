package semantic.Verbs;

import generate.CBaseLanguageExporter;
import semantic.CBaseActionEntity;
import semantic.CDataEntity;
import semantic.CEntityProcedure;
import semantic.CEntityProcedureSection;
import semantic.CProcedureReference;
import utils.*;

/**
 * @author sly
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class CEntityCallFunction extends CBaseActionEntity {

    /**
	 * @param cat
	 * @param out
	 */
    public CEntityCallFunction(int l, CObjectCatalog cat, CBaseLanguageExporter out, String ref, String refThru, CEntityProcedureSection sectionContainer) {
        super(l, cat, out);
        String sec = "";
        if (sectionContainer != null) {
            sec = sectionContainer.GetName();
        }
        if (!ref.equals("")) {
            m_Reference = new CProcedureReference(ref, sec, cat);
            cat.getCallTree().RegisterProcedureCall(this);
        }
        if (!refThru.equals("")) {
            m_ReferenceThru = new CProcedureReference(refThru, sec, cat);
            ;
        }
    }

    protected CProcedureReference m_Reference = null;

    protected CProcedureReference m_ReferenceThru = null;

    public void Clear() {
        super.Clear();
        if (m_Reference != null) {
            m_Reference.Clear();
        }
        if (m_ReferenceThru != null) {
            m_ReferenceThru.Clear();
        }
        m_Reference = null;
        m_ReferenceThru = null;
    }

    public boolean ignore() {
        if (m_ReferenceThru == null) {
            if (m_Reference != null && m_Reference.getProcedure() != null) {
                return m_Reference.getProcedure().ignore();
            }
        }
        return false;
    }

    public boolean IgnoreVariable(CDataEntity data) {
        return false;
    }

    /**
	 * @return
	 */
    public CEntityProcedure getFirstProcedure() {
        return m_Reference.getProcedure();
    }

    /**
	 * @return
	 */
    public CEntityProcedure getLastProcedure() {
        return m_ReferenceThru.getProcedure();
    }

    public boolean hasExplicitGetOut() {
        CEntityProcedure proc = m_Reference.getProcedure();
        return proc.hasExplicitGetOut();
    }

    /**
	 * @return
	 */
    public CProcedureReference getReference() {
        return m_Reference;
    }

    public void SetRepetitions(CDataEntity entity) {
        m_refRepetitions = entity;
    }

    protected CDataEntity m_refRepetitions = null;
}
