package repast.simphony.agents.model.codegen;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import repast.simphony.agents.base.Util;

/**
 * Class CodeBlock TODO
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class CodeBlock extends DepthCounter implements ISrcStructure {

    private List codeFragments = new ArrayList();

    private boolean endWithNewLine = true;

    private boolean encloseBlock = true;

    /**
	 * Adds a code fragment of this block
	 * 
	 * @param codeFragment
	 */
    public void addFragment(ISrcStructure codeFragment) {
        codeFragments.add(codeFragment);
    }

    /**
	 * Serializes the block's contents to the given writer.
	 * 
	 * @see repast.simphony.agents.model.codegen.structure.ISrcStructure#serialize(java.io.BufferedWriter)
	 */
    public void serialize(Writer writer) throws IOException {
        if (this.encloseBlock) {
            writer.write(" {");
            writer.write(Util.getPlatformLineDelimiter());
            writer.write(Util.getPlatformLineDelimiter());
        }
        for (Iterator iter = codeFragments.iterator(); iter.hasNext(); ) {
            ISrcStructure fragment = (ISrcStructure) iter.next();
            fragment.serialize(writer);
        }
        if (this.encloseBlock) {
            writer.write(Util.getPlatformLineDelimiter());
            this.decrementDepth();
            this.writeBlanks(writer);
            writer.write("}");
            if (this.endWithNewLine) writer.write(Util.getPlatformLineDelimiter());
            this.incrementDepth();
        }
    }

    public boolean isEndWithNewLine() {
        return endWithNewLine;
    }

    public void setEndWithNewLine(boolean endWithNewLine) {
        this.endWithNewLine = endWithNewLine;
    }

    public boolean isEncloseBlock() {
        return encloseBlock;
    }

    public void setEncloseBlock(boolean encloseBlock) {
        this.encloseBlock = encloseBlock;
    }
}
