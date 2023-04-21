package model;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

public class DAO {

    private Connection connection;

    private static final String url = "jdbc:mysql://localhost:3306/media?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";

    private static final String driver = "com.mysql.jdbc.Driver";

    private static final String username = "root";

    private static final String password = "project";

    public DAO() {
        try {
            Class.forName(driver);
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createUser(User user) {
        boolean result = false;
        String command = "INSERT INTO USER(fullName, birthday, email, userName, password, log) VALUE( ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getBirthday().toString());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getUserName());
            statement.setString(5, user.getPassword());
            statement.setInt(6, user.getLog());
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean login(String username, String password, int log) {
        boolean result = false;
        String command = "SELECT * FROM USER WHERE (username = ? AND password = ?) AND log = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setInt(3, log);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = true;
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean updateUser(User user) {
        boolean result = false;
        String command = "UPDATE USER set fullname = ?, birthday = ?, email = ?, password = ?, log = ? WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, user.getFullName());
            statement.setString(2, user.getBirthday().toString());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            statement.setInt(5, user.getLog());
            statement.setString(6, user.getUserName());
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean updateLog(String username, int log) {
        boolean result = false;
        String command = "UPDATE USER SET log = ? WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, log);
            statement.setString(2, username);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public User getUser(String username) {
        User user = null;
        String command = "SELECT * FROM USER WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                user = new User();
                user.setFullName(set.getString(1));
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate(2));
                user.setBirthday(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
                user.setEmail(set.getString(3));
                user.setUserName(set.getString(4));
                user.setPassword(set.getString(5));
                user.setLog(set.getInt(6));
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public String recoverUser(String email) {
        String result = "";
        String command = "SELECT username, password FROM USER WHERE email = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, email);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getString(1) + "\r\n" + set.getString(2);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean close() {
        try {
            this.connection.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createAPlaylist(String username, String playlistNameNew) {
        boolean result = false;
        String command = "INSERT INTO Playlist(username, playlistname) VALUE( ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            statement.setString(2, playlistNameNew);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deletePlaylist(String username, String playlistName) {
        boolean result = false;
        String command = "DELETE FROM PLAYLIST WHERE username = ? AND playlistname = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            statement.setString(2, playlistName);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int findByUNPlaylist(String username, String playlistName) {
        int result = -1;
        String command = "SELECT playlistid FROM Playlist WHERE username= ? AND playlistname = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            statement.setString(2, playlistName);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt(1);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean updatePlaylistName(int playlistid, String playlistNameNew) {
        boolean result = false;
        String command = "UPDATE PLaylist SET playlistname = ? WHERE playlistid = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, playlistNameNew);
            statement.setInt(2, playlistid);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Playlist> getAllPlaylistName(String username) {
        ArrayList<Playlist> result = new ArrayList<Playlist>();
        String command = "SELECT playlistname FROM Playlist WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                Playlist playlist = new Playlist(set.getString(1), true, new ArrayList<Song>());
                result.add(playlist);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int findIDPlaylist(String username, String playlistName) {
        int result = -1;
        String command = "SELECT playlistid playlistid FROM Playlist WHERE username = ? AND playlistName = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, username);
            statement.setString(2, playlistName);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt(1);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean deleteASongInPlaylist(int playlistid, int songid) {
        String command = "DELETE  FROM PlSong WHERE playlistid = ? AND songid= ? ";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, playlistid);
            statement.setInt(2, songid);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addASongPlaylist(int playlistid, int songid) {
        String command = "INSERT INTO PlSong(playlistid, songid) VALUE( ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, playlistid);
            statement.setInt(2, songid);
            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createASongInfo(Song song) {
        boolean result = false;
        String command = "INSERT INTO SONG( name, performer, writer, genrename, rate, size, date) VALUE(?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, song.getName());
            statement.setString(2, song.getPerformer());
            statement.setString(3, song.getWriter());
            statement.setString(4, song.getGenre());
            statement.setInt(5, song.getRate());
            statement.setLong(6, song.getSize());
            statement.setString(7, song.getDate().toString());
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Song> findByNSong(String name) {
        ArrayList<Song> result = new ArrayList<Song>();
        String command = "SELECT songid, name, performer, writer, genrename, rate, size, date FROM SONG WHERE name LIKE ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, "%" + name + "%");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int songid = set.getInt("songid");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate("date"));
                Song song = new Song(set.getString("name"), set.getString("performer"), set.getString("writer"), set.getString("genrename"), set.getInt("rate"), set.getLong("size"), true, new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), "http://192.168.1.102:8080/WebMedia/SongDownload?songid=" + songid);
                result.add(song);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Song> findByPSong(String performer) {
        ArrayList<Song> result = new ArrayList<Song>();
        String command = "SELECT songid, name, performer, writer, genrename, rate, size, date FROM SONG WHERE performer LIKE ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, "%" + performer + "%");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int songid = set.getInt("songid");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate("date"));
                Song song = new Song(set.getString("name"), set.getString("performer"), set.getString("writer"), set.getString("genrename"), set.getInt("rate"), set.getLong("size"), true, new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), "http://192.168.1.102:8080/WebMedia/SongDownload?songid=" + songid);
                result.add(song);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Song> findByWriter(String writer) {
        ArrayList<Song> result = new ArrayList<Song>();
        String command = "SELECT songid, name, performer, writer, genrename, rate, size, date FROM SONG WHERE writer LIKE ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, "%" + writer + "%");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int songid = set.getInt("songid");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate("date"));
                Song song = new Song(set.getString("name"), set.getString("performer"), set.getString("writer"), set.getString("genrename"), set.getInt("rate"), set.getLong("size"), true, new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), "http://192.168.1.102:8080/WebMedia/SongDownload?songid=" + songid);
                result.add(song);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Song> findByGSong(String genre) {
        ArrayList<Song> result = new ArrayList<Song>();
        String command = "SELECT songid, name, performer, writer, genrename, rate, size, date FROM SONG WHERE genrename LIKE ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, "%" + genre + "%");
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                int songid = set.getInt("songid");
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate("date"));
                Song song = new Song(set.getString("name"), set.getString("performer"), set.getString("writer"), set.getString("genrename"), set.getInt("rate"), set.getLong("size"), true, new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)), "http://192.168.1.102:8080/WebMedia/SongDownload?songid=" + songid);
                result.add(song);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean uploadSongResource(int songid, InputStream inp) {
        boolean result = false;
        String command = "UPDATE SONG SET resource = ? WHERE songid = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setBinaryStream(1, inp);
            statement.setInt(2, songid);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public InputStream downloadSongResource(int songid) {
        InputStream result = null;
        String command = "SELECT resource FROM SONG WHERE songid= ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, songid);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getBinaryStream(1);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Song findByIdSong(int songid) {
        Song result = null;
        String command = "SELECT name, performer, writer, genrename, rate, size, date FROM SONG WHERE songid= ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, songid);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = new Song();
                result.setOnline(true);
                result.setResource("http://192.168.1.102:8080/WebMedia/SongDownload?songid=" + songid);
                result.setName(set.getString(1));
                result.setPerformer(set.getString(2));
                result.setWriter(set.getString(3));
                result.setGenre(set.getString(4));
                result.setRate(set.getInt(5));
                result.setSize(set.getInt(6));
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTime(set.getDate(7));
                result.setDate(new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList findByPIPlSong(int playlistid) {
        ArrayList result = new ArrayList();
        String command = "SELECT songid FROM PLSONG WHERE playlistid = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, playlistid);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                result.add(set.getInt(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public int findIDSong(String name, String performer, String writer) {
        int result = -1;
        String command = "SELECT  songid FROM SONG WHERE (name = ? AND performer = ?) AND (writer = ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setString(1, name);
            statement.setString(2, performer);
            statement.setString(3, writer);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getInt(1);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public long getASongSize(int songid) {
        long result = -1;
        String command = "SELECT size FROM SONG WHERE songid = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, songid);
            ResultSet set = statement.executeQuery();
            if (set.next()) {
                result = set.getLong(1);
            }
            set.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean rateSong(int songid, int rate) {
        boolean result = false;
        String command = "UPDATE SONG SET rate = rate + ? WHERE songid = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.setInt(1, rate);
            statement.setInt(2, songid);
            result = statement.executeUpdate() > 0;
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        DAO dao = new DAO();
    }
}
