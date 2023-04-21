package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class ModuleOrGenerateItem {

    public ModuleOrGenerateItem(ModuleOrGenerateItemDecl ele) {
        m_item = new AModOrGen(ele);
    }

    private ModuleOrGenerateItem(A ele) {
        m_item = ele;
    }

    public static ModuleOrGenerateItem createLocalParam(ListOf<LocalParameterDecl> ele) {
        return new ModuleOrGenerateItem(new ALocalParm(ele));
    }

    public static ModuleOrGenerateItem createParam(ListOf<DefparamAssign> ele) {
        return new ModuleOrGenerateItem(new AParmOvr(ele));
    }

    public static ModuleOrGenerateItem createInst(ListOf<ModuleInstance> ele) {
        return new ModuleOrGenerateItem(new AInsts(ele));
    }

    public ModuleOrGenerateItem(ContinuousAssign ele) {
        m_item = new AContAssgn(ele);
    }

    public ModuleOrGenerateItem(InitialConstruct ele) {
        m_item = new AInitial(ele);
    }

    public ModuleOrGenerateItem(AlwaysConstruct ele) {
        m_item = new AAlways(ele);
    }

    public ModuleOrGenerateItem(LoopGenerateConstruct ele) {
        m_item = new ALoopGen(ele);
    }

    public ModuleOrGenerateItem(ConditionalGenerateConstruct ele) {
        m_item = new ACondGen(ele);
    }

    private enum EType {

        eModOrGen, eLocalParm, eParmOvr, eContAssgn, eInsts, eInitial, eAlways, eLoopGen, eCondGen
    }

    private A m_item;

    private abstract static class A {

        protected A(EType e) {
            m_type = e;
        }

        final EType m_type;
    }

    private static class AModOrGen extends A {

        private AModOrGen(ModuleOrGenerateItemDecl ele) {
            super(EType.eModOrGen);
            m_ele = ele;
        }

        private ModuleOrGenerateItemDecl m_ele;
    }

    private static class ALocalParm extends A {

        private ALocalParm(ListOf<LocalParameterDecl> ele) {
            super(EType.eLocalParm);
            m_ele = ele;
        }

        private ListOf<LocalParameterDecl> m_ele;
    }

    private static class AParmOvr extends A {

        private AParmOvr(ListOf<DefparamAssign> ele) {
            super(EType.eParmOvr);
            m_ele = ele;
        }

        private ListOf<DefparamAssign> m_ele;
    }

    private static class AContAssgn extends A {

        private AContAssgn(ContinuousAssign ele) {
            super(EType.eContAssgn);
            m_ele = ele;
        }

        private ContinuousAssign m_ele;
    }

    private static class AInsts extends A {

        private AInsts(ListOf<ModuleInstance> ele) {
            super(EType.eInsts);
            m_ele = ele;
        }

        private ListOf<ModuleInstance> m_ele;
    }

    private static class AInitial extends A {

        private AInitial(InitialConstruct ele) {
            super(EType.eInitial);
            m_ele = ele;
        }

        private InitialConstruct m_ele;
    }

    private static class AAlways extends A {

        private AAlways(AlwaysConstruct ele) {
            super(EType.eAlways);
            m_ele = ele;
        }

        private AlwaysConstruct m_ele;
    }

    private static class ALoopGen extends A {

        private ALoopGen(LoopGenerateConstruct ele) {
            super(EType.eLoopGen);
            m_ele = ele;
        }

        private LoopGenerateConstruct m_ele;
    }

    private static class ACondGen extends A {

        private ACondGen(ConditionalGenerateConstruct ele) {
            super(EType.eCondGen);
            m_ele = ele;
        }

        private ConditionalGenerateConstruct m_ele;
    }
}
