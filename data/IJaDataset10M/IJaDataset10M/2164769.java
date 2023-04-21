package galaxiia.noyau;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.Socket;
import java.util.*;
import org.jul.common.ExceptionHandler;
import org.jul.dcl.classpath.ClasspathConsistencyException;
import org.jul.dcl.classpath.URLClasspath;
import org.jul.dcl.fetcher.ClassFetcher;
import org.jul.i18n.I18n;
import galaxiia.configuration.*;
import galaxiia.exception.*;
import galaxiia.jeu.carte.Carte;
import galaxiia.jeu.carte.locale.CarteLocale;
import galaxiia.jeu.carte.multijoueur.CarteMultiJoueur;
import galaxiia.jeu.intelligence.Intelligence;
import galaxiia.jeu.unite.ClasseurUnite;
import galaxiia.noyau.enregistreur.Enregistreur;
import galaxiia.noyau.reseau.ConstantesReseau;
import galaxiia.ui.Galaxiia;
import galaxiia.ui.UI;
import galaxiia.ui.dialogues.DialogueChargement;
import galaxiia.ui.interfaces.InterfaceJoueur;
import galaxiia.ui.visionneurs.*;

public class ConstructeurNoyau implements ConstantesReseau {

    private static int getUniteAleatoire(Carte carte, int indexJoueur) {
        int[] unitesAutorisees = carte.typeUnitesAutorisees()[indexJoueur];
        return unitesAutorisees[Math.round((float) Math.random() * (unitesAutorisees.length - 1))];
    }

    private static ChargeurClasse<? extends Carte> chargeCarte(Class<? extends Carte> typeCarte, ConfigurationParties configurationParties, Partie partie) throws ExceptionInitialisation {
        final ChargeurClasse<? extends Carte> chargeurCarte;
        final URLClasspath classpathCarte = new URLClasspath(Galaxiia.loaderNoyau);
        if (partie.isCarteAleatoire()) {
            List<ChargeurClasse<? extends Carte>> cartes = new ArrayList<ChargeurClasse<? extends Carte>>();
            cartes.addAll(ChargeurClasse.trouveSousClassesConstructeurDefaut(Galaxiia.loaderNoyau, configurationParties.getClasspathCartes(), typeCarte, new ExceptionHandler.SilentHandler()));
            if (cartes.size() == 0) {
                throw new ExceptionInitialisation("Impossible de choisir aléatoirement la carte car aucune n'a été trouvée.");
            } else {
                chargeurCarte = cartes.get((int) Math.round((cartes.size() - 1) * Math.random()));
            }
        } else {
            chargeurCarte = partie.getChargeurCarte();
        }
        try {
            for (ClassFetcher classFetcher : chargeurCarte.getDependances()) {
                classFetcher.fetch();
            }
            chargeurCarte.getClassFetcher().fetch();
            chargeurCarte.chargeClasse(classpathCarte, true);
            chargeurCarte.creeInstance();
            return chargeurCarte;
        } catch (IOException e) {
            throw new ExceptionInitialisation("Impossible de charger la carte à cause d'une erreur E/S.", e);
        } catch (ClasspathConsistencyException e) {
            throw new ExceptionInitialisation("Impossible de charger la carte, un fichier nécessaire n'a pas été trouvé.", e);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInitialisation("Impossible de trouver la carte.", e);
        } catch (Throwable t) {
            throw new ExceptionInitialisation("Erreur lors du chargement de la carte.", t);
        }
    }

