package org.restlet.engine.http.header;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.restlet.data.Parameter;
import org.restlet.data.Preference;

/**
 * Preference header writer.
 * 
 * @author Jerome Louvel
 */
public class PreferenceWriter extends HeaderWriter<Preference<?>> {

    /**
     * Indicates if the quality value is valid.
     * 
     * @param quality
     *            The quality value.
     * @return True if the quality value is valid.
     */
    public static boolean isValidQuality(float quality) {
        return (quality >= 0F) && (quality <= 1F);
    }

    /**
     * Writes a list of preferences with a comma separator.
     * 
     * @param prefs
     *            The list of preferences.
     * @return The formatted list of preferences.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static String write(List prefs) {
        return new PreferenceWriter().append(prefs).toString();
    }

    @Override
    public PreferenceWriter append(Preference<?> pref) {
        append(pref.getMetadata().getName());
        if (pref.getQuality() < 1F) {
            append(";q=");
            appendQuality(pref.getQuality());
        }
        if (pref.getParameters() != null) {
            Parameter param;
            for (Iterator<Parameter> iter = pref.getParameters().iterator(); iter.hasNext(); ) {
                param = iter.next();
                if (param.getName() != null) {
                    append(';').append(param.getName());
                    if ((param.getValue() != null) && (param.getValue().length() > 0)) {
                        append('=').append(param.getValue());
                    }
                }
            }
        }
        return this;
    }

    /**
     * Formats a quality value.<br>
     * If the quality is invalid, an IllegalArgumentException is thrown.
     * 
     * @param quality
     *            The quality value as a float.
     * @return This writer.
     */
    public PreferenceWriter appendQuality(float quality) {
        if (!isValidQuality(quality)) {
            throw new IllegalArgumentException("Invalid quality value detected. Value must be between 0 and 1.");
        }
        java.text.NumberFormat formatter = java.text.NumberFormat.getNumberInstance(java.util.Locale.US);
        formatter.setMaximumFractionDigits(2);
        append(formatter.format(quality));
        return this;
    }
}
