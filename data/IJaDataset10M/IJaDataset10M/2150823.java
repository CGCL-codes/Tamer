package joshua.decoder.hypergraph;

import joshua.decoder.Support;
import joshua.decoder.Symbol;
import joshua.decoder.ff.FFTransitionResult;
import joshua.decoder.ff.FeatureFunction;
import joshua.decoder.ff.tm.Rule;
import joshua.util.FileUtility;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.logging.Logger;

/**
 * this class implement:  lazy k-best extraction on a hyper-graph
 *to seed the kbest extraction, it only needs that each deduction should have the best_cost properly set, and it does not require any list being sorted
 *instead, the priority queue heap_cands will do internal sorting
 *In fact, the real crucial cost is the transition-cost at each deduction. We store the best-cost instead of the transition cost since it is easy to do pruning and
 *find one-best. Moreover, the transition cost can be recovered by get_transition_cost(), though somewhat expensive
 *
 * to recover the model cost for each individual model, we should either have access to the model, or store the model cost in the deduction 
 * (for example, in the case of disk-hypergraph, we need to store all these model cost at each deduction)
 *
 * @author Zhifei Li, <zhifei.work@gmail.com>
 * @version $LastChangedDate: 2008-11-11 10:10:08 -0500 (Tue, 11 Nov 2008) $
 */
public class KbestExtraction {

    private static final Logger logger = Logger.getLogger(KbestExtraction.class.getName());

    HashMap tbl_virtual_items = new HashMap();

    Symbol p_symbol = null;

    static String root_sym = "ROOT";

    static int root_id;

    public KbestExtraction(Symbol symbol_) {
        p_symbol = symbol_;
        root_id = p_symbol.addNonTerminalSymbol(root_sym);
    }

    public void lazy_k_best_extract_hg(HyperGraph hg, ArrayList<FeatureFunction> l_models, int global_n, boolean extract_unique_nbest, int sent_id, BufferedWriter out, boolean extract_nbest_tree, boolean add_combined_score) {
        reset_state();
        if (hg.goal_item == null) return;
        BufferedWriter out2 = FileUtility.handle_null_writer(out);
        int next_n = 0;
        while (true) {
            String hyp_str = get_kth_hyp(hg.goal_item, ++next_n, sent_id, l_models, extract_unique_nbest, extract_nbest_tree, add_combined_score);
            if (hyp_str == null || next_n > global_n) break;
            FileUtility.write_lzf(out2, hyp_str);
            FileUtility.write_lzf(out2, "\n");
        }
    }

    public void reset_state() {
        tbl_virtual_items.clear();
    }

    public String get_kth_hyp(HGNode it, int k, int sent_id, ArrayList<FeatureFunction> l_models, boolean extract_unique_nbest, boolean extract_nbest_tree, boolean add_combined_score) {
        VirtualItem virtual_item = add_virtual_item(it);
        DerivationState cur = virtual_item.lazy_k_best_extract_item(p_symbol, this, k, extract_unique_nbest, extract_nbest_tree);
        if (cur == null) return null;
        return get_kth_hyp(cur, sent_id, l_models, extract_nbest_tree, add_combined_score);
    }

    private String get_kth_hyp(DerivationState cur, int sent_id, ArrayList<FeatureFunction> l_models, boolean extract_nbest_tree, boolean add_combined_score) {
        double[] model_cost = null;
        if (l_models != null) model_cost = new double[l_models.size()];
        String str_hyp_numeric = cur.get_hyp(p_symbol, this, extract_nbest_tree, model_cost, l_models);
        String str_hyp_str = convert_hyp_2_string(sent_id, cur, l_models, str_hyp_numeric, extract_nbest_tree, add_combined_score, model_cost);
        return str_hyp_str;
    }

    private String convert_hyp_2_string(int sent_id, DerivationState cur, ArrayList<FeatureFunction> l_models, String str_hyp_numeric, boolean extract_nbest_tree, boolean add_combined_score, double[] model_cost) {
        String[] tem = str_hyp_numeric.split("\\s+");
        StringBuffer str_hyp = new StringBuffer();
        if (sent_id >= 0) {
            str_hyp.append(sent_id);
            str_hyp.append(" ||| ");
        }
        for (int t = 0; t < tem.length; t++) {
            tem[t] = tem[t].trim();
            if (extract_nbest_tree == true && (tem[t].startsWith("(") || tem[t].endsWith(")"))) {
                if (tem[t].startsWith("(") == true) {
                    String tag = this.p_symbol.getWord(new Integer(tem[t].substring(1)));
                    str_hyp.append("(");
                    str_hyp.append(tag);
                } else {
                    int first_bracket_pos = tem[t].indexOf(")");
                    String tag = this.p_symbol.getWord(new Integer(tem[t].substring(0, first_bracket_pos)));
                    str_hyp.append(tag);
                    str_hyp.append(tem[t].substring(first_bracket_pos));
                }
            } else {
                str_hyp.append(this.p_symbol.getWord(new Integer(tem[t])));
            }
            if (t < tem.length - 1) str_hyp.append(" ");
        }
        if (model_cost != null) {
            str_hyp.append(" |||");
            double tem_sum = 0.0;
            for (int k = 0; k < model_cost.length; k++) {
                str_hyp.append(String.format(" %.3f", -model_cost[k]));
                tem_sum += model_cost[k] * l_models.get(k).getWeight();
            }
            if (Math.abs(cur.cost - tem_sum) > 1e-2) {
                System.out.println("In nbest extraction, Cost does not match; cur.cost: " + cur.cost + "; temsum: " + tem_sum);
                for (int k = 0; k < model_cost.length; k++) {
                    System.out.println(model_cost[k]);
                }
                System.exit(1);
            }
        }
        if (add_combined_score == true) str_hyp.append(String.format(" ||| %.3f", -cur.cost));
        return str_hyp.toString();
    }

