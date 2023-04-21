package uk.ac.roslin.ensembl.dao.coremodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import uk.ac.roslin.ensembl.dao.base.DAMapping;
import uk.ac.roslin.ensembl.dao.base.DAMappingSet;
import uk.ac.roslin.ensembl.model.Coordinate;
import uk.ac.roslin.ensembl.model.Coordinate.Strand;
import uk.ac.roslin.ensembl.model.Mapping;
import uk.ac.roslin.ensembl.model.database.Registry;
import uk.ac.roslin.ensembl.model.core.CoordinateSystem;
import uk.ac.roslin.ensembl.dao.factory.DAOCoreFactory;
import uk.ac.roslin.ensembl.dao.factory.DAOFactory;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.CoordinateSet;
import uk.ac.roslin.ensembl.model.ObjectType;
import uk.ac.roslin.ensembl.model.core.CoreObject;
import uk.ac.roslin.ensembl.model.core.Species;
import uk.ac.roslin.ensemblconfig.FeatureType;

/**
 *
 * @author paterson
 */
public class DADNASequence extends org.biojava3.core.sequence.DNASequence implements CoreObject, uk.ac.roslin.ensembl.model.core.DNASequence {

    protected DAOFactory daoFactory = null;

    protected String ensemblVersion = null;

    protected String dbVersion = null;

    protected Registry registry = null;

    protected Integer id = null;

    protected Integer seqRegionID = null;

    protected String name = null;

    protected String dbSpeciesName = null;

    protected Species species = null;

    protected Integer DBSeqLength = null;

    protected CoordinateSystem coordSystem = null;

    protected DAMappingSet mappings = new DAMappingSet();

    protected HashMap<ObjectType, CoordinateSet> mappedRegions = new HashMap<ObjectType, CoordinateSet>();

    protected HashMap<ObjectType, DAMappingSet> mappedObjectTypes = new HashMap<ObjectType, DAMappingSet>();

    public DADNASequence() {
        super();
    }

    public DADNASequence(DAEnsemblDNASequenceReader proxyLoader) {
        super(proxyLoader);
        ((DAEnsemblDNASequenceReader) this.getSequenceStorage()).setParent(this);
        this.setName(proxyLoader.getName());
        this.setId(proxyLoader.getSeqRegionID());
        this.setDBSeqLength(proxyLoader.getLengthInteger());
    }

    public DADNASequence(String sequence) {
        super(new DAEnsemblDNASequenceReader());
        ((DAEnsemblDNASequenceReader) this.getSequenceStorage()).setParent(this);
        ((DAEnsemblDNASequenceReader) this.getSequenceStorage()).setContents(sequence);
        ((DAEnsemblDNASequenceReader) this.getSequenceStorage()).setNonCacheLazyLoaded(true);
    }

    public DADNASequence(DAOCoreFactory factory) {
        this.setDaoFactory(factory);
    }

    public void setSequenceStorage(DAEnsemblDNASequenceReader proxyLoader) {
        this.sequenceStorage = proxyLoader;
        ((DAEnsemblDNASequenceReader) this.getSequenceStorage()).setParent(this);
    }

    public DAOCoreFactory getDaoFactory() {
        return (DAOCoreFactory) daoFactory;
    }

    public void setDaoFactory(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.setEnsemblVersion(daoFactory.getEnsemblRelease());
        this.setDBVersion(daoFactory.getDBVersion());
        this.setRegistry((Registry) daoFactory.getRegistry());
    }

    public String getEnsemblVersion() {
        if (ensemblVersion != null) {
            return ensemblVersion;
        } else if (this.daoFactory != null) {
            ensemblVersion = this.daoFactory.getEnsemblRelease();
            return ensemblVersion;
        } else {
            return ensemblVersion;
        }
    }

    private void setEnsemblVersion(String ensemblVersion) {
        this.ensemblVersion = ensemblVersion;
    }

    public String getDBVersion() {
        return dbVersion;
    }

    private void setDBVersion(String dbversion) {
        this.dbVersion = dbversion;
    }

    public Registry getRegistry() {
        if (registry != null) {
            return registry;
        } else if (this.daoFactory != null) {
            registry = this.daoFactory.getRegistry();
            return registry;
        } else {
            return registry;
        }
    }

