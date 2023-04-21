package kursova;

import java.util.HashMap;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import database.*;

/**
 * The main class of the application.
 */
public class KursovaApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        show(new KursovaView(this));
    }

    public static String UserName = "a";

    public static String UserPassword = "p";

    public static boolean isAdmin = false;

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * Обект от клас DataBase за използване в заявките
     */
    protected static DataBase database = null;

    /**
     * A convenient static getter for the application instance.
     * @return the instance of KursovaApp
     */
    public static KursovaApp getApplication() {
        return Application.getInstance(KursovaApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(KursovaApp.class, args);
    }

    public static Select getFullProjectSelect() {
        Select select = new Select("cars");
        select.joinLeft("colors", "car_clr_id", "clr_id");
        select.joinLeft("models", "car_mdl_id", "mdl_id");
        select.joinLeft("types", "car_type_id", "type_id");
        select.joinLeft("brands", "mdl_brd_id", "brd_id");
        return select;
    }

    protected static void initDataBase() {
        KursovaApp.database = new DataBase();
        KursovaApp.database.setDatabaseName("javakr");
        KursovaApp.database.setUserName("javakr");
        KursovaApp.database.setUserPassword("pass123");
        DataBase._isAdmin = false;
    }

    public static DataBase getDatabase() {
        if (KursovaApp.database == null) {
            KursovaApp.initDataBase();
        }
        return KursovaApp.database;
    }

    public static void testSql() {
        DataBase db = KursovaApp.getDatabase();
        try {
            Table table1 = new Table("typess", db);
            Select select2 = KursovaApp.getFullProjectSelect();
            HashMap[] all = table1.fetchAll(select2);
            for (HashMap row : all) {
                for (Object value : row.keySet()) {
                    System.out.println(value + " : " + row.get(value));
                }
            }
        } catch (Database_SQL_Exception e) {
            System.err.println("sql error");
            System.err.println(e.toString());
            System.exit(0);
        } catch (Database_Connection_Exception e) {
            System.err.println("sql connection error");
            System.err.println(e.toString());
            System.exit(0);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            System.err.println(e.getCause());
            System.err.println(e.getStackTrace());
            System.err.println(e.getClass());
            System.exit(0);
        }
    }

    public ItemsDialog itemsGrid = null;

    public AdminDialog adminGrid = null;
}
