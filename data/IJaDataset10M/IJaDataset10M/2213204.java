package org.poxd.persistance;

import org.apache.log4j.Logger;
import org.poxd.model.Category;
import org.poxd.model.Snippet;

/**
 * @author pollux
 * 
 */
public class SnippetDAO extends AbstractDAO {

    private static final Logger log = Logger.getLogger(SnippetDAO.class);

    public void addSnippet(Category category, Snippet snippet) {
        startOperation();
        Category cat = (Category) session.get(Category.class, category.getId());
        if (cat.getSnippets().contains(snippet)) {
            log.debug("EDIT SNIPPET " + snippet.desc());
            session.clear();
            cat.getSnippets().set(cat.getSnippets().indexOf(snippet), snippet);
            session.update(cat);
        } else {
            log.debug("ADD SNIPPET " + snippet.desc() + " TO CATEGORY :" + category);
            cat.getSnippets().add(snippet);
            session.save(cat);
        }
        endOperation();
    }

    public void deleteSnippet(Snippet source) {
        startOperation();
        session.delete(source);
        endOperation();
    }
}
