package org.jikesrvm.compilers.baseline.ppc;

import org.jikesrvm.VM;
import org.jikesrvm.classloader.MethodReference;
import org.jikesrvm.classloader.NormalMethod;
import org.jikesrvm.classloader.TypeReference;
import org.jikesrvm.compilers.baseline.BaselineCompiledMethod;
import org.jikesrvm.compilers.baseline.ReferenceMaps;
import org.jikesrvm.compilers.common.CompiledMethod;
import org.jikesrvm.compilers.common.CompiledMethods;
import org.jikesrvm.mm.mminterface.GCMapIterator;
import org.jikesrvm.ppc.BaselineConstants;
import org.jikesrvm.runtime.DynamicLink;
import org.jikesrvm.runtime.Magic;
import org.vmmagic.pragma.Uninterruptible;
import org.vmmagic.unboxed.Address;
import org.vmmagic.unboxed.Offset;
import org.vmmagic.unboxed.WordArray;

/**
 * Iterator for stack frame  built by the Baseline compiler
 * An Instance of this class will iterate through a particular
 * reference map of a method returning the offsets of any refereces
 * that are part of the input parameters, local variables, and
 * java stack for the stack frame.
 */
@Uninterruptible
public abstract class BaselineGCMapIterator extends GCMapIterator implements BaselineConstants {

    private int mapIndex;

    private int mapId;

    private ReferenceMaps maps;

    private DynamicLink dynamicLink;

    private MethodReference bridgeTarget;

    private NormalMethod currentMethod;

    private BaselineCompiledMethod currentCompiledMethod;

    private int currentNumLocals;

    private TypeReference[] bridgeParameterTypes;

    private boolean bridgeParameterMappingRequired;

    private boolean bridgeRegistersLocationUpdated;

    private boolean finishedWithRegularMap;

    private int bridgeParameterInitialIndex;

    private int bridgeParameterIndex;

    private int bridgeRegisterIndex;

    private Address bridgeRegisterLocation;

    private Address bridgeSpilledParamLocation;

    public BaselineGCMapIterator(WordArray registerLocations) {
        this.registerLocations = registerLocations;
        dynamicLink = new DynamicLink();
    }

    public void setupIterator(CompiledMethod compiledMethod, Offset instructionOffset, Address fp) {
        currentCompiledMethod = (BaselineCompiledMethod) compiledMethod;
        currentMethod = (NormalMethod) compiledMethod.getMethod();
        currentNumLocals = currentMethod.getLocalWords();
        framePtr = fp;
        maps = currentCompiledMethod.referenceMaps;
        mapId = maps.locateGCPoint(instructionOffset, currentMethod);
        mapIndex = 0;
        if (mapId < 0) {
            ReferenceMaps.jsrLock.lock();
            int JSRindex = maps.setupJSRSubroutineMap(mapId);
            while (JSRindex != 0) {
                Address nextCallerAddress;
                short location = convertIndexToLocation(JSRindex);
                if (BaselineCompilerImpl.isRegister(location)) {
                    nextCallerAddress = registerLocations.get(location).toAddress();
                } else {
                    nextCallerAddress = framePtr.plus(BaselineCompilerImpl.locationToOffset(location) - BYTES_IN_ADDRESS);
                }
                nextCallerAddress = nextCallerAddress.loadAddress();
                Offset nextMachineCodeOffset = compiledMethod.getInstructionOffset(nextCallerAddress);
                if (VM.TraceStkMaps) {
                    VM.sysWriteln("     setupJSRsubroutineMap- nested jsrs end of loop- = ");
                    VM.sysWriteln("      next jsraddress offset = ", JSRindex);
                    VM.sysWriteln("      next callers address = ", nextCallerAddress);
                    VM.sysWriteln("      next machinecodeoffset = ", nextMachineCodeOffset);
                    if (nextMachineCodeOffset.sLT(Offset.zero())) {
                        VM.sysWriteln("BAD MACHINE CODE OFFSET");
                    }
                }
                JSRindex = maps.getNextJSRAddressIndex(nextMachineCodeOffset, currentMethod);
            }
        }
        if (VM.TraceStkMaps) {
            VM.sysWrite("BaselineGCMapIterator setupIterator mapId = ");
            VM.sysWrite(mapId);
            VM.sysWrite(" for ");
            VM.sysWrite(currentMethod);
            VM.sysWrite(".\n");
        }
        bridgeTarget = null;
        bridgeParameterTypes = null;
        bridgeParameterMappingRequired = false;
        bridgeRegistersLocationUpdated = false;
        bridgeParameterIndex = 0;
        bridgeRegisterIndex = 0;
        bridgeRegisterLocation = Address.zero();
        bridgeSpilledParamLocation = Address.zero();
        if (currentMethod.getDeclaringClass().hasDynamicBridgeAnnotation()) {
            fp = Magic.getCallerFramePointer(fp);
            Address ip = Magic.getNextInstructionAddress(fp);
            int callingCompiledMethodId = Magic.getCompiledMethodID(fp);
            CompiledMethod callingCompiledMethod = CompiledMethods.getCompiledMethod(callingCompiledMethodId);
            Offset callingInstructionOffset = callingCompiledMethod.getInstructionOffset(ip);
            callingCompiledMethod.getDynamicLink(dynamicLink, callingInstructionOffset);
            bridgeTarget = dynamicLink.methodRef();
            bridgeParameterInitialIndex = dynamicLink.isInvokedWithImplicitThisParameter() ? -1 : 0;
            bridgeParameterTypes = bridgeTarget.getParameterTypes();
        }
        reset();
    }

