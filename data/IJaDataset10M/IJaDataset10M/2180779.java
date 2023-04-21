package com.ibm.JikesRVM.memoryManagers.vmInterface;

import com.ibm.JikesRVM.VM;
import com.ibm.JikesRVM.VM_Address;
import com.ibm.JikesRVM.VM_PragmaUninterruptible;
import com.ibm.JikesRVM.VM_Uninterruptible;

public final class AddressPairSet implements VM_Uninterruptible {

    int[] address;

    int cursor;

    public AddressPairSet(int size) {
        address = new int[2 * size];
    }

    public void clear() {
        cursor = 0;
    }

    public boolean isEmpty() {
        return cursor == 0;
    }

    public void push(VM_Address addr1, VM_Address addr2) {
        if (VM.VerifyAssertions) VM._assert(!addr1.isZero());
        if (VM.VerifyAssertions) VM._assert(!addr2.isZero());
        address[cursor++] = addr2.toInt();
        address[cursor++] = addr1.toInt();
    }

    public VM_Address pop1() {
        if (cursor == 0) return VM_Address.zero();
        if (VM.VerifyAssertions) VM._assert((cursor & 1) == 0);
        return VM_Address.fromInt(address[--cursor]);
    }

    public VM_Address pop2() {
        if (cursor == 0) return VM_Address.zero();
        if (VM.VerifyAssertions) VM._assert((cursor & 1) == 1);
        return VM_Address.fromInt(address[--cursor]);
    }

    public int size() {
        return cursor / 2;
    }
}
