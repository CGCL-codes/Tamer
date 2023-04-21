package webapplication2;

import com.sun.rave.web.ui.appbase.AbstractPageBean;
import com.sun.webui.jsf.component.Body;
import com.sun.webui.jsf.component.Form;
import com.sun.webui.jsf.component.Head;
import com.sun.webui.jsf.component.Html;
import com.sun.webui.jsf.component.Hyperlink;
import com.sun.webui.jsf.component.Link;
import com.sun.webui.jsf.component.Page;
import javax.faces.FacesException;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author mauricio
 */
public class menu extends AbstractPageBean {

    /**
     * <p>Automatically managed component initialization.  <strong>WARNING:</strong>
     * This method is automatically generated, so any user-specified code inserted
     * here is subject to being replaced.</p>
     */
    private void _init() throws Exception {
    }

    private Page page1 = new Page();

    public Page getPage1() {
        return page1;
    }

    public void setPage1(Page p) {
        this.page1 = p;
    }

    private Html html1 = new Html();

    public Html getHtml1() {
        return html1;
    }

    public void setHtml1(Html h) {
        this.html1 = h;
    }

    private Head head1 = new Head();

    public Head getHead1() {
        return head1;
    }

    public void setHead1(Head h) {
        this.head1 = h;
    }

    private Link link1 = new Link();

    public Link getLink1() {
        return link1;
    }

    public void setLink1(Link l) {
        this.link1 = l;
    }

    private Body body1 = new Body();

    public Body getBody1() {
        return body1;
    }

    public void setBody1(Body b) {
        this.body1 = b;
    }

    private Form form1 = new Form();

    public Form getForm1() {
        return form1;
    }

    public void setForm1(Form f) {
        this.form1 = f;
    }

    private Hyperlink creerEquipe = new Hyperlink();

    public Hyperlink getCreerEquipe() {
        return creerEquipe;
    }

    public void setCreerEquipe(Hyperlink h) {
        this.creerEquipe = h;
    }

    private Hyperlink afficherEquipes = new Hyperlink();

    public Hyperlink getAfficherEquipes() {
        return afficherEquipes;
    }

    public void setAfficherEquipes(Hyperlink h) {
        this.afficherEquipes = h;
    }

    private Hyperlink supprimerEquipe = new Hyperlink();

    public Hyperlink getSupprimerEquipe() {
        return supprimerEquipe;
    }

    public void setSupprimerEquipe(Hyperlink h) {
        this.supprimerEquipe = h;
    }

    private Hyperlink creerJoueur = new Hyperlink();

    public Hyperlink getCreerJoueur() {
        return creerJoueur;
    }

    public void setCreerJoueur(Hyperlink h) {
        this.creerJoueur = h;
    }

    private Hyperlink creerMatch = new Hyperlink();

    public Hyperlink getCreerMatch() {
        return creerMatch;
    }

    public void setCreerMatch(Hyperlink h) {
        this.creerMatch = h;
    }

    private Hyperlink supprimerJoueur = new Hyperlink();

    public Hyperlink getSupprimerJoueur() {
        return supprimerJoueur;
    }

    public void setSupprimerJoueur(Hyperlink h) {
        this.supprimerJoueur = h;
    }

    private Hyperlink afficherJoueursEquipe = new Hyperlink();

    public Hyperlink getAfficherJoueursEquipe() {
        return afficherJoueursEquipe;
    }

    public void setAfficherJoueursEquipe(Hyperlink h) {
        this.afficherJoueursEquipe = h;
    }

    private Hyperlink creerArbitre = new Hyperlink();

    public Hyperlink getCreerArbitre() {
        return creerArbitre;
    }

    public void setCreerArbitre(Hyperlink h) {
        this.creerArbitre = h;
    }

    private Hyperlink entrerResultat = new Hyperlink();

    public Hyperlink getEntrerResultat() {
        return entrerResultat;
    }

    public void setEntrerResultat(Hyperlink h) {
        this.entrerResultat = h;
    }

    private Hyperlink arbitrerMatch = new Hyperlink();

    public Hyperlink getArbitrerMatch() {
        return arbitrerMatch;
    }

    public void setArbitrerMatch(Hyperlink h) {
        this.arbitrerMatch = h;
    }

    private Hyperlink afficherArbitres = new Hyperlink();

    public Hyperlink getAfficherArbitres() {
        return afficherArbitres;
    }

    public void setAfficherArbitres(Hyperlink h) {
        this.afficherArbitres = h;
    }

    private Hyperlink afficherResultatsDate = new Hyperlink();

    public Hyperlink getAfficherResultatsDate() {
        return afficherResultatsDate;
    }

    public void setAfficherResultatsDate(Hyperlink h) {
        this.afficherResultatsDate = h;
    }

    private Hyperlink afficherResultats = new Hyperlink();

    public Hyperlink getAfficherResultats() {
        return afficherResultats;
    }

    public void setAfficherResultats(Hyperlink h) {
        this.afficherResultats = h;
    }

    /**
     * <p>Construct a new Page bean instance.</p>
     */
    public menu() {
    }

    /**
     * <p>Callback method that is called whenever a page is navigated to,
     * either directly via a URL, or indirectly via page navigation.
     * Customize this method to acquire resources that will be needed
     * for event handlers and lifecycle methods, whether or not this
     * page is performing post back processing.</p>
     * 
     * <p>Note that, if the current request is a postback, the property
     * values of the components do <strong>not</strong> represent any
     * values submitted with this request.  Instead, they represent the
     * property values that were saved for this view when it was rendered.</p>
     */
    @Override
    public void init() {
        super.init();
        try {
            _init();
        } catch (Exception e) {
            log("menu Initialization Failure", e);
            throw e instanceof FacesException ? (FacesException) e : new FacesException(e);
        }
    }

    /**
     * <p>Callback method that is called after the component tree has been
     * restored, but before any event processing takes place.  This method
     * will <strong>only</strong> be called on a postback request that
     * is processing a form submit.  Customize this method to allocate
     * resources that will be required in your event handlers.</p>
     */
    @Override
    public void preprocess() {
    }

    /**
     * <p>Callback method that is called just before rendering takes place.
     * This method will <strong>only</strong> be called for the page that
     * will actually be rendered (and not, for example, on a page that
     * handled a postback and then navigated to a different page).  Customize
     * this method to allocate resources that will be required for rendering
     * this page.</p>
     */
    @Override
    public void prerender() {
    }

    /**
     * <p>Callback method that is called after rendering is completed for
     * this request, if <code>init()</code> was called (regardless of whether
     * or not this was the page that was actually rendered).  Customize this
     * method to release resources acquired in the <code>init()</code>,
     * <code>preprocess()</code>, or <code>prerender()</code> methods (or
     * acquired during execution of an event handler).</p>
     */
    @Override
    public void destroy() {
    }

    public String creerEquipe_action() {
        return "case1";
    }

    public String afficherEquipes_action() {
        return "case12";
    }

    public String creerArbitre_action() {
        return "case7";
    }

    public String arbitrerMatch_action() {
        return "case9";
    }

    public String supprimerEquipe_action() {
        return "case2";
    }

    public String creerJoueur_action() {
        return "case3";
    }

    public String afficherJoueursEquipe_action() {
        return "case4";
    }

    public String supprimerJoueur_action() {
        return "case5";
    }

    public String creerMatch_action() {
        return "case6";
    }

    public String entrerResultat_action() {
        return "case8";
    }

    public String afficherArbitres_action() {
        return "case13";
    }

    public String afficherResultatsDate_action() {
        return "case10";
    }

    public String afficherResultats_action() {
        return "case11";
    }
}
