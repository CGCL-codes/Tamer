package com.metanology.mde.core.metaModel;

import java.io.File;
import java.util.Vector;
import java.util.Enumeration;

/**
 * A model.
 */
public class Model extends ModelElement {

    /**
     * The version of the model.
     */
    public String version = "";

    /**
     * The pathname or the root directory where MetaProgram files are created.
     */
    public String outputPath = "";

    private SubsystemCollection subsystems = null;

    private PackageCollection packages = null;

    private AssociationCollection associations = null;

    private GeneralizationCollection generalizations = null;

    private RealizationCollection realizations = null;

    private ComponentCollection components = null;

    private MetaClassCollection metaClasses = null;

    /**
	 *  Get version.
	 */
    public String getVersion() {
        return this.version;
    }

    /**
	 *  Set version.
	 */
    public void setVersion(String newVal) {
        this.version = newVal;
    }

    /**
	 *  Get outputPath.
	 */
    public String getOutputPath() {
        return this.outputPath;
    }

    /**
	 *  Set outputPath.
	 */
    public void setOutputPath(String newVal) {
        this.outputPath = newVal;
    }

    /**
     * get the collection of Subsystem.
	 * The Subsystems in this Model.
     */
    public SubsystemCollection getSubsystems() {
        if (this.subsystems == null) {
            this.subsystems = new SubsystemCollection();
        }
        if (cache != null) {
            this.subsystems = cache.getAllSubsystems();
        }
        return this.subsystems;
    }

    public void setSubsystems(SubsystemCollection newVal) {
        this.subsystems = newVal;
    }

    /**
     * get the collection of Package.
	 * The Package instances representing the packages in the object model of a
	 * software system represented by this Model.
     */
    public PackageCollection getPackages() {
        if (this.packages == null) {
            this.packages = new PackageCollection();
        }
        if (cache != null) {
            this.packages = cache.getAllPackages();
        }
        return this.packages;
    }

    public void setPackages(PackageCollection newVal) {
        this.packages = newVal;
    }

    /**
     * get the collection of Association.
	 * All the associations in the model.
	 * The returned collection is readonly. 
	 * Adding/removing element will not affect the actual model.
     */
    public AssociationCollection getAssociations() {
        AssociationCollection assocs = new AssociationCollection();
        for (java.util.Iterator i = this.getMetaClasses().iterator(); i.hasNext(); ) {
            MetaClass mclass = (MetaClass) i.next();
            for (java.util.Iterator ii = mclass.getRoles().iterator(); ii.hasNext(); ) {
                AssociationRole r = (AssociationRole) ii.next();
                Association a = r.getAssociation();
                if (!assocs.contains(a)) {
                    assocs.add(a);
                }
            }
        }
        return assocs;
    }

    public void setAssociations(AssociationCollection newVal) {
        this.associations = newVal;
    }

    /**
     * get the collection of Generalization.
	 * A collection of all the Generalizations in this model.
	 * The returned collection is readonly. Adding/removing element will not affect
	 * the actual model.
     */
    public GeneralizationCollection getGeneralizations() {
        if (this.generalizations == null) {
            this.generalizations = new GeneralizationCollection();
        }
        this.generalizations.clear();
        for (java.util.Iterator i = this.getMetaClasses().iterator(); i.hasNext(); ) {
            MetaClass mclass = (MetaClass) i.next();
            for (java.util.Iterator ii = mclass.getGeneralizations().iterator(); ii.hasNext(); ) {
                this.generalizations.add(ii.next());
            }
        }
        return this.generalizations;
    }

    public void setGeneralizations(GeneralizationCollection newVal) {
        this.generalizations = newVal;
    }

