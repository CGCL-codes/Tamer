package net.sourceforge.arcamplayer.protmoddis.mediaCollection;

public class Song {

    protected long id;

    protected String title;

    protected String autor;

    protected String album;

    protected int year;

    protected int track;

    protected int duration;

    protected float evaluation;

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(String value) {
        int intId = -1;
        try {
            intId = Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return;
        }
        if (intId != -1) {
            this.id = intId;
        }
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the autor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutor() {
        return autor;
    }

    /**
     * Sets the value of the autor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutor(String value) {
        this.autor = value;
    }

    /**
     * Gets the value of the album property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlbum() {
        return album;
    }

    /**
     * Sets the value of the album property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlbum(String value) {
        this.album = value;
    }

    /**
     * Gets the value of the year property.
     * 
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     */
    public void setYear(int value) {
        this.year = value;
    }

    /**
     * Gets the value of the track property.
     * 
     */
    public int getTrack() {
        return track;
    }

    /**
     * Sets the value of the track property.
     * 
     */
    public void setTrack(int value) {
        this.track = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     */
    public void setDuration(int value) {
        this.duration = value;
    }

    /**
     * Gets the value of the evaluation property.
     * 
     */
    public float getEvaluation() {
        return evaluation;
    }

    /**
     * Sets the value of the evaluation property.
     * 
     */
    public void setEvaluation(float value) {
        this.evaluation = value;
    }
}
