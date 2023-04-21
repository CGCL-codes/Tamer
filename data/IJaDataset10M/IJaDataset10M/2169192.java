package au.edu.archer.metadata.spi;

import au.edu.archer.metadata.mde.util.MDERecord;

/**
 * The RecordConverter SPI supplies methods for converting an MDERecord's state
 * between internal (MDE native) and external formats.
 * 
 * @author scrawley
 *
 */
public interface RecordConverter extends ServiceProvider {

    /**
     * Encode a metadata record from MDE 'native' form to a form suitable for saving.
     * The record's internal form ({@link MDERecord#getDOM()}) is converted to the appropriate
     * external form which is then assigned using {@link MDERecord#setExternalForm(Object)}.
     * 
     * @param record the metadata record with state in internal form.
     * @throws MetadataStoreException 
     */
    void encode(MDERecord record) throws MetadataStoreException;

    /**
     * Decode a metadata record in an external form into MDE 'native' form.
     * The record's external form ({@link MDERecord#getExternalForm()}) is converted to the 
     * internal form which is then assigned using {@link MDERecord#setDOM(org.w3c.dom.Document)}.
     * 
     * @param doc the metadata record with state in external form
     * @throws MetadataStoreException 
     */
    void decode(MDERecord record) throws MetadataStoreException;
}
