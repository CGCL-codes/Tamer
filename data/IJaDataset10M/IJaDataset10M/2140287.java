package com.simpledata.bc.uicomponents.filler;

import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.simpledata.bc.components.bcoption.OptionManager;
import com.simpledata.bc.components.bcoption.OptionTransaction;
import com.simpledata.bc.components.worksheet.dispatcher.TransactionsRoot0;
import com.simpledata.bc.datamodel.Dispatcher;
import com.simpledata.bc.datamodel.money.Money;
import com.simpledata.bc.datamodel.money.TransactionValue;
import com.simpledata.bc.tools.Lang;
import com.simpledata.bc.uicomponents.tools.MoneyEditor;
import com.simpledata.bc.uicomponents.tools.MoneyValueInputSlave;
import org.apache.log4j.Logger;

/**
 * A distribution method for Transactions based on the Average size of line
 */
public class DistribTrSizeOfLines implements DistributionMethod {

    private static final Logger m_log = Logger.getLogger(DistribTrSizeOfLines.class);

    Money sizeOfLine;

    FillerNode owner;

    /** this is the default line size if it cannot be determined **/
    public static final double DEFAULT_LINE_VALUE = 1000;

    /** this is the minimum line size if calculated value is low **/
    public static final double MIN_CALCULATED_LINE_VALUE = 1000;

    /** this is the minimum acceptable line size **/
    public static final double MIN_LINE_VALUE = 1;

    /**
	 * @param fn is the FillerNode that own this distribute Action.
	 * It is used by some Distribution method to find parent's Distributions
	 */
    public DistribTrSizeOfLines(FillerNode fn) {
        assert fn != null : "Cannot work on a null FillerNode";
        owner = fn;
        sizeOfLine = new Money(MIN_CALCULATED_LINE_VALUE);
        CalculationResult counter = calculateSizeOfLineFromData(owner);
        double averageSize = DEFAULT_LINE_VALUE;
        if (counter.getNumberOfLines() > 0) {
            averageSize = counter.getSumOfAmounts().getValueDefCurDouble() / counter.getNumberOfLines();
        }
        if (averageSize < MIN_CALCULATED_LINE_VALUE) {
            averageSize = MIN_CALCULATED_LINE_VALUE;
        }
        setLineSize(averageSize);
    }

    /** calculate the average size of a line from data contained here **/
    protected static CalculationResult calculateSizeOfLineFromData(FillerNode fn) {
        class Temp implements CalculationResult {

            int numberOfLines = 0;

            double sumOfAmounts = 0;

            public int getNumberOfLines() {
                return numberOfLines;
            }

            public Money getSumOfAmounts() {
                return new Money(sumOfAmounts);
            }
        }
        final Temp counter = new Temp();
        FillerVisitor fv = new TransactionValueVisitor() {

            void gotLine(NodeInfo ni, TransactionValue tv) {
                double d = tv.getMoneyValue().getValueDefCurDouble() * tv.getAverageNumber();
                counter.numberOfLines++;
                counter.sumOfAmounts += d;
            }
        };
        if (fn instanceof NodeInfo) {
            fv.run((NodeInfo) fn);
        }
        return counter;
    }

    public interface CalculationResult {

        public int getNumberOfLines();

        public Money getSumOfAmounts();
    }

    /**
	 * set the line size in default Currency value
	 * @param d must be sup or equals to MIN_LINE_VALUE
	 */
    public void setLineSize(double d) {
        if (d < MIN_LINE_VALUE) d = MIN_LINE_VALUE;
        sizeOfLine.setValue(new Money(d));
    }

    public boolean methodForward() {
        return false;
    }

    /** 
	 * return the FillerNode that owns this distribute action<BR>
	 * This is used by DistributMethod that need some informations
	 * about the parents
	 **/
    public FillerNode getOwner() {
        return owner;
    }

    /** 
	 * distribute this amount of money on this WorkSheet<BR>
	 **/
    public void distribute(Money m, Dispatcher tws, DistributionMonitor dm) {
        distributeLineOfSize(m, tws, dm, sizeOfLine);
    }

    protected static void distributeLineOfSize(Money m, Dispatcher tws, DistributionMonitor dm, Money ssizeOfLine) {
        if (dm != null) dm.distributionMonitorStep();
        if (!(tws instanceof TransactionsRoot0)) {
            m_log.error("Got a Worplace:" + tws);
            return;
        }
        TransactionsRoot0 ws = (TransactionsRoot0) tws;
        int numOfLine = DistribSizeOfLines.getNumOfLineToDistribute(m, ssizeOfLine);
        m.operationFactor(1d / numOfLine);
        if (m.getValueDouble() == 0) return;
        addTransactionTo(ws, m, numOfLine);
    }

    /** tool to add in/out transaction to a given dispatcher **/
    protected static void addTransactionTo(TransactionsRoot0 ws, Money m, int numOfLine) {
        OptionTransaction oti;
        oti = (OptionTransaction) OptionManager.createOption(ws, OptionTransaction.class);
        oti.setTransactionValue(new TransactionValue(m, numOfLine, true));
        OptionTransaction oto;
        oto = (OptionTransaction) OptionManager.createOption(ws, OptionTransaction.class);
        oto.setTransactionValue(new TransactionValue(m, numOfLine, false));
    }

    /**
	 * get the number of options the will be created from this node
	 */
    public int getCost(NodeInfo start) {
        return getCost(start, owner);
    }

    /** static cost calculator may be used by other Distribution Methods**/
    protected static int getCost(NodeInfo start, final FillerNode ownerNode) {
        class Cost {

            int value = 0;
        }
        final Cost cost = new Cost();
        FillerVisitor fv = new FillerVisitor() {

            public void run(NodeInfo ni) {
                if (ownerNode != ni && !ni.getDistributionMethod(TransactionsRoot0.class).methodForward()) return;
                if (ni.getChildren().length == 0) {
                    cost.value += ni.getRootWorkSheetPaired().size();
                } else {
                    ni.runOnChildren(this);
                }
            }
        };
        fv.run(start);
        return cost.value;
    }

    /** the text that will be shown on the UI.. will be translated **/
    private static final String TOPIC_STR = "Preffered size of a line:";

    JPanel ui;

    /**
	 * @see com.simpledata.bc.uicomponents.filler.DistributionMethod#getUI()
	 */
    public JPanel getUI() {
        if (ui != null) return ui;
        ui = new JPanel(new FlowLayout(FlowLayout.LEADING));
        MoneyValueInputSlave me = new MoneyValueInputSlave(sizeOfLine) {

            Money previousValue;

            public void stopEdit() {
                if (sizeOfLine.getValueDouble() < MIN_LINE_VALUE) {
                    sizeOfLine.setValue(MIN_LINE_VALUE);
                    refresh();
                }
                if (!previousValue.xequals(sizeOfLine)) {
                    owner.redistributeRepartition(TransactionsRoot0.class);
                }
            }

            public void startEdit() {
                previousValue = (Money) sizeOfLine.copy();
            }
        };
        ui.add(new JLabel(Lang.translate(TOPIC_STR)));
        ui.add(me);
        return ui;
    }

    /**
	 * Refresh the UI
	 */
    public void refreshUI() {
    }

    ;

    /**
	 * @see DistributionMethod#getSummary()
	 */
    public String getSummary() {
        return Lang.translate(TOPIC_STR) + MoneyEditor.MoneyToSTring(sizeOfLine);
    }
}
