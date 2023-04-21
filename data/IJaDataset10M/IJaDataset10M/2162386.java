package ExamplesJaCoP;

import java.util.ArrayList;

/**
 * FIR benchmark (16-point FIR filter).
 * 
 * Source: Ramesh Karri, Karin Hogstedt and Alex Orailoglu "Computer-Aided
 * Design of Fault-Tolerant VLSI Design Systems" IEEE Design & Test, Fall 1996
 * (Vol. 13, No. 3), pp. 88-96
 * 
 * @author Krzysztof Kuchcinski and Radoslaw Szymanek
 * @version 3.0
 */
public class FIR extends Filter {

    /**
	 * It constructs a simple FIR filter.
	 */
    public FIR() {
        this(1, 2);
    }

    /**
	 * It constructs a FIR filter with the specified delay
	 * for the addition and multiplication operation.
	 * 
	 * @param addDel the delay of the addition operation.
	 * @param mulDel the delay of the multiplication operation.
	 */
    public FIR(int addDel, int mulDel) {
        this.addDel = addDel;
        this.mulDel = mulDel;
        name = "FIR";
        int dependencies[][] = { { 0, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 4, 5 }, { 5, 6 }, { 6, 7 }, { 7, 8 }, { 9, 10 }, { 10, 2 }, { 11, 12 }, { 12, 3 }, { 13, 14 }, { 14, 4 }, { 15, 16 }, { 16, 5 }, { 17, 18 }, { 18, 6 }, { 19, 20 }, { 20, 7 }, { 21, 22 }, { 22, 8 } };
        this.dependencies = dependencies;
        int ids[] = { addId, mulId, addId, addId, addId, addId, addId, addId, addId, addId, mulId, addId, mulId, addId, mulId, addId, mulId, addId, mulId, addId, mulId, addId, mulId };
        this.ids = ids;
        int last[] = { 8 };
        this.last = last;
    }

    @Override
    public ArrayList<String> names() {
        ArrayList<String> names = new ArrayList<String>(23);
        names.add("+1");
        names.add("*2");
        names.add("+3");
        names.add("+4");
        names.add("+5");
        names.add("+6");
        names.add("+7");
        names.add("+8");
        names.add("+9");
        names.add("+10");
        names.add("*11");
        names.add("+12");
        names.add("*13");
        names.add("+14");
        names.add("*15");
        names.add("+16");
        names.add("*17");
        names.add("+18");
        names.add("*19");
        names.add("+20");
        names.add("*21");
        names.add("+22");
        names.add("*23");
        return names;
    }

    @Override
    public ArrayList<String> namesPipeline() {
        ArrayList<String> names = new ArrayList<String>(23);
        names.add("+1");
        names.add("*2");
        names.add("+3");
        names.add("+4");
        names.add("+5");
        names.add("+6");
        names.add("+7");
        names.add("+8");
        names.add("+9");
        names.add("+10");
        names.add("*11");
        names.add("+12");
        names.add("*13");
        names.add("+14");
        names.add("*15");
        names.add("+16");
        names.add("*17");
        names.add("+18");
        names.add("*19");
        names.add("+20");
        names.add("*21");
        names.add("+22");
        names.add("*23");
        names.add("+1a");
        names.add("*2a");
        names.add("+3a");
        names.add("+4a");
        names.add("+5a");
        names.add("+6a");
        names.add("+7a");
        names.add("+8a");
        names.add("+9a");
        names.add("+10a");
        names.add("*11a");
        names.add("+12a");
        names.add("*13a");
        names.add("+14a");
        names.add("*15a");
        names.add("+16a");
        names.add("*17a");
        names.add("+18a");
        names.add("*19a");
        names.add("+20a");
        names.add("*21a");
        names.add("+22a");
        names.add("*23a");
        names.add("+1b");
        names.add("*2b");
        names.add("+3b");
        names.add("+4b");
        names.add("+5b");
        names.add("+6b");
        names.add("+7b");
        names.add("+8b");
        names.add("+9b");
        names.add("+10b");
        names.add("*11b");
        names.add("+12b");
        names.add("*13b");
        names.add("+14b");
        names.add("*15b");
        names.add("+16b");
        names.add("*17b");
        names.add("+18b");
        names.add("*19b");
        names.add("+20b");
        names.add("*21b");
        names.add("+22b");
        names.add("*23b");
        return names;
    }
}
