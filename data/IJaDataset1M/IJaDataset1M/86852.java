package com.globalretailtech.pos.devices;

import javax.swing.*;
import com.globalretailtech.util.Application;
import com.globalretailtech.util.ShareProperties;
import com.globalretailtech.util.Format;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.events.*;
import com.globalretailtech.pos.ej.*;
import com.globalretailtech.pos.gui.*;
import com.globalretailtech.pos.hardware.*;
import org.apache.log4j.Logger;

/**
 * JFC operator prompt.
 *
 *
 * @author  Quentin Olson
 * @see PosUserDialog, PosGui
 */
public class OperPrompt extends JPanel implements PosGui {

    static Logger logger = Logger.getLogger(OperPrompt.class);

    private PosPrompt prompt;

    private PosPrompt custDisplay;

    private PosPrompt operDisplay;

    private String promptClass;

    public void setPrompt(PosPrompt value) {
        prompt = value;
    }

    public void setCustDisplay(PosPrompt value) {
        custDisplay = value;
    }

    public void setOperDisplay(PosPrompt value) {
        operDisplay = value;
    }

    public OperPrompt() {
        super();
        ShareProperties p = new ShareProperties(this.getClass().getName());
        if (p.Found()) {
            promptClass = p.getProperty("OperPromptClass", "com.globalretailtech.pos.gui.OneLine");
        } else {
            promptClass = "com.globalretailtech.pos.gui.OneLine";
        }
        try {
            prompt = (PosPrompt) Class.forName(promptClass).newInstance();
            custDisplay = new PosLineDisplay(new jpos.LineDisplay(), "CustDisplay");
            if (!custDisplay.isOpen()) {
                logger.warn("CustDisplay not opened");
                custDisplay = null;
            }
            operDisplay = new PosLineDisplay(new jpos.LineDisplay(), "OperDisplay");
            if (!operDisplay.isOpen()) {
                logger.warn("OperDisplay not opened");
                operDisplay = null;
            }
        } catch (Exception e) {
            logger.warn("Class not found [" + promptClass + "]", e);
        }
    }

    /** Return the graphical component */
    public JComponent getGui() {
        OneLine p = (OneLine) prompt;
        return p.getGui();
    }

    /** Initialize the displays */
    public void init(PosContext context) {
    }

    /** How to clear this thing */
    public void clear() {
        if (prompt != null) {
            prompt.setText("");
        }
        if (operDisplay != null) {
            operDisplay.setText("");
        }
    }

    public void home() {
    }

    /**
     *
     */
    public void update(PosEvent event) {
        if (event instanceof EnterPricePlu) update((EnterPricePlu) event);
    }

