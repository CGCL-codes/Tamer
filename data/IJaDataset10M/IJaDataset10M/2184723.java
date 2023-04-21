package org.jazzteam.jpatterns.core.configuration.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jazzteam.jpatterns.core.JPConstants;
import org.jazzteam.jpatterns.core.configuration.PropertiesBasedFactory;
import org.jazzteam.jpatterns.core.configuration.PropertiesProvider;
import org.jazzteam.jpatterns.core.configuration.exceptions.JPConfigException;
import org.jazzteam.jpatterns.core.configuration.exceptions.JPInitializationException;
import org.jazzteam.jpatterns.schema.CastorConfigType;
import org.jazzteam.jpatterns.schema.CastorFactoryType;
import org.jazzteam.jpatterns.schema.CastorGroupType;
import org.jazzteam.jpatterns.schema.CastorGroupTypeItem;
import org.jazzteam.jpatterns.schema.CastorNameScopePriorityType;
import org.jazzteam.jpatterns.schema.CastorSectionType;
import org.jazzteam.jpatterns.schema.Item;
import org.jazzteam.jpatterns.schema.JPatternsConfig;
import org.jazzteam.jpatterns.utils.CastorUtils;
import com.zmicer.utils.InputArgumentUtils;
import com.zmicer.utils.ObjectStateUtils;

/**
 * Default, "native" implementation of the appropriate interface.
 * 
 * $Author:: zmicer $<br/>
 * $Rev:: 67 $<br/>
 * * $Date:: 2007-08-28 21:37:07 #$<br/>
 * $Date:: 2007-08-28 21:37:07 #$<br/>
 */
public class JPatternsConfigBeansBuilderImpl implements IJPatternsConfigBeansBuilder {

    /**
	 * Logger instance.
	 */
    public static final Logger LOG = Logger.getLogger(JPatternsConfigBeansBuilderImpl.class);

    /**
	 * value for the
	 * org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_OVERRIDING_DEPTH property
	 */
    private static String OverridingDepth;

    /**
	 * value for the org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.
	 * XML_CONFIG_CUSTOM_OVERRIDES_NOT_DEPENDING_ON_PRIORITY
	 */
    private static boolean OverrideNotDependingOnPriority;

    /**
	 * Initialization block.
	 */
    {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance(PropertiesBasedFactory.getInstance().getPropertiesManager(), false);
        final String depth = propertiesProvider.getStringProperty(org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_OVERRIDING_DEPTH);
        JPatternsConfigBeansBuilderImpl.setOverridingDepth((null == depth) ? PropertiesProvider.OverridingDepths.OVERRIDING_LEVEL_SECTION.toString() : depth);
        final Boolean property = propertiesProvider.getBooleanProperty(org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_CUSTOM_OVERRIDES_NOT_DEPENDING_ON_PRIORITY);
        JPatternsConfigBeansBuilderImpl.setOverrideNotDependingOnPriority((null != property) && property);
    }

    /**
	 * @see IJPatternsConfigBeansBuilder#build(org.jazzteam.jpatterns.schema.JPatternsConfig) <br/>
	 * 
	 *      <pre>
	 * The algorithm is used here as follows:
	 * <strong>Sections filling</strong>:
	 * 1. the List of "section based" objects is formed<br/>
	 *      - in the case the section has not explicit scope - the default one from the root castor object or the
	 *      {@link org.jazzteam.jpatterns.core.JPConstants.DEFAULT_SCOPE_NAME} is set.
	 * 2. all the "section based" objects are put to the map
	 *      - in the case {@link org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_OVERRIDING_DEPTH}
	 * property equals to
	 * <code>org.jazzteam.jpatterns.core.configuration.PropertiesProvider.OverridingDepths.OVERRIDING_LEVEL_ITEM</code> then in the case
	 * of two identical sections (scopes, section name and priority), they would be merged to the one by joining the appropriate business
	 * items. If there are identical business items (scopes, name and priority) - the
	 * <code>org.jazzteam.jpatterns.core.configuration.exceptions.JPInitializationException</code> exception would appear
	 *      - in the case {@link org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_OVERRIDING_DEPTH}
	 * property equals to
	 * <code>org.jazzteam.jpatterns.core.configuration.PropertiesProvider.OverridingDepths.OVERRIDING_LEVEL_SECTION</code> then in the
	 * case of two identical sections (scopes, section name and priority)
	 * <code>org.jazzteam.jpatterns.core.configuration.exceptions.JPConfigException</code> exception would appear
	 *      - in the case of the identical pathes but different priorities they would be taken into consideration (in this case
	 * org.jazzteam.jpatterns.core.configuration.PropertiesProvider.JPProperties.XML_CONFIG_CUSTOM_OVERRIDES_NOT_DEPENDING_ON_PRIORITY
	 * would be considered too).
	 * <p/>
	 *      <strong>Business items filling</strong>:
	 *      todo [zmicer]: <<t.b.d.>>
	 * <p/>
	 * note [zmicer]: please be noticed the Section with the certain scope is allowed to store the business items with different scopes
	 * </pre>
	 */
    @Override
    public JPatternsConfigBean build(final JPatternsConfig config) {
        InputArgumentUtils.checkObjects(config);
        final JPatternsConfigBean result = new JPatternsConfigBean();
        result.setDefaultScope(config.getDefaultScope());
        result.setCastorConfig(config);
        CastorUtils.validateAndNormalizeScopesPriorities(config);
        fill(result, CastorUtils.extractCastorSectionTypeObjects(config));
        return result;
    }

