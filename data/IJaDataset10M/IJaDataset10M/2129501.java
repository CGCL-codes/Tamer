package bugcrawler.runtime.projectwizard.projecttable;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import bugcrawler.runtime.data.Project;

/**
 * LabelProvider to define the appearance of the ProjectTableViewer
 * 
 * @author TSO
 */
public class ProjectTableLabelProvider extends LabelProvider implements ITableLabelProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    /**
	 * The LabelProvider returns values for the following columnIndex 0: if the
	 * project was choosen 1: the name of the project 2: the project owner 3:
	 * the creationdate of the project
	 */
    public String getColumnText(Object element, int columnIndex) {
        Project project = (Project) element;
        switch(columnIndex) {
            case 0:
                return "checked";
            case 1:
                return project.getName();
            case 2:
                return project.getOwner();
            case 3:
                return convertDate(project.getCreated());
            default:
                return null;
        }
    }

    /**
	 * Converts a given Daten with SimpleDateFormat
	 * 
	 * @param date
	 *            Which has to be formated
	 * @return String The given Date as String formated with SimpleDateFormat as
	 *         MM/dd/yyyy
	 */
    private String convertDate(Date date) {
        return new SimpleDateFormat("MM/dd/yyyy").format(date);
    }

    public void dispose() {
    }
}
