package de.tobiasmaasland.voctrain.client.practise;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.looks.LookUtils;
import de.tobiasmaasland.voctrain.client.util.component.ComponentHelper;
import de.tobiasmaasland.voctrain.client.util.component.YEDialog;
import de.tobiasmaasland.voctrain.client.util.component.YProgressBar;
import de.tobiasmaasland.voctrain.client.util.help.HelpToolkit;
import de.tobiasmaasland.voctrain.client.util.i18n.I18NConstants;
import de.tobiasmaasland.voctrain.client.util.i18n.I18NToolkit;
import fi.mmm.yhteinen.swing.core.YIComponent;
import fi.mmm.yhteinen.swing.core.component.YButton;
import fi.mmm.yhteinen.swing.core.component.label.YLabel;
import fi.mmm.yhteinen.swing.core.component.text.YTextArea;
import fi.mmm.yhteinen.swing.core.tools.YUIToolkit;

public class PractiseView extends YEDialog implements KeyListener {

    private static final long serialVersionUID = -9167488752086656941L;

    private Dimension windowSize = new Dimension(525, 510);

    private YTextArea txtVocableFront = new YTextArea();

    private YTextArea txtVocableBack = ComponentHelper.createYTextArea();

    private YTextArea txtSolutionBack = new YTextArea();

    private JScrollPane pneSolutionBack = null;

    private YTextArea txtPronoms = new YTextArea();

    private YButton btnOk = new YButton(I18NToolkit.getLabel(I18NConstants.COMMON_VIEW_OK));

    private int mnmOk = (I18NToolkit.findI18NMnemonic("practise.view.mnemonic.ok"));

    private YLabel lblCorrect = new YLabel();

    private YButton btnHelp = new YButton();

    private YButton btnYes = new YButton(I18NToolkit.getLabel("practise.view.button.yes"));

    private int mnmYes = I18NToolkit.findI18NMnemonic("practise.view.mnemonic.yes");

    private YButton btnNo = new YButton(I18NToolkit.getLabel("practise.view.button.no"));

    private int mnmNo = I18NToolkit.findI18NMnemonic("practise.view.mnemonic.no");

    private YLabel lblCorrectCounter = new YLabel();

    private YButton btnCharN = new YButton("ñ");

    private YProgressBar pgrCollectVocs = new YProgressBar();

    public PractiseView(String langFront, String langBack) {
        setProperties();
        addComponents(langFront, langBack);
        YUIToolkit.guessViewComponents(this);
        YUIToolkit.guessMVCNames(this, false, null);
        setMVCProperties();
        bindEscToClose();
        HelpToolkit.getInstance().bindToHelpKey(getRootPane(), "practise_window");
    }

    private void setProperties() {
        this.setTitle(I18NToolkit.getLabel("practise.view.title"));
        this.setSize(windowSize);
        if (LookUtils.IS_JAVA_6_OR_LATER) {
            this.setIconImage(ComponentHelper.getDefaultViewIcon());
        }
    }

    @SuppressWarnings("unchecked")
    private void setMVCProperties() {
        txtPronoms.getYProperty().put(YIComponent.READ_ONLY, new Boolean(true));
        txtSolutionBack.getYProperty().put(YIComponent.READ_ONLY, new Boolean(true));
        lblCorrect.getYProperty().put(YIComponent.READ_ONLY, new Boolean(true));
        lblCorrectCounter.getYProperty().put(YIComponent.READ_ONLY, new Boolean(true));
        txtPronoms.setMvcName("selectedVocabulary.pronoms");
        txtSolutionBack.setMvcName("selectedVocable.back");
        this.setMvcName("practiseView");
    }

