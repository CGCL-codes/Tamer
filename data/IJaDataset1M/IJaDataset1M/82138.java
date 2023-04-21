package net.boogie.calamari.genetic.model;

import java.io.Serializable;
import java.util.List;

public interface IGenome extends Serializable {

    public List<IGene> getGenes();

    public void setGenes(List<IGene> genes);

    public IGenome createClone(boolean clearGenes);
}
