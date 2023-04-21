package org.jExigo.examples;

import org.jExigo.FixedDecimalArithmetic;
import org.jExigo.FixedDecimalNumber;

/**
 * examples of adding subtracting multiplying dividing and comparing
 * @author joseph mcverry
 *
 */
public class Example {

    /**
	 * @param args - not used
	 */
    public static void main(String[] args) {
        FixedDecimalNumber fda = new FixedDecimalNumber("1.1");
        System.out.print("Adding: " + fda.get());
        FixedDecimalNumber fdb = new FixedDecimalNumber("1.01");
        System.out.print(" + " + fdb.toString() + " = ");
        fda = FixedDecimalArithmetic.add(fda, fdb);
        System.out.println(fda.get());
        System.out.print("Subtractring: " + fda.get());
        System.out.print(" - " + fdb.toString() + " = ");
        fda = FixedDecimalArithmetic.subtract(fda, fdb);
        System.out.println(fda);
        System.out.print("Multiplying: " + fda.get());
        System.out.print(" * " + fdb.toString() + " = ");
        fda = FixedDecimalArithmetic.multiply(fda, fdb);
        System.out.println(fda);
        System.out.print("Dividing: " + fda.get());
        System.out.print(" / " + fdb.toString() + " = ");
        fda = FixedDecimalArithmetic.divide(fda, fdb);
        System.out.println(fda);
        fda.toDecimalPlaces(2);
        System.out.println("truncate to 2 decimal places " + fda);
        fda.toDecimalPlaces(3);
        System.out.println("show as 3 decimal places " + fda);
        fdb = fda.copy();
        System.out.println("fda == fdb is " + (fda.compare(fdb) == 0));
        fdb.toDecimalPlaces(14);
        System.out.println(fda + " == " + fdb + " is " + (fda.compare(fdb) == 0));
    }
}
