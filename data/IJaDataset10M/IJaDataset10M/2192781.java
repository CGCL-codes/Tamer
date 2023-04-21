package org.bejug.javacareers.jobs.view.jsf.action;

import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bejug.javacareers.common.ajax.CompletionResult;
import org.bejug.javacareers.jobs.search.lucene.PdfException;
import org.bejug.javacareers.jobs.search.lucene.SearchResult;
import org.bejug.javacareers.jobs.service.LucenePdfService;
import org.bejug.javacareers.jobs.view.jsf.forms.SearchResumeForm;
import org.bejug.javacareers.jobs.view.jsf.model.CurrentItem;
import org.bejug.javacareers.jobs.view.jsf.model.PdfSearchResultData;
import org.bejug.javacareers.jobs.view.jsf.util.Utils;
import org.bejug.javacareers.project.properties.JavaCareersConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * @author Peter Symoens (Last modified by $Author: kristofvb $)
 * @version $Revision: 1.35 $ - $Date: 2006/01/13 16:06:47 $
 */
public class ResumeAction extends BaseAction {

    /**
     * The ManageResume CommentAction logger.
     */
    private static final Log LOG = LogFactory.getLog(ResumeAction.class);

    /**
     * Todo: a web controller should not use the pdf service directly 
     * the ResumeService should use this instead
     * The Job Service reference, injected by Spring container.
     */
    private LucenePdfService lucenePdfService;

    /**
     * The user tracker, injected.
     */
    private CurrentItem currentItem;

    /**
     * Set lucenePdfService via Springs IoC injection.
     *
     * @param lucenePdfService JobService
     */
    public void setLucenePdfService(LucenePdfService lucenePdfService) {
        this.lucenePdfService = lucenePdfService;
    }

    /**
     * Process the resume upload.
     *
     * @param event the JSF action event.
     *
    public void processUploadResume(ActionEvent event) {
        LOG.info("Debug: --executing processUploadResume");
        PostResumeForm form = getUploadresumeForm();
        User user = userTracker.getCurrentUser();
        LOG.info("Debug: user = " + user);
        String path = getFilePath(user);
        form.getResume().setPublicationDate(form.getResume().getCreationDate());
        //for some reason hibertnate does not delete the resume if you just override the users resume
        if (user.getResume() != null){
        	user.getResume().setPublicationDate(form.getResume().getPublicationDate());
        	user.getResume().setTitle(form.getResume().getTitle());
        }else{
        	user.setResume(form.getResume());
        }
        LOG.info("Debug: loading resume: " + path);
        try {
            File file = getUploadedFile(path);
            if (file != null) {
                user.setCvAvailable(true);
                try {
                    adminService.storeUser(user);
                } catch (DuplicateUserNameException e) {
                    LOG.error(e);
                } catch (LastAdminException e) {
                    LOG.error(e);
                }

                //TODO update user, create update method
                form.setInfoMessage("label_uploaded_ok");
                form.setRenderInfoMessage(true);
                LOG.info("Debug: Delegating to luceneIndexer: "
                        + file.getAbsolutePath());
                lucenePdfService.addToIndex(file.getAbsolutePath(), user.getUserName());
            } else {
                LOG.error("error loading resume: " + path);
                LOG.error("error loading resume: " + path + " as internal "
                        + file.getAbsolutePath());
            }
        } catch (IOException e) {
            LOG.error("FileNotFoundException caught : " + path);
        } catch (PdfException e) {
            LOG.info("Debug: LuceneException caught");
        }

    }

    /**
     * Process delete resume.
     *
     * @param context the JSF context
     * @param prefix a String containing the prefix.
     * @param result the completionresult of the ajax search.
     */
    public void processAjaxSearch(FacesContext context, String prefix, CompletionResult result) {
        LOG.info("Debug: --> executing processAjaxSearch");
        LOG.info("Debug: criteria: " + prefix);
        try {
            lucenePdfService.setHighlightTags("", "");
            List results = lucenePdfService.getSearchResultList(prefix, 20);
            convertFileUrls(results);
            result.addItem("- Results preview:");
            for (int i = 0; i < results.size(); i++) {
                SearchResult searchResult = (SearchResult) results.get(i);
                LOG.info("Debug: Added result: " + searchResult.getFile());
                LOG.info("Debug: For hit on: " + searchResult.getContext());
                List matchResult = searchResult.getContext();
                for (int j = 0; j < matchResult.size() && j < 3; j++) {
                    String contextMatch = (String) matchResult.get(j);
                    contextMatch = contextMatch + " from " + searchResult.getUser();
                    contextMatch = contextMatch.replace(',', ';');
                    contextMatch = contextMatch + "," + searchResult.getFile();
                    LOG.info("Debug: Added: " + contextMatch);
                    result.addItem(contextMatch);
                }
            }
            LOG.info("Debug: Results processed");
            if (result.getItems().size() == 1) {
                result.addItem("- None");
            }
            LOG.info("Debug: Send back: " + results);
            LOG.info("Debug: Using" + result.getItems());
        } catch (PdfException e) {
            LOG.error(e);
        }
    }

