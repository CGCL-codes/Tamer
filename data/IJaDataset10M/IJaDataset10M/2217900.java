package org.apache.myfaces.view.facelets.tag;

import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.Metadata;

/**
 * 
 * @author Jacob Hookom
 * @version $Id: MetadataImpl.java,v 1.3 2008/07/13 19:01:36 rlubke Exp $
 */
public final class MetadataImpl extends Metadata {

    private final Metadata[] _mappers;

    private final int _size;

    public MetadataImpl(Metadata[] mappers) {
        _mappers = mappers;
        _size = mappers.length;
    }

    public void applyMetadata(FaceletContext ctx, Object instance) {
        for (int i = 0; i < _size; i++) {
            _mappers[i].applyMetadata(ctx, instance);
        }
    }
}