    private void addComponents(String langFront, String langBack) {
        FormLayout formLayout = new FormLayout("fill:100dlu, 3dlu, fill:30dlu, 3dlu, fill:80dlu, 3dlu, pref, 3dlu, pref", "pref, 3dlu, fill:100dlu, 3dlu, pref, 3dlu, fill:100dlu, " + "3dlu, pref, 3dlu, pref, 3dlu, pref");
        PanelBuilder builder = new PanelBuilder(formLayout);
        CellConstraints cc = new CellConstraints();
        builder.setDefaultDialogBorder();
        builder.addSeparator(langFront, cc.xy(1, 1));
        builder.addSeparator("", cc.xy(3, 1));
        builder.addSeparator(langBack, cc.xy(5, 1));
        txtVocableFront.setFocusable(false);
        JScrollPane pneVoc1 = new JScrollPane(txtVocableFront);
        txtPronoms.setEditable(false);
        txtPronoms.setFocusable(false);
        JScrollPane pnePronoms = new JScrollPane(txtPronoms);
        txtVocableBack.addKeyListener(this);
        JScrollPane pneVoc2 = new JScrollPane(txtVocableBack);
        builder.add(pneVoc1, cc.xy(1, 3));
        builder.add(pnePronoms, cc.xy(3, 3));
        builder.add(pneVoc2, cc.xyw(5, 3, 3));
        btnHelp.setIcon(ComponentHelper.loadIcon("/de/tobiasmaasland/voctrain/resources/images/question32.png"));
        builder.add(btnHelp, cc.xyw(3, 5, 1));
        btnOk.addKeyListener(this);
        btnOk.setMnemonic(mnmOk);
        builder.add(btnOk, cc.xyw(5, 5, 3));
        txtSolutionBack.setFocusable(false);
        txtSolutionBack.setVisible(false);
        pneSolutionBack = new JScrollPane(txtSolutionBack);
        builder.add(pneSolutionBack, cc.xyw(5, 7, 3));
        btnCharN.setFocusable(false);
        btnCharN.setMnemonic(KeyEvent.VK_F1);
        JPanel pnlCharButtons = new JPanel();
        pnlCharButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnlCharButtons.add(btnCharN);
        builder.add(pnlCharButtons, cc.xy(1, 5));
        builder.addSeparator(I18NToolkit.getLabel("practise.view.separator.statistics"), cc.xyw(1, 11, 7));
        builder.add(lblCorrectCounter, cc.xyw(1, 13, 5));
        builder.add(lblCorrect, cc.xy(7, 13));
        pgrCollectVocs.setOrientation(SwingConstants.VERTICAL);
        pgrCollectVocs.setMinimum(0);
        pgrCollectVocs.setMaximum(12);
        builder.add(pgrCollectVocs, cc.xy(9, 3));
        btnYes.setEnabled(false);
        btnYes.addKeyListener(this);
        btnYes.setMnemonic(mnmYes);
        builder.add(btnYes, cc.xy(5, 9));
        btnNo.addKeyListener(this);
        btnNo.setMnemonic(mnmNo);
        builder.add(btnNo, cc.xy(7, 9));
        this.add(builder.getPanel());
    }

    /**
	 * If called with <b>true</b>, btnYes will be enabled and txtSolution will
	 * be set visible. <br>
	 * If called with <b>false</b>, btnYes will be disabled and txtSolution will
	 * be set invisible. txtVocable2 will also request focus.
	 * 
	 * @param result
	 *            If setting for a result (true) or for the next vocable (false)
	 */
    public void setButtonsVocable(boolean result) {
        this.btnYes.setEnabled(result);
        this.txtSolutionBack.setVisible(result);
        if (!result) {
            this.txtVocableBack.requestFocusInWindow();
        }
    }

    /**
	 * Requests focus for the button btnYes.
	 */
    public void requestFocusBtnYes() {
        this.btnYes.requestFocusInWindow();
    }

    /**
	 * Requests focus for the button btnNo.
	 */
    public void requestFocusBtnNo() {
        this.btnNo.requestFocusInWindow();
    }

    public YProgressBar getPgrCollectVocs() {
        return pgrCollectVocs;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (e.getComponent() == btnOk) {
                btnOk.doClick();
            } else if (e.getComponent() == btnYes) {
                btnYes.doClick();
            } else if (e.getComponent() == btnNo) {
                btnNo.doClick();
            }
        } else if (e.getKeyCode() == mnmYes) {
            if (e.getComponent() == btnNo || e.getComponent() == btnYes) {
                btnYes.doClick();
            }
        } else if (e.getKeyCode() == mnmNo) {
            if (e.getComponent() == btnNo || e.getComponent() == btnYes || e.getComponent() == btnOk) {
                btnNo.doClick();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F2) {
            if (e.getComponent() == txtVocableBack) {
                btnCharN.doClick();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
