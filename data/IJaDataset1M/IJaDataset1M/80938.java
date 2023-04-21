package edu.gsbme.wasabi.UI.Forms.Declaration;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import edu.gsbme.wasabi.UI.Forms.FormTemplate;

public class UnitForm extends FormTemplate {

    public Text multiplier;

    public Text offset;

    public Text exponent;

    public Combo prefix;

    public String[] prefix_array = new String[] { "None", "yotta", "zetta", "exa", "peta", "tera", "giga", "mega", "kilo", "hecto", "deka", "deci", "centi", "milli", "micro", "nano", "pico", "femto", "atto", "zepto", "yocto" };

    @Override
    public void construct_layout(FormToolkit toolkit, final ScrolledForm form) {
        form.setText("Unit");
        GridLayout layout = new GridLayout();
        form.getBody().setLayout(layout);
        layout.numColumns = 2;
        form.getBody().setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.horizontalSpan = 1;
        Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        section.setLayoutData(gd);
        section.addExpansionListener(new ExpansionAdapter() {

            public void expansionStateChanged(ExpansionEvent e) {
                form.reflow(true);
            }
        });
        section.setText("Unit Property");
        section.setDescription("");
        Composite sectionClient = toolkit.createComposite(section);
        GridLayout sc = new GridLayout();
        sc.numColumns = 4;
        sectionClient.setLayout(sc);
        Label label = new Label(sectionClient, SWT.NULL);
        label.setText("Exponent :");
        gd = new GridData();
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);
        exponent = new Text(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        exponent.setLayoutData(gd);
        label = new Label(sectionClient, SWT.NULL);
        label.setText("Multiplier :");
        gd = new GridData();
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);
        multiplier = new Text(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        multiplier.setLayoutData(gd);
        label = new Label(sectionClient, SWT.NULL);
        label.setText("Offset :");
        gd = new GridData();
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);
        offset = new Text(sectionClient, SWT.BORDER);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        offset.setLayoutData(gd);
        label = new Label(sectionClient, SWT.NULL);
        label.setText("Prefix :");
        gd = new GridData();
        gd.horizontalSpan = 1;
        label.setLayoutData(gd);
        prefix = new Combo(sectionClient, SWT.BORDER | SWT.READ_ONLY);
        for (int i = 0; i < prefix_array.length; i++) {
            prefix.add(prefix_array[i]);
        }
        prefix.select(0);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 3;
        prefix.setLayoutData(gd);
        section.setClient(sectionClient);
    }

    @Override
    public void validate_form(IManagedForm mform) {
    }
}
