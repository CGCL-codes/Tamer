package org.simpleframework.servlet.resolve;

import org.simpleframework.http.Address;

class MatcherDestination implements Destination {

    private final Matcher matcher;

    private final Address address;

    private final Match match;

    private final String context;

    private final String name;

    public MatcherDestination(Matcher matcher, Match match, Address address) {
        this.context = matcher.getContext();
        this.name = match.getName();
        this.matcher = matcher;
        this.address = address;
        this.match = match;
    }

    public PathContext getContext() {
        return new PathBuilder(address, match, context);
    }

    public Service getService() {
        return matcher.lookup(name);
    }
}
