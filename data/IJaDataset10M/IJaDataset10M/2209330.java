package net.sf.iauthor.ui.editors.pages;

import net.sf.iauthor.core.Scene;
import net.sf.iauthor.ui.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Andreas Beckers
 * 
 */
public class NotesPart extends SectionPart {

    private boolean _inInit;

    private IEditorInput _input;

    private Text _text;

    /**
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
    public NotesPart(Composite parent, FormToolkit toolkit) {
        super(parent, toolkit, Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        Section section = getSection();
        section.setText(Messages.NotesPart_0);
        section.setDescription(Messages.NotesPart_1);
        Composite client = toolkit.createComposite(section);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        layout.numColumns = 2;
        client.setLayout(layout);
        section.setClient(client);
        section.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                getManagedForm().reflow(false);
            }
        });
        toolkit.createLabel(client, Messages.NotesPart_2);
        _text = toolkit.createText(client, "", SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        _text.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                if (!_inInit) markDirty();
            }
        });
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.heightHint = 500;
        gridData.widthHint = 750;
        _text.setLayoutData(gridData);
    }

    /**
	 * @param input
	 */
    public void initData(IEditorInput input) {
        _input = input;
        _inInit = true;
        Scene scene = (Scene) input.getAdapter(Scene.class);
        if (scene.getNotes() != null) _text.setText(scene.getNotes());
        _inInit = false;
    }

    /**
	 * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
	 */
    @Override
    public void commit(boolean onSave) {
        Scene scene = (Scene) _input.getAdapter(Scene.class);
        scene.setNotes(_text.getText());
        super.commit(onSave);
    }
}
