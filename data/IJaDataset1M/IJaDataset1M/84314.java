package vydavky.client.interfaces;

import java.util.Map;
import vydavky.client.objects.SkupinaValue;
import vydavky.client.objects.clientserver.NavratValue;

/**
 * Aplikacne rozhranie definujuce minimalnu funkcionalitu pre pracu so skupinami.
 */
public interface ApplicationInterfaceSkupiny {

    /**
   * Vrati zoznam nestornovanych skupin.
   *
   * @return Zoznam nestornovanych skupin.
   */
    Map<Long, SkupinaValue> getSkupiny();

    /**
   * Ulozi upravenu existujucu skupinu. Kontroluje duplicitu, aby po ulozeni
   * neexistovali dva duplicitne zaznamy.
   *
   * @param skupina Skupina, ktora ma byt ulozena. Musi uz existovat v DB.
   * @return Informacia o uspechu/neuspechu.
   */
    NavratValue ulozSkupinu(final SkupinaValue skupina);

    /**
   * Ulozi novu skupinu. Kontroluje duplicitu, aby po ulozeni
   * neexistovali dva duplicitne zaznamy.
   *
   * @param skupina Nova skupina, ktora ma byt ulozena.
   * @return Informacia o uspechu/neuspechu. Ak sa podari skupinu uspesne
   *         ulozit, v navratovom objekte sa vrati ID, pod ktorym bola ulozena.
   */
    NavratValue ulozSkupinuNew(final SkupinaValue skupina);

    /**
   * Stornuje existujucu skupinu. Kontroluje, ci skupina nie je nikde pouzita,
   * v takom pripade nesmie byt stornovana.
   *
   * @param skupina Skupina, ktora ma byt stornovana. Nesmie byt nikde pouzita
   *                (musi byt prazdna, nesmie byt v ziadnom projekte, ...),
   *                inak nesmie byt stornovana.
   * @return Informacia o uspechu/neuspechu.
   */
    NavratValue stornoSkupiny(final SkupinaValue skupina);
}
