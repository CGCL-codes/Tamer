package delphorm.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import delphorm.entite.personne.GroupeCollectif;
import delphorm.service.personne.IGroupeCollectif;

public class AjouterGroupeCollectif extends SimpleFormController {

    private static final String[] ALLOWED_PROPERTIES = { "nom", "creerQuestionnaires", "voirTousQuestionnaires", "supprimerTousQuestionnaires", "modifierTousQuestionnaires", "creerToutesInstances", "voirToutesInstances", "validerToutesInstances", "supprimerToutesInstances", "modifierToutesInstances", "creerGroupe", "supprimerTousGroupes", "modifierTousGroupes", "ajouterUtilisateursTousGroupes", "supprimerUtilisateursTousGroupes", "supprimerTousUtilisateurs", "modifierTousUtilisateurs", "voirTousUtilisateurs" };

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        super.initBinder(request, binder);
        binder.setAllowedFields(ALLOWED_PROPERTIES);
    }

    private IGroupeCollectif serviceGroupeCollectif;

    protected Object formBackingObject(HttpServletRequest request) {
        Object formulaire = request.getSession().getAttribute("formulaire");
        if (formulaire == null) {
            String id = request.getParameter("idgroupecollectif");
            if (id == null) {
                formulaire = new GroupeCollectif();
            } else {
                long numero = Long.parseLong(id);
                formulaire = serviceGroupeCollectif.chercherGroupeParId(new Long(numero));
            }
        }
        return formulaire;
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object formulaire, BindException exceptions) throws Exception {
        GroupeCollectif groupe = serviceGroupeCollectif.enregistrerGroupe((GroupeCollectif) formulaire);
        Map<String, GroupeCollectif> model = new HashMap<String, GroupeCollectif>();
        model.put("ungroupecollectif", groupe);
        return new ModelAndView(getSuccessView(), model);
    }

    public IGroupeCollectif getServiceGroupeCollectif() {
        return serviceGroupeCollectif;
    }

    public void setServiceGroupeCollectif(IGroupeCollectif serviceGroupeCollectif) {
        this.serviceGroupeCollectif = serviceGroupeCollectif;
    }
}
