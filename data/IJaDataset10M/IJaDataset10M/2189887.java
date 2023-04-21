package net.sf.iauthor.ui.editors.pages;

import net.sf.iauthor.core.Scene;
import net.sf.iauthor.ui.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
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
public class RatingsPart extends SectionPart {

    private boolean _inInit;

    private IEditorInput _input;

    private Spinner _relSpin;

    private Spinner _tenSpin;

    private Spinner _humSpin;

    private Spinner _quaSpin;

    /**
	 * @param parent
	 * @param toolkit
	 * @param style
	 */
    public RatingsPart(Composite parent, FormToolkit toolkit) {
        super(parent, toolkit, Section.TWISTIE | Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        Section section = getSection();
        section.setText(Messages.RatingsPart_0);
        section.setDescription(Messages.RatingsPart_1);
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
        toolkit.createLabel(client, Messages.RatingsPart_2);
        _relSpin = new Spinner(client, SWT.WRAP | SWT.BORDER);
        _relSpin.setMinimum(1);
        _relSpin.setMaximum(10);
        _relSpin.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!_inInit) markDirty();
            }
        });
        toolkit.adapt(_relSpin, true, true);
        toolkit.createLabel(client, Messages.RatingsPart_3);
        _tenSpin = new Spinner(client, SWT.WRAP | SWT.BORDER);
        _tenSpin.setMinimum(1);
        _tenSpin.setMaximum(10);
        _tenSpin.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!_inInit) markDirty();
            }
        });
        toolkit.createLabel(client, Messages.RatingsPart_4);
        _humSpin = new Spinner(client, SWT.WRAP | SWT.BORDER);
        _humSpin.setMinimum(1);
        _humSpin.setMaximum(10);
        _humSpin.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!_inInit) markDirty();
            }
        });
        toolkit.createLabel(client, Messages.RatingsPart_5);
        _quaSpin = new Spinner(client, SWT.WRAP | SWT.BORDER);
        _quaSpin.setMinimum(1);
        _quaSpin.setMaximum(10);
        _quaSpin.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!_inInit) markDirty();
            }
        });
    }

    /**
	 * @param editorInput
	 */
    public void initData(IEditorInput editorInput) {
        _inInit = true;
        _input = editorInput;
        Scene scene = (Scene) _input.getAdapter(Scene.class);
        if (scene.getRatingRelevance() != null) _relSpin.setSelection(scene.getRatingRelevance());
        if (scene.getRatingQuality() != null) _quaSpin.setSelection(scene.getRatingQuality());
        if (scene.getRatingHumour() != null) _humSpin.setSelection(scene.getRatingHumour());
        if (scene.getRatingTension() != null) _tenSpin.setSelection(scene.getRatingTension());
        _inInit = false;
    }

    /**
	 * @see org.eclipse.ui.forms.AbstractFormPart#commit(boolean)
	 */
    @Override
    public void commit(boolean onSave) {
        Scene scene = (Scene) _input.getAdapter(Scene.class);
        scene.setRatingHumour(_humSpin.getSelection());
        scene.setRatingRelevance(_relSpin.getSelection());
        scene.setRatingTension(_tenSpin.getSelection());
        scene.setRatingQuality(_quaSpin.getSelection());
        super.commit(onSave);
    }
}
