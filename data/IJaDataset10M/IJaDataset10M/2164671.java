package deduced.javagenerator.model.implementation;

import java.util.Iterator;
import java.util.List;
import deduced.generator.AbstractClassGenerator;
import deduced.generator.InstanceData;
import deduced.generator.codeblock.CodeBlock;
import deduced.javaconverter.JavaModelImplementationConverter;
import deduced.javagenerator.*;
import deduced.javagenerator.codeblock.AbstractMethodCodeBlock;

/**
 * @author DDuff
 */
public class ArgumentConstructorCodeBlock extends AbstractMethodCodeBlock {

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.CodeBlockWriter#openCodeBlock(deduced.generator.codeblock.CodeBlock,
     *      deduced.generator.AbstractClassGenerator)
     */
    public void openCodeBlock(CodeBlock codeBlock, AbstractClassGenerator classGenerator) {
        configureCodeBlock(codeBlock, classGenerator);
        JavaConstructor constructor = (JavaConstructor) getMethod();
        StringBuffer classContent = classGenerator.getClassContent();
        classContent.append("super(");
        classContent.append(");");
        List classInstanceList = getRecursiveInstanceList();
        Iterator parameterIterator = constructor.getParameterList().iteratorByValue();
        Iterator it = classInstanceList.iterator();
        while (it.hasNext()) {
            InstanceData instance = (InstanceData) it.next();
            MethodParameter parameter = (MethodParameter) parameterIterator.next();
            printInstanceAssign(classGenerator, instance, parameter);
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @param parameter
     * @param javaClass
     * 
     * @see deduced.javagenerator.JavaCodeBlockGenerator#generateCodeBlock(deduced.javagenerator.JavaClassGenerator,
     *      java.lang.Object)
     */
    public void printInstanceAssign(AbstractClassGenerator generator, InstanceData instance, MethodParameter parameter) {
        generator.addLineBreak();
        JavaMethod setterMethod = (JavaMethod) getConversionModel().findConvertedObject(instance, JavaModelImplementationConverter.SETTER_METHOD_INSTANCE.getKey(), getJavaClass());
        StringBuffer classContent = generator.getClassContent();
        classContent.append(setterMethod.getName());
        classContent.append("(");
        classContent.append(parameter.getName());
        classContent.append(");");
    }
}
