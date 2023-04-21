package edu.psu.gv.eadvisor.dao.hibernate;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.BeforeClass;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBeanByName;
import edu.psu.gv.eadvisor.dao.*;
import edu.psu.gv.eadvisor.domain.*;
import java.util.List;
import java.util.Set;
import static junit.framework.Assert.assertEquals;

/**
 * @author eadvisor Team
 */
@RunWith(UnitilsJUnit4TestClassRunner.class)
@SpringApplicationContext({ "applicationContext.xml" })
public class DegreeDaoHibernateTest {

    @SpringBeanByName
    private DepartmentDao departmentDao;

    @SpringBeanByName
    private CourseDao courseDao;

    @SpringBeanByName
    private RequirementDao requirementDao;

    @SpringBeanByName
    private ElectiveModuleDao electiveModuleDao;

    @SpringBeanByName
    private DegreeDao degreeDao;

    private static Degree softwareEngineeringDegree;

    private static ElectiveRequirement electiveRequirementCompleteOne;

    private static ElectiveModule requirementsEngineeringModule;

    private static ElectiveModule globalSoftwareEngineeringModule;

    private static ElectiveModule softwareArchitectureModule;

    private static Department softwareEngineeringDepartment;

    private static Department systemsEngineeringDepartment;

    private static Department informationScienceDepartment;

    private static Course softwareSystemDesign;

    private static Course advancedSoftwareEngineering;

    private static Course softwareTesting;

    private static Course softwareDocumentation;

    private static Course technicalProjectManagement;

    private static Course softwareProjectManagement;

    private static Course advancedSoftwareEngineeringStudio;

    private static Course mastersResearchPaper;

    private static Course businessProcessManagement;

    private static Course humanComputerInterface;

    private static Course softwareProcesses;

    private static Course requirementsEngineering;

    private static Course formalMethods;

    private static Course systemsThinking;

    private static Course knowledgeManagement;

    private static Course softwareProductLines;

    private static Course globalSoftwareDevelopment;

    private static Course softwareSystemArchitecture;

    private static Course serviceOrientedArchitecture;

    private static Course enterpriseIntegration;

    private static Course patternOrientedDesign;

    private static Course enterpriseServiceComputing;

    private static Course programUnderstanding;

    private static CoreRequirement softwareSystemDesignRequirement;

    private static CoreRequirement advancedSoftwareEngineeringRequirement;

    private static CoreRequirement softwareTestingRequirement;

    private static CoreRequirement softwareDocumentationRequirement;

    private static CoreRequirement managementRequirement;

    private static CoreRequirement capstone;

    @Before
    public void setUp() {
        for (Degree d : degreeDao.getAll()) {
            degreeDao.delete(d);
        }
        for (ElectiveModule em : electiveModuleDao.getAll()) {
            electiveModuleDao.delete(em);
        }
        for (Requirement r : requirementDao.getAll()) {
            requirementDao.delete(r);
        }
        for (Course c : courseDao.getAll()) {
            courseDao.delete(c);
        }
        for (Department d : departmentDao.getAll()) {
            departmentDao.delete(d);
        }
        departmentDao.save(softwareEngineeringDepartment);
        departmentDao.save(systemsEngineeringDepartment);
        departmentDao.save(informationScienceDepartment);
        courseDao.save(softwareSystemDesign);
        courseDao.save(advancedSoftwareEngineering);
        courseDao.save(softwareTesting);
        courseDao.save(softwareDocumentation);
        courseDao.save(technicalProjectManagement);
        courseDao.save(softwareProjectManagement);
        courseDao.save(advancedSoftwareEngineeringStudio);
        courseDao.save(mastersResearchPaper);
        courseDao.save(businessProcessManagement);
        courseDao.save(humanComputerInterface);
        courseDao.save(softwareProcesses);
        courseDao.save(requirementsEngineering);
        courseDao.save(formalMethods);
        courseDao.save(systemsThinking);
        courseDao.save(knowledgeManagement);
        courseDao.save(softwareProductLines);
        courseDao.save(globalSoftwareDevelopment);
        courseDao.save(softwareSystemArchitecture);
        courseDao.save(serviceOrientedArchitecture);
        courseDao.save(enterpriseIntegration);
        courseDao.save(patternOrientedDesign);
        courseDao.save(enterpriseServiceComputing);
        courseDao.save(programUnderstanding);
        requirementDao.save(softwareSystemDesignRequirement);
        requirementDao.save(advancedSoftwareEngineeringRequirement);
        requirementDao.save(softwareTestingRequirement);
        requirementDao.save(softwareDocumentationRequirement);
        requirementDao.save(managementRequirement);
        requirementDao.save(capstone);
        electiveModuleDao.save(requirementsEngineeringModule);
        electiveModuleDao.save(globalSoftwareEngineeringModule);
        electiveModuleDao.save(softwareArchitectureModule);
        requirementDao.save(electiveRequirementCompleteOne);
    }

