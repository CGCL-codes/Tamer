package bussiness.subnet;

import java.util.*;

/**
 * <p>Title: SubNet</p>
 * <p>Description: IP subnetting algorithms.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author German Viscuso
 * @version 1.0
 *
 * @(#)IPAddress.java
 *
 * Applet SubNet - IP subnetting algorithms.
 * Copyright (C) 2001 2002 German Viscuso.
 * E-mail: netquake@netquake.com.ar
 *
 * This program is free software; you can redistribute it and/or modify
 * it , under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * 
 **/
public class IPAddress implements Comparable {

    private UnsignedByte addr[] = new UnsignedByte[4];

    private IPAddress netmask = null;

    private String gateway;

    public static void main(String... args) {
        IPAddress net = new IPAddress("170.20.3.0");
        net.setNetmask(new IPAddress("255.255.255.192"));
        System.out.println(net.subNets(net.getNetmask()).length);
    }

    public IPAddress(int byte3, int byte2, int byte1, int byte0) {
        addr[0] = new UnsignedByte(8, byte0);
        addr[1] = new UnsignedByte(8, byte1);
        addr[2] = new UnsignedByte(8, byte2);
        addr[3] = new UnsignedByte(8, byte3);
        gateway = this.increment().toString();
    }

    public IPAddress(String decDotRep) {
        String ip = decDotRep;
        String[] tokens = new String[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        if (st.countTokens() == 4) {
            int i = 0;
            while (st.hasMoreTokens()) {
                tokens[i++] = st.nextToken();
            }
        }
        addr[0] = new UnsignedByte(8, Integer.parseInt(tokens[3]));
        addr[1] = new UnsignedByte(8, Integer.parseInt(tokens[2]));
        addr[2] = new UnsignedByte(8, Integer.parseInt(tokens[1]));
        addr[3] = new UnsignedByte(8, Integer.parseInt(tokens[0]));
        gateway = this.increment().toString();
    }

    public IPAddress(UnsignedByte bin) {
        addr = bin.divide(8);
    }

    public IPAddress(UnsignedByte byte3, UnsignedByte byte2, UnsignedByte byte1, UnsignedByte byte0) {
        UnsignedByte bin = new UnsignedByte(32, byte3.toString() + byte2.toString() + byte1.toString() + byte0.toString());
        addr = bin.divide(8);
    }

    public String Class() {
        int byte3 = getAddr()[3].intValue();
        if (byte3 < 128) {
            return "A";
        } else if (byte3 < 192) {
            return "B";
        } else if (byte3 < 224) {
            return "C";
        } else if (byte3 < 240) {
            return "D";
        } else if (byte3 < 248) {
            return "E";
        }
        return null;
    }

    public int netBits() {
        String cl = this.Class();
        if (cl.compareTo("A") == 0) return 8;
        if (cl.compareTo("B") == 0) return 16;
        if (cl.compareTo("C") == 0) return 24;
        if (cl.compareTo("D") == 0) return 32;
        if (cl.compareTo("E") == 0) return -1;
        return 0;
    }

    public IPAddress netMask(int subnetBits) {
        setNetmask(new IPAddress(this.fixMask(subnetBits + this.netBits())));
        return getNetmask();
    }

    public IPAddress[] subNets(IPAddress netMask) {
        setNetmask(netMask);
        return subNets();
    }

    public IPAddress[] subNets() {
        int snetbits = subnetBits(netmask);
        setNetmask(netmask);
        System.out.println(snetbits);
        IPAddress ipa[] = new IPAddress[(int) Math.pow(2, snetbits)];
        IPAddress base = IPAddress.netmask(this.Class()).and(this);
        UnsignedByte ub = new UnsignedByte(32, this.toRawBinString());
        int msb = 32 - this.netBits() - 1;
        int lsb = 32 - this.netBits() - this.subnetBits(netmask);
        String perm[] = ub.permutationsSt(msb, lsb);
        if (perm == null) return null;
        int i = perm.length - 1;
        while (i >= 0) {
            IPAddress rawsnet = new IPAddress(new UnsignedByte(32, perm[i]));
            ipa[i] = rawsnet.xor(base);
            i--;
        }
        return ipa;
    }

    public String toBinString() {
        return getAddr()[3].toString() + "." + getAddr()[2].toString() + "." + getAddr()[1].toString() + "." + getAddr()[0].toString();
    }

    public String toRawBinString() {
        return getAddr()[3].toString() + getAddr()[2].toString() + getAddr()[1].toString() + getAddr()[0].toString();
    }

    public String toString() {
        return getAddr()[3].intValue() + "." + getAddr()[2].intValue() + "." + getAddr()[1].intValue() + "." + getAddr()[0].intValue();
    }

    public int hostBits(int subnetBits) {
        if (subnetBits == 0) return this.hostBits(); else return 32 - this.netBits() - subnetBits;
    }

    public int hostBits() {
        return 32 - this.netBits();
    }

    public int hosts() {
        return (int) Math.pow(2, this.hostBits()) - 2;
    }

    public int hosts(int subnetBits) {
        if (subnetBits == 0) return this.hosts(); else return (int) Math.pow(2, this.hostBits(subnetBits)) - 2;
    }

    public int subnetBitsBar(int barnot) {
        return barnot - this.netBits();
    }

    public int subnetBits(int subnets) {
        int n = 1;
        double form = 0;
        while (form < subnets) {
            form = Math.pow(2, n) - 2;
            n++;
        }
        return --n;
    }

    public int subnetBits(IPAddress netmask) {
        Integer subnetBits = netmask.oneCount() - IPAddress.netmask(this.Class()).oneCount();
        if (subnetBits == null) return 0;
        return subnetBits.intValue();
    }

    public int posSubnets(int subnets) {
        int n = 1;
        double form = 0;
        while (form < subnets) {
            form = Math.pow(2, n) - 2;
            n++;
        }
        return (int) form;
    }

    public static IPAddress netmask(String Class) {
        if (Class.compareTo("A") == 0) return new IPAddress("255.0.0.0");
        if (Class.compareTo("B") == 0) return new IPAddress("255.255.0.0");
        if (Class.compareTo("C") == 0) return new IPAddress("255.255.255.0");
        if (Class.compareTo("D") == 0) return new IPAddress("255.255.255.255");
        if (Class.compareTo("E") == 0) return null;
        return null;
    }

    public UnsignedByte fixMask(int allnetBits) {
        int i = 31;
        int j = allnetBits;
        String s = "";
        while (i >= 0) {
            if (j > 0) {
                s = s + "1";
            } else {
                s = s + "0";
            }
            i--;
            j--;
        }
        return new UnsignedByte(s.length(), s);
    }

    public int oneCount() {
        return getAddr()[0].oneCount() + getAddr()[1].oneCount() + getAddr()[2].oneCount() + getAddr()[3].oneCount();
    }

    public int zeroCount() {
        return getAddr()[0].zeroCount() + getAddr()[1].zeroCount() + getAddr()[2].zeroCount() + getAddr()[3].zeroCount();
    }

    public IPAddress and(IPAddress other) {
        UnsignedByte byte0 = this.getAddr()[0].and(other.getAddr()[0]);
        UnsignedByte byte1 = this.getAddr()[1].and(other.getAddr()[1]);
        UnsignedByte byte2 = this.getAddr()[2].and(other.getAddr()[2]);
        UnsignedByte byte3 = this.getAddr()[3].and(other.getAddr()[3]);
        return new IPAddress(byte3, byte2, byte1, byte0);
    }

    public IPAddress xor(IPAddress other) {
        UnsignedByte byte0 = this.getAddr()[0].xor(other.getAddr()[0]);
        UnsignedByte byte1 = this.getAddr()[1].xor(other.getAddr()[1]);
        UnsignedByte byte2 = this.getAddr()[2].xor(other.getAddr()[2]);
        UnsignedByte byte3 = this.getAddr()[3].xor(other.getAddr()[3]);
        return new IPAddress(byte3, byte2, byte1, byte0);
    }

    public IPAddress or(IPAddress other) {
        UnsignedByte byte0 = this.getAddr()[0].or(other.getAddr()[0]);
        UnsignedByte byte1 = this.getAddr()[1].or(other.getAddr()[1]);
        UnsignedByte byte2 = this.getAddr()[2].or(other.getAddr()[2]);
        UnsignedByte byte3 = this.getAddr()[3].or(other.getAddr()[3]);
        return new IPAddress(byte3, byte2, byte1, byte0);
    }

    public IPAddress not() {
        UnsignedByte byte0 = this.getAddr()[0].not();
        UnsignedByte byte1 = this.getAddr()[1].not();
        UnsignedByte byte2 = this.getAddr()[2].not();
        UnsignedByte byte3 = this.getAddr()[3].not();
        return new IPAddress(byte3, byte2, byte1, byte0);
    }

    public IPAddress[] segments(IPAddress netmask) {
        IPAddress ret[] = new IPAddress[2];
        ret[0] = this.and(netmask).increment();
        ret[1] = this.broadcast(netmask).decrement();
        return ret;
    }

    public IPAddress broadcast(IPAddress netmask) {
        IPAddress notnm = netmask.not();
        IPAddress ret = this.or(notnm);
        return ret;
    }

    public IPAddress increment() {
        IPAddress ret = this.copy();
        if (ret.getAddr()[0].intValue() == 255) {
            if (ret.getAddr()[1].intValue() == 255) {
                if (ret.getAddr()[2].intValue() == 255) {
                    if (ret.getAddr()[3].intValue() == 255) {
                        ret = new IPAddress(0, 0, 0, 0);
                    } else ret.getAddr()[3] = new UnsignedByte(8, getAddr()[3].intValue() + 1);
                } else ret.getAddr()[2] = new UnsignedByte(8, getAddr()[2].intValue() + 1);
            } else ret.getAddr()[1] = new UnsignedByte(8, getAddr()[1].intValue() + 1);
        } else ret.getAddr()[0] = new UnsignedByte(8, getAddr()[0].intValue() + 1);
        return ret;
    }

    public IPAddress decrement() {
        IPAddress ret = this.copy();
        if (ret.getAddr()[0].intValue() == 0) {
            if (ret.getAddr()[1].intValue() == 0) {
                if (ret.getAddr()[2].intValue() == 0) {
                    if (ret.getAddr()[3].intValue() == 0) {
                        ret = new IPAddress(255, 255, 255, 255);
                    } else ret.getAddr()[3] = new UnsignedByte(8, getAddr()[3].intValue() - 1);
                } else ret.getAddr()[2] = new UnsignedByte(8, getAddr()[2].intValue() - 1);
            } else ret.getAddr()[1] = new UnsignedByte(8, getAddr()[1].intValue() - 1);
        } else ret.getAddr()[0] = new UnsignedByte(8, getAddr()[0].intValue() - 1);
        return ret;
    }

    public IPAddress copy() {
        return new IPAddress(getAddr()[3], getAddr()[2], getAddr()[1], getAddr()[0]);
    }

    public int compareTo(Object o) throws ClassCastException {
        IPAddress address = null;
        if (o instanceof IPAddress) address = (IPAddress) o; else throw new ClassCastException("A IPAddress Object was expected!");
        if (!address.Class().equals(this.Class())) return address.Class().compareTo(this.Class()); else {
            UnsignedByte[] address2 = address.getAddr();
            return getAddr()[0].intValue() - address2[0].intValue();
        }
    }

    public String getGateWay() {
        return this.increment().toString();
    }

    public String getBroadcast() {
        return broadcast(netmask).toString();
    }

    public IPAddress getNetmask() {
        return netmask;
    }

    public void setNetmask(IPAddress netmask) {
        this.netmask = netmask;
    }

    public UnsignedByte[] getAddr() {
        return addr;
    }

    public void setAddr(UnsignedByte[] addr) {
        this.addr = addr;
    }
}
