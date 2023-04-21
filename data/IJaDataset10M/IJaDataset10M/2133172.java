package net.maizegenetics.jGLiM;

/**
 * Created using IntelliJ IDEA.
 * Author: Peter Bradbury
 * Date: Dec 29, 2004
 * Time: 9:33:56 AM
 */
public interface Level extends Comparable<Level> {

    int getNumberOfSublevels();

    Comparable getSublevel(int sublevel);

    Comparable[] getSublevels();

    boolean contains(Comparable sublevel);
}
