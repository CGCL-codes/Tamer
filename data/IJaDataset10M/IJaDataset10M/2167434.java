package org.fudaa.ebli.impression;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import javax.swing.*;
import com.memoire.bu.*;
import com.memoire.fu.FuLog;
import org.fudaa.ebli.ressource.EbliResource;
import org.fudaa.ebli.trace.TraceLigne;

/**
 * @version $Id: EbliFillePrevisualisation.java,v 1.14 2006-09-19 14:55:54 deniger Exp $
 * @author Fred Deniger
 */
public final class EbliFillePrevisualisation extends BuInternalFrame implements ItemListener {

    private static String getStrVoir() {
        return "voir";
    }

    private static String getStrAgr() {
        return "agrandir";
    }

    static final transient Vue[] VUES = new Vue[] { new Vue("1 page", getStrVoir(), 1, 1), new Vue("2 pages", getStrVoir(), 2, 1), new Vue("4 pages", getStrVoir(), 2, 2), new Vue("Toutes les pages", getStrVoir(), 3) };

    static final transient Zoom[] ZOOMS = new Zoom[] { new Zoom(EbliResource.EBLI.getString("adapt�"), getStrAgr(), 0), new Zoom("100%", getStrAgr(), 1D), new Zoom("75%", getStrAgr(), 0.75), new Zoom("50%", getStrAgr(), 0.5), new Zoom("25%", getStrAgr(), 0.25) };

    private final BuCommonInterface app_;

    private BuButton btMiseEnPage_;

    private BuButton btFermer_;

    private BuToggleButton btAfficheMarges_;

    private BuButton btPremierePage_;

    private BuButton btPrecedent_;

    private BuButton btSuivant_;

    private BuButton btDernierePage_;

    private BuComboBox chListePages_;

    private BuComboBox chZooms_;

    private BuComboBox chVues_;

    boolean dessineMarges_;

    private boolean specificToolsConstruits_;

    private transient SimplePanelPreview[] previewPanels_;

    transient Vue vueEnCours_;

    EbliPageable target_;

    transient Zoom zoom_;

    public EbliFillePrevisualisation(final BuCommonInterface _app, final EbliPageable _target) {
        super(EbliResource.EBLI.getString("Aper�u avant impression"), true, true, true, true);
        app_ = _app;
        target_ = _target;
        specificToolsConstruits_ = false;
        zoom_ = ZOOMS[0];
        setVue(VUES[VUES.length - 1]);
        setAutoscrolls(true);
        setVisible(true);
        setSize(new Dimension(400, 400));
        dessineMarges_ = false;
    }

    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

    private void setVue(final Vue _type) {
        if ((_type == null) || (_type.equals(vueEnCours_))) {
            return;
        }
        vueEnCours_ = _type;
        final int premPage = getPremierePageAffichee();
        previewPanels_ = new SimplePanelPreview[getNbPagesAffichees()];
        for (int i = 0; i < previewPanels_.length; i++) {
            previewPanels_[i] = new SimplePanelPreview(i + premPage);
        }
        construireVue();
        majEtatBoutonsChangementAffichage();
        if (specificToolsConstruits_) {
            chVues_.setSelectedItem(_type);
        }
    }

    private Vue getVue() {
        return vueEnCours_;
    }

    private void setPremierePageAffichee(final int _idx) {
        int idx = _idx;
        if (idx < 0) {
            idx = 0;
        }
        if (idx >= getNbPagesDocument()) {
            idx = getNbPagesDocument() - 1;
        }
        if (idx == getPremierePageAffichee()) {
            return;
        }
        for (int i = 0; i < previewPanels_.length; i++) {
            previewPanels_[i].setPage(i + idx);
        }
        majEtatBoutonsChangementAffichage();
        construireVue();
    }

    private void construireVue() {
        final BuPanel vue = new BuPanel();
        vue.setLayout(new BuGridLayout(vueEnCours_.nbCols(), 10, 10, true, true));
        vue.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (int i = 0; i < previewPanels_.length; i++) {
            vue.add(previewPanels_[i]);
        }
        vue.doLayout();
        setContentPane(new JScrollPane(vue));
        repaint();
    }

