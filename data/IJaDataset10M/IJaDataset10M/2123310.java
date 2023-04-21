package es.f2020.osseo.domain;

/**
 * This class models a keyphrase for a website.
 * @see Website
 */
public class Keyphrase {

    private int id;

    private int websiteId;

    private String keyphrase;

    public Keyphrase() {
    }

    public Keyphrase(int id, int websiteId, String keyphrase) {
        this.id = id;
        this.websiteId = websiteId;
        this.keyphrase = keyphrase;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyphrase() {
        return keyphrase;
    }

    public void setKeyphrase(String keyphrase) {
        this.keyphrase = keyphrase;
    }

    public int getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(int websiteId) {
        this.websiteId = websiteId;
    }

    @Override
    public String toString() {
        return "[id:" + this.id + ",websiteId:" + this.websiteId + ",keyphrase:" + this.keyphrase + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Keyphrase) {
            Keyphrase keyphraseObj = (Keyphrase) obj;
            return this.id == keyphraseObj.getId() && this.websiteId == keyphraseObj.getWebsiteId() && this.keyphrase.equals(keyphraseObj.getKeyphrase());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
}
