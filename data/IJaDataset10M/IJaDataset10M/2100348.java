package net.sf.jmoney.stocks.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.sf.jmoney.model2.Currency;
import net.sf.jmoney.stocks.model.RatesTable.Band;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The implementation for the composite control that contains
 * the account property controls.
 */
class RatesEditorControl extends Composite {

    private class RowControls {

        public RowControls(Composite parent) {
            label1 = new Label(parent, SWT.NONE);
            percentageText = new Text(parent, SWT.BORDER);
            label2 = new Label(parent, SWT.NONE);
            upperBoundText = new Text(parent, SWT.BORDER);
            label1.setText("plus");
            percentageText.setLayoutData(new GridData(30, SWT.DEFAULT));
            upperBoundText.setLayoutData(new GridData(30, SWT.DEFAULT));
            FocusListener listener = new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    lastFocusRow = RowControls.this;
                }
            };
            percentageText.addFocusListener(listener);
            upperBoundText.addFocusListener(listener);
        }

        Label label1;

        Text percentageText;

        Label label2;

        Text upperBoundText;

        public void configureAsMiddleRow() {
            label2.setText("percent of the amount up to");
            upperBoundText.setVisible(true);
        }

        public void configureAsLastRow() {
            label2.setText("percent of the rest");
            upperBoundText.setVisible(false);
        }

        public void configureAsOnlyRow() {
            label2.setText("percent of the amount");
            upperBoundText.setVisible(false);
        }
    }

    private Text fixedAmountControl;

    private ArrayList<RowControls> rows = new ArrayList<RowControls>();

    private Composite middleRow;

    private RowControls lastFocusRow;

    private Currency currencyForFormatting;

    private Button addRowButton;

    private Button removeRowButton;

    /**
	 * @param parent
	 */
    public RatesEditorControl(Composite parent) {
        super(parent, SWT.BORDER);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        setLayout(layout);
        Composite topRow = createFixedAmountRow();
        GridData topRowData = new GridData();
        topRowData.verticalAlignment = GridData.FILL;
        topRowData.horizontalAlignment = GridData.FILL;
        topRow.setLayoutData(topRowData);
        createBandRows();
        middleRow.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, false));
        createButtonsArea();
    }

    private Composite createButtonsArea() {
        Composite buttonRow = new Composite(this, SWT.NONE);
        FillLayout buttonLayout = new FillLayout();
        buttonLayout.marginHeight = 0;
        buttonLayout.marginWidth = 0;
        buttonRow.setLayout(buttonLayout);
        addRowButton = new Button(buttonRow, SWT.PUSH);
        addRowButton.setText("Add Band");
        addRowButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                RowControls newRow = new RowControls(middleRow);
                int lastFocusRowIndex = rows.indexOf(lastFocusRow);
                rows.add(lastFocusRowIndex + 1, newRow);
                if (lastFocusRowIndex != rows.size() - 2) {
                    Control followingControl = rows.get(lastFocusRowIndex + 2).label1;
                    newRow.label1.moveAbove(followingControl);
                    newRow.percentageText.moveAbove(followingControl);
                    newRow.label2.moveAbove(followingControl);
                    newRow.upperBoundText.moveAbove(followingControl);
                    newRow.configureAsMiddleRow();
                    if (rows.size() == 2) {
                        RowControls lastRow = rows.get(1);
                        lastRow.configureAsLastRow();
                    }
                } else {
                    RowControls previousRow = rows.get(lastFocusRowIndex);
                    previousRow.configureAsMiddleRow();
                    newRow.configureAsLastRow();
                }
                removeRowButton.setEnabled(true);
                middleRow.pack(true);
                pack(true);
                getParent().pack(true);
            }
        });
        removeRowButton = new Button(buttonRow, SWT.PUSH);
        removeRowButton.setText("Remove Band");
        removeRowButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                int lastFocusRowIndex = rows.indexOf(lastFocusRow);
                RowControls row = rows.remove(lastFocusRowIndex);
                row.label1.dispose();
                row.percentageText.dispose();
                row.label2.dispose();
                row.upperBoundText.dispose();
                if (lastFocusRowIndex == rows.size()) {
                    if (rows.size() == 1) {
                        rows.get(0).configureAsOnlyRow();
                        removeRowButton.setEnabled(false);
                    } else {
                        rows.get(lastFocusRowIndex - 1).configureAsLastRow();
                    }
                }
                middleRow.pack(true);
                pack(true);
                getParent().pack(true);
            }
        });
        return buttonRow;
    }

    private Composite createFixedAmountRow() {
        Composite topRow = new Composite(this, SWT.NONE);
        GridLayout topRowLayout = new GridLayout();
        topRowLayout.numColumns = 2;
        topRowLayout.marginHeight = 0;
        topRowLayout.marginWidth = 0;
        topRow.setLayout(topRowLayout);
        new Label(topRow, SWT.NONE).setText("Fixed amount");
        fixedAmountControl = new Text(topRow, SWT.BORDER);
        return topRow;
    }

    private void createBandRows() {
        middleRow = new Composite(this, SWT.NONE);
        GridLayout middleRowLayout = new GridLayout();
        middleRowLayout.numColumns = 4;
        middleRowLayout.marginHeight = 0;
        middleRowLayout.marginWidth = 0;
        middleRow.setLayout(middleRowLayout);
    }

    /**
	 * @param account
	 */
    public void setRatesTable(RatesTable ratesTable, final Currency currencyForFormatting) {
        this.currencyForFormatting = currencyForFormatting;
        for (RowControls row : rows) {
            row.label1.dispose();
            row.percentageText.dispose();
            row.label2.dispose();
            row.upperBoundText.dispose();
        }
        if (ratesTable != null) {
            fixedAmountControl.setText(currencyForFormatting.format(ratesTable.getFixedAmount()));
            List<Band> bands = ratesTable.getBands();
            if (bands.size() == 0) {
                RowControls row = new RowControls(middleRow);
                rows.add(row);
                row.configureAsOnlyRow();
            } else if (bands.size() == 1) {
                RowControls row = new RowControls(middleRow);
                rows.add(row);
                BigDecimal percentage = bands.get(0).getProportion();
                row.percentageText.setText(percentage.movePointRight(2).toString());
                row.configureAsOnlyRow();
            } else {
                for (int rowIndex = 0; rowIndex < bands.size(); rowIndex++) {
                    Band band = bands.get(rowIndex);
                    final RowControls row = new RowControls(middleRow);
                    rows.add(row);
                    BigDecimal percentage = band.getProportion();
                    row.percentageText.setText(percentage.movePointRight(2).toString());
                    if (rowIndex != bands.size() - 1) {
                        Band nextBand = bands.get(rowIndex + 1);
                        row.configureAsMiddleRow();
                        row.upperBoundText.setText(currencyForFormatting.format(nextBand.getBandStart()));
                    } else {
                        row.configureAsLastRow();
                    }
                }
            }
        } else {
            RowControls row = new RowControls(middleRow);
            rows.add(row);
            row.configureAsOnlyRow();
        }
        removeRowButton.setEnabled(rows.size() > 1);
        middleRow.pack();
        pack();
        getParent().pack(true);
    }

    public RatesTable getRatesTable() {
        long fixedAmount = currencyForFormatting.parse(fixedAmountControl.getText());
        long bandStart = 0;
        ArrayList<Band> bands = new ArrayList<Band>();
        for (RowControls row : rows) {
            BigDecimal proportion;
            try {
                proportion = new BigDecimal(row.percentageText.getText()).movePointLeft(2);
            } catch (NumberFormatException e) {
                proportion = BigDecimal.ZERO;
            }
            bands.add(new Band(bandStart, proportion));
            try {
                bandStart = currencyForFormatting.parse(row.upperBoundText.getText());
            } catch (NumberFormatException e) {
                bandStart = 0;
            }
        }
        return new RatesTable(fixedAmount, bands);
    }
}
