package sanger.argml.graph.model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.solr.util.OpenBitSet;
import sanger.argml.environment.Environment;
import sanger.argml.environment.Environmental;
import sanger.argml.io.TextInput;
import sanger.argml.io.TextOutput;
import sanger.math.set.NaturalDomain;
import sanger.math.set.NaturalSet;
import sanger.math.set.NaturalSetCollection;
import sanger.math.set.NaturalSetException;

public class HaplotypeSet extends Environmental implements Iterable<Haplotype> {

    private NaturalDomain haplotypeDomain;

    private NaturalDomain basePairDomain;

    private NaturalSetCollection markers;

    private NaturalSetCollection missing;

    private int[] markerPositions;

    private double[] marginalAlleleFrequency = null;

    private NaturalSetCollection markersTransposed = null;

    /**
	 * Constructs a new empty haplotype set.
	 * @param env The runtime enviroment
	 */
    private HaplotypeSet(Environment env) {
        super(env);
    }

    /**
	 * Constructs a new empty haplotype set.
	 * @param env The runtime enviroment
	 * @param haplotypeDomain The haplotype domain
	 * @param snpDomain The snp domain.
	 * @param markerPositions marker positions on the chromosome coordinates.
	 */
    public HaplotypeSet(Environment env, NaturalDomain haplotypeDomain, NaturalDomain snpDomain, int[] markerPositions) {
        super(env);
        this.haplotypeDomain = haplotypeDomain;
        this.markers = new NaturalSetCollection(snpDomain, haplotypeDomain.cardinality());
        ;
        this.missing = new NaturalSetCollection(snpDomain, haplotypeDomain.cardinality());
        ;
        this.markerPositions = markerPositions;
        try {
            this.basePairDomain = new NaturalDomain(markerPositions[0], markerPositions[markerPositions.length - 1]);
        } catch (NaturalSetException e) {
            env.log().printError(e);
        }
    }

    public NaturalSetCollection haplotypes() {
        return markers;
    }

    public int[] markerPositions() {
        return markerPositions;
    }

    private void clearLazy() {
        marginalAlleleFrequency = null;
        markersTransposed = null;
    }

    public void add(Haplotype haplotype) throws NaturalSetException {
        markers.add(haplotype.getMarkers());
        missing.add(haplotype.getMissing());
        clearLazy();
    }

    public static HaplotypeSet clipSnpDomain(HaplotypeSet source, NaturalSet region) throws NaturalSetException {
        HaplotypeSet clip = null;
        if (source.markers.domain().equals(region.domain())) {
            NaturalDomain clipSnpDomain = source.markers.domain().createSubDomain(region);
            int[] mp = new int[clipSnpDomain.closureCardinality()];
            if (clipSnpDomain.isContinuous()) {
                for (int i = clipSnpDomain.min(); i <= clipSnpDomain.max(); i++) {
                    mp[clipSnpDomain.toRelativeCoordinate(i)] = source.markerPositions[source.markers.domain().toRelativeCoordinate(i)];
                }
            } else {
                for (int i = clipSnpDomain.min(); i <= clipSnpDomain.max(); i++) {
                    if (clipSnpDomain.contains(i)) {
                        mp[clipSnpDomain.toRelativeCoordinate(i)] = source.markerPositions[source.markers.domain().toRelativeCoordinate(i)];
                    } else {
                        mp[clipSnpDomain.toRelativeCoordinate(i)] = 0;
                    }
                }
            }
            clip = new HaplotypeSet(source.env(), source.haplotypeDomain, clipSnpDomain, mp);
            for (Haplotype h : source) {
                clip.add(Haplotype.project(clipSnpDomain, h));
            }
        }
        return clip;
    }

    public static HaplotypeSet clipHaplotypeDomain(HaplotypeSet source, NaturalDomain domain) throws NaturalSetException {
        HaplotypeSet hs = new HaplotypeSet(source.env(), domain, source.snpDomain(), source.markerPositions.clone());
        for (int i = 0; i < source.markers.size(); i++) {
            if (domain.contains(i)) {
                hs.add(new Haplotype(source.markers.get(i).clone(), source.missing.get(i).clone()));
            }
        }
        return hs;
    }

