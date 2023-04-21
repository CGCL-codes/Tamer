package archmapper.main.test;

import org.junit.Before;
import archmapper.main.conformance.MappingHelper;
import archmapper.main.model.ArchitectureMappingModel;
import archmapper.main.model.architecture.ArchitectureXMLDAO;
import archmapper.main.model.architecture.Component;
import archmapper.main.model.architecture.Configuration;
import archmapper.main.model.architecture.Connector;
import archmapper.main.model.architecture.Port;
import archmapper.main.model.architecture.Role;
import archmapper.main.model.archmapping.ArchitectureMapping;
import archmapper.main.model.archmapping.ArchitectureMappingDAO;
import archmapper.main.model.archmapping.ClassDefinition;
import archmapper.main.model.archmapping.ComponentMapping;
import archmapper.main.model.archmapping.ConnectorMapping;
import archmapper.main.model.archmapping.FileDefinition;
import archmapper.main.model.archmapping.PortMapping;
import archmapper.main.model.archmapping.StyleTypeInformation;
import archmapper.main.model.stylemapping.ClassType;
import archmapper.main.model.stylemapping.ComponentTypeMapping;
import archmapper.main.model.stylemapping.FileType;
import archmapper.main.model.stylemapping.PortTypeMapping;
import archmapper.main.model.stylemapping.RoleTypeMapping;
import archmapper.main.model.stylemapping.StyleMapping;
import archmapper.main.model.stylemapping.StyleMappingDAO;
import archmapper.main.model.stylemapping.RoleTypeMapping.RoleTypeDirection;

public class MappingTestBase {

    protected Configuration architecture;

    protected ArchitectureMapping archMapping;

    protected StyleMapping styleMapping;

    protected ArchitectureMappingModel model;

    protected MappingHelper mappingHelper;

    @Before
    public void setUp() {
        architecture = new Configuration();
        archMapping = new ArchitectureMapping();
        styleMapping = new StyleMapping();
        model = new ArchitectureMappingModel(architecture, styleMapping, archMapping);
        mappingHelper = new MappingHelper(model);
    }

    protected void createArchitecture() {
        architecture.setArchitecturalStyle("testStyle");
        architecture.setName("Test-Architecture");
        Component comp = new Component();
        comp.setParent(architecture);
        architecture.getComponents().add(comp);
        comp.setName("Comp1");
        comp.setStyleType("CompType1");
        architecture.getProperties().put("Prop", "Value");
        Port port = new Port();
        port.setParent(comp);
        comp.getPorts().add(port);
        port.setName("p");
        port.setStyleType("PortType1");
        Component comp2 = new Component();
        comp2.setParent(architecture);
        architecture.getComponents().add(comp2);
        comp2.setName("Comp2");
        comp2.setStyleType("CompType2");
        Port port2 = new Port();
        port2.setParent(comp2);
        comp2.getPorts().add(port2);
        port2.setName("p2");
        port2.setStyleType("PortType2");
        Connector conn = new Connector();
        architecture.getConnectors().add(conn);
        conn.setName("Connector1");
        conn.setStyleType("ConnType");
        Role role = new Role();
        role.setConnector(conn);
        conn.getRoles().add(role);
        role.setStyleType("CallerRole");
        port.getRoles().add(role);
        role.setPort(port);
        Role role2 = new Role();
        role2.setConnector(conn);
        conn.getRoles().add(role2);
        role2.setStyleType("CalleeRole");
        port2.getRoles().add(role2);
        role2.setPort(port2);
        model.setRoleDirectionInArchitecture(true);
    }

