package it.enricod.jcontextfree.gui;

import it.enricod.jcontextfree.gui.impl.ProgramsTabbedPane;
import it.enricod.jcontextfree.model.IProgramPM;
import javax.swing.JTabbedPane;
import com.google.inject.ImplementedBy;

@ImplementedBy(ProgramsTabbedPane.class)
public interface IProgramsTabbedPane {

    JTabbedPane getTabbedPane();

    void addProgramTab(IProgramPM program);
}
