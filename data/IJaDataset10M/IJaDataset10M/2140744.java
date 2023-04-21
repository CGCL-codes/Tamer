package org.fudaa.ebli.dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import com.memoire.bu.*;
import com.memoire.fu.FuLib;

/**
 * Un panneau qui peut etre facilement affichable dans une boite de dialogue. Cette classe reprend
 * le mode de fonctionnement de JOptionPane. Les methodes valide(), actionOk(), actionApply() et
 * actionCancel() peuvent �tre
 * @version $Id: EbliSimpleDialogPanel.java,v 1.21 2004-11-16 16:26:49 deniger Exp $
 * @author Bertrand Marchand,Fred Deniger
 */
public class EbliSimpleDialogPanel extends BuPanel implements ActionListener {

    public static final String CHOOSE_FILE_OPEN_DIR = "fudaaChooseFileopenDir";

    public static final String CHOOSE_FILE_TXT_FIELD_PROPERTY = "fudaaChooseFileTxtField";

    private String helpText_;

    protected String getHelpActionCommand() {
        return "COM_HELP";
    }

    public String getHelpText() {
        return helpText_;
    }

    /**
   * @param _s le nouveau texte d'aide
   */
    public final void setHelpText(String _s) {
        helpText_ = _s;
    }

    public static BuPanel getInfosPanel(BuInformationsSoftware _isoft) {
        BuPanel mpi = new BuPanel();
        mpi.setLayout(new BuVerticalLayout(5));
        mpi.setBorder(new EmptyBorder(5, 5, 5, 5));
        mpi.setBackground(mpi.getBackground().darker());
        mpi.setForeground(Color.white);
        BuPicture ml_logo = null;
        if (_isoft.logo != null) {
            ml_logo = new BuPicture(_isoft.logo);
        }
        BuLabelMultiLine ml_isoft = new BuLabelMultiLine(_isoft.name + "\nversion: " + _isoft.version + "\ndate: " + _isoft.date);
        ml_isoft.setFont(BuLib.deriveFont("Label", Font.PLAIN, -2));
        ml_isoft.setForeground(Color.white);
        if (ml_logo != null) mpi.add(ml_logo);
        mpi.add(ml_isoft);
        return mpi;
    }

    public static final boolean isCancelResponse(int r) {
        return r == JOptionPane.CANCEL_OPTION;
    }

    public static final boolean isOkResponse(int r) {
        return r == JOptionPane.OK_OPTION;
    }

    private BuLabelMultiLine lbError_;

    protected boolean modale_;

    /**
   * @param _errorText si true, un label est cree pour contenir les messages d'erreur eventuels.
   */
    public EbliSimpleDialogPanel(boolean _errorText) {
        if (_errorText) setErrorTextEnable();
    }

    /**
   * Appelle le constructeur en validant l'usage des messages d'erreur.
   */
    public EbliSimpleDialogPanel() {
        this(true);
    }

    public JComponent getError() {
        return lbError_;
    }

    protected BuTextField addDoubleText() {
        return addDoubleText(this);
    }

    protected BuTextField addDoubleText(Container _c) {
        BuTextField r = new BuTextField(20);
        r.setCharValidator(BuCharValidator.DOUBLE);
        r.setStringValidator(BuStringValidator.DOUBLE);
        r.setValueValidator(BuValueValidator.DOUBLE);
        _c.add(r);
        return r;
    }

    protected BuTextField addDoubleText(Container _c, double _d) {
        BuTextField r = addDoubleText(_c);
        r.setText(_d + "");
        return r;
    }

    protected BuTextField addDoubleText(Container _c, String _d) {
        BuTextField r = addDoubleText(_c);
        r.setText(_d);
        return r;
    }

    protected BuTextField addDoubleText(String _d) {
        return addDoubleText(this, _d);
    }

    protected BuTextField addIntegerText() {
        return addIntegerText(this);
    }

    protected BuTextField addIntegerText(Container _c, int _d) {
        BuTextField r = addIntegerText(_c);
        r.setText(_d + "");
        return r;
    }

    protected BuTextField addIntegerText(Container _c, String _d) {
        BuTextField r = addIntegerText(_c);
        r.setText(_d);
        return r;
    }

    protected BuTextField addIntegerText(int _d) {
        return addIntegerText(this, _d);
    }

    protected BuTextField addIntegerText(String _d) {
        return addIntegerText(this, _d);
    }