    private static UniteExterne[] chargeUnites(ConfigurationParties configurationParties, Partie partie, Carte carte) throws ExceptionInitialisation {
        final List<Joueur> joueurs = partie.getJoueurs();
        final List<Intelligence> intelligences = new ArrayList<Intelligence>();
        final List<Integer> unitesJoueurs = new ArrayList<Integer>();
        if (!(carte instanceof CarteMultiJoueur) && joueurs.size() < carte.nombreJoueurs()) {
            throw new ExceptionInitialisation("Le nombre de joueurs configurés est insuffisant pour la carte \"" + carte.nom() + "\" (" + carte.nombreJoueurs() + " sont requis).");
        } else {
            ChargeurClasse<Intelligence> chargeurIntelligence;
            URLClasspath classpathIntelligence = new URLClasspath(Galaxiia.loaderNoyau);
            int uniteJoueur;
            for (int i = 0; (carte instanceof CarteMultiJoueur && i < 1) || (!(carte instanceof CarteMultiJoueur) && i < carte.nombreJoueurs()); i++) {
                if (joueurs.get(i).isUniteAleatoire()) {
                    uniteJoueur = getUniteAleatoire(carte, i);
                } else {
                    uniteJoueur = joueurs.get(i).getUnite();
                    boolean uniteValide = false;
                    for (int typeUnite : carte.typeUnitesAutorisees()[i]) {
                        if (uniteJoueur == typeUnite) {
                            uniteValide = true;
                        }
                    }
                    if (!uniteValide) {
                        if (partie.isCarteAleatoire()) {
                            uniteJoueur = getUniteAleatoire(carte, i);
                        } else {
                            throw new ExceptionInitialisation("L'unité \"" + ClasseurUnite.conversionTypeNom(uniteJoueur) + "\"du joueur n°" + (i + 1) + " est invalide.");
                        }
                    }
                }
                unitesJoueurs.add(uniteJoueur);
                if (joueurs.get(i).isIntelligenceAleatoire()) {
                    List<ChargeurClasse<Intelligence>> intelligencesACharger = new ArrayList<ChargeurClasse<Intelligence>>();
                    intelligencesACharger.addAll(ChargeurClasse.trouveSousClassesConstructeurDefaut(Galaxiia.loaderNoyau, configurationParties.getClasspathIntelligences(), Intelligence.class, new ExceptionHandler.SilentHandler()));
                    if (intelligencesACharger.size() == 0) {
                        throw new ExceptionInitialisation("Impossible de choisir aléatoirement le joueur n°" + (i + 1) + " car aucune IA n'a été trouvée.");
                    } else {
                        chargeurIntelligence = intelligencesACharger.get((int) Math.round((intelligencesACharger.size() - 1) * Math.random()));
                    }
                } else {
                    chargeurIntelligence = joueurs.get(i).getChargeurIntelligence();
                }
                try {
                    for (ClassFetcher classFetcher : chargeurIntelligence.getDependances()) {
                        classFetcher.fetch();
                    }
                    chargeurIntelligence.getClassFetcher().fetch();
                    chargeurIntelligence.chargeClasse(classpathIntelligence, true);
                    final Constructor<? extends Intelligence> constructeur = chargeurIntelligence.getInstanceClasseCache().getConstructor(new Class<?>[0]);
                    if (Galaxiia.securiteChargement) {
                        GestionnaireSecurite.executionSecurisee(new Executable() {

                            public void execute() throws Throwable {
                                intelligences.add(constructeur.newInstance(new Object[0]));
                            }
                        });
                    } else {
                        intelligences.add(constructeur.newInstance(new Object[0]));
                    }
                } catch (IOException e) {
                    throw new ExceptionInitialisation("Impossible de charger l'intelligence à cause d'une erreur E/S.", e);
                } catch (ClasspathConsistencyException e) {
                    throw new ExceptionInitialisation("Impossible de charger l'intelligence, un fichier nécessaire n'a pas été trouvé.", e);
                } catch (ClassNotFoundException e) {
                    throw new ExceptionInitialisation("Impossible de trouver l'intelligence.", e);
                } catch (Throwable t) {
                    throw new ExceptionInitialisation("Erreur lors du chargement de l'intelligence.", t);
                }
            }
        }
        UniteExterne[] unitesExternes = new UniteExterne[carte instanceof CarteMultiJoueur ? 1 : carte.nombreJoueurs()];
        for (int i = 0; i < unitesExternes.length; i++) {
            unitesExternes[i] = new UniteExterne(joueurs.get(i).getChargeurIntelligence(), intelligences.get(i), unitesJoueurs.get(i), joueurs.get(i).getArguments());
        }
        return unitesExternes;
    }

