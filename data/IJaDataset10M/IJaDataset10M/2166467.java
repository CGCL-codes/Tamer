package gphoto.servlet.command.afficher;

import gphoto.bo.repertoire.EmplacementTrier;
import gphoto.servlet.command.Command;
import gphoto.vo.AfficherVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AfficherRepertoireCmd implements Command {

    public String execute(HttpServletRequest req) throws Exception {
        (new AfficherCmd()).execute(req);
        (new AfficherChangerRepertoireCmd()).execute(req);
        HttpSession session = req.getSession();
        AfficherVO afficherVO = (AfficherVO) session.getAttribute("AfficherVO");
        afficherVO.setEmplacementTrierCourant((EmplacementTrier) afficherVO.getRepertoireCourant().getEmplacement());
        return "/afficher.jsp";
    }
}
