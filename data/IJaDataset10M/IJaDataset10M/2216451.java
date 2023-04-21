package anylayout.examples;

import anylayout.Constraint;
import anylayout.LayoutContext;
import anylayout.SizeCalculator;
import anylayout.extras.BorderLayoutEmulationUtility;
import anylayout.extras.ConstraintUtility;
import fj.F;
import java.awt.Component;
import java.awt.Container;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import static anylayout.AnyLayout.useAnyLayout;
import static anylayout.extras.ConstraintBuilder.buildWithSameLeftAsTop;
import static anylayout.extras.ConstraintUtility.bottomLeft;
import static anylayout.extras.ConstraintUtility.topLeft;
import static anylayout.extras.ConstraintUtility.topRight;
import static anylayout.extras.RelativeConstraints.above;
import static anylayout.extras.RelativeConstraints.below;
import static anylayout.extras.SizeCalculatorUtility.absoluteSize;
import static java.lang.Math.max;
import static javax.swing.BorderFactory.createTitledBorder;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 * Copying the interface shown here: http://i4.tinypic.com/2464cxl.png
 */
final class TextExample {

    public static void run(final Runnable exceptionHandler) {
        setSystemLookAndFeel(exceptionHandler);
        final JFrame frame = new JFrame();
        frame.setLocationByPlatform(true);
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        final Container contentPane = frame.getContentPane();
        useAnyLayout(contentPane, 0.5f, 0.5f, absoluteSize(300, 350), ConstraintUtility.typicalDefaultConstraint(exceptionHandler));
        final JLabel formulaSyntax = new JLabel("<html><h1>Formula syntax</h1><p>Molle supports <i>parenthesised</i> formula expressions with propositional<br> and modal operators.<br>A formula is a composition of subformulas with propositional or modal operators.<br>Such operators are recognized as follows:<br><br></html>");
        formulaSyntax.setVerticalAlignment(SwingConstants.TOP);
        final Map<String, String> map = new LinkedHashMap<String, String>();
        map.put("~", "NOT");
        map.put("&", "AND");
        map.put("|", "OR");
        final JPanel propPanel = TextExample.createTitledPanel("Propositional syntax", map);
        map.clear();
        map.put("[]   ", "NECESSARY <> POSSIBLY");
        final JPanel modalPanel = TextExample.createTitledPanel("Modal syntax", map);
        final F<LayoutContext, Integer> get3 = BorderLayoutEmulationUtility.constant(3);
        final F<LayoutContext, Integer> parentSize = new F<LayoutContext, Integer>() {

            public Integer f(final LayoutContext layoutContext) {
                return layoutContext.getParentSize();
            }
        };
        final F<LayoutContext, Integer> parentMinus3 = BorderLayoutEmulationUtility.minus(parentSize, get3);
        frame.add(formulaSyntax, buildWithSameLeftAsTop(get3).setWidth(parentMinus3).setHeight(new F<LayoutContext, Integer>() {

            public Integer f(final LayoutContext context) {
                return context.getLayoutInfo(propPanel).getOffset() - 10;
            }
        }));
        frame.add(modalPanel, bottomLeft(BorderLayoutEmulationUtility.<LayoutContext, Integer>constant(3), BorderLayoutEmulationUtility.<LayoutContext, Integer>constant(3)));
        frame.add(propPanel, above(modalPanel, 10));
        frame.pack();
        frame.setVisible(true);
    }

    private static void setSystemLookAndFeel(final Runnable exceptionHandler) {
        final String lookAndFeelName = getSystemLookAndFeelClassName();
        if (!lookAndFeelName.toLowerCase().contains("gtk")) try {
            UIManager.setLookAndFeel(lookAndFeelName);
        } catch (ClassNotFoundException e) {
            exceptionHandler.run();
        } catch (InstantiationException e) {
            exceptionHandler.run();
        } catch (IllegalAccessException e) {
            exceptionHandler.run();
        } catch (UnsupportedLookAndFeelException e) {
            exceptionHandler.run();
        }
    }

    private static JPanel createTitledPanel(final String title, final Map<String, String> contents) {
        final JPanel panel = new JPanel();
        final TitledBorder titledBorder = createTitledBorder(title);
        panel.setBorder(titledBorder);
        final Map<JLabel, JLabel> map = new LinkedHashMap<JLabel, JLabel>();
        final int padding = 5;
        useAnyLayout(panel, 0.5f, 0.5f, new SizeCalculator() {

            public int getWidth() {
                int max = titledBorder.getMinimumSize(panel).width;
                for (final Map.Entry<JLabel, JLabel> entry : map.entrySet()) max = max(max, entry.getKey().getPreferredSize().width + padding * 3 + entry.getValue().getPreferredSize().width);
                return max;
            }

            public int getHeight() {
                return panel.getInsets().top + panel.getInsets().bottom + (padding + map.entrySet().iterator().next().getKey().getPreferredSize().height) * map.size() + padding;
            }
        }, new F<Component, Constraint>() {

            public Constraint f(final Component component) {
                throw new UnsupportedOperationException();
            }
        });
        JLabel aboveLeft = null;
        JLabel aboveRight = null;
        for (final Map.Entry<String, String> entry : contents.entrySet()) {
            final JLabel left = new JLabel(entry.getKey());
            final JLabel right = new JLabel(entry.getValue());
            final int top = padding + panel.getInsets().top;
            if (aboveLeft == null) {
                panel.add(left, topLeft(BorderLayoutEmulationUtility.<LayoutContext, Integer>constant(top), BorderLayoutEmulationUtility.<LayoutContext, Integer>constant(padding)));
                panel.add(right, topRight(BorderLayoutEmulationUtility.constant(top), BorderLayoutEmulationUtility.constant(padding)));
            } else {
                panel.add(left, below(aboveRight, padding));
                panel.add(right, below(aboveLeft, padding));
            }
            aboveLeft = left;
            aboveRight = right;
            map.put(left, right);
        }
        return panel;
    }
}