    int getNbPagesAffichees() {
        final int r = vueEnCours_.nbMaxPages();
        return r == 0 ? getNbPagesDocument() : r;
    }

    private int getPremierePageAffichee() {
        if ((previewPanels_ == null) || (previewPanels_[0] == null)) {
            return 0;
        }
        return previewPanels_[0].getPage();
    }

    private int getDernierePageAffichee() {
        if ((previewPanels_ == null) || (previewPanels_[previewPanels_.length - 1] == null)) {
            return 0;
        }
        return previewPanels_[previewPanels_.length - 1].getPage();
    }

    private boolean isPremierePageDocAffichee() {
        return (getPremierePageAffichee() <= 0);
    }

    private boolean isDernierePageDocAffichee() {
        return (getDernierePageAffichee() >= getNbPagesDocument() - 1);
    }

    private Zoom getZoom() {
        return zoom_;
    }

    private void setZoom(final Zoom _zoom) {
        if ((_zoom == null) || (_zoom.equals(zoom_))) {
            return;
        }
        zoom_ = _zoom;
        if (specificToolsConstruits_) {
            chZooms_.setSelectedItem(zoom_);
        }
        construireVue();
    }

    private void majEtatBoutonsChangementAffichage() {
        if (!specificToolsConstruits_) {
            return;
        }
        if (isPremierePageDocAffichee()) {
            btPrecedent_.setEnabled(false);
            btPremierePage_.setEnabled(false);
        } else {
            btPrecedent_.setEnabled(true);
            btPremierePage_.setEnabled(true);
        }
        if (isDernierePageDocAffichee()) {
            btSuivant_.setEnabled(false);
            btDernierePage_.setEnabled(false);
        } else {
            btSuivant_.setEnabled(true);
            btDernierePage_.setEnabled(true);
        }
        chListePages_.setSelectedIndex(getPremierePageAffichee());
    }

    private void majEtatBoutonsChangementCible() {
        if (!specificToolsConstruits_) {
            return;
        }
        btMiseEnPage_.setEnabled(true);
        chListePages_.removeAllItems();
        for (int i = 0; i < getNbPagesDocument(); i++) {
            chListePages_.addItem("page " + (i + 1) + "/" + getNbPagesDocument());
        }
        majEtatBoutonsChangementAffichage();
    }

    private void allerPremierePage() {
        setPremierePageAffichee(0);
    }

    private void allerDernierePage() {
        setPremierePageAffichee(getNbPagesDocument() - getNbPagesAffichees());
    }

    private void suivant() {
        setPremierePageAffichee(getPremierePageAffichee() + getNbPagesAffichees());
    }

    private void precedent() {
        setPremierePageAffichee(getPremierePageAffichee() - getNbPagesAffichees());
    }

    private void marges() {
        dessineMarges_ = btAfficheMarges_.isSelected();
        getContentPane().repaint();
    }

    private void modifieEbliPageFormat() {
        if (app_ == null) {
            new EbliMiseEnPageDialog(target_, null, null).activate();
        } else {
            new EbliMiseEnPageDialog(target_, app_, app_.getInformationsSoftware()).activate();
        }
        getContentPane().doLayout();
        getContentPane().repaint();
    }

    private void initialiseBouton(final AbstractButton _target, final String _tooltip, final String _commande) {
        _target.addActionListener(this);
        _target.setActionCommand(_commande);
        _target.setToolTipText(EbliResource.EBLI.getString(_tooltip));
    }

    private BuButton construireBoutonIcon(final String _tooltip, final String _commande, final String _icon) {
        final BuButton r = new BuButton();
        initialiseBouton(r, _tooltip, _commande);
        r.setIcon(EbliResource.EBLI.getToolIcon(_icon));
        return r;
    }

