package net.sf.antcontrib.cpptasks.intel;

import net.sf.antcontrib.cpptasks.compiler.LinkType;
import net.sf.antcontrib.cpptasks.compiler.Linker;
import net.sf.antcontrib.cpptasks.devstudio.DevStudioCompatibleLibrarian;

/**
 * Adapter for the xilib from the Intel(r) C++ Compiler for IA-32 or IA-64
 * systems running Microsoft (r) operating systems
 * 
 * @author Curt Arnold
 */
public class IntelWin32Librarian extends DevStudioCompatibleLibrarian {

    private static final IntelWin32Librarian instance = new IntelWin32Librarian();

    public static IntelWin32Librarian getInstance() {
        return instance;
    }

    protected IntelWin32Librarian() {
        super("xilib", "-qv");
    }

    public Linker getLinker(LinkType type) {
        return IntelWin32Linker.getInstance().getLinker(type);
    }
}
