package appli;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import empruntable.*;
import mesoutils.*;

public class GestionBiblio {

    private static Scanner keyboard = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));

    public static final SimpleDateFormat formatdate = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        String fileEmpruntables = Constantes.FILE_EMPRUNTABLES;
        String fileAbonnes = Constantes.FILE_ABONNES;
        String fileEmprunts = Constantes.FILE_EMPRUNTS;
        ObjectInputStream in;
        try {
            File f1 = new File(fileEmpruntables);
            if (f1.length() > 0) {
                in = new ObjectInputStream(new FileInputStream(fileEmpruntables));
                ArrayList<Empruntable> serializedEmpruntables = (ArrayList<Empruntable>) in.readObject();
                for (Empruntable E : serializedEmpruntables) EmpruntableManager.add(E);
            }
            File f2 = new File(fileAbonnes);
            if (f2.length() > 0) {
                in = new ObjectInputStream(new FileInputStream(fileAbonnes));
                ArrayList<Abonne> serializedAbonnes = (ArrayList<Abonne>) in.readObject();
                for (Abonne A : serializedAbonnes) AbonnableManager.add(A);
            }
            File f3 = new File(fileEmprunts);
            if (f3.length() > 0) {
                in = new ObjectInputStream(new FileInputStream(fileEmprunts));
                ArrayList<Emprunt> serializedEmprunts = (ArrayList<Emprunt>) in.readObject();
                for (Emprunt E : serializedEmprunts) EmpruntManager.add(E);
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
        int action;
        do {
            menu();
            action = keyboard.nextInt();
            switch(action) {
                case 1:
                    try {
                        inscription();
                        System.out.println("\nInscription effectu�e !");
                    } catch (AbonneDejaExistantException e) {
                        System.out.println("\n" + e);
                    } catch (ParseException e) {
                        System.out.println("\n" + e);
                    }
                    break;
                case 2:
                    try {
                        emprunt();
                        System.out.println("\nEmprunt termin� !");
                    } catch (AbonneInexistantException e) {
                        System.err.println("\n" + e);
                    } catch (AbonnementExpireException e) {
                        System.err.println("\n" + e);
                    } catch (LectureImpossibleException e) {
                        System.err.println("\n" + e);
                    }
                    break;
                case 3:
                    try {
                        retour();
                        System.out.println("\nRetour termin� !");
                    } catch (LectureImpossibleException e) {
                        System.err.println("\n" + e);
                    } catch (RetourImpossibleException e) {
                        System.err.println("\n" + e);
                    }
                    break;
                case 4:
                    System.out.println("\n\n+--------------------------------+");
                    System.out.println("| INFOS ABONNE                   |");
                    System.out.println("+--------------------------------+");
                    try {
                        int codeLu = LecteurCodeBarre.litNumero();
                        if (!AbonnableManager.containsId(codeLu)) throw new AbonneInexistantException();
                        Abonne A = (Abonne) AbonnableManager.getById(codeLu);
                        System.out.println(A.getInfos());
                    } catch (AbonneInexistantException e) {
                        System.out.println("\n" + e);
                    } catch (LectureImpossibleException e) {
                        System.out.println("\n" + e);
                    }
                    break;
                case 5:
                    System.out.println("\n\n+--------------------------------+");
                    System.out.println("| AFFICHAGE TOUS ABONNES         |");
                    System.out.println("+--------------------------------+");
                    String listeAb = AbonnableManager.getFullString();
                    System.out.println(listeAb.equals("") ? "Aucun abonn� pour le moment !" : listeAb);
                    break;
                case 6:
                    System.out.println("\n\n+--------------------------------+");
                    System.out.println("| AFFICHAGE TOUS EMPRUNTABLES    |");
                    System.out.println("+--------------------------------+");
                    String listeEmpruntables = EmpruntableManager.getFullString();
                    System.out.println(listeEmpruntables.equals("") ? "Aucun empruntable pour le moment !" : listeEmpruntables);
                    break;
                case 7:
                    System.out.println("\n\n+--------------------------------+");
                    System.out.println("| AFFICHAGE TOUS EMPRUNTS        |");
                    System.out.println("+--------------------------------+");
                    String listeEmprunts = EmpruntManager.getFullString();
                    System.out.println(listeEmprunts.equals("") ? "Aucun emprunt pour le moment !" : listeEmprunts);
                    break;
                case 8:
                    System.out.println("\n\n+--------------------------------+");
                    System.out.println("| EMPRUNTABLES POUR UN ABONNE    |");
                    System.out.println("+--------------------------------+");
                    int codeLu;
                    try {
                        codeLu = LecteurCodeBarre.litNumero();
                        if (!AbonnableManager.containsId(codeLu)) throw new AbonneInexistantException();
                    } catch (AbonneInexistantException e) {
                        System.out.println("\n" + e);
                        break;
                    } catch (LectureImpossibleException e) {
                        System.out.println("\n" + e);
                        break;
                    }
                    Abonne A = (Abonne) AbonnableManager.getById(codeLu);
                    System.out.println("Objets empruntables par " + A + " :");
                    System.out.println(EmpruntableManager.empruntablesPossiblesPour(A));
                    break;
                case 0:
                    System.out.println("\nSauvegarde des donn�es avant fermeture...");
                    AbonnableManager.serializeArray();
                    EmpruntManager.serializeArray();
                    System.out.println("\nFermeture de l'application...");
                    System.exit(-1);
                    break;
                default:
                    break;
            }
            System.out.println("\nAppuyez sur ENTRER pour continuer...");
            keyboard.next();
        } while (action != 0);
    }

    public static void menu() {
        System.out.println("+----------------------------------+");
        System.out.println("| APPLICATION BIBLIO MUNICIPALE    |");
        System.out.println("+----------------------------------+\n");
        System.out.println("+-----------------------------------");
        System.out.println("| GESTION                          |");
        System.out.println("+----------------------------------+");
        System.out.println("|  1 : Nouvelle inscription        |");
        System.out.println("|  2 : Emprunt                     |");
        System.out.println("|  3 : Retour                      |");
        System.out.println("+----------------------------------+");
        System.out.println("+----------------------------------+");
        System.out.println("| AFFICHAGES                       |");
        System.out.println("+----------------------------------+");
        System.out.println("|  4 : Infos d'un abonn�           |");
        System.out.println("|  5 : Tous les abonn�s            |");
        System.out.println("|  6 : Tous les empruntables       |");
        System.out.println("|  7 : Tous les emprunts           |");
        System.out.println("|  8 : Objets dispos pour un ab.   |");
        System.out.println("+----------------------------------+");
        System.out.println("+----------------------------------+");
        System.out.println("| 0 : Quitter                      |");
        System.out.println("+----------------------------------+\n");
        System.out.print("Action d�sir�e : ");
    }

    public static void inscription() throws ParseException, AbonneDejaExistantException {
        System.out.println("\n\n----------------------------------");
        System.out.println("| INSCRIPTION                    |");
        System.out.println("----------------------------------");
        System.out.println("INFORMATIONS SUR L'ABONNE");
        System.out.print("Nom : ");
        String nom = keyboard.next();
        System.out.print("Pr�nom : ");
        String prenom = keyboard.next();
        Date dateActuelle = new Date(), dateSaisie = new Date();
        do {
            try {
                System.out.print("Date de naissance (jj/MM/aaaa) : ");
                dateSaisie = formatdate.parse(keyboard.next());
                if (dateActuelle.compareTo(dateSaisie) < 0) throw new DateIncoherenteException(dateSaisie);
            } catch (DateIncoherenteException e) {
                System.err.println("\n" + e + formatdate.format(e.getDateSaisie()));
            }
        } while (dateActuelle.compareTo(dateSaisie) <= 0);
        Abonne newAbonne = new Abonne(nom, prenom, dateSaisie);
        if (AbonnableManager.alreadyContains(newAbonne)) throw new AbonneDejaExistantException();
        System.out.print("Adresse : ");
        newAbonne.setAdresse(keyboard.next());
        try {
            abonnement(newAbonne);
        } catch (AbonnementExpireException e) {
            System.err.println("Aucun abonnement choisi...");
        }
        AbonnableManager.add(newAbonne);
    }

    public static void abonnement(Abonne A) throws AbonnementExpireException {
        System.out.print(Abonnement.getListe());
        System.out.print("Abonnement (n�) : ");
        int numType = keyboard.nextInt();
        if (numType >= 0 && numType < Abonnement.NB_AB) {
            A.setTypeAb(Abonnement.indexToType(numType));
            A.setDateLimiteAb(true);
        } else {
            A.setTypeAb(0);
            A.setDateLimiteAb(false);
            throw new AbonnementExpireException(formatdate.format(A.getDateLimiteAb()));
        }
    }

    public static void emprunt() throws AbonneInexistantException, LectureImpossibleException, AbonnementExpireException {
        System.out.println("\n\n----------------------------------");
        System.out.println("| EMPRUNT                        |");
        System.out.println("----------------------------------");
        int codeLu;
        Abonne A;
        codeLu = LecteurCodeBarre.litNumero();
        if (!AbonnableManager.containsId(codeLu)) throw new AbonneInexistantException();
        A = (Abonne) AbonnableManager.getById(codeLu);
        System.out.println("\nAbonn�  : " + A);
        if (A.abonnementExpire()) {
            System.err.println("L'abonnement a expir�, il faut le renouveler");
            abonnement(A);
        }
        EmpruntManager.updateObjectCpt(A);
        System.out.println("\nLecture des codes barre objet un par un (0 pour arr�ter)...");
        ArrayList<Empruntable> listTemp = new ArrayList<Empruntable>();
        Empruntable E;
        do {
            try {
                codeLu = LecteurCodeBarre.litNumero();
                if (codeLu == 0) continue;
                if (!EmpruntableManager.containsId(codeLu)) throw new ObjetInexistantException();
                E = EmpruntableManager.getById(codeLu);
                if (E.depasseMaxEmprunt()) throw new EmpruntEnSurnombreException();
                if (!E.estEmpruntablePar(A)) throw new EmpruntNonAutoriseException();
                if (EmpruntManager.contains(new Emprunt(A, E))) {
                    listTemp.add(E);
                    EmpruntManager.getByIdE(E.getId()).setDateEmprunt(new Date());
                    System.out.println("Objet renouvel� : " + E.toString());
                } else if (!E.estDisponible()) {
                    throw new ObjetDejaEmprunteException();
                } else {
                    E.setDisponible(false);
                    listTemp.add(E);
                    EmpruntManager.add(new Emprunt(A, E));
                    System.out.println("Objet emprunt� : " + E.toString());
                }
            } catch (EmpruntImpossibleException e) {
                System.err.println("\n" + e);
                continue;
            }
        } while (codeLu != 0);
        System.out.println("R�capitulatif de l'emprunt");
        System.out.println("-------------------------------");
        System.out.println("Abonn� : " + A);
        System.out.println("Objets emprunt�s : ");
        if (listTemp.isEmpty()) System.out.println("(aucun)"); else for (Empruntable unEmpruntable : listTemp) System.out.println(unEmpruntable);
    }

    public static void retour() throws LectureImpossibleException, RetourImpossibleException {
        System.out.println("\n\n----------------------------------");
        System.out.println("| RETOURS                        |");
        System.out.println("----------------------------------");
        System.out.println("\nLecture des codes barre objet un par un (0 pour arr�ter)...");
        Emprunt E;
        Abonne A;
        int codeLu = 0;
        do {
            codeLu = LecteurCodeBarre.litNumero();
            if (codeLu == 0) continue;
            E = EmpruntManager.getByIdE(codeLu);
            E.getEmpruntable().estRetournable();
            A = E.getAbonne();
            E.setDateRetourEffective(new Date());
            double amende = E.getAmende();
            System.out.println("Abonn� : " + A);
            System.out.println("Objet rendu : " + E.getEmpruntable().toString());
            if (amende != 0) System.err.println("Retard de " + E.getDureeSemaine() + " semaines. Amende de " + amende + "� � payer");
            E.getEmpruntable().setDisponible(true);
            EmpruntManager.remove(E);
        } while (codeLu != 0);
    }
}
