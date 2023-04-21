package vqwiki;

import vqwiki.db.DBDate;
import java.io.BufferedReader;
import java.io.StringReader;

/**
 * @author garethc
 * Date: Jan 10, 2003
 */
public class TopicVersion {

    private String virtualWiki;

    private String topicName;

    private DBDate revisionDate;

    private int versionNumber;

    /**
     *
     */
    public TopicVersion(String virtualWiki, String topicName, DBDate revisionDate, int versionNumber) {
        if (virtualWiki == null) virtualWiki = "";
        this.virtualWiki = virtualWiki;
        this.topicName = topicName;
        this.revisionDate = revisionDate;
        this.versionNumber = versionNumber;
    }

    /**
     *
     */
    public int getVersionNumber() {
        return versionNumber;
    }

    /**
     *
     */
    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    /**
     *
     */
    public String getTopicName() {
        return topicName;
    }

    /**
     *
     */
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    /**
     *
     */
    public DBDate getRevisionDate() {
        return revisionDate;
    }

    /**
     *
     */
    public void setRevisionDate(DBDate revisionDate) {
        this.revisionDate = revisionDate;
    }

    /**
     *
     */
    public String getVirtualWiki() {
        return virtualWiki;
    }

    /**
     *
     */
    public void setVirtualWiki(String virtualWiki) {
        this.virtualWiki = virtualWiki;
    }

    /**
     *
     */
    public String getRawContents() throws Exception {
        return WikiBase.getInstance().getVersionManagerInstance().getVersionContents(this.virtualWiki, this.topicName, this.versionNumber);
    }

    /**
     *
     */
    public String getCookedContents() throws Exception {
        WikiBase instance = WikiBase.getInstance();
        return instance.cook(new BufferedReader(new StringReader(instance.getVersionManagerInstance().getVersionContents(this.virtualWiki, this.topicName, this.versionNumber))), this.virtualWiki);
    }

    /**
     *
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.versionNumber);
        buffer.append(":");
        buffer.append(this.revisionDate);
        return buffer.toString();
    }
}
