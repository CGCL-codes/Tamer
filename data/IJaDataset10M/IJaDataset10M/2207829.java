package LayerD.CodeDOM;

import LayerD.CodeDOM.XplParser.ParseException;
import java.io.*;
import java.util.*;
import java.text.*;

public class XplLayerDZoeProgramConfig extends XplNode implements XplNodeListCallbacks {

    String p_name;

    int p_moduleType;

    String p_defaultPlatform;

    String p_mainProcedureFileName;

    String p_defaultOutputFileName;

    XplNodeList p_SourceFile;

    XplNodeList p_OutputPlatform;

    XplNodeList p_PlatformAlias;

    XplLayerDZoeProgramRequirements p_ProgramRequirements;

    public XplLayerDZoeProgramConfig() {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType) {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        set_name(n_name);
        set_moduleType(n_moduleType);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
    }

    public XplLayerDZoeProgramConfig(XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
        if (n_SourceFile != null) for (XplNode node = n_SourceFile.FirstNode(); node != null; node = n_SourceFile.NextNode()) {
            p_SourceFile.InsertAtEnd(node);
        }
        if (n_OutputPlatform != null) for (XplNode node = n_OutputPlatform.FirstNode(); node != null; node = n_OutputPlatform.NextNode()) {
            p_OutputPlatform.InsertAtEnd(node);
        }
        set_ProgramRequirements(n_ProgramRequirements);
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType, XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        set_name(n_name);
        set_moduleType(n_moduleType);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
        if (n_SourceFile != null) for (XplNode node = n_SourceFile.FirstNode(); node != null; node = n_SourceFile.NextNode()) {
            p_SourceFile.InsertAtEnd(node);
        }
        if (n_OutputPlatform != null) for (XplNode node = n_OutputPlatform.FirstNode(); node != null; node = n_OutputPlatform.NextNode()) {
            p_OutputPlatform.InsertAtEnd(node);
        }
        set_ProgramRequirements(n_ProgramRequirements);
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType, String n_defaultPlatform, String n_mainProcedureFileName, String n_defaultOutputFileName) {
        set_name(n_name);
        set_moduleType(n_moduleType);
        set_defaultPlatform(n_defaultPlatform);
        set_mainProcedureFileName(n_mainProcedureFileName);
        set_defaultOutputFileName(n_defaultOutputFileName);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
    }

    public XplLayerDZoeProgramConfig(XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplNodeList n_PlatformAlias, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
        if (n_SourceFile != null) for (XplNode node = n_SourceFile.FirstNode(); node != null; node = n_SourceFile.NextNode()) {
            p_SourceFile.InsertAtEnd(node);
        }
        if (n_OutputPlatform != null) for (XplNode node = n_OutputPlatform.FirstNode(); node != null; node = n_OutputPlatform.NextNode()) {
            p_OutputPlatform.InsertAtEnd(node);
        }
        if (n_PlatformAlias != null) for (XplNode node = n_PlatformAlias.FirstNode(); node != null; node = n_PlatformAlias.NextNode()) {
            p_PlatformAlias.InsertAtEnd(node);
        }
        set_ProgramRequirements(n_ProgramRequirements);
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType, String n_defaultPlatform, String n_mainProcedureFileName, String n_defaultOutputFileName, XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        set_name(n_name);
        set_moduleType(n_moduleType);
        set_defaultPlatform(n_defaultPlatform);
        set_mainProcedureFileName(n_mainProcedureFileName);
        set_defaultOutputFileName(n_defaultOutputFileName);
        set_name(n_name);
        set_moduleType(n_moduleType);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType, XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplNodeList n_PlatformAlias, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        p_name = "";
        p_moduleType = XplLayerDZoeProgramModuletype_enum.EXECUTABLE;
        p_defaultPlatform = "";
        p_mainProcedureFileName = "";
        p_defaultOutputFileName = "";
        set_name(n_name);
        set_moduleType(n_moduleType);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
        if (n_SourceFile != null) for (XplNode node = n_SourceFile.FirstNode(); node != null; node = n_SourceFile.NextNode()) {
            p_SourceFile.InsertAtEnd(node);
        }
        if (n_OutputPlatform != null) for (XplNode node = n_OutputPlatform.FirstNode(); node != null; node = n_OutputPlatform.NextNode()) {
            p_OutputPlatform.InsertAtEnd(node);
        }
        if (n_PlatformAlias != null) for (XplNode node = n_PlatformAlias.FirstNode(); node != null; node = n_PlatformAlias.NextNode()) {
            p_PlatformAlias.InsertAtEnd(node);
        }
        set_ProgramRequirements(n_ProgramRequirements);
    }

