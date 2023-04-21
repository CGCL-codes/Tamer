package de.mse.mogwai.impl.swing;

import de.mse.mogwai.impl.swing.beans.*;
import org.omg.CORBA.ORB;
import de.mse.mogwai.MOGWAI.*;
import de.mse.mogwai.MOGWAI.MogwaiBaseComponentPackage.*;

/**
 * Implementation an an desktoppane servant.
 *
 * @author  Mirko Sertic
 */
public class MogwaiDesktopPaneImplementation extends MogwaiComponentImplementation implements MogwaiDesktopPaneOperations {

    private ExtendedDesktopPane m_delegate;

    private ORB m_orb;

    public MogwaiDesktopPaneImplementation(ORB orb) {
        super(new ExtendedDesktopPane());
        this.m_delegate = (ExtendedDesktopPane) this.getDelegate();
        this.m_orb = orb;
    }

    public MogwaiButton createButton(String name, String text, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createButton(name, text, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiLabel createLabel(String name, String text, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createLabel(name, text, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiList createList(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createList(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiTree createTree(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createTree(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiTextArea createTextArea(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createTextArea(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiTextField createTextField(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createTextField(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiFrame createFrame(String name) {
        return MogwaiComponentFactoryImplementation.getInstance().createFrame(name, m_orb);
    }

    public MogwaiPanel createPanel(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createPanel(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiGroupBox createGroupBox(String name, int xp, int yp, int width, int height) throws MogwaiNotSupportedException {
        return MogwaiComponentFactoryImplementation.getInstance().createGroupBox(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiRadioButton createRadioButton(String name, String text, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createRadioButton(name, text, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiCheckBox createCheckBox(String name, String text, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createCheckBox(name, text, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiComboBox createComboBox(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createComboBox(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiImage createImage(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createImage(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public MogwaiTabsheet createTabsheet(String name, int xp, int yp, int width, int height) {
        return MogwaiComponentFactoryImplementation.getInstance().createTabsheet(name, xp, yp, width, height, m_orb, this.m_delegate);
    }

    public void removeAll() {
        this.m_delegate.removeAll();
    }

    public MogwaiTable createTable(String name, int xp, int yp, int width, int height) throws MogwaiNotSupportedException {
        return MogwaiComponentFactoryImplementation.getInstance().createTable(name, xp, yp, width, height, this.m_orb, this.m_delegate);
    }

    public MogwaiDialog createDialog(String name) {
        return MogwaiComponentFactoryImplementation.getInstance().createDialog(name, m_orb, this.m_delegate);
    }

    public MogwaiDesktopPane createDesktopPane(String name) throws MogwaiNotSupportedException {
        return MogwaiComponentFactoryImplementation.getInstance().createDesktopPane(name, this.m_orb, this.m_delegate);
    }

    public MogwaiMDIChild createMDIChild(String name) {
        return MogwaiComponentFactoryImplementation.getInstance().createMDIChild(name, this.m_orb, this.m_delegate);
    }

    public MogwaiToolbar createToolbar(String name) throws MogwaiNotSupportedException {
        return MogwaiComponentFactoryImplementation.getInstance().createToolbar(name, m_orb, this.m_delegate);
    }

    public void setBackgroundFromResource(String ressourcename) {
        byte[] data = MogwaiRootImplementation.loadResourceAsByteArray(ressourcename);
        this.m_delegate.setBackgroundImage(new javax.swing.ImageIcon(data));
    }
}