    /**
     * get the collection of Realization.
	 * The Realization instances representing the realizations in the object model
	 * of a software system represented by this Model.
	 * The returned collection is readonly. Adding/removing element will not affect
	 * the actual model.
     */
    public RealizationCollection getRealizations() {
        if (this.realizations == null) {
            this.realizations = new RealizationCollection();
        }
        this.realizations.clear();
        for (java.util.Iterator i = this.getMetaClasses().iterator(); i.hasNext(); ) {
            MetaClass mclass = (MetaClass) i.next();
            for (java.util.Iterator ii = mclass.getRealizations().iterator(); ii.hasNext(); ) {
                this.realizations.add(ii.next());
            }
        }
        return this.realizations;
    }

    public void setRealizations(RealizationCollection newVal) {
        this.realizations = newVal;
    }

    /**
     * get the collection of Component.

     */
    public ComponentCollection getComponents() {
        if (this.components == null) {
            this.components = new ComponentCollection();
        }
        if (cache != null) {
            this.components = cache.getAllComponents();
        }
        return this.components;
    }

    public void setComponents(ComponentCollection newVal) {
        this.components = newVal;
    }

    /**
     * get the collection of MetaClass.
	 * The classes in this model.
	 * 
     */
    public MetaClassCollection getMetaClasses() {
        if (this.metaClasses == null) {
            this.metaClasses = new MetaClassCollection();
        }
        if (cache != null) {
            this.metaClasses = cache.getAllMetaClasses();
        }
        return this.metaClasses;
    }

    public void setMetaClasses(MetaClassCollection newVal) {
        this.metaClasses = newVal;
    }

    /**
    * Returns a String representation of this model.
    */
    public String toString() {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append("[");
        strBuf.append(this.getClass().getName());
        strBuf.append("] contains: ");
        strBuf.append("\n  [name]=");
        strBuf.append(this.name);
        strBuf.append("\n  [description]=");
        strBuf.append(this.description);
        strBuf.append("\n  [version]=");
        strBuf.append(this.version);
        strBuf.append("\n  [packages]=\n");
        for (int i = 0; i < this.getPackages().size(); i++) {
            strBuf.append(this.getPackages().elementAt(i).toString());
        }
        strBuf.append("\n  [subsystems]=\n");
        for (short i = 0; i < this.getSubsystems().size(); i++) {
            strBuf.append(this.getSubsystems().elementAt(i).toString());
        }
        strBuf.append("\n");
        return strBuf.toString();
    }

    /**
    * Return the collection of root packages.  A root package exists at the top-level
    * with no containing package.
    * 
    */
    public PackageCollection getRootPackages() {
        MetaModelCache cache = MetaModelCache.getCache(this);
        if (cache != null) {
            return cache.getPackages(null);
        }
        PackageCollection pkgs = new PackageCollection();
        for (Enumeration e = this.getPackages().elements(); e.hasMoreElements(); ) {
            Package pkg = (Package) e.nextElement();
            if (pkg.getParent() == null || pkg.getParent() == pkg) {
                pkgs.addElement(pkg);
            }
        }
        return pkgs;
    }

    /**
    * Return the collection of root subsystems. A root subsystem exists at the
    * top-level with no containing subsystem.
    * The returned collection is readonly. Adding/removing element will not affect
    * the actual model.
    */
    public SubsystemCollection getRootSubsystems() {
        MetaModelCache cache = MetaModelCache.getCache(this);
        if (cache != null) {
            return cache.getSubsystems(null);
        }
        SubsystemCollection subs = new SubsystemCollection();
        for (Enumeration e = this.getSubsystems().elements(); e.hasMoreElements(); ) {
            Subsystem s = (Subsystem) e.nextElement();
            if (s.getParent() == null || s.getParent() == s) {
                subs.addElement(s);
            }
        }
        return subs;
    }

    /**
    * Return the instance of MetaClass whose object id matches the given object
    * id.  Return null if no such MetaClass exists.
    */
    public MetaClass findMetaClass(String objId) {
        if (this.cache != null) {
            return cache.getMetaClass(objId);
        }
        for (short i = 0; i < this.getMetaClasses().size(); i++) {
            MetaClass theClass = (MetaClass) this.getMetaClasses().elementAt(i);
            if (objId.equals(theClass.objId)) {
                return theClass;
            }
        }
        return null;
    }

