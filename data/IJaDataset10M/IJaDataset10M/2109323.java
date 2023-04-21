package com.chimshaw.jblogeditor.wp;

import java.io.ObjectStreamException;
import com.chimshaw.jblogeditor.blogs.AbstractBlogCategory;

/**
 * @author lshah
 *
 */
public class WordPressBlogCategory extends AbstractBlogCategory {

    /**
   * @param id
   * @param name
   * @param url
   */
    public WordPressBlogCategory(String id, String name, String url) {
        super(id, name, url);
    }

    protected Object readResolve() throws ObjectStreamException {
        return super.readResolve();
    }
}
