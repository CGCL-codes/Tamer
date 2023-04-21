package org.remus.infomngmnt.common.ui.swt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.remus.infomngmnt.common.core.util.StringUtils;
import org.remus.infomngmnt.common.ui.image.ResourceManager;
import org.remus.infomngmt.common.ui.uimodel.provider.UimodelEditPlugin;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class TimeCombo extends Composite {

    private final CDateTime date;

    public final CDateTime getDate() {
        return this.date;
    }

    private final Button pickButton;

    public TimeCombo(final Composite parent, final int style) {
        super(parent, style);
        GridLayout gridLayout = new GridLayout(2, false);
        gridLayout.horizontalSpacing = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        this.setLayout(gridLayout);
        this.date = new CDateTime(this, CDT.TIME_MEDIUM | CDT.DATE_MEDIUM);
        GridData dateTextGridData = new GridData(SWT.FILL, SWT.FILL, false, false);
        dateTextGridData.grabExcessHorizontalSpace = true;
        dateTextGridData.widthHint = SWT.FILL;
        dateTextGridData.verticalIndent = 0;
        this.date.setLayoutData(dateTextGridData);
        this.pickButton = new Button(this, style | SWT.ARROW | SWT.DOWN);
        GridData pickButtonGridData = new GridData(SWT.RIGHT, SWT.FILL, false, true);
        pickButtonGridData.verticalIndent = 0;
        this.pickButton.setLayoutData(pickButtonGridData);
        this.pickButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(final SelectionEvent arg0) {
                MenuManager menuManager = new MenuManager();
                Menu menu = menuManager.createContextMenu(TimeCombo.this.pickButton);
                createMenu(menuManager);
                TimeCombo.this.pickButton.setMenu(menu);
                menu.setVisible(true);
            }
        });
        pack();
    }

    protected void createMenu(final MenuManager menuManager) {
        for (int i = 0, n = 7; i < n; i++) {
            menuManager.add(new DateSetAction(i));
        }
        menuManager.add(new Separator());
        menuManager.add(new Action("Choose...") {

            @Override
            public void run() {
                TimePickerDialog datePickerDialog = new TimePickerDialog(getShell(), TimeCombo.this.date.getSelection());
                if (datePickerDialog.open() == IDialogConstants.OK_ID) {
                    TimeCombo.this.date.setSelection(datePickerDialog.getSelectedDate());
                }
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return ResourceManager.getPluginImageDescriptor(UimodelEditPlugin.getPlugin(), "icons/iconexperience/calendar.png");
            }
        });
        menuManager.add(new Action("None", IAction.AS_CHECK_BOX) {

            @Override
            public void run() {
                TimeCombo.this.date.setSelection(null);
            }

            @Override
            public boolean isChecked() {
                return TimeCombo.this.date.getSelection() == null;
            }
        });
    }

    private String getDayString(final int offset) {
        Calendar instance = Calendar.getInstance();
        if (offset > 0) {
            instance.add(Calendar.DAY_OF_YEAR, offset);
        }
        String format = new SimpleDateFormat("EEEE").format(instance.getTime());
        if (offset == 0) {
            format = StringUtils.join(format, " - Today");
        }
        return format;
    }

    private Date getDate(final int offset) {
        Calendar instance = Calendar.getInstance();
        if (offset > 0) {
            instance.add(Calendar.DAY_OF_YEAR, offset);
        }
        return instance.getTime();
    }

    protected String convertDate() {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT).format(this.date);
    }

    @Override
    public void setBackground(final Color backgroundColor) {
        this.date.setBackground(backgroundColor);
        this.pickButton.setBackground(backgroundColor);
        super.setBackground(backgroundColor);
    }

    private class DateSetAction extends Action {

        private final int offset;

        public DateSetAction(final int offset) {
            this.offset = offset;
        }

        @Override
        public String getText() {
            return getDayString(this.offset);
        }

        @Override
        public void run() {
            TimeCombo.this.date.setSelection(getDate(this.offset));
        }
    }
}
