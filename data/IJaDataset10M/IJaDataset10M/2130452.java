package acgtools_agent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Suite de mthodes pour la gestion des entres/sorties (saisies, impressions...). <p>
 * @version 2.00x
 */
public final class AcgIO {

    public static boolean debug = true;

    private static long taillelogmax = 5000;

    private static int maxarchive = 5;

    private static boolean archive = true;

    /**
 * Constructeur. Normalement, il est inutile d'instancier cette classe car les mthodes sont <b>static</b>.
 */
    public AcgIO() {
    }

    /**
 * La mthode Evaluer permet de modifier les squences particulires dans une chaine de caractres. 
 * Les caractres \ sont remplacs par \\ pour les insertions dans la base de donnes, les caractres
 * ` sont remplacs par \` pour viter les problmes de scripts sous unix.
 * @param <b>chaine</b> chaine de caractres  valuer
 * @return Une chaine de caractres qui reprends au minimum la chaine initiale et qui inclus les modifications
 */
    public static String Evaluer(String chaine) {
        StringBuffer retour = new StringBuffer(2048);
        if (chaine != null) {
            for (int boucle = 0; boucle < chaine.length(); boucle++) {
                switch(chaine.charAt(boucle)) {
                    case '\'':
                        retour.append("\\'");
                        break;
                    case '`':
                        retour.append("\\'");
                        break;
                    default:
                        retour.append(chaine.charAt(boucle));
                }
            }
        }
        return new String(retour);
    }

    /**
 * La mthode ForceNull permet de renvoyer une chaine vide si la chaine parametre est null.
 * Si le parametre est une chaine valide, il est re-transmis tel quel.
 * @param <b>chaine</b> la chaine de caractres en parametre (peut tre null)
 * @return si parametre est null, une chaine vide (""), la chaine parametre sinon.
 */
    public static String ForceNull(String chaine) {
        String retour;
        if (chaine != null) retour = new String(chaine); else retour = "";
        return retour;
    }

    /**
 * La mthode PlacerGestionLog permet d'initialiser le systme d'archivage d'ACGVision.
 * La mthode initialise les proprits <b>prives</b> archive, maxarchive et taillelogmax. Ces prorits sont utilises par la
 * mthode SortieLog pour grer les fichiers de logs.<p>
 * Les archives agents et parametreur sous ACGVision sont prises en compte par les routines Log. En cas 
 * d'activation des fichiers de logs, les fichiers se remplissent jusqu' atteindre la taille maxi dfinie par
 * la proprit taillearch. Le fichier de logs est ensuite archiv si le drapeau arch est positionn (pos  <b>vrai</b>)
 * Les archives prennent ensuite un suffixe de type entier jusqu' atteindre maxarch.<p>
 * <b>Dans le cas d'une activation, le fichier archiv le plus rcent est clui qui possde le suffixe le plus grand</b>
 * @param <b>arch</b> bollen indiquant si les archives sont gres ou pas
 * @param <b>maxarch</b> indique le nombre maximum d'archives
 * @param <b>taillearch</b> indique la taille maximale en octets d'une archive
 */
    public static void PlacerGestionLog(boolean arch, int maxarch, long taillearch) {
        archive = arch;
        maxarchive = maxarch;
        taillelogmax = taillearch;
    }

