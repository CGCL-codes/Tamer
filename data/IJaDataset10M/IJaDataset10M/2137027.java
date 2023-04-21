package net.sf.jmoney.entrytable;

import net.sf.jmoney.model2.Commodity;
import net.sf.jmoney.model2.Entry;
import net.sf.jmoney.model2.EntryInfo;
import net.sf.jmoney.model2.ExtendableObject;
import net.sf.jmoney.model2.ScalarPropertyAccessor;
import net.sf.jmoney.model2.SessionChangeAdapter;
import net.sf.jmoney.model2.SessionChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Represents a table column that is either the debit or the credit column.
 * Use two instances of this class instead of a single instance of the
 * above <code>EntriesSectionProperty</code> class if you want the amount to be
 * displayed in separate debit and credit columns.
 */
public class DebitAndCreditColumns extends IndividualBlock<EntryData, BaseEntryRowControl> {

    private class DebitAndCreditCellControl implements ICellControl<EntryData> {

        private Text textControl;

        private Entry entry = null;

        private SessionChangeListener amountChangeListener = new SessionChangeAdapter() {

            @Override
            public void objectChanged(ExtendableObject changedObject, ScalarPropertyAccessor changedProperty, Object oldValue, Object newValue) {
                if (changedObject.equals(entry) && changedProperty == EntryInfo.getAmountAccessor()) {
                    setControlContent();
                }
            }
        };

        public DebitAndCreditCellControl(Text textControl) {
            this.textControl = textControl;
            textControl.addDisposeListener(new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (entry != null) {
                        entry.getDataManager().removeChangeListener(amountChangeListener);
                    }
                }
            });
        }

        public Control getControl() {
            return textControl;
        }

        public void load(EntryData data) {
            if (entry != null) {
                entry.getDataManager().removeChangeListener(amountChangeListener);
            }
            entry = data.getEntry();
            entry.getDataManager().addChangeListener(amountChangeListener);
            setControlContent();
        }

        private void setControlContent() {
            long amount = entry.getAmount();
            Commodity commodityForFormatting = entry.getCommodity();
            if (commodityForFormatting == null) {
                commodityForFormatting = commodity;
            }
            if (isDebit) {
                textControl.setText(amount < 0 ? commodityForFormatting.format(-amount) : "");
            } else {
                textControl.setText(amount > 0 ? commodityForFormatting.format(amount) : "");
            }
        }

        public void save() {
            Commodity commodityForFormatting = entry.getCommodity();
            if (commodityForFormatting == null) {
                commodityForFormatting = commodity;
            }
            String amountString = textControl.getText();
            long amount = commodityForFormatting.parse(amountString);
            long previousEntryAmount = entry.getAmount();
            long newEntryAmount;
            if (isDebit) {
                if (amount != 0) {
                    newEntryAmount = -amount;
                } else {
                    if (previousEntryAmount < 0) {
                        newEntryAmount = 0;
                    } else {
                        newEntryAmount = previousEntryAmount;
                    }
                }
            } else {
                if (amount != 0) {
                    newEntryAmount = amount;
                } else {
                    if (previousEntryAmount > 0) {
                        newEntryAmount = 0;
                    } else {
                        newEntryAmount = previousEntryAmount;
                    }
                }
            }
            entry.setAmount(newEntryAmount);
            if (entry.getTransaction().hasTwoEntries()) {
                Entry otherEntry = entry.getTransaction().getOther(entry);
                Commodity commodity1 = entry.getCommodity();
                Commodity commodity2 = otherEntry.getCommodity();
                if (commodity1 == null || commodity2 == null || commodity1.equals(commodity2)) {
                    otherEntry.setAmount(-newEntryAmount);
                }
            }
        }

        public void setFocusListener(FocusListener controlFocusListener) {
        }
    }

    private String id;

    private Commodity commodity;

    private boolean isDebit;

    public static DebitAndCreditColumns createCreditColumn(Commodity commodityForFormatting) {
        return new DebitAndCreditColumns("credit", "Credit", commodityForFormatting, false);
    }

    public static DebitAndCreditColumns createDebitColumn(Commodity commodityForFormatting) {
        return new DebitAndCreditColumns("debit", "Debit", commodityForFormatting, true);
    }

    private DebitAndCreditColumns(String id, String name, Commodity commodity, boolean isDebit) {
        super(name, 70, 2);
        this.id = id;
        this.commodity = commodity;
        this.isDebit = isDebit;
    }

    public String getId() {
        return id;
    }

    @Override
    public ICellControl<EntryData> createCellControl(BaseEntryRowControl parent) {
        final Text textControl = new Text(parent, SWT.TRAIL);
        textControl.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                switch(e.detail) {
                    case SWT.TRAVERSE_ARROW_PREVIOUS:
                        if (e.keyCode == SWT.ARROW_UP) {
                            e.doit = false;
                            e.detail = SWT.TRAVERSE_NONE;
                        }
                        break;
                    case SWT.TRAVERSE_ARROW_NEXT:
                        if (e.keyCode == SWT.ARROW_DOWN) {
                            e.doit = true;
                        }
                        break;
                }
            }
        });
        return new DebitAndCreditCellControl(textControl);
    }

    public int compare(EntryData entryData1, EntryData entryData2) {
        long amount1 = entryData1.getEntry().getAmount();
        long amount2 = entryData2.getEntry().getAmount();
        int result;
        if (amount1 < amount2) {
            result = -1;
        } else if (amount1 > amount2) {
            result = 1;
        } else {
            result = 0;
        }
        if (isDebit) {
            result = -result;
        }
        return result;
    }
}
