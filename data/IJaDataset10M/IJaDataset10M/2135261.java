package br.ufal.bibliweb;

import java.util.Vector;
import java.util.Hashtable;
import java.sql.SQLException;
import br.ufal.bibliweb.exception.ResourceNotFoundException;

/**
 * Group.java
 *
 * @author Marcello de Sales
 */
public class Group {

    /** C�digo para identificar o grupo de estudantes universit�rios: "S" */
    public static final String STUDENT_CODE = "S";

    /** C�digo para identificar o grupo de professores universit�rios: "P" */
    public static final String PROFESSOR_CODE = "P";

    /** C�digo para identificar o grupo de balconistas: "C" */
    public static final String CLERK_CODE = "C";

    /** C�digo para identificar o grupo de administradores: "A" */
    public static final String ADMIN_CODE = "A";

    /** C�digo para identificar o grupo de distribuidores: "D" */
    public static final String DISTR_CODE = "D";

    /** A identifica��o do grupo. */
    private String ID;

    /** Descri��o do grupo. */
    private String description;

    /** C�digo do grupo. */
    private String code;

    /** Resultado das buscas no banco de dados. */
    private Vector result;

    /** Conex�o com o banco de dados. */
    private DatabaseLayer database;

    public Group(String ID, DatabaseLayer database) throws ResourceNotFoundException {
        this.result = database.query("SELECT * FROM grouping WHERE group_id='" + ID + "'");
        if (this.result.size() != 1) {
            throw new ResourceNotFoundException("Grupo n�o encontrado com ID=" + ID + " !");
        } else {
            this.database = database;
            Hashtable physicalPlaceStatus = (Hashtable) result.firstElement();
            this.ID = ID;
            this.description = (String) physicalPlaceStatus.get("description");
            this.code = (String) physicalPlaceStatus.get("code");
        }
    }

    /** Retorna o identificador do Grupo. */
    public String getID() {
        return this.ID;
    }

    /** Retorna a descri��o do Grupo. */
    public String getDescription() {
        return this.description;
    }

    /** Retorna o c�digo do grupo. */
    public String getCode() {
        return this.code;
    }

    public void printAll() {
        System.out.println("############  Informacoes Grupos  %%%%%%%%%%");
        System.out.println("ID:" + this.getID());
        System.out.println("Descricao: " + this.getDescription());
        System.out.println("Code: " + this.getCode());
    }

    /** Cria um novo grupo. */
    public static String createNewGroup(String description, String code, DatabaseLayer database) {
        String newGroupID = null;
        try {
            newGroupID = Utility.getNewID(database);
            database.update("INSERT INTO grouping (group_id,description,code) VALUES ('" + newGroupID + "','" + description + "','" + code + "')");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return newGroupID;
    }

    /** Verifica se um grupo existe a partir da identifica��o groupID. */
    public static boolean exists(String groupID, DatabaseLayer database) {
        Vector thisResult = database.query("SELECT group_id FROM grouping WHERE group_id='" + groupID + "'");
        return (thisResult.size() > 0);
    }

    public static void main(String[] args) {
        try {
            DatabaseLayer db = new DatabaseLayer();
            Group.createNewGroup("Alunos", Group.STUDENT_CODE, db);
            Group.createNewGroup("Professores", Group.PROFESSOR_CODE, db);
            Group.createNewGroup("Balconistas", Group.CLERK_CODE, db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
