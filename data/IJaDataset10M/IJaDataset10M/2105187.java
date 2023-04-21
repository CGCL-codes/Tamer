package com.google.gxp.compiler.base;

/**
 * Visitor for {@link Import}s.
 *
 * @param <T> return type of visitor
 */
public interface ImportVisitor<T> {

    T visitClassImport(ClassImport imp);

    T visitPackageImport(PackageImport imp);

    T visitCppFileImport(CppFileImport imp);

    T visitCppLibraryImport(CppLibraryImport imp);
}
