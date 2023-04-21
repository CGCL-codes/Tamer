package com.listentothesong.dictionary;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.addon.BorderListPanelGenerator;
import javax.swing.addon.CopyPastePopupMenuDecorator;
import javax.swing.addon.ErrorDetailsDialog;
import javax.swing.addon.FastScrollPane;
import javax.swing.addon.JLink;
import javax.swing.addon.LoadingDialog;
import javax.swing.addon.Translator;
import javax.swing.border.EmptyBorder;
import com.listentothesong.ImagesRepository;
import com.listentothesong.ListenToTheSongPreferences;
import com.listentothesong.dictionary.api.AbstractDictionary;

@SuppressWarnings("serial")
public class DictionaryDialog extends JDialog {

    private static final Dimension DEFAULT_SIZE = new Dimension(450, 400);

    private final JFrame owner;

    private final Translator translator;

    private final DictionaryLookup dictionaryLookup;

    private final LoadingDialog loadingDialog;

    private final JTextField wordToDefine = new JTextField();

    private final DefaultComboBoxModel dictionaryListModel = new DefaultComboBoxModel();

    private final JComboBox dictionaryList = new JComboBox(dictionaryListModel);

    private final JTextArea definitionText = new JTextArea();

    private final JScrollPane scrollPane = new FastScrollPane(definitionText);

    private final DictionarySelector dictionarySelector;

    final ItemListener itemListener = new ItemListener() {

        @Override
        public void itemStateChanged(ItemEvent e) {
            try {
                AbstractDictionary selectedDictionary = (AbstractDictionary) dictionaryListModel.getSelectedItem();
                if (selectedDictionary != null) ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.setValue(selectedDictionary.getDictionaryName()); else ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.unset();
                performSearch();
            } catch (Throwable ex) {
                ErrorDetailsDialog.open(ex);
                ;
            }
        }
    };

    private JLabel dictionaryServiceLabel;