    /**
	 * @see IJPatternsConfigBeansBuilder#build(org.jazzteam.jpatterns.schema.JPatternsConfig)
	 */
    @Override
    public JPatternsConfigsBean build(final List<JPatternsConfigBaseBean> beans) {
        InputArgumentUtils.checkObjects(beans);
        final JPatternsConfigsBean result = new JPatternsConfigsBean();
        final List<CastorSectionType> sections = new ArrayList<CastorSectionType>();
        for (final JPatternsConfigBaseBean bean : beans) {
            ObjectStateUtils.strongCheck(bean);
            sections.addAll(bean.getListOfSectionItems());
        }
        fill(result, sections);
        return result;
    }

    /**
	 * Please review the Java Docs to the method {@link fill} todo [zmicer]: adjust Java Doc todo [zmicer]: think if
	 * this method could be moved to interface of the builder too
	 * 
	 * @param initialBaseBean
	 *            the initial bean to fill. Can not be null (otherwise <code>IllegalArgumentException</code> would
	 *            appear).
	 * @param sections
	 *            the List of the CastorSectionType objects. They should be normalized in the sense of scopes and
	 *            priorities(for details study
	 *            {@link org.jazzteam.jpatterns.utils.CastorUtils#validateAndNormalizeScopesPriorities(JPatternsConfig)}
	 *            ). Can not be null (otherwise <code>IllegalArgumentException</code> would appear).
	 */
    protected void fill(final JPatternsConfigBaseBean initialBaseBean, final List<CastorSectionType> sections) {
        InputArgumentUtils.checkObjects(initialBaseBean, sections);
        ObjectStateUtils.strongCheck(initialBaseBean);
        for (final CastorSectionType section : sections) {
            if ((null == section.getScope()) && (null == section.getName())) {
                throw new JPConfigException("The scope and name of CastorSectionType can not be null.");
            }
            final CastorSectionType alreadySection = initialBaseBean.getSection(section.getScope(), section.getName());
            if ((null == alreadySection) || (!section.getScope().equals(alreadySection.getScope()) || !section.getName().equals(alreadySection.getName()))) {
                initialBaseBean.setSection(section.getScope(), section);
            } else {
                initialBaseBean.setSection(section.getScope(), (CastorSectionType) choiceOrMergeCastorNameScopePriorityTypes(section, alreadySection));
            }
        }
        final Map<String, Map<String, CastorSectionType>> sectionItems = initialBaseBean.getSectionItems();
        for (final String sectionScope : sectionItems.keySet()) {
            final Map<String, CastorSectionType> sectionsMap = sectionItems.get(sectionScope);
            for (final String sectionName : sectionsMap.keySet()) {
                final CastorSectionType section = sectionsMap.get(sectionName);
                if (StringUtils.isBlank(sectionScope)) {
                    throw new JPInitializationException("Section should have scope set.");
                }
                final List<CastorNameScopePriorityType> items = CastorUtils.extractCastorNameScopePriorityTypeObjects(section);
                for (final CastorNameScopePriorityType item : items) {
                    if ((null == item.getScope()) && (null == section.getName())) {
                        throw new JPConfigException("The scope and name of Item can not be null.");
                    }
                    final CastorNameScopePriorityType alreadyItem = initialBaseBean.getBusinessItem(sectionScope, sectionName, item.getScope(), item.getName());
                    if (null == alreadyItem) {
                        initialBaseBean.setBusinessItem(sectionScope, sectionName, item);
                    } else {
                        initialBaseBean.setBusinessItem(sectionScope, sectionName, choiceOrMergeCastorNameScopePriorityTypes(item, alreadyItem));
                    }
                }
            }
        }
    }