    @BeforeClass
    public static void oneTimeSetUp() {
        setUpDepartments();
        setUpCoreCourses();
        setUpRequirementsEngineering();
        setUpGlobalSoftwareEngineering();
        setUpSoftwareArchitectureModule();
        setUpSoftwareEngineeringDegree();
    }

    private static void setUpDepartments() {
        softwareEngineeringDepartment = new Department();
        softwareEngineeringDepartment.setName("Software Engineering");
        softwareEngineeringDepartment.setCode("SWENG");
        systemsEngineeringDepartment = new Department();
        systemsEngineeringDepartment.setName("System Engineering");
        systemsEngineeringDepartment.setCode("SYSEN");
        informationScienceDepartment = new Department();
        informationScienceDepartment.setName("Information Science");
        informationScienceDepartment.setCode("INSC");
    }

    private static void setUpCoreCourses() {
        softwareSystemDesign = new Course();
        softwareSystemDesign.setName("Software System Design");
        softwareSystemDesign.setNumber("537");
        softwareSystemDesign.setDepartment(softwareEngineeringDepartment);
        softwareSystemDesign.setCredits(3.0f);
        advancedSoftwareEngineering = new Course();
        advancedSoftwareEngineering.setName("Advanced Software Engineering");
        advancedSoftwareEngineering.setNumber("580");
        advancedSoftwareEngineering.setDepartment(softwareEngineeringDepartment);
        advancedSoftwareEngineering.setCredits(3.0f);
        softwareTesting = new Course();
        softwareTesting.setName("Software Testing");
        softwareTesting.setNumber("581");
        softwareTesting.setDepartment(softwareEngineeringDepartment);
        softwareTesting.setCredits(3.0f);
        softwareDocumentation = new Course();
        softwareDocumentation.setName("Special Topics: Software Documentation");
        softwareDocumentation.setNumber("497B");
        softwareDocumentation.setDepartment(softwareEngineeringDepartment);
        softwareDocumentation.setCredits(3.0f);
        technicalProjectManagement = new Course();
        technicalProjectManagement.setName("Technical Project Management");
        technicalProjectManagement.setNumber("505");
        technicalProjectManagement.setDepartment(systemsEngineeringDepartment);
        technicalProjectManagement.setCredits(3.0f);
        softwareProjectManagement = new Course();
        softwareProjectManagement.setName("Software Project Management");
        softwareProjectManagement.setNumber("505");
        softwareProjectManagement.setDepartment(softwareEngineeringDepartment);
        softwareProjectManagement.setCredits(3.0f);
        advancedSoftwareEngineeringStudio = new Course();
        advancedSoftwareEngineeringStudio.setName("Advanced Software Engineering Studio");
        advancedSoftwareEngineeringStudio.setNumber("500");
        advancedSoftwareEngineeringStudio.setDepartment(softwareEngineeringDepartment);
        advancedSoftwareEngineeringStudio.setCredits(3.0f);
        mastersResearchPaper = new Course();
        mastersResearchPaper.setName("Masters Research Paper");
        mastersResearchPaper.setNumber("594A");
        mastersResearchPaper.setDepartment(softwareEngineeringDepartment);
        mastersResearchPaper.setCredits(3.0f);
    }

