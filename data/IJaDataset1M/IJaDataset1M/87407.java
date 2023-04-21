package at.vartmp.jschnellen.gui.helper;

import static at.vartmp.jschnellen.core.config.I18N.i18n;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import at.vartmp.jschnellen.core.util.BeanManager;
import at.vartmp.jschnellen.gui.MainGui;
import at.vartmp.jschnellen.gui.dialogs.AbstractDialog;
import at.vartmp.jschnellen.gui.modules.Gamefield;
import at.vartmp.jschnellen.gui.modules.ISchriftPanel;
import at.vartmp.jschnellen.gui.modules.IStatusPanel;
import at.vartmp.jschnellen.gui.parts.AOpponentPanel;
import at.vartmp.jschnellen.gui.parts.ChatPanel;
import at.vartmp.jschnellen.gui.parts.Kartendeck;
import at.vartmp.jschnellen.gui.parts.centerPanel.CenterPanel;

/**
 * @author Lukas Tischler [tischler.lukas_AT_gmail.com]
 */
public final class GuiBeanManager extends BeanManager {

    private GuiBeanManager() {
        super();
        this.loadBeanResource("beans/guibeans.xml");
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link javax.swing.JDialog} <br>
	 * 
	 * @param component
	 *            the name of the color
	 * @return an instance of <code>JDialog</code> or <code>null</code>
	 */
    public synchronized AbstractDialog getDialog(String component) {
        Object o = this.getObject(component, AbstractDialog.class);
        return (AbstractDialog) o;
    }

    /**
	 * This method returns an AbstractButton or <code>null</code> if the
	 * requested bean is not an instance of the class
	 * {@link javax.swing.AbstractButton} <br>
	 * The returned AbstractButton gets the ActionCommand from the
	 * <code>messages.properties</code> defined by the key <code>comonent</code>
	 * .<br>
	 * For example a button with the bean-id <code>button.ok</code> has the
	 * action command <code>button.ok</code> and this is also the the
	 * message-key defined in <code>messages.properties</code> <br>
	 * 
	 * @param component
	 *            the name of the color
	 * @return an instance of <code>AbstractButton</code> or <code>null</code>
	 */
    public synchronized AbstractButton getButton(String component) {
        Object o = this.getObject(component, AbstractButton.class);
        AbstractButton toReturn = (AbstractButton) o;
        toReturn.setActionCommand(component);
        toReturn.setText(i18n(component));
        return toReturn;
    }

    /**
	 * This method returns a instance of JLabel or <code>null</code> if the
	 * requested bean is not an instance of the class {@link javax.swing.JLabel}
	 * .<br>
	 * The returned JLabel gets the text from the
	 * <code>messages.properties</code> defined by the key <code>comonent</code>
	 * .<br>
	 * For example a label called <code>x.label</code> has the message-key
	 * <code>x</code>.<br>
	 * <br>
	 * <b>Note</b>: Each BeanDefinition has to end with <code>.label</code>
	 * 
	 * @param component
	 *            the name of the color
	 * @return an instance of <code>JLabel</code> or <code>null</code>
	 */
    public synchronized JLabel getLabel(String component) {
        Object o = this.getObject(component + ".label", JLabel.class);
        JLabel toReturn = (JLabel) o;
        toReturn.setText(i18n(component));
        return toReturn;
    }

    /**
	 * This method returns a {@link JCheckBox} or <code>null</code> if the
	 * requested bean is not an instance of the class
	 * {@link javax.swing.JCheckBox} <br>
	 * <br>
	 * <b>Note</b>: Each BeanDefinition has to end with <code>.checkbox</code>
	 * 
	 * @param component
	 *            the name of the checkbox
	 * @return an instance of <code>JCheckBox</code> or <code>null</code>
	 */
    public synchronized JCheckBox getCheckbox(String component) {
        Object o = this.getObject(component + ".checkbox", JCheckBox.class);
        return (JCheckBox) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link javax.swing.JLabel} <br>
	 * <br>
	 * <b>Note</b>: Each BeanDefinition has to end with <code>.textfield</code>
	 * 
	 * @param component
	 *            the name of the color
	 * @return an instance of <code>JLabel</code> or <code>null</code>
	 */
    public synchronized JTextField getJTextField(String component) {
        Object o = this.getObject(component + ".textfield", JTextField.class);
        return (JTextField) o;
    }

    /**
	 * This method returns a JComponent or null if the requested bean is not an
	 * instance of the class {@link javax.swing.JComponent} <br>
	 * 
	 * @param component
	 *            the name of the JComponent
	 * @return an instance of <code>JComponent</code> or <code>null</code>
	 */
    public synchronized JComponent getJComponent(String component) {
        Object o = this.getObject(component, JComponent.class);
        return (JComponent) o;
    }

