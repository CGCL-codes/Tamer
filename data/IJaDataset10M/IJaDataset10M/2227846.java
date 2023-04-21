package org.jcvi.vics.model.tasks.tools;

import org.jcvi.vics.model.tasks.Task;
import org.jcvi.vics.model.vo.ParameterException;
import org.jcvi.vics.model.vo.ParameterVO;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Mar 30, 2010
 * Time: 12:16:04 PM
 */
public class Clustalw2Task extends Task {

    public static final transient String PARAM_align = "align";

    public static final transient String PARAM_tree = "tree";

    public static final transient String PARAM_pim = "pim";

    public static final transient String PARAM_bootstrap = "bootstrap";

    public static final transient String PARAM_convert = "convert";

    public static final transient String PARAM_quicktree = "quicktree";

    public static final transient String PARAM_type = "type";

    public static final transient String PARAM_negative = "negative";

    public static final transient String PARAM_output = "output";

    public static final transient String PARAM_outorder = "outorder";

    public static final transient String PARAM_clustal_case = "clustal case";

    public static final transient String PARAM_seqnos = "seqnos";

    public static final transient String PARAM_seqno_range = "seqno_range";

    public static final transient String PARAM_range = "range";

    public static final transient String PARAM_maxseqlen = "maxseqlen";

    public static final transient String PARAM_quiet = "quiet";

    public static final transient String PARAM_stats = "stats";

    public static final transient String PARAM_ktuple = "ktuple";

    public static final transient String PARAM_topdiags = "topdiags";

    public static final transient String PARAM_window = "window";

    public static final transient String PARAM_pairgap = "pairgap";

    public static final transient String PARAM_score = "score";

    public static final transient String PARAM_pwmatrix = "pwmatrix";

    public static final transient String PARAM_pwdnamatrix = "pwdnamatrix";

    public static final transient String PARAM_pwgapopen = "pwgapopen";

    public static final transient String PARAM_pwgapext = "pwgapext";

    public static final transient String PARAM_newtree = "newtree";

    public static final transient String PARAM_usetree = "usetree";

    public static final transient String PARAM_matrix = "matrix";

    public static final transient String PARAM_dnamatrix = "dnamatrix";

    public static final transient String PARAM_gapopen = "gapopen";

    public static final transient String PARAM_gapext = "gapext";

    public static final transient String PARAM_endgaps = "endgaps";

    public static final transient String PARAM_gapdist = "gapdist";

    public static final transient String PARAM_nopgap = "nopgap";

    public static final transient String PARAM_nohgap = "nohgap";

    public static final transient String PARAM_hgapresidues = "hgapresidues";

    public static final transient String PARAM_maxdiv = "maxdiv";

    public static final transient String PARAM_transweight = "transweight";

    public static final transient String PARAM_iteration = "iteration";

    public static final transient String PARAM_numiter = "numiter";

    public static final transient String PARAM_noweights = "noweights";

    public static final transient String PARAM_profile = "profile";

    public static final transient String PARAM_newtree1 = "newtree1";

    public static final transient String PARAM_usetree1 = "usetree1";

    public static final transient String PARAM_newtree2 = "newtree2";

    public static final transient String PARAM_usetree2 = "usetree2";

    public static final transient String PARAM_sequences = "sequence";

    public static final transient String PARAM_nosecstr1 = "nosecstr1";

    public static final transient String PARAM_nosecstr2 = "nosecstr2";

    public static final transient String PARAM_secstrout = "secstrout";

    public static final transient String PARAM_helixgap = "helixgap";

    public static final transient String PARAM_strandgap = "strandgap";

    public static final transient String PARAM_loopgap = "loopgap";

    public static final transient String PARAM_terminalgap = "terminalgap";

    public static final transient String PARAM_helixendin = "helixendin";

    public static final transient String PARAM_helixendout = "helixendout";

    public static final transient String PARAM_strandendin = "strandendin";

    public static final transient String PARAM_strandendout = "strandendout";

    public static final transient String PARAM_outputtree = "outputtree";

    public static final transient String PARAM_seed = "seed";

    public static final transient String PARAM_kimura = "kimura";

    public static final transient String PARAM_tossgaps = "tossgaps";

    public static final transient String PARAM_bootlabels = "bootlabels";

    public static final transient String PARAM_clustering = "clustering";

    public static final transient String PARAM_batch = "batch";

    public static final transient String PARAM_fasta_input_node_id = "input node id";

    public static final transient String PARAM_fasta_input_file_list = "input file list";

    public Clustalw2Task() {
        super();
        setTaskName("Clustalw2Task");
        setParameter(PARAM_fasta_input_node_id, "");
        setParameter(PARAM_fasta_input_file_list, "");
    }

    public String getDisplayName() {
        return "Clustalw2Task";
    }

    public ParameterVO getParameterVO(String key) throws ParameterException {
        if (key == null) return null;
        String value = getParameter(key);
        if (value == null) return null;
        return null;
    }

