package gawky.message.part;

/**
 * Fixed length Part
 * 
 * @author Ingo Harbeck
 *
 */
public class DescF extends Desc {

    public DescF(char format, char code, int len, String name) {
        super(format, code, len, name);
    }

    public DescF(char format, int len, String name) {
        super(format, Desc.CODE_R, len, name);
    }

    public DescF(int len, String name) {
        super(Desc.FMT_A, Desc.CODE_R, len, name);
    }
}
