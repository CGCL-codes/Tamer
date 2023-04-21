package com.migniot.streamy.proxy;

import java.util.regex.Pattern;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The network preferences.
 */
public class NetworkPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    /**
	 * A verification pattern.
	 */
    public static final Pattern NUMBERS = Pattern.compile("[1-9][0-9]*");

    /**
	 * The proxy port text field.
	 */
    private Spinner portSpinner;

    /**
	 * The proxy maximum threads text field.
	 */
    private Spinner threadsSpinner;

    /**
	 * The minimum backup size text field.
	 */
    private Spinner minimumSpinner;

    /**
	 * The maximum metadata size text field.
	 */
    private Spinner maximumSpinner;

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected Control createContents(Composite parent) {
        Composite page = new Composite(parent, SWT.NONE);
        page.setLayout(new GridLayout(2, false));
        createDescriptionContent(page);
        createSizingContent(page);
        createProxyContent(page);
        return page;
    }

    /**
	 * Create the description
	 * 
	 * @param page
	 *            The parent page
	 */
    private void createDescriptionContent(Composite page) {
        Label label = new Label(page, SWT.NONE);
        label.setText("" + "The download size is the minimum size to reach to save a file\n" + " every network file bigger will be saved to disk.\n" + "The metadata size is the maximum size of packets containing\n" + " title, author and albums.\n" + "The proxy port is the network port used on localhost\n" + "The proxy maximum threads may be increased if you have both\n" + " a powerful machine and a need to open dozens of browser tabs.\n\n");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    }

    /**
	 * Create proxy sizing configuration editors.
	 * 
	 * @param page
	 *            The main composite
	 */
    private void createSizingContent(Composite page) {
        Label label = new Label(page, SWT.NONE);
        label.setText("Download size (Kb)");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.minimumSpinner = new Spinner(page, SWT.NONE);
        this.minimumSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        long minimumKb = ProxyPlugin.getDefault().getPluginPreferences().getLong(PreferenceInitializer.MINIMUM_BACKUP) / 1024;
        this.minimumSpinner.setValues((int) minimumKb, 0, 128 * 1024, 0, 1, 128);
        label = new Label(page, SWT.NONE);
        label.setText("Metadata size (Kb)");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.maximumSpinner = new Spinner(page, SWT.NONE);
        this.maximumSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        long maximumKb = ProxyPlugin.getDefault().getPluginPreferences().getLong(PreferenceInitializer.MAXIMUM_METADATA) / 1024;
        this.maximumSpinner.setValues((int) maximumKb, 0, 128 * 1024, 0, 1, 128);
    }

    /**
	 * Create proxy technical configuration editors.
	 * 
	 * @param page
	 *            The main composite
	 */
    private void createProxyContent(Composite page) {
        Label label = new Label(page, SWT.NONE);
        label.setText("Proxy port");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.portSpinner = new Spinner(page, SWT.NONE);
        this.portSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.portSpinner.setValues(ProxyPlugin.getDefault().getPluginPreferences().getInt(PreferenceInitializer.PROXY_PORT), 1025, 65535, 0, 1, 1024);
        label = new Label(page, SWT.NONE);
        label.setText("Proxy maximum threads");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.threadsSpinner = new Spinner(page, SWT.NONE);
        this.threadsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        this.threadsSpinner.setValues(ProxyPlugin.getDefault().getPluginPreferences().getInt(PreferenceInitializer.MAX_THREADS), 16, 65535, 0, 1, 16);
    }

    /**
	 * {@inheritDoc}
	 */
    public void init(IWorkbench workbench) {
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean performOk() {
        ProxyPlugin.getDefault().getPluginPreferences().setValue(PreferenceInitializer.MAX_THREADS, threadsSpinner.getSelection());
        ProxyPlugin.getDefault().getPluginPreferences().setValue(PreferenceInitializer.MAXIMUM_METADATA, maximumSpinner.getSelection() * 1024);
        ProxyPlugin.getDefault().getPluginPreferences().setValue(PreferenceInitializer.MINIMUM_BACKUP, minimumSpinner.getSelection() * 1024);
        ProxyPlugin.getDefault().getPluginPreferences().setValue(PreferenceInitializer.PROXY_PORT, portSpinner.getSelection());
        return super.performOk();
    }
}
