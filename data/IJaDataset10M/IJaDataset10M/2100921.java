package com.yougo.mp3tool.multipleItemSetter;

import java.awt.Frame;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.yougo.mp3tool.core.Mp3Item;
import com.yougo.mp3tool.core.extension.function.Configuration;
import com.yougo.mp3tool.core.extension.function.Function;
import com.yougo.mp3tool.core.extension.function.Function.ConfigurationType;
import com.yougo.mp3tool.core.gui.dialog.JAbstractDialogWrapper.DialogExitStatus;
import com.yougo.mp3tool.multipleItemSetter.gui.JInputValue;

/**
 * @author Ugo Diana
 */
public class MultipleItemSetterFunction implements Function {

    private static final Log log = LogFactory.getLog(MultipleItemSetterFunction.class);

    private MultipleItemSetterConfiguration multipleItemSetterConfiguration;

    /**
     * @see com.yougo.mp3tool.core.extension.function.Function#applyFunction(com.yougo.mp3tool.core.Mp3Item)
     */
    public Mp3Item applyFunction(Mp3Item item) throws Exception {
        item.getFieldById(multipleItemSetterConfiguration.getFieldId()).setValue(multipleItemSetterConfiguration.getValue());
        return item;
    }

    public boolean afterFunctionApplication() {
        return true;
    }

    public boolean beforeFunctionApplication(Frame owner, Configuration configuration) {
        MultipleItemSetterConfiguration multipleItemSetterConfiguration = (MultipleItemSetterConfiguration) configuration;
        if (multipleItemSetterConfiguration == null) {
            return false;
        } else {
            this.multipleItemSetterConfiguration = multipleItemSetterConfiguration;
            return true;
        }
    }

    /**
     * @see com.yougo.mp3tool.core.extension.function.Function#getConfigurationType()
     */
    public int getConfigurationType() {
        return ConfigurationType.EACH_RUN;
    }
}
