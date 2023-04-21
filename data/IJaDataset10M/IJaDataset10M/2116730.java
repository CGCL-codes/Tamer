package org.osmorc.manifest.lang.headerparser.impl;

import org.jetbrains.annotations.NotNull;
import org.osmorc.manifest.lang.headerparser.HeaderParser;
import org.osmorc.manifest.lang.headerparser.HeaderParserProvider;

/**
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class HeaderParserProviderImpl implements HeaderParserProvider {

    public HeaderParserProviderImpl(@NotNull String headerName, @NotNull HeaderParser headerParser) {
        _headerName = headerName;
        _headerParser = headerParser;
    }

    public String getHeaderName() {
        return _headerName;
    }

    public HeaderParser getHeaderParser() {
        return _headerParser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeaderParserProviderImpl that = (HeaderParserProviderImpl) o;
        return _headerName.equals(that._headerName);
    }

    @Override
    public int hashCode() {
        return _headerName.hashCode();
    }

    private String _headerName;

    private HeaderParser _headerParser;
}
