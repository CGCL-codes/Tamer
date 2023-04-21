package de.sonivis.tool.view.statistics;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;
import de.sonivis.tool.core.SONIVISCore;
import de.sonivis.tool.core.eventhandling.EventManager;
import de.sonivis.tool.core.eventhandling.IListener;
import de.sonivis.tool.core.eventhandling.INetworkFilter;
import de.sonivis.tool.core.statistics.Statistic;
import de.sonivis.tool.core.statistics.StatisticCategory;
import de.sonivis.tool.core.statistics.StatisticManager;

/**
 * Display all statistics in tabs
 * 
 * @author Anne
 * @version $Revision: 1.83.4.6 $
 */
public class StatisticsView extends ViewPart implements ControlListener, IListener {

    public static final String ID_VIEW = "de.sonivis.tool.view.statistics.StatisticsView";

    private transient TabFolder folder;

    private ImageRegistry registry;

    private boolean startedViewNow = false;

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    @Override
    public final void createPartControl(final Composite parent) {
        EventManager.getInstance().addListener(EventManager.PRE_CONNECTION_CHANGE | EventManager.NETWORK_FILTER_CHANGE | EventManager.STATISTIC_CHANGE, this);
        final Composite globalContainer = new Composite(parent, SWT.Resize | SWT.BACKGROUND);
        globalContainer.setLayout(new FillLayout());
        folder = new TabFolder(globalContainer, SWT.Resize | SWT.BORDER);
        folder.setLayout(new FormLayout());
        this.registry = new ImageRegistry();
        startedViewNow = true;
        if (SONIVISCore.getInstance().isConnectionOn() && StatisticManager.getInstance().isStatisticsActivated()) {
            final Collection<StatisticCategory> categoryMap = StatisticManager.getInstance().getStatisticCategoryMap().values();
            for (final StatisticCategory category : categoryMap) {
                if (!category.getName().equals(StatisticCategory.NODE_STATISTICS.getName())) {
                    if (category.isStatisticsValidate()) {
                        statisticChanged(category.getDestination());
                    }
                }
            }
        }
    }

    /**
	 * Passing the focus request to the viewer's control.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
    @Override
    public void setFocus() {
    }

    @Override
    public final void dispose() {
        EventManager.getInstance().removeListener(EventManager.PRE_CONNECTION_CHANGE | EventManager.NETWORK_FILTER_CHANGE | EventManager.STATISTIC_CHANGE, this);
        super.dispose();
    }

    public void controlMoved(final ControlEvent e) {
    }

    public void controlResized(final ControlEvent e) {
    }

    private void statisticsChanged(final StatisticCategory category) {
        if (category != null && !category.getName().equals(StatisticCategory.NODE_STATISTICS.getName())) {
            boolean exists = false;
            for (int i = 0; i < folder.getItems().length; i++) {
                if (folder.getItem(i).getText().equals(category.getName())) {
                    exists = true;
                }
            }
            if (!exists) {
                final TabItem tabItem = new TabItem(folder, SWT.CANCEL);
                tabItem.setText(category.getName());
                final Canvas canvas = new Canvas(folder, SWT.Resize | SWT.BACKGROUND);
                tabItem.setControl(canvas);
                for (final Statistic<?> statistic : category.getStatistics()) {
                    if (statistic.getDestination() != null) {
                        registry.remove(statistic.getName());
                        registry.put(statistic.getName(), ImageDescriptor.createFromFile(null, (String) statistic.getDestination()));
                    }
                }
                canvas.addPaintListener(new PaintListener() {

                    public void paintControl(final PaintEvent e) {
                        int i = 0;
                        for (final Statistic<?> statistic : category.getStatistics()) {
                            if (statistic.getDestination() != null) {
                                final ImageData image = registry.get(statistic.getName()).getImageData();
                                image.transparentPixel = image.palette.getPixel(new RGB(255, 255, 255));
                                final Image transparentIdeaImage = new Image(null, image);
                                e.gc.drawImage(transparentIdeaImage, i * 300, 0);
                                transparentIdeaImage.dispose();
                                i++;
                            }
                        }
                    }
                });
            }
        }
    }

    private void statisticChanged(final String destination) {
        if (destination != null) {
            final String[] filenames = destination.split("\\" + File.separator);
            final int length = filenames.length;
            boolean exists = false;
            for (int i = 0; i < folder.getItems().length; i++) {
                if ((folder.getItem(i).getText().equals(filenames[length - 1])) || (folder.getItem(i).getText().equals(StatisticCategory.NODE_STATISTICS.getName()))) {
                    exists = true;
                }
            }
            if (!exists) {
                final TabItem tabItem = new TabItem(folder, SWT.CANCEL);
                tabItem.setText(filenames[length - 1]);
                final Canvas canvas = new Canvas(folder, SWT.Resize | SWT.BACKGROUND);
                tabItem.setControl(canvas);
                final File file = new File(destination);
                final Set<String> names = new HashSet<String>();
                if (file.exists()) {
                    final String[] list = file.list();
                    for (final String element : list) {
                        final File file1 = new File(destination + File.separator + element);
                        if ((file1.getName().contains(StatisticManager.FILE_FORMAT_STATISTICS)) && (!file1.isDirectory())) {
                            names.add(file1.getName());
                            registry.remove(file1.getName());
                            registry.put(file1.getName(), ImageDescriptor.createFromFile(null, file1.getPath()));
                        }
                    }
                }
                canvas.addPaintListener(new PaintListener() {

                    public void paintControl(final PaintEvent e) {
                        int i = 0;
                        for (final String name : names) {
                            final ImageData image = registry.get(name).getImageData();
                            final Image transparentIdeaImage = new Image(null, image);
                            e.gc.drawImage(transparentIdeaImage, i * 300, 0);
                            transparentIdeaImage.dispose();
                            i++;
                        }
                    }
                });
            }
        }
    }

    private void preConnectionChange() {
        if (!startedViewNow) {
            registry.dispose();
            while (folder.getItemCount() > 0) {
                folder.getItem(0).dispose();
            }
        }
        startedViewNow = false;
    }

    private void networkFilterChanged(final INetworkFilter filter, final boolean calculateSteps) {
        if (!calculateSteps) {
            for (final TabItem item : folder.getItems()) {
                if (!item.getText().equals(StatisticCategory.CONNECTOR_STATISTICS.getName()) && !item.getText().equals(StatisticCategory.CONNECTOR_TEMPORAL_STATISTICS.getName())) {
                    item.dispose();
                }
            }
        }
    }

    @Override
    public final void update(final int eventId, final Object... arguments) {
        switch(eventId) {
            case EventManager.PRE_CONNECTION_CHANGE:
                preConnectionChange();
                break;
            case EventManager.NETWORK_FILTER_CHANGE:
                if (arguments.length == 2 && arguments[0] instanceof INetworkFilter && arguments[1] instanceof Boolean) {
                    networkFilterChanged((INetworkFilter) arguments[0], (Boolean) arguments[1]);
                }
                break;
            case EventManager.STATISTIC_CHANGE:
                if (arguments.length == 1 && arguments[0] instanceof StatisticCategory) {
                    statisticsChanged((StatisticCategory) arguments[0]);
                }
                break;
            default:
                break;
        }
    }
}