    private BuToggleButton construireToggleBoutonIcon(final String _tooltip, final String _commande, final String _icon) {
        final BuToggleButton r = new BuToggleButton();
        initialiseBouton(r, _tooltip, _commande);
        r.setIcon(EbliResource.EBLI.getToolIcon(_icon));
        return r;
    }

    private BuButton construireBoutonText(final String _tooltip, final String _commande, final String _text) {
        final BuButton r = new BuButton();
        initialiseBouton(r, _tooltip, _commande);
        r.setText(EbliResource.EBLI.getString(_text));
        return r;
    }

    private BuComboBox construireCh(final String _tooltip, final OptionItem[] _items) {
        final BuComboBox r = new BuComboBox();
        r.setModel(new DefaultComboBoxModel(_items));
        r.setRenderer(new LabelRenderer());
        r.addItemListener(this);
        r.setToolTipText(EbliResource.EBLI.getString(_tooltip));
        return r;
    }

    private void construireSpecificTools() {
        if (specificToolsConstruits_) {
            return;
        }
        btMiseEnPage_ = construireBoutonIcon("mise en page", "MISEENPAGE", "miseenpage");
        btAfficheMarges_ = construireToggleBoutonIcon("Afficher les marges", "MARGES", "reglemarge");
        btAfficheMarges_.setSelected(dessineMarges_);
        btPremierePage_ = construireBoutonIcon("Aller � la premi�re page", "PREMIERE", "reculerdebut");
        btPrecedent_ = construireBoutonIcon("Page pr�c�dente", "PRECEDENT", "reculer");
        btSuivant_ = construireBoutonIcon("Page suivante", "SUIVANT", "avancer");
        btDernierePage_ = construireBoutonIcon("Aller � la derni�re page", "DERNIERE", "avancerfin");
        chListePages_ = new BuComboBox();
        chListePages_.addItemListener(this);
        chListePages_.setToolTipText(EbliResource.EBLI.getString("Changer la premi�re page affich�e"));
        chVues_ = construireCh("Changer le type d'affichage", VUES);
        chVues_.setSelectedItem(getVue());
        chZooms_ = construireCh("Changer le zoom", ZOOMS);
        chZooms_.setSelectedItem(getZoom());
        btFermer_ = construireBoutonText("Fermer la pr�visualisation", "FERMER", "fermer");
        specificToolsConstruits_ = true;
        majEtatBoutonsChangementCible();
    }

    public JComponent[] getSpecificTools() {
        if (!specificToolsConstruits_) {
            construireSpecificTools();
        }
        final JComponent[] r = new JComponent[10];
        int i = 0;
        r[i++] = btMiseEnPage_;
        r[i++] = btAfficheMarges_;
        r[i++] = btPremierePage_;
        r[i++] = btPrecedent_;
        r[i++] = btSuivant_;
        r[i++] = btDernierePage_;
        r[i++] = chListePages_;
        r[i++] = chVues_;
        r[i++] = chZooms_;
        r[i++] = btFermer_;
        return r;
    }

    public String[] getEnabledActions() {
        return new String[] { "IMPRIMER", "MISEENPAGE" };
    }

    int getNbPagesDocument() {
        return target_.getNumberOfPages();
    }

    public EbliPageable getEbliPageable() {
        return target_;
    }

    public void setEbliPageable(final EbliPageable _target) {
        target_ = _target;
        setPremierePageAffichee(0);
        majEtatBoutonsChangementCible();
    }

    public void actionPerformed(final ActionEvent _e) {
        final String commande = _e.getActionCommand();
        if ("PREMIERE".equals(commande)) {
            allerPremierePage();
        } else if ("DERNIERE".equals(commande)) {
            allerDernierePage();
        } else if ("SUIVANT".equals(commande)) {
            suivant();
        } else if ("PRECEDENT".equals(commande)) {
            precedent();
        } else if ("MARGES".equals(commande)) {
            marges();
        } else if ("MISEENPAGE".equals(commande)) {
            modifieEbliPageFormat();
        } else if ("FERMER".equals(commande)) {
            dispose();
        }
    }