    public DictionaryDialog(JFrame owner, final Translator translator, final DictionaryLookup dictionaryLookup) {
        super(owner, translator.translate("dictionary.title"), true);
        setSize(DEFAULT_SIZE);
        setLocation(owner.getX() + (owner.getWidth() - getWidth()) / 2, owner.getY() + (owner.getHeight() - getHeight()) / 2);
        this.owner = owner;
        this.translator = translator;
        this.dictionaryLookup = dictionaryLookup;
        this.loadingDialog = new LoadingDialog(this, translator.translate("loading.title"));
        this.dictionarySelector = new DictionarySelector(this, translator, dictionaryLookup);
        definitionText.setCaretPosition(0);
        definitionText.setEditable(false);
        definitionText.setEnabled(false);
        dictionaryList.setPreferredSize(new JTextField().getPreferredSize());
        dictionaryList.setEnabled(false);
        dictionaryList.addItemListener(itemListener);
        dictionaryList.setBackground(Color.white);
        dictionaryList.setFont(dictionaryList.getFont().deriveFont(Font.PLAIN));
        CopyPastePopupMenuDecorator.decorate(definitionText, translator);
        CopyPastePopupMenuDecorator.decorate(wordToDefine, translator);
        JLabel translatorLabel = new JLabel(translator.translate("dictionary.selectsource") + ":");
        translatorLabel.setBorder(new EmptyBorder(0, 0, 3, 0));
        JPanel dictionaryPanel = new JPanel(new BorderLayout());
        dictionaryPanel.add(translatorLabel, BorderLayout.NORTH);
        dictionaryPanel.add(dictionaryList);
        dictionaryPanel.setBorder(new EmptyBorder(5, 10, 0, 10));
        JLabel dictIcon = new JLabel(ImagesRepository.DICT_ICON);
        dictIcon.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton searchButton = new JButton(translator.translate("dictionary.search"), ImagesRepository.SEARCH_ICON);
        final JLink dictionarySelectorLink = new JLink(translator.translate("dictionary.selectdict.change"));
        dictionarySelectorLink.setHorizontalTextPosition(JLabel.RIGHT);
        dictionarySelectorLink.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dictionaryList.removeItemListener(itemListener);
                try {
                    dictionarySelector.deselectAllDictionaries();
                    for (String dictionaryName : ListenToTheSongPreferences.DICTIONARIES_PREF.getValueAsStringCollection()) {
                        AbstractDictionary dictionary = dictionaryLookup.getDictionary(dictionaryName);
                        if (dictionary != null) dictionarySelector.selectDictionary(dictionary);
                    }
                    dictionarySelector.setQuickDictionary(dictionaryLookup.getDictionary(ListenToTheSongPreferences.QUICK_DEFINITION_PREF.getValue()));
                    if (dictionarySelector.openDialog()) {
                        AbstractDictionary prevSelectedDictionary = dictionaryLookup.getDictionary(ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.getValue());
                        dictionaryListModel.removeAllElements();
                        LinkedList<String> dictionaries = new LinkedList<String>();
                        for (AbstractDictionary dictionary : dictionarySelector.getPreferredDictionaries()) {
                            dictionaryListModel.addElement(dictionary);
                            dictionaries.add(dictionary.getDictionaryName());
                        }
                        if (prevSelectedDictionary != null && dictionaries.contains(prevSelectedDictionary)) dictionaryListModel.setSelectedItem(prevSelectedDictionary);
                        ListenToTheSongPreferences.DICTIONARIES_PREF.setValue(dictionaries);
                        AbstractDictionary selectedDictionary = (AbstractDictionary) dictionaryListModel.getSelectedItem();
                        if (selectedDictionary != null) ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.setValue(selectedDictionary.getDictionaryName()); else ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.unset();
                        AbstractDictionary quickDictionary = dictionarySelector.getQuickDictionary();
                        if (quickDictionary != null) ListenToTheSongPreferences.QUICK_DEFINITION_PREF.setValue(quickDictionary.getDictionaryName()); else ListenToTheSongPreferences.QUICK_DEFINITION_PREF.unset();
                        performSearch();
                    }
                } catch (Throwable t) {
                    ErrorDetailsDialog.open(t);
                } finally {
                    dictionaryList.addItemListener(itemListener);
                }
            }
        });
        for (String dictionaryName : ListenToTheSongPreferences.DICTIONARIES_PREF.getValueAsStringCollection()) dictionaryListModel.addElement(dictionaryLookup.getDictionary(dictionaryName));
        AbstractDictionary selectedDictionary = dictionaryLookup.getDictionary(ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.getValue());
        if (selectedDictionary != null) dictionaryListModel.setSelectedItem(selectedDictionary);
        JPanel wordToDefinePanel = new JPanel(new BorderLayout());
        wordToDefinePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        wordToDefinePanel.add(wordToDefine);
        wordToDefinePanel.add(searchButton, BorderLayout.EAST);
        wordToDefinePanel.add(dictionarySelectorLink, BorderLayout.SOUTH);
        wordToDefine.setFont(wordToDefine.getFont().deriveFont(Font.BOLD));
        ActionListener searchWordActionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    String word = wordToDefine.getText().trim();
                    if (word.length() > 0) searchWord(word);
                } catch (Throwable t) {
                    ErrorDetailsDialog.open(t);
                }
            }
        };
        searchButton.addActionListener(searchWordActionListener);
        wordToDefine.addActionListener(searchWordActionListener);
        JPanel dictionaryWithIconPanel = new JPanel(new BorderLayout());
        dictionaryWithIconPanel.add(dictIcon, BorderLayout.WEST);
        dictionaryWithIconPanel.add(wordToDefinePanel);
        dictionaryWithIconPanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        JPanel scrollPanePanel = new JPanel(new BorderLayout());
        scrollPanePanel.add(scrollPane);
        scrollPanePanel.setBorder(new EmptyBorder(5, 10, 0, 10));
        JButton closeButton = new JButton(translator.translate("general.close"), ImagesRepository.CLOSE_ICON);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(closeButton);
        dictionaryServiceLabel = new JLabel(" ", JLabel.CENTER);
        dictionaryServiceLabel.setBorder(new EmptyBorder(5, 10, 10, 10));
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(dictionaryServiceLabel, BorderLayout.NORTH);
        southPanel.add(buttonsPanel);
        BorderListPanelGenerator panelGenerator = new BorderListPanelGenerator(BorderLayout.NORTH);
        panelGenerator.add(dictionaryWithIconPanel);
        panelGenerator.add(dictionaryPanel);
        add(panelGenerator.extractPanel(scrollPanePanel));
        add(southPanel, BorderLayout.SOUTH);
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    setVisible(false);
                } catch (Throwable t) {
                    ErrorDetailsDialog.open(t);
                }
            }
        });
    }

    public void openDialog() {
        setVisible(true);
    }

    public void searchWord(final String word) {
        wordToDefine.setText(word);
        wordToDefine.setCaretPosition(word.length());
        performSearch();
    }

    private void performSearch() {
        final String word = wordToDefine.getText().trim();
        if (word != null && word.length() > 0) {
            loadingDialog.monitorizeTask(translator.translate("loading.dictionary.label", word), new Runnable() {

                public void run() {
                    try {
                        AbstractDictionary selectedDictionary = dictionaryLookup.getDictionary(ListenToTheSongPreferences.SELECTED_DICTIONARY_PREF.getValue());
                        if (selectedDictionary == null) {
                            definitionText.setText("");
                            dictionaryList.setEnabled(false);
                            definitionText.setEnabled(false);
                            loadingDialog.endTaskIndication();
                        } else {
                            dictionaryList.setEnabled(true);
                            String[] definitions = selectedDictionary.define(word);
                            dictionaryServiceLabel.setText(translator.translate("dictionary.powered", selectedDictionary.getServiceProviderName()));
                            if (definitions == null || definitions.length == 0) {
                                definitionText.setText("");
                                definitionText.setEnabled(false);
                                loadingDialog.endTaskIndication();
                                JOptionPane.showMessageDialog(owner, translator.translate("dictionary.notfound.label", word), translator.translate("dictionary.error.title", word), JOptionPane.ERROR_MESSAGE);
                            } else {
                                definitionText.setEnabled(true);
                                String definitionsText = null;
                                for (String definition : definitions) {
                                    if (definitionsText == null) definitionsText = definition; else definitionsText += "\n\n_____________\n\n" + definition;
                                }
                                definitionText.setText(definitionsText);
                                loadingDialog.endTaskIndication();
                            }
                        }
                    } catch (RemoteException re) {
                        loadingDialog.endTaskIndication();
                        re.printStackTrace(System.err);
                        JOptionPane.showMessageDialog(owner, translator.translate("dictionary.errorconn.label", word), translator.translate("dictionary.error.title", word), JOptionPane.ERROR_MESSAGE);
                    } catch (Throwable t) {
                        loadingDialog.endTaskIndication();
                        ErrorDetailsDialog.open(t);
                    }
                }
            });
        }
    }
}
