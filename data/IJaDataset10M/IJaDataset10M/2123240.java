package Qaop;

import java.awt.image.ImageProducer;
import java.awt.image.ImageConsumer;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.event.KeyEvent;
import java.util.Vector;

public class Spectrum extends Thread implements Z80.Env, ImageProducer {

    final Z80 cpu = new Z80(this);

    int rom48k[] = new int[16384];

    int rom128k[] = new int[16384];

    private final int ram48k[][] = new int[3][];

    private final int whole_ram[][] = new int[8][];

    private final int ram0[] = new int[16384];

    private final int ram1[] = new int[16384];

    private final int ram2[] = new int[16384];

    private final int ram3[] = new int[16384];

    private final int ram4[] = new int[16384];

    private final int ram5[] = new int[16384];

    private final int ram6[] = new int[16384];

    private final int ram7[] = new int[16384];

    int rom[];

    int vram[];

    private boolean lock48k = false;

    private int mode;

    private boolean keymatrix = false;

    int if1rom[];

    final Audio audio;

    public int[] get_rambank(int i) {
        return whole_ram[i];
    }

    private void write_ram(int addr, int v) {
        ram48k[(addr & 0xc000) >> 0xe][addr & 0x3fff] = v;
    }

    /**
         * read from video ram
         * @param addr int
         * @return int
         */
    private int read_vram(int addr) {
        return vram[addr];
    }

    private int read_ram(int addr) {
        return ram48k[(addr & 0xc000) >> 0xe][addr & 0x3fff];
    }

    public void setKeymatrix(boolean _keymatrix) {
        keymatrix = _keymatrix;
    }

    Spectrum(int mode) {
        super("Spectrum");
        this.mode = mode;
        keymatrix = true;
        rom = mode == Qaop.MODE_48K ? rom48k : rom128k;
        for (int i = 0; i < 8; i++) keyboard[i] = 0xFF;
        whole_ram[0] = ram0;
        whole_ram[1] = ram1;
        whole_ram[2] = ram2;
        whole_ram[3] = ram3;
        whole_ram[4] = ram4;
        whole_ram[5] = ram5;
        whole_ram[6] = ram6;
        whole_ram[7] = ram7;
        ram48k[0] = whole_ram[5];
        ram48k[1] = whole_ram[2];
        ram48k[2] = whole_ram[0];
        vram = whole_ram[5];
        for (int i = 6144; i < 6912; i++) write_ram(i, 070);
        audio = Audio.getAudio();
        audio.open(3500000);
    }

    public void run() {
        try {
            frames();
        } catch (InterruptedException e) {
        }
        audio.close();
    }

    private void end_frame() {
        au_update();
        au_time -= 69888;
        refresh_screen();
        if (border != border_solid) {
            int t = refrb_t;
            refresh_border();
            if (t == BORDER_START) border_solid = border;
        }
        for (int i = 0; i < consumers.size(); ) {
            ImageConsumer c = (ImageConsumer) consumers.elementAt(consumers.size() - ++i);
            update_screen(c);
        }
        cpu.time -= 69888;
        if (--flash_count <= 0) {
            flash ^= 0xFF;
            flash_count = 16;
        }
        audio.level -= audio.level >> 8;
    }

    private long time;

    private int timet;

    private void frames() throws InterruptedException {
        time = System.currentTimeMillis();
        cpu.time = -14335;
        cpu.time_limit = 55553;
        au_time = cpu.time;
        for (; ; ) {
            byte[] tap = null;
            boolean tend = false;
            synchronized (this) {
                if (want_scale != scale) {
                    scale = want_scale;
                    width = scale * W;
                    height = scale * H;
                    notifyAll();
                    abort_consumers();
                }
                if (want_pause != paused) {
                    paused = want_pause;
                    notifyAll();
                }
                if (stop_loading) {
                    loading = stop_loading = false;
                    notifyAll();
                }
                if (!paused) {
                    tap = tape;
                    tend = tape_ready;
                    if (!loading && tap != null) loading = check_load();
                }
            }
            update_keyboard();
            refresh_new();
            if (paused) {
                cpu.time = cpu.time_limit;
            } else if (loading) {
                loading = do_load(tap, tend);
                cpu.time = cpu.time_limit;
            } else {
                cpu.interrupt(0xFF);
                cpu.execute();
            }
            end_frame();
            timet += 121;
            if (timet >= 125) {
                timet -= 125;
                time++;
            }
            time += 19;
            long t = System.currentTimeMillis();
            if (t < time) {
                t = time - t;
                sleep(t);
            } else {
                yield();
                if (interrupted()) break;
                t -= 100;
                if (t > time) time = t;
            }
        }
    }

    boolean paused = true;

