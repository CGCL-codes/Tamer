package vydavky.service.ejb.zamky;

import javax.ejb.Local;
import vydavky.client.ciselniky.SystemovyZamok;
import vydavky.client.objects.clientserver.NavratValue;

/**
 * Local interface beany pre pracu so systemovymi zamkami.
 */
@Local
public interface SystemoveZamkyLocal {

    /**
   * Pokusi sa zamknut zadany systemovy zamok.
   *
   * <p>Princip funkcie je rovnaky ako u
   * {@link vydavky.service.ejb.zamky.ZamkyBean#zamkniObjekt zamkniObjekt}.</p>
   *
   * @param userId ID pouzivatela, ktory sa pokusa ziskat systemovy zamok.
   * @param zamok Zamykany systemovy zamok.
   * @return Navratova hodnota informujuca o uspechu/neuspechu.
   */
    NavratValue zamkni(final long userId, final SystemovyZamok zamok);

    /**
   * Pokusi sa odomknut zadany systemovy zamok.
   *
   * <p>Princip funkcie je podobny ako u
   * {@link vydavky.service.ejb.zamky.ZamkyBean#odomkniObjekt odomkniObjekt}.</p>
   *
   * @param userId ID pouzivatela, ktory sa pokusa odomknut systemovy zamok.
   * @param zamok Odomykany systemovy zamok.
   * @return Navratova hodnota informujuca o uspechu/neuspechu.
   */
    NavratValue odomkni(final long userId, final SystemovyZamok zamok);
}
