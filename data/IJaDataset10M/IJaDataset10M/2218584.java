package potato.primitives;

import potato.*;
import potato.objects.SqueakObject;
import potato.vm.MethodCache;
import potato.vm.Stack;
import potato.objects.SmallInteger;
import potato.vm.VM;
import potato.drawing.Screen;
import potato.drawing.BitBlt;
import potato.drawing.FormCache;
import potato.image.SqueakImage;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import potato.Arithmetics;
import potato.objects.LargeInteger;
import potato.objects.ObjectTable;
import potato.objects.SpecialObjects;
import potato.vm.InterruptHandler;
import static potato.objects.SpecialObjectConstants.*;

public class PrimitiveHandler {

    private Stack stack;

    private MethodCache methodCache;

    private InterruptHandler interruptHandler;

    private ObjectTable objectTable;

    private VM vm;

    private SqueakImage image;

    boolean successFlag;

    private Screen theDisplay;

    int[] displayBitmap;

    int displayRaster;

    BitBlt bitbltTable;

    int BWMask = 0;

    int atCacheSize = 32;

    int atCacheMask = atCacheSize - 1;

    AtCacheInfo[] atCache;

    AtCacheInfo[] atPutCache;

    AtCacheInfo nonCachedInfo;

    Stack stack() {
        return stack;
    }

    private final FileSystemPrimitives fileSystemPrimitives = new FileSystemPrimitives(this);

    private final LargeIntegerPrimitives largeIntegerPrimitives = new LargeIntegerPrimitives(this);

    private static ExpectedPrimitiveFailedException epfe = new ExpectedPrimitiveFailedException();

    public PrimitiveFailedException failUnexpected() {
        return new UnexpectedPrimitiveFailedException();
    }

    public PrimitiveFailedException failUnexpected(Exception cause) {
        return new UnexpectedPrimitiveFailedException(cause);
    }

    public PrimitiveFailedException failUnexpected(String cause) {
        return new UnexpectedPrimitiveFailedException(cause);
    }

    public static PrimitiveFailedException failExpected() {
        return epfe;
    }

    /**
     * FIXME: surely something better can be devised?
     *        Idea: make argCount a field, then this method
     *        needs no argument
     */
    Object stackReceiver(int argCount) {
        return vm.stack.stackValue(argCount);
    }

    Object pos32BitIntFor(long pos32Val) {
        if (pos32Val < Integer.MIN_VALUE || pos32Val > Integer.MAX_VALUE) {
            throw failUnexpected(new ArithmeticException("long to int overflow"));
        }
        return pos32BitIntFor((int) pos32Val);
    }

    /**
     * Note: this method does not check to see that the passed
     *       object is an instance of Boolean.
     *       
     * @return true iff object is the special Squeak true object
     */
    boolean javaBool(SqueakObject object) {
        return object == SpecialObjects.trueObj;
    }

    SqueakObject squeakNil() {
        return SpecialObjects.nilObj;
    }

    SqueakObject squeakArray(Object[] javaArray) {
        SqueakObject array = vm.instantiateClass(SpecialObjects.getSpecialObject(splOb_ClassArray), javaArray.length);
        for (int index = 0; index < javaArray.length; index++) array.setPointer(index, javaArray[index]);
        return array;
    }

    Object squeakSeconds(long millis) {
        int secs = (int) (millis / 1000);
        secs += (69 * 365 + 17) * 24 * 3600;
        return pos32BitIntFor(secs);
    }

    /**
     * snapshotPrimitive
     *    "Primitive. Write the current state of the object memory on a file in the
     *    same format as the Smalltalk-80 release. The file can later be resumed,
     *    returning you to this exact state. Return normally after writing the file.
     *    Essential. See Object documentation whatIsAPrimitive."
     *    
     *    <primitive: 97>
     *     ^nil "indicates error writing image file"
     */
    private void primitiveSnapshot() {
        System.out.println("Saving the image");
        try {
            vm.image.saveImage(new File("/tmp/image.gz"));
        } catch (Exception e) {
            throw failUnexpected(e);
        }
    }

    /**
     * Primitive 121
     * "When called with a single string argument, record the string
     * as the current image file name. When called with zero
     * arguments, return a string containing the current image file
     * name."
     */
    private Object primitiveImageFileName(int argCount) {
        if (argCount == 0) return makeStString(vm.image.imageFile().getAbsolutePath()); else throw failUnexpected(new UnsupportedOperationException("Cannot set the image name yet, argument is '" + stackNonInteger(0) + "'"));
    }

    /**
     * SystemDictionary>>vmPath.
     * Primitive 142.
     * 
     * primVmPath
     *   "Answer the path for the directory containing the Smalltalk virtual machine. 
     *   Return the empty string if this primitive is not implemented."
     *        "Smalltalk vmPath"
     *
     *   <primitive: 142>
     *   ^ ''
     */
    private SqueakObject primitiveVmPath() {
        return makeStString(System.getProperty("user.dir") + System.getProperty("file.separator"));
    }

    public PrimitiveHandler(VM theVM) {
        vm = theVM;
        stack = theVM.stack;
        methodCache = theVM.methodCache;
        interruptHandler = theVM.interruptHandler;
        image = vm.image;
        objectTable = image.objectTable;
        bitbltTable = new BitBlt(vm);
        initAtCache();
    }

    class AtCacheInfo {

        SqueakObject array;

        int size;

        int ivarOffset;

        boolean convertChars;
    }

    void initAtCache() {
        atCache = new AtCacheInfo[atCacheSize];
        atPutCache = new AtCacheInfo[atCacheSize];
        nonCachedInfo = new AtCacheInfo();
        for (int i = 0; i < atCacheSize; i++) {
            atCache[i] = new AtCacheInfo();
            atPutCache[i] = new AtCacheInfo();
        }
    }

    public void clearAtCache() {
        for (int i = 0; i < atCacheSize; i++) {
            atCache[i].array = null;
            atPutCache[i].array = null;
        }
    }

    AtCacheInfo makeCacheInfo(AtCacheInfo[] atOrPutCache, Object atOrPutSelector, SqueakObject array, boolean convertChars, boolean includeInstVars) {
        AtCacheInfo info;
        boolean cacheable = (vm.verifyAtSelector == atOrPutSelector) && (vm.verifyAtClass == array.getSqClass()) && (array.format == 3 && vm.stack.isContext(array));
        if (cacheable) {
            info = atOrPutCache[array.hashCode() & atCacheMask];
        } else {
            info = nonCachedInfo;
        }
        info.array = array;
        info.convertChars = convertChars;
        if (includeInstVars) {
            info.size = Math.max(0, indexableSize(array)) + array.instSize();
            info.ivarOffset = 0;
        } else {
            info.size = indexableSize(array);
            info.ivarOffset = (array.format < 6) ? array.instSize() : 0;
        }
        return info;
    }

    public boolean quickSendOther(Object rcvr, int lobits) {
        successFlag = true;
        try {
            switch(lobits) {
                case 0x0:
                    popNandPushIfOK(2, primitiveAt(true, true, false));
                    break;
                case 0x1:
                    popNandPushIfOK(3, primitiveAtPut(true, true, false));
                    break;
                case 0x2:
                    popNandPushIfOK(1, primitiveSize());
                    break;
                case 0x3:
                    return false;
                case 0x4:
                    return false;
                case 0x5:
                    return false;
                case 0x6:
                    pop2andDoBoolIfOK(primitiveEq(vm.stack.stackValue(1), vm.stack.stackValue(0)));
                    break;
                case 0x7:
                    popNandPushIfOK(1, SpecialObjects.getClass(vm.stack.top()));
                    break;
                case 0x8:
                    popNandPushIfOK(2, primitiveBlockCopy());
                    break;
                case 0x9:
                    primitiveBlockValue(0);
                    break;
                case 0xa:
                    primitiveBlockValue(1);
                    break;
                case 0xb:
                    return false;
                case 0xc:
                    return false;
                case 0xd:
                    return false;
                case 0xe:
                    return false;
                case 0xf:
                    return false;
                default:
                    return false;
            }
        } catch (UnexpectedPrimitiveFailedException pfe) {
            pfe.printStackTrace();
            return false;
        } catch (ExpectedPrimitiveFailedException pfe) {
            return false;
        }
        return true;
    }

