package org.whatsitcalled.webflange.webapp;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import java.io.Serializable;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.RenderedDynamicImageResource;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.whatsitcalled.webflange.ResourceFactory;
import org.whatsitcalled.webflange.model.Chart;
import org.whatsitcalled.webflange.model.LoadTest;
import org.whatsitcalled.webflange.model.LoadTestRun;
import org.whatsitcalled.webflange.model.LoadTestSummary;
import org.whatsitcalled.webflange.service.LoadTestRunningException;
import org.whatsitcalled.webflange.webapp.model.LoadTestSummaryDataProvider;
import org.whatsitcalled.wicket.CustomPagingNavigator;

public class ReportsPanel extends Panel {

    private static final Logger LOGGER = Logger.getLogger(ReportsPanel.class);

    private LoadTestReportPanel loadTestReportPanel;

    private LoadTestRunReportPanel loadTestRunReportPanel;

    public ReportsPanel(String arg0, PageParameters parameters) {
        super(arg0);
        loadTestReportPanel = new LoadTestReportPanel("loadTestReportPanel");
        loadTestReportPanel.setVisible(false);
        loadTestRunReportPanel = new LoadTestRunReportPanel("loadTestRunReportPanel");
        loadTestRunReportPanel.setVisible(false);
        add(loadTestReportPanel);
        add(loadTestRunReportPanel);
        if (parameters.containsKey("testId")) {
            Long id = parameters.getLong("testId");
            LoadTest test = ResourceFactory.getLoadTestDAO().getLoadTest(id);
            loadTestReportPanel.setDefaultModel(new CompoundPropertyModel(test));
            loadTestReportPanel.setVisible(true);
            loadTestRunReportPanel.setVisible(false);
        } else if (parameters.containsKey("runId")) {
            Long id = parameters.getLong("runId");
            LoadTestRun run = ResourceFactory.getLoadTestRunDAO().getLoadTestRun(id);
            loadTestRunReportPanel.setDefaultModel(new CompoundPropertyModel(run));
            loadTestRunReportPanel.setVisible(true);
            loadTestReportPanel.setVisible(false);
        }
    }

    public class ChartNavPanel extends Panel {

        private LoadTest test;

        private LoadTestRun run;

        private List<Chart> charts;

        public ChartNavPanel(String id, LoadTest test) {
            super(id);
            this.test = test;
            this.run = null;
            if (test.getRuns().size() < 1) {
                this.charts = new ArrayList<Chart>();
                this.setVisible(false);
            } else {
                this.charts = test.getCharts();
                this.setVisible(true);
            }
            setDefaultModel(new CompoundPropertyModel(this.charts));
        }

        public ChartNavPanel(String id, LoadTestRun run) {
            super(id);
            this.test = null;
            this.run = run;
            this.charts = run.getCharts();
            setDefaultModel(new CompoundPropertyModel(this.charts));
        }

        @Override
        protected void onModelChanged() {
            super.onModelChanged();
            removeAll();
            addOrReplace(new ListView("chartList", this.charts) {

                public void populateItem(final ListItem item) {
                    final Chart chart = (Chart) item.getModelObject();
                    item.add(new Label("chartName", chart.getName() + " "));
                    Link chartLink = new Link("chartLink") {

                        public void onClick() {
                            Chart chart = (Chart) getParent().getDefaultModelObject();
                            if (chart.isVisible()) {
                                LOGGER.debug("Hiding chart: " + chart.getName());
                                chart.setVisible(false);
                            } else {
                                LOGGER.debug("Showing chart: " + chart.getName());
                                chart.setVisible(true);
                            }
                            ResourceFactory.getChartService().saveChart(chart);
                        }
                    };
                    chartLink.add(new Label("chartVisible", new Model() {

                        public Serializable getObject() {
                            return chart.isVisible() ? "hide" : "show";
                        }
                    }));
                    item.add(chartLink);
                    item.add(new Link("deleteChart") {

                        public void onClick() {
                            Chart chart = (Chart) getParent().getDefaultModelObject();
                            if (run == null) {
                                ResourceFactory.getChartService().removeChart(chart, test);
                                loadTestReportPanel.removeAll();
                                loadTestReportPanel.setDefaultModelObject(test);
                                loadTestReportPanel.modelChanged();
                            } else {
                                ResourceFactory.getChartService().removeChart(chart, run);
                                loadTestRunReportPanel.removeAll();
                                loadTestRunReportPanel.setDefaultModelObject(run);
                                loadTestRunReportPanel.modelChanged();
                            }
                        }
                    });
                }
            });
            addOrReplace(new ListView("charts", this.charts) {

                public void populateItem(final ListItem item) {
                    final Chart chart = (Chart) item.getModelObject();
                    if (run == null) {
                        ResourceFactory.getChartService().prepareChart(chart, test.getId());
                    } else ResourceFactory.getChartService().prepareChart(chart, run);
                    ChartDynamicImageResource resource = new ChartDynamicImageResource(chart);
                    Image chartImage = new Image("chartImage", resource);
                    item.add(chartImage);
                    item.setVisible(chart.isVisible());
                }
            });
        }
    }

