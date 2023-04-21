package uk.ac.ebi.pride.gui.component.mzgraph;

import org.bushe.swing.event.ContainerEventServiceFinder;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.pride.data.controller.DataAccessController;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.action.PrideAction;
import uk.ac.ebi.pride.gui.action.impl.OpenHelpAction;
import uk.ac.ebi.pride.gui.component.DataAccessControllerPane;
import uk.ac.ebi.pride.gui.event.container.PeptideEvent;
import uk.ac.ebi.pride.gui.task.Task;
import uk.ac.ebi.pride.gui.task.impl.RetrievePeptideTask;
import uk.ac.ebi.pride.gui.utils.DefaultGUIBlocker;
import uk.ac.ebi.pride.gui.utils.GUIBlocker;
import uk.ac.ebi.pride.mzgraph.ChromatogramBrowser;
import uk.ac.ebi.pride.mzgraph.SpectrumBrowser;
import javax.help.CSH;
import javax.swing.*;
import java.awt.*;

/**
 * MzGraphViewPane is responsible for visualizing either spectrum or chromatogram
 *
 * User: rwang
 * Date: 03-Mar-2010
 * Time: 14:53:52
 */
public class MzGraphViewPane extends DataAccessControllerPane {

    private static final Logger logger = LoggerFactory.getLogger(MzGraphViewPane.class);

    /**
     * DataAccessController this component belongs to
     */
    private DataAccessController controller;

    /**
     * In memory spectrum browser
     */
    private SpectrumBrowser spectrumBrowser;

    /**
     * In memory chromatogram browser
     */
    private ChromatogramBrowser chromaBrowser;

    /**
     * True indicates it is the first spectrum to be visualized
     */
    private boolean isFirstSpectrum;

    /**
     * True indicates it is the first chromatogram to be visualized
     */
    private boolean isFirstChromatogram;

    /**
     * Reference to Desktop context
     */
    private PrideInspectorContext context;

    /**
     * Subscribe to peptide event
     */
    private SelectPeptideSubscriber peptideSubscriber;

    public MzGraphViewPane(DataAccessController controller) {
        super(controller);
    }

    @Override
    protected void setupMainPane() {
        context = (PrideInspectorContext) uk.ac.ebi.pride.gui.desktop.Desktop.getInstance().getDesktopContext();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    protected void addComponents() {
    }

    public SpectrumBrowser getSpectrumBrowser() {
        return spectrumBrowser;
    }

    public ChromatogramBrowser getChromaBrowser() {
        return chromaBrowser;
    }

    /**
     * Create spectrum browser to display spectrum
     */
    private void createSpectrumBrowser() {
        spectrumBrowser = new SpectrumBrowser();
        MzGraphPropertyPane propPane = new MzGraphPropertyPane();
        propPane.setPreferredSize(new Dimension(200, 200));
        this.addPropertyChangeListener(propPane);
        Icon propertyIcon = GUIUtilities.loadIcon(context.getProperty("property.small.icon"));
        String propertyDesc = context.getProperty("property.title");
        String propertyTooltip = context.getProperty("property.tooltip");
        spectrumBrowser.getSidePane().addComponent(propertyIcon, null, propertyTooltip, propertyDesc, propPane);
        Icon helpIcon = GUIUtilities.loadIcon(context.getProperty("help.icon.small"));
        String helpTooltip = context.getProperty("help.tooltip");
        PrideAction helpAction = new OpenHelpAction(null, helpIcon);
        helpAction.putValue(Action.SHORT_DESCRIPTION, helpTooltip);
        AbstractButton button = (AbstractButton) spectrumBrowser.getSidePane().addAction(helpAction, false);
        CSH.setHelpIDString(button, "help.mzgraph.spectra");
        button.addActionListener(new CSH.DisplayHelpFromSource(context.getMainHelpBroker()));
    }

    /**
     * Create chromatogram browser to display chromatogram
     */
    private void createChromatogramBrowser() {
        chromaBrowser = new ChromatogramBrowser();
        Icon helpIcon = GUIUtilities.loadIcon(context.getProperty("help.icon.small"));
        String helpTooltip = context.getProperty("help.tooltip");
        PrideAction helpAction = new OpenHelpAction(null, helpIcon);
        helpAction.putValue(Action.SHORT_DESCRIPTION, helpTooltip);
        AbstractButton button = (AbstractButton) chromaBrowser.getSidePane().addAction(helpAction, false);
        CSH.setHelpIDString(button, "help.mzgraph.chroma");
        button.addActionListener(new CSH.DisplayHelpFromSource(context.getMainHelpBroker()));
    }

    public void subscribeToEventBus() {
        EventService eventBus = ContainerEventServiceFinder.getEventService(this);
        peptideSubscriber = new SelectPeptideSubscriber();
        eventBus.subscribe(PeptideEvent.class, peptideSubscriber);
    }

    private class SelectPeptideSubscriber implements EventSubscriber<PeptideEvent> {

        @Override
        public void onEvent(PeptideEvent event) {
            Comparable peptideId = event.getPeptideId();
            Comparable protId = event.getIdentificationId();
            Task newTask = new RetrievePeptideTask(controller, protId, peptideId);
            newTask.setGUIBlocker(new DefaultGUIBlocker(newTask, GUIBlocker.Scope.NONE, null));
            uk.ac.ebi.pride.gui.desktop.Desktop.getInstance().getDesktopContext().addTask(newTask);
        }
    }
}
