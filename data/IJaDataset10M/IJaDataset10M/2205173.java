package horcher.maths2;

import java.util.Collection;
import java.util.Iterator;

public class Correlation extends Datensatz<ComplexD> {

    private static final long serialVersionUID = 1L;

    private final int samplerate;

    /**
	 * Creates an ArrayList containing a correlation. The samplerate is
	 * <code>samplerate</code>
	 * 
	 * @param c
	 *          Collection containing the data
	 * @param samplerate
	 *          samplerate
	 */
    public Correlation(final Collection<ComplexD> c, final int samplerate) {
        super(c);
        this.samplerate = samplerate;
        if (samplerate <= 0) throw new IllegalArgumentException("Samplerate must be higher than zero!");
        new ForEach<ComplexD>(c) {

            @Override
            public void doOne(final ComplexD one) {
                one.setIsNotComplex();
            }
        };
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public boolean add(final ComplexD mull) {
        return false;
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public void add(final int mull2, final ComplexD mull) {
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public boolean addAll(final Collection<? extends ComplexD> c) {
        return false;
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public boolean addAll(final int m, final Collection<? extends ComplexD> c) {
        return false;
    }

    @Override
    public Datensatz<Addinfo<ComplexD, Integer>> getMaxima() {
        final Datensatz<Addinfo<ComplexD, Integer>> namenlos = super.getMaxima();
        final Iterator<Addinfo<ComplexD, Integer>> it = namenlos.iterator();
        Addinfo<ComplexD, Integer> next;
        final int size = this.size();
        while (it.hasNext()) {
            next = it.next();
            if (next.getInfo() > size / 2) next.setInfo(next.getInfo() - size);
        }
        return namenlos;
    }

    public Datensatz<Addinfo<ComplexD, Integer>> getGoodMaxima() {
        final Datensatz<Addinfo<ComplexD, Integer>> namenlos = super.getMaxima();
        final Datensatz<Addinfo<ComplexD, Integer>> ret = new Datensatz<Addinfo<ComplexD, Integer>>();
        final Iterator<Addinfo<ComplexD, Integer>> it = namenlos.iterator();
        Addinfo<ComplexD, Integer> next;
        final int size = this.size();
        final int nmax = (int) (this.samplerate * horcher.Main.D_IN_M / horcher.Main.C_SCHALL);
        while (it.hasNext()) {
            next = it.next();
            if (next.getInfo() > size / 2) next.setInfo(next.getInfo() - size);
            if (next.getInfo() < nmax && next.getInfo() > -nmax) ret.add(next);
        }
        return ret;
    }

    public int getSamplerate() {
        return this.samplerate;
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public NotComplexD remove(final int mull) {
        return null;
    }

    /**
	 * @deprecated
	 */
    @Deprecated
    @Override
    public boolean remove(final Object o) {
        return false;
    }
}
