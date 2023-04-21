package com.global360.sketchpadbpmn.documents;

import java.net.URL;
import javax.swing.DefaultComboBoxModel;
import org.jdom.Namespace;
import com.global360.sketchpadbpmn.SketchpadApplication;
import com.global360.sketchpadbpmn.Utility;

public class XpdlVersion extends VersionBase {

    private static Namespace xpdl_2_2_Alpha_Namespace = Namespace.getNamespace("http://www.wfmc.org/2009/XPDL2.2alpha");

    private static Namespace xpdl_2_2_Namespace = Namespace.getNamespace("http://www.wfmc.org/2009/XPDL2.2");

    private static Namespace xpdl_2_1_Namespace = Namespace.getNamespace("http://www.wfmc.org/2008/XPDL2.1");

    private static Namespace xpdl_2_0_Namespace = Namespace.getNamespace("http://www.wfmc.org/2004/XPDL2.0alpha");

    private static Namespace xpdl_1_Namespace = Namespace.getNamespace("http://www.wfmc.org/2002/XPDL1.0");

    public static final XpdlVersion V_2_2 = new XpdlVersion(2, 2, "SketchpadBPMN.schemaFileXPDL22", "SketchpadBPMN.defaultXPDL22SchemaFile", "schemata/bpmnxpdl_40a.xsd", xpdl_2_2_Namespace);

    public static final XpdlVersion V_2_2_A = new XpdlVersion(2, 2, "SketchpadBPMN.schemaFileXPDL22", "SketchpadBPMN.defaultXPDL22SchemaFile", "schemata/bpmnxpdl_39.xsd", xpdl_2_2_Alpha_Namespace);

    public static final XpdlVersion V_2_1 = new XpdlVersion(2, 1, "SketchpadBPMN.schemaFileXPDL21", "SketchpadBPMN.defaultXPDL21SchemaFile", "schemata/bpmnxpdl_31c.xsd", xpdl_2_1_Namespace);

    public static final XpdlVersion V_2_0 = new XpdlVersion(2, 0, "SketchpadBPMN.schemaFileXPDL20", "SketchpadBPMN.defaultXPDL20SchemaFile", "schemata/bpmnxpdl_24.xsd", xpdl_2_0_Namespace);

    public static final XpdlVersion V_1_0 = new XpdlVersion(1, 0, "SketchpadBPMN.schemaFileXPDL1", "SketchpadBPMN.defaultXPDL1SchemaFile", "schemata/TC-1025_schema_10_xpdl.xsd", xpdl_1_Namespace);

    private String schemaCustomPath = null;

    private String schemaPropertyName = null;

    private String schemaDefaultValue = null;

    private Namespace namespace = null;

    public static DefaultComboBoxModel getComboBoxModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        result.addElement(V_2_0);
        result.addElement(V_2_1);
        result.addElement(V_2_2);
        return result;
    }

    public static XpdlVersion findByNamespace(String namespace) {
        if (Utility.areEqual(namespace, V_2_2.getNamespace().getURI())) return V_2_2;
        if (Utility.areEqual(namespace, V_2_2_A.getNamespace().getURI())) return V_2_2_A;
        if (Utility.areEqual(namespace, V_2_1.getNamespace().getURI())) return V_2_1;
        if (Utility.areEqual(namespace, V_2_0.getNamespace().getURI())) return V_2_0;
        if (Utility.areEqual(namespace, V_1_0.getNamespace().getURI())) return V_1_0;
        return null;
    }

    public static XpdlVersion find(int major, int minor, char mod) {
        if (major == 1) return XpdlVersion.V_1_0;
        if (major == 2) {
            if (minor == 0) return XpdlVersion.V_2_0;
            if (minor == 1) return XpdlVersion.V_2_1;
            if (minor == 2) {
                if (mod == 'a') {
                    return XpdlVersion.V_2_2_A;
                }
                return XpdlVersion.V_2_2;
            }
        }
        return null;
    }

    public static XpdlVersion find(String versionString) {
        VersionBase temporary = new XpdlVersion(versionString);
        return XpdlVersion.find(temporary.majorVersion, temporary.minorVersion, temporary.modifier);
    }

    public static XpdlVersion find(BpmnVersion bpmnVersion) {
        switch(bpmnVersion.getMajor()) {
            case 2:
                return XpdlVersion.V_2_2;
            case 1:
                switch(bpmnVersion.getMinor()) {
                    case 2:
                        return XpdlVersion.V_2_1;
                    case 1:
                        return XpdlVersion.V_2_0;
                    case 0:
                        return XpdlVersion.V_1_0;
                }
        }
        return null;
    }

    private XpdlVersion(int major, int minor, String custompath, String propname, String defaultvalue, Namespace namespaceIn) {
        super(major, minor);
        this.schemaCustomPath = custompath;
        this.schemaPropertyName = propname;
        this.schemaDefaultValue = defaultvalue;
        this.namespace = namespaceIn;
    }

    private XpdlVersion(BpmnVersion bpmnVersion) {
        set(bpmnVersion);
    }

    private XpdlVersion(String versionString) {
        set(versionString);
    }

    public void set(BpmnVersion bpmnVersion) {
        if (bpmnVersion.isTwo()) {
            this.set(2, 2);
        } else {
            this.set(2, 1);
        }
    }

    public BpmnVersion asBpmnVersion() {
        if (this.is_2_2_or_greater()) return new BpmnVersion(BpmnVersion.BPMN_2_0);
        return new BpmnVersion(BpmnVersion.BPMN_1_2);
    }

    public String getSchemaPath() {
        String schemaFile = SketchpadApplication.getProperty(schemaCustomPath, "");
        if (schemaFile.length() == 0) {
            URL earl = com.global360.sketchpadbpmn.MainFrame.class.getResource(SketchpadApplication.getProperty(schemaPropertyName, schemaDefaultValue));
            if (earl != null) {
                schemaFile = earl.toString();
            }
        }
        return schemaFile;
    }

    public Namespace getNamespace() {
        return this.namespace;
    }

    public String getSchemaLocations() {
        if (this == XpdlVersion.V_2_2_A) {
            return XpdlVersion.V_2_2_A.getNamespace().getURI() + " " + XpdlVersion.V_2_2_A.getSchemaPath() + " " + XpdlVersion.V_2_1.getNamespace().getURI() + " " + XpdlVersion.V_2_1.getSchemaPath() + " " + XpdlVersion.V_2_0.getNamespace().getURI() + " " + XpdlVersion.V_2_0.getSchemaPath() + " " + XpdlVersion.V_1_0.getNamespace().getURI() + " " + XpdlVersion.V_1_0.getSchemaPath() + " ";
        }
        return XpdlVersion.V_2_2.getNamespace().getURI() + " " + XpdlVersion.V_2_2.getSchemaPath() + " " + XpdlVersion.V_2_1.getNamespace().getURI() + " " + XpdlVersion.V_2_1.getSchemaPath() + " " + XpdlVersion.V_2_0.getNamespace().getURI() + " " + XpdlVersion.V_2_0.getSchemaPath() + " " + XpdlVersion.V_1_0.getNamespace().getURI() + " " + XpdlVersion.V_1_0.getSchemaPath() + " ";
    }
}
