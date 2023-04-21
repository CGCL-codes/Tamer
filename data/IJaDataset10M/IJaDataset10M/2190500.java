package org.deft.repository.junit.project;

import java.io.File;
import java.util.List;
import org.deft.repository.IRepository;
import org.deft.repository.Util;
import org.deft.repository.exception.DeftCrossProjectRelationException;
import org.deft.repository.exception.DeftFragmentAlreadyExistsException;
import org.deft.repository.fragment.Category;
import org.deft.repository.fragment.Chapter;
import org.deft.repository.fragment.CodeFile;
import org.deft.repository.fragment.CodeSnippet;
import org.deft.repository.fragment.Project;
import org.deft.repository.options.XfsrOptionManager;
import org.deft.repository.query.Query;
import org.deft.repository.xfsr.XmlFileSystemEclipsePluginRepository;
import org.deft.repository.xfsr.reference.CodeSnippetRef;
import org.deft.repository.xfsr.reference.CodeSnippetRefCollector;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CodeSnippetReferencesTest {

    private IRepository repository;

    private static final IPath repositoryPath = new Path("testdata/Repository/repositorydir");

    private static final IPath filesDir = new Path("testdata/Repository/csharpfiles");

    private static final String PARSER = "AntlrCSharpParser";

    private CodeSnippetRefCollector refs;

    private Project project, project1, project2;

    private Category cfCat, cfCat1, cfCat2;

    private Chapter chapter, chapter1, chapter2, chapter3;

    private CodeSnippet codeSnippet, codeSnippet1, codeSnippet2, codeSnippet3;

    private CodeFile codeFile, codeFile1, codeFile2, codeFile3;

    @BeforeClass
    public static void beforeClass() {
        File loc = repositoryPath.toFile();
        for (String s : loc.list()) {
            deleteDirectory(new File(loc.getAbsolutePath() + File.separator + s));
        }
        XmlFileSystemEclipsePluginRepository.setInstanceNull();
    }

    @Before
    public void before() {
        try {
            repository = XmlFileSystemEclipsePluginRepository.getInstance();
            XfsrOptionManager options = (XfsrOptionManager) repository.getRepositoryOptions();
            options.setRepositoryPath(repositoryPath.toOSString());
            project = repository.createNewProject("projname");
            cfCat = project.getCategory(Category.SubType.CODE_FILES);
            project1 = repository.createNewProject("projname1");
            cfCat1 = project1.getCategory(Category.SubType.CODE_FILES);
            project2 = repository.createNewProject("projname2");
            cfCat2 = project2.getCategory(Category.SubType.CODE_FILES);
            refs = new CodeSnippetRefCollector();
            chapter = repository.createNewChapter(project, "chaptername");
            chapter1 = repository.createNewChapter(project, "chaptername1");
            chapter2 = repository.createNewChapter(project, "chaptername2");
            chapter3 = repository.createNewChapter(project, "chaptername3");
            String programFilePath = filesDir.append("Program.cs").toOSString();
            codeFile = repository.importCodeFile(cfCat, programFilePath, "Program.cs", PARSER);
            codeFile1 = repository.importCodeFile(cfCat, programFilePath, "Program1.cs", PARSER);
            codeFile2 = repository.importCodeFile(cfCat, programFilePath, "Program2.cs", PARSER);
            codeFile3 = repository.importCodeFile(cfCat, programFilePath, "Program3.cs", PARSER);
            codeSnippet = repository.createCodeSnippet(codeFile, "bla", createQuery("//query"));
            codeSnippet1 = repository.createCodeSnippet(codeFile, "bla", createQuery("//query1"));
            codeSnippet2 = repository.createCodeSnippet(codeFile, "bla", createQuery("//query2"));
            codeSnippet3 = repository.createCodeSnippet(codeFile, "bla", createQuery("//query3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        File loc = repositoryPath.toFile();
        for (String s : loc.list()) {
            deleteDirectory(new File(loc.getAbsolutePath() + File.separator + s));
        }
        XmlFileSystemEclipsePluginRepository.setInstanceNull();
    }

    private static void deleteDirectory(File path) {
        if (path.exists() && path.isDirectory()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    private Query createQuery(String queryString) {
        Document doc = Util.makeDocument();
        Element query = doc.createElement("query");
        Element codeQuery = doc.createElement("codequery");
        codeQuery.setAttribute("target", queryString);
        query.appendChild(codeQuery);
        doc.appendChild(query);
        return new Query(doc);
    }

    @Test
    public void testGetCodeSnippetReference() {
        CodeSnippetRef ref = (CodeSnippetRef) refs.addFragmentToChapter(chapter, codeSnippet);
        Assert.assertEquals(ref, refs.getReference(ref.getRefId()));
    }

    @Test
    public void testGetCodeSnippetReferencesFromChapter() {
        CodeSnippetRef ref11 = refs.addFragmentToChapter(chapter1, codeSnippet1);
        CodeSnippetRef ref12 = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref21 = refs.addFragmentToChapter(chapter2, codeSnippet1);
        List<CodeSnippetRef> list1 = refs.getReferencesFromChapter(chapter1);
        List<CodeSnippetRef> list2 = refs.getReferencesFromChapter(chapter2);
        List<CodeSnippetRef> list3 = refs.getReferencesFromChapter(chapter3);
        Assert.assertEquals(list1.size(), 2);
        Assert.assertEquals(list2.size(), 1);
        Assert.assertEquals(list3.size(), 0);
        Assert.assertTrue(list1.contains(ref11));
        Assert.assertTrue(list1.contains(ref12));
        Assert.assertTrue(list2.contains(ref21));
    }

    @Test
    public void testGetReferencesFromCodeSnippet() {
        CodeSnippetRef ref11 = refs.addFragmentToChapter(chapter1, codeSnippet1);
        CodeSnippetRef ref12 = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref21 = refs.addFragmentToChapter(chapter2, codeSnippet1);
        List<CodeSnippetRef> list1 = refs.getReferencesFromFragment(codeSnippet1);
        List<CodeSnippetRef> list2 = refs.getReferencesFromFragment(codeSnippet2);
        List<CodeSnippetRef> list3 = refs.getReferencesFromFragment(codeSnippet3);
        Assert.assertEquals(list1.size(), 2);
        Assert.assertEquals(list2.size(), 1);
        Assert.assertEquals(list3.size(), 0);
        Assert.assertTrue(list1.contains(ref11));
        Assert.assertTrue(list1.contains(ref21));
        Assert.assertTrue(list2.contains(ref12));
    }

    @Test
    public void testGetCodeSnippetReferencesFromCodeFile() throws DeftCrossProjectRelationException {
        repository.removeFragment(codeSnippet1);
        repository.removeFragment(codeSnippet2);
        repository.removeFragment(codeSnippet3);
        codeSnippet1 = repository.createCodeSnippet(codeFile1, "blubb", createQuery("//query1"));
        codeSnippet2 = repository.createCodeSnippet(codeFile2, "blubb", createQuery("//query2"));
        codeSnippet3 = repository.createCodeSnippet(codeFile3, "blubb", createQuery("//query3"));
        CodeSnippetRef ref11 = refs.addFragmentToChapter(chapter1, codeSnippet1);
        CodeSnippetRef ref12 = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref21 = refs.addFragmentToChapter(chapter2, codeSnippet1);
        CodeSnippetRef ref31 = refs.addFragmentToChapter(chapter3, codeSnippet1);
        List<CodeSnippetRef> list1 = refs.getReferencesFromCodeFile(codeFile1);
        List<CodeSnippetRef> list2 = refs.getReferencesFromCodeFile(codeFile2);
        List<CodeSnippetRef> list3 = refs.getReferencesFromCodeFile(codeFile3);
        Assert.assertEquals(list1.size(), 3);
        Assert.assertEquals(list2.size(), 1);
        Assert.assertEquals(list3.size(), 0);
        Assert.assertTrue(list1.contains(ref11));
        Assert.assertTrue(list2.contains(ref12));
        Assert.assertTrue(list1.contains(ref21));
        Assert.assertTrue(list1.contains(ref31));
    }

    @Test
    public void testGetSharedReferences() {
        CodeSnippetRef ref11 = refs.addFragmentToChapter(chapter1, codeSnippet1);
        CodeSnippetRef ref12a = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref12b = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref13 = refs.addFragmentToChapter(chapter1, codeSnippet3);
        CodeSnippetRef ref22 = refs.addFragmentToChapter(chapter2, codeSnippet2);
        CodeSnippetRef ref23 = refs.addFragmentToChapter(chapter3, codeSnippet2);
        List<CodeSnippetRef> list = refs.getSharedReferences(chapter1, codeSnippet2);
        Assert.assertEquals(list.size(), 2);
        Assert.assertTrue(list.contains(ref12a));
        Assert.assertTrue(list.contains(ref12b));
    }

    @Test
    public void testRemoveCodeSnippetFromChapter() {
        CodeSnippetRef ref11 = refs.addFragmentToChapter(chapter1, codeSnippet1);
        CodeSnippetRef ref12a = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref12b = refs.addFragmentToChapter(chapter1, codeSnippet2);
        CodeSnippetRef ref13 = refs.addFragmentToChapter(chapter1, codeSnippet3);
        CodeSnippetRef ref22 = refs.addFragmentToChapter(chapter2, codeSnippet2);
        CodeSnippetRef ref23 = refs.addFragmentToChapter(chapter3, codeSnippet2);
        refs.removeFragmentFromChapter(chapter1, codeSnippet2);
        Assert.assertNotNull(refs.getReference(ref11.getRefId()));
        Assert.assertNull(refs.getReference(ref12a.getRefId()));
        Assert.assertNull(refs.getReference(ref12b.getRefId()));
        Assert.assertNotNull(refs.getReference(ref13.getRefId()));
        Assert.assertNotNull(refs.getReference(ref22.getRefId()));
        Assert.assertNotNull(refs.getReference(ref23.getRefId()));
        Assert.assertEquals(refs.getReferencesFromChapter(chapter1).size(), 2);
        Assert.assertEquals(refs.getReferencesFromChapter(chapter2).size(), 1);
        Assert.assertEquals(refs.getReferencesFromChapter(chapter3).size(), 1);
        Assert.assertEquals(refs.getReferencesFromFragment(codeSnippet2).size(), 2);
        Assert.assertEquals(refs.getReferencesFromFragment(codeSnippet1).size(), 1);
        Assert.assertEquals(refs.getReferencesFromFragment(codeSnippet3).size(), 1);
    }

    @Test
    public void testDifferentProjectRefs() throws DeftFragmentAlreadyExistsException, DeftCrossProjectRelationException {
        Chapter c1 = repository.createNewChapter(project, "c");
        Chapter c2 = repository.createNewChapter(project2, "c");
        CodeSnippetRef ref = repository.addCodeSnippetToChapter(c1, codeSnippet);
        repository.commitChapterChanges(c1);
        Assert.assertEquals(repository.getReference(ref.getRefId()), ref);
        Assert.assertFalse(repository.getCodeSnippetReferences(c1).isEmpty());
        Assert.assertTrue(repository.getCodeSnippetReferences(c2).isEmpty());
    }
}
