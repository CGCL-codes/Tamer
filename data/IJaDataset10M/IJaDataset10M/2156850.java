package playground.ou.signaltimingstragety;

public class TimeParameter {

    static final int YELLOWTIME = 3;

    static final int ALLREDTIME = 0;

    int cycleLength, phaseGreen[];

    public void setCyclelen(int cyclelen) {
        this.cycleLength = cyclelen;
    }

    public int getCyclelen() {
        return this.cycleLength;
    }

    public int[] getGreen() {
        int[] greens = this.phaseGreen;
        return greens;
    }

    public int getPhaseAactualGreen() {
        return phaseGreen[0];
    }

    public void calCyclelen_Green(int cmin, int cmax, int time, int strategy, int phaseNum, int[] criticalPhaseFlow) {
        calcCyclelen(cmin, cmax, time, strategy, phaseNum, criticalPhaseFlow);
        calGreen(strategy, phaseNum, criticalPhaseFlow);
    }

    public void calcCyclelen(int cmin, int cmax, int time, int strategy, int phaseNum, int[] criticalPhaseFlow) {
        switch(strategy) {
            case 0:
            case 1:
                this.cycleLength = new FixCycle(cmin, cmax).calc_cyclelength((int) (time / 3600));
                break;
            case 2:
                this.cycleLength = new Webster(cmin, cmax).calc_cyclelength(phaseNum, criticalPhaseFlow);
                break;
            case 3:
                this.cycleLength = new HCM(cmin, cmax).calc_cyclelength(phaseNum, criticalPhaseFlow);
                break;
            default:
                System.out.println("Error Strategy ");
                break;
        }
    }

    public void calGreen(int strategy, int phaseNum, int[] criticalPhaseFlow) {
        switch(strategy) {
            case 0:
                phaseGreen = new GreenSplit(this.cycleLength, YELLOWTIME).fareSplit(phaseNum, YELLOWTIME + ALLREDTIME);
                break;
            case 1:
            case 2:
            case 3:
                phaseGreen = new GreenSplit(this.cycleLength, YELLOWTIME).demandSplit(phaseNum, criticalPhaseFlow, YELLOWTIME + ALLREDTIME);
                break;
            default:
                System.out.println("Error Strategy ");
                break;
        }
    }
}
