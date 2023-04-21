package br.revista.action;

import br.revista.decorator.DTranslation;
import br.revista.model.Paper;
import br.revista.model.UserPaper;
import br.revista.service.TranslationService;
import br.revista.service.control.AppServiceFactory;
import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("manageTranslations")
@Scope(ScopeType.CONVERSATION)
public class ManageTranslationsAction extends BaseAction {

    private TranslationService translationService;

    private List<DTranslation> lstDTranslations;

    private String user;

    private String article;

    @In(value = "#{authenticator}")
    private Authenticator authenticator;

    public ManageTranslationsAction() {
        super("ManageTranslationsAction");
    }

    private void initListDTranslations() throws Exception {
        if (translationService == null) {
            initTranslationService();
        }
        UserPaper up = authenticator.getUserPaper(Paper.TRANSLATOR);
        lstDTranslations = DTranslation.decorateList(translationService.getListTranslationByTranslator(up));
    }

    private void initTranslationService() throws Exception {
        translationService = (TranslationService) AppServiceFactory.getAppService(TranslationService.class);
    }

    @Begin
    public String start() {
        try {
            initTranslationService();
            initListDTranslations();
        } catch (Exception ex) {
            treatException(ex);
            return null;
        }
        pageTracking.startTracking();
        return "ok";
    }

    @End
    public String goBack() {
        return pageTracking.goBack();
    }

    public void updateDTranslationList() throws Exception {
        if (translationService == null) {
            initTranslationService();
        }
        lstDTranslations = DTranslation.decorateList(translationService.getListTranslations(user, article));
    }

    public List<DTranslation> getDTranslations() throws Exception {
        if (lstDTranslations == null) {
            initListDTranslations();
        }
        return lstDTranslations;
    }

    public void setDTranslations(List<DTranslation> lstDTranslations) {
        this.lstDTranslations = lstDTranslations;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }
}
