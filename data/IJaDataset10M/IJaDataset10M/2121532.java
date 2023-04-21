package uk.ac.roslin.ensembl.mapper.core;

import java.util.HashMap;
import java.util.List;
import uk.ac.roslin.ensembl.mapper.query.FeatureQuery;

public interface GeneMapper {

    public List<HashMap> getGene(FeatureQuery query);
}
