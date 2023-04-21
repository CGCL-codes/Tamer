package es.rvp.java.simpletag.core.metadata;

import java.net.URL;

/**
 * Información de las imagenes asociadas a la portada.
 *
 * @author Rodrigo Villamil Perez
 */
public class CoverImageInfo {

    private URL url = null;

    private String mimeType = null;

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(final URL url) {
        this.url = url;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "CoverImageInfo [mimeType=" + this.mimeType + ", url=" + this.url + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (this.mimeType == null ? 0 : this.mimeType.hashCode());
        result = prime * result + (this.url == null ? 0 : this.url.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CoverImageInfo other = (CoverImageInfo) obj;
        if (this.mimeType == null) {
            if (other.mimeType != null) {
                return false;
            }
        } else if (!this.mimeType.equals(other.mimeType)) {
            return false;
        }
        if (this.url == null) {
            if (other.url != null) {
                return false;
            }
        } else if (!this.url.equals(other.url)) {
            return false;
        }
        return true;
    }
}
