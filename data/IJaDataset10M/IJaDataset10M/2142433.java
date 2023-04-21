package Dialogue;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import lecture_ecriture.ReadFile;
import zip.OutilsZip;
import Utilitaires.GestionRepertoire;
import Utilitaires.Historique;
import Utilitaires.RecupDate;
import Utilitaires.VariableEnvironement;
import accesBDD.GestionDemandes;

public class Fen_VerifMaj extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButton = null;
	private JLabel jLabel = null;
	private JLabel operation_jLabel = null;
	private JProgressBar jProgressBar = null;
	private JLabel jLabel1 = null;
	/**
	 * This is the default constructor
	 */
	public Fen_VerifMaj() {
		super();
		initialize();
		final String urlversion = "http://downloads.sourceforge.net/project/autobackupbysim/setup/version.ini";
		
		
	/*	Thread_VerifMajEnLigne majOnline = new Thread_VerifMajEnLigne (urlversion,urlsetup,jProgressBar,operation_jLabel,jLabel1);
		majOnline.start();*/
		
		
		InputStream input = null;
		FileOutputStream writeFile=null;	
		String cheminFichier=null;
		File fichier = null;
		int erreurOuverture=0;
		jLabel1.setText(" Vérification de la présence d'une mise à jour");
		File setup = new File (GestionRepertoire.RecupRepTravail()+"\\setup_AutoBackup.exe");
		if (setup.exists()==true){
			boolean effacé = setup.delete();
			if (effacé==false){
				setup.deleteOnExit();
			}
		}
		
		
			try
			{
				jLabel1.setText(" Récupération de la version disponible sur le site");
				URL url = new URL(urlversion);
				URLConnection connection = url.openConnection();
				final int fileLength = connection.getContentLength();

				if ((fileLength == -1)||(fileLength==0))
				{
					System.out.println("Invalide URL or file.");
					erreurOuverture++;
					//return false;
				}
				
				input = connection.getInputStream();
				String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);
				if (fileName.contains("%20")==true){
					fileName=fileName.replaceAll("%20", " ");
				}
				if (fileName.contains("&amp;")==true){
					fileName=fileName.replaceAll("&amp;", " and ");
				}
				cheminFichier=GestionRepertoire.RecupRepTravail()+"\\"+fileName;
				
				fichier = new File (cheminFichier);
				writeFile = new FileOutputStream(cheminFichier);
				//lecture par segment de 4Mo
				byte[] buffer = new byte[4096*1024];
				int read;

				while ((read = input.read(buffer)) > 0){
					writeFile.write(buffer, 0, read);
													
				}
					
				
				writeFile.flush();
			}
			catch (IOException e)
			{
				System.out.println("Error while trying to download the file.");
				e.printStackTrace();
				
			}
			finally
			{
				try
				{
					if (erreurOuverture==0){
						writeFile.close();
						input.close();	
					}
					
				}
				catch (IOException e)
				{
					e.printStackTrace();
					//return false;
				}
			}
			//le telech est fini, on verifie la version hebergée sur le site
			ini_Manager.ConfigMgt versionSite = null;
			try {
				versionSite = new ini_Manager.ConfigMgt ("version.ini",GestionRepertoire.RecupRepTravail()+"\\",'[');
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String VersionDispo =versionSite.getValeurDe("version");
			ini_Manager.ConfigMgt versionInstallée = null;
			try {
				versionInstallée = new ini_Manager.ConfigMgt ("version.ini",GestionRepertoire.RecupRepTravail()+"\\IniFile\\",'[');
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			String VersionEnCours = versionInstallée.getValeurDe("version");
			boolean effacé = fichier.delete();
			if (effacé==false){
				fichier.deleteOnExit();
			}
			
			jLabel1.setText(" Comparaison avec la version actuelle");
			
			if (VersionEnCours.equals(VersionDispo)==true){//la version est a jour, on regarde si on vient de faire une maj, il y a des fichier dans le repertoire "export
				try {
					jLabel1.setText(" Vérification de la présence de fichier à importer");
					ImportSQLDansBDD (GestionRepertoire.RecupRepTravail()+"\\export\\",jLabel1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				File Resultat = new File (GestionRepertoire.RecupRepTravail()+"/IniFile/Resultat.txt");
				if (Resultat.exists()){
					String Result = ReadFile.ReadLine (Resultat);
					if (Result!=null){
						JOptionPane.showMessageDialog(null, Result,
										"Pour votre information:", JOptionPane.INFORMATION_MESSAGE);
						boolean succes = Resultat.delete();
						if (!succes){
							Resultat.deleteOnExit();
						}
						
					}
				}
				this.dispose();
			}
			
			if (VersionEnCours.equals(VersionDispo)==false){//il y a une autre version sur le site de maj
															//proposition de la maj a l'utilisateur
				int demandeMaj = JOptionPane.showConfirmDialog(null, 
						"La version "+VersionDispo+" est disponible\n" +
				" Voulez-vous faire la mise à jour ?","Question",JOptionPane.YES_NO_OPTION);		//si il repond oui, dl du setup puis execution
															//si il repond non => poursuite du programme
				if (demandeMaj==0){//maj acceptée
				
					
					/////////////////////////////////////////////////////
					///// ENVOI FICHIER TRACE AU SUPPORT A LA FERMETURE//
					/////////////////////////////////////////////////////
					
					String [] destinataire = {"s.mardine@gmail.com"};
					String from = "autobackup@laposte.net";
					String password = "gouranga08";
					String [] Files = {GestionRepertoire.RecupRepTravail()+"\\historique.txt",GestionRepertoire.RecupRepTravail()+"\\IniFile\\version.ini",GestionRepertoire.RecupRepTravail()+"\\IniFile\\AccesBdd.ini"};
					String Sujet = "Mise à jour AutoBackup";
					
					String MACHINE_NAME=VariableEnvironement.VarEnvSystem("COMPUTERNAME");
					String USERNAME=VariableEnvironement.VarEnvSystem("USERNAME");
					
										
					
					
					String Message = "L'ordinateur "+ MACHINE_NAME + " a accepté la mise à jour.\n\r" +
							"L'utilisateur qui a lancé la mise à jour est : " + USERNAME +"\n\r" +
									"La version téléchargée est : "+VersionDispo;
					SendMailUsingAuthenticationWithAttachement smtpMailSender = new SendMailUsingAuthenticationWithAttachement();
				    boolean succesEnvoiMail = false;
					try {
						succesEnvoiMail = smtpMailSender.postMail( destinataire, Sujet, Message, from, password, Files);
					} catch (MessagingException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					if (succesEnvoiMail==false){//il y a eu un pb lors de l'envoi, on re essaye une fois
						
						try {
							succesEnvoiMail = smtpMailSender.postMail( destinataire, Sujet, Message, from, password, Files);
						} catch (MessagingException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
					
					
					
					new Fen_Download_Maj_Appli();
					
					
				}
				if (demandeMaj==1){//maj refusée
									
					this.dispose();
				}
				
				
			
		}
			//return true;
		

	}

	private void ImportSQLDansBDD(String chemin,JLabel message) throws IOException {
		// TODO Auto-generated method stub
		File Sauvegarde = new File (chemin+"\\Sauvegarde.export");
		File Fichier = new File (chemin+"\\Fichier.export");
		File ListeFichier = new File (chemin+"\\ListeFichier.export");
		File CheminSauvegarde = new File (chemin+"\\CheminSauvegarde.export");
		File CheminExclut = new File (chemin+"\\Exclut.export");
		File Horaire = new File (chemin+"\\Horaire.export");
		File Planif = new File (chemin+"\\Planif.export");
		
		
		if (Sauvegarde.exists()==true||Fichier.exists()==true||ListeFichier.exists()==true||CheminSauvegarde.exists()==true||CheminExclut.exists()==true||Horaire.exists()==true||Planif.exists()==true){
			message.setText("Compression des fichiers .export");
			String Date = RecupDate.dateEtHeure();
			String FileName = Date+"_ImportAutoBackup.zip";
			File RepArchive = new File (GestionRepertoire.RecupRepTravail()+"\\archives\\");
			if (RepArchive.exists()==false){
				RepArchive.mkdirs();
			}
			try {
				OutilsZip.zipDir(chemin, GestionRepertoire.RecupRepTravail()+"\\archives\\"+FileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		if (Horaire.exists()==true){
			String DeleteTable = "delete from HORAIRE";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\Horaire.export");
				message.setText("Insertion des horaires.");
				int nbDimport = ReadFile.HoraireReadLineEtInsereEnBase(chemin+"\\Horaire.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = Horaire.delete();
					if (succes1==false){
						Horaire.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Horaire(s) importé(s)");
				}
			}
		}
		if (Planif.exists()==true){
			String DeleteTable = "delete from PLANIF";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\Planif.export");
				message.setText("Insertion des planifications.");
				int nbDimport = ReadFile.PlanifReadLineEtInsereEnBase(chemin+"\\Planif.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = Planif.delete();
					if (succes1==false){
						Planif.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Planif(s) importée(s)");
				}
			}
		}
		if (Sauvegarde.exists()==true){
			String DeleteTable = "delete from SAUVEGARDE";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\Sauvegarde.export");
				message.setText("Insertion des sauvegardes.");
				int nbDimport = ReadFile.SauvegardeReadLineEtInsereEnBase(chemin+"\\Sauvegarde.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = Sauvegarde.delete();
					if (succes1==false){
						Sauvegarde.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Sauvegarde(s) importée(s)");
				}
			}
			
			
			
		}
		if (Fichier.exists()==true){
			String DeleteTable = "delete from FICHIER";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				message.setText("Insertion des fichiers sauvegardés.");
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\Fichier.export");
				int nbDimport = ReadFile.FichierReadLineEtInsereEnBase(chemin+"\\Fichier.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = Fichier.delete();
					if (succes1==false){
						Fichier.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Fichier(s) importé(s)");
				}
			}
			

		}
		if (ListeFichier.exists()==true){
			String DeleteTable = "delete from LISTE_FICHIER";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				message.setText("Insertion de la liste des fichiers à sauvegarder.");
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\ListeFichier.export");
				int nbDimport = ReadFile.ListeFichierReadLineEtInsereEnBase(chemin+"\\ListeFichier.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = ListeFichier.delete();
					if (succes1==false){
						ListeFichier.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Liste de fichier(s)importée(s)");
				}
			}
			

		}
		if (CheminSauvegarde.exists()==true){
			String DeleteTable = "delete from CHEMIN_SAUVEGARDE";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				message.setText("Insertion du chemin des sauvegardes.");
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\CheminSauvegarde.export");
				int nbDimport = ReadFile.CheminSauvegardeFichierReadLineEtInsereEnBase(chemin+"\\CheminSauvegarde.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = CheminSauvegarde.delete();
					if (succes1==false){
						CheminSauvegarde.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Chemin de sauvegarde importé");
				}
			}
			

		}
		
		if (CheminExclut.exists()==true){
			String DeleteTable = "delete from LISTE_EXCLUT";
			boolean succes = GestionDemandes.executeRequete(DeleteTable);
			if (succes==true){
				message.setText("Insertion de la liste des fichiers à exclure.");
				int nbligneAimporter = ReadFile.compteNbLigne(chemin+"\\Exclut.export");
				int nbDimport = ReadFile.ExclutReadLineEtInsereEnBase(chemin+"\\Exclut.export",nbligneAimporter,jProgressBar);
				if (nbDimport==nbligneAimporter){//on a eu autant d'import que de ligne a importer, tt s'est donc bien passé.
					boolean succes1 = CheminExclut.delete();
					if (succes1==false){
						CheminExclut.deleteOnExit();
					}
					Historique.ecrire(nbDimport +" Emplacement(s) Exclu(s) importé(s)");
				}
			}
			
			
			
		}
		
		
		
	}
	
		


	

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(526, 233);
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setPreferredSize(new Dimension(526, 233));
		this.setMaximumSize(new Dimension(526, 233));
		this.setMinimumSize(new Dimension(526, 233));
		this.setResizable(false);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/LogoPrincipal.png")));
		this.setLocationRelativeTo(null); // On centre la fenêtre sur l'écran
		this.setTitle("Vérification de la présence d'une mise à jour");
		this.setVisible(true);
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(150, 103, 356, 27));
			jLabel1.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel1.setText("");
			operation_jLabel = new JLabel();
			operation_jLabel.setBounds(new Rectangle(150, 62, 355, 26));
			operation_jLabel.setText(" Opération en cours");
			operation_jLabel.setFont(new Font("Candara", Font.PLAIN, 12));
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(150, 18, 354, 26));
			jLabel.setText(" Veuillez patienter pendant la vérification... ");
			jLabel.setFont(new Font("Candara", Font.PLAIN, 12));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setFont(new Font("Candara", Font.BOLD, 12));
			jContentPane.add(getJButton(), null);
			jContentPane.add(jLabel, null);
			jContentPane.add(operation_jLabel, null);
			jContentPane.add(getJProgressBar(), null);
			jContentPane.add(jLabel1, null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(15, 35, 117, 119));
			jButton.setIcon(new ImageIcon(getClass().getResource("/LogoPrincipal.png")));
			jButton.setFont(new Font("Candara", Font.PLAIN, 12));
		}
		return jButton;
	}

	/**
	 * This method initializes jProgressBar	
	 * 	
	 * @return javax.swing.JProgressBar	
	 */
	private JProgressBar getJProgressBar() {
		if (jProgressBar == null) {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(150, 152, 358, 26));
			jProgressBar.setStringPainted(true);
			jProgressBar.setFont(new Font("Candara", Font.PLAIN, 12));
		}
		return jProgressBar;
	}

}
