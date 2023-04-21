package apollo.datamodel;

import java.lang.Integer;
import java.util.*;
import javax.swing.JOptionPane;
import apollo.config.Config;
import apollo.util.SeqFeatureUtil;
import apollo.util.FeatureList;
import apollo.util.QuickSort;
import org.apache.log4j.*;
import org.apache.oro.text.*;
import org.apache.oro.text.regex.*;

/**
 * I think SeqFeature should implement all the functions of FeatureSet, so its
 * polymorphic, so when you have a SeqFeatureI you dont have to care whether its
 * a SeqFeature or a FeatureSet, so you dont have to keep downcasting to descend 
 * the datamodel (or do any other feature set functionality)
 * As now you go through the Vector of SeqFeatures, cast to SeqFeature,
 * then ask instanceof FeatureSetI and cast to FeatureSetI, which
 * is potentially confusing.
 *
 */
public class FeatureSet extends SeqFeature implements FeatureSetI {

    protected static final Logger logger = LogManager.getLogger(FeatureSet.class);

    public static byte POLYA_REMOVED = 1 << 1;

    /** sometimes the translation may have an unconventional start
      codon and we need to note this */
    protected static String standard_start_codon = "ATG";

    protected Vector features = new Vector(2);

    protected SequenceI hitSequence = null;

    /** 
     This flag is just for the use of bop to indicate that the 
     feature has already had a an exon removed. Or for other
     simple flags that we don't want to allocate an entire boolean
     to. 
  */
    protected byte flags;

    int translation_start = 0;

    int translation_end = 0;

    protected int plus1_frameshift = 0;

    protected int minus1_frameshift = 0;

    protected String readthrough_stop = null;

    public String genericReadThroughStopResidue = "X";

    private int readthrough_pos = 0;

    protected boolean trans_spliced = false;

    protected boolean missing_5prime = false;

    protected boolean missing_3prime = false;

    protected String start_codon = null;

    /**
   * @see #setProteinCodingGene(boolean)
   */
    private boolean isProteinCodingGene = false;

    public FeatureSet() {
        initRange();
    }

    public FeatureSet(SeqFeatureI sf) {
        super(sf);
    }

    public FeatureSet(String type, int strand) {
        initRange();
        setFeatureType(type);
        setStrand(strand);
    }

    public FeatureSet(int low, int high, String type, int strand) {
        super(low, high, type, strand);
    }

    public FeatureSet(FeatureList kids, String name, String type, int strand) {
        initRange();
        setName(name);
        setFeatureType(type);
        setStrand(strand);
        for (int i = 0; i < kids.size(); i++) addFeature(kids.getFeature(i));
    }

    private void initRange() {
        setStart(1000000000);
        setEnd(-1000000000);
    }

    public FeatureSet(FeatureSetI fs, String class_name) {
        super(fs);
        fs.sort(getStrand());
        try {
            Class sf_class = Class.forName(class_name);
            for (int i = 0; i < fs.getFeatures().size(); i++) {
                SeqFeatureI sf_from = fs.getFeatureAt(i);
                SeqFeatureI sf_to = ((SeqFeatureI) sf_class.newInstance());
                sf_to.setStrand(sf_from.getStrand());
                sf_to.setLow(sf_from.getLow());
                sf_to.setHigh(sf_from.getHigh());
                sf_to.setFeatureType(sf_from.getFeatureType());
                addFeature(sf_to);
            }
        } catch (Exception e) {
            logger.error("Unable to get feature class " + class_name, e);
        }
        FeatureSetI parent = (FeatureSetI) getRefFeature();
        if (parent != null) {
            parent.adjustEdges();
            if (parent.getFeatureType().equalsIgnoreCase("gene")) calcTranslationStartForLongestPeptide();
        }
    }

    /** FeatureSets have translations. It happens to be themselves - for now FeatSet
      implements all the TranslationI methods - in the future this might be farmed
      out to a separate Translation object */
    public boolean hasTranslation() {
        return true;
    }

    /** FeatureSetI itself implements TranslationI so just return self - in future this
      may be done with a separate translation object */
    public TranslationI getTranslation() {
        return this;
    }

    public String getName() {
        if (name.equals(Range.NO_NAME) && canHaveChildren() && size() > 0 && !(this instanceof Transcript)) return getFeatureAt(0).getName(); else return name;
    }

    public boolean hasNameBeenSet() {
        return !name.equals(Range.NO_NAME);
    }

    public int size() {
        return features != null ? features.size() : 0;
    }

    /** Add feature to end of features list, recalc low and high */
    public void addFeature(SeqFeatureI feature) {
        insertFeatureAt(feature, features.size());
    }

    public void addFeature(SeqFeatureI feature, boolean sort) {
        if (feature == null) {
            throw new NullPointerException("Can't add null feature");
        }
        if (!sort) {
            insertFeatureAt(feature, features.size());
        } else {
            insertFeatureAt(feature, calculateSortPosition(feature));
        }
    }

    /** Add feature(kid) to features list at position position. 
      Recalculate low and high */
    protected void insertFeatureAt(SeqFeatureI feature, int position) {
        if (feature == null) {
            throw new NullPointerException("Can't add null feature");
        }
        if (!features.contains(feature)) {
            if (position >= features.size()) features.addElement(feature); else features.insertElementAt(feature, position);
            feature.setRefFeature(this);
        }
        if (getRefSequence() != null && feature.getRefSequence() == null) {
            feature.setRefSequence(getRefSequence());
        } else if (this.getRefSequence() == null && feature.getRefSequence() != null) {
            this.setRefSequence(feature.getRefSequence());
        }
        adjustEdges(feature);
    }

