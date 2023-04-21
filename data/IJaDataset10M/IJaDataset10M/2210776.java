package net.esle.sinadura.gui.sections.preferences.windows;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.esle.sinadura.core.validate.CertificatePathBuilder;
import net.esle.sinadura.gui.application.ImagesResource;
import net.esle.sinadura.gui.application.LanguageResource;
import net.esle.sinadura.gui.sections.global.windows.AlertDialog;
import net.esle.sinadura.gui.sections.global.windows.FileDialogs;
import net.esle.sinadura.gui.sections.global.windows.InfoDialog;
import net.esle.sinadura.gui.sections.preferences.helpers.PreferencesHelper;
import org.bouncycastle.jce.X509Principal;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import sun.security.validator.KeyStores;

/**
 * @author zylk.net
 */
public class TrustedCertsPreferences extends FieldEditorPreferencePage {

    private static Logger logger = Logger.getLogger(SoftwareCertPreferences.class.getName());

    private Composite compositeMain = null;

    private org.eclipse.swt.widgets.List visualList = null;

    private List<String> aliasesPosition = null;

    private KeyStore ksTemp = null;

    public TrustedCertsPreferences() {
        super(FLAT);
        aliasesPosition = new ArrayList<String>();
        Set<X509Certificate> set = KeyStores.getTrustedCerts(PreferencesHelper.getTrustedKeystorePreferences());
        try {
            ksTemp = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            logger.log(Level.SEVERE, "", e);
        }
        try {
            ksTemp.load(null, null);
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, "", e);
        } catch (CertificateException e) {
            logger.log(Level.SEVERE, "", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "", e);
        }
        for (X509Certificate cert : set) {
            try {
                ksTemp.setCertificateEntry(CertificatePathBuilder.getUniqueID(cert), cert);
            } catch (KeyStoreException e) {
                logger.log(Level.SEVERE, "", e);
            }
        }
    }

    @Override
    protected void createFieldEditors() {
    }

    @Override
    protected Control createContents(Composite parent) {
        this.compositeMain = new Composite(parent, SWT.NONE);
        GridLayout gridLayoutPrincipal = new GridLayout();
        gridLayoutPrincipal.numColumns = 1;
        gridLayoutPrincipal.verticalSpacing = 10;
        gridLayoutPrincipal.marginBottom = 50;
        this.compositeMain.setLayout(gridLayoutPrincipal);
        GridData gdPrincipal = new GridData();
        gdPrincipal.horizontalAlignment = GridData.FILL;
        gdPrincipal.verticalAlignment = GridData.FILL;
        gdPrincipal.grabExcessHorizontalSpace = true;
        gdPrincipal.grabExcessVerticalSpace = true;
        this.compositeMain.setLayoutData(gdPrincipal);
        createListArea();
        return this.compositeMain;
    }

    private void createListArea() {
        Composite compositeLista = new Composite(this.compositeMain, SWT.NONE);
        GridLayout gridLayoutLista = new GridLayout();
        gridLayoutLista.numColumns = 2;
        compositeLista.setLayout(gridLayoutLista);
        GridData gdListComposite = new GridData();
        gdListComposite.horizontalAlignment = GridData.FILL;
        gdListComposite.verticalAlignment = GridData.FILL;
        gdListComposite.grabExcessHorizontalSpace = true;
        gdListComposite.grabExcessVerticalSpace = true;
        compositeLista.setLayoutData(gdListComposite);
        this.visualList = new org.eclipse.swt.widgets.List(compositeLista, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        this.visualList.addKeyListener(new SupButtonKeyListener());
        reloadVisualList();
        GridData gdList = new GridData();
        gdList.verticalSpan = 3;
        gdList.horizontalAlignment = GridData.FILL;
        gdList.verticalAlignment = GridData.FILL;
        gdList.grabExcessHorizontalSpace = true;
        gdList.grabExcessVerticalSpace = true;
        this.visualList.setLayoutData(gdList);
        Button buttonAdd = new Button(compositeLista, SWT.NONE);
        GridData gdAdd = new GridData();
        gdAdd.horizontalAlignment = GridData.FILL;
        buttonAdd.setLayoutData(gdAdd);
        buttonAdd.setText(LanguageResource.getLanguage().getString("button.add"));
        buttonAdd.setImage(new Image(this.compositeMain.getDisplay(), ImagesResource.ADD_IMG));
        buttonAdd.addSelectionListener(new ButtonAddListener());
        Button buttonShow = new Button(compositeLista, SWT.NONE);
        GridData gdMod = new GridData();
        gdMod.horizontalAlignment = GridData.FILL;
        buttonShow.setLayoutData(gdMod);
        buttonShow.setText(LanguageResource.getLanguage().getString("button.show"));
        buttonShow.setImage(new Image(this.compositeMain.getDisplay(), ImagesResource.EDIT_IMG));
        buttonShow.addSelectionListener(new ButtonShowListener());
        Button buttonRemove = new Button(compositeLista, SWT.NONE);
        GridData gdRemove = new GridData();
        gdRemove.horizontalAlignment = GridData.FILL;
        gdRemove.verticalAlignment = GridData.BEGINNING;
        buttonRemove.setLayoutData(gdRemove);
        buttonRemove.setText(LanguageResource.getLanguage().getString("button.remove"));
        buttonRemove.setImage(new Image(this.compositeMain.getDisplay(), ImagesResource.REMOVE_IMG));
        buttonRemove.addSelectionListener(new ButtonRemoveListener());
    }

    private void reloadVisualList() {
        visualList.removeAll();
        aliasesPosition = new ArrayList<String>();
        Enumeration<String> aliases = null;
        ;
        try {
            aliases = ksTemp.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                X509Certificate cert = (X509Certificate) ksTemp.getCertificate(alias);
                aliasesPosition.add(alias);
                try {
                    String s = "";
                    X509Principal principal = new X509Principal(cert.getSubjectX500Principal().getEncoded());
                    Vector<String> cn = principal.getValues(X509Principal.CN);
                    if (cn != null && cn.size() > 0) s += cn.get(0) + "";
                    visualList.add(s);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "", e);
                }
            }
        } catch (KeyStoreException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    private void removeTableFile() {
        try {
            int[] indices = visualList.getSelectionIndices();
            for (int i : indices) {
                ksTemp.deleteEntry(aliasesPosition.get(i));
            }
        } catch (KeyStoreException e) {
            logger.log(Level.SEVERE, "", e);
        }
        reloadVisualList();
    }

    class ButtonAddListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            String path = FileDialogs.openFileDialog(compositeMain.getShell(), FileDialogs.CERT_TYPE);
            if (path != null) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(path);
                    CertificateFactory cacertf = CertificateFactory.getInstance("X.509");
                    X509Certificate cert = (X509Certificate) cacertf.generateCertificate(fis);
                    X509Principal principal = null;
                    try {
                        principal = new X509Principal(cert.getSubjectX500Principal().getEncoded());
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "", e);
                    }
                    Vector<String> vec = principal.getValues();
                    String s = "";
                    for (String value : vec) {
                        s += value + " - ";
                    }
                    AlertDialog ad = new AlertDialog(compositeMain.getShell());
                    String m = MessageFormat.format(LanguageResource.getLanguage().getString("info.import.trusted_certificate"), s);
                    Boolean ok = ad.open(m);
                    if (ok) {
                        ksTemp.setCertificateEntry(CertificatePathBuilder.getUniqueID(cert), cert);
                        reloadVisualList();
                    }
                } catch (FileNotFoundException e) {
                    logger.log(Level.SEVERE, "", e);
                } catch (CertificateException e) {
                    logger.log(Level.SEVERE, "", e);
                    InfoDialog id = new InfoDialog(compositeMain.getShell());
                    id.open(LanguageResource.getLanguage().getString("error.importing.certificate"));
                } catch (KeyStoreException e) {
                    logger.log(Level.SEVERE, "", e);
                }
            }
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }

    class ButtonShowListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            try {
                int[] indices = visualList.getSelectionIndices();
                for (int i : indices) {
                    try {
                        X509Certificate cert = (X509Certificate) ksTemp.getCertificate(aliasesPosition.get(i));
                        String s = "";
                        s += LanguageResource.getLanguage().getString("preferences.trusted.show.subject") + "\n";
                        X509Principal principal = new X509Principal(cert.getSubjectX500Principal().getEncoded());
                        Vector<String> vec = principal.getValues();
                        for (String value : vec) {
                            s += value + " - ";
                        }
                        s += "\n\n";
                        s += LanguageResource.getLanguage().getString("preferences.trusted.show.valid") + "\n";
                        SimpleDateFormat dateFormat = LanguageResource.getFullFormater();
                        s += LanguageResource.getLanguage().getString("preferences.trusted.show.valid.from") + " " + dateFormat.format(cert.getNotBefore()) + "\n";
                        s += LanguageResource.getLanguage().getString("preferences.trusted.show.valid.until") + " " + dateFormat.format(cert.getNotAfter()) + "\n";
                        s += "\n";
                        boolean[] usage = cert.getKeyUsage();
                        List<String> extended = null;
                        try {
                            extended = cert.getExtendedKeyUsage();
                        } catch (CertificateParsingException e) {
                            logger.log(Level.SEVERE, "", e);
                        }
                        if (usage != null && extended != null) {
                            s += LanguageResource.getLanguage().getString("preferences.trusted.show.uses") + "\n";
                            for (int j = 0; j < usage.length; j++) {
                                if (j == 0 && usage[j]) s += "Digital signature, ";
                                if (j == 1 && usage[j]) s += "Non repudiation, ";
                                if (j == 2 && usage[j]) s += "Key encipherment, ";
                                if (j == 3 && usage[j]) s += "Data encipherment, ";
                                if (j == 4 && usage[j]) s += "Key agreement, ";
                                if (j == 5 && usage[j]) s += "KeyCert sign, ";
                                if (j == 6 && usage[j]) s += "CRL sign, ";
                                if (j == 7 && usage[j]) s += "Encipher only, ";
                                if (j == 8 && usage[j]) s += "Decipher only, ";
                            }
                            for (String ext : extended) {
                                if (ext.equals("1.3.6.1.5.5.7.3.1")) s += "Server authentication , ";
                                if (ext.equals("1.3.6.1.5.5.7.3.2")) s += "Client authentication, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.3")) s += "Code signing, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.4")) s += "E-mail protection , ";
                                if (ext.equals("1.3.6.1.5.5.7.3.5")) s += "IP security end system, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.6")) s += "IP security tunnel termination, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.7")) s += "IP security user, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.8")) s += "Timestamping, ";
                                if (ext.equals("1.3.6.1.5.5.7.3.9")) s += "OCSP signing , ";
                            }
                            s += "\n\n";
                        }
                        s += LanguageResource.getLanguage().getString("preferences.trusted.show.issuer") + "\n";
                        X509Principal principal2 = new X509Principal(cert.getIssuerX500Principal().getEncoded());
                        Vector<String> vec2 = principal2.getValues();
                        for (String value : vec2) {
                            s += value + " - ";
                        }
                        s += "\n";
                        InfoDialog id = new InfoDialog(compositeMain.getShell());
                        id.open(s);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "", e);
                    }
                }
            } catch (KeyStoreException e) {
                logger.log(Level.SEVERE, "", e);
            }
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }

    class ButtonRemoveListener implements SelectionListener {

        public void widgetSelected(SelectionEvent event) {
            removeTableFile();
        }

        public void widgetDefaultSelected(SelectionEvent event) {
            widgetSelected(event);
        }
    }

    class SupButtonKeyListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            if (SWT.DEL == e.character) {
                removeTableFile();
            }
        }
    }

    private void savePreferences() {
        PreferencesHelper.setTrustedKeystorePreferences(ksTemp);
    }

    @Override
    protected void performApply() {
        savePreferences();
        super.performApply();
    }

    @Override
    public boolean performOk() {
        savePreferences();
        return super.performOk();
    }
}