    protected JTextField addLabelFileChooserPanel(String _label) {
        return addLabelFileChooserPanel(this, _label, null, false, false);
    }

    protected JTextField addLabelFileChooserPanel(Container _c, String _label) {
        return addLabelFileChooserPanel(_c, _label, null, false, false);
    }

    protected BuTextField addLongText(Container _c, long _d) {
        return addLongText(_c, Long.toString(_d));
    }

    protected BuTextField addLongText(Container _c, String _d) {
        BuTextField r = addLongText(_c);
        r.setText(_d);
        return r;
    }

    protected BuTextField addLongText(long _d) {
        return addLongText(this, _d);
    }

    protected BuTextField addLongText(String _d) {
        return addLongText(this, _d);
    }

    /**
   * ajoute au panel un label dont la couleur de texte est rouge.
   */
    protected BuLabel addRedLabel() {
        return addRedLabel(this);
    }

    protected BuLabel addRedLabel(Container _c) {
        BuLabel r = new BuLabel();
        r.setForeground(Color.red);
        _c.add(r);
        return r;
    }

    protected BuTextField addStringText() {
        return addStringText(this);
    }

    protected BuTextField addStringText(Container _c) {
        BuTextField txt = new BuTextField();
        _c.add(txt);
        return txt;
    }

    protected BuTextField addStringText(Container _c, String _s) {
        BuTextField txt = new BuTextField();
        _c.add(txt);
        txt.setText(_s);
        return txt;
    }

    protected BuTextField addStringText(String _s) {
        return addStringText(this, _s);
    }

    /**
   * Recupere les evt des boutons ok,cancel et apply. bouton OK : valide(), actionApply(),actionOK()
   * et actionClose() ) <br/>bouton cancel : actionCancel() et et actionClose() <br/>bouton apply :
   * valide(), actionApply() <br/>
   */
    public void actionPerformed(ActionEvent _evt) {
        String com = _evt.getActionCommand();
        if ("...".equals(com)) {
            JComponent jsrc = (JComponent) _evt.getSource();
            Object o = jsrc.getClientProperty(CHOOSE_FILE_TXT_FIELD_PROPERTY);
            if (o instanceof JTextField) {
                JTextField txt = (JTextField) o;
                JLabel l = (JLabel) txt.getClientProperty("labeledBy");
                File initFile = null;
                File dir = null;
                String txtText = txt.getText();
                if ((txtText != null) && (txtText.trim().length() > 0)) {
                    initFile = new File(txt.getText());
                    dir = initFile.getParentFile();
                }
                JFileChooser bf = createFileChooser();
                Boolean b = (Boolean) jsrc.getClientProperty(CHOOSE_FILE_OPEN_DIR);
                if ((b != null) && (b.equals(Boolean.TRUE))) {
                    bf.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                }
                if ((dir != null) && (dir.isDirectory())) {
                    bf.setCurrentDirectory(dir);
                }
                if (l != null) bf.setDialogTitle(l.getText());
                bf.setMultiSelectionEnabled(false);
                int r = bf.showOpenDialog(this);
                if (r == JFileChooser.APPROVE_OPTION) {
                    File out = bf.getSelectedFile();
                    if ((initFile == null) || (!out.equals(initFile))) {
                        String s = out.getAbsolutePath();
                        txt.setText(s);
                        txt.setToolTipText(s);
                    }
                }
            }
        }
    }

    public void addEmptyBorder(int _b) {
        addEmptyBorder(this, _b);
    }

    public void addEmptyBorder(JComponent _c, int _b) {
        _c.setBorder(BorderFactory.createEmptyBorder(_b, _b, _b, _b));
    }

    public BuTextField addIntegerText(Container _c) {
        BuTextField r = new BuTextField(20);
        r.setCharValidator(BuCharValidator.INTEGER);
        r.setStringValidator(BuStringValidator.INTEGER);
        r.setValueValidator(BuValueValidator.INTEGER);
        _c.add(r);
        return r;
    }

    public BuLabel addLabel(Container _c, String _l) {
        BuLabel r = new BuLabel(_l);
        r.setVerticalAlignment(BuLabel.CENTER);
        _c.add(r);
        return r;
    }

    public BuLabel addLabel(String _l) {
        return addLabel(this, _l);
    }

