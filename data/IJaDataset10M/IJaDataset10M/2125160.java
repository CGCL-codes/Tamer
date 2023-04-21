package net.zemberek.yapi.obek;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import net.zemberek.bilgi.KaynakYukleyici;
import net.zemberek.erisim.Zemberek;
import net.zemberek.islemler.KokBulucu;
import net.zemberek.tr.yapi.TurkiyeTurkcesi;
import net.zemberek.yapi.Kok;

public class BasitKelimeObegiBulucu {

    private KelimeObekDeposu depo;

    private KokBulucu kokBulucu;

    public BasitKelimeObegiBulucu(KelimeObekDeposu depo, KokBulucu kokBulucu) {
        this.depo = depo;
        this.kokBulucu = kokBulucu;
    }

    /**
     * bir dizi kok icerisindeki olasi kok obek dizilimlerini bir liste icerisinde dondurur.
     * Ornegin "eli kalem tutan yardim etsin dort goz gozu gormeyince"  cumlesindeki koklerin
     * dizi halinde gonderildigini ve tum kelime obeklerinin dpeoda bulundugu var sayilirsa asagidaki
     * kok obekleri doner:
     * " el, kalem, tutmak"
     * " yardim etmek "
     * " dort, goz "
     * " goz, goz, gormek "
     * dikkat edilirse sistem tum olasi kok obeklerini buluyor, "dort goz" ya da "goz gozu gormemek" icin
     * TODO: bu islem henuz efektif degil.
     *
     * @param kokler
     * @return
     */
    public List<KelimeObegi> adayBul(Kok... kokler) {
        List<KelimeObegi> adaylar = new ArrayList<KelimeObegi>();
        for (int i = 0; i < kokler.length; i++) {
            if (kokler.length > 1 && i <= kokler.length - 2) for (int j = i + 1; j < kokler.length; j++) {
                Kok[] testDizi = new Kok[j - i + 1];
                System.arraycopy(kokler, i, testDizi, 0, j - i + 1);
                KelimeObegi aday = obekOlustur(testDizi);
                if (depo.var(aday)) adaylar.add(aday);
            }
        }
        return adaylar;
    }

    private KelimeObegi obekOlustur(Kok... kokler) {
        KelimeObekBileseni bilesenler[] = new KelimeObekBileseni[kokler.length];
        for (int i = 0; i < kokler.length; i++) bilesenler[i] = new KelimeObekBileseni(kokler[i], EkKalipTipi.YALIN);
        return new KelimeObegi(bilesenler);
    }

    /**
     * cumle kelimelerini cozumleyip kok adaylarindan ilklerini alarak buna dayanan
     * kok dizisi ile kelime obklerini bulmaya calisir.
     * Aslinda tum olasi kok kombinasyonlari ile denemesi gerekir.
     *
     * @param cumle
     * @return
     */
    public List<KelimeObegi> adayBul(String cumle) {
        List<Kok> kokler = new ArrayList<Kok>();
        String[] strs = cumle.replaceAll(" \\t+", "").split("[\\| ]");
        for (String str : strs) {
            Kok[] adayKokler = kokBulucu.kokBul(str);
            if (adayKokler.length > 1) {
                Arrays.sort(adayKokler, new Comparator<Kok>() {

                    public int compare(Kok o1, Kok o2) {
                        return o2.icerik().length() - o1.icerik().length();
                    }
                });
            }
            if (adayKokler.length > 0) kokler.add(adayKokler[0]);
        }
        return adayBul(kokler.toArray(new Kok[kokler.size()]));
    }

    public static void main(String[] args) throws IOException {
        Zemberek zemberek = new Zemberek(new TurkiyeTurkcesi());
        DuzyaziKelimeObegiOkuyucu o = new DuzyaziKelimeObegiOkuyucu(zemberek.kokBulucu());
        List<KelimeObegi> obekler = o.tumunuOku("deney/obekler.txt");
        MapTabanliKelimeObekDeposu kod = new MapTabanliKelimeObekDeposu();
        for (KelimeObegi obek : obekler) {
            kod.ekle(obek);
        }
        BasitKelimeObegiBulucu bkob = new BasitKelimeObegiBulucu(kod, zemberek.kokBulucu());
        BufferedReader reader = new KaynakYukleyici("utf-8").getReader("deney/test-cumleleri.txt");
        while (reader.ready()) {
            String[] cumller = reader.readLine().replaceAll("[-,]", "").split("[.]");
            for (String cumle : cumller) {
                if (cumle.length() < 5) continue;
                List<KelimeObegi> adaylar = bkob.adayBul(cumle);
                System.out.println("cumle = " + cumle);
                System.out.println("adaylar = " + adaylar);
            }
        }
    }
}
