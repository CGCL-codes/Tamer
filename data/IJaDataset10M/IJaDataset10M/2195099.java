package org.kalypso.nofdpidss.hydraulic.computation.wizard.pi.pages;

import java.io.File;
import org.kalypso.nofdpidss.core.base.gml.model.hydraulic.base.IRoughnessClass;

public interface IPiImportSettings {

    IRoughnessClass getRoughnessClass();

    File getImportDirectory();

    String getCRS();
}
