package org.fudaa.fudaa.sipor;

/**
 * Classe qui contient les informations des horaires
 * 
 * @author Adrien Hadoux
 */
public class SiporHoraire {

    double semaineCreneau1HeureDep = -1, semaineCreneau1HeureArrivee = -1;

    double semaineCreneau2HeureDep = -1, semaineCreneau2HeureArrivee = -1;

    double semaineCreneau3HeureDep = -1, semaineCreneau3HeureArrivee = -1;

    double lundiCreneau1HeureDep = -1, lundiCreneau1HeureArrivee = -1;

    double lundiCreneau2HeureDep = -1, lundiCreneau2HeureArrivee = -1;

    double lundiCreneau3HeureDep = -1, lundiCreneau3HeureArrivee = -1;

    double mardiCreneau1HeureDep = -1, mardiCreneau1HeureArrivee = -1;

    double mardiCreneau2HeureDep = -1, mardiCreneau2HeureArrivee = -1;

    double mardiCreneau3HeureDep = -1, mardiCreneau3HeureArrivee = -1;

    double mercrediCreneau1HeureDep = -1, mercrediCreneau1HeureArrivee = -1;

    double mercrediCreneau2HeureDep = -1, mercrediCreneau2HeureArrivee = -1;

    double mercrediCreneau3HeureDep = -1, mercrediCreneau3HeureArrivee = -1;

    double jeudiCreneau1HeureDep = -1, jeudiCreneau1HeureArrivee = -1;

    double jeudiCreneau2HeureDep = -1, jeudiCreneau2HeureArrivee = -1;

    double jeudiCreneau3HeureDep = -1, jeudiCreneau3HeureArrivee = -1;

    double vendrediCreneau1HeureDep = -1, vendrediCreneau1HeureArrivee = -1;

    double vendrediCreneau2HeureDep = -1, vendrediCreneau2HeureArrivee = -1;

    double vendrediCreneau3HeureDep = -1, vendrediCreneau3HeureArrivee = -1;

    double samediCreneau1HeureDep = -1, samediCreneau1HeureArrivee = -1;

    double samediCreneau2HeureDep = -1, samediCreneau2HeureArrivee = -1;

    double samediCreneau3HeureDep = -1, samediCreneau3HeureArrivee = -1;

    double dimancheCreneau1HeureDep = -1, dimancheCreneau1HeureArrivee = -1;

    double dimancheCreneau2HeureDep = -1, dimancheCreneau2HeureArrivee = -1;

    double dimancheCreneau3HeureDep = -1, dimancheCreneau3HeureArrivee = -1;

    double ferieCreneau1HeureDep = -1, ferieCreneau1HeureArrivee = -1;

    double ferieCreneau2HeureDep = -1, ferieCreneau2HeureArrivee = -1;

    double ferieCreneau3HeureDep = -1, ferieCreneau3HeureArrivee = -1;

    void affichage() {
        System.out.println("Creneau 1: heure depart: " + semaineCreneau1HeureDep + "\n 	heure d'arrive: " + semaineCreneau1HeureArrivee + "\n" + "Creneau 2: heure depart: " + semaineCreneau2HeureDep + "\n 	heure d'arrive: " + semaineCreneau2HeureArrivee + "\n Creneau 3: heure de depart: " + this.semaineCreneau3HeureDep + "\n             heure d arrivee:  " + this.semaineCreneau3HeureArrivee);
    }

