package org.silicolife.bonzai.maple.model;

import org.silicolife.bonzai.maple.model.genome.MapleGenome;
import org.silicolife.bonzai.maple.model.genome.death.SeedDeathGene;
import org.silicolife.bonzai.maple.model.genome.growth.SeedGerminationGene;
import org.silicolife.bonzai.maple.model.genome.hydration.SeedHydrationGene;
import org.silicolife.bonzai.maple.model.genome.temperature.SoilTemperatureGene;
import org.silicolife.bonzai.model.organism.Gene;
import org.silicolife.bonzai.model.organism.Cell;
import org.silicolife.bonzai.model.organism.Organism;

public class MapleSeed extends Cell {

    private boolean hasGerminated = false;

    public MapleSeed(Organism organism, MapleGenome genome) {
        super(organism, genome);
        setDry();
        setBase();
        setMild();
        setNutrientRich();
        setHealthy();
        setClean();
        for (Gene gene : genome.getGeneList()) {
            if (gene instanceof SeedGerminationGene || gene instanceof SeedDeathGene || gene instanceof SeedHydrationGene || gene instanceof SoilTemperatureGene) {
                gene.express();
            }
        }
    }

    public MapleSeed(Organism organism, Cell parentCell, MapleGenome genome) {
        this(organism, genome);
    }

    public MapleSeed clone() {
        return new MapleSeed(getOrganism(), this, (MapleGenome) getGenome().clone());
    }

    public boolean hasGerminated() {
        return hasGerminated;
    }

    public void setHasGerminated(boolean hasGerminated) {
        this.hasGerminated = hasGerminated;
    }
}