    public void setRegistry(Registry datasource) {
        this.registry = datasource;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDBName() {
        return daoFactory.getDatabaseName();
    }

    public Species getSpecies() {
        return species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }

    public ObjectType getType() {
        if (this.getCoordSystem() != null) {
            return this.getCoordSystem().getType();
        } else {
            return null;
        }
    }

    public Integer getDBSeqLength() {
        if (DBSeqLength == null) {
            this.lazyLoad();
        }
        if (DBSeqLength != null) {
            return DBSeqLength;
        } else {
            return 0;
        }
    }

    public void setDBSeqLength(Integer seqLength) {
        this.DBSeqLength = seqLength;
        this.setBioEnd(seqLength);
    }

    @Override
    public Integer getBioEnd() {
        try {
            return (super.getBioEnd() != null) ? super.getBioEnd() : this.getDBSeqLength();
        } catch (NullPointerException e) {
            return this.getDBSeqLength();
        }
    }

    @Override
    public int getLength() {
        try {
            return super.getLength();
        } catch (NullPointerException e) {
            return this.getDBSeqLength();
        }
    }

    public String getName() {
        if (name == null) {
            this.lazyLoad();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordinateSystem getCoordSystem() {
        if (coordSystem == null) {
            this.lazyLoad();
        }
        return coordSystem;
    }

    public void setCoordSystem(CoordinateSystem coordSystem) {
        this.coordSystem = coordSystem;
    }

    public DAMappingSet getMappings() {
        return this.mappings;
    }

    public DAMappingSet getMappings(ObjectType targetType) {
        if (!this.mappedObjectTypes.containsKey(targetType)) {
            return new DAMappingSet();
        } else {
            return this.mappedObjectTypes.get(targetType);
        }
    }

    public void addMapping(Mapping mapping) {
        this.mappings.add((DAMapping) mapping);
        ObjectType t = mapping.getTargetType();
        if (t != null) {
            if (!this.mappedObjectTypes.containsKey(t)) {
                this.mappedObjectTypes.put(t, new DAMappingSet());
            }
            this.mappedObjectTypes.get(t).add((DAMapping) mapping);
        }
    }

    public List<DAGene> getGenesOnRegion(Coordinate coord) throws DAOException {
        List<DAGene> out = new ArrayList<DAGene>();
        List<? extends Mapping> mappings = null;
        if (this.mappedRegions.containsKey(FeatureType.gene) && this.mappedRegions.get(FeatureType.gene).containsCoordinateWithoutGaps(coord)) {
            ArrayList<DAMapping> theseMappings = new ArrayList<DAMapping>();
            for (DAMapping m : this.getMappings(FeatureType.gene)) {
                Coordinate c = m.getSourceCoordinates();
                if (c.overlaps(coord)) {
                    theseMappings.add(m);
                }
            }
            mappings = theseMappings;
            if (mappings == null || mappings.isEmpty()) {
                return out;
            } else {
                if (coord.getStrand() == null) {
                    for (Mapping m : mappings) {
                        out.add((DAGene) m.getTarget());
                    }
                } else {
                    for (Mapping m : mappings) {
                        if (m.getSourceCoordinates().getStrand().equals(coord.getStrand())) {
                            out.add((DAGene) m.getTarget());
                        }
                    }
                }
            }
            return out;
        } else {
            mappings = this.getDaoFactory().getGeneDAO().getGeneMappingsOnRegion(this, coord);
            if (mappings == null || mappings.isEmpty()) {
                return out;
            } else {
                if (coord.getStrand() == null) {
                    for (Mapping m : mappings) {
                        out.add((DAGene) m.getSource());
                    }
                } else {
                    for (Mapping m : mappings) {
                        if (m.getTargetCoordinates().getStrand().equals(coord.getStrand())) {
                            out.add((DAGene) m.getSource());
                        }
                    }
                }
            }
            if (!mappedRegions.containsKey(FeatureType.gene)) {
                mappedRegions.put(FeatureType.gene, new CoordinateSet());
            }
            this.mappedRegions.get(FeatureType.gene).add(coord);
            return out;
        }
    }

    public List<DAGene> getGenesOnRegion(Integer start, Integer stop, Strand strand) throws DAOException {
        Strand s = strand;
        Integer begin = null;
        Integer end = null;
        if (start != null && stop != null && start.doubleValue() > stop.doubleValue()) {
            begin = stop;
            end = start;
        } else {
            begin = start;
            end = stop;
        }
        Coordinate coord = new Coordinate(start, stop, strand);
        return this.getGenesOnRegion(coord);
    }

    public List<DAGene> getGenesOnRegion(Integer start, Integer stop) throws DAOException {
        return this.getGenesOnRegion(start, stop, null);
    }

    private void lazyLoad() {
        if (this.id != null && this.getDaoFactory() != null) {
            try {
                DADNASequence fetchedSeq = (DADNASequence) this.getDaoFactory().getSequenceDAO().getSequenceByID(id);
                this.setName(fetchedSeq.getName());
                this.setDBSeqLength(fetchedSeq.getDBSeqLength());
                this.setCoordSystem(fetchedSeq.getCoordSystem());
            } catch (DAOException ex) {
            }
        }
    }

    public String getSequenceAsString(Integer begin, Integer end) {
        return super.getSequenceAsString(begin, end, org.biojava3.core.sequence.Strand.POSITIVE);
    }

    public String getReverseComplementSequenceAsString(Integer begin, Integer end) {
        DAEnsemblDNASequenceReader ss = (DAEnsemblDNASequenceReader) getSequenceStorage();
        return ss.getReverseComplementSequenceAsString(begin, end);
    }

    public String getReverseComplementSequenceAsString() {
        return this.getReverseComplementSequenceAsString(this.getBioBegin(), this.getBioEnd());
    }

    public HashMap<ObjectType, DAMappingSet> getMappedObjectTypes() {
        return mappedObjectTypes;
    }

    public HashMap<ObjectType, CoordinateSet> getMappedRegions() {
        return mappedRegions;
    }

    public String getHashID() {
        return ((this.getDaoFactory() != null) ? this.getDaoFactory().getDatabaseName() : "NODATABASE") + "_" + ((this.getType() != null) ? this.getType().toString() : "NOTYPE") + "_" + ((this.getId() != null) ? this.getId().toString() : "NOID");
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
