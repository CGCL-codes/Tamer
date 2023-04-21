package br.ufal.graw;

import java.util.Vector;
import java.util.Hashtable;

/**
 * UseCase.java for the main screen
 *
 * @author Marcello de Sales.
 */
public class UseCase {

    protected DatabaseLayer database;

    protected Vector result;

    private static UseCase useCase;

    private UseCase() {
    }

    public static UseCase getInstance(DatabaseLayer database) {
        if (useCase == null) {
            useCase = new UseCase();
            useCase.setDatabase(database);
        }
        return useCase;
    }

    public void setDatabase(DatabaseLayer database) {
        this.database = database;
    }

    public Community[] getDisciplines() {
        this.result = this.database.query("SELECT communityID FROM discipline ORDER BY communityID DESC");
        Community[] communities = new Community[this.result.size()];
        try {
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    String commID = (String) ((Hashtable) this.result.get(i)).get("communityID");
                    communities[i] = AbstractCommunity.getRealCommunity(commID, this.database);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return communities;
    }

    public Community[] getCourses() {
        this.result = this.database.query("SELECT communityID FROM community WHERE status='A' AND kind='E' ORDER BY communityID DESC");
        Community[] communities = new Community[this.result.size()];
        try {
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    String commID = (String) ((Hashtable) this.result.get(i)).get("communityID");
                    communities[i] = AbstractCommunity.getRealCommunity(commID, this.database);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return communities;
    }

    public Community[] getGroups() {
        this.result = this.database.query("SELECT communityID FROM community WHERE status='A' AND kind='G' ORDER BY communityID DESC");
        Community[] communities = new Community[this.result.size()];
        try {
            if (result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    String commID = (String) ((Hashtable) this.result.get(i)).get("communityID");
                    communities[i] = AbstractCommunity.getRealCommunity(commID, this.database);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return communities;
    }

    public void drawMovies() {
        try {
            Community[] comm = useCase.getDisciplines();
            System.out.println("Disciplinas:<BR>");
            for (int i = 0; i < comm.length; i++) {
                Community community = comm[i];
                System.out.println("<BR>T�tulo: " + community.getTitle());
                System.out.println("<BR>Respons�vel: " + community.getResponsible().getName());
                System.out.println("<BR>Institui��o: " + new Institute(((Discipline) community).getInstituteID(), database).getName());
                System.out.println("<BR>Departamento: " + new Department(((Discipline) community).getDepartmentID(), database).getName());
                System.out.println("<BR>Usu�rios: " + community.getQuantUsers());
                System.out.println("<BR>Mensagens no Forum: " + community.getQuantMessages());
                System.out.println("<BR>Documentos: " + community.getQuantDocuments());
                System.out.println("<BR>Links: " + community.getQuantLinks());
            }
            Community[] groups = useCase.getGroups();
            System.out.println("Grupos de Pesqiosa: <BR>");
            for (int i = 0; i < groups.length; i++) {
                Community community = groups[i];
                System.out.println("<BR>T�tulo: " + community.getTitle());
                System.out.println("<BR>Respons�vel: " + community.getResponsible().getName());
                System.out.println("<BR>Usu�rios: " + community.getQuantUsers());
                System.out.println("<BR>Mensagens no Forum: " + community.getQuantMessages());
                System.out.println("<BR>Documentos: " + community.getQuantDocuments());
                System.out.println("<BR>Links: " + community.getQuantLinks());
            }
            Community[] courses = useCase.getCourses();
            System.out.println("Sou cursos:<BR>");
            for (int i = 0; i < courses.length; i++) {
                Community community = groups[i];
                System.out.println("<BR>T�tulo: " + community.getTitle());
                System.out.println("<BR>Respons�vel: " + community.getResponsible().getName());
                System.out.println("<BR>Usu�rios: " + community.getQuantUsers());
                System.out.println("<BR>Mensagens no Forum: " + community.getQuantMessages());
                System.out.println("<BR>Documentos: " + community.getQuantDocuments());
                System.out.println("<BR>Links: " + community.getQuantLinks());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 *
	 */
    public static void main(String[] args) {
        try {
            DatabaseLayer db = new DatabaseLayer();
            UseCase.getInstance(db).drawMovies();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
