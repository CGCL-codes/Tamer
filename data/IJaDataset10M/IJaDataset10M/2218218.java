package net.community.chest.javaagent.dumper.filter;

/**
 * <P>Copyright as per GPLv2</P>
 * @author Lyor G.
 * @since Aug 11, 2011 2:20:37 PM
 */
public interface ClassFilter {

    boolean accept(String className);
}
