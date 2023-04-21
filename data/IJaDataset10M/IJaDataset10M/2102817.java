package agent;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class Job_cpu extends Job_seuil {

    public Job_cpu() {
        super();
        setDaemon(true);
        try {
            cmd = Agent.AgentIni.LireValeur("scripts", "cpu", "acg_sur_cpuseuil.sh");
        } catch (java.io.IOException err) {
            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
        }
    }

    public Job_cpu(String dat, String job, String infos) throws UnknownHostException, IOException {
        super(dat, job, infos);
        setDaemon(true);
        try {
            cmd = Agent.AgentIni.LireValeur("scripts", "cpu", "acg_sur_cpuseuil.sh");
        } catch (java.io.IOException err) {
            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
        }
    }

    public void Init(String chemin, String os, String scripts, String dat) {
        super.Init(chemin, os, scripts, dat);
        type_thread = "cpu";
    }

    public void run() {
        boolean localpause = false;
        super.run();
        numbloc = new String(tabvaleur[9]);
        fichier = CreerLog(new String(dat + "cpu_" + numbloc));
        fichier_commande = new File(dat + "cpu_" + numjob + File.separator + "cmd.flg");
        fichier_commande.delete();
        if (!fichier.equals("##")) {
            if (tabvaleur[2].equals("cpu")) {
                ChargerValeurs(tabvaleur[9], new Integer(tabvaleurinfos[17]).intValue(), fichier, new String(dat + "cpu_" + numbloc + File.separator + "job.prm"), cmd, tabvaleurinfos[2], new Integer(tabvaleurinfos[3]).intValue(), new Integer(tabvaleurinfos[4]).intValue(), TransBool(tabvaleurinfos[11]), new Integer(tabvaleurinfos[12]).intValue(), tabvaleurinfos[13], tabvaleurinfos[14], tabvaleurinfos[15], tabvaleurinfos[16], TransBool(tabvaleurinfos[5]), new Integer(tabvaleurinfos[6]).intValue(), tabvaleurinfos[7], tabvaleurinfos[8], tabvaleurinfos[9], tabvaleurinfos[10]);
                InitHisto(TransBool(tabvaleurinfos[18]), 1, new Integer(tabvaleurinfos[20]).intValue(), new Integer(tabvaleurinfos[21]).intValue(), 0, new Integer(tabvaleurinfos[19]).intValue());
                acgtools_agent.AcgIO.SortieLog("Job_cpu::Run ## Lancement du job cpu = " + job + " numro = " + numjob, fichier, hostname);
                entete = "CPU ";
                while (!stop) {
                    while ((exec) && (!fichier_commande.exists())) {
                        localpause = true;
                        try {
                            trait_encours = true;
                            traite_seuil();
                            if (flagenvoi) {
                                int maxboucle = tableau.size();
                                String chainehisto;
                                chainehisto = new Integer(maxboucle).toString() + "#";
                                for (int boucle = 0; boucle < maxboucle; boucle++) {
                                    String[] tableau_traiter = (String[]) tableau.get(boucle);
                                    int maxboucle2 = tableau_traiter.length;
                                    if (boucle != 0) {
                                        chainehisto += ";";
                                    } else {
                                        chainehisto += new Integer(maxboucle2 - 3).toString() + "#";
                                    }
                                    chainehisto += tableau_traiter[0];
                                    for (int boucle2 = 1; boucle2 < maxboucle2; boucle2++) {
                                        chainehisto += ";" + tableau_traiter[boucle2];
                                    }
                                }
                                tableau.clear();
                                RAZtableau();
                                flagenvoi = false;
                            }
                            trait_encours = false;
                            sleep(periode * 1000);
                        } catch (IOException err) {
                            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
                        } catch (InterruptedException err) {
                            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
                        } catch (ParseException err) {
                            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
                        }
                    }
                    if (localpause) {
                        acgtools_agent.AcgIO.SortieLog("Job_cpu::run ## Sortie du bloc EXEC, mise en pause", fichier, hostname);
                        localpause = false;
                    }
                    try {
                        sleep(periode * 1000);
                    } catch (InterruptedException err) {
                        acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
                    }
                }
            }
        }
    }

    public void Recharger(String job, String infos) {
        Suspendre();
        acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## appel a suspendre", fichier, hostname);
        try {
            while (trait_encours) {
                try {
                    sleep(100);
                } catch (InterruptedException err) {
                    acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
                }
            }
            acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## Lancement de la recherche du job", fichier, hostname);
            nb_champs = Decompose("~", job, tabvaleur);
            acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## Decomposition du job", fichier, hostname);
            nb_champsinfos = Decompose("~", infos, tabvaleurinfos);
            acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## Decomposition des infos du job", fichier, hostname);
            tabvaleur[0] = tabvaleur[0].substring(tabvaleur[0].indexOf('=') + 1, tabvaleur[0].length());
            numjob = new String(job.substring(0, job.indexOf('=')));
            ChargerValeurs(tabvaleur[9], new Integer(tabvaleurinfos[17]).intValue(), fichier, new String(dat + "cpu_" + numbloc + File.separator + "job.prm"), "acg_sur_cpuseuil.sh ", tabvaleurinfos[2], new Integer(tabvaleurinfos[3]).intValue(), new Integer(tabvaleurinfos[4]).intValue(), TransBool(tabvaleurinfos[11]), new Integer(tabvaleurinfos[12]).intValue(), tabvaleurinfos[13], tabvaleurinfos[14], tabvaleurinfos[15], tabvaleurinfos[16], TransBool(tabvaleurinfos[5]), new Integer(tabvaleurinfos[6]).intValue(), tabvaleurinfos[7], tabvaleurinfos[8], tabvaleurinfos[9], tabvaleurinfos[10]);
            acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## Chargement des nouvelles valeurs", fichier, hostname);
            InitHisto(TransBool(tabvaleurinfos[18]), 1, new Integer(tabvaleurinfos[20]).intValue(), new Integer(tabvaleurinfos[21]).intValue(), 0, new Integer(tabvaleurinfos[19]).intValue());
            acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## Modification des parametres de l'historisation", fichier, hostname);
        } catch (java.io.IOException err) {
            acgtools_agent.AcgIO.TraiterErreur(err, fichier, hostname);
        }
        Demarrer();
        acgtools_agent.AcgIO.SortieLog("Job_cpu::Recharger ## appel a Demarrer", fichier, hostname);
    }
}
