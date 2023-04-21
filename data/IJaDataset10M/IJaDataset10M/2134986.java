package uk.org.sgj.OHCApparatus.Records;

import uk.org.sgj.OHCApparatus.*;
import uk.org.sgj.OHCApparatus.Names.CanonicalNamesReviewed;

public final class OHCBookReviewRecord extends OHCJournalRecord implements OHCDerivedRecord {

    private CanonicalNamesReviewed reviewedName;

    private BookReviewData reviewedDetails;

    private OHCBookRecord bookBeingReviewed;

    private boolean hasTitle;

    protected static int recordIndex;

    FirstCiteBookReviewClass firstRefClass;

    BiblioBookReviewClass biblioClass;

    SubsRefBookReviewClass subsRefsVector;

    private static String recordTypeString;

    public final String getRecordTypeString() {
        return (recordTypeString);
    }

    static final void setRecordTypeString(String str) {
        recordTypeString = str;
    }

    public int getRecordIndex() {
        return (recordIndex);
    }

    public long getParentRecordNumber() {
        return (bookBeingReviewed.getRecordNumber());
    }

    public boolean canClone() {
        return false;
    }

    public OHCBasicRecord makeClone() {
        return (null);
    }

    public OHCBookReviewRecord(OHCBookRecord bbr) {
        super();
        recordFieldData.makeNewField(FUN.bookReviewTitle);
        bookBeingReviewed = bbr;
        firstRefClass = new FirstCiteBookReviewClass();
        biblioClass = new BiblioBookReviewClass();
        subsRefsVector = new SubsRefBookReviewClass();
    }

    protected boolean validateRecord() {
        boolean ok = super.validateRecord();
        return (ok);
    }

    public void refreshData() {
        super.refreshData();
        reviewedDetails = bookBeingReviewed.getEntryForBookReview();
        articleTitle = recordFieldData.getDataValue(FUN.bookReviewTitle);
        if (articleTitle.isEmpty()) {
            hasTitle = false;
            primaryTitle = "Review of " + reviewedDetails.getTitleForReview();
        } else {
            hasTitle = true;
            primaryTitle = articleTitle;
        }
        finalValidation(validateRecord());
    }

    class FirstCiteBookReviewClass extends FirstCiteJournalClass {

        void addTitle() {
            if (hasTitle) {
                addArticleForFollowing();
            }
        }

        void addReviewed() {
            if (hasTitle) {
                text.add(new TextPair("(review of ", "footnote"));
                reviewedDetails.getFirstRefTitled(text);
                text.add(new TextPair("), ", "footnote"));
            } else {
                text.add(new TextPair("review of ", "footnote"));
                reviewedDetails.getFirstRefUntitled(text);
            }
        }
    }

    protected final void setTextFirstReference() {
        firstRefClass.addAuthor();
        firstRefClass.addTitle();
        firstRefClass.addReviewed();
        firstRefClass.addJAbbrToTail();
    }

    class BiblioBookReviewClass extends BiblioJournalClass {

        void addTitle() {
            if (hasTitle) {
                addArticleForFollowing();
            }
        }

        void addReviewed() {
            if (hasTitle) {
                text.add(new TextPair("(review of ", "biblio"));
                reviewedDetails.getBiblioTitled(text);
                text.add(new TextPair("). ", "biblio"));
            } else {
                text.add(new TextPair("Review of ", "biblio"));
                reviewedDetails.getBiblioUntitled(text);
            }
        }
    }

    protected final void setTextBibliography() {
        biblioClass.addAuthor();
        biblioClass.addTitle();
        biblioClass.addReviewed();
        biblioClass.addJournalToTail();
        setName();
    }

    protected final void setTextSecondReference() {
        subsRefsVector.addAuthor();
        subsRefsVector.addReviewed();
        subsRefsVector.addTail();
    }

    class SubsRefBookReviewClass extends SubsRefJournalClass {

        void addReviewed() {
            text.add(new TextPair("review of ", "footnote"));
            reviewedDetails.getSubsRefs(text);
        }
    }

    @Override
    public void setNewParent(OHCBasicRecord rec) {
        bookBeingReviewed = (OHCBookRecord) rec;
    }
}
