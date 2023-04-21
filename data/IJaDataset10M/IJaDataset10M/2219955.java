package jp.ac.keio.ae.comp.yamaguti.doddle.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.sql.Statement;
import java.util.*;
import java.util.List;
import java.util.Map.*;
import javax.swing.*;
import javax.swing.event.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.data.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.taskanalyzer.*;
import jp.ac.keio.ae.comp.yamaguti.doddle.utils.*;
import net.didion.jwnl.data.*;
import net.infonode.docking.*;
import net.infonode.docking.util.*;
import net.java.sen.*;
import net.java.sen.dictionary.*;
import org.apache.log4j.*;

/**
 * @author takeshi morita
 */
public class InputDocumentSelectionPanel extends JPanel implements ListSelectionListener, ActionListener {

    private Set<String> stopWordSet;

    private JList docList;

    private JList inputDocList;

    private JComboBox docLangBox;

    private JButton addDocButton;

    private JButton removeDocButton;

    private JComboBox inputDocLangBox;

    private JButton addInputDocButton;

    private JButton removeInputDocButton;

    private JButton termExtractionButton;

    private JCheckBox genSenCheckBox;

    private JCheckBox cabochaCheckBox;

    private JCheckBox showImportanceCheckBox;

    private JCheckBox nounCheckBox;

    private JCheckBox verbCheckBox;

    private JCheckBox otherCheckBox;

    private JCheckBox oneWordCheckBox;

    private JTextField punctuationField;

    private JButton setPunctuationButton;

    public static String PUNCTUATION_CHARS = "．|。|\\.";

    public static final String COMPOUND_WORD_JA = "複合語";

    public static final String COMPOUND_WORD_EN = "Compound Word";

    private ImageIcon addDocIcon = Utils.getImageIcon("page_white_add.png");

    private ImageIcon removeDocIcon = Utils.getImageIcon("page_white_delete.png");

    private TaskAnalyzer taskAnalyzer;

    private JTextArea inputDocArea;

    private Map<String, TermInfo> termInfoMap;

    private InputTermSelectionPanel inputTermSelectionPanel;

    private DODDLEProject project;

    private View[] mainViews;

    private RootWindow rootWindow;