    boolean want_pause = true;

    public synchronized void pause(boolean y) throws InterruptedException {
        want_pause = y;
        while (paused != want_pause) wait();
    }

    public synchronized void reset() {
        stop_loading();
        cpu.reset();
        au_reset();
        rom = mode == Qaop.MODE_48K ? rom48k : rom128k;
        lock48k = false;
        vram = whole_ram[5];
    }

    public final int m1(int addr, int ir) {
        int n = cpu.time - ctime;
        if (n > 0) cont(n);
        addr -= 0x4000;
        if ((addr & 0xC000) == 0) cont1(0);
        ctime = NOCONT;
        if ((ir & 0xC000) == 0x4000) ctime = cpu.time + 4;
        if (addr >= 0) return read_ram(addr);
        n = rom[addr += 0x4000];
        if (if1rom != null && (addr & 0xE8F7) == 0) {
            if (addr == 0x0008 || addr == 0x1708) {
                if (rom == rom48k) rom = if1rom;
            } else if (addr == 0x0700) {
                if (rom == if1rom) rom = rom48k;
            }
        }
        return n;
    }

    public final int mem(int addr) {
        int n = cpu.time - ctime;
        if (n > 0) cont(n);
        ctime = NOCONT;
        addr -= 0x4000;
        if (addr >= 0) {
            if (addr < 0x4000) {
                cont1(0);
                ctime = cpu.time + 3;
            }
            return read_ram(addr);
        }
        return rom[addr + 0x4000];
    }

    public final int mem16(int addr) {
        int n = cpu.time - ctime;
        if (n > 0) cont(n);
        ctime = NOCONT;
        int addr1 = addr - 0x3FFF;
        if ((addr1 & 0x3FFF) != 0) {
            if (addr1 < 0) return rom[addr] | rom[addr1 + 0x4000] << 8;
            if (addr1 < 0x4000) {
                cont1(0);
                cont1(3);
                ctime = cpu.time + 6;
            }
            return read_ram(addr - 0x4000) | read_ram(addr1) << 8;
        }
        switch(addr1 >>> 14) {
            case 0:
                cont1(3);
                ctime = cpu.time + 6;
                return rom[addr] | read_ram(0) << 8;
            case 1:
                cont1(0);
            case 2:
                return read_ram(addr - 0x4000) | read_ram(addr1) << 8;
            default:
                return read_ram(0xBFFF) | rom[0] << 8;
        }
    }

    public final void mem(int addr, int v) {
        int n = cpu.time - ctime;
        if (n > 0) cont(n);
        ctime = NOCONT;
        addr -= 0x4000;
        if (addr < 0x4000) {
            if (addr < 0) return;
            cont1(0);
            ctime = cpu.time + 3;
            if (addr < 6912 && read_ram(addr) != v) refresh_screen();
        }
        write_ram(addr, v);
    }

    public final void mem16(int addr, int v) {
        int addr1 = addr - 0x3FFF;
        if ((addr1 & 0x3FFF) != 0) {
            int n = cpu.time - ctime;
            if (n > 0) cont(n);
            ctime = NOCONT;
            if (addr1 < 0) return;
            if (addr1 >= 0x4000) {
                write_ram(addr1 - 1, v & 0xFF);
                write_ram(addr1, v >>> 8);
                return;
            }
        }
        mem(addr, v & 0xFF);
        cpu.time += 3;
        mem(addr + 1, v >>> 8);
        cpu.time -= 3;
    }

    protected byte ay_idx;

    private byte ula28;

    /**
         * 128k port
         * @param v int
         */
    private void out7ffd(int v) {
        if (mode == Qaop.MODE_48K || lock48k) return;
        int ram_bank = v & 0x7;
        ram48k[2] = whole_ram[ram_bank];
        if ((v & 0x08) == 0x08) {
            vram = whole_ram[7];
        } else {
            vram = whole_ram[5];
        }
        if ((v & 0x10) == 0x10) {
            rom = rom48k;
        } else {
            rom = rom128k;
        }
        if ((v & 0x20) == 0x20) {
            lock48k = true;
        }
    }

    public void out(int port, int v) {
        cont_port(port);
        if ((port & 0x0001) == 0) {
            ula28 = (byte) v;
            int n = v & 7;
            if (n != border) {
                refresh_border();
                border = (byte) n;
            }
            n = sp_volt[v >> 3 & 3];
            if (n != speaker) {
                au_update();
                speaker = n;
            }
        }
        if ((port & 0x8002) == 0x8000 && ay_enabled) {
            if ((port & 0x4000) != 0) ay_idx = (byte) (v & 15); else {
                au_update();
                ay_write(ay_idx, v);
            }
        }
        if (port == 0x7ffd) {
            out7ffd(v);
        }
    }

