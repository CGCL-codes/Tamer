package org.jtools.clan.impl;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jpattern.logging.Level;
import org.jtools.activate.CentralLoader;
import org.jtools.clan.Clan;
import org.jtools.clan.ClanException;
import org.jtools.clan.Clazz;
import org.jtools.clan.Functionality;
import org.jtools.clan.Method;
import org.jtools.clan.spi.ClanSpi;
import org.jtools.clan.spi.ClazzSpi;
import org.jtools.clan.spi.FunctionalityDescriptor;
import org.jtools.clan.spi.FunctionalityRealisation;
import org.jtools.info.InfoPublisher;
import org.jtools.util.ArrayClassname;
import org.jtools.util.Classname;
import org.jtools.util.PrimitiveClassname;
import org.jtools.util.SimpleClassname;

/**
 * implementation for org.jtools.clan.Clazz 
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class SimpleClazz implements ClazzSpi {

    /**
     * implementation for org.jtools.clan.Clazz.Helper.  
     * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
     */
    public class SimpleHelper implements Clazz.Helper {

        protected final Clazz clazz;

        private Classname name = null;

        private Boolean permitted = null;

        private boolean provided = false;

        private boolean required = false;

        private boolean used = false;

        public SimpleHelper(Clazz clazz) {
            this.clazz = clazz;
        }

        protected Classname createName() {
            return new SimpleClassname(clazz.getName().getPackage() + ".ClanHelper4" + clazz.getName().getCompilationUnit(false));
        }

        public synchronized Classname getName() {
            if (!isPermitted()) return null;
            if (name == null) name = createName();
            return name;
        }

        public boolean isPermitted() {
            if (isUsed()) return true;
            if (permitted != null) return permitted.booleanValue();
            return isRecommended();
        }

        public boolean isPossible() {
            return (!(clazz.isArray() || clazz.isPrimitive()));
        }

        public synchronized boolean isProvided() {
            return provided;
        }

        public boolean isRecommended() {
            if (!isPossible()) return false;
            return (!clazz.isDefined());
        }

        public synchronized boolean isRequired() {
            return required;
        }

        public synchronized boolean isUsed() {
            return used;
        }

        public synchronized void provide() {
            if (provided) return;
            provided = true;
        }

        public synchronized void require() {
            if (required) return;
            required = true;
            setPermitted(true);
        }

        public synchronized void setName(Classname name) {
            if (this.name != null) throw new ClanException("classname already set");
            this.name = name;
        }

        public void setPermitted(boolean onOff) {
            if (isUsed() && !onOff) throw new ClanException("Helper already used.");
            if (isRequired() && !onOff) throw new ClanException("Helper required.");
            permitted = (onOff ? Boolean.TRUE : Boolean.FALSE);
        }

        public synchronized void use() {
            if (!isPermitted()) throw new ClanException("Helper not allowed.");
            used = true;
        }
    }

    private final boolean array;

    private final ClanSpi clan;

    private boolean customFinal = false;

    private boolean defined = false;

    private Clazz directComponent = null;

    private final Map<String, Object> features = new HashMap<String, Object>(10);

    private final Map<Object, Functionality> functionality = new HashMap<Object, Functionality>();

    protected final Clazz.Helper helper;

    private Class jClass = null;

    private Classname name;

    private final boolean primitive;

    protected final String rawName;

    private final Map<Object, FunctionalityRealisation> realisation = new HashMap<Object, FunctionalityRealisation>();

    private Set<Object> usages = new HashSet<Object>(11);

    private boolean used = false;

    public SimpleClazz(ClanSpi clan, Class jClass) {
        this.clan = clan;
        this.jClass = jClass;
        this.array = jClass.isArray();
        this.primitive = jClass.isPrimitive();
        this.rawName = null;
        if (this.array) this.name = new ArrayClassname(); else if (this.primitive) this.name = new PrimitiveClassname(jClass.getName()); else this.name = new SimpleClassname(jClass.getName());
        this.helper = new SimpleHelper(this);
    }

    public SimpleClazz(ClanSpi clan, String name) {
        this.clan = clan;
        this.jClass = null;
        this.array = (name.indexOf('[') >= 0);
        this.primitive = false;
        this.rawName = name;
        if (this.array) this.name = new ArrayClassname(); else this.name = new SimpleClassname(name);
        this.helper = new SimpleHelper(this);
    }

    public void addUsage(Object usage) {
        if (usage != null) usages.add(usage);
    }

    protected Object createFeature(String feature) {
        if ((feature == null) || "".equals(feature)) return null;
        return null;
    }

    protected Functionality createFunctionality(FunctionalityRealisation realisation, boolean defined) {
        return realisation.getFactory().createFunctionality(this, realisation, defined);
    }

    public boolean equals(Object to) {
        if (to == this) return true;
        if (to == null) return false;
        if (!(to instanceof org.jtools.clan.impl.SimpleClazz)) return false;
        return getName().equals(((org.jtools.clan.Clazz) to).getName());
    }

    public synchronized Functionality get(FunctionalityDescriptor functionalityDescriptor, boolean defined) {
        Object key = functionalityDescriptor.getKey();
        Functionality o = functionality.get(key);
        if ((o == null) && !functionality.containsKey(key)) {
            FunctionalityRealisation realisation = getRealisation(key);
            o = realisation.getFactory().createFunctionality(this, realisation, defined);
            functionality.put(key, o);
        }
        return o;
    }

    public Functionality get(Object functionalityKey, boolean defined) {
        FunctionalityDescriptor desc = clan.getFunctionalityDescriptor(functionalityKey);
        return get(desc, defined);
    }

    public synchronized Method get(Object functionalityKey, Object methodKey) {
        Functionality o = get(functionalityKey, false);
        if (o == null) return null;
        return o.get(methodKey);
    }

    public synchronized String getArrayBrackets() {
        return name.getArrayBrackets();
    }

    public synchronized int getArrayLevel() {
        return name.getArrayLevel();
    }

    public final Clan getClan() {
        return clan;
    }

    public final Clazz getClazz() {
        return this;
    }

    public synchronized Clazz getComponent() {
        if (array) return directComponent.getComponent();
        return this;
    }

    public final synchronized Clazz getDirectComponent() {
        if (array) return directComponent;
        return this;
    }

    public final Object getFeature(String feature) {
        Object result;
        if (!features.containsKey(feature)) {
            result = createFeature(feature);
            features.put(feature, result);
        } else result = features.get(feature);
        return result;
    }

    public Clazz.Helper getHelper() {
        return helper;
    }

    public final Class getJClass() {
        return jClass;
    }

    public InfoPublisher getLogger() {
        return clan.getLogger();
    }

    public Classname getName() {
        return name;
    }

    public String getProperty(String key) {
        return clan.getProperty(key);
    }

    public String getProperty(String key, String defValue) {
        return clan.getProperty(key, defValue);
    }

    public Double getRating(boolean usedOnly, boolean direct) {
        double weights = 0.0;
        double rating = 0.0;
        double weight;
        Double fRating;
        Functionality f;
        boolean ratedFound = false;
        FunctionalityDescriptor[] fds = clan.getFunctionalityDescriptors();
        for (int i = 0; i < fds.length; i++) {
            f = get(fds[i], false);
            if (f != null) {
                fRating = f.getRating(usedOnly, direct);
                if (fRating != null) {
                    ratedFound = true;
                    weight = f.getRatingWeight(usedOnly);
                    weights += weight;
                    rating += weight * fRating.doubleValue();
                }
            }
        }
        if (!ratedFound) return null;
        return new Double(Math.max(0.0, Math.min(100.0, Math.round(rating / weights))));
    }

    public synchronized FunctionalityRealisation getRealisation(FunctionalityDescriptor functionalityDescriptor) {
        Object key = functionalityDescriptor.getKey();
        FunctionalityRealisation result = realisation.get(key);
        if ((result == null) && !realisation.containsKey(key)) {
            result = testFunctionality(functionalityDescriptor);
            realisation.put(key, result);
        }
        return result;
    }

    public synchronized FunctionalityRealisation getRealisation(Object functionalityKey) {
        FunctionalityDescriptor fd = clan.getFunctionalityDescriptor(functionalityKey);
        return getRealisation(fd);
    }

    public Set getUsages() {
        return usages;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public void initialize() {
        if (array) {
            if (jClass != null) directComponent = clan.get(jClass.getComponentType(), clan.getArrayComponentUsage(this)); else {
                if (rawName.startsWith("[")) {
                    String x = rawName.substring(1).trim();
                    if (x.startsWith("[")) directComponent = clan.get(x, clan.getArrayComponentUsage(this)); else if (x.startsWith("L") && x.endsWith(";")) directComponent = clan.get(x.substring(1, x.length() - 1), clan.getArrayComponentUsage(this)); else throw new RuntimeException("invalid format for array classname '" + rawName + "'");
                } else if (rawName.endsWith("]")) {
                    int p = rawName.lastIndexOf('[');
                    if (p < 0) throw new RuntimeException("invalid format for array classname '" + rawName + "'");
                    directComponent = clan.get(rawName.substring(0, p).trim(), clan.getArrayComponentUsage(this));
                } else throw new RuntimeException("invalid format for array classname '" + rawName + "'");
            }
            ((org.jtools.util.ArrayClassname) name).setElement(directComponent.getName());
        }
        boolean reqhelp = onInitialize();
        getHelper().setPermitted(reqhelp);
    }

    public final boolean isArray() {
        return array;
    }

    public boolean isAssignable(String interfaceclass) {
        if (jClass == null) return false;
        if (primitive) return false;
        try {
            return CentralLoader.loadClass(interfaceclass, this).isAssignableFrom(jClass);
        } catch (ClassNotFoundException e) {
            throw new ClanException("Clan.isAssignable: ClassNotFound '" + interfaceclass + "'.", e);
        } catch (NoClassDefFoundError ncde) {
            throw new ClanException("Clan.isAssignable: NoClassDefFound '" + interfaceclass + "'.", ncde);
        } catch (IncompatibleClassChangeError icce) {
            throw new ClanException("Clan.isAssignable: IncompatibleClassChange '" + interfaceclass + "'.", icce);
        }
    }

    public synchronized boolean isClassFound() {
        return (array || (jClass != null));
    }

    public synchronized boolean isDefined() {
        return defined;
    }

    public synchronized boolean isFinal() {
        if (array) return getComponent().isFinal();
        if (jClass == null) return customFinal;
        if (jClass.isInterface()) return false;
        if (Modifier.isFinal(jClass.getModifiers())) return true;
        return customFinal;
    }

    public final boolean isPrimitive() {
        return primitive;
    }

    public synchronized boolean isUsed() {
        return used;
    }

    public boolean onInitialize() {
        return false;
    }

    public void reinitialize() {
        if (jClass != null) return;
        if (array) return;
        try {
            jClass = CentralLoader.loadClass(getName().getName(false), this);
        } catch (ClassNotFoundException cnfe) {
            clan.getLogger().log(this, Level.VERBOSE, "reinitialize", cnfe, "ClassNotFound '" + getName().getName(false) + "'.");
            return;
        } catch (NoClassDefFoundError ncde) {
            ncde.printStackTrace();
            clan.getLogger().log(this, Level.VERBOSE, "reinitialize", ncde, "NoClassDefFound '" + getName().getName(false) + "'.");
            return;
        } catch (IncompatibleClassChangeError icce) {
            icce.printStackTrace();
            clan.getLogger().log(this, Level.VERBOSE, "reinitialize", icce, "IncompatibleClassChange '" + getName().getName(false) + "'.");
            return;
        }
    }

    public void setDefined(boolean onOff) {
        defined = onOff;
    }

    public synchronized void setFinal(boolean onoff) {
        customFinal = onoff;
    }

    public synchronized void setRealisation(Object functionalityKey, Object implementationKey, Object typeKey) {
        FunctionalityDescriptor fd = clan.getFunctionalityDescriptor(functionalityKey);
        if (fd == null) throw new ClanException("unknown functionalityKey '" + functionalityKey + "'");
        FunctionalityRealisation r = fd.getRealisation(implementationKey, typeKey);
        Object old = realisation.put(functionalityKey, r);
        if ((old != null) && !old.equals(r)) functionality.remove(functionalityKey);
    }

    /** return the realisationkey for a functionality */
    protected FunctionalityRealisation testFunctionality(FunctionalityDescriptor fd) {
        FunctionalityRealisation[] realisations = fd.getRealisations();
        for (int r = 0; r < realisations.length; r++) {
            if ((realisations[r] != null) && realisations[r].test(this)) return realisations[r];
        }
        throw new ClanException("invalid functionality declaration " + fd.getKey() + ": class " + this + " belongs to no realisation.");
    }

    public String toString() {
        return "Clazz " + getName().getName(false);
    }

    public synchronized void use() {
        used = true;
    }
}
