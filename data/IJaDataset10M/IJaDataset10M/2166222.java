package eu.planets_project.ifr.core.registry.api;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import eu.planets_project.ifr.core.techreg.formats.FormatRegistry;
import eu.planets_project.ifr.core.techreg.formats.FormatRegistryFactory;
import eu.planets_project.services.datatypes.Queryable;
import eu.planets_project.services.datatypes.ServiceDescription;

/**
 * Query by example functionality for service description instances.
 * @see eu.planets_project.ifr.core.registry.api.CoreRegistryTests
 * @author Fabian Steeg (fabian.steeg@uni-koeln.de)
 */
final class Query {

    private List<ServiceDescription> descriptions = null;

    private static Log log = LogFactory.getLog(Query.class.getName());

    /**
     * @param descriptions The descriptions to query
     */
    Query(final List<ServiceDescription> descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @param sample The sample description
     * @param mode The matching strategy to use
     * @return Returns the service descriptions corresponding to the given sample instance
     */
    List<ServiceDescription> byExample(final ServiceDescription sample, final MatchingMode mode) {
        List<ServiceDescription> result = new ArrayList<ServiceDescription>();
        for (ServiceDescription description : descriptions) {
            if (matches(description, sample, mode)) {
                result.add(description);
            }
        }
        return result;
    }

    /**
     * Compares a candidate and a sample instance reflectively, using a marker annotation. This means the
     * ServiceDescription class can be extended by new values and these values can be used for the query by example
     * functionality without changing this class.
     * @param candidate The candidate service description
     * @param sample The query-by-example sample instance
     * @param mode The match mode to use
     * @return True, if the given candidate is described by the given sample, else false
     */
    private static boolean matches(final ServiceDescription candidate, final ServiceDescription sample, final MatchingMode mode) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate service description is null!");
        }
        if (!Access.authorized(candidate)) {
            return false;
        }
        if (sample == null) {
            return true;
        }
        Class<? extends ServiceDescription> clazz = sample.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getAnnotation(Queryable.class) != null) {
                try {
                    Object sampleValue = method.invoke(sample);
                    Object candidateValue = method.invoke(candidate);
                    String message = String.format("Comparing values for method '%s' of query example '%s' to candidate instance '%s'.", method.getName(), sampleValue, candidateValue);
                    log.debug(message);
                    if (sampleValue != null) {
                        if (candidateValue == null) {
                            return false;
                        } else {
                            if (sampleValue instanceof Collection<?> && candidateValue instanceof Collection<?>) {
                                Collection<?> cands = (Collection<?>) candidateValue;
                                Collection<?> samps = (Collection<?>) sampleValue;
                                if (!containsAll(cands, samps, mode)) {
                                    return false;
                                }
                            } else {
                                if (!(mode.matches(candidateValue.toString(), sampleValue.toString()))) {
                                    return false;
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * @param cands The candidate collection
     * @param samps The sample collection
     * @param mode The matching mode to use
     * @return True, if all the sample elements match a candidate element
     */
    private static boolean containsAll(final Collection<?> cands, final Collection<?> samps, final MatchingMode mode) {
        int matched = 0;
        for (Object sample : samps) {
            for (Object candidate : cands) {
                if (candidate instanceof URI && sample instanceof URI) {
                    if (mappable((URI) candidate, (URI) sample, mode)) {
                        matched++;
                        break;
                    }
                }
                if (mode.matches(candidate.toString(), sample.toString())) {
                    matched++;
                    break;
                }
            }
        }
        return matched == samps.size();
    }

    /**
     * @param candidate The candidate registry entry
     * @param sample The query sample pattern
     * @param mode The query mode
     * @return True, if the sample can be mapped onto the candidate URI
     */
    private static boolean mappable(final URI candidate, final URI sample, final MatchingMode mode) {
        if (mode.matches(candidate.toString(), sample.toString())) {
            return true;
        }
        FormatRegistry registry = FormatRegistryFactory.getFormatRegistry();
        List<URI> candidateAliases = registry.getFormatUriAliases(candidate);
        List<URI> sampleAliases = registry.getFormatUriAliases(sample);
        for (URI sampleAlias : sampleAliases) {
            for (URI candidateAlias : candidateAliases) {
                if (mode.matches(candidateAlias.toString(), sampleAlias.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
