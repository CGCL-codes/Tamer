package org.ibit.avanthotel.offer.web.util;

import java.io.Serializable;

/**
 * Value object per les dades generals d'un fitxer
 *
 * @created 19 / novembre / 2003
 */
public class FileVO implements Serializable {

    private String contentType;

    private String name;

    private long size;

    private byte[] data;

    /**
    * retorna el valor per la propietat contentType
    *
    * @return valor de contentType
    */
    public String getContentType() {
        return contentType;
    }

    /**
    * fitxa el valor de contentType
    *
    * @param contentType nou valor per contentType
    */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
    * retorna el valor per la propietat name
    *
    * @return valor de name
    */
    public String getName() {
        return name;
    }

    /**
    * fitxa el valor de name
    *
    * @param name nou valor per name
    */
    public void setName(String name) {
        this.name = name;
    }

    /**
    * retorna el valor per la propietat size
    *
    * @return valor de size
    */
    public long getSize() {
        return size;
    }

    /**
    * fitxa el valor de size
    *
    * @param size nou valor per size
    */
    public void setSize(long size) {
        this.size = size;
    }

    /**
    * retorna el valor per la propietat data
    *
    * @return valor de data
    */
    public byte[] getData() {
        return data;
    }

    /**
    * fitxa el valor de data
    *
    * @param data nou valor per data
    */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
    * @return
    */
    public String toString() {
        return "FitxerVO[ " + contentType + ", " + name + ", " + size + ", " + data + " ]";
    }
}
