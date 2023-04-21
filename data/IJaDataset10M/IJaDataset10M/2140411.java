package mf.torneo.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import mf.torneo.model.Categorie;
import mf.torneo.model.Gironi;
import mf.torneo.model.Iscrizioni;
import mf.torneo.model.Play;
import mf.torneo.model.Societa;
import mf.torneo.model.Team;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:maurofgn@tiscali.it">Mauro Fugante</a><br>
 *         Play persistence management
 */
public class PlayLoadLogic extends AbstractLogic {

    private static final long serialVersionUID = -1407633768270671786L;

    protected static final Log LOG = LogFactory.getLog(PlayLoadLogic.class);

    private Categorie categoria;

    private int nrGironi = 0;

    private Date giorno = new Date();

    private int oraInizio = 9;

    private int minutiInizio = 30;

    private int durataPartita = 20;

    private static final int NR_TEAMS_OTTIMALE_PER_GIRONE = 5;

    private static final int NR_TEAMS_MIN = 2;

    private int totTeam;

    public PlayLoadLogic() {
    }

    /**
	 * esegue le operazioni per la gestione dell'errore
	 * 
	 * @param e
	 *            Exception
	 */
    private void setError(Exception exception) {
        setRollBackOnly();
        LOG.error(exception.getMessage());
        addActionError(exception.getMessage());
    }

    /**
	 * cancella i gironi di una categoria e tutte le partite associate al girone
	 *
	 */
    public void clearGironi() {
        if (categoria == null) return;
        try {
            LOG.debug("clear Gironi");
            Query query = getSession().createQuery("from Gironi as g where g.categorie.idcate=?").setInteger(0, categoria.getIdcate().intValue());
            List gironi = query.list();
            for (Iterator iter = gironi.iterator(); iter.hasNext(); ) {
                Gironi element = (Gironi) iter.next();
                Set plays = element.getPlays();
                if (plays != null) for (Iterator iterator = plays.iterator(); iterator.hasNext(); ) {
                    Play onePlay = (Play) iterator.next();
                    getSession().delete(onePlay);
                }
                getSession().delete(element);
            }
        } catch (HibernateException e) {
            setError(e);
        }
    }

    /**
	 * cancella i team di una categoria e tutte le partite associate al girone
	 *
	 */
    public void clearTeam() {
        if (categoria == null) return;
        clearGironi();
        try {
            LOG.debug("clear Team");
            Query query = getSession().createQuery("from Team as g where g.categorie.idcate=?");
            query.setInteger(0, categoria.getIdcate().intValue());
            List gironi = query.list();
            for (Iterator iter = gironi.iterator(); iter.hasNext(); ) {
                Team element = (Team) iter.next();
                getSession().delete(element);
            }
        } catch (HibernateException e) {
            setError(e);
        }
    }

    /**
	 * carica i team sulla base delle iscrizioni
	 */
    public int loadTeam() {
        Query query = null;
        int retVal = 0;
        try {
            if (categoria == null) query = getSession().createQuery("from Iscrizioni"); else {
                query = getSession().createQuery("from Iscrizioni as i where i.categorie.idcate=?");
                query.setInteger(0, categoria.getIdcate().intValue());
            }
            List iscrizioni = query.list();
            LOG.debug("load teams");
            for (Iterator iter = iscrizioni.iterator(); iter.hasNext(); ) {
                Iscrizioni element = (Iscrizioni) iter.next();
                Societa societa = element.getSocieta();
                int nrTeam = element.getNrteam().intValue();
                for (int i = 0; i < nrTeam; i++) {
                    String nome = societa.getNome();
                    if (nrTeam > 1) nome += " (" + (i + 1) + ")";
                    Team team = new Team(null, nome, societa.getCitta(), societa.getResponsabile(), societa.getTel(), societa.getCellulare(), societa.getEmail(), element.getCategorie(), societa, null, null, null, null);
                    getSession().save(team);
                    retVal++;
                }
            }
        } catch (HibernateException e) {
            setError(e);
        }
        return retVal;
    }

    /**
	 * ritorna una lettera da A a Z
	 * @param count
	 * @return lettera da A a Z
	 */
    public String getLettar(int count) {
        int asciiCode = (count % 26) + 65;
        char[] asciiChar = { (char) asciiCode };
        String asciiChar_string = new String(asciiChar);
        return asciiChar_string;
    }

