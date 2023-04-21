package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class TaskItemDecl {

    public TaskItemDecl(BlockItemDecl ele) {
        m_item = new ABlk(ele);
    }

    public TaskItemDecl(TfPortDeclaration ele) {
        m_item = new APort(ele);
    }

    private enum EType {

        eBlk, ePort
    }

    private A m_item;

    private abstract static class A {

        protected A(EType e) {
            m_type = e;
        }

        final EType m_type;
    }

    private static class ABlk extends A {

        private ABlk(BlockItemDecl ele) {
            super(EType.eBlk);
            m_ele = ele;
        }

        private BlockItemDecl m_ele;
    }

    private static class APort extends A {

        private APort(TfPortDeclaration ele) {
            super(EType.ePort);
            m_ele = ele;
        }

        private TfPortDeclaration m_ele;
    }
}
