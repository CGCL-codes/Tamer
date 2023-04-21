package samplepluginproject.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import samplepluginproject.editors.internal.StudentsEditorTableFactory;
import samplepluginproject.model.Student;
import samplepluginproject.model.Team;

public class StudentsEditor extends EditorPart {

    private class IBaseLabelProviderImplementation extends BaseLabelProvider implements ITableLabelProvider {

        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }

        public String getColumnText(Object element, int columnIndex) {
            String result = null;
            if (element instanceof Student) {
                Student student = (Student) element;
                switch(columnIndex) {
                    case 0:
                        result = Integer.toString(student.getId());
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    default:
                        result = "error";
                        break;
                }
            }
            return result;
        }
    }

    private final class IStructuredContentProviderImplementation implements IStructuredContentProvider {

        public Object[] getElements(Object inputElement) {
            Object[] result = null;
            if (inputElement instanceof Team) {
                Team team = (Team) inputElement;
                result = team.getStudents().toArray();
            }
            return result;
        }

        public void dispose() {
        }

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }

    public static final String ID = "samplepluginproject.editor.StudentsEditor";

    private Team data;

    private Table table;

    private TableViewer tableViewer;

    @Override
    public void doSave(IProgressMonitor monitor) {
    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        setSite(site);
        setInput(input);
        data = new Team();
        createSampleData();
    }

    private void createSampleData() {
        data.createStudent(1);
        data.createStudent(2);
        data.createStudent(3);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        table = StudentsEditorTableFactory.getTable(parent);
        StudentsEditorTableFactory.configureTable(table);
        tableViewer = new TableViewer(table);
        attachProviders();
        tableViewer.setInput(data);
    }

    private void attachProviders() {
        tableViewer.setContentProvider(new IStructuredContentProviderImplementation());
        tableViewer.setLabelProvider(new IBaseLabelProviderImplementation());
    }

    @Override
    public void setFocus() {
    }
}
