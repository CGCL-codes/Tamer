package jp.seraph.jsade.model;

/**
 * オブジェクトの生成・組み立てした結果のオブジェクト・ジョイントを取得するメソッドを持つinterface
 */
public interface ObjectContainer {

    /**
     * 対象Identifierのオブジェクトを取得する。
     * 
     * @param aIdentifier
     * @return 対象オブジェクト
     */
    public ModelPart getObject(ModelObjectIdentifier aIdentifier);

    /**
     * 対象のIdeintifierのジョイントを種臆する
     * 
     * @param aIdentifier
     * @return 対象ジョイント
     */
    public Joint getJoint(JointIdentifier aIdentifier);
}
