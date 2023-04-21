package atp.view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JDialog;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.Dialog;
import javax.swing.ScrollPaneLayout;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import java.text.DecimalFormat;
import javax.swing.text.MaskFormatter;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JDesktopPane;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.MaskFormatter;
import javax.swing.ListSelectionModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.text.Position;
import javax.swing.ListModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.event.TableModelEvent;
import java.awt.Color;
import java.awt.Component;
import java.lang.Class;
import javax.swing.JOptionPane;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.lang.NumberFormatException;
import java.awt.event.InputMethodEvent;
import java.text.ParseException;
import java.awt.event.ActionListener;

import atp.MainFrame;
import atp.view.interfaces.ScheduleDialog;
import atp.view.interfaces.ScheduleFrame;
import atp.view.interfaces.ScheduleWindow;
import atp.view.models.ATPTableModel;

import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Vector;

import atp.view.ATPListPanel;
import atp.data.*;
import atp.view.helpers.*;
import atp.view.renderer.*;
/**
 * 
 * @author Stephan
 *	@version 
 */
public class ImporteBearbeitenDialog extends JDialog {
	
	private final ScheduleFrame parent;
	private final ImporteBearbeitenDialog thisDialog = this;
	
	private ATPListPanel jPanelRäume = null;
	private ATPListPanel jPanelVorlesungen = null;
	private ATPListPanel jPanelDozenten = null;
	private ATPListPanel jPanelCourseOfStudies = null;
	private XJFormattedTextField tfDozentenVorname, tfDozentenNachname, tfDozentenAnzeigename, tfDozentenTitel;
	private XJFormattedTextField tfRaumGebäude,tfRaumName,tfRaumID; 
	private XJFormattedTextField tfCOSName, tfCOSAbschluss;	
	private XJFormattedTextField ftfRaumInternalID, tfDozentenID;
	private XJFormattedTextField tfVorlesungKurzTitel1, tfVorlesungKurzTitel2, tfVorlesungFachgruppe, tfVorlesungKennung,tfVorlesungTitel;
	private JSpinner sRaumFläche, sCOSSemester, sCOSZug;
	private JComboBox cbFachbereich, cbStatus;	
	private JButton dozentNeu, dozentÄndern, dozentLöschen;
	private JButton raumNeu, raumÄndern, raumLöschen;
	private JButton vorlesungNeu, vorlesungÄndern, vorlesungLöschen;
	private JButton cosNeu, cosÄndern, cosLöschen;
	private JButton[] jbPrefPeriod;
	private JButton jbSuspPeriod, jbDeletePeriod;
	private WSZeitenTable tableWSZeiten;
	
	protected static final Random random = new Random(new GregorianCalendar().getTimeInMillis());
	
	
	/**
	 * Überschreibt die alte Methode und updated zusätzlich die Listen der TabedPanels
	 */
	public void setVisible(boolean visible){
		if(visible == true){
			update();
		}
		super.setVisible(visible);
	}

	private void update(){
		jPanelDozenten.getList().setListData(parent.getSchedule().getLecturerVector());	
		jPanelVorlesungen.getList().setListData(parent.getSchedule().getLectureVector());	
		jPanelRäume.getList().setListData(parent.getSchedule().getRoomVector());
		cbFachbereich.removeAllItems();
		String[] fachbereiche = parent.getSchedule().getFachbereiche(); 
		for(int i = 0; i < fachbereiche.length; i++)
			cbFachbereich.addItem(fachbereiche[i]);
	}

