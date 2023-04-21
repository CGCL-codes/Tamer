package frostcode.icetasks.gui.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComboBox;
import com.google.inject.Inject;
import frostcode.icetasks.data.Area;
import frostcode.icetasks.data.DataManager;
import frostcode.icetasks.data.project.Project;
import frostcode.icetasks.util.ObservableList.ListListener;

public class ProjectComboBox extends JComboBox {

    private static final long serialVersionUID = 1L;

    private final DataManager dm;

    @Inject
    public ProjectComboBox(final DataManager dm) {
        super();
        this.dm = dm;
        rebuild();
        dm.getProjects().addListener(new ListListener<Project>() {

            @Override
            public void onAdd(int startIndex, int endIndex) {
                rebuild();
            }

            @Override
            public void onChange(int index, Project element) {
                rebuild();
            }

            @Override
            public void onClear() {
                removeAllItems();
            }

            @Override
            public void onRemove(int startIndex, int endIndex, List<Project> elements) {
                rebuild();
            }
        });
        dm.addObserver(new DataManager.DataListener() {

            @Override
            public void onAreaChange(final Area area) {
                rebuild();
            }
        });
    }

    private void rebuild() {
        removeAllItems();
        addItem(null);
        List<Project> projects = new ArrayList<Project>(dm.getProjects());
        Collections.sort(projects);
        for (Project project : projects) {
            if (project.area == dm.getArea()) addItem(project);
        }
    }
}
