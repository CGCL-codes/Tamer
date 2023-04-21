package db4oToOracle.obj1test;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.googlecode.datawander.connectors.Db4oUtil;
import com.googlecode.datawander.connectors.OracleConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author MarcinStachniuk
 */
public class AppUser {

    private String name;

    private String surname;

    private Login login;

    private List<PhotoGallery> photoGallerys;

    private Task[] tasks;

    private List<Animal> animals;

    public static void saveObjectsInDb4o(AppUser[] objectsToSave) {
        try {
            com.db4o.ObjectContainer db4o = Db4oUtil.getObjectContainer();
            for (final AppUser o : objectsToSave) {
                com.db4o.ObjectSet<AppUser> objectSet = db4o.query(new com.db4o.query.Predicate<AppUser>() {

                    @Override
                    public boolean match(AppUser obj) {
                        return o.equals(obj);
                    }
                });
                if (objectSet.size() == 0) {
                    o.updateTableObject(db4o);
                    db4o.store(o);
                    System.out.println("Zachowano: " + o.toString());
                } else {
                    System.out.println("Bylo juz w bazie: " + o.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTableObject(com.db4o.ObjectContainer db4o) {
        for (int i = 0; i < photoGallerys.size(); i++) {
            PhotoGallery obj = photoGallerys.get(i);
            com.db4o.ObjectSet<PhotoGallery> objectSet = db4o.queryByExample(obj);
            while (objectSet.hasNext()) {
                PhotoGallery temp = objectSet.next();
                if (temp.equals(obj)) {
                    photoGallerys.set(i, temp);
                    System.out.println("Update object: " + obj.toString());
                    break;
                }
            }
        }
    }

    public static void saveAllRecords(AppUser[] users) throws Exception {
        if (users != null) {
            Connection conn = OracleConnector.getConnection();
            for (AppUser o : users) {
                o.saveRecord(conn);
            }
        }
    }

    private void saveRecord(Connection conn) throws SQLException {
        String sql = "insert into \"AppUser\" (\"GENERATED_ID\", \"name\", \"surname\") " + "values (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setLong(1, getIdFromDb4o());
        stmt.setString(2, name);
        stmt.setString(3, surname);
        stmt.executeUpdate();
        System.out.println("Zapisano AppUser id: " + getIdFromDb4o() + " name: " + name + " surname: " + surname);
        saveAnimals(conn);
    }

    private void saveAnimals(Connection conn) throws SQLException {
        if (animals != null) {
            PreparedStatement stmt = conn.prepareStatement("insert into \"Appuser_Animal_Assoc\" " + "(\"AppUser_ID\", \"AnimalUser_ID\") values(?,?)");
            long id = getIdFromDb4o();
            stmt.setLong(1, id);
            for (Animal a : animals) {
                stmt.setLong(2, a.getIdFromDb4o());
                stmt.executeUpdate();
                System.out.println("Zachowano asocjacje " + id + " " + a.getIdFromDb4o());
            }
        }
    }

    public static ObjectSet<AppUser> getAppUserByTaskFromDb4o(final Task o) {
        ObjectContainer db = Db4oUtil.getObjectContainer();
        ObjectSet<AppUser> result = db.query(new Predicate<AppUser>() {

            @Override
            public boolean match(AppUser et) {
                if (et.tasks != null) {
                    return java.util.Arrays.asList(et.tasks).contains(o);
                }
                return false;
            }
        });
        return result;
    }

    public static ObjectSet<AppUser> getAppUserByPhotoGalleryFromDb4o(final PhotoGallery o) {
        ObjectContainer db = Db4oUtil.getObjectContainer();
        ObjectSet<AppUser> result = db.query(new Predicate<AppUser>() {

            @Override
            public boolean match(AppUser et) {
                if (et.photoGallerys != null) {
                    return et.photoGallerys.contains(o);
                }
                return false;
            }
        });
        return result;
    }

    public static ObjectSet<AppUser> getAppUserByLoginFromDb4o(final Login o) {
        ObjectContainer db = Db4oUtil.getObjectContainer();
        ObjectSet<AppUser> result = db.query(new Predicate<AppUser>() {

            @Override
            public boolean match(AppUser et) {
                if (et.login != null) {
                    return et.login.equals(o);
                }
                return false;
            }
        });
        return result;
    }

    public List<Animal> getAnimals() {
        return animals;
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhotoGallery> getPhotoGallerys() {
        return photoGallerys;
    }

    public void setPhotoGallerys(List<PhotoGallery> photoGallerys) {
        this.photoGallerys = photoGallerys;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Task[] getTasks() {
        return tasks;
    }

    public void setTasks(Task[] tasks) {
        this.tasks = tasks;
    }

    /**
     * Pobiera id z db4o. Zakładam ze wynik bedzie inny niz zero, w przeciwnym 
     * wypadku bedzie to obiekt ktorego nie bylo w db4o
     * @return
     */
    public long getIdFromDb4o() {
        ObjectContainer db = Db4oUtil.getObjectContainer();
        long id = db.ext().getID(this);
        return id;
    }
}
