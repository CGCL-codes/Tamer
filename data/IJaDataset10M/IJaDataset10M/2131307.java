package org.lwjgl.opencl;

/**
 * Instances of this class can be used to receive OpenCL program compilation notifications.
 * A single CLCompileProgramCallback instance should only be used with programs created
 * in the same CLContext.
 *
 * @author Spasi
 */
public abstract class CLCompileProgramCallback extends CLProgramCallback {
}
