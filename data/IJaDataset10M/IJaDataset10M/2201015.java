package de.beas.explicanto.client.rcp.workspace.views;

import java.util.Iterator;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import de.bea.services.vidya.client.datasource.types.WSAnnotation;
import de.bea.services.vidya.client.datasource.types.WSAnnotationAudience;

public class AnnotationFilter extends ViewerFilter {

    public static final Logger log = Logger.getLogger(AnnotationFilter.class);

    public static final int CONTENT = 0;

    public static final int AUTHOR = 1;

    public static final int AUDIENCE = 2;

    public static final int STATUS = 3;

    public static final int DATE = 4;

    private final int type;

    private final String searchString;

    public AnnotationFilter(int type, String string) {
        this.type = type;
        searchString = string;
    }

    public boolean select(Viewer viewer, Object parentElement, Object element) {
        WSAnnotation annotation = (WSAnnotation) element;
        switch(type) {
            case CONTENT:
                if (annotation.getContent().indexOf(searchString) != -1) return true;
                break;
            case AUTHOR:
                if (annotation.getAuthor().getUsername().indexOf(searchString) != -1) return true;
                break;
            case AUDIENCE:
                StringBuffer buf = new StringBuffer();
                Iterator it = annotation.getAudience().iterator();
                while (it.hasNext()) {
                    WSAnnotationAudience ta = (WSAnnotationAudience) it.next();
                    buf.append(ta.getUser().getUsername()).append(" ");
                }
                if (buf.toString().indexOf(searchString) != -1) return true;
                break;
            case STATUS:
                if (annotation.getStatus().getValue().indexOf(searchString) != -1) return true;
                break;
            case DATE:
                return true;
            default:
                return true;
        }
        return false;
    }
}
