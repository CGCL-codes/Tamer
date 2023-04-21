package gdata;

import com.google.inject.ImplementedBy;
import com.google.gdata.util.ServiceException;
import java.io.IOException;

/**
 * Interface description...
 *
 * @author mvu
 */
@ImplementedBy(NotebookImpl.class)
public interface Notebook {

    void syncNotes() throws IOException, ServiceException;
}
