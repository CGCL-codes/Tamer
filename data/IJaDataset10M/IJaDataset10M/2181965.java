package net.sourceforge.binml.util.tree;

import net.sourceforge.binml.datatype.*;

public class DataNodeFactory {

    /**
	 * Checks the datatype
	 * 
	 * @param Object
	 *            obj
	 * @return a static string
	 */
    public static String classToString(Object obj) {
        String typeStr = "";
        if (obj instanceof SInt8) {
            typeStr = "sint8";
        } else if (obj instanceof UInt8) {
            typeStr = "uint8";
        } else if (obj instanceof SInt16) {
            typeStr = "sint16";
        } else if (obj instanceof UInt16) {
            typeStr = "uint16";
        } else if (obj instanceof UInt16Cmplx) {
            typeStr = "uint16:c";
        } else if (obj instanceof SInt32) {
            typeStr = "sint32";
        } else if (obj instanceof UInt32) {
            typeStr = "uint32";
        } else if (obj instanceof SInt64) {
            typeStr = "sint64";
        } else if (obj instanceof UInt64) {
            typeStr = "uint64";
        } else if (obj instanceof SInt8Array) {
            typeStr = "sint8[]";
        } else if (obj instanceof UInt8Array) {
            typeStr = "uint8[]";
        } else if (obj instanceof SInt16Array) {
            typeStr = "sint16[]";
        } else if (obj instanceof UInt16Array) {
            typeStr = "uint16[]";
        } else if (obj instanceof SInt32Array) {
            typeStr = "sint32[]";
        } else if (obj instanceof UInt32Array) {
            typeStr = "uint32[]";
        } else if (obj instanceof SInt64Array) {
            typeStr = "sint64[]";
        } else if (obj instanceof UInt64Array) {
            typeStr = "uint64[]";
        } else if (obj instanceof BitField) {
            typeStr = "bitfield";
        }
        return typeStr;
    }
}
