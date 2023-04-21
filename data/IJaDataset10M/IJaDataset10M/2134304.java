package net.sf.grudi.ui.editors.configuration;

import net.sf.grudi.model.search.SearchVO;
import net.sf.grudi.model.vo.VO;
import net.sf.grudi.persistence.dao.AbstractDAO;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public abstract class PlanConfiguration<T extends VO, S extends SearchVO> extends EditorConfiguration {

    public abstract void createPlanArea(Composite parent, FormToolkit toolkit);

    public abstract void bind(DataBindingContext context);

    public abstract AbstractDAO<T, S> getDAO();

    public void createButtons(ScrolledForm form) {
    }
}
