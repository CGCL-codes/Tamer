package abc.weaving.matching;

/** A joinpoint shadow that applies at a method call
 *  @author Ganesh Sittampalam
 *  @date 05-May-04
 */
public class MethodCallShadowType extends ShadowType {

    public ShadowMatch matchesAt(MethodPosition pos) {
        return MethodCallShadowMatch.matchesAt(pos);
    }
}
