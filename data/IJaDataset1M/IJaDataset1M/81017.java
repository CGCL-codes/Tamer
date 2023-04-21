package org.kootox.episodesmanager.content;

import java.util.Date;

/**
 *
 * @author couteau
 */
public class EpisodeMetaData {

    protected String serie;

    protected String lastEpisode;

    protected int status;

    protected String title;

    protected Date airingDate;

    public EpisodeMetaData() {
    }

    public EpisodeMetaData(String title, Date airingDate) {
        this.title = title;
        this.airingDate = airingDate;
        this.status = 1;
    }

    public EpisodeMetaData(String title, Date airingDate, int status) {
        this.title = title;
        this.airingDate = airingDate;
        this.status = status;
    }

    public EpisodeMetaData(String serie, String lastEpisode, int status, String title, Date airingDate) {
        this.title = title;
        this.airingDate = airingDate;
        this.status = status;
        this.serie = serie;
        this.lastEpisode = lastEpisode;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getLastEpisode() {
        return lastEpisode;
    }

    public void setLastEpisode(String lastEpisode) {
        this.lastEpisode = lastEpisode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAiringDate() {
        return airingDate;
    }

    public void setAiringDate(Date airingDate) {
        this.airingDate = airingDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getValue(int column) {
        Object result = null;
        switch(column) {
            case 0:
                result = serie;
                break;
            case 1:
                result = lastEpisode;
                break;
            case 2:
                result = status;
                break;
            case 3:
                result = title;
                break;
            case 4:
                result = airingDate;
                break;
            default:
                break;
        }
        return result;
    }

    public void setValue(int column, Object value) {
        switch(column) {
            case 0:
                serie = (String) value;
                break;
            case 1:
                lastEpisode = (String) value;
                break;
            case 2:
                status = (Integer) value;
                break;
            case 3:
                title = (String) value;
                break;
            case 4:
                airingDate = (Date) value;
                break;
            default:
                break;
        }
    }
}
