package net.tourbook.ui.tourChart;

import java.util.ArrayList;
import net.tourbook.Messages;
import net.tourbook.application.TourbookPlugin;
import net.tourbook.chart.Chart;
import net.tourbook.chart.ChartDataModel;
import net.tourbook.chart.ISliderMoveListener;
import net.tourbook.chart.SelectionChartInfo;
import net.tourbook.chart.SelectionChartXSliderPosition;
import net.tourbook.data.TourData;
import net.tourbook.preferences.ITourbookPreferences;
import net.tourbook.tour.IDataModelListener;
import net.tourbook.tour.ITourEventListener;
import net.tourbook.tour.SelectionDeletedTours;
import net.tourbook.tour.SelectionTourData;
import net.tourbook.tour.SelectionTourId;
import net.tourbook.tour.SelectionTourIds;
import net.tourbook.tour.TourEvent;
import net.tourbook.tour.TourEventId;
import net.tourbook.tour.TourManager;
import net.tourbook.ui.ITourChartViewer;
import net.tourbook.ui.UI;
import net.tourbook.ui.action.ActionEditQuick;
import net.tourbook.ui.views.tourCatalog.SelectionTourCatalogView;
import net.tourbook.ui.views.tourCatalog.TVICatalogComparedTour;
import net.tourbook.ui.views.tourCatalog.TVICatalogRefTourItem;
import net.tourbook.ui.views.tourCatalog.TVICompareResultComparedTour;
import net.tourbook.util.PostSelectionProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.ViewPart;

/**
 * Shows the selected tour in a chart
 */
public class TourChartView extends ViewPart implements ITourChartViewer {

    public static final String ID = "net.tourbook.views.TourChartView";

    private final IPreferenceStore _prefStore = TourbookPlugin.getDefault().getPreferenceStore();

    private TourChartConfiguration _tourChartConfig;

    private TourData _tourData;

    private PostSelectionProvider _postSelectionProvider;

    private ISelectionListener _postSelectionListener;

    private IPropertyChangeListener _prefChangeListener;

    private ITourEventListener _tourEventListener;

    private IPartListener2 _partListener;

    private PageBook _pageBook;

    private Label _pageNoChart;

    private TourChart _tourChart;

    private void addPartListener() {
        _partListener = new IPartListener2() {

            public void partActivated(final IWorkbenchPartReference partRef) {
            }

            public void partBroughtToTop(final IWorkbenchPartReference partRef) {
            }

            public void partClosed(final IWorkbenchPartReference partRef) {
            }

            public void partDeactivated(final IWorkbenchPartReference partRef) {
            }

            public void partHidden(final IWorkbenchPartReference partRef) {
                if (partRef.getPart(false) == TourChartView.this) {
                    _tourChart.partIsHidden();
                }
            }

            public void partInputChanged(final IWorkbenchPartReference partRef) {
            }

            public void partOpened(final IWorkbenchPartReference partRef) {
            }

            public void partVisible(final IWorkbenchPartReference partRef) {
                if (partRef.getPart(false) == TourChartView.this) {
                    _tourChart.partIsVisible();
                }
            }
        };
        getViewSite().getPage().addPartListener(_partListener);
    }

    private void addPrefListener() {
        _prefChangeListener = new IPropertyChangeListener() {

            public void propertyChange(final PropertyChangeEvent event) {
                final String property = event.getProperty();
                if (property.equals(ITourbookPreferences.GRAPH_VISIBLE) || property.equals(ITourbookPreferences.GRAPH_X_AXIS) || property.equals(ITourbookPreferences.GRAPH_X_AXIS_STARTTIME)) {
                    _tourChartConfig = TourManager.createDefaultTourChartConfig();
                    if (_tourChart != null) {
                        _tourChart.updateTourChart(_tourData, _tourChartConfig, false);
                    }
                } else if (property.equals(ITourbookPreferences.TOUR_PERSON_LIST_IS_MODIFIED)) {
                    clearView();
                    showTour();
                } else if (property.equals(ITourbookPreferences.GRAPH_MOUSE_MODE)) {
                    _tourChart.setMouseMode(event.getNewValue());
                }
            }
        };
        _prefStore.addPropertyChangeListener(_prefChangeListener);
    }

