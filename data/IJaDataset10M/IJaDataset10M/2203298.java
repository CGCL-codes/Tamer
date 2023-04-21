package uk.org.sgj.OHCApparatus.Records;

import uk.org.sgj.OHCApparatus.*;
import uk.org.sgj.OHCApparatus.Names.CanonicalNamesEditorsAsAuthors;
import uk.org.sgj.OHCApparatus.Names.CanonicalNames;
import uk.org.sgj.OHCApparatus.Names.CanonicalNamesEditors;

public abstract class OHCBookBasicDictionaryRecord extends OHCBookRecord {

    protected CanonicalNamesEditorsAsAuthors editorAuthorNames;

    protected String editorAuthorBiblio, editorAuthorSubsRefs, editorAuthorFirstRef;

    protected FirstCiteDictionaryClass firstCiteClass;

    protected BiblioDictionaryClass biblioClass;

    protected SubsRefsDictionaryClass subsRefClass;

    protected String anthology, anthologyAbbr, volume, volumes;

    protected String pages;

    public OHCBookBasicDictionaryRecord() {
        super();
        this.primaryTitleFieldDescription = FUN.articleAnthology;
        recordFieldData.makeNewFieldAtTop(FUN.editors);
        recordFieldData.makeNewFieldAtTop(FUN.articleAnthology);
        recordFieldData.makeNewFieldAtTop(FUN.articleAnthologyAbbr);
        recordFieldData.makeNewFieldAtTop(FUN.articleVols);
        firstCiteClass = new FirstCiteDictionaryClass();
        biblioClass = new BiblioDictionaryClass();
        subsRefClass = new SubsRefsDictionaryClass();
    }

    protected boolean canCiteArticle() {
        return (true);
    }

    @Override
    public void refreshData() {
        super.refreshData();
        editorAuthorNames = new CanonicalNamesEditorsAsAuthors(recordFieldData.getDataValue(FUN.editors));
        editorAuthorBiblio = editorAuthorNames.getNamesBiblio();
        editorAuthorFirstRef = editorAuthorNames.getNamesFirstRef();
        editorAuthorSubsRefs = editorAuthorNames.getNamesSubsRefs();
        primaryAuthor = editorAuthorNames;
        if (!primaryAuthor.isEmpty()) {
            primaryName = primaryAuthor.getNamesBiblio();
        }
        anthology = recordFieldData.getDataValue(FUN.articleAnthology);
        primaryTitle = anthology;
        anthologyAbbr = recordFieldData.getDataValue(FUN.articleAnthologyAbbr);
        if (anthologyAbbr.isEmpty()) {
            anthologyAbbr = new String(anthology);
        }
        subsequentTitle = anthologyAbbr;
        volumes = recordFieldData.getDataValue(FUN.articleVols);
        publisherPlace = recordFieldData.getDataValue(FUN.publisherPlace);
        publisherName = recordFieldData.getDataValue(FUN.publisherName);
        publisher = publisherPlace.concat(": " + publisherName);
        edition = recordFieldData.getDataValue(FUN.revisedEdition);
        if (!edition.isEmpty()) {
            editionFirstCite = OHCTextUtil.lowerCaseInitial(edition);
            editionBiblio = edition;
        }
        year = recordFieldData.getDataValue(FUN.year).replace('-', '‒');
    }

