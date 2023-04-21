package gr.aueb.cs.nlg.NLGEngine;

import org.w3c.dom.*;
import java.util.*;
import gr.aueb.cs.nlg.Languages.*;
import gr.aueb.cs.nlg.NLFiles.*;
import gr.aueb.cs.nlg.Utils.XmlMsgs;
import com.hp.hpl.jena.ontology.*;
import org.apache.log4j.Logger;

public class SurfaceRealization extends NLGEngineComponent {

    static Logger logger = Logger.getLogger(SurfaceRealization.class);

    private AnnotatedDescription AnnotDescription;

    private static boolean first;

    private int MAX_CHARS_PER_LINE;

    private StringBuffer REALIZED_TEXT;

    private NLGLexiconQueryManager NLGLQM;

    private Hashtable<String, String> REFs;

    private int flag;

    private OntModel ontModel;

    private String gender_ref = "";

    private String en_ref = "";

    private String gr_ref = "";

    public static String PROD_RE_BAD = "BAD_RE";

    public static String PROD_RE_NP = "NP";

    public static String PROD_RE_Pronoun = "Pronoun";

    public static String PROD_RE_Demonstrative = "Demonstrative";

    public static String PROD_RE_Article = "Article";

    public static String PROD_RE_NULL = "NullSubject";

    SurfaceRealization(int MCPL, String lang, NLGLexiconQueryManager NLGLQM, OntModel ontModel) {
        super(lang);
        shape_Text = true;
        AnnotDescription = new AnnotatedDescription();
        MAX_CHARS_PER_LINE = MCPL;
        this.NLGLQM = NLGLQM;
        this.ontModel = ontModel;
    }

    public void setModel(OntModel m) {
        this.ontModel = m;
    }

    String UserType = "";

    private boolean shape_Text;

    public void setshapeText(boolean shape_Text) {
        this.shape_Text = shape_Text;
    }

    public String Realize(XmlMsgs MyXmlMsgs, String ut) {
        AnnotDescription.GenerateAnnotatedDescritption();
        AnnotDescription.setEntityId(MyXmlMsgs.getOwner());
        UserType = ut;
        REFs = new Hashtable<String, String>();
        flag = 0;
        int type = MyXmlMsgs.getType();
        REALIZED_TEXT = new StringBuffer();
        if (type == 2 || type == 1) {
            Vector Msgs = MyXmlMsgs.getMsgs();
            int i = 0;
            while (i < Msgs.size()) {
                first = true;
                Node Msg = (Node) Msgs.get(i);
                if (Msg != null && Msg.getFirstChild() != null && Msg.getFirstChild().getLocalName() != null && Msg.getFirstChild().getLocalName().equalsIgnoreCase("Comparator")) {
                    AnnotDescription.startComparator(Msg);
                    Node comparison = Msg.getFirstChild();
                    Vector MsgSlots = XmlMsgs.ReturnChildNodes(comparison);
                    realize_slots(REALIZED_TEXT, MsgSlots, MyXmlMsgs);
                    AnnotDescription.finishComparator();
                    if (REALIZED_TEXT.charAt(REALIZED_TEXT.length() - 2) == '.') {
                        REALIZED_TEXT.deleteCharAt(REALIZED_TEXT.length() - 2);
                    }
                    if (!REALIZED_TEXT.substring(REALIZED_TEXT.length() - "��� ".length(), REALIZED_TEXT.length()).equalsIgnoreCase("��� ")) {
                        if (REALIZED_TEXT.charAt(REALIZED_TEXT.length() - 1) == ' ') REALIZED_TEXT.deleteCharAt(REALIZED_TEXT.length() - 1);
                        REALIZED_TEXT.append(", ");
                        AnnotDescription.add_TEXT(",");
                    }
                    first = false;
                }
                Vector MsgSlots = XmlMsgs.ReturnChildNodes(Msg);
                if (MsgSlots.size() > 0) {
                    realize_slots(REALIZED_TEXT, MsgSlots, MyXmlMsgs);
                    first = false;
                }
                i++;
            }
        } else {
            logger.debug("ERROR...");
        }
        String ret = "";
        if (this.shape_Text) ret = shapeText(REALIZED_TEXT); else ret = REALIZED_TEXT.toString();
        ret = ret.replaceAll(" ;", ";");
        ret = ret.replaceAll(" ,", ",");
        return ret;
    }

