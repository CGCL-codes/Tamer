package semantic.CICS;

import generate.CBaseLanguageExporter;
import semantic.CBaseActionEntity;
import semantic.CDataEntity;
import utils.CObjectCatalog;
import utils.CobolTranscoder.Notifs.NotifDeclareUseCICSPreprocessor;

/**
 * @author U930CV
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class CEntityCICSInquire extends CBaseActionEntity {

    /**
	 * @param line
	 * @param cat
	 * @param out
	 */
    public CEntityCICSInquire(int line, CObjectCatalog cat, CBaseLanguageExporter out) {
        super(line, cat, out);
        cat.SendNotifRequest(new NotifDeclareUseCICSPreprocessor());
    }

    public CDataEntity m_Program = null;

    public CDataEntity m_Transaction = null;

    public boolean ignore() {
        return false;
    }

    public void Clear() {
        super.Clear();
        m_Program = null;
        m_Transaction = null;
    }
}
