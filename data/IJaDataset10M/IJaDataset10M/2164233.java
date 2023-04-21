package net.sourceforge.pmd.jedit;

import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.tree.*;
import net.sourceforge.pmd.Language;
import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleSet;
import net.sourceforge.pmd.RuleSetFactory;
import net.sourceforge.pmd.RuleSetNotFoundException;
import net.sourceforge.pmd.RuleSets;
import net.sourceforge.pmd.jedit.checkboxtree.*;
import org.gjt.sp.jedit.jEdit;

/**
 *  Description of the Class
 *
 * @author     jiger.p
 * @created    April 22, 2003
 */
public class SelectedRules {

    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();

    private DefaultMutableTreeNode jspRoot = new DefaultMutableTreeNode();

    private TreeModel treeModel = null;

    private TreeModel jspTreeModel = null;

    private TreeCheckingModel checkingModel = null;

    private TreeCheckingModel jspCheckingModel = null;

    private List<RuleSet> pmdRulesets = null;

    private List<RuleSet> jspRulesets = null;

    private RuleSets selectedRules = null;

    private RuleSets jspSelectedRules = null;

    /**
     * Loads PMD standard rulesets and any user defined custom rulesets, creates
     * a checkbox tree model and root tree node for the rules.
     *
     * @exception  RuleSetNotFoundException  Only thrown when loading PMD standard
     * rulesets.  Any exception found while attempting to load a custom ruleset is
     * caught and a message is displayed to the user.
     */
    public SelectedRules() throws RuleSetNotFoundException {
        loadRuleSets();
        loadTree();
        loadJspTree();
    }

    public void loadRuleSets() {
        List<RuleSet> rulesets = new ArrayList<RuleSet>();
        if (pmdRulesets == null) {
            pmdRulesets = loadPMDRuleSets();
        }
        for (RuleSet ruleset : pmdRulesets) {
            rulesets.add(ruleset);
        }
        if (jspRulesets == null) {
            jspRulesets = loadJspRuleSets();
        }
        List<RuleSet> custom_rulesets = loadCustomRuleSets();
        for (RuleSet ruleset : custom_rulesets) {
            rulesets.add(ruleset);
        }
        Collections.sort(rulesets, rulesetSorter);
        for (RuleSet rs : rulesets) {
            addRuleSet2Rules(rs, root);
        }
        Collections.sort(jspRulesets, rulesetSorter);
        for (RuleSet rs : jspRulesets) {
            addRuleSet2Rules(rs, jspRoot);
        }
    }

