package rachel.http;

public class Http {

    public static final String SERVER_ID = "Rachel Ultra Light-Weight HTTP Server";

    public interface Method {

        String GET = "GET";

        String POST = "POST";

        String HEAD = "HEAD";
    }

    public interface Header {

        String DATE = "Date";

        String LAST_MODIFIED = "Last-Modified";

        String USER_AGENT = "User-Agent";

        String SERVER = "Server";

        String HOST = "Host";

        String CONTENT_TYPE = "Content-Type";

        String CONTENT_LENGTH = "Content-Length";

        String REFERER = "Referer";
    }

    public interface Status {

        int OK_200 = 200;

        int NO_CONTENT_204 = 204;

        int NOT_FOUND_404 = 404;

        int NOT_IMPLEMENTED_501 = 501;
    }
}
