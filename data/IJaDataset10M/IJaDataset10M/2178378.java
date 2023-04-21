package com.google.gdata.data;

/**
 * The Feed class customizes the BaseFeed class to represent the most
 * generic possible feed type.  One usage for this class is to enable the
 * parsing of feeds where the extension model is unknown at the start of
 * the parsing process.  Using in combination with {@link ExtensionProfile}
 * auto-extension, the feed can be parsed generically, and then the
 * {@link #getAdaptedFeed()} can be used to retrieve a more-specfic feed
 * type based upon the {@link Category} kind elements founds within the
 * parsed feed content.
 *
 * @see ExtensionProfile#setAutoExtending(boolean)
 *
 * 
 */
public class Feed extends BaseFeed<Feed, Entry> {

    /**
   * Constructs a new Feed instance that is parameterized to contain
   * Entry instances.
   */
    public Feed() {
        super(Entry.class);
    }

    @Override
    public void declareExtensions(ExtensionProfile extProfile) {
        extProfile.declareArbitraryXmlExtension(BaseFeed.class);
        super.declareExtensions(extProfile);
    }
}
