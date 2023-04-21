package br.com.visualmidia.ui.controlcenter.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import br.com.visualmidia.business.Expenditure;
import br.com.visualmidia.business.GDDate;
import br.com.visualmidia.business.Incoming;
import br.com.visualmidia.persistence.GetExpenditures;
import br.com.visualmidia.persistence.GetIncoming;
import br.com.visualmidia.persistence.GetIncomingId;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.tools.SortComparator;
import br.com.visualmidia.ui.widgets.TableOrder;

/**
 * @author  Lucas
 */
public class IncomingTable extends Composite {

    private GDSystem system;

    private Composite title;

    private Composite components;

    private Composite footer;

    private Table incomingTable;

    private br.com.visualmidia.ui.controlcenter.ExpendituresAndImcomingControlCenter accountsToIncomingControlCenter;

    private GDDate fromDate;

    private GDDate toDate;

    public static final Logger logger = Logger.getLogger(IncomingTable.class);

    public IncomingTable(Composite parent, int style, br.com.visualmidia.ui.controlcenter.ExpendituresAndImcomingControlCenter accountsToIncomingControlCenter, GDDate fromDate, GDDate toDate) {
        super(parent, style);
        this.accountsToIncomingControlCenter = accountsToIncomingControlCenter;
        this.system = GDSystem.getInstance();
        this.fromDate = fromDate;
        this.toDate = toDate;
        configure();
    }

