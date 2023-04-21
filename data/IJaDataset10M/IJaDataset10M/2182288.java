package edu.uta.futureye.lib.element;

import edu.uta.futureye.core.DOF;
import edu.uta.futureye.core.Element;
import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.lib.shapefun.SFQuadraticLocal2DFast;

public class FEQuadraticTriangle implements FiniteElementType {

    protected SFQuadraticLocal2DFast[] shapeFun = new SFQuadraticLocal2DFast[6];

    public FEQuadraticTriangle() {
        for (int i = 1; i <= 6; i++) shapeFun[i - 1] = new SFQuadraticLocal2DFast(i);
    }

    @Override
    public void assignTo(Element e) {
        for (int j = 1; j <= e.nodes.size(); j++) {
            DOF dof = new DOF(j, e.nodes.at(j).globalIndex, shapeFun[j - 1]);
            e.addNodeDOF(j, dof);
        }
    }

    @Override
    public int getDOFNumOnElement(int vsfDim) {
        return 6;
    }

    @Override
    public int getVectorShapeFunctionDim() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDOFNumOnMesh(Mesh mesh, int vsfDim) {
        return mesh.getNodeList().size();
    }

    @Override
    public void initDOFIndexGenerator(Mesh mesh) {
    }
}
