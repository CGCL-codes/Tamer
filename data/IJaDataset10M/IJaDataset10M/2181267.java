package applications.rfid.ambientLibrary.main;

import applications.rfid.ambientLibrary.main.ReviewManager;
import applications.rfid.ambientLibrary.main.ATHandler;

public interface ATBookWrapper {

    public String getTitle();

    public ATHandler setTitle(String title);

    public String getAuthors();

    public void setAuthors(String authors);

    public String getRating();

    public void addReview(String review);

    public ATHandler displayReviews(ReviewManager reviewManager);

    public void clearReviews();

    public String getMatchFromString(String keywords);

    public void setKeywordsFromString(String keywords);

    public ATHandler displayKeywords(KeywordWindow keywordWindow);
}
