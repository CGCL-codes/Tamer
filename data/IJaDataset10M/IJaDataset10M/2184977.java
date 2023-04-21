package test.fixture;

import fit.ColumnFixture;

public class EndToEndScenario extends ColumnFixture {

    public int one;

    public int two;

    public int three;

    public int sum;

    public String stringColumn;

    public Integer integerColumn;

    public int sum() {
        return 0;
    }

    public int product() {
        return 0;
    }

    public String stringMethod() {
        return stringColumn;
    }

    public Integer integerMethod() {
        return integerColumn;
    }
}
