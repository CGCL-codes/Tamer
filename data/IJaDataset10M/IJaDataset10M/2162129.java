package research.ui.editors;

import java.util.Calendar;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import research.domain.Calander;
import research.domain.Preparation;
import research.domain.Production;
import research.domain.Research;
import research.entity.EntityType;
import research.model.EntityListContentProvider;
import research.model.ListHierarchy;
import research.model.ResearchCalanderHierarchy;
import research.persistence.PersistenceManager;
import research.ui.editors.input.EntityEditorInput;

public class ProductionEditor extends FormEntityEditor {

    public static String ID = "research.ui.editors.ProductionEditor";

    private Text name;

    private Text description;

    private Text comment;

    private DateTime start;

    private DateTime stop;

    private DateTime startTime;

    private DateTime stopTime;

    private ComboViewer research;

    private ComboViewer calander;

    private ComboViewer preparation;

    private Production getEntity() {
        return (Production) entity;
    }

    @Override
    protected boolean checkInputEntityType(EntityEditorInput input) {
        return input.getEntity().getType().equals(EntityType.Production);
    }

    @Override
    protected void fill() {
        this.name.setText(this.getEntity().getName());
        this.comment.setText(this.getEntity().getComment());
        this.description.setText(this.getEntity().getDescription());
        Calendar newdate = Calendar.getInstance();
        newdate.setTime(this.getEntity().getStart());
        start.setYear(newdate.get(Calendar.YEAR));
        start.setMonth(newdate.get(Calendar.MONTH));
        start.setDay(newdate.get(Calendar.DAY_OF_MONTH));
        startTime.setHours(newdate.get(Calendar.HOUR_OF_DAY));
        startTime.setMinutes(newdate.get(Calendar.MINUTE));
        startTime.setSeconds(newdate.get(Calendar.SECOND));
        newdate = Calendar.getInstance();
        newdate.setTime(this.getEntity().getStop());
        stop.setYear(newdate.get(Calendar.YEAR));
        stop.setMonth(newdate.get(Calendar.MONTH));
        stop.setDay(newdate.get(Calendar.DAY_OF_MONTH));
        stopTime.setHours(newdate.get(Calendar.HOUR_OF_DAY));
        stopTime.setMinutes(newdate.get(Calendar.MINUTE));
        stopTime.setSeconds(newdate.get(Calendar.SECOND));
        setComboEntity(research, this.getEntity().getResearch());
        setComboEntity(calander, this.getEntity().getCalander());
        setComboEntity(preparation, this.getEntity().getPreparation());
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected boolean readAndValidate() {
        this.getEntity().setName(this.name.getText());
        this.getEntity().setDescription(this.description.getText());
        this.getEntity().setComment(this.comment.getText());
        Calendar date = Calendar.getInstance();
        date.set(this.start.getYear(), this.start.getMonth(), this.start.getDay(), this.startTime.getHours(), this.startTime.getMinutes(), this.startTime.getSeconds());
        this.getEntity().setStart(date.getTime());
        date.set(this.stop.getYear(), this.stop.getMonth(), this.stop.getDay(), this.stopTime.getHours(), this.stopTime.getMinutes(), this.stopTime.getSeconds());
        this.getEntity().setStop(date.getTime());
        Research res = (Research) getComboEntity(research);
        Calander cal = (Calander) getComboEntity(calander);
        Preparation prep = (Preparation) getComboEntity(preparation);
        if (res == null) {
            MessageDialog.openWarning(getSite().getShell(), "Error", "Выберите исследование");
            return false;
        }
        if (cal == null) {
            MessageDialog.openWarning(getSite().getShell(), "Error", "Выберите каландровую линию");
            return false;
        }
        this.getEntity().setResearch(res);
        this.getEntity().setCalander(cal);
        this.getEntity().setPreparation(prep);
        return validateText(name);
    }

    @Override
    protected void renderForm() {
        TableWrapData td;
        Label label;
        Section section;
        Composite composite;
        GridData gd;
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        td = new TableWrapData(TableWrapData.FILL);
        section.setLayoutData(td);
        section.setText("Происхождение:");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(2, false));
        section.setClient(composite);
        gd = new GridData();
        label = toolkit.createLabel(composite, "Исследование*:");
        label.setLayoutData(gd);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        research = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        research.setContentProvider(new EntityListContentProvider(new ListHierarchy(EntityType.Research), false));
        research.setLabelProvider(new LabelProvider());
        research.setInput(PersistenceManager.getInstance());
        gd.widthHint = 100;
        research.getCombo().setLayoutData(gd);
        research.getCombo().addModifyListener(getTextModifyListener());
        research.getCombo().addModifyListener(new ResearchModifyListener());
        gd = new GridData();
        label = toolkit.createLabel(composite, "Каландровая линия*:");
        label.setLayoutData(gd);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        calander = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        calander.setContentProvider(new EntityListContentProvider(new ListHierarchy(EntityType.Calander), false));
        calander.setLabelProvider(new LabelProvider());
        calander.setInput(PersistenceManager.getInstance());
        gd.widthHint = 100;
        calander.getCombo().setLayoutData(gd);
        calander.getCombo().addModifyListener(getTextModifyListener());
        gd = new GridData();
        label = toolkit.createLabel(composite, "Описание приготовления:");
        label.setLayoutData(gd);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        preparation = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        preparation.setContentProvider(new EntityListContentProvider(new ListHierarchy(EntityType.Preparation), true));
        preparation.setLabelProvider(new LabelProvider());
        preparation.setInput(PersistenceManager.getInstance());
        gd.widthHint = 100;
        preparation.getCombo().setLayoutData(gd);
        preparation.getCombo().addModifyListener(getTextModifyListener());
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        section.setLayoutData(td);
        section.setText("Название*");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(1, false));
        section.setClient(composite);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        name = toolkit.createText(composite, "", SWT.BORDER);
        gd.widthHint = 100;
        name.setLayoutData(gd);
        name.addModifyListener(getNameModifyListener());
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR);
        td = new TableWrapData(TableWrapData.FILL);
        section.setLayoutData(td);
        section.setText("Дата и время");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(3, false));
        section.setClient(composite);
        gd = new GridData();
        label = toolkit.createLabel(composite, "Время запуска:");
        label.setLayoutData(gd);
        gd = new GridData();
        start = new DateTime(composite, SWT.DATE);
        start.setLayoutData(gd);
        start.addSelectionListener(getDateSelectionListener());
        gd = new GridData();
        startTime = new DateTime(composite, SWT.TIME);
        startTime.setLayoutData(gd);
        startTime.addSelectionListener(getDateSelectionListener());
        gd = new GridData();
        label = toolkit.createLabel(composite, "Время останова:");
        label.setLayoutData(gd);
        gd = new GridData();
        stop = new DateTime(composite, SWT.DATE);
        stop.setLayoutData(gd);
        stop.addSelectionListener(getDateSelectionListener());
        stopTime = new DateTime(composite, SWT.TIME);
        stopTime.setLayoutData(gd);
        stopTime.addSelectionListener(getDateSelectionListener());
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED | Section.TITLE_BAR | Section.TWISTIE);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.maxHeight = 300;
        section.setLayoutData(td);
        section.setText("Описание");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(1, false));
        section.setClient(composite);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        description = toolkit.createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd.widthHint = 200;
        gd.heightHint = 60;
        description.setLayoutData(gd);
        description.addModifyListener(getTextModifyListener());
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED | Section.TITLE_BAR | Section.TWISTIE);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.maxHeight = 300;
        section.setLayoutData(td);
        section.setText("Комментарий");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(1, false));
        section.setClient(composite);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        comment = toolkit.createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd.widthHint = 200;
        gd.heightHint = 60;
        comment.setLayoutData(gd);
        comment.addModifyListener(getTextModifyListener());
    }

    public class ResearchModifyListener implements ModifyListener {

        public void modifyText(ModifyEvent e) {
            Research res = (Research) getComboEntity(research);
            if (res != null) {
                Calander cal = (Calander) new ResearchCalanderHierarchy().getParent(res);
                setComboEntity(calander, cal);
            }
        }
    }
}
