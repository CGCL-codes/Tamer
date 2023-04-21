package net.sf.jogger.xmlrpc;

import java.util.List;
import java.util.Map;

/**
 * <add class description here>
 * 
 * @author Guillermo Castro
 * @version $Revision: 3 $
 */
public interface BlogAPI {

    public String newPost(String appKey, String blogId, String username, String password, String content, boolean publish) throws BlogException;

    public boolean editPost(String appKey, String postId, String username, String password, String content, boolean publish) throws BlogException;

    public boolean deletePost(String appKey, String postId, String username, String password, boolean publish) throws BlogException;

    public List getRecentPosts(String appKey, String blogId, String username, String password, int numberOfPosts) throws BlogException;

    public List getUsersBlogs(String appKey, String username, String password) throws BlogException;

    public Map getUserInfo(String appKey, String username, String password) throws BlogException;

    public String getTemplate(String appKey, String blogId, String username, String password, String templateType) throws BlogException;

    public boolean setTemplate(String appKey, String blogId, String username, String password, String template, String templateType) throws BlogException;

    public String newPost(String blogId, String username, String password, Map content, boolean publish) throws BlogException;

    public boolean editPost(String postId, String username, String password, Map content, boolean publish) throws BlogException;

    public Map getPost(String postId, String username, String password) throws BlogException;

    public List getRecentPosts(String blogId, String username, String password, int numberOfPosts) throws BlogException;

    public List getCategories(String blogId, String username, String password) throws BlogException;

    public List getCategoryList(String blogId, String username, String password) throws BlogException;

    public List getPostCategories(String postId, String username, String password) throws BlogException;

    public boolean setPostCategories(String postId, String username, String password) throws BlogException;

    public List supportedMethods() throws BlogException;

    public List getTrackbackPings(String postId) throws BlogException;

    public boolean publishPost(String postId, String username, String password) throws BlogException;
}
