package unipi.virtuallab.data;

/**
 *
 * @author root
 */
public abstract class OutputBlock {

    /** Creates a new instance of OutputBlock */
    public OutputBlock(String name) {
        BlockName = name;
    }

    public String getBlockName() {
        return BlockName;
    }

    public void setBlockName(String name) {
        BlockName = name;
    }

    String BlockName;
}
