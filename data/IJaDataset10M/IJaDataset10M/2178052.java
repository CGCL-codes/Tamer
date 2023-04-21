package jpcsp.Allegrex.compiler;

/**
 * @author gid15
 *
 */
public class RecompileExecutable implements IExecutable {

    private CodeBlock codeBlock;

    public RecompileExecutable(CodeBlock codeBlock) {
        this.codeBlock = codeBlock;
    }

    @Override
    public int exec(int returnAddress, int alternativeReturnAddress, boolean isJump) throws Exception {
        int newInstanceIndex = codeBlock.getNewInstanceIndex();
        IExecutable executable = Compiler.getInstance().compile(codeBlock.getStartAddress(), newInstanceIndex);
        codeBlock.getExecutable().setExecutable(executable);
        return executable.exec(returnAddress, alternativeReturnAddress, isJump);
    }

    @Override
    public void setExecutable(IExecutable e) {
    }
}
