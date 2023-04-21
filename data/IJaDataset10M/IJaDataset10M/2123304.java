package Link;

import org.restlet.Restlet;
import org.restlet.data.*;

public class ShortlinkRestlet extends Restlet {

    String baseURI = "/";

    public ShortlinkRestlet() {
    }

    public ShortlinkRestlet(String baseURI) {
        this.baseURI = baseURI;
    }

    @Override
    public void handle(Request request, Response response) {
        String origRef = request.getOriginalRef().toString();
        String resRef = request.getRootRef().toString();
        String url = origRef.substring(resRef.length() + baseURI.length());
        LinkFile lf = new LinkFile();
        if (request.getMethod().equals(Method.GET) || request.getMethod().equals(Method.POST)) {
            if (lf.linkExists(url) || (url.length() < ShortlinkProperties.getLengthURL() + 1)) {
                String fullURL = lf.openLink(url);
                if (fullURL != null) {
                    response.setEntity(fullURL, MediaType.TEXT_HTML);
                    response.setStatus(Status.SUCCESS_OK);
                } else {
                    response.setEntity("ERROR! Shortlink not found", MediaType.TEXT_HTML);
                    response.setStatus(Status.SERVER_ERROR_INTERNAL);
                }
            } else {
                String shortLink = lf.createLink(url);
                if (shortLink != null) {
                    response.setEntity(shortLink, MediaType.TEXT_HTML);
                    response.setStatus(Status.SUCCESS_OK);
                } else {
                    response.setEntity("ERROR! Shortlink creation failed", MediaType.TEXT_HTML);
                    response.setStatus(Status.SERVER_ERROR_INTERNAL);
                }
            }
        } else if (request.getMethod().equals(Method.PUT)) {
            if (lf.linkExists(url) || (url.length() < ShortlinkProperties.getLengthURL() + 1)) {
            } else {
                int firstSlash = url.indexOf("/");
                String linkName = url.substring(0, firstSlash);
                String longLink = url.substring(firstSlash + 1);
                String shortLink = lf.createLink(longLink, linkName);
                if (shortLink != null) {
                    response.setEntity(shortLink, MediaType.TEXT_HTML);
                    response.setStatus(Status.SUCCESS_OK);
                } else {
                    response.setEntity("ERROR! Shortlink creation failed", MediaType.TEXT_HTML);
                    response.setStatus(Status.SERVER_ERROR_INTERNAL);
                }
            }
        } else if (request.getMethod().equals(Method.DELETE)) {
            if (lf.delete(url)) {
                response.setEntity("OK", MediaType.TEXT_HTML);
                response.setStatus(Status.SUCCESS_OK);
            } else {
                response.setEntity("ERROR! Shortlink delete failed", MediaType.TEXT_HTML);
                response.setStatus(Status.SERVER_ERROR_INTERNAL);
            }
        } else {
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
    }
}