    private VirtualItem add_virtual_item(HGNode it) {
        VirtualItem res = (VirtualItem) tbl_virtual_items.get(it);
        if (res == null) {
            res = new VirtualItem(it);
            tbl_virtual_items.put(it, res);
        }
        return res;
    }

    private static class VirtualItem {

        public ArrayList l_nbest = new ArrayList();

        private PriorityQueue<DerivationState> heap_cands = null;

        private HashMap<String, Integer> derivation_tbl = null;

        private HashMap nbest_str_tbl = null;

        HGNode p_item = null;

        public VirtualItem(HGNode it) {
            this.p_item = it;
        }

        private DerivationState lazy_k_best_extract_item(Symbol p_symbol, KbestExtraction kbest_extator, int k, boolean extract_unique_nbest, boolean extract_nbest_tree) {
            if (l_nbest.size() >= k) {
                return (DerivationState) l_nbest.get(k - 1);
            }
            DerivationState res = null;
            if (null == heap_cands) {
                get_candidates(p_symbol, kbest_extator, extract_unique_nbest, extract_nbest_tree);
            }
            int t_added = 0;
            while (l_nbest.size() < k) {
                if (heap_cands.size() > 0) {
                    res = heap_cands.poll();
                    if (extract_unique_nbest) {
                        String res_str = res.get_hyp(p_symbol, kbest_extator, extract_nbest_tree, null, null);
                        if (!nbest_str_tbl.containsKey(res_str)) {
                            l_nbest.add(res);
                            nbest_str_tbl.put(res_str, 1);
                        }
                    } else {
                        l_nbest.add(res);
                    }
                    lazy_next(p_symbol, kbest_extator, res, extract_unique_nbest, extract_nbest_tree);
                    t_added++;
                    if (!extract_unique_nbest && t_added > 1) {
                        Support.write_log_line("In lazy_k_best_extract, add more than one time, k is " + k, Support.ERROR);
                        System.exit(1);
                    }
                } else {
                    break;
                }
            }
            if (l_nbest.size() < k) {
                res = null;
            }
            return res;
        }

        private void lazy_next(Symbol p_symbol, KbestExtraction kbest_extator, DerivationState last, boolean extract_unique_nbest, boolean extract_nbest_tree) {
            if (last.p_edge.get_ant_items() == null) return;
            for (int i = 0; i < last.p_edge.get_ant_items().size(); i++) {
                HGNode it = (HGNode) last.p_edge.get_ant_items().get(i);
                VirtualItem virtual_it = kbest_extator.add_virtual_item(it);
                int[] new_ranks = new int[last.ranks.length];
                for (int c = 0; c < new_ranks.length; c++) new_ranks[c] = last.ranks[c];
                new_ranks[i] = last.ranks[i] + 1;
                String new_sig = DerivationState.get_signature(last.p_edge, new_ranks, last.deduction_pos);
                if (derivation_tbl.containsKey(new_sig) == true) {
                    continue;
                }
                virtual_it.lazy_k_best_extract_item(p_symbol, kbest_extator, new_ranks[i], extract_unique_nbest, extract_nbest_tree);
                if (new_ranks[i] <= virtual_it.l_nbest.size()) {
                    double cost = last.cost - ((DerivationState) virtual_it.l_nbest.get(last.ranks[i] - 1)).cost + ((DerivationState) virtual_it.l_nbest.get(new_ranks[i] - 1)).cost;
                    DerivationState t = new DerivationState(last.p_edge, new_ranks, cost, last.deduction_pos);
                    heap_cands.add(t);
                    derivation_tbl.put(new_sig, 1);
                }
            }
        }

        private void get_candidates(Symbol p_symbol, KbestExtraction kbest_extator, boolean extract_unique_nbest, boolean extract_nbest_tree) {
            heap_cands = new PriorityQueue<DerivationState>();
            derivation_tbl = new HashMap<String, Integer>();
            if (extract_unique_nbest == true) nbest_str_tbl = new HashMap();
            if (null == p_item.l_deductions) {
                System.out.println("Error, l_deductions is null in get_candidates, must be wrong");
                System.exit(1);
            }
            int pos = 0;
            for (HyperEdge hyper_edge : p_item.l_deductions) {
                DerivationState t = get_best_derivation(p_symbol, kbest_extator, hyper_edge, pos, extract_unique_nbest, extract_nbest_tree);
                if (derivation_tbl.containsKey(t.get_signature()) == false) {
                    heap_cands.add(t);
                    derivation_tbl.put(t.get_signature(), 1);
                } else {
                    System.out.println("Error: get duplicate derivation in get_candidates, this should not happen");
                    System.out.println("signature is " + t.get_signature());
                    System.out.println("l_deduction size is " + p_item.l_deductions.size());
                    System.exit(1);
                }
                pos++;
            }
        }