    /**
	 * per una categoria, azzera tutta la situazione preesistente, per poi
	 * creare le partite dei gironi eliminatori
	 */
    public void bldPlay() {
        if (categoria == null) return;
        clearTeam();
        loadTeam();
        LOG.debug("crea le partite dei gironi eliminatori");
        List altriGironi = null;
        try {
            categoria = (Categorie) getSession().get(Categorie.class, categoria.getIdcate());
            altriGironi = (List) getSession().createQuery("from Gironi where campo is not null").list();
        } catch (HibernateException e) {
            setError(e);
        }
        List teamList = getList(categoria);
        if (teamList == null || teamList.size() == 0) return;
        totTeam = teamList.size();
        if (nrGironi == 0) nrGironi = getNrGironiDefault(totTeam);
        List gironiList = divisioneInGironi(nrGironi, totTeam);
        Integer livello = new Integer(logBase2(getTipoFinaleDaGironi(gironiList.size())) + 1);
        for (int nrGirone = 0, totGironi = gironiList.size(); nrGirone < totGironi; nrGirone++) {
            GregorianCalendar oggi = getData();
            List teamGironeList = (List) gironiList.get(nrGirone);
            Gironi girone = new Gironi();
            girone.setCategorie(categoria);
            girone.setDescrizione(getText("girone") + " " + getLettar(nrGirone));
            girone.setPlaytime(new Integer(getDurataPartita()));
            girone.setTeamnr(new Integer(teamGironeList.size()));
            int campoAssegnato = findCampoLibero(altriGironi);
            girone.setCampo(new Integer(campoAssegnato));
            girone.setLivello(livello);
            girone.setEliminatorie(livello);
            girone.setPerdente(new Integer(0));
            try {
                getSession().save(girone);
            } catch (HibernateException e) {
                setError(e);
                return;
            }
            if (altriGironi == null) altriGironi = new ArrayList();
            altriGironi.add(girone);
            int totPlay = getNrPlay(teamGironeList.size());
            ArrayList sacco = new ArrayList(teamGironeList.size());
            for (int nrTeam = 0, totTeam = teamGironeList.size(); nrTeam < totTeam; nrTeam++) {
                Team team = (Team) teamGironeList.get(nrTeam);
                sacco.add(nrTeam, new TeamPlayed(team));
            }
            for (int nrPlay = 0; nrPlay < totPlay; nrPlay++) {
                Coppia coppia = getCoppia(sacco, nrPlay);
                Date time = oggi.getTime();
                oggi.add(GregorianCalendar.MINUTE, getDurataPartita());
                Play play = new Play(girone.getCampo(), "", time, girone, coppia.getTeam1(), coppia.getTeam2(), new Integer(nrPlay));
                try {
                    getSession().save(play);
                    getSession().flush();
                } catch (HibernateException e) {
                    setError(e);
                }
            }
        }
    }

    /**
	 * 
	 * @param sacco sacco contenente tutti i team del girone con il nr partite assegnate
	 * @return team con il nr minimo di partite giocate
	 */
    private Coppia getCoppia(ArrayList sacco, int seqPartita) {
        TeamPlayed teamPlayed1 = null;
        for (int nrTeam = 0, totTeam = sacco.size(); nrTeam < totTeam; nrTeam++) {
            TeamPlayed teamPlayed = (TeamPlayed) sacco.get(nrTeam);
            if (teamPlayed1 == null || teamPlayed1.getTotPlay() > teamPlayed.getTotPlay()) teamPlayed1 = teamPlayed;
        }
        TeamPlayed teamPlayed2 = null;
        for (int nrTeam = 0, totTeam = sacco.size(); nrTeam < totTeam; nrTeam++) {
            TeamPlayed teamPlayed = (TeamPlayed) sacco.get(nrTeam);
            if ((teamPlayed2 == null || teamPlayed2.getTotPlay() > teamPlayed.getTotPlay()) && !teamPlayed.equals(teamPlayed1) && teamPlayed.isFree(teamPlayed1.getTeam())) teamPlayed2 = teamPlayed;
        }
        teamPlayed1.addPlay(teamPlayed2.getTeam(), seqPartita);
        teamPlayed2.addPlay(teamPlayed1.getTeam(), seqPartita);
        return new Coppia(teamPlayed1.getTeam(), teamPlayed2.getTeam());
    }

    /**
	 * imposta la data con data ed ora iniziale e minuti = 0
	 * @return
	 */
    private GregorianCalendar getData() {
        GregorianCalendar oggi = new GregorianCalendar();
        oggi.setTime(getGiorno());
        oggi.set(GregorianCalendar.HOUR_OF_DAY, getOraInizio());
        oggi.set(GregorianCalendar.MINUTE, getMinutiInizio());
        oggi.set(GregorianCalendar.SECOND, 0);
        oggi.set(GregorianCalendar.MILLISECOND, 0);
        return oggi;
    }

