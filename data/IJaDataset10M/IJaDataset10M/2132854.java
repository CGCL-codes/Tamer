package net.sourceforge.nrl.parser.ast;

/**
 * A reference to an operator file, by file name
 * 
 * @author Christian Nentwich
 */
public interface IOperatorFileReference extends INRLAstNode {

    /**
	 * Return the file name. Can be absolute or relative.
	 * 
	 * @return the file name
	 */
    public String getFileName();

    /**
	 * Returns true if the file name is absolute, i.e. starts from a root
	 * directory. If false, it is relative to the rule file.
	 * 
	 * @return true if the file name is absolute, false otherwise
	 */
    public boolean isAbsolute();
}
