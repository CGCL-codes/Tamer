package de.bwb.ekp.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.faces.FacesMessages;
import de.bwb.ekp.interceptors.MeasureCalls;

@MeasureCalls
@Name("firmaHome")
public class FirmaHome extends EKPEntityHome<Firma> {

    private static final long serialVersionUID = 1L;

    @In(required = false)
    private FirmaList firmaList;

    @In(value = "session")
    private Session session;

    @In(create = true)
    private LogEintragHome logEintragHome;

    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false)
    private Integer currentFirmaId;

    @SuppressWarnings("unused")
    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false, value = "#{firstResult}")
    private Object firstResult;

    @SuppressWarnings("unused")
    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false, value = "#{firmaOrder}")
    private String firmaOrder;

    @SuppressWarnings("unused")
    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false, value = "#{selectGesperrte}")
    private Object selectGesperrte;

    @SuppressWarnings("unused")
    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false, value = "#{selectUmlaute}")
    private Object selectUmlaute;

    @SuppressWarnings("unused")
    @Out(required = false, scope = ScopeType.SESSION)
    @In(required = false)
    private String startCharacter;

    @In
    private FacesMessages facesMessages;

    @In(create = true)
    private ManagementsystemList managementsystemList;

    private Map<Managementsystem, Boolean> mapMsysteme;

    private Map<Integer, Integer> umsaetzeKonzern;

    private Map<Integer, Integer> umsaetzeUnternehmen;

    private String updateDetails;

    public FirmaHome() {
        super(LogObjekt.Firma, true);
    }

    public void setFirmaId(final Integer id) {
        this.currentFirmaId = id;
    }

    public Integer getFirmaId() {
        return (Integer) this.getId();
    }

    @Override
    public Object getId() {
        return this.currentFirmaId;
    }

    @Override
    protected Firma createInstance() {
        return new Firma();
    }

    /**
   * Liefert die Firma zum &uuml;bergebenen Namen.
   * 
   * @param firmaname
   * @throws NoResultException
   *           wenn nichts gefunden wurde
   * @return
   */
    public Firma getFirma(final String firmaname) {
        final Query query = this.getEntityManager().createQuery("from Firma where firmaName = :firmaName");
        query.setParameter("firmaName", firmaname);
        return (Firma) query.getSingleResult();
    }

    /**
   * Sucht eine Firma zum &uuml;bergebenen Namen. Liefert <code>null</code>
   * zur&uuml;ck, wenn nichts gefunden wurde.
   * 
   * @param firmaname
   * @return
   */
    public Firma findFirma(final String firmaname) {
        try {
            return this.getFirma(firmaname);
        } catch (final NoResultException ex) {
            return null;
        }
    }

    public void wire() {
    }

    public boolean isWired() {
        return true;
    }

    public Firma getDefinedInstance() {
        return (this.isIdDefined() && (this.getFirmaId() != 0)) ? this.getInstance() : null;
    }

    @SuppressWarnings("unchecked")
    public Map<Managementsystem, Boolean> getMapMsysteme() {
        if (this.mapMsysteme != null) {
            return this.mapMsysteme;
        }
        final Set<Managementsystem> ms = null == this.getInstance().getManagementsysteme() ? new HashSet<Managementsystem>() : this.getInstance().getManagementsysteme();
        this.mapMsysteme = BooleanMapUtil.createMap(this.managementsystemList.getResultList(), ms);
        return this.mapMsysteme;
    }

    public void setMapMsysteme(final Map<Managementsystem, Boolean> mapMsysteme) {
        this.mapMsysteme = mapMsysteme;
    }

    public void updateMsystemFirma() {
        final Set<Managementsystem> zugeordneteMsysteme = this.getInstance().getManagementsysteme();
        BooleanMapUtil.updateZugeordnete(this.mapMsysteme, zugeordneteMsysteme);
        this.update("Zertifikate");
        this.mapMsysteme = null;
    }

    public void resetCurrentFirma() {
        this.startCharacter = null;
        this.firstResult = null;
        this.firmaOrder = null;
        this.selectGesperrte = null;
        this.selectUmlaute = null;
        this.setFirmaId(null);
    }

    public void setCurrentFirma(final Firma firma) {
        this.setFirmaId(firma.getId());
    }

    public String remove(final Firma firma) {
        this.setInstance(firma);
        if (this.getInstance().getBewerbungen().isEmpty()) {
            final String retVal = super.remove();
            this.firmaList.refresh();
            return retVal;
        } else {
            this.facesMessages.addFromResourceBundle(FacesMessage.SEVERITY_ERROR, "firma.remove.error");
            return null;
        }
    }

    public String update(final String details) {
        try {
            this.checkNameChanged();
            this.checkHauptansprechpartnerChanged();
            this.updateDetails = details;
            super.update();
            return "updated";
        } catch (final DuplicateNameException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<Firma> getFirmenZuBedarfsspektren(final Collection<Untergruppe> untergruppen) {
        if (untergruppen.isEmpty()) {
            return Collections.emptyList();
        }
        final Query query = this.getEntityManager().createQuery("select distinct firma from Firma firma inner join fetch firma.hauptAnsprechpartner " + "inner join fetch firma.bedarfsspektren bedarfsspektrum " + "where bedarfsspektrum.untergruppe in (:untergruppen)");
        query.setParameter("untergruppen", untergruppen);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public Collection<Firma> getFirmenZuHauptgruppen(final Collection<Hauptgruppe> hauptgruppen) {
        if (hauptgruppen.isEmpty()) {
            return Collections.emptyList();
        }
        final Query query = this.getEntityManager().createQuery("select distinct firma from Firma firma inner join fetch firma.hauptAnsprechpartner " + "inner join fetch firma.bedarfsspektren bedarfsspektrum " + "where bedarfsspektrum.untergruppe.hauptgruppe in (:hauptgruppen)");
        query.setParameter("hauptgruppen", hauptgruppen);
        return query.getResultList();
    }

    public void jahresvertragsFirma(final Firma firma) {
        this.setInstance(firma);
        if (!firma.isJahresvertrag()) {
            firma.setJahresvertrag(true);
            this.logEintragHome.addLogEintrag(Aktion.aendern, LogObjekt.Firma, "Firma als Jahresvertragsfirma angelegt");
            this.update();
            this.firmaList.refresh();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Firma> autocomplete(final Object object) {
        final Criteria firmenNameCriteria = this.session.createCriteria(Firma.class);
        final String filter = this.normalize(object.toString());
        final List<Firma> firmenNachName = firmenNameCriteria.add(this.createFirmennameRestriction(filter)).list();
        if (firmenNachName.isEmpty()) {
            final Criteria ansprechpartnerCriteria = this.session.createCriteria(Firma.class);
            this.createHauptansprechpartnerCriteria(ansprechpartnerCriteria).add(Restrictions.disjunction().add(this.createEmailRestriction(filter)).add(this.createAnsprechpartnerRestriction(filter)));
            final List<Firma> firmen = ansprechpartnerCriteria.list();
            return firmen;
        }
        return firmenNachName;
    }

    public List<Firma> getSearchResult(final String ansprechPartner, final String firmenName, final String email, final String stadt) {
        boolean restricted = false;
        final Criteria criteria = this.session.createCriteria(Firma.class);
        criteria.addOrder(Order.asc("firmaName"));
        String filter = this.normalize(ansprechPartner);
        if (filter.trim().length() > 0) {
            restricted = true;
            this.createHauptansprechpartnerCriteria(criteria).add(this.createAnsprechpartnerRestriction(filter));
        }
        filter = this.normalize(firmenName);
        if (filter.trim().length() > 0) {
            restricted = true;
            criteria.add(this.createFirmennameRestriction(filter));
        }
        filter = this.normalize(email);
        if (filter.trim().length() > 0) {
            restricted = true;
            this.createHauptansprechpartnerCriteria(criteria).add(this.createEmailRestriction(filter));
        }
        filter = this.normalize(stadt);
        if (filter.trim().length() > 0) {
            restricted = true;
            if (this.isPlz(filter)) {
                criteria.add(this.createPlzRestriction(filter));
            } else {
                criteria.add(this.createOrtRestriction(filter));
            }
        }
        if (restricted) {
            return criteria.list();
        }
        return null;
    }

    /**
   * Pr�ft, ob der angegebene Parameter eine Postleitzahl sein kann. Dies ist
   * genau dann der Fall, wenn das Argument nur aus Zahlen besteht. Es wird
   * keine weitere �berpr�fung zum Beispiel auf L�nge oder existenz der PLZ
   * �berpr�ft!
   * 
   * @param value :
   *          der zu �berpr�fende Wert
   * @return true, wenn es sich um eine PLZ handeln kann
   */
    private boolean isPlz(final String value) {
        for (int index = value.length() - 1; index >= 0; index--) {
            if (!Character.isDigit(value.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    private Criterion createPlzRestriction(final String filter) {
        return Restrictions.ilike("plz", "%" + filter + "%");
    }

    private Criterion createOrtRestriction(final String filter) {
        return Restrictions.ilike("ort", "%" + filter + "%");
    }

    private Criterion createEmailRestriction(final String filter) {
        return Restrictions.ilike("email", "%" + filter + "%");
    }

    private Criteria createHauptansprechpartnerCriteria(final Criteria criteria) {
        return criteria.createCriteria("hauptAnsprechpartner");
    }

    private Criterion createFirmennameRestriction(final String filter) {
        return Restrictions.ilike("firmaName", "%" + filter + "%");
    }

    private Criterion createAnsprechpartnerRestriction(final String filter) {
        final String wildFilter = "%" + filter + "%";
        return Restrictions.or(Restrictions.ilike("nachname", wildFilter), Restrictions.ilike("vorname", wildFilter));
    }

    private String normalize(final String value) {
        String retVal = value;
        if (null == retVal) {
            retVal = "";
        } else {
            retVal = retVal.replace("%", "");
        }
        return retVal;
    }

    public Firma findFirma(final User user) {
        try {
            return this.getFirma(user);
        } catch (final NoResultException ex) {
            return null;
        }
    }

    public Firma getFirma(final User user) {
        final Query query = this.getEntityManager().createQuery("from Firma firma where firma.user = :user");
        query.setParameter("user", user);
        return (Firma) query.getSingleResult();
    }

    public void checkHauptansprechpartnerChanged() {
        if (this.getInstance().isHauptansprechpartnerChanged()) {
            this.facesMessages.add("Hauptansprechpartner wurde ge�ndert.");
            this.logEintragHome.addLogEintrag(Aktion.aendern, LogObjekt.Firma, "Hauptansprechpartner geaendert");
        }
    }

    private void checkNameChanged() throws DuplicateNameException {
        final Firma firma = this.getInstance();
        if (firma.isNameChanged()) {
            this.checkFirmanameExists(firma);
        }
    }

    public void checkFirmanameExists(final Firma firma) throws DuplicateNameException {
        final Firma existingFirma = this.findFirma(firma.getFirmaName());
        if ((existingFirma != null) && (existingFirma.getId() != firma.getId())) {
            throw new DuplicateNameException(firma.getFirmaName());
        }
    }

    public List<Integer> getJahre() {
        final List<Integer> jahre = new ArrayList<Integer>();
        final Calendar calendar = Calendar.getInstance();
        for (int i = 1; i <= 3; i++) {
            calendar.add(Calendar.YEAR, -1);
            jahre.add(calendar.get(Calendar.YEAR));
        }
        return jahre;
    }

    public Map<Integer, Integer> getUmsaetzeKonzern() {
        if (this.umsaetzeKonzern == null) {
            this.umsaetzeKonzern = new HashMap<Integer, Integer>();
            final List<UmsatzKonzern> listUmsaetzeKonzern = this.getInstance().getUmsaetzeKonzern();
            for (Integer jahr : this.getJahre()) {
                this.umsaetzeKonzern.put(jahr, 0);
            }
            for (UmsatzKonzern umsatzKonzern : listUmsaetzeKonzern) {
                this.umsaetzeKonzern.put(umsatzKonzern.getJahr(), umsatzKonzern.getUmsatz());
            }
        }
        return this.umsaetzeKonzern;
    }

    public void setUmsaetzeKonzern(final Map<Integer, Integer> umsaetzeKonzern) {
        this.umsaetzeKonzern = umsaetzeKonzern;
    }

    public Map<Integer, Integer> getUmsaetzeUnternehmen() {
        if (this.umsaetzeUnternehmen == null) {
            this.umsaetzeUnternehmen = new HashMap<Integer, Integer>();
            final List<UmsatzUnternehmen> listUmsaetzeUnternehmen = this.getInstance().getUmsaetzeUnternehmen();
            for (Integer jahr : this.getJahre()) {
                this.umsaetzeUnternehmen.put(jahr, 0);
            }
            for (UmsatzUnternehmen umsatzUnternehmen : listUmsaetzeUnternehmen) {
                this.umsaetzeUnternehmen.put(umsatzUnternehmen.getJahr(), umsatzUnternehmen.getUmsatz());
            }
        }
        return this.umsaetzeUnternehmen;
    }

    public void setUmsaetzeUnternehmen(final Map<Integer, Integer> umsaetzeUnternehmen) {
        this.umsaetzeUnternehmen = umsaetzeUnternehmen;
    }

    private <T extends Umsatz> void updateUmsatz(final List<T> alteUmsaetze, final Map<Integer, Integer> umsaetze, final Class<T> clazz) {
        final List<T> copyUmsaetze = new ArrayList<T>(alteUmsaetze);
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -3);
        for (T umsatz : copyUmsaetze) {
            if (umsatz.getJahr() >= cal.get(Calendar.YEAR)) {
                umsatz.setUmsatz(umsaetze.get(umsatz.getJahr()));
                umsaetze.remove(umsatz.getJahr());
            } else {
                umsaetze.remove(umsatz.getJahr());
            }
        }
        final Set<Integer> keys = umsaetze.keySet();
        for (Integer key : keys) {
            try {
                final T umsatz = clazz.newInstance();
                umsatz.setJahr(key);
                umsatz.setUmsatz(umsaetze.get(key));
                alteUmsaetze.add(umsatz);
            } catch (final InstantiationException e) {
                throw new RuntimeException(e);
            } catch (final IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String updateUnternehmenszahlen() {
        this.updateUmsatz(this.getInstance().getUmsaetzeKonzern(), this.umsaetzeKonzern, UmsatzKonzern.class);
        this.umsaetzeKonzern = null;
        this.updateUmsatz(this.getInstance().getUmsaetzeUnternehmen(), this.umsaetzeUnternehmen, UmsatzUnternehmen.class);
        this.umsaetzeUnternehmen = null;
        return this.update("Unternehmenszahlen");
    }

    @Override
    protected String getBezeichnung() {
        return this.updateDetails + " " + this.getInstance().getFirmaName();
    }

    public String getStartCharcter() {
        if (!isIdDefined()) {
            return null;
        }
        return this.getInstance().getFirmaName().substring(0, 1);
    }
}
