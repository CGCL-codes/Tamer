package org.scub.foundation.framework.base.mapping.util;

import java.util.List;
import net.sf.dozer.util.mapping.DozerBeanMapper;
import net.sf.dozer.util.mapping.converters.CustomConverter;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.RootLogger;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * Classe utilitaire pour le mapping entre objet.
 * @author Goumard Stephane (stephane.goumard@scub.net)
 */
public final class MapperDozerFactoryBean extends AbstractFactoryBean {

    /**
     * Liste des fichiers de configurations.
     */
    private List<String> mappingFiles;

    /**
     * Liste de custom conventer.
     */
    private List<CustomConverter> customConverters;

    /**
     * Singhleton mapper.
     */
    private MapperDozerBean singletonInstance;

    /**
     * Boolean singhleton.
     */
    private boolean singleton = true;

    /**
     * Logger.
     */
    private Logger logger = RootLogger.getLogger(MapperDozerFactoryBean.class);

    /**
     * Set the mappingFiles value.
     * @param mappingFiles the mappingFiles to set
     */
    public void setMappingFiles(List<String> mappingFiles) {
        logger.debug("Ajouter mapping files " + mappingFiles.size());
        this.mappingFiles = mappingFiles;
    }

    /**
     * Set the customConverters value.
     * @param customConverters the customConverters to set
     */
    public void setCustomConverters(List<CustomConverter> customConverters) {
        logger.debug("Ajouter custom converter" + customConverters.size());
        this.customConverters = customConverters;
    }

    /**
     * Retourne l'instance du singhleton.
     */
    private synchronized MapperDozerBean getSingletonInstance() {
        if (singletonInstance == null) {
            logger.info("Création d'un nouveau MapperDozerBean");
            if (mappingFiles != null) {
                for (String fileMapping : mappingFiles) {
                    logger.debug("Ajout d'un fichier de congiguration de mapping : " + fileMapping);
                }
            } else {
                logger.info("Aucun fichier de congiguration de mapping trouvé");
            }
            if (customConverters != null) {
                for (CustomConverter custom : customConverters) {
                    logger.debug("Ajout d'une classe spécifique de mapping : " + custom.getClass().getCanonicalName());
                }
            } else {
                logger.debug("Pas de classe spécifique de mapping trouvé");
            }
            singletonInstance = new MapperDozerBean(mappingFiles, customConverters);
        }
        return singletonInstance;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Class getObjectType() {
        return DozerBeanMapper.class;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSingleton() {
        return this.singleton;
    }

    /**
     * {@inheritDoc}
     */
    protected Object createInstance() throws Exception {
        return getSingletonInstance();
    }
}
