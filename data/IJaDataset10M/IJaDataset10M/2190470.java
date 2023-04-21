package ch.gmtech.lab.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import ch.gmtech.lab.CoursesPersistence;
import ch.gmtech.lab.datasource.db.ConnectionBuilder;
import ch.gmtech.lab.datasource.db.Database;
import ch.gmtech.lab.datasource.db.RelationalDb;
import ch.gmtech.lab.gui.Action;
import ch.gmtech.lab.gui.ActionHandler;
import ch.gmtech.lab.gui.ApplicationMenu;
import ch.gmtech.lab.gui.CreateCourse;
import ch.gmtech.lab.gui.CreditsCourseCreator;
import ch.gmtech.lab.gui.Display;
import ch.gmtech.lab.gui.ErrorAction;
import ch.gmtech.lab.gui.Exit;
import ch.gmtech.lab.gui.NameCourseCreator;
import ch.gmtech.lab.gui.Navigator;
import ch.gmtech.lab.gui.ShowAllCourses;
import ch.gmtech.lab.gui.UpdateCourse;
import ch.gmtech.lab.gui.UserChoice;
import ch.gmtech.lab.gui.UserMessage;
import ch.gmtech.lab.io.BufferedLineReader;
import ch.gmtech.lab.io.CoursePrinterFactory;
import ch.gmtech.lab.io.CoursesPrinter;
import ch.gmtech.lab.io.LineReader;
import ch.gmtech.lab.io.MultipleLineLayout;
import ch.gmtech.lab.io.StreamPrinter;
import ch.gmtech.lab.io.SystemLineSeparator;

public class H2Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        LineReader reader = new BufferedLineReader(new BufferedReader(new InputStreamReader(System.in)));
        Display console = new Display(reader, new StreamPrinter(System.out));
        ApplicationMenu mainMenu = new ApplicationMenu("0-Esci; 1-Crea corso; 2-Aggiorna Corso; 3-Elenca Corsi");
        Database.reggieOnH2(ConnectionBuilder.h2OnReggie()).create();
        CoursesPersistence system = new RelationalDb(ConnectionBuilder.h2OnReggie());
        UserChoice nameChoice = new UserChoice(console, new UserMessage("Inserire il nome: "));
        UserChoice creditsChoice = new UserChoice(console, new UserMessage("Inserire il numero di crediti: "));
        CoursesPrinter multipleLine = new MultipleLineLayout(new CoursePrinterFactory(), new StringBuffer(), new SystemLineSeparator());
        Action error = new ErrorAction(console);
        ActionHandler handler = new ActionHandler(error);
        handler.add("0", Exit.getInstance(console));
        handler.add("1", new CreateCourse(new NameCourseCreator(nameChoice), new CreditsCourseCreator(creditsChoice), system, console));
        handler.add("2", new UpdateCourse(new NameCourseCreator(nameChoice), new CreditsCourseCreator(creditsChoice), system, console));
        handler.add("3", new ShowAllCourses(multipleLine, system, console));
        new Navigator(console, mainMenu, handler).start();
    }
}
