package org.jazzteam.snipple.plugin.storage.local;

import java.util.List;
import org.jazzteam.snipple.plugin.exceptions.localstorage.LocalStorageException;
import org.jazzteam.snipple.plugin.model.Category;
import org.jazzteam.snipple.plugin.model.Snippet;

/**
 * Local storage interface
 * 
 * 
 * @author Константин
 * @version $Rev: $
 */
public interface ILocalStorage {

    List<Category> getCategories() throws LocalStorageException;

    List<Snippet> getSnippets(Category category) throws LocalStorageException;

    void addCategory(Category category) throws LocalStorageException;

    void removeCategory(Category category) throws LocalStorageException;

    void addSnippet(Category category, Snippet snippet) throws LocalStorageException;

    void removeSnippet(Category category, Snippet snippet) throws LocalStorageException;
}
