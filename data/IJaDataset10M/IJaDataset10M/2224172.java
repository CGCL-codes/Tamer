package jp.seraph.same.model;

import jp.seraph.jsade.core.World;
import jp.seraph.jsade.perceptor.SeeObjectType;
import jp.seraph.jsade.task.TaskSelectorAgent;
import org.apache.commons.math.geometry.Vector3D;

/**
 *
 *
 */
public class DefaultAgentModel implements AgentModel {

    public DefaultAgentModel() {
    }

    public void init(TaskSelectorAgent aAgent) {
    }

    public void updateModel(World aWorld) {
        mBall = aWorld.getBall().getVector();
        mLeft1 = aWorld.getSeeObject(SeeObjectType.LEFT_FIELD_1).getVector();
        mLeft2 = aWorld.getSeeObject(SeeObjectType.LEFT_FIELD_2).getVector();
        mRight1 = aWorld.getSeeObject(SeeObjectType.RIGHT_FIELD_1).getVector();
        mRight2 = aWorld.getSeeObject(SeeObjectType.RIGHT_FIELD_2).getVector();
        mUnitX = aWorld.getUnitVectorX();
        mUnitY = aWorld.getUnitVectorY();
        mUnitZ = aWorld.getUnitVectorZ();
    }

    private Vector3D mBall = Vector3D.zero;

    private Vector3D mLeft1 = Vector3D.zero;

    private Vector3D mLeft2 = Vector3D.zero;

    private Vector3D mRight1 = Vector3D.zero;

    private Vector3D mRight2 = Vector3D.zero;

    private Vector3D mUnitX = Vector3D.zero;

    private Vector3D mUnitY = Vector3D.zero;

    private Vector3D mUnitZ = Vector3D.zero;

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getBallVector()
     */
    public Vector3D getBallVector() {
        return mBall;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getLeftField1()
     */
    public Vector3D getLeftField1() {
        return mLeft1;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getLeftField2()
     */
    public Vector3D getLeftField2() {
        return mLeft2;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getRightField1()
     */
    public Vector3D getRightField1() {
        return mRight1;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getRightField2()
     */
    public Vector3D getRightField2() {
        return mRight2;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getUnitX()
     */
    public Vector3D getUnitX() {
        return mUnitX;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getUnitY()
     */
    public Vector3D getUnitY() {
        return mUnitY;
    }

    /**
     *
     * @see jp.seraph.same.model.AgentModel#getUnitZ()
     */
    public Vector3D getUnitZ() {
        return mUnitZ;
    }
}
