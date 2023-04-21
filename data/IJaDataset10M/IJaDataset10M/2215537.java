package com.google.gdata.data;

/**
 * The Entry class customizes the BaseFeed class to represent the most
 * generic possible entry type.  One usage for this class is to enable the
 * parsing of an entry where the extension model is unknown at the start of
 * the parsing process.  Using in combination with {@link ExtensionProfile}
 * auto-extension, the feed can be parsed generically, and then the
 * {@link #getAdaptedEntry()} can be used to retrieve a more-specfic entry
 * type based upon the {@link Category} kind elements founds within the
 * parsed entry content.
 *
 * @see ExtensionProfile#setAutoExtending(boolean)
 * APIs.
 *
 * 
 * 
 */
public class Entry extends BaseEntry<Entry> {

    /**
   * Constructs a new uninitialized Entry instance.
   */
    public Entry() {
    }

    /**
   * Constructs a new Entry by doing a shallow copy from another BaseEntry
   * instance.
   */
    public Entry(BaseEntry<?> sourceEntry) {
        super(sourceEntry);
    }

    /**
   * {@inheritDoc}
   * <p>
   * The Entry class declares support for processing of arbitrary entry
   * extension data (as XmlBlobs).  Subtypes which want stricter
   * parsing should override this method and not delegate to the base
   * implementation.
   */
    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        extProfile.declareArbitraryXmlExtension(BaseEntry.class);
    }
}
