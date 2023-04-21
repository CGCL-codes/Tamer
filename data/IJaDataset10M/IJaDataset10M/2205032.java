package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.lang.*;
import java.io.*;
import java.util.*;

public class XplNode {

    String p_Name;

    String p_errorString;

    int p_nodeType;

    Object p_value;

    XplNode p_parent;

    public XplNode() {
        p_nodeType = XplNodeType_enum.COMPLEX;
    }

    public XplNode(String new_Name) {
        p_nodeType = XplNodeType_enum.COMPLEX;
        p_Name = new_Name;
    }

    public XplNode(int new_nodeType) {
        p_nodeType = new_nodeType;
    }

    public XplNode(String new_Name, String new_stringValue) {
        p_Name = new_Name;
        p_value = new_stringValue;
        p_nodeType = XplNodeType_enum.STRING;
    }

    public XplNode(String new_Name, int new_intValue) {
        p_Name = new_Name;
        p_value = new_intValue;
        p_nodeType = XplNodeType_enum.INT;
    }

    public XplNode(String new_Name, Boolean new_boolValue) {
        p_Name = new_Name;
        p_value = new_boolValue;
        p_nodeType = XplNodeType_enum.BOOL;
    }

    public void set_Parent(XplNode new_Parent) {
        p_parent = new_Parent;
    }

    public XplNode get_Parent() {
        return p_parent;
    }

    public void set_Value(String new_stringValue) {
        if (p_nodeType == XplNodeType_enum.STRING) {
            p_value = new_stringValue;
        }
    }

    public void set_Value(int new_value) {
        if (p_nodeType == XplNodeType_enum.INT) {
            p_value = new_value;
        }
    }

    public void set_Value(Object new_value) {
        p_value = new_value;
    }

    public String get_stringValue() {
        return (String) p_value;
    }

    public int get_intValue() {
        return (Integer) p_value;
    }

    public Boolean get_boolValue() {
        return (Boolean) p_value;
    }

    public Date get_dateTimeValue() {
        return (Date) p_value;
    }

    public void set_Name(String new_Name) {
        p_Name = new_Name;
    }

    public String get_Name() {
        return p_Name;
    }

    public String get_ErrorString() {
        return p_errorString;
    }

    public void set_ErrorString(String new_errorString) {
        p_errorString = new_errorString;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplNode;
    }

    public int get_ContentTypeName() {
        if (p_nodeType == XplNodeType_enum.COMPLEX) return get_TypeName();
        switch(p_nodeType) {
            case XplNodeType_enum.STRING:
                return CodeDOMTypes.String;
            case XplNodeType_enum.INT:
                return CodeDOMTypes.Integer;
            case XplNodeType_enum.DATETIME:
                return CodeDOMTypes.DateTime;
            case XplNodeType_enum.BOOL:
                return CodeDOMTypes.Boolean;
            case XplNodeType_enum.EMPTY:
                return CodeDOMTypes.Empty;
            default:
                return CodeDOMTypes.Empty;
        }
    }

    public XplNode Clone() {
        try {
            return (XplNode) clone();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        switch(p_nodeType) {
            case XplNodeType_enum.STRING:
                writer.WriteElementString(p_Name, CodeDOM_Utils.Att_ToString((String) p_value));
                break;
            case XplNodeType_enum.INT:
                writer.WriteElementString(p_Name, CodeDOM_Utils.Att_ToString((Integer) p_value));
                break;
            case XplNodeType_enum.UNSIGNED:
                writer.WriteElementString(p_Name, CodeDOM_Utils.Att_ToString((Integer) p_value));
                break;
            case XplNodeType_enum.DATETIME:
                writer.WriteElementString(p_Name, CodeDOM_Utils.Att_ToString((Date) p_value));
                break;
            case XplNodeType_enum.BOOL:
                writer.WriteElementString(p_Name, CodeDOM_Utils.Att_ToString((Boolean) p_value));
                break;
            case XplNodeType_enum.EMPTY:
                writer.WriteElementString(p_Name, "");
                break;
            default:
                throw new CodeDOM_Exception("Error grabando un nodo basico");
        }
        ;
        return true;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        p_Name = reader.Name();
        reader.Read();
        while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
            switch(reader.NodeType()) {
                case XmlNodeType.ELEMENT:
                case XmlNodeType.ENDELEMENT:
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".No se esperaba elemento hijo en este nodo.");
                case XmlNodeType.TEXT:
                    switch(p_nodeType) {
                        case XplNodeType_enum.STRING:
                            p_value = reader.Value();
                            break;
                        case XplNodeType_enum.INT:
                            p_value = (Integer) CodeDOM_Utils.StringAtt_To_INT(reader.Value());
                            break;
                        case XplNodeType_enum.UNSIGNED:
                            p_value = (Integer) CodeDOM_Utils.StringAtt_To_INT(reader.Value());
                            break;
                        case XplNodeType_enum.DATETIME:
                            p_value = CodeDOM_Utils.StringAtt_To_DATE(reader.Value());
                            break;
                        case XplNodeType_enum.BOOL:
                            p_value = (Boolean) CodeDOM_Utils.StringAtt_To_BOOLEAN(reader.Value());
                            break;
                        case XplNodeType_enum.EMPTY:
                            break;
                    }
                    ;
                    break;
                default:
                    break;
            }
            reader.Read();
        }
        return this;
    }

    public XplNodeList Childs() {
        return null;
    }

    public String set_doc(String new_doc) {
        return null;
    }

    public String set_helpURL(String new_helpURL) {
        return null;
    }

    public String set_ldsrc(String new_ldsrc) {
        return null;
    }

    public boolean set_iny(boolean new_iny) {
        return false;
    }

    public String set_inydata(String new_inydata) {
        return null;
    }

    public String set_inyby(String new_inyby) {
        return null;
    }

    public String set_lddata(String new_lddata) {
        return null;
    }

    public String get_doc() {
        return null;
    }

    public String get_helpURL() {
        return null;
    }

    public String get_ldsrc() {
        return null;
    }

    public boolean get_iny() {
        return false;
    }

    public String get_inydata() {
        return null;
    }

    public String get_inyby() {
        return null;
    }

    public String get_lddata() {
        return null;
    }

    String get_ContentTypeNameString() {
        return "XplNode";
    }
}
