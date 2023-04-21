package org.simpleframework.servlet.resolve;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.http.Address;
import org.simpleframework.util.KeyMap;

/**
 * 
 * Resolve a list of destinations using patterns. If there is no match
 * then a default destination can be provided to simplify the task of
 * acquiring a target.
 * 
 *   *.jsp
 *   /context/*
 *   /context/servlet/*
 *   
 * Each of the destinations will contain a <code>PathContext</code>
 * from the address provided. This ensures that each destination can be
 * used to provide servlet path information dependant on what <em>stage</em> XXX
 * of processing the request is at.
 * 
 * @author Niall Gallagher
 *
 */
public class ContextMatcher implements Matcher {

    private final ContextMapper<Match> resolver;

    private final KeyMap<Service> registry;

    private final Service service;

    private final String context;

    public ContextMatcher(String context) {
        this(context, null);
    }

    public ContextMatcher(String context, Service service) {
        this.resolver = new ContextMapper<Match>(context);
        this.registry = new KeyMap<Service>();
        this.service = service;
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public Service lookup(String name) {
        return registry.get(name);
    }

    public Destination resolve(Address address) {
        List<Destination> list = resolveAll(address);
        if (list.isEmpty()) {
            return build(address);
        }
        return list.get(0);
    }

    public List<Destination> resolveAll(Address address) {
        List<Match> list = resolver.resolveAll(address);
        return build(list, address);
    }

    private Destination build(Address address) {
        return new ServiceDestination(service, address, context);
    }

    private Destination build(Match match, Address address) {
        return new MatcherDestination(this, match, address);
    }

    private List<Destination> build(List<Match> list, Address address) {
        List<Destination> result = new ArrayList<Destination>();
        for (Match match : list) {
            Destination destination = build(match, address);
            result.add(destination);
        }
        return result;
    }

    public void match(Match match) {
        resolver.match(match);
    }

    public void register(Service service) {
        String name = service.getName();
        if (name != null) {
            registry.put(name, service);
        }
    }
}
