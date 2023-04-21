package booksandfilms.client.entities;

import java.util.Comparator;

public class QuoteAll {

    private Long id;

    private String characterName;

    private String quoteText;

    private String title;

    private String type;

    private Long mediaId;

    public QuoteAll() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getQuoteText() {
        return quoteText;
    }

    public void setQuoteText(String quoteText) {
        this.quoteText = quoteText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public enum SortParameter {

        ID_ASCENDING, MEDIA_ID_ASCENDING
    }

    public static Comparator<QuoteAll> getComparator(SortParameter... sortParameters) {
        return new QuoteComparator(sortParameters);
    }

    private static class QuoteComparator implements Comparator<QuoteAll> {

        private SortParameter[] parameters;

        private QuoteComparator(SortParameter[] parameters) {
            this.parameters = parameters;
        }

        public int compare(QuoteAll o1, QuoteAll o2) {
            int comparison;
            for (SortParameter parameter : parameters) {
                switch(parameter) {
                    case ID_ASCENDING:
                        comparison = (int) (o1.getId() - o2.getId());
                        if (comparison != 0) return comparison;
                        break;
                    case MEDIA_ID_ASCENDING:
                        comparison = (int) (o1.getMediaId() - o2.getMediaId());
                        if (comparison != 0) return comparison;
                        break;
                }
            }
            return 0;
        }
    }
}
