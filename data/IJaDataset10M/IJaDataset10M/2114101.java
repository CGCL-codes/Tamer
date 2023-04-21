package net.sf.ipm;

import net.sf.ipm.baza.ResultSet;
import net.sf.ipm.sluchacze.PaintGrafikaListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use.
 * If Jigloo is being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should
 * purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE
 * CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class FormAudio extends Composite {

    private ResultSet rsDaneAudio;

    private CLabel cLabel1;

    private Text textAudio;

    private Text textKomentarz;

    private CLabel cLabel11;

    private Canvas canObrazek;

    private Text textBitrate;

    private Button butSzukaj;

    private Button butUsun;

    private Button butZmien;

    private Button butDodaj;

    private Composite composite3;

    private Text textGatunek;

    private CLabel cLabel8;

    private Text textAlbum;

    private CLabel cLabel7;

    private Text textFormatPliku;

    private CLabel cLabel16;

    private Text textCzestotliwosc;

    private CLabel cLabel15;

    private Text textTag;

    private CLabel cLabel14;

    private Text textData;

    private CLabel cLabel13;

    private Text textNrDysku;

    private Text textNrUtworu;

    private CLabel cLabel10;

    private CLabel cLabel9;

    private Composite composite2;

    private Composite composite1;

    private Button butOkladka;

    private CLabel cLabel6;

    private CLabel cLabel4;

    private Text textCzasTrwania;

    private CLabel cLabel2;

    private Shell shell;

    private Text textZespol;

    private PaintGrafikaListener paintListener;

    private Button butOdswiez;

    public FormAudio(org.eclipse.swt.widgets.Composite parent, int style, ResultSet resultSet) {
        super(parent, style);
        shell = parent.getShell();
        rsDaneAudio = resultSet;
        initGUI();
    }

    private void initGUI() {
        try {
            GridLayout thisLayout = new GridLayout();
            thisLayout.numColumns = 2;
            setLayout(thisLayout);
            setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
            composite2 = new Composite(this, SWT.NONE);
            GridLayout composite2Layout = new GridLayout();
            composite2Layout.numColumns = 3;
            GridData composite2LData = new GridData();
            composite2LData.horizontalAlignment = GridData.FILL;
            composite2LData.verticalAlignment = GridData.FILL;
            composite2LData.grabExcessVerticalSpace = true;
            composite2LData.grabExcessHorizontalSpace = true;
            composite2.setLayoutData(composite2LData);
            composite2.setLayout(composite2Layout);
            canObrazek = new Canvas(composite2, SWT.BORDER);
            GridData canvas1LData = new GridData();
            canvas1LData.horizontalSpan = 3;
            canvas1LData.widthHint = 150;
            canvas1LData.horizontalAlignment = GridData.END;
            canvas1LData.verticalAlignment = GridData.BEGINNING;
            canvas1LData.heightHint = 200;
            canObrazek.setLayoutData(canvas1LData);
            butOkladka = new Button(composite2, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            GridData butOkladkaLData = new GridData();
            butOkladkaLData.verticalAlignment = GridData.END;
            butOkladkaLData.horizontalAlignment = GridData.END;
            butOkladkaLData.horizontalSpan = 3;
            butOkladka.setLayoutData(butOkladkaLData);
            butOkladka.setText("Dodaj okładkę albumu");
            butOkladka.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent evt) {
                    String[] filterExtensions = { "*.jpg", "*.jpeg", "*.gif", "*.bmp", "*.png" };
                    FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
                    fileDialog.setText("Dodaj grafikę...");
                    fileDialog.setFilterExtensions(filterExtensions);
                    String plik = fileDialog.open();
                    if (paintListener == null) {
                        paintListener = new PaintGrafikaListener(shell.getDisplay(), plik);
                    } else {
                        canObrazek.removeListener(SWT.Paint, paintListener);
                        paintListener.setPlik(plik);
                    }
                    canObrazek.addListener(SWT.Paint, paintListener);
                    canObrazek.redraw();
                }
            });
            cLabel4 = new CLabel(composite2, SWT.NONE);
            GridData cLabel4LData = new GridData();
            cLabel4LData.verticalAlignment = GridData.BEGINNING;
            cLabel4.setLayoutData(cLabel4LData);
            cLabel4.setText("Komentarz");
            textKomentarz = new Text(composite2, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
            GridData textKomentarzLData = new GridData();
            textKomentarzLData.verticalAlignment = GridData.BEGINNING;
            textKomentarzLData.horizontalAlignment = GridData.FILL;
            textKomentarzLData.horizontalSpan = 2;
            textKomentarzLData.heightHint = 80;
            textKomentarzLData.grabExcessHorizontalSpace = true;
            textKomentarz.setLayoutData(textKomentarzLData);
            composite1 = new Composite(this, SWT.NONE);
            GridLayout composite1Layout = new GridLayout();
            composite1Layout.numColumns = 2;
            GridData composite1LData = new GridData();
            composite1LData.horizontalAlignment = GridData.FILL;
            composite1LData.verticalAlignment = GridData.FILL;
            composite1LData.grabExcessHorizontalSpace = true;
            composite1LData.grabExcessVerticalSpace = true;
            composite1.setLayoutData(composite1LData);
            composite1.setLayout(composite1Layout);
            cLabel10 = new CLabel(composite1, SWT.NONE);
            GridData cLabel10LData = new GridData();
            cLabel10LData.horizontalAlignment = GridData.END;
            cLabel10LData.verticalAlignment = GridData.BEGINNING;
            cLabel10.setLayoutData(cLabel10LData);
            cLabel10.setText("Zespół");
            textZespol = new Text(composite1, SWT.NONE);
            GridData textZespolLData = new GridData();
            textZespolLData.horizontalAlignment = GridData.FILL;
            textZespolLData.grabExcessHorizontalSpace = true;
            textZespol.setLayoutData(textZespolLData);
            cLabel1 = new CLabel(composite1, SWT.NONE);
            GridData cLabel1LData = new GridData();
            cLabel1LData.horizontalAlignment = GridData.END;
            cLabel1LData.verticalAlignment = GridData.BEGINNING;
            cLabel1.setLayoutData(cLabel1LData);
            cLabel1.setText("Utwór");
            textAudio = new Text(composite1, SWT.NONE);
            GridData textTytulLData = new GridData();
            textTytulLData.horizontalAlignment = GridData.FILL;
            textTytulLData.grabExcessHorizontalSpace = true;
            textAudio.setLayoutData(textTytulLData);
            cLabel7 = new CLabel(composite1, SWT.NONE);
            GridData cLabel7LData = new GridData();
            cLabel7LData.horizontalAlignment = GridData.END;
            cLabel7LData.verticalAlignment = GridData.BEGINNING;
            cLabel7.setLayoutData(cLabel7LData);
            cLabel7.setText("Album");
            textAlbum = new Text(composite1, SWT.NONE);
            GridData textTytulEnLData = new GridData();
            textTytulEnLData.horizontalAlignment = GridData.FILL;
            textTytulEnLData.grabExcessHorizontalSpace = true;
            textAlbum.setLayoutData(textTytulEnLData);
            cLabel13 = new CLabel(composite1, SWT.NONE);
            GridData cLabel13LData = new GridData();
            cLabel13LData.horizontalAlignment = GridData.END;
            cLabel13LData.verticalAlignment = GridData.BEGINNING;
            cLabel13.setLayoutData(cLabel13LData);
            cLabel13.setText("Rok wydania");
            textData = new Text(composite1, SWT.NONE);
            GridData textDataLData = new GridData();
            textDataLData.horizontalAlignment = GridData.FILL;
            textDataLData.grabExcessHorizontalSpace = true;
            textData.setLayoutData(textDataLData);
            cLabel9 = new CLabel(composite1, SWT.NONE);
            GridData cLabel9LData = new GridData();
            cLabel9LData.horizontalAlignment = GridData.END;
            cLabel9LData.verticalAlignment = GridData.BEGINNING;
            cLabel9.setLayoutData(cLabel9LData);
            cLabel9.setText("Nr utworu");
            GridData textKrajLData = new GridData();
            textKrajLData.horizontalAlignment = GridData.FILL;
            textKrajLData.grabExcessHorizontalSpace = true;
            textNrUtworu = new Text(composite1, SWT.NONE);
            textNrUtworu.setLayoutData(textKrajLData);
            cLabel6 = new CLabel(composite1, SWT.NONE);
            GridData cLabel6LData = new GridData();
            cLabel6LData.horizontalAlignment = GridData.END;
            cLabel6LData.verticalAlignment = GridData.BEGINNING;
            cLabel6.setLayoutData(cLabel6LData);
            cLabel6.setText("Nr dysku");
            GridData textGatunekLData = new GridData();
            textGatunekLData.horizontalAlignment = GridData.FILL;
            textGatunekLData.grabExcessHorizontalSpace = true;
            textNrDysku = new Text(composite1, SWT.NONE);
            textNrDysku.setLayoutData(textGatunekLData);
            cLabel8 = new CLabel(composite1, SWT.NONE);
            GridData cLabel8LData = new GridData();
            cLabel8LData.horizontalAlignment = GridData.END;
            cLabel8LData.verticalAlignment = GridData.BEGINNING;
            cLabel8.setLayoutData(cLabel8LData);
            cLabel8.setText("Czas trwania");
            textCzasTrwania = new Text(composite1, SWT.NONE);
            GridData textCzasTrwaniaLData = new GridData();
            textCzasTrwaniaLData.horizontalAlignment = GridData.FILL;
            textCzasTrwaniaLData.grabExcessHorizontalSpace = true;
            textCzasTrwania.setLayoutData(textCzasTrwaniaLData);
            cLabel2 = new CLabel(composite1, SWT.NONE);
            GridData cLabel2LData = new GridData();
            cLabel2LData.horizontalAlignment = GridData.END;
            cLabel2LData.verticalAlignment = GridData.BEGINNING;
            cLabel2.setLayoutData(cLabel2LData);
            cLabel2.setText("Gatunek");
            textGatunek = new Text(composite1, SWT.NONE);
            GridData textOcenaLData = new GridData();
            textOcenaLData.horizontalAlignment = GridData.FILL;
            textOcenaLData.grabExcessHorizontalSpace = true;
            textGatunek.setLayoutData(textOcenaLData);
            cLabel14 = new CLabel(composite1, SWT.NONE);
            GridData cLabel14LData = new GridData();
            cLabel14LData.horizontalAlignment = GridData.END;
            cLabel14LData.verticalAlignment = GridData.BEGINNING;
            cLabel14.setLayoutData(cLabel14LData);
            cLabel14.setText("Tag");
            GridData textRezyseriaLData = new GridData();
            textRezyseriaLData.horizontalAlignment = GridData.FILL;
            textRezyseriaLData.grabExcessHorizontalSpace = true;
            textTag = new Text(composite1, SWT.NONE);
            textTag.setLayoutData(textRezyseriaLData);
            cLabel15 = new CLabel(composite1, SWT.NONE);
            GridData cLabel15LData = new GridData();
            cLabel15LData.horizontalAlignment = GridData.END;
            cLabel15LData.verticalAlignment = GridData.BEGINNING;
            cLabel15.setLayoutData(cLabel15LData);
            cLabel15.setText("Częstotliwość");
            GridData textScenariuszLData = new GridData();
            textScenariuszLData.horizontalAlignment = GridData.FILL;
            textScenariuszLData.grabExcessHorizontalSpace = true;
            textCzestotliwosc = new Text(composite1, SWT.NONE);
            textCzestotliwosc.setLayoutData(textScenariuszLData);
            cLabel16 = new CLabel(composite1, SWT.NONE);
            GridData cLabel16LData = new GridData();
            cLabel16LData.horizontalAlignment = GridData.END;
            cLabel16LData.verticalAlignment = GridData.BEGINNING;
            cLabel16.setLayoutData(cLabel16LData);
            cLabel16.setText("Bitrate");
            textBitrate = new Text(composite1, SWT.NONE);
            GridData textBitrateLData = new GridData();
            textBitrateLData.horizontalAlignment = GridData.FILL;
            textBitrateLData.grabExcessHorizontalSpace = true;
            textBitrate.setLayoutData(textBitrateLData);
            cLabel11 = new CLabel(composite1, SWT.NONE);
            GridData cLabel11LData = new GridData();
            cLabel11LData.horizontalAlignment = GridData.END;
            cLabel11LData.verticalAlignment = GridData.BEGINNING;
            cLabel11.setLayoutData(cLabel11LData);
            cLabel11.setText("Format pliku");
            GridData textFormatPlikuLData = new GridData();
            textFormatPlikuLData.horizontalAlignment = GridData.FILL;
            textFormatPlikuLData.grabExcessHorizontalSpace = true;
            textFormatPliku = new Text(composite1, SWT.NONE);
            textFormatPliku.setLayoutData(textFormatPlikuLData);
            composite3 = new Composite(this, SWT.NONE);
            GridData composite3LData = new GridData(SWT.END, SWT.END, false, false);
            composite3LData.horizontalSpan = 2;
            composite3.setLayoutData(composite3LData);
            composite3LData = null;
            composite3.setLayout(new GridLayout(5, true));
            butSzukaj = new Button(composite3, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            GridData butSzukajLData = new GridData();
            butSzukajLData.horizontalAlignment = GridData.FILL;
            butSzukaj.setLayoutData(butSzukajLData);
            butSzukaj.setText("Szukaj");
            butSzukaj.setVisible(false);
            butSzukaj.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    System.out.println("butSzukaj.widgetSelected, event=" + evt);
                }
            });
            butOdswiez = new Button(composite3, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            butOdswiez.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
            butOdswiez.setText("Odśwież");
            butOdswiez.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    System.out.println("butOdswiez.widgetSelected, event=" + evt);
                }
            });
            butDodaj = new Button(composite3, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            GridData butDodajLData = new GridData();
            butDodajLData.horizontalAlignment = GridData.FILL;
            butDodaj.setLayoutData(butDodajLData);
            butDodaj.setText("Dodaj");
            butDodaj.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    System.out.println("butDodaj.widgetSelected, event=" + evt);
                }
            });
            butZmien = new Button(composite3, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            GridData butZmienLData = new GridData();
            butZmienLData.horizontalAlignment = GridData.FILL;
            butZmien.setLayoutData(butZmienLData);
            butZmien.setText("Zmień");
            butZmien.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    System.out.println("butZmien.widgetSelected, event=" + evt);
                }
            });
            butUsun = new Button(composite3, SWT.PUSH | SWT.FLAT | SWT.CENTER);
            GridData butUsuńLData = new GridData();
            butUsuńLData.horizontalAlignment = GridData.FILL;
            butUsun.setLayoutData(butUsuńLData);
            butUsun.setText("Usuń");
            butUsun.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    System.out.println("butUsun.widgetSelected, event=" + evt);
                }
            });
            this.layout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
