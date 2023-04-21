package info.monitorenter.gui.chart.controls.errorbarwizard;

import info.monitorenter.gui.chart.IErrorBarPainter;
import info.monitorenter.gui.chart.IPointPainterConfigurableUI;
import info.monitorenter.gui.chart.events.ErrorBarPainterActionSetSegmentColor;
import info.monitorenter.gui.chart.events.ErrorBarPainterActionSetSegmentPainter;
import info.monitorenter.gui.chart.pointpainters.PointPainterDisc;
import info.monitorenter.gui.chart.pointpainters.PointPainterLine;
import info.monitorenter.gui.util.ColorIcon;
import info.monitorenter.util.FileUtil;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * A panel for selection of {@link info.monitorenter.gui.chart.IErrorBarPainter#setStartPointPainter(IPointPainterConfigurableUI)}
 * {@link info.monitorenter.gui.chart.IErrorBarPainter#setEndPointPainter(info.monitorenter.gui.chart.IPointPainterConfigurableUI)} and
 * {@link info.monitorenter.gui.chart.IErrorBarPainter#setConnectionPainter(info.monitorenter.gui.chart.IPointPainterConfigurableUI)} .
 * <p>
 * 
 * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann </a>
 * @version $Revision: 1.16 $
 */
public class ErrorBarPainterEditPanel extends JPanel {

    /**
     * Base implementation that allows selection of an <code>{@link info.monitorenter.gui.chart.IPointPainter}</code> and it's
     * <code>{@link java.awt.Color}</code> and holds the parental <code>{@link IErrorBarPainter}</code> to assign the painter to (as a
     * segment: start, end or connetion).
     * <p>
     * 
     * Implementations have to add the action listeners that defines to which segment of the <code>{@link IErrorBarPainter}</code>. the
     * configured painter will set to.
     * <p>
     * 
     * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
     * 
     * 
     * @version $Revision: 1.16 $
     */
    private class SegmentChooserPanel extends JPanel {

        /**
         * An adaptor that registers itself as a <code>{@link PropertyChangeListener}</code> on an
         * <code>{@link info.monitorenter.gui.chart.IErrorBarPainter.ISegment}</code> and sets the color to the constructor given <code>{@link ColorIcon}</code>.
         * <p>
         * 
         * @author <a href="mailto:Achim.Westermann@gmx.de">Achim Westermann</a>
         * 
         * 
         * @version $Revision: 1.16 $
         */
        final class ColorIconUpdater implements PropertyChangeListener {

            /** The color icon to adapt to the color of the segment. */
            private ColorIcon m_adaptee;

            /**
             * Constructor with the adaptee which will stick to the color of the given segment.
             * <p>
             * 
             * @param adaptee
             *            the color icon to adapt the color property of the segment to.
             */
            protected ColorIconUpdater(final ColorIcon adaptee) {
                this.m_adaptee = adaptee;
            }

            /**
             * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
             */
            public void propertyChange(final PropertyChangeEvent evt) {
                String property = evt.getPropertyName();
                if (ErrorBarPainterEditPanel.SegmentChooserPanel.this.m_segment.getPropertySegmentColor().equals(property)) {
                    this.m_adaptee.setColor((Color) evt.getNewValue());
                }
            }
        }

        /** Generated <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 6645527616687209066L;

        /** The error bar painter segment to configure. */
        protected IErrorBarPainter.ISegment m_segment;

        /**
         * Creates a panel that offers configuration of the given error bar painter.
         * <p>
         * 
         * @param errorBarPainterSegement
         *            the error bar painter segment to configure.
         */
        public SegmentChooserPanel(final IErrorBarPainter.ISegment errorBarPainterSegement) {
            super();
            this.m_segment = errorBarPainterSegement;
            this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), errorBarPainterSegement.getName(), TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION));
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.gridx = 0;
            final AbstractAction colorAction = new ErrorBarPainterActionSetSegmentColor(errorBarPainterSegement, this, "color");
            JComboBox painterSelector = new JComboBox();
            IPointPainterConfigurableUI<?> pointPainter;
            IPointPainterConfigurableUI<?> usedPainter = errorBarPainterSegement.getPointPainter();
            Action action;
            pointPainter = null;
            boolean painterSelected = false;
            action = new ErrorBarPainterActionSetSegmentPainter(errorBarPainterSegement, pointPainter, "Empty");
            painterSelector.addItem(action);
            if (usedPainter == null) {
                painterSelector.setSelectedItem(action);
            }
            pointPainter = new PointPainterLine();
            action = new ErrorBarPainterActionSetSegmentPainter(errorBarPainterSegement, pointPainter, FileUtil.cutExtension(pointPainter.getClass().getName()).getValue());
            painterSelector.addItem(action);
            if (usedPainter != null && pointPainter.getClass().equals(usedPainter.getClass())) {
                painterSelector.setSelectedItem(action);
                painterSelected = true;
            }
            pointPainter = new PointPainterDisc();
            action = new ErrorBarPainterActionSetSegmentPainter(errorBarPainterSegement, pointPainter, FileUtil.cutExtension(pointPainter.getClass().getName()).getValue());
            painterSelector.addItem(action);
            if (usedPainter != null && pointPainter.getClass().equals(usedPainter.getClass())) {
                painterSelector.setSelectedItem(action);
                painterSelected = true;
            }
            painterSelector.addActionListener(new ActionListener() {

                /**
                 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
                 */
                public void actionPerformed(final ActionEvent e) {
                    JComboBox source = (JComboBox) e.getSource();
                    Action currentAction = (Action) source.getSelectedItem();
                    currentAction.actionPerformed(e);
                    if (currentAction.getValue(Action.NAME).equals("Empty")) {
                        colorAction.setEnabled(false);
                    } else {
                        colorAction.setEnabled(true);
                    }
                }
            });
            this.add(painterSelector, gbc);
            ColorIcon colorIcon = new ColorIcon(errorBarPainterSegement.getColor());
            colorAction.putValue(Action.SMALL_ICON, colorIcon);
            colorAction.setEnabled(painterSelected);
            JButton colorChooserButton = new JButton(colorAction);
            gbc.gridy = 1;
            errorBarPainterSegement.addPropertyChangeListener(errorBarPainterSegement.getPropertySegmentColor(), new ColorIconUpdater(colorIcon));
            this.add(colorChooserButton, gbc);
        }
    }

    /** Generated <code>serialVersionUID</code>. */
    private static final long serialVersionUID = -6564631494967160532L;

    /** The error bar painter to configure with segments. */
    private IErrorBarPainter m_errorBarPainter;

    /**
     * Creates a panel that offers configuration of the given error bar painter.
     * <p>
     * 
     * @param errorBarPainter
     *            the error bar painter to configure.
     */
    public ErrorBarPainterEditPanel(final IErrorBarPainter errorBarPainter) {
        super();
        this.m_errorBarPainter = errorBarPainter;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(2, 2, 2, 2);
        this.add(new SegmentChooserPanel(this.m_errorBarPainter.getSegmentStart()), gbc);
        gbc.gridy = 1;
        this.add(new SegmentChooserPanel(this.m_errorBarPainter.getSegmentConnection()), gbc);
        gbc.gridy = 2;
        this.add(new SegmentChooserPanel(this.m_errorBarPainter.getSegmentEnd()), gbc);
    }
}