    public class LoadTestReportPanel extends Panel {

        private String name = "";

        public LoadTestReportPanel(String arg0) {
            super(arg0);
        }

        @Override
        protected void onModelChanged() {
            super.onModelChanged();
            removeAll();
            final LoadTest test = (LoadTest) getDefaultModelObject();
            if (test.getEnabled()) {
                HeaderContributor contributor = new HeaderContributor(new IHeaderContributor() {

                    public void renderHead(IHeaderResponse response) {
                        response.renderString("<meta http-equiv=\"refresh\" content=\"60\" >\n");
                    }
                });
                add(contributor);
            }
            addOrReplace(new Label("testName", test.getName()));
            ChartNavPanel chartNavPanel = new ChartNavPanel("chartNav", test);
            chartNavPanel.setVisible(test.getCharts().size() > 0);
            addOrReplace(chartNavPanel);
            LoadTestChartForm chartForm = new LoadTestChartForm("chartForm", new CompoundPropertyModel(new Chart()), test);
            addOrReplace(chartForm);
        }

        public void setLoadTest(final Long id) {
            LoadTest newLoadTest = ResourceFactory.getLoadTestService().getLoadTest(id);
            ResourceFactory.getLoadTestService().saveLoadTest(newLoadTest);
            setDefaultModel(new CompoundPropertyModel(newLoadTest));
        }
    }

    public class LoadTestChartForm extends Form {

        private LoadTest loadTest;

        private LoadTestSummaryDataView view;

        private LoadTestRun reloadRun = new LoadTestRun();

        public LoadTestChartForm(String arg0, IModel arg1, LoadTest test) {
            super(arg0, arg1);
            setLoadTest(test);
            this.view = new LoadTestSummaryDataView("summaries", new LoadTestSummaryDataProvider(test), test);
            view.setItemsPerPage(10);
            addOrReplace(view);
            addOrReplace(new CustomPagingNavigator("navigator", view));
            final Button delete = new Button("deleteButton") {

                public void onSubmit() {
                    List<LoadTestRun> runs = view.getRuns();
                    try {
                        ResourceFactory.getLoadTestService().removeSelectedRuns(loadTest.getId(), runs);
                        loadTestReportPanel.setLoadTest(loadTest.getId());
                    } catch (LoadTestRunningException e) {
                        LOGGER.error("Unable to delete runs...");
                    }
                }
            };
            delete.add(new SimpleAttributeModifier("onclick", "return confirm('Delete selected runs?');"));
            delete.add(new TestNotEnabledValidator(test.getId(), test.getName() + " must not be enabled or running to delete run data!"));
            add(delete);
            final Button chart = new Button("chartButton") {

                public void onSubmit() {
                    Chart chart = getChart();
                    ResourceFactory.getChartService().generateDefaultChart(chart, loadTest.getId());
                    List<LoadTestRun> runs = view.getRuns();
                    for (LoadTestRun run : runs) {
                        if (run.getSelected()) {
                            LOGGER.debug("Run is selected: " + run.getTime());
                        }
                    }
                    loadTestReportPanel.setLoadTest(loadTest.getId());
                }
            };
            add(chart);
            TextField name = new TextField("name") {

                public boolean isRequired() {
                    Form form = (Form) findParent(Form.class);
                    return form.getRootForm().findSubmittingButton() == chart;
                }
            };
            add(name);
            final Button reload = new Button("reloadButton") {

                public void onSubmit() {
                    ResourceFactory.getGrinderTestRunner().loadStats(loadTest.getId(), reloadRun.getTime());
                }
            };
            add(reload);
            TextField date = new TextField("date", new PropertyModel(reloadRun, "time")) {

                public boolean isRequired() {
                    Form form = (Form) findParent(Form.class);
                    return form.getRootForm().findSubmittingButton() == reload;
                }
            };
            add(date);
            add(new CheckBox("showTests"));
            add(new CheckBox("showErrors"));
            add(new CheckBox("showMeanTestTime"));
            add(new CheckBox("showTestTimeStandardDeviation"));
            add(new CheckBox("showMeanResponseLength"));
            add(new CheckBox("showResponseBytesPerSecond"));
            add(new CheckBox("showResponseErrors"));
            add(new CheckBox("showMeanTimeToResolveHost"));
            add(new CheckBox("showMeanTimeToEstablishConnection"));
            add(new CheckBox("showMeanTimeToFirstByte"));
        }

