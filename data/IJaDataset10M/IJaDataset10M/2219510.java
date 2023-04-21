package org.jazzteam.snipple.action.snippet;

import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.jazzteam.snipple.action.Action;
import org.jazzteam.snipple.data.FavoriteDataService;
import org.jazzteam.snipple.data.SnippetContentDataService;
import org.jazzteam.snipple.data.SnippetDataService;
import org.jazzteam.snipple.model.Snippet;
import org.jazzteam.snipple.model.User;

public class GetSnippetAction extends Action implements ServletRequestAware, ServletResponseAware {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1387779962274037543L;

    private Snippet snippet;

    private SnippetDataService snippetDataService;

    private SnippetContentDataService snippetContentDataService;

    private FavoriteDataService favoriteDataService;

    private Long id;

    private Map<String, String> cookiesMap;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private String votedSnippetID;

    private boolean added;

    private boolean owner;

    public GetSnippetAction(SnippetDataService snippetDataService, SnippetContentDataService snippetContentDataService, FavoriteDataService favoriteDataService) {
        this.snippetDataService = snippetDataService;
        this.snippetContentDataService = snippetContentDataService;
        this.favoriteDataService = favoriteDataService;
    }

    public String index() {
        votedSnippetID = getSnippetIdFromCookies(id);
        User user = (User) session.get("user");
        if (id != null) {
            snippet = snippetDataService.find(id);
        }
        if (userNotNull(user)) {
            added = isAlreadyAddedToFavorites(id, user);
            owner = isOwnerOfCurrentSnippet(snippet, user);
        }
        return SUCCESS;
    }

    public String getSnippetIdFromCookies(Long id) {
        for (Cookie c : request.getCookies()) {
            if (c.getName().equalsIgnoreCase("snippetId" + id)) {
                return c.getValue();
            }
        }
        return null;
    }

    public boolean isAlreadyAddedToFavorites(Long id, User user) {
        return favoriteDataService.findBySnippetIdAndUserId(id, user.getId()) != null;
    }

    public boolean isOwnerOfCurrentSnippet(Snippet snippet, User user) {
        return snippet.getUser().getLogin().equalsIgnoreCase(user.getLogin());
    }

    public boolean userNotNull(User user) {
        return user != null;
    }

    public Snippet getSnippet() {
        return snippet;
    }

    public void setSnippet(Snippet snippet) {
        this.snippet = snippet;
    }

    public SnippetDataService getSnippetDataService() {
        return snippetDataService;
    }

    public void setSnippetDataService(SnippetDataService snippetDataService) {
        this.snippetDataService = snippetDataService;
    }

    public SnippetContentDataService getSnippetContentDataService() {
        return snippetContentDataService;
    }

    public void setSnippetContentDataService(SnippetContentDataService snippetContentDataService) {
        this.snippetContentDataService = snippetContentDataService;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getVotedSnippetID() {
        return votedSnippetID;
    }

    public void setVotedSnippetID(String votedSnippetID) {
        this.votedSnippetID = votedSnippetID;
    }

    public Map<String, String> getCookiesMap() {
        return cookiesMap;
    }

    public void setCookiesMap(Map<String, String> cookiesMap) {
        this.cookiesMap = cookiesMap;
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }
}
