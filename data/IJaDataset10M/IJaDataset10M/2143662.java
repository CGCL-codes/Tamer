package ideah.run;

import com.intellij.execution.configurations.SimpleProgramParameters;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

final class HaskellParameters extends SimpleProgramParameters {

    private Sdk ghc;

    private String mainFile;

    private String rtFlags;

    public Sdk getGhc() {
        return ghc;
    }

    public void setGhc(Sdk ghc) {
        this.ghc = ghc;
    }

    public String getMainFile() {
        return mainFile;
    }

    public void setMainFile(String mainFile) {
        this.mainFile = mainFile;
    }

    public String getRuntimeFlags() {
        return rtFlags;
    }

    public void setRuntimeFlags(String rtFlags) {
        this.rtFlags = rtFlags;
    }

    public void configureByModule(@NotNull Module module) {
        setGhc(getModuleGhc(module));
    }

    public static Sdk getModuleGhc(@NotNull Module module) {
        Sdk ghc = ModuleRootManager.getInstance(module).getSdk();
        if (ghc == null) return null;
        VirtualFile homeDirectory = ghc.getHomeDirectory();
        if (homeDirectory == null || !homeDirectory.isValid()) return null;
        return ghc;
    }
}