    boolean primitiveEq(Object arg1, Object arg2) {
        if (SmallInteger.isSmallInt(arg1) && SmallInteger.isSmallInt(arg2)) {
            return ((Integer) arg1).intValue() == ((Integer) arg2).intValue();
        }
        return arg1 == arg2;
    }

    Object primitiveBitAnd() {
        int rcvr = stackPos32BitValue(1);
        int arg = stackPos32BitValue(0);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        return pos32BitIntFor(rcvr & arg);
    }

    Object primitiveBitOr() {
        int rcvr = stackPos32BitValue(1);
        int arg = stackPos32BitValue(0);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        return pos32BitIntFor(rcvr | arg);
    }

    Object primitiveBitXor() {
        int rcvr = stackPos32BitValue(1);
        int arg = stackPos32BitValue(0);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        return pos32BitIntFor(rcvr ^ arg);
    }

    Object primitiveBitShift() {
        int rcvr = stackPos32BitValue(1);
        int arg = stackInteger(0);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        return pos32BitIntFor(Arithmetics.safeShift(rcvr, arg));
    }

    int doQuo(int rcvr, int arg) {
        if (arg == 0) {
            success(false);
            return 0;
        }
        if (rcvr > 0) {
            if (arg > 0) {
                return rcvr / arg;
            } else {
                return 0 - (rcvr / (0 - arg));
            }
        } else {
            if (arg > 0) {
                return 0 - ((0 - rcvr) / arg);
            } else {
                return (0 - rcvr) / (0 - arg);
            }
        }
    }

