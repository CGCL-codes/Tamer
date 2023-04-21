package v2k.parser.tree;

/**
 *
 * @author karl
 */
public class GenvarExpression {

    public GenvarExpression(Object e1, Object e2) {
        m_e1 = (E1) e1;
        m_e2 = (E2) e2;
    }

    private E1 m_e1;

    private E2 m_e2;

    static class E1 {

        public E1(UnaryOp uop, GenvarPrimary prim) {
            m_uop = uop;
            m_prim = prim;
        }

        private UnaryOp m_uop;

        private GenvarPrimary m_prim;
    }

    static class E2 {

        E2(BinaryOp bop, GenvarExpression expr, Object e2) {
            m_item = new ABinop(bop, expr, (E2) e2);
        }

        E2(GenvarExpression trueExpr, GenvarExpression falseExpr1, Object falseExpr2) {
            m_item = new ATernop(trueExpr, falseExpr1, (E2) falseExpr2);
        }

        private static enum EType {

            eBinop, eTernop
        }

        ;

        private A m_item;

        private abstract static class A {

            protected A(EType e) {
                m_type = e;
            }

            final EType m_type;
        }

        private static class ABinop extends A {

            private ABinop(BinaryOp bop, GenvarExpression expr, E2 e2) {
                super(EType.eBinop);
                m_op = bop;
                m_expr = expr;
                m_expr2 = e2;
            }

            private BinaryOp m_op;

            private GenvarExpression m_expr;

            private E2 m_expr2;
        }

        private static class ATernop extends A {

            private ATernop(GenvarExpression trueExpr, GenvarExpression falseExpr1, E2 falseExpr2) {
                super(EType.eTernop);
                m_trueExpr = trueExpr;
                m_falseExpr1 = falseExpr1;
                m_falseExpr2 = falseExpr2;
            }

            private GenvarExpression m_trueExpr;

            private GenvarExpression m_falseExpr1;

            private E2 m_falseExpr2;
        }
    }
}
