package com.aimluck.eip.common;

import javax.servlet.http.HttpServletRequest;
import com.aimluck.eip.http.HttpServletRequestLocator;

/**
 * 会社情報、部署情報、役職情報をメモリ上に保持するクラスです。 <br />
 * 
 */
public class ALBlogManager extends ALEipManager {

    /** Singleton */
    private static ALBlogManager manager = new ALBlogManager();

    /** BlogResultDataキー */
    private static String BLOG_USER_LIST_KEY = "com.aimluck.eip.blog.BlogEntryLatestSelectData.userDataList";

    /**
   * 
   * @return
   */
    public static ALBlogManager getInstance() {
        return manager;
    }

    public Object getUserDataList() {
        HttpServletRequest request = HttpServletRequestLocator.get();
        if (request != null) {
            Object obj = request.getAttribute(BLOG_USER_LIST_KEY);
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    public void setUserDataList(Object obj) {
        HttpServletRequest request = HttpServletRequestLocator.get();
        if (request != null) {
            request.setAttribute(BLOG_USER_LIST_KEY, obj);
        }
    }
}
