package org.silicolife.bonzai.maple.model.genome.growth;

import org.silicolife.bonzai.model.organism.Cell;
import org.silicolife.bonzai.model.organism.Gene;
import org.silicolife.bonzai.model.organism.Genome;

public abstract class GrowthGene extends Gene {

    public boolean evaluate(Genome genome) throws Exception {
        Cell cell = genome.getCell();
        return (cell.isDry() == false && cell.isFlooded() == false && cell.isFreezing() == false && cell.isNutrientNone() == false && cell.isDiseased() == false && cell.isPoisoned() == false);
    }

    public int calculateGrowthFactor(Cell cell) throws Exception {
        int growthFactor = 1;
        if (cell.isWet()) growthFactor++;
        if (cell.isAlkali() || cell.isAcidic()) growthFactor--;
        if (cell.isCold()) growthFactor--; else if (cell.isWarm()) growthFactor++; else if (cell.isHot()) growthFactor += 2;
        if (cell.isNutrientPoor()) growthFactor--; else if (cell.isNutrientRich()) growthFactor++;
        if (cell.isInfected()) growthFactor--;
        if (cell.isDirty()) growthFactor--;
        return growthFactor;
    }
}
