package matrix;

import takatuka.drivers.msp430.ADConverter;
import takatuka.drivers.msp430.Gpio;

public class ADC {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        long start, stop;
        short res = 0, num;
        short[] data = new short[16];
        final byte ACC_POWER_PIN = 0x01;
        System.out.println("Program starts.");
        Gpio.config(Gpio.PORT_6, ACC_POWER_PIN, Gpio.CONFIG_OUTPUT_FULL);
        Gpio.setBits(Gpio.PORT_6, ACC_POWER_PIN);
        ADConverter.init(ADConverter.MODE_SEQUENCE_REPEAT, ADConverter.SHT_1024, ADConverter.SHT_64, ADConverter.INT_REF_1_5_V);
        ADConverter.openChannel(ADConverter.CHANNEL_1, ADConverter.REF_AVCC_AVSS);
        ADConverter.openChannel(ADConverter.CHANNEL_2, ADConverter.REF_AVCC_AVSS);
        ADConverter.openChannel(ADConverter.CHANNEL_TEMPERATURE, ADConverter.REF_VINTREFPOS_AVSS);
        num = 200;
        start = System.currentTimeMillis();
        for (int idx = 0; idx < num; idx++) {
            res = ADConverter.getSample(ADConverter.CHANNEL_TEMPERATURE, data);
        }
        stop = System.currentTimeMillis();
        System.out.print("First ");
        System.out.print(num);
        System.out.print(" samples were ret=");
        System.out.print(res);
        System.out.print("  val#0=");
        System.out.print(data[0]);
        System.out.print(" and took ");
        System.out.print(stop - start);
        System.out.print(" ms (");
        System.out.print((float) (stop - start) / (float) num);
        System.out.println(" ms each).");
        stop = System.currentTimeMillis() + 1 * 60 * 1000;
        do {
            res = ADConverter.getSample(ADConverter.CHANNEL_TEMPERATURE, data);
            System.out.print("ret=");
            System.out.print(res);
            for (int idx = 0; idx < res; idx++) {
                System.out.print("  val#");
                System.out.print(idx);
                System.out.print("=");
                System.out.print(data[idx]);
            }
            System.out.print("     \r");
        } while (System.currentTimeMillis() < stop);
        ADConverter.closeChannel(ADConverter.CHANNEL_1);
        ADConverter.closeChannel(ADConverter.CHANNEL_2);
        ADConverter.closeChannel(ADConverter.CHANNEL_TEMPERATURE);
        ADConverter.shutDown();
        System.out.println();
        System.out.println("Ready.\n");
    }
}
