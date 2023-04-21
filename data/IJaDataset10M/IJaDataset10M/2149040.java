package org.jmlspecs.samples.prelimdesign;

public interface MoneyOps extends MoneyComparable {

    public MoneyOps plus(Money m2);

    public MoneyOps minus(Money m2);

    public MoneyOps scaleBy(double factor);
}
