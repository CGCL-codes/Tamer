package org.fernwood.jbasic.funcs;

import org.fernwood.jbasic.Status;
import org.fernwood.jbasic.compiler.CompileContext;
import org.fernwood.jbasic.runtime.ArgumentList;
import org.fernwood.jbasic.runtime.ByteCode;
import org.fernwood.jbasic.runtime.JBasicException;
import org.fernwood.jbasic.runtime.SimpleCipher;
import org.fernwood.jbasic.runtime.SymbolTable;
import org.fernwood.jbasic.value.Value;

/**
 * <b>CIPHER()</b> JBasic Function
 * <p>
 * <table>
 * <tr><td><b>Description:</b></td><td>Encrypt a string</td></tr>
 * <tr><td><b>Invocation:</b></td><td><code>s1 = CIPHER( s [, p] )</code></td></tr>
 * <tr><td><b>Returns:</b></td><td>String</td></tr>
 * </table>
 * <p>
 * Encrypt a string s using a salted password cipher.  If a specific password
 * is to be used, it is the second parameter.  If not supplied, a default 
 * internal password is used.  The same string s will always be encrypted to the
 * same resulting string s1, given the same value for p.
 * <p>
 * Use the DECIPHER() function to decrypt the string, using the same password value.
 * <p>
 * This function uses the java.crypt package, but only uses the moderately secure
 * password encryption functions.  As a result, this encryption scheme does not
 * fall into the category of cryptography export restrictions.
 * @author cole
 */
public class CipherFunction extends JBasicFunction {

    /**
	 * Compile the CIPHER function.  This involves a call to the CIPHER runtime
	 * function, followed by a conversion of the encoded integer array to a
	 * string representation.
	 * @param work the context of the compilation operation
	 * @return Status indicating that the function was compiled successfully.
	 * @throws JBasicException if an argument or execution error occurs
	 */
    public Status compile(final CompileContext work) throws JBasicException {
        work.validate(1, 2);
        work.byteCode.add(ByteCode._CALLF, work.argumentCount, "CIPHER");
        work.byteCode.add(ByteCode._CALLF, 1, "CRYPTSTR");
        return new Status();
    }

    /**
	 * Runtime execution of the function via _CALLF
	 * 
	 * @param argList the function argument list and count already 
	 * popped from the runtime data stack
	 * @param symbols the currently active symbol table
	 * @return a Value containing the function result.
	 * @throws JBasicException if an argument or execution error occurs
	 */
    public Value run(final ArgumentList argList, final SymbolTable symbols) throws JBasicException {
        argList.validate(1, 2, new int[] { Value.STRING, Value.STRING });
        SimpleCipher cipher = new SimpleCipher(argList.session);
        if (argList.size() == 2) cipher.setPassword(argList.stringElement(1)); else {
            SymbolTable globals = argList.session.globals();
            Value dfpw;
            try {
                dfpw = globals.reference("SYS$SECRET");
            } catch (JBasicException e) {
                dfpw = null;
            }
            cipher.setPassword(dfpw);
        }
        return cipher.encrypt(argList.element(0));
    }
}
