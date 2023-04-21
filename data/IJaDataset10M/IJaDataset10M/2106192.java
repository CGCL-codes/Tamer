package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplInitializerList extends XplNode implements XplNodeListCallbacks {

    boolean p_array;

    String p_doc;

    String p_helpURL;

    String p_ldsrc;

    boolean p_iny;

    String p_inydata;

    String p_inyby;

    String p_lddata;

    XplNodeList p_Childs;

    public XplInitializerList() {
        p_array = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
    }

    public XplInitializerList(boolean n_array) {
        p_array = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_array(n_array);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
    }

    public XplInitializerList(boolean n_array, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata) {
        set_array(n_array);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
    }

    public XplInitializerList(XplNodeList n_Childs) {
        p_array = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
        if (n_Childs != null) for (XplNode node = n_Childs.FirstNode(); node != null; node = n_Childs.NextNode()) {
            p_Childs.InsertAtEnd(node);
        }
    }

    public XplInitializerList(boolean n_array, XplNodeList n_Childs) {
        p_array = false;
        p_doc = "";
        p_helpURL = "";
        p_ldsrc = "";
        p_iny = false;
        p_inydata = "";
        p_inyby = "";
        p_lddata = "";
        set_array(n_array);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
        if (n_Childs != null) for (XplNode node = n_Childs.FirstNode(); node != null; node = n_Childs.NextNode()) {
            p_Childs.InsertAtEnd(node);
        }
    }

    public XplInitializerList(boolean n_array, String n_doc, String n_helpURL, String n_ldsrc, boolean n_iny, String n_inydata, String n_inyby, String n_lddata, XplNodeList n_Childs) {
        set_array(n_array);
        set_doc(n_doc);
        set_helpURL(n_helpURL);
        set_ldsrc(n_ldsrc);
        set_iny(n_iny);
        set_inydata(n_inydata);
        set_inyby(n_inyby);
        set_lddata(n_lddata);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
        if (n_Childs != null) for (XplNode node = n_Childs.FirstNode(); node != null; node = n_Childs.NextNode()) {
            p_Childs.InsertAtEnd(node);
        }
    }

    public XplNode Clone() {
        XplInitializerList copy = new XplInitializerList();
        copy.set_array(this.p_array);
        copy.set_doc(this.p_doc);
        copy.set_helpURL(this.p_helpURL);
        copy.set_ldsrc(this.p_ldsrc);
        copy.set_iny(this.p_iny);
        copy.set_inydata(this.p_inydata);
        copy.set_inyby(this.p_inyby);
        copy.set_lddata(this.p_lddata);
        for (XplNode node = p_Childs.FirstNode(); node != null; node = p_Childs.NextNode()) {
            copy.Childs().InsertAtEnd(node.Clone());
        }
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplInitializerList;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_array != false) writer.WriteAttributeString("array", CodeDOM_Utils.Att_ToString(p_array));
        if (p_doc != "") writer.WriteAttributeString("doc", CodeDOM_Utils.Att_ToString(p_doc));
        if (p_helpURL != "") writer.WriteAttributeString("helpURL", CodeDOM_Utils.Att_ToString(p_helpURL));
        if (p_ldsrc != "") writer.WriteAttributeString("ldsrc", CodeDOM_Utils.Att_ToString(p_ldsrc));
        if (p_iny != false) writer.WriteAttributeString("iny", CodeDOM_Utils.Att_ToString(p_iny));
        if (p_inydata != "") writer.WriteAttributeString("inydata", CodeDOM_Utils.Att_ToString(p_inydata));
        if (p_inyby != "") writer.WriteAttributeString("inyby", CodeDOM_Utils.Att_ToString(p_inyby));
        if (p_lddata != "") writer.WriteAttributeString("lddata", CodeDOM_Utils.Att_ToString(p_lddata));
        if (p_Childs != null) if (!p_Childs.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("array")) {
                    this.set_array(CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value()));
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
        p_Childs.Clear();
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("e")) {
                            tempNode = new XplExpression();
                            tempNode.Read(reader);
                            p_Childs.InsertAtEnd(tempNode);
                            break;
                        } else if (reader.Name().equals("list")) {
                            tempNode = new XplInitializerList();
                            tempNode.Read(reader);
                            p_Childs.InsertAtEnd(tempNode);
                            break;
                        } else {
                            throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nombre de nodo '" + reader.Name() + "' inesperado como hijo de elemento '" + this.get_Name() + "'.");
                        }
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

    public boolean get_array() {
        return p_array;
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

    public XplNodeList Childs() {
        return p_Childs;
    }

    public boolean set_array(boolean new_array) {
        boolean back_array = p_array;
        p_array = new_array;
        return back_array;
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

    public boolean InsertCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("e")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplExpression) {
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        if (node.get_Name().equals("list")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplInitializerList) {
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        return false;
    }

    public boolean RemoveNodeCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        return true;
    }

    public XplNodeList get_eNodeList() {
        XplNodeList new_List = new XplNodeList();
        for (XplNode node = p_Childs.FirstNode(); node != null; node = p_Childs.NextNode()) {
            if (node.get_Name().equals("e")) {
                new_List.InsertAtEnd(node);
            }
        }
        return new_List;
    }

    public XplNodeList get_listNodeList() {
        XplNodeList new_List = new XplNodeList();
        for (XplNode node = p_Childs.FirstNode(); node != null; node = p_Childs.NextNode()) {
            if (node.get_Name().equals("list")) {
                new_List.InsertAtEnd(node);
            }
        }
        return new_List;
    }

    public static final XplExpression new_e() {
        XplExpression node = new XplExpression();
        node.set_Name("e");
        return node;
    }

    public static final XplInitializerList new_list() {
        XplInitializerList node = new XplInitializerList();
        node.set_Name("list");
        return node;
    }
}
