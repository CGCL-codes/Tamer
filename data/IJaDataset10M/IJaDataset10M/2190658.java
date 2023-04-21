package edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.impl;

import java.util.ArrayList;
import java.util.List;
import edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.ICollection;
import edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.ICommunity;

public class Community extends DSpaceObject implements ICommunity {

    private List<ICollection> collections;

    private List<ICommunity> subcommunities;

    public Community(String handle) {
        super(handle);
    }

    public List<ICollection> getCollections() {
        return collections;
    }

    public List<ICommunity> getSubcommunities() {
        return subcommunities;
    }

    public void addCollection(ICollection collection) {
        if (collections == null) collections = new ArrayList<ICollection>();
        this.collections.add(collection);
    }

    public void addSubcommunity(ICommunity community) {
        if (subcommunities == null) subcommunities = new ArrayList<ICommunity>();
        this.subcommunities.add(community);
    }
}
