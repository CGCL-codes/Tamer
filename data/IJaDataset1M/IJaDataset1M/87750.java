package org.sodbeans.compiler.hop.impl;

import java.io.File;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.filesystems.FileObject;
import org.openide.modules.InstalledFileLocator;
import org.sodbeans.compiler.api.CompilerCodeCompletionRequest;
import org.sodbeans.compiler.api.CompilerCodeCompletionResults;
import org.sodbeans.compiler.api.CompilerCodeMatcher;
import org.sodbeans.compiler.api.CompilerDataStorage;
import org.sodbeans.compiler.api.CompilerErrorManager;
import org.sodbeans.compiler.api.descriptors.CompilerFileDescriptor;
import org.sodbeans.compiler.hop.descriptors.HopFileDescriptor;
import org.sonify.vm.AbstractVirtualMachine;
import org.sonify.vm.hop.HopVirtualMachine;
import org.sonify.vm.intermediate.symbol.FileDescriptor;
import org.sodbeans.plugins.*;
import org.sonify.vm.CodeCompletionRequest;
import org.sonify.vm.CodeCompletionResult;
import org.sonify.vm.hop.HopStandardLibrary;

/**
 * This class implements the general Sodbeans compiler class with the HOP
 * virtual machine.
 * 
 * @author Andreas Stefik
 */
public class HopCompiler extends org.sodbeans.compiler.api.Compiler {

    protected static HopDataStorage data;

    private static final String STANDARD_LIBRARY_CODE_BASE_NAME = "org.sodbeans.hop.libraries";

    private static final String STANDARD_LIBRARY_ROOT_NAME = "Libraries";

    private static final String STANDARD_LIBRARY_INDEX_PATH = "indexes/hop.index";

    private static final String STANDARD_LIBRARY_ROOT_PATH = "hop";

    private static final Logger logger = Logger.getLogger(HopCompiler.class.getName());

    public HopCompiler() {
    }

    private static void vmSingleton() {
        if (virtualMachine == null) {
            HopStandardLibrary.overrideStandardLibraryPath(getRootFolder(), getIndexFile());
            virtualMachine = new HopVirtualMachine();
            addPlugins(virtualMachine);
            compilerErrorManager = new HopCompilerErrorManager();
            ((HopCompilerErrorManager) compilerErrorManager).setMachine(virtualMachine);
            data = new HopDataStorage();
            data.setVirtualMachine(virtualMachine);
            virtualMachine.addListener(data);
        }
    }

    private static File getIndexFile() {
        File file = InstalledFileLocator.getDefault().locate(STANDARD_LIBRARY_INDEX_PATH, STANDARD_LIBRARY_CODE_BASE_NAME, false);
        return file;
    }

    public static File getRootFolder() {
        File file = InstalledFileLocator.getDefault().locate(STANDARD_LIBRARY_ROOT_PATH, STANDARD_LIBRARY_CODE_BASE_NAME, false);
        return file;
    }

    private static void addPlugins(AbstractVirtualMachine machine) {
        DefaultPluginLoader loader = new DefaultPluginLoader();
        loader.loadIntoVirtualMachine(machine);
        loader.checkConsistency(machine);
    }

    protected void createVM() {
        vmSingleton();
    }

    /**
     * Obtains the current Virtual Machine in use.
     *
     * @return
     */
    public static AbstractVirtualMachine getVirtualMachine() {
        vmSingleton();
        return virtualMachine;
    }

    /**
     * Returns the file extension used by this compiler.
     * 
     * @return
     */
    public String getSourceFileExtension() {
        return "hop";
    }

    @Override
    public CompilerFileDescriptor getFileDescriptor(FileObject file) {
        if (file != null) {
            File ioFile = org.openide.filesystems.FileUtil.toFile(file);
            FileDescriptor fd = virtualMachine.getSymbolTable().getFileDescriptor(ioFile.getAbsolutePath());
            if (fd == null && !hasBuiltAllOnce()) {
                compile();
                fd = virtualMachine.getSymbolTable().getFileDescriptor(ioFile.getAbsolutePath());
            }
            HopFileDescriptor cfd = new HopFileDescriptor();
            cfd.setFileDescriptor(fd);
            return cfd;
        } else {
            return null;
        }
    }

    @Override
    public Iterator<CompilerFileDescriptor> getFileDescriptors() {
        Iterator<FileDescriptor> files = virtualMachine.getSymbolTable().getFileDescriptors();
        HopFileDescriptorIterator it = new HopFileDescriptorIterator();
        it.setFiles(files);
        return it;
    }

    @Override
    public CompilerErrorManager getCompilerErrorManager() {
        return compilerErrorManager;
    }

    @Override
    public CompilerDataStorage getDataStorage() {
        return data;
    }

    public CompilerCodeCompletionResults requestCodeCompletionResult(CompilerCodeCompletionRequest request) {
        HopCompilerCodeCompletionResults results = new HopCompilerCodeCompletionResults();
        CodeCompletionRequest req = new CodeCompletionRequest();
        req.setFileKey(request.getFileKey());
        req.setLine(request.getLine());
        req.setLineNumber(request.getLineNumber());
        req.setStartOffset(request.getStartOffset());
        CodeCompletionResult result = this.virtualMachine.requestCodeCompletionResult(req);
        results.setVirtualMachineResults(result);
        return results;
    }
}
