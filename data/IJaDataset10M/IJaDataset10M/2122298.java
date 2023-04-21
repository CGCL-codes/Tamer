package net.sf.devtool.casaapp.parser.antlr.internal;

import java.io.InputStream;
import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.xtext.parsetree.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import org.eclipse.xtext.conversion.ValueConverterException;
import net.sf.devtool.casaapp.services.GuiDSLGrammarAccess;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class InternalGuiDSLParser extends AbstractInternalAntlrParser {

    public static final String[] tokenNames = new String[] { "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_STRING", "RULE_ID", "RULE_HALIGNMENT", "RULE_INT", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_WS", "RULE_ANY_OTHER", "'import'", "'screen'", "'with'", "'kind'", "'list'", "'{'", "'}'", "'formular'", "'label'", "'['", "']'", "'field'", "'text'", "'='", "'halign'", "'fieldref'", "'input'" };

    public static final int RULE_ID = 5;

    public static final int RULE_STRING = 4;

    public static final int RULE_ANY_OTHER = 11;

    public static final int RULE_HALIGNMENT = 6;

    public static final int RULE_INT = 7;

    public static final int RULE_WS = 10;

    public static final int RULE_SL_COMMENT = 9;

    public static final int EOF = -1;

    public static final int RULE_ML_COMMENT = 8;

    public InternalGuiDSLParser(TokenStream input) {
        super(input);
    }

    public String[] getTokenNames() {
        return tokenNames;
    }

    public String getGrammarFileName() {
        return "../net.sf.devtool.casaapp/src-gen/net/sf/devtool/casaapp/parser/antlr/internal/InternalGuiDSL.g";
    }

    private GuiDSLGrammarAccess grammarAccess;

    public InternalGuiDSLParser(TokenStream input, IAstFactory factory, GuiDSLGrammarAccess grammarAccess) {
        this(input);
        this.factory = factory;
        registerRules(grammarAccess.getGrammar());
        this.grammarAccess = grammarAccess;
    }

    @Override
    protected InputStream getTokenFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream("net/sf/devtool/casaapp/parser/antlr/internal/InternalGuiDSL.tokens");
    }

    @Override
    protected String getFirstRuleName() {
        return "Model";
    }

    public final EObject entryRuleModel() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleModel = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getModelRule(), currentNode);
                pushFollow(FOLLOW_ruleModel_in_entryRuleModel73);
                iv_ruleModel = ruleModel();
                _fsp--;
                current = iv_ruleModel;
                match(input, EOF, FOLLOW_EOF_in_entryRuleModel83);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleModel() throws RecognitionException {
        EObject current = null;
        EObject lv_imports_0 = null;
        EObject lv_screens_1 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    loop1: do {
                        int alt1 = 2;
                        int LA1_0 = input.LA(1);
                        if ((LA1_0 == 12)) {
                            alt1 = 1;
                        }
                        switch(alt1) {
                            case 1:
                                {
                                    currentNode = createCompositeNode(grammarAccess.getModelAccess().getImportsImportParserRuleCall_0_0(), currentNode);
                                    pushFollow(FOLLOW_ruleImport_in_ruleModel142);
                                    lv_imports_0 = ruleImport();
                                    _fsp--;
                                    if (current == null) {
                                        current = factory.create(grammarAccess.getModelRule().getType().getClassifier());
                                        associateNodeWithAstElement(currentNode.getParent(), current);
                                    }
                                    try {
                                        add(current, "imports", lv_imports_0, "Import", currentNode);
                                    } catch (ValueConverterException vce) {
                                        handleValueConverterException(vce);
                                    }
                                    currentNode = currentNode.getParent();
                                }
                                break;
                            default:
                                break loop1;
                        }
                    } while (true);
                    loop2: do {
                        int alt2 = 2;
                        int LA2_0 = input.LA(1);
                        if ((LA2_0 == 13)) {
                            alt2 = 1;
                        }
                        switch(alt2) {
                            case 1:
                                {
                                    currentNode = createCompositeNode(grammarAccess.getModelAccess().getScreensScreenParserRuleCall_1_0(), currentNode);
                                    pushFollow(FOLLOW_ruleScreen_in_ruleModel181);
                                    lv_screens_1 = ruleScreen();
                                    _fsp--;
                                    if (current == null) {
                                        current = factory.create(grammarAccess.getModelRule().getType().getClassifier());
                                        associateNodeWithAstElement(currentNode.getParent(), current);
                                    }
                                    try {
                                        add(current, "screens", lv_screens_1, "Screen", currentNode);
                                    } catch (ValueConverterException vce) {
                                        handleValueConverterException(vce);
                                    }
                                    currentNode = currentNode.getParent();
                                }
                                break;
                            default:
                                break loop2;
                        }
                    } while (true);
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleImport() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleImport = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getImportRule(), currentNode);
                pushFollow(FOLLOW_ruleImport_in_entryRuleImport219);
                iv_ruleImport = ruleImport();
                _fsp--;
                current = iv_ruleImport;
                match(input, EOF, FOLLOW_EOF_in_entryRuleImport229);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleImport() throws RecognitionException {
        EObject current = null;
        Token lv_scriptURI_1 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 12, FOLLOW_12_in_ruleImport263);
                    createLeafNode(grammarAccess.getImportAccess().getImportKeyword_0(), null);
                    {
                        lv_scriptURI_1 = (Token) input.LT(1);
                        match(input, RULE_STRING, FOLLOW_RULE_STRING_in_ruleImport285);
                        createLeafNode(grammarAccess.getImportAccess().getScriptURISTRINGTerminalRuleCall_1_0(), "scriptURI");
                        if (current == null) {
                            current = factory.create(grammarAccess.getImportRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "scriptURI", lv_scriptURI_1, "STRING", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleScreen() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleScreen = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getScreenRule(), currentNode);
                pushFollow(FOLLOW_ruleScreen_in_entryRuleScreen326);
                iv_ruleScreen = ruleScreen();
                _fsp--;
                current = iv_ruleScreen;
                match(input, EOF, FOLLOW_EOF_in_entryRuleScreen336);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleScreen() throws RecognitionException {
        EObject current = null;
        Token lv_name_1 = null;
        EObject lv_screenkind_4 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 13, FOLLOW_13_in_ruleScreen370);
                    createLeafNode(grammarAccess.getScreenAccess().getScreenKeyword_0(), null);
                    {
                        lv_name_1 = (Token) input.LT(1);
                        match(input, RULE_ID, FOLLOW_RULE_ID_in_ruleScreen392);
                        createLeafNode(grammarAccess.getScreenAccess().getNameIDTerminalRuleCall_1_0(), "name");
                        if (current == null) {
                            current = factory.create(grammarAccess.getScreenRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "name", lv_name_1, "ID", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                    match(input, 14, FOLLOW_14_in_ruleScreen409);
                    createLeafNode(grammarAccess.getScreenAccess().getWithKeyword_2(), null);
                    match(input, 15, FOLLOW_15_in_ruleScreen418);
                    createLeafNode(grammarAccess.getScreenAccess().getKindKeyword_3(), null);
                    {
                        currentNode = createCompositeNode(grammarAccess.getScreenAccess().getScreenkindScreenKindParserRuleCall_4_0(), currentNode);
                        pushFollow(FOLLOW_ruleScreenKind_in_ruleScreen452);
                        lv_screenkind_4 = ruleScreenKind();
                        _fsp--;
                        if (current == null) {
                            current = factory.create(grammarAccess.getScreenRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode.getParent(), current);
                        }
                        try {
                            set(current, "screenkind", lv_screenkind_4, "ScreenKind", currentNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                        currentNode = currentNode.getParent();
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleScreenKind() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleScreenKind = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getScreenKindRule(), currentNode);
                pushFollow(FOLLOW_ruleScreenKind_in_entryRuleScreenKind489);
                iv_ruleScreenKind = ruleScreenKind();
                _fsp--;
                current = iv_ruleScreenKind;
                match(input, EOF, FOLLOW_EOF_in_entryRuleScreenKind499);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleScreenKind() throws RecognitionException {
        EObject current = null;
        EObject this_List_0 = null;
        EObject this_Formular_1 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                int alt3 = 2;
                int LA3_0 = input.LA(1);
                if ((LA3_0 == 16)) {
                    alt3 = 1;
                } else if ((LA3_0 == 19)) {
                    alt3 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("268:1: (this_List_0= ruleList | this_Formular_1= ruleFormular )", 3, 0, input);
                    throw nvae;
                }
                switch(alt3) {
                    case 1:
                        {
                            currentNode = createCompositeNode(grammarAccess.getScreenKindAccess().getListParserRuleCall_0(), currentNode);
                            pushFollow(FOLLOW_ruleList_in_ruleScreenKind546);
                            this_List_0 = ruleList();
                            _fsp--;
                            current = this_List_0;
                            currentNode = currentNode.getParent();
                        }
                        break;
                    case 2:
                        {
                            currentNode = createCompositeNode(grammarAccess.getScreenKindAccess().getFormularParserRuleCall_1(), currentNode);
                            pushFollow(FOLLOW_ruleFormular_in_ruleScreenKind573);
                            this_Formular_1 = ruleFormular();
                            _fsp--;
                            current = this_Formular_1;
                            currentNode = currentNode.getParent();
                        }
                        break;
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleList() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleList = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getListRule(), currentNode);
                pushFollow(FOLLOW_ruleList_in_entryRuleList605);
                iv_ruleList = ruleList();
                _fsp--;
                current = iv_ruleList;
                match(input, EOF, FOLLOW_EOF_in_entryRuleList615);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleList() throws RecognitionException {
        EObject current = null;
        EObject lv_guiElements_2 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 16, FOLLOW_16_in_ruleList649);
                    createLeafNode(grammarAccess.getListAccess().getListKeyword_0(), null);
                    match(input, 17, FOLLOW_17_in_ruleList658);
                    createLeafNode(grammarAccess.getListAccess().getLeftCurlyBracketKeyword_1(), null);
                    int cnt4 = 0;
                    loop4: do {
                        int alt4 = 2;
                        int LA4_0 = input.LA(1);
                        if ((LA4_0 == 20 || LA4_0 == 23)) {
                            alt4 = 1;
                        }
                        switch(alt4) {
                            case 1:
                                {
                                    currentNode = createCompositeNode(grammarAccess.getListAccess().getGuiElementsListGuiElementParserRuleCall_2_0(), currentNode);
                                    pushFollow(FOLLOW_ruleListGuiElement_in_ruleList692);
                                    lv_guiElements_2 = ruleListGuiElement();
                                    _fsp--;
                                    if (current == null) {
                                        current = factory.create(grammarAccess.getListRule().getType().getClassifier());
                                        associateNodeWithAstElement(currentNode.getParent(), current);
                                    }
                                    try {
                                        add(current, "guiElements", lv_guiElements_2, "ListGuiElement", currentNode);
                                    } catch (ValueConverterException vce) {
                                        handleValueConverterException(vce);
                                    }
                                    currentNode = currentNode.getParent();
                                }
                                break;
                            default:
                                if (cnt4 >= 1) break loop4;
                                EarlyExitException eee = new EarlyExitException(4, input);
                                throw eee;
                        }
                        cnt4++;
                    } while (true);
                    match(input, 18, FOLLOW_18_in_ruleList706);
                    createLeafNode(grammarAccess.getListAccess().getRightCurlyBracketKeyword_3(), null);
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleFormular() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleFormular = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getFormularRule(), currentNode);
                pushFollow(FOLLOW_ruleFormular_in_entryRuleFormular739);
                iv_ruleFormular = ruleFormular();
                _fsp--;
                current = iv_ruleFormular;
                match(input, EOF, FOLLOW_EOF_in_entryRuleFormular749);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleFormular() throws RecognitionException {
        EObject current = null;
        Token lv_name_1 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 19, FOLLOW_19_in_ruleFormular783);
                    createLeafNode(grammarAccess.getFormularAccess().getFormularKeyword_0(), null);
                    {
                        lv_name_1 = (Token) input.LT(1);
                        match(input, RULE_ID, FOLLOW_RULE_ID_in_ruleFormular805);
                        createLeafNode(grammarAccess.getFormularAccess().getNameIDTerminalRuleCall_1_0(), "name");
                        if (current == null) {
                            current = factory.create(grammarAccess.getFormularRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "name", lv_name_1, "ID", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleListGuiElement() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleListGuiElement = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getListGuiElementRule(), currentNode);
                pushFollow(FOLLOW_ruleListGuiElement_in_entryRuleListGuiElement846);
                iv_ruleListGuiElement = ruleListGuiElement();
                _fsp--;
                current = iv_ruleListGuiElement;
                match(input, EOF, FOLLOW_EOF_in_entryRuleListGuiElement856);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleListGuiElement() throws RecognitionException {
        EObject current = null;
        EObject this_Label_0 = null;
        EObject this_Field_1 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                int alt5 = 2;
                int LA5_0 = input.LA(1);
                if ((LA5_0 == 20)) {
                    alt5 = 1;
                } else if ((LA5_0 == 23)) {
                    alt5 = 2;
                } else {
                    NoViableAltException nvae = new NoViableAltException("407:1: (this_Label_0= ruleLabel | this_Field_1= ruleField )", 5, 0, input);
                    throw nvae;
                }
                switch(alt5) {
                    case 1:
                        {
                            currentNode = createCompositeNode(grammarAccess.getListGuiElementAccess().getLabelParserRuleCall_0(), currentNode);
                            pushFollow(FOLLOW_ruleLabel_in_ruleListGuiElement903);
                            this_Label_0 = ruleLabel();
                            _fsp--;
                            current = this_Label_0;
                            currentNode = currentNode.getParent();
                        }
                        break;
                    case 2:
                        {
                            currentNode = createCompositeNode(grammarAccess.getListGuiElementAccess().getFieldParserRuleCall_1(), currentNode);
                            pushFollow(FOLLOW_ruleField_in_ruleListGuiElement930);
                            this_Field_1 = ruleField();
                            _fsp--;
                            current = this_Field_1;
                            currentNode = currentNode.getParent();
                        }
                        break;
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleLabel() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleLabel = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getLabelRule(), currentNode);
                pushFollow(FOLLOW_ruleLabel_in_entryRuleLabel962);
                iv_ruleLabel = ruleLabel();
                _fsp--;
                current = iv_ruleLabel;
                match(input, EOF, FOLLOW_EOF_in_entryRuleLabel972);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleLabel() throws RecognitionException {
        EObject current = null;
        Token lv_name_1 = null;
        EObject lv_text_3 = null;
        EObject lv_halign_4 = null;
        EObject lv_fieldref_5 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 20, FOLLOW_20_in_ruleLabel1006);
                    createLeafNode(grammarAccess.getLabelAccess().getLabelKeyword_0(), null);
                    {
                        lv_name_1 = (Token) input.LT(1);
                        match(input, RULE_ID, FOLLOW_RULE_ID_in_ruleLabel1028);
                        createLeafNode(grammarAccess.getLabelAccess().getNameIDTerminalRuleCall_1_0(), "name");
                        if (current == null) {
                            current = factory.create(grammarAccess.getLabelRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "name", lv_name_1, "ID", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                    match(input, 21, FOLLOW_21_in_ruleLabel1045);
                    createLeafNode(grammarAccess.getLabelAccess().getLeftSquareBracketKeyword_2(), null);
                    int alt6 = 2;
                    int LA6_0 = input.LA(1);
                    if ((LA6_0 == 24)) {
                        alt6 = 1;
                    }
                    switch(alt6) {
                        case 1:
                            {
                                currentNode = createCompositeNode(grammarAccess.getLabelAccess().getTextTextPropertyParserRuleCall_3_0(), currentNode);
                                pushFollow(FOLLOW_ruleTextProperty_in_ruleLabel1079);
                                lv_text_3 = ruleTextProperty();
                                _fsp--;
                                if (current == null) {
                                    current = factory.create(grammarAccess.getLabelRule().getType().getClassifier());
                                    associateNodeWithAstElement(currentNode.getParent(), current);
                                }
                                try {
                                    set(current, "text", lv_text_3, "TextProperty", currentNode);
                                } catch (ValueConverterException vce) {
                                    handleValueConverterException(vce);
                                }
                                currentNode = currentNode.getParent();
                            }
                            break;
                    }
                    int alt7 = 2;
                    int LA7_0 = input.LA(1);
                    if ((LA7_0 == 26)) {
                        alt7 = 1;
                    }
                    switch(alt7) {
                        case 1:
                            {
                                currentNode = createCompositeNode(grammarAccess.getLabelAccess().getHalignHAlignPropertyParserRuleCall_4_0(), currentNode);
                                pushFollow(FOLLOW_ruleHAlignProperty_in_ruleLabel1118);
                                lv_halign_4 = ruleHAlignProperty();
                                _fsp--;
                                if (current == null) {
                                    current = factory.create(grammarAccess.getLabelRule().getType().getClassifier());
                                    associateNodeWithAstElement(currentNode.getParent(), current);
                                }
                                try {
                                    set(current, "halign", lv_halign_4, "HAlignProperty", currentNode);
                                } catch (ValueConverterException vce) {
                                    handleValueConverterException(vce);
                                }
                                currentNode = currentNode.getParent();
                            }
                            break;
                    }
                    int alt8 = 2;
                    int LA8_0 = input.LA(1);
                    if ((LA8_0 == 27)) {
                        alt8 = 1;
                    }
                    switch(alt8) {
                        case 1:
                            {
                                currentNode = createCompositeNode(grammarAccess.getLabelAccess().getFieldrefFieldRefParserRuleCall_5_0(), currentNode);
                                pushFollow(FOLLOW_ruleFieldRef_in_ruleLabel1157);
                                lv_fieldref_5 = ruleFieldRef();
                                _fsp--;
                                if (current == null) {
                                    current = factory.create(grammarAccess.getLabelRule().getType().getClassifier());
                                    associateNodeWithAstElement(currentNode.getParent(), current);
                                }
                                try {
                                    set(current, "fieldref", lv_fieldref_5, "FieldRef", currentNode);
                                } catch (ValueConverterException vce) {
                                    handleValueConverterException(vce);
                                }
                                currentNode = currentNode.getParent();
                            }
                            break;
                    }
                    match(input, 22, FOLLOW_22_in_ruleLabel1171);
                    createLeafNode(grammarAccess.getLabelAccess().getRightSquareBracketKeyword_6(), null);
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleField() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleField = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getFieldRule(), currentNode);
                pushFollow(FOLLOW_ruleField_in_entryRuleField1204);
                iv_ruleField = ruleField();
                _fsp--;
                current = iv_ruleField;
                match(input, EOF, FOLLOW_EOF_in_entryRuleField1214);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleField() throws RecognitionException {
        EObject current = null;
        Token lv_name_1 = null;
        EObject lv_input_3 = null;
        EObject lv_halign_4 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 23, FOLLOW_23_in_ruleField1248);
                    createLeafNode(grammarAccess.getFieldAccess().getFieldKeyword_0(), null);
                    {
                        lv_name_1 = (Token) input.LT(1);
                        match(input, RULE_ID, FOLLOW_RULE_ID_in_ruleField1270);
                        createLeafNode(grammarAccess.getFieldAccess().getNameIDTerminalRuleCall_1_0(), "name");
                        if (current == null) {
                            current = factory.create(grammarAccess.getFieldRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "name", lv_name_1, "ID", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                    match(input, 21, FOLLOW_21_in_ruleField1287);
                    createLeafNode(grammarAccess.getFieldAccess().getLeftSquareBracketKeyword_2(), null);
                    int alt9 = 2;
                    int LA9_0 = input.LA(1);
                    if ((LA9_0 == 28)) {
                        alt9 = 1;
                    }
                    switch(alt9) {
                        case 1:
                            {
                                currentNode = createCompositeNode(grammarAccess.getFieldAccess().getInputInputPropertyParserRuleCall_3_0(), currentNode);
                                pushFollow(FOLLOW_ruleInputProperty_in_ruleField1321);
                                lv_input_3 = ruleInputProperty();
                                _fsp--;
                                if (current == null) {
                                    current = factory.create(grammarAccess.getFieldRule().getType().getClassifier());
                                    associateNodeWithAstElement(currentNode.getParent(), current);
                                }
                                try {
                                    set(current, "input", lv_input_3, "InputProperty", currentNode);
                                } catch (ValueConverterException vce) {
                                    handleValueConverterException(vce);
                                }
                                currentNode = currentNode.getParent();
                            }
                            break;
                    }
                    int alt10 = 2;
                    int LA10_0 = input.LA(1);
                    if ((LA10_0 == 26)) {
                        alt10 = 1;
                    }
                    switch(alt10) {
                        case 1:
                            {
                                currentNode = createCompositeNode(grammarAccess.getFieldAccess().getHalignHAlignPropertyParserRuleCall_4_0(), currentNode);
                                pushFollow(FOLLOW_ruleHAlignProperty_in_ruleField1360);
                                lv_halign_4 = ruleHAlignProperty();
                                _fsp--;
                                if (current == null) {
                                    current = factory.create(grammarAccess.getFieldRule().getType().getClassifier());
                                    associateNodeWithAstElement(currentNode.getParent(), current);
                                }
                                try {
                                    set(current, "halign", lv_halign_4, "HAlignProperty", currentNode);
                                } catch (ValueConverterException vce) {
                                    handleValueConverterException(vce);
                                }
                                currentNode = currentNode.getParent();
                            }
                            break;
                    }
                    match(input, 22, FOLLOW_22_in_ruleField1374);
                    createLeafNode(grammarAccess.getFieldAccess().getRightSquareBracketKeyword_5(), null);
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleTextProperty() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleTextProperty = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getTextPropertyRule(), currentNode);
                pushFollow(FOLLOW_ruleTextProperty_in_entryRuleTextProperty1407);
                iv_ruleTextProperty = ruleTextProperty();
                _fsp--;
                current = iv_ruleTextProperty;
                match(input, EOF, FOLLOW_EOF_in_entryRuleTextProperty1417);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleTextProperty() throws RecognitionException {
        EObject current = null;
        Token lv_value_2 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 24, FOLLOW_24_in_ruleTextProperty1451);
                    createLeafNode(grammarAccess.getTextPropertyAccess().getTextKeyword_0(), null);
                    match(input, 25, FOLLOW_25_in_ruleTextProperty1460);
                    createLeafNode(grammarAccess.getTextPropertyAccess().getEqualsSignKeyword_1(), null);
                    {
                        lv_value_2 = (Token) input.LT(1);
                        match(input, RULE_STRING, FOLLOW_RULE_STRING_in_ruleTextProperty1482);
                        createLeafNode(grammarAccess.getTextPropertyAccess().getValueSTRINGTerminalRuleCall_2_0(), "value");
                        if (current == null) {
                            current = factory.create(grammarAccess.getTextPropertyRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "value", lv_value_2, "STRING", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleHAlignProperty() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleHAlignProperty = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getHAlignPropertyRule(), currentNode);
                pushFollow(FOLLOW_ruleHAlignProperty_in_entryRuleHAlignProperty1523);
                iv_ruleHAlignProperty = ruleHAlignProperty();
                _fsp--;
                current = iv_ruleHAlignProperty;
                match(input, EOF, FOLLOW_EOF_in_entryRuleHAlignProperty1533);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleHAlignProperty() throws RecognitionException {
        EObject current = null;
        Token lv_value_2 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 26, FOLLOW_26_in_ruleHAlignProperty1567);
                    createLeafNode(grammarAccess.getHAlignPropertyAccess().getHalignKeyword_0(), null);
                    match(input, 25, FOLLOW_25_in_ruleHAlignProperty1576);
                    createLeafNode(grammarAccess.getHAlignPropertyAccess().getEqualsSignKeyword_1(), null);
                    {
                        lv_value_2 = (Token) input.LT(1);
                        match(input, RULE_HALIGNMENT, FOLLOW_RULE_HALIGNMENT_in_ruleHAlignProperty1598);
                        createLeafNode(grammarAccess.getHAlignPropertyAccess().getValueHAlignmentTerminalRuleCall_2_0(), "value");
                        if (current == null) {
                            current = factory.create(grammarAccess.getHAlignPropertyRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "value", lv_value_2, "HAlignment", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleFieldRef() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleFieldRef = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getFieldRefRule(), currentNode);
                pushFollow(FOLLOW_ruleFieldRef_in_entryRuleFieldRef1639);
                iv_ruleFieldRef = ruleFieldRef();
                _fsp--;
                current = iv_ruleFieldRef;
                match(input, EOF, FOLLOW_EOF_in_entryRuleFieldRef1649);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleFieldRef() throws RecognitionException {
        EObject current = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 27, FOLLOW_27_in_ruleFieldRef1683);
                    createLeafNode(grammarAccess.getFieldRefAccess().getFieldrefKeyword_0(), null);
                    match(input, 25, FOLLOW_25_in_ruleFieldRef1692);
                    createLeafNode(grammarAccess.getFieldRefAccess().getEqualsSignKeyword_1(), null);
                    {
                        if (current == null) {
                            current = factory.create(grammarAccess.getFieldRefRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        match(input, RULE_ID, FOLLOW_RULE_ID_in_ruleFieldRef1714);
                        createLeafNode(grammarAccess.getFieldRefAccess().getFieldrefFieldCrossReference_2_0(), "fieldref");
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject entryRuleInputProperty() throws RecognitionException {
        EObject current = null;
        EObject iv_ruleInputProperty = null;
        try {
            {
                currentNode = createCompositeNode(grammarAccess.getInputPropertyRule(), currentNode);
                pushFollow(FOLLOW_ruleInputProperty_in_entryRuleInputProperty1750);
                iv_ruleInputProperty = ruleInputProperty();
                _fsp--;
                current = iv_ruleInputProperty;
                match(input, EOF, FOLLOW_EOF_in_entryRuleInputProperty1760);
            }
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public final EObject ruleInputProperty() throws RecognitionException {
        EObject current = null;
        Token lv_value_2 = null;
        EObject temp = null;
        setCurrentLookahead();
        resetLookahead();
        try {
            {
                {
                    match(input, 28, FOLLOW_28_in_ruleInputProperty1794);
                    createLeafNode(grammarAccess.getInputPropertyAccess().getInputKeyword_0(), null);
                    match(input, 25, FOLLOW_25_in_ruleInputProperty1803);
                    createLeafNode(grammarAccess.getInputPropertyAccess().getEqualsSignKeyword_1(), null);
                    {
                        lv_value_2 = (Token) input.LT(1);
                        match(input, RULE_STRING, FOLLOW_RULE_STRING_in_ruleInputProperty1825);
                        createLeafNode(grammarAccess.getInputPropertyAccess().getValueSTRINGTerminalRuleCall_2_0(), "value");
                        if (current == null) {
                            current = factory.create(grammarAccess.getInputPropertyRule().getType().getClassifier());
                            associateNodeWithAstElement(currentNode, current);
                        }
                        try {
                            set(current, "value", lv_value_2, "STRING", lastConsumedNode);
                        } catch (ValueConverterException vce) {
                            handleValueConverterException(vce);
                        }
                    }
                }
            }
            resetLookahead();
            lastConsumedNode = currentNode;
        } catch (RecognitionException re) {
            recover(input, re);
            appendSkippedTokens();
        } finally {
        }
        return current;
    }

    public static final BitSet FOLLOW_ruleModel_in_entryRuleModel73 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleModel83 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleImport_in_ruleModel142 = new BitSet(new long[] { 0x0000000000003002L });

    public static final BitSet FOLLOW_ruleScreen_in_ruleModel181 = new BitSet(new long[] { 0x0000000000002002L });

    public static final BitSet FOLLOW_ruleImport_in_entryRuleImport219 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleImport229 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_12_in_ruleImport263 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_RULE_STRING_in_ruleImport285 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleScreen_in_entryRuleScreen326 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleScreen336 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_13_in_ruleScreen370 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_RULE_ID_in_ruleScreen392 = new BitSet(new long[] { 0x0000000000004000L });

    public static final BitSet FOLLOW_14_in_ruleScreen409 = new BitSet(new long[] { 0x0000000000008000L });

    public static final BitSet FOLLOW_15_in_ruleScreen418 = new BitSet(new long[] { 0x0000000000090000L });

    public static final BitSet FOLLOW_ruleScreenKind_in_ruleScreen452 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleScreenKind_in_entryRuleScreenKind489 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleScreenKind499 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleList_in_ruleScreenKind546 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleFormular_in_ruleScreenKind573 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleList_in_entryRuleList605 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleList615 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_16_in_ruleList649 = new BitSet(new long[] { 0x0000000000020000L });

    public static final BitSet FOLLOW_17_in_ruleList658 = new BitSet(new long[] { 0x0000000000900000L });

    public static final BitSet FOLLOW_ruleListGuiElement_in_ruleList692 = new BitSet(new long[] { 0x0000000000940000L });

    public static final BitSet FOLLOW_18_in_ruleList706 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleFormular_in_entryRuleFormular739 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleFormular749 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_19_in_ruleFormular783 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_RULE_ID_in_ruleFormular805 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleListGuiElement_in_entryRuleListGuiElement846 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleListGuiElement856 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleLabel_in_ruleListGuiElement903 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleField_in_ruleListGuiElement930 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleLabel_in_entryRuleLabel962 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleLabel972 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_20_in_ruleLabel1006 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_RULE_ID_in_ruleLabel1028 = new BitSet(new long[] { 0x0000000000200000L });

    public static final BitSet FOLLOW_21_in_ruleLabel1045 = new BitSet(new long[] { 0x000000000D400000L });

    public static final BitSet FOLLOW_ruleTextProperty_in_ruleLabel1079 = new BitSet(new long[] { 0x000000000C400000L });

    public static final BitSet FOLLOW_ruleHAlignProperty_in_ruleLabel1118 = new BitSet(new long[] { 0x0000000008400000L });

    public static final BitSet FOLLOW_ruleFieldRef_in_ruleLabel1157 = new BitSet(new long[] { 0x0000000000400000L });

    public static final BitSet FOLLOW_22_in_ruleLabel1171 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleField_in_entryRuleField1204 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleField1214 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_23_in_ruleField1248 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_RULE_ID_in_ruleField1270 = new BitSet(new long[] { 0x0000000000200000L });

    public static final BitSet FOLLOW_21_in_ruleField1287 = new BitSet(new long[] { 0x0000000014400000L });

    public static final BitSet FOLLOW_ruleInputProperty_in_ruleField1321 = new BitSet(new long[] { 0x0000000004400000L });

    public static final BitSet FOLLOW_ruleHAlignProperty_in_ruleField1360 = new BitSet(new long[] { 0x0000000000400000L });

    public static final BitSet FOLLOW_22_in_ruleField1374 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleTextProperty_in_entryRuleTextProperty1407 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleTextProperty1417 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_24_in_ruleTextProperty1451 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_ruleTextProperty1460 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_RULE_STRING_in_ruleTextProperty1482 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleHAlignProperty_in_entryRuleHAlignProperty1523 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleHAlignProperty1533 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_26_in_ruleHAlignProperty1567 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_ruleHAlignProperty1576 = new BitSet(new long[] { 0x0000000000000040L });

    public static final BitSet FOLLOW_RULE_HALIGNMENT_in_ruleHAlignProperty1598 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleFieldRef_in_entryRuleFieldRef1639 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleFieldRef1649 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_27_in_ruleFieldRef1683 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_ruleFieldRef1692 = new BitSet(new long[] { 0x0000000000000020L });

    public static final BitSet FOLLOW_RULE_ID_in_ruleFieldRef1714 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_ruleInputProperty_in_entryRuleInputProperty1750 = new BitSet(new long[] { 0x0000000000000000L });

    public static final BitSet FOLLOW_EOF_in_entryRuleInputProperty1760 = new BitSet(new long[] { 0x0000000000000002L });

    public static final BitSet FOLLOW_28_in_ruleInputProperty1794 = new BitSet(new long[] { 0x0000000002000000L });

    public static final BitSet FOLLOW_25_in_ruleInputProperty1803 = new BitSet(new long[] { 0x0000000000000010L });

    public static final BitSet FOLLOW_RULE_STRING_in_ruleInputProperty1825 = new BitSet(new long[] { 0x0000000000000002L });
}