    public static HaplotypeSet readFastPHASE(Environment env, TextInput fpin, TextInput fpout) throws IOException, NaturalSetException, TooManyAllelesExcpetion {
        HaplotypeSet result = new HaplotypeSet(env);
        Matcher matcher;
        fpin.reader().readLine();
        NaturalDomain snpDomain = new NaturalDomain(Integer.parseInt(fpin.reader().readLine()));
        result.markerPositions = new int[snpDomain.closureCardinality()];
        matcher = Pattern.compile("[1-9][0-9]*").matcher(fpin.reader().readLine());
        int i = 0;
        while (matcher.find()) {
            if (i >= result.markerPositions.length) throw new TooManyAllelesExcpetion("Too many markers found in header");
            result.markerPositions[i] = Integer.parseInt(matcher.group());
            i++;
        }
        CharacterHaplotypeSet characterHaplotypes = new CharacterHaplotypeSet(snpDomain);
        Pattern haplotypePattern = Pattern.compile("^[0-9ATCGM]{" + snpDomain.closureCardinality() + "}$");
        String line = fpout.reader().readLine();
        while (null != line) {
            line = line.replace(" ", "").replace("?", "M");
            matcher = haplotypePattern.matcher(line);
            if (matcher.matches()) {
                characterHaplotypes.add(line.toCharArray());
            }
            line = fpout.reader().readLine();
        }
        result.markers = characterHaplotypes.getHaplotypes();
        result.missing = characterHaplotypes.getMissingNucleotides();
        result.haplotypeDomain = new NaturalDomain(result.markers.size());
        result.basePairDomain = new NaturalDomain(result.markerPositions[0], result.markerPositions[result.markerPositions.length - 1]);
        return result;
    }

    public static HaplotypeSet readFastPHASE(Environment env, TextInput fpin) throws IOException, NaturalSetException, TooManyAllelesExcpetion {
        HaplotypeSet result = new HaplotypeSet(env);
        Matcher matcher;
        fpin.reader().readLine();
        NaturalDomain snpDomain = new NaturalDomain(Integer.parseInt(fpin.reader().readLine()));
        result.markerPositions = new int[snpDomain.closureCardinality()];
        matcher = Pattern.compile("[1-9][0-9]*").matcher(fpin.reader().readLine());
        int i = 0;
        while (matcher.find()) {
            if (i >= result.markerPositions.length) throw new TooManyAllelesExcpetion("Too many markers found in header");
            result.markerPositions[i] = Integer.parseInt(matcher.group());
            i++;
        }
        CharacterHaplotypeSet characterHaplotypes = new CharacterHaplotypeSet(snpDomain);
        Pattern haplotypePattern = Pattern.compile("^[0-9ATCGM]{" + snpDomain.closureCardinality() + "}$");
        String line = fpin.reader().readLine();
        while (null != line) {
            line = line.replace(" ", "").replace("?", "M");
            matcher = haplotypePattern.matcher(line);
            if (matcher.matches()) {
                characterHaplotypes.add(line.toCharArray());
            }
            line = fpin.reader().readLine();
        }
        result.markers = characterHaplotypes.getHaplotypes();
        result.missing = characterHaplotypes.getMissingNucleotides();
        result.haplotypeDomain = new NaturalDomain(result.markers.size());
        result.basePairDomain = new NaturalDomain(result.markerPositions[0], result.markerPositions[result.markerPositions.length - 1]);
        return result;
    }

