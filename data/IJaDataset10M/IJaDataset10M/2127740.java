package fixtures;

import hello.Till;

public class TillFixture {

    Till till = new Till(0);

    public boolean soldFor(int n, String flowers, double dollars) {
        till.add(dollars);
        return true;
    }

    public boolean soldFlowersFor(int n, String flowers, double dollars) {
        till.add(dollars);
        return true;
    }

    public double balanceIs() {
        return till.balance();
    }
}