    public static GestionnaireVisionneurs initialisePartie(DialogueChargement.GestionnaireDialogue gestionnaireDialogue, InterfaceJoueur interfaceJoueur, Set<Class<? extends Visionneur>> typesVisionneurs, ConfigurationParties configurationParties) throws ExceptionInitialisation {
        final I18n i18n = UI.getI18nInstance(ConstructeurNoyau.class);
        final Partie partie = configurationParties.getParties().get(configurationParties.getPartieCourante());
        if (partie == null) {
            throw new ExceptionInitialisation("Partie inconnue : " + configurationParties.getPartieCourante() + ".");
        } else if (partie.getTypePartie() == TypePartie.NON_CONFIGUREE) {
            throw new ExceptionInitialisation("Il n'est pas possible de charger une partie non configurée.");
        } else {
            try {
                Class<? extends Noyau> typeNoyau = partie.getClasseNoyau();
                ChargeurClasse<? extends Carte> chargeurCarte = null;
                Carte carte = null;
                switch(partie.getTypePartie()) {
                    case LOCALE:
                        gestionnaireDialogue.changeMessage(i18n.get("map-loading"));
                        chargeurCarte = chargeCarte(CarteLocale.class, configurationParties, partie);
                        carte = chargeurCarte.getInstanceCache();
                        break;
                    case MULTIJOUEUR_CLIENT:
                        gestionnaireDialogue.changeMessage(i18n.get("map-downloading"));
                        chargeurCarte = null;
                        Socket socket = new Socket(InetAddress.getByName(partie.getAdresseReseau()), partie.getPortReseau());
                        DataInput inputStream = new DataInputStream(socket.getInputStream());
                        DataOutput outputStream = new DataOutputStream(socket.getOutputStream());
                        outputStream.writeByte(COMMANDE_NOM_CARTE);
                        String nomClasse = inputStream.readUTF();
                        try {
                            carte = Galaxiia.loaderNoyau.loadClass(nomClasse).asSubclass(Carte.class).newInstance();
                        } catch (Throwable t) {
                        }
                        if (carte == null) {
                            outputStream.writeByte(COMMANDE_TELECHARGEMENT_CARTE);
                            carte = ChargeurClasse.recupereClasse(nomClasse, Carte.class, Galaxiia.loaderNoyau, inputStream).creeInstance();
                        }
                        outputStream.writeByte(COMMANDE_TERMINE);
                        socket.close();
                        break;
                    case MULTIJOUEUR_SERVEUR:
                        gestionnaireDialogue.changeMessage(i18n.get("map-loading"));
                        chargeurCarte = chargeCarte(CarteMultiJoueur.class, configurationParties, partie);
                        carte = chargeurCarte.getInstanceCache();
                        break;
                    default:
                        throw new ExceptionInitialisation("Type de noyau invalide : " + typeNoyau + ".");
                }
                gestionnaireDialogue.changeMessage(i18n.get("initializing-core"));
                Enregistreur enregistreur = new Enregistreur(carte);
                GestionnaireVisionneurs gestionnaireVisionneurs = new GestionnaireVisionneurs(interfaceJoueur, enregistreur, typesVisionneurs);
                Noyau noyau;
                switch(partie.getTypePartie()) {
                    case LOCALE:
                        noyau = typeNoyau.getConstructor(Enregistreur.class, GestionnaireVisionneurs.class, Carte.class, UniteExterne[].class).newInstance(enregistreur, gestionnaireVisionneurs, carte, chargeUnites(configurationParties, partie, carte));
                        break;
                    case MULTIJOUEUR_CLIENT:
                        noyau = typeNoyau.getConstructor(Enregistreur.class, GestionnaireVisionneurs.class, Carte.class, InetAddress.class, Integer.TYPE, UniteExterne.class).newInstance(enregistreur, gestionnaireVisionneurs, carte, InetAddress.getByName(partie.getAdresseReseau()), partie.getPortReseau(), chargeUnites(configurationParties, partie, carte)[0]);
                        break;
                    case MULTIJOUEUR_SERVEUR:
                        noyau = typeNoyau.getConstructor(Enregistreur.class, GestionnaireVisionneurs.class, Carte.class, InetAddress.class, Integer.TYPE, ChargeurClasse.class).newInstance(enregistreur, gestionnaireVisionneurs, carte, partie.getAdresseReseau() == null ? null : InetAddress.getByName(partie.getAdresseReseau()), partie.getPortReseau(), chargeurCarte);
                        break;
                    default:
                        throw new ExceptionInitialisation("Type de noyau invalide : " + typeNoyau + ".");
                }
                gestionnaireDialogue.changeMessage(i18n.get("starting-core"));
                noyau.demarre();
                return gestionnaireVisionneurs;
            } catch (ExceptionInitialisation e) {
                throw new ExceptionInitialisation("Impossible de démarrer le noyau.", e);
            } catch (Throwable t) {
                throw new ExceptionInitialisation("Erreur fatale lors de la création du noyau !", t);
            }
        }
    }
}
