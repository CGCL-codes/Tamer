package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.wizard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.model.IModel;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.model.ModelFactory;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Bin;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Refrence;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.RefrenceClass;

/**
 * used to create refrences
 * 
 * @author wohlgemuth
 */
public class CreateRefrenceWizard extends Wizard {

    private final HashMap<RefrenceClass, Refrence> refrences = new HashMap<RefrenceClass, Refrence>();

    private class MainPage extends WizardPage {

        protected MainPage(final String pageName) {
            super(pageName);
            setDescription("please select the wished class and provide a value");
        }

        @SuppressWarnings("unchecked")
        public void createControl(final Composite parent) {
            final Composite main = new Composite(parent, SWT.NONE);
            final GridLayout layout = new GridLayout(2, true);
            layout.marginBottom = 5;
            layout.marginRight = 5;
            layout.marginLeft = 5;
            layout.marginTop = 5;
            main.setLayout(layout);
            final IModel<Object> model = ModelFactory.newInstance().createModel(RefrenceClass.class);
            model.setQuery("from RefrenceClass");
            final Object[] o = model.executeQuery();
            for (final Object element : o) {
                final RefrenceClass c = (RefrenceClass) element;
                final Button button = new Button(main, SWT.CHECK);
                button.setText(c.getName());
                button.setToolTipText(c.getDescription());
                final GridData left = new GridData();
                left.grabExcessHorizontalSpace = true;
                left.grabExcessVerticalSpace = false;
                left.horizontalAlignment = GridData.BEGINNING;
                left.verticalAlignment = GridData.BEGINNING;
                button.setLayoutData(left);
                final Text text = new Text(main, SWT.BORDER);
                text.setToolTipText("please insert here you value for this reference");
                text.addKeyListener(new KeyListener() {

                    public void keyPressed(final KeyEvent e) {
                        createRefrence(c, text);
                    }

                    /**
					 * creates the actual refrence
					 * 
					 * @param c
					 * @param text
					 */
                    private void createRefrence(final RefrenceClass c, final Text text) {
                        final String value = text.getText();
                        if (value.length() > 0) {
                            setErrorMessage(null);
                        } else {
                            setErrorMessage("please enter a value into the textfield");
                            return;
                        }
                        final Refrence refrence = new Refrence();
                        refrence.setRefrenceClass(c);
                        refrence.setValue(value);
                        if (refrences.containsKey(c)) {
                            refrences.remove(c);
                        }
                        refrences.put(c, refrence);
                        getContainer().updateButtons();
                        canFlipToNextPage();
                    }

                    public void keyReleased(final KeyEvent e) {
                        createRefrence(c, text);
                    }
                });
                final GridData right = new GridData();
                right.grabExcessHorizontalSpace = true;
                right.grabExcessVerticalSpace = false;
                right.horizontalAlignment = GridData.FILL;
                right.verticalAlignment = GridData.BEGINNING;
                text.setLayoutData(right);
                button.addSelectionListener(new SelectionListener() {

                    public void widgetDefaultSelected(final SelectionEvent e) {
                    }

                    public void widgetSelected(final SelectionEvent e) {
                        text.setEnabled(button.getSelection());
                        if (button.getSelection() == false) {
                            refrences.remove(c);
                        }
                    }
                });
                button.setSelection(false);
                text.setEnabled(false);
            }
            setControl(main);
        }

        @Override
        public boolean canFlipToNextPage() {
            return !refrences.isEmpty();
        }
    }

    private final Collection<Bin> bins;

    private MainPage main;

    public CreateRefrenceWizard(final Bin a) {
        bins = new Vector<Bin>();
        bins.add(a);
    }

    public CreateRefrenceWizard(final Collection<Bin> a) {
        bins = a;
    }

    @Override
    public void addPages() {
        super.addPages();
        main = new MainPage("create refrence");
        addPage(main);
    }

    @Override
    public boolean performFinish() {
        final IModel model = ModelFactory.newInstance().createModel(Refrence.class);
        for (final Bin bin : bins) {
            for (final Refrence refrence : refrences.values()) {
                final Refrence ref = new Refrence();
                ref.setBin(bin);
                ref.setValue(refrence.getValue());
                ref.setRefrenceClass(refrence.getRefrenceClass());
                model.add(ref);
            }
        }
        return true;
    }

    @Override
    public boolean canFinish() {
        return main.canFlipToNextPage();
    }

    @Override
    public boolean performCancel() {
        return super.performCancel();
    }
}
