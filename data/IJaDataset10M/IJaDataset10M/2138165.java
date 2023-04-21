package de.joergjahnke.gameboy;

import de.joergjahnke.common.io.Serializable;
import de.joergjahnke.common.io.SerializationUtils;
import de.joergjahnke.common.util.Observer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Vector;

/**
 * Implements a Gameboy cartridge.<br>
 * For a good documentation on the Gameboy cartridge types see <a href='http://verhoeven272.nl/fruttenboel/Gameboy/pandocs.html.gz#thecartridgeheader'>http://verhoeven272.nl/fruttenboel/Gameboy/pandocs.html.gz#thecartridgeheader</a>.
 *
 * @author Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public class Cartridge implements Serializable, Observer {

    /**
     * Size of a ROM bank is 16k
     */
    public static final int ROM_BANK_SIZE = 0x4000;

    /**
     * Size of a RAM bank is 8k
     */
    public static final int RAM_BANK_SIZE = 0x2000;

    /**
     * start of the ROM bank area
     */
    private static final int ROM_BANK_AREA = 0x4000;

    /**
     * start of the RAM bank area
     */
    private static final int RAM_BANK_AREA = 0xa000;

    /**
     * do we default the RTC with the system clock
     */
    private static final boolean DEFAULT_RTC_WITH_SYSTEM_CLOCK = false;

    /**
     * supported file extensions for cartridge files
     */
    public static final Vector SUPPORTED_EXTENSIONS = new Vector();

    static {
        SUPPORTED_EXTENSIONS.addElement("gbc");
        SUPPORTED_EXTENSIONS.addElement("cgb");
        SUPPORTED_EXTENSIONS.addElement("gb");
    }

    /**
     * the cartridge title
     */
    private final String title;

    /**
     * use Gameboy color mode?
     */
    private final boolean isGBC;

    /**
     * cartridge type
     */
    private final int cartridgeType;

    /**
     * ROM size
     */
    private final int romSize;

    /**
     * RAM size
     */
    private final int ramSize;

    /**
     * ROM banks
     */
    private final byte[][] romBanks;

    /**
     * RAM banks
     */
    private final byte[][] ramBanks;

    /**
     * the gameboy the cartridge is attached to
     */
    private final Gameboy gameboy;

    /**
     * handles cartridge read & write operations
     */
    private final CartridgeImpl cartridgeImpl;

    /**
     * Create a new cartridge
     * 
     * @param   gameboy the gameboy this cartridge is attached to
     * @param	romStream	stream with cartridge data
     * @param	saveStream	stream with data of a saved game
     * @throws IOException	if the cartridge data cannot be read
     */
    public Cartridge(final Gameboy gameboy, final InputStream romStream, final InputStream saveStream) throws IOException {
        this.gameboy = gameboy;
        byte[] buffer = new byte[ROM_BANK_SIZE];
        romStream.read(buffer);
        final StringBuffer titleBuffer = new StringBuffer();
        for (int i = 0x134; i < 0x144 && buffer[i] != 0; ++i) {
            titleBuffer.append((char) buffer[i]);
        }
        this.title = titleBuffer.toString();
        this.isGBC = (buffer[0x143] & 0x80) != 0;
        this.cartridgeType = buffer[0x147] & 0xff;
        final String cartridgeTypeName = getCartridgeTypeName();
        if (cartridgeTypeName.startsWith("MBC1") || cartridgeTypeName.startsWith("ROM")) {
            this.cartridgeImpl = new MBC1CartridgeImpl();
        } else if (cartridgeTypeName.startsWith("MBC2")) {
            this.cartridgeImpl = new MBC2CartridgeImpl();
        } else if (cartridgeTypeName.startsWith("MBC3")) {
            this.cartridgeImpl = new MBC3CartridgeImpl();
        } else if (cartridgeTypeName.startsWith("MBC5")) {
            this.cartridgeImpl = new MBC5CartridgeImpl();
        } else {
            this.cartridgeImpl = new MBC1CartridgeImpl();
            this.gameboy.getLogger().warning("Unsupported cartridge type: " + cartridgeTypeName + "! Trying with MBC1 cartridge handling.");
        }
        this.romSize = 0x8000 << (buffer[0x148] & 0xff);
        this.ramSize = buffer[0x149] == 1 ? 0x0800 : buffer[0x149] == 2 ? 0x2000 : buffer[0x149] == 3 ? 0x8000 : 0;
        this.romBanks = new byte[this.romSize / ROM_BANK_SIZE][ROM_BANK_SIZE];
        System.arraycopy(buffer, 0, this.romBanks[0], 0, buffer.length);
        for (int i = 1; i < romBanks.length; ++i) {
            romStream.read(buffer);
            System.arraycopy(buffer, 0, this.romBanks[i], 0, buffer.length);
        }
        this.ramBanks = new byte[Math.max(1, this.ramSize / RAM_BANK_SIZE)][RAM_BANK_SIZE];
        if (saveStream != null) {
            buffer = new byte[RAM_BANK_SIZE];
            for (int i = 0; i < ramBanks.length; ++i) {
                saveStream.read(buffer);
                System.arraycopy(buffer, 0, this.ramBanks[i], 0, buffer.length);
            }
        }
    }

    /**
     * Get the cartridge title
     * 
     * @return	title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Check whether this cartridge supports Gameboy Color features
     * 
     * @return	true if we have a Gameboy Color cartridge
     */
    public boolean isGBC() {
        return this.isGBC;
    }

    /**
     * Get the cartridge type
     * 
     * @return	integer defining the cartridge type
     */
    public int getCartridgeType() {
        return this.cartridgeType;
    }

    /**
     * Get the cartridge type name
     * 
     * @return  cartridge type e.g. MBC1
     */
    public String getCartridgeTypeName() {
        switch(this.cartridgeType) {
            case 0x00:
                return "ROM Only";
            case 0x01:
                return "MBC1";
            case 0x02:
                return "MBC1+RAM";
            case 0x03:
                return "MBC1+RAM+Battery";
            case 0x05:
                return "MBC2";
            case 0x06:
                return "MBC2+Battery";
            case 0x08:
                return "ROM+RAM";
            case 0x09:
                return "ROM+RAM+Battery";
            case 0x0b:
                return "MMM1";
            case 0x0c:
                return "MMM1+RAM";
            case 0x0d:
                return "MMM1+RAM+Battery";
            case 0x0f:
                return "MBC3+Timer+Battery";
            case 0x10:
                return "MBC3+Timer+RAM+Battery";
            case 0x11:
                return "MBC3";
            case 0x12:
                return "MBC3+RAM";
            case 0x13:
                return "MBC3+RAM+Battery";
            case 0x15:
                return "MBC4";
            case 0x16:
                return "MBC4+RAM";
            case 0x17:
                return "MBC4+RAM+Battery";
            case 0x19:
                return "MBC5";
            case 0x1a:
                return "MBC5+RAM";
            case 0x1b:
                return "MBC5+RAM+Battery";
            case 0x1c:
                return "MBC5+Rumble";
            case 0x1d:
                return "MBC5+Rumble+RAM";
            case 0x1e:
                return "MBC5+Rumble+RAM+Battery";
            case 0xfe:
                return "HuC3";
            case 0xff:
                return "HuC1+RAM+Battery";
            default:
                return "Unknown (" + this.cartridgeType + ")";
        }
    }

    /**
     * Check whether the cartridge contains a battery buffering the RAM data
     * 
     * @return	true if the cartridge has battery support
     */
    public boolean hasBatterySupport() {
        return getCartridgeTypeName().indexOf("Battery") >= 0;
    }

    /**
     * Get the size of the cartridge ROM
     * 
     * @return	number of bytes
     */
    public int getROMSize() {
        return this.romSize;
    }

    /**
     * Get the size of the cartridge RAM
     * 
     * @return	number of bytes
     */
    public int getRAMSize() {
        return this.ramSize;
    }

    /**
     * Get the ROM banks of the cartridge
     * 
     * @return	ROM banks, each 32k in size
     */
    public byte[][] getROMBanks() {
        return this.romBanks;
    }

    /**
     * Get the RAM banks of the cartridge
     * 
     * @return	RAM banks, each 8k in size
     */
    public byte[][] getRAMBanks() {
        return this.ramBanks;
    }

    /**
     * Write to the cartridge
     * 
     * @param   adr address to write to
     * @param   data    data to write
     */
    public final void writeByte(final int adr, final byte data) {
        this.cartridgeImpl.writeByte(adr, data);
    }

    /**
     * Serialization for a Cartridge includes only the cartridge data that might be modified during
     * game execution. The cartridge itself needs to be loaded normally before serialization to restore
     * a running game.
     * 
     * @param out   stream to save to
     * @throws java.io.IOException if the cartridge cannot be saved
     */
    public void serialize(final DataOutputStream out) throws IOException {
        this.cartridgeImpl.serialize(out);
        for (int i = 0; i < this.ramBanks.length; ++i) {
            SerializationUtils.serialize(out, this.ramBanks[i]);
        }
    }

    public void deserialize(final DataInputStream in) throws IOException {
        this.cartridgeImpl.deserialize(in);
        for (int i = 0; i < this.ramBanks.length; ++i) {
            SerializationUtils.deserialize(in, this.ramBanks[i]);
        }
    }

    /**
     * abstract inner class implementing cartridge read & write operations
     */
    abstract class CartridgeImpl implements Serializable, Observer {

        /**
         * is writing to cartridge RAM enabled?
         */
        protected boolean areRAMWritesEnabled = true;

        /**
         * ROM/RAM banking mode
         */
        protected boolean isROMBankingMode = true;

        /**
         * Currently active 16k ROM bank in memory $4000-$7fff
         */
        protected int currentROMBank = 1;

        /**
         * Currently active 8k ROM bank in memory $a000-$bfff
         */
        protected int currentRAMBank = 0;

        /**
         * Write to the cartridge
         * 
         * @param   adr address to write to
         * @param   data    data to write
         */
        public abstract void writeByte(final int adr, final byte data);

        /**
         * Set new ROM bank to be active at $4000-$7fff of the main memory.
         * Also copies the new ROM from the cartridge to main memory.
         * 
         * @param romBank	ROM bank number to activate
         */
        protected final void setROMBank(final int romBank) {
            if (romBank != this.currentROMBank) {
                this.currentROMBank = romBank;
                System.arraycopy(getROMBanks()[this.currentROMBank], 0, gameboy.getCPU().memory, ROM_BANK_AREA, ROM_BANK_SIZE);
            }
        }

        /**
         * Set new RAM bank to be active at $a000-$bfff of the main memory.
         * Also copies the new RAM from the cartridge to main memory.
         * Old RAM is automatically written through to the cartridge and does not
         * need to be copied back to the cartridge.
         * 
         * @param ramBank	RAM bank number to activate
         */
        protected final void setRAMBank(final int ramBank) {
            if (ramBank != this.currentRAMBank) {
                this.currentRAMBank = ramBank;
                System.arraycopy(getRAMBanks()[this.currentRAMBank], 0, gameboy.getCPU().memory, RAM_BANK_AREA, RAM_BANK_SIZE);
            }
        }

        public void serialize(final DataOutputStream out) throws IOException {
            out.writeBoolean(this.areRAMWritesEnabled);
            out.writeBoolean(this.isROMBankingMode);
            out.writeInt(this.currentROMBank);
            out.writeInt(this.currentRAMBank);
        }

        public void deserialize(final DataInputStream in) throws IOException {
            this.areRAMWritesEnabled = in.readBoolean();
            this.isROMBankingMode = in.readBoolean();
            this.currentROMBank = in.readInt();
            this.currentRAMBank = in.readInt();
        }

        public void update(Object observed, Object arg) {
        }
    }

    /**
     * Implements cartridge writes to MBC2 type cartridges
     */
    class MBC1CartridgeImpl extends CartridgeImpl {

        public void writeByte(int adr, byte data) {
            switch(adr & 0xe000) {
                case 0x0000:
                    this.areRAMWritesEnabled = (data & 0x0f) == 0x0a;
                    break;
                case 0x2000:
                    {
                        setROMBank((this.currentROMBank & 0xe0) + Math.max(1, data & 0x1f));
                        break;
                    }
                case ROM_BANK_AREA:
                    {
                        if (this.isROMBankingMode) {
                            setROMBank((this.currentROMBank & 0x1f) + ((data & 0x03) << 5));
                        } else {
                            setRAMBank(data & 0x03);
                        }
                        break;
                    }
                case 0x6000:
                    this.isROMBankingMode = (data & 1) == 0;
                    break;
                case RAM_BANK_AREA:
                    if (this.areRAMWritesEnabled) {
                        gameboy.getCPU().memory[adr] = data;
                        ramBanks[this.currentRAMBank][adr & (RAM_BANK_SIZE - 1)] = data;
                    }
                    break;
            }
        }
    }

    /**
     * Implements cartridge writes to MBC2 type cartridges
     */
    class MBC2CartridgeImpl extends MBC1CartridgeImpl {

        public void writeByte(int adr, byte data) {
            switch(adr & 0xe000) {
                case 0x0000:
                    if ((adr & 0x100) == 0) {
                        super.writeByte(adr, data);
                    }
                    this.areRAMWritesEnabled = (data & 0x0f) == 0x0a;
                    break;
                case 0x2000:
                    {
                        if ((adr & 0x100) != 0) {
                            super.writeByte(adr, (byte) (data & 0x0f));
                        }
                        break;
                    }
                default:
                    super.writeByte(adr, data);
            }
        }
    }

    /**
     * Implements cartridge writes to MBCC type cartridges
     */
    class MBC3CartridgeImpl extends MBC1CartridgeImpl {

        /**
         * index of the RTC seconds register
         */
        private static final int SECONDS = 0x00;

        /**
         * index of the RTC minutes register
         */
        private static final int MINUTES = 0x01;

        /**
         * index of the RTC hours register
         */
        private static final int HOURS = 0x02;

        /**
         * index of the RTC register with the lower 8 bits of the day counter
         */
        private static final int DAYS_LOW = 0x03;

        /**
         * index of the RTC register with the higher bits of the day counter
         */
        private static final int DAYS_HIGH = 0x04;

        /**
         * real time clock
         */
        private final Date clock;

        /**
         * last CPY cycle when we updated the clock
         */
        private long lastRTCUpdate = 0;

        /**
         * RTC clock counter registers
         */
        private int[] rtc = new int[5];

        /**
         * currently active RTC register
         */
        private int rtcIndex = -1;

        /**
         * latch clock data on next write to $6000-$7fff?
         */
        private boolean latchRTC = false;

        /**
         * is the clock still running?
         */
        private boolean isClockActive = true;

        /**
         * speed of the Gameboy CPU, used when updating the clock
         */
        private long cpuSpeed = Gameboy.ORIGINAL_SPEED_CLASSIC;

        /**
         * Create a new MBC3CartridgeImpl setting the RTC of the cartridge to the current date
         */
        protected MBC3CartridgeImpl() {
            this.isROMBankingMode = false;
            this.clock = new Date();
            if (DEFAULT_RTC_WITH_SYSTEM_CLOCK) {
                final long days = getRTCDays();
                final long offset = (days - (days % 0x200)) * 24 * 60 * 60 * 1000;
                this.clock.setTime(this.clock.getTime() - offset);
            } else {
                this.clock.setTime(0);
            }
        }

        public void writeByte(int adr, byte data) {
            switch(adr & 0xe000) {
                case 0x2000:
                    {
                        setROMBank(Math.max(1, data & 0x7f));
                        break;
                    }
                case ROM_BANK_AREA:
                    if (data >= 0x08 && data <= 0x0c) {
                        this.rtcIndex = data - 0x08;
                        final byte[] memory_ = gameboy.getCPU().memory;
                        memory_[RAM_BANK_AREA] = (byte) this.rtc[this.rtcIndex];
                        for (int len = 1; len < RAM_BANK_SIZE; len <<= 1) {
                            System.arraycopy(memory_, RAM_BANK_AREA, memory_, RAM_BANK_AREA + len, len);
                        }
                        this.currentRAMBank = -1;
                    } else {
                        this.rtcIndex = -1;
                        super.writeByte(adr, (byte) (data & 0x03));
                    }
                    break;
                case 0x6000:
                    if (this.latchRTC && data == 0x01) {
                        latchClock();
                        this.latchRTC = false;
                    } else if (data == 0x00) {
                        this.latchRTC = true;
                    } else {
                        this.latchRTC = false;
                    }
                    break;
                case RAM_BANK_AREA:
                    switch(this.rtcIndex) {
                        case SECONDS:
                            updateClock();
                            this.clock.setTime(this.clock.getTime() + ((data & 0xff) - getRTCSeconds()) * 1000);
                            break;
                        case MINUTES:
                            updateClock();
                            this.clock.setTime(this.clock.getTime() + ((data & 0xff) - getRTCMinutes()) * 1000 * 60);
                            break;
                        case HOURS:
                            updateClock();
                            this.clock.setTime(this.clock.getTime() + ((data & 0xff) - getRTCHours()) * 1000 * 60 * 60);
                            break;
                        case DAYS_LOW:
                            updateClock();
                            this.clock.setTime(this.clock.getTime() + ((data & 0xff) - (getRTCDays() % 0x100)) * 1000 * 60 * 60 * 24);
                            break;
                        case DAYS_HIGH:
                            {
                                updateClock();
                                final int days = (getRTCDays() % 0x100) + ((data & 0x01) != 0 ? 0x100 : 0) + ((data & 0x80) != 0 ? 0x200 : 0);
                                this.clock.setTime(this.clock.getTime() + (days - getRTCDays()) * 1000 * 60 * 60 * 24);
                                this.isClockActive = (data & 0x40) == 0;
                                break;
                            }
                        default:
                            super.writeByte(adr, data);
                    }
                    break;
                default:
                    super.writeByte(adr, data);
            }
        }

        /**
         * Is the RTC clock currently running
         * 
         * @return  true if it is running, false if it is stopped
         */
        private boolean isClockActive() {
            return this.isClockActive;
        }

        /**
         * Get the seconds from the RTC
         * 
         * @return  0-59
         */
        private int getRTCSeconds() {
            return (int) ((this.clock.getTime() / 1000) % 60);
        }

        /**
         * Get the minutes from the RTC
         * 
         * @return  0-59
         */
        private int getRTCMinutes() {
            return (int) ((this.clock.getTime() / 1000 / 60) % 60);
        }

        /**
         * Get the hours from the RTC
         * 
         * @return  0-23
         */
        private int getRTCHours() {
            return (int) ((this.clock.getTime() / 1000 / 60 / 60) % 24);
        }

        /**
         * Get the days from the RTC
         * 
         * @return  value >= 0
         */
        private int getRTCDays() {
            return (int) (this.clock.getTime() / 1000 / 60 / 60 / 24);
        }

        /**
         * Update the RTC registers with the current time
         */
        private void latchClock() {
            updateClock();
            this.rtc[SECONDS] = getRTCSeconds();
            this.rtc[MINUTES] = getRTCMinutes();
            this.rtc[HOURS] = getRTCHours();
            this.rtc[DAYS_LOW] = getRTCDays() % 0x100;
            this.rtc[DAYS_HIGH] = ((getRTCDays() % 0x200) >> 8) + (isClockActive() ? 0 : 0x40) + (getRTCDays() >= 0x200 ? 0x80 : 0);
        }

        /**
         * Update the clock to the current time
         */
        private void updateClock() {
            if (isClockActive()) {
                final CPU cpu = gameboy.getCPU();
                final long passedMillis = (cpu.getCycles() - this.lastRTCUpdate) * 1000 / this.cpuSpeed;
                this.clock.setTime(this.clock.getTime() + passedMillis);
                this.lastRTCUpdate = cpu.getCycles();
            }
        }

        public void serialize(final DataOutputStream out) throws IOException {
            super.serialize(out);
            updateClock();
            out.writeLong(this.clock.getTime());
            out.writeBoolean(this.latchRTC);
            out.writeBoolean(this.isClockActive);
            SerializationUtils.serialize(out, this.rtc);
        }

        public void deserialize(final DataInputStream in) throws IOException {
            super.deserialize(in);
            this.clock.setTime(in.readLong());
            this.lastRTCUpdate = gameboy.getCPU().getCycles();
            this.latchRTC = in.readBoolean();
            this.isClockActive = in.readBoolean();
            SerializationUtils.deserialize(in, this.rtc);
        }

        public void update(Object observed, Object arg) {
            if (observed == gameboy.getCPU() && arg instanceof Long) {
                updateClock();
                this.cpuSpeed = ((Long) arg).longValue();
            }
        }
    }

    /**
     * Implements cartridge writes to MBC5 type cartridges
     */
    class MBC5CartridgeImpl extends MBC1CartridgeImpl {

        public void writeByte(int adr, byte data) {
            switch(adr & 0xe000) {
                case 0x2000:
                    setROMBank(Math.max(1, data));
                    break;
                case ROM_BANK_AREA:
                    setRAMBank(data & 0x07);
                    break;
                default:
                    super.writeByte(adr, data);
            }
        }
    }

    public void update(Object observed, Object arg) {
        this.cartridgeImpl.update(observed, arg);
    }
}
