package org.kalypso.nofdpidss.hydraulic.computation.wizard.computation.pages;

import org.kalypso.model.wspm.sobek.core.interfaces.ILastfall;
import org.kalypso.nofdpidss.core.base.gml.model.project.base.IVariant;

public interface IPageSelectLastfall {

    public IVariant[] getVariants();

    public ILastfall[] getLastfalls();
}