    public void reset() {
        mapIndex = 0;
        finishedWithRegularMap = false;
        if (bridgeTarget != null) {
            bridgeParameterMappingRequired = true;
            bridgeParameterIndex = bridgeParameterInitialIndex;
            bridgeRegisterIndex = FIRST_VOLATILE_GPR;
            bridgeRegisterLocation = framePtr.loadAddress();
            bridgeRegisterLocation = bridgeRegisterLocation.minus(BYTES_IN_DOUBLE * (LAST_NONVOLATILE_FPR - FIRST_VOLATILE_FPR + 1) + BYTES_IN_ADDRESS * (LAST_NONVOLATILE_GPR - FIRST_VOLATILE_GPR + 1));
            Address callersFP = Magic.getCallerFramePointer(framePtr);
            bridgeSpilledParamLocation = callersFP.plus(STACKFRAME_HEADER_SIZE);
        }
    }

    /**
   * given a index in the local area (biased : local0 has index 1)
   *   this routine determines the correspondig offset in the stack
   */
    public short convertIndexToLocation(int index) {
        if (index == 0) return 0;
        if (index <= currentNumLocals) {
            return currentCompiledMethod.getGeneralLocalLocation(index - 1);
        } else {
            return currentCompiledMethod.getGeneralStackLocation(index - 1 - currentNumLocals);
        }
    }

