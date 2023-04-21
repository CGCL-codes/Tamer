package fabrique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import donnee.Mission;

public class FabMission {

    public static FabMission fm;

    private static PreparedStatement pcreerMission;

    private static PreparedStatement plisterID;

    private static PreparedStatement plisterMission;

    private static PreparedStatement prechercherMission;

    private static PreparedStatement prechercherMissionsduClient;

    private static PreparedStatement prechercherMissiondelExpertiseMission;

    private static PreparedStatement prechercherMissiondelaPlanificationMission;

    private static PreparedStatement pupdateEtatMission;

    private static String creerMission = "insert into Mission values (?,?,?,?,?)";

    private static String listerID = "select ID from Mission order by ID";

    private static String listerMission = "select * from Mission order by ID";

    private static String rechercherMission = "select Mission.id,Mission.libelle,Mission.etatPlanification,Mission.nbJours from Mission where Mission.id=?";

    private static String rechercherMissionsduClient = "select Mission.id,Mission.libelle,Mission.etatPlanification,Mission.nbJours from Mission,Client where Mission.nomClient = Client.nom and Client.nom=?";

    private static String rechercherMissiondelExpertiseMission = "select Mission.id,Mission.libelle,Mission.etatPlanification,Mission.nbJours from Mission,ExpertiseMission where Mission.id = ExpertiseMission.idMission and ExpertiseMission.id=?";

    private static String rechercherMissiondelaPlanificationMission = "select Mission.id,Mission.libelle,Mission.etatPlanification,Mission.nbJours from Mission,PlanificationMission where Mission.id = PlanificationMission.idMission and PlanificationMission.id=?";

    private static String updateEtatMission = "update Mission set etatplanification=? where Mission.id=?";

    public static synchronized FabMission getInstance() {
        if (fm == null) fm = new FabMission();
        return fm;
    }

    public static void setConnection(Connection c) {
        try {
            pcreerMission = c.prepareStatement(creerMission);
            plisterID = c.prepareStatement(listerID);
            plisterMission = c.prepareStatement(listerMission);
            prechercherMission = c.prepareStatement(rechercherMission);
            prechercherMissionsduClient = c.prepareStatement(rechercherMissionsduClient);
            prechercherMissiondelExpertiseMission = c.prepareStatement(rechercherMissiondelExpertiseMission);
            prechercherMissiondelaPlanificationMission = c.prepareStatement(rechercherMissiondelaPlanificationMission);
            pupdateEtatMission = c.prepareStatement(updateEtatMission);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private FabMission() {
    }

    public Mission creerMission(String libelle, boolean etat, int nbJours, String nomClient) {
        int idM = 0;
        ResultSet res = null;
        try {
            res = plisterID.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            while (res.next()) idM = res.getInt("id");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        idM++;
        try {
            pcreerMission.setInt(1, idM);
            pcreerMission.setString(2, libelle);
            pcreerMission.setBoolean(3, etat);
            pcreerMission.setInt(4, nbJours);
            pcreerMission.setString(5, nomClient);
            pcreerMission.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Mission(idM, libelle, etat, nbJours);
    }

    public Mission rechercherMission(int idM) {
        try {
            prechercherMission.setInt(1, idM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = prechercherMission.executeQuery();
            while (res.next()) {
                return new Mission(res.getInt(1), res.getString(2), res.getBoolean(3), res.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterator<Mission> rechercherMissionsduClient(String nom) {
        try {
            prechercherMissionsduClient.setString(1, nom);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = prechercherMissionsduClient.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Mission> lesMiss = new ArrayList<Mission>();
        try {
            while (res.next()) {
                lesMiss.add(new Mission(res.getInt(1), res.getString(2), res.getBoolean(3), res.getInt(4)));
            }
            return lesMiss.iterator();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mission rechercherMissiondelExpertiseMission(int idEM) {
        try {
            prechercherMissiondelExpertiseMission.setInt(1, idEM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = prechercherMissiondelExpertiseMission.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (res.next()) return new Mission(res.getInt(1), res.getString(2), res.getBoolean(3), res.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Mission rechercherMissiondelaPlanificationMission(int idPM) {
        try {
            prechercherMissiondelaPlanificationMission.setInt(1, idPM);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ResultSet res = null;
        try {
            res = prechercherMissiondelaPlanificationMission.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (res.next()) return new Mission(res.getInt(1), res.getString(2), res.getBoolean(3), res.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterator<Mission> lister() {
        ResultSet res = null;
        try {
            res = plisterMission.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Mission> list = new ArrayList<Mission>();
        try {
            while (res.next()) list.add(new Mission(res.getInt(1), res.getString(2), res.getBoolean(3), res.getInt(4)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.iterator();
    }

    public void setEtatMission(boolean etat, int id) {
        try {
            pupdateEtatMission.setBoolean(1, etat);
            pupdateEtatMission.setInt(2, id);
            pupdateEtatMission.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
