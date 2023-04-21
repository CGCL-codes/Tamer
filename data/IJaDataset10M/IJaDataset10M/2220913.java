package bahar.bilgi;

import bahar.swing.DersEvent;
import org.bushe.swing.event.EventBus;
import org.jcaki.Chars;
import org.jcaki.SimpleTextWriter;
import org.jcaki.Strings;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class DersOturumu {

    private final AtomicInteger zamanSaniye = new AtomicInteger(0);

    public int yazilanHarfSayisi;

    public int gorunenHataSayisi;

    public int toplamHataSayisi;

    private String yazilan;

    public DersBilgisi dersBilgisi;

    private AtomicBoolean running = new AtomicBoolean(false);

    private final Set<OturumDinleyici> dinleyiciler = new HashSet<OturumDinleyici>();

    public void yazilanArttir() {
        yazilanHarfSayisi++;
        for (OturumDinleyici oturumDinleyici : dinleyiciler) {
            oturumDinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, harfHizHasapla());
        }
    }

    public String getYazilan() {
        return yazilan;
    }

    public void setYazilan(String yazilan) {
        this.yazilan = yazilan;
    }

    public String harfHizHasapla() {
        if (zamanSaniye.get() > 3) {
            float f = (60f * yazilanHarfSayisi / (float) zamanSaniye.get());
            return String.format("%.1f", f);
        } else return "--";
    }

    public void gorunenHataArttir() {
        gorunenHataSayisi++;
        for (OturumDinleyici oturumDinleyici : dinleyiciler) {
            oturumDinleyici.harfYazildi(yazilanHarfSayisi, gorunenHataSayisi, harfHizHasapla());
        }
    }

    public void tumHataArttir() {
        toplamHataSayisi++;
    }

    public DersOturumu(DersBilgisi dersBilgisi) {
        this.dersBilgisi = dersBilgisi;
        initialize();
    }

    public void addDinleyici(OturumDinleyici... dinleyici) {
        this.dinleyiciler.addAll(Arrays.asList(dinleyici));
    }

    Timer timer = new Timer("MyTimer");

    public void initialize() {
        TimerTask timerTask = new TimerTask() {

            public void run() {
                if (running.get()) {
                    zamanSaniye.incrementAndGet();
                    if (!dersBilgisi.sureVar(zamanSaniye())) {
                        EventBus.publish(new DersEvent(true));
                        return;
                    }
                    for (OturumDinleyici oturumDinleyici : dinleyiciler) {
                        oturumDinleyici.saniyeArtti(zamanSaniye.get(), harfHizHasapla());
                    }
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    public void durakla() {
        running.set(false);
    }

    public void devamEt() {
        running.set(true);
    }

    private int zamanSaniye() {
        return zamanSaniye.get();
    }

    public int sure() {
        return zamanSaniye();
    }

    public int yazilanKelimeSayisi() {
        if (!Strings.hasText(yazilan)) return 0; else return Strings.whiteSpacesToSingleSpace(yazilan).split("[ ]").length;
    }

    public static final char CHAR_cc = 'ç';

    public static final char CHAR_gg = 'ğ';

    public static final char CHAR_ii = 'ı';

    public static final char CHAR_oo = 'ö';

    public static final char CHAR_ss = 'ş';

    public static final char CHAR_uu = 'ü';

    public void stopTimer() {
        timer.cancel();
    }

    public void kaydet() throws IOException {
        String kullaniciAdi = dersBilgisi.kullaniciAdi.toLowerCase(new Locale("tr"));
        StringBuilder sb = new StringBuilder();
        for (char c : kullaniciAdi.toCharArray()) {
            if (c == CHAR_cc) sb.append('c'); else if (c == CHAR_gg) sb.append('g'); else if (c == CHAR_ii) sb.append('i'); else if (c == CHAR_oo) sb.append('o'); else if (c == CHAR_ss) sb.append('s'); else if (c == CHAR_uu) sb.append('u'); else if (Chars.isAsciiAlphanumeric(c)) sb.append(c);
        }
        String fileName = Strings.eliminateWhiteSpaces(sb.toString().replaceAll("[ ]+", "-")) + "_" + (dersBilgisi.kullaniciNumarasi + ".snc");
        SimpleTextWriter swf = SimpleTextWriter.keepOpenUTF8Writer(new File(fileName));
        swf.writeLine("Ad:" + dersBilgisi.kullaniciAdi);
        swf.writeLine("Numara:" + dersBilgisi.kullaniciNumarasi);
        swf.writeLine("Klavye:" + dersBilgisi.klavye.ad);
        if (dersBilgisi.surelimi()) swf.writeLine("Verilen Sure (sn):" + dersBilgisi.verilenSureSaniye); else swf.writeLine("Verilen Sure (sn): yok");
        swf.writeLine("Sure (sn):" + String.format("%.1f", (float) zamanSaniye.get()));
        swf.writeLine("Beklenen Yazi:" + dersBilgisi.icerik);
        swf.writeLine("Yazilan Yazi :" + yazilan);
        swf.writeLine("Toplam harf sayisi:" + dersBilgisi.icerik.length());
        swf.writeLine("Kelime sayisi:" + dersBilgisi.kelimeSayisi());
        swf.writeLine("Yazilan harf sayisi:" + yazilan.length());
        swf.writeLine("Yazilan kelime sayisi:" + yazilanKelimeSayisi());
        swf.writeLine("Gorunen Hata Sayisi:" + gorunenHataSayisi);
        swf.writeLine("Toplam Hata Sayisi:" + toplamHataSayisi);
        swf.writeLine("Hiz (dakika/kelime):" + kelimeHizHesapla());
        swf.writeLine("Hiz (dakika/harf):" + harfHizHasapla());
        swf.close();
    }

    private String kelimeHizHesapla() {
        float f = (60f * yazilanKelimeSayisi() / (float) zamanSaniye.get());
        return String.format("%.1f", f);
    }
}
