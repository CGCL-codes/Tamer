package org.perlipse.internal.validators.compiler;

import org.eclipse.dltk.validators.core.AbstractValidatorType;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidator;
import org.perlipse.core.PerlCoreConstants;

public class PerlCompilerFactory extends AbstractValidatorType {

    private static String COMPILER_ID = "org.perlipse.core.perlCompiler";

    @SuppressWarnings("unchecked")
    public PerlCompilerFactory() {
        validators.put(COMPILER_ID, createValidator(COMPILER_ID));
    }

    public IValidator createValidator(String id) {
        return new PerlCompiler(id, getName(), this);
    }

    public String getID() {
        return COMPILER_ID;
    }

    public String getName() {
        return "Perl Compiler";
    }

    public String getNature() {
        return PerlCoreConstants.NATURE_ID;
    }

    public boolean isBuiltin() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean supports(Class validatorType) {
        return ISourceModuleValidator.class.equals(validatorType);
    }
}
