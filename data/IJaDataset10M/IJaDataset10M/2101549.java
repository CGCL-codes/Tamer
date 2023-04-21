package org.jmlspecs.eclipse.jdt.core.tests.checker;

import org.jmlspecs.annotation.Model;
import org.jmlspecs.annotation.NonNull;
import org.jmlspecs.annotation.NonNullByDefault;
import org.jmlspecs.annotation.Nullable;

/**
 * This class represents a compiled version of a .jml file.
 */
@NonNullByDefault
public abstract class JirSpecTestHelper1$jir {

    @Model
    @NonNull
    public Object oModel;

    @NonNull
    public Object oNotModel;

    @NonNull
    public Integer iDeclaredNonNull;

    public Integer iImplicitlyNonNull;

    @Nullable
    public Integer iDeclaredNullable;

    @Nullable
    public static Integer siDeclaredNullable;

    public JirSpecTestHelper1$jir(@NonNull Integer i) {
        throw new RuntimeException();
    }

    @Nullable
    public Integer plus(@NonNull Integer i, @Nullable Integer j) {
        return 0;
    }

    public Integer inc(Integer i) {
        throw new RuntimeException();
    }
}
