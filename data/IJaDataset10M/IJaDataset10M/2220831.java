package gphoto.servlet.command.afficher;

import gphoto.services.PhotoServices;
import gphoto.services.RepertoireServices;
import gphoto.services.impl.PhotoServicesImpl;
import gphoto.services.impl.RepertoireServicesImpl;
import gphoto.servlet.command.Command;
import gphoto.vo.AfficherVO;
import gphoto.vo.DeplacerRepertoireVO;
import gphoto.vo.Message;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DeplacerPhotosEffectuerCmd implements Command {

    public String execute(HttpServletRequest req) throws Exception {
        HttpSession session = req.getSession();
        AfficherVO afficherVO = (AfficherVO) session.getAttribute("AfficherVO");
        boolean isConnected = (session.getAttribute("connexion") != null);
        DeplacerRepertoireVO deplacerRepVO = (DeplacerRepertoireVO) session.getAttribute("DeplacerRepertoireVO");
        if (deplacerRepVO.getRepertoireCourant() == null) {
            Message msg = new Message("D�placement impossible, aucun r�pertoire s�lectionn�.", Message.MESSAGE_ERREUR);
            session.setAttribute("message", msg);
            return "/afficher.jsp";
        }
        PhotoServices ps = PhotoServicesImpl.getInstance();
        ps.deplacerPhotosDans(deplacerRepVO.getPhotos(), deplacerRepVO.getRepertoireCourant());
        Message msg = new Message("D�placement effectu�.", Message.MESSAGE_INFO);
        session.setAttribute("message", msg);
        afficherVO.setEmplacementTrierCourant(deplacerRepVO.getEmplacementCourant());
        afficherVO.setRepertoireCourant(deplacerRepVO.getRepertoireCourant());
        RepertoireServices rs = RepertoireServicesImpl.getInstance();
        afficherVO.setRepertoires(rs.getAllRepertoires(afficherVO.getRepertoireCourant()));
        if (isConnected) {
            ps.searchEchantillonAllPhotosDesRepertoires(afficherVO.getRepertoires());
        } else {
            ps.searchEchantillonPhotosNonPriveesDesRepertoires(afficherVO.getRepertoires());
        }
        if (isConnected) {
            afficherVO.setPhotos(ps.getAllPhotos(afficherVO.getRepertoireCourant()));
        } else {
            afficherVO.setPhotos(ps.getPhotosNonPrivees(afficherVO.getRepertoireCourant()));
        }
        return "/afficher.jsp";
    }
}
