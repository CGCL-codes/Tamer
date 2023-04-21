package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class ModuleOrGenerateItemDecl {

    public ModuleOrGenerateItemDecl(TaskDeclaration ele) {
        m_item = new ATask(ele);
    }

    public ModuleOrGenerateItemDecl(FuncDecl ele) {
        m_item = new AFunc(ele);
    }

    private ModuleOrGenerateItemDecl(A ele) {
        m_item = ele;
    }

    public static ModuleOrGenerateItemDecl createNet(ListOf<NetDeclaration> ele) {
        return new ModuleOrGenerateItemDecl(new ANet(ele));
    }

    public static ModuleOrGenerateItemDecl createReg(ListOf<RegDecl> ele) {
        return new ModuleOrGenerateItemDecl(new AReg(ele));
    }

    public static ModuleOrGenerateItemDecl createInt(ListOf<IntegerDecl> ele) {
        return new ModuleOrGenerateItemDecl(new AInteger(ele));
    }

    public static ModuleOrGenerateItemDecl createReal(ListOf<RealDecl> ele) {
        return new ModuleOrGenerateItemDecl(new AReal(ele));
    }

    public static ModuleOrGenerateItemDecl createTime(ListOf<TimeDecl> ele) {
        return new ModuleOrGenerateItemDecl(new ATime(ele));
    }

    public static ModuleOrGenerateItemDecl createRealtime(ListOf<RealtimeDecl> ele) {
        return new ModuleOrGenerateItemDecl(new ARealtime(ele));
    }

    public static ModuleOrGenerateItemDecl createEvent(ListOf<EventDecl> ele) {
        return new ModuleOrGenerateItemDecl(new AEvent(ele));
    }

    public static ModuleOrGenerateItemDecl createGenvar(ListOf<GenvarIdent> ele) {
        return new ModuleOrGenerateItemDecl(new AGenvar(ele));
    }

    public static enum EType {

        eNet, eReg, eInteger, eReal, eTime, eRealtime, eEvent, eGenvar, eTask, eFunc
    }

    private A m_item;

    private abstract static class A {

        protected A(EType e) {
            m_type = e;
        }

        final EType m_type;
    }

    private static class ANet extends A {

        private ANet(ListOf<NetDeclaration> ele) {
            super(EType.eNet);
            m_ele = ele;
        }

        private ListOf<NetDeclaration> m_ele;
    }

    private static class AReg extends A {

        private AReg(ListOf<RegDecl> ele) {
            super(EType.eReg);
            m_ele = ele;
        }

        private ListOf<RegDecl> m_ele;
    }

    private static class AInteger extends A {

        private AInteger(ListOf<IntegerDecl> ele) {
            super(EType.eInteger);
            m_ele = ele;
        }

        private ListOf<IntegerDecl> m_ele;
    }

    private static class AReal extends A {

        private AReal(ListOf<RealDecl> ele) {
            super(EType.eReal);
            m_ele = ele;
        }

        private ListOf<RealDecl> m_ele;
    }

    private static class ATime extends A {

        private ATime(ListOf<TimeDecl> ele) {
            super(EType.eTime);
            m_ele = ele;
        }

        private ListOf<TimeDecl> m_ele;
    }

    private static class ARealtime extends A {

        private ARealtime(ListOf<RealtimeDecl> ele) {
            super(EType.eRealtime);
            m_ele = ele;
        }

        private ListOf<RealtimeDecl> m_ele;
    }

    private static class AEvent extends A {

        private AEvent(ListOf<EventDecl> ele) {
            super(EType.eEvent);
            m_ele = ele;
        }

        private ListOf<EventDecl> m_ele;
    }

    private static class AGenvar extends A {

        private AGenvar(ListOf<GenvarIdent> ele) {
            super(EType.eGenvar);
            m_ele = ele;
        }

        private ListOf<GenvarIdent> m_ele;
    }

    private static class ATask extends A {

        private ATask(TaskDeclaration ele) {
            super(EType.eTask);
            m_ele = ele;
        }

        private TaskDeclaration m_ele;
    }

    private static class AFunc extends A {

        private AFunc(FuncDecl ele) {
            super(EType.eFunc);
            m_ele = ele;
        }

        private FuncDecl m_ele;
    }
}