    private int calculateSortPosition(SeqFeatureI feature) {
        int setSize = size();
        int location = setSize;
        if (setSize > 0) {
            if (feature.getStrand() != getStrand() || getStrand() == 0) {
                boolean found = false;
                for (int i = 0; i < setSize && !found; i++) {
                    SeqFeatureI sf = getFeatureAt(i);
                    if (feature.getLow() < sf.getLow()) {
                        location = i;
                        found = true;
                    }
                }
            } else {
                if (pastThreePrimeEnd(feature)) {
                    location = setSize;
                } else if (beforeFivePrimeEnd(feature)) {
                    location = 0;
                } else {
                    boolean found = false;
                    SeqFeatureI preceding_sf = null;
                    for (int i = 0; i < setSize && !found; i++) {
                        SeqFeatureI sf = getFeatureAt(i);
                        if ((feature.getStrand() == 1 && feature.getStart() < sf.getStart() && (preceding_sf == null || feature.getStart() >= preceding_sf.getStart())) || (feature.getStrand() == -1 && feature.getStart() > sf.getStart() && (preceding_sf == null || feature.getStart() <= preceding_sf.getStart()))) {
                            location = i;
                            found = true;
                        }
                        preceding_sf = sf;
                    }
                }
            }
        }
        return location;
    }

    /**
   * Returns true if the feature passed in has a 5 prime start
   * that is located beyound the 3prime end of this feature.
   * False is the feature is not 3prime of this feature
   */
    public boolean pastThreePrimeEnd(SeqFeatureI feature) {
        if (feature.getStrand() != getStrand()) {
            logger.info(getName() + " and  " + feature.getName() + " are on opposite strands, so 5prime test " + " is not relevant");
            return false;
        } else {
            return ((getStrand() == 1 && feature.getLow() > getHigh()) || (getStrand() == -1 && feature.getHigh() < getLow()));
        }
    }

    /**
   * Returns true if the feature passed in has a 3 prime end
   * that is more 5prime than this feature. False is the feature
   * is not 5prime of this feature
   */
    public boolean beforeFivePrimeEnd(SeqFeatureI feature) {
        if (feature.getStrand() != getStrand()) {
            logger.info(getName() + " and  " + feature.getName() + " are on opposite strands, so 5prime test " + " is not relevant");
            return false;
        } else {
            return ((feature.getStrand() == 1 && feature.getHigh() < getLow()) || (feature.getStrand() == -1 && feature.getLow() > getHigh()));
        }
    }

    public void deleteFeature(SeqFeatureI feature) {
        if (feature == null) {
            throw new NullPointerException("Can't remove null feature");
        }
        for (int i = 0; i < features.size(); ++i) {
            SeqFeatureI feat = (SeqFeatureI) features.get(i);
            if (feat == feature || feat.isClone() && feat.getCloneSource() == feature) {
                features.removeElementAt(i);
                adjustEdges();
                break;
            }
        }
    }

    public SeqFeatureI deleteFeatureAt(int i) {
        if (i < size()) {
            SeqFeatureI sf = getFeatureAt(i);
            features.removeElementAt(i);
            adjustEdges();
            return sf;
        }
        return null;
    }

    public SeqFeatureI getFeatureAt(int i) {
        return (i >= 0 && i < features.size() ? (SeqFeatureI) features.elementAt(i) : null);
    }

    public Vector getFeatures() {
        return features;
    }

    public void clearKids() {
        features = new Vector();
    }

