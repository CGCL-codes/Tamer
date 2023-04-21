package net.sf.nazgaroth.game.attr;

/**
 * Zawiera glowne atrybuty postaci.
 * 
 * Atrybuty postaci mają wartość zawartą w przedziale 1-200. 200 jest maksymalną
 * trenowalną wartością atrybutu. Nie jest jednak wartością ostateczną. Może ta
 * granica być przekroczona za pomocą specjalnych mikstur lub magicznego
 * uzbrojenia.
 * 
 * @author pawel
 * 
 */
public class Stats {

    /**
	 * Siła to podstawowy atrybut dla wojownika. Determinuje ona jego tężyznę
	 * fizyczną, zdolność do dźwigania ciężkich rzeczy, jak też i wpływa na
	 * ilość zadawanych obrażeń w walce bronią białą oraz bez broni. -
	 * modyfikuje udźwig; 1ptSI/10K (max. 2000K) − modyfikuje obrażenia zadawane
	 * w walce wręcz; 1ptSI/+0,2OBR (max. +40OBR)
	 */
    private float strength;

    /**
	 * Zręczność (ZR) Zręczność to atrybut ważny zarówno dla wojownika, jak i
	 * każdego kto para się walką na dystans. Wpływa głównie na obrażenia
	 * zadawane z dystansu, lecz i wojownik walczący wręcz powinien od czasu do
	 * czasu poświęcić nieco uwagi na jego trening, gdyż atrybut ten
	 * odpowiedzialny jest za wartość sprawnego unikania ciosów przeciwnika. -
	 * modyfikuje obrażenia zadane z dystansu; 1ptZR/+0,2OBR (max. + 40OBR) -
	 * modyfikuje wartość unikania ciosów; 1ptZR/0,25%UN (max. 50% szans na
	 * unik)
	 */
    private float dexterity;

    /**
	 * Wytrzymałość (WT) Wytrzymałość to atrybut ważny dla każdej klasy. Jego
	 * podstawowa funkcja to ilość PŻ postaci. Im wyższa wartość WT, tym więcej
	 * posiada ona PŻ. Atrybut ten odpowiedzialny jest także za parowanie
	 * ciosów, zatem każdy powinien go należycie trenować. - modyfikuje punkty
	 * życia; 1ptWT/2ptPŻ (max. 400_PŻ) - modyfikuje wartość parowania ciosów;
	 * 1ptWT/+0,1PA (max. +20PA) - wpływa na wartość odczuwanego głodu; Zasada
	 * jest prosta – przy przedziale WT 1-49 jest to 1GŁ/50 tur. Przy przedziale
	 * WT 50-99 1GŁ/100 tur. Przy przedziale WT 100-149 1GŁ/150 tur, zaś
	 * przedział WT 150-200 to 1GŁ/200 tur. - wpływa na wartość odczuwanego
	 * pragnienia. Zasada odczuwania pragnienia jest taka sama jak w przypadku
	 * GŁ.
	 */
    private float endurance;

    /**
	 * Inteligencja (INT) Inteligencja to atrybut przeznaczony właściwie tylko
	 * dla klas parających się magią, lecz i wojownicy nie powinni o nim
	 * zapominać, gdyż ich zdolności zasilane są tą samą energią co i
	 * niszczycielskie czary innych klas. Atrybut ten odpowiada głównie za
	 * poziom energii bohatera, lecz modyfikuje też wartość obrażeń zadanych
	 * czarami. Wpływa też na naturalną zdolność regeneracji energii. Każdy
	 * szanujący się mag powinien posiadać wysoką inteligencję. - modyfikuje
	 * energię; 1ptINT/3ptEN (max. 600PtEN) - modyfikuje obrażenia zadane
	 * czarami; 1ptINT/+0,2OBR (max. +40OBR) - modyfikuje zdolność regeneracji
	 * energii; 1ptINT/+0,1 zregenerowanej energii na turę (max. +20
	 * zregenerowanej energii na turę)
	 */
    private float inteligence;

    /**
	 * Charyzma to atrybut odpowiadający głównie za reakcję Bohaterów
	 * Niezależnych na gracza. Poza tym modyfikuje on stosunek kupców do
	 * bohatera. modyfikuje reakcję BN na gracza −ceny u kupców; 1ptCH/-1%ceny,
	 * aż do 100%, potem; 2ptCH/-1%ceny. Początkowo zawsze jest to 200% ceny
	 * danego przedmiotu. Spada ta wartość w tempie -1% za punkt CH, aż do
	 * poziomu 100% ceny. Potem wartość ta spada w tempie -1% za punkt CH, zatem
	 * w efekcie na poziomie CH równym 200, ceny u kupców mają wartość 50%
	 */
    private float charisma;

    /**
	 * Szczęście to ciekawy atrybut. Wpływa on na wiele zdolności bohatera,
	 * choćby jego zdolność do zadawania krytycznych i zabójczych obrażeń, jak
	 * też i zdolności w szukaniu skarbów... - modyfikuje wartość uderzenia
	 * krytycznego; 1ptSZ/0,09%UK (max. 18%UK) - modyfikuje wartość tzw.
	 * „automordu”; 1ptSZ/0,005%AM (max. 1%AM) - modyfikuje wartość znalezienia
	 * skarbu podczas łowienia ryb; 1SZ/0,05% (max. 10%) − modyfikuje wartość
	 * znalezienia skarbu podczas kopania; 1SZ/0,1% (max. 20%)
	 */
    private float luck;

    public final float getCharisma() {
        return charisma;
    }

    protected final void setCharisma(float charisma) {
        this.charisma = charisma;
    }

    public final float getDexterity() {
        return dexterity;
    }

    protected final void setDexterity(float dexterity) {
        this.dexterity = dexterity;
    }

    public final float getEndurance() {
        return endurance;
    }

    protected final void setEndurance(float endurance) {
        this.endurance = endurance;
    }

    public final float getInteligence() {
        return inteligence;
    }

    protected final void setInteligence(float inteligence) {
        this.inteligence = inteligence;
    }

    public final float getLuck() {
        return luck;
    }

    protected final void setLuck(float luck) {
        this.luck = luck;
    }

    public final float getStrength() {
        return strength;
    }

    protected final void setStrength(float strength) {
        this.strength = strength;
    }
}
