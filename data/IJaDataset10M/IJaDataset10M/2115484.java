package jp.ekasi.pms.ui.grid;

import jp.ekasi.pms.model.Project;
import org.eclipse.nebula.jface.gridviewer.GridTreeViewer;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.swt.widgets.Composite;

/**
 * �v���W�F�N�g��Grid��ɕ\������Viewer
 * @author sumari
 */
public class ProjectGridViewer extends GridTreeViewer {

    /**
	 * �R���X�g���N�^.<br>
	 * @param parent
	 */
    public ProjectGridViewer(Composite parent) {
        super(parent);
    }

    /**
	 * �R���X�g���N�^.<br>
	 * @param parent
	 * @param style
	 */
    public ProjectGridViewer(Composite parent, int style) {
        super(parent, style);
    }

    /**
	 * �R���X�g���N�^.<br>
	 * @param grid
	 */
    public ProjectGridViewer(Grid grid) {
        super(grid);
    }

    /**
	 * @param project Project���Z�b�g����B
	 */
    public void setProject(Project project) {
        this.setInput(project);
    }

    /**
	 * @return Project ��Ԃ��B
	 */
    public Project getProject() {
        return (Project) this.getInput();
    }
}
