package huf.data.compare;

import java.io.File;

/**
 * Comparator which compares instances of Files using their <code>getName()</code> method.
 */
public class FileNameComparator implements IComparator<File> {

    private static final long serialVersionUID = -5157870519631397983L;

    public FileNameComparator() {
    }

    private final StringComparator comp = new StringComparator();

    /**
	 * Compare objects <code>first</code> and <code>second</code> and return the result.
	 *
	 * @param first  first object to be compared
	 * @param second second object to be compared
	 * @return       {@link #LESSER LESSER} if first object is <i>lesser</i> than second one,<br/>
	 *               {@link #EQUAL EQUAL} if first object is <i>equal</i> to second one or<br/>
	 *               {@link #GREATER GREATER} if first object is <i>greater</i> than second one
	 */
    @Override
    public int compare(File first, File second) {
        return comp.compare(first == null ? null : first.getName(), second == null ? null : second.getName());
    }
}
