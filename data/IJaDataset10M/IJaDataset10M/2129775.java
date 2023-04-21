package org.ietr.preesm.codegen.model.factories;

import net.sf.dftools.algorithm.model.parameters.InvalidExpressionException;
import net.sf.dftools.algorithm.model.sdf.SDFAbstractVertex;
import net.sf.dftools.algorithm.model.sdf.SDFEdge;
import net.sf.dftools.algorithm.model.sdf.SDFGraph;
import org.ietr.preesm.codegen.model.CodeGenSDFBroadcastVertex;
import org.ietr.preesm.codegen.model.CodeGenSDFForkVertex;
import org.ietr.preesm.codegen.model.CodeGenSDFJoinVertex;
import org.ietr.preesm.codegen.model.CodeGenSDFRoundBufferVertex;
import org.ietr.preesm.codegen.model.ICodeGenSDFVertex;
import org.ietr.preesm.codegen.model.buffer.AbstractBufferContainer;
import org.ietr.preesm.codegen.model.buffer.Buffer;
import org.ietr.preesm.codegen.model.buffer.SubBuffer;
import org.ietr.preesm.codegen.model.buffer.SubBufferAllocation;
import org.ietr.preesm.codegen.model.containers.AbstractCodeContainer;
import org.ietr.preesm.codegen.model.expression.BinaryExpression;
import org.ietr.preesm.codegen.model.expression.ConstantExpression;
import org.ietr.preesm.codegen.model.expression.IExpression;
import org.ietr.preesm.codegen.model.main.ICodeElement;
import org.ietr.preesm.core.types.DataType;

/**
 * Creating code elements from a vertex
 * 
 * @author jpiat
 * @author mpelcat
 */
public class CodeElementFactory {

    /**
	 * Create an element considering its type
	 * 
	 * @param name
	 *            The name of the code element to be created
	 * @param parentContainer
	 *            The parent container of the code element
	 * @param vertex
	 *            The vertex corresponding to the code element
	 * @return The created code element, null if failed to create the code
	 *         element
	 */
    public static ICodeElement createElement(String name, AbstractCodeContainer parentContainer, SDFAbstractVertex vertex) {
        try {
            return ((ICodeGenSDFVertex) vertex).getCodeElement(parentContainer);
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void treatSpecialBehaviorVertex(String name, AbstractBufferContainer parentContainer, SDFAbstractVertex vertex) {
        try {
            if (vertex instanceof CodeGenSDFForkVertex) {
                SDFEdge incomingEdge = null;
                for (SDFEdge inEdge : ((SDFGraph) vertex.getBase()).incomingEdgesOf(vertex)) {
                    incomingEdge = inEdge;
                }
                Buffer inBuffer = parentContainer.getBuffer(incomingEdge);
                for (SDFEdge outEdge : ((SDFGraph) vertex.getBase()).outgoingEdgesOf(vertex)) {
                    ConstantExpression index = new ConstantExpression("", new DataType("int"), ((CodeGenSDFForkVertex) vertex).getEdgeIndex(outEdge));
                    String buffName = parentContainer.getBuffer(outEdge).getName();
                    IExpression expr = new BinaryExpression("%", new BinaryExpression("*", index, new ConstantExpression(outEdge.getProd().intValue())), new ConstantExpression(inBuffer.getSize()));
                    SubBuffer subElt = new SubBuffer(buffName, outEdge.getProd().intValue(), expr, inBuffer, outEdge, parentContainer);
                    parentContainer.removeBufferAllocation(parentContainer.getBuffer(outEdge));
                    parentContainer.addSubBufferAllocation(new SubBufferAllocation(subElt));
                }
            } else if (vertex instanceof CodeGenSDFJoinVertex) {
                SDFEdge outgoingEdge = null;
                for (SDFEdge outEdge : ((SDFGraph) vertex.getBase()).outgoingEdgesOf(vertex)) {
                    outgoingEdge = outEdge;
                }
                Buffer outBuffer = parentContainer.getBuffer(outgoingEdge);
                for (SDFEdge inEdge : ((SDFGraph) vertex.getBase()).incomingEdgesOf(vertex)) {
                    ConstantExpression index = new ConstantExpression("", new DataType("int"), ((CodeGenSDFJoinVertex) vertex).getEdgeIndex(inEdge));
                    String buffName = parentContainer.getBuffer(inEdge).getName();
                    IExpression expr = new BinaryExpression("%", new BinaryExpression("*", index, new ConstantExpression(inEdge.getCons().intValue())), new ConstantExpression(outBuffer.getSize()));
                    SubBuffer subElt = new SubBuffer(buffName, inEdge.getCons().intValue(), expr, outBuffer, inEdge, parentContainer);
                    parentContainer.removeBufferAllocation(parentContainer.getBuffer(inEdge));
                    parentContainer.addSubBufferAllocation(new SubBufferAllocation(subElt));
                }
            } else if (vertex instanceof CodeGenSDFBroadcastVertex) {
                SDFEdge incomingEdge = null;
                for (SDFEdge inEdge : ((SDFGraph) vertex.getBase()).incomingEdgesOf(vertex)) {
                    incomingEdge = inEdge;
                }
                Buffer inBuffer = parentContainer.getBuffer(incomingEdge);
                for (SDFEdge outEdge : ((SDFGraph) vertex.getBase()).outgoingEdgesOf(vertex)) {
                    ConstantExpression index = new ConstantExpression("", new DataType("int"), 0);
                    String buffName = parentContainer.getBuffer(outEdge).getName();
                    IExpression expr = new BinaryExpression("%", new BinaryExpression("*", index, new ConstantExpression(outEdge.getCons().intValue())), new ConstantExpression(inBuffer.getSize()));
                    SubBuffer subElt = new SubBuffer(buffName, outEdge.getCons().intValue(), expr, inBuffer, outEdge, parentContainer);
                    parentContainer.removeBufferAllocation(parentContainer.getBuffer(outEdge));
                    parentContainer.addSubBufferAllocation(new SubBufferAllocation(subElt));
                }
            } else if (vertex instanceof CodeGenSDFRoundBufferVertex) {
                SDFEdge outgoingEdge = null;
                for (SDFEdge outEdge : ((SDFGraph) vertex.getBase()).outgoingEdgesOf(vertex)) {
                    outgoingEdge = outEdge;
                }
                Buffer outBuffer = parentContainer.getBuffer(outgoingEdge);
                for (SDFEdge inEdge : ((SDFGraph) vertex.getBase()).incomingEdgesOf(vertex)) {
                    ConstantExpression index = new ConstantExpression("", new DataType("int"), 0);
                    String buffName = parentContainer.getBuffer(inEdge).getName();
                    IExpression expr = new BinaryExpression("%", new BinaryExpression("*", index, new ConstantExpression(inEdge.getCons().intValue())), new ConstantExpression(outBuffer.getSize()));
                    SubBuffer subElt = new SubBuffer(buffName, inEdge.getCons().intValue(), expr, outBuffer, inEdge, parentContainer);
                    parentContainer.removeBufferAllocation(parentContainer.getBuffer(inEdge));
                    parentContainer.addSubBufferAllocation(new SubBufferAllocation(subElt));
                }
            }
        } catch (InvalidExpressionException e) {
            e.printStackTrace();
        }
    }
}