    public FeatureList getLeafFeatsOver(int pos) {
        FeatureList leafFeatsOver = new FeatureList();
        if (!hasKids()) return super.getLeafFeatsOver(pos);
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            leafFeatsOver.addAll(getFeatureAt(i).getLeafFeatsOver(pos));
        }
        return leafFeatsOver;
    }

    /** Set low and high according to lowest and highest coord in kids */
    public void adjustEdges() {
        low = 1000000000;
        high = -1000000000;
        for (int i = 0; i < size(); i++) {
            adjustEdges(getFeatureAt(i));
        }
    }

    /** If span has higher high and or lower low than current, reset high/low */
    public void adjustEdges(SeqFeatureI span) {
        boolean adjusted = false;
        if (span.getLow() < getLow()) {
            setLow(span.getLow());
            adjusted |= true;
        }
        if (span.getHigh() > high) {
            setHigh(span.getHigh());
            adjusted |= true;
        }
        if (adjusted && getRefFeature() != null) {
            ((FeatureSetI) getRefFeature()).adjustEdges();
        }
    }

    /** Returns FeatureList of Features with hit name. If parents alignement
   * seq display id has name doesnt look at children
   * This has pretty much been replaced by findFeaturesByAllNames,
   * which checks hit names as well as regular names. So should this
   * method be deleted, or is there still some use for it?
   */
    public FeatureList findFeaturesByHitName(String hname) {
        FeatureList feats = new FeatureList();
        if (getHitSequence() != null) {
            if (getHitSequence().getName().equals(hname)) {
                feats.addFeature(this);
                return feats;
            }
        }
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            SeqFeatureI sf = getFeatureAt(i);
            if (sf instanceof FeaturePairI) {
                if (((FeaturePairI) sf).getHname().equals(hname)) {
                    feats.addFeature(sf);
                }
            } else if (sf.canHaveChildren()) {
                SequenceI s = ((FeatureSetI) sf).getHitSequence();
                boolean found = false;
                if (s != null) {
                    if (s.getName().equals(hname)) {
                        feats.addFeature(sf);
                        found = true;
                    }
                }
                if (!found) {
                    FeatureList hitChildren = ((FeatureSetI) sf).findFeaturesByHitName(hname);
                    feats.addAllFeatures(hitChildren);
                }
            }
        }
        return feats;
    }

    public FeatureList findFeaturesByName(String name) {
        return findFeaturesByName(name, false);
    }

    public FeatureList findFeaturesByName(String name, boolean kidNamesOverParent) {
        FeatureList feats = new FeatureList();
        boolean selfMatches = false;
        if (getName().equalsIgnoreCase(name) || (getId() != null && getId().equalsIgnoreCase(name))) {
            if (kidNamesOverParent) {
                selfMatches = true;
            } else {
                feats.addFeature(this);
                return feats;
            }
        }
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            SeqFeatureI sf = getFeatureAt(i);
            if (sf.getName().equalsIgnoreCase(name) || (getId() != null && getId().equalsIgnoreCase(name))) {
                feats.addFeature(sf);
            } else if (sf.canHaveChildren()) {
                FeatureList fl = ((FeatureSetI) sf).findFeaturesByName(name);
                feats.addAllFeatures(fl);
            }
        }
        if (kidNamesOverParent && selfMatches && feats.isEmpty()) feats.addFeature(this);
        return feats;
    }

    /** Searches recursively on both name and hit name */
    public FeatureList findFeaturesByAllNames(String name) {
        return findFeaturesByAllNames(name, false);
    }

    public FeatureList findFeaturesByAllNames(String searchString, boolean useRegExp) {
        return findFeaturesByAllNames(searchString, useRegExp, false);
    }

    /** useRegExp is whether to search using pattern as a regular expression
      In fact, we ALWAYS do a RegExp search with the ORO pattern matchers.
      However, if useRegExp is FALSE, we prepend "^" on the pattern,
      but we still allow user to specify a "*" wildcard, which we 
      replace with ".*" for compatibility with RegExp. Note there is no longer 
      any need to lower case names and patterns as the pattern will be
      compiled with the CASE_INSENSITIVE_MASK 
      @parameter kidNamesOverParents means if a kid/descendant has the searchString
      then return the kid instead of the parent - even if the parent matches
      this is needed as if a parent as NO_NAME it will take on the name of its
      first kid - so you could end up with an ancestor of the feature you really
      are after.
  */
    public FeatureList findFeaturesByAllNames(String searchString, boolean useRegExp, boolean kidNamesOverParent) {
        FeatureList feats = new FeatureList();
        Perl5Compiler p5Compiler = new Perl5Compiler();
        Perl5Pattern pattern = null, defaultPattern = null;
        Perl5Matcher matcher = new Perl5Matcher();
        if (!useRegExp) {
            searchString = prepareNonRegExpString(searchString);
        }
        try {
            defaultPattern = (Perl5Pattern) p5Compiler.compile("invalid perl5 expression");
            pattern = (Perl5Pattern) p5Compiler.compile(searchString, p5Compiler.CASE_INSENSITIVE_MASK);
        } catch (MalformedPatternException ex) {
            pattern = defaultPattern;
        }
        if (matcher.contains("no_name", pattern)) return feats;
        boolean selfMatches = false;
        if (matcher.contains(getName(), pattern)) {
            if (kidNamesOverParent) {
                selfMatches = true;
            } else {
                feats.addFeature(this);
                return feats;
            }
        } else if (getHitSequence() != null) {
            if (matcher.contains(getHitSequence().getName(), pattern)) {
                if (kidNamesOverParent) {
                    selfMatches = true;
                } else {
                    feats.addFeature(this);
                    return feats;
                }
            }
        }
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            SeqFeatureI sf = getFeatureAt(i);
            if (matcher.contains(sf.getName(), pattern)) {
                feats.addFeature(sf);
            } else if (sf instanceof FeaturePairI) {
                if (matcher.contains(((FeaturePairI) sf).getHname(), pattern)) {
                    feats.addFeature(sf);
                }
            } else if (sf.canHaveChildren()) {
                SequenceI s = ((FeatureSetI) sf).getHitSequence();
                boolean found = false;
                if (s != null) {
                    if (matcher.contains(s.getName(), pattern)) {
                        feats.addFeature(sf);
                        found = true;
                    }
                }
                if (!found) {
                    FeatureList hitChildren = ((FeatureSetI) sf).findFeaturesByAllNames(searchString, true);
                    feats.addAllFeatures(hitChildren);
                }
            }
        }
        if (kidNamesOverParent && selfMatches && feats.isEmpty()) feats.addFeature(this);
        return feats;
    }

    public void setRefSequence(SequenceI seq) {
        this.refSeq = seq;
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            getFeatureAt(i).setRefSequence(seq);
        }
    }

    /** Is this an explicit alignment used by jalview? -
   * the alternative to cigars?
   *   no I dont think it is - its used by the analysis adapters & game
   * if kids have hit feats then this is the hit seq associated with them
   * This is a convenience for having to get hit seq from kids */
    public void setHitSequence(SequenceI seq) {
        this.hitSequence = seq;
    }

    /** if kids have hit feats then this is the hit seq associated with them
   * This is a convenience for having to get hit seq from kids */
    public SequenceI getHitSequence() {
        return this.hitSequence;
    }

    public double getScore() {
        double bestScore = 0.0;
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            if (getFeatureAt(i).getScore() > bestScore) {
                bestScore = getFeatureAt(i).getScore();
            }
        }
        return bestScore;
    }

    public int getNumberOfDescendents() {
        int numFeat = 0;
        int setSize = size();
        for (int i = 0; i < setSize; i++) {
            SeqFeatureI feat = getFeatureAt(i);
            if (feat.hasKids()) {
                numFeat += ((FeatureSetI) feat).getNumberOfDescendents();
            } else {
                numFeat++;
            }
        }
        return numFeat;
    }

    /** This method determines if there are any child SeqFeatures in
      this set (FeatureSets are NOT included). */
    public boolean canHaveChildren() {
        return true;
    }

    public boolean hasDescendents() {
        int setSize = size();
        boolean leaves = false;
        for (int i = 0; i < setSize && !leaves; i++) {
            SeqFeatureI feat = getFeatureAt(i);
            if (feat.canHaveChildren()) {
                if (((FeatureSetI) feat).hasDescendents()) {
                    leaves = true;
                }
            } else {
                leaves = true;
            }
        }
        return leaves;
    }

    /** Returns the FIRST feature in the set containing the position 
      return this if none of children contain pos but FS does (intron)
      return null if doesnt contain position.  */
    public SeqFeatureI getFeatureContaining(int position) {
        int setSize = size();
        SeqFeatureI container = null;
        if (canHaveChildren()) {
            for (int i = 0; i < setSize && container == null; i++) {
                SeqFeatureI sf = getFeatureAt(i);
                if (sf.contains(position)) container = sf;
            }
        } else {
            if (this.contains(position)) container = this;
        }
        return container;
    }

    public int getIndexContaining(int position) {
        int setSize = size();
        int feature_index = -1;
        for (int i = 0; i < setSize && feature_index < 0; i++) {
            SeqFeatureI sf = getFeatureAt(i);
            if (sf.contains(position)) feature_index = i;
        }
        return feature_index;
    }

    public int getFeatureIndex(SeqFeatureI sf) {
        if (features.contains(sf)) {
            return features.indexOf(sf);
        } else {
            return -1;
        }
    }

    public void sort(int sortStrand) {
        sort(sortStrand, false);
    }

    /** sort the child features of the set
   * 
   * @param sortStrand - sort by minus strand or positive/no strand
   * @param byLow - sort by genomic position
   * @see SeqFeatureUtil.sort()
   */
    public void sort(int sortStrand, boolean byLow) {
        SeqFeatureUtil.sort(features, sortStrand, byLow);
        adjustEdges();
    }

    public String translate() {
        String aa = "";
        String mRNA = get_cDNA();
        if (mRNA == null) {
            return "";
        }
        if (mRNA.length() > 2) {
            aa = getUntrimmedAA(get_ORF(mRNA), getFeaturePosition(translation_start) - 1);
            logger.debug("FeatureSet.translate(): getUntrimmedAA() returned " + aa);
        }
        if (unConventionalStart()) {
            if (aa.length() < 2) {
                logger.error("untrimmed aa sequence for " + getName() + ", which has unconventional start, is too short: " + aa + " mRNA = " + mRNA);
                return null;
            }
            aa = "M" + aa.substring(1);
        }
        return aa;
    }

    /** This sets the start at a standard start
      codon that gives the longest peptide (which may not be the first start codon).
      The stop codon is also automatically set (if there is one). If a longer peptide
      results from starting at the beginning of the transcript (with missing 5 prime)
      then the start of the transcript is used and missing5Pime is true. Also sets stop
      - if there is no stop, missing 3 prime is set to true (& trans end is 0)*/
    public void calcTranslationStartForLongestPeptide() {
        logger.debug("setting translation start at first codon for " + getName());
        translation_end = 0;
        setMissing5prime(false);
        String the_mRNA = get_cDNA();
        if (the_mRNA == null) {
            logger.warn("calcTranslationStartForLongetPep: no cDNA sequence for " + getName());
            return;
        }
        String longest_peptide = "";
        int best_start_index = -1;
        logger.debug("setTranslationStartAtFirstCodon: the_mRNA.length() = " + the_mRNA.length());
        if (the_mRNA.length() > 3) {
            int start_index = the_mRNA.indexOf(standard_start_codon);
            while (start_index >= 0) {
                String aa = getTrimmedAA(get_ORF(the_mRNA, start_index, -1), start_index);
                if (aa.length() > longest_peptide.length()) {
                    longest_peptide = aa;
                    best_start_index = start_index;
                }
                start_index = the_mRNA.indexOf(standard_start_codon, start_index + 1);
            }
            if (Config.getStyle().isPartialORFsAllowed()) {
                start_index = 0;
                while (start_index < 3) {
                    String orf = get_ORF(the_mRNA, start_index, -1);
                    String aa = getTrimmedAA(orf, start_index);
                    if (aa.length() > longest_peptide.length()) {
                        setMissing5prime(true);
                        longest_peptide = aa;
                        best_start_index = start_index;
                    }
                    start_index++;
                }
            }
        }
        if (best_start_index >= 0) {
            boolean setEnd = true;
            initTranslationStart(getGenomicPosition(best_start_index + 1), setEnd);
        } else {
            translation_start = 0;
            setMissing5prime(true);
            setMissing3prime(true);
        }
    }

    private String getUntrimmedAA(String orf, int orf_offset) {
        String aa = na2aa(orf);
        int stop_index = aa.indexOf('*');
        if (stop_index >= 0 && readthrough_stop != null) {
            int stop_offset = (3 * stop_index) + orf_offset;
            readthrough_pos = getGenomicPosition(stop_offset + 1);
            String tmp = (aa.substring(0, stop_index) + readthrough_stop + aa.substring(stop_index + 1));
            aa = tmp;
        }
        return aa;
    }

    private String getTrimmedAA(String orf, int orf_offset) {
        String aa = getUntrimmedAA(orf, orf_offset);
        int stop_index = aa.indexOf('*');
        if (stop_index >= 0) aa = aa.substring(0, stop_index);
        return aa;
    }

    /**
   * A setter is needed for some featureSet, like gene prediction results, 
   * which can have translation start and stop whithout being instances of transcripts   
   * @author cpommier
   */
    public void setProteinCodingGene(boolean isProteinCodingGene) {
        this.isProteinCodingGene = isProteinCodingGene;
    }

    /** Whoa!  Why does this always return true?  Are all FeatureSets really protein
   * coding genes? Or is it just that this method is never called, because it is
   * overridden in more specific classes? 
   * I agree - changing to return false - MG 11.21.05
   * Change, now return the value of isProteinCodingGene (this flag defaults to false) Cyril P 01.15.06
   * @see #setProteinCodingGene(boolean)
   * */
    public boolean isProteinCodingGene() {
        return isProteinCodingGene;
    }

    /** Sets the translation end to the end of the ORF from the current
   translation start OR to the end of the last exon if no stop
   codons are found in phase. if missing stop then stop is set to 0 and missing3Prime
   is set to true. */
    public void setTranslationEndFromStart() {
        if (!this.contains(translation_start) && isProteinCodingGene()) {
            logger.warn("translation start set inappropriately to " + translation_start + " for " + getName() + " (low = " + getLow() + ", high = " + getHigh() + ")");
            if (missing_5prime) {
                translation_start = 0;
                translation_end = 0;
                setMissing3prime(true);
                return;
            }
            calcTranslationStartForLongestPeptide();
        }
        translation_end = 0;
        setMissing3prime(false);
        if (!isProteinCodingGene()) return;
        String the_mRNA = get_cDNA();
        String orf = get_ORF(the_mRNA);
        int orf_offset = getFeaturePosition(translation_start) - 1;
        String aa = getUntrimmedAA(orf, orf_offset);
        boolean foundStop = false;
        if (aa != null) {
            int stop_index = aa.indexOf('*');
            if (stop_index > 0) {
                int stop_offset = (3 * stop_index) + orf_offset;
                stop_offset += edit_offset_adjust;
                int stopPos = getGenomicPosition(stop_offset + 1);
                setTranslationEnd((int) stopPos);
                foundStop = true;
            } else if (stop_index == 0) {
                logger.warn(getName() + ": the first codon is a stop codon!");
                calcTranslationStartForLongestPeptide();
                foundStop = (!isMissing3prime() && translation_start > 0);
            }
            setMissing3prime(!foundStop && translation_start != 0);
        } else {
            logger.error("failed to set translation limits");
        }
    }

    public boolean setTranslationStart(int pos) {
        return setTranslationStart(pos, false);
    }

    /** If pos is not contained in FeatureSet, set trans start fails and false 
      is returned, true on success. */
    public boolean setTranslationStart(int pos, boolean set_end) {
        SeqFeatureI held_in = (SeqFeatureI) getFeatureContaining(pos);
        setMissing5prime(held_in == null);
        if (!missing_5prime) {
            initTranslationStart(pos, set_end);
        } else {
            translation_start = 0;
            logger.warn("unable to set translation start to " + pos + " for " + getName() + ", which has " + size() + " exons:");
            for (int i = 0; i < size(); i++) {
                RangeI sf = getFeatureAt(i);
                logger.debug("\tExon " + i + ": " + sf.getStart() + "-" + sf.getEnd());
            }
        }
        return (!missing_5prime);
    }

    /** This internal method doesn't mess with the 5prime flag
      which may have already been set in setTranslationStartAtFirstCodon
  */
    private void initTranslationStart(int pos, boolean set_end) {
        translation_start = pos;
        if (!missing_5prime && getRefSequence() != null && getRefSequence().isSequenceAvailable(pos)) {
            start_codon = getRefSequence().getResidues(pos, pos + (2 * getStrand()));
        }
        if (set_end) setTranslationEndFromStart();
    }

    public void setTranslationEndNoPhase(int pos) {
        translation_end = (int) pos;
        if (this.plus1_frameshift != 0) {
            if (!withinCDS(plus1_frameshift)) {
                logger.info(getName() + " clearing +1 frameshift");
                this.plus1_frameshift = 0;
            }
        }
        if (this.minus1_frameshift != 0) {
            if (!withinCDS(minus1_frameshift)) {
                logger.info(getName() + " clearing -1 frameshift");
                this.minus1_frameshift = 0;
            }
        }
    }

    public void setTranslationEnd(int pos) {
        setTranslationEndNoPhase(pos);
        setPhases();
    }

    /** Returns start of translation in genomic coords */
    public int getTranslationStart() {
        return translation_start;
    }

    public int getTranslationEnd() {
        return translation_end;
    }

    /** Returns true if there is end of translation for the transcript,
      ie getTranslationEnd()!=0. Sometimes a valid stop codon does not exist
      for a transcript. This can happen with funny sequence data, like
      if theres a gap in the sequence where the stop is. Also in an 
      intermediate editing state the exon with the stop codon might get 
      deleted and the annotator has not added or extended an exon yet. */
    public boolean hasTranslationEnd() {
        return getTranslationEnd() != 0;
    }

    /** Returns true if transcript has a translation start (!=0) */
    public boolean hasTranslationStart() {
        return getTranslationStart() != 0;
    }

    public int getPositionFrom(int base_position, int base_offset) {
        int end_base = 0;
        if (base_position > 0) {
            int at_trans_pos = getFeaturePosition(base_position);
            end_base = getGenomicPosition(at_trans_pos + base_offset);
        }
        if (end_base <= 0) end_base = getEnd();
        return end_base;
    }

    public int getLastBaseOfStopCodon() {
        return (getPositionFrom(translation_end, 2));
    }

    public String get_cDNA() {
        if (hasKids()) return (getSplicedTranscript(0, size())); else return super.get_cDNA();
    }

    public String getSplicedTranscript(int startExon, int endExon) {
        StringBuffer dna = new StringBuffer();
        SequenceI seq = getRefSequence();
        edit_offset_adjust = 0;
        if (seq == null) {
            return "";
        } else {
            for (int i = startExon; i < endExon; i++) {
                SeqFeatureI span = getFeatureAt(i);
                String res = seq.getResidues((int) span.getStart(), (int) span.getEnd());
                if (res != null) dna.append(res);
            }
            dna = amend_RNA(dna);
            return dna.toString();
        }
    }

    public int getSplicedLength() {
        return (getSplicedLength(0, size()));
    }

    /** This needs to be fixed to account for edits to the genomic
      sequence */
    public int getSplicedLength(int startExon, int endExon) {
        int spliced_length = 0;
        for (int i = startExon; i < endExon; i++) {
            SeqFeatureI span = getFeatureAt(i);
            spliced_length += span.length();
        }
        return spliced_length;
    }

    /** will return an empty string if the translation start site has
   not been set. this is in contrast to a SeqFeature which returns
   whatever it can */
    public String get_ORF(String mRNA) {
        if (canHaveChildren()) {
            if (this.contains(translation_start)) {
                int start_offset = getFeaturePosition(translation_start) - 1;
                int end_offset = ((translation_end > 0) ? getFeaturePosition(translation_end) - 1 : -1);
                if (start_offset < 0) {
                    logger.error(getName() + " has start offset < 0");
                }
                return get_ORF(mRNA, start_offset, end_offset);
            } else {
                logger.error(getName() + " does not have translation start");
                return "";
            }
        } else {
            return super.get_ORF(mRNA);
        }
    }

    protected String get_ORF(String mRNA, int start_offset, int end_offset) {
        if (!canHaveChildren()) return super.get_ORF(mRNA);
        StringBuffer orf = new StringBuffer();
        if (mRNA != null) {
            int prior_offset = start_offset;
            SequenceEdit[] edit_list = buildORFEditList();
            SequenceEdit[] mRNA_edit_list = buildmRNAEditList();
            int edits = (edit_list != null ? edit_list.length : 0);
            for (int i = 0; i < edits; i++) {
                SequenceEdit edit = edit_list[i];
                String edit_type = edit.getEditType();
                int edit_offset = getAdjustedFeaturePosition(edit.getPosition(), mRNA_edit_list) - 1;
                if (edit_type.equals(SequenceI.DELETION)) {
                    orf.append(getSubSequence(mRNA, prior_offset, edit_offset));
                    edit_offset_adjust++;
                    prior_offset = edit_offset + 1;
                } else if (edit_type.equals(SequenceI.INSERTION)) {
                    orf.append(getSubSequence(mRNA, prior_offset, edit_offset) + edit.getResidue());
                    edit_offset_adjust--;
                    prior_offset = edit_offset;
                }
            }
            if (end_offset != -1) {
                int mRNA_edits = (mRNA_edit_list != null ? mRNA_edit_list.length : 0);
                for (int i = 0; i < mRNA_edits; i++) {
                    String type = mRNA_edit_list[i].getEditType();
                    if (type.equals(SequenceI.DELETION)) end_offset--; else if (type.equals(SequenceI.INSERTION)) end_offset++;
                }
            }
            orf.append(getSubSequence(mRNA, prior_offset, end_offset));
        } else {
            logger.warn(getName() + " has no mRNA");
        }
        return orf.toString();
    }

    /**
     Positions of edits within the ORF are dependent upon 
     positions of edits that have already taken place in
     the transcript - what a fussy bit of code */
    private int getAdjustedFeaturePosition(int genomic_pos, SequenceEdit[] edit_list) {
        int plain_pos = getFeaturePosition(genomic_pos);
        int feat_pos = plain_pos;
        if (edit_list != null) {
            for (int i = 0; i < edit_list.length; i++) {
                SequenceEdit seq_edit = edit_list[i];
                if ((seq_edit.getPosition() < genomic_pos && getStrand() >= 0) || (seq_edit.getPosition() > genomic_pos && getStrand() < 0)) {
                    if (seq_edit.getEditType().equals(SequenceI.DELETION)) {
                        feat_pos--;
                    }
                    if (seq_edit.getEditType().equals(SequenceI.INSERTION)) {
                        feat_pos++;
                    }
                }
            }
        }
        return feat_pos;
    }

    /**
   * This is an important method. (SUZ)
   * It tranforms a position that is in genomic coordinates into
   * the equivalent position in feature coordinates. In simpler,
   * more practical terms: If a there is a translation start site
   * that is located on the genomic at position N, this returns
   * where the translation start site would be on the mRNA (edited).
   * So it starts from the beginning of the transcript (base 1 to
   * the transcript and some bigger number on the genomic) and
   * returns an offset relative to that 1, not the genomic.
   *
   */
    public int getFeaturePosition(int genomic_pos) {
        int offset = 0;
        int transcript_position = 1;
        boolean stop = false;
        SeqFeatureI previous = null;
        if (!this.canHaveChildren()) return super.getFeaturePosition(genomic_pos);
        SeqFeatureI held_in = (SeqFeatureI) getFeatureContaining(genomic_pos);
        int setSize = size();
        if (held_in != null && genomic_pos != 0) {
            if (!trans_spliced) {
                if (getStrand() == 1) {
                    for (int i = 0; i < setSize && !stop; i++) {
                        SeqFeatureI span = getFeatureAt(i);
                        if (previous == null) {
                            offset = genomic_pos - span.getStart();
                        } else {
                            offset -= (span.getStart() - previous.getEnd()) - 1;
                        }
                        stop = (genomic_pos <= span.getEnd());
                        previous = span;
                    }
                } else {
                    for (int i = 0; i < setSize && !stop; i++) {
                        SeqFeatureI span = getFeatureAt(i);
                        if (previous == null) {
                            offset = span.getStart() - genomic_pos;
                        } else {
                            offset -= (previous.getEnd() - span.getStart()) - 1;
                        }
                        stop = (genomic_pos >= span.getEnd());
                        previous = span;
                    }
                }
                transcript_position = offset + 1;
            } else {
                for (int i = 0; i < setSize && !stop; i++) {
                    SeqFeatureI span = getFeatureAt(i);
                    stop = span.contains(genomic_pos);
                    if (stop) {
                        offset += Math.abs(genomic_pos - span.getStart());
                    } else {
                        offset += span.length();
                    }
                }
                transcript_position = offset + 1;
            }
        } else {
            logger.warn("featureSet.getFeaturePosition: " + getName() + " (" + getStart() + "-" + getEnd() + ")" + " has no feature containing genomic_pos " + genomic_pos);
            transcript_position = 0;
        }
        return transcript_position;
    }

    /** Converts a transcript position (1 based without introns of course)
      to a genomic position */
    public int getGenomicPosition(int transcript_pos) {
        if (!this.canHaveChildren()) return super.getGenomicPosition(transcript_pos);
        int genome_pos = -1;
        int transcript_offset = transcript_pos - 1;
        int setSize = size();
        for (int i = 0; i < setSize && genome_pos < 0; i++) {
            SeqFeatureI span = getFeatureAt(i);
            int start = span.getStart();
            int check = (span.getStrand() == 1 ? start + transcript_offset : start - transcript_offset);
            if (span.contains(check)) genome_pos = check;
            transcript_offset -= span.length();
        }
        return genome_pos;
    }

    /** For a position in peptide coordinates get the corresponding genomic position 
      For now just do this in FeatureSet, I could imagine having a peptide object */
    public int getGenomicPosForPeptidePos(int peptidePosition) {
        int featPos = peptidePosition * 3 + getFeaturePosition(getTranslationStart()) - 3;
        return getGenomicPosition(featPos);
    }

    public int getPeptidePosForGenomicPos(int genomicPosition) {
        int featPos = getFeaturePosition(genomicPosition);
        int startFeatPos = getFeaturePosition(getTranslationStart());
        int i = 2;
        return featPos / 3 - startFeatPos / 3;
    }

    protected void setPhases() {
        int setSize = size();
        if (setSize == 0) {
            return;
        }
        SeqFeatureI start_span = getFeatureContaining(getTranslationStart());
        SeqFeatureI end_span = getFeatureContaining(getTranslationEnd());
        if (start_span == null || end_span == null) {
            logger.warn("FeatureSet.setPhases: " + getName() + " can't set phases--some sites undefined " + " tss=" + getTranslationStart() + " tes=" + getTranslationEnd());
            return;
        }
        int start_index = getFeatureIndex(start_span);
        int end_index = getFeatureIndex(end_span);
        for (int i = 0; i < start_index; i++) {
            getFeatureAt(i).setPhase(0);
        }
        int phase = (int) (Math.abs(getTranslationStart() - start_span.getStart()) % 3);
        for (int i = start_index; i <= end_index; i++) {
            getFeatureAt(i).setPhase(phase);
            phase = (3 - getFeatureAt(i).getEndPhase()) % 3;
        }
        for (int i = end_index + 1; i < setSize; i++) {
            getFeatureAt(i).setPhase(0);
        }
    }

    /**
   * General implementation of Visitor pattern. (see apollo.util.Visitor).
   **/
    public void accept(apollo.util.Visitor visitor) {
        visitor.visit(this);
    }

    public boolean isTransSpliced() {
        return trans_spliced;
    }

    public void setMissing5prime(boolean partial) {
        this.missing_5prime = partial;
        if (partial) start_codon = null;
    }

    public boolean isMissing5prime() {
        return missing_5prime;
    }

    public void setMissing3prime(boolean partial) {
        this.missing_3prime = partial;
    }

    public boolean isMissing3prime() {
        return missing_3prime;
    }

    public boolean unConventionalStart() {
        return (start_codon != null && !start_codon.equals(standard_start_codon));
    }

    public String getStartAA() {
        return na2aa(start_codon);
    }

    public String getStartCodon() {
        return start_codon;
    }

    public boolean hasReadThroughStop() {
        return readthrough_stop != null;
    }

    public String readThroughStopResidue() {
        return readthrough_stop;
    }

    public int readThroughStopPosition() {
        if (readthrough_stop != null) return readthrough_pos; else return 0;
    }

    /** The generic version--set readthrough stop to true or false.
      If true, the readthrough residue is set to the genericReadThroughStopResidue. */
    public void setReadThroughStop(boolean readthrough) {
        if ((this.readthrough_stop == null && readthrough) || (this.readthrough_stop != null && !readthrough)) {
            if (!readthrough) this.readthrough_stop = null; else this.readthrough_stop = genericReadThroughStopResidue;
            setTranslationEndFromStart();
        }
    }

    public void setReadThroughStop(String residue) {
        if (residue.equals("true") || residue.equals("false")) setReadThroughStop(residue.equals("true")); else if ((this.readthrough_stop == null && residue != null) || (this.readthrough_stop != null && !(this.readthrough_stop.equals(residue)))) {
            if (!(residue.equals("U")) && !org.bdgp.util.DNAUtils.isValidOneLetterAA(residue)) {
                String warn = "transcript " + getName() + " contains invalid amino acid code '" + residue + "' for readthrough stop codon";
                logger.warn(warn);
                if (apollo.config.Config.internalMode()) JOptionPane.showMessageDialog(null, "WARNING: " + warn);
            }
            this.readthrough_stop = residue;
            setTranslationEndFromStart();
        }
    }

    /** any errors in the genomic sequence will apply to all of the
      transcripts for the gene */
    public boolean isSequencingErrorPosition(int base_position) {
        if (getRefSequence() != null) return getRefSequence().isSequencingErrorPosition(base_position); else return false;
    }

    /** any errors in the genomic sequence will apply to all of the
      transcripts for the gene */
    public SequenceEdit getSequencingErrorAtPosition(int base_position) {
        if (getRefSequence() != null) return getRefSequence().getSequencingErrorAtPosition(base_position); else return null;
    }

    /** Overrides SeqFeature.flipFlop.
      flipFlops descendants as well via recursion.
      copied from berkeley_branch(old MAIN trunk)
  */
    public void flipFlop() {
        setStrand(getStrand() * -1);
        for (int i = 0; i < size(); i++) {
            SeqFeatureI span = getFeatureAt(i);
            span.flipFlop();
        }
        sort(getStrand());
    }

    /** to get a field-by-field replica of this feature */
    public Object clone() {
        FeatureSetI clone = (FeatureSetI) super.clone();
        if (clone != null) {
            if (canHaveChildren()) {
                int setSize = size();
                ((FeatureSet) clone).clearKids();
                for (int i = 0; i < setSize; i++) {
                    SeqFeatureI span = getFeatureAt(i);
                    clone.addFeature((SeqFeatureI) span.clone());
                }
            }
        }
        return clone;
    }

    /** clean up a string that was NOT intended as a RegExp search:
      escape all special chars other than "*", replace "*" with ".*",
      and sandwich prepend "^" 
  */
    String prepareNonRegExpString(String in) {
        StringBuffer cleaned = new StringBuffer("^");
        char[] chars = in.toCharArray();
        for (int index = 0; index < chars.length; index++) {
            switch(chars[index]) {
                case '*':
                    cleaned.append('.');
                    break;
                case '[':
                case ']':
                case '(':
                case ')':
                case '|':
                case '$':
                case '^':
                case '.':
                case '?':
                case '+':
                case '\\':
                case '#':
                    cleaned.append('\\');
                    break;
            }
            cleaned.append(chars[index]);
        }
        return cleaned.toString();
    }

    /** TranslationI interface */
    public RangeI getTranslationRange() {
        return new Range(getTranslationStart(), getTranslationEnd());
    }

    /** TranslationI method - no-op overridden by Transcript. dont think FeatureSet
      needs to implement as i think only annotation Transcripts lose can become
      invalidated, but if so we can always migrate Transcripts stuff here. */
    public void setPeptideValidity(boolean validity) {
    }

    public SequenceEdit[] buildEditList() {
        SequenceEdit[] mRNAedits = buildmRNAEditList();
        SequenceEdit[] ORFedits = buildORFEditList();
        if (mRNAedits != null || ORFedits != null) {
            SequenceEdit[] edit_list;
            if (mRNAedits != null && ORFedits != null) {
                int edits = mRNAedits.length + ORFedits.length;
                int[] int_list = new int[edits];
                edit_list = new SequenceEdit[edits];
                for (int i = 0; i < mRNAedits.length; i++) {
                    SequenceEdit seq_edit = mRNAedits[i];
                    int_list[i] = seq_edit.getPosition();
                    edit_list[i] = seq_edit;
                }
                for (int i = 0; i < ORFedits.length; i++) {
                    SequenceEdit seq_edit = ORFedits[i];
                    int_list[i] = seq_edit.getPosition();
                    edit_list[i] = seq_edit;
                }
                QuickSort.sort(int_list, edit_list);
            } else if (mRNAedits != null) edit_list = mRNAedits; else edit_list = ORFedits;
            return edit_list;
        } else return null;
    }

    protected SequenceEdit[] buildORFEditList() {
        int edits = ((plus1_frameshift > 0 ? 1 : 0) + (minus1_frameshift > 0 ? 1 : 0));
        if (edits > 0) {
            int[] int_list = new int[edits];
            SequenceEdit[] edit_list = new SequenceEdit[edits];
            int count = 0;
            if (plus1_frameshift > 0) {
                int_list[count] = plus1_frameshift;
                edit_list[count] = new SequenceEdit(SequenceI.DELETION, int_list[count], null);
                count++;
            }
            if (minus1_frameshift > 0) {
                int_list[count] = minus1_frameshift;
                String base = (refSeq != null && refSeq.isSequenceAvailable(minus1_frameshift) ? refSeq.getResidues(minus1_frameshift - strand, minus1_frameshift) : null);
                edit_list[count] = new SequenceEdit(SequenceI.INSERTION, int_list[count], base.substring(0, 1));
                count++;
            }
            QuickSort.sort(int_list, edit_list);
            return edit_list;
        } else {
            return null;
        }
    }

    public int plus1FrameShiftPosition() {
        return plus1_frameshift;
    }

    public int minus1FrameShiftPosition() {
        return minus1_frameshift;
    }

    public boolean setPlus1FrameShiftPosition(int shift_pos) {
        boolean okay = true;
        if (this.plus1_frameshift != shift_pos) {
            if (shift_pos > 0) {
                okay = withinCDS(shift_pos);
            } else {
                shift_pos = 0;
            }
            if (okay) {
                this.plus1_frameshift = shift_pos;
                setTranslationEndFromStart();
            }
        }
        return okay;
    }

    public boolean setMinus1FrameShiftPosition(int shift_pos) {
        boolean okay = true;
        if (this.minus1_frameshift != shift_pos) {
            if (shift_pos > 0) {
                okay = withinCDS(shift_pos);
            } else {
                shift_pos = 0;
            }
            if (okay) {
                this.minus1_frameshift = shift_pos;
                setTranslationEndFromStart();
            }
        }
        return okay;
    }

    public boolean withinCDS(int pos) {
        boolean okay = true;
        int first_base = translation_start;
        int last_base = getLastBaseOfStopCodon();
        if (pos > 0) {
            okay = (getIndexContaining(pos) != -1);
            okay &= ((getStrand() >= 0 && pos >= first_base) || (getStrand() == -1 && pos <= first_base));
            okay &= ((last_base == 0) || (last_base != 0 && getStrand() >= 0 && pos <= last_base) || (last_base != 0 && getStrand() == -1 && pos >= last_base));
        }
        return okay;
    }

    /**
    return the current state of the bit for this flag
  */
    public boolean isFlagSet(int mask) {
        return ((flags & mask) != 0);
    }

    public void setFlag(boolean state, byte mask) {
        if (state) {
            flags |= mask;
        } else if ((flags & mask) == mask) {
            flags ^= mask;
        }
    }

    /** For debugging */
    public String toString() {
        return "[FeatureSet " + id + ": type = " + type + ", biotype = " + biotype + ", range = " + getStart() + "-" + getEnd() + ((getStrand() == -1) ? " (minus)" : "") + "; has " + getFeatures().size() + "" + " children]";
    }

    /** Return true if range has been assigned high & low */
    public boolean rangeIsUnassigned() {
        return super.rangeIsUnassigned() || (low == 1000000000 && high == -1000000000);
    }
}