    /**
   * Methode de recopie d'un horaire dans l horaire this Cette m�thode permet d'�viter une affectation d'adresse et
   * donc lors de la saisie d'un horaire pour une donn�e (quai, navire...) on peut utiliser le meme objet horaire ;
   * son adresse ne sera pas affect� aux nouveaux objets cr�� par la saisie et donc a chaque modification de l
   * objet horaire les horaire des objets cr��s ne seront pas modifi�
   * 
   * @param h horaire a recopier
   */
    void recopie(final SiporHoraire h) {
        this.dimancheCreneau1HeureArrivee = h.dimancheCreneau1HeureArrivee;
        this.dimancheCreneau1HeureDep = h.dimancheCreneau1HeureDep;
        this.dimancheCreneau2HeureArrivee = h.dimancheCreneau2HeureArrivee;
        this.dimancheCreneau2HeureDep = h.dimancheCreneau2HeureDep;
        this.dimancheCreneau3HeureArrivee = h.dimancheCreneau3HeureArrivee;
        this.dimancheCreneau3HeureDep = h.dimancheCreneau3HeureDep;
        this.ferieCreneau1HeureArrivee = h.ferieCreneau1HeureArrivee;
        this.ferieCreneau1HeureDep = h.ferieCreneau1HeureDep;
        this.ferieCreneau2HeureArrivee = h.ferieCreneau2HeureArrivee;
        this.ferieCreneau2HeureDep = h.ferieCreneau2HeureDep;
        this.ferieCreneau3HeureArrivee = h.ferieCreneau3HeureArrivee;
        this.ferieCreneau3HeureDep = h.ferieCreneau3HeureDep;
        this.lundiCreneau1HeureArrivee = h.lundiCreneau1HeureArrivee;
        this.lundiCreneau1HeureDep = h.lundiCreneau1HeureDep;
        this.lundiCreneau2HeureArrivee = h.lundiCreneau2HeureArrivee;
        this.lundiCreneau2HeureDep = h.lundiCreneau2HeureDep;
        this.lundiCreneau3HeureArrivee = h.lundiCreneau3HeureArrivee;
        this.lundiCreneau3HeureDep = h.lundiCreneau3HeureDep;
        this.mardiCreneau1HeureArrivee = h.mardiCreneau1HeureArrivee;
        this.mardiCreneau1HeureDep = h.mardiCreneau1HeureDep;
        this.mardiCreneau2HeureArrivee = h.mardiCreneau2HeureArrivee;
        this.mardiCreneau2HeureDep = h.mardiCreneau2HeureDep;
        this.mardiCreneau3HeureArrivee = h.mardiCreneau3HeureArrivee;
        this.mardiCreneau3HeureDep = h.mardiCreneau3HeureDep;
        this.mercrediCreneau1HeureArrivee = h.mercrediCreneau1HeureArrivee;
        this.mercrediCreneau1HeureDep = h.mercrediCreneau1HeureDep;
        this.mercrediCreneau2HeureArrivee = h.mercrediCreneau2HeureArrivee;
        this.mercrediCreneau2HeureDep = h.mercrediCreneau2HeureDep;
        this.mercrediCreneau3HeureArrivee = h.mercrediCreneau3HeureArrivee;
        this.mercrediCreneau3HeureDep = h.mercrediCreneau3HeureDep;
        this.jeudiCreneau1HeureArrivee = h.jeudiCreneau1HeureArrivee;
        this.jeudiCreneau1HeureDep = h.jeudiCreneau1HeureDep;
        this.jeudiCreneau2HeureArrivee = h.jeudiCreneau2HeureArrivee;
        this.jeudiCreneau2HeureDep = h.jeudiCreneau2HeureDep;
        this.jeudiCreneau3HeureArrivee = h.jeudiCreneau3HeureArrivee;
        this.jeudiCreneau3HeureDep = h.jeudiCreneau3HeureDep;
        this.vendrediCreneau1HeureArrivee = h.vendrediCreneau1HeureArrivee;
        this.vendrediCreneau1HeureDep = h.vendrediCreneau1HeureDep;
        this.vendrediCreneau2HeureArrivee = h.vendrediCreneau2HeureArrivee;
        this.vendrediCreneau2HeureDep = h.vendrediCreneau2HeureDep;
        this.vendrediCreneau3HeureArrivee = h.vendrediCreneau3HeureArrivee;
        this.vendrediCreneau3HeureDep = h.vendrediCreneau3HeureDep;
        this.samediCreneau1HeureArrivee = h.samediCreneau1HeureArrivee;
        this.samediCreneau1HeureDep = h.samediCreneau1HeureDep;
        this.samediCreneau2HeureArrivee = h.samediCreneau2HeureArrivee;
        this.samediCreneau2HeureDep = h.samediCreneau2HeureDep;
        this.samediCreneau3HeureArrivee = h.samediCreneau3HeureArrivee;
        this.samediCreneau3HeureDep = h.samediCreneau3HeureDep;
        this.semaineCreneau1HeureArrivee = h.semaineCreneau1HeureArrivee;
        this.semaineCreneau1HeureDep = h.semaineCreneau1HeureDep;
        this.semaineCreneau2HeureArrivee = h.semaineCreneau2HeureArrivee;
        this.semaineCreneau2HeureDep = h.semaineCreneau2HeureDep;
        this.semaineCreneau3HeureArrivee = h.semaineCreneau3HeureArrivee;
        this.semaineCreneau3HeureDep = h.semaineCreneau3HeureDep;
    }
}
