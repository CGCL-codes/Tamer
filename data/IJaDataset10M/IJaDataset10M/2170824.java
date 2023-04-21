package com.otom.bcel.basic;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.otom.bcel.Mapper;
import com.otom.bcel.annotations.MapClassTo;

public class PrimitiveToPrimitiveTest {

    @Test
    public void testSourceToDestination() {
        PrimitiveToPrimitiveSource source = new PrimitiveToPrimitiveSource();
        PrimitiveToPrimitiveDestination destination = new PrimitiveToPrimitiveDestination();
        source.setCharToInt('a');
        source.setCharToByte('a');
        source.setCharToShort('a');
        source.setCharToDouble('a');
        source.setCharToFloat('a');
        source.setCharToLong('a');
        source.setIntToByte(10);
        source.setIntToShort(10);
        source.setIntToLong(10);
        source.setIntToFloat(10);
        source.setIntToDouble(10);
        source.setLongToByte(10);
        source.setLongToShort(10);
        source.setLongToFloat(10);
        source.setLongToDouble(10);
        source.setFloatToDouble(10);
        source.setFloatToByte(10);
        source.setFloatToShort(10);
        source.setDoubleToByte(10);
        source.setDoubleToShort(10);
        Mapper<PrimitiveToPrimitiveSource, PrimitiveToPrimitiveDestination> mapper = new Mapper<PrimitiveToPrimitiveSource, PrimitiveToPrimitiveDestination>();
        mapper.map(source, destination);
        assertEquals(97d, destination.getCharToDouble(), 0);
        assertEquals(97f, destination.getCharToFloat(), 0);
        assertEquals(97, destination.getCharToByte(), 0);
        assertEquals(97, destination.getCharToShort(), 0);
        assertEquals(97, destination.getCharToInt());
        assertEquals(97L, destination.getCharToLong());
        assertEquals(10, destination.getIntToShort(), 0);
        assertEquals(10, destination.getIntToByte(), 0);
        assertEquals(10L, destination.getIntToLong(), 0);
        assertEquals(10f, destination.getIntToFloat(), 0);
        assertEquals(10d, destination.getIntToDouble(), 0);
        assertEquals(10, destination.getLongToByte(), 0);
        assertEquals(10, destination.getLongToShort(), 0);
        assertEquals(10f, destination.getLongToFloat(), 0);
        assertEquals(10d, destination.getLongToDouble(), 0);
        assertEquals(10d, destination.getFloatToDouble(), 0);
        assertEquals(10, destination.getFloatToByte(), 0);
        assertEquals(10, destination.getFloatToShort(), 0);
        assertEquals(10, destination.getFloatToByte(), 0);
        assertEquals(10, destination.getDoubleToShort(), 0);
    }

    @Test
    public void testDestinationToSource() {
        PrimitiveToPrimitiveSource source = new PrimitiveToPrimitiveSource();
        PrimitiveToPrimitiveDestination destination = new PrimitiveToPrimitiveDestination();
        destination.setCharToInt(97);
        destination.setCharToDouble(97);
        destination.setCharToFloat(97);
        destination.setCharToLong(97);
        destination.setCharToByte((byte) 97);
        destination.setCharToShort((short) 97);
        destination.setIntToByte((byte) 10);
        destination.setIntToShort((short) 10);
        destination.setIntToLong(10);
        destination.setIntToFloat(10);
        destination.setIntToDouble(10);
        destination.setLongToFloat(10);
        destination.setLongToByte((byte) 10);
        destination.setLongToShort((short) 10);
        destination.setLongToDouble(10);
        destination.setFloatToDouble(10);
        destination.setFloatToByte((byte) 10);
        destination.setFloatToShort((short) 10);
        destination.setDoubleToByte((byte) 10);
        destination.setDoubleToShort((short) 10);
        Mapper<PrimitiveToPrimitiveDestination, PrimitiveToPrimitiveSource> mapper = new Mapper<PrimitiveToPrimitiveDestination, PrimitiveToPrimitiveSource>();
        mapper.map(destination, source);
        assertEquals('a', source.getCharToByte());
        assertEquals('a', source.getCharToShort());
        assertEquals('a', source.getCharToDouble());
        assertEquals('a', source.getCharToFloat());
        assertEquals('a', source.getCharToInt());
        assertEquals('a', source.getCharToLong());
        assertEquals(10, source.getIntToByte());
        assertEquals(10, source.getIntToShort());
        assertEquals(10, source.getIntToLong());
        assertEquals(10, source.getIntToFloat());
        assertEquals(10, source.getIntToDouble());
        assertEquals(10, source.getLongToByte());
        assertEquals(10, source.getLongToShort());
        assertEquals(10L, source.getLongToFloat());
        assertEquals(10L, source.getLongToDouble());
        assertEquals(10f, source.getFloatToDouble(), 0);
        assertEquals(10, source.getFloatToByte(), 0);
        assertEquals(10, source.getFloatToShort(), 0);
        assertEquals(10, source.getDoubleToByte(), 0);
        assertEquals(10, source.getDoubleToShort(), 0);
    }

