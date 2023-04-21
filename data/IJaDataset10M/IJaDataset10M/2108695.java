package org.peaseplate.scriptengine;

import java.nio.charset.Charset;
import org.peaseplate.utils.exception.CompileException;

public interface ScriptCompiler {

    Script load(String name) throws CompileException;

    Script load(String name, Charset charset) throws CompileException;

    /**
	 * Creates a script from the specified string.
	 * 
	 * @param source the script to compile
	 * @return the script
	 * @throws CompileException on occasion, well formatted
	 */
    public Script compile(String source) throws CompileException;

    /**
	 * Creates a script from the specified char array.
	 * 
	 * @param source the script to compile
	 * @return the script
	 * @throws CompileException on occasion, well formatted
	 */
    public Script compile(char[] source) throws CompileException;

    /**
	 * Creates a script from the specified char array. If the offset is > 0, you should set the initial line and column
	 * to provide error handling with correct locations
	 * 
	 * @param initialLine
	 * @param initialColumnchar
	 * @param initialLine the initial line, 1 by default
	 * @param initialColumn the initial column, 1 by default
	 * @param source the script to compile
	 * @param offset the offset
	 * @param length the length
	 * @return the script
	 * @throws CompileException on occasion, well formatted
	 */
    public Script compile(int initialLine, int initialColumn, char[] source, int offset, int length) throws CompileException;
}