    /**
	 * This method returns a ImageIcon or null if the requested bean is not an
	 * instance of the class {@link javax.swing.ImageIcon} <br>
	 * <br>
	 * <b>Note</b>: Each BeanDefinition has to end with <code>.icon</code>
	 * 
	 * @param icon
	 *            the name of the icon
	 * @return an instance of <code>ImageIcon</code> or <code>null</code>
	 */
    public synchronized ImageIcon getImageIcon(String icon) {
        Object o = this.getObject(icon + ".icon", ImageIcon.class);
        return (ImageIcon) o;
    }

    /**
	 * This method returns a ActionListener or null if the requested bean is not
	 * an instance of the class {@link java.awt.event.ActionListener} <br>
	 * 
	 * @param listener
	 *            the name of the ActionListener
	 * @return an instance of <code>JComponent</code> or <code>null</code>
	 */
    public synchronized ActionListener getActionListener(String listener) {
        Object o = this.getObject(listener, ActionListener.class);
        return (ActionListener) o;
    }

    /**
	 * This method returns a ActionListener or null if the requested bean is not
	 * an instance of the class {@link java.awt.event.ActionListener} <br>
	 * 
	 * @param listener
	 *            the name of the ActionListener
	 * @return an instance of <code>JComponent</code> or <code>null</code>
	 */
    public synchronized MouseListener getMouseListener(String listener) {
        Object o = this.getObject(listener, MouseListener.class);
        return (MouseListener) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link at.vartmp.jschnellen.gui.parts.Kartendeck} <br>
	 * 
	 * @return an instance of <code>Kartendeck</code> or <code>null</code>
	 */
    public synchronized Kartendeck getKartendeck() {
        Object o = this.getObject("kartendeck", Kartendeck.class);
        return (Kartendeck) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class
	 * {@link at.vartmp.jschnellen.gui.parts.centerPanel.CenterPanel} <br>
	 * 
	 * @return an instance of <code>AnsagePanel</code> or <code>null</code>
	 */
    public synchronized CenterPanel getCenterPanel() {
        Object o = this.getObject("centerPanel", CenterPanel.class);
        return (CenterPanel) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link Gamefield} <br>
	 * 
	 * @return an instance of <code>AnsagePanel</code> or <code>null</code>
	 */
    public synchronized Gamefield getGamefield() {
        Object o = this.getObject("gamefield", Gamefield.class);
        return (Gamefield) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link at.vartmp.jschnellen.gui.MainGui} <br>
	 * 
	 * @return an instance of <code>CenterPanel</code> or <code>null</code>
	 */
    public synchronized MainGui getMainGui() {
        Object o = this.getObject("mainGui", MainGui.class);
        return (MainGui) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link at.vartmp.jschnellen.gui.parts.ChatPanel} <br>
	 * 
	 * @return an instance of <code>CenterPanel</code> or <code>null</code>
	 */
    public synchronized ChatPanel getChatPanel() {
        Object o = this.getObject("chatPanel", ChatPanel.class);
        return (ChatPanel) o;
    }

    /**
	 * This method returns a color or null if the requested bean is not an
	 * instance of the class {@link IStatusPanel} <br>
	 * 
	 * @return an instance of <code>IStatusPanel</code> or <code>null</code>
	 */
    public synchronized IStatusPanel getStatusText() {
        Object o = this.getObject("statusText", IStatusPanel.class);
        return (IStatusPanel) o;
    }

    /**
	 * This method returns the implementation of {@link ISchriftPanel} or
	 * <code>null</code> if the requested bean is not an instance of the class
	 * {@link ISchriftPanel} <br>
	 * 
	 * @return an instance of <code>ISchriftPanel</code> or <code>null</code>
	 */
    public synchronized ISchriftPanel getSchriftText() {
        Object o = this.getObject("schriftText", ISchriftPanel.class);
        return (ISchriftPanel) o;
    }

    /**
	 * Gets the {@link ServerLoader}
	 * 
	 * @return the server loader
	 */
    public synchronized ServerLoader getServerLoader() {
        Object o = this.getObject("serverLoader", ServerLoader.class);
        return (ServerLoader) o;
    }

    /**
	 * Gets the opponent panels.
	 * 
	 * @return the opponent panels
	 */
    public synchronized AOpponentPanel<?>[] getOpponentPanels() {
        AOpponentPanel<?> opponents[] = new AOpponentPanel<?>[] { (AOpponentPanel<?>) getObject("opponentLeft", AOpponentPanel.class), (AOpponentPanel<?>) getObject("opponentTop", AOpponentPanel.class), (AOpponentPanel<?>) getObject("opponentRight", AOpponentPanel.class) };
        return opponents;
    }

    /**
	 * Gets the {@link InputChecker}
	 * 
	 * @return the input checker
	 */
    public synchronized InputChecker getInputChecker() {
        Object o = this.getObject("inputChecker", InputChecker.class);
        return (InputChecker) o;
    }

    private static class SingletonHolder {

        private static final GuiBeanManager beanManager = new GuiBeanManager();
    }

    /**
	 * Gets the single instance of BeanManager.
	 * 
	 * @return single instance of BeanManager
	 */
    public static GuiBeanManager getInstance() {
        return SingletonHolder.beanManager;
    }
}
