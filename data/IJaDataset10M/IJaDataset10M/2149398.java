package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplFactoryTypesDatabase extends XplNode implements XplNodeListCallbacks {

    int p_version;

    XplNodeList p_Childs;

    public XplFactoryTypesDatabase() {
        p_version = 1;
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
    }

    public XplFactoryTypesDatabase(int n_version) {
        p_version = 1;
        set_version(n_version);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
    }

    public XplFactoryTypesDatabase(XplNodeList n_Childs) {
        p_version = 1;
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
        if (n_Childs != null) for (XplNode node = n_Childs.FirstNode(); node != null; node = n_Childs.NextNode()) {
            p_Childs.InsertAtEnd(node);
        }
    }

    public XplFactoryTypesDatabase(int n_version, XplNodeList n_Childs) {
        p_version = 1;
        set_version(n_version);
        p_Childs = new XplNodeList();
        p_Childs.set_Parent(this);
        p_Childs.set_CheckNodeCallback(this);
        if (n_Childs != null) for (XplNode node = n_Childs.FirstNode(); node != null; node = n_Childs.NextNode()) {
            p_Childs.InsertAtEnd(node);
        }
    }

    public XplNode Clone() {
        XplFactoryTypesDatabase copy = new XplFactoryTypesDatabase();
        copy.set_version(this.p_version);
        for (XplNode node = p_Childs.FirstNode(); node != null; node = p_Childs.NextNode()) {
            copy.Childs().InsertAtEnd(node.Clone());
        }
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplFactoryTypesDatabase;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_version != 1) writer.WriteAttributeString("version", CodeDOM_Utils.Att_ToString(p_version));
        if (p_Childs != null) if (!p_Childs.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("version")) {
                    this.set_version(CodeDOM_Utils.StringAtt_To_INT(reader.Value()));
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
                        if (reader.Name().equals("Classfactory")) {
                            tempNode = new XplClassfactoryData();
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

    public int get_version() {
        return p_version;
    }

    public XplNodeList Childs() {
        return p_Childs;
    }

    public int set_version(int new_version) {
        int back_version = p_version;
        p_version = new_version;
        return back_version;
    }

    public boolean InsertCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("Classfactory")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplClassfactoryData) {
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

    public XplNodeList get_ClassfactoryNodeList() {
        XplNodeList new_List = new XplNodeList();
        for (XplNode node = p_Childs.FirstNode(); node != null; node = p_Childs.NextNode()) {
            if (node.get_Name().equals("Classfactory")) {
                new_List.InsertAtEnd(node);
            }
        }
        return new_List;
    }

    public static final XplClassfactoryData new_Classfactory() {
        XplClassfactoryData node = new XplClassfactoryData();
        node.set_Name("Classfactory");
        return node;
    }
}
