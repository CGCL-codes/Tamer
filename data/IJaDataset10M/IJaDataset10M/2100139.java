package disk.components;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.chenillekit.tapestry.core.components.InPlaceEditor;
import disk.data.FileDAO;
import disk.entities.File;

public class FileGrid {

    private File file;

    @Parameter(required = true)
    private GridDataSource files;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public GridDataSource getFiles() {
        return files;
    }

    public void setFiles(GridDataSource files) {
        this.files = files;
    }

    @SuppressWarnings("unused")
    @Component
    private InPlaceEditor inPlaceEditor;

    @Inject
    private FileDAO filesDAO;

    @OnEvent(component = "inPlaceEditor", value = InPlaceEditor.SAVE_EVENT)
    void actionFromEditor(Long fileId, String info) {
        File file = filesDAO.find(fileId);
        file.setInfo(info);
        filesDAO.update(file);
    }
}
