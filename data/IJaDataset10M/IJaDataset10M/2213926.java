package net.kortsoft.gameportlet.culandcon;

public class MoreThan5PopulationDistributionException extends PopulationDistibutionException {

    private int num;

    public int getNum() {
        return num;
    }

    public MoreThan5PopulationDistributionException(int num) {
        super();
        this.num = num;
    }

    private static final long serialVersionUID = -7774299421304106452L;
}