    protected void createArchitectureMapping() {
        ComponentMapping compMapping = new ComponentMapping();
        compMapping.setParent(archMapping);
        archMapping.getComponentMapping().add(compMapping);
        compMapping.setComponentName("Comp1");
        compMapping.setDefaultPackage("de.superpackage");
        ClassDefinition classDef = new ClassDefinition();
        classDef.setParent(compMapping);
        compMapping.getClassDefinition().add(classDef);
        classDef.setType("ClassType1");
        classDef.setClassName("ClassName1");
        FileDefinition fileDef = new FileDefinition();
        fileDef.setParent(compMapping);
        compMapping.getFileDefinition().add(fileDef);
        fileDef.setFilename("file1");
        fileDef.setType("filetype1");
        PortMapping portMapping = new PortMapping();
        compMapping.getPortMapping().add(portMapping);
        portMapping.setParent(compMapping);
        portMapping.setPortName("p");
        portMapping.getExposedClass().add(classDef);
        portMapping.getUsingClass().add(classDef);
        ComponentMapping comp2Mapping = new ComponentMapping();
        comp2Mapping.setComponentName("Comp2");
        archMapping.getComponentMapping().add(comp2Mapping);
        comp2Mapping.setParent(archMapping);
        ClassDefinition classDef2 = new ClassDefinition();
        comp2Mapping.getClassDefinition().add(classDef2);
        classDef2.setParent(comp2Mapping);
        classDef2.setClassName("Class2");
        classDef2.setType("ClassType2");
        ConnectorMapping connMapping = new ConnectorMapping();
        connMapping.setParent(archMapping);
        archMapping.getConnectorMapping().add(connMapping);
        connMapping.setConnectorName("Connector1");
        StyleTypeInformation stInfo = new StyleTypeInformation();
        archMapping.getStyleTypeInformation().add(stInfo);
        stInfo.setParent(archMapping);
        stInfo.setType("CompType2");
        stInfo.setDefaultPackage("com.testpackage");
    }

    protected void createStyleMapping() {
        styleMapping.setStyleName("testStyle");
        ComponentTypeMapping compT1Mapping = new ComponentTypeMapping();
        compT1Mapping.setTypeName("CompType1");
        styleMapping.getComponentTypeMapping().add(compT1Mapping);
        compT1Mapping.setExternal(true);
        FileType fileType1 = new FileType();
        compT1Mapping.getFileTypes().add(fileType1);
        fileType1.setFilenameEnding("txt");
        fileType1.setTemplateText("TestTemplate");
        fileType1.setOptional(true);
        fileType1.setTypeName("filetype1");
        ComponentTypeMapping compT2Mapping = new ComponentTypeMapping();
        compT2Mapping.setTypeName("CompType2");
        styleMapping.getComponentTypeMapping().add(compT2Mapping);
        compT2Mapping.setExternal(false);
        ClassType classType = new ClassType();
        compT2Mapping.getClassTypes().add(classType);
        classType.setTypeName("ClassType2");
        classType.setSingleton(true);
        PortTypeMapping ptMapping = new PortTypeMapping();
        styleMapping.getPortTypeMapping().add(ptMapping);
        ptMapping.setTypeName("PortType2");
        ptMapping.getExportedTypes().add(classType);
        RoleTypeMapping rtMapping1 = new RoleTypeMapping();
        styleMapping.getRoleTypeMapping().add(rtMapping1);
        rtMapping1.setTypeName("CalleeRole");
        rtMapping1.setDirection(RoleTypeDirection.in);
        RoleTypeMapping rtMapping2 = new RoleTypeMapping();
        styleMapping.getRoleTypeMapping().add(rtMapping2);
        rtMapping2.setTypeName("CallerRole");
        rtMapping2.setDirection(RoleTypeDirection.out);
        model.setRoleDirectionInArchitecture(true);
    }

    protected void loadFiles() {
        architecture = ArchitectureXMLDAO.read(MappingTestBase.class.getResourceAsStream("testArchitecture.arch.xml"));
        styleMapping = StyleMappingDAO.read(MappingTestBase.class.getResourceAsStream("testStyleMapping.xml"));
        archMapping = ArchitectureMappingDAO.read(MappingTestBase.class.getResourceAsStream("testArchMapping.xml"));
        model = new ArchitectureMappingModel(architecture, styleMapping, archMapping);
        mappingHelper = new MappingHelper(model);
    }
}