    /**
 * La mthode SortieLog permet l'criture d'une chaine de caractres dans le fichier log.
 * C'est cette mthode qui  chaque nouvelle ecriture compare la taille du fichier de log avec la taille maximum
 * dfinie par la mthode PlacerGestionLog. Dans le cas d'un dpassement de taille, les mises en archives et les
 * ventuelles permutations de fichiers en cas de dpassement du nombre d'archives sont aussi grs par la mthode.
 * @param <b>chaine</b> la chine  inscrire dans le fichier de log.
 * @param <b>fichier</b> le nom du fichier de log (chemin absolu ou relatif)
 * @param <b>hostname</b> le nom de la machine qui sera inscrit au dbut de chaque ligne de log
 */
    public static void SortieLog(String chaine, String fichier, String hostname) {
        File flog, flogtmp, flogtmp2;
        String chemin;
        if (debug) {
            try {
                flog = new File(fichier);
                chemin = flog.getParent();
                String flognom = flog.getName();
                if ((flog.exists()) && (archive)) {
                    long taille = flog.length();
                    if (taille > taillelogmax) {
                        int boucle = 1;
                        flogtmp = new File(chemin + File.separator + flognom + ".old_" + boucle);
                        while (flogtmp.exists() && (boucle < maxarchive)) {
                            boucle++;
                            flogtmp = new File(chemin + File.separator + flognom + ".old_" + boucle);
                        }
                        if (flogtmp.exists() && (boucle == maxarchive)) {
                            flogtmp = new File(chemin + File.separator + flognom + ".old_1");
                            if (flogtmp.delete()) {
                                for (boucle = 2; boucle <= maxarchive; boucle++) {
                                    flogtmp2 = new File(chemin + File.separator + flognom + ".old_" + boucle);
                                    flogtmp = new File(chemin + File.separator + flognom + ".old_" + (boucle - 1));
                                    flogtmp2.renameTo(flogtmp);
                                }
                                flogtmp = new File(chemin + File.separator + flognom + ".old_" + maxarchive);
                            }
                        } else flog.renameTo(flogtmp);
                    }
                }
                BufferedWriter file = new BufferedWriter(new FileWriter(fichier, true));
                file.write(RetourneDate(new java.util.GregorianCalendar()) + " - " + hostname + " : " + chaine);
                file.newLine();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (java.util.MissingResourceException e) {
                e.printStackTrace();
            }
        }
    }

    /**
 * La mthode RetourneDate retourne une chaine formate pour une bonne insertion dans PostGreSQL.
 * @param <b>ladate</b> date au format java.util.GregorianCalendar
 * @return Chaine formate. Format : AAAA/MM/JJ HH:MM:SS(+-)OO o +- est le signe de dcalage de l'offset
 * et OO est l'offset en heures.
 */
    public static String RetourneDate(java.util.GregorianCalendar ladate) {
        String temp;
        String retour;
        retour = new String(new Integer(ladate.get(java.util.GregorianCalendar.YEAR)).toString());
        retour += "/";
        temp = "0" + (new Integer(ladate.get(java.util.GregorianCalendar.MONTH) + 1).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        retour += "/";
        temp = "0" + (new Integer(ladate.get(java.util.GregorianCalendar.DAY_OF_MONTH)).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        retour += " ";
        temp = "0" + (new Integer(ladate.get(java.util.GregorianCalendar.HOUR_OF_DAY)).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        retour += ":";
        temp = "0" + (new Integer(ladate.get(java.util.GregorianCalendar.MINUTE)).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        retour += ":";
        temp = "0" + (new Integer(ladate.get(java.util.GregorianCalendar.SECOND)).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        int valoffset = new Integer(ladate.get(java.util.GregorianCalendar.DST_OFFSET) / (3600000)).intValue();
        valoffset += new Integer(ladate.get(java.util.GregorianCalendar.ZONE_OFFSET) / (3600000)).intValue();
        if (valoffset > 0) retour += "+"; else retour += "-";
        temp = "0" + (new Integer(java.lang.Math.abs(valoffset)).toString());
        retour += temp.substring(temp.length() - 2, temp.length());
        return retour;
    }

    /**
 * La mthode Retourne_Dateactuelle permet de renvoyer une date formate par la mthode 
 * RetourneDate, mais en utilisant le groupe date/heure actuel (now)
 * @return Chaine formate. Format : AAAA/MM/JJ HH:MM:SS(+-)OO o +- est le signe de dcalage de l'offset
 * et OO est l'offset en heures.
 */
    public static java.util.GregorianCalendar Retourne_Dateactuelle() {
        String retour;
        java.util.GregorianCalendar ladate = new java.util.GregorianCalendar(java.util.Locale.FRENCH);
        retour = RetourneDate(ladate);
        return ladate;
    }

    /**
 * La mthode Retourne_ID permet de retourner un identifiant unique bas sur le temps en cours
 * @return une chaine reprsentant un ID de type Long
 */
    public static String Retourne_ID() {
        String retour;
        java.util.Calendar ladate = new java.util.GregorianCalendar(java.util.Locale.FRENCH);
        long temp = ladate.getTime().getTime();
        retour = new Long(temp).toString();
        return retour;
    }

    /**
 * La mthode Chaine retourne une concatnation d'un groupe date/heure et d'un parametre. Cette mthode
 * est trs utilise dans les fichiers de logs pour dater les entres
 * @param <b>chaine</b> la chaine de caractres  coller au groupe date/heure
 * @return Une chaine compose du groupe date/heure (now) et du parametre spars par un espace
 */
    public static String Chaine(String chaine) {
        return new String(Retourne_Dateactuelle() + " " + chaine);
    }

    /**
 * La mthode ChaineEx retourne une concatnation d'un nombre spcifique d'espaces, du groupe date/heure et d'une chaine
 * passe en paramtres.
 * @param <b>chaine</b> la chaine de caractres  coller aux espaces et groupes date/heure
 * @param <b>nbre</b> le nombre d'espaces multipli par 3 de dcalage vers la droite
 * @return Une chaine compose du nombre de fois 3 espaces, du groupe date/heure (now) et du parametre spars par un espace
 */
    public static String ChaineEx(String chaine, int nbre) {
        String deplace;
        if (nbre > 0) {
            deplace = "";
            for (int boucle = 0; boucle < nbre; boucle++) {
                deplace = deplace + "   ";
            }
        } else deplace = "";
        return new String(deplace + Retourne_Dateactuelle() + " " + chaine);
    }

    /**
 * La mthode Information_OS renvoie le systme sur lequel la machine virtuelle fonctionne
 * Les ventuels espaces sont remplacs par des _. Par exemple, Windows 2000 devient Windows_2000
 * @return Une chaine de caractre nommant le systme d'exploitation courant
 */
    public static final String Informations_OS() {
        return new String(System.getProperty("os.name").replace(' ', '_'));
    }

    /**
 * La mthode InformationsJVM retourne une concatnation de tous les paramtres de la JVM courante
 * @return Une chaine de caractre comportant les informations suivantes :
 * <ul>
 * <ui> java.version
 * <ui> java.vendor
 * <ui> java.home
 * <ui> java.vm.specification.version
 * <ui> java.vm.specification.vendor
 * <ui> java.vm.specification.name
 * <ui> java.vm.version
 * <ui> java.vm.vendor
 * <ui> java.vm.name
 * <ui> java.specification.version
 * <ui> java.specification.vendor
 * <ui> java.specification.name
 * <ui> os.name
 * <ui> os.arch
 * <ui> os.version
 * <ui> file.separator
 * <ui> path.separator
 * </ul>
 */
    public static String InformationsJVM() {
        String java_version = System.getProperty("java.version");
        if (debug) System.out.println("java->Version : " + java_version);
        String java_vendor = System.getProperty("java.vendor");
        if (debug) System.out.println("java->Vendeur : " + java_vendor);
        String java_home = System.getProperty("java.home");
        if (debug) System.out.println("java->Home : " + java_home);
        String java_vm_specification_version = System.getProperty("java.vm.specification.version");
        if (debug) System.out.println("java->vm->specification->version : " + java_vm_specification_version);
        String java_vm_specification_vendor = System.getProperty("java.vm_specification.vendor");
        if (debug) System.out.println("java->vm->specification->vendor : " + java_vm_specification_vendor);
        String java_vm_specification_name = System.getProperty("java.vm.specification.name");
        if (debug) System.out.println("java->vm->specification->name : " + java_vm_specification_name);
        String java_vm_version = System.getProperty("java.vm.version");
        if (debug) System.out.println("java->vm->version : " + java_vm_version);
        String java_vm_vendor = System.getProperty("java.vm.vendor");
        if (debug) System.out.println("java->vm->vendor : " + java_vm_vendor);
        String java_vm_name = System.getProperty("java.vm.name");
        if (debug) System.out.println("java->vm->name : " + java_vm_name);
        String java_specification_version = System.getProperty("java.specification.version");
        if (debug) System.out.println("java->specification->version : " + java_specification_version);
        String java_specification_vendor = System.getProperty("java.specification.vendor");
        if (debug) System.out.println("java->specification->vendor : " + java_specification_vendor);
        String java_specification_name = System.getProperty("java.specification.name");
        if (debug) System.out.println("java->specification->name : " + java_specification_name);
        String os_name = System.getProperty("os.name");
        if (debug) System.out.println("Nom de l'OS : " + os_name);
        String os_arch = System.getProperty("os.arch");
        if (debug) System.out.println("Architecture de l'OS : " + os_arch);
        String os_version = System.getProperty("os.version");
        if (debug) System.out.println("Version de l'OS : " + os_version);
        String file_separator = System.getProperty("file.separator");
        if (debug) System.out.println("Sparateur de fichier : " + file_separator);
        String path_separator = System.getProperty("path.separator");
        if (debug) System.out.println("Sparateur de chemin : " + path_separator);
        String retour = new String(java_version + "#" + java_vendor + "#" + java_home + "#" + java_vm_specification_version + "#" + java_vm_specification_vendor + "#" + java_vm_specification_name + "#" + java_vm_version + "#" + java_vm_vendor + "#" + java_vm_name + "#" + java_specification_version + "#" + java_specification_vendor + "#" + java_specification_name + "#" + os_name + "#" + os_arch + "#" + os_version + "#" + file_separator + "#" + path_separator);
        return retour;
    }

    /**
 * La mthode InformationsIP renvoie une concatnation reprsentant l'adresse IP et le nom de la machine courante
 * @return Une chaine de caractres compose de l'adresse IP et du nom de machine spars par le caractre #
 */
    public static String InformationsIP() throws UnknownHostException {
        InetAddress Inet = InetAddress.getLocalHost();
        String IP_adresse = Inet.getHostAddress();
        if (debug) System.out.println("Host->Adresse IP : " + IP_adresse);
        String IP_nom = Inet.getHostName();
        if (debug) System.out.println("Host->Nom IP : " + IP_nom);
        return new String(IP_adresse + "#" + IP_nom);
    }

    /**
 * La mthode TraiterErreur permet d'ecrire dans un fichier de log une erreur gnre  partir d'une exception leve.
 * @param <b>err</b> l'exception leve
 * @param <b>fichier</b> le nom du fichier de log (chemin absolu de prfrence)
 * @param <b>nom</b> nom de la machine pour faire concorder l'appel  la mthode SortieLog
 */
    public static void TraiterErreur(Exception err, String fichier, String nom) {
        SortieLog("Mthode AcgIO::TraiterErreur - Traitement d'exception", fichier, nom);
        SortieLog("     ## " + err.toString() + err.getMessage(), fichier, nom);
        err.printStackTrace();
        System.out.println(err.getCause());
        System.out.println(err.getMessage());
    }
}