    /**
     * Process the simple cv search action.
     *
     * @param actionEvent the JSF action event.
     */
    public void processSimpleSearch(ActionEvent actionEvent) {
        LOG.info("Debug: --> executing processSimpleSearch");
        SearchResumeForm form = (SearchResumeForm) Utils.getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), "searchResumeForm");
        if (form.getCriteria().trim().equals(Utils.getMessage("label_search"))) {
            outcome = null;
        } else {
            LOG.info("Debug: criteria: " + form.getCriteria());
            List results = null;
            try {
                lucenePdfService.setHighlightTags("<i><b>", "</b></i>");
                results = lucenePdfService.getSearchResultList(form.getCriteria());
            } catch (PdfException e) {
                LOG.error(e);
            }
            if (results.size() > 0) {
                convertFileUrls(results);
                PdfSearchResultData.setSearchResults(results);
                form.setRenderPdfSearchResult(true);
                LOG.info("Debug: data found");
            } else {
                LOG.info("Debug: no data found");
                form.setInfoMessage(Utils.getMessage("label_noDataFound"));
                form.setRenderInfoMessage(true);
            }
            outcome = "go_cvsearch";
        }
    }

    /**
     * @param results List
     */
    private void convertFileUrls(List results) {
        for (Iterator iterator = results.iterator(); iterator.hasNext(); ) {
            SearchResult pdfSearchResult = (SearchResult) iterator.next();
            String file = pdfSearchResult.getFile();
            pdfSearchResult.setFile(convertFilePathToUrl(file));
        }
    }

    /**
     * @param filePath String
     * @return url String
     */
    private String convertFilePathToUrl(String filePath) {
        LOG.info("Debug: FilePath= " + filePath);
        filePath = filePath.replaceAll("\\\\", "/");
        LOG.info("Debug: FilePathreplaced= " + filePath);
        LOG.info("Debug: uploadPath = " + getProjectProperties().getUploadPath());
        int index = filePath.indexOf(getProjectProperties().getUploadPath());
        String localPath = filePath.substring(index + 1);
        LOG.info("Debug: localPath = " + localPath);
        String baseUrl = getProjectProperties().getUrl() + getProjectProperties().getAppname() + "/";
        LOG.info("Debug: baseUrl= " + baseUrl);
        String url = baseUrl.concat(localPath);
        LOG.info("Debug:  url = " + url);
        return url;
    }

    private JavaCareersConfig getProjectProperties() {
        ApplicationContext ctx = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
        return (JavaCareersConfig) ctx.getBean("config");
    }

    /**
	 * @param currentItem The currentItem to set.
	 */
    public void setCurrentItem(CurrentItem currentItem) {
        this.currentItem = currentItem;
    }
}
