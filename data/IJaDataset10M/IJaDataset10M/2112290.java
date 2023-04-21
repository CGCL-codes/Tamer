package com.tunelib.client.managed.request;

import com.google.gwt.requestfactory.shared.EntityProxy;
import org.springframework.roo.addon.gwt.RooGwtMirroredFrom;
import com.tunelib.server.domain.Artist;
import com.google.gwt.requestfactory.shared.ProxyFor;

@RooGwtMirroredFrom(Artist.class)
@ProxyFor(Artist.class)
public interface ArtistProxy extends EntityProxy {

    abstract Long getId();

    abstract String getName();

    abstract Integer getVersion();

    abstract void setId(Long id);

    abstract void setName(String name);

    abstract void setVersion(Integer version);
}
