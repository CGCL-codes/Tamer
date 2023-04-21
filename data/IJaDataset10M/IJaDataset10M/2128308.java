package clump.language.compilation.generator.common;

import clump.language.analysis.EntityUtils;
import clump.language.analysis.IEnvironment;
import clump.language.analysis.IPackageDefinition;
import clump.language.analysis.PackageUtils;
import clump.language.analysis.ResourceUtils;
import clump.language.analysis.exception.EntityCannotBeSolvedException;
import clump.language.analysis.impl.PackageDefinition;
import clump.language.ast.IEntity;
import clump.language.ast.specification.expression.impl.TopEntity;
import clump.language.compilation.generator.exception.CompilationException;
import clump.language.compilation.typechecker.exception.TypeCheckerException;
import opala.lexing.ILocation;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class PackageCompiler extends Compiler<IPackageDefinition, Void> {

    private final PrintStream outStream;

    private final boolean verbose;

    private final boolean debug;

    private final File classDirectory;

    private void printVerboseInformation(String information) {
        if (verbose) {
            System.err.println(information);
        }
    }

    private void printStackTraceOnDemand(Throwable e) {
        if (this.debug) {
            if (e.getCause() != null) {
                printStackTraceOnDemand(e.getCause());
            } else {
                e.printStackTrace();
            }
        }
        if (this.verbose) {
            System.exit(1);
        }
    }

    public PackageCompiler(File classDirectory, PrintStream outStream, boolean verbose, boolean debug) {
        super();
        this.outStream = outStream;
        this.verbose = verbose;
        this.debug = debug;
        this.classDirectory = classDirectory;
    }

    private void dumpEntityCode(IEntity definition) throws CompilationException {
        if (definition.equals(TopEntity.SINGLETON)) {
        } else {
            File resourceDirName = PackageUtils.getPackagePathName(classDirectory, definition.getPackageDefinition());
            if (resourceDirName.mkdirs() == false && resourceDirName.isDirectory() == false) {
                throw new CompilationException(resourceDirName + ": not a directory").setLocation(definition.getLocation());
            }
            try {
                ResourceUtils.writeToFile(ResourceUtils.getResourceFileName(resourceDirName, definition), definition);
            } catch (IOException e) {
                throw new CompilationException(e.getMessage()).setLocation(definition.getLocation());
            }
        }
    }

    public void compilationUnit(PackageDefinition definition) throws TypeCheckerException, EntityCannotBeSolvedException, CompilationException {
        final IEnvironment environment = definition.getEnvironment();
        final String pathName = EntityUtils.getPathName(definition.getPath());
        for (IEntity entity : environment.getEntities()) {
            try {
                long t0 = System.currentTimeMillis();
                this.dumpEntityCode(entity);
                this.printVerboseInformation("{Symbol} " + pathName + entity.getSpecification() + " [" + entity.getLocation() + "] in " + (System.currentTimeMillis() - t0) + "ms");
            } catch (CompilationException e) {
                final ILocation location = e.getLocation() != null ? e.getLocation() : entity.getSpecification().getLocation();
                this.outStream.println("# Symbol generation error " + location);
                this.outStream.println("# in " + entity.getDefinitionKind() + " " + entity.getSpecification() + ": " + e.getMessage());
                this.outStream.println();
                this.printStackTraceOnDemand(e);
            }
        }
        for (IPackageDefinition packageDefinition : definition.getSubPackages()) {
            this.compile(packageDefinition);
        }
    }
}
