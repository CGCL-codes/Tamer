package gameboy.core.cartridge;

import gameboy.core.driver.ClockDriver;

public class MBC3 implements MBC {

    private ClockDriver clock;

    private byte[] rom;

    private byte[] ram;

    private int romSize;

    private int ramSize;

    private int romBank;

    private int ramBank;

    private boolean ramEnable;

    private int clockRegister;

    private int clockLatch;

    private long clockTime;

    private int clockSeconds, clockMinutes, clockHours, clockDays, clockControl;

    private int clockLSeconds, clockLMinutes, clockLHours, clockLDays, clockLControl;

    public MBC3(byte[] rom, byte[] ram, ClockDriver clock) {
        this.clock = clock;
        setROM(rom);
        setRAM(ram);
    }

    public final void reset() {
        romBank = ROM_BANK_SIZE;
        ramBank = 0;
        ramEnable = false;
        clockTime = clock.getTime();
        clockLatch = clockRegister = 0;
        clockSeconds = clockMinutes = clockHours = clockDays = clockControl = 0;
        clockLSeconds = clockLMinutes = clockLHours = clockLDays = clockLControl = 0;
    }

    public final int read(int address) {
        if (address <= 0x3FFF) {
            return rom[address] & 0xFF;
        } else if (address <= 0x7FFF) {
            return rom[romBank + (address & 0x3FFF)] & 0xFF;
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            if (ramBank >= 0) {
                return ram[ramBank + (address & 0x1FFF)] & 0xFF;
            } else {
                if (clockRegister == 0x08) return clockLSeconds;
                if (clockRegister == 0x09) return clockLMinutes;
                if (clockRegister == 0x0A) return clockLHours;
                if (clockRegister == 0x0B) return clockLDays;
                if (clockRegister == 0x0C) return clockLControl;
            }
        }
        return 0xFF;
    }

    public final void write(int address, int data) {
        if (address <= 0x1FFF) {
            if (ramSize > 0) ramEnable = ((data & 0x0A) == 0x0A);
        } else if (address <= 0x3FFF) {
            if (data == 0) data = 1;
            romBank = ((data & 0x7F) << 14) & romSize;
        } else if (address <= 0x5FFF) {
            if (data >= 0x00 && data <= 0x03) {
                ramBank = (data << 13) & ramSize;
            } else {
                ramBank = -1;
                clockRegister = data;
            }
        } else if (address <= 0x7FFF) {
            if (clockLatch == 0 && data == 1) {
                latchClock();
            }
            if (data == 0 || data == 1) clockLatch = data;
        } else if (address >= 0xA000 && address <= 0xBFFF) {
            if (ramEnable) {
                if (ramBank >= 0) {
                    ram[ramBank + (address & 0x1FFF)] = (byte) data;
                } else {
                    updateClock();
                    if (clockRegister == 0x08) clockSeconds = data;
                    if (clockRegister == 0x09) clockMinutes = data;
                    if (clockRegister == 0x0A) clockHours = data;
                    if (clockRegister == 0x0B) clockDays = data;
                    if (clockRegister == 0x0C) clockControl = (clockControl & 0x80) | data;
                }
            }
        }
    }

    private final void latchClock() {
        updateClock();
        clockLSeconds = clockSeconds;
        clockLMinutes = clockMinutes;
        clockLHours = clockHours;
        clockLDays = clockDays & 0xFF;
        clockLControl = (clockControl & 0xFE) | ((clockDays >> 8) & 0x01);
    }

    private final void updateClock() {
        long now = clock.getTime();
        if ((clockControl & 0x40) == 0) {
            long elapsed = now - clockTime;
            while (elapsed >= 24 * 60 * 60) {
                elapsed -= 24 * 60 * 60;
                clockDays++;
            }
            while (elapsed >= 60 * 60) {
                elapsed -= 60 * 60;
                clockHours++;
            }
            while (elapsed >= 60) {
                elapsed -= 60;
                clockMinutes++;
            }
            clockSeconds += elapsed;
            while (clockSeconds >= 60) {
                clockSeconds -= 60;
                clockMinutes++;
            }
            while (clockMinutes >= 60) {
                clockMinutes -= 60;
                clockHours++;
            }
            while (clockHours >= 24) {
                clockHours -= 24;
                clockDays++;
            }
            while (clockDays >= 512) {
                clockDays -= 512;
                clockControl |= 0x80;
            }
        }
        clockTime = now;
    }

    private void setROM(byte[] buffer) {
        int banks = buffer.length / ROM_BANK_SIZE;
        if (banks < 2 || banks > 128) throw new RuntimeException("Invalid MCB3 ROM size");
        rom = buffer;
        romSize = ROM_BANK_SIZE * banks - 1;
    }

    private void setRAM(byte[] buffer) {
        int banks = buffer.length / RAM_BANK_SIZE;
        if (banks < 0 || banks > 4) throw new RuntimeException("Invalid MBC3 RAM size");
        ram = buffer;
        ramSize = RAM_BANK_SIZE * banks - 1;
    }
}
