package com.scythebill.birdlist.model.taxa;

import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.scythebill.birdlist.model.taxa.Taxon.Type;
import com.scythebill.birdlist.model.util.TaxonComparator;
import java.util.Comparator;

/** Base implementation of Taxonomy. */
public class TaxonomyImpl implements Taxonomy {

    private Taxon root;

    private final Comparator<Taxon> taxonComparator = new TaxonComparator();

    private final BiMap<String, Taxon> taxa = HashBiMap.create();

    private final String name;

    private final String id;

    private volatile int speciesCount = 0;

    public TaxonomyImpl(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaxonomyImpl() {
        this(null, null);
    }

    /**
   * Assigns the root taxon, and marks the root as built.
   */
    public void setRoot(Taxon taxon) {
        Preconditions.checkState(root == null, "Root to taxonomy already set");
        root = taxon;
    }

    @Override
    public Taxon getRoot() {
        return root;
    }

    @Override
    public Taxon findSpecies(String fullName) {
        int space = fullName.indexOf(' ');
        if (space < 1) {
            throw new IllegalArgumentException("'" + fullName + "' does not contain a space");
        }
        String genusName = fullName.substring(0, space);
        Taxon genus = root.findByName(genusName, Type.genus);
        if (genus == null) {
            return null;
        }
        String speciesName = fullName.substring(space + 1);
        return genus.findByName(speciesName, Type.species);
    }

    @Override
    public String getId(Taxon taxon) {
        return taxa.inverse().get(taxon);
    }

    @Override
    public Taxon getTaxon(String id) {
        return taxa.get(id);
    }

    public void register(Taxon taxon, String id) {
        if ((taxon == null) || (id == null)) {
            throw new NullPointerException();
        }
        if (taxa.containsValue(taxon)) {
            throw new IllegalArgumentException(taxon.toString() + " is already present with ID " + getId(taxon));
        }
        if (taxa.containsKey(id)) {
            throw new IllegalArgumentException(id + " is already present as " + getTaxon(id));
        }
        if (taxon.getType() == Type.species) {
            speciesCount++;
        }
        taxa.put(id, taxon);
    }

    @Override
    public void registerWithNewId(Taxon taxon) {
        Preconditions.checkNotNull(taxon);
        String id = _calculateId(taxon);
        register(taxon, id);
    }

    @Override
    public void unregister(Taxon taxon) {
        Preconditions.checkArgument(taxon.getTaxonomy() == this, "Not registered with this taxonomy");
        taxa.inverse().remove(taxon);
        if (taxon.getType() == Type.species) {
            speciesCount--;
        }
    }

    @Override
    public BiMap<String, Taxon> asBimap() {
        return Maps.unmodifiableBiMap(taxa);
    }

    /**
   * @return a comparator of Taxon, for taxa in this Taxonomy
   */
    @Override
    public Comparator<Taxon> comparator() {
        return taxonComparator;
    }

    private String _calculateId(Taxon taxon) {
        StringBuilder builder = new StringBuilder(16);
        builder.append(_PREFIXES.get(taxon.getType()));
        int shortNameLength = 3;
        switch(taxon.getType()) {
            case species:
            case group:
            case subspecies:
                Taxon parent = taxon.getParent();
                if (parent == null) {
                    throw new NullPointerException("" + taxon + " does not have a parent.");
                }
                String parentPrefix = _PREFIXES.get(parent.getType());
                String parentName = parent.getId().substring(parentPrefix.length());
                builder.append(parentName);
                break;
            case genus:
                shortNameLength = 5;
                break;
            default:
                break;
        }
        builder.append(_getShortName(taxon.getName(), shortNameLength));
        int baseLength = builder.length();
        String id = null;
        for (int i = 0; true; i++) {
            if (i > 0) {
                builder.append(Integer.toString(i));
            }
            id = builder.toString();
            if (!taxa.containsKey(id)) {
                break;
            }
            builder.setLength(baseLength);
        }
        return id;
    }

    private static String _getShortName(String fullName, int shortNameLength) {
        if (fullName == null) {
            return "";
        }
        return fullName.substring(0, Math.min(shortNameLength, fullName.length()));
    }

    private static final Map<Taxon.Type, String> _PREFIXES = new HashMap<Type, String>();

    static {
        _PREFIXES.put(Taxon.Type.classTaxon, "class");
        _PREFIXES.put(Taxon.Type.family, "fam");
        _PREFIXES.put(Taxon.Type.genus, "ge");
        _PREFIXES.put(Taxon.Type.order, "ord");
        _PREFIXES.put(Taxon.Type.phylum, "phylum");
        _PREFIXES.put(Taxon.Type.species, "sp");
        _PREFIXES.put(Taxon.Type.group, "gr");
        _PREFIXES.put(Taxon.Type.subspecies, "ssp");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSpeciesCount() {
        return speciesCount;
    }
}
