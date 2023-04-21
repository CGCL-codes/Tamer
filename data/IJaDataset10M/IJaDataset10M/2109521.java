package trstudio.beansmetric.core.metricsource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import trstudio.beansmetric.core.Avg;
import trstudio.beansmetric.core.Calculator;
import trstudio.beansmetric.core.CalculatorProcess;
import trstudio.beansmetric.core.Computable;
import trstudio.beansmetric.core.LevelSource;
import trstudio.beansmetric.core.Max;
import trstudio.beansmetric.core.Metric;
import trstudio.beansmetric.core.MetricType;
import trstudio.beansmetric.core.TRBeansMetric;
import trstudio.beansmetric.core.beansmetric.CompilationUnitElement;

/**
 * Représente la classe de base d'une source avec métrique.
 *
 * @author Sebastien Villemain
 * @author Frank Sauer
 */
public abstract class MetricSource implements Computable, Serializable {

    private final Map<String, MetricSource> children = new HashMap<String, MetricSource>();

    private final Map<MetricType, Metric> values = new EnumMap<MetricType, Metric>(MetricType.class);

    private final Map<String, Avg> averages = new HashMap<String, Avg>();

    private final Map<String, Max> maxima = new HashMap<String, Max>();

    protected String handle = null;

    private transient MetricSource parent = null;

    private CompilationUnitElement element = null;

    protected Dispatcher dispatcher = null;

    private boolean doRecurse = true;

    /**
	 * Initialisation du métrique.
	 *
	 * @param element
	 */
    public void initialize(CompilationUnitElement element, Dispatcher dispatcher) {
        this.element = element;
        this.handle = element.getHandle();
        this.dispatcher = dispatcher;
        children.clear();
        initializeRecurse();
    }

    /**
	 * Initialisation de tous les enfants.
	 */
    protected void initializeRecurse() {
        if (doRecurse) {
            initializeChildren();
            for (MetricSource source : children.values()) {
                if (!TRBeansMetric.isInterrupted()) {
                    source.initializeRecurse();
                }
            }
            compute(null);
            save();
            doRecurse = false;
        }
    }

    /**
	 * Retourne l'element source.
	 *
	 * @return
	 */
    public CompilationUnitElement getElement() {
        return element;
    }

    /**
	 * Ajoute un enfant.
	 *
	 * @param child
	 */
    public void addChild(MetricSource child) {
        if (child != null) {
            String handle = child.getHandle();
            if (!children.containsKey(handle)) {
                children.put(handle, child);
                child.parent = this;
            }
        } else {
            TRBeansMetric.LOGGER.log(Level.WARNING, "Unable to add child with NULL value [{0}].", getHandle());
        }
    }

    /**
	 * Retourne la liste des enfants.
	 *
	 * @return
	 */
    public Map<String, MetricSource> getChildren() {
        return children;
    }

    /**
	 * Retourne le parent attaché.
	 *
	 * @return
	 */
    public MetricSource getParent() {
        return parent;
    }

    /**
	 * Retourne tous les enfants ayant une valeur directe pour le métrique.
	 *
	 * @param per
	 * @param metric
	 * @return
	 */
    public MetricSource[] getChildrenHaving(LevelSource per, MetricType metric) {
        List<MetricSource> result = new ArrayList<MetricSource>();
        for (MetricSource src : children.values()) {
            if ((src.getValue(metric) != null) | (src.getMaximum(metric, per) != null) | (src.getAverage(metric, per) != null)) {
                result.add(src);
            }
        }
        MetricSource[] metricSources = new MetricSource[result.size()];
        return result.toArray(metricSources);
    }

    public void compute(MetricSource source) {
        List<CalculatorProcess> calculators = getCalculators();
        if (calculators != null) {
            for (CalculatorProcess calculator : calculators) {
                if (!TRBeansMetric.isInterrupted()) {
                    try {
                        calculator.compute(this);
                    } catch (OutOfMemoryError m) {
                        throw m;
                    } catch (Exception e) {
                        TRBeansMetric.LOGGER.log(Level.WARNING, "Unable to compute calculator.", e);
                    }
                }
            }
        }
    }

    /**
	 * Retourne l'identifiant de la source courante.
	 *
	 * @return
	 */
    public String getHandle() {
        return handle;
    }

    protected abstract void initializeChildren();

    /**
	 * Utilisé par les calculateurs pour stocker la valeur des résultats.
	 *
	 * @param value
	 */
    public void setValue(Metric value) {
        values.put(value.getId(), value);
    }

