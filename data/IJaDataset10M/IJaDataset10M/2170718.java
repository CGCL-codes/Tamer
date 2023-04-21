package bioroot;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import bioroot.oligo.*;
import bioroot.plasmid.*;
import bioroot.antibody.*;
import java.io.*;
import util.gen.*;
import bioroot.strain.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

/**
 * @author nix
 * Creates a tab delimited text file for download, a full copy of a users collection limited by user type.
 */
public class Archive extends HttpServlet {

    private static Logger log = Logger.getLogger(Archive.class);

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserBean userBean = Util.fetchUserBean(request, response);
        DBUtil bioRoot = null;
        String redirectPage = "WEB-INF/Jsps/Util/Archive.jsp";
        if (userBean.isGuestOrFriend()) {
            request.setAttribute("archiveMessage", "<p>Sorry, only standard users and super users can archive a collection.<p>");
        } else {
            bioRoot = new DBUtil("orgbioro_bioroot");
            OligoBean[] oligoBeans = Archive.makeOligoBeans(bioRoot, userBean);
            PlasmidBean[] plasmidBeans = Archive.makePlasmidBeans(bioRoot, userBean);
            StrainBean[] strainBeans = Archive.makeStrainBeans(bioRoot, userBean);
            AntibodyBean[] antibodyBeans = Archive.makeAntibodyBeans(bioRoot, userBean);
            boolean oligos = oligoBeans != null && oligoBeans.length != 0;
            boolean plasmids = plasmidBeans != null && plasmidBeans.length != 0;
            boolean strains = strainBeans != null && strainBeans.length != 0;
            boolean antibodies = antibodyBeans != null && antibodyBeans.length != 0;
            if (oligos == false && plasmids == false && strains == false && antibodies == false) request.setAttribute("archiveMessage", "<p>No reagents were found to archive?!<p>"); else {
                String oligoTable = "";
                if (oligos) {
                    oligoTable = Archive.fetchTabbedTableColumnRow(userBean.getOligoPreference().getFeatures()) + "\n" + OligoSpreadSheet.fetchOligoTableRows(userBean.getOligoPreference(), oligoBeans, new int[] { 0, 0 }, bioRoot, false, true);
                }
                String plasmidTable = "";
                if (plasmids) {
                    plasmidTable = Archive.fetchTabbedTableColumnRow(userBean.getPlasmidPreference().getFeatures()) + "\n" + PlasmidSpreadSheet.fetchPlasmidTableRows(userBean, plasmidBeans, null, bioRoot, false, true);
                }
                String strainTable = "";
                if (strains) {
                    strainTable = Archive.fetchTabbedTableColumnRow(userBean.getStrainPreference().getFeatures()) + "\n" + StrainSpreadSheet.fetchStrainTableRows(userBean, strainBeans, null, bioRoot, false, true);
                }
                String antibodyTable = "";
                if (antibodies) {
                    antibodyTable = Archive.fetchTabbedTableColumnRow(userBean.getAntibodyPreference().getFeatures()) + "\n" + AntibodySpreadSheet.fetchAntibodyTableRows(userBean, antibodyBeans, null, bioRoot, false, true);
                }
                StringBuffer tableSB = new StringBuffer("BioRoot ");
                tableSB.append(userBean.getLabGroup());
                if (userBean.isSuperUser()) tableSB.append(" Complete Archive "); else tableSB.append(" Partial Archive ");
                tableSB.append(Misc.getDate());
                tableSB.append("\n");
                if (oligos) {
                    tableSB.append("\nOligos...\n");
                    tableSB.append(oligoTable);
                }
                if (plasmids) {
                    tableSB.append("\nPlasmids...\n");
                    tableSB.append(plasmidTable);
                }
                if (strains) {
                    tableSB.append("\nStrains...\n");
                    tableSB.append(strainTable);
                }
                if (antibodies) {
                    tableSB.append("\nAntibodies...\n");
                    tableSB.append(antibodyTable);
                }
                String table = tableSB.toString();
                table = table.replaceAll("&nbsp;", " ");
                table = table.replaceAll("<small>", "");
                table = table.replaceAll("</small>", "");
                table = table.replaceAll("&micro;", "u");
                table = table.replaceAll("&lt;", "<");
                table = table.replaceAll("&gt;", ">");
                table = table.replaceAll("&#34;", "\"");
                table = table.replaceAll("&#39;", "'");
                StringBuffer name = new StringBuffer();
                name.append(Misc.getDate().replaceAll(" ", "") + "_" + userBean.getLabGroup());
                if (userBean.isSuperUser()) name.append("All_"); else name.append("Partial_");
                name.append(Passwords.createRandowWord(4));
                ArrayList filesAL = fetchFiles(plasmidBeans, strainBeans, antibodyBeans, Util.filepath, bioRoot);
                File unzipped = new File(Util.filepath + "Archives", name.toString() + ".txt");
                try {
                    PrintWriter out = new PrintWriter(new FileWriter(unzipped));
                    out.print(table);
                    out.close();
                    filesAL.add(unzipped);
                    File[] filesToZip = new File[filesAL.size()];
                    filesAL.toArray(filesToZip);
                    File zipped = new File(Util.filepath + "Archives", name.toString() + ".zip");
                    IO.zip(filesToZip, zipped);
                    unzipped.delete();
                    StringBuffer message = new StringBuffer("<br><br><br>");
                    if (Misc.isNotEmpty(oligoTable)) message.append(oligoBeans.length + " Oligos were parsed.<p>");
                    if (Misc.isNotEmpty(plasmidTable)) message.append(plasmidBeans.length + " Plasmids were extracted.<p>");
                    if (Misc.isNotEmpty(strainTable)) message.append(strainBeans.length + " Strains were extracted.<p>");
                    if (Misc.isNotEmpty(antibodyTable)) message.append(antibodyBeans.length + " Antibodies were extracted.<p>");
                    if (userBean.isSuperUser() == false) message.append("This represents only part of your lab's collection. Ask a super user to archive the entire collection.<p>");
                    message.append("Download and save the following &nbsp;&nbsp;&nbsp;&nbsp;<a href='" + Util.HTMLPath + "Archives/" + zipped.getName() + "'>" + zipped.getName() + "</a><br><br><br><br>");
                    request.setAttribute("archiveMessage", "<p>" + message + "<p>");
                    File[] files = new File(Util.filepath + "Archives").listFiles();
                    int numFiles = files.length;
                    long now = System.currentTimeMillis();
                    for (int i = 0; i < numFiles; i++) {
                        if (now - files[i].lastModified() > 100000) files[i].delete();
                    }
                } catch (Exception e) {
                    log.error("Problem with zipping", e);
                    request.setAttribute("archiveMessage", "<p>A problem has occured while attempting to archive your " + "collection.  If you would, contact BioRoot ASAP. Note this time stamp:" + new java.util.Date() + "<p>");
                }
            }
        }
        if (bioRoot != null) bioRoot.closeConnection();
        RequestDispatcher dispatcher = request.getRequestDispatcher(redirectPage);
        dispatcher.forward(request, response);
    }

    /**Fetches any plasmid or strain files.*/
    public static ArrayList fetchFiles(PlasmidBean[] plasmids, StrainBean[] strains, AntibodyBean[] antibodies, String filePath, DBUtil bioRoot) {
        ArrayList a = new ArrayList();
        if (plasmids != null) {
            int num = plasmids.length;
            String path = filePath + "Upload/PlasmidFiles/";
            for (int i = 0; i < num; i++) {
                if (Misc.isNotEmpty(plasmids[i].getFileName())) {
                    File f = new File(path + plasmids[i].getFileName());
                    if (f.exists()) a.add(f); else log.error("Problem, one of the plasmid files is missing when Archive was called. " + f);
                }
            }
        }
        if (strains != null) {
            int num = strains.length;
            String path = filePath + "Upload/StrainFiles/";
            for (int i = 0; i < num; i++) {
                if (Misc.isNotEmpty(strains[i].getFileName())) {
                    File f = new File(path + strains[i].getFileName());
                    if (f.exists()) a.add(f); else log.error("Problem, one of the strain files is missing when Archive was called. " + f);
                }
            }
        }
        if (antibodies != null) {
            int num = antibodies.length;
            String path = filePath + "Upload/AntibodyFiles/";
            for (int i = 0; i < num; i++) {
                if (Misc.isNotEmpty(antibodies[i].getFileName())) {
                    File f = new File(path + antibodies[i].getFileName());
                    if (f.exists()) a.add(f); else log.error("Problem, one of the antibody files is missing when Archive was called. " + f);
                    AntibodyPrep[] preps = antibodies[i].getAntibodyPreps(bioRoot);
                    for (int j = 0; j < preps.length; j++) {
                        if (Misc.isNotEmpty(preps[j].getFileName())) {
                            File file = new File(path + preps[j].getFileName());
                            if (file.exists()) a.add(file); else log.error("Problem, one of the antibody prep files is missing when Archive was called. " + f);
                        }
                    }
                }
            }
        }
        return a;
    }

    /**Makes an array of all visibile OligoBeans*/
    public static OligoBean[] makeOligoBeans(DBUtil bioRoot, UserBean userBean) {
        String sql = buildSqlStatement(userBean, "Oligo");
        OligoBean[] oligoBeans = null;
        if (sql != null) oligoBeans = OligoSpreadSheet.fetchOligoBeans(sql, bioRoot);
        return oligoBeans;
    }

    /**Makes an array of all visibile PlasmidBeans*/
    public static PlasmidBean[] makePlasmidBeans(DBUtil bioRoot, UserBean userBean) {
        String sql = buildSqlStatement(userBean, "Plasmid");
        PlasmidBean[] plasmidBeans = null;
        if (sql != null) plasmidBeans = PlasmidSpreadSheet.fetchPlasmidBeans(sql, bioRoot);
        return plasmidBeans;
    }

    /**Makes an array of all visibile AntibodyBeans*/
    public static AntibodyBean[] makeAntibodyBeans(DBUtil bioRoot, UserBean userBean) {
        String sql = buildSqlStatement(userBean, "Antibody");
        AntibodyBean[] beans = null;
        if (sql != null) beans = AntibodySpreadSheet.fetchAntibodyBeans(sql, bioRoot);
        return beans;
    }

    /**Makes an array of all visibile StrainBeans*/
    public static StrainBean[] makeStrainBeans(DBUtil bioRoot, UserBean userBean) {
        String sql = buildSqlStatement(userBean, "Strain");
        StrainBean[] strainBeans = null;
        if (sql != null) strainBeans = StrainSpreadSheet.fetchStrainBeans(sql, bioRoot);
        return strainBeans;
    }

    /**Returns a table header row using the Preferece obj*/
    public static String fetchTabbedTableColumnRow(Feature[] features) {
        int numFeatures = features.length;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numFeatures; i++) {
            sb.append(features[i].getName());
            sb.append("\t");
        }
        return sb.toString();
    }

    /**Build tab delimited table rows.*/
    public static String buildTabTableRows(String[][] columnRows) {
        int numColumns = columnRows.length;
        int numBeans = columnRows[0].length;
        StringBuffer sb = new StringBuffer(numBeans * numColumns * 10);
        Pattern pat = Pattern.compile("\\n|\\t|\\r");
        Matcher mat;
        for (int i = 0; i < numBeans; i++) {
            mat = pat.matcher(columnRows[0][i]);
            sb.append(mat.replaceAll(" "));
            for (int j = 1; j < numColumns; j++) {
                sb.append("\t");
                if (Misc.isNotEmpty(columnRows[j][i])) {
                    mat = pat.matcher(columnRows[j][i]);
                    sb.append(mat.replaceAll(" "));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**Creates a sql statement for super users and standard users returns null for everyone else, lab specific.*/
    public static String buildSqlStatement(UserBean user, String tableName) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM " + tableName + " WHERE");
        if (user.isSuperUser()) {
            sb.append(" labGroupId=");
            sb.append(user.getLabGroupId());
        } else if (user.getType().equals("standard")) {
            sb.append(" labGroupId=");
            sb.append(user.getLabGroupId());
            sb.append(" AND (visibility IN ('WWW', 'Lab Mates') OR ownerId=");
            sb.append(user.getId());
            sb.append(")");
        } else return null;
        sb.append(" ORDER BY id DESC");
        return sb.toString();
    }
}
