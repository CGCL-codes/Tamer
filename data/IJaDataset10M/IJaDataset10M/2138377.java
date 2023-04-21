package jmri.jmrit.vsdecoder;

import java.beans.PropertyChangeListener;
import org.jdom.Element;
import javax.swing.JButton;
import javax.swing.JComponent;

public class MomentarySoundEvent extends SoundEvent implements PropertyChangeListener {

    JButton button;

    Trigger t;

    ButtonTrigger bt;

    public MomentarySoundEvent() {
        this(null, null);
    }

    public MomentarySoundEvent(String n) {
        this(n, n);
    }

    public MomentarySoundEvent(String n, String bl) {
        super(n, bl);
        button = null;
    }

    @Override
    public boolean hasButton() {
        if ((buttontype == ButtonType.NONE) || (buttontype == ButtonType.ENGINE) || (button == null)) return (false); else return (true);
    }

    public void setButton(JButton b) {
        button = b;
    }

    @Override
    public JComponent getButton() {
        return (button);
    }

    @Override
    public void setButtonLabel(String bl) {
        button.setText(bl);
    }

    @Override
    public String getButtonLabel() {
        return (button.getText());
    }

    @Override
    protected ButtonTrigger setupButtonAction(Element te) {
        bt = new ButtonTrigger(te.getAttributeValue("name"));
        button_trigger_list.put(bt.getName(), bt);
        log.debug("new ButtonTrigger " + bt.getName() + " type " + buttontype.toString());
        button.addMouseListener(bt);
        return (bt);
    }

    @Override
    public Element getXml() {
        Element me = new Element("SoundEvent");
        me.setAttribute("name", name);
        me.setAttribute("label", me.getText());
        for (Trigger t : trigger_list.values()) {
            me.addContent(t.getXml());
        }
        return (me);
    }

    @Override
    public void setXml(Element el) {
        this.setXml(el, null);
    }

    @Override
    public void setXml(Element el, VSDFile vf) {
        button = new JButton();
        super.setXml(el, vf);
        button.setText(el.getAttributeValue("label"));
    }

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MomentarySoundEvent.class.getName());
}
