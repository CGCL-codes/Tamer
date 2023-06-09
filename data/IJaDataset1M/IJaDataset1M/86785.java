package org.codehaus.groovy.grails.plugins.searchable.compass.config.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.groovy.grails.commons.GrailsDomainClass;
import org.codehaus.groovy.grails.plugins.searchable.SearchableUtils;
import org.codehaus.groovy.grails.plugins.searchable.compass.mapping.*;
import org.compass.core.config.CompassConfiguration;
import org.springframework.util.Assert;
import org.springframework.core.Ordered;
import java.io.InputStream;
import java.util.*;

/**
 * Configures Compass with searchable domain classes according to a "searchable" class property value
 *
 * @author Maurice Nicholson
 */
public class SearchableClassPropertySearchableGrailsDomainClassMappingConfigurator implements SearchableGrailsDomainClassMappingConfigurator, Ordered {

    private static final Log LOG = LogFactory.getLog(SearchableClassPropertySearchableGrailsDomainClassMappingConfigurator.class);

    private SearchableGrailsDomainClassCompassClassMapper classMapper;

    private SearchableCompassClassMappingXmlBuilder compassClassMappingXmlBuilder;

    /**
     * Returns the collection of GrailsDomainClasses that are mapped by this instance
     * @param grailsDomainClasses ALL domain classes
     * @return the collection of domain classes mapped by this instance
     */
    public Collection getMappedBy(Collection grailsDomainClasses) {
        Set mappedBy = new HashSet();
        for (Iterator iter = grailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
            Object value = SearchableUtils.getSearchablePropertyValue(grailsDomainClass);
            if (value != null) {
                if (!((value instanceof Boolean) && value.equals(Boolean.FALSE))) {
                    mappedBy.add(grailsDomainClass);
                }
                continue;
            }
            if (SearchableUtils.isEmbeddedPropertyOfOtherDomainClass(grailsDomainClass, grailsDomainClasses)) {
                mappedBy.add(grailsDomainClass);
            }
        }
        return mappedBy;
    }

    /**
     * Configure the Mapping in the CompassConfiguration for the given domain class
     *
     * @param compassConfiguration          the CompassConfiguration instance
     * @param configurationContext          a configuration context, for flexible parameter passing
     * @param searchableGrailsDomainClasses searchable domain classes to map
     */
    public void configureMappings(CompassConfiguration compassConfiguration, Map configurationContext, Collection searchableGrailsDomainClasses) {
        Assert.notNull(classMapper, "classMapper cannot be null");
        Assert.notNull(compassClassMappingXmlBuilder, "compassClassMappingXmlBuilder cannot be null");
        List classMappings = new ArrayList();
        for (Iterator iter = searchableGrailsDomainClasses.iterator(); iter.hasNext(); ) {
            GrailsDomainClass grailsDomainClass = (GrailsDomainClass) iter.next();
            CompassClassMapping classMapping = classMapper.getCompassClassMapping(grailsDomainClass, searchableGrailsDomainClasses);
            classMappings.add(classMapping);
        }
        CompassMappingUtils.resolveAliases(classMappings, searchableGrailsDomainClasses);
        for (Iterator iter = classMappings.iterator(); iter.hasNext(); ) {
            CompassClassMapping classMapping = (CompassClassMapping) iter.next();
            InputStream inputStream = compassClassMappingXmlBuilder.buildClassMappingXml(classMapping);
            LOG.debug("Adding [" + classMapping.getMappedClass().getName() + "] mapping to CompassConfiguration");
            compassConfiguration.addInputStream(inputStream, classMapping.getMappedClass().getName().replaceAll("\\.", "/") + ".cpm.xml");
        }
    }

    /**
     * Get this strategy's name
     *
     * @return name
     */
    public String getName() {
        return "searchable class property";
    }

    public void setMappingDescriptionProviderManager(CompositeSearchableGrailsDomainClassCompassClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    public void setCompassClassMappingXmlBuilder(SearchableCompassClassMappingXmlBuilder compassClassMappingXmlBuilder) {
        this.compassClassMappingXmlBuilder = compassClassMappingXmlBuilder;
    }

    /**
     * Determines the order of this mapping configurator in relation to others
     * @return the order
     */
    public int getOrder() {
        return 1;
    }
}
