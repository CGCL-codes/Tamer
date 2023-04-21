package net.charabia.jsmoothgen.application.gui.editors;

import net.charabia.jsmoothgen.skeleton.*;
import net.charabia.jsmoothgen.application.*;
import net.charabia.jsmoothgen.application.gui.*;
import net.charabia.jsmoothgen.application.gui.util.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import com.l2fprod.common.swing.*;
import com.l2fprod.common.propertysheet.*;

public class ExecutableName extends Editor {

    private FileSelectionTextField m_selector = new FileSelectionTextField();

    public ExecutableName() {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, m_selector);
    }

    public void dataChanged() {
        if (getBaseDir() != null) m_selector.setBaseDir(getBaseDir());
        if (m_model.getExecutableName() != null) m_selector.setFile(getAbsolutePath(new java.io.File(m_model.getExecutableName()))); else m_selector.setFile(null);
    }

    public void updateModel() {
        if (m_selector.getFile() != null) m_model.setExecutableName(m_selector.getFile().toString()); else m_model.setExecutableName(null);
    }

    public String getLabel() {
        return "EXECUTABLE_LABEL";
    }

    public String getDescription() {
        return "EXECUTABLE_HELP";
    }
}