    public XplLayerDZoeProgramConfig(String n_name, int n_moduleType, String n_defaultPlatform, String n_mainProcedureFileName, String n_defaultOutputFileName, XplNodeList n_SourceFile, XplNodeList n_OutputPlatform, XplNodeList n_PlatformAlias, XplLayerDZoeProgramRequirements n_ProgramRequirements) {
        set_name(n_name);
        set_moduleType(n_moduleType);
        set_defaultPlatform(n_defaultPlatform);
        set_mainProcedureFileName(n_mainProcedureFileName);
        set_defaultOutputFileName(n_defaultOutputFileName);
        p_SourceFile = new XplNodeList();
        p_SourceFile.set_Parent(this);
        p_SourceFile.set_CheckNodeCallback(this);
        p_OutputPlatform = new XplNodeList();
        p_OutputPlatform.set_Parent(this);
        p_OutputPlatform.set_CheckNodeCallback(this);
        p_PlatformAlias = new XplNodeList();
        p_PlatformAlias.set_Parent(this);
        p_PlatformAlias.set_CheckNodeCallback(this);
        p_ProgramRequirements = null;
        if (n_SourceFile != null) for (XplNode node = n_SourceFile.FirstNode(); node != null; node = n_SourceFile.NextNode()) {
            p_SourceFile.InsertAtEnd(node);
        }
        if (n_OutputPlatform != null) for (XplNode node = n_OutputPlatform.FirstNode(); node != null; node = n_OutputPlatform.NextNode()) {
            p_OutputPlatform.InsertAtEnd(node);
        }
        if (n_PlatformAlias != null) for (XplNode node = n_PlatformAlias.FirstNode(); node != null; node = n_PlatformAlias.NextNode()) {
            p_PlatformAlias.InsertAtEnd(node);
        }
        set_ProgramRequirements(n_ProgramRequirements);
    }

    public XplNode Clone() {
        XplLayerDZoeProgramConfig copy = new XplLayerDZoeProgramConfig();
        copy.set_name(this.p_name);
        copy.set_moduleType(this.p_moduleType);
        copy.set_defaultPlatform(this.p_defaultPlatform);
        copy.set_mainProcedureFileName(this.p_mainProcedureFileName);
        copy.set_defaultOutputFileName(this.p_defaultOutputFileName);
        for (XplNode node = p_SourceFile.FirstNode(); node != null; node = p_SourceFile.NextNode()) {
            copy.get_SourceFile().InsertAtEnd(node.Clone());
        }
        for (XplNode node = p_OutputPlatform.FirstNode(); node != null; node = p_OutputPlatform.NextNode()) {
            copy.get_OutputPlatform().InsertAtEnd(node.Clone());
        }
        for (XplNode node = p_PlatformAlias.FirstNode(); node != null; node = p_PlatformAlias.NextNode()) {
            copy.get_PlatformAlias().InsertAtEnd(node.Clone());
        }
        if (p_ProgramRequirements != null) copy.set_ProgramRequirements((XplLayerDZoeProgramRequirements) p_ProgramRequirements.Clone());
        copy.set_Name(this.get_Name());
        return (XplNode) copy;
    }

    public int get_TypeName() {
        return CodeDOMTypes.XplLayerDZoeProgramConfig;
    }

    public boolean Write(XplWriter writer) throws IOException, CodeDOM_Exception {
        boolean result = true;
        writer.WriteStartElement(this.get_Name());
        if (p_name != "") writer.WriteAttributeString("name", CodeDOM_Utils.Att_ToString(p_name));
        if (p_moduleType != XplLayerDZoeProgramModuletype_enum.EXECUTABLE) writer.WriteAttributeString("moduleType", CodeDOM_STV.XPLLAYERDZOEPROGRAMMODULETYPE_ENUM[(int) p_moduleType]);
        if (p_defaultPlatform != "") writer.WriteAttributeString("defaultPlatform", CodeDOM_Utils.Att_ToString(p_defaultPlatform));
        if (p_mainProcedureFileName != "") writer.WriteAttributeString("mainProcedureFileName", CodeDOM_Utils.Att_ToString(p_mainProcedureFileName));
        if (p_defaultOutputFileName != "") writer.WriteAttributeString("defaultOutputFileName", CodeDOM_Utils.Att_ToString(p_defaultOutputFileName));
        if (p_SourceFile != null) if (!p_SourceFile.Write(writer)) result = false;
        if (p_OutputPlatform != null) if (!p_OutputPlatform.Write(writer)) result = false;
        if (p_PlatformAlias != null) if (!p_PlatformAlias.Write(writer)) result = false;
        if (p_ProgramRequirements != null) if (!p_ProgramRequirements.Write(writer)) result = false;
        writer.WriteEndElement();
        return result;
    }