    private void configure() {
        setLayout(new FormLayout());
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
        addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
                gc.drawLine(0, 0, 0, getClientArea().height);
                gc.drawLine(0, 0, getClientArea().width, 0);
                gc.drawLine(0, getClientArea().height - 1, getClientArea().width, getClientArea().height - 1);
                gc.drawLine(getClientArea().width - 1, 0, getClientArea().width - 1, getClientArea().height);
                gc.dispose();
            }
        });
        createTitle();
        createFooter();
        createComponentsArea();
    }

    private void createFooter() {
        footer = new Composite(this, SWT.NONE);
        footer.addListener(SWT.Paint, new Listener() {

            public void handleEvent(Event arg0) {
                GC gc = new GC(footer);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
                gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                gc.fillGradientRectangle(0, 0, footer.getClientArea().width, footer.getClientArea().height, true);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
                gc.setLineWidth(1);
                gc.drawLine(0, 1, footer.getClientArea().width, 1);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
                gc.drawLine(0, 2, footer.getClientArea().width, 2);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                gc.dispose();
            }
        });
        FormData data = new FormData();
        data.left = new FormAttachment(0, 1);
        data.right = new FormAttachment(100, -1);
        data.bottom = new FormAttachment(100, -1);
        data.height = 30;
        footer.setLayoutData(data);
    }

    private void createTitle() {
        title = new Composite(this, SWT.NONE);
        title.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
                gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                gc.fillGradientRectangle(0, 0, title.getClientArea().width, title.getClientArea().height - 2, true);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
                gc.setLineWidth(1);
                gc.drawLine(0, title.getClientArea().height - 2, title.getClientArea().width, title.getClientArea().height - 2);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
                gc.drawLine(0, title.getClientArea().height - 1, title.getClientArea().width, title.getClientArea().height - 1);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                gc.dispose();
            }
        });
        FormData data = new FormData();
        data.left = new FormAttachment(0, 1);
        data.top = new FormAttachment(0, 1);
        data.right = new FormAttachment(100, -1);
        data.height = 30;
        title.setLayoutData(data);
    }

    private void createComponentsArea() {
        components = new Composite(this, SWT.NONE);
        components.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
        GridLayout layout = new GridLayout(1, true);
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        components.setLayout(layout);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 1);
        data.top = new FormAttachment(title, 1);
        data.right = new FormAttachment(100, -1);
        data.bottom = new FormAttachment(footer, -1);
        components.setLayoutData(data);
        createTable();
    }

    private void createTable() {
        incomingTable = new Table(components, SWT.NONE | SWT.FULL_SELECTION | SWT.VERTICAL);
        incomingTable.setLinesVisible(true);
        incomingTable.setHeaderVisible(true);
        incomingTable.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event arg0) {
                TableLayout layout = new TableLayout();
                layout.addColumnData(new ColumnWeightData(5, 50, true));
                layout.addColumnData(new ColumnWeightData(5, 120, true));
                layout.addColumnData(new ColumnWeightData(20, 100, true));
                layout.addColumnData(new ColumnWeightData(10, 75, true));
                layout.addColumnData(new ColumnWeightData(10, 100, true));
                layout.addColumnData(new ColumnWeightData(5, 75, true));
                layout.addColumnData(new ColumnWeightData(5, 75, true));
                layout.addColumnData(new ColumnWeightData(5, 100, true));
                layout.addColumnData(new ColumnWeightData(20, 100, true));
                incomingTable.setLayout(layout);
            }
        });
        TableColumn transactionNumber = new TableColumn(incomingTable, SWT.CENTER);
        transactionNumber.setResizable(true);
        transactionNumber.setText("Num");
        TableColumn first = new TableColumn(incomingTable, SWT.LEFT);
        first.setResizable(true);
        first.setText("Pr�ximo Pagamento");
        TableColumn description = new TableColumn(incomingTable, SWT.LEFT);
        description.setResizable(true);
        description.setText("Descri��o");
        TableColumn operation = new TableColumn(incomingTable, SWT.LEFT);
        operation.setResizable(true);
        operation.setText("Opera��o");
        TableColumn value = new TableColumn(incomingTable, SWT.RIGHT);
        value.setResizable(true);
        value.setText("Valor");
        TableColumn next = new TableColumn(incomingTable, SWT.LEFT);
        next.setResizable(true);
        next.setText("In�cio");
        TableColumn last = new TableColumn(incomingTable, SWT.LEFT);
        last.setResizable(true);
        last.setText("Termino");
        TableColumn frequency = new TableColumn(incomingTable, SWT.LEFT);
        frequency.setResizable(true);
        frequency.setText("Freq��ncia");
        TableColumn observation = new TableColumn(incomingTable, SWT.LEFT);
        observation.setResizable(true);
        observation.setText("Observa��o");
        GridData data = new GridData(GridData.FILL_BOTH);
        incomingTable.setLayoutData(data);
        new TableOrder(incomingTable);
        updateTable();
    }

    public Table getAccountTableTree() {
        return incomingTable;
    }

    @SuppressWarnings(value = { "unchecked" })
    public void updateTable() {
        incomingTable.removeAll();
        try {
            Map<String, Incoming> map = (Map<String, Incoming>) system.query(new GetIncoming());
            List<Incoming> list = new ArrayList<Incoming>();
            for (Incoming incoming : map.values()) {
                if (incoming.isActive()) {
                    GDDate date = new GDDate(incoming.getNextPaymentDate());
                    int frequency = incoming.getFrequency();
                    if (incoming.getNumberOfOcurrencies() > 0) {
                        int i = 1;
                        while (i <= incoming.getNumberOfOcurrencies()) {
                            if (date.afterOrEqualsDay(fromDate) && date.beforeOrEqualsDay(toDate)) {
                                Incoming myBill = new Incoming(incoming.getId(), incoming.getDescription(), new GDDate(date), incoming.getFrequency(), incoming.getNumberOfOcurrencies(), incoming.getValue(), incoming.isFixedValue(), incoming.getCategoryId(), incoming.getObservation(), incoming.getFirstPaymentDate(), incoming.getLastPaymentDate());
                                list.add(myBill);
                            }
                            date = skip(date, frequency);
                            if (date == null) {
                                break;
                            }
                            i++;
                        }
                    } else {
                        while (date.beforeOrEqualsDay(toDate)) {
                            if (date.afterOrEqualsDay(fromDate) && date.beforeOrEqualsDay(toDate)) {
                                Incoming myBill = new Incoming(incoming.getId(), incoming.getDescription(), new GDDate(date), incoming.getFrequency(), incoming.getNumberOfOcurrencies(), incoming.getValue(), incoming.isFixedValue(), incoming.getCategoryId(), incoming.getObservation(), incoming.getFirstPaymentDate(), incoming.getLastPaymentDate());
                                list.add(myBill);
                            }
                            date = skip(date, frequency);
                            if (date == null) {
                                break;
                            }
                        }
                    }
                }
            }
            SortComparator comparator = new SortComparator();
            Collections.sort(list, comparator);
            int bgColorControl = 0;
            for (Incoming incoming : list) {
                if (incoming.isActive()) {
                    TableItem item = new TableItem(incomingTable, SWT.NONE | SWT.MULTI | SWT.WRAP);
                    if (bgColorControl == 1) {
                        item.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
                        bgColorControl = 0;
                    } else {
                        bgColorControl = 1;
                    }
                    GDDate todayPlusOneWeek = new GDDate();
                    todayPlusOneWeek.addDays(7);
                    GDDate nextIncomingDate = incoming.getNextPaymentDate();
                    GDDate today = new GDDate();
                    Incoming incomingToCompare = (Incoming) system.query(new GetIncoming(incoming.getId()));
                    GDDate paymentDayBill = incomingToCompare.getNextPaymentDate();
                    if (paymentDayBill.beforeDay(nextIncomingDate) && paymentDayBill.beforeDay(today)) {
                        item.setImage(new Image(null, "img/icoExpiradNotPayed.png"));
                    } else if (nextIncomingDate.beforeOrEqualsDay(todayPlusOneWeek) && nextIncomingDate.afterOrEqualsDay(today)) {
                        item.setImage(new Image(null, "img/icoWarningExpiration.png"));
                    } else if (nextIncomingDate.beforeDay(today)) {
                        item.setImage(new Image(null, "img/icoExpirad.png"));
                    } else {
                        item.setImage(new Image(null, "img/icoNotPay.png"));
                    }
                    String firstIncomingDate = (incoming.getFirstPaymentDate() == null) ? "" : new GDDate(incoming.getFirstPaymentDate()).getFormatedDate();
                    String lastIncomingDate = (incoming.getLastPaymentDate() == null) ? "" : new GDDate(incoming.getLastPaymentDate()).getFormatedDate();
                    item.setText(0, incoming.getId());
                    item.setText(1, nextIncomingDate.getFormatedDate());
                    item.setText(2, incoming.getDescription());
                    item.setText(3, system.getBillPlan().get(incoming.getCategoryId()).getDescription());
                    item.setText(4, incoming.getValue().getFormatedValue() + ((incoming.isFixedValue()) ? "" : " ~"));
                    item.setText(5, firstIncomingDate);
                    item.setText(6, lastIncomingDate);
                    item.setText(7, accountsToIncomingControlCenter.frequency[incoming.getFrequency()]);
                    item.setText(8, incoming.getObservation());
                }
            }
        } catch (Exception e) {
            logger.error("GetIncoming Exception: ", e);
        }
    }

    public void setFromDate(GDDate date) {
        this.fromDate = date;
    }

    public void setToDate(GDDate date) {
        this.toDate = date;
    }

    private GDDate skip(GDDate nextPaymentDate, int frequency) {
        if (frequency == 1) nextPaymentDate.addDays(1); else if (frequency == 2) nextPaymentDate.addDays(7); else if (frequency == 3) nextPaymentDate.addDays(14); else if (frequency == 4) nextPaymentDate.addMonth(1); else if (frequency == 5) nextPaymentDate.addMonth(2); else if (frequency == 6) nextPaymentDate.addMonth(3); else if (frequency == 7) nextPaymentDate.addMonth(4); else if (frequency == 8) nextPaymentDate.addMonth(6); else if (frequency == 9) nextPaymentDate.addYear(1); else {
            return null;
        }
        return nextPaymentDate;
    }
}
