package pl.xperios.rdk.client.commons;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.BoxComponent;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.HashMap;
import pl.xperios.rdk.shared.Bean;
import pl.xperios.rdk.shared.XLog;

/**
 *
 * @author Praca
 */
public abstract class AbstractDictionary<D extends Bean<Long>> implements Dictionaryable<D> {

    private GenericRpcServiceAsync<D> service;

    private DictionaryPanel dictionaryPanel;

    Button newButton;

    Button editButton;

    Button delButton;

    Button printButton;

    D selectedBean;

    ArrayList<D> selectedBeans;

    private static PrintConfig printConfig = null;

    private static HashMap<String, Bean> beans = new HashMap<String, Bean>();

    private boolean initialized = false;

    public AbstractDictionary() {
        this(false);
        if (printConfig == null) {
            printConfig = initPrintConfig();
        }
    }

    public AbstractDictionary(Boolean init) {
        if (init) {
            init();
        }
    }

    public abstract GenericRpcServiceAsync<D> initAsyncService();

    public GenericRpcServiceAsync<D> getService() {
        if (service == null) {
            service = initAsyncService();
        }
        return service;
    }

    @Override
    public DictionarySearchPanel getSearchPanel() {
        return new DictionarySearchPanelDefault();
    }

    public void bindDictionaryPanel(DictionaryPanel dictionaryPanel) {
        this.dictionaryPanel = dictionaryPanel;
    }

    public String getShortName() {
        return getFullName();
    }

    public abstract String getFullName();

    public DictionaryEditPanel<D> getEditPanel() {
        return null;
    }

    public ArrayList<Button> getButtonsForGrid() {
        ArrayList<Button> out = new ArrayList<Button>();
        out.add(newButton);
        out.add(editButton);
        out.add(delButton);
        out.add(printButton);
        return out;
    }

    public ArrayList<ColumnConfig> getGridColumns() {
        return new ArrayList<ColumnConfig>();
    }

    public GridFilters getGridFilters() {
        return new GridFilters();
    }

    public ArrayList<String> getExtraColumnsToDbIncluded() {
        return null;
    }

    public String getAutoExpandColumn() {
        return null;
    }

    public boolean isLoadingOnStart() {
        return true;
    }

    public boolean isNumbererAvaible() {
        return true;
    }

    public boolean isCheckerAvaible() {
        return true;
    }

    public String getTemplate() {
        return null;
    }

    public abstract D getNewBean();

    public void onSelectedOneRecord(D bean) {
        XLog.debug("Selected one record: " + bean);
        editButton.enable();
        delButton.enable();
        selectedBean = bean;
        selectedBeans = new ArrayList<D>();
        selectedBeans.add(bean);
    }

    public void onSelectedManyRecords(ArrayList<D> beans) {
        XLog.debug("Selected many records: " + beans);
        editButton.disable();
        delButton.enable();
        selectedBean = null;
        selectedBeans = beans;
    }

    public void onDeselected() {
        XLog.debug("Event: onDeselected");
        editButton.disable();
        delButton.disable();
        selectedBean = null;
        selectedBeans = null;
    }

    public void onDoubleGridClick(D bean) {
        XLog.debug("Event: onDoubleGridClick");
        selectedBean = bean;
    }

    public void beforeSaveUpdate(D bean) {
        XLog.debug("Event: beforeSaveUpdate");
    }

    public void beforeSaveNew(D bean) {
        XLog.debug("Event: beforeSaveNew");
    }