    /**
	 * combinazioni di un numero in classe 2 
	 * @param qtaTeam nr team in gioco
	 * @return nr di partite totale da disputare
	 */
    public int getNrPlay(int nrTeam) {
        int nrPlay = 0;
        int teamxPlay = 2;
        for (int i = nrTeam - teamxPlay + 1; i > 0; i--) nrPlay += i;
        return nrPlay;
    }

    /**
	 * divide tutti team in gironi
	 * @param stack
	 * @param nrGironi nr Gironi che si vogliono ottenere
	 * @param totTeam nr di team di categoria
	 * @return
	 */
    private List divisioneInGironi(int nrGironi, int totTeam) {
        ArrayList gironi = new ArrayList(nrGironi);
        for (int i = 0; i < nrGironi; i++) {
            gironi.add(i, new ArrayList());
        }
        Stack stack = getLuck();
        for (int count = 0; !stack.isEmpty(); ) {
            Societa societa = (Societa) stack.pop();
            Set teams = societa.getTeams();
            for (Iterator iter = teams.iterator(); iter.hasNext(); ) {
                Team oneTeam = (Team) iter.next();
                if (oneTeam.getCategorie().getIdcate().equals(categoria.getIdcate())) {
                    int gironeIdx = count % nrGironi;
                    ((List) gironi.get(gironeIdx)).add(oneTeam);
                    count++;
                    LOG.debug("girone:" + gironeIdx + " team: " + oneTeam.getSocieta().getNome() + " " + oneTeam.getNome());
                }
            }
        }
        return gironi;
    }

    /**
	 * 
	 * @param categoria
	 * @return
	 */
    private Stack getLuck() {
        List socList = null;
        try {
            socList = getSession().createCriteria(Societa.class).list();
        } catch (HibernateException e) {
            setError(e);
        }
        Random random = new Random();
        Stack stack = new Stack();
        for (int index = 0; socList.size() > 0; index++) {
            int caso = random.nextInt(socList.size());
            Object element = socList.get(caso);
            stack.push(element);
            socList.remove(caso);
        }
        return stack;
    }

    /**
	 * 
	 * @param categoria
	 * @return lista di team di categoria
	 */
    private List getList(Categorie categoria) {
        Criteria criteria = null;
        List teamList = null;
        try {
            criteria = getSession().createCriteria(Team.class).add(Expression.eq("categorie", categoria)).addOrder(Order.asc("societa"));
            teamList = criteria.list();
        } catch (HibernateException e) {
            setError(e);
        }
        return teamList;
    }

    /**
	 * cerca un campo libero
	 * @param altriGironi
	 * @return
	 */
    private int findCampoLibero(List altriGironi) {
        if (altriGironi == null || altriGironi.size() == 0) return 1;
        int[] occupati = new int[altriGironi.size()];
        for (int nrGirone = 0, totGironi = altriGironi.size(); nrGirone < totGironi; nrGirone++) {
            Gironi element = (Gironi) altriGironi.get(nrGirone);
            occupati[nrGirone] = element.getCampo().intValue();
        }
        Arrays.sort(occupati);
        for (int nrcampo = 0; nrcampo < occupati.length; nrcampo++) {
            if (nrcampo + 1 < occupati[nrcampo]) return nrcampo + 1;
        }
        return occupati[occupati.length - 1] + 1;
    }

    public int getNrGironiDefault(int nrTeams) {
        int retVal = 0;
        if (nrTeams > NR_TEAMS_OTTIMALE_PER_GIRONE) retVal = Math.min((int) Math.round((float) nrTeams / NR_TEAMS_OTTIMALE_PER_GIRONE), getTipoFinale(nrTeams)); else retVal = (nrTeams < NR_TEAMS_MIN ? 0 : 1);
        return retVal;
    }

    /**
    *
    * @param gironi
    * @return tipo finali
    */
    public static int getTipoFinaleDaGironi(int gironi) {
        if (gironi <= 0) return 0;
        int pp = (int) Math.pow(2, logBase2(gironi));
        return pp;
    }

    /**
	 * @param gironi gironi
	 * @return nr di team ripescati
	 */
    public static int getRipescati(int gironi) {
        return (getTipoFinaleDaGironi(gironi) - gironi) * 2;
    }

    /**
	 * @param gironi gironi
	 * @return nr di team ripescati
	 */
    public static int getPartiteFinali(int gironi) {
        return (gironi == 0 ? 0 : logBase2(getTipoFinaleDaGironi(gironi)) + 1);
    }

    /**
	 * ritorna il tipo girone in base alla quantit� di squadre
	 * @param qtaTeam
	 * @return nr gironi per la fase finale
	 */
    public int getTipoFinale(int team) {
        return team <= 3 ? 0 : logBase2(team) - 1;
    }

