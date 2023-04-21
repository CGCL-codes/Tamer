package org.authorsite.bib.loader.ris;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import org.authorsite.bib.AbstractHuman;
import org.authorsite.bib.Article;
import org.authorsite.bib.Individual;
import org.authorsite.bib.Journal;
import org.authorsite.bib.WorkDates;
import junit.framework.TestCase;

public class ArticleHandlerTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        Bibliography.getInstance().clearIndividuals();
        Bibliography.getInstance().clearCollectives();
        Bibliography.getInstance().clearJournals();
        Bibliography.getInstance().clearArticles();
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        Bibliography.getInstance().clearIndividuals();
        Bibliography.getInstance().clearCollectives();
        Bibliography.getInstance().clearJournals();
        Bibliography.getInstance().clearArticles();
        super.tearDown();
    }

    public void testSingleArticle() throws Exception {
        RISEntry entry = new RISEntry();
        entry.addEntryLine(new RISEntryLine("TY", "JOUR"));
        entry.addEntryLine(new RISEntryLine("A1", "King, John"));
        entry.addEntryLine(new RISEntryLine("A1", "Bar, Foo"));
        entry.addEntryLine(new RISEntryLine("T1", "Foo and Baa: The Sheep Connection"));
        entry.addEntryLine(new RISEntryLine("JO", "Journal of Foo Studies"));
        entry.addEntryLine(new RISEntryLine("Y1", "1999"));
        entry.addEntryLine(new RISEntryLine("VL", "V"));
        entry.addEntryLine(new RISEntryLine("IS", "2"));
        entry.addEntryLine(new RISEntryLine("SP", "192-223"));
        Journal j = new Journal();
        j.setTitle("Journal of Foo Studies");
        Individual jk = new Individual();
        jk.setName("King");
        jk.setGivenNames("John");
        Individual fb = new Individual();
        fb.setName("Bar");
        fb.setGivenNames("Foo");
        Article a = new Article();
        a.setTitle("Foo and Baa: The Sheep Connection");
        a.setYears(new WorkDates(1999));
        a.setJournal(j);
        a.setVolume("V");
        a.setIssue("2");
        a.setPages("192-223");
        a.addAuthor(jk);
        a.addAuthor(fb);
        ArticleHandler handler = new ArticleHandler();
        handler.handleEntry(entry);
        assertTrue(Bibliography.getInstance().getIndividuals().containsKey(jk));
        assertTrue(Bibliography.getInstance().getIndividuals().containsKey(fb));
        assertTrue(Bibliography.getInstance().getJournals().containsKey(j));
        assertTrue(Bibliography.getInstance().getArticles().containsKey(a));
        Article aA = Bibliography.getInstance().getAuthoritativeArticle(a);
        assertNotSame(a, aA);
        assertTrue(aA.getId() > 0);
        Set<AbstractHuman> authors = aA.getAuthors();
        assertEquals(2, authors.size());
        assertTrue(authors.contains(jk));
        assertTrue(authors.contains(fb));
        for (AbstractHuman author : authors) {
            assertTrue(author.getId() > 0);
            if (author.equals(jk)) {
                assertNotSame(author, jk);
            } else if (author.equals(fb)) {
                assertNotSame(author, fb);
            }
        }
        Journal aJ = aA.getJournal();
        assertEquals(aJ, Bibliography.getInstance().getAuthoritativeJournal(j));
        assertTrue(aJ.getId() > 0);
        assertTrue(aJ.getId() != aA.getId());
        assertNotSame(j, aJ);
    }

    public void testSingleArticleWithEP() throws Exception {
        RISEntry entry = new RISEntry();
        entry.addEntryLine(new RISEntryLine("TY", "JOUR"));
        entry.addEntryLine(new RISEntryLine("A1", "King, John"));
        entry.addEntryLine(new RISEntryLine("A1", "Bar, Foo"));
        entry.addEntryLine(new RISEntryLine("T1", "Foo and Baa: The Sheep Connection"));
        entry.addEntryLine(new RISEntryLine("JO", "Journal of Foo Studies"));
        entry.addEntryLine(new RISEntryLine("Y1", "1999"));
        entry.addEntryLine(new RISEntryLine("VL", "V"));
        entry.addEntryLine(new RISEntryLine("IS", "2"));
        entry.addEntryLine(new RISEntryLine("SP", "192"));
        entry.addEntryLine(new RISEntryLine("EP", "223"));
        Journal j = new Journal();
        j.setTitle("Journal of Foo Studies");
        Individual jk = new Individual();
        jk.setName("King");
        jk.setGivenNames("John");
        Individual fb = new Individual();
        fb.setName("Bar");
        fb.setGivenNames("Foo");
        Article a = new Article();
        a.setTitle("Foo and Baa: The Sheep Connection");
        a.setYears(new WorkDates(1999));
        a.setJournal(j);
        a.setVolume("V");
        a.setIssue("2");
        a.setPages("192-223");
        a.addAuthor(jk);
        a.addAuthor(fb);
        Article aA = Bibliography.getInstance().getAuthoritativeArticle(a);
        assertEquals(a.getPages(), aA.getPages());
    }

    public void testMultipleArticlesSameJournalSomeSameAuthors() throws Exception {
        RISEntry entry = new RISEntry();
        entry.addEntryLine(new RISEntryLine("TY", "JOUR"));
        entry.addEntryLine(new RISEntryLine("A1", "King, John"));
        entry.addEntryLine(new RISEntryLine("A1", "Bar, Foo"));
        entry.addEntryLine(new RISEntryLine("T1", "Foo and Baa: The Sheep Connection"));
        entry.addEntryLine(new RISEntryLine("JO", "Journal of Foo Studies"));
        entry.addEntryLine(new RISEntryLine("Y1", "1999"));
        entry.addEntryLine(new RISEntryLine("VL", "V"));
        entry.addEntryLine(new RISEntryLine("IS", "2"));
        entry.addEntryLine(new RISEntryLine("SP", "192-223"));
        RISEntry entry2 = new RISEntry();
        entry2.addEntryLine(new RISEntryLine("TY", "JOUR"));
        entry2.addEntryLine(new RISEntryLine("A1", "Bar, Foo"));
        entry2.addEntryLine(new RISEntryLine("A1", "Wibble, Wobble"));
        entry2.addEntryLine(new RISEntryLine("T1", "Wibbling and Wobbling"));
        entry2.addEntryLine(new RISEntryLine("JO", "Journal of Foo Studies"));
        entry2.addEntryLine(new RISEntryLine("Y1", "2000"));
        entry2.addEntryLine(new RISEntryLine("VL", "VII"));
        entry2.addEntryLine(new RISEntryLine("IS", "3"));
        entry2.addEntryLine(new RISEntryLine("SP", "334-340"));
        Journal j = new Journal();
        j.setTitle("Journal of Foo Studies");
        Individual jk = new Individual();
        jk.setName("King");
        jk.setGivenNames("John");
        Individual fb = new Individual();
        fb.setName("Bar");
        fb.setGivenNames("Foo");
        Individual ww = new Individual();
        ww.setName("Wibble");
        ww.setGivenNames("Wobble");
        Article a1 = new Article();
        a1.setTitle("Foo and Baa: The Sheep Connection");
        a1.setYears(new WorkDates(1999));
        a1.setJournal(j);
        a1.setVolume("V");
        a1.setIssue("2");
        a1.setPages("192-223");
        a1.addAuthor(jk);
        a1.addAuthor(fb);
        Article a2 = new Article();
        a2.setTitle("Wibbling and Wobbling");
        a2.setYears(new WorkDates(2000));
        a2.setJournal(j);
        a2.setVolume("VII");
        a2.setIssue("3");
        a2.setPages("334-340");
        a2.addAuthor(fb);
        a2.addAuthor(ww);
        ArticleHandler handler = new ArticleHandler();
        handler.handleEntry(entry);
        handler.handleEntry(entry2);
        assertTrue(Bibliography.getInstance().getIndividuals().containsKey(jk));
        assertTrue(Bibliography.getInstance().getIndividuals().containsKey(fb));
        assertTrue(Bibliography.getInstance().getIndividuals().containsKey(ww));
        assertTrue(Bibliography.getInstance().getArticles().containsKey(a1));
        assertTrue(Bibliography.getInstance().getArticles().containsKey(a2));
        Article aA1 = Bibliography.getInstance().getAuthoritativeArticle(a1);
        Article aA2 = Bibliography.getInstance().getAuthoritativeArticle(a2);
        assertSame(aA1.getJournal(), aA2.getJournal());
        Individual aA2fb = null;
        Individual aA1fb = null;
        for (AbstractHuman author : aA1.getAuthors()) {
            if (author.equals(fb)) {
                aA1fb = (Individual) author;
            }
        }
        for (AbstractHuman author : aA2.getAuthors()) {
            if (author.equals(fb)) {
                aA2fb = (Individual) author;
            }
        }
        assertSame(aA2fb, aA1fb);
    }
}