    public int in(int port) {
        cont_port(port);
        if ((port & 0x00E0) == 0) return kempston;
        if ((port & 0xC002) == 0xC000 && ay_enabled) {
            if (ay_idx >= 14 && (ay_reg[7] >> ay_idx - 8 & 1) == 0) return 0xFF;
            return ay_reg[ay_idx];
        }
        int v = 0xFF;
        if ((port & 0x0001) == 0) {
            for (int i = 0; i < 8; i++) if ((port & 0x100 << i) == 0) v &= keyboard[i];
            v &= ula28 << 2 | 0xBF;
        } else if (cpu.time >= 0) {
            int t = cpu.time;
            int y = t / 224;
            t %= 224;
            if (y < 192 && t < 124 && (t & 4) == 0) {
                int x = t >> 1 & 1 | t >> 2;
                if ((t & 1) == 0) x += y & 0x1800 | y << 2 & 0xE0 | y << 8 & 0x700; else x += 6144 | y << 2 & 0x3E0;
                v = read_ram(x);
            }
        }
        return v;
    }

    static final int NOCONT = 99999;

    int ctime;

    private final void cont1(int t) {
        t += cpu.time;
        if (t < 0 || t >= 191 * 224 + 126) return;
        if ((t & 7) >= 6) return;
        if (t % 224 < 126) cpu.time += 6 - (t & 7);
    }

    private final void cont(int n) {
        int s, k;
        int t = ctime;
        if (t + n <= 0) return;
        s = 191 * 224 + 126 - t;
        if (s < 0) return;
        s %= 224;
        if (s > 126) {
            n -= s - 126;
            if (n <= 0) return;
            t = 6;
            k = 15;
        } else {
            k = s >>> 3;
            s &= 7;
            if (s == 7) {
                s--;
                if (--n == 0) return;
            }
            t = s;
        }
        n = n - 1 >> 1;
        if (k < n) n = k;
        cpu.time += t + 6 * n;
    }

    private void cont_port(int port) {
        int n = cpu.time - ctime;
        if (n > 0) cont(n);
        if ((port & 0xC000) != 0x4000) {
            if ((port & 0x0001) == 0) cont1(1);
            ctime = NOCONT;
        } else {
            ctime = cpu.time;
            cont(2 + ((port & 1) << 1));
            ctime = cpu.time + 4;
        }
    }

    static byte[] palcolor(int m) {
        byte a[] = new byte[16];
        for (int n = 0; n < 8; n++) if ((n & m) != 0) {
            a[n] = (byte) 0xCD;
            a[n + 8] = (byte) 0xFF;
        }
        return a;
    }

    static final ColorModel cm = new IndexColorModel(8, 16, palcolor(2), palcolor(4), palcolor(1));

    private Vector consumers = new Vector(1);

    public synchronized void addConsumer(ImageConsumer ic) {
        try {
            ic.setDimensions(width, height);
            consumers.addElement(ic);
            ic.setHints(ic.RANDOMPIXELORDER | ic.SINGLEPASS);
            if (isConsumer(ic)) ic.setColorModel(cm);
            force_redraw();
        } catch (Exception e) {
            if (isConsumer(ic)) ic.imageComplete(ImageConsumer.IMAGEERROR);
        }
    }

    public boolean isConsumer(ImageConsumer ic) {
        return consumers.contains(ic);
    }

    public synchronized void removeConsumer(ImageConsumer ic) {
        consumers.removeElement(ic);
    }

    public void startProduction(ImageConsumer ic) {
        addConsumer(ic);
    }

    public void requestTopDownLeftRightResend(ImageConsumer ic) {
    }

    private void abort_consumers() {
        for (; ; ) {
            int s = consumers.size();
            if (s == 0) break;
            s--;
            ImageConsumer c = (ImageConsumer) consumers.elementAt(s);
            consumers.removeElementAt(s);
            c.imageComplete(ImageConsumer.IMAGEABORTED);
        }
    }

    private static final int Mh = 6;

    private static final int Mv = 6;

    static final int W = 256 + 8 * Mh * 2;

    static final int H = 192 + 8 * Mv * 2;

    int width = W, height = H, scale = 0, want_scale = 0;