    public boolean doPrimitive(int index, int argCount) {
        successFlag = true;
        try {
            switch(index) {
                case 1:
                    popNandPushIntIfOK(2, stackInteger(1) + stackInteger(0));
                    break;
                case 2:
                    popNandPushIntIfOK(2, stackInteger(1) - stackInteger(0));
                    break;
                case 3:
                    pop2andDoBoolIfOK(stackInteger(1) < stackInteger(0));
                    break;
                case 4:
                    pop2andDoBoolIfOK(stackInteger(1) > stackInteger(0));
                    break;
                case 5:
                    pop2andDoBoolIfOK(stackInteger(1) <= stackInteger(0));
                    break;
                case 6:
                    pop2andDoBoolIfOK(stackInteger(1) >= stackInteger(0));
                    break;
                case 7:
                    pop2andDoBoolIfOK(stackInteger(1) == stackInteger(0));
                    break;
                case 8:
                    pop2andDoBoolIfOK(stackInteger(1) != stackInteger(0));
                    break;
                case 9:
                    popNandPushIntIfOK(2, Arithmetics.safeMultiply(stackInteger(1), stackInteger(0)));
                    break;
                case 10:
                    popNandPushIntIfOK(2, Arithmetics.quickDivide(stackInteger(1), stackInteger(0)));
                    break;
                case 11:
                    popNandPushIntIfOK(2, Arithmetics.mod(stackInteger(1), stackInteger(0)));
                    break;
                case 12:
                    popNandPushIntIfOK(2, Arithmetics.div(stackInteger(1), stackInteger(0)));
                    break;
                case 13:
                    popNandPushIntIfOK(2, doQuo(stackInteger(1), stackInteger(0)));
                    break;
                case 14:
                    popNandPushIfOK(2, primitiveBitAnd());
                    break;
                case 15:
                    popNandPushIfOK(2, primitiveBitOr());
                    break;
                case 16:
                    popNandPushIfOK(2, primitiveBitXor());
                    break;
                case 17:
                    popNandPushIfOK(2, primitiveBitShift());
                    break;
                case 18:
                    primitiveMakePoint();
                    break;
                case 19:
                    return false;
                case 21:
                    largeIntegerPrimitives.primitiveLargeIntegerAdd(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 22:
                    largeIntegerPrimitives.primitiveLargeIntegerSubtract(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 23:
                    largeIntegerPrimitives.primitiveLargeIntegerLT(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 24:
                    largeIntegerPrimitives.primitiveLargeIntegerGT(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 25:
                    largeIntegerPrimitives.primitiveLargeIntegerLE(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 26:
                    largeIntegerPrimitives.primitiveLargeIntegerGE(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 29:
                    largeIntegerPrimitives.primitiveLargeIntegerMultiply(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 30:
                    largeIntegerPrimitives.primitiveLargeIntegerDivide(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 31:
                    largeIntegerPrimitives.primitiveLargeIntegerModulo(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 32:
                    largeIntegerPrimitives.primitiveLargeIntegerIntegralDivide(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 33:
                    largeIntegerPrimitives.primitiveLargeIntegerQuo(stack.stackValue(1), stack.stackValue(0));
                    break;
                case 40:
                    primitiveAsFloat();
                    break;
                case 41:
                    popNandPushFloatIfOK(2, stackFloat(1) + stackFloat(0));
                    break;
                case 42:
                    popNandPushFloatIfOK(2, stackFloat(1) - stackFloat(0));
                    break;
                case 43:
                    pop2andDoBoolIfOK(stackFloat(1) < stackFloat(0));
                    break;
                case 44:
                    pop2andDoBoolIfOK(stackFloat(1) > stackFloat(0));
                    break;
                case 45:
                    pop2andDoBoolIfOK(stackFloat(1) <= stackFloat(0));
                    break;
                case 46:
                    pop2andDoBoolIfOK(stackFloat(1) >= stackFloat(0));
                    break;
                case 47:
                    pop2andDoBoolIfOK(stackFloat(1) == stackFloat(0));
                    break;
                case 48:
                    pop2andDoBoolIfOK(stackFloat(1) != stackFloat(0));
                    break;
                case 49:
                    popNandPushFloatIfOK(2, stackFloat(1) * stackFloat(0));
                    break;
                case 50:
                    popNandPushFloatIfOK(2, safeFDiv(stackFloat(1), stackFloat(0)));
                    break;
                case 51:
                    popNandPushIntIfOK(1, primitiveTruncate(stackFloat(0)));
                    break;
                case 52:
                    popNandPushFloatIfOK(1, primitiveFractionalPart(stackFloat(0)));
                    break;
                case 53:
                    popNandPushIntIfOK(1, primitiveExponent(stackFloat(0)));
                    break;
                case 54:
                    popNandPushFloatIfOK(2, primitiveTimesTwoPower(stackFloat(1), stackInteger(0)));
                    break;
                case 58:
                    popNandPushFloatIfOK(1, StrictMath.log(stackFloat(0)));
                    break;
                case 60:
                    popNandPushIfOK(2, primitiveAt(false, false, false));
                    break;
                case 61:
                    popNandPushIfOK(3, primitiveAtPut(false, false, false));
                    break;
                case 62:
                    popNandPushIfOK(1, primitiveSize());
                    break;
                case 63:
                    popNandPushIfOK(2, primitiveAt(false, true, false));
                    break;
                case 64:
                    popNandPushIfOK(3, primitiveAtPut(false, true, false));
                    break;
                case 65:
                    primitiveNext();
                    break;
                case 66:
                    primitiveNextPut();
                    break;
                case 67:
                    primitiveAtEnd();
                    break;
                case 68:
                    popNandPushIfOK(2, primitiveAt(false, false, true));
                    break;
                case 69:
                    popNandPushIfOK(3, primitiveAtPut(false, false, true));
                    break;
                case 70:
                    popNandPushIfOK(1, vm.instantiateClass(stackNonInteger(0), 0));
                    break;
                case 71:
                    popNandPushIfOK(2, primitiveNewWithSize());
                    break;
                case 72:
                    popNandPushIfOK(2, primitiveArrayBecome(false));
                    break;
                case 73:
                    popNandPushIfOK(2, primitiveAt(false, false, true));
                    break;
                case 74:
                    popNandPushIfOK(3, primitiveAtPut(false, false, true));
                    break;
                case 75:
                    popNandPushIfOK(1, primitiveHash());
                    break;
                case 77:
                    popNandPushIfOK(1, primitiveSomeInstance(stackNonInteger(0)));
                    break;
                case 78:
                    popNandPushIfOK(1, primitiveNextInstance(stackNonInteger(0)));
                    break;
                case 79:
                    popNandPushIfOK(3, primitiveNewMethod());
                    break;
                case 80:
                    popNandPushIfOK(2, primitiveBlockCopy());
                    break;
                case 81:
                    primitiveBlockValue(argCount);
                    break;
                case 83:
                    vm.primitivePerform(argCount);
                    break;
                case 84:
                    vm.primitivePerformWithArgs(SpecialObjects.getClass(vm.stack.stackValue(2)));
                    break;
                case 85:
                    semaphoreSignal();
                    break;
                case 86:
                    semaphoreWait();
                    break;
                case 87:
                    processResume();
                    break;
                case 88:
                    processSuspend();
                    break;
                case 89:
                    methodCache.clearMethodCache();
                    break;
                case 90:
                    popNandPushIfOK(1, primitiveMousePoint());
                    break;
                case 91:
                    throw failExpected();
                case 96:
                    bitbltTable.primitiveCopyBits(argCount, theDisplay);
                    break;
                case 97:
                    primitiveSnapshot();
                case 100:
                    vm.primitivePerformInSuperclass((SqueakObject) vm.stack.top());
                    break;
                case 101:
                    beCursor(argCount);
                    break;
                case 102:
                    beDisplay();
                    break;
                case 104:
                    bitbltTable.primitiveDrawLoop(argCount, theDisplay);
                    break;
                case 105:
                    popNandPushIfOK(5, primitiveStringReplace());
                    break;
                case 106:
                    popNandPushIfOK(1, makePointWithXandY(SmallInteger.smallFromInt(640), SmallInteger.smallFromInt(480)));
                    break;
                case 107:
                    popNandPushIfOK(1, primitiveMouseButtons());
                    break;
                case 108:
                    popNandPushIfOK(1, primitiveKbdNext());
                    break;
                case 109:
                    popNandPushIfOK(1, primitiveKbdPeek());
                    break;
                case 110:
                    popNandPushIfOK(2, (vm.stack.stackValue(1) == vm.stack.stackValue(0)) ? SpecialObjects.trueObj : SpecialObjects.falseObj);
                    break;
                case 112:
                    popNandPushIfOK(1, SmallInteger.smallFromInt(objectTable.spaceLeft()));
                    break;
                case 113:
                    System.exit(0);
                    return true;
                case 116:
                    methodCache.flushMethodCacheForMethod((SqueakObject) vm.stack.top());
                    break;
                case 119:
                    methodCache.flushMethodCacheForSelector((SqueakObject) vm.stack.top());
                    break;
                case 121:
                    popNandPushIfOK(1, primitiveImageFileName(argCount));
                    break;
                case 122:
                    BWMask = ~BWMask;
                    break;
                case 124:
                    popNandPushIfOK(2, registerSemaphore(splOb_TheLowSpaceSemaphore));
                    break;
                case 125:
                    popNandPushIfOK(2, setLowSpaceThreshold());
                    break;
                case 128:
                    popNandPushIfOK(2, primitiveArrayBecome(true));
                    break;
                case 129:
                    popNandPushIfOK(1, image.specialObjectsArray);
                    break;
                case 130:
                    popNandPushIfOK(1, SmallInteger.smallFromInt(objectTable.fullGC()));
                    break;
                case 131:
                    popNandPushIfOK(1, SmallInteger.smallFromInt(objectTable.partialGC()));
                    break;
                case 132:
                    popNandPushIfOK(2, primitiveObjectPointsTo());
                    break;
                case 134:
                    popNandPushIfOK(2, registerSemaphore(splOb_TheInterruptSemaphore));
                    break;
                case 135:
                    popNandPushIfOK(1, millisecondClockValue());
                    break;
                case 136:
                    popNandPushIfOK(3, primitiveSignalAtMilliseconds());
                    break;
                case 137:
                    popNandPushIfOK(1, primSeconds());
                    break;
                case 138:
                    popNandPushIfOK(1, primitiveSomeObject());
                    break;
                case 139:
                    popNandPushIfOK(1, primitiveNextObject(stackNonInteger(0)));
                    break;
                case 142:
                    popNandPushIfOK(1, primitiveVmPath());
                    break;
                case 148:
                    popNandPushIfOK(1, ((SqueakObject) vm.stack.top()).cloneIn(image));
                    break;
                case 149:
                    popNandPushIfOK(2, SpecialObjects.nilObj);
                    break;
                case 150:
                    popNandPushIfOK(2, fileSystemPrimitives.fileAtEnd(argCount));
                    break;
                case 151:
                    popNandPushIfOK(2, fileSystemPrimitives.fileClose(argCount));
                    break;
                case 152:
                    popNandPushIfOK(2, fileSystemPrimitives.getPosition(argCount));
                    break;
                case 153:
                    popNandPushIfOK(3, fileSystemPrimitives.openWritable(argCount));
                    break;
                case 154:
                    popNandPushIfOK(5, fileSystemPrimitives.readIntoStartingAtCount(argCount));
                    break;
                case 155:
                    popNandPushIfOK(3, fileSystemPrimitives.fileSetPosition(argCount));
                    break;
                case 156:
                    popNandPushIfOK(2, fileSystemPrimitives.fileDelete(argCount));
                    break;
                case 157:
                    popNandPushIfOK(2, fileSystemPrimitives.fileSize(argCount));
                    break;
                case 158:
                    popNandPushIfOK(5, fileSystemPrimitives.fileWrite(argCount));
                    break;
                case 159:
                    popNandPushIfOK(3, fileSystemPrimitives.fileRename(argCount));
                    break;
                case 160:
                    popNandPushIfOK(2, fileSystemPrimitives.directoryCreate(argCount));
                    break;
                case 161:
                    popNandPushIfOK(1, fileSystemPrimitives.directoryDelimitor());
                    break;
                case 162:
                    popNandPushIfOK(3, fileSystemPrimitives.lookupEntryInIndex(argCount));
                    break;
                case 230:
                    primitiveYield(argCount);
                    break;
                case 231:
                    theDisplay.redisplay(true);
                    break;
                case 235:
                    popNandPushIntIfOK(3, primitiveStringCompareWithCollated());
                    break;
                case 243:
                    primitiveStringTranslateFromToTable();
                    break;
                case 245:
                    popNandPushIntIfOK(4, primitiveStringIndexOfAsciiInStringStartingAt());
                    break;
                case 576:
                    String s = vm.stack.pop().toString();
                    System.out.println(s);
                    break;
                default:
                    {
                        System.err.println("undefined primitive: " + index);
                        return false;
                    }
            }
        } catch (UnexpectedPrimitiveFailedException pfe) {
            pfe.printStackTrace();
            return false;
        } catch (ExpectedPrimitiveFailedException pfe) {
            return false;
        }
        return true;
    }

    void pop2andDoBoolIfOK(boolean bool) {
        vm.success = successFlag;
        if (!vm.pushBoolAndPeek(bool)) throw failExpected();
    }

    void popNandPushIfOK(int nToPop, Object returnValue) {
        if (!successFlag || returnValue == null) {
            throw failExpected();
        }
        vm.stack.popNandPush(nToPop, returnValue);
    }

    void popNandPushIntIfOK(int nToPop, int returnValue) {
        popNandPushIfOK(nToPop, SmallInteger.smallFromInt(returnValue));
    }

    void popNandPushFloatIfOK(int nToPop, double returnValue) {
        if (!successFlag) {
            throw failExpected();
        }
        popNandPushIfOK(nToPop, makeFloat(returnValue));
    }

    int stackInteger(int nDeep) {
        return checkSmallInt(vm.stack.stackValue(nDeep));
    }

    int checkSmallInt(Object maybeSmall) {
        if (SmallInteger.isSmallInt(maybeSmall)) {
            return SmallInteger.intFromSmall((Integer) maybeSmall);
        }
        success(false);
        return 0;
    }

    double stackFloat(int nDeep) {
        return checkFloat(vm.stack.stackValue(nDeep));
    }

    double checkFloat(Object maybeFloat) {
        if (SpecialObjects.isA(maybeFloat, splOb_ClassFloat)) {
            return ((SqueakObject) maybeFloat).getFloatBits();
        }
        success(false);
        return 0.0d;
    }

    double safeFDiv(double dividend, double divisor) {
        if (divisor == 0.0d) {
            success(false);
            return 1.0d;
        }
        return dividend / divisor;
    }

    SqueakObject checkNonSmallInt(Object maybeSmall) {
        if (SmallInteger.isSmallInt(maybeSmall)) {
            success(false);
            return SpecialObjects.nilObj;
        }
        return (SqueakObject) maybeSmall;
    }

    int stackPos32BitValue(int nDeep) {
        Object stackVal = vm.stack.stackValue(nDeep);
        if (SmallInteger.isSmallInt(stackVal)) {
            int value = SmallInteger.intFromSmall((Integer) stackVal);
            if (value >= 0) {
                return value;
            }
            success(false);
            return 0;
        }
        if (!SpecialObjects.isA(stackVal, splOb_ClassLargePositiveInteger)) {
            success(false);
            return 0;
        }
        byte[] bytes = (byte[]) ((SqueakObject) stackVal).bits;
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value = value + ((bytes[i] & 255) << (8 * i));
        }
        return value;
    }

    Object pos32BitIntFor(int pos32Val) {
        if (pos32Val >= 0) {
            Object smallInt = SmallInteger.smallFromInt(pos32Val);
            if (smallInt != null) {
                return smallInt;
            }
        }
        SqueakObject lgIntClass = SpecialObjects.getSpecialObject(splOb_ClassLargePositiveInteger);
        SqueakObject lgIntObj = vm.instantiateClass(lgIntClass, 4);
        byte[] bytes = (byte[]) lgIntObj.bits;
        for (int i = 0; i < 4; i++) {
            bytes[i] = (byte) ((pos32Val >> (8 * i)) & 255);
        }
        return lgIntObj;
    }

    SqueakObject stackNonInteger(int nDeep) {
        return checkNonSmallInt(vm.stack.stackValue(nDeep));
    }

    SqueakObject squeakBool(boolean bool) {
        return bool ? SpecialObjects.trueObj : SpecialObjects.falseObj;
    }

    void primitiveAsFloat() {
        int intValue = stackInteger(0);
        if (!successFlag) {
            throw failExpected();
        }
        vm.stack.popNandPush(1, makeFloat(intValue));
    }

    int primitiveTruncate(double floatVal) {
        if (!(-1073741824.0 <= floatVal) && (floatVal <= 1073741823.0)) {
            throw failExpected();
        }
        return (new Double(floatVal)).intValue();
    }

    double primitiveFractionalPart(double floatVal) {
        return floatVal - (int) floatVal;
    }

    int primitiveExponent(double floatVal) {
        if (floatVal >= 1.0) return (int) StrictMath.floor(StrictMath.log(floatVal) / StrictMath.log(2.0));
        if (floatVal > 0.0) {
            int positive = primitiveExponent(1.0 / floatVal);
            if (floatVal == (1.0 / primitiveTimesTwoPower(1.0, positive))) return -positive;
            return -positive - 1;
        }
        if (floatVal == 0.0) return -1;
        return primitiveExponent(-floatVal);
    }

    double primitiveTimesTwoPower(double floatVal, int power) {
        return floatVal * StrictMath.pow(2.0, (double) power);
    }

    SqueakObject makeFloat(double value) {
        SqueakObject floatClass = SpecialObjects.getSpecialObject(splOb_ClassFloat);
        SqueakObject newFloat = vm.instantiateClass(floatClass, -1);
        newFloat.setFloatBits(value);
        return newFloat;
    }

    public void primitiveMakePoint() {
        Object x = vm.stack.stackValue(1);
        Object y = vm.stack.stackValue(0);
        vm.stack.popNandPush(2, makePointWithXandY(x, y));
    }

    SqueakObject makePointWithXandY(Object x, Object y) {
        SqueakObject pointClass = SpecialObjects.getSpecialObject(splOb_ClassPoint);
        SqueakObject newPoint = vm.instantiateClass(pointClass, 0);
        newPoint.setPointer(Constants.Point_x, x);
        newPoint.setPointer(Constants.Point_y, y);
        return newPoint;
    }

    SqueakObject primitiveNewWithSize() {
        int size = stackPos32BitValue(0);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        return vm.instantiateClass(((SqueakObject) vm.stack.stackValue(1)), size);
    }

    SqueakObject primitiveNewMethod() {
        Object headerInt = vm.stack.top();
        int byteCount = stackInteger(1);
        int methodHeader = checkSmallInt(headerInt);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        int litCount = (methodHeader >> 9) & 0xFF;
        SqueakObject method = vm.instantiateClass(((SqueakObject) vm.stack.stackValue(2)), byteCount);
        Object[] pointers = new Object[litCount + 1];
        Arrays.fill(pointers, SpecialObjects.nilObj);
        pointers[0] = headerInt;
        method.methodAddPointers(pointers);
        return method;
    }

    SqueakObject makeStString(String javaString) {
        byte[] byteString = javaString.getBytes();
        SqueakObject stString = vm.instantiateClass(SpecialObjects.getSpecialObject(splOb_ClassString), javaString.length());
        System.arraycopy(byteString, 0, stString.bits, 0, byteString.length);
        return stString;
    }

    Object primitiveSize() {
        Object rcvr = vm.stack.top();
        int size = indexableSize(rcvr);
        if (size == -1) {
            success(false);
        }
        return pos32BitIntFor(size);
    }

    Object primitiveAt(boolean cameFromAtBytecode, boolean convertChars, boolean includeInstVars) {
        SqueakObject array = stackNonInteger(1);
        int index = stackPos32BitValue(0);
        if (!successFlag) {
            return array;
        }
        AtCacheInfo info;
        if (cameFromAtBytecode) {
            info = atCache[array.hashCode() & atCacheMask];
            if (info.array != array) {
                success(false);
                return array;
            }
        } else {
            if (array.format == 6 && SpecialObjects.isA(array, splOb_ClassFloat)) {
                long floatBits = Double.doubleToRawLongBits(array.getFloatBits());
                if (index == 1) {
                    return pos32BitIntFor((int) (floatBits >> 32));
                }
                if (index == 2) {
                    return pos32BitIntFor((int) (floatBits & 0xFFFFFFFF));
                }
                success(false);
                return array;
            }
            info = makeCacheInfo(atCache, vm.specialSelectors[32], array, convertChars, includeInstVars);
        }
        if (index < 1 || index > info.size) {
            success(false);
            return array;
        }
        if (includeInstVars) {
            return array.pointers[index - 1];
        }
        if (array.format < 6) {
            return array.pointers[index - 1 + info.ivarOffset];
        }
        if (array.format < 8) {
            int value = ((int[]) array.bits)[index - 1];
            return pos32BitIntFor(value);
        }
        if (array.format < 12) {
            int value = (((byte[]) array.bits)[index - 1]) & 0xFF;
            if (info.convertChars) {
                return charFromInt(value);
            } else {
                return SmallInteger.smallFromInt(value);
            }
        }
        int offset = array.pointersSize() * 4;
        if (index - 1 - offset < 0) {
            success(false);
            return array;
        }
        return SmallInteger.smallFromInt((((byte[]) array.bits)[index - 1 - offset]) & 0xFF);
    }

    SqueakObject charFromInt(int ascii) {
        SqueakObject charTable = SpecialObjects.getSpecialObject(splOb_CharacterTable);
        return charTable.fetchPointerNI(ascii);
    }

    Object primitiveAtPut(boolean cameFromAtBytecode, boolean convertChars, boolean includeInstVars) {
        SqueakObject array = stackNonInteger(2);
        int index = stackPos32BitValue(1);
        if (!successFlag) {
            return array;
        }
        AtCacheInfo info;
        if (cameFromAtBytecode) {
            info = atPutCache[array.hashCode() & atCacheMask];
            if (info.array != array) {
                success(false);
                return array;
            }
        } else {
            info = makeCacheInfo(atPutCache, vm.specialSelectors[34], array, convertChars, includeInstVars);
        }
        if (index < 1 || index > info.size) {
            success(false);
            return array;
        }
        Object objToPut = vm.stack.stackValue(0);
        if (includeInstVars) {
            array.pointers[index - 1] = objToPut;
            return objToPut;
        }
        if (array.format < 6) {
            array.pointers[index - 1 + info.ivarOffset] = objToPut;
            return objToPut;
        }
        int intToPut;
        if (array.format < 8) {
            intToPut = stackPos32BitValue(0);
            if (!successFlag) {
                return objToPut;
            }
            ((int[]) array.bits)[index - 1] = intToPut;
            return objToPut;
        }
        if (info.convertChars) {
            if (SmallInteger.isSmallInt(objToPut)) {
                success(false);
                return objToPut;
            }
            SqueakObject sqObjToPut = (SqueakObject) objToPut;
            if (!SpecialObjects.isA(sqObjToPut, splOb_ClassCharacter)) {
                success(false);
                return objToPut;
            }
            Object asciiToPut = sqObjToPut.fetchPointer(0);
            if (!(SmallInteger.isSmallInt(asciiToPut))) {
                success(false);
                return objToPut;
            }
            intToPut = SmallInteger.intFromSmall((Integer) asciiToPut);
        } else {
            if (!(SmallInteger.isSmallInt(objToPut))) {
                success(false);
                return objToPut;
            }
            intToPut = SmallInteger.intFromSmall((Integer) objToPut);
        }
        if (intToPut < 0 || intToPut > 255) {
            success(false);
            return objToPut;
        }
        if (array.format < 8) {
            ((byte[]) array.bits)[index - 1] = (byte) intToPut;
            return objToPut;
        }
        boolean hasLarge = array.hasLarge();
        int offset = hasLarge ? 0 : array.pointersSize() * 4;
        if (index - 1 - offset < 0) {
            success(false);
            return array;
        }
        ((byte[]) array.bits)[index - 1 - offset] = (byte) intToPut;
        if (hasLarge) {
            array.invalidateLarge();
            LargeInteger.getOrCacheBig(array);
        }
        return objToPut;
    }

    int indexableSize(Object obj) {
        if (SmallInteger.isSmallInt(obj)) {
            return -1;
        }
        SqueakObject sqObj = (SqueakObject) obj;
        short fmt = sqObj.format;
        if (fmt < 2) {
            return -1;
        }
        if (fmt == 3 && vm.stack.isContext(sqObj)) {
            return sqObj.fetchInteger(Constants.Context_stackPointer).intValue();
        }
        if (fmt < 6) {
            return sqObj.pointersSize() - sqObj.instSize();
        }
        if (fmt < 12) {
            return sqObj.bitsSize();
        }
        return sqObj.bitsSize() + (4 * sqObj.pointersSize());
    }

    SqueakObject primitiveStringReplace() {
        SqueakObject dst = (SqueakObject) vm.stack.stackValue(4);
        int dstPos = stackInteger(3) - 1;
        int count = stackInteger(2) - dstPos;
        SqueakObject src = (SqueakObject) vm.stack.stackValue(1);
        int srcPos = stackInteger(0) - 1;
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        short srcFmt = src.format;
        short dstFmt = dst.format;
        if (dstFmt < 8) {
            if (dstFmt != srcFmt) {
                success(false);
                return dst;
            } else if ((dstFmt & 0xC) != (srcFmt & 0xC)) {
                success(false);
                return dst;
            }
        }
        if (srcFmt < 4) {
            int totalLength = src.pointersSize();
            int srcInstSize = src.instSize();
            srcPos += srcInstSize;
            if ((srcPos < 0) || (srcPos + count) > totalLength) {
                success(false);
                return SpecialObjects.nilObj;
            }
            totalLength = dst.pointersSize();
            int dstInstSize = dst.instSize();
            dstPos += dstInstSize;
            if ((dstPos < 0) || (dstPos + count) > totalLength) {
                success(false);
                return SpecialObjects.nilObj;
            }
            System.arraycopy(src.pointers, srcPos, dst.pointers, dstPos, count);
            return dst;
        } else {
            int totalLength = src.bitsSize();
            if ((srcPos < 0) || (srcPos + count) > totalLength) {
                success(false);
                return SpecialObjects.nilObj;
            }
            totalLength = dst.bitsSize();
            if ((dstPos < 0) || (dstPos + count) > totalLength) {
                success(false);
                return SpecialObjects.nilObj;
            }
            System.arraycopy(src.bits, srcPos, dst.bits, dstPos, count);
            if (dst.hasLarge()) {
                dst.invalidateLarge();
                LargeInteger.getOrCacheBig(dst);
            }
            return dst;
        }
    }

    void primitiveNext() {
        SqueakObject stream = stackNonInteger(0);
        if (!successFlag) {
            throw failExpected();
        }
        Object[] streamBody = stream.pointers;
        if (streamBody == null || streamBody.length < (Constants.Stream_limit + 1)) {
            throw failExpected();
        }
        Object array = streamBody[Constants.Stream_array];
        if (SmallInteger.isSmallInt(array)) {
            throw failExpected();
        }
        int index = checkSmallInt(streamBody[Constants.Stream_position]);
        int limit = indexableSize(array);
        if (index >= limit) throw failExpected();
        index++;
        Object result;
        SqueakObject sqArray = (SqueakObject) array;
        int fmt = sqArray.format;
        if (fmt <= 4) {
            result = sqArray.fetchPointer(index - 1);
        } else if (fmt < 8) {
            result = pos32BitIntFor(((int[]) sqArray.bits)[index - 1]);
        } else if (fmt >= 16) {
            result = charFromInt(((byte[]) sqArray.bits)[index - 1]);
        } else {
            if (SpecialObjects.isA(sqArray, splOb_ClassString)) {
                result = charFromInt(((byte[]) sqArray.bits)[index - 1]);
            } else result = SmallInteger.smallFromInt(((byte[]) sqArray.bits)[index - 1]);
        }
        if (successFlag) {
            streamBody[Constants.Stream_position] = SmallInteger.smallFromInt(index);
            popNandPushIfOK(1, result);
        } else failExpected();
    }

    void primitiveNextPut() {
        SqueakObject stream = stackNonInteger(1);
        if (!successFlag) {
            throw failExpected();
        }
        Object[] streamBody = stream.pointers;
        if (streamBody == null || streamBody.length < (Constants.Stream_limit + 1)) {
            throw failExpected();
        }
        Object array = streamBody[Constants.Stream_array];
        if (SmallInteger.isSmallInt(array)) {
            throw failExpected();
        }
        int index = checkSmallInt(streamBody[Constants.Stream_position]);
        int limit = indexableSize(array);
        Object value = stack.stackValue(0);
        if (!successFlag) throw failExpected();
        if (index >= limit) throw failExpected();
        index++;
        SqueakObject sqArray = (SqueakObject) array;
        int fmt = sqArray.format;
        if (fmt <= 4) {
            sqArray.setPointer(index - 1, value);
        } else if (fmt < 8) {
            ((int[]) sqArray.bits)[index - 1] = SmallInteger.intFromSmall((Integer) value);
        } else {
            Object valToPut;
            if (SpecialObjects.isA(value, splOb_ClassCharacter) || fmt >= 16) {
                valToPut = asciiOfCharacter((SqueakObject) value);
            } else {
                valToPut = value;
            }
            if (SmallInteger.isSmallInt(valToPut)) {
                int intValToPut = SmallInteger.intFromSmall((Integer) valToPut);
                if (!(intValToPut >= 0 && intValToPut <= 255)) throw failExpected();
                if (fmt >= 12 && fmt <= 15) {
                    ((byte[]) sqArray.bits)[index - 4 * sqArray.pointersSize() - 1] = (byte) intValToPut;
                } else ((byte[]) sqArray.bits)[index - 1] = (byte) intValToPut;
            } else throw failExpected();
        }
        if (successFlag) {
            streamBody[Constants.Stream_position] = SmallInteger.smallFromInt(index);
            popNandPushIfOK(2, value);
        } else throw failExpected();
    }

    Integer asciiOfCharacter(SqueakObject c) {
        if (!SpecialObjects.isA(c, splOb_ClassCharacter)) return SmallInteger.smallFromInt(0); else return c.fetchInteger(Constants.CharacterValueIndex);
    }

    void primitiveAtEnd() {
        SqueakObject stream = (SqueakObject) stack.stackValue(0);
        if (!successFlag) {
            throw failExpected();
        }
        Object[] streamBody = stream.pointers;
        if (streamBody == null || streamBody.length < (Constants.Stream_limit + 1)) {
            throw failExpected();
        }
        Object array = streamBody[Constants.Stream_array];
        int index = checkSmallInt(streamBody[Constants.Stream_position]);
        int limit = indexableSize(array);
        if (successFlag) {
            popNandPushIfOK(1, squeakBool(index >= limit));
        } else throw failExpected();
    }

    SqueakObject primitiveBlockCopy() {
        Object rcvr = vm.stack.stackValue(1);
        if (SmallInteger.isSmallInt(rcvr)) {
            success(false);
        }
        Object sqArgCount = vm.stack.top();
        if (!(SmallInteger.isSmallInt(sqArgCount))) {
            success(false);
        }
        SqueakObject homeCtxt = (SqueakObject) rcvr;
        if (!vm.stack.isContext(homeCtxt)) {
            success(false);
        }
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        if (SmallInteger.isSmallInt(homeCtxt.fetchPointer(Constants.Context_method))) {
            homeCtxt = homeCtxt.fetchPointerNI(Constants.BlockContext_home);
        }
        int blockSize = homeCtxt.pointersSize() - homeCtxt.instSize();
        SqueakObject newBlock = vm.instantiateClass((SpecialObjects.getSpecialObject(splOb_ClassBlockContext)), blockSize);
        Integer initialPC = vm.encodeSqueakPC(vm.pc + 2, vm.method);
        newBlock.setPointer(Constants.BlockContext_initialIP, initialPC);
        newBlock.setPointer(Constants.Context_instructionPointer, initialPC);
        newBlock.setPointer(Constants.Context_stackPointer, SmallInteger.smallFromInt(0));
        newBlock.setPointer(Constants.BlockContext_argumentCount, sqArgCount);
        newBlock.setPointer(Constants.BlockContext_home, homeCtxt);
        newBlock.setPointer(Constants.Context_sender, SpecialObjects.nilObj);
        return newBlock;
    }

    void primitiveBlockValue(int argCount) {
        Object rcvr = vm.stack.stackValue(argCount);
        if (!SpecialObjects.isA(rcvr, splOb_ClassBlockContext)) {
            throw failExpected();
        }
        SqueakObject block = (SqueakObject) rcvr;
        Object blockArgCount = block.fetchPointer(Constants.BlockContext_argumentCount);
        if (!SmallInteger.isSmallInt(blockArgCount)) {
            throw failExpected();
        }
        if ((((Integer) blockArgCount).intValue() != argCount)) {
            throw failExpected();
        }
        if (block.fetchPointer(Constants.BlockContext_caller) != SpecialObjects.nilObj) {
            throw failExpected();
        }
        System.arraycopy((Object) vm.activeContext.pointers, vm.stack.sp - argCount + 1, (Object) block.pointers, Constants.Context_tempFrameStart, argCount);
        Integer initialIP = block.fetchInteger(Constants.BlockContext_initialIP);
        block.setPointer(Constants.Context_instructionPointer, initialIP);
        block.setPointer(Constants.Context_stackPointer, new Integer(argCount));
        block.setPointer(Constants.BlockContext_caller, vm.activeContext);
        vm.stack.popN(argCount + 1);
        vm.newActiveContext(block);
    }

    Object primitiveHash() {
        Object rcvr = vm.stack.top();
        if (SmallInteger.isSmallInt(rcvr)) {
            success(false);
            return SpecialObjects.nilObj;
        }
        return new Integer(((SqueakObject) rcvr).hash);
    }

    Object setLowSpaceThreshold() {
        int nBytes = stackInteger(0);
        if (successFlag) {
            vm.lowSpaceThreshold = nBytes;
        }
        return vm.stack.stackValue(1);
    }

    SqueakObject getScheduler() {
        SqueakObject assn = SpecialObjects.getSpecialObject(splOb_SchedulerAssociation);
        return assn.fetchPointerNI(Constants.Assn_value);
    }

    void processResume() {
        SqueakObject process = (SqueakObject) vm.stack.top();
        resume(process);
    }

    void processSuspend() {
        SqueakObject activeProc = getScheduler().fetchPointerNI(Constants.ProcSched_activeProcess);
        if (vm.stack.top() != activeProc) {
            throw failExpected();
        }
        vm.stack.popNandPush(1, SpecialObjects.nilObj);
        transferTo(pickTopProcess());
    }

    void semaphoreWait() {
        SqueakObject sema = (SqueakObject) vm.stack.top();
        if (!SpecialObjects.isA(sema, splOb_ClassSemaphore)) {
            throw failExpected();
        }
        int excessSignals = sema.fetchInteger(Constants.Semaphore_excessSignals).intValue();
        if (excessSignals > 0) {
            sema.setPointer(Constants.Semaphore_excessSignals, SmallInteger.smallFromInt(excessSignals - 1));
        } else {
            SqueakObject activeProc = getScheduler().fetchPointerNI(Constants.ProcSched_activeProcess);
            linkProcessToList(activeProc, sema);
            transferTo(pickTopProcess());
        }
    }

    void semaphoreSignal() {
        SqueakObject sema = (SqueakObject) vm.stack.top();
        if (!SpecialObjects.isA(sema, splOb_ClassSemaphore)) {
            throw failExpected();
        }
        synchronousSignal(sema);
    }

    public void synchronousSignal(SqueakObject sema) {
        if (isEmptyList(sema)) {
            int excessSignals = sema.fetchInteger(Constants.Semaphore_excessSignals).intValue();
            sema.setPointer(Constants.Semaphore_excessSignals, SmallInteger.smallFromInt(excessSignals + 1));
        } else {
            resume(removeFirstLinkOfList(sema));
        }
        return;
    }

    void resume(SqueakObject newProc) {
        SqueakObject activeProc = getScheduler().fetchPointerNI(Constants.ProcSched_activeProcess);
        int activePriority = activeProc.fetchInteger(Constants.Proc_priority).intValue();
        int newPriority = newProc.fetchInteger(Constants.Proc_priority).intValue();
        if (newPriority > activePriority) {
            putToSleep(activeProc);
            transferTo(newProc);
        } else {
            putToSleep(newProc);
        }
    }

    void putToSleep(SqueakObject aProcess) {
        int priority = aProcess.fetchInteger(Constants.Proc_priority).intValue();
        SqueakObject processLists = getScheduler().fetchPointerNI(Constants.ProcSched_processLists);
        SqueakObject processList = processLists.fetchPointerNI(priority - 1);
        linkProcessToList(aProcess, processList);
    }

    void transferTo(SqueakObject newProc) {
        SqueakObject sched = getScheduler();
        SqueakObject oldProc = sched.fetchPointerNI(Constants.ProcSched_activeProcess);
        sched.setPointer(Constants.ProcSched_activeProcess, newProc);
        oldProc.setPointer(Constants.Proc_suspendedContext, vm.activeContext);
        vm.newActiveContext(newProc.fetchPointerNI(Constants.Proc_suspendedContext));
        newProc.setPointer(Constants.Proc_suspendedContext, SpecialObjects.nilObj);
        vm.reclaimableContextCount = 0;
    }

    SqueakObject pickTopProcess() {
        SqueakObject schedLists = getScheduler().fetchPointerNI(Constants.ProcSched_processLists);
        int p = schedLists.pointersSize() - 1;
        p = p - 1;
        SqueakObject processList = schedLists.fetchPointerNI(p);
        while (isEmptyList(processList)) {
            p = p - 1;
            if (p < 0) {
                return SpecialObjects.nilObj;
            }
            processList = schedLists.fetchPointerNI(p);
        }
        return removeFirstLinkOfList(processList);
    }

    void linkProcessToList(SqueakObject proc, SqueakObject aList) {
        if (isEmptyList(aList)) {
            aList.setPointer(Constants.LinkedList_firstLink, proc);
        } else {
            SqueakObject lastLink = aList.fetchPointerNI(Constants.LinkedList_lastLink);
            lastLink.setPointer(Constants.Link_nextLink, proc);
        }
        aList.setPointer(Constants.LinkedList_lastLink, proc);
        proc.setPointer(Constants.Proc_myList, aList);
    }

    boolean isEmptyList(SqueakObject aLinkedList) {
        return aLinkedList.fetchPointerNI(Constants.LinkedList_firstLink) == SpecialObjects.nilObj;
    }

    SqueakObject removeFirstLinkOfList(SqueakObject aList) {
        SqueakObject first = aList.fetchPointerNI(Constants.LinkedList_firstLink);
        SqueakObject last = aList.fetchPointerNI(Constants.LinkedList_lastLink);
        if (first == last) {
            aList.setPointer(Constants.LinkedList_firstLink, SpecialObjects.nilObj);
            aList.setPointer(Constants.LinkedList_lastLink, SpecialObjects.nilObj);
        } else {
            SqueakObject next = first.fetchPointerNI(Constants.Link_nextLink);
            aList.setPointer(Constants.LinkedList_firstLink, next);
        }
        first.setPointer(Constants.Link_nextLink, SpecialObjects.nilObj);
        return first;
    }

    SqueakObject primitiveObjectPointsTo() {
        Object thang = stack.stackValue(0);
        Object rcvr = stack.stackValue(1);
        if (SmallInteger.isSmallInt(rcvr)) return squeakBool(false);
        SqueakObject sqr = (SqueakObject) rcvr;
        if (!sqr.isPointers()) throw failExpected();
        for (int i = 0; i < sqr.pointersSize(); i++) if (sqr.fetchPointer(i) == thang) return squeakBool(true);
        return squeakBool(false);
    }

    SqueakObject registerSemaphore(int specialObjSpec) {
        SqueakObject sema = (SqueakObject) vm.stack.top();
        SpecialObjects.registerSpecialObject(sema, specialObjSpec, splOb_ClassSemaphore);
        return (SqueakObject) vm.stack.stackValue(1);
    }

    Object primitiveSignalAtMilliseconds() {
        int msTime = stackInteger(0);
        Object sema = stackNonInteger(1);
        Object rcvr = stackNonInteger(2);
        if (!successFlag) {
            return SpecialObjects.nilObj;
        }
        if (SpecialObjects.isA(sema, splOb_ClassSemaphore)) {
            SpecialObjects.registerSpecialObject((SqueakObject) sema, splOb_TheTimerSemaphore, splOb_ClassSemaphore);
            interruptHandler.nextWakeupTick = msTime;
        } else {
            SpecialObjects.registerSpecialObject(SpecialObjects.nilObj, splOb_TheTimerSemaphore, splOb_ClassSemaphore);
            interruptHandler.nextWakeupTick = 0;
        }
        return rcvr;
    }

    Integer millisecondClockValue() {
        return SmallInteger.smallFromInt((int) (System.currentTimeMillis() & (long) (SmallInteger.maxSmallInt >> 1)));
    }

    void beDisplay() {
        SqueakObject rcvr = (SqueakObject) vm.stack.top();
        success(rcvr.isPointers() && rcvr.lengthOf() >= 4);
        if (successFlag) {
            SpecialObjects.forceRegisterSpecialObject(rcvr, splOb_TheDisplay);
        }
        FormCache disp = new FormCache(rcvr, vm);
        if (disp.squeakForm == null) {
            throw failExpected();
        }
        displayBitmap = disp.bits;
        boolean remap = theDisplay != null;
        if (remap) {
            Dimension requestedExtent = new Dimension(disp.width, disp.height);
            if (!theDisplay.getExtent().equals(requestedExtent)) {
                System.err.println("Squeak: changing screen size to " + disp.width + "@" + disp.height);
                theDisplay.setExtent(requestedExtent);
            }
        } else {
            theDisplay = new Screen("Potato", disp.width, disp.height, disp.depth, vm);
        }
        theDisplay.setBits(displayBitmap, disp.depth);
        if (!remap) {
            theDisplay.open();
        }
    }

    void beCursor(int argCount) {
        if (theDisplay == null) {
            return;
        }
        SqueakObject cursorObj = SpecialObjects.nilObj;
        SqueakObject maskBitsIndex;
        SqueakObject bitsObj = SpecialObjects.nilObj;
        SqueakObject offsetObj = SpecialObjects.nilObj;
        int extentX, extentY, depth, offsetX, offsetY;
        extentX = extentY = depth = 0;
        if (argCount == 0) {
            cursorObj = stackNonInteger(0);
            maskBitsIndex = SpecialObjects.nilObj;
        }
        if (argCount == 1) {
            cursorObj = stackNonInteger(1);
            maskBitsIndex = stackNonInteger(0);
        }
        FormCache cursorForm = new FormCache(cursorObj, vm);
        if (!successFlag || cursorForm.squeakForm == null) {
            throw failExpected();
        }
        offsetObj = checkNonSmallInt(cursorObj.fetchPointer(4));
        if (!SpecialObjects.isA(offsetObj, splOb_ClassPoint)) {
            throw failExpected();
        }
        offsetX = checkSmallInt(offsetObj.pointers[0]);
        offsetY = checkSmallInt(offsetObj.pointers[1]);
        if (!successFlag) {
            throw failExpected();
        }
        theDisplay.setCursor(cursorForm.bits, BWMask);
    }

    void primitiveYield(int numArgs) {
        long millis = 100;
        if (numArgs > 1) {
            throw failExpected();
        }
        if (numArgs > 0) {
            int micros = stackInteger(0);
            if (!successFlag) {
                throw failExpected();
            }
            vm.stack.pop();
            millis = micros / 1000;
        }
        synchronized (vm) {
            try {
                while (!vm.screenEvent) {
                    vm.wait(millis);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(PrimitiveHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    SqueakObject primitiveMousePoint() {
        SqueakObject pointClass = SpecialObjects.getSpecialObject(splOb_ClassPoint);
        SqueakObject newPoint = vm.instantiateClass(pointClass, 0);
        Point lastMouse = theDisplay.getLastMousePoint();
        newPoint.setPointer(Constants.Point_x, SmallInteger.smallFromInt(lastMouse.x));
        newPoint.setPointer(Constants.Point_y, SmallInteger.smallFromInt(lastMouse.y));
        return newPoint;
    }

    Integer primitiveMouseButtons() {
        return SmallInteger.smallFromInt(theDisplay.getLastMouseButtonStatus());
    }

    Object primitiveKbdNext() {
        return SmallInteger.smallFromInt(theDisplay.keyboardNext());
    }

    Object primitiveKbdPeek() {
        if (theDisplay == null) {
            return (Object) SpecialObjects.nilObj;
        }
        int peeked = theDisplay.keyboardPeek();
        return peeked == 0 ? (Object) SpecialObjects.nilObj : SmallInteger.smallFromInt(peeked);
    }

    SqueakObject primitiveArrayBecome(boolean doBothWays) {
        SqueakObject rcvr = stackNonInteger(1);
        SqueakObject arg = stackNonInteger(0);
        if (!successFlag) {
            return rcvr;
        }
        success(objectTable.bulkBecome(rcvr.pointers, arg.pointers, doBothWays));
        return rcvr;
    }

    SqueakObject primitiveSomeObject() {
        return objectTable.nextInstance(0, null);
    }

    SqueakObject primitiveSomeInstance(SqueakObject sqClass) {
        return objectTable.nextInstance(0, sqClass);
    }

    Object primitiveNextObject(SqueakObject priorObject) {
        SqueakObject nextObject = objectTable.nextInstance(objectTable.otIndexOfObject(priorObject) + 1, null);
        if (nextObject == SpecialObjects.nilObj) {
            return SmallInteger.smallFromInt(0);
        }
        return nextObject;
    }

    SqueakObject primitiveNextInstance(SqueakObject priorInstance) {
        SqueakObject sqClass = (SqueakObject) priorInstance.sqClass;
        return objectTable.nextInstance(objectTable.otIndexOfObject(priorInstance) + 1, sqClass);
    }

    Object primSeconds() {
        int secs = (int) (System.currentTimeMillis() / 1000);
        secs += ((69 * 365 + 17) * 24 * 3600);
        return pos32BitIntFor(secs);
    }

    void success(boolean value) {
        successFlag = successFlag && value;
    }

    int primitiveStringCompareWithCollated() {
        SqueakObject string1 = (SqueakObject) vm.stack.stackValue(2);
        SqueakObject string2 = (SqueakObject) vm.stack.stackValue(1);
        SqueakObject order = (SqueakObject) vm.stack.stackValue(0);
        int len1 = ((byte[]) string1.bits).length;
        int len2 = ((byte[]) string2.bits).length;
        for (int i = 0; i < Math.min(len1, len2); i++) {
            char c1 = (char) ((byte[]) order.bits)[((byte[]) string1.bits)[i]];
            char c2 = (char) ((byte[]) order.bits)[((byte[]) string2.bits)[i]];
            if (c1 != c2) if (c1 < c2) return 1; else return 3;
        }
        if (len1 == len2) return 2;
        if (len1 < len2) return 1;
        return 3;
    }

    void primitiveStringTranslateFromToTable() {
        SqueakObject string = stackNonInteger(3);
        int start = stackInteger(2);
        int stop = stackInteger(1);
        SqueakObject table = stackNonInteger(0);
        if (!successFlag) throw failExpected();
        byte[] stringBits = (byte[]) string.bits;
        byte[] tableBits = (byte[]) table.bits;
        for (int k = start; k <= stop; k++) stringBits[k - 1] = tableBits[stringBits[k - 1]];
        stack.popN(4);
    }

    int primitiveStringIndexOfAsciiInStringStartingAt() {
        int startAt = stackInteger(0);
        if (!successFlag) throw failUnexpected("startAt is not an int");
        SqueakObject str = (SqueakObject) stackNonInteger(1);
        SqueakObject rcvr = (SqueakObject) stackNonInteger(3);
        byte ascii = (byte) stackInteger(2);
        if (!successFlag) throw failUnexpected("ascii is not an int");
        int size = str.bitsSize();
        for (int k = startAt; k <= size; k++) if (((byte[]) str.bits)[k - 1] == ascii) return k;
        return 0;
    }

    /**
     * This method is to be used to push the result of a LargeInteger primitive.
     * If the result of the primitive is a SmallInteger, the passed BigInteger
     * is coerced into a SmallInteger and pushed. Otherwise, the result is a
     * Large****tiveInteger, which is pushed if possible.
     * 
     * @param big the result of a LargeInteger primitive.
     */
    void pop2andPushPossiblyCoercedBigIfOK(BigInteger big) {
        if (LargeInteger.isBig(big)) {
            if (big.compareTo(BigInteger.ZERO) > 0) {
                byte[] lpi_bytes = LargeInteger.squeakBytes(big);
                SqueakObject squeak_result = vm.instantiateClass(SpecialObjects.getSpecialObject(splOb_ClassLargePositiveInteger), lpi_bytes.length);
                System.arraycopy(lpi_bytes, 0, (byte[]) squeak_result.bits, 0, lpi_bytes.length);
                squeak_result.assignLarge(big);
                popNandPushIfOK(2, squeak_result);
            } else {
                throw failExpected();
            }
        } else {
            popNandPushIntIfOK(2, big.intValue());
        }
    }
}
