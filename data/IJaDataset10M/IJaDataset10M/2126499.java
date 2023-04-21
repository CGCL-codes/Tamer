package org.isaaclabs.mymdb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author hemanth.sunkara
 * @version $Revision: 1.0 $
 */
public class Movie {

    /**
     * Separator used in file naming
     */
    public static final String SEPERATOR = " - ";

    /**
     * Base API prefix string. Add movie name at the end to form full URL
     */
    private static final String MAIN_API_URL = "http://www.imdbapi.com/?t=";

    /**
     * Prefix url to form IMDB url. Add movie id at the end to form the full url
     */
    private static final String IMDB_URL = "http://www.imdb.com/title/";

    /**
     * Stores all the info file fields
     */
    private String[] fieldValues;

    /**
     * Looks up imdb and populates info file fields for a given movie
     * @param movieTitle String
     * @throws ClientProtocolException
     * @throws IOException
     * @throws JSONException
     */
    public Movie(String movieTitle) throws ClientProtocolException, IOException, JSONException {
        this.fieldValues = new String[MovieFields.values().length];
        String fullUrl = MAIN_API_URL.concat(URLEncoder.encode(movieTitle, "UTF-8"));
        String responseJSON = HttpUtils.hitURL(fullUrl);
        populate(responseJSON);
    }

    /**
     * Populates the info file fields from the api response
     * @param jsonStr String
     * @throws JSONException
     * @throws ClientProtocolException
     * @throws IOException
     */
    private void populate(String jsonStr) throws JSONException, ClientProtocolException, IOException {
        JSONObject urlResponse = new JSONObject(jsonStr);
        for (MovieFields field : MovieFields.values()) {
            if (field.present) {
                this.fieldValues[field.position] = unescape(urlResponse.getString(field.name));
            }
        }
        this.fieldValues[MovieFields.URL.position] = getImdbUrl();
        this.fieldValues[MovieFields.STORYLINE.position] = getStoryLine();
    }

    /**
     * Writes the info file
     * @param directory File
     * @throws IOException
     */
    protected void writeToFile(File directory) throws IOException {
        FileWriter fileWriter = new FileWriter(directory + File.separator + this.fieldValues[MovieFields.TITLE.position].replace(':', '-') + ".txt");
        BufferedWriter infoFileWriter = new BufferedWriter(fileWriter);
        for (MovieFields field : MovieFields.values()) {
            infoFileWriter.write(field.name + " : " + this.fieldValues[field.position] + "\r\n");
        }
        infoFileWriter.close();
    }

    /**
     * Forms the new filename string
     * @return File
     */
    public File getNewFileName() {
        String title = this.fieldValues[MovieFields.TITLE.position];
        if (title == null) {
            return null;
        }
        String newName = this.fieldValues[MovieFields.RATING.position] + SEPERATOR + title + SEPERATOR + this.fieldValues[MovieFields.YEAR.position];
        return new File(newName.replace(':', '-'));
    }

    /**
     * Un-escapes the HTML special characters to UTF-8 characters
     * @param string String
     * @return String
     */
    private String unescape(String string) {
        return StringEscapeUtils.unescapeHtml(string);
    }

    /**
     * Forms the Imdb Url.
     * @return String
     */
    private String getImdbUrl() {
        return IMDB_URL.concat(this.fieldValues[MovieFields.ID.position]);
    }

    /**
     * Parses the imdb movie page and extracts the story line.
     * @return String
     * @throws ClientProtocolException
     * @throws IOException
     */
    private String getStoryLine() throws ClientProtocolException, IOException {
        String response = HttpUtils.hitURL(this.fieldValues[MovieFields.URL.position]);
        String storyLine = response.split("<h2>Storyline</h2>")[1].split("</p>")[0].split("<p>")[1].split("\n")[0];
        return unescape(storyLine);
    }
}