        private Chart getChart() {
            return (Chart) this.getModelObject();
        }

        public LoadTest getLoadTest() {
            return loadTest;
        }

        public void setLoadTest(final LoadTest loadTest) {
            this.loadTest = loadTest;
        }
    }

    public class LoadTestRunReportPanel extends Panel {

        private String name = "";

        public LoadTestRunReportPanel(String arg0) {
            super(arg0);
        }

        @Override
        protected void onModelChanged() {
            super.onModelChanged();
            final LoadTestRun run = (LoadTestRun) getDefaultModelObject();
            LOGGER.debug(run.toString());
            File dataFile = ResourceFactory.getLoadTestService().getDataFile(run);
            DownloadLink dfLink = new DownloadLink("dataFileLink", dataFile);
            dfLink.setVisible(dataFile.exists());
            addOrReplace(dfLink);
            File summaryFile = ResourceFactory.getLoadTestService().getSummaryFile(run);
            DownloadLink sfLink = new DownloadLink("summaryFileLink", summaryFile);
            sfLink.setVisible(summaryFile.exists());
            addOrReplace(sfLink);
            File errorFile = ResourceFactory.getLoadTestService().getErrorFile(run);
            DownloadLink efLink = new DownloadLink("errorFileLink", errorFile);
            efLink.setVisible(errorFile.exists());
            addOrReplace(efLink);
            Label testNameLabel = new Label("testName", run.getLoadTest().getName());
            Link testLink = new BookmarkablePageLink("testLink", HomePage.class, new PageParameters("testId=" + run.getLoadTest().getId()));
            testLink.add(testNameLabel);
            addOrReplace(testLink);
            addOrReplace(new Label("runName", getFormattedDate(run.getTime())));
            ChartNavPanel chartNavPanel = new ChartNavPanel("chartNav", run);
            chartNavPanel.setVisible(run.getCharts().size() > 0);
            addOrReplace(chartNavPanel);
            LoadTestRunChartForm chartForm = new LoadTestRunChartForm("chartForm", new CompoundPropertyModel(new Chart()));
            addOrReplace(chartForm);
            chartForm.setRun(run);
        }

        public void setRun(final Long id) {
            LoadTestRun newLoadTestRun = ResourceFactory.getLoadTestRunDAO().getLoadTestRun(id);
            ResourceFactory.getLoadTestRunDAO().saveLoadTestRun(newLoadTestRun);
            setDefaultModel(new CompoundPropertyModel(newLoadTestRun));
        }
    }

    public class LoadTestRunChartForm extends Form {

        private LoadTestRun run;

        public LoadTestRunChartForm(String arg0, IModel arg1) {
            super(arg0, arg1);
            add(new RequiredTextField("name"));
            add(new CheckBox("showTests"));
            add(new CheckBox("showErrors"));
            add(new CheckBox("showMeanTestTime"));
            add(new CheckBox("showTestTimeStandardDeviation"));
            add(new CheckBox("showMeanResponseLength"));
            add(new CheckBox("showResponseBytesPerSecond"));
            add(new CheckBox("showResponseErrors"));
            add(new CheckBox("showMeanTimeToResolveHost"));
            add(new CheckBox("showMeanTimeToEstablishConnection"));
            add(new CheckBox("showMeanTimeToFirstByte"));
        }

        @Override
        protected void onSubmit() {
            Chart chart = getChart();
            ResourceFactory.getChartService().generateDefaultChart(chart, run);
            loadTestRunReportPanel.setRun(run.getId());
        }

        private Chart getChart() {
            return (Chart) this.getModelObject();
        }

        public LoadTestRun getRun() {
            return run;
        }

        public void setRun(final LoadTestRun run) {
            this.run = run;
            add(new LoadTestRunSummaryDataView("summaries", new LoadTestSummaryDataProvider(run), run));
        }
    }

    public String getFormattedDate(long ldate) {
        Format formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        Date date = new Date(ldate);
        return formatter.format(date);
    }

    protected final class LoadTestSummaryDataView extends DataView {

        private LoadTest test = null;

        private List<LoadTestRun> runs = new ArrayList<LoadTestRun>();

