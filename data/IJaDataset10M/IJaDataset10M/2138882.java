package clump.kernel;

public interface IFunctionInstance extends IData {

    /**
     * Invoke the method available for selected type parameters
     *
     * @param specialisations Type specialisations
     * @param parameters      Parameters to be used for constructor selection
     * @return A data
     * @throws clump.kernel.exception.NativeRuntimeException
     *          Thrown by the code or by the kernel
     */
    IData apply(IType[] specialisations, IData[] parameters) throws clump.kernel.exception.NativeRuntimeException;
}