    /**
	 * listen for events when a tour is selected
	 */
    private void addSelectionListener() {
        _postSelectionListener = new ISelectionListener() {

            public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
                if (part == TourChartView.this) {
                    return;
                }
                onSelectionChanged(selection);
            }
        };
        getSite().getPage().addPostSelectionListener(_postSelectionListener);
    }

    private void addTourEventListener() {
        _tourEventListener = new ITourEventListener() {

            public void tourChanged(final IWorkbenchPart part, final TourEventId eventId, final Object eventData) {
                if (part == TourChartView.this) {
                    return;
                }
                if (eventId == TourEventId.SEGMENT_LAYER_CHANGED) {
                    _tourChart.updateLayerSegment((Boolean) eventData);
                } else if (eventId == TourEventId.TOUR_CHART_PROPERTY_IS_MODIFIED) {
                    _tourChart.updateTourChart(true, true);
                } else if (eventId == TourEventId.TOUR_CHANGED && eventData instanceof TourEvent) {
                    if (_tourData == null || part == TourChartView.this) {
                        return;
                    }
                    final ArrayList<TourData> modifiedTours = ((TourEvent) eventData).getModifiedTours();
                    if (modifiedTours != null) {
                        final long chartTourId = _tourData.getTourId();
                        for (final TourData tourData : modifiedTours) {
                            if (tourData == null) {
                                clearView();
                                return;
                            }
                            if (tourData.getTourId() == chartTourId) {
                                updateChart(tourData);
                                _postSelectionProvider.clearSelection();
                                return;
                            }
                        }
                    }
                } else if (eventId == TourEventId.CLEAR_DISPLAYED_TOUR) {
                    clearView();
                } else if (eventId == TourEventId.UPDATE_UI) {
                    if (_tourData == null) {
                        return;
                    }
                    final Long tourId = _tourData.getTourId();
                    if (UI.containsTourId(eventData, tourId) != null) {
                        updateChart(TourManager.getInstance().getTourData(tourId));
                    }
                }
            }
        };
        TourManager.getInstance().addTourEventListener(_tourEventListener);
    }

    private void clearView() {
        _tourData = null;
        _tourChart.updateChart(null, false);
        _pageBook.showPage(_pageNoChart);
        _postSelectionProvider.clearSelection();
    }

    @Override
    public void createPartControl(final Composite parent) {
        createUI(parent);
        addSelectionListener();
        addPrefListener();
        addTourEventListener();
        addPartListener();
        getSite().setSelectionProvider(_postSelectionProvider = new PostSelectionProvider());
        showTour();
    }

    private void createUI(final Composite parent) {
        _pageBook = new PageBook(parent, SWT.NONE);
        _pageNoChart = new Label(_pageBook, SWT.NONE);
        _pageNoChart.setText(Messages.UI_Label_no_chart_is_selected);
        _tourChart = new TourChart(_pageBook, SWT.FLAT, true);
        _tourChart.setShowZoomActions(true);
        _tourChart.setShowSlider(true);
        _tourChart.setTourInfoActionsEnabled(true);
        _tourChart.setToolBarManager(getViewSite().getActionBars().getToolBarManager(), true);
        _tourChart.setContextProvider(new TourChartContextProvicer(this));
        _tourChart.addDoubleClickListener(new Listener() {

            public void handleEvent(final Event event) {
                if (_tourData.getTourPerson() != null) {
                    ActionEditQuick.doAction(TourChartView.this);
                }
            }
        });
        _tourChartConfig = TourManager.createDefaultTourChartConfig();
        _tourChart.addDataModelListener(new IDataModelListener() {

            public void dataModelChanged(final ChartDataModel chartDataModel) {
                chartDataModel.setTitle(TourManager.getTourTitleDetailed(_tourData));
            }
        });
        _tourChart.addSliderMoveListener(new ISliderMoveListener() {

            public void sliderMoved(final SelectionChartInfo chartInfoSelection) {
                _postSelectionProvider.setSelection(chartInfoSelection);
            }
        });
    }

    @Override
    public void dispose() {
        saveState();
        getSite().getPage().removePostSelectionListener(_postSelectionListener);
        getViewSite().getPage().removePartListener(_partListener);
        TourManager.getInstance().removeTourEventListener(_tourEventListener);
        _prefStore.removePropertyChangeListener(_prefChangeListener);
        super.dispose();
    }

    /**
	 * fire slider move event when the chart is drawn the first time or when the focus gets the
	 * chart, this will move the sliders in the map to the correct position
	 */
    private void fireSliderPosition() {
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                TourManager.fireEvent(TourEventId.SLIDER_POSITION_CHANGED, _tourChart.getChartInfo(), TourChartView.this);
            }
        });
    }

    public ArrayList<TourData> getSelectedTours() {
        if (_tourData == null) {
            return null;
        }
        final ArrayList<TourData> tourList = new ArrayList<TourData>();
        tourList.add(_tourData);
        return tourList;
    }

    public TourChart getTourChart() {
        return _tourChart;
    }

    private void onSelectionChanged(final ISelection selection) {
        if (selection instanceof SelectionTourData) {
            final TourData selectionTourData = ((SelectionTourData) selection).getTourData();
            if (selectionTourData != null) {
                if (_tourData != null && _tourData.equals(selectionTourData)) {
                    return;
                }
                updateChart(selectionTourData);
            }
        } else if (selection instanceof SelectionTourIds) {
            final SelectionTourIds selectionTourId = (SelectionTourIds) selection;
            final ArrayList<Long> tourIds = selectionTourId.getTourIds();
            if (tourIds != null && tourIds.size() > 0) {
                updateChart(tourIds.get(0));
            }
        } else if (selection instanceof SelectionTourId) {
            final SelectionTourId selectionTourId = (SelectionTourId) selection;
            final Long tourId = selectionTourId.getTourId();
            updateChart(tourId);
        } else if (selection instanceof SelectionChartInfo) {
            final ChartDataModel chartDataModel = ((SelectionChartInfo) selection).chartDataModel;
            if (chartDataModel != null) {
                final Object tourId = chartDataModel.getCustomData(TourManager.CUSTOM_DATA_TOUR_ID);
                if (tourId instanceof Long) {
                    final TourData tourData = TourManager.getInstance().getTourData((Long) tourId);
                    if (tourData != null) {
                        if (_tourData == null || _tourData.equals(tourData) == false) {
                            updateChart(tourData);
                        }
                        final SelectionChartInfo chartInfo = (SelectionChartInfo) selection;
                        _tourChart.setXSliderPosition(new SelectionChartXSliderPosition(_tourChart, chartInfo.leftSliderValuesIndex, chartInfo.rightSliderValuesIndex));
                    }
                }
            }
        } else if (selection instanceof SelectionChartXSliderPosition) {
            final SelectionChartXSliderPosition xSliderPosition = (SelectionChartXSliderPosition) selection;
            final Chart chart = xSliderPosition.getChart();
            if (chart != null && chart != _tourChart) {
                final Object tourId = chart.getChartDataModel().getCustomData(TourManager.CUSTOM_DATA_TOUR_ID);
                if (tourId instanceof Long) {
                    final TourData tourData = TourManager.getInstance().getTourData((Long) tourId);
                    if (tourData != null) {
                        if (_tourData.equals(tourData)) {
                            xSliderPosition.setChart(_tourChart);
                        }
                    }
                }
            }
            _tourChart.setXSliderPosition(xSliderPosition);
        } else if (selection instanceof StructuredSelection) {
            final Object firstElement = ((StructuredSelection) selection).getFirstElement();
            if (firstElement instanceof TVICatalogComparedTour) {
                updateChart(((TVICatalogComparedTour) firstElement).getTourId());
            } else if (firstElement instanceof TVICompareResultComparedTour) {
                final TVICompareResultComparedTour compareResultItem = (TVICompareResultComparedTour) firstElement;
                final TourData tourData = TourManager.getInstance().getTourData(compareResultItem.getComparedTourData().getTourId());
                updateChart(tourData);
            }
        } else if (selection instanceof SelectionTourCatalogView) {
            final SelectionTourCatalogView tourCatalogSelection = (SelectionTourCatalogView) selection;
            final TVICatalogRefTourItem refItem = tourCatalogSelection.getRefItem();
            if (refItem != null) {
                updateChart(refItem.getTourId());
            }
        } else if (selection instanceof SelectionDeletedTours) {
            clearView();
        }
    }

    private void saveState() {
        _tourChart.saveState();
    }

    @Override
    public void setFocus() {
        _tourChart.setFocus();
        _postSelectionProvider.setSelection(new SelectionTourData(_tourChart, _tourData));
        fireSliderPosition();
    }

    private void showTour() {
        onSelectionChanged(getSite().getWorkbenchWindow().getSelectionService().getSelection());
        if (_tourData == null) {
            showTourFromTourProvider();
        }
    }

    private void showTourFromTourProvider() {
        _pageBook.showPage(_pageNoChart);
        Display.getCurrent().asyncExec(new Runnable() {

            public void run() {
                if (_pageBook.isDisposed()) {
                    return;
                }
                if (_tourData != null) {
                    return;
                }
                final ArrayList<TourData> selectedTours = TourManager.getSelectedTours();
                if (selectedTours != null && selectedTours.size() > 0) {
                    updateChart(selectedTours.get(0));
                }
            }
        });
    }

    private void updateChart() {
        if (_tourData == null) {
            return;
        }
        TourManager.getInstance().setActiveTourChart(_tourChart);
        _pageBook.showPage(_tourChart);
        _tourChart.updateTourChart(_tourData, _tourChartConfig, false);
        setTitleToolTip(TourManager.getTourDateShort(_tourData));
    }

    private void updateChart(final long tourId) {
        if (_tourData != null && _tourData.getTourId() == tourId) {
            return;
        }
        updateChart(TourManager.getInstance().getTourData(tourId));
        fireSliderPosition();
    }

    private void updateChart(final TourData tourData) {
        if (tourData == null) {
            return;
        }
        _tourData = tourData;
        updateChart();
    }
}
