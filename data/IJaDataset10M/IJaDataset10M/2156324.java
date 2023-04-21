package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplDirectoutput extends XplNode {

    String p_TargetPlatform;

    String p_TargetPlatformDetail;

    String p_output;

    String p_doc;

    String p_helpURL;

    String p_ldsrc;

    boolean p_iny;

    String p_inydata;

    String p_inyby;

    String p_lddata;

    public XplDirectoutput() {
        p_TargetPlatform = "";
        p_TargetPlatformDetail = "";
        p_output = "";
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
    }

    public XplDirectoutput(String n_TargetPlatform, String n_TargetPlatformDetail, String n_output) {
        p_TargetPlatform = "";
        p_TargetPlatformDetail = "";
        p_output = "";
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_TargetPlatform(n_TargetPlatform);
        set_TargetPlatformDetail(n_TargetPlatformDetail);
        set_output(n_output);
    }

    public XplDirectoutput(String n_TargetPlatform, String n_TargetPlatformDetail, String n_output, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata) {
        set_TargetPlatform(n_TargetPlatform);
        set_TargetPlatformDetail(n_TargetPlatformDetail);
        set_output(n_output);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
    }

    public XplNode Clone() {
        XplDirectoutput copy = new XplDirectoutput();
        copy.set_TargetPlatform(this.p_TargetPlatform);
        copy.set_TargetPlatformDetail(this.p_TargetPlatformDetail);
        copy.set_output(this.p_output);
        copy.set_doc(this.p_doc);
        copy.set_helpURL(this.p_helpURL);
        copy.set_ldsrc(this.p_ldsrc);
        copy.set_iny(this.p_iny);
        copy.set_inydata(this.p_inydata);
        copy.set_inyby(this.p_inyby);
        copy.set_lddata(this.p_lddata);
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplDirectoutput;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_TargetPlatform != "") writer.WriteAttributeString("TargetPlatform", CodeDOM_Utils.Att_ToString(p_TargetPlatform));
        if (p_TargetPlatformDetail != "") writer.WriteAttributeString("TargetPlatformDetail", CodeDOM_Utils.Att_ToString(p_TargetPlatformDetail));
        if (p_output != "") writer.WriteAttributeString("output", CodeDOM_Utils.Att_ToString(p_output));
        if (p_doc != "") writer.WriteAttributeString("doc", CodeDOM_Utils.Att_ToString(p_doc));
        if (p_helpURL != "") writer.WriteAttributeString("helpURL", CodeDOM_Utils.Att_ToString(p_helpURL));
        if (p_ldsrc != "") writer.WriteAttributeString("ldsrc", CodeDOM_Utils.Att_ToString(p_ldsrc));
        if (p_iny != false) writer.WriteAttributeString("iny", CodeDOM_Utils.Att_ToString(p_iny));
        if (p_inydata != "") writer.WriteAttributeString("inydata", CodeDOM_Utils.Att_ToString(p_inydata));
        if (p_inyby != "") writer.WriteAttributeString("inyby", CodeDOM_Utils.Att_ToString(p_inyby));
        if (p_lddata != "") writer.WriteAttributeString("lddata", CodeDOM_Utils.Att_ToString(p_lddata));
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("TargetPlatform")) {
                    this.set_TargetPlatform(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("TargetPlatformDetail")) {
                    this.set_TargetPlatformDetail(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("output")) {
                    this.set_output(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
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
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                    case XmlNodeType.TEXT:
                        throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".No se esperaba texto o elemento hijo en este nodo.");
                    case XmlNodeType.ENDELEMENT:
                        break;
                    default:
                        break;
                }
                reader.Read();
            }
        }
        return this;
    }

    public String get_TargetPlatform() {
        return p_TargetPlatform;
    }

    public String get_TargetPlatformDetail() {
        return p_TargetPlatformDetail;
    }

    public String get_output() {
        return p_output;
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

    public String set_TargetPlatform(String new_TargetPlatform) {
        String back_TargetPlatform = p_TargetPlatform;
        p_TargetPlatform = new_TargetPlatform;
        return back_TargetPlatform;
    }

    public String set_TargetPlatformDetail(String new_TargetPlatformDetail) {
        String back_TargetPlatformDetail = p_TargetPlatformDetail;
        p_TargetPlatformDetail = new_TargetPlatformDetail;
        return back_TargetPlatformDetail;
    }

    public String set_output(String new_output) {
        String back_output = p_output;
        p_output = new_output;
        return back_output;
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
}
