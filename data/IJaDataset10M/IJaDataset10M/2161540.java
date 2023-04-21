package com.myapp.util.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.Border;

/**
 * 
 * @author andre
 */
public class Util {

    public static Point calculateScreenCenter(JFrame frame) {
        Dimension sc = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension fr = frame.getSize();
        int x = sc.width / 2;
        int y = sc.height / 2;
        x -= (fr.width / 2);
        y -= (fr.height / 2);
        return new Point(x, y);
    }

    public static void centerFrame(JFrame frame) {
        frame.setLocation(calculateScreenCenter(frame));
    }

    public static void quitOnClose(JFrame appFrame) {
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void title(JComponent comp, String title) {
        Border border = BorderFactory.createTitledBorder(title);
        comp.setBorder(border);
    }

    /**
     * sets the jfilechooser to "details view" (quick and dirty)
     * <br>
     * hack to set details view as default, look for a abstractButton
     * in the jfc's subcomponents with the same icon as detailsbutton
     * of the JFilechooser. when found, click it programmatically.
     * 
     * @param chooser
     *            the filechooser to be set to deteilsview
     */
    public static void clickOnDetailViewButton(JFileChooser chooser) {
        Component c = null;
        try {
            c = findSubComponent(AbstractButton.class, chooser, "getIcon", UIManager.getIcon("FileChooser.detailsViewIcon"));
            ((AbstractButton) c).doClick();
        } catch (Exception e) {
            e.printStackTrace();
            assert false : "caught " + e;
        }
    }

    /**
     * find a component in the subcomponents of a tree. you may specify a
     * property and a class to match.
     * 
     * @param clazz
     *            the class the component must be an instance of
     * @param parent
     *            the container the component must be child of
     * @param getter
     *            the name of the the getter method
     * @param getValue
     *            the expected return value of the getter
     * @return the component that is the first matching in the component tree
     * @throws Exception
     *             various
     */
    public static Component findSubComponent(Class<? extends Component> clazz, Container parent, String getter, Object getValue) throws Exception {
        List<Component> list = new ArrayList<Component>();
        for (Component component : parent.getComponents()) {
            if (clazz.isAssignableFrom(component.getClass())) list.add(clazz.cast(component));
            list.addAll(getAllChildren(clazz, (Container) component));
        }
        Method method = clazz.getMethod(getter);
        for (Component component : list) {
            Object testVal = method.invoke(component);
            if (getValue == null ? testVal == null : getValue.equals(testVal)) return component;
        }
        return null;
    }

    /**
     * collects the children of an component tree being instances of given class
     * 
     * @param clazz
     *            the class to match against
     * @param container
     *            the container which children are tested
     * @return a list with all of the matching subcomponents
     */
    private static List<Component> getAllChildren(Class<? extends Component> clazz, Container container) {
        List<Component> tList = new ArrayList<Component>();
        for (Component component : container.getComponents()) {
            if (clazz.isAssignableFrom(component.getClass())) tList.add(clazz.cast(component));
            tList.addAll(getAllChildren(clazz, (Container) component));
        }
        return tList;
    }
}
