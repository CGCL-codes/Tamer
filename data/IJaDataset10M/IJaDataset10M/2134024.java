package com.justin.topcoder;

public class RoomNumber {

    /**
	 * TopCoder - SRM 351 DIV2 - 250 points
	 * 
	 * You are going to stick the number of your room on the door. 
	 * The shop near your house suggests wonderful sets of plastic digits. 
	 * Each set contains exactly ten digits - one of each digit between 0 and 9, inclusive. 
	 * Return the number of sets required to write your room number. 
	 * Note that 6 can be used as 9 and vice versa.
	 * 
	 * @param roomNumber between 1 and 1000000, inclusive
	 * 
	 * @return the number of set
	 */
    public int numberOfSets(int roomNumber) {
        int[] flags = new int[10];
        int tempDigits;
        while (roomNumber > 0) {
            tempDigits = roomNumber % 10;
            flags[tempDigits]++;
            roomNumber = roomNumber / 10;
        }
        int max = Math.round((flags[6] + flags[9]) / 2);
        for (int i = 0; i < 10; i++) {
            if (i == 6 || i == 9) {
                if (Math.round(flags[i] / 2) > max) {
                    max = (flags[i] / 2);
                }
            } else {
                if (flags[i] > max) {
                    max = flags[i];
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        RoomNumber test = new RoomNumber();
        System.out.println(test.numberOfSets(888888));
    }
}