    private void initButtons() {
        newButton = new Button("Nowy", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                newRecord();
            }
        });
        editButton = new Button("Edycja", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                editRecord();
            }
        });
        delButton = new Button("Usuń", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                delRecords();
            }
        });
        final String className = getClass().getName();
        printButton = new Button("Drukuj", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                activatePrintingWindow(className);
            }
        });
    }

    private void newRecord() {
        final DictionaryEditPanel<D> editPanel = getEditPanel();
        if (editPanel == null) {
            final MessageBox box = MessageBox.alert("Błąd", "Brak uprawnień, bądź funkcja niezaimplementowana", new SelectionListener<MessageBoxEvent>() {

                @Override
                public void componentSelected(MessageBoxEvent ce) {
                }
            });
            box.show();
            return;
        }
        final Window out = new Window();
        out.setHeading("Nowy rekord");
        out.setModal(true);
        out.setButtonAlign(HorizontalAlignment.CENTER);
        out.addButton(new Button("Zapisz", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                XLog.debug("Zapisz");
                ArrayList<String> validation = editPanel.validateForm();
                if (validation != null && !validation.isEmpty()) {
                    UITools.showValideAlert(validation);
                    return;
                }
                D beanToSave = getNewBean();
                editPanel.fillBeanFromForm(beanToSave);
                beforeSaveNew(beanToSave);
                getService().save(beanToSave, new XAsync<Long>() {

                    @Override
                    public void succeed(Long result) {
                        out.hide();
                        dictionaryPanel.reloadSearching();
                    }

                    @Override
                    public void failed(Throwable caught) {
                        super.failed(caught);
                        Info.display("Failed", "Failed saving");
                    }
                });
            }
        }));
        out.addButton(new Button("Wyczyść", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                editPanel.clear();
            }
        }));
        out.addButton(new Button("Sprawdź", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                UITools.showValideAlert(editPanel.validateForm());
            }
        }));
        out.addButton(new Button("Anuluj", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                out.hide();
            }
        }));
        BoxComponent widget = (BoxComponent) editPanel.getWidget();
        if (widget.getWidth() < 400) {
            out.setWidth(900);
        }
        out.add(widget, new RowData(-1, -1, new Margins(5)));
        out.show();
    }

    private void editRecord() {
        final DictionaryEditPanel<D> editPanel = getEditPanel();
        if (editPanel == null) {
            final MessageBox box = MessageBox.alert("Błąd", "Brak uprawnień, bądź funkcja niezaimplementowana", new SelectionListener<MessageBoxEvent>() {

                @Override
                public void componentSelected(MessageBoxEvent ce) {
                }
            });
            box.show();
            return;
        }
        getService().get(new XParameters(selectedBean.getAllPropertiesNames(), selectedBean.getPropertyId(), selectedBean.getId(), false), new AsyncCallback<Result<D>>() {

            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void onSuccess(Result<D> result) {
                editPanel.clear();
                final D editingBean = result.getFirst();
                XLog.debug("Bean editing: " + editingBean);
                editPanel.fillFormFromBean(editingBean);
                final Window out = new Window();
                out.setHeading("Nowy rekord");
                out.setModal(true);
                out.setButtonAlign(HorizontalAlignment.CENTER);
                out.addButton(new Button("Zapisz", new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        XLog.debug("Zapisz");
                        ArrayList<String> validation = editPanel.validateForm();
                        if (validation != null && !validation.isEmpty()) {
                            UITools.showValideAlert(validation);
                            return;
                        }
                        editPanel.fillBeanFromForm(editingBean);
                        beforeSaveUpdate(editingBean);
                        getService().save(editingBean, new XAsync<Long>() {

                            @Override
                            public void succeed(Long result) {
                                out.hide();
                                dictionaryPanel.reloadSearching();
                            }

                            @Override
                            public void failed(Throwable caught) {
                                super.failed(caught);
                                Info.display("Failed", "Failed saving");
                            }
                        });
                    }
                }));
                out.addButton(new Button("Wyczyść", new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        editPanel.clear();
                    }
                }));
                out.addButton(new Button("Sprawdź", new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        UITools.showValideAlert(editPanel.validateForm());
                    }
                }));
                out.addButton(new Button("Anuluj", new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        out.hide();
                    }
                }));
                BoxComponent widget = (BoxComponent) editPanel.getWidget();
                if (widget.getWidth() < 400) {
                    out.setWidth(900);
                }
                out.add(widget, new RowData(-1, -1, new Margins(5)));
                out.show();
            }
        });
    }

    private void delRecords() {
        if (selectedBeans == null || selectedBeans.isEmpty()) {
            final MessageBox box = MessageBox.alert("Usuwanie", "Nie zaznaczono rekordów do usunięcia", new SelectionListener<MessageBoxEvent>() {

                @Override
                public void componentSelected(MessageBoxEvent ce) {
                }
            });
            box.show();
            return;
        }
        getService().del(selectedBean, new AsyncCallback<Void>() {

            public void onFailure(Throwable caught) {
                final MessageBox box = MessageBox.alert("Usuwanie", "Nie udało się skasować rekordu: " + caught.getMessage() + ".", new SelectionListener<MessageBoxEvent>() {

                    @Override
                    public void componentSelected(MessageBoxEvent ce) {
                    }
                });
                box.show();
            }

            public void onSuccess(Void result) {
                dictionaryPanel.reloadSearching();
            }
        });
    }

    public void init() {
        initialized = true;
        initButtons();
    }

    public boolean isInitialized() {
        return initialized;
    }

    public PrintConfig initPrintConfig() {
        PrintConfig out = new PrintConfig();
        for (ColumnConfig columnConfig : getGridColumns()) {
            if (columnConfig.isHidden()) {
                continue;
            }
            out.setColumn(columnConfig.getId(), columnConfig.getHeader(), columnConfig.getWidth());
        }
        return out;
    }

    public PrintConfig getPrintConfig() {
        return printConfig;
    }

    public void activatePrintingWindow(final String className) {
        Window window = new Window();
        window.setAutoWidth(true);
        window.setModal(true);
        window.setHeading("Drukowanie");
        FormPanel panel = new FormPanel();
        panel.setHeaderVisible(false);
        panel.setBodyBorder(false);
        final RadioGroup columnType = new RadioGroup("columnType");
        columnType.setFieldLabel("Szerokości kolumn");
        Radio defaultMode = new Radio();
        defaultMode.setId("default");
        defaultMode.setBoxLabel("Domyślny");
        defaultMode.setValue(true);
        columnType.add(defaultMode);
        Radio ownMode = new Radio();
        ownMode.setId("custom");
        ownMode.setBoxLabel("Własny");
        columnType.add(ownMode);
        panel.add(columnType);
        final RadioGroup orientation = new RadioGroup("orientation");
        orientation.setFieldLabel("Orientacja strony");
        Radio portraitOrientation = new Radio();
        portraitOrientation.setId("portrait");
        portraitOrientation.setBoxLabel("Pionowo");
        portraitOrientation.setValue(true);
        orientation.add(portraitOrientation);
        Radio landscapeOrientation = new Radio();
        landscapeOrientation.setId("landscape");
        landscapeOrientation.setBoxLabel("Poziomo");
        orientation.add(landscapeOrientation);
        panel.add(orientation);
        LayoutContainer typePanel1 = new LayoutContainer(new ColumnLayout());
        LayoutContainer typePanel2 = new LayoutContainer(new ColumnLayout());
        LayoutContainer typePanel3 = new LayoutContainer(new ColumnLayout());
        LayoutContainer typePanel4 = new LayoutContainer(new ColumnLayout());
        panel.add(new Label("Format:"));
        final RadioGroup outputType = new RadioGroup("outputType");
        outputType.setFieldLabel("Wynik");
        outputType.setResizeFields(true);
        Radio pdf = new Radio();
        Radio docx = new Radio();
        Radio rtf = new Radio();
        Radio ods = new Radio();
        Radio odt = new Radio();
        Radio html = new Radio();
        Radio xhtml = new Radio();
        Radio xml = new Radio();
        Radio text = new Radio();
        Radio jpg = new Radio();
        Radio gif = new Radio();
        Radio png = new Radio();
        Radio xls = new Radio();
        Radio xlsx = new Radio();
        Radio csv = new Radio();
        outputType.add(pdf);
        outputType.add(docx);
        outputType.add(rtf);
        outputType.add(ods);
        outputType.add(odt);
        outputType.add(html);
        outputType.add(xhtml);
        outputType.add(xml);
        outputType.add(text);
        outputType.add(text);
        outputType.add(jpg);
        outputType.add(gif);
        outputType.add(png);
        outputType.add(xls);
        outputType.add(xlsx);
        outputType.add(csv);
        typePanel1.add(pdf);
        typePanel1.add(docx);
        typePanel1.add(rtf);
        typePanel1.add(ods);
        typePanel1.add(odt);
        typePanel2.add(html);
        typePanel2.add(xhtml);
        typePanel2.add(xml);
        typePanel2.add(text);
        typePanel2.add(text);
        typePanel3.add(jpg);
        typePanel3.add(gif);
        typePanel3.add(png);
        typePanel3.add(xls);
        typePanel3.add(xlsx);
        typePanel4.add(csv);
        pdf.setId("pdf");
        pdf.setBoxLabel("PDF");
        pdf.setValue(true);
        docx.setId("docx");
        docx.setBoxLabel("DOCX");
        rtf.setId("rtf");
        rtf.setBoxLabel("RTF");
        ods.setId("ods");
        ods.setBoxLabel("ODS");
        odt.setId("odt");
        odt.setBoxLabel("ODT");
        html.setId("html");
        html.setBoxLabel("HTML");
        xhtml.setId("xhtml");
        xhtml.setBoxLabel("XHTML");
        xml.setId("xml");
        xml.setBoxLabel("XML");
        text.setId("text");
        text.setBoxLabel("TXT");
        jpg.setId("jpg");
        jpg.setBoxLabel("JPG");
        gif.setId("gif");
        gif.setBoxLabel("GIF");
        png.setId("png");
        png.setBoxLabel("PNG");
        xls.setId("xls");
        xls.setBoxLabel("XLS");
        xlsx.setId("xlsx");
        xlsx.setBoxLabel("XLSX");
        csv.setId("csv");
        csv.setBoxLabel("CSV");
        panel.add(typePanel1);
        panel.add(typePanel2);
        panel.add(typePanel3);
        panel.add(typePanel4);
        window.add(panel);
        window.addButton(new Button("Drukuj / Eksportuj", new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                String groupState = null;
                if (dictionaryPanel.getGrid().getStore() instanceof GroupingStore) {
                    groupState = ((GroupingStore<D>) dictionaryPanel.getGrid().getStore()).getGroupState();
                }
                Printing.print(service.getClass().getName(), className, dictionaryPanel.getLastConfig(), dictionaryPanel.getGrid().getColumnModel().getColumns(), groupState, dictionaryPanel.getSearchingParameters(), getExtraColumnsToDbIncluded(), dictionaryPanel.getGrid().getAutoExpandColumn(), columnType.getValue().getId(), orientation.getValue().getId(), outputType.getValue().getId());
            }
        }));
        window.show();
    }

    public DictionarySearchPanel getChooserSearchPanel() {
        return getSearchPanel();
    }

    public D getSelectedBean() {
        return selectedBean;
    }

    public ArrayList<D> getSelectedBeans() {
        return selectedBeans;
    }

    public String getDomainIdName() {
        return initDomain().getPropertyId();
    }

    public HashMap<String, String> getPropertiesClass() {
        return initDomain().getPropertiesClass();
    }

    private Bean initDomain() {
        String thisName = this.getClass().getName();
        if (!beans.containsKey(thisName)) {
            beans.put(thisName, getNewBean());
        }
        return beans.get(thisName);
    }

    public String getTreeColumnRender() {
        return null;
    }
}
