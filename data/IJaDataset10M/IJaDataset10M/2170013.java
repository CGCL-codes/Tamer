package com.tarpri.preferences;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.jhlabs.awt.ParagraphLayout;

/**
 * @author root Created on Aug 24, 2004 TODO To change the template for this
 *         generated type comment go to Window - Preferences - Java - Code
 *         Generation - Code and Comments
 */
public class ListPreferencesValues extends PreferenceSheet implements ActionListener {

    private JTextField _in_censored_word;

    private JButton _add_censored, _remove_censored, _change_censored;

    private JCheckBox _is_regex, _match_case, _active_option;

    private JTable _censored_words;

    private MatchTableModel _censored_words_data;

    /**
     * 
     */
    public ListPreferencesValues(String preference_name) {
        super();
        _preference_name = preference_name;
        _censored_words_data = new MatchTableModel();
        _censored_words = new JTable(_censored_words_data);
        _in_censored_word = new JTextField(50);
        _add_censored = new JButton("Add Match");
        _remove_censored = new JButton("Remove Match");
        _change_censored = new JButton("Change Match");
        _add_censored.addActionListener(this);
        _remove_censored.addActionListener(this);
        _change_censored.addActionListener(this);
        _is_regex = new JCheckBox("Regular Expression", false);
        _match_case = new JCheckBox("Match Case", false);
        _active_option = new JCheckBox("Active", true);
        build_gui();
    }

    private void build_gui() {
        setLayout(new ParagraphLayout());
        Container container = this;
        container.add(new JLabel("Category Options:"));
        container.add(_active_option);
        container.add(new JLabel("Find:"), ParagraphLayout.NEW_PARAGRAPH);
        container.add(_in_censored_word);
        container.add(_is_regex, ParagraphLayout.NEW_LINE);
        container.add(_match_case);
        container.add(_add_censored, ParagraphLayout.NEW_LINE);
        container.add(_change_censored);
        container.add(_remove_censored);
        JScrollPane view_list = new JScrollPane(_censored_words);
        view_list.setPreferredSize(new Dimension(600, 200));
        container.add(view_list, ParagraphLayout.NEW_LINE);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == _add_censored) {
            _censored_words_data.add(build_user_match());
        } else if (arg0.getSource() == _remove_censored) {
            _censored_words_data.remove(_censored_words.getSelectedRow());
        } else if (arg0.getSource() == _change_censored) {
            _censored_words_data.set(_censored_words.getSelectedRow(), build_user_match());
        }
    }

    protected TarpriMatch build_user_match() {
        return new TarpriMatch(_in_censored_word.getText(), _is_regex.isSelected(), _match_case.isSelected());
    }

    /**
     * @see tarpri.inetfilter.preferences.PreferenceSheet#load_values(java.io.DataInputStream)
     */
    public void load_values(PreferencesState input) {
        PreferenceCategory category = input.get_preferences(_preference_name);
        if (category != null) {
            _active = category.isActive();
            _active_option.setSelected(_active);
            List<TarpriMatch> matches = category.getValues();
            if (matches != null) _censored_words_data.add(matches);
        }
    }

    /**
     * @see tarpri.inetfilter.preferences.PreferenceSheet#save_values(java.io.DataOutputStream)
     */
    public void save_values(PreferencesState output) {
        _active = _active_option.isSelected();
        PreferenceCategory category = new PreferenceCategory(_active, _preference_name, _censored_words_data.toList());
        output.set_preferences(_preference_name, category);
    }
}