    private static void setUpRequirementsEngineering() {
        requirementsEngineeringModule = new ElectiveModule();
        requirementsEngineeringModule.setName("Requirements Engineering Module");
        businessProcessManagement = new Course();
        businessProcessManagement.setName("Special Topics: Business Process Management");
        businessProcessManagement.setNumber("597");
        businessProcessManagement.setDepartment(informationScienceDepartment);
        businessProcessManagement.setCredits(3f);
        humanComputerInterface = new Course();
        humanComputerInterface.setName("Special Topics: Human Computer Interface");
        humanComputerInterface.setNumber("497");
        humanComputerInterface.setDepartment(softwareEngineeringDepartment);
        humanComputerInterface.setCredits(3f);
        softwareProcesses = new Course();
        softwareProcesses.setName("Special Topics: Software Processes");
        softwareProcesses.setNumber("497");
        softwareProcesses.setDepartment(softwareEngineeringDepartment);
        softwareProcesses.setCredits(3f);
        requirementsEngineering = new Course();
        requirementsEngineering.setName("Special Topics: Requirements Engineering");
        requirementsEngineering.setNumber("597");
        requirementsEngineering.setDepartment(softwareEngineeringDepartment);
        requirementsEngineering.setCredits(3f);
        formalMethods = new Course();
        formalMethods.setName("Special Topics: Formal Methods");
        formalMethods.setNumber("597");
        formalMethods.setDepartment(softwareEngineeringDepartment);
        formalMethods.setCredits(3f);
        systemsThinking = new Course();
        systemsThinking.setName("Special Topics:  Systems Thinking");
        systemsThinking.setNumber("597");
        systemsThinking.setDepartment(softwareEngineeringDepartment);
        systemsThinking.setCredits(3f);
        knowledgeManagement = new Course();
        knowledgeManagement.setName("Special Topics:  Knowledge Management");
        knowledgeManagement.setNumber("597");
        knowledgeManagement.setDepartment(softwareEngineeringDepartment);
        knowledgeManagement.setCredits(3f);
        requirementsEngineeringModule.setCreditsToSatisfy(18f);
        Set<Course> requirementsEngineeringCourses = requirementsEngineeringModule.getCourses();
        requirementsEngineeringCourses.add(businessProcessManagement);
        requirementsEngineeringCourses.add(humanComputerInterface);
        requirementsEngineeringCourses.add(softwareProcesses);
        requirementsEngineeringCourses.add(requirementsEngineering);
        requirementsEngineeringCourses.add(formalMethods);
        requirementsEngineeringCourses.add(systemsThinking);
        requirementsEngineeringCourses.add(knowledgeManagement);
    }

    private static void setUpGlobalSoftwareEngineering() {
        globalSoftwareEngineeringModule = new ElectiveModule();
        globalSoftwareEngineeringModule.setName("Global Software Engineering");
        softwareProductLines = new Course();
        softwareProductLines.setName("Special Topics: Software Product Lines");
        softwareProductLines.setNumber("497");
        softwareProductLines.setDepartment(softwareEngineeringDepartment);
        softwareProductLines.setCredits(3f);
        globalSoftwareDevelopment = new Course();
        globalSoftwareDevelopment.setName("Special Topics: Global Software Development");
        globalSoftwareDevelopment.setNumber("597");
        globalSoftwareDevelopment.setDepartment(softwareEngineeringDepartment);
        globalSoftwareDevelopment.setCredits(3f);
        softwareSystemArchitecture = new Course();
        softwareSystemArchitecture.setName("Special Topics: Software Systems Architecture");
        softwareSystemArchitecture.setNumber("597");
        softwareSystemArchitecture.setDepartment(softwareEngineeringDepartment);
        softwareSystemArchitecture.setCredits(3f);
        globalSoftwareEngineeringModule.setCreditsToSatisfy(18f);
        globalSoftwareEngineeringModule.getCourses().add(softwareProcesses);
        globalSoftwareEngineeringModule.getCourses().add(softwareProductLines);
        globalSoftwareEngineeringModule.getCourses().add(globalSoftwareDevelopment);
        globalSoftwareEngineeringModule.getCourses().add(knowledgeManagement);
        globalSoftwareEngineeringModule.getCourses().add(systemsThinking);
    }