    public BuTextField addLabelDoubleText(Container _c, String _label) {
        addLabel(_c, _label);
        return addDoubleText(_c);
    }

    public BuTextField addLabelDoubleText(String _label) {
        return addLabelDoubleText(this, _label);
    }

    /**
   * Permet de construire un panel avec un jtextfield et un bouton (...) permettant de selectionner
   * un fichier. Le textfield est automatiquement mis a jour. Le fileChooser est cree a partir de la
   * methode {@link #createFileChooser() createFileChooser}et il a pour titre <code>_label</code>
   * (si non nul).
   * @return JtextField contenant le chemin du fichier selectionne
   * @param _c le container dans lequel on veut ajoute le panel (et le label)
   * @param _label le texte du label mis devant le textfield. Si nul, aucun label n'est ajoute.
   * @param _initValue la valeur initiale du textfield
   * @param _chooseDir si true, le filechooser utilise sera en mode repertoire
   * @param _labelInSamePanel si true, le label est ajoute au panel cree. si false le label est
   *          ajoute en premier au container <code>_c</code>.
   */
    public JTextField addLabelFileChooserPanel(Container _c, String _label, String _initValue, boolean _chooseDir, boolean _labelInSamePanel) {
        BuPanel pn = new BuPanel();
        if (_label == null) pn.setLayout(new BuHorizontalLayout(0, false, true)); else pn.setLayout(new BuHorizontalLayout(5, false, true));
        JTextField r = new BuTextField();
        if (_initValue != null) {
            r.setToolTipText(_initValue);
            r.setText(_initValue);
        }
        r.setColumns(25);
        if (_label != null) {
            JLabel lb = _labelInSamePanel ? addLabel(pn, _label) : addLabel(_c, _label);
            lb.setVerticalTextPosition(SwingConstants.CENTER);
            lb.setLabelFor(r);
        }
        pn.add(r);
        JButton j = new BuButton("...");
        if (_chooseDir) {
            j.putClientProperty(CHOOSE_FILE_OPEN_DIR, Boolean.TRUE);
        }
        j.putClientProperty(CHOOSE_FILE_TXT_FIELD_PROPERTY, r);
        j.setActionCommand("...");
        j.addActionListener(this);
        pn.add(j);
        _c.add(pn);
        return r;
    }

    /**
   * Cree un panel de selection de fichier avec comme label <code>_label</code>
   * @param _labelInSamePanel si true, le label est ajoute au panel cree.Sinon, le label est ajoute
   *          a ce panel.
   */
    public JTextField addLabelFileChooserPanel(String _label, boolean _labelInSamePanel) {
        return addLabelFileChooserPanel(this, _label, null, false, _labelInSamePanel);
    }

    public JTextField addLabelFileChooserPanel(String _label, String _initValue) {
        return addLabelFileChooserPanel(this, _label, _initValue, false, false);
    }

    public JTextField addLabelFileChooserPanel(Container _cont, String _label, String _initValue) {
        return addLabelFileChooserPanel(_cont, _label, _initValue, false, false);
    }

    public JTextField addLabelFileChooserPanel(String _label, String _initValue, boolean _chooseDir) {
        return addLabelFileChooserPanel(this, _label, _initValue, _chooseDir, false);
    }

    public BuTextField addLabelIntegerText(Container _c, String _label) {
        addLabel(_c, _label);
        return addIntegerText(_c);
    }

    public BuTextField addLabelIntegerText(String _label) {
        return addLabelIntegerText(this, _label);
    }

    public BuTextField addLabelStringText(Container _c, String _label) {
        addLabel(_c, _label);
        return addStringText(_c);
    }

    public BuTextField addLabelStringText(String _label) {
        addLabel(_label);
        return addStringText();
    }

    public BuTextField addLongText() {
        return addLongText(this);
    }

    public BuTextField addLongText(Container _c) {
        BuTextField r = new BuTextField(20);
        r.setCharValidator(BuCharValidator.LONG);
        r.setStringValidator(BuStringValidator.LONG);
        r.setValueValidator(BuValueValidator.LONG);
        _c.add(r);
        return r;
    }

    /**
   * Affiche le panel dans une boite de dialogue.
   * @param _parent la boite de dialogue parent
   * @param _titre le titre de la boite de dialogue
   */
    public void affiche(Dialog _parent) {
        new EbliSimpleDialog(_parent, this).afficheDialog();
    }

