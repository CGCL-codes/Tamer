package org.objectwiz.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.client.UnitProxy;
import org.objectwiz.metadata.MappedClass;
import org.objectwiz.metadata.MappedProperty;
import org.objectwiz.representation.EntityRepresentation;

/**
 * Helper for analyzing the paths between entities of two different classes.
 *
 * It determines which are the properties (and sub-properties) of the origin class
 * (<code>fromClass</code>) that may lead to instances of the destination class
 * (<code>destClass</code>) and then retrieves all the possible associations between
 * instances of the two classes.
 *
 * Instances of the destination class that are linked to a specific entity of
 * the origin class can be retrieved using {@link #getForwardDependencies(Object)} and
 * the other way round using {@link #getBackwardDependencies(Object)}.
 * 
 * This object is not thread-safe.
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public class DependencyAnalyzer<E, F> {

    private static final Log logger = LogFactory.getLog(DependencyAnalyzer.class);

    private UnitProxy proxy;

    private EntityManager em;

    private MappedClass fromClass;

    private MappedClass destClass;

    private int maxEntries;

    private Collection<Route> routes;

    private Map<MappedClass, Set<MappedProperty>> referencingProperties;

    private int dependenciesCount;

    private Map<E, Set<F>> forwardDependencies = new HashMap<E, Set<F>>();

    private Map<F, Set<E>> backwardDependencies = new HashMap<F, Set<E>>();

    /**
     * Creates a new instance of this algorithm that returns a series of {@link PathFindingResult}
     * indexed by their origin. These results represent all the paths from all entities of
     * <code>fromClass</code> to all entities of <code>destClass</code>.     
     *
     * @param proxy              The proxy corresponding to the unit to analyze.
     * @param em                 The entity manager corresponding to the unit to analyze.
     * @param fromClass          The origins of the paths are all the instances of this class.
     * @param destClass          The destination of the paths are all the instances of this class.
     * @param maxEntries         The maximum number of references that will be fetched during one round.
     */
    public DependencyAnalyzer(UnitProxy proxy, EntityManager em, MappedClass fromClass, MappedClass destClass, List<String> specificRoutes, int maxEntries) {
        this.proxy = proxy;
        this.em = em;
        this.fromClass = fromClass;
        this.destClass = destClass;
        this.maxEntries = maxEntries;
        Date startTime = new Date();
        logger.info("Running new dependency analysis: " + fromClass.getClassName() + " <-> " + destClass.getClassName());
        EntityRepresentation currentRepresentation = proxy.getRepresentation();
        try {
            proxy.getApplication().changeRepresentation(EntityRepresentation.POJO);
            if (specificRoutes != null) {
                parseRoutes(specificRoutes);
            } else {
                findAllRoutes();
            }
            buildReferencingPropertiesMap();
            loadDependencies();
        } catch (Exception e) {
            logger.warn("Problem", e);
        } finally {
            proxy.getApplication().changeRepresentation(currentRepresentation);
        }
        long elapsed = (new Date().getTime() - startTime.getTime());
        logger.info("Finished running dependency analysis (took " + elapsed + " ms): " + dependenciesCount + " dependencies found");
    }

    /**
     * A route consists of a series of properties that can lead from
     * object of one class to objects of another class.
     */
    public class Route extends Stack<MappedProperty> {

        private MappedClass origin;

        private MappedClass destination;

        public Route(MappedClass origin, MappedClass destination) {
            this.origin = origin;
            this.destination = destination;
        }

        public MappedClass getOrigin() {
            return origin;
        }

        public MappedClass getDestination() {
            return destination;
        }

        @Override
        public synchronized String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append(origin.getClassName());
            buf.append("#");
            Iterator<MappedProperty> propertyIt = iterator();
            while (propertyIt.hasNext()) {
                buf.append(propertyIt.next().getName());
                buf.append(" -> ");
            }
            buf.append(destination.getClassName());
            return buf.toString();
        }
    }

    /**
     * A route is an instance of a {@link StructuralRoute} with actual objects.
     */
    private class Origin {

        private MappedClass clazz;

        private Object entity;

        public Origin(MappedClass clazz, Object entity) {
            this.clazz = clazz;
            this.entity = entity;
        }
    }

    public Collection<Route> getRoutes() {
        return routes;
    }

    public Collection<F> getForwardDependencies(E entity) {
        return getDependencies(forwardDependencies, fromClass, entity);
    }

    public Collection<E> getBackwardDependencies(F entity) {
        return getDependencies(backwardDependencies, destClass, entity);
    }

    private <H, G> Collection<H> getDependencies(Map<G, Set<H>> dependenciesMap, MappedClass expectedClass, G entity) {
        if (!expectedClass.isInstance(entity)) {
            throw new RuntimeException("Invalid entity, expected " + expectedClass.getClassName() + ", got: " + proxy.getMappedClass(entity));
        }
        Collection<H> dependencies = dependenciesMap.get(entity);
        if (dependencies == null) return new ArrayList();
        return dependencies;
    }

    private void parseRoutes(Collection<String> routeDefinitions) {
        routes = new ArrayList();
        for (String definition : routeDefinitions) {
            try {
                routes.add(parseRoute(definition));
            } catch (Exception e) {
                logger.warn("Error while parsing route, this route will be disabled", e);
            }
        }
    }

    private Route parseRoute(String definition) {
        logger.info("Parsing route: " + definition);
        Route route = null;
        MappedClass currentClass = null;
        StringTokenizer tokenizer = new StringTokenizer(definition, "->");
        while (tokenizer.hasMoreElements()) {
            String next = tokenizer.nextToken();
            StringTokenizer tokenizer2 = new StringTokenizer(next, "#");
            if (tokenizer2.countTokens() != 2) {
                throw new IllegalArgumentException("Invalid property definition: " + definition);
            }
            String className = tokenizer2.nextToken();
            String propertyName = tokenizer2.nextToken();
            MappedClass originClass = proxy.getMappedClassBySimpleName(className, true);
            if (currentClass != null && (!currentClass.isAssignableFrom(originClass))) {
                throw new RuntimeException("Invalid route definition: " + originClass + " is not a child of " + currentClass);
            }
            MappedProperty property = originClass.getProperty(propertyName);
            if (route == null) {
                route = new Route(originClass, destClass);
            }
            route.add(property);
            currentClass = getOppositeClass(property);
        }
        if (route == null || (!currentClass.isAssignableFrom(destClass))) {
            throw new RuntimeException("Invalid route, does not end with destination class");
        }
        return route;
    }

    /**
     * Step 1 - Analysis of the structure of the unit to determine all the possible
     *          routes available in the model from the origin class to the destination class.
     *
     *          Recursive algorithm: the routes that lead from origin class ORIG to destination
     *          class DEST are all the routes that lead to adjacent classes of ORIG to DEST preceded by
     *          the property of ORIG that leads to these adjacent classes.
     */
    private void findAllRoutes() {
        routes = findAllRoutesHelper(fromClass, new HashSet());
        logger.info("Total routes found: " + routes.size() + "\n" + displayRoutes());
    }

    private void buildReferencingPropertiesMap() {
        referencingProperties = new HashMap();
        for (Route route : routes) {
            Iterator<MappedProperty> chunkIt = route.iterator();
            while (chunkIt.hasNext()) {
                MappedProperty prop = chunkIt.next();
                MappedClass oppositeClass = getOppositeClass(prop);
                Set<MappedProperty> properties = referencingProperties.get(oppositeClass);
                if (properties == null) {
                    properties = new HashSet();
                    referencingProperties.put(oppositeClass, properties);
                }
                properties.add(prop);
            }
        }
    }

    private Collection<Route> findAllRoutesHelper(MappedClass origin, Set<MappedClass> classesToSkip) {
        boolean isInfoEnabled = logger.isInfoEnabled();
        if (origin.isAssignableFrom(destClass)) {
            return Arrays.asList(new Route(origin, origin));
        }
        Map<MappedProperty, MappedClass> outAssociations = new HashMap();
        for (MappedProperty property : origin.getMappedProperties(true, true)) {
            if (!property.getType().isAssociationType()) continue;
            if (property.isTransient() || property.isVirtual() || property.getType().isEnum() || property.getType().getAssociationType().isEmbedded()) continue;
            if (isInfoEnabled) logger.info("Potential association property: " + property.getFullName());
            MappedClass referencedClass = getOppositeClass(property);
            if (classesToSkip.contains(referencedClass)) {
                if (isInfoEnabled) logger.info("Class already referenced, skipping: " + origin.getClassName());
                return new ArrayList();
            }
            outAssociations.put(property, referencedClass);
        }
        Set<MappedClass> adjacentClasses = new HashSet<MappedClass>(outAssociations.values());
        if (isInfoEnabled) {
            logger.info("Classes that are adjacent to " + origin.getClassName() + ": " + adjacentClasses);
        }
        classesToSkip.addAll(origin.getClassHierarchy());
        Map<MappedClass, Collection<Route>> partialRoutesByClass = new HashMap();
        for (MappedClass adjacentClass : adjacentClasses) {
            logger.info("Finding routes for adjacent class: " + adjacentClass.getClassName());
            Collection<Route> partialRoutes = findAllRoutesHelper(adjacentClass, classesToSkip);
            partialRoutesByClass.put(adjacentClass, partialRoutes);
        }
        Collection<Route> fullRoutes = new ArrayList();
        for (Map.Entry<MappedProperty, MappedClass> association : outAssociations.entrySet()) {
            logger.info("Consolidating routes for: " + association.getKey());
            for (Route partialRoute : partialRoutesByClass.get(association.getValue())) {
                Route route = new Route(association.getKey().getMappedClass(), partialRoute.getDestination());
                route.add(association.getKey());
                route.addAll(partialRoute);
                fullRoutes.add(route);
                if (isInfoEnabled) {
                    logger.info("Route from: " + association.getKey().getMappedClass().getClassName() + ": " + route);
                }
            }
        }
        return fullRoutes;
    }

    private String displayRoutes() {
        StringBuilder buf = new StringBuilder();
        for (Route route : routes) {
            buf.append(route);
            buf.append("\n");
        }
        return buf.toString();
    }

    /**
     * Step 2 Using the structural paths that have been discovered at step 1, populate
     *        the map of paths incrementally. The destinations of the PathFindingResult
     *        in the map below are the destinations that are reached by the algorithm at
     *        a given iteration, not the final destinations.
     */
    private void loadDependencies() {
        Map<Origin, F> dependencies = new HashMap();
        for (Object destination : proxy.loadObjects(destClass, 0, maxEntries, null)) {
            dependencies.put(new Origin(destClass, destination), (F) destination);
        }
        logger.info("Added " + dependencies.size() + " destinations");
        int remainingPaths;
        int i = 0;
        do {
            remainingPaths = floodOnce(dependencies);
            if (logger.isInfoEnabled()) {
                logger.info("Iteration #" + (i++) + ": " + remainingPaths + " remaing paths to explore");
            }
        } while (remainingPaths > 0);
    }

    private int floodOnce(Map<Origin, F> dependencies) {
        boolean isInfoEnabled = logger.isInfoEnabled();
        Map<Origin, F> newDependencies = new HashMap();
        Iterator<Map.Entry<Origin, F>> dependenciesIt = dependencies.entrySet().iterator();
        while (dependenciesIt.hasNext()) {
            Map.Entry<Origin, F> entry = dependenciesIt.next();
            Origin origin = entry.getKey();
            F destination = entry.getValue();
            dependenciesIt.remove();
            for (MappedProperty referencingProperty : getReferencingProperties(origin.clazz)) {
                if (isInfoEnabled) {
                    logger.info("Looking for " + referencingProperty.getMappedClass().getClassSimpleName() + " that reference " + origin.entity + " with property: " + referencingProperty.getName());
                }
                Collection<Object> referencingEntities = loadEntitiesAssociatedToObject(referencingProperty, origin.entity);
                if (isInfoEnabled) {
                    logger.info("Found " + referencingEntities.size() + " referencing entities.");
                }
                for (Object referencingEntity : referencingEntities) {
                    if (fromClass.isInstance(referencingEntity)) {
                        logger.info("** Found dependency: " + referencingEntity + " <-> " + destination);
                        registerDependency(forwardDependencies, (E) referencingEntity, destination);
                        registerDependency(backwardDependencies, destination, (E) referencingEntity);
                        dependenciesCount++;
                        continue;
                    }
                    logger.info("Registering new path: " + referencingEntity + " -> " + destination);
                    newDependencies.put(new Origin(proxy.getMappedClass(referencingEntity), referencingEntity), destination);
                }
            }
        }
        dependencies.putAll(newDependencies);
        return newDependencies.size();
    }

    private Collection<MappedProperty> getReferencingProperties(MappedClass clazz) {
        Collection<MappedProperty> properties = new ArrayList();
        MappedClass mc = clazz;
        while (mc != null) {
            if (referencingProperties.containsKey(mc)) {
                properties.addAll(referencingProperties.get(mc));
            }
            mc = mc.getParentMappedClass();
        }
        return properties;
    }

    private <G, H> void registerDependency(Map<G, Set<H>> map, G key, H value) {
        Set<H> values = map.get(key);
        if (values == null) {
            values = new HashSet();
            map.put(key, values);
        }
        values.add(value);
    }

    private Collection<Object> loadEntitiesAssociatedToObject(MappedProperty property, Object target) {
        if (property.getType().isCollection()) {
            return em.createQuery("select e from " + property.getMappedClass().getClassName() + " e, " + "IN (e." + property.getName() + ") a where a=:target").setParameter("target", target).getResultList();
        } else {
            return em.createQuery("select e from " + property.getMappedClass().getClassName() + " e " + "where e." + property.getName() + "=:target").setParameter("target", target).getResultList();
        }
    }

    private MappedClass getOppositeClass(MappedProperty property) {
        return property.getType().getAssociationType().getAssociatedMappedClass();
    }
}
