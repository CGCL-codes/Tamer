package org.inigma.waragent.iniglets;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.inigma.utopia.calculator.MilitaryCalculator;
import org.inigma.utopia.utils.I18NUtil;
import org.inigma.utopia.utils.NumberUtil;

public class MilitaryCalculatorIniglet extends AbstractUtopiaAgentIniglet implements Listener {

    private static final int PRECISION = 2;

    private static final NumberFormat decimalFormat = I18NUtil.getPercentageFormat();

    private Text[] inputTexts;

    private Text rangeText;

    private Table resultsTable;

    private float range;

    public MilitaryCalculatorIniglet() {
        this.range = config.getFloat("range", 0.4f);
        this.inputTexts = new Text[4];
        GridLayout layout = new GridLayout();
        layout.makeColumnsEqualWidth = false;
        layout.numColumns = 4;
        panel.setLayout(layout);
        Button clearResults = new Button(panel, SWT.PUSH);
        clearResults.setText("Clear");
        clearResults.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 2, 1));
        clearResults.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                for (Text input : inputTexts) {
                    input.setText("");
                }
                handleEvent(null);
            }
        });
        Label label;
        label = new Label(panel, SWT.RIGHT | SWT.SHADOW_ETCHED_IN);
        label.setText("Range");
        label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        this.rangeText = new Text(panel, SWT.SINGLE | SWT.BORDER);
        this.rangeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
        this.rangeText.addListener(SWT.DefaultSelection, this);
        for (int i = 0; i < this.inputTexts.length; i++) {
            label = new Label(panel, SWT.RIGHT | SWT.SHADOW_ETCHED_IN);
            label.setText("SoM Def");
            label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
            this.inputTexts[i] = new Text(panel, SWT.SINGLE | SWT.BORDER);
            this.inputTexts[i].setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
            this.inputTexts[i].addListener(SWT.DefaultSelection, this);
        }
        resultsTable = new Table(panel, SWT.BORDER | SWT.HIDE_SELECTION);
        resultsTable.setHeaderVisible(true);
        resultsTable.setLinesVisible(true);
        TableColumn column = new TableColumn(resultsTable, SWT.NONE);
        column.setText("#");
        column.setWidth(50);
        column = new TableColumn(resultsTable, SWT.NONE);
        column.setText("Defense");
        column.setWidth(200);
        resultsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 6, 6));
        shell.setSize(400, 300);
    }

    @Override
    public void display() {
        this.rangeText.setText(decimalFormat.format(range));
        shell.setVisible(true);
    }

    @Override
    public Image getDisplayIcon() {
        return new Image(shell.getDisplay(), getClass().getResourceAsStream("/org/inigma/waragent/attack.png"));
    }

    @Override
    public String getDisplayName() {
        return "Attack Calculator";
    }

    public void handleEvent(Event event) {
        range = NumberUtil.getNumber(rangeText.getText()).floatValue();
        Collection<Integer> values = new ArrayList<Integer>();
        for (Text input : inputTexts) {
            String text = input.getText();
            Number number = NumberUtil.getNumber(text);
            if (number != null && number.intValue() > 0) {
                values.add(number.intValue());
            }
        }
        MilitaryCalculator calculator = new MilitaryCalculator();
        calculator.setPrecision(PRECISION);
        calculator.setMinRange(1.0f - range);
        calculator.setMaxRange(1.0f + range);
        Collection<Integer> matches = calculator.getMatches(values);
        int i = 0;
        resultsTable.removeAll();
        for (Integer match : matches) {
            TableItem item = new TableItem(resultsTable, SWT.NONE);
            item.setText(0, "" + ++i);
            item.setText(1, "" + match);
        }
        config.put("range", this.range);
    }
}