    public void itemStateChanged(final ItemEvent _e) {
        if (_e.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        final Object source = _e.getSource();
        if (source.equals(chListePages_)) {
            setPremierePageAffichee(chListePages_.getSelectedIndex());
        } else if (source.equals(chZooms_)) {
            setZoom((Zoom) chZooms_.getSelectedItem());
        } else if (source.equals(chVues_)) {
            setVue((Vue) chVues_.getSelectedItem());
        }
    }

    final class SimplePanelPreview extends JComponent {

        int page_;

        SimplePanelPreview() {
            this(0);
        }

        SimplePanelPreview(final int _page) {
            page_ = _page;
            this.setToolTipText("" + (page_ + 1) + "/" + getNbPagesDocument());
        }

        public void setPage(final int _page) {
            page_ = _page;
        }

        public int getPage() {
            return page_;
        }

        private PageFormat pageFormat() {
            return target_.getPageFormat(page_);
        }

        public int getPageHeight() {
            return (int) (pageFormat().getHeight());
        }

        public int getPageWidth() {
            return (int) (pageFormat().getWidth());
        }

        public double getPageImageableX() {
            return pageFormat().getImageableX();
        }

        public double getPageImageableY() {
            return pageFormat().getImageableY();
        }

        public double getPageImageableWidth() {
            return pageFormat().getImageableWidth();
        }

        public double getPageImageableHeight() {
            return pageFormat().getImageableHeight();
        }

        private boolean pageValide() {
            return ((page_ < target_.getNumberOfPages()) && (page_ > -1));
        }

        public Dimension getPreferredSize() {
            final Rectangle2D size = getAffineTransform().createTransformedShape(getDrawZoneAgrandie()).getBounds2D();
            return new Dimension((int) (size.getWidth()), (int) (size.getHeight()));
        }

        public String getToolTipText() {
            return EbliResource.EBLI.getString("page") + " " + (page_ + 1) + "/" + getNbPagesDocument();
        }

        public void paintComponent(final Graphics _g) {
            final Graphics2D g2d = (Graphics2D) _g;
            final Color initColor = g2d.getColor();
            final AffineTransform initTransform = g2d.getTransform();
            g2d.transform(getAffineTransform());
            final Shape initClip = _g.getClip();
            final Rectangle2D drawZone = getDrawZone();
            g2d.clip(getDrawZoneAgrandie());
            if (pageValide()) {
                paintPage(g2d, drawZone);
            } else {
                paintEmptyPage(g2d, drawZone);
            }
            g2d.setClip(initClip);
            g2d.setTransform(initTransform);
            g2d.setColor(initColor);
            super.paintComponent(_g);
        }

        private void paintPage(final Graphics2D _g2d, final Rectangle2D _drawZone) {
            _g2d.setColor(Color.white);
            _g2d.fill(_drawZone);
            try {
                target_.getPrintable(page_).print(_g2d, pageFormat(), 0);
            } catch (final PrinterException _ex) {
                FuLog.warning(_ex);
            }
            _g2d.setColor(Color.black);
            _g2d.draw(_drawZone);
            if (dessineMarges_) {
                paintMarge(_g2d);
            }
        }

        private void paintMarge(final Graphics2D _g2d) {
            final TraceLigne ligne = new TraceLigne();
            ligne.setTypeTrait(TraceLigne.TIRETE);
            ligne.setCouleur(Color.black);
            final int x = (int) getPageImageableX();
            final int y = (int) getPageImageableY();
            final int iw = (int) getPageImageableWidth();
            final int ih = (int) getPageImageableHeight();
            final int pw = getPageWidth();
            final int ph = getPageHeight();
            ligne.dessineTrait(_g2d, x, 0, x, ph);
            ligne.dessineTrait(_g2d, x + iw, 0, x + iw, ph);
            ligne.dessineTrait(_g2d, 0, y, pw, y);
            ligne.dessineTrait(_g2d, 0, y + ih, pw, y + ih);
        }

        private AffineTransform getAffineTransform() {
            AffineTransform r = new AffineTransform();
            final double zoomScale = zoom_.getScale();
            if (zoomScale == 0) {
                final Dimension dimAffichage = getContentPane().getSize();
                final double wZoom = (dimAffichage.getWidth()) / (vueEnCours_.nbCols() * (getPageWidth()));
                final int nbLignes = (int) (Math.ceil((double) (getNbPagesAffichees()) / (double) (vueEnCours_.nbCols())));
                final double hZoom = (dimAffichage.getHeight()) / (nbLignes * (getPageHeight()));
                final double zoomFinal = Math.min(hZoom, wZoom) * 0.95;
                r.scale(zoomFinal, zoomFinal);
            } else {
                final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
                r = gc.getNormalizingTransform();
                r.concatenate(gc.getDefaultTransform());
                if (zoomScale != 1) {
                    r.scale(zoomScale, zoomScale);
                }
            }
            return r;
        }

        private Rectangle2D getDrawZone() {
            return new Rectangle2D.Double(0, 0, getPageWidth(), getPageHeight());
        }

        private Rectangle2D getDrawZoneAgrandie() {
            return new Rectangle2D.Double(0, 0, getPageWidth() + 10, getPageHeight() + 10);
        }

        private void paintEmptyPage(final Graphics2D _g2d, final Shape _drawZone) {
            _g2d.setColor(Color.GRAY);
            _g2d.fill(_drawZone);
        }
    }
}

class OptionItem {

