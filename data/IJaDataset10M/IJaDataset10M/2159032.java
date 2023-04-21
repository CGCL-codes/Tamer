package Thread;

import ini_Manager.ConfigMgt;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import Utilitaires.Comptage;
import Utilitaires.GestionRepertoire;
import Utilitaires.Historique;

public class Thread_SendMail extends Thread {
	
	
	//tte les decalration necessaire...
	protected JList listeEntr�e;
	protected DefaultListModel modelListeEntr�e;
	protected JProgressBar Progress;
	protected JTextField mailClient_Label,USER_label,FROM_label,PASSWORD_label,SMTP_SERVER_label;

	
	/**
     * envoi un mail via sendmail.exe
     * @param FichierTrait�s -JList les fichiers � envoyer
     * @param ModelFichierCorrig�s -DefaultListModel model de la liste. 
     * @param ProgressBar -JProgressBar progression de l'envoi
     * @param from -JTextField expediteur
     * @param user -JTextField nom d'utilisateur
     * @param password -JTextField mot de passe
     * @param smtp -JTextField serveur smtp
     * @param adresseMailPourRenvoi -JTextField adresse mail vis�e 
     *  
     */
	
	public Thread_SendMail (JList FichierTrait�s,
			DefaultListModel ModelFichierCorrig�s,
			JProgressBar ProgressBar,
			JTextField from,JTextField user,JTextField password,JTextField smtp,JTextField adresseMailPourRenvoi
			)
			{
		
		listeEntr�e=FichierTrait�s;
		modelListeEntr�e=ModelFichierCorrig�s;
		Progress=ProgressBar;
		mailClient_Label=adresseMailPourRenvoi;
		USER_label=user;
		PASSWORD_label=password;
		FROM_label=from;
		SMTP_SERVER_label=smtp;
		
				
	}
	
	public void run(){
		
		//on commence par regarder l'adresse mail de renvoi, 
		//si c'est wanadoo sant�, on va chercher dans le fichier Config.ini, 
		//les param wanadoo sant�
		
		final String AdresseMail = mailClient_Label.getText();
		
		// on verifie si l'adresse de renvoi est bien renseign�e, 
		//si ce n'est pas le cas, on affiche un message a l'utilisateur et on sort
		
		if (AdresseMail.equals("")){
			JOptionPane.showMessageDialog(null, "l'adresse mail du client n'est pas renseign�e, envoi impossible",
					"Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (AdresseMail.indexOf("@sante.wanadoo.fr")!=-1){//le mail du client est chez wanadoo sant�
			// On cr�e une instance config qui permet de g�rer la gestion d'un fichier   	
			try {
				
				ConfigMgt config = null;
				try {
					config = new ConfigMgt("Config.ini",GestionRepertoire.RecupRepTravail()+"\\Ini File\\",'[');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				final String USER = config.getValeurDe("USER_WANADOO_SANTE");
				final String PASSWORD = config.getValeurDe("PASSWORD_WANADOO_SANTE");
				final String FROM = config.getValeurDe("FROM_WANADOO_SANTE");
				final String SMTP = config.getValeurDe("SMTP_WANADOO_SANTE");
				
				FROM_label.setText(FROM);
				USER_label.setText(USER);
				PASSWORD_label.setText(PASSWORD);
				SMTP_SERVER_label.setText(SMTP);
			
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		//on a fini de s'occuper des parametres 
		
		//on lance l'envoi des mail
		Comptage count = new Comptage ("./corrig�s");
		int nbMailARenvoyer = count.getNbFichier();
		//int nbMailARenvoyer = listeEntr�e.getModel().getSize();
		//on est oblig� le le passer comme ca car la commande DOs ne supporte pas le "./.../"
		String repertoireDesMailsARenvoyer = GestionRepertoire.RecupRepTravail()+"\\corrig�s\\";
		File repCorrig� = new File (repertoireDesMailsARenvoyer);
		File[] subfiles = repCorrig�.listFiles();
		
		for (int i=0;i<nbMailARenvoyer;i++){
			listeEntr�e.setSelectedIndex(i);
			//on construit le chemin du mail � renvoyer
			final String cheminduFichierARevoyer = subfiles[i].toString();
			
			//final String cheminduFichierARevoyer = repertoireDesMailsARenvoyer+modelListeEntr�e.getElementAt(listeEntr�e.getSelectedIndex()).toString();
			final int PourcentProgression = (100*(i+1))/nbMailARenvoyer;
			final int index = i;	
			try {
					SwingUtilities.invokeAndWait(new Runnable(){
						
						public void run(){
											    
						    //on affiche la progression dans la progressBar
							Progress.setValue(PourcentProgression);
							Progress.setString(PourcentProgression+" %");
							listeEntr�e.ensureIndexIsVisible(index);
							Runtime r = Runtime.getRuntime();
							try {
								Historique.ecrire ("Envoi du fichier " + cheminduFichierARevoyer +"� l'adresse " + AdresseMail +"\n\r");
														
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							
							//on cr�er la commande qui servira a envoyer le message
						     String cmdEnvoi = String.format("cmd.exe /c for %%1 in (\"%s\") do \"sendEmail.exe\" -t %s -f %s -s %s -xu %s -xp %s -v -o message-format=raw< %%1 >>%s",cheminduFichierARevoyer, AdresseMail, FROM_label.getText(), SMTP_SERVER_label.getText(), USER_label.getText(), PASSWORD_label.getText(),"historique.txt");
						     try {
								Process p = r.exec(cmdEnvoi);
								try {
									p.waitFor();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
						
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
			
		}
		
		//panelAnim�.stop();
		//jpanel.setVisible(false);
		//jpanel.removeAll();
		//fin de l'envoi, on passe a la suite
		JOptionPane.showMessageDialog(null, "Traitement termin�",
				"Ok", JOptionPane.INFORMATION_MESSAGE);
		
		Progress.setValue(0);
		Progress.setString("0 %");
	}
}
			
			

		
		
		
	