    public InputDocumentSelectionPanel(InputTermSelectionPanel iwsPanel, DODDLEProject p) {
        project = p;
        inputTermSelectionPanel = iwsPanel;
        termInfoMap = new HashMap<String, TermInfo>();
        stopWordSet = new HashSet<String>();
        docList = new JList(new DefaultListModel());
        docList.addListSelectionListener(this);
        JScrollPane docListScroll = new JScrollPane(docList);
        inputDocList = new JList(new DefaultListModel());
        inputDocList.addListSelectionListener(this);
        JScrollPane inputDocListScroll = new JScrollPane(inputDocList);
        DefaultComboBoxModel docLangBoxModel = new DefaultComboBoxModel(new Object[] { "en", "ja" });
        docLangBox = new JComboBox(docLangBoxModel);
        docLangBox.addActionListener(this);
        addDocButton = new JButton(new AddDocAction(Translator.getTerm("AddDocumentButton")));
        removeDocButton = new JButton(new RemoveDocAction(Translator.getTerm("RemoveDocumentButton")));
        DefaultComboBoxModel inputDocLangBoxModel = new DefaultComboBoxModel(new Object[] { "en", "ja" });
        inputDocLangBox = new JComboBox(inputDocLangBoxModel);
        inputDocLangBox.addActionListener(this);
        addInputDocButton = new JButton(new AddInputDocAction(Translator.getTerm("AddInputDocumentButton")));
        removeInputDocButton = new JButton(new RemoveInputDocAction(Translator.getTerm("RemoveInputDocumentButton")));
        inputDocArea = new JTextArea();
        inputDocArea.setLineWrap(true);
        JScrollPane inputDocAreaScroll = new JScrollPane(inputDocArea);
        JPanel docButtonPanel = new JPanel();
        docButtonPanel.setLayout(new BorderLayout());
        docButtonPanel.setLayout(new GridLayout(1, 3));
        docButtonPanel.add(docLangBox);
        docButtonPanel.add(addDocButton);
        docButtonPanel.add(removeDocButton);
        JPanel docPanel = new JPanel();
        docPanel.setLayout(new BorderLayout());
        docPanel.add(docListScroll, BorderLayout.CENTER);
        docPanel.add(docButtonPanel, BorderLayout.SOUTH);
        punctuationField = new JTextField(10);
        punctuationField.setText(PUNCTUATION_CHARS);
        setPunctuationButton = new JButton(Translator.getTerm("SetPunctuationCharacterButton"));
        setPunctuationButton.addActionListener(this);
        JPanel punctuationPanel = new JPanel();
        punctuationPanel.add(punctuationField);
        punctuationPanel.add(setPunctuationButton);
        JPanel inputDocButtonPanel = new JPanel();
        inputDocButtonPanel.setLayout(new GridLayout(1, 3));
        inputDocButtonPanel.add(inputDocLangBox);
        inputDocButtonPanel.add(addInputDocButton);
        inputDocButtonPanel.add(removeInputDocButton);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(inputDocButtonPanel, BorderLayout.WEST);
        southPanel.add(punctuationPanel, BorderLayout.EAST);
        JPanel inputDocPanel = new JPanel();
        inputDocPanel.setLayout(new BorderLayout());
        inputDocPanel.add(inputDocListScroll, BorderLayout.CENTER);
        inputDocPanel.add(southPanel, BorderLayout.SOUTH);
        termExtractionButton = new JButton(Translator.getTerm("InputTermExtractionButton"), Utils.getImageIcon("input_term_selection.png"));
        termExtractionButton.addActionListener(this);
        genSenCheckBox = new JCheckBox(Translator.getTerm("GensenCheckBox"));
        genSenCheckBox.setSelected(false);
        cabochaCheckBox = new JCheckBox(Translator.getTerm("CabochaCheckBox"));
        cabochaCheckBox.setSelected(true);
        showImportanceCheckBox = new JCheckBox("重要度");
        nounCheckBox = new JCheckBox(Translator.getTerm("NounCheckBox"));
        nounCheckBox.setSelected(true);
        verbCheckBox = new JCheckBox(Translator.getTerm("VerbCheckBox"));
        verbCheckBox.setSelected(false);
        otherCheckBox = new JCheckBox(Translator.getTerm("OtherPOSCheckBox"));
        oneWordCheckBox = new JCheckBox(Translator.getTerm("OneCharacterCheckBox"));
        JPanel morphemeAnalysisPanel = new JPanel();
        morphemeAnalysisPanel.add(genSenCheckBox);
        morphemeAnalysisPanel.add(cabochaCheckBox);
        morphemeAnalysisPanel.add(nounCheckBox);
        morphemeAnalysisPanel.add(verbCheckBox);
        morphemeAnalysisPanel.add(otherCheckBox);
        morphemeAnalysisPanel.add(oneWordCheckBox);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(morphemeAnalysisPanel, BorderLayout.WEST);
        buttonPanel.add(termExtractionButton, BorderLayout.EAST);
        mainViews = new View[2];
        ViewMap viewMap = new ViewMap();
        mainViews[0] = new View(Translator.getTerm("InputDocumentList"), null, inputDocPanel);
        mainViews[1] = new View(Translator.getTerm("InputDocumentArea"), null, inputDocAreaScroll);
        for (int i = 0; i < mainViews.length; i++) {
            viewMap.addView(i, mainViews[i]);
        }
        rootWindow = Utils.createDODDLERootWindow(viewMap);
        setLayout(new BorderLayout());
        add(rootWindow, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setXGALayout() {
        SplitWindow sw2 = new SplitWindow(false, 0.4f, mainViews[0], mainViews[1]);
        rootWindow.setWindow(sw2);
    }

    private void setStopWordSet() {
        stopWordSet.clear();
        BufferedReader reader = null;
        try {
            File file = new File(STOP_WORD_LIST_FILE);
            if (!file.exists()) {
                return;
            }
            FileInputStream fis = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine();
                stopWordSet.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
        }
    }

    public boolean isOneWordChecked() {
        return oneWordCheckBox.isSelected();
    }

    public boolean isStopWord(String w) {
        return stopWordSet.contains(w);
    }

    private void deleteFiles(File file) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    private String getTextFileName(String fileName) {
        if (!fileName.endsWith("txt")) {
            fileName += ".txt";
        }
        return fileName;
    }

    private void saveFiles(Map<File, String> fileTextStringMap, File saveDir) {
        BufferedWriter writer = null;
        try {
            for (Entry<File, String> entrySet : fileTextStringMap.entrySet()) {
                File file = entrySet.getKey();
                String text = entrySet.getValue();
                File saveFile = new File(saveDir, getTextFileName(file.getName()));
                FileOutputStream fos = new FileOutputStream(saveFile);
                writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
                writer.write(text);
                writer.close();
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                }
            }
        }
    }

    private Map<File, String> getFileTextStringMap(ListModel listModel) {
        Map<File, String> fileTextStringMap = new HashMap<File, String>();
        for (int i = 0; i < listModel.getSize(); i++) {
            Document doc = (Document) listModel.getElementAt(i);
            fileTextStringMap.put(doc.getFile(), doc.getText());
        }
        return fileTextStringMap;
    }

    /**
     * 同名のファイルが複数ある場合には，上書きされる
     */
    public void saveDocuments(File saveDir) {
        File inputDocs = new File(saveDir, "inputDocs");
        Map<File, String> fileTextStringMap = getFileTextStringMap(inputDocList.getModel());
        if (!inputDocs.mkdir()) {
            deleteFiles(inputDocs);
        }
        saveFiles(fileTextStringMap, inputDocs);
        saveDocumentInfo(saveDir);
    }

    public void saveDocumentInfo(File saveDir) {
        File docInfo = new File(saveDir, ProjectFileNames.DOC_INFO_FILE);
        BufferedWriter writer = null;
        try {
            FileOutputStream fos = new FileOutputStream(docInfo);
            writer = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            for (int i = 0; i < inputDocList.getModel().getSize(); i++) {
                Document doc = (Document) inputDocList.getModel().getElementAt(i);
                writer.write("inputDoc," + doc.getFile().getAbsolutePath() + "," + doc.getLang() + "\n");
            }
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ioe2) {
                    ioe2.printStackTrace();
                }
            }
        }
    }

    public void saveDocumentInfo(int projectID, Statement stmt) {
        DBManagerDialog.deleteTableContents(projectID, stmt, "doc_info");
        int docID = 1;
        try {
            for (int i = 0; i < inputDocList.getModel().getSize(); i++) {
                Document doc = (Document) inputDocList.getModel().getElementAt(i);
                String path = URLEncoder.encode(doc.getFile().getAbsolutePath(), "UTF8");
                String lang = doc.getLang();
                String text = URLEncoder.encode(doc.getText(), "UTF8");
                String sql = "INSERT INTO doc_info (Project_ID,Doc_ID,Doc_Path,Language,Text) " + "VALUES(" + projectID + "," + docID + ",'" + path + "','" + lang + "','" + text + "')";
                stmt.executeUpdate(sql);
                docID++;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void openDocuments(File openDir) {
        File docs = new File(openDir, ProjectFileNames.DOC_DIR);
        if (docs.listFiles() != null) {
            Set fileSet = new TreeSet();
            getFiles(docs.listFiles(), fileSet);
            if (fileSet == null) {
                return;
            }
            addDocuments(docList, fileSet);
        }
    }

    public void loadDocuments(File openDir) {
        File docInfo = new File(openDir, ProjectFileNames.DOC_INFO_FILE);
        if (!docInfo.exists()) {
            return;
        }
        BufferedReader reader = null;
        try {
            FileInputStream fis = new FileInputStream(docInfo);
            reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] info = line.split(",");
                if (info.length != 3) {
                    continue;
                }
                String type = info[0];
                String fileName = info[1];
                String lang = info[2];
                if (type.equals("doc")) {
                    DefaultListModel model = (DefaultListModel) docList.getModel();
                    model.addElement(new Document(lang, new File(fileName)));
                } else if (type.equals("inputDoc")) {
                    DefaultListModel model = (DefaultListModel) inputDocList.getModel();
                    model.addElement(new Document(lang, new File(fileName)));
                }
            }
            inputTermSelectionPanel.setInputDocumentListModel(inputDocList.getModel());
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
        }
    }

    public void loadDocuments(int projectID, Statement stmt) {
        try {
            String sql = "SELECT * from doc_info where Project_ID=" + projectID;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String docPath = URLDecoder.decode(rs.getString("Doc_Path"), "UTF8");
                String lang = rs.getString("Language");
                String text = URLDecoder.decode(rs.getString("Text"), "UTF8");
                DefaultListModel model = (DefaultListModel) inputDocList.getModel();
                model.addElement(new Document(lang, new File(docPath), text));
            }
            inputTermSelectionPanel.setInputDocumentListModel(inputDocList.getModel());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void openInputDocuments(File openDir) {
        File inputDocs = new File(openDir, ProjectFileNames.INPUT_DOC_DIR);
        if (inputDocs.listFiles() != null) {
            Set fileSet = new TreeSet();
            getFiles(inputDocs.listFiles(), fileSet);
            if (fileSet == null) {
                return;
            }
            addDocuments(inputDocList, fileSet);
        }
    }

    public int getDocNum() {
        return docList.getModel().getSize() + inputDocList.getModel().getSize();
    }

    private void setTermInfoMap(String term, String pos, File doc, boolean isInputDoc) {
        TermInfo info = null;
        if (termInfoMap.get(term) != null) {
            info = termInfoMap.get(term);
        } else {
            int docNum = docList.getModel().getSize() + inputDocList.getModel().getSize();
            info = new TermInfo(term, docNum);
        }
        if (!(pos.equals(COMPOUND_WORD_EN) || pos.equals(COMPOUND_WORD_JA))) {
            info.addPos(pos);
        } else if (info.getPosSet().size() == 0) {
            info.addPos(pos);
        }
        if (isInputDoc) {
            info.putInputDoc(doc);
        } else {
            info.putDoc(doc);
        }
        termInfoMap.put(term, info);
    }

    private String runSSTagger(String text) {
        StringBuffer buf = new StringBuffer("");
        BufferedReader reader = null;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SS_TAGGER_HOME + File.separator + "tmp.txt"), "UTF-8"));
            text = text.replaceAll("．|\\*", " ");
            bw.write(text);
            bw.close();
            ProcessBuilder processBuilder = new ProcessBuilder(SS_TAGGER_HOME + File.separator + "tagger.exe", "-i", "tmp.txt");
            processBuilder.directory(new File(SS_TAGGER_HOME));
            ssTaggerProcess = processBuilder.start();
            reader = new BufferedReader(new InputStreamReader(ssTaggerProcess.getErrorStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                DODDLE.STATUS_BAR.printMessage("SS-Tagger " + line);
                if (line.matches(".*15")) {
                    break;
                }
            }
            reader.close();
            reader = new BufferedReader(new InputStreamReader(ssTaggerProcess.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                buf.append(line);
                buf.append("\n");
            }
            reader.close();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(SS_TAGGER_HOME + File.separator + "tmpTagger.txt"), "UTF-8"));
            bw.write(buf.toString());
            bw.close();
        } catch (IOException ioe) {
            DODDLE.getLogger().log(Level.DEBUG, "SS Tagger can not execute.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
        }
        return buf.toString();
    }

    private void setTermInfo(String term, String pos, String basicStr, File file, boolean isInputDoc) {
        WordNetDic wordNetAPI = WordNetDic.getInstance();
        if (nounCheckBox.isSelected() && isEnNoun(pos)) {
            IndexWord indexWord = wordNetAPI.getIndexWord(POS.NOUN, term.toLowerCase());
            if (indexWord != null && indexWord.getLemma() != null) {
                basicStr = indexWord.getLemma();
            }
            setTermInfoMap(basicStr, pos, file, isInputDoc);
        } else if (verbCheckBox.isSelected() && isEnVerb(pos)) {
            IndexWord indexWord = wordNetAPI.getIndexWord(POS.VERB, term.toLowerCase());
            if (indexWord != null && indexWord.getLemma() != null) {
                basicStr = indexWord.getLemma();
            }
            setTermInfoMap(basicStr, pos, file, isInputDoc);
        } else if (otherCheckBox.isSelected() && isEnOther(pos)) {
            setTermInfoMap(basicStr, pos, file, isInputDoc);
        }
    }

    private void setTermInfo(String word, String basicStr, File file, boolean isInputDoc) {
        WordNetDic wordNetAPI = WordNetDic.getInstance();
        if (nounCheckBox.isSelected()) {
            IndexWord indexWord = wordNetAPI.getIndexWord(POS.NOUN, word.toLowerCase());
            if (indexWord != null && indexWord.getLemma() != null) {
                basicStr = indexWord.getLemma();
                setTermInfoMap(basicStr, "noun", file, isInputDoc);
            }
        }
        if (verbCheckBox.isSelected()) {
            IndexWord indexWord = wordNetAPI.getIndexWord(POS.VERB, word.toLowerCase());
            if (indexWord != null && indexWord.getLemma() != null) {
                basicStr = indexWord.getLemma();
                setTermInfoMap(basicStr, "verb", file, isInputDoc);
            }
        }
        if (otherCheckBox.isSelected()) {
            setTermInfoMap(basicStr, "", file, isInputDoc);
        }
    }

    private void enTermExtraction(String text, Document doc, boolean isInputDoc) {
        File file = doc.getFile();
        String taggedText = runSSTagger(text);
        if (taggedText.length() != 0) {
            String[] token = taggedText.split("\\s");
            if (token == null) {
                return;
            }
            for (int i = 0; i < token.length; i++) {
                String[] info = token[i].split("/");
                if (info.length != 2) {
                    continue;
                }
                String word = info[0];
                String pos = info[1];
                String basicStr = word.toLowerCase();
                if (!oneWordCheckBox.isSelected() && basicStr.length() == 1) {
                    continue;
                }
                if (isStopWord(basicStr)) {
                    continue;
                }
                setTermInfo(word, pos, basicStr, file, isInputDoc);
            }
        } else {
            String[] words = text.split("\\s");
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String basicStr = word.toLowerCase();
                if (!oneWordCheckBox.isSelected() && basicStr.length() == 1) {
                    continue;
                }
                if (isStopWord(basicStr)) {
                    continue;
                }
                setTermInfo(word, basicStr, file, isInputDoc);
            }
        }
        if (genSenCheckBox.isSelected()) {
            Set<String> comoundWordSet = getGensenCompoundWordSet(text, doc.getLang());
            for (String compoundWord : comoundWordSet) {
                setTermInfoMap(compoundWord, COMPOUND_WORD_EN, file, isInputDoc);
            }
        }
    }

    private void jaTermExtraction(String text, Document doc, boolean isInputDoc) {
        File file = doc.getFile();
        try {
            StringTagger tagger = SenFactory.getStringTagger(DODDLEConstants.GOSEN_CONFIGURATION_FILE);
            List<Token> tokenList = tagger.analyze(text);
            for (Token token : tokenList) {
                String pos = token.getMorpheme().getPartOfSpeech();
                String basicStr = token.getMorpheme().getBasicForm();
                if (!oneWordCheckBox.isSelected() && basicStr.length() == 1) {
                    continue;
                }
                if (isStopWord(basicStr)) {
                    continue;
                }
                if (nounCheckBox.isSelected() && isJaNoun(pos)) {
                    setTermInfoMap(basicStr, pos, file, isInputDoc);
                } else if (verbCheckBox.isSelected() && isJaVerb(pos)) {
                    setTermInfoMap(basicStr, pos, file, isInputDoc);
                } else if (otherCheckBox.isSelected() && isJaOther(pos)) {
                    setTermInfoMap(basicStr, pos, file, isInputDoc);
                }
            }
            if (genSenCheckBox.isSelected()) {
                Set<String> compoundWordSet = getGensenCompoundWordSet(text, doc.getLang());
                for (String compoundWord : compoundWordSet) {
                    setTermInfoMap(compoundWord, COMPOUND_WORD_JA, file, isInputDoc);
                }
            }
            if (cabochaCheckBox.isSelected()) {
                CabochaDocument cabochaDoc = taskAnalyzer.loadUseCaseTask(doc.getFile());
                setWordInfoForCabochaDoc(cabochaDoc.getCompoundWordCountMap(), doc);
                setWordInfoForCabochaDoc(cabochaDoc.getCompoundWordWithNokakuCountMap(), doc);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void setWordInfoForCabochaDoc(Map<String, Integer> map, Document doc) {
        for (Entry<String, Integer> entry : map.entrySet()) {
            String compoundWord = entry.getKey();
            int count = entry.getValue();
            setTermInfoMap(compoundWord, COMPOUND_WORD_JA, doc.getFile(), true);
            TermInfo wordInfo = termInfoMap.get(compoundWord);
            wordInfo.putInputDoc(doc.getFile(), count);
        }
    }

    private void termExtraction(Document doc, boolean isInputDoc) {
        if (doc.getLang().equals("ja")) {
            jaTermExtraction(doc.getText(), doc, isInputDoc);
        } else if (doc.getLang().equals("en")) {
            enTermExtraction(doc.getText(), doc, isInputDoc);
        }
    }

    private Process jaMorphologicalAnalyzerProcess;

    private Process termExtractProcess;

    private Process ssTaggerProcess;

    public static String Japanese_Morphological_Analyzer = "C:/Program Files/Chasen/chasen.exe";

    public static String Japanese_Morphological_Analyzer_CharacterSet = "UTF-8";

    public static String Japanese_Dependency_Structure_Analyzer = "C:/Program Files/CaboCha/bin/cabocha.exe";

    public static String PERL_EXE = "C:/Perl/bin/perl.exe";

    public static String SS_TAGGER_HOME = "C:/DODDLE-OWL/postagger-1.0";

    public static final String RESOURCE_DIR = "jp/ac/keio/ae/comp/yamaguti/doddle/resources/";

    private static String TERM_EXTRACT_CHASEN_EXE = "ex_chasen.pl";

    private static String TERM_EXTRACT_MECAB_EXE = "ex_mecab.pl";

    private static String TERM_EXTRACT_TAGGER_EXE = "ex_brillstagger.pl";

    public static String TERM_EXTRACT_SCRIPTS_DIR = "C:/DODDLE-OWL/TermExtractScripts";

    public static String XDOC2TXT_EXE = "C:/DODDLE-OWL/d2txt123/xdoc2txt.exe";

    public static String STOP_WORD_LIST_FILE = "C:/DODDLE-OWL/stop_word_list.txt";

    public void destroyProcesses() {
        if (jaMorphologicalAnalyzerProcess != null) {
            jaMorphologicalAnalyzerProcess.destroy();
        }
        if (termExtractProcess != null) {
            termExtractProcess.destroy();
        }
        if (ssTaggerProcess != null) {
            ssTaggerProcess.destroy();
        }
        if (taskAnalyzer != null) {
            taskAnalyzer.destroyProcess();
        }
    }

    public Set<String> getGensenCompoundWordSet(String text, String lang) {
        Set<String> wordSet = new HashSet<String>();
        BufferedReader reader = null;
        try {
            if (lang.equals("ja")) {
                reader = getGenSenReader(text);
            } else if (lang.equals("en")) {
                reader = getSSTaggerReader();
            }
            String line = "";
            String splitStr = "\\s+";
            if (lang.equals("en")) {
                splitStr = "\t";
            }
            while ((line = reader.readLine()) != null) {
                String[] lines = line.split(splitStr);
                if (lines.length < 2) {
                    continue;
                }
                String word = lines[0].toLowerCase();
                if (lang.equals("en")) {
                    word = word.replaceAll("\\s+", " ");
                }
                String importance = lines[1];
                if (lang.equals("en") && word.split("\\s+").length == 1) {
                    continue;
                }
                if (!oneWordCheckBox.isSelected() && word.length() == 1) {
                    continue;
                }
                if (showImportanceCheckBox.isSelected()) {
                    wordSet.add(word + "(" + importance + ")");
                } else {
                    wordSet.add(word);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioe2) {
                ioe2.printStackTrace();
            }
            deleteTempFiles();
        }
        return wordSet;
    }

    private BufferedReader getSSTaggerReader() throws IOException {
        String taggerPath = "";
        taggerPath = TERM_EXTRACT_SCRIPTS_DIR + File.separator + TERM_EXTRACT_TAGGER_EXE;
        ProcessBuilder processBuilder = new ProcessBuilder(PERL_EXE, taggerPath, SS_TAGGER_HOME + File.separator + "tmpTagger.txt");
        termExtractProcess = processBuilder.start();
        return new BufferedReader(new InputStreamReader(termExtractProcess.getInputStream(), "UTF-8"));
    }

    private File tmpFile;

    private File tmpJapaneseMorphologicalAnalyzerFile;

    private void deleteTempFiles() {
        if (tmpFile != null) {
            tmpFile.deleteOnExit();
        }
        if (tmpJapaneseMorphologicalAnalyzerFile != null) {
            tmpJapaneseMorphologicalAnalyzerFile.deleteOnExit();
        }
    }

    /**
     * @param text
     * @param rt
     * @return
     * @throws IOException
     */
    private BufferedReader getGenSenReader(String text) throws IOException {
        tmpFile = File.createTempFile("tmp", null);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tmpFile), Japanese_Morphological_Analyzer_CharacterSet));
        bw.write(text);
        bw.close();
        tmpJapaneseMorphologicalAnalyzerFile = File.createTempFile("tmpJpMorphologicalAnalyzer", null);
        ProcessBuilder processBuilder = null;
        if (Japanese_Morphological_Analyzer.matches(".*mecab.*")) {
            processBuilder = new ProcessBuilder(Japanese_Morphological_Analyzer, "-o", tmpJapaneseMorphologicalAnalyzerFile.getAbsolutePath(), tmpFile.getAbsolutePath());
        } else {
            processBuilder = new ProcessBuilder(Japanese_Morphological_Analyzer, "-i", "w", "-o", tmpJapaneseMorphologicalAnalyzerFile.getAbsolutePath(), tmpFile.getAbsolutePath());
        }
        jaMorphologicalAnalyzerProcess = processBuilder.start();
        String path = "";
        String TERM_EXTRACT_EXE = TERM_EXTRACT_CHASEN_EXE;
        if (Japanese_Morphological_Analyzer.matches(".*mecab.*")) {
            TERM_EXTRACT_EXE = TERM_EXTRACT_MECAB_EXE;
        }
        path = TERM_EXTRACT_SCRIPTS_DIR + File.separator + TERM_EXTRACT_EXE;
        processBuilder = new ProcessBuilder(PERL_EXE, path, tmpJapaneseMorphologicalAnalyzerFile.getAbsolutePath());
        termExtractProcess = processBuilder.start();
        return new BufferedReader(new InputStreamReader(termExtractProcess.getInputStream(), Japanese_Morphological_Analyzer_CharacterSet));
    }

    private boolean isJaNoun(String pos) {
        return pos.indexOf("名詞") == 0;
    }

    private boolean isJaVerb(String pos) {
        return pos.indexOf("動詞") == 0;
    }

    private boolean isJaOther(String pos) {
        return !(isJaNoun(pos) || isJaVerb(pos));
    }

    private boolean isEnNoun(String pos) {
        return pos.indexOf("NN") != -1;
    }

    private boolean isEnVerb(String pos) {
        return pos.indexOf("VB") != -1;
    }

    private boolean isEnOther(String pos) {
        return !(isEnNoun(pos) || isEnVerb(pos));
    }

    private void setUpperConcept() {
        if (!UpperConceptManager.hasUpperConceptLabelSet()) {
            return;
        }
        for (Entry<String, TermInfo> entry : termInfoMap.entrySet()) {
            String term = entry.getKey();
            TermInfo info = entry.getValue();
            Set<String> upperConceptLabelSet = UpperConceptManager.getUpperConceptLabelSet(term);
            if (info != null) {
                for (String ucLabel : upperConceptLabelSet) {
                    info.addUpperConcept(ucLabel);
                }
            }
        }
    }

    class TermExtractionWorker extends SwingWorker implements PropertyChangeListener {

        private int currentTaskCnt;

        public TermExtractionWorker(int taskCnt) {
            currentTaskCnt = 1;
            DODDLE.STATUS_BAR.setLastMessage(Translator.getTerm("InputTermExtractionButton"));
            DODDLE.STATUS_BAR.startTime();
            DODDLE.STATUS_BAR.initNormal(taskCnt);
            DODDLE.STATUS_BAR.lock();
            addPropertyChangeListener(this);
            setStopWordSet();
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                taskAnalyzer = new TaskAnalyzer();
                UpperConceptManager.makeUpperOntologyList();
                setProgress(currentTaskCnt++);
                termInfoMap.clear();
                ListModel listModel = inputDocList.getModel();
                for (int i = 0; i < listModel.getSize(); i++) {
                    Document doc = (Document) listModel.getElementAt(i);
                    DODDLE.STATUS_BAR.setLastMessage(doc.getFile().getName());
                    termExtraction(doc, true);
                }
                listModel = docList.getModel();
                for (int i = 0; i < listModel.getSize(); i++) {
                    Document doc = (Document) listModel.getElementAt(i);
                    DODDLE.STATUS_BAR.setLastMessage(doc.getFile().getName());
                    termExtraction(doc, false);
                }
                setProgress(currentTaskCnt++);
                setUpperConcept();
                removeDocWordSet();
                int docNum = docList.getModel().getSize() + inputDocList.getModel().getSize();
                inputTermSelectionPanel.setInputTermInfoTableModel(termInfoMap, docNum);
                inputTermSelectionPanel.setInputDocumentListModel(inputDocList.getModel());
                setProgress(currentTaskCnt++);
                DODDLE.setSelectedIndex(DODDLEConstants.INPUT_WORD_SELECTION_PANEL);
                setProgress(currentTaskCnt++);
            } finally {
                DODDLE.STATUS_BAR.setLastMessage(Translator.getTerm("InputTermExtractionDoneMessage"));
                DODDLE.STATUS_BAR.unLock();
                DODDLE.STATUS_BAR.hideProgressBar();
                project.addLog("TermExtractionButton");
            }
            return "done";
        }

        @Override
        public void done() {
            destroyProcesses();
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() instanceof Integer) {
                DODDLE.STATUS_BAR.setValue(currentTaskCnt);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == termExtractionButton) {
            destroyProcesses();
            TermExtractionWorker worker = new TermExtractionWorker(4);
            DODDLE.STATUS_BAR.setSwingWorker(worker);
            worker.execute();
        } else if (e.getSource() == docLangBox) {
            if (docList.getSelectedValues().length == 1) {
                String lang = (String) docLangBox.getSelectedItem();
                Document doc = (Document) docList.getSelectedValue();
                doc.setLang(lang);
                updateUI();
            }
        } else if (e.getSource() == inputDocLangBox) {
            if (inputDocList.getSelectedValues().length == 1) {
                String lang = (String) inputDocLangBox.getSelectedItem();
                Document doc = (Document) inputDocList.getSelectedValue();
                doc.setLang(lang);
                updateUI();
            }
        } else if (e.getSource() == setPunctuationButton) {
            PUNCTUATION_CHARS = punctuationField.getText();
            ListModel inputDocModel = inputDocList.getModel();
            for (int i = 0; i < inputDocModel.getSize(); i++) {
                Document doc = (Document) inputDocModel.getElementAt(i);
                doc.resetText();
            }
            Document doc = (Document) inputDocList.getSelectedValue();
            if (doc != null) {
                inputDocArea.setText(doc.getText());
                inputDocArea.setCaretPosition(0);
                docLangBox.setSelectedItem(doc.getLang());
            }
            updateUI();
        }
    }

    private void removeDocWordSet() {
        Set<String> docWordSet = new HashSet<String>();
        for (TermInfo info : termInfoMap.values()) {
            if (!info.isInputWord()) {
                docWordSet.add(info.getTerm());
            }
        }
        for (String dw : docWordSet) {
            termInfoMap.remove(dw);
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == inputDocList && inputDocList.getSelectedValues().length == 1) {
            Document doc = (Document) inputDocList.getSelectedValue();
            inputDocArea.setText(doc.getText());
            inputDocArea.setCaretPosition(0);
            inputDocLangBox.setSelectedItem(doc.getLang());
        } else if (e.getSource() == docList && docList.getSelectedValues().length == 1) {
            Document doc = (Document) docList.getSelectedValue();
            inputDocArea.setText(doc.getText());
            inputDocArea.setCaretPosition(0);
            docLangBox.setSelectedItem(doc.getLang());
        }
    }

    private Set getFiles(File[] files, Set fileSet) {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile()) {
                fileSet.add(file);
            } else if (file.isDirectory()) {
                getFiles(file.listFiles(), fileSet);
            }
        }
        return fileSet;
    }

    private Set getFiles() {
        JFileChooser chooser = new JFileChooser(DODDLEConstants.PROJECT_HOME);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int retval = chooser.showOpenDialog(DODDLE.rootPane);
        if (retval != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File[] files = chooser.getSelectedFiles();
        Set fileSet = new TreeSet();
        getFiles(files, fileSet);
        return fileSet;
    }

    public String getTargetTextLines(String word) {
        StringWriter writer = new StringWriter();
        ListModel listModel = inputDocList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            Document doc = (Document) listModel.getElementAt(i);
            String text = doc.getText();
            if (text != null) {
                writer.write("[ " + doc.getFile().getAbsolutePath() + " ]\n");
                String[] lines = text.split("\n");
                for (int j = 0; j < lines.length; j++) {
                    String line = lines[j];
                    if (line.indexOf(word) != -1) {
                        writer.write((j + 1) + ": " + line + "\n");
                    }
                }
                writer.write("\n");
            }
        }
        return writer.toString();
    }

    public String getTargetHtmlLines(String word) {
        StringWriter writer = new StringWriter();
        writer.write("<html><body>");
        ListModel listModel = inputDocList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            Document doc = (Document) listModel.getElementAt(i);
            String text = doc.getText();
            StringBuilder buf = new StringBuilder();
            if (text != null) {
                String[] lines = text.split("\n");
                for (int j = 0; j < lines.length; j++) {
                    String line = lines[j];
                    if (line.matches(".*" + word + ".*")) {
                        line = line.replaceAll(word, "<b><font size=3 color=red>" + word + "</font></b>");
                        buf.append("<b><font size=3 color=navy>");
                        if (DODDLEConstants.LANG.equals("en")) {
                            buf.append(Translator.getTerm("LineMessage"));
                            buf.append(" ");
                            buf.append((j + 1));
                        } else {
                            buf.append((j + 1));
                            buf.append(Translator.getTerm("LineMessage"));
                        }
                        buf.append(": </font></b>");
                        buf.append("<font size=3>");
                        buf.append(line);
                        buf.append("</font>");
                        buf.append("<br>");
                    }
                }
            }
            if (0 < buf.toString().length()) {
                writer.write("<font size=3><b>" + doc.getFile().getAbsolutePath() + "</b></font><br>");
                writer.write(buf.toString());
            }
        }
        writer.write("</body></html>");
        return writer.toString();
    }

    private void addDocuments(JList list, Set fileSet) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        for (Iterator i = fileSet.iterator(); i.hasNext(); ) {
            File file = (File) i.next();
            Document doc = new Document(file);
            String text = doc.getText();
            if (30 < text.split(" ").length) {
                doc.setLang("en");
            }
            model.addElement(doc);
        }
    }

    class AddDocAction extends AbstractAction {

        public AddDocAction(String title) {
            super(title);
        }

        public void actionPerformed(ActionEvent e) {
            Set fileSet = getFiles();
            if (fileSet == null) {
                return;
            }
            addDocuments(docList, fileSet);
        }
    }

    class AddInputDocAction extends AbstractAction {

        public AddInputDocAction(String title) {
            super(title, addDocIcon);
        }

        public void actionPerformed(ActionEvent e) {
            Set fileSet = getFiles();
            if (fileSet == null) {
                return;
            }
            addDocuments(inputDocList, fileSet);
            project.getConceptDefinitionPanel().setInputDocList();
            project.addLog("AddInputDocumentButton");
        }
    }

    class RemoveDocAction extends AbstractAction {

        public RemoveDocAction(String title) {
            super(title);
        }

        public void actionPerformed(ActionEvent e) {
            Object[] removeElements = docList.getSelectedValues();
            DefaultListModel model = (DefaultListModel) docList.getModel();
            for (int i = 0; i < removeElements.length; i++) {
                model.removeElement(removeElements[i]);
            }
            inputDocArea.setText("");
        }
    }

    class RemoveInputDocAction extends AbstractAction {

        public RemoveInputDocAction(String title) {
            super(title, removeDocIcon);
        }

        public void actionPerformed(ActionEvent e) {
            Object[] removeElements = inputDocList.getSelectedValues();
            DefaultListModel model = (DefaultListModel) inputDocList.getModel();
            for (int i = 0; i < removeElements.length; i++) {
                model.removeElement(removeElements[i]);
            }
            inputDocArea.setText("");
            project.getConceptDefinitionPanel().setInputDocList();
            project.addLog("RemoveInputDocumentButton");
        }
    }

    class EditInputDocAction extends AbstractAction {

        public EditInputDocAction(String title) {
            super(title);
        }

        public void actionPerformed(ActionEvent e) {
            Document doc = (Document) inputDocList.getSelectedValue();
            doc.setText(inputDocArea.getText());
            project.addLog("Edit");
        }
    }

    public Set<Document> getDocSet() {
        TreeSet<Document> docSet = new TreeSet<Document>();
        ListModel listModel = inputDocList.getModel();
        for (int i = 0; i < listModel.getSize(); i++) {
            Document doc = (Document) listModel.getElementAt(i);
            docSet.add(doc);
        }
        return docSet;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DODDLEConstants.EDR_HOME = "C:/usr/eclipse_workspace/DODDLE_DIC/";
        frame.getContentPane().add(new InputDocumentSelectionPanel(null, null), BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}
