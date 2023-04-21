package de.regnis.visuallayout;

import com.intellij.psi.*;
import de.regnis.q.gui.layout.*;
import de.regnis.visuallayout.model.*;
import de.regnis.visuallayout.utils.*;

/**
 * @author Thomas Singer
 */
final class VLModelBuilder {

    private static final String GROUP_CLASS_NAME = QTableGridGroup.class.getName();

    private final VLModel model = new VLModel();

    public VLModelBuilder() {
    }

    public VLModel finish() {
        model.ensureEnoughColumnsAndRows();
        return model;
    }

    public void handleMethod(PsiMethod method, PsiMethodCallExpression methodCallExpression) throws VLException {
        final String signature = VLPsiUtils.getSignature(method);
        if (handleAddRows("addRowNoStretch", VLStretchValue.NO_STRETCH, signature, methodCallExpression)) {
        } else if (handleAddRows("addRowStretchIfRequired", VLStretchValue.STRETCH_IF_REQUIRED, signature, methodCallExpression)) {
        } else if (handleAddRows("addRowStretchOnResize", VLStretchValue.STRETCH_ON_RESIZE, signature, methodCallExpression)) {
        } else if (QCompareUtils.areEqual(signature, "int addRowBetweenComponents()")) {
            handleAddRowXxx(VLRow.HEIGHT_BETWEEN_COMPONENTS, VLStretchValue.NO_STRETCH);
        } else if (QCompareUtils.areEqual(signature, "int addRowSeparator()")) {
            handleAddRowXxx(VLRow.HEIGHT_SEPARATOR, VLStretchValue.NO_STRETCH);
        } else if (handleAddColumns("addColumnNoStretch", VLStretchValue.NO_STRETCH, signature, methodCallExpression)) {
        } else if (handleAddColumns("addColumnStretchIfRequired", VLStretchValue.STRETCH_IF_REQUIRED, signature, methodCallExpression)) {
        } else if (handleAddColumns("addColumnStretchOnResize", VLStretchValue.STRETCH_ON_RESIZE, signature, methodCallExpression)) {
        } else if (QCompareUtils.areEqual(signature, "int addColumnIndentation()")) {
            handleAddColumnXxx(VLColumn.WIDTH_INDENTATION, VLStretchValue.NO_STRETCH);
        } else if (QCompareUtils.areEqual(signature, "int addColumnBetweenEditorComponents()")) {
            handleAddColumnXxx(VLColumn.WIDTH_BETWEEN_EDITOR_COMPONENTS, VLStretchValue.NO_STRETCH);
        } else if (QCompareUtils.areEqual(signature, "int addColumnBetweenComponents()")) {
            handleAddColumnXxx(VLColumn.WIDTH_BETWEEN_COMPONENTS, VLStretchValue.NO_STRETCH);
        } else if (QCompareUtils.areEqual(signature, "int addEditorColumnsStretchOnResize()")) {
            handleAddColumnXxx(VLColumn.WIDTH_CONTENT, VLStretchValue.NO_STRETCH);
            handleAddColumnXxx(VLColumn.WIDTH_BETWEEN_EDITOR_COMPONENTS, VLStretchValue.NO_STRETCH);
            handleAddColumnXxx(VLColumn.WIDTH_CONTENT, VLStretchValue.STRETCH_ON_RESIZE);
        } else if (QCompareUtils.areEqual(signature, "void addComponent(javax.swing.JComponent, int, int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 3);
            model.addComponent(new VLSingleComponent(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), 1, 1, true, true, true, true));
        } else if (QCompareUtils.areEqual(signature, "void addComponent(javax.swing.JComponent, int, int, int, int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 5);
            model.addComponent(new VLSingleComponent(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), getInt(expressions[3]), getInt(expressions[4]), true, true, true, true));
        } else if (QCompareUtils.areEqual(signature, "void addComponent(javax.swing.JComponent, int, int, int, int, boolean, boolean, boolean, boolean)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 9);
            model.addComponent(new VLSingleComponent(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), getInt(expressions[3]), getInt(expressions[4]), getBoolean(expressions[5]), getBoolean(expressions[6]), getBoolean(expressions[7]), getBoolean(expressions[8])));
        } else if (QCompareUtils.areEqual(signature, "void addComponent(javax.swing.JComponent, int, int, boolean, boolean, boolean, boolean)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 7);
            model.addComponent(new VLSingleComponent(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), 1, 1, getBoolean(expressions[3]), getBoolean(expressions[4]), getBoolean(expressions[5]), getBoolean(expressions[6])));
        } else if (QCompareUtils.areEqual(signature, "void addCompFill(javax.swing.JComponent, int, int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 3);
            final String componentText = expressions[0].getText();
            final int row = getInt(expressions[1]);
            final int column = getInt(expressions[2]);
            model.addComponent(new VLSingleComponent(componentText, row, column, 1, model.getColumnCount() - column, true, true, true, true));
        } else if (QCompareUtils.areEqual(signature, "void addEditor(de.regnis.q.gui.layout.QEditor, int, int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 3);
            model.addComponent(new VLEditor(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), 1, 1, true, true, true, true));
        } else if (QCompareUtils.areEqual(signature, "void addEditor(de.regnis.q.gui.layout.QEditor, int, int, int, int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 5);
            model.addComponent(new VLEditor(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), getInt(expressions[3]), getInt(expressions[4]), true, true, true, true));
        } else if (QCompareUtils.areEqual(signature, "void addEditor(de.regnis.q.gui.layout.QEditor, int, int, boolean, boolean, boolean, boolean)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 7);
            model.addComponent(new VLEditor(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), 1, 1, getBoolean(expressions[3]), getBoolean(expressions[4]), getBoolean(expressions[5]), getBoolean(expressions[6])));
        } else if (QCompareUtils.areEqual(signature, "void addEditor(de.regnis.q.gui.layout.QEditor, int, int, int, int, boolean, boolean, boolean, boolean)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 9);
            model.addComponent(new VLEditor(expressions[0].getText(), getInt(expressions[1]), getInt(expressions[2]), getInt(expressions[3]), getInt(expressions[4]), getBoolean(expressions[5]), getBoolean(expressions[6]), getBoolean(expressions[7]), getBoolean(expressions[8])));
        } else {
            throw new VLException("Unsupported method call '" + signature + "'.", methodCallExpression);
        }
    }

    private boolean handleAddColumns(String methodName, VLStretchValue stretchValue, String signature, PsiMethodCallExpression methodCallExpression) throws VLException {
        if (QCompareUtils.areEqual(signature, "int " + methodName + "()")) {
            handleAddColumnXxx(VLColumn.WIDTH_CONTENT, stretchValue);
            return true;
        }
        if (QCompareUtils.areEqual(signature, "int " + methodName + "(int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 1);
            handleAddColumnXxx(expressions[0].getText(), stretchValue);
            return true;
        }
        if (QCompareUtils.areEqual(signature, "int " + methodName + "(int, " + GROUP_CLASS_NAME + ")")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 2);
            handleAddColumnXxx(expressions[0].getText(), stretchValue, expressions[1].getText());
            return true;
        }
        return false;
    }

    private boolean handleAddRows(String methodName, VLStretchValue stretchValue, String signature, PsiMethodCallExpression methodCallExpression) throws VLException {
        if (QCompareUtils.areEqual(signature, "int " + methodName + "()")) {
            handleAddRowXxx("0", stretchValue);
            return true;
        }
        if (QCompareUtils.areEqual(signature, "int " + methodName + "(int)")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 1);
            handleAddRowXxx(expressions[0].getText(), stretchValue);
            return true;
        }
        if (QCompareUtils.areEqual(signature, "int " + methodName + "(int, " + GROUP_CLASS_NAME + ")")) {
            final PsiExpression[] expressions = getArgumentExpressions(methodCallExpression, 2);
            handleAddRowXxx(expressions[0].getText(), stretchValue, expressions[1].getText());
            return true;
        }
        return false;
    }

    private PsiExpression[] getArgumentExpressions(PsiMethodCallExpression methodCallExpression, int expectedParameterCount) throws VLException {
        final PsiExpressionList argumentList = methodCallExpression.getArgumentList();
        if (argumentList == null) {
            throw new VLException("No parameters found", methodCallExpression);
        }
        final PsiExpression[] expressions = argumentList.getExpressions();
        if (expressions.length != expectedParameterCount) {
            throw new VLException(expectedParameterCount + " parameters expected", methodCallExpression);
        }
        return expressions;
    }

    private void handleAddColumnXxx(String columnWidth, VLStretchValue stretchValue) {
        handleAddColumnXxx(columnWidth, stretchValue, null);
    }

    private void handleAddColumnXxx(String columnWidth, VLStretchValue stretchValue, String group) {
        model.addColumn(new VLColumn(columnWidth, stretchValue, group));
    }

    private void handleAddRowXxx(String rowHeight, VLStretchValue stretchValue) {
        handleAddRowXxx(rowHeight, stretchValue, null);
    }

    private void handleAddRowXxx(String rowHeight, VLStretchValue stretchValue, String group) {
        model.addRow(new VLRow(rowHeight, stretchValue, group));
    }

    private static int getInt(PsiExpression expression) throws VLException {
        try {
            return Integer.parseInt(expression.getText());
        } catch (NumberFormatException ex) {
            throw new VLException("Expected integer value", expression);
        }
    }

    private boolean getBoolean(PsiExpression expression) throws VLException {
        final String text = expression.getText();
        if (QCompareUtils.areEqual(text, "true")) {
            return true;
        }
        if (QCompareUtils.areEqual(text, "false")) {
            return false;
        }
        throw new VLException("Expected boolean value", expression);
    }
}
