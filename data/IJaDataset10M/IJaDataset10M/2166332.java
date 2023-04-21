package net.isammoc.hexapod.communication;

import net.isammoc.hexapod.HexapodServo;

public class ServoMessage extends BasicMessage {

    /**
	 * Set the byte corresponding to the specified {@code servo}.
	 * 
	 * @param servo
	 * @param value
	 *            a byte not equals to neither 0x00 nor 0xFF
	 * @throws IllegalArgumentException
	 *             If {@code value} is either 0x00 or 0xFF
	 */
    public void setByte(final HexapodServo servo, final byte value) throws IllegalArgumentException {
        if ((value == 0x00) || (value == (byte) 0xff)) {
            throw new IllegalArgumentException("value must NOT be 0x00 or 0xFF");
        }
        this.setByte(servo.ordinal(), value);
    }

    public void setUnsignedByte(final HexapodServo servo, final int value) {
        this.setUnsignedByte(servo.ordinal(), value);
    }

    public int getUnsignedByte(final HexapodServo servo) {
        return super.getUnsignedByte(servo.ordinal());
    }
}
