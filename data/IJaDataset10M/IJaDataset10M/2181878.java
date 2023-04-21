package jauntecore.readclass;

/**
 * Class to assist reading of Class Access Flags 
 * @author Adam Gashlin
 *
 */
public class ClassAccessFlags {

    short flags;

    /**
	 * create a flags object from the given bitfield
	 * @param flags
	 */
    public ClassAccessFlags(short flags) {
        this.flags = flags;
    }

    /**
	 * is this class public?
	 * @return
	 */
    public boolean isPublic() {
        return (flags & 0x0001) != 0;
    }

    /**
	 * is this class final?
	 * @return
	 */
    public boolean isFinal() {
        return (flags & 0x0010) != 0;
    }

    /**
	 * does this class use new invokespecial semantics?<p>all should have this set
	 * @return
	 */
    public boolean isSuper() {
        return (flags & 0x0020) != 0;
    }

    /**
	 * is this an interface?
	 * @return
	 */
    public boolean isInterface() {
        return (flags & 0x200) != 0;
    }

    /**
	 * is this an abstract class?
	 * @return
	 */
    public boolean isAbstract() {
        return (flags & 0x0400) != 0;
    }

    /**
	 * get the raw flags bitfield
	 * @return
	 */
    public short getFlags() {
        return flags;
    }

    /**
	 * produce a handy textual representation of the set flags
	 */
    public String toString() {
        StringBuilder sb = new StringBuilder(" ");
        if (isPublic()) sb.append("public ");
        if (isFinal()) sb.append("final ");
        if (isSuper()) sb.append("super ");
        if (isInterface()) sb.append("interface ");
        if (isAbstract()) sb.append("abstract ");
        return sb.toString();
    }
}
