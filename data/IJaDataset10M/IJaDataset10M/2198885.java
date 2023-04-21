package edu.gsbme.geometrykernel.algorithm.Transformation;

import edu.gsbme.geometrykernel.algorithm.IAlgorithm;
import edu.gsbme.geometrykernel.algorithm.Topology.addBrepModels;
import edu.gsbme.geometrykernel.data.BaseModel.brepModel;
import edu.gsbme.geometrykernel.exception.WrongParameterUsed;
import edu.gsbme.geometrykernel.information.Parameter;
import edu.gsbme.geometrykernel.information.parameter.EmbedParameter;

public class embedAlgorithm extends IAlgorithm {

    public embedAlgorithm(String id) {
        super(id);
    }

    public static brepModel embed(brepModel model1, brepModel model2) {
        brepModel result = addBrepModels.add(model1, model2);
        return result;
    }

    @Override
    public Object run(Parameter parameter) throws WrongParameterUsed {
        if (parameter instanceof EmbedParameter) {
            EmbedParameter para = ((EmbedParameter) parameter);
            return embed(para.model1, para.model2);
        } else throw (new WrongParameterUsed());
    }
}
