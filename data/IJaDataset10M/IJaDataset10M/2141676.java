package userInterface.CV;

import java.util.Vector;
import javax.servlet.http.HttpSession;
import businessClasses.Education;
import businessClasses.Language;
import businessClasses.User;
import businessClasses.WorkExperience;
import com.itmill.toolkit.service.ApplicationContext;
import com.itmill.toolkit.terminal.gwt.server.WebApplicationContext;
import com.itmill.toolkit.ui.Button;
import com.itmill.toolkit.ui.CustomComponent;
import com.itmill.toolkit.ui.CustomLayout;
import com.itmill.toolkit.ui.Label;
import com.itmill.toolkit.ui.OrderedLayout;
import com.itmill.toolkit.ui.Panel;
import com.itmill.toolkit.ui.Window;
import com.itmill.toolkit.ui.Button.ClickEvent;
import database.CvQueryController;

/**
 * Preview of the CV panel
 */
public class CVPreviewPanel extends CustomComponent implements Button.ClickListener {

    private CustomLayout cvPreviewLayout = new CustomLayout("cvPreviewLayout");

    private OrderedLayout workExperience_Orderedlayout = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);

    private OrderedLayout education_Orderedlayout = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);

    private OrderedLayout language_Orderedlayout = new OrderedLayout(OrderedLayout.ORIENTATION_VERTICAL);

    private Label lbBirthday = new Label("Date of Birthday");

    private Label lbFirstname = new Label("First Name");

    private Label lbLastname = new Label("Last Name");

    private Label lbAddress = new Label("Street Address");

    private Label lbPostcode = new Label("Post Code");

    private Label lbCity = new Label("City");

    private Label lbCountry = new Label("Country");

    private Label lbNationality = new Label("Nationality");

    private Label lbTelephone = new Label("Telephone");

    private Label lbMobile = new Label("Mobile");

    private Label lbFax = new Label("Fax");

    private Label lbEmail = new Label("Email Address");

    private Label lbBirthdayValue = new Label();

    private Label lbFirstnameValue = new Label();

    private Label lbLastnameValue = new Label();

    private Label lbAddressValue = new Label();

    private Label lbPostcodeValue = new Label();

    private Label lbCityValue = new Label();

    private Label lbCountryValue = new Label();

    private Label lbNationalityValue = new Label();

    private Label lbTelephoneValue = new Label();

    private Label lbMobileValue = new Label();

    private Label lbFaxValue = new Label();

    private Label lbEmailValue = new Label();

    private Label lbWorkexperienceTitle = new Label("Work Experience");

    private Label lbWeFromDate = new Label("From Date");

    private Label lbWeToDate = new Label("To Date");

    private Label lbOccupation = new Label("Occupation");

    private Label lbMainActivity = new Label("Main Activities");

    private Label lbEmployerName = new Label("Employer Name");

    private Label lbEmployerAddr = new Label("Employer Address");

    private Label lbBusinessType = new Label("Business Type");

    private Label lbEducationTitle = new Label("Education Background");

    private Label lbEbFromDate = new Label("From Date");

    private Label lbEbToDate = new Label("To Date");

    private Label lbQualification = new Label("Title of Qualification");

    private Label lbSubject = new Label("Principal Subjects");

    private Label lbUniversity = new Label("University/College");

    private Label lbEbLevel = new Label("Level");

    private Label lbLanguageTitle = new Label("Language Skills");

    private Label lbLanguage = new Label("Language Name");

    private Label lbLanLevel = new Label("Level");

    private Label lbSocial = new Label("Social Skills");

    private Label lbOrganisational = new Label("Organisational Skills");

    private Label lbTechnical = new Label("Technical Skills");

    private Label lbComputer = new Label("Computer Skills");

    private Label lbOther = new Label("Other Skills");

    private Label lbLicense = new Label("Driver License");

    private Label lbSocialValue = new Label();

    private Label lbOrganisationalValue = new Label();

    private Label lbTechnicalValue = new Label();

    private Label lbComputerValue = new Label();

    private Label lbOtherValue = new Label();

    private Label lbLicenseValue = new Label();

    private Button btBack = new Button("Back", this);

    private Button btBackMembersProfile = new Button("Back", this);

    private Button btBackFrontpage = new Button("Back", this);

    private ApplicationContext ctx;

    private WebApplicationContext webCtx;

    private HttpSession session;

    private String username;

    private Window main;

    public CustomLayout masterPageLayout = new CustomLayout("jobSeekerLayout");

    private Panel cvPreviewPanel = new Panel("CV Preview");

    public CVPreviewPanel(CustomLayout jsLayout, Window mainWindow) {
        main = mainWindow;
        ctx = main.getApplication().getContext();
        webCtx = (WebApplicationContext) ctx;
        session = webCtx.getHttpSession();
        username = (String) session.getAttribute("UserName");
        cvPreviewLayout.addComponent(lbBirthday, "lbBirthday");
        cvPreviewLayout.addComponent(lbFirstname, "lbFirstname");
        cvPreviewLayout.addComponent(lbLastname, "lbLastname");
        cvPreviewLayout.addComponent(lbAddress, "lbAddress");
        cvPreviewLayout.addComponent(lbPostcode, "lbPostcode");
        cvPreviewLayout.addComponent(lbCity, "lbCity");
        cvPreviewLayout.addComponent(lbCountry, "lbCountry");
        cvPreviewLayout.addComponent(lbNationality, "lbNationality");
        cvPreviewLayout.addComponent(lbTelephone, "lbTelephone");
        cvPreviewLayout.addComponent(lbMobile, "lbMobile");
        cvPreviewLayout.addComponent(lbFax, "lbFax");
        cvPreviewLayout.addComponent(lbEmail, "lbEmail");
        cvPreviewLayout.addComponent(lbBirthdayValue, "lbBirthdayValue");
        cvPreviewLayout.addComponent(lbFirstnameValue, "lbFirstnameValue");
        cvPreviewLayout.addComponent(lbLastnameValue, "lbLastnameValue");
        cvPreviewLayout.addComponent(lbAddressValue, "lbAddressValue");
        cvPreviewLayout.addComponent(lbPostcodeValue, "lbPostcodeValue");
        cvPreviewLayout.addComponent(lbCityValue, "lbCityValue");
        cvPreviewLayout.addComponent(lbCountryValue, "lbCountryValue");
        cvPreviewLayout.addComponent(lbNationalityValue, "lbNationalityValue");
        cvPreviewLayout.addComponent(lbTelephoneValue, "lbTelephoneValue");
        cvPreviewLayout.addComponent(lbMobileValue, "lbMobileValue");
        cvPreviewLayout.addComponent(lbFaxValue, "lbFaxValue");
        cvPreviewLayout.addComponent(lbEmailValue, "lbEmailValue");
        Vector<WorkExperience> workexperienceVector = CvQueryController.getWorkExerience(username);
        CustomLayout cv_workExperienceLayout = new CustomLayout("cv_workExperienceLayout");
        for (int i = 0; i < workexperienceVector.size(); i++) {
            WorkExperience workexperience = new WorkExperience();
            workexperience = workexperienceVector.get(i);
            cv_workExperienceLayout.addComponent(lbWeFromDate, "lbWeFromDate");
            cv_workExperienceLayout.addComponent(lbWeToDate, "lbWeToDate");
            cv_workExperienceLayout.addComponent(lbOccupation, "lbOccupation");
            cv_workExperienceLayout.addComponent(lbMainActivity, "lbMainActivity");
            cv_workExperienceLayout.addComponent(lbEmployerName, "lbEmployerName");
            cv_workExperienceLayout.addComponent(lbEmployerAddr, "lbEmployerAddr");
            cv_workExperienceLayout.addComponent(lbBusinessType, "lbBusinessType");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getFromDate()), "lbWeFromDateValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getToDate()), "lbWeToDateValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getOccupation()), "lbOccupationValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getResponsibilities()), "lbMainActivityValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getEmployerName()), "lbEmployerNameValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getEmployerAddress()), "lbEmployerAddrValue");
            cv_workExperienceLayout.addComponent(new Label(workexperience.getBusinessType()), "lbBusinessTypeValue");
            workExperience_Orderedlayout.addComponent(cv_workExperienceLayout);
            cvPreviewLayout.addComponent(lbWorkexperienceTitle, "lbWorkexperienceTitle");
            cvPreviewLayout.addComponent(workExperience_Orderedlayout, "workExperience_Orderedlayout");
        }
        Vector<Education> educationVector = CvQueryController.getEduation(username);
        CustomLayout cv_EducationLayout = new CustomLayout("cv_EducationLayout");
        for (int i = 0; i < educationVector.size(); i++) {
            Education education = new Education();
            education = educationVector.get(i);
            cv_EducationLayout.addComponent(lbEbFromDate, "lbEbFromDate");
            cv_EducationLayout.addComponent(new Label(education.getFromDate()), "lbEbFromDateValue");
            cv_EducationLayout.addComponent(lbEbToDate, "lbEbToDate");
            cv_EducationLayout.addComponent(new Label(education.getToDate()), "lbEbToDateValue");
            if (education.getTitleOfQualification().equals("")) {
                cv_EducationLayout.addComponent(lbQualification, "lbQualification");
                cv_EducationLayout.addComponent(new Label(education.getTitleOfQualification()), "lbQualificationValue");
            }
            if (education.getPrincipalSubjects().equals("")) {
                cv_EducationLayout.addComponent(lbSubject, "lbSubject");
                cv_EducationLayout.addComponent(new Label(education.getPrincipalSubjects()), "lbSubjectValue");
            }
            if (education.getSchoolName().equals("")) {
                cv_EducationLayout.addComponent(lbUniversity, "lbUniversity");
                cv_EducationLayout.addComponent(new Label(education.getSchoolName()), "lbUniversityValue");
            }
            if (education.getEducationLevel().equals("")) {
                cv_EducationLayout.addComponent(lbEbLevel, "lbEbLevel");
                cv_EducationLayout.addComponent(new Label(education.getEducationLevel()), "lbEbLevelValue");
            }
            education_Orderedlayout.addComponent(cv_EducationLayout);
            cvPreviewLayout.addComponent(lbEducationTitle, "lbEducationTitle");
            cvPreviewLayout.addComponent(education_Orderedlayout, "education_Orderedlayout");
        }
        Vector<Language> languageVector = CvQueryController.getLanguage(username);
        CustomLayout cv_languageLayout = new CustomLayout("cv_languageLayout");
        for (int i = 0; i < languageVector.size(); i++) {
            Language language = new Language();
            language = languageVector.get(i);
            cv_languageLayout.addComponent(lbLanguage, "lbLanguage");
            cv_languageLayout.addComponent(lbLanLevel, "lbLanLevel");
            cv_languageLayout.addComponent(new Label(language.getLanguage()), "lbLanguageValue");
            cv_languageLayout.addComponent(new Label(language.getLanguageLevel()), "lbLanLevelValue");
            language_Orderedlayout.addComponent(cv_languageLayout);
            cvPreviewLayout.addComponent(lbLanguageTitle, "lbLanguageTitle");
            cvPreviewLayout.addComponent(language_Orderedlayout, "languageOrderedlayout");
        }
        cvPreviewLayout.addComponent(lbSocial, "lbSocial");
        cvPreviewLayout.addComponent(lbOrganisational, "lbOrganisational");
        cvPreviewLayout.addComponent(lbTechnical, "lbTechnical");
        cvPreviewLayout.addComponent(lbComputer, "lbComputer");
        cvPreviewLayout.addComponent(lbOther, "lbOther");
        cvPreviewLayout.addComponent(lbLicense, "lbLicense");
        cvPreviewLayout.addComponent(lbSocialValue, "lbSocialValue");
        cvPreviewLayout.addComponent(lbOrganisationalValue, "lbOrganisationalValue");
        cvPreviewLayout.addComponent(lbTechnicalValue, "lbTechnicalValue");
        cvPreviewLayout.addComponent(lbComputerValue, "lbComputerValue");
        cvPreviewLayout.addComponent(lbOtherValue, "lbOtherValue");
        cvPreviewLayout.addComponent(lbLicenseValue, "lbLicenseValue");
        cvPreviewLayout.addComponent(btBack, "btBack");
        User cvDetails = new User();
        cvDetails = CvQueryController.getCv(username);
        lbBirthdayValue.setCaption(cvDetails.getDateOfBirth());
        lbFirstnameValue.setCaption(cvDetails.getFirstname());
        lbLastnameValue.setCaption(cvDetails.getLastname());
        lbAddressValue.setCaption(cvDetails.getStreet());
        lbPostcodeValue.setCaption(cvDetails.getPostalCode());
        lbCityValue.setCaption(cvDetails.getCity());
        lbCountryValue.setCaption(cvDetails.getCountry());
        lbNationalityValue.setCaption(cvDetails.getNationality());
        lbTelephoneValue.setCaption(cvDetails.getPhoneNo());
        lbMobileValue.setCaption(cvDetails.getMobileNo());
        lbFaxValue.setCaption(cvDetails.getFax());
        lbEmailValue.setCaption(cvDetails.getEmail());
        lbSocialValue.setCaption(cvDetails.getSocialskills());
        lbOrganisationalValue.setCaption(cvDetails.getOrganisationalSkills());
        lbTechnicalValue.setCaption(cvDetails.getTechnicalSkills());
        lbComputerValue.setCaption(cvDetails.getComputerSkills());
        lbOtherValue.setCaption(cvDetails.getOtherSkills());
        lbLicenseValue.setCaption(cvDetails.getDrivingLicence());
        cvPreviewPanel.addComponent(cvPreviewLayout);
        cvPreviewPanel.setCaption("CV Preview");
        cvPreviewPanel.setStyleName("cvPreviewPanel");
        setCompositionRoot(cvPreviewPanel);
        masterPageLayout = jsLayout;
    }

    public void buttonClick(ClickEvent event) {
        if (event.getSource() == btBack) {
            cvPreviewPanel.removeAllComponents();
            masterPageLayout.removeComponent("employeeContentLocation");
            masterPageLayout.addComponent(new PersonalSkillPanel(masterPageLayout, main), "employeeContentLocation");
        } else if (event.getSource() == btBackMembersProfile) {
            cvPreviewPanel.removeAllComponents();
            masterPageLayout.removeComponent("employeeContentLocation");
            masterPageLayout.addComponent(new CVPreviewPanel(masterPageLayout, main), "employeeContentLocation");
        } else if (event.getSource() == btBackFrontpage) {
        }
    }

    public Vector<CustomLayout> getEducationCustomLayout() {
        Vector<CustomLayout> customLayout = new Vector<CustomLayout>();
        CustomLayout cv_EducationLayout1 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout2 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout3 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout4 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout5 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout6 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout7 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout8 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout9 = new CustomLayout("cv_EducationLayout");
        CustomLayout cv_EducationLayout10 = new CustomLayout("cv_EducationLayout");
        customLayout.add(cv_EducationLayout1);
        customLayout.add(cv_EducationLayout2);
        customLayout.add(cv_EducationLayout3);
        customLayout.add(cv_EducationLayout4);
        customLayout.add(cv_EducationLayout5);
        customLayout.add(cv_EducationLayout6);
        customLayout.add(cv_EducationLayout7);
        customLayout.add(cv_EducationLayout8);
        customLayout.add(cv_EducationLayout9);
        customLayout.add(cv_EducationLayout10);
        return customLayout;
    }
}
