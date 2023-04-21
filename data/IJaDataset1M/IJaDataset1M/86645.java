package com.javaeedev.web.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.javaeedev.domain.Article;
import com.javaeedev.domain.ArticleCategory;
import com.javaeedev.domain.Board;
import com.javaeedev.domain.BoardCategory;
import com.javaeedev.domain.Resource;
import com.javaeedev.domain.ResourceCategory;
import com.javaeedev.domain.Tag;
import com.javaeedev.domain.User;
import com.javaeedev.domain.search.ResourceItem;
import com.javaeedev.facade.impl.SearchFacadeImpl;
import com.javaeedev.util.HttpUtil;
import com.javaeedev.web.AbstractAdminController;

/**
 * Update objects, such as user.lock, etc.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/admin/update.jspx"
 */
public class UpdateController extends AbstractAdminController {

    @Override
    protected void afterComplete(HttpServletRequest request, HttpServletResponse response) {
        searchFacade.reindex(request);
    }

    public String doUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean lock = HttpUtil.getBoolean(request, "lock");
        int pageIndex = HttpUtil.getInt(request, "page", 1);
        User user = facade.queryUser(HttpUtil.getString(request, "username"));
        if (lock) {
            int lockDays = HttpUtil.getInt(request, "lockDays");
            facade.updateLock(user, lockDays);
        } else {
            facade.updateUnlock(user);
        }
        return "/admin/list.jspx?object=User&page=" + pageIndex;
    }

    public String doTag(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        String description = HttpUtil.getString(request, "description", "");
        Tag tag = facade.queryTag(id);
        tag.setDescription(description);
        facade.updateTag(tag);
        return "/admin/list.jspx?object=Tag";
    }

    public String doBoard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        String boardCategoryId = HttpUtil.getString(request, "boardCategoryId");
        String description = HttpUtil.getString(request, "description", "");
        int displayOrder = HttpUtil.getInt(request, "displayOrder");
        String title = HttpUtil.getString(request, "title");
        BoardCategory boardCategory = facade.queryBoardCategory(boardCategoryId);
        Board board = facade.queryBoard(id);
        board.setBoardCategory(boardCategory);
        board.setDescription(description);
        board.setDisplayOrder(displayOrder);
        board.setTitle(title);
        facade.updateBoard(board);
        return "/admin/list.jspx?object=Board";
    }

    public String doBoardCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        String description = HttpUtil.getString(request, "description", "");
        int displayOrder = HttpUtil.getInt(request, "displayOrder", 0);
        String title = HttpUtil.getString(request, "title");
        BoardCategory boardCategory = facade.queryBoardCategory(id);
        boardCategory.setDescription(description);
        boardCategory.setDisplayOrder(displayOrder);
        boardCategory.setTitle(title);
        facade.updateBoardCategory(boardCategory);
        return "/admin/list.jspx?object=BoardCategory";
    }

    public String doArticleCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        String description = HttpUtil.getString(request, "description", "");
        int displayOrder = HttpUtil.getInt(request, "displayOrder", 0);
        String title = HttpUtil.getString(request, "title");
        ArticleCategory articleCategory = facade.queryArticleCategory(id);
        articleCategory.setDescription(description);
        articleCategory.setDisplayOrder(displayOrder);
        articleCategory.setTitle(title);
        facade.updateArticleCategory(articleCategory);
        return "/admin/list.jspx?object=ArticleCategory";
    }

    public String doArticleDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String articleId = HttpUtil.getString(request, "articleId");
        String[] ids = request.getParameterValues("categoryIds");
        Set<String> set = new HashSet<String>();
        if (ids != null) {
            for (String id : ids) {
                set.add(id);
            }
        }
        String[] tagTitles = request.getParameterValues("tags");
        Set<String> tagSet = new HashSet<String>();
        if (tagTitles != null) {
            for (String t : tagTitles) {
                tagSet.add(t.trim().toLowerCase());
            }
        }
        List<String> tags = new ArrayList<String>(tagSet.size());
        tags.addAll(tagSet);
        Article article = facade.queryArticle(articleId);
        List<ArticleCategory> categories = facade.queryArticleCategories();
        List<ArticleCategory> selected = new ArrayList<ArticleCategory>(categories.size());
        for (ArticleCategory c : categories) {
            if (set.contains(c.getId())) selected.add(c);
        }
        facade.updateArticleAndCategory(article, selected);
        facade.updateTagsForArticle(article, tags);
        return "/admin/list.jspx?object=ArticleDetail&articleId=" + articleId;
    }

    public String doResourceCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        String description = HttpUtil.getString(request, "description", "");
        int displayOrder = HttpUtil.getInt(request, "displayOrder", 0);
        String title = HttpUtil.getString(request, "title");
        ResourceCategory resourceCategory = facade.queryResourceCategory(id);
        resourceCategory.setDescription(description);
        resourceCategory.setDisplayOrder(displayOrder);
        resourceCategory.setTitle(title);
        facade.updateResourceCategory(resourceCategory);
        return "/admin/list.jspx?object=ResourceCategory";
    }

    public String doOneResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String resourceId = HttpUtil.getString(request, "resourceId");
        Resource tmp = (Resource) HttpUtil.createFormBean(request, Resource.class);
        String[] ids = request.getParameterValues("categoryIds");
        Set<String> set = new HashSet<String>();
        if (ids != null) {
            for (String id : ids) {
                set.add(id);
            }
        }
        Resource resource = facade.queryResource(resourceId);
        resource.setContent(tmp.getContent());
        resource.setDescription(tmp.getDescription());
        resource.setTitle(tmp.getTitle());
        resource.setLicense(tmp.getLicense());
        resource.setUrl(tmp.getUrl());
        resource.setUpdatedDate(System.currentTimeMillis());
        List<ResourceCategory> categories = facade.queryResourceCategories();
        List<ResourceCategory> selected = new ArrayList<ResourceCategory>(categories.size());
        for (ResourceCategory c : categories) {
            if (set.contains(c.getId())) selected.add(c);
        }
        facade.updateResource(resource);
        facade.updateResourceAndCategory(resource, selected);
        request.setAttribute(SearchFacadeImpl.INDEX_OBJECT, new ResourceItem(resource));
        return "/admin/list.jspx?object=Resource";
    }
}