    private void realize_slots(StringBuffer REALIZED_TEXT, Vector MsgSlots, XmlMsgs MyXmlMsgs) {
        try {
            for (int j = 0; j < MsgSlots.size(); j++) {
                Node Slot = (Node) MsgSlots.get(j);
                if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.VERB_TAG)) {
                    if (Slot.getParentNode().getLocalName().equalsIgnoreCase("comparator") && exist("val", Slot.getParentNode().getAttributes())) {
                        String ret = MyXmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.plural_VERB_TAG);
                        AnnotDescription.add_VERB(ret, get_for_Property(Slot), get_Interest(Slot), get_Assim(Slot));
                        addText(ret);
                    } else {
                        String the_num_of_the_verb = "";
                        if (j - 1 >= 0 && XmlMsgs.compare((Node) MsgSlots.get(j - 1), XmlMsgs.prefix, XmlMsgs.OWNER_TAG)) {
                            String owner_URI = MyXmlMsgs.getAttribute(Slot.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF);
                            Object obj = NLGLQM.getEntry(owner_URI, getLanguage(), this.UserType);
                            if (obj instanceof Lex_Entry_EN) {
                                the_num_of_the_verb = ((Lex_Entry_EN) obj).get_num();
                            } else if (obj instanceof Lex_Entry_GR) {
                                the_num_of_the_verb = ((Lex_Entry_GR) obj).get_num();
                            } else the_num_of_the_verb = XmlMsgs.SINGULAR;
                        } else {
                            the_num_of_the_verb = XmlMsgs.SINGULAR;
                        }
                        String ret = "";
                        if (the_num_of_the_verb.equals(XmlMsgs.SINGULAR)) ret = MyXmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.singular_VERB_TAG); else ret = MyXmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.plural_VERB_TAG);
                        AnnotDescription.add_VERB(ret, get_for_Property(Slot), get_Interest(Slot), get_Assim(Slot));
                        addText(ret);
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.TEXT_TAG) || XmlMsgs.compare(Slot, XmlMsgs.prefix, "Prep")) {
                    if (Slot.getTextContent().compareTo("��") != 0) {
                        String ret = Slot.getTextContent();
                        AnnotDescription.add_TEXT(ret, get_for_Property(Slot), get_RE_Role(Slot), get_Ref(Slot), get_Interest(Slot), get_Assim(Slot), getPrep(Slot));
                        addText(ret);
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)) {
                    if (Languages.isEnglish(getLanguage())) {
                        String ret = "is";
                        AnnotDescription.add_VERB(ret, get_for_Property(Slot), get_Interest(Slot), get_Assim(Slot));
                        addText(ret);
                    } else if (Languages.isGreek(getLanguage())) {
                        String ret = "�����";
                        AnnotDescription.add_VERB(ret, get_for_Property(Slot), get_Interest(Slot), get_Assim(Slot));
                        addText(ret);
                    }
                    String next_filler_URI = findNextFiller(Slot);
                    Object obj = NLGLQM.getEntry(next_filler_URI, getLanguage(), this.UserType);
                    if (obj instanceof Lex_Entry_EN || obj instanceof Lex_Entry_GR) {
                        logger.debug("PUT" + MyXmlMsgs.getAttribute(Slot.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF) + "," + next_filler_URI);
                        REFs.put(MyXmlMsgs.getAttribute(Slot.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF), next_filler_URI);
                    }
                } else if (XmlMsgs.compare(Slot, "", "COMMA")) {
                    REALIZED_TEXT.deleteCharAt(REALIZED_TEXT.length() - 1);
                    REALIZED_TEXT.append(", ");
                    AnnotDescription.add_TEXT(",");
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, "ROWNER")) {
                    AnnotDescription.add_RE(get_Ref(Slot), get_RE_Role(Slot));
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.OWNER_TAG)) {
                    String owner_URI = MyXmlMsgs.getAttribute(Slot.getParentNode(), XmlMsgs.prefix, XmlMsgs.REF);
                    String OwnerCase = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                    String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                    if (owner_URI.equalsIgnoreCase("")) owner_URI = MyXmlMsgs.getAttribute(Slot.getParentNode(), XmlMsgs.prefix, "entity");
                    String real_owner_URI = owner_URI;
                    if (Slot.getParentNode().getNodeName().equals("owlnl:Comparator")) {
                        String temp = MyXmlMsgs.getAttribute(Slot.getParentNode().getParentNode(), XmlMsgs.prefix, "ComparatorEntities");
                        if (!temp.equals("")) {
                            real_owner_URI = temp;
                        }
                    }
                    AnnotDescription.setStringCompEntities(real_owner_URI);
                    if (!is_RE_Auto(Slot)) {
                        if (getLanguage().equals(Languages.ENGLISH)) {
                            Object obj = NLGLQM.getEntry(owner_URI, getLanguage(), this.UserType);
                            if (obj != null) {
                                String ret = getReferringExprFromUser(Slot, ((Lex_Entry_EN) obj).get_Gender(), ((Lex_Entry_EN) obj).get_num(), OwnerCase, ((Lex_Entry_EN) obj).get(OwnerCase));
                                AnnotDescription.add_RE(ret, real_owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            } else {
                                String ret = GRE(owner_URI, Slot);
                                AnnotDescription.add_RE(ret, real_owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            }
                        } else {
                            Object obj = NLGLQM.getEntry(owner_URI, getLanguage(), this.UserType);
                            if (obj != null) {
                                String ret = getReferringExprFromUser(Slot, ((Lex_Entry_GR) obj).get_Gender(), ((Lex_Entry_GR) obj).get_num(), OwnerCase, ((Lex_Entry_GR) obj).get(OwnerCase));
                                AnnotDescription.add_RE(ret, real_owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            } else {
                                String ret = GRE(owner_URI, Slot);
                                AnnotDescription.add_RE(ret, real_owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            }
                        }
                    } else {
                        String ret = GRE(owner_URI, Slot);
                        String level = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS);
                        AnnotDescription.add_RE(ret, real_owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                        addText(ret);
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.PREVIOUS_TAG)) {
                    String next_URI = MyXmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)).getParentNode(), XmlMsgs.prefix, "entity");
                    String owner_URI = Slot.getTextContent();
                    boolean singular = false;
                    if ((j + 1) < MsgSlots.size()) {
                        if (XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, "form").equalsIgnoreCase("")) singular = true;
                        String NP = "";
                        if (Languages.isGreek(getLanguage())) {
                            Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                            if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_FEMININE)) {
                                NP = NLGLQM.getEntry(owner_URI + "Fem", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_MASCULINE)) {
                                NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else {
                                NP = NLGLQM.getEntry(owner_URI + "Ne", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            }
                        } else {
                            Lex_Entry_EN entry = (Lex_Entry_EN) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                            NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                        }
                        if (NP == "") {
                            addText("NOT FOUND previous");
                        } else {
                            String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                            AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(NP);
                        }
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, "LOCATION")) {
                    boolean singular = false;
                    {
                        String next_URI = XmlMsgs.getAttribute(((Node) MsgSlots.get(j - 1)).getParentNode(), XmlMsgs.prefix, "entity");
                        if (XmlMsgs.getAttribute(((Node) MsgSlots.get(j)).getParentNode(), XmlMsgs.prefix, "form").equalsIgnoreCase("")) singular = true;
                        String owner_URI = Slot.getTextContent();
                        String originalURI = owner_URI;
                        if (owner_URI.indexOf("-") >= 0) owner_URI = owner_URI.substring(0, owner_URI.indexOf("-"));
                        if (!owner_URI.equalsIgnoreCase("")) {
                            String NP = NLGLQM.getEntry(owner_URI, XmlMsgs.ACCUSATIVE_TAG, !singular, getLanguage());
                            if (NP == "") {
                                addText("NOT FOUND location");
                            } else {
                                if (Languages.isGreek(getLanguage())) {
                                    if (originalURI.indexOf("Now") > 0) {
                                        int pos = NP.indexOf("���������");
                                        if (pos > 0) NP = NP.substring(0, pos + "���������".length()) + " ����" + NP.substring(pos + "���������".length()); else {
                                            NP = NP.substring(0, NP.indexOf("����������") + "����������".length()) + " ����" + NP.substring(NP.indexOf("����������") + "����������".length());
                                        }
                                    }
                                    if (originalURI.indexOf("Also") > 0) {
                                        int pos = NP.indexOf("���������");
                                        if (pos > 0) NP = NP.substring(0, pos + "���������".length()) + ", ������," + NP.substring(pos + "���������".length()); else NP = NP.substring(0, NP.indexOf("����������") + "����������".length()) + ", ������," + NP.substring(NP.indexOf("����������") + "����������".length());
                                    }
                                    if (originalURI.indexOf("Still") > 0) {
                                        int pos = NP.indexOf("���������");
                                        if (pos > 0) NP = NP.substring(0, pos + "���������".length()) + ", �����," + NP.substring(pos + "���������".length()); else NP = NP.substring(0, NP.indexOf("����������") + "����������".length()) + ", �����," + NP.substring(NP.indexOf("����������") + "����������".length());
                                    }
                                } else {
                                    if (originalURI.indexOf("Also") > 0) {
                                        int pos = NP.indexOf("is");
                                        if (pos > 0) NP = NP.substring(0, pos + "is".length()) + " also" + NP.substring(pos + "is".length()); else NP = NP.substring(0, NP.indexOf("are") + "are".length()) + " also" + NP.substring(NP.indexOf("are") + "are".length());
                                    }
                                    if (originalURI.indexOf("Now") > 0) {
                                        System.out.println(originalURI);
                                        int pos = NP.indexOf("is");
                                        if (pos > 0) NP = NP.substring(0, pos + "is".length()) + " now" + NP.substring(pos + "is".length()); else NP = NP.substring(0, NP.indexOf("are") + "are".length()) + " now" + NP.substring(NP.indexOf("are") + "are".length());
                                    }
                                    if (originalURI.indexOf("Still") > 0) {
                                        int pos = NP.indexOf("is");
                                        if (pos > 0) NP = NP.substring(0, pos + "is".length()) + " still" + NP.substring(pos + "is".length()); else NP = NP.substring(0, NP.indexOf("are") + "are".length()) + " still" + NP.substring(NP.indexOf("are") + "are".length());
                                    }
                                }
                                String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                                setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(NP);
                            }
                        }
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.AYTOS_TAG)) {
                    String next_URI = XmlMsgs.getAttribute(((Node) MsgSlots.get(j)).getParentNode().getFirstChild(), XmlMsgs.prefix, "entity");
                    String owner_URI = Slot.getTextContent();
                    boolean singular = false;
                    if ((j + 1) < MsgSlots.size()) {
                        singular = true;
                        String NP = "";
                        if (!next_URI.equalsIgnoreCase("")) {
                            if (getLanguage().equalsIgnoreCase("en")) {
                                Lex_Entry_EN entry = (Lex_Entry_EN) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                                gender_ref = entry.get_Gender();
                            } else {
                                Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                                gender_ref = entry.get_Gender();
                            }
                        }
                        if (gender_ref.equalsIgnoreCase(XmlMsgs.GENDER_FEMININE)) {
                            NP = NLGLQM.getEntry(owner_URI + "Fem", XmlMsgs.NOMINATIVE_TAG, !singular, getLanguage());
                        } else if (gender_ref.equalsIgnoreCase(XmlMsgs.GENDER_MASCULINE)) {
                            NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.NOMINATIVE_TAG, !singular, getLanguage());
                        } else {
                            NP = NLGLQM.getEntry(owner_URI + "Ne", XmlMsgs.NOMINATIVE_TAG, !singular, getLanguage());
                        }
                        if (NP == "") {
                            addText("NOT FOUND previous");
                        } else {
                            String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                            AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(NP);
                        }
                    }
                }
                if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.UNIQUE_TAG)) {
                    String next_URI = ((Node) MsgSlots.get(j + 1)).getTextContent();
                    String owner_URI = Slot.getTextContent();
                    boolean singular = false;
                    if ((j + 1) < MsgSlots.size()) {
                        if (XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, "form").equalsIgnoreCase("")) {
                            singular = true;
                        }
                        String NP = "";
                        if (getLanguage().equalsIgnoreCase("El")) {
                            Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                            if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_FEMININE)) {
                                NP = NLGLQM.getEntry(owner_URI + "Fem", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_MASCULINE)) {
                                NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else {
                                NP = NLGLQM.getEntry(owner_URI + "Ne", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            }
                        } else NP = "the only";
                        if (gr_ref.equalsIgnoreCase("")) {
                            if (getLanguage().equalsIgnoreCase("El")) {
                                Node next_filler = Slot.getNextSibling();
                                String next_filler_URI = next_filler.getTextContent();
                                Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_filler_URI, getLanguage(), this.UserType, 1);
                                if (entry != null) {
                                    String gender = entry.get_Gender();
                                    String num = entry.get_num();
                                    String Case = XmlMsgs.getAttribute(next_filler, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                                    try {
                                        gr_ref = NLGLQM.getEntry(next_filler_URI, Case, false, getLanguage());
                                    } catch (Exception ignored) {
                                    }
                                    gender_ref = gender;
                                }
                            } else {
                                Node next_filler = Slot.getNextSibling();
                                String next_filler_URI = next_filler.getTextContent();
                                Lex_Entry_EN entry = (Lex_Entry_EN) NLGLQM.getEntry(next_filler_URI, getLanguage(), this.UserType, 1);
                                if (entry != null) {
                                    String gender = entry.get_Gender();
                                    String num = entry.get_num();
                                    String Case = XmlMsgs.getAttribute(next_filler, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                                    {
                                        en_ref = NLGLQM.getEntry(next_filler_URI, Case, false, getLanguage());
                                    }
                                    gender_ref = gender;
                                }
                            }
                        }
                        if (NP == "") {
                            addText("NOT FOUND unique");
                        } else {
                            String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                            AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(NP);
                        }
                    }
                }
                if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.COMMON_TAG)) {
                    String next_URI = ((Node) MsgSlots.get(j + 1)).getTextContent();
                    String owner_URI = Slot.getTextContent();
                    boolean singular = false;
                    if ((j + 1) < MsgSlots.size()) {
                        if (XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, "form").equalsIgnoreCase("")) singular = true;
                        String NP = "";
                        if (getLanguage().equalsIgnoreCase("El")) {
                            Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                            if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_FEMININE)) {
                                NP = NLGLQM.getEntry(owner_URI + "Fem", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_MASCULINE)) {
                                NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else {
                                NP = NLGLQM.getEntry(owner_URI + "Ne", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            }
                        } else {
                            NP = "all";
                        }
                        if (NP == "") {
                            addText("NOT FOUND common");
                        } else {
                            String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                            AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(NP);
                        }
                    }
                }
                if (XmlMsgs.compare(Slot, XmlMsgs.prefix, "most")) {
                    String next_URI = ((Node) MsgSlots.get(j + 1)).getTextContent();
                    if (next_URI.endsWith("previous")) next_URI = ((Node) MsgSlots.get(j + 2)).getTextContent();
                    String owner_URI = Slot.getTextContent();
                    boolean singular = false;
                    if ((j + 1) < MsgSlots.size()) {
                        if (XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, "form").equalsIgnoreCase("")) singular = true;
                        String NP = "";
                        if (getLanguage().equalsIgnoreCase("El")) {
                            Lex_Entry_GR entry = (Lex_Entry_GR) NLGLQM.getEntry(next_URI, getLanguage(), this.UserType, 1);
                            if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_FEMININE)) {
                                NP = NLGLQM.getEntry(owner_URI + "Fem", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else if (entry.get_Gender().equalsIgnoreCase(XmlMsgs.GENDER_MASCULINE)) {
                                NP = NLGLQM.getEntry(owner_URI + "M", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            } else {
                                NP = NLGLQM.getEntry(owner_URI + "Ne", XmlMsgs.getAttribute(((Node) MsgSlots.get(j + 1)), XmlMsgs.prefix, XmlMsgs.CASE_TAG), !singular, getLanguage());
                            }
                        } else {
                            NP = "most of the";
                        }
                        if (NP == "") {
                            addText("NOT FOUND most");
                        } else {
                            String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                            AnnotDescription.add_RE(NP, owner_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(NP);
                        }
                    }
                } else if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.FILLER_TAG)) {
                    String Filler_URI = Slot.getTextContent();
                    String re_type = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
                    if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.CASE_TAG).compareTo("") != 0) {
                        if (Languages.isEnglish(getLanguage())) {
                            Object obj = NLGLQM.getEntry(Filler_URI, getLanguage(), this.UserType);
                            if (obj instanceof Lex_Entry_EN) {
                                Lex_Entry_EN entry = (Lex_Entry_EN) obj;
                                String Case = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                                if (entry == null) {
                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    String ret = "NOT FOUND FILLER[null]";
                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                } else {
                                    String NP = null;
                                    if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "form").equalsIgnoreCase("")) NP = entry.get(Case); else NP = entry.get(Case, XmlMsgs.PLURAL);
                                    if (NP.compareTo("") == 0) {
                                        setProduced_RE(Slot, this.PROD_RE_BAD);
                                        String ret = "NOT FOUND FILLER";
                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                        addText(ret);
                                    } else {
                                        Node pre_Slot = Slot.getPreviousSibling();
                                        if (pre_Slot != null && pre_Slot.getTextContent().equalsIgnoreCase("�����") && XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "form").equalsIgnoreCase("")) {
                                            if (entry != null) {
                                                REALIZED_TEXT.delete(REALIZED_TEXT.indexOf("�����"), REALIZED_TEXT.length());
                                                addText("another " + NP);
                                            }
                                        } else if (Slot.getPreviousSibling() != null && XmlMsgs.compare(Slot.getPreviousSibling(), XmlMsgs.prefix, XmlMsgs.TEXT_TAG) && Slot.getPreviousSibling().getTextContent().compareTo(", a kind of") == 0) {
                                            addText(NP);
                                            setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                            AnnotDescription.add_RE(NP, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                        } else {
                                            if (!is_RE_Auto(Slot)) {
                                                String ret = getReferringExprFromUser(Slot, entry.get_Gender(), entry.get_num(), Case, NP);
                                                AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                addText(ret);
                                            } else {
                                                Node pre_Slot2 = Slot.getPreviousSibling();
                                                if (pre_Slot2 != null && XmlMsgs.compare(pre_Slot2, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)) {
                                                    String ret = NP;
                                                    if (entry.get_num().equalsIgnoreCase("singular")) ret = EnglishArticles.getIndefiniteArticle(NP) + " " + NP;
                                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                    addText(ret);
                                                } else {
                                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                    AnnotDescription.add_RE(NP, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                    addText(NP);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (obj instanceof CannedList) {
                                System.out.println("CANNED_TEST");
                                System.out.println(obj);
                                if (obj != null) {
                                    String ret = ((CannedList) obj).getCannedText(getLanguage());
                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                } else {
                                    String ret = "[null canned]";
                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                }
                            } else {
                                String ret = "[NOUN OR CANNED TEXT NOT FOUND]";
                                AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            }
                        } else if (Languages.isGreek(getLanguage())) {
                            Object obj = NLGLQM.getEntry(Filler_URI, getLanguage(), this.UserType);
                            if (obj instanceof Lex_Entry_GR) {
                                Lex_Entry_GR entry = (Lex_Entry_GR) obj;
                                String Case = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                                if (entry == null) {
                                    this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    String ret = "NOT FOUND FILLER[null]";
                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                } else {
                                    String NP = "";
                                    if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "form").equalsIgnoreCase("")) NP = entry.get(Case); else NP = entry.get(Case, XmlMsgs.PLURAL);
                                    if (NP.compareTo("") == 0) {
                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                        String ret = "NOT FOUND FILLER";
                                        setProduced_RE(Slot, this.PROD_RE_BAD);
                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                        addText(ret);
                                    } else {
                                        Node pre_Slot = Slot.getPreviousSibling();
                                        if (pre_Slot != null && pre_Slot.getTextContent().equalsIgnoreCase("�����") && XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "form").equalsIgnoreCase("")) {
                                            if (entry != null) {
                                                REALIZED_TEXT.delete(REALIZED_TEXT.indexOf("�����"), REALIZED_TEXT.length());
                                                if (entry.get_Gender().compareTo(XmlMsgs.GENDER_MASCULINE) == 0) {
                                                    String ret = "����" + " ����� " + NP;
                                                    this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                    addText(ret);
                                                } else if (entry.get_Gender().compareTo(XmlMsgs.GENDER_FEMININE) == 0) {
                                                    String ret = "���" + " ����� " + NP;
                                                    this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                    addText(ret);
                                                } else if (entry.get_Gender().compareTo(XmlMsgs.GENDER_NONPESRSONAL) == 0) {
                                                    String ret = "���" + " ����� " + NP;
                                                    this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                    addText(ret);
                                                }
                                            }
                                        } else if (pre_Slot != null) {
                                            if (XmlMsgs.compare(pre_Slot, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)) {
                                                if (entry != null) {
                                                    if (entry.get_Gender().compareTo(XmlMsgs.GENDER_MASCULINE) == 0) {
                                                        String ret = "����" + " " + NP;
                                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                        addText(ret);
                                                    } else if (entry.get_Gender().compareTo(XmlMsgs.GENDER_FEMININE) == 0) {
                                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                        String ret = "���" + " " + NP;
                                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                        addText(ret);
                                                    } else if (entry.get_Gender().compareTo("neuter") == 0) {
                                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                        String ret = "���" + " " + NP;
                                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                        addText(ret);
                                                    }
                                                } else {
                                                    addText("����/���/���" + " " + NP);
                                                }
                                            } else {
                                                logger.debug(Filler_URI + " " + entry.get_Gender() + entry.get_num() + Case);
                                                if (!is_RE_Auto(Slot)) {
                                                    String ret = getReferringExprFromUser(Slot, entry.get_Gender(), entry.get_num(), Case, NP);
                                                    addText(ret);
                                                    AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                } else {
                                                    if (Slot.getPreviousSibling().getTextContent().compareTo("��") == 0) {
                                                        String ret = "��" + " " + NP;
                                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                        addText(ret);
                                                    } else {
                                                        String ret = NP;
                                                        this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                                        AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                                        addText(ret);
                                                    }
                                                }
                                            }
                                        } else {
                                            String ret = NP;
                                            this.setProduced_RE(Slot, SurfaceRealization.PROD_RE_NP);
                                            AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                            addText(ret);
                                        }
                                    }
                                }
                            } else if (obj instanceof CannedList) {
                                if (obj != null) {
                                    String ret;
                                    if (Slot.getPreviousSibling() != null && Slot.getPreviousSibling().getTextContent().compareTo("��") == 0) {
                                        ret = "�� " + ((CannedList) obj).getCannedText(getLanguage());
                                    } else {
                                        ret = ((CannedList) obj).getCannedText(getLanguage());
                                    }
                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                } else {
                                    String ret = "canned null";
                                    setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                    AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                    addText(ret);
                                }
                            } else {
                                String ret = "[NOUN OR CANNED TEXT NOT FOUND]";
                                setProduced_RE(Slot, SurfaceRealization.PROD_RE_BAD);
                                AnnotDescription.add_CANNED_TEXT(ret, Filler_URI, Slot.getParentNode().getNamespaceURI() + Slot.getParentNode().getLocalName(), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                                addText(ret);
                            }
                        }
                    } else {
                        if (Slot.getTextContent().indexOf("^^http://") > 0) {
                            String text = (Slot.getTextContent()).substring(0, Slot.getTextContent().indexOf("^^http://"));
                            logger.debug("text:" + text);
                            String ret = text;
                            AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(ret);
                        } else {
                            String text = Slot.getTextContent();
                            logger.debug("text:" + text);
                            String ret = text;
                            AnnotDescription.add_RE(ret, Filler_URI, re_type, get_for_Property(Slot), getProduced_RE(Slot), get_RE_Role(Slot), get_Interest(Slot), get_Assim(Slot));
                            addText(ret);
                        }
                    }
                }
                if (j == MsgSlots.size() - 1) {
                    if (REALIZED_TEXT.charAt(REALIZED_TEXT.length() - 2) != '.') {
                        REALIZED_TEXT.deleteCharAt(REALIZED_TEXT.length() - 1);
                        if (!Slot.getParentNode().getNodeName().equals("owlnl:Comparator")) {
                            AnnotDescription.add_TEXT(".");
                            REALIZED_TEXT.append(". ");
                        }
                    } else {
                        AnnotDescription.add_startPeriod();
                        AnnotDescription.add_startSentence();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSpace() {
        REALIZED_TEXT.append(" ");
    }

    public void addText(String text) {
        if (text.compareTo("") != 0) {
            REALIZED_TEXT.append(Capitalize(text));
            addSpace();
        }
    }

    private String Capitalize(String text) {
        if (SurfaceRealization.first) {
            SurfaceRealization.first = false;
            char ch = Character.toUpperCase(text.charAt(0));
            return ch + "" + text.substring(1);
        } else {
            return text;
        }
    }

    public static String CapitalizeText(String text) {
        if (text.length() > 0) {
            char ch = Character.toUpperCase(text.charAt(0));
            return ch + "" + text.substring(1);
        } else {
            return text;
        }
    }

    private String shapeText(StringBuffer text) {
        int num_of_lines = text.length() / MAX_CHARS_PER_LINE;
        StringBuffer shaped_text = new StringBuffer();
        for (int i = 0; i < num_of_lines; i++) {
            int index = text.indexOf(" ", MAX_CHARS_PER_LINE + i * MAX_CHARS_PER_LINE);
            if (index != -1) text.insert(index, '\n');
        }
        return new String(text);
    }

    public boolean isURI(String str) {
        if (str.indexOf("^^http://") >= 0) {
            return false;
        } else {
            return true;
        }
    }

    private String GRE(String owner_URI, Node Slot) {
        String referring_expression = "";
        Object obj = NLGLQM.getEntry(owner_URI, getLanguage(), this.UserType);
        String OwnerCase = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
        if (obj != null) {
            if (obj instanceof Lex_Entry_GR || obj instanceof Lex_Entry_EN) {
                Lex_Entry entry = null;
                if (obj instanceof Lex_Entry_GR) entry = (Lex_Entry_GR) obj; else if (obj instanceof Lex_Entry_EN) entry = (Lex_Entry_EN) obj;
                String gender = entry.get_Gender();
                String num = entry.get_num();
                String NP = entry.get(OwnerCase);
                if (NP.compareTo("") != 0) {
                    referring_expression = GREforNotAnonEntity(owner_URI, OwnerCase, Slot, true);
                } else {
                    referring_expression = GREforAnonEntity(owner_URI, OwnerCase, Slot);
                }
            } else {
                referring_expression = ((CannedList) obj).getCannedText(getLanguage());
            }
        } else {
            referring_expression = GREforAnonEntity(owner_URI, OwnerCase, Slot);
        }
        return referring_expression;
    }

    private String GREforAnonEntity(String entityURI, String OwnerCase, Node Slot) {
        logger.debug("GREforAnonEntity " + entityURI + " " + OwnerCase);
        if (REFs.containsKey(entityURI)) {
            String entityIS_A_URI = REFs.get(entityURI);
            Object obj = NLGLQM.getEntry(entityIS_A_URI, getLanguage(), this.UserType);
            if (isAnon(entityIS_A_URI, OwnerCase)) {
                return "NOT FOUND is-a entity";
            } else {
                if (Languages.isEnglish(getLanguage())) {
                    if (!is_RE_Auto(Slot)) {
                        return getReferringExprFromUser(Slot, ((Lex_Entry_EN) obj).get_Gender(), ((Lex_Entry_EN) obj).get_num(), OwnerCase, ((Lex_Entry_EN) obj).get(OwnerCase));
                    } else {
                        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel4) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return "this" + " " + "particular" + " " + ((Lex_Entry_EN) obj).get(OwnerCase);
                        }
                        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel3) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return "this" + " " + ((Lex_Entry_EN) obj).get(OwnerCase);
                        } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel2) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return "the" + " " + ((Lex_Entry_EN) obj).get(OwnerCase);
                        } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel1) == 0) {
                            if (Slot.getParentNode().getFirstChild().getLocalName().equalsIgnoreCase("comparator")) {
                                if (exist("Val", Slot.getParentNode().getFirstChild().getAttributes()) || (exist("location", Slot.getParentNode().getFirstChild().getAttributes()) && !exist("multyComp", Slot.getParentNode().getFirstChild().getAttributes()))) {
                                    setProduced_RE(Slot, this.PROD_RE_NP);
                                    return "this" + " " + ((Lex_Entry_EN) obj).get(OwnerCase);
                                } else {
                                    setProduced_RE(Slot, this.PROD_RE_Pronoun);
                                    String prn = Pronouns.getPronoun(OwnerCase, ((Lex_Entry_EN) obj).get_num(), ((Lex_Entry_EN) obj).get_Gender());
                                    return prn;
                                }
                            } else {
                                setProduced_RE(Slot, this.PROD_RE_Pronoun);
                                String prn = Pronouns.getPronoun(OwnerCase, ((Lex_Entry_EN) obj).get_num(), ((Lex_Entry_EN) obj).get_Gender());
                                return prn;
                            }
                        }
                    }
                } else if (Languages.isGreek(getLanguage())) {
                    Lex_Entry_GR LE_GR = ((Lex_Entry_GR) obj);
                    if (!is_RE_Auto(Slot)) {
                        return getReferringExprFromUser(Slot, ((Lex_Entry_GR) obj).get_Gender(), ((Lex_Entry_GR) obj).get_num(), OwnerCase, ((Lex_Entry_GR) obj).get(OwnerCase));
                    } else {
                        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel4) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase)) + " " + SurfaceRealizationHelper.getfoo2(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase) + " " + LE_GR.get(OwnerCase);
                        }
                        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel3) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return SurfaceRealizationHelper.getfoo(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, true) + " " + LE_GR.get(OwnerCase);
                        } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel2) == 0) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase);
                        } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel1) == 0) {
                            if (OwnerCase.compareTo(XmlMsgs.GENITIVE_TAG) == 0) {
                                setProduced_RE(Slot, this.PROD_RE_Article);
                                return GreekArticles.getArticle(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase));
                            } else {
                                if (Slot.getParentNode().getFirstChild().getLocalName().equalsIgnoreCase("comparator")) {
                                    if (exist("Val", Slot.getParentNode().getFirstChild().getAttributes())) {
                                        setProduced_RE(Slot, this.PROD_RE_Demonstrative);
                                        return SurfaceRealizationHelper.getfoo(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, true) + " " + LE_GR.get(OwnerCase);
                                    } else {
                                        setProduced_RE(Slot, this.PROD_RE_NULL);
                                        return "";
                                    }
                                } else {
                                    setProduced_RE(Slot, this.PROD_RE_NULL);
                                    return "";
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Node nextSlot = Slot.getNextSibling();
            if (XmlMsgs.compare(nextSlot, XmlMsgs.prefix, XmlMsgs.IS_A_TAG)) {
                Node nextFiller = findNextFillerNode(Slot);
                String nextFillerURI = nextFiller.getTextContent();
                String FillerCase = XmlMsgs.getAttribute(nextFiller, XmlMsgs.prefix, XmlMsgs.CASE_TAG);
                if (isAnon(nextFillerURI, FillerCase)) {
                    setProduced_RE(Slot, this.PROD_RE_BAD);
                    return "[NOT FOUND next Filler]";
                } else {
                    if (Languages.isGreek(getLanguage())) {
                        if (!is_RE_Auto(Slot)) {
                            Lex_Entry_GR LE_GR = (Lex_Entry_GR) NLGLQM.getEntry(nextFillerURI, getLanguage(), this.UserType, 1);
                            return getReferringExprFromUser(Slot, LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase));
                        } else {
                            setProduced_RE(Slot, this.PROD_RE_Demonstrative);
                            Lex_Entry_GR LE_GR = (Lex_Entry_GR) NLGLQM.getEntry(nextFillerURI, getLanguage(), this.UserType, 1);
                            return SurfaceRealizationHelper.getfoo(LE_GR.get_Gender(), LE_GR.get_num(), FillerCase, false);
                        }
                    } else if (Languages.isEnglish(getLanguage())) {
                        if (!is_RE_Auto(Slot)) {
                            Lex_Entry_EN LE_EN = (Lex_Entry_EN) NLGLQM.getEntry(nextFillerURI, getLanguage(), this.UserType, 1);
                            return getReferringExprFromUser(Slot, LE_EN.get_Gender(), LE_EN.get_num(), OwnerCase, LE_EN.get(OwnerCase));
                        } else {
                            setProduced_RE(Slot, this.PROD_RE_Demonstrative);
                            return "this";
                        }
                    }
                }
            } else {
                String ClassType = NLGEngine.getClassType(ontModel, entityURI);
                Object obj = NLGLQM.getEntry(ClassType, getLanguage(), this.UserType);
                if (obj instanceof Lex_Entry_EN || obj instanceof Lex_Entry_GR) {
                    REFs.put(entityURI, ClassType);
                }
                logger.debug("entityURI" + entityURI);
                String ret = "";
                if (obj != null) {
                    if (obj instanceof Lex_Entry_EN) {
                        Lex_Entry_EN le = ((Lex_Entry_EN) obj);
                        ret = le.get(OwnerCase);
                        if (ret.equals("")) {
                            setProduced_RE(Slot, this.PROD_RE_BAD);
                            return "[NOUN OR CANNED TEXT NOT FOUND]";
                        }
                        if (Slot.getParentNode().getLocalName().equalsIgnoreCase("comparator")) {
                            setProduced_RE(Slot, this.PROD_RE_NP);
                            return ret;
                        } else {
                            setProduced_RE(Slot, this.PROD_RE_Demonstrative);
                            return "this" + " " + ret;
                        }
                    } else if (obj instanceof Lex_Entry_GR) {
                        Lex_Entry_GR le = ((Lex_Entry_GR) obj);
                        ret = le.get(OwnerCase);
                        if (ret.equals("")) {
                            setProduced_RE(Slot, this.PROD_RE_BAD);
                            return "[NOUN OR CANNED TEXT NOT FOUND]";
                        }
                        return SurfaceRealizationHelper.getfoo(le.get_Gender(), le.get_num(), OwnerCase, true) + " " + ret;
                    } else {
                        ret = ((CannedList) obj).getCannedText(getLanguage());
                        if (ret.equals("")) {
                            setProduced_RE(Slot, this.PROD_RE_BAD);
                            return "[NOUN OR CANNED TEXT NOT FOUND]";
                        }
                        return ret;
                    }
                } else {
                    setProduced_RE(Slot, this.PROD_RE_BAD);
                    return "[NOUN OR CANNED TEXT NOT FOUND]";
                }
            }
        }
        return "ERROR";
    }

    public String addArticle(Lex_Entry entry, String str) {
        if (Languages.isEnglish(getLanguage())) {
            if (entry.getCountable() && entry.get_num().equalsIgnoreCase("singular")) {
                return EnglishArticles.getIndefiniteArticle(str) + " " + str;
            } else {
                return str;
            }
        }
        if (Languages.isGreek(getLanguage())) {
            if (entry.getCountable()) {
                if (entry.get_Gender().compareTo(XmlMsgs.GENDER_MASCULINE) == 0) {
                    return "����" + " " + str;
                } else if (entry.get_Gender().compareTo(XmlMsgs.GENDER_FEMININE) == 0) {
                    return "���" + " " + str;
                } else if (entry.get_Gender().compareTo("neuter") == 0) {
                    return "���" + " " + str;
                }
            } else {
                return str;
            }
        }
        return str;
    }

    private String GREforNotAnonEntity(String entityURI, String OwnerCase, Node Slot, boolean b) {
        logger.debug("GREforNotAnonEntity " + entityURI + " " + OwnerCase);
        if (NLGEngine.isClass(ontModel, entityURI) && !XmlMsgs.compare(Slot.getParentNode(), XmlMsgs.prefix, XmlMsgs.COMPARATOR_TAG)) {
            Object obj = NLGLQM.getEntry(entityURI, getLanguage(), this.UserType);
            if (obj instanceof CannedList) {
                CannedList CL = ((CannedList) obj);
                String text = CL.getCannedText(getLanguage());
                return text;
            }
            if (Languages.isEnglish(getLanguage())) {
                Lex_Entry_EN LE_EN = ((Lex_Entry_EN) obj);
                if (LE_EN.getCountable()) {
                    this.setProduced_RE(Slot, this.PROD_RE_NP);
                    if (LE_EN.get_num().equalsIgnoreCase("singular")) return EnglishArticles.getIndefiniteArticle(LE_EN.get(OwnerCase)) + " " + LE_EN.get(OwnerCase); else return LE_EN.get(OwnerCase);
                } else {
                    this.setProduced_RE(Slot, this.PROD_RE_NP);
                    return LE_EN.get(OwnerCase);
                }
            }
            if (Languages.isGreek(getLanguage())) {
                Lex_Entry_GR LE_GR = ((Lex_Entry_GR) obj);
                if (LE_GR.get_Gender().compareTo(XmlMsgs.GENDER_MASCULINE) == 0) {
                    this.setProduced_RE(Slot, this.PROD_RE_NP);
                    return "����" + " " + LE_GR.get(OwnerCase);
                } else if (LE_GR.get_Gender().compareTo(XmlMsgs.GENDER_FEMININE) == 0) {
                    this.setProduced_RE(Slot, this.PROD_RE_NP);
                    return "���" + " " + LE_GR.get(OwnerCase);
                } else if (LE_GR.get_Gender().compareTo("neuter") == 0) {
                    this.setProduced_RE(Slot, this.PROD_RE_NP);
                    return "���" + " " + LE_GR.get(OwnerCase);
                }
                return LE_GR.get(OwnerCase);
            }
        }
        if (REFs.containsKey(entityURI) && b) {
            return GREforAnonEntity(entityURI, OwnerCase, Slot);
        } else {
            Object obj = NLGLQM.getEntry(entityURI, getLanguage(), this.UserType);
            if (Languages.isEnglish(getLanguage())) {
                Lex_Entry_EN LE_EN = ((Lex_Entry_EN) obj);
                if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel4) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes())) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_EN, LE_EN.get(OwnerCase));
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_EN, LE_EN.get(OwnerCase));
                        }
                    }
                }
                if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel3) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes())) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.SINGULAR);
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            if (this.REALIZED_TEXT.lastIndexOf(" the") > this.REALIZED_TEXT.length() - 6) {
                                this.setProduced_RE(Slot, this.PROD_RE_NP);
                                return LE_EN.get(OwnerCase);
                            } else {
                                this.setProduced_RE(Slot, this.PROD_RE_NP);
                                return addArticle(LE_EN, LE_EN.get(OwnerCase));
                            }
                        }
                    }
                } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel2) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes())) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_EN, LE_EN.get(OwnerCase));
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_EN.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_EN, LE_EN.get(OwnerCase));
                        }
                    }
                } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel1) == 0) {
                    this.setProduced_RE(Slot, this.PROD_RE_Pronoun);
                    String prn = Pronouns.getPronoun(OwnerCase, ((Lex_Entry_EN) obj).get_num(), ((Lex_Entry_EN) obj).get_Gender());
                    return prn;
                }
            } else if (Languages.isGreek(getLanguage())) {
                Lex_Entry_GR LE_GR = ((Lex_Entry_GR) obj);
                if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel4) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes())) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_GR, LE_GR.get(OwnerCase));
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), XmlMsgs.PLURAL, OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), XmlMsgs.SINGULAR, OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase);
                        }
                    }
                }
                if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel3) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes()) && Slot.getParentNode().getLocalName().equalsIgnoreCase("comparator")) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_GR.get(OwnerCase);
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), XmlMsgs.PLURAL, OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase);
                        }
                    }
                } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel2) == 0) {
                    if (exist("previous", Slot.getParentNode().getAttributes())) {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return addArticle(LE_GR, LE_GR.get(OwnerCase));
                        }
                    } else {
                        if (exist("form", Slot.getAttributes())) {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), XmlMsgs.PLURAL, OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase, XmlMsgs.PLURAL);
                        } else {
                            this.setProduced_RE(Slot, this.PROD_RE_NP);
                            return GreekArticles.getArticle(LE_GR.get_Gender(), XmlMsgs.SINGULAR, OwnerCase, LE_GR.get(OwnerCase)) + " " + LE_GR.get(OwnerCase);
                        }
                    }
                } else if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RE_FOCUS).compareTo(XmlMsgs.FOCUSLevel1) == 0) {
                    if (OwnerCase.compareTo(XmlMsgs.GENITIVE_TAG) == 0) {
                        this.setProduced_RE(Slot, this.PROD_RE_NP);
                        return GreekArticles.getArticle(LE_GR.get_Gender(), LE_GR.get_num(), OwnerCase, LE_GR.get(OwnerCase));
                    } else {
                        this.setProduced_RE(Slot, this.PROD_RE_NULL);
                        return "";
                    }
                }
            }
        }
        return "ERROR";
    }

    private boolean isAnon(String entityURI, String OwnerCase) {
        Object obj = NLGLQM.getEntry(entityURI, getLanguage(), this.UserType);
        if (obj != null) {
            Lex_Entry entry = null;
            if (obj instanceof Lex_Entry_GR || obj instanceof Lex_Entry_EN) {
                if (obj instanceof Lex_Entry_GR) entry = (Lex_Entry_GR) obj; else if (obj instanceof Lex_Entry_EN) entry = (Lex_Entry_EN) obj;
                String NP = entry.get(OwnerCase);
                if (NP.compareTo("") == 0) {
                    return true;
                } else {
                    return false;
                }
            } else if (obj instanceof CannedList) {
                CannedList CL = ((CannedList) obj);
                String text = CL.getCannedText(getLanguage());
                if (text.compareTo("") == 0) return true; else return false;
            }
        } else {
            return true;
        }
        return true;
    }

    private String findNextFiller(Node Slot) {
        Node next_filler = null;
        next_filler = Slot.getNextSibling();
        while (!XmlMsgs.compare(next_filler, XmlMsgs.prefix, XmlMsgs.FILLER_TAG)) {
            next_filler = next_filler.getNextSibling();
        }
        String next_filler_URI = next_filler.getTextContent();
        return next_filler_URI;
    }

    private Node findNextFillerNode(Node Slot) {
        Node next_filler = null;
        next_filler = Slot.getNextSibling();
        while (!XmlMsgs.compare(next_filler, XmlMsgs.prefix, XmlMsgs.FILLER_TAG)) {
            next_filler = next_filler.getNextSibling();
        }
        return next_filler;
    }

    public boolean is_RE_Auto(Node Slot) {
        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE).compareTo(XmlMsgs.RE_AUTO) == 0 || XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE).compareTo("") == 0) return true; else return false;
    }

    public void removePreviousWords() {
        if (REALIZED_TEXT.indexOf(",") < 0 && REALIZED_TEXT.indexOf(".") < 0) {
            if (getLanguage().equalsIgnoreCase("El")) {
                String tmp = REALIZED_TEXT.substring(0, REALIZED_TEXT.indexOf("�����"));
                REALIZED_TEXT.delete(0, REALIZED_TEXT.length() - 1);
                REALIZED_TEXT.append(tmp + "����� ");
            } else {
                String tmp = REALIZED_TEXT.substring(0, REALIZED_TEXT.indexOf("is"));
                REALIZED_TEXT.delete(0, REALIZED_TEXT.length() - 1);
                REALIZED_TEXT.append(tmp + "is ");
            }
        }
    }

    public boolean exist(String name, NamedNodeMap map) {
        for (int i = 0; i < map.getLength(); i++) {
            if (map.item(i).getLocalName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public String getReferringExprFromUser(Node Slot, String Gender, String number, String Case, String text) {
        if (XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE).compareTo(XmlMsgs.RE_AUTO) != 0) {
            String retype = XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.RETYPE);
            if (retype.compareTo(XmlMsgs.RE_PRONOUN) == 0) {
                if (Languages.isEnglish(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_Pronoun);
                    return Pronouns.getPronoun(Case, number, Gender);
                } else if (Languages.isGreek(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NULL);
                    return "";
                }
            } else if (retype.compareTo(XmlMsgs.RE_DEF_ART) == 0) {
                if (Languages.isEnglish(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    return "the" + " " + text;
                } else if (Languages.isGreek(getLanguage())) {
                    if (Slot.getPreviousSibling() != null && Slot.getPreviousSibling().getTextContent().compareTo("��") == 0) {
                        setProduced_RE(Slot, this.PROD_RE_NP);
                        return GreekArticles.get_prepositional_phrase(Gender, number, Case, text) + " " + text;
                    } else {
                        setProduced_RE(Slot, this.PROD_RE_NP);
                        return GreekArticles.getArticle(Gender, number, Case, text) + " " + text;
                    }
                }
            } else if (retype.compareTo(XmlMsgs.RE_INDEF_ART) == 0) {
                if (Languages.isEnglish(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    if (number.equalsIgnoreCase("singular")) return EnglishArticles.getIndefiniteArticle(text) + " " + text; else return text;
                } else if (Languages.isGreek(getLanguage())) {
                    if (Gender.compareTo(XmlMsgs.GENDER_MASCULINE) == 0) {
                        setProduced_RE(Slot, this.PROD_RE_NP);
                        return "����" + " " + text;
                    } else if (Gender.compareTo(XmlMsgs.GENDER_FEMININE) == 0) {
                        setProduced_RE(Slot, this.PROD_RE_NP);
                        return "���" + " " + text;
                    } else if (Gender.compareTo("neuter") == 0) {
                        setProduced_RE(Slot, this.PROD_RE_NP);
                        return "���" + " " + text;
                    }
                }
            } else if (retype.compareTo(XmlMsgs.RE_DEMONSTRATIVE) == 0) {
                if (Languages.isEnglish(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    return "this" + " " + text;
                } else if (Languages.isGreek(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    return SurfaceRealizationHelper.getfoo(Gender, number, Case, true) + " " + text;
                }
            } else if (retype.compareTo(XmlMsgs.RE_FULLNAME) == 0) {
                if (Languages.isEnglish(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    return text;
                } else if (Languages.isGreek(getLanguage())) {
                    setProduced_RE(Slot, this.PROD_RE_NP);
                    return text;
                }
            } else {
                setProduced_RE(Slot, this.PROD_RE_BAD);
                return "ERROR";
            }
        }
        return "GREforNotAnonEntity did not procused an appropriate referring expression";
    }

    public AnnotatedDescription getAnnotatedDescription() {
        return this.AnnotDescription;
    }

    private void setProduced_RE(Node Slot, String type) {
        ((Element) Slot).setAttribute("owlnl:Produced_RE", type);
        if (XmlMsgs.compare(Slot, XmlMsgs.prefix, XmlMsgs.OWNER_TAG)) {
            set_RE_ROLE(Slot, XmlMsgs.OWNER_TAG);
        } else {
            set_RE_ROLE(Slot, XmlMsgs.FILLER_TAG);
        }
    }

    private String getProduced_RE(Node Slot) {
        if (Slot.getAttributes() != null) {
            Node nd = Slot.getAttributes().getNamedItem("owlnl:Produced_RE");
            if (nd != null) {
                return nd.getTextContent();
            }
        }
        return "NOT_FOUND_RE";
    }

    private String get_RE_Role(Node Slot) {
        if (Slot.getAttributes() != null) {
            Node nd = Slot.getAttributes().getNamedItem("owlnl:role");
            if (nd != null) {
                return nd.getTextContent();
            }
        }
        return "NOT_FOUND_RE_ROLE";
    }

    private void set_RE_ROLE(Node Slot, String type) {
        ((Element) Slot).setAttribute("owlnl:role", type);
    }

    private String get_for_Property(Node Slot) {
        return XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "forProperty");
    }

    private String get_Interest(Node Slot) {
        return XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "interest");
    }

    private String get_Assim(Node Slot) {
        return XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.ASSIMIL_SCORE);
    }

    private String get_Ref(Node Slot) {
        return XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, XmlMsgs.REF);
    }

    private String getPrep(Node Slot) {
        return XmlMsgs.getAttribute(Slot, XmlMsgs.prefix, "Prep");
    }
}
