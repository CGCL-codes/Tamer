package plcopen.inf.type.group.sfc;

import plcopen.inf.model.IGraphicElement;

/**
 * SFC를 위해 추가되는 모든 도형적 요소들이 가져야 할 공통에 대해 정의하는 Interface. XML 파싱과 문서 작성을 위해 필요한
 * Terminal이 상수로 정의되어 있다.
 * 
 * @author swkim
 * 
 */
public interface ISFCObject extends IGraphicElement {

    public static final String ID_JUMPSTEP = "jumpStep";

    public static final String ID_MARCOSTEP = "macroStep";

    public static final String ID_NEGATED = "negated";

    public static final String ID_SELCON = "selectionConvergence";

    public static final String ID_SELDIV = "selectionDivergence";

    public static final String ID_SIMULCON = "simultaneousConvergence";

    public static final String ID_SIMULDIV = "simultaneousDivergence";

    public static final String ID_STEP = "step";

    public static final String ID_TRANSITION = "transition";
}
