package de.lema.dao;

import javax.persistence.EntityManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import de.lema.bo.XmlConfiguration;

public class XmlConfigurationDao {

    @Inject
    private Provider<EntityManager> emProv;

    public XmlConfiguration find(String key) {
        EntityManager em = emProv.get();
        XmlConfiguration find = em.find(XmlConfiguration.class, key);
        em.close();
        return find;
    }
}
