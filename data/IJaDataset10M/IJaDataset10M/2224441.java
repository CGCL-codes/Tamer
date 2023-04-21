package de.unikoeln.genetik.popgen.jfms.model.event;

public class PopulationSizeChangeEvent extends DemographyChangeEvent {

    public final int newDiploidSize;

    public PopulationSizeChangeEvent(int time, int subPopulation, int diploidSize) {
        super(time, subPopulation);
        newDiploidSize = diploidSize;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(" -e");
        if (subPopulation == DemographyChangeEvent.ALL) {
            sb.append("N ");
        } else {
            sb.append("n " + subPopulation + " ");
        }
        sb.append(newDiploidSize);
        return sb.toString();
    }
}
