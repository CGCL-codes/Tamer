package jEDF.Resources;

import java.util.*;

/**
 *
 * <p>Titre : jEDF</p>
 * <p>Description : Java European Data Format Viewer and Analyser</p>
 * <p>Author : Nizar Kerkeni</p>
 * <p>Copyright : Copyright (c) 2003-2006</p>
 * <p>Version : 2.0</p>
 */
public class MyResource_FR extends MyResource_EN {

    private HashMap hashMap = new HashMap();

    private void initialize() {
        hashMap.put("File", "Fichier");
        hashMap.put("Open", "Ouvrir");
        hashMap.put("Close", "Fermer");
        hashMap.put("Display", "Affichage");
        hashMap.put("Zoom in", "Zoom avant");
        hashMap.put("Zoom out", "Zoom arriére");
        hashMap.put("Reset zoom", "Réinitialiser le zoom");
        hashMap.put("Hypnogram", "Hypnogramme");
        hashMap.put("New", "Nouveau");
        hashMap.put("Default", "Par défaut");
        hashMap.put("Preset", "Pré-programmé");
        hashMap.put("Precise Scoring", "Scorage fin");
        hashMap.put("Language", "Langue");
        hashMap.put("Error", "Erreur");
        hashMap.put("Warning", "Avertissement");
        hashMap.put("EDF files", "Fichiers EDF");
        hashMap.put("OK", "OK");
        hashMap.put("Close hypnogram", "Fermer l'hypnogramme");
        hashMap.put("Open hypnogram", "Ouvrir un hypnogramme");
        hashMap.put("EDF file informations", "Informations du fichier EDF");
        hashMap.put("EDF version", "Version EDF");
        hashMap.put("Patient Identification", "Identification patient");
        hashMap.put("Record Identification", "Identification enregistrement");
        hashMap.put("Recording Start Date", "Début enregistrement");
        hashMap.put("Recording Duration", "               Durée");
        hashMap.put("Number of Epochs", "Nombre d'époques");
        hashMap.put("Epoch Duration (s)", "Durée Epoque (s)");
        hashMap.put("Number of signals", "Nombre de signaux");
        hashMap.put("EDF file", "Fichier EDF");
        hashMap.put("Label", "Etiquette");
        hashMap.put("Transducer type", "Type capteur");
        hashMap.put("Physical dimension", "Unité physique");
        hashMap.put("Physical min", "Min physique");
        hashMap.put("Physical max", "Max Physique");
        hashMap.put("Digital min", "Min numérique");
        hashMap.put("Digital max", "Max numérique");
        hashMap.put("Prefiltering", "Pre-filtrage");
        hashMap.put("Number of samples", "Nombre points");
        hashMap.put("Awake", "Veille");
        hashMap.put("Rem", "SP");
        hashMap.put("Stage 1", "Stade 1");
        hashMap.put("Stage 2", "Stade 2");
        hashMap.put("Stage 3", "Stade 3");
        hashMap.put("Stage 4", "Stade 4");
        hashMap.put("Movement", "Mouvement");
        hashMap.put("The specified hypnogram does not have the same number", "L'hypnogramme spécifié ne semble pas avoir le même nombre");
        hashMap.put("\nof epochs as the loaded EDF file.", "\nd'époques que le fichier EDF chargé.");
        hashMap.put("The specified file can only be opened in read-only mode.", "Le fichier spécifié ne peut être ouvert qu'en lecture seule.");
        hashMap.put("You are attempting to close a modified hypnogram without it being saved first.\nAre you sure you want to continue ?", "Vous essayez de fermer un hypnogramme modifié sans que ce dernier ait été sauvegardé au préalable.\nEtes-vous sûr de vouloir continuer ?");
        hashMap.put("0,1,2,3,4,5,6 and 9 are Wake,1,2,3,4,REM,MT and unscored, respectively", "0,1,2,3,4,5,6 et 9 correspondent respectivement à Veille,1,2,3,4,SP,MT et non scoré.");
        hashMap.put("Ready whenever you are !", "Prêt dès que vous l'êtes !");
        hashMap.put("Unable to access the specified file.", "Impossible d'accéder au fichier spécifié.");
        hashMap.put("The specified file does not appear to be\nan EDF standard compliant file.", "Le fichier spécifié ne semble pas être un fichier EDF conforme.");
        hashMap.put("I/O Error : please verify that you have the rights to access the specified file\nand that it is not in use by another program.", "Erreur E/S : veuillez vérifier que vous aves le droit d'accéeder au fichier spécifié\net qu'il n'est pas utilisé par un autre programme.");
        hashMap.put("Epoch n° ", "Epoque n° ");
        hashMap.put("                 Hypnogram Epoch Duration : ", "                 Durée de l'époque de l'hypnogramme : ");
        hashMap.put("EDF files", "Fichiers EDF");
        hashMap.put("Hypnogram", "Hypnogramme");
        hashMap.put("Save", "Sauver");
        hashMap.put("New", "Nouveau");
        hashMap.put("Preset", "Pré-enregistré");
        hashMap.put("None", "Aucun");
        hashMap.put("Hypnogram creation", "Création de l'hypnogramme");
        hashMap.put("Extract from signal", "Extraire du signal");
        hashMap.put("Please choose the signal to extract the hypnogram from :", "Veuillez choisir le signal d'où extraire l'hypnogramme :");
        hashMap.put("Extract", "Extraire");
        hashMap.put("Load from file", "Charger depuis le fichier");
        hashMap.put("Please choose an input file :", "Veuillez choisir un fichier d'entrée :");
        hashMap.put("Browse", "Parcourir");
        hashMap.put("Load", "Charger");
        hashMap.put("Please choose the desired epoch duration (in s):", "Veuillez choisir la durée d'une époque.");
        hashMap.put("New Hypnogram", "Nouvel hypnogramme");
        hashMap.put("Please choose the file to record the hypnogram to:", "Veuillez choisir le fichier où enregistrer l'hypnogramme.");
        hashMap.put("Create", "Créer");
        hashMap.put("The specified signal does not appear to contain hypnogram data.", "Le signal spécifié ne semble pas contenir d'information sur l'hypnogramme.");
        hashMap.put("The specified file does not appear to correspond to the currently loaded data.", "Le fichier spécifié ne semble pas correspondre aux données chargées.");
        hashMap.put("Continuing will replace the content of the specified file.\nAre you sure you want to continue ?", "Continuer effacera le contenu du fichier spécifié.\nEtes-vous certain de vouloir continuer ?");
        hashMap.put("Please input an integer between 1 and 60 inclusive.", "Veuillez entrer un entier entre 1 et 60 inclus.");
        hashMap.put("Cannot create the file : please specify a correct filename\nand verify that you have the rights to write to it.", "Ne peux créer le fichier : veuillez spécifier un nom correct\net vérifier que vous avez le droit d'y écrire.");
        hashMap.put("Hypnogram record", "Enregistrement de l'hypnogramme");
        hashMap.put("Please verify that you have selected a correct signal input for each channel.", "Veuillez vérifier que chaque chaîne ait un signal correct associé.");
        hashMap.put(" do not correspond.\nPlease verify that they have the same sampling frequency.", " ne correspondent pas.\nVeuillez vérifier qu'ils ont la même fréquence d'échantillonage.");
        hashMap.put("The signals of the channel n°", "Les signaux de la chaîne n°");
        hashMap.put("Montage creation", "Création d'un montage");
        hashMap.put("Unipolar", "Unipolaire");
        hashMap.put("Bipolar", "Bipolaire");
        hashMap.put("Quit", "Quitter");
        hashMap.put("Epochs from :", "Epoques de :");
        hashMap.put("Output type :", "Type de sortie :");
        hashMap.put("Tools", "Outils");
        hashMap.put("FFT analysis", "Analyses FFT");
        hashMap.put("FFT text file", "Fichier FFT de type texte");
        hashMap.put("Sampling Rate", "Fréquence d'échentillonage");
        hashMap.put("Increase size", "Agrandir");
        hashMap.put("Decrease size", "Diminuer");
        hashMap.put("Reset size", "Réinitialiser");
        hashMap.put("Reverse", "Inverser");
        hashMap.put("Cancel", "Annuler");
        hashMap.put("Assign this montage to ", "Assigner ce montage à ");
        hashMap.put("Please choose the file you want to load.", "Veuillez choisir le fichier que vous souhaitez charger.");
        hashMap.put("Please choose the file you want to save to.", "Veuillez choisir le fichier où vous voulez sauver.");
        hashMap.put("Please choose the wanted granularity, in seconds :", "Veuillez entrer la granularité souhaitée, en secondes :");
        hashMap.put("Error : please verify that you have specified a correct filename\n", "Erreur : veuillez vérifier que vous avez spécifié un nom de fichier correct\n");
        hashMap.put("and that you have write access on it.", "et que vous avez les droits en écriture dessus.");
        hashMap.put("Error : please verify that you have specified a correct Precise Scoring filename\n", "Erreur : veuillez vérifier que vous avez spécifié un nom de fichier Scorage Fin valide\n");
        hashMap.put("The specified file already exits.\n", "Le fichier spécifié existe déjà.\n");
        hashMap.put("Continuing will erase its content.\nAre you sure you want to continue ?", "Continuer effacera son contenu.\nEtes-vous sûr de vouloir continnuer ?");
        hashMap.put("Error : please input a correct integer value for the granularity.", "Erreur : veuillez entrer une valeur entière correcte pour la granularité.");
        hashMap.put("and that you have write access on it.", "et que vous avez des droits en écriture dessus.");
        hashMap.put("Informations", "Informations");
        hashMap.put("Error : the specified file does not seem to contain precise scoring data.", "Erreur : le fichier précisé ne semble pas contenir de données de scorage fin.");
        hashMap.put("Export channel to text file", "Exporter la chaîne au format texte");
        hashMap.put("Export Hypnogram To Text", "Exporter l'hypnogramme au format texte");
        hashMap.put("Export File To Text", "Exporter fichier au format texte");
        hashMap.put("Export to Text", "Exporter en Texte");
        hashMap.put("Complete channel", "Chaîne complete");
        hashMap.put("By duration (s)", "Par durée (s)");
        hashMap.put("Channel", "Chaîne");
        hashMap.put("Epochs from", "Epoques de");
        hashMap.put("to", "à");
        hashMap.put("Output file", "Fichier de sortie");
        hashMap.put("Version", "Version");
        hashMap.put("Text file", "Fichier texte");
        hashMap.put("The specified file already exists.\nAre you sure you want to replace its content ?", "Le fichier spécifié existe déjà.\nEtes-vous certain de vouloir le remplacer ?");
        hashMap.put("The operation has been completed successfully !", "L'opération s'est achevé avec succés !");
        hashMap.put("Success", "Succés");
        hashMap.put("Please verify that you have entered valid parameters.", "Veuillez vérifier que vous avez entré des paramètres valides.");
        hashMap.put("I/O Error : please verify that you have the right to write to the specified file.", "Erreur E/S : veuillez vérifier que vous avez le droit d'écrire dans le fichier spécifié.");
        hashMap.put("The operation has been aborted on user request.", "L'opération a été annulée à la demande de l'utilisateur.");
        hashMap.put("Aborted", "Annulé");
        hashMap.put("Unscored", "Non scorée");
        hashMap.put("Help", "Aide");
        hashMap.put("About", "A propos de");
        hashMap.put("About jEDF", "A propos de jEDF");
        hashMap.put("Contribution", "Contribution");
        hashMap.put("Licence", "Licence");
        hashMap.put("Keyboard shortcuts", "Raccourics clavier");
        hashMap.put("General navigation", "Navigation générale");
        hashMap.put("Go to the previous epoch", "Aller à l'époque précédente");
        hashMap.put("Go to the next epoch", "Aller à l'époque suivante");
        hashMap.put("Go to the beginning", "Aller au début");
        hashMap.put("Go to the end", "Aller à la fin");
        hashMap.put("Hypnogram stages", "Stades de l'hypnogramme");
        hashMap.put("Awake", "Eveil");
        hashMap.put("Movement", "Mouvement");
        hashMap.put("Stage 1 to 4", "Stade 1 à 4");
        hashMap.put("REM", "Sommeil paradoxal");
        hashMap.put("Unscored", "Non scoré");
        hashMap.put("Precise scoring", "Scorage fin");
        hashMap.put("Precise scoring file", " Fichier scorage fin");
        hashMap.put("Go to the previous precise scoring label", "Aller à l'étiquette précédente du scorage fin");
        hashMap.put("Go to the next precise scoring label", "Aller à l'étiquette suivante du scorage fin");
        hashMap.put("Set a custom label", "Définir une étiquette personnalisée");
        hashMap.put("Set state awake, opened eyes", "Définir état veille, yeux ouverts");
        hashMap.put("Set state awake, closed eyes", "Définir état veille, yeux fermés");
        hashMap.put("Set artifact", "Définir artefact");
        hashMap.put("Set state vertex peak", "Définir pointe vertex");
        hashMap.put("Set state spindle", "Définir fuseau");
        hashMap.put("Set state K complex", "Définir complexe K");
        hashMap.put("Set state Saw Tooth", "Définir Dent de scie");
        hashMap.put("Duplicate previous state", "Dupliquer état précédent");
        hashMap.put("home key", "touche HOME");
        hashMap.put("end key", "touche FIN");
        hashMap.put("numpad 0", "pavé numérique 0");
        hashMap.put("numpad 1 to 4", "pavé numérique 1 à 4");
        hashMap.put("numpad 5", "pavé numérique 5");
        hashMap.put("numpad 6", "pavé numérique 6");
        hashMap.put("down", "bas");
        hashMap.put("up", "haut");
        hashMap.put("space", "espace ");
        hashMap.put("numpad 7", "pavé numérique 7");
        hashMap.put("numpad 8", "pavé numérique 8");
        hashMap.put("numpad 9", "pavé numérique 9");
        hashMap.put("1 to 4", "1 à 4");
        hashMap.put("Invert", "Inverser");
        hashMap.put("FFT Analysis", "Analyse FFT");
        hashMap.put("Interpolation", "Interpolation");
        hashMap.put("First epoch", "Première époque");
        hashMap.put("Previous epoch", "Epoque précédente");
        hashMap.put("Next epoch", "Epoque suivante");
        hashMap.put("Last epoch", "Dernière époque");
        hashMap.put("Raw", "brut");
        hashMap.put("Sleep bands", "Bandes de sommeil");
        hashMap.put("Visualization mode :", "Mode de visualisation");
        hashMap.put("Auto scroll", "Avance automatique");
        hashMap.put("", "");
        hashMap.put("", "");
    }

    /**
     * constructor
     */
    public MyResource_FR() {
        initialize();
    }

    public Object handleGetObject(String key) {
        String str = (String) hashMap.get(key);
        if (str == null) {
            System.out.println("traduire en français : \"" + key + "\"");
            return key;
        }
        return str;
    }

    public Enumeration getKeys() {
        return null;
    }
}
