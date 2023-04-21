package com.mr.qa.action;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.hibernate.Transaction;
import com.mr.qa.GlobalConfigs;
import com.mr.qa.SpendMoneyConstants;
import com.mr.qa.UserSession;
import com.mr.qa.UserSessionUtil;
import com.mr.qa.GlobalConfigs.UploadFileType;
import com.mr.qa.bo.ShopArticle;
import com.mr.qa.bo.ShopArticleContribute;
import com.mr.qa.bo.ShopArticleHistory;
import com.mr.qa.bo.ShopCategory;
import com.mr.qa.bo.User;
import com.mr.qa.dao.impl.ShopArticleDAO;
import com.mr.qa.dao.impl.ShopCategoryDAO;
import com.mr.qa.dao.impl.UserDAO;
import com.mr.qa.exception.QaException;
import com.mr.qa.form.ShopArticleForm;
import com.mr.qa.util.HibernateSessionFactory;
import com.mr.qa.util.mail.Mail;
import com.mr.qa.util.mail.MailUtil;

public class ShopArticleAction extends BaseAction {

    @Override
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward af = null;
        ActionMessages errors = new ActionMessages();
        ActionMessages messages = new ActionMessages();
        UserSession us = null;
        us = UserSessionUtil.getUserSession(request);
        String action = this.getAction(request);
        ShopArticleForm articleForm = (ShopArticleForm) form;
        if ("preadd".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                ShopCategory shopCategory = new ShopCategory();
                List categoryList = shopCategory.getAvailCategories();
                request.setAttribute("categoryList", categoryList);
            }
        } else if ("add".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                addArticle(articleForm, errors);
                if (!errors.isEmpty()) {
                    String path = "/shoparticle.mrqa?action=preadd";
                    af = new ActionForward(path);
                    af.setRedirect(true);
                }
            }
        } else if ("admin".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                getAllArticles(articleForm, request);
            }
        } else if ("get".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                ShopArticle article = getArticle(articleForm, request);
                request.setAttribute("article", article);
            }
        } else if ("update".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                updateArticle(articleForm, errors);
                if (!errors.isEmpty()) {
                    String path = "/shoparticle.mrqa?action=get&id=" + articleForm.getId();
                    af = new ActionForward(path);
                    af.setRedirect(true);
                }
            }
        } else if ("delete".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                if (delete(articleForm, errors, request)) {
                    String path = mapping.findForward(action).getPath();
                    af = new ActionForward(path);
                    af.setRedirect(true);
                } else {
                    request.setAttribute("articleId", articleForm.getId());
                    af = mapping.findForward("predelete");
                }
            }
        } else if ("predelete".equals(action)) {
            request.setAttribute("articleId", articleForm.getId());
        } else if ("exchangelist".equals(action)) {
            if (us == null) {
                exchangeListNoLogin(request);
                String path = "shoparticleexchangelistnologin.tiles";
                af = new ActionForward(path);
                af.setRedirect(true);
            }
            getAllArticlesNoZero(articleForm, request);
        } else if ("preexchange".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.findById(us.getUserId());
                if (!checkCanExchange(articleForm, user, errors)) {
                    String path = "/shoparticle.mrqa?action=exchangelist";
                    if (articleForm.getPage() > 0 && articleForm.getPageSize() > 0) {
                        path += "&page=" + articleForm.getPage() + "&pageSize=" + articleForm.getPageSize();
                    }
                    af = new ActionForward(path);
                    af.setRedirect(true);
                }
            }
        } else if ("exchange".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.findById(us.getUserId());
                if (exchange(articleForm, user, errors, request)) {
                    messages.add("", new ActionMessage("article.exchangesuccess"));
                } else {
                    af = mapping.findForward("preexchange");
                }
            }
        } else if ("viewhistory".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                viewHistory(articleForm, request);
            }
        } else if ("changehistorypagesize".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else if (!"1".equals(us.getType())) {
                errors.add("login", new ActionMessage("admin.requireadmin"));
                action = "relogin";
            } else {
                GlobalConfigs.SIZE_SHOP_ARTICLE_HISTORY_LIST = articleForm.getPageSize();
                af = mapping.findForward(action);
                String path = af.getPath().replace("!?", articleForm.getId());
                af = new ActionForward(path);
                af.setRedirect(true);
            }
        } else if ("precontribute".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            }
        } else if ("contribute".equals(action)) {
            if (us == null) {
                errors.add("relogin", new ActionMessage("login.required"));
                action = "login";
            } else {
                UserDAO userDAO = new UserDAO();
                User user = userDAO.findById(us.getUserId());
                if (contribute(articleForm, user, errors, request)) {
                    messages.add("", new ActionMessage("article.contribute.success"));
                } else {
                    af = mapping.findForward("precontribute");
                }
            }
        }
        if (af == null) {
            af = mapping.findForward(action);
        }
        if (errors.size() > 0) {
            saveErrors(request, errors);
            if (af.getRedirect()) saveErrors(request.getSession(), errors);
        }
        if (messages.size() > 0) {
            saveMessages(request, messages);
            if (af.getRedirect()) saveMessages(request.getSession(), messages);
        }
        return af;
    }

    private void checkArticle(ShopArticleForm form, ActionMessages errors) {
        ShopArticle article = new ShopArticle();
        ShopArticle tempArticle = null;
        if (form.getId() == null) {
            tempArticle = article.getByName(form.getName());
        } else {
            tempArticle = article.getByNameExceptThis(form.getName(), Integer.valueOf(form.getId()));
        }
        if (tempArticle != null) {
            errors.add("", new ActionMessage("article.exists", form.getName()));
            return;
        }
        String awardUserName = form.getAwardUserName();
        if (awardUserName != null && awardUserName.length() > 0) {
            UserDAO userDAO = new UserDAO();
            List userList = userDAO.findByLoginName(awardUserName);
            if (userList == null || userList.isEmpty()) {
                errors.add("", new ActionMessage("nouser", "nouser"));
                return;
            }
        }
        FormFile pic = form.getPicture();
        if (pic != null && pic.getFileSize() > 0) {
            String fileName = pic.getFileName();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            if (ext != null && !".jpg".equalsIgnoreCase(ext) && !".gif".equalsIgnoreCase(ext) && !".png".equalsIgnoreCase(ext)) {
                errors.add("error", new ActionMessage("imageheader.filename"));
                return;
            }
        }
    }

    private void addArticle(ShopArticleForm form, ActionMessages errors) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            checkArticle(form, errors);
            if (!errors.isEmpty()) {
                return;
            }
            ShopArticle article = new ShopArticle();
            article.setName(form.getName());
            String awardUserName = form.getAwardUserName();
            if (awardUserName != null && awardUserName.length() > 0) {
                UserDAO userDAO = new UserDAO();
                List userList = userDAO.findByLoginName(awardUserName);
                User awardUser = (User) userList.get(0);
                article.setAwardUser(awardUser);
            }
            Integer categoryId = form.getCategoryId();
            if (categoryId != null && categoryId.compareTo(new Integer(0)) > 0) {
                ShopCategoryDAO shopCategoryCAO = new ShopCategoryDAO();
                ShopCategory shopCategory = shopCategoryCAO.findById(categoryId);
                article.setCategory(shopCategory);
            }
            FormFile pic = form.getPicture();
            QuestionAction qa = new QuestionAction();
            String picPath = qa.uploadFile(pic, UploadFileType.article_img, errors);
            if (!errors.isEmpty()) {
                return;
            }
            article.setPicture(picPath);
            article.setDescription(form.getDescription());
            article.setMoney(form.getMoney());
            article.setQuantity(form.getQuantity());
            article.setExchangeCount(new Integer(0));
            if (form.getAwardMoney() != null && form.getAwardMoney().length() > 0) {
                article.setAwardMoney(Integer.valueOf(form.getAwardMoney()));
            }
            article.add();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private void getAllArticles(ShopArticleForm form, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            ShopArticle article = new ShopArticle();
            int page = form.getPage();
            int pageSize = form.getPageSize();
            if (pageSize == 0) {
                pageSize = GlobalConfigs.SHOP_ARTICLE_LIST_SIZE;
            }
            Integer recordCount = article.getAllArticlesCount(form.getCategoryId(), page, pageSize);
            List list = article.getAllArticles(form.getCategoryId(), page, pageSize);
            request.setAttribute("articleList", list);
            ShopCategory category = new ShopCategory();
            List categoryList = category.getAvailCategories();
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("categoryId", form.getCategoryId());
            request.setAttribute("page", "" + page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("resultSize", recordCount);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private void getAllArticlesNoZero(ShopArticleForm form, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            ShopArticle article = new ShopArticle();
            int page = form.getPage();
            int pageSize = form.getPageSize();
            if (pageSize == 0) {
                pageSize = GlobalConfigs.SHOP_ARTICLE_LIST_SIZE;
            }
            Integer recordCount = article.getAllArticlesCount(form.getCategoryId(), page, pageSize);
            List list = article.getAllArticlesExchangeList(form.getCategoryId(), page, pageSize);
            request.setAttribute("articleList", list);
            ShopCategory category = new ShopCategory();
            List categoryList = category.getAvailCategories();
            request.setAttribute("categoryList", categoryList);
            request.setAttribute("categoryId", form.getCategoryId());
            request.setAttribute("page", "" + page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("resultSize", recordCount);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private ShopArticle getArticle(ShopArticleForm form, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            ShopArticle article = new ShopArticle();
            article = article.getById(Integer.valueOf(form.getId()));
            ShopCategory category = new ShopCategory();
            List categoryList = category.getAvailCategories();
            request.setAttribute("categoryList", categoryList);
            tx.commit();
            return article;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private void updateArticle(ShopArticleForm form, ActionMessages errors) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            checkArticle(form, errors);
            if (!errors.isEmpty()) {
                return;
            }
            ShopArticle article = new ShopArticle();
            article = article.getById(Integer.valueOf(form.getId()));
            article.setName(form.getName());
            String awardUserName = form.getAwardUserName();
            if (awardUserName != null && awardUserName.length() > 0) {
                UserDAO userDAO = new UserDAO();
                List userList = userDAO.findByLoginName(awardUserName);
                User awardUser = (User) userList.get(0);
                article.setAwardUser(awardUser);
            }
            Integer categoryId = form.getCategoryId();
            if (categoryId != null && categoryId.compareTo(new Integer(0)) > 0) {
                ShopCategoryDAO shopCategoryCAO = new ShopCategoryDAO();
                ShopCategory shopCategory = shopCategoryCAO.findById(categoryId);
                article.setCategory(shopCategory);
            }
            FormFile pic = form.getPicture();
            if (pic != null && pic.getFileSize() > 0) {
                QuestionAction qa = new QuestionAction();
                String picPath = qa.uploadFile(pic, UploadFileType.article_img, errors);
                if (!errors.isEmpty()) {
                    return;
                }
                article.setPicture(picPath);
            }
            article.setDescription(form.getDescription());
            article.setMoney(form.getMoney());
            article.setQuantity(form.getQuantity());
            if (form.getAwardMoney() != null && form.getAwardMoney().length() > 0) {
                article.setAwardMoney(Integer.valueOf(form.getAwardMoney()));
            } else {
                article.setAwardMoney(null);
            }
            article.update();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private boolean delete(ShopArticleForm form, ActionMessages errors, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            String vc = form.getVc();
            if (vc == null || vc.length() < 4) {
                errors.add("vc", new ActionMessage("verificationcode.required"));
                return false;
            }
            String vcInSession = UserSessionUtil.getVerificationCodes(request);
            if (!vcInSession.equals(vc)) {
                errors.add("vc", new ActionMessage("verificationcode.error"));
                return false;
            }
            ShopArticle article = new ShopArticle();
            article = article.getById(Integer.valueOf(form.getId()));
            article.delete();
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private boolean checkCanExchange(ShopArticleForm form, User user, ActionMessages errors) {
        ShopArticle article = new ShopArticle();
        article = article.getById(Integer.valueOf(form.getId()));
        if (article.getQuantity() <= 0) {
            errors.add("", new ActionMessage("article.noquantiy"));
            return false;
        }
        if (user.getUserMoney().getTotalMoney().compareTo(article.getMoney()) < 0) {
            errors.add("", new ActionMessage("article.notenoughmoney", "" + article.getMoney()));
            return false;
        }
        return true;
    }

    private boolean exchange(ShopArticleForm form, User user, ActionMessages errors, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            String vc = form.getVc();
            if (vc == null || vc.length() < 4) {
                errors.add("vc", new ActionMessage("verificationcode.required"));
                return false;
            }
            String vcInSession = UserSessionUtil.getVerificationCodes(request);
            if (!vcInSession.equals(vc)) {
                errors.add("vc", new ActionMessage("verificationcode.error"));
                return false;
            }
            ShopArticle article = new ShopArticle();
            if (!checkCanExchange(form, user, errors)) return false;
            article = article.getById(Integer.valueOf(form.getId()));
            article.setQuantity(article.getQuantity() - 1);
            article.setExchangeCount(article.getExchangeCount() + 1);
            ShopArticleDAO dao = new ShopArticleDAO();
            if (article.getAwardUser() != null) {
                User awardUser = article.getAwardUser();
                dao.spendMoney(awardUser, article.getId(), "捐赠" + article.getName(), "exchangeArticle", SpendMoneyConstants.articleAward, article.getAwardMoney());
            }
            dao.spendMoney(user, article.getId(), article.getName(), "exchangeArticle", SpendMoneyConstants.articleExchange, 0 - article.getMoney());
            ShopArticleHistory history = new ShopArticleHistory();
            history.setArticle(article);
            history.setCompany(form.getCompany());
            history.setDate(new Date());
            history.setEmail(form.getEmail());
            history.setMoney(article.getMoney());
            history.setPostAddr(form.getPostAddr());
            history.setPostCode(form.getPostCode());
            history.setRealName(form.getRealName());
            history.setTelephone(form.getTelephone());
            history.setUser(user);
            history.add();
            Mail mail = generateExchangeMail(article, history, user);
            MailUtil.sendServiceSimpleHtmlMail(mail);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private Mail generateExchangeMail(ShopArticle article, ShopArticleHistory history, User user) {
        Mail mail = new Mail();
        String title = "用户" + user.getLoginName() + "申请积分换礼";
        StringBuffer body = new StringBuffer("用户" + user.getLoginName() + "申请积分换礼<br>");
        body.append("礼品：" + article.getName() + "<br>");
        body.append("真实姓名：" + history.getRealName() + "<br>");
        body.append("联系电话：" + history.getTelephone() + "<br>");
        body.append("公司名称：" + history.getCompany() + "<br>");
        body.append("邮寄地址：" + history.getPostAddr() + "<br>");
        body.append("邮政编码：" + history.getPostCode() + "<br>");
        body.append("电子邮箱：" + history.getEmail() + "<br>");
        mail.setSubject(title);
        mail.setBody(body.toString());
        mail.setFrom("service@mainframer.cn");
        mail.setTo(article.getCategory().getEmail());
        return mail;
    }

    private boolean contribute(ShopArticleForm form, User user, ActionMessages errors, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            String vc = form.getVc();
            if (vc == null || vc.length() < 4) {
                errors.add("vc", new ActionMessage("verificationcode.required"));
                return false;
            }
            String vcInSession = UserSessionUtil.getVerificationCodes(request);
            if (!vcInSession.equals(vc)) {
                errors.add("vc", new ActionMessage("verificationcode.error"));
                return false;
            }
            ShopArticleContribute contribute = new ShopArticleContribute();
            contribute.setArticleName(form.getArticleName());
            contribute.setArticleDescription(form.getArticleDescription());
            contribute.setArticleQuantity(form.getArticleQuantity());
            contribute.setContact(form.getContact());
            contribute.setDate(new Date());
            contribute.setEmail(form.getEmail());
            contribute.setRealName(form.getRealName());
            contribute.setUser(user);
            contribute.add();
            Mail mail = generateContributeMail(contribute);
            MailUtil.sendServiceSimpleHtmlMail(mail);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private Mail generateContributeMail(ShopArticleContribute contribute) {
        Mail mail = new Mail();
        User user = contribute.getUser();
        String title = "用户" + user.getLoginName() + "捐赠礼品";
        StringBuffer body = new StringBuffer("用户" + user.getLoginName() + "捐赠礼品");
        body.append("物品信息<br>");
        body.append("礼品: " + contribute.getArticleName() + "<br>");
        body.append("礼品描述：" + contribute.getArticleDescription() + "<br>");
        body.append("礼品数量：" + contribute.getArticleQuantity() + "<br>");
        body.append("个人信息：<br>");
        body.append("真实姓名：" + contribute.getRealName() + "<br>");
        body.append("联系方式" + contribute.getContact() + "<br>");
        body.append("电子邮箱：" + contribute.getEmail() + "<br>");
        mail.setSubject(title);
        mail.setBody(body.toString());
        mail.setFrom("service@mainframer.cn");
        mail.setTo(GlobalConfigs.mail_address);
        return mail;
    }

    private boolean viewHistory(ShopArticleForm form, HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            ShopArticleHistory history = new ShopArticleHistory();
            int page = form.getPage();
            int pageSize = form.getPageSize();
            if (pageSize == 0) {
                pageSize = GlobalConfigs.SIZE_SHOP_ARTICLE_HISTORY_LIST;
            }
            Integer articleId = Integer.valueOf(form.getId());
            int recordCount = history.getCountByArticleId(articleId);
            List resultList = history.getByArticleId(articleId, page, pageSize);
            request.setAttribute("page", "" + page);
            request.setAttribute("pageSize", pageSize);
            request.setAttribute("resultSize", recordCount);
            request.setAttribute("hisotorylist", resultList);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }

    private boolean exchangeListNoLogin(HttpServletRequest request) throws QaException {
        Transaction tx = null;
        try {
            tx = HibernateSessionFactory.getSession().beginTransaction();
            ShopArticle article = new ShopArticle();
            List list = article.getAllArticlesNoZeroOrderByExchange(1, GlobalConfigs.SHOP_ARTICLE_LIST_SIZE_NAV);
            request.setAttribute("popList", list);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            throw new QaException(e);
        } finally {
            HibernateSessionFactory.closeSession();
        }
    }
}
