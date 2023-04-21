package org.jazzteam.snipple.plugin.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.util.List;
import org.jazzteam.snipple.plugin.exceptions.localstorage.LocalStorageException;
import org.jazzteam.snipple.plugin.model.Category;
import org.jazzteam.snipple.plugin.model.Snippet;
import org.jazzteam.snipple.plugin.storage.local.FileSystemLocalStorage;
import org.jazzteam.snipple.plugin.storage.local.ILocalStorage;
import org.junit.Test;

public class WorkWithXMLTest {

    @Test
    public void testAdding() throws LocalStorageException {
        File testRootDirectory = new File("E://test");
        testRootDirectory.mkdir();
        ILocalStorage storage = new FileSystemLocalStorage("E://test");
        Category cath = new Category();
        cath.setName("aaa");
        storage.addCategory(cath);
        List<Snippet> snippetList = null;
        Snippet snip = new Snippet();
        snip.setName("test");
        storage.addSnippet(cath, snip);
        snip.setName("new");
        storage.addSnippet(cath, snip);
        snippetList = storage.getSnippets(cath);
        assertTrue(snippetList.size() == 2);
        assertEquals("test", storage.getSnippets(cath).get(1).getName());
        assertEquals("new", storage.getSnippets(cath).get(0).getName());
        snip.setName("new");
        storage.removeSnippet(cath, snip);
        snippetList = storage.getSnippets(cath);
        assertTrue(snippetList.size() == 1);
        assertEquals("test", storage.getSnippets(cath).get(0).getName());
        snip.setName("test");
        storage.removeSnippet(cath, snip);
        snippetList = storage.getSnippets(cath);
        assertTrue(snippetList.size() == 0);
        storage.removeCategory(cath);
        testRootDirectory.delete();
    }
}
