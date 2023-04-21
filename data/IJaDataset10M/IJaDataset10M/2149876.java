package uk.org.sgj.OHCApparatus.Records;

import uk.org.sgj.OHCApparatus.*;
import java.util.*;

public abstract class OHCBookDictionaryArticleRecord extends OHCBasicRecord implements OHCDerivedRecord {

    protected static int recordIndex;

    BiblioEncyclopediaClass biblioClass;

    FirstCiteEncyclopediaClass firstCiteClass;

    SubsRefsEncyclopediaClass subsRefClass;

    protected String volume, pages;

    DictionaryData parentData;

    public OHCBookDictionaryArticleRecord() {
        super();
        this.primaryTitleFieldDescription = FUN.articleTitle;
        recordFieldData.makeNewField(FUN.authorName);
        recordFieldData.makeNewField(FUN.articleTitle);
        recordFieldData.makeNewField(FUN.subsArticleTitle);
        recordFieldData.makeNewField(FUN.articleVol);
        recordFieldData.makeNewField(FUN.pages);
        biblioClass = new BiblioEncyclopediaClass();
        firstCiteClass = new FirstCiteEncyclopediaClass();
        subsRefClass = new SubsRefsEncyclopediaClass();
    }

    @Override
    protected boolean validateRecord() {
        boolean ok = super.validateRecord();
        String details = "";
        if (ok) {
            if (ok) {
                if (authorName.isEmpty()) {
                    ok = false;
                    details = new String("Please specify the author of this article.");
                }
            }
            if (ok) {
                if (articleTitle.isEmpty()) {
                    ok = false;
                    details = new String("Please specify the title of the article.");
                }
            }
            if (ok) {
                if (pages.isEmpty()) {
                    ok = false;
                    details = new String("Please specify the pages which the article occupies in this anthology or in the specified volume of the anthology.");
                }
            }
            if (ok) {
                if (articleTitle.isEmpty()) {
                    ok = false;
                    details = new String("You must supply the title of the article.");
                }
            }
            if (ok) {
                if (subsArticleTitle.isEmpty()) {
                    ok = false;
                    details = new String("Please supply a title for this article to be used in any citations after the first one.\nThis may be a shortened form of the title, or identical to it.");
                }
            }
            if (!ok) {
                validationErrorText = details;
            }
        }
        return (ok);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        articleTitle = recordFieldData.getDataValue(FUN.articleTitle);
        primaryTitle = articleTitle;
        subsArticleTitle = recordFieldData.getDataValue(FUN.subsArticleTitle);
        if (subsArticleTitle.isEmpty()) {
            subsArticleTitle = new String(articleTitle);
        }
        volume = recordFieldData.getDataValue(FUN.articleVol);
        pages = recordFieldData.getDataValue(FUN.pages).replace('-', '‒');
        primaryAuthor = authorName;
        if (!primaryAuthor.isEmpty()) {
            primaryName = primaryAuthor.getNamesBiblio();
        }
    }

    @Override
    public final Iterator<TextPair> getTextFirstReference() {
        return (firstRefVector.iterator());
    }

    @Override
    public final Iterator<TextPair> getTextSecondReference() {
        return (subsRefsVector.iterator());
    }

    @Override
    public final Iterator<TextPair> getTextBibliography() {
        return (biblioVector.iterator());
    }

    @Override
    protected void setTextFirstReference() {
        firstCiteClass.addAuthor();
        firstCiteClass.addArticle();
        firstCiteClass.addParenTail();
        firstCiteClass.addVol();
        firstCiteClass.addTail();
    }

    class FirstCiteEncyclopediaClass extends FirstCiteBasicClass {

        protected void addVol() {
            text.add(new TextPair(" ", "footnote"));
            if (!volume.isEmpty()) {
                text.add(new TextPair(volume + ":", "footnote"));
            }
        }

        protected void addParenTail() {
            parentData.setFirstRefTail(text);
        }
    }

    class BiblioEncyclopediaClass extends BiblioBasicClass {

        protected void addArticleDetails() {
            addArticle();
            text.add(new TextPair(" " + OHCTextUtil.pagesForRange(pages) + " in ", "biblio"));
        }

        protected void addVol() {
            if (!volume.isEmpty()) {
                text.add(new TextPair("vol. " + volume + " of ", "biblio"));
            }
        }

        protected void addParenTail() {
            parentData.setBibTail(text);
        }
    }

    class SubsRefsEncyclopediaClass extends SubsRefBasicClass {

        protected void addParenTail() {
            parentData.setSubsRefsTail(text);
        }

        protected void addVol() {
            text.add(new TextPair(" ", "footnote"));
            if (!volume.isEmpty()) {
                text.add(new TextPair(volume + ":", "footnote"));
            }
        }
    }
}