    /**
	 * Utilisé par les propagateurs pour stocker leurs résultats.
	 *
	 * @param value
	 */
    public void setAverage(Avg value) {
        averages.put(value.getScope().toString() + value.getId(), value);
    }

    /**
	 * Utilisé par les propagateurs pour stocker leurs résultats.
	 *
	 * @param value
	 */
    public void setMaximum(Max value) {
        maxima.put(value.getScope().toString() + value.getId(), value);
    }

    /**
	 * Retourne le métrique.
	 * Si aucune métrique trouvé, retourne <code>null</code>.
	 *
	 * @param id
	 * @return Metric or <code>null</code>.
	 */
    public Metric getValue(MetricType id) {
        Metric partial = null;
        if (values.containsKey(id)) {
            partial = values.get(id);
        }
        return partial;
    }

    /**
	 * Retourne tous les métriques.
	 *
	 * @return
	 */
    public Map<MetricType, Metric> getValues() {
        return values;
    }

    protected void save() {
        detachChildren();
    }

    /**
	 * Détache tous les enfants.
	 */
    private void detachChildren() {
        for (Iterator<MetricSource> i = children.values().iterator(); i.hasNext(); ) {
            MetricSource source = i.next();
            source.dispose();
            i.remove();
        }
        children.clear();
    }

    protected void dispose() {
        parent = null;
    }

    /**
	 * Retourne la liste de tous les métriques définis dans les enfants de ce noeud avec le nom donné.
	 *
	 * @param id
	 * @return
	 */
    public List<Metric> getMetricsFromChildren(MetricType id) {
        List<Metric> metrics = new ArrayList<Metric>();
        for (MetricSource source : children.values()) {
            Metric sourceMetric = source.getValue(id);
            if (sourceMetric != null) {
                metrics.add(sourceMetric);
            } else {
                TRBeansMetric.LOGGER.log(Level.INFO, "Metric {0} not found in {1}.", new Object[] { id, source.toString() });
            }
        }
        return metrics;
    }

    /**
	 * Retourne la liste des moyennes calculées définis dans les enfants de ce noeud avec le nom donné.
	 *
	 * @param name
	 * @param per
	 * @return
	 */
    public List<Avg> getAveragesFromChildren(MetricType name, LevelSource per) {
        List<Avg> averages = new ArrayList<Avg>();
        for (MetricSource source : children.values()) {
            Avg sourceAvg = source.getAverage(name, per);
            if (sourceAvg != null) {
                averages.add(sourceAvg);
            } else {
                TRBeansMetric.LOGGER.log(Level.INFO, "Average {0},{1} not found in {2}.", new Object[] { name, per.toString(), source.toString() });
            }
        }
        return averages;
    }

    /**
	 * Retourne la liste des valeurs maximum définis chez les enfants de ce noeud avec le nom donné.
	 *
	 * @param name
	 * @param per
	 * @return
	 */
    public List<Max> getMaximaFromChildren(MetricType name, LevelSource per) {
        List<Max> maxes = new ArrayList<Max>();
        for (MetricSource source : children.values()) {
            Max maximum = source.getMaximum(name, per);
            if (maximum != null) {
                maxes.add(maximum);
            }
        }
        return maxes;
    }

    /**
	 * Retourne la moyenne déjà calculé et mis en cache.
	 *
	 * @param name
	 * @param per
	 * @return
	 */
    public Avg getAverage(MetricType name, LevelSource per) {
        return averages.get(per.toString() + name.toString());
    }

    /**
	 * Retourne la liste des moyennes déjà calculées.
	 *
	 * @return
	 */
    public Map<String, Avg> getAverages() {
        return averages;
    }

    /**
	 * Retourne le maximum déjà calculé et mis en cache.
	 *
	 * @param name
	 * @param per
	 * @return Avg
	 */
    public Max getMaximum(MetricType name, LevelSource per) {
        return maxima.get(per.toString() + name.toString());
    }

    /**
	 * Retourne la liste des maximums déjà calculées.
	 *
	 * @return
	 */
    public Map<String, Max> getMaxima() {
        return maxima;
    }

    /**
	 * Niveau de la source.
	 *
	 * @return
	 */
    public abstract LevelSource getLevel();

    /**
	 * Liste des calulcateurs à executer pour cette source.
	 *
	 * @return
	 */
    private List<CalculatorProcess> getCalculators() {
        return Calculator.getCalculators(getLevel());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof MetricSource) {
            MetricSource source = (MetricSource) obj;
            return source.getHandle().equals(getHandle());
        }
        return false;
    }

    public int hashCode() {
        return getHandle().hashCode();
    }

    public String toString() {
        return getHandle();
    }
}
