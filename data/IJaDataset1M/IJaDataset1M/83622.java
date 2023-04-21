package org.freeworld.jmultiplug.binding.base;

import org.freeworld.jmultiplug.binding.base.context.BindingContext;
import org.freeworld.jmultiplug.binding.base.convert.Conversion;
import org.freeworld.jmultiplug.binding.base.settergetter.GetterBinding;
import org.freeworld.jmultiplug.binding.base.settergetter.SetterBinding;

public interface BindingLink {

    public void setGetter(GetterBinding getter);

    public GetterBinding getGetter();

    public void setSetter(SetterBinding setter);

    public SetterBinding getSetter();

    public void setGetToPutConverter(Conversion fetchToPutConverter);

    public Conversion getGetToPutConverter();

    public void setPutToGetConverter(Conversion revertConverter);

    public Conversion getPutToGetConverter();

    public void setBindingContext(BindingContext bindingContext);

    public BindingContext getBindingContext();

    public void activateLink();

    public void deactivateLink();

    public BindingStatus getStatus();

    public boolean isRevertSupported();

    public boolean isContextSupported();
}