    @MapClassTo(value = PrimitiveToPrimitiveDestination.class, biDirectional = true)
    public class PrimitiveToPrimitiveSource {

        /** Test primitive to primitive conversion **/
        private char charToInt;

        private char charToLong;

        private char charToFloat;

        private char charToDouble;

        private char charToByte;

        private char charToShort;

        private int intToShort;

        private int intToByte;

        private int intToDouble;

        private int intToFloat;

        private int intToLong;

        private long longToShort;

        private long longToByte;

        private long longToDouble;

        private int longToFloat;

        private float floatToDouble;

        private float floatToByte;

        private float floatToShort;

        private double doubleToByte;

        private double doubleToShort;

        private byte byteToShort;

        public byte getByteToShort() {
            return byteToShort;
        }

        public void setByteToShort(byte byteToShort) {
            this.byteToShort = byteToShort;
        }

        public float getFloatToByte() {
            return floatToByte;
        }

        public void setFloatToByte(float floatToByte) {
            this.floatToByte = floatToByte;
        }

        public float getFloatToShort() {
            return floatToShort;
        }

        public void setFloatToShort(float floatToShort) {
            this.floatToShort = floatToShort;
        }

        public double getDoubleToByte() {
            return doubleToByte;
        }

        public void setDoubleToByte(double doubleToByte) {
            this.doubleToByte = doubleToByte;
        }

        public double getDoubleToShort() {
            return doubleToShort;
        }

        public void setDoubleToShort(double doubleToShort) {
            this.doubleToShort = doubleToShort;
        }

        public long getLongToShort() {
            return longToShort;
        }

        public void setLongToShort(long longToShort) {
            this.longToShort = longToShort;
        }

        public long getLongToByte() {
            return longToByte;
        }

        public void setLongToByte(long longToByte) {
            this.longToByte = longToByte;
        }

        public char getCharToInt() {
            return charToInt;
        }

        public void setCharToInt(char charToInt) {
            this.charToInt = charToInt;
        }

        public char getCharToLong() {
            return charToLong;
        }

        public void setCharToLong(char charToLong) {
            this.charToLong = charToLong;
        }

        public char getCharToFloat() {
            return charToFloat;
        }

        public void setCharToFloat(char charToFloat) {
            this.charToFloat = charToFloat;
        }

        public char getCharToDouble() {
            return charToDouble;
        }

        public void setCharToDouble(char charToDouble) {
            this.charToDouble = charToDouble;
        }

        public int getIntToDouble() {
            return intToDouble;
        }

        public void setIntToDouble(int intToDouble) {
            this.intToDouble = intToDouble;
        }

        public int getIntToFloat() {
            return intToFloat;
        }

        public void setIntToFloat(int intToFloat) {
            this.intToFloat = intToFloat;
        }

        public int getIntToLong() {
            return intToLong;
        }

        public void setIntToLong(int intToLong) {
            this.intToLong = intToLong;
        }

        public long getLongToDouble() {
            return longToDouble;
        }

        public void setLongToDouble(long longToDouble) {
            this.longToDouble = longToDouble;
        }

        public int getLongToFloat() {
            return longToFloat;
        }

        public void setLongToFloat(int longToFloat) {
            this.longToFloat = longToFloat;
        }

        public float getFloatToDouble() {
            return floatToDouble;
        }

        public void setFloatToDouble(float floatToDouble) {
            this.floatToDouble = floatToDouble;
        }

        public char getCharToByte() {
            return charToByte;
        }

        public void setCharToByte(char charToByte) {
            this.charToByte = charToByte;
        }

        public char getCharToShort() {
            return charToShort;
        }

