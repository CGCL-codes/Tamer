package org.fudaa.fudaa.crue.options;

import org.fudaa.fudaa.crue.options.services.InstallationService;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

/**
 *
 * @author deniger ( genesis)
 */
public class Installer extends ModuleInstall {

    InstallationService service = Lookup.getDefault().lookup(InstallationService.class);

    @Override
    public void restored() {
        service.load();
    }
}