    /**
    * Return the Component instance whose object id matches the given object id.
    *  Return null if no such Component instance exists.
    */
    public Component findComponent(String objId) {
        if (objId == null || objId.length() == 0) return null;
        if (this.cache != null) {
            return this.cache.getComponent(objId);
        }
        for (Enumeration cmps = this.getComponents().elements(); cmps.hasMoreElements(); ) {
            Component theComponent = (Component) cmps.nextElement();
            if (theComponent.objId.equals(objId)) {
                return theComponent;
            }
        }
        return null;
    }

    /**
    * Return a collection of MetaClass instances whose names match the given name.
    *  Return null if no such MetaClass instances exist.
    * The returned collection is readonly. Adding/removing element will not affect
    * the actual model.
    */
    public Vector findMetaClassesByName(String className) {
        StringBuffer path = new StringBuffer();
        for (int i = 0; i < className.length(); i++) {
            char c = className.charAt(i);
            if (pathSeperators.indexOf(c) >= 0) {
                if (path.length() > 0 && pathSeperators.indexOf(path.charAt(path.length() - 1)) < 0) {
                    path.append(File.separatorChar);
                }
            } else {
                path.append(c);
            }
        }
        Vector retClasses = new Vector();
        if (path.length() == className.length()) {
            for (Enumeration e = this.getMetaClasses().elements(); e.hasMoreElements(); ) {
                MetaClass theClass = (MetaClass) e.nextElement();
                if (className.equals(theClass.name)) {
                    retClasses.addElement(theClass);
                }
            }
        } else {
            MetaClass mclass = this.cache.getMetaClassByPath(path.toString());
            if (mclass != null) {
                retClasses.add(mclass);
            }
        }
        return retClasses;
    }

    /**
    * Return the instance of Association whose object id matches the given object
    * id.  Return null if no such instance exists.
    */
    public Association findAssociation(String objId) {
        if (this.cache != null) {
            return cache.getAssociation(objId);
        }
        for (short i = 0; i < this.getAssociations().size(); i++) {
            Association theAssoc = (Association) this.getAssociations().elementAt(i);
            if (objId.equals(theAssoc.objId)) {
                return (Association) theAssoc;
            }
        }
        return null;
    }

    /**
    * Return the instance of Generalization whose object id matches the given
    * object id.  Return null if no such instance exists.
    */
    public Generalization findGeneralization(String objId) {
        if (this.cache != null) {
            return cache.getGeneralization(objId);
        }
        for (short i = 0; i < this.getGeneralizations().size(); i++) {
            Generalization theGen = (Generalization) this.getGeneralizations().elementAt(i);
            if (objId.equals(theGen.objId)) {
                return (Generalization) theGen;
            }
        }
        return null;
    }

    /**
    * Return the instance of Realizaiton whose object id matches the given object
    * id.  Return null if no such Realization instance exists.
    */
    public Realization findRealization(String objId) {
        if (this.cache != null) {
            return cache.getRealization(objId);
        }
        for (short i = 0; i < this.getRealizations().size(); i++) {
            Realization theReal = (Realization) this.getRealizations().elementAt(i);
            if (objId.equals(theReal.objId)) {
                return (Realization) theReal;
            }
        }
        return null;
    }

    /**
    * Return the instance of Package whose object id matches the given object
    * id.  Return null if no such Package instance exists.
    */
    public Package findPackage(String objId) {
        if (objId == null || objId.length() == 0) {
            return null;
        }
        if (this.cache != null) {
            return this.cache.getPackage(objId);
        }
        for (Enumeration e = this.getPackages().elements(); e.hasMoreElements(); ) {
            Package p = (Package) e.nextElement();
            if (p.getObjId().equals(objId)) {
                return p;
            }
        }
        return null;
    }

