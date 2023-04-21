package org.CDS.Gui;

import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class Welcome extends Window {

    Window winLogin;

    Register register;

    Groupbox gb;

    Button cancelButton = null;

    Connection conn;

    private String person = "NoBody";

    private int loginedID;

    String authority = "Unknown";

    public void insertSampleData() {
        initializeTheConnection();
        System.out.println("Inserting Sample Datas...");
        String createUserTableQuery = "CREATE TABLE user(" + "id integer," + "name varchar(50)," + "userName varchar(50)," + "password varchar(50)," + "surname varchar(50)," + "deptID integer," + "lastSession varchar(50)," + "registerDate varchar(50)," + "grupId integer," + "mail varchar(50)," + "roleId integer " + ") ";
        String createRoleTableQuery = "CREATE TABLE role(" + "id integer," + "name varchar(50)" + ")";
        String createMessageTableQuery = "CREATE TABLE message(" + "id integer," + "subject varchar(50)," + "content varchar(500)," + "senderId integer," + "receiverId integer," + "mesDate varchar(50)," + "mesTime varchar(50)" + ")";
        String createGrupTableQuery = "CREATE TABLE grup(" + "id integer," + "name varchar(50)," + "registerDate varchar(50)" + ")";
        String createDepartmentTableQuery = "CREATE TABLE department(" + "id integer," + "name varchar(50)" + ")";
        String createFormTableQuery = "CREATE TABLE form(" + "id integer," + "name varchar(50)," + "title varchar(50)," + "senderId integer," + "receiverId integer," + "formDate varchar(50)," + "formTime varchar(50)," + "content varchar(500)" + ")";
        String createFormItemsTableQuery = "CREATE TABLE formItem(" + "id integer," + "name varchar(50)," + "value varchar(50)," + "formId varchar(50)," + "type varchar(50)" + ")";
        try {
            update(createUserTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createFormItemsTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createFormTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createDepartmentTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createGrupTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createRoleTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        try {
            update(createMessageTableQuery);
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
        int uid = 1, udeptId = 1, uroleId = 1;
        String strName = "Ali", strPassword = "12345";
        String rName = "User";
        String gName = "Ciu Bilisim Ekibi";
        String dName = "Bilisim Sistemleri Muhendisligi";
        String fName = "Birincil Form", fTitle = "Baslik1", fContent = "Bu bir Formdur...", fTime = "form Time", fDate = "Form Date";
        int fRecId = 1, fSendId = 2;
        String mSubject = "Konu1", mContent = "Merhabalar, \nBu mesaj sistem tarafindan olusturuldu.. \nTesekkurler..", mDate = "Message Date", mTime = "Message Time";
        int mSendId = 1, mRecId = 2;
        String insertUserQuery = "INSERT INTO user (id,name,password,deptId,roleId) VALUES (" + uid + ",'" + strName + "','" + strPassword + "'," + udeptId + ",'" + uroleId + "')";
        String insertRoleQuery = "INSERT INTO role (id,name) VALUES (" + uid + ",'" + rName + "')";
        String insertGrupQuery = "INSERT INTO grup (id,name) VALUES (" + uid + ",'" + gName + "')";
        String insertDepartmentQuery = "INSERT INTO department (id,name) VALUES (" + uid + ",'" + dName + "')";
        String insertFormQuery = "INSERT INTO form (id,name,title,senderId,receiverId,formDate,formTime,content) VALUES (" + uid + ",'" + fName + "','" + fTitle + "'," + fSendId + "," + fRecId + ",'" + fDate + "','" + fTime + "','" + fContent + "')";
        String insertMessageQuery = "INSERT INTO message (id,subject,content,senderId,receiverId,mesDate,mesTime) VALUES (" + uid + ",'" + mSubject + "','" + mContent + "'," + mSendId + "," + mRecId + ",'" + mDate + "','" + mTime + "')";
        try {
            update(insertUserQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            update(insertRoleQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            update(insertMessageQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            update(insertFormQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            update(insertDepartmentQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            update(insertGrupQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            strName = "Veli";
            uid = 2;
            uroleId = 1;
            udeptId = 1;
            insertUserQuery = "INSERT INTO user (id,name,password,deptId,roleId) VALUES (" + uid + ",'" + strName + "','" + strPassword + "'," + udeptId + ",'" + uroleId + "')";
            update(insertUserQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            strName = "Admin";
            strPassword = "123";
            uid = 3;
            uroleId = 2;
            udeptId = 1;
            insertUserQuery = "INSERT INTO user (id,name,password,deptId,roleId) VALUES (" + uid + ",'" + strName + "','" + strPassword + "'," + udeptId + ",'" + uroleId + "')";
            update(insertUserQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            uid = 2;
            rName = "Admin";
            insertRoleQuery = "INSERT INTO role (id,name) VALUES (" + uid + ",'" + rName + "')";
            update(insertRoleQuery);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        try {
            System.out.println("Closing the Connection...");
            shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Cannot be closed...");
        }
    }

    public Welcome() {
        this.setWidth("100%");
        this.setHeight("100%");
        winLogin = new Window();
        winLogin.setWidth("350px");
        winLogin.setParent(this);
        winLogin.setTitle("Welcome To The System");
        winLogin.doHighlighted();
        gb = new Groupbox();
        gb.setParent(winLogin);
        Hbox loginVbox = new Hbox();
        loginVbox.setParent(gb);
        Vbox labelHbox = new Vbox();
        Vbox textHbox = new Vbox();
        Label nameLabel = new Label();
        nameLabel.setValue("Name : ");
        Label passLabel = new Label();
        passLabel.setValue("Password : ");
        final Textbox nameTextbox = new Textbox();
        final Textbox passTextbox = new Textbox();
        passTextbox.setType("password");
        labelHbox.setParent(loginVbox);
        textHbox.setParent(loginVbox);
        nameLabel.setParent(labelHbox);
        nameTextbox.setParent(textHbox);
        passLabel.setParent(labelHbox);
        passTextbox.setParent(textHbox);
        Button loginButton = new Button();
        loginButton.setLabel("Log Me in");
        loginButton.setParent(gb);
        Button registerButton = new Button();
        registerButton.setLabel("Register To System");
        registerButton.setParent(gb);
        Button insertSampleDataButton = new Button();
        insertSampleDataButton.setLabel("Create Database, Tables and Insert Sample Data...");
        insertSampleDataButton.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                System.out.println("Creating Database, Tables and Inserting Sample Data...");
                try {
                    insertSampleData();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Sample Datas cannot be created...");
                }
            }
        });
        registerButton.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                registerFunction();
            }
        });
        loginButton.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                if (nameTextbox.getValue() != "") {
                    loginFunction(nameTextbox.getValue(), passTextbox.getValue());
                } else {
                    try {
                        Messagebox.show("Please Enter a User Name ", "WARNING", Messagebox.OK, authority, Messagebox.IGNORE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
	 * Functions Starts Here
	 */
    public String whoIs() {
        return person;
    }

    public void cancelThis() {
        register.detach();
        cancelButton.detach();
        winLogin.setVisible(true);
        winLogin.doHighlighted();
    }

    /**
	 *Call the Register and make needed processes
	 */
    public void registerFunction() {
        initializeTheConnection();
        System.out.println("Register Function Started...");
        register = new Register();
        winLogin.setVisible(false);
        register.setParent(this);
        Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("" + dim);
        register.setWidth("" + dim.getWidth() / 2 + "px");
        register.setHeight("" + gb.getHeight() + "px");
        register.setTitle("Register..");
        cancelButton = new Button();
        cancelButton.setLabel("Cancel");
        cancelButton.setParent(this);
        cancelButton.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                cancelThis();
            }
        });
        register.registerButton.addEventListener(Events.ON_CLICK, new EventListener() {

            public void onEvent(Event arg0) throws Exception {
                String getDeptQuery = "SELECT name FROM department";
                String getGroupQuery = "SELECT name FROM grup";
                try {
                    initializeTheConnection();
                } catch (Exception ie) {
                    ie.printStackTrace();
                }
                ArrayList<String> deptList = new ArrayList<String>();
                ArrayList<String> groupList = new ArrayList<String>();
                try {
                    deptList = query(getDeptQuery);
                    groupList = query(getGroupQuery);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String strDept, strGroup, strName, strPassword, strMail, strSurname;
                int intDept = 0, intGroup = 0;
                strName = register.nameTextbox.getValue();
                strSurname = register.surnameTextbox.getValue();
                strPassword = register.passTextbox.getValue();
                ArrayList<String> deptId = new ArrayList<String>();
                if (deptList.contains(register.deptCombobox.getValue())) {
                    strDept = register.deptCombobox.getValue();
                    System.out.println("DeptName Matched..");
                    String getDeptIdQuery = "SELECT id FROM department WHERE NAME ='" + strDept + "'";
                    deptId = query(getDeptIdQuery);
                    deptId.get(deptId.size() - 1);
                    System.out.println(deptId);
                    intDept = Integer.parseInt(deptId.get(deptId.size() - 1));
                    System.out.println("" + intDept);
                } else {
                    strDept = "Unknown Department";
                }
                if (groupList.contains(register.groupCombobox.getValue())) {
                    System.out.println("GroupName Matched..");
                    strGroup = register.groupCombobox.getValue();
                } else {
                    strGroup = "Unknown Group";
                }
                Calendar cal = Calendar.getInstance();
                String ster = "" + cal.getTime();
                System.out.println(ster);
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
                String strTime, strDate = df.format(cal.getTime());
                strTime = tf.format(cal.getTime());
                int idept = 0;
                try {
                    idept = Integer.parseInt(deptId.get(deptId.size() - 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int igrup = 1;
                System.out.println("Dept no : " + idept + "Grub Numarası : " + igrup);
                System.out.println("Other Values : " + "VALUES ('" + strName + "','" + strSurname + "','" + strPassword + "'," + idept + "," + igrup + ")");
                String insertUserQuery = "INSERT INTO USER (name,surname,password,deptId,grupId,registerDate,roleID)" + "VALUES ('" + strName + "','" + strSurname + "','" + strPassword + "'," + idept + "," + igrup + ",'" + strDate + "',1)";
                try {
                    update(insertUserQuery);
                    register.clearAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Makes the needed processes to be logined
	 */
    public void loginFunction(String entry, String pass) {
        String user = entry;
        System.out.println("Login Function Started..For user : " + user);
        initializeTheConnection();
        ArrayList<String> isAdminList = new ArrayList<String>();
        ArrayList<String> userList = new ArrayList<String>();
        ArrayList<String> userRoleIdList = new ArrayList<String>();
        ArrayList<String> authorityList = new ArrayList<String>();
        int userRoleId;
        String getUserQuery = "SELECT name FROM USER";
        String getRoleIDQuery = "SELECT roleID FROM user WHERE name ='" + user + "'";
        try {
            userRoleIdList = query(getRoleIDQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Role id cannot be loaded..");
        }
        if (!userRoleIdList.isEmpty()) {
            userRoleId = Integer.parseInt(userRoleIdList.get(userRoleIdList.size() - 1));
            String strGetAuthorityQuery = "SELECT name FROM role WHERE id = " + userRoleId + "";
            try {
                authorityList = query(strGetAuthorityQuery);
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("UnKnown Authority...");
            }
        }
        if (authorityList.isEmpty()) {
            System.out.println("Authority list is empty");
        } else {
            authority = authorityList.get(authorityList.size() - 1);
            try {
                System.out.println("Getting User List...");
                userList = query(getUserQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (userList.contains(user)) {
            System.out.println("User name is correct...");
            String userPassQuery = "SELECT password from USER WHERE name = '" + user + "'";
            ArrayList<String> passList = new ArrayList<String>();
            try {
                passList = query(userPassQuery);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            String validPass = passList.get(passList.size() - 1);
            System.out.println("Deneme : " + passList.get(passList.size() - 1).toString());
            if (pass.equals(validPass)) {
                System.out.println("Pass Validated");
                person = "User";
            }
            ArrayList<String> idList = new ArrayList<String>();
            String getIDQuery = "SELECT id FROM user WHERE name  = '" + user + "'";
            try {
                System.out.println("Getting the id of the logined person...");
                idList = query(getIDQuery);
                setLoginedID(Integer.parseInt(idList.get(idList.size() - 1)));
                System.out.println("Hello : " + getLoginedID());
                System.out.println(idList.get(idList.size() - 1).toString());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            if (authority.equals("Admin")) {
                if (pass.equals(validPass)) {
                    System.out.println("Pass Validated");
                    System.out.println("User is Admin..");
                    person = "Admin";
                }
            } else {
                System.out.println("This is no Admin..");
            }
        } else {
            System.out.println("Wrong User Name ...");
        }
        if (person.equals("Admin")) {
            try {
                Messagebox.show("Welcome " + user, "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Admin Entered..");
            AdminPage adminWin = new AdminPage();
            adminWin.setUserID(getLoginedID());
            adminWin.initializeIt();
            adminWin.setParent(this);
            adminWin.setVisible(true);
            adminWin.setHeight("100%");
            adminWin.setWidth("100%");
            winLogin.setVisible(false);
        } else if (person.equals("User")) {
            try {
                Messagebox.show("Welcome " + user, "Information", Messagebox.OK, Messagebox.INFORMATION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("User Entered..");
            CDSystem systemWin = new CDSystem();
            systemWin.setUserID(getLoginedID());
            systemWin.setParent(this);
            systemWin.setVisible(true);
            systemWin.setHeight("100%");
            systemWin.setWidth("100%");
            systemWin.initializeIt();
            winLogin.setVisible(false);
            winLogin.setVisible(false);
        }
        try {
            shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLoginedID() {
        return loginedID;
    }

    public void setLoginedID(int loginedID) {
        this.loginedID = loginedID;
    }

    /**
	 * DATABASE FUNCTIONS
	 */
    public void initializeTheConnection() {
        try {
            String url = "org.hsqldb.jdbcDriver";
            Class.forName(url);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            String url = "jdbc:hsqldb:/media/Yedek/DB";
            String name = "sa";
            String password = "";
            conn = DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() throws SQLException {
        Statement st = conn.createStatement();
        st.execute("SHUTDOWN");
        conn.close();
    }

    public synchronized ArrayList<String> query(String expression) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        ArrayList<String> donenListe = new ArrayList<String>();
        st = conn.createStatement();
        rs = st.executeQuery(expression);
        donenListe = dump(rs);
        st.close();
        return donenListe;
    }

    public synchronized void update(String expression) throws SQLException {
        Statement st = null;
        st = conn.createStatement();
        int i = st.executeUpdate(expression);
        if (i == -1) {
            System.out.println("db error : " + expression);
        }
        st.close();
    }

    public ArrayList<String> dump(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int colmax = meta.getColumnCount();
        int i;
        Object o = null;
        String veri = "";
        String isim = "";
        ArrayList<String> isimler = new ArrayList<String>();
        while (rs.next()) {
            for (i = 0; i < colmax; ++i) {
                o = rs.getObject(i + 1);
                veri = o.toString();
                isimler.add(veri);
            }
            System.out.println(" ");
        }
        return isimler;
    }
}
