package net.sf.sql2java.common.configuration;

import java.util.HashMap;
import java.util.Map;
import net.sf.sql2java.common.beans.Database;
import net.sf.sql2java.common.configuration.xml.XmlCollection;
import net.sf.sql2java.common.configuration.xml.XmlReference;
import net.sf.sql2java.common.configuration.xml.XmlTag;
import net.sf.sql2java.conversion.Conversion;
import net.sf.sql2java.conversion.ConversionReference;
import net.sf.sql2java.fetching.DatabaseFetcher;
import net.sf.sql2java.generator.Generator;
import net.sf.sql2java.generator.scripting.Script;
import net.sf.sql2java.generator.template.Template;
import net.sf.sql2java.packages.Pack;
import net.sf.sql2java.packages.PackageReference;
import net.sf.sql2java.swing.AreaListModel;
import net.sf.sql2java.swing.EngineComboBoxModel;

/**
 * Configuration.java
 *     Created on 22-jul-2007, 12:04:24
 *
 * @author dge2
 */
@XmlTag("sql2java")
public class Configuration {

    @XmlReference("database")
    private Database database;

    @XmlReference("fetcher")
    private DatabaseFetcher fetcher;

    @XmlReference("generator")
    private Generator generator;

    @XmlCollection(collectionTag = "packages", elementTag = "package", elementClass = PackageReference.class)
    private Map<String, PackageReference> packs;

    @XmlReference("conversion")
    private ConversionReference conversionReference;

    @XmlReference("engines")
    private EngineComboBoxModel engines;

    @XmlReference("areas")
    private AreaListModel areas;

    public void addPackage(PackageReference pack) {
        if (packs == null) packs = new HashMap<String, PackageReference>();
        packs.put(pack.getName(), pack);
    }

    public void removePack(PackageReference pack) {
        if (packs != null) packs.remove(pack);
    }

    public Pack getPack(String name) {
        if (packs != null) return packs.get(name).getPack();
        return null;
    }

    public Database getDatabase() {
        return database;
    }

    public DatabaseFetcher getFetcher() {
        return fetcher;
    }

    public Generator getGenerator() {
        return generator;
    }

    public Conversion getConversion() {
        return conversionReference.getConversion();
    }

    public Map<String, PackageReference> getPackageReferences() {
        return packs;
    }

    public Map<String, Pack> getPacks() {
        Map<String, Pack> result = new HashMap<String, Pack>();
        for (PackageReference ref : packs.values()) result.put(ref.getName(), ref.getPack());
        return result;
    }

    public Template getTemplate(String locator) {
        Template result = null;
        String[] parts = locator.split(".", 2);
        Pack pack = packs.get(parts[0]).getPack();
        result = pack.getTemplate(parts[1]);
        return result;
    }

    public Script getScript(String locator) {
        Script result = null;
        String[] parts = locator.split(".", 2);
        Pack pack = packs.get(parts[0]).getPack();
        result = pack.getScript(parts[1]);
        return result;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setFetcher(DatabaseFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public void setGenerator(Generator generator) {
        generator.setParent(this);
        this.generator = generator;
    }

    public void setConversionReference(ConversionReference conversionReference) {
        this.conversionReference = conversionReference;
    }

    /**
     * @return the engines
     */
    public EngineComboBoxModel getEngines() {
        return engines;
    }

    /**
     * @param engines the engines to set
     */
    public void setEngines(EngineComboBoxModel engines) {
        this.engines = engines;
    }

    /**
     * @return the areas
     */
    public AreaListModel getAreas() {
        return areas;
    }

    /**
     * @param areas the areas to set
     */
    public void setAreas(AreaListModel areas) {
        this.areas = areas;
    }
}