    /**
   * Get location of next reference.
   * A zero return indicates that no more references exist.
   */
    public Address getNextReferenceAddress() {
        if (!finishedWithRegularMap) {
            if (mapId < 0) {
                mapIndex = maps.getNextJSRRefIndex(mapIndex);
            } else {
                mapIndex = maps.getNextRefIndex(mapIndex, mapId);
            }
            if (VM.TraceStkMaps) {
                VM.sysWrite("BaselineGCMapIterator getNextReferenceIndex = ");
                VM.sysWrite(mapIndex);
                VM.sysWrite(".\n");
                if (mapId < 0) {
                    VM.sysWrite("Index is a JSR return address ie internal pointer.\n");
                }
            }
            if (mapIndex != 0) {
                short location = convertIndexToLocation(mapIndex);
                if (VM.TraceStkMaps) {
                    VM.sysWrite("BaselineGCMapIterator getNextReference location = ");
                    VM.sysWrite(location);
                    VM.sysWriteln();
                }
                Address nextCallerAddress;
                if (BaselineCompilerImpl.isRegister(location)) {
                    nextCallerAddress = registerLocations.get(location).toAddress();
                } else {
                    nextCallerAddress = framePtr.plus(BaselineCompilerImpl.locationToOffset(location) - BYTES_IN_ADDRESS);
                }
                nextCallerAddress = nextCallerAddress.loadAddress();
                if (BaselineCompilerImpl.isRegister(location)) {
                    return registerLocations.get(location).toAddress();
                } else {
                    return framePtr.plus(BaselineCompilerImpl.locationToOffset(location) - BYTES_IN_ADDRESS);
                }
            } else {
                finishedWithRegularMap = true;
            }
        }
        if (bridgeParameterMappingRequired) {
            if (!bridgeRegistersLocationUpdated) {
                Address location = framePtr.plus(BaselineCompilerImpl.getFrameSize(currentCompiledMethod));
                location = location.minus((LAST_NONVOLATILE_FPR - FIRST_VOLATILE_FPR + 1) * BYTES_IN_DOUBLE);
                for (int i = LAST_NONVOLATILE_GPR; i >= FIRST_VOLATILE_GPR; --i) {
                    location = location.minus(BYTES_IN_ADDRESS);
                    registerLocations.set(i, location.toWord());
                }
                bridgeRegistersLocationUpdated = true;
            }
            if (bridgeParameterIndex == -1) {
                bridgeParameterIndex += 1;
                bridgeRegisterIndex += 1;
                bridgeRegisterLocation = bridgeRegisterLocation.plus(BYTES_IN_ADDRESS);
                if (VM.TraceStkMaps) {
                    VM.sysWrite("BaselineGCMapIterator getNextReferenceOffset, ");
                    VM.sysWrite("  this, bridge, returning: ");
                    VM.sysWrite(bridgeRegisterLocation.minus(BYTES_IN_ADDRESS));
                    VM.sysWrite("\n");
                }
                return bridgeRegisterLocation.minus(BYTES_IN_ADDRESS);
            }
            while (bridgeParameterIndex < bridgeParameterTypes.length) {
                TypeReference bridgeParameterType = bridgeParameterTypes[bridgeParameterIndex++];
                if (bridgeRegisterIndex <= LAST_VOLATILE_GPR) {
                    if (bridgeParameterType.isReferenceType()) {
                        bridgeRegisterLocation = bridgeRegisterLocation.plus(BYTES_IN_ADDRESS);
                        bridgeRegisterIndex += 1;
                        if (VM.TraceStkMaps) {
                            VM.sysWrite("BaselineGCMapIterator getNextReferenceOffset, ");
                            VM.sysWrite("  parm: ");
                            VM.sysWrite(bridgeRegisterLocation.minus(BYTES_IN_ADDRESS));
                            VM.sysWrite("\n");
                        }
                        return bridgeRegisterLocation.minus(BYTES_IN_ADDRESS);
                    } else if (bridgeParameterType.isLongType()) {
                        bridgeRegisterIndex += VM.BuildFor64Addr ? 1 : 2;
                        bridgeRegisterLocation = bridgeRegisterLocation.plus(BYTES_IN_LONG);
                    } else if (bridgeParameterType.isDoubleType() || bridgeParameterType.isFloatType()) {
                    } else {
                        bridgeRegisterIndex += 1;
                        bridgeRegisterLocation = bridgeRegisterLocation.plus(BYTES_IN_ADDRESS);
                    }
                } else {
                    if (bridgeParameterType.isReferenceType()) {
                        bridgeSpilledParamLocation = bridgeSpilledParamLocation.plus(BYTES_IN_ADDRESS);
                        if (VM.TraceStkMaps) {
                            VM.sysWrite("BaselineGCMapIterator getNextReferenceOffset, dynamic link spilled parameter, returning: ");
                            VM.sysWrite(bridgeSpilledParamLocation.minus(BYTES_IN_ADDRESS));
                            VM.sysWrite(".\n");
                        }
                        return bridgeSpilledParamLocation.minus(BYTES_IN_ADDRESS);
                    } else if (bridgeParameterType.isLongType()) {
                        bridgeSpilledParamLocation = bridgeSpilledParamLocation.plus(BYTES_IN_LONG);
                    } else if (bridgeParameterType.isDoubleType()) {
                        bridgeSpilledParamLocation = bridgeSpilledParamLocation.plus(BYTES_IN_DOUBLE);
                    } else if (bridgeParameterType.isFloatType()) {
                        bridgeSpilledParamLocation = bridgeSpilledParamLocation.plus(BYTES_IN_FLOAT);
                    } else {
                        bridgeSpilledParamLocation = bridgeSpilledParamLocation.plus(BYTES_IN_ADDRESS);
                    }
                }
            }
        }
        return Address.zero();
    }