    public XplNode Read(XplReader reader) throws ParseException, CodeDOM_Exception, IOException {
        this.set_Name(reader.Name());
        if (reader.HasAttributes()) {
            String tmpStr = "";
            boolean flag = false;
            int count = 0;
            for (int i = 1; i <= reader.AttributeCount(); i++) {
                reader.MoveToAttribute(i);
                if (reader.Name().equals("name")) {
                    this.set_name(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("moduleType")) {
                    tmpStr = CodeDOM_Utils.StringAtt_To_STRING(reader.Value());
                    count = 0;
                    flag = false;
                    for (int n = 0; n < CodeDOM_STV.XPLLAYERDZOEPROGRAMMODULETYPE_ENUM.length; n++) {
                        String str = CodeDOM_STV.XPLLAYERDZOEPROGRAMMODULETYPE_ENUM[n];
                        if (str.equals(tmpStr)) {
                            this.set_moduleType(count);
                            flag = true;
                            break;
                        }
                        count++;
                    }
                } else if (reader.Name().equals("defaultPlatform")) {
                    this.set_defaultPlatform(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("mainProcedureFileName")) {
                    this.set_mainProcedureFileName(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else if (reader.Name().equals("defaultOutputFileName")) {
                    this.set_defaultOutputFileName(CodeDOM_Utils.StringAtt_To_STRING(reader.Value()));
                } else {
                    throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Atributo '" + reader.Name() + "' invalido en elemento '" + this.get_Name() + "'.");
                }
            }
            reader.MoveToElement();
        }
        this.p_ProgramRequirements = null;
        if (!reader.IsEmptyElement()) {
            reader.Read();
            while (reader.NodeType() != XmlNodeType.ENDELEMENT) {
                XplNode tempNode = null;
                switch(reader.NodeType()) {
                    case XmlNodeType.ELEMENT:
                        if (reader.Name().equals("SourceFile")) {
                            tempNode = new XplSourceFile();
                            tempNode.Read(reader);
                            this.get_SourceFile().InsertAtEnd(tempNode);
                        } else if (reader.Name().equals("OutputPlatform")) {
                            tempNode = new XplTargetPlatform();
                            tempNode.Read(reader);
                            this.get_OutputPlatform().InsertAtEnd(tempNode);
                        } else if (reader.Name().equals("PlatformAlias")) {
                            tempNode = new XplPlatformAlias();
                            tempNode.Read(reader);
                            this.get_PlatformAlias().InsertAtEnd(tempNode);
                        } else if (reader.Name().equals("ProgramRequirements")) {
                            tempNode = new XplLayerDZoeProgramRequirements();
                            tempNode.Read(reader);
                            if (this.get_ProgramRequirements() != null) throw new CodeDOM_Exception("Linea: " + reader.LineNumber() + ".Nodo '" + reader.Name() + "' repetido como hijo de elemento '" + this.get_Name() + "'.");
                            this.set_ProgramRequirements((XplLayerDZoeProgramRequirements) tempNode);
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

    public String get_name() {
        return p_name;
    }

    public int get_moduleType() {
        return p_moduleType;
    }

    public String get_defaultPlatform() {
        return p_defaultPlatform;
    }

    public String get_mainProcedureFileName() {
        return p_mainProcedureFileName;
    }

    public String get_defaultOutputFileName() {
        return p_defaultOutputFileName;
    }

    public XplNodeList get_SourceFile() {
        return p_SourceFile;
    }

    public XplNodeList get_OutputPlatform() {
        return p_OutputPlatform;
    }

    public XplNodeList get_PlatformAlias() {
        return p_PlatformAlias;
    }

    public XplLayerDZoeProgramRequirements get_ProgramRequirements() {
        return p_ProgramRequirements;
    }

    public String set_name(String new_name) {
        String back_name = p_name;
        p_name = new_name;
        return back_name;
    }

    public int set_moduleType(int new_moduleType) {
        int back_moduleType = p_moduleType;
        p_moduleType = new_moduleType;
        return back_moduleType;
    }

    public String set_defaultPlatform(String new_defaultPlatform) {
        String back_defaultPlatform = p_defaultPlatform;
        p_defaultPlatform = new_defaultPlatform;
        return back_defaultPlatform;
    }

    public String set_mainProcedureFileName(String new_mainProcedureFileName) {
        String back_mainProcedureFileName = p_mainProcedureFileName;
        p_mainProcedureFileName = new_mainProcedureFileName;
        return back_mainProcedureFileName;
    }

    public String set_defaultOutputFileName(String new_defaultOutputFileName) {
        String back_defaultOutputFileName = p_defaultOutputFileName;
        p_defaultOutputFileName = new_defaultOutputFileName;
        return back_defaultOutputFileName;
    }

    public boolean InsertCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        if (nodeList == p_SourceFile) return acceptInsertNodeCallback_SourceFile(node, parent);
        if (nodeList == p_OutputPlatform) return acceptInsertNodeCallback_OutputPlatform(node, parent);
        if (nodeList == p_PlatformAlias) return acceptInsertNodeCallback_PlatformAlias(node, parent);
        return false;
    }

    public boolean RemoveNodeCallback(XplNodeList nodeList, XplNode node, XplNode parent) {
        return true;
    }

    public boolean acceptInsertNodeCallback_SourceFile(XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("SourceFile")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplSourceFile) {
                this.set_ErrorString("El elemento de tipo '" + node.get_ContentTypeName() + "' no es valido como componente de 'XplSourceFile'.");
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        this.set_ErrorString("El elemento de nombre '" + node.get_Name() + "' no es valido como componente de 'XplSourceFile'.");
        return false;
    }

    public boolean acceptInsertNodeCallback_OutputPlatform(XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("OutputPlatform")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplTargetPlatform) {
                this.set_ErrorString("El elemento de tipo '" + node.get_ContentTypeName() + "' no es valido como componente de 'XplTargetPlatform'.");
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        this.set_ErrorString("El elemento de nombre '" + node.get_Name() + "' no es valido como componente de 'XplTargetPlatform'.");
        return false;
    }

    public boolean acceptInsertNodeCallback_PlatformAlias(XplNode node, XplNode parent) {
        if (node == null) return false;
        if (node.get_Name().equals("PlatformAlias")) {
            if (node.get_ContentTypeName() != CodeDOMTypes.XplPlatformAlias) {
                this.set_ErrorString("El elemento de tipo '" + node.get_ContentTypeName() + "' no es valido como componente de 'XplPlatformAlias'.");
                return false;
            }
            node.set_Parent(parent);
            return true;
        }
        this.set_ErrorString("El elemento de nombre '" + node.get_Name() + "' no es valido como componente de 'XplPlatformAlias'.");
        return false;
    }

    public XplLayerDZoeProgramRequirements set_ProgramRequirements(XplLayerDZoeProgramRequirements new_ProgramRequirements) {
        XplLayerDZoeProgramRequirements back_ProgramRequirements = p_ProgramRequirements;
        p_ProgramRequirements = new_ProgramRequirements;
        if (p_ProgramRequirements != null) {
            p_ProgramRequirements.set_Name("ProgramRequirements");
            p_ProgramRequirements.set_Parent(this);
        }
        return back_ProgramRequirements;
    }

    public static final XplSourceFile new_SourceFile() {
        XplSourceFile node = new XplSourceFile();
        node.set_Name("SourceFile");
        return node;
    }

    public static final XplTargetPlatform new_OutputPlatform() {
        XplTargetPlatform node = new XplTargetPlatform();
        node.set_Name("OutputPlatform");
        return node;
    }

    public static final XplPlatformAlias new_PlatformAlias() {
        XplPlatformAlias node = new XplPlatformAlias();
        node.set_Name("PlatformAlias");
        return node;
    }

    public static final XplLayerDZoeProgramRequirements new_ProgramRequirements() {
        XplLayerDZoeProgramRequirements node = new XplLayerDZoeProgramRequirements();
        node.set_Name("ProgramRequirements");
        return node;
    }
}
