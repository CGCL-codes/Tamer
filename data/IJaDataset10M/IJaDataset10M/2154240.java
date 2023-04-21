package fr.umlv.jee.hibou.web.faq;

import com.opensymphony.xwork2.ActionSupport;
import fr.umlv.jee.hibou.wsclient.FaqBean;
import fr.umlv.jee.hibou.wsclient.HibouWS;
import fr.umlv.jee.hibou.wsclient.HibouWSService;
import fr.umlv.jee.hibou.wsclient.UserBean;
import fr.umlv.jee.hibou.wsclient.UserListBean;

/**
 * This class initializes the faq page in free view
 * @author bnak, micka, matt, alex
 *
 */
public class ReadFaq extends ActionSupport {

    private static final long serialVersionUID = -5911610874099279532L;

    private final HibouWS hibouWSPort = new HibouWSService().getHibouWSPort();

    private static final String TECHNICAL_ERROR = "technical_error";

    private String projectName;

    private String projectLeader;

    private String response1;

    private String response2;

    private String response3;

    private String response4;

    private String response5;

    /**
	 * Init the page
	 */
    @Override
    public String execute() {
        try {
            UserListBean userListBean = hibouWSPort.getProjectLeaders(projectName);
            StringBuilder stringBuilder = new StringBuilder();
            for (UserBean userBean : userListBean.getList()) {
                stringBuilder.append(userBean.getEmail());
                stringBuilder.append(", ");
            }
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
            projectLeader = stringBuilder.toString();
            System.out.println("init faq -->" + projectName + " chefs : " + projectLeader);
            FaqBean faqBean = hibouWSPort.getFaq(projectName);
            response1 = faqBean.getResponse1();
            response2 = faqBean.getResponse2();
            response3 = faqBean.getResponse3();
            response4 = faqBean.getResponse4();
            response5 = faqBean.getResponse5();
            System.out.println(getText("question1") + " --> " + response1);
            System.out.println(getText("question2") + " --> " + response2);
            System.out.println(getText("question3") + " --> " + response3);
            System.out.println(getText("question4") + " --> " + response4);
            System.out.println(getText("question5") + " --> " + response5);
        } catch (Exception e) {
            return TECHNICAL_ERROR;
        }
        return SUCCESS;
    }

    /**
	 * @return the projectLeader
	 */
    public String getProjectLeader() {
        return projectLeader;
    }

    /**
	 * @param projectLeader the projectLeader to set
	 */
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**
	 * @return the projectName
	 */
    public String getProjectName() {
        return projectName;
    }

    /**
	 * @param projectName the projectName to set
	 */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
	 * @return the response1
	 */
    public String getResponse1() {
        return response1;
    }

    /**
	 * @param response1 the response1 to set
	 */
    public void setResponse1(String response1) {
        this.response1 = response1;
    }

    /**
	 * @return the response2
	 */
    public String getResponse2() {
        return response2;
    }

    /**
	 * @param response2 the response2 to set
	 */
    public void setResponse2(String response2) {
        this.response2 = response2;
    }

    /**
	 * @return the response3
	 */
    public String getResponse3() {
        return response3;
    }

    /**
	 * @param response3 the response3 to set
	 */
    public void setResponse3(String response3) {
        this.response3 = response3;
    }

    /**
	 * @return the response4
	 */
    public String getResponse4() {
        return response4;
    }

    /**
	 * @param response4 the response4 to set
	 */
    public void setResponse4(String response4) {
        this.response4 = response4;
    }

    /**
	 * @return the response5
	 */
    public String getResponse5() {
        return response5;
    }

    /**
	 * @param response5 the response5 to set
	 */
    public void setResponse5(String response5) {
        this.response5 = response5;
    }
}
