package org.commsuite.model.ws;

/**
 * @since 1.0
 * TODO: PRZYZNAC SIE ! KTO POPELNIL TA KLASE ? ;)
 */
public interface WSContents {

    public Long getId();

    public byte[] getData();

    public void setData(byte[] contentsBin);

    public String getMimeType();

    public void setMimeType(String mimeType);

    public String getDescription();

    public void setDescription(String description);
}
