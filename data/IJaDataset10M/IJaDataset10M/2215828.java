package uk.ac.roslin.ensembl.datasourceaware.core;

import org.biojava3.core.sequence.DNASequence;
import uk.ac.roslin.ensembl.config.FeatureType;
import uk.ac.roslin.ensembl.config.MarkerType;
import uk.ac.roslin.ensembl.datasourceaware.DAXRef;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.AbstractMapping;
import uk.ac.roslin.ensembl.model.DoubleMapping;
import uk.ac.roslin.ensembl.model.DoubleMappingSet;
import uk.ac.roslin.ensembl.model.Mapping;
import uk.ac.roslin.ensembl.model.MappingSet;
import uk.ac.roslin.ensembl.model.ObjectType;
import uk.ac.roslin.ensembl.model.core.Marker;

public class DAMarker extends DAFeature implements Marker {

    DNASequence primerL = null;

    DNASequence primerR = null;

    MarkerType markerType = null;

    int minimumPrimerDistance = 0;

    int maximumPrimerDistance = 0;

    protected DoubleMappingSet geneticMappings = new DoubleMappingSet();

    int mappingCount = 0;

    @Override
    void reinitialize() throws DAOException {
    }

    @Override
    public ObjectType getType() {
        return FeatureType.marker_feature;
    }

    @Override
    public void setLeftPrimer(DNASequence primerL) {
        this.primerL = primerL;
    }

    @Override
    public void setRightPrimer(DNASequence primerR) {
        this.primerR = primerR;
    }

    @Override
    public DNASequence getLeftPrimer() {
        return this.primerL;
    }

    @Override
    public DNASequence getRightPrimer() {
        return this.primerR;
    }

    @Override
    public MarkerType getMarkerType() {
        return this.markerType;
    }

    @Override
    public void setMarkerType(MarkerType type) {
        this.markerType = type;
    }

    @Override
    public void setMinimumPrimerDistance(int distance) {
        this.minimumPrimerDistance = distance;
    }

    @Override
    public void setMaximumPrimerDistance(int distance) {
        this.maximumPrimerDistance = distance;
    }

    @Override
    public int getMinimumPrimerDistance() {
        return this.minimumPrimerDistance;
    }

    @Override
    public int getMaximumPrimerDistance() {
        return this.maximumPrimerDistance;
    }

    @Override
    public Boolean addMapping(AbstractMapping mapping) {
        if (mapping instanceof Mapping && this.mappings.add((Mapping) mapping)) {
            ObjectType t = mapping.getTargetType();
            if (t != null) {
                if (!this.objectTypeMappings.containsKey(t)) {
                    this.objectTypeMappings.put(t, new MappingSet());
                }
                this.objectTypeMappings.get(t).add((Mapping) mapping);
            }
            return true;
        } else if (mapping instanceof DoubleMapping && this.geneticMappings.add((DoubleMapping) mapping)) {
        }
        return false;
    }

    @Override
    public DoubleMappingSet getGeneticMappings() {
        return this.geneticMappings;
    }

    public DAXRef getXRef() {
        return super.getDisplayXRef();
    }

    public void setXRef(DAXRef ref) {
        super.setDisplayXRef(ref);
    }

    @Override
    public int getMappingCount() {
        return this.mappingCount;
    }

    public void setMappingCount(int mappingCount) {
        this.mappingCount = mappingCount;
    }

    @Override
    public boolean isUniquelyMapped() {
        return mappingCount == 1;
    }
}
