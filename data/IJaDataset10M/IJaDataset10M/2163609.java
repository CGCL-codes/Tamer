package org.openconcerto.modules.common.http;

import java.io.File;
import java.io.IOException;
import org.openconcerto.erp.modules.AbstractModule;
import org.openconcerto.erp.modules.ModuleFactory;
import org.openconcerto.erp.modules.ModulePackager;
import org.openconcerto.utils.FileUtils;

public final class Module extends AbstractModule {

    public Module(ModuleFactory f) throws IOException {
        super(f);
    }

    @Override
    protected void start() {
    }

    @Override
    protected void stop() {
    }

    public static void main(String[] args) throws IOException {
        final File propsFile = new File("module.properties");
        final File distDir = new File("dist");
        FileUtils.mkdir_p(distDir);
        final ModulePackager modulePackager = new ModulePackager(propsFile, new File("bin/"));
        modulePackager.addJarsFromDir(new File("lib"));
        modulePackager.writeToDir(distDir);
        modulePackager.writeToDir(new File("../OpenConcerto/Modules"));
    }
}