    /**
     *
     */
    public void update(TrainingMode event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(RegisterOpen event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(FirstItem event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(CloseCashDrawer event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(PopEmployee event) {
    }

    public void update(ClearKey event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(PosError event) {
        if (prompt != null) {
            prompt.setErrorText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setErrorText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setErrorText(event.promptText());
        }
    }

    public void update(EjBank event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(EnterKey event) {
    }

    public void update(Pause event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(Function function) {
        if (prompt != null) {
            prompt.setText(Format.print(function.promptText(), Format.toMoney(Double.toString(function.num()), Application.locale()), " ", prompt.getWidth()));
        }
        if (operDisplay != null) {
            operDisplay.setText(Format.print(function.promptText(), Format.toMoney(Double.toString(function.num()), Application.locale()), " ", operDisplay.getWidth()));
        }
        if (custDisplay != null) {
            custDisplay.setText(Format.print(function.promptText(), Format.toMoney(Double.toString(function.num()), Application.locale()), " ", custDisplay.getWidth()));
        }
    }

    public void update(LogOn event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(RecallEj event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(NumKey event) {
        if (prompt != null) {
            prompt.setText(Format.print(event.promptText(), event.numText(), " ", prompt.getWidth()));
        }
        if (operDisplay != null) {
            operDisplay.setText(Format.print(event.promptText(), event.numText(), " ", operDisplay.getWidth()));
        }
    }

    public void update(EnterPricePlu event) {
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
    }

    public void update(MultKey event) {
        if (prompt != null) {
            prompt.setText(Format.print(event.promptText(), event.numText(), " ", prompt.getWidth()));
        }
        if (operDisplay != null) {
            operDisplay.setText(Format.print(event.promptText(), event.numText(), " ", operDisplay.getWidth()));
        }
        if (custDisplay != null) {
            custDisplay.setText(Format.print(event.promptText(), event.numText(), " ", custDisplay.getWidth()));
        }
    }

    public void update(PosNumberDialog event) {
        clear();
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(Quantity event) {
        clear();
        if (prompt != null) {
            prompt.setText(event.promptText());
        }
        if (operDisplay != null) {
            operDisplay.setText(event.promptText());
        }
        if (custDisplay != null) {
            custDisplay.setText(event.promptText());
        }
    }

    public void update(EjCheckTender tender) {
        switch(tender.state()) {
            case EjCheckTender.ENTER_CASH_BACK_AMOUNT:
            case EjCheckTender.ENTER_CHECK_NO:
                prompt.setText(tender.promptText());
                break;
            case EjCheckTender.TENDER_FINAL:
                prompt.setText(Format.print(tender.desc(), Format.toMoney(Double.toString(tender.extAmount()), tender.context().locale()), " ", prompt.getWidth()));
                break;
            default:
        }
    }

    public void update(EjCCTender tender) {
        switch(tender.state()) {
            case EjCCTender.ENTER_CASH_BACK_AMOUNT:
            case EjCCTender.ENTER_CC_NO:
            case EjCCTender.ENTER_CC_EXPR:
                prompt.setText(tender.promptText());
                break;
            case EjCCTender.TENDER_FINAL:
                prompt.setText(Format.print(tender.desc(), Format.toMoney(Double.toString(tender.extAmount()), tender.context().locale()), " ", prompt.getWidth()));
                break;
            default:
        }
    }

    public void update(EjItem item) {
        if (prompt != null) {
            prompt.setText(Format.print(item.desc(), Format.toMoney(Double.toString(item.extAmount()), item.context().locale()), " ", prompt.getWidth()));
        }
        if (operDisplay != null) {
            operDisplay.setText(Format.print(item.desc(), Format.toMoney(Double.toString(item.extAmount()), item.context().locale()), " ", operDisplay.getWidth()));
        }
        if (custDisplay != null) {
            custDisplay.setText(Format.print(item.desc(), Format.toMoney(Double.toString(item.extAmount()), item.context().locale()), " ", custDisplay.getWidth()));
        }
    }

    public void update(EjAltCurrTender tender) {
        prompt.setText(tender.promptText());
    }

    public void update(EjTender total) {
        prompt.setText(Format.print(total.desc(), Format.toMoney(Double.toString(total.extAmount()), total.context().locale()), " ", prompt.getWidth()));
    }

    public void update(NewLogon event) {
    }

    /**
     * Update display for all ej lines in one method.
     * They are done like this for reprints.
     */
    public void update(EjLine line) {
        switch(line.lineType()) {
            case EjLine.TRANS_HEADER:
                break;
            case EjLine.ITEM:
                update((EjItem) line);
                break;
            case EjLine.PROMOTION:
                update((EjPromotion) line);
                break;
            case EjLine.TAX:
                update((EjTax) line);
                break;
            case EjLine.TENDER:
                update((EjTender) line);
                break;
            case EjLine.BANK:
                update((EjBank) line);
                break;
            case EjLine.ACCOUNT:
                break;
            case EjLine.ALT_CURRENCY_TENDER:
                update((EjAltCurrTender) line);
                break;
            case EjLine.CHECK_TENDER:
                update((EjCheckTender) line);
                break;
            case EjLine.CC_TENDER:
                update((EjCCTender) line);
                break;
            default:
                logger.warn("Unhandled ej type in OperPrompt " + line.toString());
                break;
        }
    }

    public void update(String message) {
        if (operDisplay != null) operDisplay.setText(message);
        if (prompt != null) prompt.setText(message);
    }

    public void open() {
    }

    public void close() {
        logger.info("Close OperPrompt");
        prompt = null;
        operDisplay = null;
        custDisplay = null;
    }
}