    /**
   * Affiche le panel dans une boite de dialogue.
   * @param _parent la fenetre parent
   */
    public void affiche(Frame _parent) {
        new EbliSimpleDialog(_parent, this).afficheDialog();
    }

    /**
   * Affiche le panel dans une boite de dialogue modale.
   * @param _parent la fenetre parent
   * @return la reponse
   */
    public int afficheModale(Component _parent) {
        EbliSimpleDialog s = new EbliSimpleDialog(this);
        int r = s.afficheDialogModal();
        s.dispose();
        return r;
    }

    public int afficheModale(Component _parent, int _option) {
        EbliSimpleDialog s = new EbliSimpleDialog(this);
        s.setOption(_option);
        int r = s.afficheDialogModal();
        s.dispose();
        return r;
    }

    public int afficheModale(Component _parent, String _t, int _option) {
        EbliSimpleDialog s = new EbliSimpleDialog(this);
        s.setOption(_option);
        s.setTitle(_t);
        int r = s.afficheDialogModal();
        s.dispose();
        return r;
    }

    public int afficheModale(Component _parent, String _title) {
        EbliSimpleDialog s = new EbliSimpleDialog(this);
        s.setTitle(_title);
        int r = s.afficheDialogModal();
        s.dispose();
        return r;
    }

    /**
   * Affiche le panel dans une boite de dialogue modale.
   * @param _parent la boite de dialogue parent
   */
    public int afficheModale(Dialog _parent) {
        EbliSimpleDialog s = new EbliSimpleDialog(_parent, this);
        int r = s.afficheDialogModal();
        s.dispose();
        return r;
    }

    public int afficheModale(Dialog _parent, String _title) {
        EbliSimpleDialog d = new EbliSimpleDialog(_parent, this);
        d.setTitle(_title);
        int r = d.afficheDialogModal();
        d.dispose();
        return r;
    }

    /**
   * Affiche le panel dans une boite de dialogue modale.
   * @param _parent la fenetre parent
   */
    public int afficheModale(Frame _parent) {
        EbliSimpleDialog d = new EbliSimpleDialog(_parent, this);
        int r = d.afficheDialogModal();
        d.dispose();
        return r;
    }

    public int afficheModale(Frame _parent, String _title) {
        EbliSimpleDialog d = new EbliSimpleDialog(_parent, this);
        d.setTitle(_title);
        int r = d.afficheDialogModal();
        d.dispose();
        return r;
    }

    public int afficheModale(Frame _parent, String _title, Runnable run) {
        EbliSimpleDialog d = new EbliSimpleDialog(_parent, this);
        d.setTitle(_title);
        int r = d.afficheDialogModal(run);
        d.dispose();
        return r;
    }

    /**
   * M�thode � surcharger : elle est appelee si le bouton APPLY est active.
   */
    public void apply() {
    }

    public void cancel() {
    }

    public void cancelErrorText() {
        if (lbError_.isVisible()) {
            lbError_.setVisible(false);
            lbError_.setText("");
            doLayout();
        }
    }

    /**
   * Appeler avant la fermeture effective du dialogue.
   */
    public void closeDialog() {
    }

    public JFileChooser createFileChooser() {
        return new EbliFileChooser();
    }

    /**
   * Renvoie la chaine caracterisant le contenu de ce dialogue.
   */
    public Object getValue() {
        throw new NoSuchMethodError("Methode non implantee");
    }

    public void ok() {
        apply();
    }

    public void setErrorText(String _error) {
        if (lbError_ == null) {
            new Throwable().printStackTrace();
            return;
        }
        lbError_.setVisible(true);
        lbError_.setText(_error);
        lbError_.revalidate();
        lbError_.repaint();
    }

    public final void setErrorTextEnable() {
        if (lbError_ == null) {
            lbError_ = new BuLabelMultiLine();
            lbError_.setWrapMode(BuLabelMultiLine.WORD);
            lbError_.setForeground(Color.red);
            lbError_.revalidate();
        }
    }

    public void setErrorTextUnable() {
        lbError_ = null;
    }

    public void setModale(boolean _modale) {
        modale_ = _modale;
    }

    /**
   * Permet d'initialiser ce dialogue a l'aide d'un objet : envoie une exception par defaut.
   */
    public void setValue(Object _f) {
        throw new NoSuchMethodError("Methode non implantee");
    }

    public boolean valide() {
        return true;
    }
}
