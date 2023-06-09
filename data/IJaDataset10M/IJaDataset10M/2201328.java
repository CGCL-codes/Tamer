package org.jdesktop.swingx;

import org.jdesktop.swingx.decorator.PatternFilter;
import org.jdesktop.swingx.decorator.PatternHighlighter;
import org.jdesktop.swingx.decorator.PatternMatcher;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Rudimentary search panel.
 * 
 * Updates PatternMatchers from user input.
 * 
 * Supports 
 * 
 * <ol>
 * <li> text input to match
 * <li> match rules like contains/equals/... 
 * <li> toggle case sensitive match
 * </ol>
 * 
 * TODO: allow custom PatternModel and/or access 
 * to configuration of bound PatternModel. 
 * 
 * TODO: fully support control of multiple PatternMatchers.
 * 
 * @author Ramesh Gupta
 * @author Jeanette Winzenburg
 * 
 */
public class JXSearchPanel extends AbstractPatternPanel {

    public static final String MATCH_RULE_ACTION_COMMAND = "selectMatchRule";

    private JComboBox searchCriteria;

    private List<PatternMatcher> patternMatchers;

    public JXSearchPanel() {
        initComponents();
        build();
        initActions();
        bind();
        getPatternModel().setIncremental(true);
    }

    public void addPatternMatcher(PatternMatcher matcher) {
        getPatternMatchers().add(matcher);
        updateFieldName(matcher);
    }

    /**
     * sets the PatternFilter control.
     * 
     * PENDING: change to do a addPatternMatcher to enable multiple control.
     * 
     */
    public void setPatternFilter(PatternFilter filter) {
        getPatternMatchers().add(filter);
        updateFieldName(filter);
    }

    /**
     * sets the PatternHighlighter control.
     * 
     * PENDING: change to do a addPatternMatcher to enable multiple control.
     * 
     */
    public void setPatternHighlighter(PatternHighlighter highlighter) {
        getPatternMatchers().add(highlighter);
        updateFieldName(highlighter);
    }

    /**
     * set the label of the search combo.
     * 
     * @param name
     */
    public void setFieldName(String name) {
        searchLabel.setText(name);
    }

    /**
     * returns the label of the search combo.
     * 
     */
    public String getFieldName() {
        return searchLabel.getText();
    }

    /**
     * returns the current compiled Pattern.
     * 
     * @return the current compiled <code>Pattern</code>
     */
    public Pattern getPattern() {
        return patternModel.getPattern();
    }

    /**
     * @param matcher
     */
    protected void updateFieldName(PatternMatcher matcher) {
        if (matcher instanceof PatternFilter) {
            PatternFilter filter = (PatternFilter) matcher;
            searchLabel.setText(filter.getColumnName());
        } else {
            if (searchLabel.getText().length() == 0) {
                searchLabel.setText("Field");
            }
        }
    }

    /**
     * 
     */
    public void match() {
        for (Iterator<PatternMatcher> iter = getPatternMatchers().iterator(); iter.hasNext(); ) {
            iter.next().setPattern(getPattern());
        }
    }

    /**
     * set's the PatternModel's MatchRule to the selected in combo. 
     * 
     * NOTE: this
     * is public as an implementation side-effect! 
     * No need to ever call directly.
     */
    public void updateMatchRule() {
        getPatternModel().setMatchRule((String) searchCriteria.getSelectedItem());
    }

    private List<PatternMatcher> getPatternMatchers() {
        if (patternMatchers == null) {
            patternMatchers = new ArrayList<PatternMatcher>();
        }
        return patternMatchers;
    }

    @Override
    protected void initExecutables() {
        super.initExecutables();
        getActionMap().put(MATCH_RULE_ACTION_COMMAND, createBoundAction(MATCH_RULE_ACTION_COMMAND, "updateMatchRule"));
    }

    /**
     * bind the components to the patternModel/actions.
     */
    @Override
    protected void bind() {
        super.bind();
        List matchRules = getPatternModel().getMatchRules();
        ComboBoxModel model = new DefaultComboBoxModel(matchRules.toArray());
        model.setSelectedItem(getPatternModel().getMatchRule());
        searchCriteria.setModel(model);
        searchCriteria.setAction(getAction(MATCH_RULE_ACTION_COMMAND));
    }

    /**
     * build container by adding all components.
     * PRE: all components created.
     */
    private void build() {
        add(searchLabel);
        add(searchCriteria);
        add(searchField);
        add(matchCheck);
    }

    /**
     * create contained components.
     * 
     *
     */
    @Override
    protected void initComponents() {
        super.initComponents();
        searchCriteria = new JComboBox();
    }
}
