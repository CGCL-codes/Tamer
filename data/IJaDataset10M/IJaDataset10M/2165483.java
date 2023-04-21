package is.iclt.icenlp.IceParser;

import is.iclt.icenlp.common.configuration.Configuration;
import is.iclt.icenlp.core.utils.Word;
import is.iclt.icenlp.facade.IceParserFacade;
import java.io.IOException;
import java.util.List;

public class IceParser implements IIceParser {

    private static IceParser instance_ = null;

    private IceParserFacade parser;

    private boolean include_functions = true;

    private boolean phrase_per_line = true;

    private String mark_subject_left = "<@←SUBJ>";

    private String mark_subject_right = "<@SUBJ→>";

    private String mark_obj_left = "<@←OBJ>";

    private String mark_obj_right = "<@OBJ→>";

    private String parsedString;

    private String value_IceParserOutput = "";

    public static synchronized IceParser instance() {
        if (instance_ == null) instance_ = new IceParser();
        return instance_;
    }

    public String getParsedString() {
        return parsedString;
    }

    protected IceParser() {
        String value_mark_subject_left = Configuration.getInstance().getValue("mark_subject_left");
        if (value_mark_subject_left != null) {
            this.mark_subject_left = value_mark_subject_left;
        }
        String value_mark_subject_right = Configuration.getInstance().getValue("mark_subject_right");
        if (value_mark_subject_right != null) {
            this.mark_subject_right = value_mark_subject_right;
        }
        String value_mark_obj_right = Configuration.getInstance().getValue("mark_obj_right");
        if (value_mark_subject_right != null) {
            this.mark_obj_right = value_mark_obj_right;
        }
        String value_mark_obj_left = Configuration.getInstance().getValue("mark_obj_left");
        if (value_mark_subject_right != null) {
            this.mark_obj_left = value_mark_obj_left;
        }
        if (Configuration.getInstance().getValue("IceParserOutput").toLowerCase().equals("tcf")) {
            value_IceParserOutput = "tcf";
        } else if (Configuration.getInstance().getValue("IceParserOutput").toLowerCase().equals("xml")) {
            value_IceParserOutput = "xml";
        } else if (Configuration.getInstance().getValue("IceParserOutput").toLowerCase().equals("alt")) {
            if (Configuration.getInstance().getValue("IceParserOutputTMP").equals("xml")) {
                value_IceParserOutput = "xml";
            } else if (Configuration.getInstance().getValue("IceParserOutputTMP").equals("tcf")) {
                value_IceParserOutput = "tcf";
            } else if (Configuration.getInstance().getValue("IceParserOutputTMP").equals("txt")) {
                value_IceParserOutput = "txt";
            }
        }
        System.out.println("[i] IceParser instance created.");
        parser = new IceParserFacade();
    }

    public String parse(String text) {
        try {
            return parser.parse(text, include_functions, "one_phrase_per_line", false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String parse(List<Word> words) {
        String taggedString = this.getTagStr(words);
        try {
            boolean merge = false;
            boolean error = false;
            if (Configuration.getInstance().getValue("IceParserOutput").equals("alt")) {
                value_IceParserOutput = Configuration.getInstance().getValue("IceParserOutputTMP");
                if (Configuration.getInstance().getValue("IceParserOutputError").equals("true")) {
                    error = true;
                }
                ;
                if (Configuration.getInstance().getValue("IceParserOutputMerge").equals("true")) {
                    merge = true;
                }
                ;
            }
            System.out.println("gDB>>taggedString=(" + taggedString + ")");
            String strParse = this.parser.parse(taggedString, include_functions, value_IceParserOutput, error, merge);
            if (value_IceParserOutput.equals("txt")) {
                parsedString = strParse.replaceAll("\\.\\ \\.", ". .\n");
                return null;
            } else if (value_IceParserOutput.equals("t1l") || value_IceParserOutput.equals("tcf") || value_IceParserOutput.equals("xml")) {
                parsedString = strParse;
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTagStr(List<Word> wordList) {
        StringBuilder strBuilder = new StringBuilder();
        for (Word w : wordList) {
            strBuilder.append(w.getLexeme() + " " + w.getTag() + " ");
        }
        return strBuilder.toString();
    }
}