    /**
    * Return the instance of Subsystem whose object id matches the given object
    * id.  Return null if no such Subsystem instance exists.
    */
    public Subsystem findSubsystem(String objId) {
        if (objId == null || objId.length() == 0) {
            return null;
        }
        if (this.cache != null) {
            return this.cache.getSubsystem(objId);
        }
        for (Enumeration e = this.getSubsystems().elements(); e.hasMoreElements(); ) {
            Subsystem s = (Subsystem) e.nextElement();
            if (s.getObjId().equals(objId)) {
                return s;
            }
        }
        return null;
    }

    /**
    * Return the Components at the subsystem root.  Root components have no parent.
    * The returned collection is readonly. Adding/removing element will not affect
    * the actual model.
    */
    public ComponentCollection getRootComponents() {
        MetaModelCache cache = MetaModelCache.getCache(this);
        if (cache != null) {
            return cache.getComponents(null);
        }
        ComponentCollection collection = new ComponentCollection();
        for (Enumeration mcomps = this.getComponents().elements(); mcomps.hasMoreElements(); ) {
            Component mc = (Component) mcomps.nextElement();
            if (mc.getSubsystem() == null) {
                collection.addElement(mc);
            }
        }
        return collection;
    }

    public Object cloneObject(java.util.Map parameters, Object clone) {
        java.util.Map objMap = (java.util.Map) parameters.get("model.element");
        if (objMap.get(this) != null) return objMap.get(this);
        if (clone == null) clone = new Model();
        super.cloneObject(parameters, clone);
        if (version != null) ((Model) clone).version = new String(version);
        if (outputPath != null) ((Model) clone).outputPath = new String(outputPath);
        for (java.util.Iterator iter = getSubsystems().iterator(); iter.hasNext(); ) {
            ((Model) clone).getSubsystems().add(((Subsystem) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getPackages().iterator(); iter.hasNext(); ) {
            ((Model) clone).getPackages().add(((Package) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getAssociations().iterator(); iter.hasNext(); ) {
            ((Model) clone).getAssociations().add(((Association) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getGeneralizations().iterator(); iter.hasNext(); ) {
            ((Model) clone).getGeneralizations().add(((Generalization) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getRealizations().iterator(); iter.hasNext(); ) {
            ((Model) clone).getRealizations().add(((Realization) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getComponents().iterator(); iter.hasNext(); ) {
            ((Model) clone).getComponents().add(((Component) iter.next()).cloneObject(parameters, null));
        }
        for (java.util.Iterator iter = getMetaClasses().iterator(); iter.hasNext(); ) {
            ((Model) clone).getMetaClasses().add(((MetaClass) iter.next()).cloneObject(parameters, null));
        }
        return clone;
    }

    private MetaModelCache cache;

    private String mdeVersion = null;

    private static String pathSeperators = "\\/:";

    /**
     * Returns the cache.
     * @return MetaModelCache
     */
    MetaModelCache getCache() {
        return cache;
    }

    /**
     * Sets the cache.
     * @param cache The cache to set
     */
    public void setCache(MetaModelCache cache) {
        this.cache = cache;
    }

    /**
     * get MetaClasses directly under the logical root 'Packages' -- those that have no parent package
     */
    public MetaClassCollection getRootMetaClasses() {
        MetaModelCache cache = MetaModelCache.getCache(this);
        if (cache != null) {
            return cache.getMetaClasses(null);
        }
        MetaClassCollection collection = new MetaClassCollection();
        for (Enumeration mclasses = this.getMetaClasses().elements(); mclasses.hasMoreElements(); ) {
            MetaClass mc = (MetaClass) mclasses.nextElement();
            if (mc.getHoldingPackage() == null) {
                collection.addElement(mc);
            }
        }
        return collection;
    }

    /**
	 * @return
	 */
    public String getMdeVersion() {
        return mdeVersion;
    }

    /**
	 * @param string
	 */
    public void setMdeVersion(String string) {
        mdeVersion = string;
    }
}
