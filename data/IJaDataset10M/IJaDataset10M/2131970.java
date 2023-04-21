package org.melati.servlet;

import org.melati.Melati;

/**
 * Create a FormDataAdaptor from a {@link Melati} and
 * the {@link MultipartFormField} which was uploaded.
 */
public class TemporaryFileDataAdaptorFactory extends FormDataAdaptorFactory {

    /** 
   * Get the {@link FormDataAdaptor}. 
   *
   * @param melati The {@link Melati}
   * @param field  A {@link MultipartFormField}
   * @return The {@link FormDataAdaptor}
   */
    public FormDataAdaptor getIt(final Melati melati, MultipartFormField field) {
        return new TemporaryFileDataAdaptor();
    }
}
