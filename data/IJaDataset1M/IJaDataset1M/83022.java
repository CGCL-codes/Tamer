package apollo.config;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.text.NumberFormat;
import apollo.datamodel.AnnotatedFeature;
import apollo.datamodel.AnnotatedFeatureI;
import apollo.datamodel.DbXref;
import apollo.datamodel.ExonI;
import apollo.datamodel.SeqFeatureI;
import apollo.datamodel.StrandedFeatureSetI;
import apollo.datamodel.Transcript;
import apollo.datamodel.SequenceI;
import apollo.editor.AddTransaction;
import apollo.editor.CompoundTransaction;
import apollo.editor.Transaction;
import apollo.editor.UpdateTransaction;
import apollo.editor.TransactionSubpart;
import apollo.util.SeqFeatureUtil;
import org.apache.log4j.*;

/** This has a lot of stuff that needs to go in FlyNameAdapter subclass.
    work in progress */
public class GmodNameAdapter extends DefaultNameAdapter {

    protected static final Logger logger = LogManager.getLogger(GmodNameAdapter.class);

    protected static int annotNumber = 1;

    /** generates a name for a given feature - this only works for
      genes, transcripts and exons */
    public String generateName(StrandedFeatureSetI annots, String curation_name, SeqFeatureI feature) {
        if (feature.isTranscript()) {
            return generateTranscriptName(feature);
        } else if (feature.isExon()) {
            Transcript transcript = ((ExonI) feature).getTranscript();
            if (transcript.isProteinCodingGene()) return (transcript.getName() + " exon " + (transcript.getFeatureIndex(feature) + 1)); else return (transcript.getName());
        } else if (feature.hasAnnotatedFeature()) {
            return generateId(annots, curation_name, feature);
        } else return "???";
    }

    /** Produces next temp id - suffix not used (used in fly subclass) make 
   separate method without suffix and curation_name? */
    private String generateAnnotTempId(StrandedFeatureSetI annots, String prefix) {
        if (annots == null) {
            logger.warn("GmodNameAdapter.genAnnTempId: annots is null!?");
            return "";
        }
        prefix = prefix + getTempSuffix();
        int num = nextAnnotNumber(annots);
        return prefix + num;
    }

    /** generates both temp names and split names. a split annot can have 3 kinds of
      names: temp, id, & real-user-added name. temps, and ids just get temp ids. if its
      a real-user-added name that means its being split and it needs split numbers.
      old split numbers are looked for and added to if there. a split number is a :#
      at the end of a real user name. looks at other annots with same name(prefix) and 
      if they have split numbers uses next highest. curationName not used here
      (for fly subclass) */
    public String generateAnnotSplitName(SeqFeatureI annot, StrandedFeatureSetI annotParent, String curationName) {
        if (isTemp(annot) || isAnnotId(annot)) return generateAnnotTempId(annotParent, getIDPrefix(annot));
        String nameWithoutNumber = annot.getName();
        int index = nameWithoutNumber.lastIndexOf(":");
        if (index > 0 && (index + 1) < nameWithoutNumber.length()) {
            nameWithoutNumber = nameWithoutNumber.substring(0, index);
        }
        int nextSplitNumber = 1;
        for (int i = 0; i < annotParent.size(); i++) {
            SeqFeatureI annotSibling = annotParent.getFeatureAt(i);
            String this_name = annotSibling.getName();
            if (this_name.startsWith(nameWithoutNumber)) {
                try {
                    int splitNumber = getSplitNumber(this_name);
                    if (splitNumber >= nextSplitNumber) nextSplitNumber = splitNumber + 1;
                } catch (NumberFormatException e) {
                }
            }
        }
        return nameWithoutNumber + ":" + nextSplitNumber;
    }

    /** Parses out :# at the end of splitName (eg Hph:3). If no :# or # is not a number
      throws NumberFormatException */
    private int getSplitNumber(String splitName) throws NumberFormatException {
        int splitNumber = 0;
        int index = splitName.indexOf(":");
        if (index == -1) throw new NumberFormatException("No split number present");
        String num_str = splitName.substring(index + 1);
        int namenum = Integer.parseInt(num_str);
        return namenum;
    }

