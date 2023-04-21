package org.butu.frame.references.gui;

import org.butu.core.entity.IEntity;
import org.butu.frame.references.spi.IRefRow;
import org.butu.gui.widgets.table.TableActionListener;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * ������� �����, ����������� �������� ����� ������ �� ������ �� ��������� ������, ������������ AListArea
 * (�.�. ������� ������). ��������� ������ � ������ ������� ���������� ������ AListForm. �� ������ �����
 * ��������������� ������������ � �������� �������� ��� ��������� ����������� AListArea. 
 * @author kbakaras
 *
 */
public class BListForm<E extends IEntity, R extends IRefRow<E>> extends ListForm<E, R> {

    private Class<? extends AListArea<E, R>> listAreaClass;

    private Integer listStyle;

    public BListForm(Shell shell, int listStyle, Class<? extends AListArea<E, R>> listAreaClass) {
        super(shell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        this.listAreaClass = listAreaClass;
        this.listStyle = listStyle;
    }

    public BListForm(Shell shell, Class<? extends AListArea<E, R>> listAreaClass) {
        super(shell);
        setShellStyle(getShellStyle() | SWT.RESIZE);
        this.listAreaClass = listAreaClass;
    }

    protected AListArea<E, R> doCreateDialogArea(Composite parent, E initial) {
        try {
            AListArea<E, R> listArea;
            if (listStyle != null) {
                listArea = listAreaClass.getConstructor(Composite.class, int.class).newInstance(parent, listStyle);
            } else {
                listArea = listAreaClass.getConstructor(Composite.class).newInstance(parent);
            }
            GridDataFactory.fillDefaults().grab(true, true).applyTo(listArea);
            listArea.setDialogSettings(getDialogBoundsSettings());
            listArea.setPapro(getPapro());
            listArea.setLookupClass(getLookupClass());
            listArea.getTf().getViewer().addActionListener(new TableActionListener() {

                public void action(TableActionEvent event) {
                    event.doIt = false;
                    okPressed();
                }
            });
            listArea.init(initial);
            return listArea;
        } catch (Exception e) {
            throw new ListFormCreationException(e);
        }
    }

    protected IDialogSettings getDialogBoundsSettings() {
        IDialogSettings settings = getDialogSettings();
        if (settings != null) {
            IDialogSettings localSettings = settings.getSection(listAreaClass.getSimpleName());
            if (localSettings == null) localSettings = settings.addNewSection(listAreaClass.getSimpleName());
            return localSettings;
        } else {
            return null;
        }
    }
}
