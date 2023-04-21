package MyOrm.Data.config;

import MyOrm.Data.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import MyOrm.Data.Mapper.DataBaseConnection;
import MyOrm.Data.Mapper.Fields;
import MyOrm.Data.Mapper.Table;
import StaticClass.Classi;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;

/**
 *
 * @author oscar
 */
public class XmlMapping {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private static DataBaseConnection dbConnection = null;

    private HashMap tables = new HashMap();

    private static jData data = null;

    private String mapFilename = "";

    private String removeDescr(String s) {
        String sx[] = s.split("=");
        return sx[1].substring(1, sx[1].length() - 1);
    }

    private String removeDescrJoin(String s) {
        return s.substring(s.indexOf("=") + 1).replace("\"", "");
    }

    public static jData Connection() {
        return data;
    }

    public XmlMapping(String filename) {
        this.mapFilename = filename;
        init();
    }

    public XmlMapping() {
        if (!StaticClass.Classi.mapFile.equals("")) {
            this.mapFilename = StaticClass.Classi.mapFile;
        }
        init();
    }

    public void init() {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            FileInputStream is = null;
            if (this.mapFilename.equals("")) {
                is = new FileInputStream(classLoader.getResource("MyOrmConfig").getFile() + "/myormmap.xml");
            } else {
                is = new FileInputStream(this.mapFilename);
            }
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementationLS implem = (DOMImplementationLS) builder.getDOMImplementation().getFeature("LS", "3.0");
            builder.setErrorHandler(new MyErrorHandler());
            Document response = factory.newDocumentBuilder().parse(is);
            if (data == null || Classi.threaded) {
                data = new jData();
                if (dbConnection == null) {
                    dbConnection = new DataBaseConnection();
                    try {
                        dbConnection.setUrl(response.getElementsByTagName("url").item(0).getChildNodes().item(0).getNodeValue());
                    } catch (Exception e) {
                        System.out.println("url not set");
                    }
                    try {
                        dbConnection.setDriver(response.getElementsByTagName("driver").item(0).getChildNodes().item(0).getNodeValue());
                    } catch (Exception e) {
                        System.out.println("driver not set");
                    }
                    try {
                        dbConnection.setDataSourceContext(response.getElementsByTagName("datasource").item(0).getChildNodes().item(0).getNodeValue());
                    } catch (Exception e) {
                        System.out.println("driver not set");
                    }
                    try {
                        dbConnection.setUserName(response.getElementsByTagName("username").item(0).getChildNodes().item(0).getNodeValue());
                    } catch (Exception e) {
                        dbConnection.setUserName("");
                        System.out.println("username not set");
                    }
                    try {
                        dbConnection.setPassword(response.getElementsByTagName("password").item(0).getChildNodes().item(0).getNodeValue());
                    } catch (Exception e) {
                        dbConnection.setPassword("");
                        System.out.println("password not set");
                    }
                }
                data.setDriverName(getDbConnection().getDriver());
                data.setUrl(getDbConnection().getUrl());
                data.setUserName(getDbConnection().getUserName());
                data.setPassword(getDbConnection().getPassword());
                data.setDataSourceContext(getDbConnection().getDataSourceContext());
                data.start();
            }
            NodeList database = response.getElementsByTagName("table");
            for (int i = 0; i < database.getLength(); i++) {
                NodeList ls = database.item(i).getChildNodes();
                Table tb = new Table();
                ArrayList<Fields> fd = new ArrayList<Fields>();
                ArrayList<Fields> keyLs = new ArrayList<Fields>();
                ArrayList<Fields> forenkeyLs = new ArrayList<Fields>();
                String nTable = removeDescr((String) database.item(i).getAttributes().getNamedItem("name").toString());
                tb.setTableName(nTable);
                String nList = removeDescr((String) database.item(i).getAttributes().getNamedItem("list").toString());
                tb.setListName(nList);
                String nBean = removeDescr((String) database.item(i).getAttributes().getNamedItem("bean").toString());
                tb.setClassName(nBean);
                boolean field = false;
                boolean key = false;
                boolean forenkey = false;
                for (int j = 1; j < ls.getLength(); j++) {
                    field = ls.item(j).getNodeName().equals("field");
                    key = ls.item(j).getNodeName().equals("key");
                    forenkey = ls.item(j).getNodeName().equals("forenfield");
                    if (!ls.item(j).getNodeName().equals("#text")) {
                        if (field) {
                            Fields fd1 = new Fields();
                            fd1.setTableField(removeDescr(ls.item(j).getAttributes().getNamedItem("name").toString()));
                            fd1.setClassField(removeDescr(ls.item(j).getAttributes().getNamedItem("beanField").toString()));
                            fd1.setTypeField(removeDescr(ls.item(j).getAttributes().getNamedItem("type").toString()));
                            if (fd1.getTypeField().equals("autoinc")) {
                                fd1.setTypeField("Long");
                                fd1.setAutoinc(true);
                            }
                            fd.add(fd1);
                            field = false;
                            key = false;
                            forenkey = false;
                        } else if (key) {
                            Fields fd1 = new Fields();
                            fd1.setTableField(removeDescr(ls.item(j).getAttributes().getNamedItem("name").toString()));
                            fd1.setClassField(removeDescr(ls.item(j).getAttributes().getNamedItem("beanField").toString()));
                            fd1.setTypeField("key");
                            keyLs.add(fd1);
                            key = false;
                            forenkey = false;
                        } else if (forenkey) {
                            Fields fd1 = new Fields();
                            fd1.setTable(removeDescr(ls.item(j).getAttributes().getNamedItem("table").toString()));
                            fd1.setBeanField(removeDescr(ls.item(j).getAttributes().getNamedItem("beanField").toString()));
                            fd1.setJoin(removeDescrJoin(ls.item(j).getAttributes().getNamedItem("join").toString()));
                            fd1.setTypeField("forenkey");
                            forenkeyLs.add(fd1);
                            forenkey = false;
                            key = false;
                        }
                    }
                }
                tb.setFields((Fields[]) fd.toArray(new Fields[fd.size()]));
                tb.setKey((Fields[]) keyLs.toArray(new Fields[keyLs.size()]));
                tb.setForenkey((Fields[]) forenkeyLs.toArray(new Fields[forenkeyLs.size()]));
                tables.put(tb.getListName().substring(tb.getListName().lastIndexOf(".") + 1), tb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Table searchTable(String tblName) {
        return (Table) getTables().get(tblName);
    }

    public Table searchTableForBeanName(String beanName) {
        Iterator it = getTables().values().iterator();
        while (it.hasNext()) {
            Table tmp = ((Table) it.next());
            if (tmp.getClassName().equals(beanName)) {
                return tmp;
            }
        }
        return null;
    }

    public Table[] tables() {
        ArrayList<Table> tt = new ArrayList<Table>();
        Iterator it = getTables().values().iterator();
        while (it.hasNext()) {
            Table tmp = ((Table) it.next());
            tt.add(tmp);
        }
        return (Table[]) tt.toArray(new Table[tt.size()]);
    }

    public String[] listaClassi() {
        ArrayList ls = new ArrayList();
        Iterator it = getTables().values().iterator();
        while (it.hasNext()) {
            Table tbl = (Table) it.next();
            ls.add(tbl.getClassName());
        }
        return (String[]) ls.toArray(new String[ls.size()]);
    }

    public DataBaseConnection getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(DataBaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public jData Data() {
        return data;
    }

    public HashMap getTables() {
        return tables;
    }

    public void setTables(HashMap tables) {
        this.tables = tables;
    }
}
