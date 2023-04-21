package org.osmorc.manifest.lang.headerparser.impl;

import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.lang.headerparser.HeaderParserProvider;
import org.osmorc.manifest.lang.headerparser.HeaderParserProviderRepostory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class StandardManifestHeaderParserProviderRepostory implements HeaderParserProviderRepostory {

    public StandardManifestHeaderParserProviderRepostory(SimpleHeaderParser simpleHeaderParser) {
        _headerProviders = new ArrayList<HeaderParserProvider>();
        _headerProviders.add(new HeaderParserProviderImpl("Manifest-Version", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Created-By", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Signature-Version", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Class-Path", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Implementation-Title", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Implementation-Version", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Implementation-Vendor", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Implementation-Vendor-Id", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Implementation-URL", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Specification-Title", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Specification-Version", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Specification-Vendor", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Sealed", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Content-Type", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Java-Bean", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("MD5-Digest", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("SHA-Digest", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Magic", simpleHeaderParser));
        _headerProviders.add(new HeaderParserProviderImpl("Name", simpleHeaderParser));
    }

    @NotNull
    public Collection<HeaderParserProvider> getHeaderParserProviders() {
        return Collections.unmodifiableList(_headerProviders);
    }

    private final List<HeaderParserProvider> _headerProviders;
}
