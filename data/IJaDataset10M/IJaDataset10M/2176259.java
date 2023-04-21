package java.beans;

import javax.swing.JTabbedPane;

class SwingJTabbedPanePersistenceDelegate extends DefaultPersistenceDelegate {

    @Override
    @SuppressWarnings({ "nls", "boxing" })
    protected void initialize(Class<?> type, Object oldInstance, Object newInstance, Encoder enc) {
        super.initialize(type, oldInstance, newInstance, enc);
        if (type != oldInstance.getClass()) {
            return;
        }
        JTabbedPane pane = (JTabbedPane) oldInstance;
        int count = pane.getTabCount();
        for (int i = 0; i < count; i++) {
            Expression getterExp = new Expression(pane.getComponentAt(i), pane, "getComponentAt", new Object[] { i });
            try {
                Object oldVal = getterExp.getValue();
                enc.writeExpression(getterExp);
                Object targetVal = enc.get(oldVal);
                Object newVal = null;
                try {
                    JTabbedPane newJTabbedPane = (JTabbedPane) newInstance;
                    newVal = new Expression(newJTabbedPane.getComponent(i), newJTabbedPane, "getComponentAt", new Object[] { i }).getValue();
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
                if (null == targetVal) {
                    if (null != newVal) {
                        Statement setterStm = new Statement(oldInstance, "addTab", new Object[] { pane.getTitleAt(i), oldVal });
                        enc.writeStatement(setterStm);
                    }
                } else {
                    Statement setterStm = new Statement(oldInstance, "addTab", new Object[] { pane.getTitleAt(i), oldVal });
                    enc.writeStatement(setterStm);
                }
            } catch (Exception ex) {
                enc.getExceptionListener().exceptionThrown(ex);
            }
        }
    }
}
