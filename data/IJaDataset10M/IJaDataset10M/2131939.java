package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplComplexfunctioncall extends XplNode {

    boolean p_indexer;

    String p_targetClass;

    String p_targetMember;

    boolean p_donotrender;

    boolean p_evaluate;

    boolean p_ignoreforsubprogram;

    String p_doc;

    String p_helpURL;

    String p_ldsrc;

    boolean p_iny;

    String p_inydata;

    String p_inyby;

    String p_lddata;

    XplExpression p_l;

    XplCexpression p_ce;

    XplFunctionBody p_fbk;

    public XplComplexfunctioncall() {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        p_l = null;
        p_ce = null;
        p_fbk = null;
    }

    public XplComplexfunctioncall(boolean n_indexer, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram) {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_indexer(n_indexer);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        p_l = null;
        p_ce = null;
        p_fbk = null;
    }

    public XplComplexfunctioncall(XplExpression n_l) {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        p_l = null;
        p_ce = null;
        p_fbk = null;
        set_l(n_l);
    }

    public XplComplexfunctioncall(boolean n_indexer, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram, XplExpression n_l) {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_indexer(n_indexer);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        p_l = null;
        p_ce = null;
        p_fbk = null;
        set_l(n_l);
    }

    public XplComplexfunctioncall(boolean n_indexer, String n_targetClass, String n_targetMember, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata) {
        set_indexer(n_indexer);
        set_targetClass(n_targetClass);
        set_targetMember(n_targetMember);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        p_l = null;
        p_ce = null;
        p_fbk = null;
    }

    public XplComplexfunctioncall(XplExpression n_l, XplCexpression n_ce, XplFunctionBody n_fbk) {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        p_l = null;
        p_ce = null;
        p_fbk = null;
        set_l(n_l);
        set_ce(n_ce);
        set_fbk(n_fbk);
    }

    public XplComplexfunctioncall(boolean n_indexer, String n_targetClass, String n_targetMember, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata, XplExpression n_l) {
        set_indexer(n_indexer);
        set_targetClass(n_targetClass);
        set_targetMember(n_targetMember);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        set_indexer(n_indexer);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        p_l = null;
        p_ce = null;
        p_fbk = null;
    }

    public XplComplexfunctioncall(boolean n_indexer, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram, XplExpression n_l, XplCexpression n_ce, XplFunctionBody n_fbk) {
        p_indexer = false;
        p_targetClass = "";
        p_targetMember = "";
        p_donotrender = false;
        p_evaluate = false;
        p_ignoreforsubprogram = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_indexer(n_indexer);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        p_l = null;
        p_ce = null;
        p_fbk = null;
        set_l(n_l);
        set_ce(n_ce);
        set_fbk(n_fbk);
    }

    public XplComplexfunctioncall(boolean n_indexer, String n_targetClass, String n_targetMember, boolean n_donotrender, boolean n_evaluate, boolean n_ignoreforsubprogram, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata, XplExpression n_l, XplCexpression n_ce, XplFunctionBody n_fbk) {
        set_indexer(n_indexer);
        set_targetClass(n_targetClass);
        set_targetMember(n_targetMember);
        set_donotrender(n_donotrender);
        set_evaluate(n_evaluate);
        set_ignoreforsubprogram(n_ignoreforsubprogram);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        p_l = null;
        p_ce = null;
        p_fbk = null;
        set_l(n_l);
        set_ce(n_ce);
        set_fbk(n_fbk);
    }

    public XplNode Clone() {
        XplComplexfunctioncall copy = new XplComplexfunctioncall();
        copy.set_indexer(this.p_indexer);
        copy.set_targetClass(this.p_targetClass);
        copy.set_targetMember(this.p_targetMember);
        copy.set_donotrender(this.p_donotrender);
        copy.set_evaluate(this.p_evaluate);
        copy.set_ignoreforsubprogram(this.p_ignoreforsubprogram);
        copy.set_doc(this.p_doc);
        copy.set_helpURL(this.p_helpURL);
        copy.set_ldsrc(this.p_ldsrc);
        copy.set_iny(this.p_iny);
        copy.set_inydata(this.p_inydata);
        copy.set_inyby(this.p_inyby);
        copy.set_lddata(this.p_lddata);
        if (p_l != null) copy.set_l((XplExpression) p_l.Clone());
        if (p_ce != null) copy.set_ce((XplCexpression) p_ce.Clone());
        if (p_fbk != null) copy.set_fbk((XplFunctionBody) p_fbk.Clone());
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplComplexfunctioncall;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_indexer != false) writer.WriteAttributeString("indexer", CodeDOM_Utils.Att_ToString(p_indexer));
        if (p_targetClass != "") writer.WriteAttributeString("targetClass", CodeDOM_Utils.Att_ToString(p_targetClass));
        if (p_targetMember != "") writer.WriteAttributeString("targetMember", CodeDOM_Utils.Att_ToString(p_targetMember));
        if (p_donotrender != false) writer.WriteAttributeString("donotrender", CodeDOM_Utils.Att_ToString(p_donotrender));
        if (p_evaluate != false) writer.WriteAttributeString("evaluate", CodeDOM_Utils.Att_ToString(p_evaluate));
        if (p_ignoreforsubprogram != false) writer.WriteAttributeString("ignoreforsubprogram", CodeDOM_Utils.Att_ToString(p_ignoreforsubprogram));
        if (p_doc != "") writer.WriteAttributeString("doc", CodeDOM_Utils.Att_ToString(p_doc));
        if (p_helpURL != "") writer.WriteAttributeString("helpURL", CodeDOM_Utils.Att_ToString(p_helpURL));
        if (p_ldsrc != "") writer.WriteAttributeString("ldsrc", CodeDOM_Utils.Att_ToString(p_ldsrc));
        if (p_iny != false) writer.WriteAttributeString("iny", CodeDOM_Utils.Att_ToString(p_iny));
        if (p_inydata != "") writer.WriteAttributeString("inydata", CodeDOM_Utils.Att_ToString(p_inydata));
        if (p_inyby != "") writer.WriteAttributeString("inyby", CodeDOM_Utils.Att_ToString(p_inyby));
        if (p_lddata != "") writer.WriteAttributeString("lddata", CodeDOM_Utils.Att_ToString(p_lddata));
        if (p_l != null) if (!p_l.Write(writer)) result = false;
        if (p_ce != null) if (!p_ce.Write(writer)) result = false;
        if (p_fbk != null) if (!p_fbk.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("indexer")) {
                    this.set_indexer(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("targetClass")) {
                    this.set_targetClass(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("targetMember")) {
                    this.set_targetMember(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("donotrender")) {
                    this.set_donotrender(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("evaluate")) {
                    this.set_evaluate(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("ignoreforsubprogram")) {
                    this.set_ignoreforsubprogram(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("doc")) {
                    this.set_doc(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("helpURL")) {
                    this.set_helpURL(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("ldsrc")) {
                    this.set_ldsrc(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("iny")) {
                    this.set_iny(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
                } else if (reader.Name().equals("inydata")) {
                    this.set_inydata(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("inyby")) {
                    this.set_inyby(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("lddata")) {
                    this.set_lddata(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else {
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Atributo '" + reader.Name() + "' invalido en elemento '" + this.get_Name() + "'.");
                }
            }
            reader.MoveToElement();
        }
        this.p_l = null;
        this.p_ce = null;
        this.p_fbk = null;
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("l")) {
                            tempNode = new XplExpression();
                            tempNode.Read(reader);
                            if (this.get_l() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_l((XplExpression) tempNode);
                        } else if (reader.Name().equals("ce")) {
                            tempNode = new XplCexpression();
                            tempNode.Read(reader);
                            if (this.get_ce() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_ce((XplCexpression) tempNode);
                        } else if (reader.Name().equals("fbk")) {
                            tempNode = new XplFunctionBody();
                            tempNode.Read(reader);
                            if (this.get_fbk() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_fbk((XplFunctionBody) tempNode);
                        } else {
                            throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nombre de nodo '" + reader.Name() + "' inesperado como hijo de elemento '" + this.get_Name() + "'.");
                        }
                        break;
                    case XmlNodeType.ENDELEMENT:
                        break;
                    case XmlNodeType.TEXT:
                        throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".No se esperaba texto en este nodo.");
                    default:
                        break;
                }
                reader.Read();
            }
        }
        return this;
    }

    public boolean get_indexer() {
        return p_indexer;
    }

    public String get_targetClass() {
        return p_targetClass;
    }

    public String get_targetMember() {
        return p_targetMember;
    }

    public boolean get_donotrender() {
        return p_donotrender;
    }

    public boolean get_evaluate() {
        return p_evaluate;
    }

    public boolean get_ignoreforsubprogram() {
        return p_ignoreforsubprogram;
    }

    public String get_doc() {
        return p_doc;
    }

    public String get_helpURL() {
        return p_helpURL;
    }

    public String get_ldsrc() {
        return p_ldsrc;
    }

    public boolean get_iny() {
        return p_iny;
    }

    public String get_inydata() {
        return p_inydata;
    }

    public String get_inyby() {
        return p_inyby;
    }

    public String get_lddata() {
        return p_lddata;
    }

    public XplExpression get_l() {
        return p_l;
    }

    public XplCexpression get_ce() {
        return p_ce;
    }

    public XplFunctionBody get_fbk() {
        return p_fbk;
    }

    public boolean set_indexer(boolean new_indexer) {
        boolean back_indexer = p_indexer;
        p_indexer = new_indexer;
        return back_indexer;
    }

    public String set_targetClass(String new_targetClass) {
        String back_targetClass = p_targetClass;
        p_targetClass = new_targetClass;
        return back_targetClass;
    }

    public String set_targetMember(String new_targetMember) {
        String back_targetMember = p_targetMember;
        p_targetMember = new_targetMember;
        return back_targetMember;
    }

    public boolean set_donotrender(boolean new_donotrender) {
        boolean back_donotrender = p_donotrender;
        p_donotrender = new_donotrender;
        return back_donotrender;
    }

    public boolean set_evaluate(boolean new_evaluate) {
        boolean back_evaluate = p_evaluate;
        p_evaluate = new_evaluate;
        return back_evaluate;
    }

    public boolean set_ignoreforsubprogram(boolean new_ignoreforsubprogram) {
        boolean back_ignoreforsubprogram = p_ignoreforsubprogram;
        p_ignoreforsubprogram = new_ignoreforsubprogram;
        return back_ignoreforsubprogram;
    }

    public String set_doc(String new_doc) {
        String back_doc = p_doc;
        p_doc = new_doc;
        return back_doc;
    }

    public String set_helpURL(String new_helpURL) {
        String back_helpURL = p_helpURL;
        p_helpURL = new_helpURL;
        return back_helpURL;
    }

    public String set_ldsrc(String new_ldsrc) {
        String back_ldsrc = p_ldsrc;
        p_ldsrc = new_ldsrc;
        return back_ldsrc;
    }

    public boolean set_iny(boolean new_iny) {
        boolean back_iny = p_iny;
        p_iny = new_iny;
        return back_iny;
    }

    public String set_inydata(String new_inydata) {
        String back_inydata = p_inydata;
        p_inydata = new_inydata;
        return back_inydata;
    }

    public String set_inyby(String new_inyby) {
        String back_inyby = p_inyby;
        p_inyby = new_inyby;
        return back_inyby;
    }

    public String set_lddata(String new_lddata) {
        String back_lddata = p_lddata;
        p_lddata = new_lddata;
        return back_lddata;
    }

    public XplExpression set_l(XplExpression new_l) {
        XplExpression back_l = p_l;
        p_l = new_l;
        if (p_l != null) {
            p_l.set_Name("l");
            p_l.set_Parent(this);
        }
        return back_l;
    }

    public XplCexpression set_ce(XplCexpression new_ce) {
        XplCexpression back_ce = p_ce;
        p_ce = new_ce;
        if (p_ce != null) {
            p_ce.set_Name("ce");
            p_ce.set_Parent(this);
        }
        return back_ce;
    }

    public XplFunctionBody set_fbk(XplFunctionBody new_fbk) {
        XplFunctionBody back_fbk = p_fbk;
        p_fbk = new_fbk;
        if (p_fbk != null) {
            p_fbk.set_Name("fbk");
            p_fbk.set_Parent(this);
        }
        return back_fbk;
    }

    public static final XplExpression new_l() {
        XplExpression node = new XplExpression();
        node.set_Name("l");
        return node;
    }

    public static final XplCexpression new_ce() {
        XplCexpression node = new XplCexpression();
        node.set_Name("ce");
        return node;
    }

    public static final XplFunctionBody new_fbk() {
        XplFunctionBody node = new XplFunctionBody();
        node.set_Name("fbk");
        return node;
    }
}
