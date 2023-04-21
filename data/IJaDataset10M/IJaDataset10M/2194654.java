package org.jazzteam.snipple.plugin.storage.local;

import java.util.ArrayList;
import java.util.List;
import org.jazzteam.snipple.plugin.model.Category;
import org.jazzteam.snipple.plugin.model.Snippet;

/**
 * Stores snippets, loaded from local storage in memory
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public class LocalSnippetsMemoryStorage {

    private static LocalSnippetsMemoryStorage instance;

    private List<Category> categories = new ArrayList<Category>();

    public static LocalSnippetsMemoryStorage getInstance() {
        if (instance == null) {
            instance = new LocalSnippetsMemoryStorage();
        }
        return instance;
    }

    private LocalSnippetsMemoryStorage() {
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public void addSnippet(Category category, Snippet snippet) {
        category.addChild(snippet);
        snippet.setParent(category);
    }

    public void removeSnippet(Snippet snippet) {
        if (snippet.getParent() != null) {
            snippet.getParent().getChildren().remove(snippet);
            snippet.setParent(null);
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
