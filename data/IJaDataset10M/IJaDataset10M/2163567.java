package record.web.ui.input.common;

import record.web.config.Config;
import record.web.ui.input.InputWindow;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.api.Intbox;

public abstract class IntegerInputWindow extends InputWindow<Integer> {

    /**
	 * Compiler generated serial ID.
	 */
    private static final long serialVersionUID = -6203709346417252653L;

    private final int defaultInt;

    private Intbox intboxInteger;

    public IntegerInputWindow(int defaultInt) {
        super();
        this.defaultInt = defaultInt;
    }

    public IntegerInputWindow(String title, String border, boolean closable, int defaultInt) {
        super(title, border, closable);
        this.defaultInt = defaultInt;
    }

    @Override
    protected void cancelInput() {
    }

    @Override
    protected Integer confirmInput() throws WrongValueException {
        return this.intboxInteger.getValue();
    }

    @Override
    protected String getZul() {
        return Config.WebContent.Private.Common.Input.PAGE_INTEGER;
    }

    @Override
    protected void doBeforeModal(Component customRoot) {
        super.doBeforeModal(customRoot);
        this.intboxInteger = (Intbox) customRoot.getFellow("intboxInteger");
        this.intboxInteger.setValue(this.defaultInt);
        this.intboxInteger.select();
    }
}
