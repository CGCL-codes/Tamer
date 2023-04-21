package org.jcr_blog.wtc;

import org.jcr_blog.entities.Blog;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

/**
 *
 * @author Sebastian Prehn
 */
@Name("themeService")
@Scope(ScopeType.STATELESS)
public class ThemeService {

    private static final String DEFAULT_TEMPLATE = "/org/jcr_blog/template/default";

    @In(required = true, create = true)
    private Blog blog;

    public String getTemplate() {
        if (blog.getTemplate() != null && !blog.getTemplate().isEmpty()) {
            return blog.getTemplate();
        }
        return DEFAULT_TEMPLATE;
    }
}