    public String getDescrGirone(int nrGironi) {
        return (nrGironi > 0 && logBase2(nrGironi) < 6 ? getText(GironiLogic.TIPO_GIRONE[logBase2(nrGironi)]) : (nrGironi > 0 ? "" + nrGironi : ""));
    }

    /**
	 * eponente della base 2 che rappresenta il numero
	 * @param number
	 * @return logaritmo base 2
	 */
    private static int logBase2(int number) {
        return ((int) ((number == 0 ? 0 : Math.log(number) / Math.log(2))));
    }

    public Date getInizioTorneo() {
        return getData().getTime();
    }

    public Date getOraFine(int nrPlay) {
        GregorianCalendar xx = getData();
        xx.add(GregorianCalendar.MINUTE, getDurataPartita() * nrPlay);
        return xx.getTime();
    }

    private class Coppia {

        Team team1;

        Team team2;

        public Coppia(Team team1) {
            this.team1 = team1;
        }

        public Coppia(Team team1, Team team2) {
            this.team1 = team1;
            this.team2 = team2;
        }

        public Team getTeam1() {
            return team1;
        }

        public void setTeam1(Team team1) {
            this.team1 = team1;
        }

        public Team getTeam2() {
            return team2;
        }

        public void setTeam2(Team team2) {
            this.team2 = team2;
        }
    }

    private class TeamPlayed {

        Team team;

        int totPlay;

        List teamAvversarie;

        private TeamPlayed(Team team) {
            this.team = team;
            teamAvversarie = new ArrayList();
        }

        private Team getTeam() {
            return team;
        }

        private int getTotPlay() {
            return totPlay;
        }

        private Team addPlay(Team team, int seq) {
            this.totPlay += 100 + seq;
            teamAvversarie.add(team);
            return this.getTeam();
        }

        /**
		 * 
		 * @param team a esaminare
		 * @return true se il team passato non � mai stato tra gli avversari
		 */
        private boolean isFree(Team team) {
            boolean retVal = true;
            for (Iterator iter = teamAvversarie.iterator(); retVal == true && iter.hasNext(); ) {
                retVal = !team.equals((Team) iter.next());
            }
            return retVal;
        }

        public boolean equals(Object other) {
            if (!(other instanceof TeamPlayed)) return false;
            TeamPlayed castOther = (TeamPlayed) other;
            return (this.getTeam().getTeam().intValue() == castOther.getTeam().getTeam().intValue());
        }
    }

    public Date getGiorno() {
        return (giorno == null ? new Date() : giorno);
    }

    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }

    public int getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(int oraInizio) {
        this.oraInizio = oraInizio;
    }

    public int getMinutiInizio() {
        return minutiInizio;
    }

    public void setMinutiInizio(int minutiInizio) {
        this.minutiInizio = minutiInizio;
    }

    public int getDurataPartita() {
        return durataPartita;
    }

    public void setDurataPartita(int durataPartita) {
        this.durataPartita = durataPartita;
    }

    public Categorie getCategoria() {
        if (categoria == null) categoria = new Categorie();
        return categoria;
    }

    public void setCategoria(Categorie categoria) {
        this.categoria = categoria;
    }

    public int getNrGironi() {
        return nrGironi;
    }

    public void setNrGironi(int nrGironi) {
        this.nrGironi = nrGironi;
    }

    public int getTotTeam() {
        return totTeam;
    }

    public void setTotTeam(int totTeam) {
        this.totTeam = totTeam;
    }

    public int getEliminati() {
        return getTotTeam() - (getVincenti() + getPerdenti());
    }

    public int getLivello() {
        return getTipoFinale(getTotTeam());
    }

    public String getLivelloDescr() {
        return GironiLogic.TIPO_GIRONE[getLivello()];
    }

    public int getPartiteFinali() {
        return (getPerdenti() > 0 ? 4 : 0);
    }

    public int getPartitePerdenti() {
        return (getTotTeam() >= getVincenti() / 2 ? getVincenti() / 2 : 0);
    }

    public int getPartiteVincenti() {
        return getVincenti() > 0 ? getVincenti() - (getPerdenti() == 0 ? 1 : 2) : 0;
    }

    public int getPerdenti() {
        return (getTotTeam() - getVincenti() >= getVincenti() / 2 ? getVincenti() / 2 : 0);
    }

    public int getTotPartite() {
        return getPartiteVincenti() + getPartitePerdenti() + getPartiteFinali();
    }

    public int getVincenti() {
        return (getTotTeam() > 3 ? (int) Math.pow(2, getLivello() + 1) : 0);
    }

    public int getCampi() {
        return getVincenti() / 2;
    }
}
