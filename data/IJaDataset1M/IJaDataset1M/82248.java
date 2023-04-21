package org.silicolife.bonzai.maple.model.genome.growth;

import org.silicolife.bonzai.maple.model.MapleLeaves;
import org.silicolife.bonzai.maple.model.MapleStem;
import org.silicolife.bonzai.model.organism.Genome;

public class LeafGrowthGene extends GrowthGene {

    @Override
    public boolean evaluate(Genome genome) throws Exception {
        return genome.getCell() instanceof MapleLeaves && super.evaluate(genome);
    }

    public void action(Genome genome) throws Exception {
        MapleLeaves cell = (MapleLeaves) genome.getCell();
        MapleStem stem = (MapleStem) cell.getOrganism().getLivingCell("MapleStem");
        int growthFactor = calculateGrowthFactor(cell);
        growthFactor *= stem.getTerminalBranchCount();
        for (int i = 0; i < growthFactor; i++) {
            cell.increaseLeafCount();
        }
        cell.consumeNutrient();
    }

    public Object clone() {
        return new LeafGrowthGene();
    }

    public boolean validateParam(String key, Object value) throws Exception {
        return false;
    }
}
