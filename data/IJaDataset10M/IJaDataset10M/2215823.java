package citizenAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CollectCitizen {

    public static BufferedWriter writer;

    public static File f;

    public static DocumentBuilderFactory factory;

    public static DocumentBuilder builder;

    public static Document doc;

    public static Connection connect = null;

    public static Statement stat = null;

    public static ResultSet rs = null;

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, SQLException {
        Initial();
        File counxml = new File("F:\\erep\\countries.xml");
        File resultfile = new File("F:\\erep\\citizens.txt");
        FileWriter filewriter;
        filewriter = new FileWriter(resultfile);
        writer = new BufferedWriter(filewriter);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(counxml);
        NodeList nl = doc.getElementsByTagName("country");
        String name = null;
        String fname = "";
        for (int i = 0; i < nl.getLength(); i++) {
            Node country = nl.item(i);
            NodeList info = country.getChildNodes();
            for (int j = 0; j < info.getLength(); j++) {
                Node temp = info.item(j);
                if (temp.getNodeName().equals("name")) {
                    name = temp.getTextContent();
                    break;
                }
            }
            String[] split = name.split(" ");
            System.out.println(name);
            for (int m = 0; m < split.length; m++) {
                fname += split[m] + "_";
            }
            fname += "citizens.xml";
            LoadToTxt(fname);
            fname = "";
        }
        stat.execute("Load Data InFile 'F:/erep/citizens.txt' Into Table `citizen` Lines Terminated By '\r\n';");
    }

    public static void Initial() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/erep?user=root&password=mysql");
            stat = connect.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadToTxt(String name) throws IOException {
        try {
            f = new File("D:\\Downloads\\" + name);
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            NodeList nl = doc.getElementsByTagName("citizen");
            Citizen temp = new Citizen();
            String buff = null;
            int stars = 0;
            int rank = 0;
            for (int i = 0; i < nl.getLength(); i++) {
                Node citizen = nl.item(i);
                NodeList citizenInfo = citizen.getChildNodes();
                for (int j = 0; j < citizenInfo.getLength(); j++) {
                    Node node = citizenInfo.item(j);
                    if (node.getNodeName().equals("is-organization")) {
                        if (node.getTextContent().equals("true")) {
                            temp.isorg = "org";
                        } else {
                            temp.isorg = "citizen";
                        }
                    }
                    if (node.getNodeName() == "citizenship") {
                        NodeList cship = node.getChildNodes();
                        for (int n = 0; n < cship.getLength(); n++) if (cship.item(n).getNodeName() == "country") {
                            buff = cship.item(n).getChildNodes().item(1).getTextContent();
                            temp.country = Integer.valueOf(buff);
                        }
                    }
                    if (node.getNodeName() == "sex") {
                    }
                    if (node.getNodeName() == "military") {
                        NodeList mili = node.getChildNodes();
                        for (int k = 0; k < mili.getLength(); k++) {
                            if (mili.item(k).getNodeName() == "stars") {
                                buff = mili.item(k).getTextContent();
                                if (buff != "") stars = Integer.valueOf(buff); else stars = 0;
                            }
                            if (mili.item(k).getNodeName() == "rank") {
                                buff = mili.item(k).getTextContent();
                                rank = RankToInt(buff);
                            }
                            if (mili.item(k).getNodeName() == "rank-points") {
                                buff = mili.item(k).getTextContent();
                                if (buff != "") temp.rankpoint = Integer.valueOf(buff); else temp.rankpoint = 0;
                            }
                        }
                    }
                    if (node.getNodeName() == "military-skills") {
                        NodeList strength = node.getChildNodes();
                        for (int l = 0; l < strength.getLength(); l++) {
                            if (strength.item(l).getNodeName() == "military-skill") {
                                NodeList sp = strength.item(l).getChildNodes();
                                for (int m = 0; m < sp.getLength(); m++) {
                                    if (sp.item(m).getNodeName() == "points") {
                                        buff = sp.item(m).getTextContent();
                                        if (buff != "") temp.strength = Double.parseDouble(buff); else temp.strength = 0;
                                    }
                                }
                            }
                        }
                    }
                    if (node.getNodeName() == "work-skill-points") {
                        buff = node.getTextContent();
                        if (buff != "") temp.workskill = Integer.valueOf(buff); else temp.workskill = 0;
                    }
                    if (node.getNodeName() == "name") {
                        temp.name = node.getTextContent();
                        String[] x = temp.name.split("'");
                        temp.name = x[0];
                        if (x.length != 1) for (int y = 1; y < x.length; y++) temp.name += "\\\'" + x[y];
                    }
                    if (node.getNodeName() == "id") {
                        buff = node.getTextContent();
                        temp.id = Integer.valueOf(buff);
                    }
                    if (node.getNodeName() == "experience-points") {
                        buff = node.getTextContent();
                        if (buff != "") temp.exp = Integer.valueOf(buff); else temp.exp = 0;
                    }
                }
                temp.mililevel = stars + rank;
                double influ = (temp.mililevel + 5) * (temp.strength + 400) / 200;
                writer.write(temp.name + "\t" + temp.mililevel + "\t" + temp.isorg + "\t" + temp.rankpoint + "\t" + temp.country + "\t" + temp.workskill + "\t" + temp.strength + "\t" + temp.exp + "\t" + temp.id + "\t" + influ + "\r\n");
                writer.flush();
            }
            nl = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc = null;
        f = null;
        factory = null;
        builder = null;
    }

    public static int RankToInt(String rank) {
        if (rank.equals("Recruit")) return 1;
        if (rank.equals("Private")) return 2;
        if (rank.equals("Corporal")) return 6;
        if (rank.equals("Sergeant")) return 10;
        if (rank.equals("Lieutenant")) return 14;
        if (rank.equals("Captain")) return 18;
        if (rank.equals("Major")) return 22;
        if (rank.equals("Commander")) return 26;
        if (rank.equals("Lt Colonel")) return 30;
        if (rank.equals("Colonel")) return 34;
        if (rank.equals("General")) return 38;
        if (rank.equals("Field Marshal")) return 42;
        if (rank.equals("Supreme Marshal")) return 46;
        if (rank.equals("National Force")) return 50;
        if (rank.equals("World Class Force")) return 54;
        if (rank.equals("Legendary Force")) return 58;
        if (rank.equals("God of War")) return 62;
        return 0;
    }
}
