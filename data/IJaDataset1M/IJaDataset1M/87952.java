package deductions.runtime;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import n3_project.helpers.GuiResourceBundle;
import n3_project.helpers.ITriple;
import n3_project.helpers.Triple;
import unif.ITripleStoreRETE;
import unif.Instanciator;
import unif.TripleStoreDrools;

/**
 * Forward the relevant GUI events to the KB. This is done via introspection.
 * There is no decisions made in this Java code, just verbatim forwarding of the
 * event properties. See
 * <a href="http://deductions.svn.sourceforge.net/viewvc/deductions/html/runtime-architecture.html"
 * > runtime architecture</a>
 */
public class GUIKBAdapter implements ActionListener, FocusListener, WindowListener {

    private ITripleStoreRETE storage = new TripleStoreDrools();

    private Instanciator instantiator;

    /** activate new implementations of this N3 - Java mapping, see html/documentation.html#L5714 */
    boolean javaObjectsInDrools = true;

    public GUIKBAdapter(Instanciator instanciator_) {
        this.instantiator = instanciator_;
        if (instantiator == null) {
            instantiator = new Instanciator();
        }
        setInstantiator(instantiator);
    }

    /** */
    public GUIKBAdapter() {
    }

    final String objectToID(Object o) {
        String objectToID = null;
        if (instantiator != null) {
            objectToID = instantiator.objectToID(o);
        }
        if (objectToID == null) {
            return Triple.wrapAsURI("run", storage.createId("event"));
        }
        return "_:" + objectToID;
    }

    final String objectToIDOrLiteral(Object o) {
        if (Number.class.isAssignableFrom(o.getClass()) || o.getClass().equals(Boolean.class) || o.getClass().isPrimitive()) {
            return o.toString();
        } else if (String.class.isAssignableFrom(o.getClass())) {
            return "\"" + o.toString() + "\"";
        } else {
            return objectToID(o);
        }
    }

    /**
	 * use cases:
	 * <ol>
	 * <li>create the form from the KB, given a Resource and the
	 * Statements about it</li>
	 * <li>create the form from a Resource being a Class
	 * instance</li>
	 * </ol>
	 */
    @Override
    public final void actionPerformed(ActionEvent e) {
        Logger.getLogger("theDefault").warning(GuiResourceBundle.getString("guikbadapter.actionperformed"));
        forwardGUIEventsToKB(e);
    }

    /**
	 * forward GUI Events To KB, and fire All Rules
	 *
	 * @param e
	 */
    private void forwardGUIEventsToKB(AWTEvent e) {
        if (javaObjectsInDrools) {
            forwardGUIEventsToKBEmbed(e);
        } else {
            forwardGUIEventsToKBInstantiator(e);
        }
    }

    private void forwardGUIEventsToKBEmbed(AWTEvent e) {
        final Object source = e.getSource();
        if (source instanceof JTextField || source instanceof JButton) {
            final String Id = objectToID(e);
            if (storage instanceof TripleStoreDrools) {
                final TripleStoreDrools droolsStrorage = (TripleStoreDrools) storage;
                droolsStrorage.assign(Id, e);
                try {
                    droolsStrorage.fireAllRules();
                } catch (final Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println("GUIKBAdapter.forwardGUIEventsToKBEmbed(): Id " + Id + " - " + e);
            }
        }
    }

    private void forwardGUIEventsToKBInstantiator(AWTEvent e) {
        final String Id = objectToID(e);
        final Object source = e.getSource();
        if (source instanceof JTextField) {
            final JTextField textField = (JTextField) source;
            String textFieldId = textField.getName();
            if (textFieldId == null) {
                textFieldId = instantiator.objectToID(textField);
            }
            if (textFieldId != null) {
                storage.store(new Triple("_:" + textFieldId, Triple.wrapAsURI("javap", "text"), "\"" + textField.getText() + "\""));
            } else {
                System.err.println("GUIKBAdapter.forwardGUIEventsToKB(): textFieldId == null for " + textField.getText());
            }
            storage.store(new Triple(Id, Triple.wrapAsURI("app", "timestamp"), Long.toString(new Date().getTime())));
        }
        final List<Triple> list = ObjectProperties2N3.properties2N3(e, this, Id);
        for (final Triple triple : list) {
            storage.store(triple);
        }
        try {
            storage.fireAllRules();
            final List<ITriple> n3Result = storage.getN3Result();
            instantiator.instanciate(n3Result, true);
        } catch (final Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public final void focusGained(FocusEvent e) {
        forwardGUIEventsToKB(e);
    }

    /**
	 * use case 3. update the KB after a user action (Focus lost)
	 */
    @Override
    public final void focusLost(FocusEvent e) {
        Logger.getLogger("theDefault").info(MessageFormat.format(GuiResourceBundle.getString("guikbadapter.focuslost.0"), e));
        forwardGUIEventsToKB(e);
    }

    public final ITripleStoreRETE getStorage() {
        return storage;
    }

    /** instantiate JavaScript objects from the given N3 storage */
    public void setStorage(ITripleStoreRETE storage) {
        this.storage = storage;
        if (instantiator != null) {
            final List<ITriple> n3Result = storage.getN3Result();
            instantiator.instanciate(n3Result);
        }
    }

    public void setInstantiator(Instanciator instantiator) {
        this.instantiator = instantiator;
        instantiator.getEngine().put("GUIKBAdapter", this);
    }

    /** */
    public void addContainerListener(JFrame jf) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowopened"));
        forwardGUIEventsToKB(e);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowclosing"));
        forwardGUIEventsToKB(e);
    }

    @Override
    public void windowActivated(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowactivated"));
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowclosed"));
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowdeactivated"));
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowdeiconified"));
    }

    @Override
    public void windowIconified(WindowEvent e) {
        Logger.getLogger("theDefault").info(GuiResourceBundle.getString("guikbadapter.windowiconified"));
    }

    /** add a Titled Border */
    public final void addTitledBorder(JComponent c, String title) {
        final Border lb = LineBorder.createBlackLineBorder();
        c.setBorder(BorderFactory.createTitledBorder(lb, title));
    }

    public final void setBoldFont(JComponent component) {
        final Font font = new java.awt.Font("Dialog", Font.BOLD, 12);
        component.setFont(font);
    }

    private static final JPanel emptyPanel = new JPanel();

    /** add a Component to given {@link Container}, in such a way that index
	 * 1 can be called before index 0 ;
	 * this bring the functionality of mastering component order in generated
	 * application. */
    public final void add(Container cont, Component comp, int index) {
        final int size = cont.getComponentCount();
        if (index > size) {
            for (int position = size; position < index; position++) {
                cont.add(emptyPanel, position);
            }
        }
        cont.add(comp, index);
    }

    /**
	 * @return the javaObjectsInDrools
	 */
    public final boolean isJavaObjectsInDrools() {
        return javaObjectsInDrools;
    }

    /**
	 * @param javaObjectsInDrools the javaObjectsInDrools to set
	 */
    public void setJavaObjectsInDrools(boolean javaObjectsInDrools) {
        this.javaObjectsInDrools = javaObjectsInDrools;
    }

    public static void forceRepack(Container container) {
        GUIGenerationHelper.forceRepack(container);
    }

    public static void setLayout(Container container, LayoutManager layout) {
        Container actualContainer = container;
        if (container instanceof JFrame) {
            actualContainer = ((JFrame) container).getContentPane();
        }
        actualContainer.setLayout(layout);
    }
}