    public void write(TextOutput out) throws IOException {
        out.writer().println(markers.size() + " 0 " + markers.domain().cardinality());
        for (int i = 0; i < markerPositions.length; i++) {
            out.writer().print(markerPositions[i]);
            out.writer().println();
        }
        for (int i = 0; i < markers.size(); i++) {
            OpenBitSet mk = markers.get(i).map();
            OpenBitSet ms = missing.get(i).map();
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j <= markers.domain().last(); j++) {
                if (ms.fastGet(j)) sb.append('M'); else sb.append(mk.fastGet(j) ? '1' : '0');
            }
            out.writer().println(sb.toString());
        }
    }

    public String toString() {
        StringBuilder display = new StringBuilder();
        display.append("Haplotypes {");
        display.append(" SNPS: ");
        display.append(snpDomain());
        display.append(", SEQS: ");
        display.append(haplotypeDomain);
        display.append(" }");
        return display.toString();
    }

    public Iterator<Haplotype> iterator() {
        return new HaplotypeIterator(this);
    }

    /**
	 * Iterates over {@link Haplotype haplotypes} in a {@link HaplotypeSet}.
	 * @author lg8
	 */
    public class HaplotypeIterator implements Iterator<Haplotype> {

        private Iterator<NaturalSet> markerIterator;

        private Iterator<NaturalSet> missingIterator;

        public HaplotypeIterator(HaplotypeSet haplotypeSet) {
            markerIterator = haplotypeSet.markers.iterator();
            missingIterator = haplotypeSet.missing.iterator();
        }

        public boolean hasNext() {
            return markerIterator.hasNext() && missingIterator.hasNext();
        }

        public Haplotype next() {
            return new Haplotype(markerIterator.next(), missingIterator.next());
        }

        public void remove() {
            markerIterator.remove();
            missingIterator.remove();
        }
    }

    /**
	 * @return the basePairDomain
	 */
    public NaturalDomain basePairDomain() {
        return basePairDomain;
    }

    /**
	 * @return the haplotypeDomain
	 */
    public NaturalDomain haplotypeDomain() {
        return haplotypeDomain;
    }

    /**
	 * @return the snpDomain
	 */
    public NaturalDomain snpDomain() {
        return markers.domain();
    }

    public boolean hasMissing() {
        boolean result = false;
        for (NaturalSet s : missing) {
            if (!s.isEmpty()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public int coordinate(int i) throws NaturalSetException {
        return markerPositions[snpDomain().toRelativeCoordinate(i)];
    }

    private static class CharacterHaplotypeSet implements Iterable<char[]> {

        protected NaturalDomain domain;

        protected ArrayList<char[]> characterSets;

        protected NaturalSetCollection markers;

        protected NaturalSetCollection missing;

        public CharacterHaplotypeSet(NaturalDomain domain, int size) {
            this.domain = domain;
            this.characterSets = new ArrayList<char[]>(size);
            this.markers = null;
            this.missing = null;
        }

        public CharacterHaplotypeSet(NaturalDomain domain) {
            this.domain = domain;
            this.characterSets = new ArrayList<char[]>();
            this.markers = null;
            this.missing = null;
        }

        public CharacterHaplotypeSet transpose() throws NaturalSetException {
            CharacterHaplotypeSet t = new CharacterHaplotypeSet(new NaturalDomain(this.cardinality()), this.domain.closureCardinality());
            for (int j = 0; j < this.domain.closureCardinality(); j++) {
                char[] column = new char[this.cardinality()];
                for (int i = 0; i < this.cardinality(); i++) {
                    column[i] = this.characterSets.get(i)[j];
                }
                t.add(column);
            }
            return t;
        }

        public void add(char[] characterSet) throws NaturalSetException {
            if (domain.closureCardinality() == characterSet.length) {
                characterSets.add(characterSet);
            } else {
                throw new NaturalSetException("Incompatible domains");
            }
        }

        public int cardinality() {
            return characterSets.size();
        }

        private void convertToBinary() throws NaturalSetException, TooManyAllelesExcpetion {
            CharacterHaplotypeSet transpose = transpose();
            NaturalSetCollection hSet = new NaturalSetCollection(transpose.domain(), transpose.cardinality());
            NaturalSetCollection mSet = new NaturalSetCollection(transpose.domain(), transpose.cardinality());
            for (char[] row : transpose) {
                Character a = null, b = null;
                OpenBitSet binRow = new OpenBitSet(row.length);
                OpenBitSet binMissingRow = new OpenBitSet(row.length);
                for (int i = 0; i < row.length; i++) {
                    if (row[i] != 'M') {
                        if (a == null) {
                            a = row[i];
                        } else if (b == null && row[i] != a.charValue()) {
                            b = row[i];
                        } else if (b != null && row[i] != a.charValue() && row[i] != b.charValue()) {
                            throw new TooManyAllelesExcpetion("Column " + i + " has too many alleles");
                        }
                        if (b != null && row[i] == b) binRow.fastFlip(i);
                    } else {
                        binMissingRow.fastFlip(i);
                    }
                }
                hSet.add(new NaturalSet(transpose.domain(), binRow));
                mSet.add(new NaturalSet(transpose.domain(), binMissingRow));
            }
            markers = NaturalSetCollection.transpose(hSet);
            missing = NaturalSetCollection.transpose(mSet);
        }

        public void print(PrintWriter w) throws IOException {
            for (int i = 0; i < this.cardinality(); i++) {
                w.println(characterSets.get(i));
            }
        }

        public NaturalDomain domain() {
            return domain;
        }

        public Iterator<char[]> iterator() {
            return new RowIterator(this);
        }

        public class RowIterator implements Iterator<char[]> {

            private Iterator<char[]> iterator;

            public RowIterator(CharacterHaplotypeSet matrix) {
                iterator = matrix.characterSets.iterator();
            }

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public char[] next() {
                return iterator.next();
            }

            public void remove() {
                iterator.remove();
            }
        }

        public NaturalSetCollection getHaplotypes() throws NaturalSetException, TooManyAllelesExcpetion {
            if (markers == null) {
                convertToBinary();
            }
            return markers;
        }

        public NaturalSetCollection getMissingNucleotides() throws NaturalSetException, TooManyAllelesExcpetion {
            if (missing == null) {
                convertToBinary();
            }
            return missing;
        }
    }

    public double[] marginalAlleleFrequencies() throws NaturalSetException {
        if (marginalAlleleFrequency == null) {
            marginalAlleleFrequency = new double[markersTransposed().size()];
            int j = 0;
            for (NaturalSet marker : markersTransposed()) {
                marginalAlleleFrequency[j] = (double) marker.cardinality() / (double) markers.size();
                j++;
            }
        }
        return marginalAlleleFrequency;
    }

    public double[] minorAlleleFrequencies() throws NaturalSetException {
        double[] result = new double[marginalAlleleFrequencies().length];
        for (int i = 0; i < result.length; i++) {
            double value = marginalAlleleFrequencies()[i];
            result[i] = (value <= 0.5) ? value : 1 - value;
        }
        return result;
    }

    public double marginalAlleleFrequency(int i) throws NaturalSetException {
        return marginalAlleleFrequencies()[snpDomain().toRelativeCoordinate(i)];
    }

    public double minorAlleleFrequency(int i) throws NaturalSetException {
        double value = marginalAlleleFrequency(i);
        return (value <= 0.5) ? value : 1 - value;
    }

    private NaturalSetCollection markersTransposed() throws NaturalSetException {
        if (markersTransposed == null) {
            markersTransposed = NaturalSetCollection.transpose(markers);
        }
        return markersTransposed;
    }

    public double rsquare(int i, int j) throws NaturalSetException {
        double result = 0;
        double pi = marginalAlleleFrequency(i);
        double pj = marginalAlleleFrequency(j);
        if (pi > 0 && pj > 0) {
            NaturalSet allelei = markersTransposed().get(snpDomain().toRelativeCoordinate(i));
            NaturalSet allelej = markersTransposed().get(snpDomain().toRelativeCoordinate(j));
            result = (double) NaturalSet.intersect(allelei, allelej).cardinality();
            result /= (double) markers.size();
            result -= (pi * pj);
            result *= result;
            result /= (pi * (1 - pi) * pj * (1 - pj));
            if (result > 1.0) {
                if (result < 1.05) result = 1.0; else env().log().printInfo("warrning LD float error of more then 5% over 1.0 [" + i + "," + j + "]:" + result);
            }
        }
        return result;
    }
}