    public String texte_;

    public BuIcon icone_;

    public boolean activee_;

    public OptionItem(final String _texte, final String _icone) {
        this(_texte, _icone, true);
    }

    public OptionItem(final String _texte, final String _icone, final boolean _activee) {
        texte_ = EbliResource.EBLI.getString(_texte);
        activee_ = _activee;
        icone_ = EbliResource.EBLI.getToolIcon(_icone);
    }
}

final class Vue extends OptionItem {

    private int nbCols_;

    private int nbLignes_;

    protected Vue(final String _titre, final String _icone, final int _nbCols) {
        this(_titre, _icone, _nbCols, 0);
    }

    protected Vue(final String _titre, final String _icone, final int _nbCols, final int _nbLignes) {
        super(_titre, _icone);
        nbCols_ = Math.max(_nbCols, 1);
        nbLignes_ = _nbLignes;
    }

    protected int nbMaxPages() {
        return nbCols_ * nbLignes_;
    }

    protected int nbCols() {
        return nbCols_;
    }
}

final class Zoom extends OptionItem {

    private final double scale_;

    protected Zoom(final String _titre, final String _icone, final double _scale) {
        super(_titre, _icone);
        scale_ = _scale;
    }

    protected double getScale() {
        return scale_;
    }
}

final class LabelRenderer extends BuLabel implements ListCellRenderer {

    public Component getListCellRendererComponent(final JList _list, final Object _value, final int _index, final boolean _selected, final boolean _focus) {
        final OptionItem option = (OptionItem) _value;
        setHorizontalAlignment(LEFT);
        if (option == null) {
            this.setText("Erreur");
            this.setEnabled(false);
            this.setIcon(null);
        } else {
            this.setText(option.texte_);
            this.setEnabled(option.activee_ && _list.isEnabled());
            if (_index == -1) {
                this.setIcon(option.icone_);
                this.setDisabledIcon(option.icone_);
            } else {
                this.setIcon(null);
            }
        }
        if (_selected) {
            this.setOpaque(true);
            this.setBackground(_list.getSelectionBackground());
            this.setForeground(_list.getSelectionForeground());
            this.setBorder(UIManager.getBorder("ComboBox.selectedCellBorder"));
        } else {
            this.setOpaque(false);
            this.setBackground(_list.getBackground());
            this.setForeground(_list.getForeground());
            this.setBorder(UIManager.getBorder("ComboBox.cellBorder"));
        }
        return this;
    }
}
