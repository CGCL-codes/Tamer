package com.traxel.lumbermill.filter;

public class FilterEvent {

    private final Filter _source;

    public FilterEvent(Filter source) {
        _source = source;
    }

    public Filter getSource() {
        return _source;
    }
}