    public synchronized void scale(int m) {
        want_scale = m;
        try {
            while (scale != m) wait();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    public int scale() {
        return scale;
    }

    private void force_redraw() {
        for (int i = 0; i < screen.length; i++) screen[i] = 0x1FF;
        border_solid = -1;
    }

    final int screen[] = new int[W / 8 * H];

    final int scrchg[] = new int[24];

    int flash_count = 16;

    int flash = 0x8000;

    private int refresh_t, refresh_a, refresh_b, refresh_s;

    private final void refresh_new() {
        refresh_t = refresh_b = 0;
        refresh_s = Mv * W + Mh;
        refresh_a = 0x1800;
        refrb_p = 0;
        refrb_t = BORDER_START;
        refrb_x = -Mh;
        refrb_y = -8 * Mv;
        refrb_r = 1;
    }

    private final void refresh_screen() {
        int ft = cpu.time;
        if (ft < refresh_t) return;
        final int flash = this.flash;
        int a = refresh_a, b = refresh_b;
        int t = refresh_t, s = refresh_s;
        do {
            int sch = 0;
            int v = read_vram(a) << 8 | read_vram(b++);
            if (v >= 0x8000) v ^= flash;
            v = canonic[v];
            if (v != screen[s]) {
                screen[s] = v;
                sch = 1;
            }
            v = read_vram(a + 1) << 8 | read_vram(b++);
            if (v >= 0x8000) v ^= flash;
            v = canonic[v];
            if (v != screen[++s]) {
                screen[s] = v;
                sch += 2;
            }
            if (sch != 0) scrchg[a - 0x1800 >> 5] |= sch << (a & 31);
            a += 2;
            t += 8;
            s++;
            if ((a & 31) != 0) continue;
            t += 96;
            s += 2 * Mh;
            a -= 32;
            b += 256 - 32;
            if ((b & 0x700) != 0) continue;
            a += 32;
            b += 32 - 0x800;
            if ((b & 0xE0) != 0) continue;
            b += 0x800 - 256;
            if (b >= 6144) {
                t = 99999;
                break;
            }
        } while (ft >= t);
        refresh_a = a;
        refresh_b = b;
        refresh_t = t;
        refresh_s = s;
    }

    private static final int BORDER_START = -224 * 8 * Mv - 4 * Mh + 4;

    private int refrb_p, refrb_t;

    private int refrb_x, refrb_y, refrb_r;

    private int brdchg_ud, brdchg_l, brdchg_r;

    private final void refresh_border() {
        int ft = cpu.time;
        if (ft < refrb_t) return;
        border_solid = -1;
        int t = refrb_t;
        int b = canonic[border << 11];
        int p = refrb_p;
        int x = refrb_x;
        int r = refrb_r;
        loop: do {
            if (refrb_y < 0 || refrb_y >= 192) {
                do {
                    if (screen[p] != b) {
                        screen[p] = b;
                        brdchg_ud |= r;
                    }
                    p++;
                    t += 4;
                    if (++x < 32 + Mh) continue;
                    x = -Mh;
                    t += 224 - 4 * (Mh + 32 + Mh);
                    if ((++refrb_y & 7) != 0) continue;
                    if (refrb_y == 0) {
                        r = 1;
                        continue loop;
                    } else if (refrb_y == 192 + 8 * Mv) {
                        t = 99999;
                        break loop;
                    }
                    r <<= 1;
                } while (ft >= t);
                break;
            }
            for (; ; ) {
                if (x < 0) {
                    for (; ; ) {
                        if (screen[p] != b) {
                            screen[p] = b;
                            brdchg_l |= r;
                        }
                        p++;
                        t += 4;
                        if (++x == 0) break;
                        if (ft < t) break loop;
                    }
                    x = 32;
                    p += 32;
                    t += 4 * 32;
                    if (ft < t) break loop;
                }
                for (; ; ) {
                    if (screen[p] != b) {
                        screen[p] = b;
                        brdchg_r |= r;
                    }
                    p++;
                    t += 4;
                    if (++x == 32 + Mh) break;
                    if (ft < t) break loop;
                }
                x = -Mh;
                t += 224 - 4 * (Mh + 32 + Mh);
                if ((++refrb_y & 7) == 0) {
                    if (refrb_y == 192) {
                        r = 1 << Mv;
                        continue loop;
                    }
                    r <<= 1;
                }
                if (ft < t) break loop;
            }
        } while (ft >= t);
        refrb_r = r;
        refrb_x = x;
        refrb_p = p;
        refrb_t = t;
    }

    private final void update_box(ImageConsumer cons, int y, int x, int w, byte buf[]) {
        int si = y * W + x;
        int p = 0;
        x <<= 3;
        y <<= 3;
        int h, s;
        if (scale == 1) {
            s = w * 8;
            for (int n = 0; n < 8; n++) {
                for (int k = 0; k < w; k++) {
                    int m = screen[si++];
                    byte c0 = (byte) (m >>> 8 & 0xF);
                    byte c1 = (byte) (m >>> 12);
                    m &= 0xFF;
                    do buf[p++] = (m & 1) == 0 ? c0 : c1; while ((m >>>= 1) != 0);
                }
                si += (W / 8) - w;
            }
            h = 8;
        } else {
            h = scale << 3;
            s = w * h;
            for (int n = 0; n < 8; n++) {
                for (int k = 0; k < w; k++) {
                    int m = screen[si++];
                    byte c0 = (byte) (m >>> 8 & 0xF);
                    byte c1 = (byte) (m >>> 12);
                    m &= 0xFF;
                    do {
                        buf[p] = buf[p + 1] = buf[p + s] = buf[p + s + 1] = (m & 1) == 0 ? c0 : c1;
                        p += 2;
                    } while ((m >>>= 1) != 0);
                }
                p += s;
                si += (W / 8) - w;
            }
            x *= scale;
            y *= scale;
        }
        cons.setPixels(x, y, s, h, cm, buf, 0, s);
    }

    private void update_screen(ImageConsumer ic) {
        int m = brdchg_ud;
        brdchg_ud = 0;
        int bl = brdchg_l;
        brdchg_l = 0;
        int br = brdchg_r;
        brdchg_r = 0;
        boolean chg = (m | bl | br) != 0;
        byte buf[] = new byte[8 * W * scale * scale];
        if (m != 0) for (int r = 0; r < Mv; r++, m >>>= 1) if ((m & 1) != 0) update_box(ic, r, 0, Mh + 32 + Mh, buf);
        for (int r = 0; r < 24; r++) {
            int d = scrchg[r];
            scrchg[r] = 0;
            int x = Mh, n = 0;
            if ((bl & 1) != 0) {
                n = x;
                x = 0;
            }
            for (; ; ) {
                while ((d & 1) != 0) {
                    n++;
                    d >>>= 1;
                }
                if (n != 0) {
                    if (x + n == Mh + 32 && (br & 1) != 0) n += Mh;
                    chg = true;
                    update_box(ic, Mv + r, x, n, buf);
                    x += n;
                    if (x >= Mh + 32) break;
                }
                if (d == 0) {
                    if ((br & 1) == 0) break;
                    x = Mh + 32;
                    n = Mh;
                    continue;
                }
                do {
                    x++;
                    d >>>= 1;
                } while ((d & 1) == 0);
                n = 1;
                d >>>= 1;
            }
            bl >>>= 1;
            br >>>= 1;
        }
        if (m != 0) for (int r = Mv + 24; r < Mv + 24 + Mv; r++, m >>>= 1) if ((m & 1) != 0) update_box(ic, r, 0, Mh + 32 + Mh, buf);
        if (chg) ic.imageComplete(ImageConsumer.SINGLEFRAMEDONE);
    }

    static final int canonic[] = new int[32768];

    static {
        for (int a = 0; a < 0x8000; a += 0x100) {
            int b = a >> 3 & 0x0800;
            int p = a >> 3 & 0x0700;
            int i = a & 0x0700;
            if (p != 0) p |= b;
            if (i != 0) i |= b;
            canonic[a] = p << 4 | 0xFF;
            canonic[a | 0xFF] = i << 4 | 0xFF;
            for (int m = 1; m < 255; m += 2) {
                if (i != p) {
                    int xm = m >>> 4 | m << 4;
                    xm = xm >>> 2 & 0x33 | xm << 2 & 0xCC;
                    xm = xm >>> 1 & 0x55 | xm << 1 & 0xAA;
                    canonic[a | m] = i << 4 | p | xm;
                    canonic[a | m ^ 0xFF] = p << 4 | i | xm;
                } else canonic[a | m] = canonic[a | m ^ 0xFF] = p << 4 | 0xFF;
            }
        }
    }

    byte border = (byte) 7;

    byte border_solid = -1;

    static final int CHANNEL_VOLUME = 26000;

    static final int SPEAKER_VOLUME = 50000;

    boolean ay_enabled;

    void ay(boolean y) {
        if (!y) ay_mix = 0;
        ay_enabled = y;
    }

    private int speaker;

    private static final int sp_volt[];

    private final byte ay_reg[] = new byte[16];

    private int ay_aper, ay_bper, ay_cper, ay_nper, ay_eper;

    private int ay_acnt, ay_bcnt, ay_ccnt, ay_ncnt, ay_ecnt;

    private int ay_gen, ay_mix, ay_ech, ay_dis;

    private int ay_avol, ay_bvol, ay_cvol;

    private int ay_noise = 1;

    private int ay_ekeep;

    private boolean ay_div16;

    private int ay_eattack, ay_ealt, ay_estep;

    private static final int ay_volt[];

    void ay_write(int n, int v) {
        switch(n) {
            case 0:
                ay_aper = ay_aper & 0xF00 | v;
                break;
            case 1:
                ay_aper = ay_aper & 0x0FF | (v &= 15) << 8;
                break;
            case 2:
                ay_bper = ay_bper & 0xF00 | v;
                break;
            case 3:
                ay_bper = ay_bper & 0x0FF | (v &= 15) << 8;
                break;
            case 4:
                ay_cper = ay_cper & 0xF00 | v;
                break;
            case 5:
                ay_cper = ay_cper & 0x0FF | (v &= 15) << 8;
                break;
            case 6:
                ay_nper = v &= 31;
                break;
            case 7:
                ay_mix = ~(v | ay_dis);
                break;
            case 8:
            case 9:
            case 10:
                int a = v &= 31, x = 011 << (n - 8);
                if (v == 0) {
                    ay_dis |= x;
                    ay_ech &= ~x;
                } else if (v < 16) {
                    ay_dis &= (x = ~x);
                    ay_ech &= x;
                } else {
                    ay_dis &= ~x;
                    ay_ech |= x;
                    a = ay_estep ^ ay_eattack;
                }
                ay_mix = ~(ay_reg[7] | ay_dis);
                a = ay_volt[a];
                switch(n) {
                    case 8:
                        ay_avol = a;
                        break;
                    case 9:
                        ay_bvol = a;
                        break;
                    case 10:
                        ay_cvol = a;
                        break;
                }
                break;
            case 11:
                ay_eper = ay_eper & 0xFF00 | v;
                break;
            case 12:
                ay_eper = ay_eper & 0xFF | v << 8;
                break;
            case 13:
                ay_eshape(v &= 15);
                break;
        }
        ay_reg[n] = (byte) v;
    }

    private void ay_eshape(int v) {
        if (v < 8) v = v < 4 ? 1 : 7;
        ay_ekeep = (v & 1) != 0 ? 1 : -1;
        ay_ealt = (v + 1 & 2) != 0 ? 15 : 0;
        ay_eattack = (v & 4) != 0 ? 15 : 0;
        ay_estep = 15;
        ay_ecnt = -1;
        ay_echanged();
    }

    private void ay_echanged() {
        int v = ay_volt[ay_estep ^ ay_eattack];
        int x = ay_ech;
        if ((x & 1) != 0) ay_avol = v;
        if ((x & 2) != 0) ay_bvol = v;
        if ((x & 4) != 0) ay_cvol = v;
    }

    private int ay_tick() {
        int x = 0;
        if ((--ay_acnt & ay_aper) == 0) {
            ay_acnt = -1;
            x ^= 1;
        }
        if ((--ay_bcnt & ay_bper) == 0) {
            ay_bcnt = -1;
            x ^= 2;
        }
        if ((--ay_ccnt & ay_cper) == 0) {
            ay_ccnt = -1;
            x ^= 4;
        }
        if (ay_div16 ^= true) {
            ay_gen ^= x;
            return x & ay_mix;
        }
        if ((--ay_ncnt & ay_nper) == 0) {
            ay_ncnt = -1;
            if ((ay_noise & 1) != 0) {
                x ^= 070;
                ay_noise ^= 0x28000;
            }
            ay_noise >>= 1;
        }
        if ((--ay_ecnt & ay_eper) == 0) {
            ay_ecnt = -1;
            if (ay_ekeep != 0) {
                if (ay_estep == 0) {
                    ay_eattack ^= ay_ealt;
                    ay_ekeep >>= 1;
                    ay_estep = 16;
                }
                ay_estep--;
                if (ay_ech != 0) {
                    ay_echanged();
                    x |= 0x100;
                }
            }
        }
        ay_gen ^= x;
        return x & ay_mix;
    }

    private int au_value() {
        int g = ay_mix & ay_gen;
        int v = speaker;
        if ((g & 011) == 0) v += ay_avol;
        if ((g & 022) == 0) v += ay_bvol;
        if ((g & 044) == 0) v += ay_cvol;
        return v;
    }

    private int au_time;

    private int au_val, au_dt;

    private void au_update() {
        int t = cpu.time;
        au_time += (t -= au_time);
        int dv = au_value() - au_val;
        if (dv != 0) {
            au_val += dv;
            audio.step(0, dv);
        }
        int dt = au_dt;
        for (; t >= dt; dt += 16) {
            if (ay_tick() == 0) continue;
            dv = au_value() - au_val;
            if (dv == 0) continue;
            au_val += dv;
            audio.step(dt, dv);
            t -= dt;
            dt = 0;
        }
        au_dt = dt - t;
        audio.step(t, 0);
    }

    void au_reset() {
        speaker = 0;
        ay_mix = ay_gen = 0;
        ay_avol = ay_bvol = ay_cvol = 0;
        ay_ekeep = 0;
        ay_dis = 077;
    }

    static boolean muted = false;

    static int volume = 50;

    public void mute(boolean v) {
        muted = v;
        setvol();
    }

    int volumeChg(int chg) {
        int v = volume + chg;
        if (v < 0) v = 0; else if (v > 100) v = 100;
        volume = v;
        setvol();
        return v;
    }

    static {
        sp_volt = new int[4];
        ay_volt = new int[16];
        setvol();
    }

    static void setvol() {
        double a = muted ? 0 : volume / 100.;
        a *= a;
        sp_volt[2] = (int) (SPEAKER_VOLUME * a);
        sp_volt[3] = (int) (SPEAKER_VOLUME * 1.06 * a);
        a *= CHANNEL_VOLUME;
        int n;
        ay_volt[n = 15] = (int) a;
        do {
            ay_volt[--n] = (int) (a *= 0.7071);
        } while (n > 1);
    }

    public final int keyboard[] = new int[8];

    public int kempston = 0;

    public final KeyEvent keys[] = new KeyEvent[8];

    static final int arrowsDefault[] = { 0143, 0124, 0134, 0144 };

    int arrows[] = arrowsDefault;

    void update_keyboard() {
        for (int i = 0; i < 8; i++) keyboard[i] = 0xFF;
        kempston = 0;
        int m[] = new int[] { -1, -1, -1, -1, -1 };
        int s = 0;
        synchronized (keys) {
            for (int i = 0; i < keys.length; i++) if (keys[i] != null) {
                int k = key(keys[i]);
                if (k < 0) continue;
                s |= k;
                if (k < 01000) pressed(k, m);
            }
        }
        if ((s & 0300) == 0) s |= s >>> 3 & 0300;
        if ((s & 0100) != 0) pressed(000, m);
        if ((s & 0200) != 0) pressed(017, m);
    }

    private final void pressed(int k, int m[]) {
        int a = k & 7, b = k >>> 3 & 7;
        int v = keyboard[a] & ~(1 << b);
        keyboard[a] = v;
        if (keymatrix) {
            int n = m[b];
            m[b] = a;
            if (n >= 0) v |= keyboard[n];
            for (n = 0; n < 8; n++) if ((keyboard[n] | v) != 0xFF) keyboard[n] = v;
        }
    }

    private int key(KeyEvent e) {
        int c = e.getKeyCode();
        int a = e.getKeyChar();
        int i = "[AQ10P\n ZSW29OL]XDE38IKMCFR47UJNVGT56YHB".indexOf((char) c);
        if (i >= 0) simple: {
            int s = 0;
            if (c >= KeyEvent.VK_0 && c <= KeyEvent.VK_9) {
                if (c != (int) a) break simple;
                if (e.isAltDown()) s = 0100;
            }
            return i | s;
        }
        if (a != '\0') {
            i = "\t\0\0!_\"\0\0:\0\0@);=\0\0\0\0#(\0+.?\0<$'\0-,/\0>%&\0^*".indexOf(a);
            if (i >= 0) return i | 0200;
        }
        switch(c) {
            case KeyEvent.VK_INSERT:
            case KeyEvent.VK_ESCAPE:
                return 0103;
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_LEFT:
                i = 0;
                break;
            case KeyEvent.VK_KP_DOWN:
            case KeyEvent.VK_DOWN:
                i = 3;
                break;
            case KeyEvent.VK_KP_UP:
            case KeyEvent.VK_UP:
                i = 2;
                break;
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_RIGHT:
                i = 1;
                break;
            case KeyEvent.VK_BACK_SPACE:
                return 0104;
            case KeyEvent.VK_SHIFT:
                return 01000;
            case KeyEvent.VK_CONTROL:
                kempston |= 0x10;
            case KeyEvent.VK_ALT:
                return 02000;
            default:
                return -1;
        }
        kempston |= 1 << (i ^ 1);
        return e.isAltDown() ? arrowsDefault[i] : (arrows != null ? arrows[i] : -1);
    }

    public void setArrows(String s) {
        if (s.substring(0, 2).equals("NO")) {
            arrows = null;
            return;
        }
        arrows = new int[4];
        for (int i = 0; i < 4; i++) {
            int c = -1;
            if (i < s.length()) c = "Caq10pE_zsw29olSxde38ikmcfr47ujnvgt56yhb".indexOf(s.charAt(i));
            if (c < 0) c = arrowsDefault[i];
            arrows[i] = c;
        }
    }

    private boolean check_load() {
        int pc = cpu.pc();
        if (cpu.ei() || pc < 0x56B || pc > 0x604) return false;
        int sp = cpu.sp();
        if (pc >= 0x5E3) {
            pc = mem16(sp);
            sp = (char) (sp + 2);
            if (pc == 0x5E6) {
                pc = mem16(sp);
                sp = (char) (sp + 2);
            }
        }
        if (pc < 0x56B || pc > 0x58E) return false;
        cpu.sp(sp);
        cpu.ex_af();
        if (tape_changed || tape_ready && tape.length <= tape_blk) {
            tape_changed = false;
            tape_blk = 0;
        }
        tape_pos = tape_blk;
        return true;
    }

    private boolean loading, stop_loading;

    private byte[] tape;

    private int tape_blk;

    private int tape_pos;

    private boolean tape_changed = false;

    private boolean tape_ready = false;

    public synchronized void stop_loading() {
        stop_loading = true;
        try {
            while (loading) wait();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        }
    }

    public synchronized void tape(byte[] tape, boolean end) {
        if (tape == null) tape_changed = true;
        tape_ready = end;
        this.tape = tape;
    }

    private final boolean do_load(byte[] tape, boolean ready) {
        if (tape_changed || (keyboard[7] & 1) == 0) {
            cpu.f(0);
            return false;
        }
        int p = tape_pos;
        int ix = cpu.ix();
        int de = cpu.de();
        int h, l = cpu.hl();
        h = l >> 8 & 0xFF;
        l &= 0xFF;
        int a = cpu.a();
        int f = cpu.f();
        int rf = -1;
        if (p == tape_blk) {
            p += 2;
            if (tape.length < p) {
                if (ready) {
                    cpu.pc(cpu.pop());
                    cpu.f(cpu.FZ);
                }
                return !ready;
            }
            tape_blk = p + (tape[p - 2] & 0xFF | tape[p - 1] << 8 & 0xFF00);
            h = 0;
        }
        for (; ; ) {
            if (p == tape_blk) {
                rf = cpu.FZ;
                break;
            }
            if (p == tape.length) {
                if (ready) rf = cpu.FZ;
                break;
            }
            l = tape[p++] & 0xFF;
            h ^= l;
            if (de == 0) {
                a = h;
                rf = 0;
                if (a < 1) rf = cpu.FC;
                break;
            }
            if ((f & cpu.FZ) == 0) {
                a ^= l;
                if (a != 0) {
                    rf = 0;
                    break;
                }
                f |= cpu.FZ;
                continue;
            }
            if ((f & cpu.FC) != 0) mem(ix, l); else {
                a = mem(ix) ^ l;
                if (a != 0) {
                    rf = 0;
                    break;
                }
            }
            ix = (char) (ix + 1);
            de--;
        }
        cpu.ix(ix);
        cpu.de(de);
        cpu.hl(h << 8 | l);
        cpu.a(a);
        if (rf >= 0) {
            f = rf;
            cpu.pc(cpu.pop());
        }
        cpu.f(f);
        tape_pos = p;
        return rf < 0;
    }

    public final void autoload() {
        rom = rom48k;
        cpu.i(0x3F);
        int p = 16384;
        do mem(p++, 0); while (p < 22528);
        do mem(p++, 070); while (p < 23296);
        do mem(p++, 0); while (p < 65536);
        mem16(23732, --p);
        p -= 0xA7;
        System.arraycopy(rom48k, 0x3E08, ram48k[2], p - 49152, 0xA8);
        mem16(23675, p--);
        mem(23608, 0x40);
        mem16(23730, p);
        mem16(23606, 0x3C00);
        mem(p--, 0x3E);
        cpu.sp(p);
        mem16(23613, p - 2);
        cpu.iy(0x5C3A);
        cpu.im(1);
        cpu.ei(true);
        mem16(23631, 0x5CB6);
        System.arraycopy(rom48k, 0x15AF, ram48k[0], 0x1CB6, 0x15);
        p = 0x5CB6 + 0x14;
        mem16(23639, p++);
        mem16(23635, p);
        mem16(23627, p);
        mem(p++, 0x80);
        mem16(23641, p);
        mem16(p, 0x22EF);
        mem16(p + 2, 0x0D22);
        mem(p + 4, 0x80);
        p += 5;
        mem16(23649, p);
        mem16(23651, p);
        mem16(23653, p);
        mem(23693, 070);
        mem(23695, 070);
        mem(23624, 070);
        mem16(23561, 0x0523);
        mem(23552, 0xFF);
        mem(23556, 0xFF);
        System.arraycopy(rom48k, 0x15C6, ram48k[0], 0x1C10, 14);
        mem16(23688, 0x1821);
        mem(23659, 2);
        mem16(23656, 0x5C92);
        mem(23611, 0x0C);
        cpu.pc(4788);
        au_reset();
    }
}
