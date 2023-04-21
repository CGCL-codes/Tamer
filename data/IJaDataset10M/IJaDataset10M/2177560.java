package com.mia.sct.instruments;

import static com.mia.sct.util.MIAVariables.STRING_TYPE;
import com.mia.sct.data.model.InstrumentModel;
import com.mia.sct.util.ResourceBundleKeys;
import com.mia.sct.view.ViewManager;

/**
 * StringInstrument.java
 * 
 * Class represents a stringed instrument definition
 * 
 * @author Devon Bryant
 * @since Aug 23, 2007
 */
public class StringInstrument extends AbstractInstrument {

    /**
	 * Constructor
	 * 
	 * @param inModel Instrument Model
	 */
    public StringInstrument(InstrumentModel inModel) {
        super(inModel);
        setInstrumentType(STRING_TYPE);
    }

    public StringInstrument() {
        super(new InstrumentModel());
        setName(ViewManager.getViewString(ResourceBundleKeys.NEW_STRING_INST_TITLE, "*New String Instrument"));
    }

    public String getInstrumentType() {
        return STRING_TYPE;
    }

    public Object clone() {
        StringInstrument result = null;
        result = new StringInstrument((InstrumentModel) instrumentModel.clone());
        return result;
    }
}