    @Override
    protected boolean validateRecord() {
        boolean ok = super.validateRecord();
        String details = "";
        if (ok) {
            if (editorAuthorNames.isEmpty()) {
                ok = false;
                details = new String("Please specify the name of the editor of this work.");
            }
            if (ok) {
                if (!editorAuthorNames.isValid()) {
                    ok = false;
                    details = new String("The name of the editor of the book is incorrectly formatted.\n");
                }
            }
            if (year.isEmpty()) {
                ok = false;
                details = new String("You must specify a year for this publication.");
            }
            if (ok) {
                if (anthology.isEmpty()) {
                    ok = false;
                    details = new String("Please specify the name of this anthology (dictionary, encyclopaedia or commentary).");
                }
            }
            if (ok) {
                if (anthologyAbbr.isEmpty()) {
                    ok = false;
                    details = new String("Please specify the abbreviated name of this anthology (dictionary, encyclopaedia or commentary).");
                }
            }
            if (ok) {
                if (publisherPlace.isEmpty()) {
                    ok = false;
                    details = new String("You must supply the location of the publisher.");
                }
            }
            if (ok) {
                if (publisherName.isEmpty()) {
                    ok = false;
                    details = new String("You must supply the name of the publisher.");
                }
            }
            if (!ok) {
                validationErrorText = details;
            }
        }
        return (ok);
    }

    class FirstCiteDictionaryClass extends FirstCiteClass {

        protected void removeAllElements() {
            brackets.removeAllElements();
        }

        protected void addAnthology() {
            text.add(new TextPair(anthologyAbbr + " ", "footnote italic"));
        }
    }

    class BiblioDictionaryClass extends BiblioClass {

        protected void addVolumeStuff() {
            if (!volumes.isEmpty()) {
                bracketBiblio.add(new TextPair(volumes, "biblio"));
                bracketBiblio.add(new TextPair(" vols. ", "biblio"));
            }
        }

        void addEditor() {
            text.add(new TextPair(editorAuthorBiblio, "biblio"));
        }

        void addVols() {
            if (!volumes.isEmpty()) {
                text.add(new TextPair(volumes + " vols. ", "biblio"));
            }
        }

        protected void removeAllElements() {
            bracketBiblio.removeAllElements();
        }

        void addVol() {
            if (!volume.isEmpty()) {
                text.add(new TextPair("vol. " + volume + " of ", "biblio"));
            }
        }

        void addAnthology() {
            text.add(new TextPair(OHCTextUtil.titleForPunctuation(anthology), "biblio italic"));
            text.add(new TextPair(". ", "biblio italic"));
        }
    }

    class SubsRefsDictionaryClass extends SubsRefClass {

        void addVol() {
            if (!volume.isEmpty()) {
                text.add(new TextPair(volume + ":", "footnote"));
            }
        }
    }

    @Override
    protected final void setTextFirstReference() {
    }

    @Override
    protected final void setTextSecondReference() {
    }

    @Override
    protected final void setTextBibliography() {
        biblioClass.removeAllElements();
        biblioClass.addEditor();
        biblioClass.addAnthology();
        biblioClass.addIllustrator();
        biblioClass.addTranslator();
        biblioClass.addVolumeStuff();
        biblioClass.addRevision();
        biblioClass.addSeries();
        biblioClass.addReprint();
        biblioClass.addPublisher();
        biblioClass.addYear();
        biblioClass.addBracketBiblio();
        setName();
    }

    protected String subsequentTitle;
}

abstract class DictionaryData {

    String title;

    OHCTextVector fnBrackets;

    OHCTextVector bibBrackets;

    String frTail;

    String bibTitle;

    String frTitle, titleSubsRefs;

    CanonicalNames can;

    DictionaryData(String t, String ts, OHCTextVector bb, CanonicalNames nm) {
        title = t;
        titleSubsRefs = ts;
        bibBrackets = bb;
        can = new CanonicalNamesEditors(nm.getString());
    }

    protected void setFirstRefTail(OHCTextVector text) {
        String t;
        if (titleSubsRefs.isEmpty()) {
            t = new String(title);
        } else {
            t = new String(titleSubsRefs);
        }
        text.add(new TextPair(OHCTextUtil.titleNoPunctuation(t), "footnote italic"));
    }

    protected void setSubsRefsTail(OHCTextVector text) {
        setFirstRefTail(text);
    }

    protected abstract void setBibTail(OHCTextVector text);
}