    /** Construct a name for a new transcript */
    private String generateTranscriptName(SeqFeatureI transcript) {
        return generateTranscriptNameOrId(transcript, transcript.getRefFeature().getName());
    }

    protected String generateTranscriptId(SeqFeatureI trans) {
        return generateTranscriptNameOrId(trans, trans.getRefFeature().getId());
    }

    /** Returns true if this annot type needs a suffix (e.g. -RA).
      Currently this decision is made by whether it is a first-tier annotation.
      Test should probably be whether this is a single-level annot (which can
      now be specified in tiers file). */
    public boolean needsSuffix(String type) {
        return Config.getPropertyScheme().getTierProperty(type).isFirstTier();
    }

    /** generates suffix and appends it to annotNameOrId 
      could also do this with a boolean instead of the String(?) */
    private String generateTranscriptNameOrId(SeqFeatureI transcript, String annotNameOrId) {
        SeqFeatureI annot = transcript.getRefFeature();
        Vector transcripts = annot.getFeatures();
        int t_index = transcripts.indexOf(transcript);
        if (t_index < 0) t_index = transcripts.size();
        if (!needsSuffix(annot.getFeatureType())) return annotNameOrId;
        boolean addTemp = !isTemp(annotNameOrId);
        String suffix = generateTranscriptSuffix(t_index, transcripts, addTemp);
        String newNameOrId = annotNameOrId + suffix;
        return newNameOrId;
    }

    /** Return -transcript#:temp, where # is transcript number. If suffix already being
      used, uses the first unused #. :temp is added to trip chado trigger to create
      new transcript name - which prevents naming collisions. */
    protected String generateTranscriptSuffix(int t_index, Vector transcripts, boolean addTemp) {
        ++t_index;
        String suffix = getTranscriptSuffixRoot() + t_index;
        if (suffixInUse(transcripts, suffix, t_index - 1)) {
            ++t_index;
            suffix = generateTranscriptSuffix(t_index, transcripts, false);
        }
        if (addTemp) suffix += ":temp";
        if (suffixInUse(transcripts, suffix, t_index - 1)) {
            return (generateTranscriptSuffix(t_index + 1, transcripts, addTemp));
        }
        return suffix;
    }

    /** generates a ID for a given feature. for annots calls generateAnnotTempId
      which produces a temp id */
    public String generateId(StrandedFeatureSetI annots, String curation_name, SeqFeatureI feature) {
        if (feature.isTranscript()) {
            return generateTranscriptId(feature);
        } else if (feature.hasAnnotatedFeature()) {
            return generateAnnotTempId(annots, getIDPrefix(feature));
        } else return "???";
    }

    /** Generate the next number for use in temporary annotation id 
   in DefaultNameAdapter theres also a nextAnnotNumber but it takes a sfs and a Class -
   this seems funny */
    private int nextAnnotNumber(StrandedFeatureSetI annots) {
        int num = annotNumber;
        for (int i = 0; i < annots.size(); i++) {
            SeqFeatureI g = annots.getFeatureAt(i);
            String this_id = g.getId();
            num = skipUsedTempNum(this_id, num);
        }
        SequenceI seq = annots.getRefSequence();
        if (seq != null) {
            HashMap errors = seq.getGenomicErrors();
            if (errors != null) {
                for (Object o : errors.values()) {
                    SeqFeatureI error = (SeqFeatureI) o;
                    num = skipUsedTempNum(error.getId(), num);
                }
            }
        }
        for (int i = 0; i < getTransactionManager().size(); i++) {
            Transaction trans = getTransactionManager().getTransaction(i);
            String id = trans.getProperty("id", Transaction.OLD);
            num = skipUsedTempNum(id, num);
            id = trans.getProperty("id", Transaction.NEW);
            num = skipUsedTempNum(id, num);
            id = trans.getProperty("annotation_id", Transaction.OLD);
            num = skipUsedTempNum(id, num);
        }
        annotNumber = num;
        return annotNumber;
    }

