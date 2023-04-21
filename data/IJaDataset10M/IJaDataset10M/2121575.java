package edu.gsbme.geometrykernel.algorithm;

import edu.gsbme.geometrykernel.kobject;
import edu.gsbme.geometrykernel.data.Idata;
import edu.gsbme.geometrykernel.exception.WrongParameterUsed;
import edu.gsbme.geometrykernel.information.Parameter;

/**
 * Abstract algorithm class
 * @author David
 *
 */
public abstract class IAlgorithm extends kobject {

    Idata[] data;

    public IAlgorithm(String id) {
        super(id);
    }

    public void setData(Idata[] data) {
        this.data = data;
    }

    public Object run(Parameter parameter, Idata[] data) throws WrongParameterUsed {
        setData(data);
        return run(parameter);
    }

    public abstract Object run(Parameter parameter) throws WrongParameterUsed;
}
