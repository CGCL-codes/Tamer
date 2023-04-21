package javacrawler.http;

/**
 * Clase HttpHead
 * @author luis
 */
public class HttpHead {

    private String http_version;

    private StatusCode statusCode;

    /**
     * Retorna la version de http utilizada.
     * @return
     */
    public final String getHttp_version() {
        return http_version;
    }

    /**
     * Establece la version de http utilizada.
     * @param http_version
     */
    public void setHttp_version(String http_version) {
        this.http_version = http_version;
    }

    /**
     * Retorna el estado de la peticion HTTP
     * @return
     */
    public final StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * Establece el estado de la peticion HTTP
     * @param statusCode
     */
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HttpHead other = (HttpHead) obj;
        if ((this.http_version == null) ? (other.http_version != null) : !this.http_version.equals(other.http_version)) {
            return false;
        }
        if (this.statusCode != other.statusCode) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.http_version != null ? this.http_version.hashCode() : 0);
        hash = 97 * hash + this.statusCode.hashCode();
        return hash;
    }
}
