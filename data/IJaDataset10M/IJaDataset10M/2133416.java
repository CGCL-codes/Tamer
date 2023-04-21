package de.beas.explicanto.client.sec.properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.sec.jaxb.TimeOfDay;
import de.beas.explicanto.client.sec.model.ScreenplayEditorInput;
import de.beas.explicanto.client.sec.model.ScreenplayModel;
import de.beas.explicanto.client.sec.views.model.TimeOfDayConsts;

/**
 * TimeOfDayPS
 * 
 * @author Alexandru.Gyulai
 * @version 1.0
 * 
 */
public class TimeOfDayPS implements IPropertySource2 {

    private TimeOfDay timeOfday;

    private ScreenplayModel model;

    IPropertyDescriptor[] descriptors;

    private static Log log = LogFactory.getLog(LocationPS.class);

    public TimeOfDayPS(TimeOfDay timeOfday) {
        this.timeOfday = timeOfday;
        init();
    }

    public void init() {
        model = getModel();
    }

    public boolean isPropertyResettable(Object id) {
        return false;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public Object getEditableValue() {
        return null;
    }

    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (model == null) {
            log.error("model == null");
            return new IPropertyDescriptor[0];
        }
        if (descriptors != null) return descriptors;
        descriptors = new IPropertyDescriptor[2];
        descriptors[0] = new TextPropertyDescriptor(TimeOfDayConsts.NAME_FIELD_ID, TimeOfDayConsts.NAME_FIELD);
        ((TextPropertyDescriptor) descriptors[0]).setValidator(new ICellEditorValidator() {

            public String isValid(Object value) {
                if (((String) value).trim().length() == 0) return I18N.translate("sec.dialog.timeOfDay.emptyName");
                return null;
            }
        });
        descriptors[1] = new TextPropertyDescriptor(TimeOfDayConsts.INFO_FIELD_ID, TimeOfDayConsts.INFO_FIELD);
        return descriptors;
    }

    public Object getPropertyValue(Object id) {
        if (id.equals(TimeOfDayConsts.NAME_FIELD_ID)) return timeOfday.getName(); else if (id.equals(TimeOfDayConsts.INFO_FIELD_ID)) return timeOfday.getInformation();
        return "no property";
    }

    public void resetPropertyValue(Object id) {
    }

    public void setPropertyValue(Object id, Object value) {
        if (model == null) {
            log.error("model == null");
            return;
        }
        if (id.equals(TimeOfDayConsts.NAME_FIELD_ID)) model.updateTimeOfDay(timeOfday.getId(), (String) value, timeOfday.getInformation()); else if (id.equals(TimeOfDayConsts.INFO_FIELD_ID)) model.updateTimeOfDay(timeOfday.getId(), timeOfday.getName(), (String) value);
    }

    protected ScreenplayModel getModel() {
        IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (activeEditor == null) return null;
        IEditorInput input = activeEditor.getEditorInput();
        if (!(input instanceof ScreenplayEditorInput)) return null;
        return (ScreenplayModel) ((ScreenplayEditorInput) input).getAdapter(ScreenplayModel.class);
    }
}
