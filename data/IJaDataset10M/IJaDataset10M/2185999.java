package uk.ac.roslin.ensembl.model.core;

import uk.ac.roslin.ensembl.config.MarkerType;
import uk.ac.roslin.ensembl.model.DoubleMappingSet;

public interface Marker extends Feature {

    public void setLeftPrimer(org.biojava3.core.sequence.DNASequence primerL);

    public void setRightPrimer(org.biojava3.core.sequence.DNASequence primerR);

    public org.biojava3.core.sequence.DNASequence getLeftPrimer();

    public org.biojava3.core.sequence.DNASequence getRightPrimer();

    public MarkerType getMarkerType();

    public void setMarkerType(MarkerType type);

    public void setMinimumPrimerDistance(int distance);

    public void setMaximumPrimerDistance(int distance);

    public int getMinimumPrimerDistance();

    public int getMaximumPrimerDistance();

    public DoubleMappingSet getGeneticMappings();

    public int getMappingCount();

    public boolean isUniquelyMapped();
}
