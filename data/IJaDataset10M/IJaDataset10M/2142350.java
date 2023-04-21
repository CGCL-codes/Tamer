package org.jmlspecs.jir.util;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.Type;
import org.jmlspecs.jir.ast.external.JirAstNodeConverter;
import org.jmlspecs.jir.ast.jdt.dom.JdtAstConverterHelper;
import org.jmlspecs.jir.binding.IBindingManager;
import org.jmlspecs.jir.info.external.JirBytecodeProcessor;

public class DomJirBytecodeProcessor extends JirBytecodeProcessor<Expression, Type> {

    public DomJirBytecodeProcessor(final IBindingManager<Expression, Type> bndMan) {
        super(new JirAstNodeConverter<Expression, org.eclipse.jdt.core.dom.Type>(new JdtAstConverterHelper(), bndMan));
    }
}