    public boolean nameIsId(SeqFeatureI feature) {
        String prefix;
        boolean is_ID = false;
        if (feature instanceof ExonI) {
            prefix = "EX";
            is_ID = (getCG(feature.getName(), prefix) != null || feature.getName().startsWith(prefix + ":temp") || (feature.getName().startsWith(prefix) && feature.getName().indexOf("tmp") > 0));
            prefix = "CG";
        } else if (feature instanceof Transcript) {
            prefix = "CT";
            is_ID = (getCG(feature.getName(), prefix) != null || feature.getName().startsWith(prefix + ":temp") || (feature.getName().startsWith(prefix) && feature.getName().indexOf("tmp") > 0));
            prefix = "CG";
        } else if (feature instanceof AnnotatedFeature) {
            prefix = "CG";
            is_ID = (getCG(feature.getName(), prefix) != null || feature.getName().startsWith(prefix + ":temp") || (feature.getName().startsWith(prefix) && feature.getName().indexOf("tmp") > 0));
            prefix = "CR";
        } else prefix = "???";
        is_ID |= (getCG(feature.getName(), prefix) != null || feature.getName().startsWith(prefix + ":temp") || (feature.getName().startsWith(prefix) && feature.getName().indexOf("tmp") > 0));
        return is_ID;
    }

    protected boolean isAnnotId(SeqFeatureI annot) {
        return annot.getName().startsWith(getIDPrefix(annot));
    }

    private boolean isNoName(String name) {
        return name.equals(SeqFeatureI.NO_NAME);
    }

    /** This is for top level annots (eg gene). not underling annots (transcript)
      sets gene name, adds old gene name as synonym, sets transcript names. */
    public CompoundTransaction setAnnotName(AnnotatedFeatureI annot, String newName) {
        CompoundTransaction ct = new CompoundTransaction(this);
        String oldName = annot.getName();
        ct.addTransaction(setName(annot, newName));
        ct.addTransaction(addSynonym(annot, oldName));
        ct.addTransaction(setAllTranscriptNamesFromAnnot(annot));
        return ct;
    }

    private CompoundTransaction setAllTranscriptNamesFromAnnot(AnnotatedFeatureI annot) {
        CompoundTransaction compTrans = new CompoundTransaction(this);
        for (int i = 0; i < annot.size(); i++) {
            AnnotatedFeatureI trans = annot.getFeatureAt(i).getAnnotatedFeature();
            CompoundTransaction t = setTranscriptNameFromAnnot(trans, annot);
            compTrans.addTransaction(t);
        }
        return compTrans;
    }

    public CompoundTransaction setTranscriptNameFromAnnot(AnnotatedFeatureI trans, AnnotatedFeatureI ann) {
        String transcriptSuffix = getTranscriptSuffix(trans, ann);
        String transcript_name = ann.getName() + transcriptSuffix;
        return setTranscriptName(trans, transcript_name);
    }

    public CompoundTransaction setTranscriptName(AnnotatedFeatureI trans, String name) {
        CompoundTransaction compTrans = new CompoundTransaction(this);
        if (trans.getName() != null && trans.getName().equals(name)) return null;
        String oldName = trans.getName();
        UpdateTransaction ut = super.setName(trans, name);
        compTrans.addTransaction(ut);
        if (trans.getRefFeature().isProteinCodingGene()) {
            CompoundTransaction seqTrans = setSeqNamesFromTranscript(trans);
            compTrans.addTransaction(seqTrans);
        }
        AddTransaction at = super.addSynonym(trans, oldName);
        compTrans.addTransaction(at);
        return compTrans;
    }

    public CompoundTransaction setTranscriptId(SeqFeatureI trans, String id) {
        CompoundTransaction compTrans = super.setTranscriptId(trans, id);
        if (trans.getRefFeature().isProteinCodingGene()) {
            compTrans.addTransaction(setPeptideIdFromTranscript(trans));
        }
        compTrans.setSource(this);
        return compTrans;
    }

    /** chado generates peptide ids not apollo. apollo either keeps the existing
      accession, and if null just creates a temp pep id (just uses name) */
    protected UpdateTransaction setPeptideIdFromTranscript(SeqFeatureI transcript) {
        if (transcript.getPeptideSequence() != null && transcript.getPeptideSequence().getAccessionNo() != null && !isTemp(transcript.getId())) return null;
        String tempPepId = generatePeptideNameFromTranscriptName(transcript.getId());
        if (tempPepId == null) tempPepId = transcript.getName() + getPeptideSuffixRoot() + getTempSuffix();
        if (!isTemp(tempPepId)) tempPepId += getTempSuffix();
        String oldPepId = null;
        TransactionSubpart ts = TransactionSubpart.PEPTIDE_ID;
        UpdateTransaction ut = new UpdateTransaction(transcript, ts, oldPepId, tempPepId);
        ut.editModel();
        return ut;
    }

