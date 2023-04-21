package org.ugue.utils.http;

/**
 * <p>This class represents tha HTTP Status Line, which is the first line that
 * server sends to the client.</p>
  * 
 * <p>Examples of http status lines:</p>
 * 
 * <ul>
 * 	<li>HTTP/1.1 200 OK</li>
 * 	<li>HTTP/1.0 404 Not Found</li>
 * </ul>
 * 
 * @author fpreto
 *
 */
public class HTTPStatusLine {

    private HTTPVersion version;

    private HTTPStatusCode code;

    private String reason_phrase;

    /**
	 * Default constructor. The elements of the status line is passed through parameters.
	 * 
	 * @param version the HTTP version
	 * @param code the status code
	 * @param reason_phrase the reason phrase
	 */
    public HTTPStatusLine(HTTPVersion version, HTTPStatusCode code, String reason_phrase) {
        this.version = version;
        this.code = code;
        this.reason_phrase = reason_phrase;
    }

    /**
	 * Alternative constructor. It will parse the line and extract the elements from it.
	 * 
	 * @param line a String with HTTP status line
	 */
    public HTTPStatusLine(String line) {
        int firstSpaceIdx = line.indexOf(' ');
        int secondSpaceIdx = line.indexOf(' ', firstSpaceIdx + 1);
        if (firstSpaceIdx < 0) {
            throw new IllegalArgumentException("Malformed status-line");
        }
        if (secondSpaceIdx < 0) {
            throw new IllegalArgumentException("Malformed status-line");
        }
        String versionStr = line.substring(0, firstSpaceIdx);
        version = HTTPVersion.parse(versionStr);
        String codeStr = line.substring(firstSpaceIdx + 1, secondSpaceIdx).trim();
        try {
            int codeInt = Integer.parseInt(codeStr);
            code = HTTPStatusCode.parse(codeInt);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Malformed status-line", e);
        }
        reason_phrase = line.substring(secondSpaceIdx + 1).trim();
        if (version == null | reason_phrase.length() <= 0) {
            throw new IllegalArgumentException("Malformed status-line");
        }
    }

    /**
	 * @return the version
	 */
    public HTTPVersion getVersion() {
        return version;
    }

    /**
	 * @return the code
	 */
    public HTTPStatusCode getCode() {
        return code;
    }

    /**
	 * @return the reason_phrase
	 */
    public String getReasonPhrase() {
        return reason_phrase;
    }

    public String toString() {
        return version + " " + code.getCode() + " " + reason_phrase;
    }
}