        public void setCharToShort(char charToShort) {
            this.charToShort = charToShort;
        }

        public int getIntToShort() {
            return intToShort;
        }

        public void setIntToShort(int intToShort) {
            this.intToShort = intToShort;
        }

        public int getIntToByte() {
            return intToByte;
        }

        public void setIntToByte(int intToByte) {
            this.intToByte = intToByte;
        }
    }

    public class PrimitiveToPrimitiveDestination {

        /** Test primitive to primitive conversion **/
        private int charToInt;

        private long charToLong;

        private float charToFloat;

        private double charToDouble;

        private byte charToByte;

        private short charToShort;

        private short intToShort;

        private byte intToByte;

        private double intToDouble;

        private float intToFloat;

        private long intToLong;

        private double longToDouble;

        private float longToFloat;

        private double floatToDouble;

        private short longToShort;

        private byte longToByte;

        private byte floatToByte;

        private short floatToShort;

        private byte doubleToByte;

        private short doubleToShort;

        private short byteToShort;

        public short getByteToShort() {
            return byteToShort;
        }

        public void setByteToShort(short byteToShort) {
            this.byteToShort = byteToShort;
        }

        public byte getFloatToByte() {
            return floatToByte;
        }

        public void setFloatToByte(byte floatToByte) {
            this.floatToByte = floatToByte;
        }

        public short getFloatToShort() {
            return floatToShort;
        }

        public void setFloatToShort(short floatToShort) {
            this.floatToShort = floatToShort;
        }

        public byte getDoubleToByte() {
            return doubleToByte;
        }

        public void setDoubleToByte(byte doubleToByte) {
            this.doubleToByte = doubleToByte;
        }

        public short getDoubleToShort() {
            return doubleToShort;
        }

        public void setDoubleToShort(short doubleToShort) {
            this.doubleToShort = doubleToShort;
        }

        public short getLongToShort() {
            return longToShort;
        }

        public void setLongToShort(short longToShort) {
            this.longToShort = longToShort;
        }

        public byte getLongToByte() {
            return longToByte;
        }

        public void setLongToByte(byte longToByte) {
            this.longToByte = longToByte;
        }

        public byte getCharToByte() {
            return charToByte;
        }

        public void setCharToByte(byte charToByte) {
            this.charToByte = charToByte;
        }

        public short getCharToShort() {
            return charToShort;
        }

        public void setCharToShort(short charToShort) {
            this.charToShort = charToShort;
        }

        public int getCharToInt() {
            return charToInt;
        }

        public void setCharToInt(int charToInt) {
            this.charToInt = charToInt;
        }

        public long getCharToLong() {
            return charToLong;
        }

        public void setCharToLong(long charToLong) {
            this.charToLong = charToLong;
        }

        public float getCharToFloat() {
            return charToFloat;
        }

        public void setCharToFloat(float charToFloat) {
            this.charToFloat = charToFloat;
        }

        public double getCharToDouble() {
            return charToDouble;
        }

        public void setCharToDouble(double charToDouble) {
            this.charToDouble = charToDouble;
        }

        public double getIntToDouble() {
            return intToDouble;
        }

        public void setIntToDouble(double intToDouble) {
            this.intToDouble = intToDouble;
        }

        public float getIntToFloat() {
            return intToFloat;
        }

        public void setIntToFloat(float intToFloat) {
            this.intToFloat = intToFloat;
        }

        public long getIntToLong() {
            return intToLong;
        }

        public void setIntToLong(long intToLong) {
            this.intToLong = intToLong;
        }

        public double getLongToDouble() {
            return longToDouble;
        }

        public void setLongToDouble(double longToDouble) {
            this.longToDouble = longToDouble;
        }

        public float getLongToFloat() {
            return longToFloat;
        }

        public void setLongToFloat(float longToFloat) {
            this.longToFloat = longToFloat;
        }

        public double getFloatToDouble() {
            return floatToDouble;
        }

        public void setFloatToDouble(double floatToDouble) {
            this.floatToDouble = floatToDouble;
        }

        public short getIntToShort() {
            return intToShort;
        }

        public void setIntToShort(short intToShort) {
            this.intToShort = intToShort;
        }

        public byte getIntToByte() {
            return intToByte;
        }

        public void setIntToByte(byte intToByte) {
            this.intToByte = intToByte;
        }
    }
}