    protected String getTranscriptSuffix(SeqFeatureI trans, AnnotatedFeatureI annot) {
        String transcriptName = trans.getName();
        int index = (transcriptName != null ? transcriptName.lastIndexOf("-") : -1);
        if (index > 0) return transcriptName.substring(index); else if (annot != null) {
            boolean addTemp = !isTemp(annot.getName());
            return generateTranscriptSuffix(annot.getFeatureIndex(trans), annot.getFeatures(), addTemp);
        } else {
            logger.error("FlyNameAdap.getTransSuffix trans " + trans + " has no suffix");
            return "";
        }
    }

    /** Rice/gmod does peptide names (but not cdna names). apollo never displays seq
      names and rice triggers automatically change prot names on transcript name change
      This is actually needed to 
      give peptides a unique id for putting together chado transactions, otherwise it
      defaults to transcript name and things get messed up. 
      This is called by setTranscriptNameFromAnnot(). used to be called
      by FED as well - no longer.*/
    protected CompoundTransaction setSeqNamesFromTranscript(AnnotatedFeatureI trans) {
        CompoundTransaction ct = new CompoundTransaction(this);
        if (trans.hasPeptideSequence()) {
            String pepName = generatePeptideNameFromTranscriptName(trans.getName());
            TransactionSubpart ts = TransactionSubpart.PEPTIDE_NAME;
            UpdateTransaction t = new UpdateTransaction(trans, ts);
            t.setOldSubpartValue(trans.getPeptideSequence().getName());
            t.setNewSubpartValue(pepName);
            t.editModel();
            ct.addTransaction(t);
        }
        return ct;
    }

    /** Generates peptide name from existing transcript name. 
      Replace transcript suffix with peptide suffix */
    public String generatePeptideNameFromTranscriptName(String transcriptName) {
        String pepName = transcriptName;
        int index = transcriptName.indexOf(getTranscriptSuffixRoot());
        if (index == -1) return null;
        String ordinal = transcriptName.substring(index + getTranscriptSuffixRoot().length());
        pepName = transcriptName.substring(0, index) + getPeptideSuffixRoot() + ordinal;
        return pepName;
    }

    /** Generates peptide id from existing transcript id. 
      Replace transcript suffix with peptide suffix */
    public String generatePeptideIdFromTranscriptId(String transcriptId) {
        return generatePeptideNameFromTranscriptName(transcriptId);
    }

    private static final String transcriptSuffixRoot = "-transcript";

    /** This needs some explaining. This is the root of the suffix. Its the part of the
      transcript suffix that doesnt change. so for rice/gmod its "-transcript", 
      for fly its "-R" */
    protected String getTranscriptSuffixRoot() {
        return transcriptSuffixRoot;
    }

    private static final String peptideSuffixRoot = "-protein";

    /** The root of the suffix is the non changing part of the suffix - the suffix\
      minus the ordinal. for gmod/rice its "-protein" */
    protected String getPeptideSuffixRoot() {
        return peptideSuffixRoot;
    }

    /** This is used as a check if transcript suffix editing is allowed in the annot
      info editor. gmod/rice disallows this by default so this is actually not
      relevant yet for rice. (fly allows this so its important there). 
      move to fly? */
    public String getTranscriptNamePattern() {
        String pat = ".*" + getTranscriptSuffixRoot() + getTranscriptOrdinalPattern();
        if (transcriptCanHaveTempSuffix()) pat += "(" + getTempSuffix() + ")*";
        return pat;
    }

    protected String getTranscriptOrdinalPattern() {
        if (transcriptOrdinalIsNumeric()) return "\\d+"; else return "[A-Z]+";
    }

    /** Return true if transcripts do numbers in suffix, false if do letter [A-Z]
      in suffix as ordinal. Gmod/rice is numeric(true), fly is alpha(false) */
    protected boolean transcriptOrdinalIsNumeric() {
        return true;
    }

    /** Returns true. gmod temps transcript suffixes (by default). the :temp indicates
      that the db needs to create a new transcript name */
    protected boolean transcriptCanHaveTempSuffix() {
        return true;
    }

