package com.google.gwt.dev.js.rhino;

final class BinaryDigitReader {

    int lgBase;

    int digit;

    int digitPos;

    String digits;

    int start;

    int end;

    BinaryDigitReader(int base, String digits, int start, int end) {
        lgBase = 0;
        while (base != 1) {
            lgBase++;
            base >>= 1;
        }
        digitPos = 0;
        this.digits = digits;
        this.start = start;
        this.end = end;
    }

    int getNextBinaryDigit() {
        if (digitPos == 0) {
            if (start == end) return -1;
            char c = digits.charAt(start++);
            if ('0' <= c && c <= '9') digit = c - '0'; else if ('a' <= c && c <= 'z') digit = c - 'a' + 10; else digit = c - 'A' + 10;
            digitPos = lgBase;
        }
        return digit >> --digitPos & 1;
    }
}
