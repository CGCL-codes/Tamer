package jd.client.get;

import jd.client.Response;
import jd.client.exception.ResponseParseException;

/**
 * @author Denis Migol
 * 
 */
public class GetRCVersionResponse extends Response {

    private String version;

    @Override
    public Response parse(final String response) throws ResponseParseException {
        setVersion(response);
        return super.parse(response);
    }

    /**
	 * @param version
	 *            the version to set
	 */
    protected void setVersion(final String version) {
        if (version != null) {
            this.version = version.trim();
        } else {
            this.version = version;
        }
    }

    /**
	 * @return the version
	 */
    public String getVersion() {
        return version;
    }
}
