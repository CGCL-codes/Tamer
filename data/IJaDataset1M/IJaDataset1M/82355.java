package org.dcm4chex.archive.web.maverick.trash;

import java.util.List;
import org.dcm4che.data.Dataset;
import org.dcm4chex.archive.ejb.interfaces.ContentManager;
import org.dcm4chex.archive.ejb.interfaces.ContentManagerHome;
import org.dcm4chex.archive.util.EJBHomeFactory;
import org.dcm4chex.archive.web.maverick.Dcm4cheeFormController;
import org.dcm4chex.archive.web.maverick.model.StudyModel;

/**
 * 
 * @author franz.willer@gwi-ag.com
 * @version $Revision: 11261 $ $Date: 2009-05-08 04:15:19 -0400 (Fri, 08 May 2009) $
 * @since 19.12.2005
 */
public class ExpandTrashPatientCtrl extends Dcm4cheeFormController {

    protected int patPk;

    protected boolean expand;

    public final void setPatPk(int patPk) {
        this.patPk = patPk;
    }

    public final void setExpand(boolean expand) {
        this.expand = expand;
    }

    protected String perform() throws Exception {
        TrashFolderForm folderForm = TrashFolderForm.getTrashFolderForm(getCtx());
        if (expand) {
            ContentManagerHome home = (ContentManagerHome) EJBHomeFactory.getFactory().lookup(ContentManagerHome.class, ContentManagerHome.JNDI_NAME);
            ContentManager cm = home.create();
            try {
                List studies = cm.listStudiesOfPrivatePatient(patPk);
                for (int i = 0, n = studies.size(); i < n; i++) studies.set(i, new StudyModel((Dataset) studies.get(i)));
                folderForm.getPatientByPk(patPk).setStudies(studies);
            } finally {
                try {
                    cm.remove();
                } catch (Exception e) {
                }
            }
        } else {
            folderForm.getPatientByPk(patPk).getStudies().clear();
        }
        return SUCCESS;
    }

    protected String getCtrlName() {
        return "trash";
    }
}
