package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import modele.analyselinguistique.AnalyseurLinguistique;
import modele.analyselinguistique.FabriqueTypeAnalyse;
import modele.articles.Article;
import modele.articles.Element;
import modele.articles.Mot;
import modele.modelesdelangage.FabriqueModeleDeLangage;
import modele.modelesdelangage.ModeleDeLangage;

public class ControleurArticle implements ActionListener, ComponentListener {

    private VueArticle m_vA = null;

    private Article m_article = null;

    private AnalyseurLinguistique m_analyseur;

    private boolean analyseMotParMot = true;

    public ControleurArticle() {
        m_article = Article.getInstance();
        m_article.registerControleur(this);
        ModeleDeLangage mdl = FabriqueModeleDeLangage.creerModeleDeLangage("data/ML_3-gram_JEP-2002-2004-2008.arpa", "data/ML_3-gram_JEP-2002-2004-2008.lex");
        m_analyseur = new AnalyseurLinguistique();
        m_analyseur.setModeleDeLangage(mdl);
        build();
    }

    private void build() {
        this.m_vA = new VueArticle(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
    }

    public void ajouter(String chaine, int offset) {
        m_article.ajouter(chaine, offset);
        lancerAnanlyseMotParMot();
    }

    public void supprimer(int offsetDebut, int offsetFin) {
        m_article.supprimer(offsetDebut, offsetFin);
        lancerAnanlyseMotParMot();
    }

    public int[] remplacerMot(String nouveauMot, int offset) {
        return m_article.remplacerMot(nouveauMot, offset);
    }

    public List<String> getSuggestions(int offset) {
        Element elt = m_article.getElementAt(offset);
        if (elt.getTypeElement().equals(Element.TYPE_MOT)) return m_analyseur.getSuggestions((Mot) elt, 3);
        return null;
    }

    public void lancerAnanlyseMotParMot() {
        if (!analyseMotParMot) {
            m_analyseur.setTypeAnalyse(FabriqueTypeAnalyse.getAnalyseMotParMot());
            analyseMotParMot = true;
        }
        m_analyseur.analyser(m_article);
    }

    public void lancerAnalyseComplete() {
        m_analyseur.setTypeAnalyse(FabriqueTypeAnalyse.getAnalyseComplete());
        analyseMotParMot = false;
        m_analyseur.analyser(m_article);
    }

    public void updateHighlight(int debut, int fin, float proba) {
        Editeur edit = (Editeur) m_vA.getEditeur();
        edit.surligner(debut, fin, proba);
    }

    public VueArticle getView() {
        return this.m_vA;
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
    }

    @Override
    public void componentResized(ComponentEvent arg0) {
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
    }
}
