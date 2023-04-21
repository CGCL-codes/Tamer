package org.fudaa.fudaa.lido.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;
import com.memoire.bu.BuButton;
import com.memoire.bu.BuLabel;
import com.memoire.bu.BuPanel;
import com.memoire.bu.BuTextField;
import com.memoire.bu.BuVerticalLayout;
import org.fudaa.dodico.corba.lido.SParametresCondLimBlocCLM;
import org.fudaa.dodico.corba.lido.SParametresCondLimPointLigneCLM;
import org.fudaa.ebli.commun.EbliPreferences;
import org.fudaa.ebli.dialog.BDialogContent;
import org.fudaa.fudaa.commun.projet.FudaaParamEvent;
import org.fudaa.fudaa.lido.LidoResource;
import org.fudaa.fudaa.lido.ihmhelper.gestion.LidoParamsHelper;

/**
 * @version      $Revision: 1.11 $ $Date: 2006-09-19 15:05:00 $ by $Author: deniger $
 * @author       Axel von Arnim
 */
public class LidoLimitePermEditor extends LidoCustomizer {

    public static final int LIMNI = LidoResource.LIMITE.LIMNI;

    public static final int HYDRO = LidoResource.LIMITE.HYDRO;

    private SParametresCondLimBlocCLM cl_;

    private BuTextField tfVal_, tfNomLoi_;

    private LidoParamsHelper ph_;

    private int type_;

    public LidoLimitePermEditor(final int type, final LidoParamsHelper ph) {
        super((type == LIMNI ? "Cote" : type == HYDRO ? "D�bit" : null) + " permanent");
        type_ = type;
        ph_ = ph;
        init();
    }

    public LidoLimitePermEditor(final BDialogContent parent, final int type, final LidoParamsHelper ph) {
        super(parent, (type == LIMNI ? "Cote" : type == HYDRO ? "D�bit" : null) + " permanent");
        type_ = type;
        ph_ = ph;
        init();
    }

    private void init() {
        final Container content = getContentPane();
        int n = 0;
        final BuPanel pn = new BuPanel();
        pn.setLayout(new BuVerticalLayout(5, true, true));
        pn.setBorder(new EmptyBorder(new Insets(INSETS_SIZE, INSETS_SIZE, INSETS_SIZE, INSETS_SIZE)));
        tfNomLoi_ = new BuTextField();
        pn.add(new BuLabel("Nom de la loi : "), n++);
        pn.add(tfNomLoi_, n++);
        tfVal_ = BuTextField.createDoubleField();
        if (type_ == HYDRO) {
            pn.add(new BuLabel("Entrez le d�bit : "), n++);
        } else if (type_ == LIMNI) {
            pn.add(new BuLabel("Entrez la cote : "), n++);
        }
        pn.add(tfVal_, n++);
        final BuButton btSupprimerLoi = new BuButton("Supprimer cette loi");
        btSupprimerLoi.setActionCommand("SUPPRIMERLOI");
        btSupprimerLoi.addActionListener(this);
        pn.add(btSupprimerLoi, n++);
        setNavPanel(EbliPreferences.DIALOG.VALIDER | EbliPreferences.DIALOG.ALIGN_CENTER);
        content.add(BorderLayout.CENTER, pn);
        pack();
    }

    public void actionPerformed(final ActionEvent e) {
        final String cmd = e.getActionCommand();
        if ("VALIDER".equals(cmd)) {
            if (getValeurs()) {
                objectModified();
                firePropertyChange("limite", null, cl_);
                firePropertyChange("valider", null, cl_);
            }
            if (verifieContraintes()) {
                fermer();
            }
        } else if ("SUPPRIMERLOI".equals(cmd)) {
            ph_.LOICLM().supprimeLoiclm(cl_);
            cl_ = null;
            objectDeleted();
            fermer();
        }
    }

    private boolean verifieContraintes() {
        return true;
    }

    public void setObject(final Object _n) {
        if (!(_n instanceof SParametresCondLimBlocCLM)) {
            return;
        }
        if (_n == cl_) {
            return;
        }
        final SParametresCondLimBlocCLM vp = cl_;
        cl_ = (SParametresCondLimBlocCLM) _n;
        setValeurs();
        setObjectModified(false);
        LIDO_MODIFY_EVENT = new FudaaParamEvent(this, 0, LidoResource.CLM, cl_, "loiclm " + (cl_.numCondLim));
        firePropertyChange("limite", vp, cl_);
    }

    public boolean restore() {
        return false;
    }

    protected boolean getValeurs() {
        boolean changed = false;
        if ((cl_ == null) || (cl_.point == null)) {
            return changed;
        }
        final Double val = (Double) tfVal_.getValue();
        if (val == null) {
            return changed;
        }
        final double realVal = val.doubleValue();
        if (type_ == HYDRO) {
            if ((cl_.point.length < 2) || (cl_.point[0].qLim != realVal)) {
                changed = true;
            }
            cl_.point = new SParametresCondLimPointLigneCLM[2];
            cl_.point[0] = new SParametresCondLimPointLigneCLM();
            cl_.point[1] = new SParametresCondLimPointLigneCLM();
            cl_.point[0].qLim = cl_.point[1].qLim = realVal;
            cl_.point[0].zLim = cl_.point[1].zLim = 0.;
            cl_.point[0].tLim = 1.;
            cl_.point[1].tLim = 2.;
            cl_.nbPoints = 2;
        } else if (type_ == LIMNI) {
            if ((cl_.point.length < 2) || (cl_.point[0].zLim != realVal)) {
                changed = true;
            }
            cl_.point = new SParametresCondLimPointLigneCLM[2];
            cl_.point[0] = new SParametresCondLimPointLigneCLM();
            cl_.point[1] = new SParametresCondLimPointLigneCLM();
            cl_.point[0].zLim = cl_.point[1].zLim = realVal;
            cl_.point[0].qLim = cl_.point[1].qLim = 0.;
            cl_.point[0].tLim = 1.;
            cl_.point[1].tLim = 2.;
            cl_.nbPoints = 2;
        }
        String nom = tfNomLoi_.getText();
        if ((nom == null) || (nom.trim().equals(""))) {
            nom = "LOI LIMITE";
        }
        if (!nom.equals(cl_.description)) {
            changed = true;
            cl_.description = nom;
        }
        return changed;
    }

    protected void setValeurs() {
        if ((cl_ == null) || (cl_.point == null) || (cl_.point.length == 0)) {
            return;
        }
        if (type_ == HYDRO) {
            tfVal_.setValue(new Double(cl_.point[0].qLim));
        } else if (type_ == LIMNI) {
            tfVal_.setValue(new Double(cl_.point[0].zLim));
        }
        tfNomLoi_.setText(cl_.description);
    }

    protected boolean isObjectModificationImportant(final Object o) {
        return (o == cl_);
    }
}