    public Address getNextReturnAddressAddress() {
        if (mapId >= 0) {
            if (VM.TraceStkMaps) {
                VM.sysWrite("BaselineGCMapIterator getNextReturnAddressOffset mapId = ");
                VM.sysWrite(mapId);
                VM.sysWrite(".\n");
            }
            return Address.zero();
        }
        mapIndex = maps.getNextJSRReturnAddrIndex(mapIndex);
        if (VM.TraceStkMaps) {
            VM.sysWrite("BaselineGCMapIterator getNextReturnAddressIndex = ");
            VM.sysWrite(mapIndex);
            VM.sysWrite(".\n");
        }
        if (mapIndex == 0) return Address.zero();
        short location = convertIndexToLocation(mapIndex);
        if (VM.TraceStkMaps) {
            VM.sysWrite("BaselineGCMapIterator getNextReturnAddress location = ");
            VM.sysWrite(location);
            VM.sysWrite(".\n");
        }
        Address nextCallerAddress;
        if (BaselineCompilerImpl.isRegister(location)) {
            nextCallerAddress = registerLocations.get(location).toAddress();
        } else {
            nextCallerAddress = framePtr.plus(BaselineCompilerImpl.locationToOffset(location) - BYTES_IN_ADDRESS);
        }
        nextCallerAddress = nextCallerAddress.loadAddress();
        if (BaselineCompilerImpl.isRegister(location)) {
            return registerLocations.get(location).toAddress();
        } else {
            return framePtr.plus(BaselineCompilerImpl.locationToOffset(location) - BYTES_IN_ADDRESS);
        }
    }

    public void cleanupPointers() {
        updateCallerRegisterLocations();
        maps.cleanupPointers();
        maps = null;
        if (mapId < 0) {
            ReferenceMaps.jsrLock.unlock();
        }
        bridgeTarget = null;
        bridgeParameterTypes = null;
    }

    public int getType() {
        return CompiledMethod.BASELINE;
    }

    private void updateCallerRegisterLocations() {
        if (!currentMethod.getDeclaringClass().hasDynamicBridgeAnnotation()) {
            if (VM.TraceStkMaps) VM.sysWriteln("    Update Caller RegisterLocations");
            Address addr = framePtr.plus(BaselineCompilerImpl.getFrameSize(currentCompiledMethod));
            addr = addr.minus((currentCompiledMethod.getLastFloatStackRegister() - FIRST_FLOAT_LOCAL_REGISTER + 1) << LOG_BYTES_IN_DOUBLE);
            for (int i = currentCompiledMethod.getLastFixedStackRegister(); i >= FIRST_FIXED_LOCAL_REGISTER; --i) {
                addr = addr.minus(BYTES_IN_ADDRESS);
                registerLocations.set(i, addr.toWord());
            }
        }
    }

    public int getStackDepth() {
        return maps.getStackDepth(mapId);
    }
}
