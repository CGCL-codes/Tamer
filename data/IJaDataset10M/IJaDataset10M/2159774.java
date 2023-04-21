package br.ufba.sysaco.init;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import br.gov.frameworkdemoiselle.annotation.Shutdown;
import br.gov.frameworkdemoiselle.annotation.Startup;
import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.ufba.sysaco.business.EnderecoBC;
import br.ufba.sysaco.business.UnidadeSaudeBC;

@ApplicationScoped
public class ApplicationLoader {

    @Inject
    private EnderecoBC enderecoBC;

    @Startup
    @Transactional
    public void load() {
    }

    @Shutdown
    public void unload() {
    }
}