	/**
	 * 
	 * @param parent.getSchedule() setzt den Stundenplan, zu dem das zu erschaffende Objekt gehört
	 */
	public ImporteBearbeitenDialog(ScheduleFrame parent) {
		super((Frame)parent, true);
		this.parent = parent;
		this.setTitle("Importe Bearbeiten");
		this.setSize(1000, 700);
		
		JTabbedPane jtp = new JTabbedPane();
		initJPanelDozenten();
		jtp.addTab("Dozenten", null, jPanelDozenten, "Dozentenliste bearbeiten");
		initJPanelRäume();
		jtp.addTab("Räume", null, jPanelRäume, "Raumliste bearbeiten");
		initJPanelVorlesungen();
		jtp.addTab("Vorlesungen", null, jPanelVorlesungen, "Vorlesungsliste bearbeiten");	
		this.setContentPane(jtp);
		this.addWindowListener(new IBWindowListener());
		this.disableAllTextFields();
	}
	/**
	 * Belegt ein Panel mit einem GridBagLayout und legt darauf Komponenten an, wobei
	 * die ersten zwei aufeinanderfolgenden hintereinander gelegt und nächsten zwei
	 * in der nächsten Reihe hintereinandergelegt werden.
	 * (Zum Erstellen von Text-Labels mit darauffolgenden Texteingabefeldern.) 
	 * @param inPanel dieses Panel wird bearbeitet
	 * @param compInsert diese Komponenten werden eingefügt
	 */
	private void gridbagLayout(JPanel inPanel, JComponent[] compInsert){
		GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        inPanel.setLayout(gridbag);
        
        c.ipadx = 0;
        c.ipady = 0;
        c.anchor = GridBagConstraints.WEST;
      
        for(int i = 0; i < compInsert.length; i++){
	        c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
	        c.fill = GridBagConstraints.NONE;      //reset to default
	        c.weightx = 0.0;                       //reset to default
	        inPanel.add(compInsert[i],c);
	        i++;

	        c.gridwidth = GridBagConstraints.REMAINDER;     //end row
	        c.fill = GridBagConstraints.HORIZONTAL;
	        c.weightx = 1.0;
	        inPanel.add(compInsert[i],c);
        }
	}

	
	/**
	 * Initialisiert das Dozentenpanel
	 */
	private void initJPanelDozenten() {
		jPanelDozenten = new ATPListPanel(new LecturerRenderer(), new ListListener());
		JPanel jPanelDozentenEingabe;
		
		///Eingabemöglichkeiten
		/////////Formatierung 
		MaskFormatter formatterID = null;
    	MaskFormatter formatterNachname = null;
    	MaskFormatter formatterAnzeigename = null;
    	MaskFormatter formatterVorname = null;
    	MaskFormatter formatterTitel = null;
		try {
			formatterID = new MaskFormatter("#####");
			formatterNachname = new MaskFormatter("********************");
		   	formatterAnzeigename = new MaskFormatter("************");
		   	formatterVorname = new MaskFormatter("***************");
		   	formatterTitel = new MaskFormatter("************");       
		} catch (java.text.ParseException exc) {
		    System.err.println("formatter is bad: " + exc.getMessage());
		    System.exit(-1);
		}
		    ////////////////////////////////////////
			tfDozentenID = new XJFormattedTextField(formatterID); 
			tfDozentenID.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
			tfDozentenAnzeigename = new XJFormattedTextField(formatterAnzeigename);
			tfDozentenNachname = new XJFormattedTextField(formatterNachname);
			tfDozentenVorname = new XJFormattedTextField(formatterVorname);
			tfDozentenTitel = new XJFormattedTextField(formatterTitel);
			cbStatus = new JComboBox();
			cbFachbereich = new JComboBox();
			
			tfDozentenNachname.setColumns(2);
			
			JComponent[] c = {new JLabel("ID-Nummer: "),tfDozentenID, 
					new JLabel("Anzeige: "), tfDozentenAnzeigename, 
					new JLabel("Name: "), tfDozentenNachname, 
					new JLabel("Vorname: "),tfDozentenVorname, 
					new JLabel("Titel: "), tfDozentenTitel,
					new JLabel("Status: "), cbStatus,
					new JLabel("Fachbereich: "), cbFachbereich
			};
			
			
			
			
			cbStatus.addItem(new ListTupel(1,"Lehrbeauftragter"));
			cbStatus.addItem(new ListTupel(2,"Angestellter"));
			
			cbFachbereich.addItem(new ListTupel("Inf","Informatik"));
			
			dozentNeu = new JButton("Neuen Dozent anlegen");
			dozentÄndern = new JButton("Dozent ändern");
			dozentLöschen = new JButton("Dozent löschen");
			
			ButtonDozentenEingabeActionListener buttonDozentenActionListener =
				new ButtonDozentenEingabeActionListener();
			dozentNeu.addActionListener(buttonDozentenActionListener);
			dozentÄndern.addActionListener(buttonDozentenActionListener);
			dozentLöschen.addActionListener(buttonDozentenActionListener);
			dozentNeu.setActionCommand("dozentAnlegen");
			dozentÄndern.setActionCommand("dozentÄndern");
			dozentLöschen.setActionCommand("dozentLöschen");
			
			
			
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.CENTER;
			gbc.fill = GridBagConstraints.BOTH;
			
			GridBagLayout gbl = new GridBagLayout();
			JPanel jpButtonEingabeInside = new JPanel(gbl);
			gbc.gridwidth = GridBagConstraints.REMAINDER;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			jpButtonEingabeInside.add(dozentÄndern, gbc);
			gbc.gridwidth = GridBagConstraints.RELATIVE;
			jpButtonEingabeInside.add(dozentNeu, gbc);
			jpButtonEingabeInside.add(dozentLöschen, gbc);
			GridBagConstraints constraints = new GridBagConstraints();
			
			JPanel jpButtonEingabeOutside = new JPanel(new BorderLayout());
			jpButtonEingabeOutside.add(jpButtonEingabeInside, BorderLayout.NORTH);
			
			    
			
			jPanelDozentenEingabe = new JPanel(new BorderLayout());
			JPanel eingabeNorth = new JPanel(new GridBagLayout());
			JPanel eingabeNorthPerson = new JPanel();
					
			eingabeNorth.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			eingabeNorthPerson.setBorder(BorderFactory.createEmptyBorder(0,0,0,10));
			
			constraints.ipadx = constraints.ipady = 100;
			constraints.anchor = GridBagConstraints.CENTER;    
			constraints.fill = GridBagConstraints.BOTH;                          
			constraints.gridwidth = GridBagConstraints.RELATIVE;     
			constraints.weighty = constraints.weightx = 0.4;
			eingabeNorth.add(eingabeNorthPerson, constraints);
			
			jPanelDozentenEingabe.add(eingabeNorth, BorderLayout.NORTH);
			gridbagLayout(eingabeNorthPerson, c);
			
			constraints.ipadx = constraints.ipady = 0;
			constraints.anchor = GridBagConstraints.CENTER;    
			constraints.fill = GridBagConstraints.BOTH;                          
			constraints.gridwidth = GridBagConstraints.REMAINDER;     
			constraints.weighty = constraints.weightx = 1.0;
			eingabeNorthPerson.add(jpButtonEingabeOutside, constraints);
		
		
			////////// Wunsch- / Sperrzeiten- Table
			tableWSZeiten = new WSZeitenTable();		
			JScrollPane tablePanel = new JScrollPane(tableWSZeiten);
			jPanelDozentenEingabe.add(tablePanel, BorderLayout.CENTER);
			
			
			GridBagLayout gblPeriod = new GridBagLayout();
			GridBagConstraints gbcPeriod = new GridBagConstraints();
			JPanel jpPeriodButton = new JPanel(gblPeriod);
			//gbcPeriod.gridwidth = GridBagConstraints.RELATIVE;
			gbcPeriod.fill = GridBagConstraints.HORIZONTAL;
			gbcPeriod.anchor = GridBagConstraints.WEST;
			//gbcPeriod.weighty = 
			gbcPeriod.weightx = 1.0;
			
			jbPrefPeriod = new JButton[Lecturer.MAX_PREF_TIME_LEVELS];
			WSPeriodButtonActionListener periodButtonListener = new WSPeriodButtonActionListener();
			for(int i = 0; i < jbPrefPeriod.length; i++){
				jbPrefPeriod[i] = new JButton("Präferenzzeit " + (i + 1));
				jbPrefPeriod[i].setActionCommand("Wunschzeit " + i);
				jbPrefPeriod[i].addActionListener(periodButtonListener);
				if(i == jbPrefPeriod.length - 1) 
					gbcPeriod.gridwidth = GridBagConstraints.REMAINDER;
				jpPeriodButton.add(jbPrefPeriod[i], gbcPeriod);
			}
			
			JPanel jpPeriodButtonDown = new JPanel(gblPeriod);
			GridBagLayout gblPeriodButtonDown = new GridBagLayout();
		
			gbcPeriod.gridwidth = GridBagConstraints.REMAINDER;
			jpPeriodButton.add(jpPeriodButtonDown, gbcPeriod);
		
			gbcPeriod.gridwidth = GridBagConstraints.RELATIVE;
			jbSuspPeriod = new JButton("Sperrzeit");
			jbSuspPeriod.setActionCommand("Sperrzeit");
			jbSuspPeriod.addActionListener(periodButtonListener);	
			jpPeriodButtonDown.add(jbSuspPeriod, gbcPeriod);
			jbDeletePeriod = new JButton("Löschen");
			jbDeletePeriod.setActionCommand("Periode löschen");
			jbDeletePeriod.addActionListener(periodButtonListener);
			jpPeriodButtonDown.add(jbDeletePeriod, gbcPeriod);
			
			jPanelDozentenEingabe.add(jpPeriodButton, BorderLayout.SOUTH);
			jPanelDozenten.setRechtesPanel(jPanelDozentenEingabe);
			
	}
	/**
	 * Initialisiert das Raumpanel
	 */
	private void initJPanelRäume(){
		jPanelRäume = new ATPListPanel(new RoomRenderer(), new ListListener());
		
		//////Eingabeteil rechts
		/////////Formatierung 
		MaskFormatter formatterInterneID = null;
    	MaskFormatter formatterGebäude = null;
    	MaskFormatter formatterRaumnummer = null;
    	MaskFormatter formatterBezeichnung = null;
		try {
			formatterInterneID = new MaskFormatter("####");	///?? "AAAA" ??
			formatterGebäude = new MaskFormatter("AAA");
			formatterRaumnummer = new MaskFormatter("AAAA");
			formatterBezeichnung = new MaskFormatter("*****************************");
		} catch (java.text.ParseException exc) {
		    System.err.println("formatter is bad: " + exc.getMessage());
		    System.exit(-1);
		}
		
		JPanel jPanelRäumeEingabeNorth = new JPanel();
		tfRaumName = new XJFormattedTextField(formatterBezeichnung);
		ftfRaumInternalID = new XJFormattedTextField(formatterInterneID);
		tfRaumID = new XJFormattedTextField(formatterRaumnummer);
		tfRaumGebäude = new XJFormattedTextField(formatterGebäude);
		sRaumFläche = new JSpinner( new SpinnerNumberModel(0, 0, 9999, 1));
		JPanel jpSpinnerFläche = new JPanel(new BorderLayout());	//Damit sich der Spinner
		jpSpinnerFläche.add(sRaumFläche, BorderLayout.WEST);	//nicht von ganz links 
		JComponent[] c = {new JLabel("Bezeichnung: "), tfRaumName, 	//nach ganz rechts erstreckt.
				new JLabel("Raum-ID: "), tfRaumID, 
				new JLabel("Gebäude: "), tfRaumGebäude,
				new JLabel("Interne ID: "), ftfRaumInternalID,
				new JLabel("Fläche: "),jpSpinnerFläche};
	
		gridbagLayout(jPanelRäumeEingabeNorth,  c);
		
		JPanel jPanelRäumeEingabe = new JPanel(new BorderLayout());
		jPanelRäumeEingabe.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		raumNeu = new JButton("Neuen Raum anlegen");
		raumÄndern = new JButton("Raum ändern");
		raumLöschen = new JButton("Raum löschen");
		
		ButtonRaumEingabeActionListener buttonRaumActionListener =
			new ButtonRaumEingabeActionListener();
		raumNeu.addActionListener(buttonRaumActionListener);
		raumÄndern.addActionListener(buttonRaumActionListener);
		raumLöschen.addActionListener(buttonRaumActionListener);
		raumNeu.setActionCommand("raumAnlegen");
		raumÄndern.setActionCommand("raumÄndern");
		raumLöschen.setActionCommand("raumLöschen");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		
		GridBagLayout gbl = new GridBagLayout();		
		JPanel jpButtonEingabePageEnd = new JPanel(gbl);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		jpButtonEingabePageEnd.add(raumÄndern, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		jpButtonEingabePageEnd.add(raumNeu, gbc);	
		jpButtonEingabePageEnd.add(raumLöschen, gbc);
		GridBagConstraints constraints = new GridBagConstraints();
			        
		constraints.ipadx = constraints.ipady = 20;
		constraints.anchor = GridBagConstraints.CENTER;
	      
	      
		constraints.fill = GridBagConstraints.NONE;                          
		constraints.gridwidth = GridBagConstraints.SOUTH;     
		constraints.weightx = 1.0;
		        
		jPanelRäumeEingabeNorth.add(jpButtonEingabePageEnd, constraints);

		////Split für Raumliste links und Eingabe rechts
		
		JPanel eingabeNorthWest = new JPanel(new BorderLayout());
		eingabeNorthWest.add(jPanelRäumeEingabeNorth, BorderLayout.CENTER);
		jPanelRäumeEingabe.add(eingabeNorthWest, BorderLayout.NORTH);
		
		jPanelRäume.setRechtesPanel(jPanelRäumeEingabe);
	}
	/**
	 * Initialisiert das Vorlesungspanel
	 *
	 */
	private void initJPanelVorlesungen(){
		jPanelVorlesungen = new ATPListPanel(new LectureRenderer(), new ListListener());

		
		//////Eingabeteil rechts
		/////////Formatierung 
    	MaskFormatter formatterKennung = null;
    	MaskFormatter formatterTitel = null;
    	MaskFormatter formatterKurzTitel1 = null;
    	MaskFormatter formatterKurzTitel2 = null;
    	MaskFormatter formatterFachgruppe = null;
		try {
			formatterKennung = new MaskFormatter("******");
			formatterTitel = new MaskFormatter("**********************************");
			formatterKurzTitel1 = new MaskFormatter("**********");
			formatterKurzTitel2 = new MaskFormatter("**********");
			formatterFachgruppe = new MaskFormatter("**********************");
		} catch (java.text.ParseException exc) {
		    System.err.println("formatter is bad: " + exc.getMessage());
		    System.exit(-1);
		}
		
		JPanel jPanelVorlesungenEingabeNorth = new JPanel();
		tfVorlesungKennung = new XJFormattedTextField(formatterKennung);
		tfVorlesungTitel = new XJFormattedTextField(formatterTitel);
		tfVorlesungKurzTitel1 = new XJFormattedTextField(formatterKurzTitel1);
		tfVorlesungKurzTitel2 = new XJFormattedTextField(formatterKurzTitel2);
		tfVorlesungFachgruppe = new XJFormattedTextField(formatterFachgruppe);
		JComponent[] c = {
				new JLabel("Kennung: "), tfVorlesungKennung,
				new JLabel("Titel: "), tfVorlesungTitel,
				new JLabel("Kurztitel 1: "), tfVorlesungKurzTitel1,
				new JLabel("Kurztitel 2: "), tfVorlesungKurzTitel2,
				new JLabel("Fachgruppe: "), tfVorlesungFachgruppe};
	
		gridbagLayout(jPanelVorlesungenEingabeNorth,  c);
		
		JPanel jPanelVorlesungenEingabe = new JPanel(new BorderLayout());
		jPanelVorlesungenEingabe.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		vorlesungNeu = new JButton("Neue Vorlesung anlegen");
		vorlesungÄndern = new JButton("Vorlesung ändern");
		vorlesungLöschen = new JButton("Vorlesung löschen");
		
		ButtonVorlesungEingabeActionListener buttonVorlesungEingabeActionListener = 
			new ButtonVorlesungEingabeActionListener();
		vorlesungNeu.addActionListener(buttonVorlesungEingabeActionListener);
		vorlesungÄndern.addActionListener(buttonVorlesungEingabeActionListener);
		vorlesungLöschen.addActionListener(buttonVorlesungEingabeActionListener);
		vorlesungNeu.setActionCommand("vorlesungAnlegen");
		vorlesungÄndern.setActionCommand("vorlesungÄndern");
		vorlesungLöschen.setActionCommand("vorlesungLöschen");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		
		GridBagLayout gbl = new GridBagLayout();
		JPanel jpButtonEingabePageEnd = new JPanel(gbl);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		jpButtonEingabePageEnd.add(vorlesungÄndern, gbc);
		gbc.gridwidth = GridBagConstraints.RELATIVE;
		jpButtonEingabePageEnd.add(vorlesungNeu, gbc);	
		jpButtonEingabePageEnd.add(vorlesungLöschen, gbc);
		
		GridBagConstraints constraints = new GridBagConstraints();	        
		constraints.ipadx = constraints.ipady = 20;
		constraints.anchor = GridBagConstraints.CENTER;	      
		constraints.fill = GridBagConstraints.NONE;                          
		constraints.gridwidth = GridBagConstraints.SOUTH;     
		constraints.weightx = 1.0;
		
		jPanelVorlesungenEingabeNorth.add(jpButtonEingabePageEnd, constraints);
		
		////Split für Vorlesungenliste links und Eingabe rechts
		
		jPanelVorlesungenEingabe.add(jPanelVorlesungenEingabeNorth, BorderLayout.NORTH);
		
		
		
		jPanelVorlesungen.setRechtesPanel(jPanelVorlesungenEingabe);	//add(splitAuswahlUndEingabePaneVorlesungen, BorderLayout.CENTER);
		
	}
	
	/**
	 * Initialisiert das CourseOfStudies- Panel.
	 *
	 */
	private void initJPanelCourseOfStudies(){
		jPanelCourseOfStudies =  new ATPListPanel(new LectureRenderer(), new ListListener());
		//////////////////////Ostseite
		/////////Formatierung 
    	MaskFormatter formatterName = null;
    	MaskFormatter formatterSemester = null;
    	MaskFormatter formatterAbschluss = null;
    	MaskFormatter formatterZug = null;
		try {
			formatterName = new MaskFormatter("********************");	//TODO unsicher
			//formatterSemester = new MaskFormatter("##");	
			formatterAbschluss = new MaskFormatter("?");
			//formatterZug = new MaskFormatter("##");	//TODO unsicher
		} catch (java.text.ParseException exc) {
		    System.err.println("formatter is bad: " + exc.getMessage());
		    System.exit(-1);
		}
		JPanel jPanelCourseOfStudiesEast = new JPanel(new BorderLayout());
		tfCOSName = new XJFormattedTextField(formatterName); 
		//tfCOSSemester = new JFormattedTextField(formatterSemester); 
		tfCOSAbschluss = new XJFormattedTextField(formatterAbschluss); 
		//tfCOSZug = new JFormattedTextField(formatterZug);
		
		sCOSSemester = new JSpinner( new SpinnerNumberModel(0, 0, 99, 1));
		JPanel jpSpinnerSemester = new JPanel(new BorderLayout());	//Damit sich der Spinner
		jpSpinnerSemester.add(sCOSSemester, BorderLayout.WEST);	//nicht von ganz links nach ganz rechts erstreckt
		sCOSZug = new JSpinner( new SpinnerNumberModel(0, 0, 99, 1));
		JPanel jpSpinnerZug = new JPanel(new BorderLayout());	//Damit sich der Spinner
		jpSpinnerZug.add(sCOSZug, BorderLayout.WEST);	//nicht von ganz links nach ganz rechts erstreckt
		
		
		JComponent[] c = {new JLabel("Name: "), tfCOSName,
				new JLabel("Abschluss: "), tfCOSAbschluss,
				new JLabel("Semester: "), jpSpinnerSemester,
				new JLabel("Zug: "), jpSpinnerZug, 
				};
		JPanel jPanelCOSEastNorth = new JPanel();
		gridbagLayout(jPanelCOSEastNorth,  c);
		
		jPanelCOSEastNorth.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		//jPanelCourseOfStudiesWest.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		cosNeu = new JButton("Neuen Studiengang anlegen");
		cosÄndern = new JButton("Studiengang ändern");
		cosLöschen = new JButton("Studiengang löschen");
		
		ButtonCOSEingabeActionListener buttonCOSEingabeActionListener = 
			new ButtonCOSEingabeActionListener();
		cosNeu.addActionListener(buttonCOSEingabeActionListener);
		cosÄndern.addActionListener(buttonCOSEingabeActionListener);
		cosLöschen.addActionListener(buttonCOSEingabeActionListener);
		cosNeu.setActionCommand("cosAnlegen");
		cosÄndern.setActionCommand("cosÄndern");
		cosLöschen.setActionCommand("cosLöschen");
		
		GridLayout glBEPageEnd = new GridLayout();
		glBEPageEnd.setHgap(15);
		glBEPageEnd.setColumns(3);
		glBEPageEnd.setRows(1);
		JPanel jpButtonEingabePageEnd = new JPanel(glBEPageEnd);
		jpButtonEingabePageEnd.add(cosNeu);
		jpButtonEingabePageEnd.add(cosÄndern);
		jpButtonEingabePageEnd.add(cosLöschen);
		
		GridBagConstraints constraints = new GridBagConstraints();	        
		constraints.ipadx = constraints.ipady = 20;
		constraints.anchor = GridBagConstraints.CENTER;	      
		constraints.fill = GridBagConstraints.NONE;                          
		constraints.gridwidth = GridBagConstraints.SOUTH;     
		constraints.weightx = 1.0;
		
		jPanelCOSEastNorth.add(jpButtonEingabePageEnd, constraints);
		
		
		jPanelCourseOfStudiesEast.add(jPanelCOSEastNorth, BorderLayout.NORTH);
		
		jPanelCourseOfStudies.setRechtesPanel(jPanelCourseOfStudiesEast);
		
		
	}
	
	/**
	 * Disabled alle TextFelder auf allen Panels.
	 *
	 */
	private void disableAllTextFields(){
		if(jPanelDozenten.getList().getSelectionModel().isSelectionEmpty()){
			tfDozentenVorname.setEnabled(false); tfDozentenNachname.setEnabled(false); tfDozentenAnzeigename.setEnabled(false); tfDozentenTitel.setEnabled(false);
			dozentÄndern.setEnabled(false); 
			dozentLöschen.setEnabled(false);
			for(int i = 0; i < jbPrefPeriod.length; i++)
				jbPrefPeriod[i].setEnabled(false);
			jbSuspPeriod.setEnabled(false); 
			jbDeletePeriod.setEnabled(false);
			cbFachbereich.setEnabled(false); cbStatus.setEnabled(false);
			tfDozentenID.setEnabled(false);
		}
		if(jPanelRäume.getList().getSelectionModel().isSelectionEmpty()){
			tfRaumGebäude.setEnabled(false); tfRaumName.setEnabled(false); tfRaumID.setEnabled(false);
			raumÄndern.setEnabled(false);
			raumLöschen.setEnabled(false);
			sRaumFläche.setEnabled(false);
			ftfRaumInternalID.setEnabled(false); 
		}
		if(jPanelVorlesungen.getList().getSelectionModel().isSelectionEmpty()){
			vorlesungÄndern.setEnabled(false);
			vorlesungLöschen.setEnabled(false);
			tfVorlesungKurzTitel1.setEnabled(false); tfVorlesungKurzTitel2.setEnabled(false); tfVorlesungFachgruppe.setEnabled(false); tfVorlesungKennung.setEnabled(false); tfVorlesungTitel.setEnabled(false);	
		}
	}
	
	/**
	 * Enabled alle TextFelder auf allen Panels.
	 *
	 */
	private void enableAllTextFields(){
		tfDozentenVorname.setEnabled(true); tfDozentenNachname.setEnabled(true); tfDozentenAnzeigename.setEnabled(true); tfDozentenTitel.setEnabled(true);
		tfRaumGebäude.setEnabled(true); tfRaumName.setEnabled(true); tfRaumID.setEnabled(true); 
		//tfCOSName.setEnabled(true); tfCOSAbschluss.setEnabled(true);	
		ftfRaumInternalID.setEnabled(true); tfDozentenID.setEnabled(true);
		tfVorlesungKurzTitel1.setEnabled(true); tfVorlesungKurzTitel2.setEnabled(true); tfVorlesungFachgruppe.setEnabled(true); tfVorlesungKennung.setEnabled(true); tfVorlesungTitel.setEnabled(true);
		sRaumFläche.setEnabled(true);// sCOSSemester.setEnabled(true); sCOSZug.setEnabled(true);
		cbFachbereich.setEnabled(true); cbStatus.setEnabled(true);
		dozentNeu.setEnabled(true); dozentÄndern.setEnabled(true); dozentLöschen.setEnabled(true);
		for(int i = 0; i < jbPrefPeriod.length; i++)
			jbPrefPeriod[i].setEnabled(true);
		jbSuspPeriod.setEnabled(true); 
		jbDeletePeriod.setEnabled(true);
	}
	
	/**
	 * Gibt die Lecturers zurück, die gerade in der WSZeiten- Table eingetragen sind.
	 * @return Gibt die Lecturers zurück, die gerade in der WSZeiten- Table eingetragen sind.
	 */
	protected Vector<Lecturer> getWSZeitenTableLecturers(){
		Object[] lecsObj = ((ATPTableModel)tableWSZeiten.getModel()).getRowObjects();
		if(lecsObj == null)
			return null;
		
		Vector<Lecturer> lecVec = new Vector<Lecturer>(lecsObj.length);
		for(int i = 0; i < lecsObj.length; i++)
			lecVec.addElement((Lecturer)lecsObj[i]);
		return lecVec;
	}
	
	/**
	 * Implementierung des ListSelectionListeners für die Hauptlisten auf den TabbedPanels.
	 * @author Stephan
	 *
	 */
	private class ListListener implements ListSelectionListener{
		public void valueChanged(ListSelectionEvent e){
			Object[] auswahl = ((JList)e.getSource()).getSelectedValues();
			for(int i = 0; i < auswahl.length; i++)
				auswahl[i] = auswahl[i];
			
				if(((JList)e.getSource()).equals(jPanelDozenten.getList())){
					tfDozentenID.setEnabled(true);
					tfDozentenVorname.setEnabled(true);
					tfDozentenNachname.setEnabled(true);
					tfDozentenAnzeigename.setEnabled(true);
					tfDozentenTitel.setEnabled(true);
					cbFachbereich.setEnabled(true); 
					cbStatus.setEnabled(true);
					dozentNeu.setEnabled(true); 
					dozentÄndern.setEnabled(true);
					dozentLöschen.setEnabled(true);
					for(int i = 0; i < jbPrefPeriod.length; i++)
						jbPrefPeriod[i].setEnabled(true);
					jbSuspPeriod.setEnabled(true); 
					jbDeletePeriod.setEnabled(true);
					
					tfDozentenID.setText("00000");
					if(auswahl.length > 1){
						tfDozentenID.setEnabled(false);
						tfDozentenID.setText("");
						tfDozentenVorname.setEnabled(false);
						tfDozentenVorname.setText("");
						tfDozentenNachname.setEnabled(false);
						tfDozentenNachname.setText("");
						tfDozentenAnzeigename.setEnabled(false);
						tfDozentenAnzeigename.setText("");
					}
					
					Lecturer[] auswahlLecs = new Lecturer[auswahl.length];
					for(int i = 0; i < auswahl.length; i++)
						auswahlLecs[i] = (Lecturer)auswahl[i];					
					((ATPTableModel)tableWSZeiten.getModel()).setRowObjects(auswahlLecs);
					tableWSZeiten.tableChanged(new TableModelEvent(tableWSZeiten.getModel()));
					tableWSZeiten.setExtendedPeriodData(parent.getSchedule().getLecturersWorkingTimes(getWSZeitenTableLecturers()), Color.ORANGE, "B");
					
					
					if(auswahl.length == 1){
						tfDozentenID.setText(((Lecturer)auswahl[0]).getId());
						tfDozentenVorname.setText(((Lecturer)auswahl[0]).getFirstName());
						tfDozentenNachname.setText(((Lecturer)auswahl[0]).getLastName());
						tfDozentenAnzeigename.setText(((Lecturer)auswahl[0]).getIndicatedName());
						tfDozentenTitel.setText(((Lecturer)auswahl[0]).getTitle());
						
						ListTupel lt = ListTupel.findInComboBox(cbStatus, ((Lecturer)auswahl[0]).getState());
						if(lt != null)
							cbStatus.setSelectedItem(lt);
						else
							cbStatus.setSelectedIndex(-1);
						
						String fachbereich = ((Lecturer)auswahl[0]).getDepartment();
						if(fachbereich != null)
							cbFachbereich.setSelectedItem(fachbereich);
						else
							cbFachbereich.setSelectedIndex(-1);
						
						//////WSZeiten laden:
						//wsZeitListenEinrichten((Lecturer)auswahl[0]);
					
					}
				}else if(((JList)e.getSource()).equals(jPanelRäume.getList())){
					tfRaumName.setEnabled(true);
					tfRaumID.setEnabled(true);
					ftfRaumInternalID.setEnabled(true);
					tfRaumGebäude.setEnabled(true);
					sRaumFläche.setEnabled(true);
					raumNeu.setEnabled(true); 
					raumÄndern.setEnabled(true);
					raumLöschen.setEnabled(true);
					
					if(auswahl.length > 1){
						tfRaumName.setEnabled(false);
						tfRaumName.setText("");
						tfRaumID.setEnabled(false);
						tfRaumID.setText("");
						ftfRaumInternalID.setEnabled(false);
						ftfRaumInternalID.setText("");
					}
					
					if(auswahl.length == 1){
						tfRaumName.setText(((Room)auswahl[0]).getName());
						tfRaumID.setText(((Room)auswahl[0]).getId());
						ftfRaumInternalID.setText(((Integer)((Room)auswahl[0]).getInternalID()).toString());
						tfRaumGebäude.setText(((Room)auswahl[0]).getBuilding());
						sRaumFläche.setValue(((Room)auswahl[0]).getArea());
					}
				}else if(((JList)e.getSource()).equals(jPanelVorlesungen.getList())){
					 tfVorlesungKurzTitel1.setEnabled(true);
					 tfVorlesungKurzTitel2.setEnabled(true); 
					 tfVorlesungFachgruppe.setEnabled(true);
					 tfVorlesungTitel.setEnabled(true);
					 tfVorlesungKennung.setEnabled(true);
					 vorlesungNeu.setEnabled(true); 
					 vorlesungÄndern.setEnabled(true);
					 vorlesungLöschen.setEnabled(true);
					 
					if(auswahl.length > 1){
						 tfVorlesungKurzTitel1.setEnabled(false);
						 tfVorlesungKurzTitel1.setText("");
						 tfVorlesungKurzTitel2.setEnabled(false);
						 tfVorlesungKurzTitel2.setText("");
						 tfVorlesungFachgruppe.setEnabled(false);
						 tfVorlesungFachgruppe.setText("");
						 tfVorlesungKennung.setEnabled(false);
						 tfVorlesungKennung.setText("");
						 tfVorlesungTitel.setEnabled(false);
						 tfVorlesungTitel.setText("");
					}
					
					if(auswahl.length == 1){
						tfVorlesungTitel.setText(((Lecture)auswahl[0]).getTitle());
						tfVorlesungKurzTitel1.setText(((Lecture)auswahl[0]).getShort1());
						tfVorlesungKurzTitel2.setText(((Lecture)auswahl[0]).getShort2());
						tfVorlesungFachgruppe.setText(((Lecture)auswahl[0]).getFachgruppe());
						tfVorlesungKennung.setText(((Lecture)auswahl[0]).getOriginalId());
					}
				}else if(((JList)e.getSource()).equals(jPanelCourseOfStudies.getList())){
					if(auswahl.length > 1){
						 tfCOSName.setEnabled(false);
						 tfCOSName.setText("");
						 //tfCOSAbschluss.setEnabled(false);
						 //tfCOSAbschluss.setText("")
					}else{
						tfCOSName.setEnabled(true);
					}
					
					if(auswahl.length == 1){
						tfCOSName.setText(((CourseOfStudies)auswahl[0]).getName());
						tfCOSAbschluss.setText(((CourseOfStudies)auswahl[0]).getAbschluss());
						sCOSSemester.setValue(((CourseOfStudies)auswahl[0]).getSemester());
						sCOSZug.setValue(((CourseOfStudies)auswahl[0]).getZug());
					}
				}
		}
	}
	/**
	 * Implementierung des ActionListeners für die Sucheingabenfelder über den
	 * Hauptlisten auf den TabbedPanels.
	 * @author Stephan 
	 */
	private class SuchTextFeldActionListener implements ActionListener{
		final private ATPList liste; 
		final private int textFeldNr;
		public void actionPerformed(ActionEvent e){
			if(textFeldNr == 0)
				liste.sucheDurchName(e.getActionCommand());
			else if(textFeldNr == 1)
				liste.sucheDurchNummer(e.getActionCommand());
			liste.repaint();
		 }
		 /**
		  * @param mitListeVerknüpft Gibt die Liste an, in der der Sucheintrag des Textfeldes gesucht werden soll.
		  * @param textFeldNr Gibt an, welches Suchfeld den Listener benutzt.
		  */
		 public SuchTextFeldActionListener(JList mitListeVerknüpft, int textFeldNr){
			 this.liste = (ATPList)mitListeVerknüpft;
			 this.textFeldNr = textFeldNr;
		 }
	}
	/**
	 * Erstellt ein neuen Dozenten un fügt ihn der Dozentenliste hinzu.
	 * @param id ID des Dozenten
	 * @param vorname Vorname des Dozenten
	 * @param nachname Nachname des Dozenten
	 * @param anzeigeName Anzeigename des Dozenten
	 * @param titel Titel des Dozenten
	 * @param fachbereich Fachbereich des Dozenten
	 * @param status Status des Dozenten (nur 1 oder 2 erlaubt)
	 */
	private void neuerDozent(String id, String vorname, String nachname
			, String anzeigeName, String titel, String fachbereich, int status){
		Lecturer neuLec = new Lecturer(
		vorname, 
		nachname, 
		anzeigeName, 
		id,
		titel,
		fachbereich,
		status
		);
		parent.getSchedule().getLecturerVector().addElement(neuLec);
		jPanelDozenten.getList().setListData(parent.getSchedule().getLecturerVector());
		jPanelDozenten.getList().setSelectedIndex(jPanelDozenten.getList().getModel().getSize()-1);
	}
	/**
	 * Erstellt einen neuen Raum und fügt ihn der Raumliste hinzu.
	 * @param id ID des Raumes
	 * @param name Name des Raumes
	 * @param building Name des Gebäudes, in dem sich der Raum befindet
	 * @param internalID interne ID des Raumes
	 * @param capacity Kapazität des Raumes
	 */
	private void neuerRaum(String id, String name, String building, int capacity){
		Room neuRoom = new Room();
		neuRoom.setId(id); 
		neuRoom.setBuilding(building);
		neuRoom.setArea(capacity);
		neuRoom.setInternalID(0);
		neuRoom.setName(name);
		
		parent.getSchedule().getRoomVector().addElement(neuRoom);
		jPanelRäume.getList().setListData(parent.getSchedule().getRoomVector());
		jPanelRäume.getList().setSelectedIndex(jPanelRäume.getList().getModel().getSize()-1);
	}
	/**
	 * Erstellt eine neue Vorlesung und fügt sie in die Vorlesungsliste ein.
	 * @param id ID der Vorlesung
	 * @param title Titel der Vorlesung
	 * @param indicatedName Name der Vorlesung
	 * @param originalID Original-ID der Vorlesung
	 * @param fachgruppe VorlesungFachgruppe
	 * @param short1 kurzer Kommentar für die Vorlesung 
	 * @param short2 noch ein kurzer Kommentar für die Vorlesung 
	 */
	private void neueVorlesung(String id, String title, String indicatedName, String  originalID,
			String fachgruppe, String short1, String short2){
		Lecture neuLec = new Lecture();
		neuLec.setShort1(short1);
		neuLec.setShort2(short2);
		neuLec.setOriginalId(originalID);
		neuLec.setFachgruppe(fachgruppe);
		neuLec.setTitle(title);
		
		parent.getSchedule().getLectureVector().addElement(neuLec);
		jPanelVorlesungen.getList().setListData(parent.getSchedule().getLectureVector());
		jPanelVorlesungen.getList().setSelectedIndex(jPanelVorlesungen.getList().getModel().getSize()-1);
	}
	/**
	 * Erstellt einen neuen CourseOfStudies.
	 * @param fachbereich der Fachbereich
	 * @param semester das Semester
	 * @param abschluss der Abschluss
	 * @param zug der Zug
	 */
	private void neuerCOS(String fachbereich, int semester, String abschluss, int zug){
		CourseOfStudies cos = new CourseOfStudies(fachbereich,  semester,  abschluss,  zug);
		
		parent.getSchedule().getCourseOfStudiesVector().addElement(cos);
		jPanelCourseOfStudies.getList().setListData(parent.getSchedule().getCourseOfStudiesVector());
		jPanelCourseOfStudies.getList().setSelectedIndex(jPanelCourseOfStudies.getList().getModel().getSize()-1);
	}
	/**
	 * Gibt zurück, ob schon ein Lecturer mit der selben UniqueID existiert, wie ein bestimmter anderer.
	 * @param lecturer dessen UniqueID soll auf Eindeutigkeit geprüft werden.
	 * @return true eindeutig
	 */
	protected boolean doesLecturerWithSameUniqueIDAlreadyExist(Lecturer lecturer){
		for(int i = 0; i < parent.getSchedule().getLecturerVector().size(); i++)
			if(parent.getSchedule().getLecturerVector().get(i) != lecturer
					&& parent.getSchedule().getLecturerVector().get(i).getUniqueID().equals(lecturer.getUniqueID()))
				return true;
		return false;
	}
	/**
	 * Gibt zurück, ob schon ein Lecture mit der selben UniqueID existiert, wie eine bestimmte andere.
	 * @param lecture dessen UniqueID soll auf Eindeutigkeit geprüft werden.
	 * @return true eindeutig
	 */
	protected boolean doesLectureWithSameUniqueIDAlreadyExist(Lecture lecture){
		for(int i = 0; i < parent.getSchedule().getLectureVector().size(); i++)
			if(parent.getSchedule().getLectureVector().get(i) != lecture
					&& parent.getSchedule().getLectureVector().get(i).getUniqueID().equals(lecture.getUniqueID()))
				return true;
		return false;
	}
	/**
	 * Implementiert den ActionListener für die oberen Buttons auf dem Dozentenpanel.
	 * @author Stephan
	 *
	 */
	private class ButtonDozentenEingabeActionListener implements ActionListener{
		 public void actionPerformed(ActionEvent e){
			 if(e.getActionCommand().equals("dozentAnlegen")){
				 Lecturer newLec = new Lecturer();
				 int id;
				 do{
					 id = (Math.abs(random.nextInt()) % 89999) + 10000;
					 newLec.setId(""+id);
				 }
				 while(doesLecturerWithSameUniqueIDAlreadyExist(newLec));
				 
				 neuerDozent(""+id,"Vorname","Nachname","AnzeigeName","keiner", "undefiniert", -1);					
			 }else  if(e.getActionCommand().equals("dozentÄndern")){
				 Object[] sel = jPanelDozenten.getList().getSelectedValues();
				 Lecturer[] lecs = new Lecturer[sel.length];
				 for(int i = 0; i < lecs.length; i ++)
					 lecs[i] = (Lecturer)sel[i];
				 int[] lecturerIndices = jPanelDozenten.getList().getSelectedIndices();
				 if(lecs.length == 1){
					 	//////prüfen, ob ID doppelt ist:
					 String idTextFeldInhalt = tfDozentenID.getText().trim();
					 if(!idTextFeldInhalt.equals("00000")&& !idTextFeldInhalt.equals("")){
						 Object oldID = lecs[0].getUniqueID();
						 lecs[0].setId(tfDozentenID.getText().trim());
						 if(doesLecturerWithSameUniqueIDAlreadyExist(lecs[0])){
							 lecs[0].setId((String)oldID);
							 tfDozentenID.setText((String)oldID);
								JOptionPane.showMessageDialog(thisDialog, "Die Dozenten-ID ist schon vergeben!\nKeine Änderungen vorgenommen!"
										, "Warnung", JOptionPane.WARNING_MESSAGE);
								return;
							}
					 }
						/////////////////////////////////
					 lecs[0].setFirstName(tfDozentenVorname.getText().trim()); 		
					 lecs[0].setLastName(tfDozentenNachname.getText().trim());  
					 lecs[0].setIndicatedName(tfDozentenAnzeigename.getText().trim()); 
					 if(!idTextFeldInhalt.equals("00000") && !idTextFeldInhalt.equals(""))
					 	lecs[0].setId(tfDozentenID.getText().trim());
					 lecs[0].setTitle(tfDozentenTitel.getText().trim());
					 String fachbereich = ((String)cbFachbereich.getSelectedItem());
					 if(fachbereich != null)
						 lecs[0].setDepartment(fachbereich);
					 ListTupel lt = ((ListTupel)cbStatus.getSelectedItem());
					 if(lt != null)
						 lecs[0].setState(Integer.valueOf(lt.getObject().toString())); 
					 jPanelDozenten.getList().repaint();
					 jPanelDozenten.getList().setSelectedIndices(lecturerIndices);
				 }
			 }else  if(e.getActionCommand().equals("dozentLöschen")){
				 Object[] lecturers = jPanelDozenten.getList().getSelectedValues();
				 int[] lecturerIndices = jPanelDozenten.getList().getSelectedIndices();
				 for(int i = 0; i<lecturers.length; i++){
					 parent.getSchedule().getLecturerVector().removeElement(lecturers[i]);
					 jPanelDozenten.getList().setListData(parent.getSchedule().getLecturerVector());
					 parent.getSchedule().getLecturerVector().removeElement(lecturers[i]);
				 }
				 if(lecturerIndices.length != 0)
					 if(lecturerIndices[0] < jPanelDozenten.getList().getModel().getSize())
						 jPanelDozenten.getList().setSelectedIndex(lecturerIndices[0]);
					 else
						 jPanelDozenten.getList().setSelectedIndex(jPanelDozenten.getList().getModel().getSize() - 1);
			 }
		 }
	}
	/**
	 * Testes, ob in der Schedule schon ein Raum mit der UniqueID des Anderen Raumes existiert.
	 * @param room Raum, dessen ID auf Einzigartigkeit in der Schedule geprüft wird.
	 * @return true = gibts schon; false = gibts noch nicht
	 */
	protected boolean doesRoomWithSameUniqueIDAlreadyExist(Room room){
		for(int i = 0; i < parent.getSchedule().getRoomVector().size(); i++) 
			if(parent.getSchedule().getRoomVector().get(i) != room
					&& parent.getSchedule().getRoomVector().get(i).getUniqueID().equals(room.getUniqueID()))
					return true;
		return false;
	}
	/**
	 * Implementiert den ActionListener für die Buttons auf dem Raumpanel.
	 * @author Stephan
	 *
	 */
	private class ButtonRaumEingabeActionListener implements ActionListener{
		 public void actionPerformed(ActionEvent e){
			 if(e.getActionCommand().equals("raumAnlegen")){
				Room newRoom = new Room("", "neuer Raum", "unbekanntes Gebäude",0);
				int idEnding = parent.getSchedule().getRoomVector().size();
				do{
					idEnding ++;
					newRoom.setId(""+idEnding);
				}
				while(doesRoomWithSameUniqueIDAlreadyExist(newRoom));
					
				 neuerRaum(""+idEnding, "neuer Raum", "unbekanntes Gebäude",0);
				
			 }else  if(e.getActionCommand().equals("raumÄndern")){
				 Object[] sel = jPanelRäume.getList().getSelectedValues();
				 Room[] rooms = new Room[sel.length];
				 for(int i = 0; i < sel.length; i++)
					 rooms[i] = (Room)sel[i];
				 int[] roomIndices = jPanelRäume.getList().getSelectedIndices();
				 if(rooms.length == 1){
					 Object oldID = rooms[0].getUniqueID();
					 rooms[0].setId(tfRaumID.getText().trim());
					 if(doesRoomWithSameUniqueIDAlreadyExist(rooms[0])){
						 rooms[0].setId((String)oldID);
						 tfRaumID.setText((String)oldID);
						 JOptionPane.showMessageDialog(thisDialog, "Die Raum-ID ist schon vergeben!\nKeine Änderungen vorgenommen!"
									, "Warnung", JOptionPane.WARNING_MESSAGE);
							return;
					 }
					 rooms[0].setBuilding(tfRaumGebäude.getText().trim()); 		
					 rooms[0].setArea((Integer)sRaumFläche.getValue());  
					 rooms[0].setId(tfRaumID.getText().trim()); 
					 rooms[0].setName(tfRaumName.getText().trim());
					int idVal;
					try{
						idVal = Integer.valueOf(ftfRaumInternalID.getText().trim());
						rooms[0].setInternalID(idVal);
					}
					catch(NumberFormatException exc){
						rooms[0].setInternalID(0);
					}
					 jPanelRäume.getList().repaint();
					 jPanelRäume.getList().setSelectedIndices(roomIndices);
				 }
			 
			 }else  if(e.getActionCommand().equals("raumLöschen")){
				 Object[] rooms = jPanelRäume.getList().getSelectedValues();
				 int[] roomIndices = jPanelRäume.getList().getSelectedIndices();
				 for(int i = 0; i<rooms.length; i++){
					 parent.getSchedule().getRoomVector().removeElement(rooms[i]);
					 jPanelRäume.getList().setListData(parent.getSchedule().getRoomVector());
					 parent.getSchedule().getRoomVector().removeElement(rooms[i]);
				 }
				 if(roomIndices.length != 0)
					 if(roomIndices[0] < jPanelRäume.getList().getModel().getSize())
						 jPanelRäume.getList().setSelectedIndex(roomIndices[0]);
					 else
						 jPanelRäume.getList().setSelectedIndex(jPanelRäume.getList().getModel().getSize() - 1);
			 }
		 }
	}
	/**
	 * Implementiert den ActionListener für die Buttons auf dem Vorlesungspanel.
	 * @author Stephan
	 *
	 */
	private class ButtonVorlesungEingabeActionListener implements ActionListener{
		 public void actionPerformed(ActionEvent e){
			 if(e.getActionCommand().equals("vorlesungAnlegen")){
				 Lecture newLec = new Lecture();
				 int id;
				 do{
					 id = (Math.abs(random.nextInt()) % 899999) + 100000;
					 newLec.setOriginalId(""+id);
				 }
				 while(doesLectureWithSameUniqueIDAlreadyExist(newLec));
				 
				 neueVorlesung(""+id, "neue Vorlesung","keiner" , ""+id,"???", "", "");	 
			 }else  if(e.getActionCommand().equals("vorlesungÄndern")){
				 Object[] sel = jPanelVorlesungen.getList().getSelectedValues();
				 Lecture[] lecs = new Lecture[sel.length];
				 for(int i = 0; i < sel.length; i++)
					 lecs[i] = (Lecture)sel[i];
				 int[] lectureIndices = jPanelVorlesungen.getList().getSelectedIndices();
				 if(lecs.length == 1){	
					 
					 String oldID = lecs[0].getOriginalId();	 
					 lecs[0].setOriginalId(tfVorlesungKennung.getText().trim());
					 if(!doesLectureWithSameUniqueIDAlreadyExist(lecs[0])){ 
						 lecs[0].setFachgruppe(tfVorlesungFachgruppe.getText().trim()); 
						 lecs[0].setShort1(tfVorlesungKurzTitel1.getText().trim());
						 lecs[0].setShort2(tfVorlesungKurzTitel2.getText().trim());
						 lecs[0].setTitle(tfVorlesungTitel.getText().trim());
						 
						 jPanelVorlesungen.getList().repaint();
						 jPanelVorlesungen.getList().setSelectedIndices(lectureIndices);
					 }
					 else{
						 lecs[0].setOriginalId(oldID);
						 tfVorlesungKennung.setText(oldID);
						 JOptionPane.showMessageDialog(thisDialog, "Die Vorlesungs-ID ist schon vergeben!\nKeine Änderungen vorgenommen!"
									, "Warnung", JOptionPane.WARNING_MESSAGE);
					 }
				 }			 
			 }else  if(e.getActionCommand().equals("vorlesungLöschen")){
				 Object[] lectures = jPanelVorlesungen.getList().getSelectedValues();
				 int[] lectureIndices = jPanelVorlesungen.getList().getSelectedIndices();
				 for(int i = 0; i<lectures.length; i++){
					 parent.getSchedule().getLectureVector().removeElement(lectures[i]);
					 jPanelVorlesungen.getList().setListData(parent.getSchedule().getLectureVector());
					 parent.getSchedule().getLectureVector().removeElement(lectures[i]);
				 }
				 if(lectureIndices.length != 0)
					 if(lectureIndices[0] < jPanelVorlesungen.getList().getModel().getSize())
						 jPanelVorlesungen.getList().setSelectedIndex(lectureIndices[0]);
					 else
						 jPanelVorlesungen.getList().setSelectedIndex(jPanelVorlesungen.getList().getModel().getSize() - 1);
			 }
		 }
	}
	
	/**
	 * Implementiert den ActionListener für die Buttons auf dem CourseOfStudies- Panel.
	 * @author Stephan
	 *
	 */
	private class ButtonCOSEingabeActionListener implements ActionListener{
		 public void actionPerformed(ActionEvent e){
			 if(e.getActionCommand().equals("cosAnlegen")){
				 neuerCOS("kein Fachbereich", 0,"keine Abschluss" , 0);
				 
			 }else  if(e.getActionCommand().equals("cosÄndern")){
				 Object[] sel = jPanelCourseOfStudies.getList().getSelectedValues();
				 CourseOfStudies[] coss = new CourseOfStudies[sel.length];
				 for(int i = 0; i < sel.length; i++)
					 coss[i] = (CourseOfStudies)sel[i];
				 int[] cosIndices = jPanelCourseOfStudies.getList().getSelectedIndices();
				 if(coss.length == 1){
					 coss[0].setName(tfCOSName.getText().trim()); 		
					 //coss[0].setSemester(Integer.valueOf(tfCOSSemester.getText()));  
					 coss[0].setSemester((Integer)sCOSSemester.getValue());
					 coss[0].setAbschluss(tfCOSAbschluss.getText().trim());
					 coss[0].setZug((Integer)sCOSZug.getValue()); 
					 jPanelCourseOfStudies.getList().repaint();
					 jPanelCourseOfStudies.getList().setSelectedIndices(cosIndices);
				 }			 
			 }else  if(e.getActionCommand().equals("cosLöschen")){
				 Object[] coss = jPanelCourseOfStudies.getList().getSelectedValues();
				 int[] cosIndices = jPanelCourseOfStudies.getList().getSelectedIndices();
				 for(int i = 0; i<coss.length; i++){
					 parent.getSchedule().getCourseOfStudiesVector().removeElement(coss[i]);
					 jPanelCourseOfStudies.getList().setListData(parent.getSchedule().getCourseOfStudiesVector());
					 parent.getSchedule().getCourseOfStudiesVector().removeElement(coss[i]);
				 }
				 if(cosIndices.length != 0)
					 if(cosIndices[0] < jPanelCourseOfStudies.getList().getModel().getSize())
						 jPanelCourseOfStudies.getList().setSelectedIndex(cosIndices[0]);
					 else
						 jPanelCourseOfStudies.getList().setSelectedIndex(jPanelCourseOfStudies.getList().getModel().getSize() - 1);
			 }
		 }	 
		 
	}

	/**
	 * Implementiert den ActionListener für die Tabellenbutton auf dem Dozentenpanel.
	 * @author Stephan
	 *
	 */
	private class WSPeriodButtonActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			 if(tableWSZeiten.getSelectedColumns().length != 0){
			 if(e.getActionCommand().startsWith("Wunschzeit")){
				 int level = Integer.valueOf(new String("" + e.getActionCommand().trim().charAt(e.getActionCommand().trim().length() - 1)));
				 for(int column = tableWSZeiten.getSelectedColumns()[0]; column <= tableWSZeiten.getSelectedColumns()[tableWSZeiten.getSelectedColumns().length - 1]; column++)	
				 	tableWSZeiten.setValueAt(level, tableWSZeiten.getSelectedRow(), column);
				 tableWSZeiten.valueChanged(new ListSelectionEvent(tableWSZeiten, tableWSZeiten.getSelectedRow(), tableWSZeiten.getSelectedRow(), false));
			 }
			 else if(e.getActionCommand().equals("Sperrzeit")){
				 for(int column = tableWSZeiten.getSelectedColumns()[0]; column <= tableWSZeiten.getSelectedColumns()[tableWSZeiten.getSelectedColumns().length - 1]; column++)	
					 tableWSZeiten.setValueAt(-1, tableWSZeiten.getSelectedRow(), column);
				 tableWSZeiten.valueChanged(new ListSelectionEvent(tableWSZeiten, tableWSZeiten.getSelectedRow(), tableWSZeiten.getSelectedRow(), false));
			 }
			 else if(e.getActionCommand().equals("Periode löschen")){
				 for(int column = tableWSZeiten.getSelectedColumns()[0]; column <= tableWSZeiten.getSelectedColumns()[tableWSZeiten.getSelectedColumns().length - 1]; column++)	
					 tableWSZeiten.setValueAt(null, tableWSZeiten.getSelectedRow(), column);
				 tableWSZeiten.valueChanged(new ListSelectionEvent(tableWSZeiten, tableWSZeiten.getSelectedRow(), tableWSZeiten.getSelectedRow(), false));
			 }
		}
		}
	}
	
	/**
	 * WindowListener, der bei Fensteröffnung alle Textfelder disabled und
	 * bei Schließung den Stundenplan auf Integrität prüft und updatet. 
	 * @author Stephan
	 *
	 */
	private class IBWindowListener extends WindowAdapter{
		 public void windowClosing(WindowEvent e){	 
			 thisDialog.dispose();
			 ((MainFrame)parent).killInteferringOccsWithMessage();
			 parent.update();		
		 }
		 public void windowOpened(WindowEvent e){
			 disableAllTextFields();
		 }
		 public void windowActivated(WindowEvent e){
			 disableAllTextFields();
		 }
	}
	
	/**
	 * Diese Textfelder reagieren bei Fokusverlust mit einer Eingabebestätigung.
	 * @author Stephan
	 *
	 */
	protected class XJFormattedTextField extends JFormattedTextField{
		public XJFormattedTextField(MaskFormatter mf){
			super(mf);
			this.setFocusLostBehavior(JFormattedTextField.COMMIT);
		}
	}
}
	



