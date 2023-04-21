package net.simplemodel.core.generator;

import net.simplemodel.core.ast.SMModuleDeclaration;
import org.eclipse.core.runtime.CoreException;

public interface IGenerator {

    IGenerationResult generate(IGeneratorContext context, SMModuleDeclaration module) throws CoreException;
}
