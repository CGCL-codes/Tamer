package com.sun.tools.attach;

/**
 * When a {@link java.lang.SecurityManager SecurityManager} set, this
 * is the permission which will be checked when code invokes {@link
 * VirtualMachine#attach VirtalMachine.attach} to attach to a target virtual 
 * machine.
 * This permission is also checked when an {@link
 * com.sun.tools.attach.spi.AttachProvider AttachProvider} is created. </p>
 * 
 * <p> An <code>AttachPermission</code> object contains a name (also referred
 * to as a "target name") but no actions list; you either have the
 * named permission or you don't.
 * The following table provides a summary description of what the
 * permission allows, and discusses the risks of granting code the
 * permission.
 * <P>
 * <table border=1 cellpadding=5 summary="Table shows permission
 * target name, what the permission allows, and associated risks">
 * <tr>
 * <th>Permission Target Name</th>
 * <th>What the Permission Allows</th>
 * <th>Risks of Allowing this Permission</th>
 * </tr>
 *
 * <tr>
 *   <td>attachVirtualMachine</td>
 *   <td>Ability to attach to another Java virtual machine and load agents
 *       into that VM.
 *   </td>
 *   <td>This allows an attacker to control the target VM which can potentially
 *       cause it to misbehave.
 *   </td>
 * </tr>
 *
 * <tr>
 *   <td>createAttachProvider</td>
 *   <td>Ability to create an <code>AttachProvider</code> instance.
 *   </td>
 *   <td>This allows an attacker to create an AttachProvider which can 
 *       potentially be used to attach to other Java virtual machines.
 *   </td>
 * </tr>

 *
 * </table>
 
 * <p>
 * Programmers do not normally create AttachPermission objects directly.
 * Instead they are created by the security policy code based on reading
 * the security policy file.
 *
 * @see com.sun.tools.attach.VirtualMachine
 * @see com.sun.tools.attach.spi.AttachProvider
 */
public final class AttachPermission extends java.security.BasicPermission {

    /** use serialVersionUID for interoperability */
    static final long serialVersionUID = -4619447669752976181L;

    /**
     * Constructs a new AttachPermission object.
     *
     * @param name Permission name. Must be either "attachVirtualMachine",
     *             or "createAttachProvider".
     *
     * @throws NullPointerException if name is <code>null</code>.
     * @throws IllegalArgumentException if the name is invalid.
     */
    public AttachPermission(String name) {
        super(name);
        if (!name.equals("attachVirtualMachine") && !name.equals("createAttachProvider")) {
            throw new IllegalArgumentException("name: " + name);
        }
    }

    /**
     * Constructs a new AttachPermission object.
     *
     * @param name Permission name.   Must be either "attachVirtualMachine",
     *             or "createAttachProvider".
     *
     * @param actions Not used and should be <code>null</code>, or
     *                the empty string.
     *
     * @throws NullPointerException if name is <code>null</code>.
     * @throws IllegalArgumentException if arguments are invalid.
     */
    public AttachPermission(String name, String actions) {
        super(name);
        if (!name.equals("attachVirtualMachine") && !name.equals("createAttachProvider")) {
            throw new IllegalArgumentException("name: " + name);
        }
        if (actions != null && actions.length() > 0) {
            throw new IllegalArgumentException("actions: " + actions);
        }
    }
}
