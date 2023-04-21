package nl.tranquilizedquality.adm.gwt.gui.server.service.artifact;

import nl.tranquilizedquality.adm.commons.business.domain.MavenModule;
import nl.tranquilizedquality.adm.commons.domain.factory.AbstractFactory;
import nl.tranquilizedquality.adm.core.persistence.db.hibernate.bean.HibernateMavenModule;
import nl.tranquilizedquality.adm.gwt.gui.client.model.artifact.ClientMavenModule;

/**
 * Factory that transforms client beans into persistent beans and vice versa.
 * 
 * @author Salomo Petrus (salomo.petrus@gmail.com)
 * @since 24 sep. 2011
 */
class MavenModuleFactory extends AbstractFactory<ClientMavenModule, HibernateMavenModule, MavenModule> {

    @Override
    protected ClientMavenModule createNewClientBean() {
        return new ClientMavenModule();
    }

    @Override
    protected HibernateMavenModule createNewPersistentBean() {
        return new HibernateMavenModule();
    }
}
