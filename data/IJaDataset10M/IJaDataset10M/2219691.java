package com.j2memetronome.midi;

/**
 *
 * @author dmartins
 */
public interface DrumKit {

    byte[] DRUM_NUMBERS = { 0x23, 0x24, 0x25, 0x26, 0x27, 0x28, 0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x3A, 0x3B, 0x22, 0x21 };

    int ACOUSTIC_BASS_DRUM = 0;

    int BASS_DRUM = 1;

    int SIDE_STICK = 2;

    int ACOUSTIC_SNARE = 3;

    int HAND_CLAP = 4;

    int ELECTRIC_SNARE = 5;

    int LOW_FLOOR_TOM = 6;

    int CLOSED_HI_HAT = 7;

    int HIGH_FLOOR_TOM = 8;

    int PEDAL_HI_HAT = 9;

    int LOW_TOM = 10;

    int OPEN_HI_HAT = 11;

    int LOW_MID_TOM = 12;

    int HI_MID_TOM = 13;

    int CRASH_CYMBAL_1 = 14;

    int HIGH_TOM = 15;

    int RIDE_CYMBAL_1 = 16;

    int CHINESE_CYMBAL = 17;

    int RIDE_BELL = 18;

    int TAMBOURINE = 19;

    int SPLASH_CYMBAL = 20;

    int COWBELL = 21;

    int CRASH_CYMBAL_2 = 22;

    int VIBRASLAP = 23;

    int RIDE_CYMBAL2 = 24;

    int METRONOME_BELL = 25;

    int METRONOME_CLICK = 26;
}
