package com.bradmcevoy.http;

public interface MakeCollectionableResource extends CollectionResource {

    CollectionResource createCollection(String newName);
}
