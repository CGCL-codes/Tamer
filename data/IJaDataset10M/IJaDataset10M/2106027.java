package org.restlet.client.engine.http.header;

import java.util.List;
import org.restlet.client.data.Language;

/**
 * Language header writer.
 * 
 * @author Jerome Louvel
 */
public class LanguageWriter extends MetadataWriter<Language> {

    /**
     * Writes a list of languages.
     * 
     * @param languages
     *            The languages to write.
     * @return This writer.
     */
    public static String write(List<Language> languages) {
        return new LanguageWriter().append(languages).toString();
    }
}
