package com.web.music.interfaces;

import java.io.InputStream;

/**
 * HTTP client module used to download data from the Web
 */
public interface HTTPClient {

    /**
	 * Downloads page content from specified URL and saves it on disc
	 * 
	 * @param url - page URL to download
	 * @return	name of file on local disc
	 */
    public String downloadToFile(String url);

    /**
	 * Downloads page content from specified URL and returns it as InputStream
	 * 
	 * @param url - page URL to download
	 * @return	input stream
	 */
    public InputStream downloadToInputStream(String url);
}