        protected LoadTestSummaryDataView(String arg0, IDataProvider arg1, LoadTest test) {
            super(arg0, arg1);
            this.test = test;
        }

        protected void populateItem(final Item item) {
            final LoadTestSummary summary = (LoadTestSummary) item.getModelObject();
            LOGGER.debug("populating item:" + summary.toString());
            LoadTestRun curRun = summary.getRun();
            runs.add(curRun);
            CheckBox runCheckBox = new CheckBox("selected", new PropertyModel(curRun, "selected"));
            item.add(runCheckBox);
            String name = null;
            BookmarkablePageLink link = null;
            String dateString = getFormattedDate(summary.getRun().getTime());
            name = dateString;
            link = new BookmarkablePageLink("link", HomePage.class, new PageParameters("runId=" + summary.getRun().getId()));
            link.add(new Label("test", name));
            item.add(link);
            item.add(new Label("errors", Long.toString(summary.getErrors())));
            item.add(new Label("meanResponseLength", Long.toString(summary.getMeanResponseLength())));
            item.add(new Label("meanTestTime", Long.toString(summary.getMeanTestTime())));
            item.add(new Label("meanTimeToEstablishConnection", Long.toString(summary.getMeanTimeToEstablishConnection())));
            item.add(new Label("meanTimeToFirstByte", Long.toString(summary.getMeanTimeToFirstByte())));
            item.add(new Label("meanTimeToResolveHost", Long.toString(summary.getMeanTimeToResolveHost())));
            item.add(new Label("responseBytesPerSecond", Long.toString(summary.getResponseBytesPerSecond())));
            item.add(new Label("responseErrors", Long.toString(summary.getResponseErrors())));
            item.add(new Label("tests", Long.toString(summary.getTests())));
            item.add(new Label("testTimeStandardDeviation", Float.toString(summary.getTestTimeStandardDeviation())));
            item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {

                public Object getObject() {
                    return (item.getIndex() % 2 == 1) ? "even" : "odd";
                }
            }));
        }

        public List<LoadTestRun> getRuns() {
            return runs;
        }

        public void setRuns(List<LoadTestRun> runs) {
            this.runs = runs;
        }
    }

    public class ChartDynamicImageResource extends RenderedDynamicImageResource {

        private Chart chart;

        public ChartDynamicImageResource(Chart c) {
            super(c.getChartImage().getWidth(), c.getChartImage().getHeight());
            this.chart = c;
        }

        @Override
        protected boolean render(Graphics2D g) {
            g.drawImage(chart.getChartImage(), 0, 0, Color.WHITE, null);
            return true;
        }
    }

    protected final class LoadTestRunSummaryDataView extends DataView {

        private LoadTestRun run = null;

        private List<LoadTestRun> runs = new ArrayList<LoadTestRun>();

        protected LoadTestRunSummaryDataView(String arg0, IDataProvider arg1, LoadTestRun run) {
            super(arg0, arg1);
            this.run = run;
        }

        protected void populateItem(final Item item) {
            final LoadTestSummary summary = (LoadTestSummary) item.getModelObject();
            String name = null;
            BookmarkablePageLink link = null;
            name = summary.getTestName();
            link = new BookmarkablePageLink("link", HomePage.class, new PageParameters("testId=" + run.getLoadTest().getId()));
            link.add(new Label("test", name));
            item.add(link);
            item.add(new Label("errors", Long.toString(summary.getErrors())));
            item.add(new Label("meanResponseLength", Long.toString(summary.getMeanResponseLength())));
            item.add(new Label("meanTestTime", Long.toString(summary.getMeanTestTime())));
            item.add(new Label("meanTimeToEstablishConnection", Long.toString(summary.getMeanTimeToEstablishConnection())));
            item.add(new Label("meanTimeToFirstByte", Long.toString(summary.getMeanTimeToFirstByte())));
            item.add(new Label("meanTimeToResolveHost", Long.toString(summary.getMeanTimeToResolveHost())));
            item.add(new Label("responseBytesPerSecond", Long.toString(summary.getResponseBytesPerSecond())));
            item.add(new Label("responseErrors", Long.toString(summary.getResponseErrors())));
            item.add(new Label("tests", Long.toString(summary.getTests())));
            item.add(new Label("testTimeStandardDeviation", Float.toString(summary.getTestTimeStandardDeviation())));
            item.add(new Label("uri", summary.getTestUri()));
            item.add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {

                public Object getObject() {
                    return (item.getIndex() % 2 == 1) ? "even" : "odd";
                }
            }));
        }
    }
}
