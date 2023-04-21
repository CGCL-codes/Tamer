package research.ui.editors;

import java.sql.Blob;
import java.sql.SQLException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import research.domain.Research;
import research.domain.Stabilizer;
import research.entity.EntityType;
import research.model.EntityListContentProvider;
import research.model.ListHierarchy;
import research.persistence.PersistenceManager;
import research.ui.editors.input.EntityEditorInput;

public class StabilizerEditor extends FormEntityEditor {

    public static String ID = "research.ui.editors.StabilizerEditor";

    private ComboViewer research;

    private Text name;

    private Text descr;

    private Image image;

    private Label photo;

    private Stabilizer getEntity() {
        return (Stabilizer) entity;
    }

    @Override
    protected boolean checkInputEntityType(EntityEditorInput input) {
        return input.getEntity().getType().equals(EntityType.Stabilizer);
    }

    @Override
    protected void fill() {
        this.name.setText(this.getEntity().getName());
        this.descr.setText(this.getEntity().getDescription());
        setComboEntity(research, this.getEntity().getResearch());
        try {
            Blob blob = getEntity().getPhoto();
            if (blob != null) {
                image = new Image(getSite().getShell().getDisplay(), blob.getBinaryStream());
                photo.setLayoutData(new GridData(image.getBounds().width, image.getBounds().height));
                photo.setImage(image);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected boolean readAndValidate() {
        this.getEntity().setName(this.name.getText());
        this.getEntity().setDescription(this.descr.getText());
        Research cal = (Research) getComboEntity(research);
        if (cal == null) {
            MessageDialog.openWarning(getSite().getShell(), "Ошибка", "Выберите исследование");
            return false;
        }
        this.getEntity().setPhoto(null);
        this.getEntity().setResearch(cal);
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
        section.setText("Исследование");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(2, false));
        section.setClient(composite);
        gd = new GridData();
        label = toolkit.createLabel(composite, "Выберите исследование*:");
        label.setLayoutData(gd);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        research = new ComboViewer(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
        research.setContentProvider(new EntityListContentProvider(new ListHierarchy(EntityType.Research), false));
        research.setLabelProvider(new LabelProvider());
        research.setInput(PersistenceManager.getInstance());
        gd.widthHint = 100;
        research.getCombo().setLayoutData(gd);
        research.getCombo().addModifyListener(getTextModifyListener());
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
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED | Section.TITLE_BAR | Section.TWISTIE);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.maxHeight = 300;
        section.setLayoutData(td);
        section.setText("Описание");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(1, false));
        section.setClient(composite);
        gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        descr = toolkit.createText(composite, "", SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        gd.widthHint = 200;
        gd.heightHint = 60;
        descr.setLayoutData(gd);
        descr.addModifyListener(getTextModifyListener());
        section = toolkit.createSection(form.getBody(), Section.TITLE_BAR | Section.EXPANDED | Section.TITLE_BAR | Section.TWISTIE);
        td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.maxHeight = 300;
        section.setLayoutData(td);
        section.setText("Изображение");
        composite = toolkit.createComposite(section);
        composite.setLayout(new GridLayout(1, false));
        section.setClient(composite);
        gd = new GridData();
        photo = toolkit.createLabel(composite, "", SWT.NONE);
        photo.setLayoutData(gd);
    }
}