    private String getTempSuffix() {
        return ":temp";
    }

    /** Returns true if changing type from oldType to newType will cause a change
      in feature ID, i.e. the ID prefix will change to reflect the new type. 
      This code should probably be moved to default name adapter */
    public boolean typeChangeCausesIdChange(String oldType, String newType) {
        return !getIDPrefix(oldType).equals(getIDPrefix(newType));
    }

    /** convert oldId to a new id in new type format */
    public String getNewIdFromTypeChange(String oldId, String oldType, String newType) {
        String oldPrefix = getIDPrefix(oldType);
        String oldSuffix = oldId.substring(oldPrefix.length());
        String newPrefix = getIDPrefix(newType);
        return newPrefix + oldSuffix;
    }

    /** Special version for fly--knows proper format for transcript names */
    protected boolean match(SeqFeatureI sf, String matchString) {
        if (sf.getFeatureType().equals("transcript")) return Pattern.matches(getTranscriptNamePattern(), matchString); else if (sf.getId().indexOf("temp") >= 0 || sf.getId().indexOf("tmp") >= 0) {
            FeatureProperty fp = Config.getPropertyScheme().getFeatureProperty(sf.getTopLevelType());
            if (fp == null || (fp != null && fp.getIdFormat() == null)) return true; else return Pattern.matches(getPrefix(fp.getIdFormat()), getPrefix(matchString));
        } else return super.match(sf, matchString);
    }

    /** funny method - if name starts with prefix it returns everything up to the 
   * first colon - if there is a colon right after the prefix (CG:) it returns null -
   * this is true of fly temp ids (CG:temp1:...) this is used by name IsId which is fly
   * specific
   */
    private String getCG(String name, String prefix) {
        String cg_name = null;
        String tmp_name = name;
        if (tmp_name.startsWith(prefix)) {
            int index = tmp_name.indexOf(":");
            if (index > 0) tmp_name = tmp_name.substring(0, index);
            if (!tmp_name.equals(prefix)) {
                cg_name = tmp_name;
            }
        }
        return cg_name;
    }

    /** A simpler signature for this might be (AnnotatedFeatureI gene, String suffix)
      I dont think t_index is necasary as the suffix in question should be
      new - even different from the transcript it came from if the trans has
      a name yet - but maybe im missing something */
    public boolean suffixInUse(Vector transcripts, String suffix, int t_index) {
        boolean used = false;
        for (int i = 0; i < transcripts.size() && !used; i++) {
            if (i != t_index) {
                Transcript t = (Transcript) transcripts.elementAt(i);
                used = t.getName().endsWith(suffix);
            }
        }
        return used;
    }

    /** Returns the prefix of a SeqFeature's ID format (e.g. "CG" for genes) */
    protected String getIDPrefix(SeqFeatureI sf) {
        return getIDPrefix(sf.getFeatureType());
    }

    private String getIDPrefix(String type) {
        FeatureProperty fp = Config.getPropertyScheme().getFeatureProperty(type);
        if (fp == null || (fp != null && fp.getIdFormat() == null)) return getDefaultIDPrefix();
        String idFormat = fp.getIdFormat();
        return getPrefix(idFormat);
    }

    /** The prefix to use if idFormat is not specified in tiers file for a type */
    protected String getDefaultIDPrefix() {
        return "GMOD";
    }

    /** Given a format string (e.g. RICE\d) returns the prefix (e.g. RICE)
      This allows for colons ater prefix, fly doesnt. */
    protected String getPrefix(String idFormat) {
        Pattern p = Pattern.compile("[a-zA-Z_]+:*");
        Matcher m = p.matcher(idFormat);
        boolean patternFound = m.find();
        if (!patternFound) return idFormat;
        return idFormat.substring(0, m.end());
    }

    public String getNewChadoDbUniquename(String featType, long pk) {
        int minimumIntegerDigits = 8;
        String delim = ":";
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(minimumIntegerDigits);
        return getIDPrefix(featType) + delim + nf.format(pk);
    }

    public String getNewChadoDbUniquename(SeqFeatureI feat, long pk) {
        return getNewChadoDbUniquename(feat.getFeatureType(), pk);
    }
}
