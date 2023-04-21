package QQV4Client;

public class User {

    private String userQQ;

    private String userName;

    private String pwd;

    private String sex;

    private String brithdata;

    private int qqmoney;

    private String myBlive;

    private int star;

    private String home;

    private String work;

    /**
   * ��һ��������
   * @param userQQ:�û�QQ
   * @param pwd:�û�����
   */
    public User(String userQQ, String pwd) {
        this.userQQ = userQQ;
        this.pwd = pwd;
    }

    /**
 * �ڶ���������.����ע���
 * @param userQQ:ӵ��QQ
 * @param userNameӵ������
 * @param pwd�û�����
 * @param sex�û��Ա�
 * @param brithdata�û�
 * @param qqmoney:Q��
 * @param myBlive:����ǩ��
 * @param star:QQ����
 * @param home:��ַ
 * @param work:ְλ
 */
    public User(String userQQ, String userName, String pwd, String sex, String brithdata, int qqmoney, String myBlive, int star, String home, String work) {
        super();
        this.userQQ = userQQ;
        this.userName = userName;
        this.pwd = pwd;
        this.sex = sex;
        this.brithdata = brithdata;
        this.qqmoney = qqmoney;
        this.myBlive = myBlive;
        this.star = star;
        this.home = home;
        this.work = work;
    }

    /**
 * @return the userQQ
 */
    public String getUserQQ() {
        return userQQ;
    }

    /**
 * @param userQQ the userQQ to set
 */
    public void setUserQQ(String userQQ) {
        this.userQQ = userQQ;
    }

    /**
 * @return the userName
 */
    public String getUserName() {
        return userName;
    }

    /**
 * @param userName the userName to set
 */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
 * @return the pwd
 */
    public String getPwd() {
        return pwd;
    }

    /**
 * @param pwd the pwd to set
 */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
 * @return the sex
 */
    public String getSex() {
        return sex;
    }

    /**
 * @param sex the sex to set
 */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
 * @return the brithdata
 */
    public String getBrithdata() {
        return brithdata;
    }

    /**
 * @param brithdata the brithdata to set
 */
    public void setBrithdata(String brithdata) {
        this.brithdata = brithdata;
    }

    /**
 * @return the qqmoney
 */
    public int getQqmoney() {
        return qqmoney;
    }

    /**
 * @param qqmoney the qqmoney to set
 */
    public void setQqmoney(int qqmoney) {
        this.qqmoney = qqmoney;
    }

    /**
 * @return the myBlive
 */
    public String getMyBlive() {
        return myBlive;
    }

    /**
 * @param myBlive the myBlive to set
 */
    public void setMyBlive(String myBlive) {
        this.myBlive = myBlive;
    }

    /**
 * @return the star
 */
    public int getStar() {
        return star;
    }

    /**
 * @param star the star to set
 */
    public void setStar(int star) {
        this.star = star;
    }

    /**
 * @return the home
 */
    public String getHome() {
        return home;
    }

    /**
 * @param home the home to set
 */
    public void setHome(String home) {
        this.home = home;
    }

    /**
 * @return the work
 */
    public String getWork() {
        return work;
    }

    /**
 * @param work the work to set
 */
    public void setWork(String work) {
        this.work = work;
    }
}
