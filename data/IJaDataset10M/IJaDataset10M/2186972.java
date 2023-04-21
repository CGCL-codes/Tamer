package cn.edu.thss.iise.beehivez.server.mining;

import org.processmining.converting.HNetToPetriNetConverter;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogSummary;
import org.processmining.framework.models.heuristics.HeuristicsNet;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.mining.geneticmining.GeneticMinerUI;
import org.processmining.mining.geneticmining.RealGeneticMiner;
import org.processmining.mining.geneticmining.duplicates.DTGeneticMiner;
import org.processmining.mining.geneticmining.duplicates.DTGeneticMinerResult;
import org.processmining.mining.geneticmining.duplicates.DTGeneticMinerUI;

public class DupTGeneticMiner extends ControlFlowMiner {

    @Override
    public String getDesription() {
        return null;
    }

    @Override
    public String getName() {
        return "DTGeneticMiner";
    }

    @Override
    public PetriNet mine(LogReader logReader) {
        if (logReader != null) {
            LogSummary summary = logReader.getLogSummary();
            DTGeneticMiner miningPlugin = new DTGeneticMiner();
            miningPlugin.ui = new DTGeneticMinerUI(summary);
            DTGeneticMinerResult resultlist = (DTGeneticMinerResult) miningPlugin.mine(logReader);
            HeuristicsNet HNetResult = resultlist.population[0];
            HNetToPetriNetConverter converter = new HNetToPetriNetConverter();
            PetriNet result = converter.convert(HNetResult);
            return result;
        } else {
            System.err.println("No log reader could be constructed.");
            return null;
        }
    }
}
