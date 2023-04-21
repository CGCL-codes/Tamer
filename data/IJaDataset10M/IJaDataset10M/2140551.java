package com.doculibre.intelligid.wicket.models;

import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.ObjetNumerique;

@SuppressWarnings("serial")
public class ProcessusActiviteModel extends ReloadableObjetNumeriqueModel {

    @Override
    protected ObjetNumerique load(Long id) {
        FGDDelegate delegate = new FGDDelegate();
        return delegate.getProcessusActivite(id);
    }
}