    public String generateCommandOptions() throws ParameterException {
        StringBuffer sb = new StringBuffer();
        addCommandParameterFlag(sb, "-align", PARAM_align);
        addCommandParameterFlag(sb, "-tree", PARAM_tree);
        addCommandParameterFlag(sb, "-bootstrap", PARAM_bootstrap);
        addCommandParameterFlag(sb, "-convert", PARAM_convert);
        addCommandParameterFlag(sb, "-quicktree", PARAM_quicktree);
        addCommandParameterEquals(sb, "-type", PARAM_type);
        addCommandParameterFlag(sb, "-negative", PARAM_negative);
        addCommandParameterEquals(sb, "-output", PARAM_output);
        addCommandParameterEquals(sb, "-outorder", PARAM_outorder);
        addCommandParameterEquals(sb, "-case", PARAM_clustal_case);
        addCommandParameterEquals(sb, "-seqnos", PARAM_seqnos);
        addCommandParameterEquals(sb, "-seqno_range", PARAM_seqno_range);
        addCommandParameterEquals(sb, "-range", PARAM_range);
        addCommandParameterEquals(sb, "-maxseqlen", PARAM_maxseqlen);
        addCommandParameterFlag(sb, "-quiet", PARAM_quiet);
        addCommandParameterEquals(sb, "-stats", PARAM_stats);
        addCommandParameterEquals(sb, "-ktuple", PARAM_ktuple);
        addCommandParameterEquals(sb, "-topdiags", PARAM_topdiags);
        addCommandParameterEquals(sb, "-window", PARAM_window);
        addCommandParameterEquals(sb, "-pairgap", PARAM_pairgap);
        addCommandParameterEquals(sb, "-score", PARAM_score);
        addCommandParameterEquals(sb, "-pwmatrix", PARAM_pwmatrix);
        addCommandParameterEquals(sb, "-pwdnamatrix", PARAM_pwdnamatrix);
        addCommandParameterEquals(sb, "-pwgapopen", PARAM_pwgapopen);
        addCommandParameterEquals(sb, "-pwgapext", PARAM_pwgapext);
        addCommandParameterEquals(sb, "-newtree", PARAM_newtree);
        addCommandParameterEquals(sb, "-usetree", PARAM_usetree);
        addCommandParameterEquals(sb, "-matrix", PARAM_matrix);
        addCommandParameterEquals(sb, "-dnamatrix", PARAM_dnamatrix);
        addCommandParameterEquals(sb, "-gapopen", PARAM_gapopen);
        addCommandParameterEquals(sb, "-gapext", PARAM_gapext);
        addCommandParameterFlag(sb, "-endgaps", PARAM_endgaps);
        addCommandParameterEquals(sb, "-gapdist", PARAM_gapdist);
        addCommandParameterFlag(sb, "-nopgap", PARAM_nopgap);
        addCommandParameterFlag(sb, "-nohpgap", PARAM_nohgap);
        addCommandParameterEquals(sb, "-hgapresidues", PARAM_hgapresidues);
        addCommandParameterEquals(sb, "-maxdiv", PARAM_maxdiv);
        addCommandParameterEquals(sb, "-transweight", PARAM_transweight);
        addCommandParameterEquals(sb, "-iteration", PARAM_iteration);
        addCommandParameterEquals(sb, "-numiter", PARAM_numiter);
        addCommandParameterFlag(sb, "-noweights", PARAM_noweights);
        addCommandParameterFlag(sb, "-profile", PARAM_profile);
        addCommandParameterEquals(sb, "-newtree1", PARAM_newtree1);
        addCommandParameterEquals(sb, "-usetree1", PARAM_usetree1);
        addCommandParameterEquals(sb, "-newtree2", PARAM_newtree2);
        addCommandParameterEquals(sb, "-usetree", PARAM_usetree2);
        addCommandParameterFlag(sb, "-sequences", PARAM_sequences);
        addCommandParameterFlag(sb, "-nosecstr1", PARAM_nosecstr1);
        addCommandParameterFlag(sb, "-nosecstr2", PARAM_nosecstr2);
        addCommandParameterEquals(sb, "-secstrout", PARAM_secstrout);
        addCommandParameterEquals(sb, "-helixgap", PARAM_helixgap);
        addCommandParameterEquals(sb, "-strandga", PARAM_strandgap);
        addCommandParameterEquals(sb, "-loopgap", PARAM_loopgap);
        addCommandParameterEquals(sb, "-terminalgap", PARAM_terminalgap);
        addCommandParameterEquals(sb, "-helixendin", PARAM_helixendin);
        addCommandParameterEquals(sb, "-helixendout", PARAM_helixendout);
        addCommandParameterEquals(sb, "-strandendin", PARAM_strandendin);
        addCommandParameterEquals(sb, "-strandendout", PARAM_strandendout);
        addCommandParameterEquals(sb, "-outputtree", PARAM_outputtree);
        addCommandParameterEquals(sb, "-seed", PARAM_seed);
        addCommandParameterFlag(sb, "-kimura", PARAM_kimura);
        addCommandParameterFlag(sb, "-tossgaps", PARAM_tossgaps);
        addCommandParameterEquals(sb, "-bootlabels", PARAM_bootlabels);
        addCommandParameterEquals(sb, "-clustering", PARAM_clustering);
        addCommandParameterFlag(sb, "-batch", PARAM_batch);
        return sb.toString();
    }
}
