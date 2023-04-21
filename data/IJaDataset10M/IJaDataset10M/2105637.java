package com.fh.auge.ui.sections;

import java.util.List;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public interface IViewSectionFactory {

    public List<IViewSection> createSections(Composite parent, FormToolkit toolkit);
}