        private DerivationState get_best_derivation(Symbol p_symbol, KbestExtraction kbest_extator, HyperEdge hyper_edge, int deduct_pos, boolean extract_unique_nbest, boolean extract_nbest_tree) {
            int[] ranks;
            double cost = 0;
            if (hyper_edge.get_ant_items() == null) {
                ranks = null;
                cost = hyper_edge.best_cost;
            } else {
                ranks = new int[hyper_edge.get_ant_items().size()];
                for (int i = 0; i < hyper_edge.get_ant_items().size(); i++) {
                    ranks[i] = 1;
                    HGNode child_it = (HGNode) hyper_edge.get_ant_items().get(i);
                    VirtualItem virtual_child_it = kbest_extator.add_virtual_item(child_it);
                    virtual_child_it.lazy_k_best_extract_item(p_symbol, kbest_extator, ranks[i], extract_unique_nbest, extract_nbest_tree);
                }
                cost = hyper_edge.best_cost;
            }
            DerivationState t = new DerivationState(hyper_edge, ranks, cost, deduct_pos);
            return t;
        }
    }

    ;

    private static class DerivationState implements Comparable<DerivationState> {

        HyperEdge p_edge;

        int deduction_pos;

        int[] ranks;

        double cost;

        public DerivationState(HyperEdge e, int[] r, double c, int pos) {
            p_edge = e;
            ranks = r;
            cost = c;
            deduction_pos = pos;
        }

        private String get_signature() {
            return get_signature(p_edge, ranks, deduction_pos);
        }

        private static String get_signature(HyperEdge p_edge2, int[] ranks2, int pos) {
            StringBuffer res = new StringBuffer();
            res.append(pos);
            if (ranks2 != null) for (int i = 0; i < ranks2.length; i++) {
                res.append(" ");
                res.append(ranks2[i]);
            }
            return res.toString();
        }

        private String get_hyp(Symbol p_symbol, KbestExtraction kbest_extator, boolean tree_format, double[] model_cost, ArrayList l_models) {
            if (model_cost != null) compute_cost(p_edge, model_cost, l_models);
            StringBuffer res = new StringBuffer();
            Rule rl = p_edge.get_rule();
            if (rl == null) {
                if (tree_format == true) {
                    res.append("(");
                    res.append(root_id);
                    res.append(" ");
                }
                for (int id = 0; id < p_edge.get_ant_items().size(); id++) {
                    HGNode child = (HGNode) p_edge.get_ant_items().get(id);
                    VirtualItem virtual_child = kbest_extator.add_virtual_item(child);
                    res.append(((DerivationState) virtual_child.l_nbest.get(ranks[id] - 1)).get_hyp(p_symbol, kbest_extator, tree_format, model_cost, l_models));
                    if (id < p_edge.get_ant_items().size() - 1) res.append(" ");
                }
                if (tree_format == true) res.append(")");
            } else {
                if (tree_format == true) {
                    res.append("(");
                    res.append(rl.lhs);
                    res.append(" ");
                }
                for (int c = 0; c < rl.english.length; c++) {
                    if (p_symbol.isNonterminal(rl.english[c]) == true) {
                        int id = p_symbol.getEngNonTerminalIndex(rl.english[c]);
                        HGNode child = (HGNode) p_edge.get_ant_items().get(id);
                        VirtualItem virtual_child = kbest_extator.add_virtual_item(child);
                        res.append(((DerivationState) virtual_child.l_nbest.get(ranks[id] - 1)).get_hyp(p_symbol, kbest_extator, tree_format, model_cost, l_models));
                    } else {
                        res.append(rl.english[c]);
                    }
                    if (c < rl.english.length - 1) res.append(" ");
                }
                if (tree_format == true) res.append(")");
            }
            return res.toString();
        }

        private void compute_cost(HyperEdge dt, double[] model_cost, ArrayList l_models) {
            if (model_cost == null) return;
            for (int k = 0; k < l_models.size(); k++) {
                FeatureFunction m = (FeatureFunction) l_models.get(k);
                double t_res = 0;
                if (dt.get_rule() != null) {
                    FFTransitionResult tem_tbl = HyperGraph.computeTransition(dt, m, -1, -1);
                    t_res = tem_tbl.getTransitionCost();
                } else {
                    t_res = HyperGraph.computeFinalTransition(dt, m);
                }
                model_cost[k] += t_res;
            }
        }

        public int compareTo(DerivationState another) throws ClassCastException {
            if (!(another instanceof DerivationState)) throw new ClassCastException("An Derivation object expected.");
            if (this.cost < ((DerivationState) another).cost) return -1; else if (this.cost == ((DerivationState) another).cost) return 0; else return 1;
        }
    }
}