    /**
	 * Choice one of the given CastorNameScopePriorityType castor objects or perform the merging if it is necessary.
	 * Please review the comments to the main <code>fill</code> method to understand how it works (depending on the
	 * mentioned properties etc.) <br/>
	 * These objects should have the equal pathes to be passed here (scope, name) - otherwise the
	 * IllegalArgumentException appear
	 * 
	 * @param castor1
	 *            the first object, Can not be null (otherwise <code>IllegalArgumentException</code> would appear).
	 * @param castor2
	 *            the second object, Can not be null (otherwise <code>IllegalArgumentException</code> would appear).
	 * 
	 * @return the object is more prioritized here
	 */
    protected CastorNameScopePriorityType choiceOrMergeCastorNameScopePriorityTypes(final CastorNameScopePriorityType castor1, final CastorNameScopePriorityType castor2) {
        InputArgumentUtils.checkObjects(castor1, castor2);
        InputArgumentUtils.checkStrings(true, castor1.getScope(), castor1.getName(), castor1.getPriority());
        InputArgumentUtils.checkStrings(true, castor2.getScope(), castor2.getName(), castor2.getPriority());
        if (!castor1.getScope().equals(castor2.getScope()) || !castor1.getName().equals(castor2.getName())) {
            throw new IllegalArgumentException("The provided object should have the identical pathes to be checked / compared.");
        }
        if (!castor1.getClass().equals(castor2.getClass())) {
            throw new IllegalArgumentException("The classes of the provided objects should be identical.");
        }
        if (castor1.getPriority().equals(castor2.getPriority())) {
            if (!(castor1 instanceof CastorSectionType)) {
                throw new JPConfigException("The elements with identical pathes can not be merged - scope [" + castor1.getScope() + "], name [" + castor1.getName() + "], priority [" + castor1.getPriority() + "]; class name " + "is [" + castor1.getClass().getName() + "]");
            } else {
                if (JPatternsConfigBeansBuilderImpl.OverridingDepth.equals(org.jazzteam.jpatterns.core.configuration.PropertiesProvider.OverridingDepths.OVERRIDING_LEVEL_SECTION.toString())) {
                    throw new JPConfigException("The elements with identical pathes can not be merged - scope [" + castor1.getScope() + "], name [" + castor1.getName() + "], priority [" + castor1.getPriority() + "]; class " + "name is [" + castor1.getClass().getName() + "]");
                } else if (JPatternsConfigBeansBuilderImpl.OverridingDepth.equals(org.jazzteam.jpatterns.core.configuration.PropertiesProvider.OverridingDepths.OVERRIDING_LEVEL_ITEM.toString())) {
                    final List<CastorNameScopePriorityType> items = CastorUtils.extractCastorNameScopePriorityTypeObjects((CastorSectionType) castor2);
                    for (final CastorNameScopePriorityType item : items) {
                        if (castor1 instanceof CastorConfigType) {
                            if (!(item instanceof Item)) {
                                throw new JPConfigException("The CastorNameScopePriorityType item should be of Item type in the " + "case castor section object is of the type CastorConfigType");
                            }
                            ((CastorConfigType) castor1).addItem((Item) item);
                        } else if (castor1 instanceof CastorFactoryType) {
                            if (!(item instanceof Item)) {
                                throw new JPConfigException("The CastorNameScopePriorityType item should be of Item type in the " + "case castor section object is of the type CastorFactoryType");
                            }
                            ((CastorFactoryType) castor1).addItem((Item) item);
                        } else if (castor1 instanceof CastorGroupType) {
                            final CastorGroupTypeItem groupItem = CastorUtils.constructGroupItem(item);
                            ((CastorGroupType) castor1).addCastorGroupTypeItem(groupItem);
                        }
                    }
                    return castor1;
                }
            }
        } else {
            final int firstPriority = CastorUtils.getPriority(castor1);
            final int secondPriority = CastorUtils.getPriority(castor2);
            boolean firstPrioritized = false;
            boolean secondPrioritized = false;
            if (castor1.getPriority().startsWith(JPConstants.PRIORITIZED_PRIOTITY_PREFIX)) {
                firstPrioritized = true;
            }
            if (castor2.getPriority().startsWith(JPConstants.PRIORITIZED_PRIOTITY_PREFIX)) {
                secondPrioritized = true;
            }
            if ((firstPrioritized && secondPrioritized) || (!firstPrioritized && !secondPrioritized)) {
                return (firstPriority > secondPriority) ? castor1 : castor2;
            } else if (firstPrioritized) {
                if (JPatternsConfigBeansBuilderImpl.OverrideNotDependingOnPriority) {
                    return castor1;
                } else {
                    return (firstPriority > secondPriority) ? castor1 : castor2;
                }
            } else {
                if (JPatternsConfigBeansBuilderImpl.OverrideNotDependingOnPriority) {
                    return castor2;
                } else {
                    return (firstPriority > secondPriority) ? castor1 : castor2;
                }
            }
        }
        return null;
    }

    /**
	 * Set the overriding depth. This method is necessary for the unit testing.
	 * 
	 * @param depth
	 *            the value we ned to set, Can not be null (otherwise <code>IllegalArgumentException</code> would
	 *            appear).
	 */
    protected static void setOverridingDepth(final String depth) {
        InputArgumentUtils.checkStrings(true, depth);
        JPatternsConfigBeansBuilderImpl.OverridingDepth = depth;
    }

    /**
	 * @return overriding depth
	 */
    protected static String getOverridingDepth() {
        return JPatternsConfigBeansBuilderImpl.OverridingDepth;
    }

    /**
	 * Set the OverrideNotDependingOnPriority static field with the new value. This method is necessary for unit
	 * testing.
	 * 
	 * @param value
	 *            new boolean value
	 */
    protected static void setOverrideNotDependingOnPriority(final boolean value) {
        JPatternsConfigBeansBuilderImpl.OverrideNotDependingOnPriority = value;
    }

    /**
	 * @return the value of OverrideNotDependingOnPriority
	 */
    protected static boolean getOverrideNotDependingOnPriority() {
        return JPatternsConfigBeansBuilderImpl.OverrideNotDependingOnPriority;
    }
}
