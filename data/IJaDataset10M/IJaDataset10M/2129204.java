package org.rakiura.bool;

/**
 * Represents a functional mapping on <code>n</code> inputs and 
 * <code>m</code> outputs. This implementation assumes one-to-one mapping
 * on fully defined domain. For not fully defined domains use 
 * <code>PartialMapping</code> instead. 
 * 
 * <br><br>
 * Function.java<br> 
 * Created: 17/02/2004 13:49:45<br> 
 * 
 * @author <a href="mariusz@rakiura.org">Mariusz Nowostawski</a>
 * @version @version@ $revision$
 */
public class Mapping implements Function {

    int inSize;

    int outSize;

    int alphabetSize;

    int[] outputValues;

    public Mapping(int inoutSize, int alphSize) {
        this(inoutSize, inoutSize, alphSize);
    }

    public Mapping(int inputs, int outputs, int alphSize) {
        this.inSize = inputs;
        this.outSize = outputs;
        this.alphabetSize = alphSize;
        this.outputValues = new int[(int) Math.pow(this.alphabetSize, this.inSize) * this.outSize];
    }

    public int[] getRandomInput() {
        final int[] in = new int[this.inSize];
        for (int i = 0; i < in.length; i++) {
            in[i] = AutomataToolbox.random.nextInt(this.alphabetSize);
        }
        return in;
    }

    public int[] evaluate(final int[] input) {
        return evaluate(input, new int[this.outSize]);
    }

    public int[] evaluate(final int[] input, final int[] output) {
        int index = 0;
        int j = input.length - 1;
        for (int i = 0; i < input.length; i++) {
            index += (int) Math.pow(this.alphabetSize, i) * input[j--] * this.outSize;
        }
        for (int i = 0; i < output.length; i++) {
            output[i] = this.outputValues[index + i];
        }
        return output;
    }

    public void setOutputValue(int[] input, int[] output) {
        int index = 0;
        int j = input.length - 1;
        for (int i = 0; i < input.length; i++) {
            index += (int) Math.pow(this.alphabetSize, i) * input[j--] * this.outSize;
        }
        for (int i = 0; i < output.length; i++) {
            this.outputValues[index + i] = output[i];
        }
    }

    public static Mapping createRandomMapping(int in, int out, int a) {
        final Mapping f = new Mapping(in, out, a);
        for (int i = 0; i < f.outputValues.length; i++) {
            f.outputValues[i] = AutomataToolbox.random.nextInt(a);
        }
        return f;
    }

    /** 
	 * Returns the output values in "nice permutation" order. 
	 * @return table handle.
	 **/
    public String getTable() {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < this.outputValues.length; i++) {
            s.append(this.outputValues[i]);
        }
        return s.toString();
    }

    public static final boolean nextPermutation(int[] p, int alphabet) {
        for (int i = p.length - 1; i >= 0; i--) {
            if (p[i] < alphabet - 1) {
                p[i] = p[i] + 1;
                break;
            }
            if (i > 0) {
                p[i] = 0;
            } else {
                p[0] = alphabet;
            }
        }
        if (p[0] == alphabet) {
            p[0] = 0;
            return false;
        }
        return true;
    }

    public static final String asString(int[] a) {
        StringBuffer s = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            s.append(a[i]);
        }
        return s.toString();
    }

    /** 
	 * Testing only. 
	 * @param args arguments. 
	 **/
    public static void main(String[] args) {
        Mapping f = new Mapping(2, 1, 2);
        f.setOutputValue(new int[] { 0, 0 }, new int[] { 0 });
        f.setOutputValue(new int[] { 0, 1 }, new int[] { 1 });
        f.setOutputValue(new int[] { 1, 0 }, new int[] { 0 });
        f.setOutputValue(new int[] { 1, 1 }, new int[] { 1 });
        System.out.println("Bool f: " + f.getTable());
        System.out.println(" 0 0  ->  " + f.evaluate(new int[] { 0, 0 })[0]);
        System.out.println(" 0 1  ->  " + f.evaluate(new int[] { 0, 1 })[0]);
        System.out.println(" 1 0  ->  " + f.evaluate(new int[] { 1, 0 })[0]);
        System.out.println(" 1 1  ->  " + f.evaluate(new int[] { 1, 1 })[0]);
        Mapping g = Mapping.createRandomMapping(2, 1, 2);
        System.out.println("Bool g: " + g.getTable());
        System.out.println(" 0 0  ->  " + g.evaluate(new int[] { 0, 0 })[0]);
        System.out.println(" 0 1  ->  " + g.evaluate(new int[] { 0, 1 })[0]);
        System.out.println(" 1 0  ->  " + g.evaluate(new int[] { 1, 0 })[0]);
        System.out.println(" 1 1  ->  " + g.evaluate(new int[] { 1, 1 })[0]);
        Mapping h = Mapping.createRandomMapping(3, 1, 3);
        System.out.println("Non-Bool h: " + h.getTable());
        System.out.println(" 0 0 0 ->  " + h.evaluate(new int[] { 0, 0, 0 })[0]);
        System.out.println(" 0 0 1 ->  " + h.evaluate(new int[] { 0, 0, 1 })[0]);
        System.out.println(" 0 0 2 ->  " + h.evaluate(new int[] { 0, 0, 2 })[0]);
        System.out.println(" 0 1 0 ->  " + h.evaluate(new int[] { 0, 1, 0 })[0]);
        System.out.println(" 0 1 1 ->  " + h.evaluate(new int[] { 0, 1, 1 })[0]);
        System.out.println(" 0 1 1 ->  " + h.evaluate(new int[] { 0, 1, 2 })[0]);
        System.out.println(" 0 2 0 ->  " + h.evaluate(new int[] { 0, 2, 0 })[0]);
        System.out.println(" 0 2 1 ->  " + h.evaluate(new int[] { 0, 2, 1 })[0]);
        System.out.println(" 0 2 2 ->  " + h.evaluate(new int[] { 0, 2, 2 })[0]);
        System.out.println(" 1 0 0 ->  " + h.evaluate(new int[] { 1, 0, 0 })[0]);
        System.out.println(" 1 0 1 ->  " + h.evaluate(new int[] { 1, 0, 1 })[0]);
        System.out.println(" 1 0 2 ->  " + h.evaluate(new int[] { 1, 0, 2 })[0]);
        System.out.println("\n==============");
        int[] w = new int[] { 0, 0, 0, 0, 0 };
        while (nextPermutation(w, 2)) System.out.println(asString(w));
    }
}
