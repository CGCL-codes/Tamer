package cloudspace.vm.javassist;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.expr.Expr;
import cloudspace.vm.javassist.AbstractCommand.SignatureType;

/**
 * The Interface CommandInterface. This provides access to all of the fields in
 * a command.
 */
public interface CommandInterface {

    /**
     * This uses a command to translate an expression call.
     * 
     * @param call
     *            the expresison call to be translated
     * @param expressionName
     *            the name of the expression. This is used to identify the
     *            correct command.
     * 
     * @throws MalformedCommandException
     *             the malformed command exception
     * @throws CannotCompileException
     *             the cannot compile exception
     */
    public void translate(Expr call, boolean isSuper) throws MalformedCommandException, CannotCompileException;

    /**
     * This translates a method call.
     * 
     * @param member
     *            the member
     * @param expressionName
     *            the signature used to identify the method call.
     * 
     * @throws MalformedCommandException
     *             the malformed command exception
     * @throws CannotCompileException
     *             the cannot compile exception
     */
    public void translate(CtMethod member, String expressionName) throws MalformedCommandException, CannotCompileException;

    public void translate(CtClass clazz) throws MalformedCommandException, CannotCompileException;

    /**
     * This gets the replacement text from the command.
     * 
     * @return the replacement
     */
    public String getReplacement();

    /**
     * Gets the method signature from the command.
     * 
     * @return the signature
     */
    public String getSignature();

    /**
     * Gets the type of signature from the command.
     * 
     * @return the signature type
     */
    public SignatureType getSignatureType();

    /**
     * gives a string representation of the command. This is simply a one line
     * string directly from the javassist config.
     * 
     * @return the string
     */
    public String toString();
}
