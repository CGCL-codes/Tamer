package de.ueppste.ljb.server.model;

import java.sql.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import de.ueppste.ljb.server.Options;
import de.ueppste.ljb.share.User;
import de.ueppste.misc.MySqlCon;

/**
 * Stellt Funktionen bereit um die User in der Datenbank zu bearbeiten
 * @author bernhard
 */
public class ModelUser {

    /** Logger */
    private static Logger logger = Logger.getLogger(ModelUser.class.getName());

    /** Instanz der MySqlVerbindung */
    private MySqlCon database;

    /**
	 * Konstruktor
	 * Stellt die Verbindung zu Datenbank her
	 */
    public ModelUser() {
        this.database = new MySqlCon(Options.getSqlServer(), Options.getSqlDatabase(), Options.getSqlUser(), Options.getSqlPassword());
        try {
            this.database.connect();
        } catch (Exception e) {
            System.exit(-1);
        }
    }

    /**
	 * Gibt die richtige User-Instanz aufgrund Name und Passwort zurueck.
	 * Name und Passwort muss stimmen.
	 * @param user	zu ueberpruefende User-Instanz
	 * @return richtig User Instanz
	 */
    public User getRealUser(User user) {
        if (user == null) return null;
        User realUser = null;
        try {
            PreparedStatement ps = this.database.preparedStatement("SELECT *  FROM `user` WHERE `name` = ? AND `pwHash` = ? LIMIT 1");
            ps.setString(1, user.getName());
            ps.setInt(2, user.getPwHash());
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                realUser = new User(result.getString("name"), result.getInt("pwHash"), result.getInt("id"));
                realUser.setMp3Scan(result.getBoolean("mp3_scan"));
                realUser.setMp3Upload(result.getBoolean("mp3_upload"));
                realUser.setMp3Edit(result.getBoolean("mp3_edit"));
                realUser.setMusikControl(result.getBoolean("musik_control"));
                realUser.setPlaylistItems(result.getInt("playlist_items"));
                realUser.setPlaylistDefiniteElements(result.getInt("playlist_definite_elements"));
                realUser.setPlaylistRemoveAll(result.getBoolean("playlist_remove_all"));
                realUser.setPlaylistRemoveOwn(result.getBoolean("playlist_remove_own"));
                realUser.setPlaylistShuffle(result.getBoolean("playlist_shuffle"));
                realUser.setUserManagement(result.getBoolean("user_management"));
                realUser.setUserVisible(result.getBoolean("user_visible"));
                realUser.setUserLogin(result.getBoolean("user_login"));
                realUser.setUserStatus(result.getInt("user_status"));
                realUser.setSkipthis(result.getBoolean("skipthis"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return realUser;
    }

    /**
	 * Legt einen neuen User in der Datenbank an.
	 * @param user	Anzulegender User: Name, Passwort und Admin müssen gesetzt sein.
	 * @return User erfolgreich angelegt (true oder false)
	 */
    public boolean addUser(User user) {
        if (this.userNameFree(user.getName())) {
            try {
                PreparedStatement ps = this.database.preparedStatement("INSERT INTO `user` (`name`, `pwHash`, `user_login`, `user_visible`, `user_management`, `playlist_items`, `playlist_definite_elements`, `playlist_remove_own`, `playlist_remove_all`, `playlist_shuffle`, `mp3_scan`, `mp3_upload`, `mp3_edit`, `musik_control`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, user.getName());
                ps.setInt(2, user.getPwHash());
                ps.setBoolean(3, user.isUserLogin());
                ps.setBoolean(4, user.isUserVisible());
                ps.setBoolean(5, user.isUserManagement());
                ps.setInt(6, user.getPlaylistItems());
                ps.setInt(7, user.getPlaylistDefiniteElements());
                ps.setBoolean(8, user.isPlaylistRemoveOwn());
                ps.setBoolean(9, user.isPlaylistRemoveAll());
                ps.setBoolean(10, user.isPlaylistShuffle());
                ps.setBoolean(11, user.isMp3Scan());
                ps.setBoolean(12, user.isMp3Upload());
                ps.setBoolean(13, user.isMp3Edit());
                ps.setBoolean(14, user.isMusikControl());
                ps.executeUpdate();
                ps.close();
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        return false;
    }

    /**
	 * Loescht einen User aus dem System.
	 * Id des User muss gesetzt sein.
	 * @param user	zu loeschender User
	 */
    public void delUser(User user) {
        try {
            PreparedStatement ps = this.database.preparedStatement("DELETE FROM `user` WHERE `id` = ? LIMIT 1");
            ps.setInt(1, user.getId());
            ps.executeUpdate();
            ps.close();
            Statement statement = this.database.createStatement();
            statement.executeUpdate("DELETE FROM archiv WHERE (user_id NOT IN(SELECT id FROM user)) AND user_id != 0");
            statement.executeUpdate("DELETE FROM playlist WHERE user_id NOT IN(SELECT id FROM user)");
            statement.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
	 * Bearbeite einen User im System.
	 * Passwort wird nicht geändert.
	 * @param user		User der bearbeitet werden soll
	 * @param newUser	Attribute für den neuen User
	 * @return User wurde bearbeitet (true oder false)
	 */
    public boolean editUser(User user, User newUser) {
        try {
            if (!user.getName().equals(newUser.getName()) && !this.userNameFree(newUser.getName())) return false;
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `name` = ?, `user_login` = ?, `user_visible` = ?, `user_management` = ?, `playlist_items` = ?, `playlist_definite_elements` = ?, `playlist_remove_own` = ?, `playlist_remove_all` = ?, `playlist_shuffle` = ?, `mp3_scan` = ?, `mp3_upload` = ?, `mp3_edit` = ?, `musik_control` = ? WHERE `id` = ? LIMIT 1");
            ps.setString(1, newUser.getName());
            ps.setBoolean(2, newUser.isUserLogin());
            ps.setBoolean(3, newUser.isUserVisible());
            ps.setBoolean(4, newUser.isUserManagement());
            ps.setInt(5, newUser.getPlaylistItems());
            ps.setInt(6, newUser.getPlaylistDefiniteElements());
            ps.setBoolean(7, newUser.isPlaylistRemoveOwn());
            ps.setBoolean(8, newUser.isPlaylistRemoveAll());
            ps.setBoolean(9, newUser.isPlaylistShuffle());
            ps.setBoolean(10, newUser.isMp3Scan());
            ps.setBoolean(11, newUser.isMp3Upload());
            ps.setBoolean(12, newUser.isMp3Edit());
            ps.setBoolean(13, newUser.isMusikControl());
            ps.setInt(14, user.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * Bearbeite einen User im System.
	 * Id des user muss gesetzt sein.
	 * Es wird nur das Passwort geändert
	 * @param userId	Id des Users der bearbeitet werden soll
	 * @param pwHash	Der neue PasswortHash
	 * @return User wurde bearbeitet (true oder false)
	 */
    public boolean editUserPasswort(int userId, int pwHash) {
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `pwHash` = ? WHERE `id` = ? LIMIT 1");
            ps.setInt(1, pwHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * Testet ob ein Username bereits in der Datenbank existiert
	 * @param name	Zu testender Username
	 * @return Name ist noch frei (true oder false)
	 */
    public boolean userNameFree(String name) {
        if (name == null) return false;
        if (name.toLowerCase().equals("server") || name.toLowerCase().equals("")) return false;
        return !this.userNameExist(name);
    }

    /**
	 * Testen ob es einen User mit diesem Namen gibt
	 * @param name Name des zu testenden User
	 * @return boolean
	 */
    public boolean userNameExist(String name) {
        if (name == null) return false;
        boolean ok = false;
        try {
            PreparedStatement ps = this.database.preparedStatement("SELECT count(*) FROM `user` WHERE `name` = ? LIMIT 1");
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                if (result.getInt(1) > 0) ok = true;
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return ok;
    }

    /**
	 * Gibt den aktuellen Vektor von User-Instanzen aus der Datenbank.
	 * Alles außer Passwort ist gesetzt.
	 * @param onlyVisible	Nur User die visible sind?
	 * @return UserVektor
	 */
    public Vector<User> getUserVektor(boolean onlyVisible) {
        Vector<User> userVector = new Vector<User>();
        try {
            PreparedStatement ps;
            if (onlyVisible) ps = this.database.preparedStatement("SELECT *  FROM `user` WHERE `user_visible` = 1 ORDER BY `name` ASC"); else ps = this.database.preparedStatement("SELECT *  FROM `user` ORDER BY `name` ASC");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                User user = new User(result.getString("name"), 0, result.getInt("id"));
                user.setMp3Scan(result.getBoolean("mp3_scan"));
                user.setMp3Upload(result.getBoolean("mp3_upload"));
                user.setMp3Edit(result.getBoolean("mp3_edit"));
                user.setMusikControl(result.getBoolean("musik_control"));
                user.setPlaylistItems(result.getInt("playlist_items"));
                user.setPlaylistDefiniteElements(result.getInt("playlist_definite_elements"));
                user.setPlaylistRemoveAll(result.getBoolean("playlist_remove_all"));
                user.setPlaylistRemoveOwn(result.getBoolean("playlist_remove_own"));
                user.setPlaylistShuffle(result.getBoolean("playlist_shuffle"));
                user.setUserManagement(result.getBoolean("user_management"));
                user.setUserVisible(result.getBoolean("user_visible"));
                user.setUserLogin(result.getBoolean("user_login"));
                user.setUserStatus(result.getInt("user_status"));
                user.setSkipthis(result.getBoolean("skipthis"));
                userVector.add(user);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return userVector;
    }

    /**
	 * Gibt einen User anhand des Namens
	 * @param name Name des Users
	 * @return User
	 */
    public User getUserByName(String name) {
        if (name == null) return null;
        User user = null;
        try {
            PreparedStatement ps = this.database.preparedStatement("SELECT * FROM `user` WHERE `name` = ? LIMIT 1");
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                user = new User(result.getString("name"), -1, result.getInt("id"));
                user.setUserStatus(result.getInt("user_status"));
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return user;
    }

    /**
	 * Setzt den Userstatus auf online
	 * @param user
	 * @return	true oder false
	 */
    public boolean setUserStatusOnline(User user) {
        return this.setUserStatus(user, User.USERONLINE);
    }

    /**
	 * Setzt den Userstatus auf offline
	 * @param user
	 * @return	true oder false
	 */
    public boolean setUserStatusOffline(User user) {
        return this.setUserStatus(user, User.USEROFFLINE);
    }

    /**
	 * Setzt den Userstatus auf away
	 * @param user
	 * @return	true oder false
	 */
    public boolean setUserStatusAway(User user) {
        return this.setUserStatus(user, User.USERAWAY);
    }

    /**
	 * Setzt den Status eines Users und aktualisiert seinen Timestamp
	 * @param user	Zu Ändernder User
	 * @param userStatus	Neuer Status des Users
	 * @return	true oder false
	 */
    public boolean setUserStatus(User user, int userStatus) {
        if (user == null) return false;
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `user_status` = ?, `user_timestamp` = ? WHERE `id` = ? LIMIT 1");
            ps.setInt(1, userStatus);
            ps.setLong(2, System.currentTimeMillis());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * Anzahl der User die Online und Visible sind
	 * @return	Anzahl der Onlineuser
	 */
    public int getUserOnlineCount() {
        int count = 0;
        try {
            PreparedStatement ps = this.database.preparedStatement("SELECT COUNT(*)  FROM `user` WHERE `user_status` = ? AND `user_visible` = 1");
            ps.setInt(1, User.USERONLINE);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                count = result.getInt(1);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return count;
    }

    /**
	 * Setzt den Timestamp des users
	 * @param user	User
	 * @return true oder false
	 */
    public boolean setUserTimestamp(User user) {
        if (user == null) return false;
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `user_timestamp` = ? WHERE `id` = ? LIMIT 1");
            ps.setLong(1, System.currentTimeMillis());
            ps.setInt(2, user.getId());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        return true;
    }

    /**
	 * Setzt User auf offline die den Timeout überschritten haben
	 * @return	true - Zeilen wurden geändert.
	 */
    public boolean checkUserStatus() {
        int changed = 0;
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `user_status` = ? WHERE `user_timestamp` < ? AND `user_status` != ?");
            ps.setInt(1, User.USEROFFLINE);
            ps.setLong(2, System.currentTimeMillis() - (Options.getUserTimeout() + 1) * 1000);
            ps.setInt(3, User.USEROFFLINE);
            changed = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        if (changed != 0) {
            return true;
        }
        return false;
    }

    /**
	 * Setzt den Wunsch eines Userser dieses Lied zu skippen
	 * @param user
	 * @return true -> Skipthis wurde gesetzt; false -> Skipthis wurde nicht gesetzt,
	 */
    public boolean setSkipthis(User user) {
        if (user == null) return false;
        int changed = 0;
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `skipthis` = '1' WHERE `id` = ? AND `skipthis` != 1;");
            ps.setInt(1, user.getId());
            changed = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        if (changed != 0) {
            return true;
        }
        return false;
    }

    /**
	 * Setzt die Skipwünsche aller User zurück
	 * @return true -> es wurde etwas geändert; false -> es wurde nichts geändert
	 */
    public boolean resetSkipthis() {
        int changed = 0;
        try {
            PreparedStatement ps = this.database.preparedStatement("UPDATE `user` SET `skipthis` = 0 WHERE `skipthis` != 0");
            changed = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        if (changed != 0) return true;
        return false;
    }

    /**
	 * Berechnet ob ein Skipwunsch der User erfolgreich gewesen ist
	 * @return true -> es haben genug online User für ein Skip gestimmt; false -> es haben noch nicht genügend User für ein Skip gestimmt
	 */
    public boolean calcSkipthis() {
        int skipvote = 0;
        int percent = Options.getSkipPercentages();
        int online = this.getUserOnlineCount();
        if (online == 0) return false;
        if (percent == 0) return false;
        try {
            PreparedStatement ps = this.database.preparedStatement("SELECT COUNT(*)  FROM `user` WHERE `user_status` = ? AND `user_visible` = 1 AND `skipthis` = 1");
            ps.setInt(1, User.USERONLINE);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                skipvote = result.getInt(1);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        if ((percent * online) <= (skipvote * 100)) return true;
        return false;
    }
}
