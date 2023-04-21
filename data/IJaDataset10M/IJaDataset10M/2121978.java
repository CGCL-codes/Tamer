package uk.ac.roslin.ensembl.mapper.core;

import java.util.HashMap;
import java.util.List;
import uk.ac.roslin.ensembl.datasourceaware.core.DACoordinateSystem;

public interface SpeciesMapper {

    public List<DACoordinateSystem> getCoordSystems(HashMap map);

    public List<HashMap> setFeatureCS(HashMap map);
}
