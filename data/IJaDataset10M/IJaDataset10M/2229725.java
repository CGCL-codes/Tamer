package jcomplexity.gui;

import java.util.ListIterator;
import jcomplexity.model.QList;
import jcomplexity.parameter.Parameter;
import com.trolltech.qt.QtPropertyReader;
import com.trolltech.qt.QtPropertyResetter;
import com.trolltech.qt.QtPropertyUser;
import com.trolltech.qt.QtPropertyWriter;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QWidget;

public class ArgumentEditor extends QComboBox {

    public ArgumentEditor() {
        this(null);
    }

    public ArgumentEditor(QWidget parent) {
        super(parent);
        refresh();
    }

    @QtPropertyUser
    @QtPropertyReader
    public final Parameter parameter() {
        return (Parameter) itemData(currentIndex(), ItemDataRole.UserRole);
    }

    @QtPropertyWriter
    public final void setParameter(Parameter parameter) {
        setCurrentIndex(findData(parameter, ItemDataRole.UserRole));
    }

    @QtPropertyResetter
    public final void resetParameter() {
        setParameter(null);
    }

    @QtPropertyReader
    public Class<?> type() {
        return type;
    }

    @QtPropertyWriter
    public void setType(Class<?> type) {
        this.type = type;
        refresh();
    }

    private Class<?> type;

    @QtPropertyReader
    public QList<Parameter> parameters() {
        return parameters;
    }

    @QtPropertyWriter
    public void setParameters(QList<Parameter> parameters) {
        this.parameters = parameters;
        refresh();
    }

    private QList<Parameter> parameters;

    private void refresh() {
        Parameter argument = parameter();
        clear();
        insertItem(0, "null", null);
        Class<?> type = type();
        if (type != null) {
            ListIterator<Parameter> it = parameters().listIterator();
            while (it.hasNext()) {
                int index = it.nextIndex();
                Parameter parameter = it.next();
                if (parameter.canApplyTo(type)) {
                    insertItem(index + 1, parameter.getFullName(), parameter);
                }
            }
        }
        setParameter(argument);
    }
}
