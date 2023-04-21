package deduced.javagenerator.codeblock;

import deduced.generator.AbstractClassGenerator;
import deduced.generator.DefaultCodeBlockWriter;
import deduced.generator.codeblock.CodeBlock;
import deduced.javagenerator.JavaMethod;

/**
 * <p>
 * Title: DefineCodeBlock
 * </p>
 * <p>
 * Description: DefineCodeBlock
 * </p>
 */
public class InterfaceMethodCodeBlock extends DefaultCodeBlockWriter {

    /**
     * (non-Javadoc)
     * 
     * @see deduced.generator.CodeBlockWriter#openCodeBlock(deduced.generator.codeblock.CodeBlock,
     *      deduced.generator.AbstractClassGenerator)
     */
    public void openCodeBlock(CodeBlock codeBlock, AbstractClassGenerator classGenerator) {
        JavaMethod javaMethod = (JavaMethod) codeBlock.getLinkedObject();
        MethodCodeBlock.printMethodHeader(classGenerator, javaMethod);
        classGenerator.getClassContent().append(";");
        classGenerator.addLineBreak();
    }
}
