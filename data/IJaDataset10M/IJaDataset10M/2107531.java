package org.jcr_blog.persistence.entities;

import org.jcr_blog.domain.Comment;

/**
 *
 * @author Sebastian Prehn <sebastian.prehn@planetswebdesign.de>
 */
public class CommentInternal extends Comment implements JcrEntity {

    private static final long serialVersionUID = -519110044635293659L;

    private String path;

    @Override
    public void setPath(final String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
