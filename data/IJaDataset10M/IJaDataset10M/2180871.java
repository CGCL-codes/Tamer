package codeGenerator.misc;

import codeGenerator.Utility;

/**
 * <p>Title: Project Code Generator</p>
 * <p>Description: This class has methods which generate code to be inserted in
 * DAOFactory, ServiceLocator and Struts Config files.</p>
 * <p>Copyright � 2008 Shaz Solutions. All Rights Reserved.</p>
 * <p>Company: Shaz Solutions</p>
 * @author Shahzad Masud
 * @created June 04, 2008
 * @version 1.0
 */
public class Misc {

    private String daoInterfaceName = null;

    private String daoImplName = null;

    private String mgrName = null;

    private String actionName = null;

    private String actionClassName = null;

    private String[] forwardNames = null;

    private String[] forwardPaths = null;

    private String formName = null;

    private String formBeanName = null;

    private String fileName = null;

    /**
   * This constructor does not do anything
   */
    public Misc() {
        ;
    }

    public void generateMiscCode() {
        StringBuffer contents = new StringBuffer();
        contents.append(getDAOFactoryCode());
        contents.append(getServiceLocatorCode());
        contents.append(getStrutsConfigFormBeanCode());
        contents.append(getStrutsConfigActionMappingCode());
        Utility.writeFile(getFileName(), contents.toString());
    }

    public void setFileName(String newFileName) {
        this.fileName = newFileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getDAOFactoryCode() {
        StringBuffer contents = new StringBuffer();
        contents.append("/****************** DAO Factory Code Started ******************/\n\n");
        contents.append("// Insert following one line in the declaration section of DAOFactory class.\n\n");
        contents.append("  private ");
        contents.append(getDAOInterfaceName());
        contents.append(" ");
        contents.append(Utility.firstLetterSmall(getDAOInterfaceName()));
        contents.append(" = null;\n\n");
        contents.append("// Insert the following method into methods section of DAOFactory class.\n\n");
        contents.append("  /**\n");
        contents.append("   * This method returns an object of implemented class of ");
        contents.append(getDAOInterfaceName());
        contents.append(".\n");
        contents.append("   * @return An object of implemented class of ");
        contents.append(getDAOInterfaceName());
        contents.append(".\n");
        contents.append("   */\n");
        contents.append("  public ");
        contents.append(getDAOInterfaceName());
        contents.append(" get");
        contents.append(getDAOInterfaceName());
        contents.append("() {\n");
        contents.append("    if(");
        contents.append(Utility.firstLetterSmall(getDAOInterfaceName()));
        contents.append(" == null) {\n");
        contents.append("      ");
        contents.append(Utility.firstLetterSmall(getDAOInterfaceName()));
        contents.append(" = new ");
        contents.append(getDAOImplName());
        contents.append("();\n");
        contents.append("    } // end of if\n");
        contents.append("    return ");
        contents.append(Utility.firstLetterSmall(getDAOInterfaceName()));
        contents.append(";\n");
        contents.append("  } // end of method get");
        contents.append(getDAOInterfaceName());
        contents.append("\n\n");
        contents.append("/****************** DAO Factory Code Ended ******************/\n\n");
        return contents.toString();
    }

    public String getServiceLocatorCode() {
        StringBuffer contents = new StringBuffer();
        contents.append("/****************** Service Locator Code Started ******************/\n\n");
        contents.append("// Insert following two lines in the declaration section of Service Locator class.\n\n");
        contents.append("  public static final int ");
        contents.append(getMgrName().toUpperCase());
        contents.append("_MGR = 1;\n");
        contents.append("  public static final int ");
        contents.append(getMgrName().toUpperCase());
        contents.append("_MGR_NAME = \"");
        contents.append(getMgrName());
        contents.append("Mgr\";\n\n");
        contents.append("// Insert following two lines in getLocalService(int aServiceId) method within switch construct.\n\n");
        contents.append("      case ");
        contents.append(getMgrName().toUpperCase());
        contents.append("_MGR:\n");
        contents.append("        return new ");
        contents.append(getMgrName());
        contents.append("MgrImpl();\n\n");
        contents.append("// Insert following two lines in getServiceName(int aServiceId) method within switch construct.\n\n");
        contents.append("      case ");
        contents.append(getMgrName().toUpperCase());
        contents.append("_MGR:\n");
        contents.append("        return ");
        contents.append(getMgrName().toUpperCase());
        contents.append("_MGR_NAME;\n\n");
        contents.append("/****************** Service Locator Code Ended ******************/\n\n");
        return contents.toString();
    }

    public String getStrutsConfigActionMappingCode() {
        StringBuffer contents = new StringBuffer();
        contents.append("    <action    path=\"/");
        contents.append(getActionName());
        contents.append("\"\n");
        contents.append("               type=\"ch.epfl.bmi.action.");
        contents.append(getActionClassName());
        contents.append("\"\n");
        contents.append("               name=\"");
        contents.append(getFormName());
        contents.append("\"\n");
        contents.append("               scope=\"request\"\n");
        contents.append("               validate=\"false\"\n");
        contents.append("               input=\"input\"\n");
        contents.append("               >\n");
        for (int i = 0; i < getForwardNames().length; i++) {
            contents.append("      <forward name=\"");
            contents.append(getForwardNames()[i]);
            contents.append("\" path=\"");
            contents.append(getForwardPaths()[i]);
            contents.append("\"/>\n");
        }
        contents.append("    </action>\n\n");
        return contents.toString();
    }

    public String getStrutsConfigFormBeanCode() {
        StringBuffer contents = new StringBuffer();
        contents.append("    <form-bean name=\"");
        contents.append(getFormName());
        contents.append("\" type=\"ch.epfl.bmi.form.");
        contents.append(getFormBeanName());
        contents.append("\" />\n\n");
        return contents.toString();
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getDAOImplName() {
        return daoImplName;
    }

    public void setDAOImplName(String daoImplName) {
        this.daoImplName = daoImplName;
    }

    public String getDAOInterfaceName() {
        return daoInterfaceName;
    }

    public void setDAOInterfaceName(String daoInterfaceName) {
        this.daoInterfaceName = daoInterfaceName;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getMgrName() {
        return mgrName;
    }

    /**
   * Please do not include word Mgr in the name.
   * @mgrName name of the manager class without the word Mgr
   */
    public void setMgrName(String mgrName) {
        this.mgrName = mgrName;
    }

    public String getActionClassName() {
        return actionClassName;
    }

    public void setActionClassName(String actionClassName) {
        this.actionClassName = actionClassName;
    }

    public String getFormBeanName() {
        return formBeanName;
    }

    public void setFormBeanName(String formBeanName) {
        this.formBeanName = formBeanName;
    }

    public String[] getForwardNames() {
        return forwardNames;
    }

    public void setForwardNames(String[] forwardNames) {
        this.forwardNames = forwardNames;
    }

    public String[] getForwardPaths() {
        return forwardPaths;
    }

    public void setForwardPaths(String[] forwardPaths) {
        this.forwardPaths = forwardPaths;
    }
}