    private static void setUpSoftwareArchitectureModule() {
        softwareArchitectureModule = new ElectiveModule();
        softwareArchitectureModule.setName("Software Architecture");
        serviceOrientedArchitecture = new Course();
        serviceOrientedArchitecture.setName("Special Topics: Service-Oriented Architecture");
        serviceOrientedArchitecture.setNumber("597");
        serviceOrientedArchitecture.setDepartment(informationScienceDepartment);
        serviceOrientedArchitecture.setCredits(3f);
        enterpriseIntegration = new Course();
        enterpriseIntegration.setName("Enterprise Integration");
        enterpriseIntegration.setNumber("568");
        enterpriseIntegration.setDepartment(softwareEngineeringDepartment);
        enterpriseIntegration.setCredits(3f);
        patternOrientedDesign = new Course();
        patternOrientedDesign.setName("Pattern-Oriented Design");
        patternOrientedDesign.setNumber("585");
        patternOrientedDesign.setDepartment(softwareEngineeringDepartment);
        patternOrientedDesign.setCredits(3f);
        enterpriseServiceComputing = new Course();
        enterpriseServiceComputing.setName("Special Topics: Enterprise Service Computing");
        enterpriseServiceComputing.setNumber("597");
        enterpriseServiceComputing.setDepartment(softwareEngineeringDepartment);
        enterpriseServiceComputing.setCredits(3f);
        programUnderstanding = new Course();
        programUnderstanding.setName("Special Topics: Program Understanding");
        programUnderstanding.setNumber("597");
        programUnderstanding.setDepartment(softwareEngineeringDepartment);
        programUnderstanding.setCredits(3f);
        softwareArchitectureModule.setCreditsToSatisfy(18f);
        softwareArchitectureModule.getCourses().add(serviceOrientedArchitecture);
        softwareArchitectureModule.getCourses().add(softwareProductLines);
        softwareArchitectureModule.getCourses().add(enterpriseIntegration);
        softwareArchitectureModule.getCourses().add(patternOrientedDesign);
        softwareArchitectureModule.getCourses().add(softwareSystemArchitecture);
        softwareArchitectureModule.getCourses().add(enterpriseServiceComputing);
        softwareArchitectureModule.getCourses().add(programUnderstanding);
    }

    public static void setUpSoftwareEngineeringDegree() {
        softwareEngineeringDegree = new Degree();
        softwareEngineeringDegree.setName("Software Engineering");
        softwareEngineeringDegree.setMinimumGpa(3.0f);
        softwareSystemDesignRequirement = new CoreRequirement();
        softwareSystemDesignRequirement.setCreditsToSatisfy(3.0f);
        softwareSystemDesignRequirement.getCourses().add(softwareSystemDesign);
        advancedSoftwareEngineeringRequirement = new CoreRequirement();
        advancedSoftwareEngineeringRequirement.setCreditsToSatisfy(3.0f);
        advancedSoftwareEngineeringRequirement.getCourses().add(advancedSoftwareEngineering);
        softwareTestingRequirement = new CoreRequirement();
        softwareTestingRequirement.setCreditsToSatisfy(3.0f);
        softwareTestingRequirement.getCourses().add(softwareTesting);
        softwareDocumentationRequirement = new CoreRequirement();
        softwareDocumentationRequirement.setCreditsToSatisfy(3.0f);
        softwareDocumentationRequirement.getCourses().add(softwareDocumentation);
        managementRequirement = new CoreRequirement();
        managementRequirement.setCreditsToSatisfy(3.0f);
        managementRequirement.getCourses().add(softwareProjectManagement);
        managementRequirement.getCourses().add(technicalProjectManagement);
        capstone = new CoreRequirement();
        capstone.setCreditsToSatisfy(3.0f);
        capstone.getCourses().add(advancedSoftwareEngineeringStudio);
        capstone.getCourses().add(mastersResearchPaper);
        electiveRequirementCompleteOne = new ElectiveRequirement();
        electiveRequirementCompleteOne.setNumToSatisfy(1);
        electiveRequirementCompleteOne.getElectiveModules().add(requirementsEngineeringModule);
        electiveRequirementCompleteOne.getElectiveModules().add(globalSoftwareEngineeringModule);
        electiveRequirementCompleteOne.getElectiveModules().add(softwareArchitectureModule);
        softwareEngineeringDegree.setMinimumGpa(3.0f);
        softwareEngineeringDegree.getRequirements().add(softwareSystemDesignRequirement);
        softwareEngineeringDegree.getRequirements().add(advancedSoftwareEngineeringRequirement);
        softwareEngineeringDegree.getRequirements().add(softwareTestingRequirement);
        softwareEngineeringDegree.getRequirements().add(managementRequirement);
        softwareEngineeringDegree.getRequirements().add(capstone);
        softwareEngineeringDegree.getRequirements().add(electiveRequirementCompleteOne);
    }

    @Test
    public void saveAndGetTest() {
        degreeDao.save(softwareEngineeringDegree);
        Degree retrievedDegree = degreeDao.get(softwareEngineeringDegree.getId());
        assertEquals(softwareEngineeringDegree, retrievedDegree);
    }
}
