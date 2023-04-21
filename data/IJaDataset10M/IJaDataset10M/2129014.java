package br.revista.service;

import br.revista.dao.LayoutDAO;
import br.revista.dao.control.DaoFactory;
import br.revista.dao.jpa.LayoutJPADAO;
import br.revista.exception.ObjectNotFoundException;
import br.revista.model.Article;
import br.revista.model.File;
import br.revista.model.Layout;
import br.revista.model.Message;
import br.revista.model.Paper;
import br.revista.model.User;
import br.revista.model.UserPaper;
import br.revista.model.Volume;
import br.revista.service.control.AnnTransactional;
import br.revista.service.control.AppServiceFactory;
import br.revista.util.email.EmailUtil;
import br.revista.util.email.Link;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.bcel.generic.IFEQ;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.Component;

public class LayoutService {

    private LayoutDAO layoutDAO;

    private ArticleService articleService;

    private MessageService messageService;

    private UserService userService;

    private VolumeService volumeService;

    private EmailUtil emailUtil = (EmailUtil) Component.getInstance(EmailUtil.class);

    public LayoutService() {
        try {
            layoutDAO = DaoFactory.getDao(LayoutJPADAO.class, Layout.class);
            articleService = (ArticleService) AppServiceFactory.getAppService(ArticleService.class);
            messageService = (MessageService) AppServiceFactory.getAppService(MessageService.class);
            userService = (UserService) AppServiceFactory.getAppService(UserService.class);
            volumeService = (VolumeService) AppServiceFactory.getAppService(VolumeService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Layout getCurrentLayout(Article article) throws ObjectNotFoundException {
        return layoutDAO.recoverCurrentLayoutByArticle(article);
    }

    public Layout getLayoutById(Long code) throws ObjectNotFoundException {
        return (Layout) layoutDAO.getById(code);
    }

    public List<Layout> getListLayoutByPublisher(UserPaper up) {
        return layoutDAO.recoverLayoutsByPublisher(up);
    }

    public List<Layout> getListByUser(String user) {
        if (StringUtils.isEmpty(user)) {
            return new ArrayList<Layout>();
        }
        return layoutDAO.recoverLayoutsByUser(user);
    }

    public void saveFormattedFiles(Layout layout, List<File> lstFiles) {
        if (layout.getStatusForArticle() == Layout.FORMATTING_ARTICLE) {
            layout.setStatusForArticle(Layout.FORMATTED_ARTICLE);
            if (layout.getStatusForTranslatedArticle() == Layout.FORMATTED_TRANSLATED_ARTICLE) {
                layout.setStatus(Layout.FORMATTING_COMPLETED);
                if (layout.getArticle().getStatus() == Article.FORMATTING) {
                    layout.getArticle().setStatus(Article.FINISHED);
                    layout.getArticle().setLayout(layout);
                    articleService.updateArticle(layout.getArticle());
                    Message message = null;
                    for (User user : userService.getListUsersByPaper(Paper.EDITOR)) {
                        try {
                            message = messageService.insertSystemMessage(user, EmailUtil.TITLE_ARTICLE_FORMATTED, EmailUtil.ARTICLE_FORMATTED);
                            emailUtil.send(message, new Link(layout.getArticle().getTitle(), EmailUtil.URL_EDITOR_ARTICLE + layout.getArticle().getCode()));
                        } catch (ObjectNotFoundException ex) {
                        }
                    }
                    Article article = articleService.getFullArticleById(layout.getArticle().getCode());
                    Volume volume = article.getVolume();
                    message.setTitle(EmailUtil.TITLE_ARTICLE_FORMATTED_CHIEF);
                    message.setText(EmailUtil.ARTICLE_FORMATTED_CHIEF);
                    for (User user : userService.getListUsersByPaper(Paper.EDITOR_CHIEF)) {
                        message.setReciever(user);
                        emailUtil.send(message, new Link(volume.getNumber() + " / " + volume.getYear(), EmailUtil.URL_EDITOR_CHIEF_VOLUME + volume.getCode()), layout.getArticle().getTitle());
                    }
                }
            }
        }
        for (File file : lstFiles) {
            file.setLayoutFormatted(layout);
        }
        layout.setFilesFormatted(new HashSet(lstFiles));
        layout.setDateEnd(new GregorianCalendar());
        createOrUpdateLayout(layout);
    }

    public void saveTranslatedFormattedFiles(Layout layout, List<File> lstFiles) {
        if (layout.getStatusForTranslatedArticle() == Layout.FORMATTING_TRANSLATED_ARTICLE) {
            layout.setStatusForTranslatedArticle(Layout.FORMATTED_TRANSLATED_ARTICLE);
            if (layout.getStatusForArticle() == Layout.FORMATTED_ARTICLE) {
                layout.setStatus(Layout.FORMATTING_COMPLETED);
                if (layout.getArticle().getStatus() == Article.FORMATTING) {
                    layout.getArticle().setStatus(Article.FINISHED);
                    layout.getArticle().setLayout(layout);
                    articleService.updateArticle(layout.getArticle());
                    Message message = null;
                    for (User user : userService.getListUsersByPaper(Paper.EDITOR)) {
                        try {
                            message = messageService.insertSystemMessage(user, EmailUtil.TITLE_ARTICLE_FORMATTED, EmailUtil.ARTICLE_FORMATTED);
                            emailUtil.send(message, new Link(layout.getArticle().getTitle(), EmailUtil.URL_EDITOR_ARTICLE + layout.getArticle().getCode()));
                        } catch (ObjectNotFoundException ex) {
                        }
                    }
                    Article article = articleService.getFullArticleById(layout.getArticle().getCode());
                    Volume volume = article.getVolume();
                    message.setTitle(EmailUtil.TITLE_ARTICLE_FORMATTED_CHIEF);
                    message.setText(EmailUtil.ARTICLE_FORMATTED_CHIEF);
                    for (User user : userService.getListUsersByPaper(Paper.EDITOR_CHIEF)) {
                        message.setReciever(user);
                        emailUtil.send(message, new Link(volume.getNumber() + " / " + volume.getYear(), EmailUtil.URL_EDITOR_CHIEF_VOLUME + volume.getCode()), layout.getArticle().getTitle());
                    }
                }
            }
        }
        for (File file : lstFiles) {
            file.setLayoutTranslatedFormatted(layout);
        }
        layout.setTranslatedFilesFormatted(new HashSet(lstFiles));
        layout.setDateEnd(new GregorianCalendar());
        createOrUpdateLayout(layout);
    }

    @AnnTransactional
    public void createOrUpdateLayout(Layout layout) {
        if (layout.getCode() == null) {
            layoutDAO.insert(layout);
        } else {
            try {
                Layout temp = (Layout) layoutDAO.getByIdWithLock(layout.getCode());
                layoutDAO.update(layout);
            } catch (ObjectNotFoundException ex) {
                Logger.getLogger(LayoutService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
