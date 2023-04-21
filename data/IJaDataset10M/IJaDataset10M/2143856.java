package com.google.code.jahath;

public abstract class HostAddress {

    @Override
    public abstract String toString();

    public static HostAddress parse(String s) throws AddressParseException {
        int address = 0;
        int b = 0;
        int j = 0;
        boolean expectDigit = true;
        int i = 0;
        int c;
        do {
            c = i == s.length() ? -1 : s.charAt(i);
            if (c == '.' || c == -1) {
                if (expectDigit) {
                    return parseDnsAddress(s);
                } else {
                    address = (address << 8) | b;
                    j++;
                    b = 0;
                    expectDigit = c != -1;
                }
            } else if ('0' <= c && c <= '9') {
                b = 10 * b + c - '0';
                expectDigit = false;
            } else {
                return parseDnsAddress(s);
            }
            i++;
        } while (c != -1);
        if (j == 4 && !expectDigit) {
            return new IPv4Address(address);
        } else {
            return parseDnsAddress(s);
        }
    }

    private static DnsAddress parseDnsAddress(String s) throws AddressParseException {
        return new DnsAddress(s);
    }
}
