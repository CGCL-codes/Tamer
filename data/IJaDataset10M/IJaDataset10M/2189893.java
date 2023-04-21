package com.webkreator.qlue.view;

/**
 * The role of a view resolver is to map a URL to a view. This simplest
 * implementation will remove the suffix and return the resulting string as view
 * name.
 */
public class ViewResolver {

    /**
	 * Determine view name, given URI.
	 * 
	 * @param requestUri
	 * @return
	 */
    public String resolveView(String requestUri) {
        if (requestUri.indexOf("..") != -1) {
            throw new RuntimeException("ViewResolver: backreferences not allowed in URI: " + requestUri);
        }
        int i = requestUri.lastIndexOf('.');
        if (i != -1) {
            requestUri = requestUri.substring(0, i);
        }
        return requestUri;
    }
}
