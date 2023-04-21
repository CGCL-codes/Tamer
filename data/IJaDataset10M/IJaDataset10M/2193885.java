package corebean;

/******************************************
 * Filename			: DesSrc.java
 * Decription		: Bean dung chuyen trang
 * Created date		: Jun 3, 2008
 * Author			: Phonglt
 ******************************************/
public class DesSrc {

    private String group;

    private String func;

    private String file;

    /****************************************************
	 * Method name		: getGroup [GETTER]
	 * Return type		: DesSrc
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public String getGroup() {
        return group;
    }

    /****************************************************
	 * Method name		: setGroup [SETTER]
	 * Return type		: void
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public void setGroup(String group) {
        this.group = group;
    }

    /****************************************************
	 * Method name		: getFunc [GETTER]
	 * Return type		: DesSrc
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public String getFunc() {
        return func;
    }

    /****************************************************
	 * Method name		: setFunc [SETTER]
	 * Return type		: void
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public void setFunc(String func) {
        this.func = func;
    }

    /****************************************************
	 * Method name		: getFile [GETTER]
	 * Return type		: DesSrc
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public String getFile() {
        return file;
    }

    /****************************************************
	 * Method name		: setFile [SETTER]
	 * Return type		: void
	 * Decription		: 
	 * Created date		: Jun 3, 2008
	 * Author			: Phonglt
	 ****************************************************/
    public void setFile(String file) {
        this.file = file;
    }

    public String forward() {
        return "success";
    }
}
