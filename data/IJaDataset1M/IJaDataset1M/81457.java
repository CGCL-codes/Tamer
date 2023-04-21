package coursimpl;
import cours.*;
import utils.*;

public class Poterie  extends Cours{
	final static String INTITULE = "Poterie";
	final static double AGE_REDUCTION = 16;
	final static double REDUCTION = 50;
	private static int TARIF;
	
	Poterie(String intitulé,Adherent animateur,Horaire horaire,
			int nbMaxInscrits)throws JourException, CoursNullException{
		
		super(intitulé,animateur,horaire,nbMaxInscrits,TARIF);

	}
	
	
	public Poterie(Adherent animateur,Horaire horaire,
			int nbMaxInscrits)throws JourException, CoursNullException{
		
		super(INTITULE,animateur,horaire,nbMaxInscrits,TARIF);

	}
	
	public void ajouterAdherent(Adherent adherent) throws CoursCompletException, DejaInscritException, AgeNonConformeException, AjoutAdherentCoursException{
		
			super.ajouterAdherent(adherent);
	}
	
	public double getReduction(Adherent a){
		if(a.getAge() < AGE_REDUCTION)
			return REDUCTION;
		return 0;
	}
	
}