    private List<RuleSet> loadPMDRuleSets() {
        List<RuleSet> rulesets = new ArrayList<RuleSet>();
        try {
            RuleSetFactory rsf = new RuleSetFactory();
            for (Iterator<RuleSet> i = rsf.getRegisteredRuleSets(); i.hasNext(); ) {
                RuleSet rs = i.next();
                rulesets.add(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulesets;
    }

    private List<RuleSet> loadJspRuleSets() {
        List<RuleSet> rulesets = new ArrayList<RuleSet>();
        try {
            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("rulesets/jsprulesets.properties"));
            String filename_list = props.getProperty("rulesets.filenames");
            RuleSetFactory rsf = new RuleSetFactory();
            if (filename_list != null) {
                String[] filenames = filename_list.split(",");
                for (String filename : filenames) {
                    RuleSet rs = rsf.createRuleSet(getClass().getClassLoader().getResourceAsStream(filename));
                    if (rs != null) {
                        rulesets.add(rs);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rulesets;
    }

    private List<RuleSet> loadCustomRuleSets() {
        List<RuleSet> rulesets = new ArrayList<RuleSet>();
        try {
            String customRuleSetPath = jEdit.getProperty("pmd.customRulesPath");
            if (!(customRuleSetPath == null)) {
                RuleSetFactory rsf = new RuleSetFactory();
                RuleSets ruleSets = rsf.createRuleSets(customRuleSetPath);
                if (ruleSets.getAllRuleSets() != null) {
                    for (RuleSet rs : ruleSets.getAllRuleSets()) {
                        rulesets.add(rs);
                    }
                }
            }
        } catch (RuleSetNotFoundException e) {
            JOptionPane.showMessageDialog(null, jEdit.getProperty("net.sf.pmd.There_was_an_error_loading_one_or_more_custom_rulesets,_so_no_custom_rulesets_were_loaded", "There was an error loading one or more custom rulesets, so no custom rulesets were loaded"), jEdit.getProperty("net.sf.pmd.Error_Loading_Custom_Ruleset", "Error Loading Custom Ruleset"), JOptionPane.ERROR_MESSAGE);
        }
        return rulesets;
    }

    protected void loadGoodRulesTree() {
        Properties goodRules = new Properties();
        try {
            goodRules.load(getClass().getClassLoader().getResourceAsStream("default_rules.props"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        treeModel = new DefaultTreeModel(root);
        checkingModel = new DefaultTreeCheckingModel(treeModel);
        selectedRules = new RuleSets();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode ruleSetNode = (DefaultMutableTreeNode) root.getChildAt(i);
            RuleSet ruleset = new RuleSet();
            boolean hadCheckedRule = false;
            for (int j = 0; j < ruleSetNode.getChildCount(); j++) {
                DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) ruleSetNode.getChildAt(j);
                TreePath path = new TreePath(ruleNode.getPath());
                RuleNode rn = (RuleNode) ruleNode.getUserObject();
                String goodRuleChecked = goodRules.getProperty(PMDJEditPlugin.OPTION_RULES_PREFIX + rn.getRule().getName());
                boolean checked = goodRuleChecked == null ? false : "true".equals(goodRuleChecked);
                if (checked) {
                    checkingModel.addCheckingPath(path);
                    ruleset.addRule(rn.getRule());
                    hadCheckedRule = true;
                }
            }
            if (hadCheckedRule) {
                selectedRules.addRuleSet(ruleset);
            }
        }
    }

    protected void loadTree() {
        treeModel = new DefaultTreeModel(root);
        checkingModel = new DefaultTreeCheckingModel(treeModel);
        selectedRules = new RuleSets();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode ruleSetNode = (DefaultMutableTreeNode) root.getChildAt(i);
            RuleSet ruleset = new RuleSet();
            ruleset.setName("java");
            ruleset.setLanguage(Language.JAVA);
            boolean hadCheckedRule = false;
            for (int j = 0; j < ruleSetNode.getChildCount(); j++) {
                DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) ruleSetNode.getChildAt(j);
                TreePath path = new TreePath(ruleNode.getPath());
                RuleNode rn = (RuleNode) ruleNode.getUserObject();
                boolean checked = jEdit.getBooleanProperty(PMDJEditPlugin.OPTION_RULES_PREFIX + rn.getRule().getName(), false);
                if (checked) {
                    checkingModel.addCheckingPath(path);
                    ruleset.addRule(rn.getRule());
                    hadCheckedRule = true;
                }
            }
            if (hadCheckedRule) {
                selectedRules.addRuleSet(ruleset);
            }
        }
    }

    protected void loadJspTree() {
        jspTreeModel = new DefaultTreeModel(jspRoot);
        jspCheckingModel = new DefaultTreeCheckingModel(jspTreeModel);
        jspSelectedRules = new RuleSets();
        for (int i = 0; i < jspRoot.getChildCount(); i++) {
            DefaultMutableTreeNode ruleSetNode = (DefaultMutableTreeNode) jspRoot.getChildAt(i);
            RuleSet ruleset = new RuleSet();
            ruleset.setName("jsp");
            ruleset.setLanguage(Language.JSP);
            boolean hadCheckedRule = false;
            for (int j = 0; j < ruleSetNode.getChildCount(); j++) {
                DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) ruleSetNode.getChildAt(j);
                TreePath path = new TreePath(ruleNode.getPath());
                RuleNode rn = (RuleNode) ruleNode.getUserObject();
                boolean checked = jEdit.getBooleanProperty(PMDJEditPlugin.OPTION_RULES_PREFIX + rn.getRule().getName(), false);
                if (checked) {
                    jspCheckingModel.addCheckingPath(path);
                    ruleset.addRule(rn.getRule());
                    hadCheckedRule = true;
                }
            }
            if (hadCheckedRule) {
                jspSelectedRules.addRuleSet(ruleset);
            }
        }
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    public TreeModel getJspTreeModel() {
        return jspTreeModel;
    }

    public TreeCheckingModel getCheckingModel() {
        return checkingModel;
    }

    public TreeCheckingModel getJspCheckingModel() {
        return jspCheckingModel;
    }

    public void save(TreeCheckingModel tcm) {
        checkingModel = tcm;
        root = (DefaultMutableTreeNode) checkingModel.getTreeModel().getRoot();
        for (int i = 0; i < root.getChildCount(); i++) {
            DefaultMutableTreeNode ruleSetNode = (DefaultMutableTreeNode) root.getChildAt(i);
            for (int j = 0; j < ruleSetNode.getChildCount(); j++) {
                DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) ruleSetNode.getChildAt(j);
                TreePath path = new TreePath(ruleNode.getPath());
                boolean checked = checkingModel.isPathChecked(path);
                RuleNode rn = (RuleNode) ruleNode.getUserObject();
                jEdit.setBooleanProperty(PMDJEditPlugin.OPTION_RULES_PREFIX + rn.getRule().getName(), checked);
            }
        }
    }

    public void saveJspRules(TreeCheckingModel tcm) {
        jspCheckingModel = tcm;
        jspRoot = (DefaultMutableTreeNode) jspCheckingModel.getTreeModel().getRoot();
        for (int i = 0; i < jspRoot.getChildCount(); i++) {
            DefaultMutableTreeNode ruleSetNode = (DefaultMutableTreeNode) jspRoot.getChildAt(i);
            for (int j = 0; j < ruleSetNode.getChildCount(); j++) {
                DefaultMutableTreeNode ruleNode = (DefaultMutableTreeNode) ruleSetNode.getChildAt(j);
                TreePath path = new TreePath(ruleNode.getPath());
                boolean checked = jspCheckingModel.isPathChecked(path);
                RuleNode rn = (RuleNode) ruleNode.getUserObject();
                jEdit.setBooleanProperty(PMDJEditPlugin.OPTION_RULES_PREFIX + rn.getRule().getName(), checked);
            }
        }
    }

    /**
     *  Gets the selectedRules attribute of the SelectedRules object
     *
     * @return    The selectedRules value
     */
    public RuleSets getSelectedRules() {
        return selectedRules;
    }

    public RuleSets getJspSelectedRules() {
        return jspSelectedRules;
    }

    public RuleSets getCombinedRules() {
        RuleSets sets = new RuleSets();
        if (selectedRules != null) {
            for (RuleSet rs : selectedRules.getAllRuleSets()) {
                sets.addRuleSet(rs);
            }
        }
        if (jspSelectedRules != null) {
            for (RuleSet rs : jspSelectedRules.getAllRuleSets()) {
                sets.addRuleSet(rs);
            }
        }
        return sets;
    }

    /**
     *  Adds a feature to the RuleSet2Rules attribute of the SelectedRules object
     *
     * @param  rs  The feature to be added to the RuleSet2Rules attribute
     */
    private void addRuleSet2Rules(RuleSet rs, DefaultMutableTreeNode rootNode) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new RuleSetNode(rs));
        List<Rule> rules = new ArrayList<Rule>(rs.getRules());
        Collections.sort(rules, ruleSorter);
        for (Rule rule : rules) {
            DefaultMutableTreeNode ruleNode = new DefaultMutableTreeNode(new RuleNode(rule));
            node.add(ruleNode);
        }
        rootNode.add(node);
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getJspRoot() {
        return jspRoot;
    }

    private final Comparator<Rule> ruleSorter = new Comparator<Rule>() {

        public int compare(Rule r1, Rule r2) {
            if (r1 == null) {
                return 1;
            }
            if (r2 == null) {
                return -1;
            }
            return r1.getName().compareTo(r2.getName());
        }
    };

    private final Comparator<RuleSet> rulesetSorter = new Comparator<RuleSet>() {

        public int compare(RuleSet r1, RuleSet r2) {
            if (r1 == null) {
                return 1;
            }
            if (r2 == null) {
                return -1;
            }
            return r1.getName().compareTo(r2.getName());
        }
    };
}
