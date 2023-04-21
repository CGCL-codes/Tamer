package de.jmda.mview.typeshape.relation;

/**
 * <b>R</b>elation <b>E</b>ndpoint <b>C</b>onnection (REC) connecting source
 * with target connector point in <b>O</b>rthogonal routing style. Both relation
 * endpoints have horizontal direction (H2H).
 *
 * @author roger.jmda@gmail.com
 */
public abstract class RECOH2H extends RECOrthogonal {

    /**
	 * @param relationEndpointHelperSource
	 * @param relationEndpointHelperTarget
	 */
    public RECOH2H(RelationEndpointHelper relationEndpointHelperSource, RelationEndpointHelper relationEndpointHelperTarget) throws IllegalArgumentException {
        super(relationEndpointHelperSource, relationEndpointHelperTarget);
    }

    @Override
    protected void checkStateInvalidated() throws StateInvalidatedException {
        super.checkStateInvalidated();
        if (false == checkRelationEndpointsHorizontal()) {
            throw new StateInvalidatedException("both relation endpoints have to be horizontal\n" + "source relation endpoint is horizontal: " + relationEndpointHelperSource.isHorizontal() + "\n" + "target relation endpoint is horizontal: " + relationEndpointHelperTarget.isHorizontal());
        }
    }

    private boolean checkRelationEndpointsHorizontal() {
        return RECOH2H.checkRelationEndpointsHorizontal(relationEndpointHelperSource, relationEndpointHelperTarget);
    }

    public static boolean checkRelationEndpointsHorizontal(RelationEndpointHelper r1, RelationEndpointHelper r2) {
        return r1.isHorizontal() && r2.isHorizontal();
    }
}
