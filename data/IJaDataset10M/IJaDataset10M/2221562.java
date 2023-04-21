package delphorm.web.aspect;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.springframework.aop.MethodBeforeAdvice;
import delphorm.entite.personne.GroupeCollectif;
import delphorm.service.personne.IGroupeCollectif;

public class VerifParamsIdGroupeCollectifFacultatif implements MethodBeforeAdvice {

    IGroupeCollectif serviceGroupeCollectif;

    public void before(Method method, Object[] arguments, Object target) throws Throwable {
        HttpServletRequest request = (HttpServletRequest) arguments[0];
        String idGStr = request.getParameter("idgroupecollectif");
        if (idGStr != null) {
            long idG;
            if (idGStr != null) {
                try {
                    idG = Long.parseLong(idGStr);
                } catch (NumberFormatException e) {
                    throw new delphorm.web.exception.ParametreException("parametre.mauvaisformat", "idgroupecollectif");
                }
                GroupeCollectif groupeCollectif = serviceGroupeCollectif.chercherGroupeParId(new Long(idG));
                if (groupeCollectif == null) throw new delphorm.web.exception.ParametreException("parametre.invalide", "idgroupecollectif");
            }
        }
    }

    public IGroupeCollectif getServiceGroupeCollectif() {
        return serviceGroupeCollectif;
    }

    public void setServiceGroupeCollectif(IGroupeCollectif serviceGroupeCollectif) {
        this.serviceGroupeCollectif = serviceGroupeCollectif;
    }
}
