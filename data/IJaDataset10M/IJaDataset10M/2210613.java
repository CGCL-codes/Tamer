package gg.de.sbmp3.actions.edit.artist;

import gg.de.sbmp3.actions.AbstractAction;
import gg.de.sbmp3.backend.BEFactory;
import gg.de.sbmp3.backend.EditBE;
import gg.de.sbmp3.backend.data.AlbumBean;
import gg.de.sbmp3.backend.data.ArtistBean;
import gg.de.sbmp3.common.Util;
import gg.de.sbmp3.forms.AlbumFormBean;
import gg.de.sbmp3.forms.ArtistFormBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DoEditArtist extends AbstractAction {

    private static Log log = LogFactory.getLog(DoEditArtist.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        ArtistFormBean artistForm = (ArtistFormBean) form;
        EditBE editBe = BEFactory.getEditBE(getCurrentUser(request));
        ArtistBean artist = editBe.getArtist(artistForm.getId());
        ArtistBean existingArtist = editBe.getArtist(artistForm.getName());
        if (existingArtist != null && existingArtist.getId() != artistForm.getId()) {
            if (!artistForm.isMerged()) {
                ArtistFormBean existingArtistForm = new ArtistFormBean();
                ShowEditArtist.fillArtistForm(existingArtist, existingArtistForm);
                ArtistFormBean mergedArtistForm = fillMergedArtistForm(artistForm, existingArtist);
                request.setAttribute("artistFormBean", mergedArtistForm);
                request.getSession().setAttribute("newArtistFormBean", artistForm);
                request.getSession().setAttribute("existingArtistFormBean", existingArtistForm);
                return mapping.findForward("merge");
            } else {
                request.getSession().removeAttribute("newArtistFormBean");
                request.getSession().removeAttribute("existingArtistFormBean");
                editBe.changeArtistForAllFiles(existingArtist.getId(), artistForm.getId());
                editBe.remove(existingArtist);
                fillArtistBean(artistForm, artist);
                editBe.edit(artist);
                request.setAttribute("message", "Artists merged.");
                return mapping.findForward("msg");
            }
        } else {
            fillArtistBean(artistForm, artist);
            editBe.edit(artist);
            request.setAttribute("message", "Artist changed.");
            return mapping.findForward("msg");
        }
    }

    private ArtistFormBean fillMergedArtistForm(ArtistFormBean artistForm, ArtistBean existingArtist) {
        ArtistFormBean mergedArtistForm = new ArtistFormBean();
        mergedArtistForm.setMerged(true);
        mergedArtistForm.setId(artistForm.getId());
        mergedArtistForm.setName(artistForm.getName());
        if (artistForm.getUrl() == null || artistForm.getUrl().trim().equals("")) mergedArtistForm.setUrl(existingArtist.getUrl()); else mergedArtistForm.setUrl(artistForm.getUrl());
        return mergedArtistForm;
    }

    private void fillArtistBean(ArtistFormBean artistForm, ArtistBean artist) {
        artist.setName(artistForm.getName());
        artist.setUrl(artistForm.getUrl());
    }
}
