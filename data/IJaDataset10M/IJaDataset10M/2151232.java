package com.pcmsolutions.device.EMU.E4.parameter;

import com.pcmsolutions.device.EMU.E4.zcommands.E4EditableParameterModelZCommandMarker;
import com.pcmsolutions.device.EMU.DeviceException;
import com.pcmsolutions.system.ZCommandProvider;
import com.pcmsolutions.system.ZCommandProviderHelper;

/**
 * Created by IntelliJ IDEA.
 * User: pmeehan
 * Date: 10-Mar-2003
 * Time: 17:07:04
 * To change this template use Options | File Templates.
 */
public interface EditableParameterModel extends ReadableParameterModel, ZCommandProvider {

    public static final ZCommandProviderHelper cmdProviderHelper = new ZCommandProviderHelper(E4EditableParameterModelZCommandMarker.class, ReadableParameterModel.cmdProviderHelper);

    public void setValue(Integer value) throws ParameterException;

    public void offsetValue(Integer offset) throws ParameterException;

    public void offsetValue(Double offsetAsFOR) throws ParameterException;

    public void setValueString(String value) throws ParameterException;

    public void setValueUnitlessString(String value) throws ParameterException;

    public void defaultValue() throws ParameterException;
}
